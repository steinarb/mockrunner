package com.mockrunner.mock.web;


/**
 * Mock implementation of <code>SessionCookieConfig</code>.
 */
public class MockSessionCookieConfig //implements SessionCookieConfig
{
    private String comment;
    private String domain;
    private int maxAge;
    private String name;
    private String path;
    private boolean isHttpOnly;
    private boolean isSecure;
    
    public MockSessionCookieConfig()
    {
        comment = null;
        domain = null;
        maxAge = -1;
        name = "JSESSIONID";
        path = null;
        isHttpOnly = false;
        isSecure = false;
    }

    public String getComment()
    {
        return comment;
    }

    public String getDomain()
    {
        return domain;
    }

    public int getMaxAge()
    {
        return maxAge;
    }

    public String getName()
    {
        return name;
    }

    public String getPath()
    {
        return path;
    }

    public boolean isHttpOnly()
    {
        return isHttpOnly;
    }

    public boolean isSecure()
    {
        return isSecure;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public void setHttpOnly(boolean isHttpOnly)
    {
        this.isHttpOnly = isHttpOnly;
    }

    public void setMaxAge(int maxAge)
    {
        this.maxAge = maxAge;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public void setSecure(boolean isSecure)
    {
        this.isSecure = isSecure;
    }
}
