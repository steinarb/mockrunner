package com.mockrunner.mock.jms;

import javax.jms.DeliveryMode;
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
    private boolean disableMessageId;
    private boolean disableTimestamp;
    private int deliveryMode;
    private int priority;
    private long timeToLive;
    private boolean closed;

    public MockQueueSender(MockQueueConnection connection, MockQueue queue)
    {
        this.connection = connection;
        this.queue = queue;
        disableMessageId = false;
        disableTimestamp = false;
        deliveryMode = DeliveryMode.NON_PERSISTENT;
        priority = 4;
        timeToLive = 0;
        closed = false;
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

    public void setDisableMessageID(boolean disableMessageId) throws JMSException
    {
        connection.throwJMSException();
        this.disableMessageId = disableMessageId;
    }

    public boolean getDisableMessageID() throws JMSException
    {
        connection.throwJMSException();
        return disableMessageId;
    }

    public void setDisableMessageTimestamp(boolean disableTimestamp) throws JMSException
    {
        connection.throwJMSException();
        this.disableTimestamp = disableTimestamp;
    }

    public boolean getDisableMessageTimestamp() throws JMSException
    {
        connection.throwJMSException();
        return disableTimestamp;
    }

    public void setDeliveryMode(int deliveryMode) throws JMSException
    {
        connection.throwJMSException();
        this.deliveryMode = deliveryMode;
    }

    public int getDeliveryMode() throws JMSException
    {
        connection.throwJMSException();
        return deliveryMode;
    }

    public void setPriority(int priority) throws JMSException
    {
        connection.throwJMSException();
        this.priority = priority;
    }

    public int getPriority() throws JMSException
    {
        connection.throwJMSException();
        return priority;
    }

    public void setTimeToLive(long timeToLive) throws JMSException
    {
        connection.throwJMSException();
        this.timeToLive = timeToLive;
    }

    public long getTimeToLive() throws JMSException
    {
        connection.throwJMSException();
        return timeToLive;
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        closed = true;
    }
    
    public boolean isClosed()
    {
        return closed;
    }
}
