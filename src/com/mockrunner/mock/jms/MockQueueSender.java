package com.mockrunner.mock.jms;

import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;

/**
 * Mock implementation of JMS <code>QueueSender</code>.
 */
public class MockQueueSender extends MockMessageProducer implements QueueSender
{
    private MockQueue queue;

    public MockQueueSender(MockConnection connection, MockQueue queue)
    {
        super(connection);
        this.queue = queue; 
    }
     
    public Queue getQueue() throws JMSException
    {
        getConnection().throwJMSException();
        return queue;
    }

    public void send(Message message) throws JMSException
    {
        getConnection().throwJMSException();
        if(null == queue)
        {
            throw new InvalidDestinationException("Queue must not be null");
        }
        setJMSProperties(message);
        queue.addMessage(message);
    }

    public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
    {
        getConnection().throwJMSException();
        if(null == queue)
        {
            throw new InvalidDestinationException("Queue must not be null");
        }
        setJMSProperties(message, deliveryMode, priority, timeToLive);
        queue.addMessage(message);
    }

    public void send(Queue queue, Message message) throws JMSException
    {
        getConnection().throwJMSException();
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
        getConnection().throwJMSException();
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
}
