package com.mockrunner.mock.jdbc;

import java.sql.Ref;
import java.sql.SQLException;
import java.util.Map;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of <code>Ref</code>.
 */
public class MockRef implements Ref, Cloneable
{
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
    
    public boolean equals(Object obj)
    {
        if(null == obj) return false;
        if(!obj.getClass().equals(this.getClass())) return false;
        MockRef other = (MockRef)obj;
        if(null != baseTypeName && !baseTypeName.equals(other.baseTypeName)) return false;
        if(null != other.baseTypeName && !other.baseTypeName.equals(baseTypeName)) return false;
        if(null == object && null == other.object) return true;
        if(null == object || null == other.object) return false;
        return object.equals(other.object);
    }

    public int hashCode()
    {
        int hashCode = 0;
        if(null != baseTypeName) hashCode = (31 * hashCode) + baseTypeName.hashCode();
        if(null != object) hashCode = (31 * hashCode) + object.hashCode();
        return hashCode;
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
            throw new NestedApplicationException(exc);
        }
    }
}
