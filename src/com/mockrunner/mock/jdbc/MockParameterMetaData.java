package com.mockrunner.mock.jdbc;

import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Mock implementation of <code>ParameterMetaData</code>.
 */
public class MockParameterMetaData implements ParameterMetaData
{
    private int parameterCount;
    
    public MockParameterMetaData()
    {
        parameterCount = 0;
    }
    
    public void setupParameterCount(int count)
    {
        parameterCount = count;
    }
    
    public int getParameterCount() throws SQLException
    {
        return parameterCount;
    }

    public int getParameterMode(int param) throws SQLException
    {
        return ParameterMetaData.parameterModeUnknown;
    }

    public int getParameterType(int param) throws SQLException
    {
        return Types.OTHER;
    }

    public int getPrecision(int param) throws SQLException
    {
        return 0;
    }

    public int getScale(int param) throws SQLException
    {
        return 0;
    }

    public int isNullable(int param) throws SQLException
    {
        return ParameterMetaData.parameterNullable;
    }

    public boolean isSigned(int param) throws SQLException
    {
        return false;
    }

    public String getParameterClassName(int param) throws SQLException
    {
        return Object.class.getName();
    }

    public String getParameterTypeName(int param) throws SQLException
    {
        return "";
    }
}
