package com.mockrunner.test.web;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;

import junit.framework.TestCase;

import com.mockrunner.mock.web.MockHttpServletRequest;

public class MockHttpServletRequestTest extends TestCase
{
    public void testHeaders()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
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
        MockHttpServletRequest request = new MockHttpServletRequest();
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
        MockHttpServletRequest request = new MockHttpServletRequest();
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
}
