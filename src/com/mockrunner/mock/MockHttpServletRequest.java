package com.mockrunner.mock;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

import com.mockobjects.servlet.MockRequestDispatcher;

/**
 * Mock implementation of <code>HttpServletRequest</code>.
 */
public class MockHttpServletRequest extends com.mockobjects.servlet.MockHttpServletRequest
{
    private Map attributes = new HashMap();
    private Map parameters = new HashMap();
    private MockRequestDispatcher dispatcher = new MockRequestDispatcher();

    public String getParameter(String key)
    {
        String[] values = getParameterValues(key);
        if (null != values && 0 < values.length)
        {
            return values[0];
        }
        return null;
    }
    
    public void clearParameters()
    {
        parameters.clear();
    }

    public String[] getParameterValues(String key)
    {
        return (String[]) parameters.get(key);
    }

    public void setupAddParameter(String key, String[] values)
    {
        parameters.put(key, values);
    }

    public void setupAddParameter(String key, String value)
    {
        setupAddParameter(key, new String[] { value });
    }

    public Enumeration getParameterNames()
    {
        Vector parameterKeys = new Vector(parameters.keySet());
        return parameterKeys.elements();
    }

    public Map getParameterMap()
    {
        return Collections.unmodifiableMap(parameters);
    }
    
    public void clearAttributes()
    {
        attributes.clear();
    }

    public Object getAttribute(String key)
    {
        return attributes.get(key);
    }

    public Enumeration getAttributeNames()
    {
        Vector attKeys = new Vector(attributes.keySet());
        return attKeys.elements();
    }

    public void removeAttribute(String key)
    {
        attributes.remove(key);
    }

    public void setAttribute(String key, Object value)
    {
        attributes.put(key, value);
    }

    public HttpSession getSession(boolean arg0)
    {
        return super.getSession();
    }

    public RequestDispatcher getRequestDispatcher(String arg0)
    {
        return dispatcher;
    }
}
