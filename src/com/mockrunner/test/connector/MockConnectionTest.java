package com.mockrunner.test.connector;

import javax.resource.ResourceException;
import javax.resource.cci.LocalTransaction;

import junit.framework.TestCase;

import com.mockrunner.mock.connector.cci.MockConnection;
import com.mockrunner.mock.connector.cci.MockConnectionMetaData;
import com.mockrunner.mock.connector.cci.MockLocalTransaction;
import com.mockrunner.mock.connector.cci.MockResultSetInfo;

public class MockConnectionTest extends TestCase
{
    private MockConnection connection;

    protected void setUp() throws Exception
    {
        connection = new MockConnection();
    }

    protected void tearDown() throws Exception
    {
        connection = null;
    }
    
    public void testGetMetaData() throws Exception
    {
        assertTrue(connection.getMetaData() instanceof MockConnectionMetaData);
        MockConnectionMetaData metaData = new MockConnectionMetaData() {};
        connection.setMetaData(metaData);
        assertSame(metaData, connection.getMetaData());
    }
    
    public void testGetResultSetInfo() throws Exception
    {
        assertTrue(connection.getResultSetInfo() instanceof MockResultSetInfo);
        MockResultSetInfo resultSetInfo = new MockResultSetInfo() {};
        connection.setResultSetInfo(resultSetInfo);
        assertSame(resultSetInfo, connection.getResultSetInfo());
    }
    
    public void testGetLocalTransaction() throws Exception
    {
        assertTrue(connection.getLocalTransaction() instanceof MockLocalTransaction);
        MockLocalTransaction localTransaction = new MockLocalTransaction() {};
        connection.setLocalTransaction(localTransaction);
        assertSame(localTransaction, connection.getLocalTransaction());
        assertSame(localTransaction, connection.getMockLocalTransaction());
        LocalTransaction otherLocalTransaction = new TestLocalTransaction() {};
        connection.setLocalTransaction(otherLocalTransaction);
        assertSame(otherLocalTransaction, connection.getLocalTransaction());
        assertNull(connection.getMockLocalTransaction());
    }
    
    private class TestLocalTransaction implements LocalTransaction
    {
        public void begin() throws ResourceException
        {
            
        }

        public void commit() throws ResourceException
        {
            
        }

        public void rollback() throws ResourceException
        {
            
        }
    }
}
