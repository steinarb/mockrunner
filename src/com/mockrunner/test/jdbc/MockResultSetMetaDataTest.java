package com.mockrunner.test.jdbc;

import com.mockrunner.mock.jdbc.MockResultSetMetaData;

import junit.framework.TestCase;

public class MockResultSetMetaDataTest extends TestCase
{
    private MockResultSetMetaData metaData;

    protected void setUp() throws Exception
    {
        metaData = new MockResultSetMetaData();
    }

    protected void tearDown() throws Exception
    {
        metaData = null;
    }
    
    public void testSetAndGet() throws Exception
    {
        metaData.setColumnClassName(1, "ClassName");
        metaData.setColumnCount(4);
        metaData.setColumnTypeName(2, "TypeName");
        metaData.setColumnDisplaySize(3, 7);
        metaData.setSearchable(2, false);
        assertEquals("ClassName", metaData.getColumnClassName(1));
        assertEquals(Object.class.getName(), metaData.getColumnClassName(2));
        assertEquals(4, metaData.getColumnCount());
        assertEquals("TypeName", metaData.getColumnTypeName(2));
        assertEquals("", metaData.getColumnTypeName(1));
        assertEquals(7, metaData.getColumnDisplaySize(3));
        assertEquals(4, metaData.getColumnDisplaySize(5));
        assertFalse(metaData.isSearchable(2));
        assertTrue(metaData.isSearchable(1));
    }
}
