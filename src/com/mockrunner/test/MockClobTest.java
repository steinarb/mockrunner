package com.mockrunner.test;

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
    }
}
