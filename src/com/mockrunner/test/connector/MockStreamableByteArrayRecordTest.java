package com.mockrunner.test.connector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import com.mockrunner.mock.connector.cci.MockStreamableByteArrayRecord;

public class MockStreamableByteArrayRecordTest extends TestCase
{
    private MockStreamableByteArrayRecord record;

    protected void setUp() throws Exception
    {
        record = new MockStreamableByteArrayRecord();
    }

    protected void tearDown() throws Exception
    {
        record = null;
    }
    
    public void testSetAndGetContent()
    {
        assertNull(record.getContent());
        record.setContent(null);
        assertNull(record.getContent());
        record.setContent(new byte[0]);
        assertTrue(Arrays.equals(new byte[0], record.getContent()));
        byte[] content = new byte[] {1, 2, 3};
        record.setContent(content);
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, record.getContent()));
        content[0] = 3;
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, record.getContent()));
        content = record.getContent();
        content[0] = 3;
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, record.getContent()));
        record.setContent(null);
        assertNull(record.getContent());
    }
    
    public void testRead() throws Exception
    {
        record.read(null);
        assertNull(record.getContent());
        record.read(new ByteArrayInputStream(new byte[0]));
        assertTrue(Arrays.equals(new byte[0], record.getContent()));
        record.read(new ByteArrayInputStream(new byte[] {1, 2, 3}));
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, record.getContent()));
        record.read(null);
        assertNull(record.getContent());
    }
    
    public void testWrite() throws Exception
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        record.write(output);
        assertEquals(0, output.size());
        record.setContent(new byte[0]);
        record.write(output);
        assertEquals(0, output.size());
        record.setContent(new byte[] {1, 2, 3});
        record.write(output);
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, output.toByteArray()));
    }
    
    public void testEquals() throws Exception
    {
        assertFalse(record.equals(null));
        assertFalse(record.equals("abc"));
        assertTrue(record.equals(record));
        record.setRecordName("1");
        record.setRecordShortDescription("2");
        MockStreamableByteArrayRecord other = new MockStreamableByteArrayRecord();
        assertFalse(record.equals(other));
        other.setRecordName("1");
        assertFalse(record.equals(other));
        other.setRecordShortDescription("2");
        assertTrue(record.equals(other));
        record.setContent(new byte[] {1, 2, 3});
        assertFalse(record.equals(other));
        other.read(new ByteArrayInputStream(new byte[] {1, 2, 3}));
        assertTrue(record.equals(other));
        assertTrue(other.equals(record));
        assertEquals(other.hashCode(), record.hashCode());
        other.setContent(null);
        assertFalse(record.equals(other));
        assertFalse(other.equals(record));
        record.setContent(null);
        assertTrue(record.equals(other));
        assertTrue(other.equals(record));
        assertEquals(other.hashCode(), record.hashCode());
    }
    
    public void testClone()
    {
        record.setRecordName("1");
        record.setRecordShortDescription("2");
        MockStreamableByteArrayRecord clone = (MockStreamableByteArrayRecord)record.clone();
        assertEquals("1", clone.getRecordName());
        assertEquals("2", clone.getRecordShortDescription());
        assertNull(clone.getContent());
        record.setContent(new byte[0]);
        clone = (MockStreamableByteArrayRecord)record.clone();
        assertEquals("1", clone.getRecordName());
        assertEquals("2", clone.getRecordShortDescription());
        assertTrue(Arrays.equals(new byte[0], clone.getContent()));
        record.setContent(new byte[] {1, 2, 3});
        clone = (MockStreamableByteArrayRecord)record.clone();
        assertEquals("1", clone.getRecordName());
        assertEquals("2", clone.getRecordShortDescription());
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, clone.getContent()));
    }
}
