package com.mockrunner.util;

import java.util.ArrayList;
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
}
