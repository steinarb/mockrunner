package com.mockrunner.mock.web;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Mock implementation of <code>HttpServletResponse</code>.
 */
public class MockHttpServletResponse extends com.mockobjects.servlet.MockHttpServletResponse
{
    private PrintWriter writer;
    
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
    
    public PrintWriter getWriter() throws IOException
    {
        if(null == writer)
        {
            writer = new PrintWriter(getOutputStream());
        }
        return writer;
    }
}
