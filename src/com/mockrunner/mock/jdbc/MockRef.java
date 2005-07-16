package com.mockrunner.mock.jdbc;

import java.sql.Ref;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Mock implementation of <code>Ref</code>.
 */
public class MockRef implements Ref, Cloneable
{
    private final static Log log = LogFactory.getLog(MockRef.class);
    private Object object;
    private String baseTypeName;
    
    public MockRef(Object object)
    {
        this.object = object;
        baseTypeName = "";
    }
    
    public String getBaseTypeName() throws SQLException
    {
        return baseTypeName;
    }

    /**
     * Sets the base type name.
     * @param baseTypeName the base type name
     */
    public void setBaseTypeName(String baseTypeName)
    {
        this.baseTypeName = baseTypeName;
    }

    public Object getObject(Map map) throws SQLException
    {
        return object;
    }

    public Object getObject() throws SQLException
    {
        return object;
    }
    
    public void setObject(Object object) throws SQLException
    {
        this.object = object;
    }
    
    public String toString()
    {
        return "Ref data: " + object.toString();
    }
    
    public Object clone()
    {
        try
        {
            return (MockRef)super.clone();
        } 
        catch(CloneNotSupportedException exc)
        {
            log.error(exc.getMessage(), exc);
        }
        return null;
    }
}
