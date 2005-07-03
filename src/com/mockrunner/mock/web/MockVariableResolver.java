package com.mockrunner.mock.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.VariableResolver;

/**
 * Mock implementation of <code>VariableResolver</code>.
 */
public class MockVariableResolver implements VariableResolver
{
    private Map variables = new HashMap();
    
    /**
     * Adds a variable that resolves to the specified object.
     * @param name the variable name
     * @param value the variable value
     */
    public void addVariable(String name, Object value)
    {
        variables.put(name, value);
    }
    
    /**
     * Clears all variables.
     */
    public void clearVariables()
    {
        variables.clear();
    }

    public Object resolveVariable(String name) throws ELException
    {
        return variables.get(name);
    }
}
