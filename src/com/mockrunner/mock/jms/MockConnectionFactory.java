package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;

/**
 * Mock implementation of JMS <code>ConnectionFactory</code>.
 * Can be used as generic factory for JMS 1.1.
 * Also implements <code>QueueConnectionFactory</code> and
 * <code>TopicConnectionFactory</code> and can be used to 
 * create queue and topic connections as well as generic 
 * JMS 1.1 connections. It is recommended to use
 * {@link com.mockrunner.mock.jms.MockQueueConnectionFactory}
 * if you only use queues and 
 * {@link com.mockrunner.mock.jms.MockTopicConnectionFactory}
 * if you only use topics.
 * This implementation is primary for generic JMS 1.1 connections
 * but can also be used, if a server provides one implementation
 * for both domains (which is not portable).
 */
public class MockConnectionFactory implements QueueConnectionFactory, TopicConnectionFactory
{
    private DestinationManager destinationManager;
    private ConfigurationManager configurationManager;
    private List connections;
    private JMSException exception;

    public MockConnectionFactory(DestinationManager destinationManager, ConfigurationManager configurationManager)
    {
        connections = new ArrayList();
        this.destinationManager = destinationManager;
        this.configurationManager = configurationManager;
        exception = null;
    }
    
    public Connection createConnection() throws JMSException
    {
        MockConnection connection = new MockConnection(destinationManager, configurationManager);
        connection.setJMSException(exception);
        connections.add(connection);
        return connection;
    }

    public Connection createConnection(String name, String password) throws JMSException
    {
        return createConnection();
    }
    
    public QueueConnection createQueueConnection() throws JMSException
    {
        MockQueueConnection connection = new MockQueueConnection(destinationManager(), configurationManager());
        connection.setJMSException(exception());
        connections().add(connection);
        return connection;
    }

    public QueueConnection createQueueConnection(String name, String password) throws JMSException
    {
        return createQueueConnection();
    }
    
    public TopicConnection createTopicConnection() throws JMSException
    {
        MockTopicConnection connection = new MockTopicConnection(destinationManager(), configurationManager());
        connection.setJMSException(exception());
        connections().add(connection);
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
        connections.clear();
    }

    /**
     * Returns the connection with the specified index
     * or <code>null</code> if no such connection
     * exists.
     * @param index the index
     * @return the connection
     */
    public MockConnection getConnection(int index)
    {
        if(connections.size() <= index) return null;
        return (MockConnection)connections.get(index);
    }

    /**
     * Returns the latest created connection
     * or <code>null</code> if no such connection
     * exists.
     * @return the connection
     */
    public MockConnection getLatestConnection()
    {
        if(connections.size() == 0) return null;
        return (MockConnection)connections.get(connections.size() - 1);
    }
    
    protected DestinationManager destinationManager()
    {
        return destinationManager;
    }
    
    protected ConfigurationManager configurationManager()
    {
        return configurationManager;
    }
    
    protected List connections()
    {
        return connections;
    }
    
    protected JMSException exception()
    {
        return exception;
    }
}
