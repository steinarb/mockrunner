package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;

/**
 * Mock implementation of JMS <code>QueueSender</code>.
 */
public class MockQueueSender extends MockMessageProducer implements QueueSender
{
    public MockQueueSender(MockConnection connection, MockSession session, MockQueue queue)
    {
        super(connection, session, queue);
    }

    public Queue getQueue() throws JMSException
    {
        return (Queue)getDestination();
    }

    public void send(Message message) throws JMSException
    {
        super.send(message);
    }

    public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
    {
        super.send(message, deliveryMode, priority, timeToLive);
    }

    public void send(Queue queue, Message message) throws JMSException
    {
        super.send(queue, message);
    }

    public void send(Queue queue, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
    {
        super.send(queue, message, deliveryMode, priority, timeToLive);
    }
}
