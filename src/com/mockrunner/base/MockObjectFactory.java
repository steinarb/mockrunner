package com.mockrunner.base;

import com.mockrunner.mock.web.WebMockObjectFactory;

/**
 * @deprecated
 * use {@link com.mockrunner.mock.web.WebMockObjectFactory}
 */
public class MockObjectFactory extends WebMockObjectFactory
{
    public MockObjectFactory()
    {
        
    }

    public MockObjectFactory(MockObjectFactory factory)
    {
        super(factory);
    }

    public MockObjectFactory(MockObjectFactory factory, boolean createNewSession)
    {
        super(factory, createNewSession);
    }
}
