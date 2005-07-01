package com.mockrunner.test.jdbc;

import java.util.Arrays;

import com.mockrunner.mock.jdbc.MockArray;
import com.mockrunner.mock.jdbc.MockResultSet;

import junit.framework.TestCase;

public class MockArrayTest extends TestCase
{
    private MockArray stringArray;
    private MockArray byteArray;

    protected void setUp() throws Exception
    {
        super.setUp();
        stringArray = new MockArray(new String[] {"This", "is", "a", "test", "array"});
        byteArray = new MockArray(new byte[]{1, 2, 3, 4, 5, 6, 7}); 
    }

    public void testGetArray() throws Exception
    {
        Object array = stringArray.getArray();
        assertTrue(array instanceof String[]);
        assertTrue(Arrays.equals((String[])array, new String[] {"This", "is", "a", "test", "array"}));
        array = stringArray.getArray(2, 3);
        assertTrue(array instanceof String[]);
        assertTrue(Arrays.equals((String[])array, new String[] {"is", "a", "test"}));
        array = byteArray.getArray(1, 7);
        assertTrue(array instanceof byte[]);
        assertTrue(Arrays.equals((byte[])array, new byte[]{1, 2, 3, 4, 5, 6, 7}));
        array = byteArray.getArray(1, 1);
        assertTrue(array instanceof byte[]);
        assertTrue(Arrays.equals((byte[])array, new byte[]{1}));
    }
    
    public void testGetResultSet() throws Exception
    {
        MockResultSet resultSet = (MockResultSet)stringArray.getResultSet();
        assertEquals(5, resultSet.getRowCount());
        resultSet.next();
        assertEquals(1, resultSet.getInt(1));
        assertEquals("This", resultSet.getString(2));
        resultSet.next();
        assertEquals(2, resultSet.getInt(1));
        assertEquals("is", resultSet.getString(2));
        resultSet.next();
        assertEquals(3, resultSet.getInt(1));
        assertEquals("a", resultSet.getString(2));
        resultSet.next();
        assertEquals(4, resultSet.getInt(1));
        assertEquals("test", resultSet.getString(2));
        resultSet.next();
        assertEquals(5, resultSet.getInt(1));
        assertEquals("array", resultSet.getString(2));
        resultSet = (MockResultSet)byteArray.getResultSet(3, 2);
        assertEquals(2, resultSet.getRowCount());
        resultSet.next();
        assertEquals(1, resultSet.getInt(1));
        assertEquals(3, resultSet.getByte(2));
        resultSet.next();
        assertEquals(2, resultSet.getInt(1));
        assertEquals(4, resultSet.getByte(2));
    }
    
    public void testEquals() throws Exception
    {
        MockArray stringArray1 = new MockArray(new String[] {"1", "2"});
        assertFalse(stringArray1.equals(null));
        assertTrue(stringArray1.equals(stringArray1));
        MockArray stringArray2 = new MockArray(new String[] {"1", "2", "3"});
        assertFalse(stringArray1.equals(stringArray2));
        assertFalse(stringArray2.equals(stringArray1));
        stringArray2 = new MockArray(new String[] {"1", "2"});
        assertTrue(stringArray1.equals(stringArray2));
        assertTrue(stringArray2.equals(stringArray1));
        assertEquals(stringArray1.hashCode(), stringArray2.hashCode());
        stringArray2.setBaseTypeName(null);
        assertFalse(stringArray1.equals(stringArray2));
        assertFalse(stringArray2.equals(stringArray1));
        stringArray1.setBaseTypeName(null);
        assertTrue(stringArray1.equals(stringArray2));
        assertTrue(stringArray2.equals(stringArray1));
        assertEquals(stringArray1.hashCode(), stringArray2.hashCode());
        stringArray1.setBaseTypeName("xyz");
        stringArray2.setBaseTypeName("xyz1");
        assertFalse(stringArray1.equals(stringArray2));
        assertFalse(stringArray2.equals(stringArray1));
        stringArray2.setBaseTypeName("xyz");
        assertTrue(stringArray1.equals(stringArray2));
        assertTrue(stringArray2.equals(stringArray1));
        assertEquals(stringArray1.hashCode(), stringArray2.hashCode());
        stringArray1.setBaseType(5);
        assertFalse(stringArray1.equals(stringArray2));
        assertFalse(stringArray2.equals(stringArray1));
        stringArray2.setBaseType(5);
        assertTrue(stringArray1.equals(stringArray2));
        assertTrue(stringArray2.equals(stringArray1));
        assertEquals(stringArray1.hashCode(), stringArray2.hashCode());
    }
    
    public void testClone() throws Exception
    {
        MockArray copy = (MockArray)stringArray.clone();
        assertTrue(Arrays.equals((Object[])stringArray.getArray(), (Object[])copy.getArray()));
        String[] array = (String[])stringArray.getArray();
        array[0] = "Test";
        assertFalse(Arrays.equals((Object[])stringArray.getArray(), (Object[])copy.getArray()));
    }
}
