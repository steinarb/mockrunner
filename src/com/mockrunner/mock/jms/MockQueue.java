package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.Queue;

/**
 * Mock implementation of JMS <code>Queue</code>.
 */
public class MockQueue implements Queue
{
    private String name;
    
    public MockQueue(String name)
    {
        this.name = name;
    }
    
    public String getQueueName() throws JMSException
    {
        return name;
    }
}
