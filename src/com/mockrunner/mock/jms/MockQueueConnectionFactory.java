package com.mockrunner.mock.jms;

import javax.jms.Connection;
import javax.jms.JMSException;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;

/**
 * Mock implementation of JMS <code>QueueConnectionFactory</code>.
 */
public class MockQueueConnectionFactory extends MockConnectionFactory
{
    public MockQueueConnectionFactory(DestinationManager destinationManager, ConfigurationManager configurationManager)
    {
        super(destinationManager, configurationManager);
    }
    
    public Connection createConnection() throws JMSException
    {
        return createQueueConnection();
    }

    public Connection createConnection(String name, String password) throws JMSException
    {
        return createQueueConnection();
    }
    
    /**
     * Returns the connection with the specified index
     * or <code>null</code> if no such connection
     * exists.
     * @param index the index
     * @return the connection
     */
    public MockQueueConnection getQueueConnection(int index)
    {
        if(connections().size() <= index) return null;
        return (MockQueueConnection)connections().get(index);
    }
    
    /**
     * Returns the latest created connection
     * or <code>null</code> if no such connection
     * exists.
     * @return the connection
     */
    public MockQueueConnection getLatestQueueConnection()
    {
        if(connections().size() == 0) return null;
        return (MockQueueConnection)connections().get(connections().size() - 1);
    }
}
