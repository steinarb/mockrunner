package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;

/**
 * Mock implementation of JMS <code>ConnectionFactory</code>.
 * Can be used if the domain type (queue or topic) does
 * not matter (only with JMS 1.1)
 */
public class MockConnectionFactory implements ConnectionFactory
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
     * resp. <code>null</code> if no such connection
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
     * resp. <code>null</code> if no such connection
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
