package com.mockrunner.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mockrunner.util.ArrayUtil;

import junit.framework.TestCase;

public class ArrayUtilTest extends TestCase
{
    public void testGetListFromByteArray()
    {
        byte[] testArray = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
        List list = ArrayUtil.getListFromByteArray(testArray);
        assertTrue(list.size() == 9);
        for(int ii = 0; ii < testArray.length; ii++)
        {
            byte nextByteFromList = ((Byte)list.get(ii)).byteValue();
            assertEquals(testArray[ii], nextByteFromList);
        }
    }
    
    public void testGetListFromObjectArray()
    {
        String[] testArray = new String[] {"abc", "def", "ghi"};
        List list = ArrayUtil.getListFromObjectArray(testArray);
        assertTrue(list.size() == 3);
        for(int ii = 0; ii < testArray.length; ii++)
        {
            Object nextObjectFromList = list.get(ii);
            assertEquals(testArray[ii], nextObjectFromList);
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
        byte[] data = ArrayUtil.getByteArrayFromList(list);
        assertTrue(data.length == 11);
        for(int ii = 0; ii < 9; ii++)
        {
            assertEquals(ii, data[ii]);
        }
        assertEquals(0, data[9]);
        assertEquals(0, data[10]);
        data = ArrayUtil.getByteArrayFromList(list, 5);
        assertTrue(data.length == 6);
        assertEquals(5, data[0]);
        assertEquals(6, data[1]);
        assertEquals(7, data[2]);
        assertEquals(8, data[3]);
        assertEquals(0, data[4]);
        assertEquals(0, data[5]);
        data = ArrayUtil.getByteArrayFromList(list, 5, 4);
        assertTrue(data.length == 4);
        assertEquals(5, data[0]);
        assertEquals(6, data[1]);
        assertEquals(7, data[2]);
        assertEquals(8, data[3]);
        data = ArrayUtil.getByteArrayFromList(list, 0, 0);
        assertTrue(data.length == 0);
        data = ArrayUtil.getByteArrayFromList(list, 0, 1);
        assertTrue(data.length == 1);
        assertEquals(0, data[0]);
        data = ArrayUtil.getByteArrayFromList(new ArrayList(), 0, 0);
        assertNotNull(data);
        assertTrue(data.length == 0);
    }
    
    public void testAddBytesToArrayList()
    {
        ArrayList list = new ArrayList();
        byte[] testArray = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
        ArrayUtil.addBytesToList(testArray, list, 0);
        assertTrue(list.size() == 9);
        for(int ii = 0; ii < testArray.length; ii++)
        {
            byte nextByteFromList = ((Byte)list.get(ii)).byteValue();
            assertEquals(testArray[ii], nextByteFromList);
        }
        ArrayUtil.addBytesToList(testArray, 1, 3, list, 9);
        assertTrue(list.size() == 12);
        for(int ii = 0; ii < testArray.length; ii++)
        {
            byte nextByteFromList = ((Byte)list.get(ii)).byteValue();
            assertEquals(testArray[ii], nextByteFromList);
        }
        assertEquals(1, ((Byte)list.get(9)).byteValue());
        assertEquals(2, ((Byte)list.get(10)).byteValue());
        assertEquals(3, ((Byte)list.get(11)).byteValue());
        ArrayUtil.addBytesToList(testArray, 1, 3, list, 0);
        assertTrue(list.size() == 12);
        assertEquals(1, ((Byte)list.get(0)).byteValue());
        assertEquals(2, ((Byte)list.get(1)).byteValue());
        assertEquals(3, ((Byte)list.get(2)).byteValue());
        assertEquals(3, ((Byte)list.get(3)).byteValue());
        ArrayUtil.addBytesToList(testArray, list, 100);
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
        List truncatedList = ArrayUtil.truncateList(list, 50);
        assertTrue(truncatedList.size() == 50);
        for(int ii = 0; ii < 50; ii++)
        {
            assertEquals("Test" + ii, truncatedList.get(ii));
        }
    }
    
    public void testTruncateArray()
    {
        String[] stringArray = new String[] {"This", "is", "an", "array", "of", "strings"};
        stringArray = (String[])ArrayUtil.truncateArray(stringArray, 3);
        assertTrue(stringArray.length == 3);
        assertEquals("This", stringArray[0]);
        assertEquals("is", stringArray[1]);
        assertEquals("an", stringArray[2]);
        boolean[] booleanArray = new boolean[] {true, true, false, true, false, false};
        booleanArray = (boolean[])ArrayUtil.truncateArray(booleanArray, 2, 3);
        assertTrue(booleanArray.length == 3);
        assertEquals(false, booleanArray[0]);
        assertEquals(true, booleanArray[1]);
        assertEquals(false, booleanArray[2]);
    }
    
    public void testContains()
    {
        byte[] testArray = new byte[] {1, 1, 5, 3, 4, 5, 7, 7, 8, 3, 2};
        assertEquals(-1, ArrayUtil.contains(testArray, new byte[] {0}));
        assertEquals(-1, ArrayUtil.contains(testArray, new byte[] {0, 2}));
        assertEquals(0, ArrayUtil.contains(testArray, new byte[] {1}));
        assertEquals(1, ArrayUtil.contains(testArray, new byte[] {1, 5}));
        assertEquals(6, ArrayUtil.contains(testArray, new byte[] {7, 7, 8}));
        assertEquals(0, ArrayUtil.contains(testArray, new byte[] {1, 1, 5, 3, 4, 5, 7, 7, 8, 3}));
        assertEquals(0, ArrayUtil.contains(testArray, testArray));
        assertEquals(-1, ArrayUtil.contains(testArray, new byte[] {1, 1, 5, 3, 4, 5, 7, 7, 8, 3, 0}));
        assertEquals(10, ArrayUtil.contains(testArray, new byte[] {2}));
        assertEquals(2, ArrayUtil.contains(testArray, new byte[] {5, 3}));
        assertEquals(0, ArrayUtil.contains(testArray, new byte[] {}));
        
        assertEquals(2, ArrayUtil.contains(testArray, new byte[] {5, 3}), 1);
        assertEquals(-1, ArrayUtil.contains(testArray, new byte[] {5, 3}), 5);
        assertEquals(-1, ArrayUtil.contains(testArray, testArray), 1);
        assertEquals(1, ArrayUtil.contains(testArray, new byte[] {1}), 1);
        assertEquals(6, ArrayUtil.contains(testArray, new byte[] {7, 7, 8}), 6);
    }
    
    public void testConvertToObjectArray()
    {
        int[] intArray = new int[] {1, 2, 3};
        Object[] integerWrappedArray = ArrayUtil.convertToObjectArray(intArray);
        assertTrue(integerWrappedArray instanceof Integer[]);
        assertEquals(new Integer(1), integerWrappedArray[0]);
        assertEquals(new Integer(2), integerWrappedArray[1]);
        assertEquals(new Integer(3), integerWrappedArray[2]);
        double[] doubleArray = new double[] {1.3};
        Object[] doubleWrappedArray = ArrayUtil.convertToObjectArray(doubleArray);
        assertTrue(doubleWrappedArray instanceof Double[]);
        assertEquals(new Double(1.3), doubleWrappedArray[0]);
        String[] stringArray = new String[] {"This", "is", "an", "array", "of", "strings"};
        Object[] stringTargetArray = ArrayUtil.convertToObjectArray(stringArray);
        assertTrue(stringArray == stringTargetArray);
        boolean[] booleanArray = new boolean[] {true, true, false};
        Object[] booleanWrappedArray = ArrayUtil.convertToObjectArray(booleanArray);
        assertTrue(booleanWrappedArray instanceof Boolean[]);
        assertEquals(new Boolean(true), booleanWrappedArray[0]);
        assertEquals(new Boolean(true), booleanWrappedArray[1]);
        assertEquals(new Boolean(false), booleanWrappedArray[2]);
    }
    
    public void testConvertToArray()
    {
        int[] intArray = new int[] {1, 2, 3};
        Object array = ArrayUtil.convertToArray(intArray);
        assertTrue(intArray == array);
        String[] stringArray = new String[] {"This", "is", "an", "array", "of", "strings"};
        array = ArrayUtil.convertToArray(stringArray);
        assertTrue(stringArray == array);
        array = ArrayUtil.convertToArray("Test");
        assertTrue(array instanceof String[]);
        assertTrue(((String[])array).length == 1);
        assertEquals("Test", ((String[])array)[0]);
    }
    
    public void testCopyArray()
    {
        byte[] byteArray = new byte[] {1, 2, 3, 4, 5};
        byte[] copyByteArray = (byte[])ArrayUtil.copyArray(byteArray);
        assertFalse(byteArray == copyByteArray);
        assertTrue(Arrays.equals(byteArray, copyByteArray));
        String testWrong = (String)ArrayUtil.copyArray("testWrong");
        assertEquals(testWrong, "testWrong");
        String[] stringArray = new String[] {"This", "is", "an", "array", "of", "strings"};
        String[] copyStringArray = (String[])ArrayUtil.copyArray(stringArray);
        assertFalse(stringArray == copyStringArray);
        assertTrue(Arrays.equals(stringArray, copyStringArray));
        Object myObject = new Object();
        Object[] myArray = new Object[] {myObject};
        Object[] myCopy = (Object[])ArrayUtil.copyArray(myArray);
        assertFalse(myArray == myCopy);
        assertTrue(myArray[0] == myCopy[0]);
    }
}
