package com.mockrunner.mock;

import java.util.Enumeration;

public class MockFilterConfig extends com.mockobjects.servlet.MockFilterConfig
{
    public String getInitParameter(String key)
    {
        return getServletContext().getInitParameter(key);
    }

    public Enumeration getInitParameterNames()
    {
        return getServletContext().getInitParameterNames();
    }
}
