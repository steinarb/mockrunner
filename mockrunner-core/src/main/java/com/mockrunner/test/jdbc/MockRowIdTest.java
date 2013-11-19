package com.mockrunner.test.jdbc;

import java.util.Arrays;

import junit.framework.TestCase;

import com.mockrunner.mock.jdbc.MockRowId;

public class MockRowIdTest extends TestCase
{
    private MockRowId rowId;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        rowId = new MockRowId(new byte[] {1, 2, 3});
    }
    
    public void testGetBytes() throws Exception
    {
        assertTrue(Arrays.equals(rowId.getBytes(), new byte[] {1, 2, 3}));
        MockRowId emptyRowId = new MockRowId(new byte[0]);
        assertTrue(Arrays.equals(emptyRowId.getBytes(), new byte[0]));
    }
    
    public void testEquals() throws Exception
    {
        assertFalse(rowId.equals(null));
        assertTrue(rowId.equals(rowId));
        MockRowId rowId2 = new MockRowId(new byte[] {1, 2, 3, 4});
        assertFalse(rowId.equals(rowId2));
        assertFalse(rowId2.equals(rowId));
        rowId2 = new MockRowId(new byte[] {1, 2, 3});
        assertTrue(rowId.equals(rowId2));
        assertTrue(rowId2.equals(rowId));
        assertEquals(rowId.hashCode(), rowId2.hashCode());
    }
    
    public void testClone() throws Exception
    {
        MockRowId cloneRowId = (MockRowId)rowId.clone();
        assertTrue(Arrays.equals(rowId.getBytes(), cloneRowId.getBytes()));
        rowId.getBytes()[0] = 25;
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, cloneRowId.getBytes()));
        cloneRowId = (MockRowId)new MockRowId(new byte[0]).clone();
        assertTrue(Arrays.equals(new byte[0], cloneRowId.getBytes()));
    }
    
    public void testToString() throws Exception
    {
        MockRowId rowId = new MockRowId(new byte[0]);
        assertEquals(MockRowId.class.getName() + ": []", rowId.toString());
        rowId = new MockRowId(new byte[] {1});
        assertEquals(MockRowId.class.getName() + ": [1]", rowId.toString());
        rowId = new MockRowId(new byte[] {1, 2, 3});
        assertEquals(MockRowId.class.getName() + ": [1, 2, 3]", rowId.toString());
    }
}
