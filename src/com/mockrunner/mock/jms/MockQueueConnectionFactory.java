package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

/**
 * Mock implementation of JMS <code>QueueConnectionFactory</code>.
 */
public class MockQueueConnectionFactory implements QueueConnectionFactory
{
    private QueueConnection queueConnection;
    
    public void setQueueConnection(QueueConnection queueConnection)
    {
        this.queueConnection = queueConnection;
    }
    
    public QueueConnection createQueueConnection() throws JMSException
    {
        return queueConnection;
    }

    public QueueConnection createQueueConnection(String name, String password) throws JMSException
    {
        return queueConnection;
    }
}
