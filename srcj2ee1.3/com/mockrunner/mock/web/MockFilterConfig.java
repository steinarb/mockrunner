package com.mockrunner.mock.web;

import java.util.Enumeration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * Mock implementation of <code>FilterConfig</code>.
 */
public class MockFilterConfig implements FilterConfig
{
    private ServletContext context;
    private String name;
    
    public void setupServletContext(ServletContext context)
    {
        this.context = context;
    }
    
    public String getInitParameter(String key)
    {
        return getServletContext().getInitParameter(key);
    }

    public Enumeration getInitParameterNames()
    {
        return getServletContext().getInitParameterNames();
    }
    
    public String getFilterName()
    {
        return name;
    }
    
    public void setFilterName(String name)
    {
        this.name = name;
    }

    public ServletContext getServletContext()
    {
        return context;
    }
}
