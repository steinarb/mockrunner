package com.mockrunner.base;

import com.mockrunner.mock.web.WebMockObjectFactory;

/**
 * Generic implementation of {@link HTMLOutputModule}.
 * Can be used to to test HTML output in modules that
 * does not extend {@link HTMLOutputModule}.
 */
public class GenericHTMLOutputModule extends HTMLOutputModule
{
    private WebMockObjectFactory factory;
    
    public GenericHTMLOutputModule(WebMockObjectFactory factory)
    {
        this.factory = factory;
    }
    
    public String getOutput()
    {
        try
        {
            factory.getMockResponse().getWriter().flush();    
        }
        catch(Exception exc)
        {
        
        }
        return factory.getMockResponse().getOutputStreamContents();
    }
}
