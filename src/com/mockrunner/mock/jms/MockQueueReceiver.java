package com.mockrunner.mock.jms;

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

    public MockQueueReceiver(MockQueueConnection connection, MockQueue queue, String messageSelector)
    {
        this.connection = connection;
        this.queue = queue;
        this.messageSelector = messageSelector;
        closed = false;
    }
    
    public boolean isClosed()
    {
        return closed;
    }
    
    public void receiveMessage(Message message)
    {

    }
    
    public List getReceivedMessageList()
    {
        return null;
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
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public void setMessageListener(MessageListener arg0) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public Message receive() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public Message receive(long arg0) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public Message receiveNoWait() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        closed = true;
    }
}
