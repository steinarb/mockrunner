package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueReceiver;

public class MockQueueReceiver implements QueueReceiver
{
    private MockQueueConnection connection;
    private MockQueue queue;

    public MockQueueReceiver(MockQueueConnection connection, MockQueue queue)
    {
        this.connection = connection;
        this.queue = queue;
    }

    public Queue getQueue() throws JMSException
    {
        connection.throwJMSException();
        return queue;
    }

    public String getMessageSelector() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
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
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }
}
