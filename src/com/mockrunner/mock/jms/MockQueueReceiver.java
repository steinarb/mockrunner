package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueReceiver;

/**
 * Mock implementation of JMS <code>QueueReceiver</code>.
 */
public class MockQueueReceiver extends MockMessageConsumer implements QueueReceiver
{
    private MockQueue queue;
    
    public MockQueueReceiver(MockConnection connection, MockQueue queue)
    {
        this(connection, queue, null);
    }

    public MockQueueReceiver(MockConnection connection, MockQueue queue, String messageSelector)
    {
        super(connection, messageSelector);
        this.queue = queue;
    }

    public Queue getQueue() throws JMSException
    {
        getConnection().throwJMSException();
        return queue;
    }
    
    public Message receive() throws JMSException
    {
        getConnection().throwJMSException();
        if(isClosed())
        {
            throw new JMSException("Receiver is closed");
        }
        if(queue.isEmpty()) return null;
        return queue.getMessage();
    }
}
