package com.mockrunner.mock.jdbc;

import java.util.List;
import java.util.Map;

/**
 * Abstract base class for all statement types
 * that support out parameters, i.e. <code>CallableStatement</code>.
 */
public class AbstractOutParameterResultSetHandler extends AbstractParameterResultSetHandler
{
    public Map getOutParameter(String sql, Map parameters)
    {
        return null;
    }
    
    public void clearOutParameter()
    {
    
    }
    
    public void prepareGlobalOutParameter(Map outParameters)
    {
    
    }
    
    public void prepareOutParameter(String sql, Map outParameters)
    {

    }
    
    public void prepareOutParameter(String sql, Map outParameters, Object[] parameters)
    {

    }
    
    public void prepareOutParameter(String sql, Map outParameters, List parameters)
    {

    }
    
    public void prepareOutParameter(String sql, Map outParameters, Map parameters)
    {

    }
}
