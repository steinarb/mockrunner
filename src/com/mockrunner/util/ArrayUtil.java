package com.mockrunner.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class for arrays
 */
public class ArrayUtil
{
    /**
     * Returns a <code>List</code> containing the bytes from the
     * specified array as <code>Byte</code> objects.
     * @param data the byte data
     * @return the <code>List</code> with the <code>Byte</code> objects
     */
    public static List getListFromByteArray(byte[] data)
    {
        ArrayList list = new ArrayList(data.length);
        for(int ii = 0; ii < data.length; ii++)
        {
            list.add(new Byte(data[ii]));
        }
        return list;
    }
    
    /**
     * Returns a <code>List</code> containing the objects from the
     * specified array.
     * @param data the data
     * @return the <code>List</code> with the objects
     */
    public static List getListFromObjectArray(Object[] data)
    {
        ArrayList list = new ArrayList(data.length);
        for(int ii = 0; ii < data.length; ii++)
        {
            list.add(data[ii]);
        }
        return list;
    }
    
    /**
     * Returns a byte array containing the bytes from the <code>List</code>.
     * The <code>List</code> must contain <code>Byte</code> objects.
     * <code>null</code> entries in the <code>List</code> are
     * allowed, the resulting byte will be 0.
     * @param data the <code>List</code>
     * @return the resulting byte array
     */
    public static byte[] getByteArrayFromList(List data)
    {
        return getByteArrayFromList(data, 0);
    }
    
    /**
     * Returns a byte array containing the bytes from the <code>List</code>.
     * The <code>List</code> must contain <code>Byte</code> objects.
     * <code>null</code> entries in the <code>List</code> are
     * allowed, the resulting byte will be 0.
     * @param data the <code>List</code>
     * @param index the index at which to start
     * @return the resulting byte array
     */
    public static byte[] getByteArrayFromList(List data, int index)
    {
        return getByteArrayFromList(data, index, data.size() - index);
    }
    
    /**
     * Returns a byte array containing the bytes from the <code>List</code>.
     * The <code>List</code> must contain <code>Byte</code> objects.
     * <code>null</code> entries in the <code>List</code> are
     * allowed, the resulting byte will be 0.
     * @param data the <code>List</code>
     * @param index the index at which to start
     * @param len the number of bytes
     * @return the resulting byte array
     */
    public static byte[] getByteArrayFromList(List data, int index, int len)
    {
        if(data.size() == 0) return new byte[0];
        if(index >= data.size())
        {
            throw new IndexOutOfBoundsException("Position " + index + " invalid in List of size " + data.size());
        }
        byte[] byteData = new byte[len];
        for(int ii = index; ii < data.size() && ii < index + len; ii++)
        {
            Byte nextValue = (Byte)data.get(ii);
            if(null != nextValue)
            {
                byteData[ii - index] = nextValue.byteValue();
            }
        }
        return byteData;
    }
    
    /**
     * Copies the bytes from the specified array to the specified
     * <code>List</code> as <code>Byte</code> objects starting
     * at the specified index. Grows the list if necessary.
     * <i>index</i> must be a valid index in the list.
     * @param data the byte data
     * @param list the <code>List</code>
     * @param index the index at which to start copying
     */
    public static void addBytesToList(byte[] data, List list, int index)
    {
        addBytesToList(data, 0, data.length, list, index);
    }
    
    /**
     * Copies the bytes from the specified array to the specified
     * <code>List</code> as <code>Byte</code> objects starting
     * at the specified index. Grows the list if necessary.
     * <i>index</i> must be a valid index in the list.
     * @param data the byte data
     * @param offset the offset into the byte array at which to start
     * @param len the number of bytes to copy
     * @param list the <code>List</code>
     * @param index the index at which to start copying
     */
    public static void addBytesToList(byte[] data, int offset, int len, List list, int index)
    {
        int bytesToIncrease = index + len - list.size();
        if(bytesToIncrease > 0)
        {
            for(int ii = 0; ii < bytesToIncrease; ii++)
            {
                list.add(null);
            }
        }
        for(int ii = index; ii < index + len; ii++)
        {
            list.set(ii, new Byte(data[offset + ii - index]));
        }
    }
    
    /**
     * Returns a truncated version of the specified <code>List</code>.
     * @param list the <code>List</code>
     * @param len the truncate length
     * @return the truncated <code>List</code>
     */
    public static List truncateList(List list, int len)
    {
        if(len >= list.size()) return list;
        ArrayList newList = new ArrayList(len);
        for(int ii = 0; ii < len; ii++)
        {
            newList.add(list.get(ii));
        }
        return newList;
    }
    
