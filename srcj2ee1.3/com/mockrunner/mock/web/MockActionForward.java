package com.mockrunner.mock.web;

import org.apache.struts.action.ActionForward;

/**
 * Mock implementation of <code>ActionForward</code>.
 */
public class MockActionForward extends MockForwardConfig
{
    public MockActionForward()
    {
        this(null, false);
    }
    
    public MockActionForward(String name)
    {
        this(name, false);
    }
    
    public MockActionForward(String name, boolean redirect)
    {
        
        super();
        setName(name);
        setPath(null);
        setRedirect(redirect);
        
    }
    
    public MockActionForward(String name, String path, boolean redirect)
    {
        super();
        setName(name);
        setPath(path);
        setRedirect(redirect);
    }
    
    public MockActionForward(String name, String path, boolean redirect, boolean contextRelative)
    {
        super();
        setName(name);
        setPath(path);
        setRedirect(redirect);
        setContextRelative(contextRelative);
    }

    public MockActionForward(String name, String path, boolean redirect, String module)
    {
        super();
        setName(name);
        setPath(path);
        setRedirect(redirect);
        setModule(module);
    }
    
    public MockActionForward(ActionForward copyMe) 
    {
        setName(copyMe.getName());
        setPath(copyMe.getPath());
        setRedirect(copyMe.getRedirect());
        setContextRelative(copyMe.getContextRelative());
    }
    
    public boolean verifyName(String name)
    {
        if (null == getName()) return false;
        if (getName().equals(name))
        {
            return true;
        }
        return false;
    }
    
    public boolean verifyPath(String path)
    {
        if (null == getPath()) return false;
        if (getPath().equals(path))
        {
            return true;
        }
        return false;
    }
    
    public boolean verifyRedirect(boolean redirect)
    {
        return getRedirect() == redirect;
    }
}
