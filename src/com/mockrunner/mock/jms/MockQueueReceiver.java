package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueReceiver;

/**
 * Mock implementation of JMS <code>QueueReceiver</code>.
 */
public class MockQueueReceiver implements QueueReceiver
{
    private MockQueueConnection connection;
    private MockQueue queue;
    private String messageSelector;
    private boolean closed;
    private MessageListener messageListener;
    private List messages;
    private List receivedMessages;

    public MockQueueReceiver(MockQueueConnection connection, MockQueue queue, String messageSelector)
    {
        this.connection = connection;
        this.queue = queue;
        this.messageSelector = messageSelector;
        closed = false;
        messageListener = null;
        messages = new ArrayList();
        receivedMessages = new ArrayList();
    }
    
    /**
     * Returns if this receiver was closed.
     * @return <code>true</code> if this receiver is closed
     */
    public boolean isClosed()
    {
        return closed;
    }
    
    /**
     * Adds a message that is immediately propagated to the
     * message listener. If no message listener is set, the message
     * is stored an can be received using the <code>receive</code>
     * methods.
     * @param message the message
     */
    public void receiveMessage(Message message)
    {
        receivedMessages.add(message);
        if(null != messageListener)
        {
            messageListener.onMessage(message);
        }
        else
        {
            messages.add(message);
        }
    }
    
    /**
     * Returns a <code>List</code> of all messages received
     * with this receiver.
     * @return the <code>List</code> of messages
     */
    public List getReceivedMessageList()
    {
        return Collections.unmodifiableList(receivedMessages);
    }

    public Queue getQueue() throws JMSException
    {
        connection.throwJMSException();
        return queue;
    }

    public String getMessageSelector() throws JMSException
    {
        connection.throwJMSException();
        return messageSelector;
    }

    public MessageListener getMessageListener() throws JMSException
    {
        connection.throwJMSException();
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) throws JMSException
    {
        connection.throwJMSException();
        this.messageListener = messageListener;
    }

    public Message receive() throws JMSException
    {
        connection.throwJMSException();
        if(messages.size() <= 0) return null;
        return (Message)messages.remove(0);
    }

    public Message receive(long timeout) throws JMSException
    {
        connection.throwJMSException();
        return receive();
    }

    public Message receiveNoWait() throws JMSException
    {
        connection.throwJMSException();
        return receive();
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        closed = true;
    }
}
