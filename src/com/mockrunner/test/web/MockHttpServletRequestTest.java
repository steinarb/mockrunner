package com.mockrunner.test.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;

import junit.framework.TestCase;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockRequestDispatcher;

public class MockHttpServletRequestTest extends TestCase
{
    private MockHttpServletRequest request;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        request = new MockHttpServletRequest();
    }
    
    public void testAddRequestParameter() throws Exception
    {
        request.setupAddParameter("abc", "abc");
        assertEquals("abc", request.getParameter("abc"));
        request.setupAddParameter("abc", "123");
        assertEquals("123", request.getParameter("abc"));
        assertEquals(1, request.getParameterValues("abc").length);
        request.setupAddParameter("abc", new String[] {"123", "456"});
        assertEquals("123", request.getParameter("abc"));
        assertEquals(2, request.getParameterValues("abc").length);
        assertEquals("123", request.getParameterValues("abc")[0]);
        assertEquals("456", request.getParameterValues("abc")[1]);
    }
    
    public void testHeaders()
    {
        request.addHeader("testHeader", "xyz");
        request.addHeader("testHeader", "abc");
        Enumeration enum = request.getHeaders("testHeader");
        List list = new ArrayList();
        list.add(enum.nextElement());
        list.add(enum.nextElement());
        assertFalse(enum.hasMoreElements());
        assertTrue(list.contains("xyz"));
        assertTrue(list.contains("abc"));
        assertNull(request.getHeader("anotherHeader"));
        request.addHeader("dateHeader", "Friday, 06 Feb 2004 22:59:48 GMT");
        enum = request.getHeaderNames();
        list = new ArrayList();
        list.add(enum.nextElement());
        list.add(enum.nextElement());
        assertFalse(enum.hasMoreElements());
        assertTrue(list.contains("testHeader"));
        assertTrue(list.contains("dateHeader"));
        long date = request.getDateHeader("dateHeader");
        assertEquals(1076108388000l, date);
        assertEquals(-1, request.getDateHeader("anotherDateHeader"));
        request.setHeader("dateHeader", "3.3.1980");
        try
        {
            request.getDateHeader("dateHeader");
            fail();
        }
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        request.setHeader("intHeader", "25");
        int value = request.getIntHeader("intHeader");
        assertEquals(25, value);
        assertEquals(-1, request.getIntHeader("anotherIntHeader"));
        request.setHeader("intHeader", "xyz");
        try
        {
            request.getIntHeader("intHeader");
            fail();
        }
        catch(NumberFormatException exc)
        {
            //should throw exception
        }
    }
    
    public void testCookies()
    {
        request.addCookie(new Cookie("name1", "value1"));
        request.addCookie(new Cookie("name2", "value2"));
        request.addCookie(new Cookie("name3", "value3"));
        Cookie[] cookies = request.getCookies();
        assertTrue(cookies.length == 3);
        assertEquals("name1", cookies[0].getName());
        assertEquals("value1", cookies[0].getValue());
        assertEquals("name2", cookies[1].getName());
        assertEquals("value2", cookies[1].getValue());
        assertEquals("name3", cookies[2].getName());
        assertEquals("value3", cookies[2].getValue());
    }
    
    public void testBodyContent() throws Exception
    {
        request.setBodyContent("test\nanothertest");
        BufferedReader reader = request.getReader();
        assertEquals("test", reader.readLine());
        assertEquals("anothertest", reader.readLine());
        assertEquals(-1, reader.read());
        request.setBodyContent(new byte[] {65, 66, 67});
        ServletInputStream stream = request.getInputStream();
        assertEquals(65, stream.read());
        assertEquals(66, stream.read());
        assertEquals(67, stream.read());
        reader = request.getReader();
        assertEquals('A', (char)reader.read());
        assertEquals('B', (char)reader.read());
        assertEquals('C', (char)reader.read());
    }
    
    public void testRequestDispatcher() throws Exception
    {
        final String rdPath1 = "rdPathOne";
        final String rdPath2 = "rdPathTwo";
        final String rdPath3 = "rdPathThree";
    
        assertEquals(0, request.getRequestDispatcherMap().size());

        MockRequestDispatcher rd1 = (MockRequestDispatcher)request.getRequestDispatcher(rdPath1);
        assertEquals(rdPath1, rd1.getPath());
        assertNull(rd1.getForwardedRequest());
        assertNull(rd1.getIncludedRequest());
        
        assertEquals(1, request.getRequestDispatcherMap().size());
        assertTrue(request.getRequestDispatcherMap().containsKey(rdPath1));
        assertSame(rd1, request.getRequestDispatcherMap().get(rdPath1));
        
        MockRequestDispatcher actualRd2 = new MockRequestDispatcher();
        request.setRequestDispatcher(rdPath2, actualRd2);
        MockRequestDispatcher rd2 = (MockRequestDispatcher)request.getRequestDispatcher(rdPath2);
        assertEquals(rdPath2, rd2.getPath());
        assertSame(actualRd2, rd2);
        assertNull(rd1.getForwardedRequest());
        assertNull(rd1.getIncludedRequest());
        
        assertEquals(2, request.getRequestDispatcherMap().size());
        assertTrue(request.getRequestDispatcherMap().containsKey(rdPath2));
        assertSame(rd2, request.getRequestDispatcherMap().get(rdPath2));
        
        RequestDispatcher actualRd3 = new TestRequestDispatcher();
        request.setRequestDispatcher(rdPath3, actualRd3);
        RequestDispatcher rd3 = request.getRequestDispatcher(rdPath3);
        assertSame(actualRd3, rd3);
        
        assertEquals(3, request.getRequestDispatcherMap().size());
        assertTrue(request.getRequestDispatcherMap().containsKey(rdPath3));
        assertSame(rd3, request.getRequestDispatcherMap().get(rdPath3));                          
    }
    
    private class TestRequestDispatcher implements RequestDispatcher
    {
        
        public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException
        {

        }
        
        public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException
        {

        }
    }
}
