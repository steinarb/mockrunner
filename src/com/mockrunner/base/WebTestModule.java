package com.mockrunner.base;

import com.mockrunner.mock.web.WebMockObjectFactory;

/**
 * This class provides some convenience methods for
 * request and session attribute handling
 */
public abstract class WebTestModule
{
    private WebMockObjectFactory mockFactory;

    public WebTestModule(WebMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }

    /**
     * Adds an empty request parameter. Same as
     * <code>addRequestParameter(key, "")</code>.
     * @param key the request key
     */
    public void addRequestParameter(String key)
    {
        addRequestParameter(key, "");
    }

    /**
     * Adds a request parameter.
     * @param key the request key
     * @param value the request value
     */
    public void addRequestParameter(String key, String value)
    {
        mockFactory.getMockRequest().setupAddParameter(key, value);
    }

    /**
     * Adds several request parameters. 
     * @param key the
     * @param values the request values 
     */
    public void addRequestParameter(String key, String[] values)
    {
        mockFactory.getMockRequest().setupAddParameter(key, values);
    }

    /**
     * Returns the request parameter for the specified key
     * @param key the request key
     * @return the parameter
     */
    public String getRequestParameter(String key)
    {
        return mockFactory.getWrappedRequest().getParameter(key);
    }

    /**
     * Returns the request attribute for the specified key
     * @param key the request key
     * @return the attribute
     */
    public Object getRequestAttribute(String key)
    {
        return mockFactory.getWrappedRequest().getAttribute(key);
    }

    /**
     * Sets the request attribute for the specified key
     * @param key the request key
     * @param value the value
     */
    public void setRequestAttribute(String key, Object value)
    {
        mockFactory.getWrappedRequest().setAttribute(key, value);
    }

    /**
     * Returns the session attribute for the specified key
     * @param key the session key
     * @return the attribute
     */
    public Object getSessionAttribute(String key)
    {
        return mockFactory.getMockSession().getAttribute(key);
    }

    /**
     * Sets the session attribute for the specified key
     * @param key the session key
     * @param value the value
     */
    public void setSessionAttribute(String key, Object value)
    {
        mockFactory.getMockSession().setAttribute(key, value);
    }
}
