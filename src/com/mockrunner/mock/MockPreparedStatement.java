package com.mockrunner.mock;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Mock implementation of <code>PreparedStatement</code>.
 */
public class MockPreparedStatement extends com.mockobjects.sql.MockPreparedStatement
{
    private Map setObjects = new HashMap();
    
    public Map getObjectMap()
    {
        return setObjects;
    }
    
    public Object getObject(int index)
    {
        return setObjects.get(new Integer(index));
    }
    
    public void setObject(int index, Object object) throws SQLException 
    {
        setObjects.put(new Integer(index), object);
    }
}
