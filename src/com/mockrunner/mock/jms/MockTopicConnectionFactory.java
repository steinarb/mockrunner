package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

import com.mockrunner.jms.DestinationManager;

/**
 * Mock implementation of JMS <code>TopicConnectionFactory</code>.
 */
public class MockTopicConnectionFactory implements TopicConnectionFactory
{
    private DestinationManager destinationManager;
    private List topicConnections;
    private JMSException exception;
    
    public MockTopicConnectionFactory(DestinationManager destinationManager)
    {
        this.destinationManager = destinationManager;
        topicConnections = new ArrayList();
        exception = null;
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
        MockTopicConnection connection = new MockTopicConnection(destinationManager);
        connection.setJMSException(exception);
        topicConnections.add(connection);
        return connection;
    }

    public TopicConnection createTopicConnection(String name, String password) throws JMSException
    {
        return createTopicConnection();
    }
    
    /**
     * Set an exception that will be passed to all
     * created connections. This can be used to
     * simulate server errors. Check out
     * {@link MockConnection#setJMSException}
     * for details.
     * @param exception the exception
     */
    public void setJMSException(JMSException exception)
    {
        this.exception = exception;
    }
    
    /**
     * Clears the list of connections
     */
    public void clearConnections()
    {
        topicConnections.clear();
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
        if(topicConnections.size() <= index) return null;
        return (MockTopicConnection)topicConnections.get(index);
    }

    /**
     * Returns the latest created connection
     * resp. <code>null</code> if no such connection
     * exists.
     * @return the connection
     */
    public MockTopicConnection getLatestTopicConnection()
    {
        if(topicConnections.size() == 0) return null;
        return (MockTopicConnection)topicConnections.get(topicConnections.size() - 1);
    }
}
