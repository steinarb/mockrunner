package com.mockrunner.test.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockServletOutputStream;
import com.mockrunner.mock.web.WebConstants;

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
        SimpleDateFormat expectedDateFormat = new SimpleDateFormat(WebConstants.DATE_FORMAT_HEADER, Locale.US);
        String expectedDateString = expectedDateFormat.format(new Date(0));
        assertEquals(expectedDateString, response.getHeader("dateHeader"));
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
    
    public void testFlush() throws IOException
    {
        response.getOutputStream().write('a');
        response.flushBuffer();
        assertEquals("a", ((MockServletOutputStream)response.getOutputStream()).getContent());
    }
    
    public void testReset() throws IOException
    {
        response.addHeader("testHeader", "xyz");
        assertTrue(response.getHeaderList("testHeader").size() == 1);
        response.getOutputStream().write('a');
        response.getWriter().write("xyz");
        response.resetBuffer();
        assertEquals("", ((MockServletOutputStream)response.getOutputStream()).getContent());
        assertTrue(response.getHeaderList("testHeader").size() == 1);
        response.reset();
        assertNull(response.getHeaderList("testHeader"));
    }
}
