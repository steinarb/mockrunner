package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicSession;

/**
 * Mock implementation of JMS <code>Topic</code>.
 */
public class MockTopic implements Topic
{
    private MockConnection connection;
    private Set sessions;
    private String name;
    private List currentMessages;
    private List receivedMessages;
    
    public MockTopic(MockConnection connection, String name)
    {
        this.connection = connection;
        this.name = name;
        sessions = new HashSet();
        currentMessages = new ArrayList();
        receivedMessages = new ArrayList();
    }
        
    public String getTopicName() throws JMSException
    {
        connection.throwJMSException();
        return name;
    }
    
    /**
     * Adds a message to this <code>Topic</code> that will
     * be propagated to the corresponding receivers.
     * @param message the message
     */
    public void addMessage(Message message) throws JMSException
    {
        receivedMessages.add(message);    
        boolean isConsumed = false;
        if(!connection.isStopped())
        {
            Iterator sessionsIterator = sessions.iterator();
            while(sessionsIterator.hasNext())
            {
                MockTopicSession session = (MockTopicSession)sessionsIterator.next();
                MessageListener globalListener = session.getMessageListener();
                if(null != globalListener)
                {
                    globalListener.onMessage(message);
                    isConsumed = true;
                }
                else
                {
                    List subscribers = session.getTransmissionManager().getTopicSubscriberList(name);
                    for(int ii = 0; ii < subscribers.size(); ii++)
                    {
                        MockTopicSubscriber subscriber = (MockTopicSubscriber)subscribers.get(ii);
                        if(subscriber.canConsume())
                        {
                            subscriber.receiveMessage(message);
                            isConsumed = true;
                        }
                    }
                    Map durableSubscribers = session.getTransmissionManager().getDurableTopicSubscriberMap(name);
                    Iterator keys = durableSubscribers.keySet().iterator();
                    while(keys.hasNext())
                    {
                        MockTopicSubscriber subscriber = (MockTopicSubscriber)durableSubscribers.get(keys.next());
                        if(subscriber.canConsume())
                        {
                            subscriber.receiveMessage(message);
                            isConsumed = true;
                        }
                    }
                }
            }
        }
        if(!isConsumed)
        {
            currentMessages.add(message);
        }
    }
    
    /**
     * Returns if this topic contains messages.
     * @return <code>false</code> if there's at least one message,
     *         <code>true</code> otherwise
     */
    public boolean isEmpty()
    {
        return currentMessages.size() <= 0;
    }

    /**
     * Clears all current messages.
     */
    public void clear()
    {
        currentMessages.clear();
    }

    /**
     * Clears all current messages and resets the list of received messages.
     */
    public void reset()
    {
        currentMessages.clear();
        receivedMessages.clear();
    }

    /**
     * Returns the next message, that is in the topic. The message
     * will be deleted from the topic. If there's no message 
     * <code>null</code> will be returned.
     * @return the <code>Message</code>
     */
    public Message getMessage()
    {
        if(currentMessages.size() <= 0) return null;
        return (Message)currentMessages.remove(0);
    }

    /**
     * Returns a <code>List</code> of all messages. 
     * No messages will be deleted.
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
    public void addTopicSession(TopicSession session)
    {
        sessions.add(session);
    }
    
    protected MockConnection getConnection()
    {
        return connection;
    }
}
