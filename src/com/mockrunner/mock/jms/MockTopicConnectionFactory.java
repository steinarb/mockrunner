package com.mockrunner.mock.jms;

import javax.jms.Connection;
import javax.jms.JMSException;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;

/**
 * Mock implementation of JMS <code>TopicConnectionFactory</code>.
 */
public class MockTopicConnectionFactory extends MockConnectionFactory
{
    public MockTopicConnectionFactory(DestinationManager destinationManager, ConfigurationManager configurationManager)
    {
        super(destinationManager, configurationManager);
    }
    
    public Connection createConnection() throws JMSException
    {
        return createTopicConnection();
    }

    public Connection createConnection(String name, String password) throws JMSException
    {
        return createTopicConnection();
    }

    /**
     * Returns the connection with the specified index
     * or <code>null</code> if no such connection
     * exists.
     * @param index the index
     * @return the connection
     */
    public MockTopicConnection getTopicConnection(int index)
    {
        if(connections().size() <= index) return null;
        return (MockTopicConnection)connections().get(index);
    }

    /**
     * Returns the latest created connection
     * or <code>null</code> if no such connection
     * exists.
     * @return the connection
     */
    public MockTopicConnection getLatestTopicConnection()
    {
        if(connections().size() == 0) return null;
        return (MockTopicConnection)connections().get(connections().size() - 1);
    }
}
