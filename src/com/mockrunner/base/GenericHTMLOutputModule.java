package com.mockrunner.base;

/**
 * Generic implementation of {@link HTMLOutputModule}.
 */
public class GenericHTMLOutputModule extends HTMLOutputModule
{
    private MockObjectFactory factory;
    
    public GenericHTMLOutputModule(MockObjectFactory factory)
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
