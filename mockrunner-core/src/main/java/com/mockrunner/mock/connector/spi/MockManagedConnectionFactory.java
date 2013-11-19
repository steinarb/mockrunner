package com.mockrunner.mock.connector.spi;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;

/**
 * Mock implementation of <code>ManagedConnectionFactory</code>.
 */
public class MockManagedConnectionFactory implements ManagedConnectionFactory, Serializable 
{
	private static final long serialVersionUID = 3257291348330558264L;

    private PrintWriter logWriter;
    private ConnectionManager connectionManager;

    public Object createConnectionFactory() throws ResourceException
    {
        return createConnectionFactory(null);
    }

    public Object createConnectionFactory(ConnectionManager connectionManager) throws ResourceException
    {
        this.connectionManager = connectionManager;
        return this;
    }

    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo requestInfo) throws ResourceException
    {
        Object managedConnection = null;
        if(connectionManager == null)
        {
            // TODO is this correct?
            managedConnection = new MockManagedConnection().getConnection(subject, requestInfo);
        } 
        else
        {
            managedConnection = connectionManager.allocateConnection(this, requestInfo);
        }
        return (MockManagedConnection)managedConnection;
    }

    public ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException
    {
        ManagedConnection managedConnection = null;
        Iterator iterator = set.iterator();
        if(iterator.hasNext())
        {
            // TODO also process subject and connectionrequestinfo
            managedConnection = (ManagedConnection)iterator.next();
        }
        return managedConnection;
    }

    public PrintWriter getLogWriter() throws ResourceException
    {
        return logWriter;
    }

    public void setLogWriter(PrintWriter logWriter) throws ResourceException
    {
        this.logWriter = logWriter;
    }
}
