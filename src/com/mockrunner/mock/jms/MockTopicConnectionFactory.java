package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.List;

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
    
    public MockTopicConnectionFactory(DestinationManager destinationManager)
    {
        this.destinationManager = destinationManager;
        topicConnections = new ArrayList();
    }
    
    public TopicConnection createTopicConnection() throws JMSException
    {
        TopicConnection connection = new MockTopicConnection(destinationManager);
        topicConnections.add(connection);
        return connection;
    }

    public TopicConnection createTopicConnection(String name, String password) throws JMSException
    {
        return createTopicConnection();
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
