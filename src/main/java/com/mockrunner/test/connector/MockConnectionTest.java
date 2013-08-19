package com.mockrunner.test.connector;

import java.util.List;

import javax.resource.ResourceException;
import javax.resource.cci.Interaction;
import javax.resource.cci.LocalTransaction;

import junit.framework.TestCase;

import com.mockrunner.mock.connector.cci.MockConnection;
import com.mockrunner.mock.connector.cci.MockConnectionMetaData;
import com.mockrunner.mock.connector.cci.MockInteraction;
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
    
    public void testGetInteractionList() throws Exception
    {
        Interaction interaction1 = connection.createInteraction();
        Interaction interaction2 = connection.createInteraction();
        Interaction interaction3 = connection.createInteraction();
        List interactionList = connection.getInteractionList();
        assertEquals(3, interactionList.size());
        assertEquals(interaction1, interactionList.get(0));
        assertEquals(interaction2, interactionList.get(1));
        assertEquals(interaction3, interactionList.get(2));
        try
        {
            interactionList.add(new MockInteraction(connection));
            fail();
        }
        catch(RuntimeException exc)
        {
            //expected exception
        }
    }
    
    public void testClose() throws Exception
    {
        MockInteraction interaction1 = (MockInteraction)connection.createInteraction();
        MockInteraction interaction2 = (MockInteraction)connection.createInteraction();
        MockInteraction interaction3 = (MockInteraction)connection.createInteraction();
        connection.close();
        assertTrue(connection.isClosed());
        assertTrue(interaction1.isClosed());
        assertTrue(interaction2.isClosed());
        assertTrue(interaction3.isClosed());
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
