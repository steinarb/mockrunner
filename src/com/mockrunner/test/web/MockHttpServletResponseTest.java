package com.mockrunner.test.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

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
    
    public void testResetAll() throws Exception
    {
        response.addHeader("header", "headervalue");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        response.setBufferSize(10);
        response.resetAll();
        assertNull(response.getHeader("header"));
        assertEquals(8192, response.getBufferSize());
        assertFalse(response.wasErrorSent());
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
        response.clearHeaders();
        Enumeration headers = response.getHeaderNames();
        assertFalse(headers.hasMoreElements());
    }
    
    public void testGetHeaderNames()
    {
        response.addHeader("testHeader1", "xyz1");
        response.addHeader("testHeader1", "abc");
        response.addHeader("testHeader2", "xyz2");
        response.addHeader("testHeader3", "xyz3");
        Enumeration headerNamesEnum = response.getHeaderNames();
        List headerNames = new ArrayList();
        while(headerNamesEnum.hasMoreElements())
        {
            headerNames.add(headerNamesEnum.nextElement());
        }
        assertEquals(3, headerNames.size());
        assertTrue(headerNames.contains("testHeader1"));
        assertTrue(headerNames.contains("testHeader2"));
        assertTrue(headerNames.contains("testHeader3"));
    }
    
    public void testHeadersCaseInsensitive()
    {
        response.addHeader("testHeader", "xyz");
        response.addHeader("TESTHeader", "abc");
        response.addHeader("MYHEADER1", "xyz");
        response.addHeader("myHeader2", "abc");
        assertEquals("xyz", response.getHeader("myheader1"));
        assertEquals("abc", response.getHeader("MYHEADER2"));
        Enumeration headers = response.getHeaderNames();
        List headerNames = new ArrayList();
        while(headers.hasMoreElements())
        {
            headerNames.add(headers.nextElement());
        }
        assertEquals(3, headerNames.size());
        assertTrue(headerNames.contains("testHeader"));
        assertTrue(headerNames.contains("MYHEADER1"));
        assertTrue(headerNames.contains("myHeader2"));
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
    
    public void testSetCharacterEncoding() throws IOException
    {
        response.setCharacterEncoding("ISO-8859-1");
        response.getWriter().write("ה");
        response.getWriter().flush();
        response.getOutputStream().println("ה");
        response.setCharacterEncoding("US-ASCII");
        assertFalse(response.getOutputStreamContent().startsWith("הה"));
        response.setCharacterEncoding("ISO-8859-1");
        assertTrue(response.getOutputStreamContent().startsWith("הה"));
    }
}
