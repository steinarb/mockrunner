package com.mockrunner.base;

import junit.framework.TestCase;

/**
 * Delegator for {@link WebTestModule}. The corresponding
 * adapters extend this class. This class is used for the basic
 * adapter versions.
 */
public abstract class BasicWebTestCase extends TestCase
{
    public BasicWebTestCase()
    {

    }

    public BasicWebTestCase(String arg0)
    {
        super(arg0);
    }
    
    /**
     * Implemented by concrete subclasses.
     */ 
    protected abstract WebTestModule getWebTestModule();
    
    /**
     * Delegates to {@link WebTestModule#addRequestParameter(String)}
     */
    protected void addRequestParameter(String key)
    {
        getWebTestModule().addRequestParameter(key);
    }

    /**
     * Delegates to {@link WebTestModule#addRequestParameter(String, String)}
     */
    protected void addRequestParameter(String key, String value)
    {
        getWebTestModule().addRequestParameter(key, value);
    }

    /**
     * Delegates to {@link WebTestModule#addRequestParameter(String, String[])}
     */
    protected void addRequestParameter(String key, String[] values)
    {
        getWebTestModule().addRequestParameter(key, values);
    }

    /**
     * Delegates to {@link WebTestModule#getRequestParameter(String)}
     */
    protected String getRequestParameter(String key)
    {
        return getWebTestModule().getRequestParameter(key);
    }

    /**
     * Delegates to {@link WebTestModule#getRequestAttribute(String)}
     */
    protected Object getRequestAttribute(String key)
    {
        return getWebTestModule().getRequestAttribute(key);
    }

    /**
     * Delegates to {@link WebTestModule#setRequestAttribute(String, Object)}
     */
    protected void setRequestAttribute(String key, Object value)
    {
        getWebTestModule().setRequestAttribute(key, value);
    }

    /**
     * Delegates to {@link WebTestModule#setRequestAttribute(String, Object)}
     */
    protected Object getSessionAttribute(String key)
    {
        return getWebTestModule().getSessionAttribute(key);
    }

    /**
     * Delegates to {@link WebTestModule#setSessionAttribute(String, Object)}
     */
    protected void setSessionAttribute(String key, Object value)
    {
        getWebTestModule().setSessionAttribute(key, value);
    }
}
