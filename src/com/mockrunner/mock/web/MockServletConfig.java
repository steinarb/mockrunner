package com.mockrunner.mock.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * Mock implementation of <code>ServletConfig</code>.
 */
public class MockServletConfig implements ServletConfig
{
    private ServletContext servletContext;
    private Map initParameters;
    private String servletName;
    
    public MockServletConfig()
    {
        initParameters = new HashMap();
    }

    public synchronized String getServletName()
    {
        return servletName;
    }
    
    public synchronized void setServletName(String servletName)
    {
        this.servletName = servletName;
    }

    public synchronized ServletContext getServletContext()
    {
        return servletContext;
    }
    
    public synchronized void setServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }
    
    public synchronized void clearInitParameters()
    {
        initParameters.clear();
    }

    public synchronized String getInitParameter(String name)
    {
        return (String)initParameters.get(name);
    }
    
    public synchronized void setInitParameter(String name, String value) 
    {
        initParameters.put(name, value);
    }

    public synchronized Enumeration getInitParameterNames()
    {
        return new Vector(initParameters.keySet()).elements();
    }

}
