package com.mockrunner.mock.jdbc;

import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mock implementation of <code>Struct</code>.
 */
public class MockStruct implements Struct, Cloneable
{
    private String sqlTypeName;
    private List attributes;
    
    public MockStruct(String sqlTypeName)
    {
        this.sqlTypeName = sqlTypeName;
        attributes = new ArrayList();
    }
    
    public String getSQLTypeName() throws SQLException
    {
        return sqlTypeName;
    }
    
    public Object[] getAttributes() throws SQLException
    {
        return attributes.toArray();
    }

    public Object[] getAttributes(Map map) throws SQLException
    {
        return getAttributes();
    }
    
    public void addAttribute(Object attribute)
    {
        attributes.add(attribute);
    }
    
    public void addAttributes(Object[] attributes)
    {
        for(int ii = 0; ii < attributes.length; ii++)
        {
            addAttribute(attributes[ii]);
        }
    }
    
    public void addAttributes(List attributes)
    {
        addAttributes(attributes.toArray());
    }
    
    public void clearAttributes()
    {
        attributes.clear();
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Struct data:\n");
        buffer.append("SQLTypeName: " + sqlTypeName + "\n");
        for(int ii = 0; ii < attributes.size(); ii++)
        {
            buffer.append("Attribute " + ii + ": " + attributes.get(ii)+ "\n");
        }
        buffer.append("\n");
        return buffer.toString();
    }
    
    public Object clone()
    {
        MockStruct struct = new MockStruct(sqlTypeName);
        struct.addAttributes(attributes.toArray());
        return struct;
    }
}
