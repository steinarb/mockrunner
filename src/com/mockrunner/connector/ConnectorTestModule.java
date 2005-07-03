package com.mockrunner.connector;

import java.util.List;

import javax.resource.cci.RecordFactory;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;
import com.mockrunner.mock.connector.cci.MockRecordFactory;

/**
 * Module for JCA tests.
 */
public class ConnectorTestModule 
{
	private ConnectorMockObjectFactory mockFactory;

	public ConnectorTestModule(ConnectorMockObjectFactory mockFactory)
	{
		this.mockFactory = mockFactory;
	}
    
    /**
     * Returns the {@link InteractionHandler}.
     * @return the {@link InteractionHandler}
     */
    public InteractionHandler getInteractionHandler()
    {
        return mockFactory.getInteractionHandler();
    }
    
    private MockRecordFactory getMockRecordFactory()
    {
        try
        {
            RecordFactory factory = mockFactory.getMockConnectionFactory().getRecordFactory();
            if(factory instanceof MockRecordFactory)
            {
                return (MockRecordFactory)factory;
            }
            return null;
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Returns a list of all created indexed records
     * by delegating to {@link com.mockrunner.mock.connector.cci.MockRecordFactory#getCreatedIndexedRecords()}.
     * @return the <code>List</code> of all created indexed records
     */
    public List getCreatedIndexedRecords()
    {
        return getMockRecordFactory().getCreatedIndexedRecords();
    }
    
    /**
     * Returns a list of created indexed records that match the specified name
     * by delegating to {@link com.mockrunner.mock.connector.cci.MockRecordFactory#getCreatedIndexedRecords(String)}.
     * @param recordName the name of the record
     * @return the <code>List</code> of matching indexed records
     */
    public List getCreatedIndexedRecords(String recordName)
    {
        return getMockRecordFactory().getCreatedIndexedRecords(recordName);
    }
    
    /**
     * Returns a list of all created mapped records
     * by delegating to {@link com.mockrunner.mock.connector.cci.MockRecordFactory#getCreatedMappedRecords()}.
     * @return the <code>List</code> of all created mapped records
     */
    public List getCreatedMappedRecords()
    {
        return getMockRecordFactory().getCreatedMappedRecords();
    }
    
    /**
     * Returns a list of created mapped records that match the specified name
     * by delegating to {@link com.mockrunner.mock.connector.cci.MockRecordFactory#getCreatedMappedRecords(String)}.
     * @param recordName the name of the record
     * @return the <code>List</code> of matching mapped records
     */
    public List getCreatedMappedRecords(String recordName)
    {
        return getMockRecordFactory().getCreatedMappedRecords(recordName);
    }
    
    /**
     * Verifies that <code>expected</code> number of indexed records
     * have been created.
     * @param expected the expected number of indexed records
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberCreatedIndexedRecords(int expected)
    {
        int actual = getMockRecordFactory().getNumberCreatedIndexedRecords();
        if(actual != expected)
        {
            throw new VerifyFailedException("Expected " + expected + " indexed records, actual " + actual + " indexed records");
        }
    }
    
    /**
     * Verifies that <code>expected</code> number of indexed records
     * with the specified name have been created.
     * @param recordName the name of the record
     * @param expected the expected number of indexed records
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberCreatedIndexedRecords(String recordName, int expected)
    {
        List list = getCreatedIndexedRecords(recordName);
        if(list.size() != expected)
        {
            throw new VerifyFailedException("Expected " + expected + " indexed records with the name " + recordName + ", actual " + list.size() + " indexed records");
        }
    }
    
    /**
     * Verifies that <code>expected</code> number of mapped records
     * have been created.
     * @param expected the expected number of mapped records
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberCreatedMappedRecords(int expected)
    {
        int actual = getMockRecordFactory().getNumberCreatedMappedRecords();
        if(actual != expected)
        {
            throw new VerifyFailedException("Expected " + expected + " mapped records, actual " + actual + " mapped records");
        }
    }
    
    /**
     * Verifies that <code>expected</code> number of mapped records
     * with the specified name have been created.
     * @param recordName the name of the record
     * @param expected the expected number of mapped records
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberCreatedMappedRecords(String recordName, int expected)
    {
        List list = getCreatedMappedRecords(recordName);
        if(list.size() != expected)
        {
            throw new VerifyFailedException("Expected " + expected + " mapped records with the name " + recordName + ", actual " + list.size() + " mapped records");
        }
    }
}
