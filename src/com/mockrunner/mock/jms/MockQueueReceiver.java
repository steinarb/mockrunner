package com.mockrunner.mock.jms;

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

    public MockQueueReceiver(MockQueueConnection connection, MockQueue queue, String messageSelector)
    {
        this.connection = connection;
        this.queue = queue;
        this.messageSelector = messageSelector;
        closed = false;
        messageListener = null;
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
     * message listener. If there's no message listener,
     * nothing happens.
     * @param message the message
     */
    public void receiveMessage(Message message)
    {
        if(null == messageListener) return;
        messageListener.onMessage(message);
    }
    
    /**
     * Returns if this receiver can consume an incoming message,
     * i.e. if a <code>MessageListener</code> is registered.
     * @return <code>true</code> if this receiver can consume the message
     */
    public boolean canConsume()
    {
        return messageListener != null;
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
        if(queue.isEmpty()) return null;
        return queue.getMessage();
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
