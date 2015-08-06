package com.mockrunner.mock.jdbc;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.util.common.ArrayUtil;

/**
 * Mock implementation of <code>Array</code>.
 */
public class MockArray implements Array, Cloneable
{
    private String sqlTypeName = "";
    private int baseType = 0;
    private Object array;
    private boolean wasFreeCalled = false;
    
    public MockArray(Object array)
    {
        this.array = ArrayUtil.convertToArray(array);
    }
    
    /**
     * Sets the base type.
     * @param baseType the base type
     */
    public void setBaseType(int baseType)
    {
        this.baseType = baseType;
    }

    /**
     * Sets the base type name.
     * @param sqlTypeName the base type name
     */
    public void setBaseTypeName(String sqlTypeName)
    {
        this.sqlTypeName = sqlTypeName;
    }

    public int getBaseType() throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        return baseType;
    }
    
    public String getBaseTypeName() throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        return sqlTypeName;
    }

    public Object getArray() throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        return array;
    }
    
    public Object getArray(Map map) throws SQLException
    {
        return getArray();
    }

    public Object getArray(long index, int count) throws SQLException
    {
        return ArrayUtil.truncateArray(getArray(), (int)(index - 1), count);
    }
    
    public Object getArray(long index, int count, Map map) throws SQLException
    {
        return getArray(index, count);
    }

    public ResultSet getResultSet() throws SQLException
    {
        return getResultSet(1, java.lang.reflect.Array.getLength(array));
    }

    public ResultSet getResultSet(long index, int count) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        Integer[] firstColumn = new Integer[count];
        for(int ii = 0; ii < count; ii++)
        {
            firstColumn[ii] = ii + 1;
        }
        Object[] secondColumn = ArrayUtil.convertToObjectArray(array);
        secondColumn = (Object[])ArrayUtil.truncateArray(secondColumn, (int)(index - 1), count);
        MockResultSet resultSet = new MockResultSet(String.valueOf(hashCode()));
        resultSet.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_READ_ONLY);
        resultSet.addColumn(firstColumn);
        resultSet.addColumn(secondColumn);
        return resultSet;
    }

    public ResultSet getResultSet(long index, int count, Map map) throws SQLException
    {
        return getResultSet(index, count);
    }

    public ResultSet getResultSet(Map map) throws SQLException
    {
        return getResultSet();
    }
    
    public void free() throws SQLException
    {
        wasFreeCalled = true;
    }

    /**
     * Returns if {@link #free} has been called.
     * @return <code>true</code> if {@link #free} has been called,
     *         <code>false</code> otherwise
     */
    public boolean wasFreeCalled()
    {
        return wasFreeCalled;
    }
    
    public boolean equals(Object obj)
    {
        if(null == obj) return false;
        if(!obj.getClass().equals(this.getClass())) return false;
        MockArray other = (MockArray)obj;
        if(baseType != other.baseType) return false;
        if(null == sqlTypeName && null != other.sqlTypeName) return false;
        if(null != sqlTypeName && !sqlTypeName.equals(other.sqlTypeName)) return false;
        if(wasFreeCalled != other.wasFreeCalled()) return false;
        return ArrayUtil.areArraysEqual(array, other.array);
    }

    @Override
    public int hashCode()
    {
        int hashCode = ArrayUtil.computeHashCode(array);
        hashCode = (31 * hashCode) + baseType;
        if(null != sqlTypeName) hashCode = (31 * hashCode) + sqlTypeName.hashCode();
        hashCode = (31 * hashCode) + (wasFreeCalled ? 31 : 62);
        return hashCode;
    }

    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder("Array data: [");
        Object[] arrayData = ArrayUtil.convertToObjectArray(array);
        for(int ii = 0; ii < arrayData.length; ii++)
        {
            buffer.append(arrayData[ii]);
            if(ii < arrayData.length - 1)
            {
                buffer.append(", ");
            }
        }
        buffer.append("]");
        return buffer.toString();
    }
    
    @Override
    public Object clone()
    {
        try
        {
            MockArray copy = (MockArray)super.clone();
            copy.array = ArrayUtil.copyArray(array);
            return copy;
        }
        catch(CloneNotSupportedException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
}
