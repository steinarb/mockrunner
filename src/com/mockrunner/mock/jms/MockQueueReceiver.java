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
    private MockQueueSession session;
    private MockQueue queue;
    
    public MockQueueReceiver(MockQueueConnection connection, MockQueueSession session, MockQueue queue)
    {
        this(connection, session, queue, null); 
    }

    public MockQueueReceiver(MockQueueConnection connection, MockQueueSession session, MockQueue queue, String messageSelector)
    {
        super(connection, messageSelector);
        this.session = session;
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
        Message message = queue.getMessage();
        if(session.isAutoAcknowledge()) message.acknowledge();
        return message;
    }
}
