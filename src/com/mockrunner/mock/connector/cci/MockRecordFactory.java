package com.mockrunner.mock.connector.cci;

import java.util.ArrayList;
import java.util.List;

import javax.resource.ResourceException;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.Record;
import javax.resource.cci.RecordFactory;

/**
 * Mock implementation of <code>RecordFactory</code>.
 */
public class MockRecordFactory implements RecordFactory
{
    private List indexedRecords;
    private List mappedRecords;
    
    public MockRecordFactory()
    {
        indexedRecords = new ArrayList();
        mappedRecords = new ArrayList();
    }
    
    /**
     * Resets the list of created indexed records.
     */
    public void resetIndexedRecords()
    {
        indexedRecords.clear();
    }
    
    /**
     * Resets the list of created mapped records.
     */
    public void resetMappedRecords()
    {
        mappedRecords.clear();
    }
    
    /**
     * Returns the number of created indexed records.
     * @return the number of created indexed records
     */
    public int getNumberCreatedIndexedRecords()
    {
        return indexedRecords.size();
    }
    
    /**
     * Returns the number of created mapped records.
     * @return the number of created mapped records
     */
    public int getNumberCreatedMappedRecords()
    {
        return mappedRecords.size();
    }
    
    /**
     * Returns a list of all created indexed records.
     * @return the <code>List</code> of all created indexed records
     */
    public List getCreatedIndexedRecords()
    {
        return getCreatedRecords(null, false, indexedRecords);
    }
    
    /**
     * Returns a list of created indexed records that match the specified name.
     * @param recordName the name of the record
     * @return the <code>List</code> of matching indexed records
     */
    public List getCreatedIndexedRecords(String recordName)
    {
        return getCreatedRecords(recordName, true, indexedRecords);
    }
    
    /**
     * Returns a list of all created mapped records.
     * @return the <code>List</code> of all created mapped records
     */
    public List getCreatedMappedRecords()
    {
        return getCreatedRecords(null, false, mappedRecords);
    }
    
    /**
     * Returns a list of created mapped records that match the specified name.
     * @param recordName the name of the record
     * @return the <code>List</code> of matching mapped records
     */
    public List getCreatedMappedRecords(String recordName)
    {
        return getCreatedRecords(recordName, true, mappedRecords);
    }
    
    private List getCreatedRecords(String recordName, boolean recognizeRecordName, List recordWrapperList)
    {
        List result = new ArrayList();
        for(int ii = 0; ii < recordWrapperList.size(); ii++)
        {
            RecordWrapper currentWrapper = (RecordWrapper)recordWrapperList.get(ii);
            if(!recognizeRecordName)
            {
                result.add(currentWrapper.getRecord());
            }
            else
            {
                addRecordIfMatching(recordName, result, currentWrapper);
            }
        }
        return result;
    }

    private void addRecordIfMatching(String recordName, List result, RecordWrapper currentWrapper)
    {
        if(null == recordName)
        {
            if(null == currentWrapper.getRecordName())
            {
                result.add(currentWrapper.getRecord());
            }
        }
        else
        {
            if(recordName.equals(currentWrapper.getRecordName()))
            {
                result.add(currentWrapper.getRecord());
            }
        }
    }

    public IndexedRecord createIndexedRecord(String recordName) throws ResourceException
    {
        MockIndexedRecord record = new MockIndexedRecord(recordName);
        indexedRecords.add(new RecordWrapper(recordName, record));
        return record;
    }

    public MappedRecord createMappedRecord(String recordName) throws ResourceException
    {
        MockMappedRecord record = new MockMappedRecord(recordName);
        mappedRecords.add(new RecordWrapper(recordName, record));
        return record;
    }
    
    private class RecordWrapper
    {
        private String recordName;
        private Record record;
        
        public RecordWrapper(String recordName, Record record)
        {
            this.recordName = recordName;
            this.record = record;
        }
        
        public String getRecordName()
        {
            return recordName;
        }

        public Record getRecord()
        {
            return record;
        } 
    }
}
