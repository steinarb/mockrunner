package com.mockrunner.mock.web;

/**
 * Mock implementation of <code>UserPrincipal</code>.
 */
public class MockUserPrincipal implements java.security.Principal
{
    String mName;

    public MockUserPrincipal(String name)
    {
        mName = name;
    }
    
    public String getName()
    {
        return mName;
    }
}
