package com.mockrunner.test.connector;

import java.util.List;

import javax.resource.cci.Interaction;
import javax.resource.cci.Record;

import junit.framework.TestCase;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.connector.ConnectorTestModule;
import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;

public class ConnectorTestModuleTest extends TestCase
{
    private ConnectorMockObjectFactory mockFactory;
    private ConnectorTestModule module;
    
    protected void setUp() throws Exception
    {
        mockFactory = new ConnectorMockObjectFactory();
        module = new ConnectorTestModule(mockFactory);
    }
    
    protected void tearDown() throws Exception
    {
        mockFactory = null;
        module = null;
    }
    
    private void createIndexedRecord(String name) throws Exception
    {
        mockFactory.getMockConnectionFactory().getRecordFactory().createIndexedRecord(name);
    }
    
    private void createMappedRecord(String name) throws Exception
    {
        mockFactory.getMockConnectionFactory().getRecordFactory().createMappedRecord(name);
    }
    
    public void testGetInteractionList() throws Exception
    {
        Interaction interaction1 = mockFactory.getMockConnection().createInteraction();
        Interaction interaction2 = mockFactory.getMockConnection().createInteraction();
        List interactionList = module.getInteractionList();
        assertEquals(2, interactionList.size());
        assertEquals(interaction1, interactionList.get(0));
        assertEquals(interaction2, interactionList.get(1));
    }
    
    public void testVerifyConnectionClosed() throws Exception
    {
        try
        {
            module.verifyConnectionClosed();
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //expected exception
        }
        mockFactory.getMockConnection().close();
        module.verifyConnectionClosed();
    }
    
    public void testVerifyInteractionClosed() throws Exception
    {
        Interaction interaction1 = mockFactory.getMockConnection().createInteraction();
        Interaction interaction2 = mockFactory.getMockConnection().createInteraction();
        Interaction interaction3 = mockFactory.getMockConnection().createInteraction();
        try
        {
            module.verifyAllInteractionsClosed();
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //expected exception
        }
        try
        {
            module.verifyInteractionClosed(3);
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //expected exception
        }
        try
        {
            module.verifyInteractionClosed(2);
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //expected exception
        }
        interaction3.close();
        module.verifyInteractionClosed(2);
        try
        {
            module.verifyAllInteractionsClosed();
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //expected exception
        }
        interaction1.close();
        interaction2.close();
        module.verifyInteractionClosed(0);
        module.verifyInteractionClosed(1);
        module.verifyAllInteractionsClosed();
    }
    
    public void testGetCreatedIndexedRecords() throws Exception
    {
        createIndexedRecord("indexedRecord");
        createIndexedRecord("indexedRecord");
        createIndexedRecord("anotherIndexedRecord");
        List list = module.getCreatedIndexedRecords();
        assertEquals(3, list.size());
        assertEquals("indexedRecord", ((Record)list.get(0)).getRecordName());
        assertEquals("indexedRecord", ((Record)list.get(1)).getRecordName());
        assertEquals("anotherIndexedRecord", ((Record)list.get(2)).getRecordName());
        list = module.getCreatedIndexedRecords("indexedRecord");
        assertEquals(2, list.size());
        assertEquals("indexedRecord", ((Record)list.get(0)).getRecordName());
        assertEquals("indexedRecord", ((Record)list.get(1)).getRecordName());
        list = module.getCreatedIndexedRecords("anotherIndexedRecord");
        assertEquals(1, list.size());
        assertEquals("anotherIndexedRecord", ((Record)list.get(0)).getRecordName());
        list = module.getCreatedIndexedRecords("xyz");
        assertEquals(0, list.size());
    }
    
    public void testGetCreatedMappedRecords() throws Exception
    {
        createMappedRecord("mappedRecord");
        createMappedRecord("mappedRecord");
        createMappedRecord("anotherMappedRecord");
        List list = module.getCreatedMappedRecords();
        assertEquals(3, list.size());
        assertEquals("mappedRecord", ((Record)list.get(0)).getRecordName());
        assertEquals("mappedRecord", ((Record)list.get(1)).getRecordName());
        assertEquals("anotherMappedRecord", ((Record)list.get(2)).getRecordName());
        list = module.getCreatedMappedRecords("mappedRecord");
        assertEquals(2, list.size());
        assertEquals("mappedRecord", ((Record)list.get(0)).getRecordName());
        assertEquals("mappedRecord", ((Record)list.get(1)).getRecordName());
        list = module.getCreatedMappedRecords("anotherMappedRecord");
        assertEquals(1, list.size());
        assertEquals("anotherMappedRecord", ((Record)list.get(0)).getRecordName());
        list = module.getCreatedMappedRecords("xyz");
        assertEquals(0, list.size());
    }
    
    public void testVerifyCreatedIndexedRecords() throws Exception
    {
        createIndexedRecord("indexedRecord");
        createIndexedRecord("indexedRecord");
        createIndexedRecord("anotherIndexedRecord");
        module.verifyNumberCreatedIndexedRecords(3);
        module.verifyNumberCreatedIndexedRecords("indexedRecord", 2);
        module.verifyNumberCreatedIndexedRecords("anotherIndexedRecord", 1);
        module.verifyNumberCreatedMappedRecords("xyz", 0);
        try
        {
            module.verifyNumberCreatedIndexedRecords("indexedRecord", 3);
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberCreatedIndexedRecords(1);
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    public void testVerifyNumberCreatedMappedRecords() throws Exception
    {
        createMappedRecord("mappedRecord");
        createMappedRecord("anotherMappedRecord");
        createMappedRecord("anotherMappedRecord");
        createMappedRecord("anotherMappedRecord");
        module.verifyNumberCreatedMappedRecords(4);
        module.verifyNumberCreatedMappedRecords("mappedRecord", 1);
        module.verifyNumberCreatedMappedRecords("anotherMappedRecord", 3);
        module.verifyNumberCreatedMappedRecords("xyz", 0);
        try
        {
            module.verifyNumberCreatedMappedRecords("xyz", 1);
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberCreatedMappedRecords(3);
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    public void testVerifyLocalTransaction() throws Exception
    {
        module.verifyLocalTransactionNotCommitted();
        module.verifyLocalTransactionNotRolledBack();
        try
        {
            module.verifyLocalTransactionCommitted();
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyLocalTransactionRolledBack();
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        mockFactory.getMockConnection().getLocalTransaction().begin();
        mockFactory.getMockConnection().getLocalTransaction().commit();
        module.verifyLocalTransactionCommitted();
        module.verifyLocalTransactionNotRolledBack();
        try
        {
            module.verifyLocalTransactionNotCommitted();
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        mockFactory.getMockConnection().getLocalTransaction().begin();
        mockFactory.getMockConnection().getLocalTransaction().rollback();
        module.verifyLocalTransactionNotCommitted();
        module.verifyLocalTransactionRolledBack();
        try
        {
            module.verifyLocalTransactionCommitted();
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
}
