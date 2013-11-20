package com.mockrunner.tag;

import com.mockrunner.mock.web.WebMockObjectFactory;

/**
 * Simple base class for implementations of
 * {@link com.mockrunner.tag.RuntimeAttribute}.
 */
public abstract class AbstractRuntimeAttribute implements RuntimeAttribute
{
    private WebMockObjectFactory factory;
     
    public AbstractRuntimeAttribute(WebMockObjectFactory factory)
    {
        this.factory = factory;
    }
    
    /**
     * Get the {@link com.mockrunner.mock.web.WebMockObjectFactory} passed
     * in the constructor.
     * @return the {@link com.mockrunner.mock.web.WebMockObjectFactory}
     */
    public WebMockObjectFactory getWebMockObjectFactory()
    {
        return factory;
    }
}
