package com.mockrunner.test.web;

import java.io.IOException;

import com.mockrunner.mock.web.MockHttpServletResponse;

import junit.framework.TestCase;

public class MockHttpServletResponseTest extends TestCase
{
    public void testHeaders()
    {
        MockHttpServletResponse response = new MockHttpServletResponse();
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
        assertEquals("Thu Jan 01 01:00:00 CET 1970", response.getHeader("dateHeader"));
        response.addIntHeader("intHeader", 0);
        assertEquals("0", response.getHeader("intHeader"));
    }
    
    public void testOutputStreams() throws IOException
    {
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.getOutputStream().print("test");
        response.getWriter().print(true);
        response.getWriter().print("test");
        response.flushBuffer();
        assertEquals("testtruetest", response.getOutputStreamContent());
    }
}
