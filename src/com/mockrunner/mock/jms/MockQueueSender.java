package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;

/**
 * Mock implementation of JMS <code>QueueSender</code>.
 */
public class MockQueueSender implements QueueSender
{
    private MockQueueConnection connection;
    private MockQueue queue;

    public MockQueueSender(MockQueueConnection connection, MockQueue queue)
    {
        this.connection = connection;
        this.queue = queue;
    }
    
    public Queue getQueue() throws JMSException
    {
        connection.throwJMSException();
        return queue;
    }

    public void send(Message arg0) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public void send(Message arg0, int arg1, int arg2, long arg3) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public void send(Queue arg0, Message arg1) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public void send(Queue arg0, Message arg1, int arg2, int arg3, long arg4) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public void setDisableMessageID(boolean arg0) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public boolean getDisableMessageID() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return false;
    }

    public void setDisableMessageTimestamp(boolean arg0) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public boolean getDisableMessageTimestamp() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return false;
    }

    public void setDeliveryMode(int arg0) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public int getDeliveryMode() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return 0;
    }

    public void setPriority(int arg0) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public int getPriority() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return 0;
    }

    public void setTimeToLive(long arg0) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public long getTimeToLive() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return 0;
    }

    public void close() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }
}
