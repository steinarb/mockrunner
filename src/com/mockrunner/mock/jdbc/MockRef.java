package com.mockrunner.mock.jdbc;

import java.sql.Ref;
import java.sql.SQLException;
import java.util.Map;

/**
 * Mock implementation of <code>Ref</code>.
 */
public class MockRef implements Ref
{
    private Object object;
    
    public MockRef(Object object)
    {
        this.object = object;
    }
    
    public String getBaseTypeName() throws SQLException
    {
        return "";
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
}
