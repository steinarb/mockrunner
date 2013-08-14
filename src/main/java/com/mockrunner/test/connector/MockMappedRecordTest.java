package com.mockrunner.test.connector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.mockrunner.mock.connector.cci.MockMappedRecord;

public class MockMappedRecordTest extends TestCase
{
    private MockMappedRecord mappedRecord;

    protected void setUp() throws Exception
    {
        mappedRecord = new MockMappedRecord();
    }

    protected void tearDown() throws Exception
    {
        mappedRecord = null;
    }
    
    public void testMapOperations()
    {
        assertTrue(mappedRecord.isEmpty());
        assertEquals(0, mappedRecord.size());
        mappedRecord.put("1", "1");
        assertFalse(mappedRecord.isEmpty());
        assertEquals(1, mappedRecord.size());
        mappedRecord.put("2", "2");
        Set keySet = mappedRecord.keySet();
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains("1"));
        assertTrue(keySet.contains("2"));
        assertTrue(mappedRecord.containsKey("1"));
        assertTrue(mappedRecord.containsValue("1"));
        assertTrue(mappedRecord.containsKey("2"));
        assertTrue(mappedRecord.containsValue("2"));
        mappedRecord.remove("2");
        assertTrue(mappedRecord.containsKey("1"));
        assertTrue(mappedRecord.containsValue("1"));
        assertFalse(mappedRecord.containsKey("2"));
        assertFalse(mappedRecord.containsValue("2"));
        Iterator values = mappedRecord.values().iterator();
        assertEquals("1", values.next());
        assertFalse(values.hasNext());
        mappedRecord.putAll(mappedRecord);
        assertEquals(1, mappedRecord.size());
        mappedRecord.clear();
        assertTrue(mappedRecord.isEmpty());
        assertEquals(0, mappedRecord.size());
    }
    
    public void testEquals()
    {
        assertFalse(mappedRecord.equals(null));
        assertFalse(mappedRecord.equals("abc"));
        assertTrue(mappedRecord.equals(mappedRecord));
        mappedRecord.setRecordName("1");
        mappedRecord.setRecordShortDescription("2");
        mappedRecord.put("1", "3");
        MockMappedRecord other = new MockMappedRecord();
        assertFalse(mappedRecord.equals(other));
        other.put("1", "3");
        assertFalse(mappedRecord.equals(other));
        other.setRecordName("1");
        assertFalse(mappedRecord.equals(other));
        other.setRecordShortDescription("2");
        assertTrue(mappedRecord.equals(other));
        assertTrue(other.equals(mappedRecord));
        assertEquals(other.hashCode(), mappedRecord.hashCode());
        other.put(null, null);
        assertFalse(mappedRecord.equals(other));
        assertFalse(other.equals(mappedRecord));
        mappedRecord.put(null, null);
        assertTrue(mappedRecord.equals(other));
        assertTrue(other.equals(mappedRecord));
        assertEquals(other.hashCode(), mappedRecord.hashCode());
    }
    
    public void testToString()
    {
        mappedRecord.setRecordName("recordName");
        mappedRecord.setRecordShortDescription("shortDescription");
        Map map = new HashMap();
        map.put("1", "1");
        map.put("2", "2");
        mappedRecord.putAll(map);
        String string = mappedRecord.toString();
        assertTrue(-1 != string.indexOf(MockMappedRecord.class.getName()));
        assertTrue(-1 != string.indexOf("recordName"));
        assertTrue(-1 != string.indexOf("shortDescription"));
        assertTrue(-1 != string.indexOf(map.toString()));
    }
    
    public void testClone()
    {
        mappedRecord.setRecordName("1");
        mappedRecord.setRecordShortDescription("2");
        mappedRecord.put("1", "This");
        mappedRecord.put("2", "is");
        mappedRecord.put("3", "a");
        mappedRecord.put("4", "record");
        MockMappedRecord clone = (MockMappedRecord)mappedRecord.clone();
        assertNotSame(clone, mappedRecord);
        assertEquals("1", clone.getRecordName());
        assertEquals("2", clone.getRecordShortDescription());
        assertEquals("This", clone.get("1"));
        assertEquals("is", clone.get("2"));
        assertEquals("a", clone.get("3"));
        assertEquals("record", clone.get("4"));
    }
}
