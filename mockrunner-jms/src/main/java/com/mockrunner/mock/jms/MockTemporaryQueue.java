package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.TemporaryQueue;

/**
 * Mock implementation of JMS <code>TemporaryQueue</code>.
 */
public class MockTemporaryQueue extends MockQueue implements TemporaryQueue
{
    private boolean deleted;
    
    public MockTemporaryQueue()
    {
        super("TemporaryQueue");
        deleted = false;
    }
    
    /**
     * Returns if this temporary queue is deleted.
     * @return <code>true</code> if this queue is deleted 
     */
    public boolean isDeleted()
    {
        return deleted;
    }

    public void delete() throws JMSException
    {
        deleted = true;
    }
}
