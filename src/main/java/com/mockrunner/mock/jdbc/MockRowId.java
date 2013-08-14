package com.mockrunner.mock.jdbc;

import java.sql.RowId;
import java.util.Arrays;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of <code>RowId</code>.
 */
public class MockRowId implements RowId, Cloneable
{
    private byte[] rowIdData;
    
    public MockRowId(byte[] data)
    {
        rowIdData = (byte[])data.clone();
    }
    
    public byte[] getBytes()
    {
        return rowIdData;
    }

    public boolean equals(Object otherObject)
    {
        if(null == otherObject) return false;
        if(!otherObject.getClass().equals(this.getClass())) return false;
        MockRowId otherRowId = (MockRowId)otherObject;
        return Arrays.equals(rowIdData, otherRowId.getBytes());
    }

    public int hashCode()
    {
        int value = 17;
        for(int ii = 0; ii < rowIdData.length; ii++)
        {
            value = (31 * value) + rowIdData[ii];
        }
        return value;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getClass().getName() + ": [");
        for(int ii = 0; ii < rowIdData.length; ii++)
        {
            buffer.append(rowIdData[ii]);
            if(ii < rowIdData.length - 1)
            {
                buffer.append(", ");
            }
        }
        buffer.append("]");
        return buffer.toString();
    }

    public Object clone()
    {
        try
        {
            MockRowId copy = (MockRowId)super.clone();
            copy.rowIdData = (byte[])rowIdData.clone();
            return copy;
        } 
        catch(CloneNotSupportedException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
}
