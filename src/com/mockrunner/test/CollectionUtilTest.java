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
}
