package com.mockrunner.test.connector;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionMetaData;
import javax.resource.cci.Interaction;
import javax.resource.cci.LocalTransaction;
import javax.resource.cci.ResultSetInfo;

import com.mockrunner.mock.connector.cci.MockConnection;
import com.mockrunner.mock.connector.cci.MockConnectionFactory;

import junit.framework.TestCase;

public class MockConnectionFactoryTest extends TestCase
{
    public void testConnection() throws Exception
    {
        MockConnectionFactory factory = new MockConnectionFactory();
        MockConnection mockConnection = new MockConnection();
        factory.setConnection(mockConnection);
        assertSame(mockConnection, factory.getConnection());
        assertSame(mockConnection, factory.getConnection(null));
        assertSame(mockConnection, factory.getMockConnection());
        TestConnection testConnection = new TestConnection();
        factory.setConnection(testConnection);
        assertSame(testConnection, factory.getConnection());
        assertSame(testConnection, factory.getConnection(null));
        assertNull(factory.getMockConnection());
    }
    
    private class TestConnection implements Connection
    {
        public void close() throws ResourceException
        {
            
        }

        public Interaction createInteraction() throws ResourceException
        {
            return null;
        }

        public LocalTransaction getLocalTransaction() throws ResourceException
        {
            return null;
        }

        public ConnectionMetaData getMetaData() throws ResourceException
        {
            return null;
        }

        public ResultSetInfo getResultSetInfo() throws ResourceException
        {
            return null;
        }
    }
}
