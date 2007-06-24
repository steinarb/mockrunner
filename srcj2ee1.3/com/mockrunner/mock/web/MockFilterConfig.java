package com.mockrunner.mock.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * Mock implementation of <code>FilterConfig</code>.
 */
public class MockFilterConfig implements FilterConfig
{
    private ServletContext context;
    private Map initParameters;
    private String name;
    
    public MockFilterConfig()
    {
        initParameters = new HashMap();
    }

    /**
     * Sets the <code>ServletContext</code>.
     * @param context the <code>ServletContext</code>
     */
    public synchronized void setupServletContext(ServletContext context)
    {
        this.context = context;
    }

    public synchronized String getFilterName()
    {
        return name;
    }
    
    public synchronized void setFilterName(String name)
    {
        this.name = name;
    }

    public synchronized ServletContext getServletContext()
    {
        return context;
    }
    
    public synchronized void clearInitParameters()
    {
        initParameters.clear();
    }

    /**
     * Clears the init parameters.
     */
    public synchronized String getInitParameter(String name)
    {
        return (String)initParameters.get(name);
    }
    
    /**
     * Sets an init parameter.
     * @param name the name
     * @param value the value
     */
    public synchronized void setInitParameter(String name, String value) 
    {
        initParameters.put(name, value);
    }
    
    /**
     * Sets several init parameters.
     * @param parameters the parameter map
     */
    public synchronized void setInitParameters(Map parameters) 
    {
        initParameters.putAll(parameters);
    }

    public synchronized Enumeration getInitParameterNames()
    {
        return new Vector(initParameters.keySet()).elements();
    }
}
