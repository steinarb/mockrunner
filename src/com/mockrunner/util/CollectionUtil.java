package com.mockrunner.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Util class for collections
 */
public class CollectionUtil
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
    
    public static byte[] getByteArrayFromList(List data, int index, int len)
    {
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
