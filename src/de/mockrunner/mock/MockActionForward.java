package de.mockrunner.mock;

public class MockActionForward extends MockForwardConfig
{
    public MockActionForward()
    {
        this(null, false);
    }

    public MockActionForward(String path)
    {
        this(path, false);
    }

    public MockActionForward(String path, boolean redirect)
    {

        super();
        setName(null);
        setPath(path);
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

    public boolean verifyPath(String name)
    {
        if (null == getPath())
            return false;
        if (getPath().equals(name))
        {
            return true;
        }
        return false;
    }
}
