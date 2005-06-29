package com.mockrunner.mock.connector.spi;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnectionFactory;

/**
 * Mock implementation of <code>ConnectionManager</code>.
 */
public class MockConnectionManager implements ConnectionManager 
{
	private static final long serialVersionUID = 3257571719568175408L;

	public Object allocateConnection(ManagedConnectionFactory managedConnectionFactory, ConnectionRequestInfo connectionRequestInfo) throws ResourceException 
    {
		return null;
	}
}
