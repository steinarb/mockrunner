package com.mockrunner.mock.web;

import java.security.Principal;

/**
 * Mock implementation of <code>UserPrincipal</code>.
 */
public class MockUserPrincipal implements Principal
{
    private String mName;

    public MockUserPrincipal(String name)
    {
        mName = name;
    }
    
    public String getName()
    {
        return mName;
    }
}
