package com.mockrunner.mock.jdbc;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.mockrunner.util.ArrayUtil;

/**
 * Mock implementation of a JDBC 3.0 <code>Array</code>.
 */
public class MockArray implements Array
{
    private Object array;
    
    public MockArray(Object array)
    {
        this.array = ArrayUtil.convertToArray(array);
    }
    
    public int getBaseType() throws SQLException
    {
        return 0;
    }
    
    public String getBaseTypeName() throws SQLException
    {
        return "";
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
}
