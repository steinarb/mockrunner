package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

import com.mockrunner.jms.DestinationManager;

/**
 * Mock implementation of JMS <code>QueueConnectionFactory</code>.
 */
public class MockQueueConnectionFactory implements QueueConnectionFactory
{
    private DestinationManager destinationManager;
    private List queueConnections;
    private JMSException exception;
    
    public MockQueueConnectionFactory(DestinationManager destinationManager)
    {
        queueConnections = new ArrayList();
        this.destinationManager = destinationManager;
        exception = null;
    }
    
    public QueueConnection createQueueConnection() throws JMSException
    {
        MockQueueConnection connection = new MockQueueConnection(destinationManager);
        connection.setJMSException(exception);
        queueConnections.add(connection);
        return connection;
    }

    public QueueConnection createQueueConnection(String name, String password) throws JMSException
    {
        return createQueueConnection();
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
        queueConnections.clear();
    }
    
    /**
     * Returns the connection with the specified index
     * resp. <code>null</code> if no such connection
     * exists.
     * @param index the index
     * @return the connection
     */
    public MockQueueConnection getQueueConnection(int index)
    {
        if(queueConnections.size() <= index) return null;
        return (MockQueueConnection)queueConnections.get(index);
    }
    
    /**
     * Returns the latest created connection
     * resp. <code>null</code> if no such connection
     * exists.
     * @return the connection
     */
    public MockQueueConnection getLatestQueueConnection()
    {
        if(queueConnections.size() == 0) return null;
        return (MockQueueConnection)queueConnections.get(queueConnections.size() - 1);
    }
}
