package com.mockrunner.mock.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

import com.mockrunner.jms.DestinationManager;

/**
 * Mock implementation of JMS <code>TopicConnectionFactory</code>.
 */
public class MockTopicConnectionFactory extends MockConnectionFactory implements TopicConnectionFactory
{
    public MockTopicConnectionFactory(DestinationManager destinationManager)
    {
        super(destinationManager);
    }
    
    public Connection createConnection() throws JMSException
    {
        return createTopicConnection();
    }

    public Connection createConnection(String name, String password) throws JMSException
    {
        return createTopicConnection();
    }
    
    public TopicConnection createTopicConnection() throws JMSException
    {
        MockTopicConnection connection = new MockTopicConnection(destinationManager());
        connection.setJMSException(exception());
        connections().add(connection);
        return connection;
    }

    public TopicConnection createTopicConnection(String name, String password) throws JMSException
    {
        return createTopicConnection();
    }

    /**
     * Returns the connection with the specified index
     * resp. <code>null</code> if no such connection
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
     * resp. <code>null</code> if no such connection
     * exists.
     * @return the connection
     */
    public MockTopicConnection getLatestTopicConnection()
    {
        if(connections().size() == 0) return null;
        return (MockTopicConnection)connections().get(connections().size() - 1);
    }
}
