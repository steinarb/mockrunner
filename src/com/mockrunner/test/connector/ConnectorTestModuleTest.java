package com.mockrunner.test.connector;

import java.util.List;

import javax.resource.cci.Record;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.connector.ConnectorTestModule;
import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;

import junit.framework.TestCase;

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
