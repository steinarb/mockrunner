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
        return "";
    }

    public ServletContext getServletContext()
    {
        return context;
    }
}
