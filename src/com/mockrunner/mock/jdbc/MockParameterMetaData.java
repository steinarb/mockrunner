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
    private Map parameterModeMap;
    private Map parameterTypeMap;
    private Map precisionMap;
    private Map scaleMap;
    private Map isNullableMap;
    private Map isSignedMap;
    private Map parameterClassNameMap;
    private Map parameterTypeNameMap;
    
    public MockParameterMetaData()
    {
        parameterCount = 0;
        parameterModeMap = new HashMap();
        parameterTypeMap = new HashMap();
        precisionMap = new HashMap();
        scaleMap = new HashMap();
        isNullableMap = new HashMap();
        isSignedMap = new HashMap();
        parameterClassNameMap = new HashMap();
        parameterTypeNameMap = new HashMap();
    }
    
    public void setParameterCount(int count)
    {
        parameterCount = count;
    }
    
    public void setParameterMode(int param, int parameterMode)
    {
        parameterModeMap.put(new Integer(param), new Integer(parameterMode));
    }
    
    public void setParameterType(int param, int parameterType)
    {
        parameterTypeMap.put(new Integer(param), new Integer(parameterType));
    }
    
    public void setPrecision(int param, int precision)
    {
        precisionMap.put(new Integer(param), new Integer(precision));
    }
    
    public void setScale(int param, int scale)
    {
        scaleMap.put(new Integer(param), new Integer(scale));
    }
    
    public void setNullable(int param, int nullable)
    {
        isNullableMap.put(new Integer(param), new Integer(nullable));
    }
    
    public void setSigned(int param, boolean signed)
    {
        isSignedMap.put(new Integer(param), new Boolean(signed));
    }
    
    public void setParameterClassName(int param, String parameterClassName)
    {
        parameterClassNameMap.put(new Integer(param), parameterClassName);
    }
    
    public void setParameterTypeName(int param, String parameterTypeName)
    {
        parameterTypeNameMap.put(new Integer(param), parameterTypeName);
    }
    
    public int getParameterCount() throws SQLException
    {
        return parameterCount;
    }

    public int getParameterMode(int param) throws SQLException
    {
        Integer parameterMode = (Integer)parameterModeMap.get(new Integer(param));
        if(null == parameterMode) return parameterModeUnknown;
        return parameterMode.intValue();
    }

    public int getParameterType(int param) throws SQLException
    {
        Integer parameterType = (Integer)parameterTypeMap.get(new Integer(param));
        if(null == parameterType) return Types.OTHER;
        return parameterType.intValue();
    }

    public int getPrecision(int param) throws SQLException
    {
        Integer precision = (Integer)precisionMap.get(new Integer(param));
        if(null == precision) return 0;
        return precision.intValue();
    }

    public int getScale(int param) throws SQLException
    {
        Integer scale = (Integer)scaleMap.get(new Integer(param));
        if(null == scale) return 0;
        return scale.intValue();
    }

    public int isNullable(int param) throws SQLException
    {
        Integer isNullable = (Integer)isNullableMap.get(new Integer(param));
        if(null == isNullable) return parameterNullable;
        return isNullable.intValue();
    }

    public boolean isSigned(int param) throws SQLException
    {
        Boolean isSigned = (Boolean)isSignedMap.get(new Integer(param));
        if(null == isSigned) return false;
        return isSigned.booleanValue();
    }

    public String getParameterClassName(int param) throws SQLException
    {
        String parameterClassName = (String)parameterClassNameMap.get(new Integer(param));
        if(null == parameterClassName) return Object.class.getName();
        return parameterClassName;
    }

    public String getParameterTypeName(int param) throws SQLException
    {
        String ParameterTypeName = (String)parameterTypeNameMap.get(new Integer(param));
        if(null == ParameterTypeName) return Object.class.getName();
        return ParameterTypeName;
    }
}
