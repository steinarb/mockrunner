package com.mockrunner.mock;

/**
 * Mock implementation of <code>HttpServletResponse</code>.
 */
public class MockHttpServletResponse extends com.mockobjects.servlet.MockHttpServletResponse
{
    public String encodeURL(String url)
    {
        return url;
    }

    public String encodeRedirectUrl(String url)
    {
        return url;
    }

    public String encodeRedirectURL(String url)
    {
        return url;
    }

    public String encodeUrl(String url)
    {
        return url;
    }
}
