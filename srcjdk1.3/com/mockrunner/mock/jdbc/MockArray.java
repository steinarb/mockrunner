package com.mockrunner.mock.jdbc;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mockrunner.util.common.ArrayUtil;

/**
 * Mock implementation of <code>Array</code>.
 */
public class MockArray implements Array, Cloneable
{
    private final static Log log = LogFactory.getLog(MockArray.class);
    private String sqlTypeName = "";
    private int baseType = 0;
    private Object array;
    
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
        return baseType;
    }
    
    public String getBaseTypeName() throws SQLException
    {
        return sqlTypeName;
    }

    public Object getArray() throws SQLException
    {
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
        Integer[] firstColumn = new Integer[count];
        for(int ii = 0; ii < count; ii++)
        {
            firstColumn[ii] = new Integer(ii + 1);
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

    public boolean equals(Object obj)
    {
        if(null == obj) return false;
        if(!obj.getClass().equals(this.getClass())) return false;
        MockArray other = (MockArray)obj;
        if(baseType != other.baseType) return false;
        if(null == sqlTypeName && null != other.sqlTypeName) return false;
        if(null != sqlTypeName && !sqlTypeName.equals(other.sqlTypeName)) return false;
        return ArrayUtil.areArraysEqual(array, other.array);
    }

    public int hashCode()
    {
        int hashCode = ArrayUtil.computeHashCode(array);
        hashCode += 31 * baseType;
        if(null != sqlTypeName) hashCode += 31 * sqlTypeName.hashCode();
        return hashCode;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Array data: ");
        Object[] arrayData = ArrayUtil.convertToObjectArray(array);
        for(int ii = 0; ii < arrayData.length; ii++)
        {
            buffer.append("[" + arrayData[ii].toString() + "] ");
        }
        return buffer.toString();
    }
    
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
            log.error(exc.getMessage(), exc);
        }
        return null;
    }
}
