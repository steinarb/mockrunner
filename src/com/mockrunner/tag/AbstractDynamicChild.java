package com.mockrunner.tag;

import com.mockrunner.mock.web.WebMockObjectFactory;

/**
 * Simple base class for implementations of
 * {@link com.mockrunner.tag.DynamicChild}.
 */
public abstract class AbstractDynamicChild implements DynamicChild
{
    private WebMockObjectFactory factory;
     
    public AbstractDynamicChild(WebMockObjectFactory factory)
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
