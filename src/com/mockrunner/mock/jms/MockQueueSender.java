package com.mockrunner.mock.jms;

import javax.jms.DeliveryMode;
import javax.jms.InvalidDestinationException;
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
    
    /**
     * Returns if this sender was closed.
     * @return <code>true</code> if this sender is closed
     */
    public boolean isClosed()
    {
        return closed;
    }
    
    public Queue getQueue() throws JMSException
    {
        connection.throwJMSException();
        return queue;
    }

    public void send(Message message) throws JMSException
    {
        connection.throwJMSException();
        if(null == queue)
        {
            throw new InvalidDestinationException("Queue must not be null");
        }
        setJMSProperties(message);
        queue.addMessage(message);
    }

    public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
    {
        connection.throwJMSException();
        if(null == queue)
        {
            throw new InvalidDestinationException("Queue must not be null");
        }
        setJMSProperties(message, deliveryMode, priority, timeToLive);
        queue.addMessage(message);
    }

    public void send(Queue queue, Message message) throws JMSException
    {
        connection.throwJMSException();
        if(null == queue)
        {
            throw new InvalidDestinationException("Queue must not be null");
        }
        setJMSProperties(message);
        if(queue instanceof MockQueue)
        {
            ((MockQueue)queue).addMessage(message);
        }
    }

    public void send(Queue queue, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
    {
        connection.throwJMSException();
        if(null == queue)
        {
            throw new InvalidDestinationException("Queue must not be null");
        }
        setJMSProperties(message, deliveryMode, priority, timeToLive);
        if(queue instanceof MockQueue)
        {
            ((MockQueue)queue).addMessage(message);
        }
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

    private void setJMSProperties(Message message) throws JMSException
    {
        setJMSProperties(message, deliveryMode, priority, timeToLive);
    }
    
    private void setJMSProperties(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
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
            message.setJMSMessageID(String.valueOf(Math.random()));
        }
    }
}
