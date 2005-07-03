package com.mockrunner.test.connector;

import java.util.List;

import com.mockrunner.mock.connector.cci.MockIndexedRecord;
import com.mockrunner.mock.connector.cci.MockMappedRecord;
import com.mockrunner.mock.connector.cci.MockRecordFactory;

import junit.framework.TestCase;

public class MockRecordFactoryTest extends TestCase
{
    private MockRecordFactory recordFactory;

    protected void setUp() throws Exception
    {
        recordFactory = new MockRecordFactory();
    }

    protected void tearDown() throws Exception
    {
        recordFactory = null;
    }
    
    public void testCreateIndexedRecord() throws Exception
    {
        assertEquals(0, recordFactory.getNumberCreatedIndexedRecords());
        MockIndexedRecord record1 = (MockIndexedRecord)recordFactory.createIndexedRecord("recordName1");
        assertEquals("recordName1", record1.getRecordName());
        assertEquals("recordName1", record1.getRecordShortDescription());
        MockIndexedRecord record2 = (MockIndexedRecord)recordFactory.createIndexedRecord("recordName2");
        assertEquals("recordName2", record2.getRecordName());
        assertEquals("recordName2", record2.getRecordShortDescription());
        assertEquals(2, recordFactory.getNumberCreatedIndexedRecords());
        List recordList = recordFactory.getCreatedIndexedRecords();
        assertEquals(2, recordList.size());
        assertSame(record1, recordList.get(0));
        assertSame(record2, recordList.get(1));
        recordFactory.resetIndexedRecords();
        assertEquals(0, recordFactory.getNumberCreatedIndexedRecords());
        assertEquals(0, recordFactory.getCreatedIndexedRecords().size());
    }
    
    public void testCreateMappedRecord() throws Exception
    {
        assertEquals(0, recordFactory.getNumberCreatedMappedRecords());
        MockMappedRecord record1 = (MockMappedRecord)recordFactory.createMappedRecord("recordName1");
        assertEquals("recordName1", record1.getRecordName());
        assertEquals("recordName1", record1.getRecordShortDescription());
        MockMappedRecord record2 = (MockMappedRecord)recordFactory.createMappedRecord("recordName2");
        assertEquals("recordName2", record2.getRecordName());
        assertEquals("recordName2", record2.getRecordShortDescription());
        assertEquals(2, recordFactory.getNumberCreatedMappedRecords());
        List recordList = recordFactory.getCreatedMappedRecords();
        assertEquals(2, recordList.size());
        assertSame(record1, recordList.get(0));
        assertSame(record2, recordList.get(1));
        recordFactory.resetMappedRecords();
        assertEquals(0, recordFactory.getNumberCreatedMappedRecords());
        assertEquals(0, recordFactory.getCreatedMappedRecords().size());
    }
    
    public void testGetCreatedIndexedRecords() throws Exception
    {
        MockIndexedRecord record1 = (MockIndexedRecord)recordFactory.createIndexedRecord("recordName");
        MockIndexedRecord record2 = (MockIndexedRecord)recordFactory.createIndexedRecord(null);
        MockIndexedRecord record3 = (MockIndexedRecord)recordFactory.createIndexedRecord("recordName");
        MockIndexedRecord record4 = (MockIndexedRecord)recordFactory.createIndexedRecord("anotherRecordName");
        MockIndexedRecord record5 = (MockIndexedRecord)recordFactory.createIndexedRecord("anotherRecordName");
        assertEquals(5, recordFactory.getCreatedIndexedRecords().size());
        record1.setRecordName("anotherRecordName");
        record4.setRecordName("recordName");
        List list = recordFactory.getCreatedIndexedRecords("recordName");
        assertEquals(2, list.size());
        assertSame(record1, list.get(0));
        assertSame(record3, list.get(1));
        list = recordFactory.getCreatedIndexedRecords("anotherRecordName");
        assertEquals(2, list.size());
        assertSame(record4, list.get(0));
        assertSame(record5, list.get(1));
        list = recordFactory.getCreatedIndexedRecords(null);
        assertEquals(1, list.size());
        assertSame(record2, list.get(0));
        recordFactory.resetIndexedRecords();
        assertEquals(0, recordFactory.getCreatedIndexedRecords().size());
        assertEquals(0, recordFactory.getCreatedIndexedRecords("recordName").size());
        assertEquals(0, recordFactory.getCreatedIndexedRecords("anotherRecordName").size());
    }
    
    public void testGetCreatedMappedRecords() throws Exception
    {
        MockMappedRecord record1 = (MockMappedRecord)recordFactory.createMappedRecord("recordName");
        MockMappedRecord record2 = (MockMappedRecord)recordFactory.createMappedRecord(null);
        MockMappedRecord record3 = (MockMappedRecord)recordFactory.createMappedRecord("recordName");
        MockMappedRecord record4 = (MockMappedRecord)recordFactory.createMappedRecord("anotherRecordName");
        MockMappedRecord record5 = (MockMappedRecord)recordFactory.createMappedRecord("anotherRecordName");
        assertEquals(5, recordFactory.getCreatedMappedRecords().size());
        record1.setRecordName("anotherRecordName");
        record4.setRecordName("recordName");
        List list = recordFactory.getCreatedMappedRecords("recordName");
        assertEquals(2, list.size());
        assertSame(record1, list.get(0));
        assertSame(record3, list.get(1));
        list = recordFactory.getCreatedMappedRecords("anotherRecordName");
        assertEquals(2, list.size());
        assertSame(record4, list.get(0));
        assertSame(record5, list.get(1));
        list = recordFactory.getCreatedMappedRecords(null);
        assertEquals(1, list.size());
        assertSame(record2, list.get(0));
        recordFactory.resetMappedRecords();
        assertEquals(0, recordFactory.getCreatedMappedRecords().size());
        assertEquals(0, recordFactory.getCreatedMappedRecords("recordName").size());
        assertEquals(0, recordFactory.getCreatedMappedRecords("anotherRecordName").size());
    }
}
