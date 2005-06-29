package com.mockrunner.mock.connector.spi;

import java.io.PrintWriter;
import java.util.Vector;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

/**
 * Mock implementation of <code>ManagedConnection</code>.
 */
public class MockManagedConnection implements ManagedConnection 
{
	private PrintWriter pw;
	private ManagedConnectionMetaData metaData;
	private Vector listeners;

	public MockManagedConnection() 
    {
		metaData = new MockManagedConnectionMetaData();
		listeners = new Vector();
	}

	public Object getConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException 
    {
		return null;
	}

	public void destroy() throws ResourceException 
    {

	}

	public void cleanup() throws ResourceException 
    {

	}

	public void associateConnection(Object arg0) throws ResourceException 
    {

	}

	public void addConnectionEventListener(ConnectionEventListener listener) 
    {
		listeners.add(listener);
	}

	public void removeConnectionEventListener(ConnectionEventListener listener) 
    {
		listeners.remove(listener);
	}

	public XAResource getXAResource() throws ResourceException 
    {
		return null;
	}

	public LocalTransaction getLocalTransaction() throws ResourceException 
    {
		return null;
	}

	public ManagedConnectionMetaData getMetaData() throws ResourceException 
    {
		return metaData;
	}

	public PrintWriter getLogWriter() throws ResourceException 
    {
		return pw;
	}

	public void setLogWriter(PrintWriter pw) throws ResourceException 
    {
		this.pw = pw;
	}
    
    public void setMetaData(ManagedConnectionMetaData metaData)
    {
        this.metaData = metaData;
    }
}
