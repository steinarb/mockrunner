package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueSession;

/**
 * Mock implementation of JMS <code>Queue</code>.
 */
public class MockQueue implements Queue
{
    private MockConnection connection;
    private Set sessions;
    private String name;
    private List currentMessages;
    private List receivedMessages;
    
    public MockQueue(MockConnection connection, String name)
    {
        this.name = name;
        this.connection = connection;
        sessions = new HashSet();
        currentMessages = new ArrayList();
        receivedMessages = new ArrayList();
    }
    
    public String getQueueName() throws JMSException
    {
        connection.throwJMSException();
        return name;
    }
    
    /**
     * Adds a message to this <code>Queue</code> that will
     * be propagated to the corresponding receiver. Only one
     * receiver will get the message. The order is not
     * predictable.
     * @param message the message
     */
    public void addMessage(Message message) throws JMSException
    {
        receivedMessages.add(message);
        Iterator sessionsIterator = sessions.iterator();
        boolean isConsumed = false;
        while(sessionsIterator.hasNext() && !isConsumed)
        {
            MockQueueSession session = (MockQueueSession)sessionsIterator.next();
            MessageListener globalListener = session.getMessageListener();
            if(null != globalListener)
            {
                globalListener.onMessage(message);
                isConsumed = true;
            }
            else
            {
                MockQueueReceiver receiver = session.getTransmissionManager().getQueueReceiver(name);
                if(receiver != null && receiver.canConsume())
                {
                    receiver.receiveMessage(message);
                    isConsumed = true;
                }
            }
        }
        if(!isConsumed)
        {
            currentMessages.add(message);
        }
    }
    
    /**
     * Returns if this queue contains messages.
     * @return <code>false</code> if there's at least one message in the queue,
     *         <code>true</code> otherwise
     */
    public boolean isEmpty()
    {
        return currentMessages.size() <= 0;
    }
    
    /**
     * Returns the next message, that is in the queue. The message
     * will be deleted from the queue. If there's no message in the
     * queue, <code>null</code> will be returned.
     * @return the <code>Message</code>
     */
    public Message getMessage()
    {
        if(currentMessages.size() <= 0) return null;
        return (Message)currentMessages.remove(0);
    }
    
    /**
     * Returns a <code>List</code> of all messages, that are currently
     * in the queue. No messages will be deleted from the queue.
     * @return the <code>List</code> of messages
     */
    public List getCurrentMessageList()
    {
        return Collections.unmodifiableList(currentMessages);
    }
    
    /**
     * Returns a <code>List</code> of all received messages.
     * @return the <code>List</code> of messages
     */
    public List getReceivedMessageList()
    {
        return Collections.unmodifiableList(receivedMessages);
    }
    
    /**
     * Adds a <code>QueueSession</code>.
     * @param session the session
     */
    public void addQueueSession(QueueSession session)
    {
        sessions.add(session);
    }
    
    protected MockConnection getConnection()
    {
        return connection;
    }
}
