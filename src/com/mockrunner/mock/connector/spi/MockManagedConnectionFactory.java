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
    
	private PrintWriter pw;
	private ConnectionManager cm;

	public Object createConnectionFactory() throws ResourceException 
    {
		return createConnectionFactory(null);
	}

	public Object createConnectionFactory(ConnectionManager cm) throws ResourceException 
    {
		this.cm = cm;
		return this;
	}

	public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cri) throws ResourceException 
    {
		Object mc = null;
		if(cm == null)
        {
			// TODO is this correct?
			mc = new MockManagedConnection().getConnection(subject, cri);
		} 
        else 
        {
			mc = cm.allocateConnection(this, cri);
		}
		return (MockManagedConnection)mc;
	}

	public ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException 
    {
		ManagedConnection match = null;
        Iterator iter = set.iterator();
        if (iter.hasNext()) 
        {
			// TODO also process subject and connectionrequestinfo
            match = (ManagedConnection) iter.next();
        }
        return match;
	}

	public PrintWriter getLogWriter() throws ResourceException 
    {
		return pw;
	}

	public void setLogWriter(PrintWriter pw) throws ResourceException 
    {
		this.pw = pw;
	}
}
