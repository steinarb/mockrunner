package com.mockrunner.mock.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;

/**
 * Mock implementation of <code>HttpSession</code>.
 */
public class MockHttpSession implements HttpSession
{
    public HashMap attributes = new HashMap();
    private String sessionId;
    private boolean isNew;
    private boolean isValid;
    private long creationTime;
    private ServletContext servletContext;
    private int maxInactiveInterval = -1;

    public MockHttpSession()
    {
        super();
        isValid = true;
        creationTime = System.currentTimeMillis();
        sessionId = new Double(Math.random()).toString();
    }

    public synchronized void setupServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    public synchronized ServletContext getServletContext()
    {
        return servletContext;
    }
    
    public synchronized boolean isValid()
    {
        return isValid;
    }

    public synchronized boolean isNew()
    {
        return isNew;
    }

    public synchronized void setUpIsNew(boolean isNew)
    {
        this.isNew = isNew;
    }

    public synchronized long getCreationTime()
    {
        return creationTime;
    }

    public synchronized void invalidate()
    {
        Map clone = new HashMap(attributes);
        Iterator keys = clone.keySet().iterator();
        while (keys.hasNext())
        {
            removeAttribute((String) keys.next());
        }
        isValid = false;
    }

    public synchronized String getId()
    {
        return sessionId;
    }

    public synchronized Object getValue(String key)
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        return getAttribute(key);
    }

    public synchronized String[] getValueNames()
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        Vector attKeys = new Vector(attributes.keySet());
        return (String[]) attKeys.toArray();
    }

    public synchronized void putValue(String key, Object value)
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        setAttribute(key, value);
    }

    public synchronized void removeValue(String key)
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        removeAttribute(key);
    }
    
    public synchronized void clearAttributes()
    {
        attributes.clear();
    }

    public synchronized Object getAttribute(String key)
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        return attributes.get(key);
    }

    public synchronized Enumeration getAttributeNames()
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        Vector attKeys = new Vector(attributes.keySet());
        return attKeys.elements();
    }

    public synchronized void removeAttribute(String key)
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        callValueUnboundMethod(key, attributes.get(key));
        attributes.remove(key);
    }

    public synchronized void setAttribute(String key, Object value)
    {
        if (!isValid) throw new IllegalStateException("session invalid");
        Object oldValue = getAttribute(key);
        if(null != oldValue)
        {
            callValueUnboundMethod(key, oldValue);
        }
        callValueBoundMethod(key, value);
        attributes.put(key, value);
    }

    private synchronized void callValueBoundMethod(String key, Object value)
    {
        if (value instanceof HttpSessionBindingListener)
        {
            MockHttpSessionBindingEvent event = new MockHttpSessionBindingEvent(this, key, attributes.get(key));
            ((HttpSessionBindingListener) value).valueBound(event);
        }
    }

    private synchronized void callValueUnboundMethod(String key, Object value)
    {
        if (value instanceof HttpSessionBindingListener)
        {
            MockHttpSessionBindingEvent event = new MockHttpSessionBindingEvent(this, key, attributes.get(key));
            ((HttpSessionBindingListener) value).valueUnbound(event);
        }
    }
    
    public synchronized long getLastAccessedTime()
    {
        return System.currentTimeMillis();
    }
    
    public synchronized void setMaxInactiveInterval(int maxInactiveInterval)
    {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public synchronized int getMaxInactiveInterval()
    {
        return maxInactiveInterval;
    }

    public synchronized HttpSessionContext getSessionContext()
    {
        return new MockSessionContext();
    }
}
