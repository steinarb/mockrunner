package com.mockrunner.test.web;

import java.io.IOException;

import com.mockrunner.mock.web.MockHttpServletResponse;

import junit.framework.TestCase;

public class MockHttpServletResponseTest extends TestCase
{
    private MockHttpServletResponse response;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        response = new MockHttpServletResponse();
    }
    
    public void testHeaders()
    {
        response.addHeader("testHeader", "xyz");
        response.addHeader("testHeader", "abc");
        assertTrue(response.getHeaderList("testHeader").size() == 2);
        assertTrue(response.getHeaderList("testHeader").contains("xyz"));
        assertTrue(response.getHeaderList("testHeader").contains("abc"));
        response.setHeader("testHeader", "abc");
        assertTrue(response.getHeaderList("testHeader").size() == 1);
        assertTrue(response.getHeaderList("testHeader").contains("abc"));
        assertEquals("abc", response.getHeader("testHeader"));
        response.addDateHeader("dateHeader", 0);
        assertEquals("Thu, 1 Jan 1970 01:00:00 +0100", response.getHeader("dateHeader"));
        response.addIntHeader("intHeader", 0);
        assertEquals("0", response.getHeader("intHeader"));
    }
    
    public void testOutputStreams() throws IOException
    {
        response.getOutputStream().print("test");
        response.getWriter().print(true);
        response.getWriter().print("test");
        response.flushBuffer();
        assertEquals("testtruetest", response.getOutputStreamContent());
    }
    
    public void testGetSetContentType() throws IOException
    {
        assertNull(response.getContentType());
        response.setContentType("myType");
        assertEquals("myType", response.getContentType());
    }
}
