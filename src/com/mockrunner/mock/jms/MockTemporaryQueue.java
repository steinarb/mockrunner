package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.TemporaryQueue;

/**
 * Mock implementation of JMS <code>TemporaryQueue</code>.
 */
public class MockTemporaryQueue extends MockQueue implements TemporaryQueue
{
    private boolean deleted;
    
    public MockTemporaryQueue(MockQueueConnection connection)
    {
        super(connection, "TemporaryQueue");
        deleted = false;
    }

    public void delete() throws JMSException
    {
        getConnection().throwJMSException();
        deleted = true;
    }

    public boolean isDeleted()
    {
        return deleted;
    }
}
