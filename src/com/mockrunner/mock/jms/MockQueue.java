package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.Queue;

/**
 * Mock implementation of JMS <code>Queue</code>.
 */
public class MockQueue implements Queue
{
    private MockQueueConnection connection;
    private String name;
    
    public MockQueue(MockQueueConnection connection, String name)
    {
        this.name = name;
        this.connection = connection;
    }
    
    public String getQueueName() throws JMSException
    {
        connection.throwJMSException();
        return name;
    }
    
    protected MockQueueConnection getConnection()
    {
        return connection;
    }
}
