package de.mockrunner.mock;

import org.apache.struts.action.ActionForward;

/**
 * Mock implementation of <code>ForwardConfig</code>.
 */
public class MockForwardConfig extends ActionForward
{
	private String name = null;
	private String path = null;
	private boolean redirect = false;
	private boolean contextRelative = false;
	
	public MockForwardConfig() 
	{

	}
	 
    public MockForwardConfig(String name, String path, boolean redirect) 
    {
        super();
        setName(name);
        setPath(path);
        setRedirect(redirect);
    }

    public MockForwardConfig(String name, String path, boolean redirect, boolean contextRelative) 
    {

        super();
        setName(name);
        setPath(path);
        setRedirect(redirect);
        setContextRelative(contextRelative);

    }

    public boolean getContextRelative() 
    {
        return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) 
    {
        this.contextRelative = contextRelative;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getPath() 
	{
        return path;
    }

    public void setPath(String path) 
    {
        this.path = path;
    }

    public boolean getRedirect() 
    {
        return redirect;
    }

    public void setRedirect(boolean redirect) 
    {
        this.redirect = redirect;
    }

   
    public String toString() 
    {
        StringBuffer sb = new StringBuffer("ForwardConfig[");
        sb.append("name=");
        sb.append(this.name);
        sb.append(",path=");
        sb.append(this.path);
        sb.append(",redirect=");
        sb.append(this.redirect);
        sb.append("]");
        return (sb.toString());

    }
}
