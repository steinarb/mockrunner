package com.mockrunner.test;

import java.util.ArrayList;
import java.util.List;

import com.mockrunner.util.CollectionUtil;

import junit.framework.TestCase;

public class CollectionUtilTest extends TestCase
{
    public void testGetListFromByteArray()
    {
        byte[] testArray = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
        List list = CollectionUtil.getListFromByteArray(testArray);
        assertTrue(list.size() == 9);
        for(int ii = 0; ii < testArray.length; ii++)
        {
            byte nextByteFromList = ((Byte)list.get(ii)).byteValue();
            assertEquals(testArray[ii], nextByteFromList);
        }
    }
    
    public void testGetByteArrayFromList()
    {
        ArrayList list = new ArrayList();
        for(int ii = 0; ii < 9; ii++)
        {
            list.add(new Byte((byte)ii));
        }
        list.add(null);
        list.add(new Byte((byte)0));
        byte[] data = CollectionUtil.getByteArrayFromList(list);
        assertTrue(data.length == 11);
        for(int ii = 0; ii < 9; ii++)
        {
            assertEquals(ii, data[ii]);
        }
        assertEquals(0, data[9]);
        assertEquals(0, data[10]);
        data = CollectionUtil.getByteArrayFromList(list, 5);
        assertTrue(data.length == 6);
        assertEquals(5, data[0]);
        assertEquals(6, data[1]);
        assertEquals(7, data[2]);
        assertEquals(8, data[3]);
        assertEquals(0, data[4]);
        assertEquals(0, data[5]);
        data = CollectionUtil.getByteArrayFromList(list, 5, 4);
        assertTrue(data.length == 4);
        assertEquals(5, data[0]);
        assertEquals(6, data[1]);
        assertEquals(7, data[2]);
        assertEquals(8, data[3]);
        data = CollectionUtil.getByteArrayFromList(list, 0, 0);
        assertTrue(data.length == 0);
        data = CollectionUtil.getByteArrayFromList(list, 0, 1);
        assertTrue(data.length == 1);
        assertEquals(0, data[0]);
    }
    
    public void testAddBytesToArrayList()
    {
        ArrayList list = new ArrayList();
        byte[] testArray = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
        CollectionUtil.addBytesToList(testArray, list, 0);
        assertTrue(list.size() == 9);
        for(int ii = 0; ii < testArray.length; ii++)
        {
            byte nextByteFromList = ((Byte)list.get(ii)).byteValue();
            assertEquals(testArray[ii], nextByteFromList);
        }
        CollectionUtil.addBytesToList(testArray, 1, 3, list, 9);
        assertTrue(list.size() == 12);
        for(int ii = 0; ii < testArray.length; ii++)
        {
            byte nextByteFromList = ((Byte)list.get(ii)).byteValue();
            assertEquals(testArray[ii], nextByteFromList);
        }
        assertEquals(1, ((Byte)list.get(9)).byteValue());
        assertEquals(2, ((Byte)list.get(10)).byteValue());
        assertEquals(3, ((Byte)list.get(11)).byteValue());
        CollectionUtil.addBytesToList(testArray, 1, 3, list, 0);
        assertTrue(list.size() == 12);
        assertEquals(1, ((Byte)list.get(0)).byteValue());
        assertEquals(2, ((Byte)list.get(1)).byteValue());
        assertEquals(3, ((Byte)list.get(2)).byteValue());
        assertEquals(3, ((Byte)list.get(3)).byteValue());
        CollectionUtil.addBytesToList(testArray, list, 100);
        assertTrue(list.size() == 109);
        for(int ii = 12; ii < 100; ii++)
        {
            assertNull(list.get(ii));
        }
        for(int ii = 100; ii < 109; ii++)
        {
            byte nextByteFromList = ((Byte)list.get(ii)).byteValue();
            assertEquals(testArray[ii - 100], nextByteFromList);
        }
    }
    
    public void testTruncateList()
    {
        ArrayList list = new ArrayList();
        for(int ii = 0; ii < 100; ii++)
        {
            list.add("Test" + ii);
        }
        List truncatedList = CollectionUtil.truncateList(list, 50);
        assertTrue(truncatedList.size() == 50);
        for(int ii = 0; ii < 50; ii++)
        {
            assertEquals("Test" + ii, truncatedList.get(ii));
        }
    }
    
    public void testContains()
    {
        byte[] testArray = new byte[] {1, 1, 5, 3, 4, 5, 7, 7, 8, 3, 2};
        assertEquals(-1, CollectionUtil.contains(testArray, new byte[] {0}));
        assertEquals(-1, CollectionUtil.contains(testArray, new byte[] {0, 2}));
        assertEquals(0, CollectionUtil.contains(testArray, new byte[] {1}));
        assertEquals(1, CollectionUtil.contains(testArray, new byte[] {1, 5}));
        assertEquals(6, CollectionUtil.contains(testArray, new byte[] {7, 7, 8}));
        assertEquals(0, CollectionUtil.contains(testArray, new byte[] {1, 1, 5, 3, 4, 5, 7, 7, 8, 3}));
        assertEquals(0, CollectionUtil.contains(testArray, testArray));
        assertEquals(-1, CollectionUtil.contains(testArray, new byte[] {1, 1, 5, 3, 4, 5, 7, 7, 8, 3, 0}));
        assertEquals(10, CollectionUtil.contains(testArray, new byte[] {2}));
        assertEquals(2, CollectionUtil.contains(testArray, new byte[] {5, 3}));
        assertEquals(0, CollectionUtil.contains(testArray, new byte[] {}));
        
        assertEquals(2, CollectionUtil.contains(testArray, new byte[] {5, 3}), 1);
        assertEquals(-1, CollectionUtil.contains(testArray, new byte[] {5, 3}), 5);
        assertEquals(-1, CollectionUtil.contains(testArray, testArray), 1);
        assertEquals(1, CollectionUtil.contains(testArray, new byte[] {1}), 1);
        assertEquals(6, CollectionUtil.contains(testArray, new byte[] {7, 7, 8}), 6);
    }
}
