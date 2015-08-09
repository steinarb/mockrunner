package com.mockrunner.mock.jdbc;

import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * Mock implementation of <code>ParameterMetaData</code>.
 */
public class MockParameterMetaData implements ParameterMetaData
{
    private int parameterCount;
    private final Map<Integer, Integer> parameterModeMap;
    private final Map<Integer, Integer> parameterTypeMap;
    private final Map<Integer, Integer> precisionMap;
    private final Map<Integer, Integer> scaleMap;
    private final Map<Integer, Integer> isNullableMap;
    private final Map<Integer, Boolean> isSignedMap;
    private final Map<Integer, String> parameterClassNameMap;
    private final Map<Integer, String> parameterTypeNameMap;
    
    public MockParameterMetaData()
    {
        parameterCount = 0;
        parameterModeMap = new HashMap<Integer, Integer>();
        parameterTypeMap = new HashMap<Integer, Integer>();
        precisionMap = new HashMap<Integer, Integer>();
        scaleMap = new HashMap<Integer, Integer>();
        isNullableMap = new HashMap<Integer, Integer>();
        isSignedMap = new HashMap<Integer, Boolean>();
        parameterClassNameMap = new HashMap<Integer, String>();
        parameterTypeNameMap = new HashMap<Integer, String>();
    }
    
    public void setParameterCount(int count)
    {
        parameterCount = count;
    }
    
    public void setParameterMode(int param, int parameterMode)
    {
        parameterModeMap.put(param, parameterMode);
    }
    
    public void setParameterType(int param, int parameterType)
    {
        parameterTypeMap.put(param, parameterType);
    }
    
    public void setPrecision(int param, int precision)
    {
        precisionMap.put(param, precision);
    }
    
    public void setScale(int param, int scale)
    {
        scaleMap.put(param, scale);
    }
    
    public void setNullable(int param, int nullable)
    {
        isNullableMap.put(param, nullable);
    }
    
    public void setSigned(int param, boolean signed)
    {
        isSignedMap.put(param, signed);
    }
    
    public void setParameterClassName(int param, String parameterClassName)
    {
        parameterClassNameMap.put(param, parameterClassName);
    }
    
    public void setParameterTypeName(int param, String parameterTypeName)
    {
        parameterTypeNameMap.put(param, parameterTypeName);
    }
    
    public int getParameterCount() throws SQLException
    {
        return parameterCount;
    }

    public int getParameterMode(int param) throws SQLException
    {
        Integer parameterMode = parameterModeMap.get(param);
        if(null == parameterMode) return parameterModeUnknown;
        return parameterMode;
    }

    public int getParameterType(int param) throws SQLException
    {
        Integer parameterType = parameterTypeMap.get(param);
        if(null == parameterType) return Types.OTHER;
        return parameterType;
    }

    public int getPrecision(int param) throws SQLException
    {
        Integer precision = precisionMap.get(param);
        if(null == precision) return 0;
        return precision;
    }

    public int getScale(int param) throws SQLException
    {
        Integer scale = scaleMap.get(param);
        if(null == scale) return 0;
        return scale;
    }

    public int isNullable(int param) throws SQLException
    {
        Integer isNullable = isNullableMap.get(param);
        if(null == isNullable) return parameterNullable;
        return isNullable;
    }

    public boolean isSigned(int param) throws SQLException
    {
        Boolean isSigned = isSignedMap.get(param);
        if(null == isSigned) return false;
        return isSigned;
    }

    public String getParameterClassName(int param) throws SQLException
    {
        String parameterClassName = parameterClassNameMap.get(new Integer(param));
        if(null == parameterClassName) return Object.class.getName();
        return parameterClassName;
    }

    public String getParameterTypeName(int param) throws SQLException
    {
        String ParameterTypeName = parameterTypeNameMap.get(new Integer(param));
        if(null == ParameterTypeName) return Object.class.getName();
        return ParameterTypeName;
    }
    
    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        throw new SQLException("No object found for " + iface);
    }
}