    /**
     * Returns a truncated copy of <i>sourceArray</i>. <i>len</i>
     * entries are copied.
     * @param sourceArray the source array
     * @param len the truncate length
     * @return the truncated array
     * @throws IllegalArgumentException if the specified object
     *         is not an array (either of reference or primitive
     *         component type)
     */
    public static Object truncateArray(Object sourceArray, int len)
    {
        return truncateArray(sourceArray, 0, len);
    }
    
    /**
     * Returns a truncated copy of <i>sourceArray</i>. <i>len</i>
     * entries are copied starting at the specified index.
     * @param sourceArray the source array
     * @param index the start index
     * @param len the truncate length
     * @return the truncated array
     * @throws IllegalArgumentException if the specified object
     *         is not an array (either of reference or primitive
     *         component type)
     */
    public static Object truncateArray(Object sourceArray, int index, int len)
    {
        if(!sourceArray.getClass().isArray())
        {
            throw new IllegalArgumentException("sourceArray must be an array");
        }
        Object targetArray = Array.newInstance(sourceArray.getClass().getComponentType(), len);
        System.arraycopy(sourceArray, index, targetArray, 0, len);
        return targetArray;
    }
    
    /**
     * Returns an object array by wrapping primitive types. If the 
     * specified array is of primitive component type, an <code>Object[]</code>
     * with the corresponding wrapper component type is returned.
     * If the specified array is already an object array, the instance is
     * returned unchanged.
     * @param sourceArray the array
     * @return the corresponding object array
     * @throws IllegalArgumentException if the specified object
     *         is not an array (either of reference or primitive
     *         component type)
     */
    public static Object[] convertToObjectArray(Object sourceArray)
    {
        if(!sourceArray.getClass().isArray())
        {
            throw new IllegalArgumentException("sourceArray must be an array");
        }
        Class componentType = sourceArray.getClass().getComponentType();
        if(!componentType.isPrimitive())
        {
            return (Object[])sourceArray;
        }
        if(componentType == Boolean.TYPE)
        {
            componentType = Boolean.class;
        }
        else if(componentType == Byte.TYPE)
        {
            componentType = Byte.class;
        }
        else if(componentType == Character.TYPE)
        {
            componentType = Character.class;
        }
        else if(componentType == Short.TYPE)
        {
            componentType = Short.class;
        }
        else if(componentType == Integer.TYPE)
        {
            componentType = Integer.class;
        }
        else if(componentType == Long.TYPE)
        {
            componentType = Long.class;
        }
        else if(componentType == Float.TYPE)
        {
            componentType = Float.class;
        }
        else if(componentType == Double.TYPE)
        {
            componentType = Double.class;
        }
        int length = Array.getLength(sourceArray);
        Object[] targetArray = (Object[])Array.newInstance(componentType, length);
        for(int ii = 0; ii < length; ii++)
        {
            targetArray[ii] = Array.get(sourceArray, ii);
        }
        return targetArray;
    }
    
    /**
     * Creates an array with a single object as component.
     * If the specified object is an array, it will be returned
     * unchanged. Otherwise an array with the specified object
     * as the single element will be returned.
     * @param object the object
     * @return the corresponding array
     */
    public static Object convertToArray(Object object)
    {
        if(object.getClass().isArray()) return object;
        Object array = Array.newInstance(object.getClass(), 1);
        Array.set(array, 0, object);
        return array;
    }
    
    /**
     * Returns the index of the first occurence of the
     * array <i>bytes</i> in the array <i>source</i>.
     * @param source the array in which to search
     * @param bytes the array to search
     * @return the index of the first occurence, resp.
     *         -1, if <i>source</i> does not contain <i>bytes</i>
     */
    public static int contains(byte[] source, byte[] bytes)
    {
        return contains(source, bytes, 0);
    }
    
    /**
     * Returns the index of the first occurence of the
     * array <i>bytes</i> in the array <i>source</i>.
     * @param source the array in which to search
     * @param bytes the array to search
     * @param the index at which to begin the search
     * @return the index of the first occurence, resp.
     *         -1, if <i>source</i> does not contain <i>bytes</i>
     */
    public static int contains(byte[] source, byte[] bytes, int index)
    {
        if(index + bytes.length > source.length) return -1;
        for(int ii = index; ii <= source.length - bytes.length; ii++)
        {
            int yy = 0; 
            while(yy < bytes.length && bytes[yy] == source[ii + yy]) yy++;
            if(yy == bytes.length) return ii;
        }
        return -1;
    }
}
