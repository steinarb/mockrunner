package com.mockrunner.test.connector;

import junit.framework.TestCase;

import com.mockrunner.mock.connector.cci.MockRecord;

public class MockRecordTest extends TestCase
{
    private MockRecord record;

    protected void setUp() throws Exception
    {
        record = new MockRecord();
    }

    protected void tearDown() throws Exception
    {
        record = null;
    }
    
    public void testEquals()
    {
        assertFalse(record.equals(null));
        assertFalse(record.equals("abc"));
        assertTrue(record.equals(record));
        MockRecord other = new MockRecord();
        assertTrue(record.equals(other));
        assertTrue(other.equals(record));
        assertEquals(record.hashCode(), other.hashCode());
        record.setRecordName(null);
        assertFalse(record.equals(other));
        other.setRecordName(null);
        assertTrue(record.equals(other));
        assertTrue(other.equals(record));
        assertEquals(record.hashCode(), other.hashCode());
        record.setRecordShortDescription(null);
        assertFalse(record.equals(other));
        other.setRecordShortDescription(null);
        assertTrue(record.equals(other));
        assertTrue(other.equals(record));
        assertEquals(record.hashCode(), other.hashCode());
        record.setRecordName("aRecordName");
        other.setRecordName("anotherRecordName");
        assertFalse(record.equals(other));
        record.setRecordName("anotherRecordName");
        assertTrue(record.equals(other));
        assertTrue(other.equals(record));
        assertEquals(record.hashCode(), other.hashCode());
        record.setRecordShortDescription("shortDescription");
        other.setRecordShortDescription("otherShortDescription");
        assertFalse(record.equals(other));
        other.setRecordShortDescription("shortDescription");
        assertTrue(record.equals(other));
        assertTrue(other.equals(record));
        assertEquals(record.hashCode(), other.hashCode());
    }
    
    public void testToString()
    {
        record.setRecordName("recordName");
        record.setRecordShortDescription("shortDescription");
        String string = record.toString();
        assertTrue(-1 != string.indexOf(MockRecord.class.getName()));
        assertTrue(-1 != string.indexOf("recordName"));
        assertTrue(-1 != string.indexOf("shortDescription"));
    }
    
    public void testClone() throws Exception
    {
        record.setRecordName("recordName");
        record.setRecordShortDescription("shortDescription");
        MockRecord clone = (MockRecord)record.clone();
        assertNotSame(clone, record);
        assertEquals("recordName", clone.getRecordName());
        assertEquals("shortDescription", clone.getRecordShortDescription());
    }
}
