package com.mockrunner.mock.jms;

import javax.jms.BytesMessage;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.StreamMessage;

/**
 * Mock implementation of JMS <code>MessageProducer</code>.
 */
public abstract class MockMessageProducer implements MessageProducer
{
    private MockConnection connection;
    private boolean closed;
    private boolean disableMessageId;
    private boolean disableTimestamp;
    private int deliveryMode;
    private int priority;
    private long timeToLive;
    
    public MockMessageProducer(MockConnection connection)
    {
        this.connection = connection;
        closed = false;
        disableMessageId = false;
        disableTimestamp = false;
        deliveryMode = DeliveryMode.NON_PERSISTENT;
        priority = 4;
        timeToLive = 0;
    }
    
    /**
     * Returns if this producer was closed.
     * @return <code>true</code> if this sender is closed
     */
    public boolean isClosed()
    {
        return closed;
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        closed = true;
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

    protected void setJMSProperties(Message message) throws JMSException
    {
        setJMSProperties(message, deliveryMode, priority, timeToLive);
    }

    protected void setJMSProperties(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
    {
        message.setJMSDeliveryMode(deliveryMode);
        message.setJMSPriority(priority);
        long currentTime = System.currentTimeMillis();
        if(!disableTimestamp)
        {
            message.setJMSTimestamp(currentTime);
        }
        if(0 == timeToLive)
        {
            message.setJMSExpiration(0);
        }
        else
        {
            message.setJMSExpiration(currentTime + timeToLive);
        }
        if(!disableMessageId)
        {
            message.setJMSMessageID("ID:" + String.valueOf(Math.random()));
        }
        if(message instanceof BytesMessage)
        {
            ((BytesMessage)message).reset();
        }
        if(message instanceof StreamMessage)
        {
            ((StreamMessage)message).reset();
        }
    }
    
    protected MockConnection getConnection()
    {
        return connection;
    }
}
