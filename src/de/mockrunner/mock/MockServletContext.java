package de.mockrunner.mock;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

/**
 * Mock implementation of <code>ServletContext</code>.
 */
public class MockServletContext extends com.mockobjects.servlet.MockServletContext
{
    private HashMap attributes = new HashMap();
    
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
}
