package com.mockrunner.mock.jdbc;

import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mock implementation of <code>Struct</code>.
 */
public class MockStruct implements Struct
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
}
