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
    private Map parameterMode;
    private Map parameterType;
    private Map precision;
    private Map scale;
    private Map nullable;
    private Map signed;
    private Map parameterClassName;
    private Map parameterTypeName;
    
    public MockParameterMetaData()
    {
        parameterCount = 0;
        parameterMode = new HashMap();
        parameterType = new HashMap();
        precision = new HashMap();
        scale = new HashMap();
        nullable = new HashMap();
        signed = new HashMap();
        parameterClassName = new HashMap();
        parameterTypeName = new HashMap();
    }
    
    public void setupParameterCount(int count)
    {
        parameterCount = count;
    }
    
    public void setupParameterMode(int param, int mode)
    {
        parameterMode.put(new Integer(param), new Integer(mode));
    }
    
    public void setupParameterType(int param, int type)
    {
        parameterType.put(new Integer(param), new Integer(type));
    }
    
    public void setupPrecision(int param, int prec)
    {
        precision.put(new Integer(param), new Integer(prec));
    }
    
    public void setupScale(int param, int paramScale)
    {
        scale.put(new Integer(param), new Integer(paramScale));
    }
    
    public void setupIsNullable(int param, int isNullable)
    {
        nullable.put(new Integer(param), new Integer(isNullable));
    }
    
    public void setupIsSigned(int param, boolean isSigned)
    {
        signed.put(new Integer(param), new Boolean(isSigned));
    }
    
    public void setupParameterClassName(int param, String className)
    {
        parameterClassName.put(new Integer(param), className);
    }
    
    public void setupParameterTypeName(int param, String typeName)
    {
        parameterTypeName.put(new Integer(param), typeName);
    }
    
    public int getParameterCount() throws SQLException
    {
        return parameterCount;
    }

    public int getParameterMode(int param) throws SQLException
    {
        Integer mode = (Integer)parameterMode.get(new Integer(param));
        if(null == mode) return ParameterMetaData.parameterModeUnknown;
        return mode.intValue();
    }

    public int getParameterType(int param) throws SQLException
    {
        Integer type = (Integer)parameterType.get(new Integer(param));
        if(null == type) return Types.OTHER;
        return type.intValue();
    }

    public int getPrecision(int param) throws SQLException
    {
        Integer prec = (Integer)precision.get(new Integer(param));
        if(null == prec) return 0;
        return prec.intValue();
    }

    public int getScale(int param) throws SQLException
    {
        Integer paramScale = (Integer)scale.get(new Integer(param));
        if(null == paramScale) return 0;
        return paramScale.intValue();
    }

    public int isNullable(int param) throws SQLException
    {
        Integer isNullable = (Integer)nullable.get(new Integer(param));
        if(null == isNullable) return ParameterMetaData.parameterNullable;
        return isNullable.intValue();
    }

    public boolean isSigned(int param) throws SQLException
    {
        Boolean isSigned = (Boolean)signed.get(new Integer(param));
        if(null == isSigned) return false;
        return isSigned.booleanValue();
    }

    public String getParameterClassName(int param) throws SQLException
    {
        String className = (String)parameterClassName.get(new Integer(param));
        if(null == className) return Object.class.getName();
        return className;
    }

    public String getParameterTypeName(int param) throws SQLException
    {
        String typeName = (String)parameterTypeName.get(new Integer(param));
        if(null == typeName) return "";
        return typeName;
    }

}
