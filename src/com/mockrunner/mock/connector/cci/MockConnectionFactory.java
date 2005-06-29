package com.mockrunner.mock.connector.cci;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ConnectionSpec;
import javax.resource.cci.RecordFactory;
import javax.resource.cci.ResourceAdapterMetaData;

/**
 * Mock implementation of <code>ConnectionFactory</code>
 */
public class MockConnectionFactory implements ConnectionFactory 
{
	private MockConnection connection;
    
    public void setConnection(MockConnection connection)
    {
        this.connection = connection;
    }

	public Connection getConnection() throws ResourceException 
    {
		return connection;
	}

	public MockConnection getMockConnection() 
    {
		return connection;
	}

	public Connection getConnection(ConnectionSpec cs) throws ResourceException 
    {
		return connection;
	}

	public RecordFactory getRecordFactory() throws ResourceException 
    {
		return null;
	}

	public ResourceAdapterMetaData getMetaData() throws ResourceException 
    {
		return null;
	}

	public void setReference(Reference reference) 
    {

	}

	public Reference getReference() throws NamingException 
    {
		return null;
	}
}
