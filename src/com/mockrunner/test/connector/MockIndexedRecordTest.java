package com.mockrunner.test.connector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.mockrunner.mock.connector.cci.MockIndexedRecord;

public class MockIndexedRecordTest extends TestCase
{
    private MockIndexedRecord indexedRecord;

    protected void setUp() throws Exception
    {
        indexedRecord = new MockIndexedRecord();
    }

    protected void tearDown() throws Exception
    {
        indexedRecord = null;
    }
    
    public void testListOperations()
    {
        assertTrue(indexedRecord.isEmpty());
        assertEquals(0, indexedRecord.size());
        indexedRecord.add("1");
        assertFalse(indexedRecord.isEmpty());
        assertEquals(1, indexedRecord.size());
        indexedRecord.add("2");
        Iterator iterator = indexedRecord.iterator();
        assertEquals("1", iterator.next());
        assertEquals("2", iterator.next());
        assertFalse(iterator.hasNext());
        assertEquals(1, indexedRecord.indexOf("2"));
        indexedRecord.set(0, "5");
        String[] array = (String[])indexedRecord.toArray(new String[indexedRecord.size()]);
        assertTrue(Arrays.equals(array, new String[] {"5", "2"}));
        try
        {
            indexedRecord.add(5, "5");
            fail();
        } 
        catch(IndexOutOfBoundsException exc)
        {
            //should throw exception
        }
        indexedRecord.remove(1);
        assertEquals(1, indexedRecord.size());
        assertEquals("5", indexedRecord.get(0));
        indexedRecord.remove("7");
        assertEquals(1, indexedRecord.size());
        indexedRecord.clear();
        assertTrue(indexedRecord.isEmpty());
        assertEquals(0, indexedRecord.size());
    }
    
    public void testEquals()
    {
        assertFalse(indexedRecord.equals(null));
        assertFalse(indexedRecord.equals("abc"));
        assertTrue(indexedRecord.equals(indexedRecord));
        indexedRecord.setRecordName("1");
        indexedRecord.setRecordShortDescription("2");
        indexedRecord.add("3");
        MockIndexedRecord other = new MockIndexedRecord();
        assertFalse(indexedRecord.equals(other));
        other.add("3");
        assertFalse(indexedRecord.equals(other));
        other.setRecordName("1");
        assertFalse(indexedRecord.equals(other));
        other.setRecordShortDescription("2");
        assertTrue(indexedRecord.equals(other));
        assertTrue(other.equals(indexedRecord));
        assertEquals(other.hashCode(), indexedRecord.hashCode());
        other.add(null);
        assertFalse(indexedRecord.equals(other));
        assertFalse(other.equals(indexedRecord));
        indexedRecord.add(null);
        assertTrue(indexedRecord.equals(other));
        assertTrue(other.equals(indexedRecord));
        assertEquals(other.hashCode(), indexedRecord.hashCode());
    }
    
    public void testToString()
    {
        indexedRecord.setRecordName("recordName");
        indexedRecord.setRecordShortDescription("shortDescription");
        List list = new ArrayList();
        list.add("1");
        list.add("2");
        indexedRecord.addAll(list);
        String string = indexedRecord.toString();
        assertTrue(-1 != string.indexOf(MockIndexedRecord.class.getName()));
        assertTrue(-1 != string.indexOf("recordName"));
        assertTrue(-1 != string.indexOf("shortDescription"));
        assertTrue(-1 != string.indexOf(list.toString()));
    }
    
    public void testClone()
    {
        indexedRecord.setRecordName("1");
        indexedRecord.setRecordShortDescription("2");
        indexedRecord.add("This");
        indexedRecord.add("is");
        indexedRecord.add("a");
        indexedRecord.add("record");
        MockIndexedRecord clone = (MockIndexedRecord)indexedRecord.clone();
        assertNotSame(clone, indexedRecord);
        assertEquals("1", clone.getRecordName());
        assertEquals("2", clone.getRecordShortDescription());
        assertEquals("This", clone.get(0));
        assertEquals("is", clone.get(1));
        assertEquals("a", clone.get(2));
        assertEquals("record", clone.get(3));
    }
}
