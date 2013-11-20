package com.mockrunner.test.jdbc;

import java.sql.ParameterMetaData;

import junit.framework.TestCase;

import com.mockrunner.mock.jdbc.MockParameterMetaData;

public class MockParameterMetaDataTest extends TestCase
{
    private MockParameterMetaData metaData;

    protected void setUp() throws Exception
    {
        metaData = new MockParameterMetaData();
    }

    protected void tearDown() throws Exception
    {
        metaData = null;
    }
    
    public void testSetAndGet() throws Exception
    {
        metaData.setParameterClassName(1, "ClassName");
        metaData.setPrecision(2, 3);
        metaData.setSigned(1, true);
        metaData.setParameterCount(5);
        metaData.setParameterMode(1, ParameterMetaData.parameterModeIn);
        assertEquals("ClassName", metaData.getParameterClassName(1));
        assertEquals(Object.class.getName(), metaData.getParameterClassName(2));
        assertEquals(3, metaData.getPrecision(2));
        assertEquals(0, metaData.getPrecision(1));
        assertTrue(metaData.isSigned(1));
        assertFalse(metaData.isSigned(2));
        assertEquals(5, metaData.getParameterCount());
        assertEquals(ParameterMetaData.parameterModeIn, metaData.getParameterMode(1));
        assertEquals(ParameterMetaData.parameterModeUnknown, metaData.getParameterMode(2));
    }
}
