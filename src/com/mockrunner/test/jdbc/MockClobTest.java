package com.mockrunner.test.jdbc;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import com.mockrunner.mock.jdbc.MockClob;

import junit.framework.TestCase;

public class MockClobTest extends TestCase
{
    private MockClob clob;

    protected void setUp() throws Exception
    {
        super.setUp();
        clob = new MockClob("This is a Test Clob");
    }

    public void testGetData() throws Exception
    {
        assertEquals(" is a Test Clob", clob.getSubString(5, 15));
        assertEquals("This is a Test Clob", clob.getSubString(1, 19));
        assertEquals("Th", clob.getSubString(1, 2));
        assertEquals("C", clob.getSubString(16, 1));
        Reader reader = clob.getCharacterStream();
        char[] charData = new char[19];
        reader.read(charData);
        String readString = new String(charData);
        assertEquals("This is a Test Clob", readString);
        InputStream stream = clob.getAsciiStream();
        byte[] byteData = new byte[19];
        stream.read(byteData);
        assertTrue(Arrays.equals("This is a Test Clob".getBytes(), byteData));
    }
    
    public void testPosition() throws Exception
    {
        assertEquals(16, clob.position("Clob", 1));
        assertEquals(16, clob.position(new MockClob("Clob"), 5));
        assertEquals(-1, clob.position(new MockClob("XYZ"), 5));
        assertEquals(1, clob.position("T", 1));
        assertEquals(11, clob.position("T", 2));
        assertEquals(1, clob.position(clob, 1));
    }
    
    public void testUpdateData() throws Exception
    {
        clob.setString(11, "XYZZ");
        assertEquals("This is a XYZZ Clob", clob.getSubString(1, 19));
        clob.setString(11, "Test Mock Clob");
        assertEquals("This is a Test Mock Clob", clob.getSubString(1, 24));
        clob.setString(1, "XYZ This", 4, 4);
        assertEquals("This is a Test Mock Clob", clob.getSubString(1, 24));
        OutputStream stream = clob.setAsciiStream(1);
        stream.write(new byte[] {65, 66, 67, 68});
        assertEquals("ABCD is a Test Mock Clob", clob.getSubString(1, 24));
        Writer writer = clob.setCharacterStream(5);
        writer.write("FFG");
        stream.write(69);
        assertEquals("ABCDEFG a Test Mock Clob", clob.getSubString(1, 24));
        writer = clob.setCharacterStream(1);
        writer.write("This is a Test ClobThis is a Test Clob");
        assertEquals("This is a Test ClobThis is a Test Clob", clob.getSubString(1, 38));
    }
    
    public void testEquals() throws Exception
    {
        MockClob clob1 = new MockClob("This is a Test Clob");
        assertFalse(clob1.equals(null));
        assertTrue(clob1.equals(clob1));
        MockClob clob2 = new MockClob("This is another Test Clob");
        assertFalse(clob1.equals(clob2));
        assertFalse(clob2.equals(clob1));
        clob2 = new MockClob("This is a Test Clob");
        assertTrue(clob1.equals(clob2));
        assertTrue(clob2.equals(clob1));
        assertEquals(clob1.hashCode(), clob2.hashCode());
    }
    
    public void testClone() throws Exception
    {
        MockClob cloneClob = (MockClob)clob.clone();
        clob.setString(1, "Test");
        assertEquals("Test is a Test Clob", clob.getSubString(1, 19));
        assertEquals("This is a Test Clob", cloneClob.getSubString(1, 19));
    }
}
