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
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;

import junit.framework.TestCase;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockRequestDispatcher;

public class MockHttpServletRequestTest extends TestCase
{
    private MockHttpServletRequest request;
    
    protected void setUp()
    {
        request = new MockHttpServletRequest();
    }
    
    protected void tearDown()
    {
        request = null;
    }
    
    public void testResetAll() throws Exception
    {
        request.setAttribute("key", "value");
        request.addHeader("header", "headervalue");
        request.setContentLength(5);
        request.resetAll();
        assertNull(request.getAttribute("key"));
        assertNull(request.getHeader("header"));
        assertEquals(-1, request.getContentLength());
    }
    
    public void testAttributeListenerCalled()
    {
        TestAttributeListener listener1 = new TestAttributeListener();
        TestAttributeListener listener2 = new TestAttributeListener();
        TestAttributeListener listener3 = new TestAttributeListener();
        request.addAttributeListener(listener1);
        request.addAttributeListener(listener2);
        request.addAttributeListener(listener3);
        request.setAttribute("key", "value");
        assertTrue(listener1.wasAttributeAddedCalled());
        assertTrue(listener2.wasAttributeAddedCalled());
        assertTrue(listener3.wasAttributeAddedCalled());
        assertFalse(listener1.wasAttributeReplacedCalled());
        assertFalse(listener2.wasAttributeReplacedCalled());
        assertFalse(listener3.wasAttributeReplacedCalled());
        listener1.reset();
        listener2.reset();
        listener3.reset();
        request.setAttribute("key", "value1");
        assertFalse(listener1.wasAttributeAddedCalled());
        assertFalse(listener2.wasAttributeAddedCalled());
        assertFalse(listener3.wasAttributeAddedCalled());
        assertTrue(listener1.wasAttributeReplacedCalled());
        assertTrue(listener2.wasAttributeReplacedCalled());
        assertTrue(listener3.wasAttributeReplacedCalled());
        request.removeAttribute("key");
        assertTrue(listener1.wasAttributeRemovedCalled());
        assertTrue(listener2.wasAttributeRemovedCalled());
        assertTrue(listener3.wasAttributeRemovedCalled());
    }

    public void testAttributeListenerValues()
    {
        TestAttributeOrderListener listener = new TestAttributeOrderListener();
        request.addAttributeListener(listener);
        request.setAttribute("key", "value");
        assertEquals("key", listener.getAddedEventKey());
        assertEquals("value", listener.getAddedEventValue());
        request.setAttribute("key", "anotherValue");
        assertEquals("key", listener.getReplacedEventKey());
        assertEquals("value", listener.getReplacedEventValue());
        request.removeAttribute("key");
        assertEquals("key", listener.getRemovedEventKey());
        assertEquals("anotherValue", listener.getRemovedEventValue());
    }

    public void testAttributeListenerNullValue()
    {
        TestAttributeListener listener = new TestAttributeListener();
        request.addAttributeListener(listener);
        request.setAttribute("key", null);
        assertFalse(listener.wasAttributeAddedCalled());
        request.setAttribute("key", "xyz");
        assertTrue(listener.wasAttributeAddedCalled());
        request.setAttribute("key", null);
        assertTrue(listener.wasAttributeRemovedCalled());
        assertFalse(listener.wasAttributeReplacedCalled());
        listener.reset();
        request.setAttribute("key", "xyz");
        assertTrue(listener.wasAttributeAddedCalled());
        assertFalse(listener.wasAttributeReplacedCalled());
        request.removeAttribute("myKey");
        assertFalse(listener.wasAttributeRemovedCalled());
    }
    
    public void testGetAttributeNames()
    {
        Enumeration enumeration = request.getAttributeNames();
        assertFalse(enumeration.hasMoreElements());
        request.setAttribute("key", null);
        enumeration = request.getAttributeNames();
        assertFalse(enumeration.hasMoreElements());
        request.setAttribute("key1", "value1");
        request.setAttribute("key2", "value2");
        enumeration = request.getAttributeNames();
        List testList = new ArrayList();
        testList.add(enumeration.nextElement());
        testList.add(enumeration.nextElement());
        assertFalse(enumeration.hasMoreElements());
        assertTrue(testList.contains("key1"));
        assertTrue(testList.contains("key2"));
        request.setAttribute("key2", null);
        assertNull(request.getAttribute("key2"));
        enumeration = request.getAttributeNames();
        testList = new ArrayList();
        testList.add(enumeration.nextElement());
        assertFalse(enumeration.hasMoreElements());
        assertTrue(testList.contains("key1"));
        request.setAttribute("key1", null);
        assertNull(request.getAttribute("key1"));
        enumeration = request.getAttributeNames();
        assertFalse(enumeration.hasMoreElements());
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
        Enumeration headers = request.getHeaders("testHeader");
        List list = new ArrayList();
        list.add(headers.nextElement());
        list.add(headers.nextElement());
        assertFalse(headers.hasMoreElements());
        assertTrue(list.contains("xyz"));
        assertTrue(list.contains("abc"));
        assertNull(request.getHeader("anotherHeader"));
        request.addHeader("dateHeader", "Friday, 06 Feb 2004 22:59:48 GMT");
        headers = request.getHeaderNames();
        list = new ArrayList();
        list.add(headers.nextElement());
        list.add(headers.nextElement());
        assertFalse(headers.hasMoreElements());
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
        request.clearHeaders();
        headers = request.getHeaderNames();
        assertFalse(headers.hasMoreElements());
    }
    
    public void testHeadersCaseInsensitive()
    {
        request.addHeader("testHeader", "xyz");
        request.addHeader("TESTHeader", "abc");
        Enumeration headers = request.getHeaders("testHeader");
        List list = new ArrayList();
        list.add(headers.nextElement());
        list.add(headers.nextElement());
        assertFalse(headers.hasMoreElements());
        assertTrue(list.contains("xyz"));
        assertTrue(list.contains("abc"));
        request.addHeader("MYHEADER1", "xyz");
        request.addHeader("myHeader2", "abc");
        assertEquals("xyz", request.getHeader("myheader1"));
        assertEquals("abc", request.getHeader("MYHEADER2"));
        headers = request.getHeaderNames();
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
        
        request.clearRequestDispatcherMap();
        assertEquals(0, request.getRequestDispatcherMap().size());
    }
    
    public void testSessionCreation() throws Exception
    {
        request.setSession(new MockHttpSession());
        assertNull(request.getSession(false));
        assertNotNull(request.getSession());
        assertNotNull(request.getSession(false));
        assertNotNull(request.getSession(true));
        request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());
        assertNotNull(request.getSession(true));
        assertNotNull(request.getSession(false));
    }
    
    private class TestAttributeListener implements ServletRequestAttributeListener
    {
        private boolean wasAttributeAddedCalled = false;
        private boolean wasAttributeReplacedCalled = false;
        private boolean wasAttributeRemovedCalled = false;
    
        public void attributeAdded(ServletRequestAttributeEvent event)
        {
            wasAttributeAddedCalled = true;
        }

        public void attributeRemoved(ServletRequestAttributeEvent event)
        {
            wasAttributeRemovedCalled = true;
        }

        public void attributeReplaced(ServletRequestAttributeEvent event)
        {
            wasAttributeReplacedCalled = true;
        }
    
        public void reset()
        {
            wasAttributeAddedCalled = false;
            wasAttributeReplacedCalled = false;
            wasAttributeRemovedCalled = false;
        }
    
        public boolean wasAttributeAddedCalled()
        {
            return wasAttributeAddedCalled;
        }

        public boolean wasAttributeRemovedCalled()
        {
            return wasAttributeRemovedCalled;
        }

        public boolean wasAttributeReplacedCalled()
        {
            return wasAttributeReplacedCalled;
        }
    }

    private class TestAttributeOrderListener implements ServletRequestAttributeListener
    {
        private String addedEventKey;
        private Object addedEventValue;
        private String replacedEventKey;
        private Object replacedEventValue;
        private String removedEventKey;
        private Object removedEventValue;
    
        public void attributeAdded(ServletRequestAttributeEvent event)
        {
            addedEventKey = event.getName();
            addedEventValue = event.getValue();
        }

        public void attributeRemoved(ServletRequestAttributeEvent event)
        {
            removedEventKey = event.getName();
            removedEventValue = event.getValue();
        }

        public void attributeReplaced(ServletRequestAttributeEvent event)
        {
            replacedEventKey = event.getName();
            replacedEventValue = event.getValue();
        }
    
        public String getAddedEventKey()
        {
            return addedEventKey;
        }

        public Object getAddedEventValue()
        {
            return addedEventValue;
        }

        public String getRemovedEventKey()
        {
            return removedEventKey;
        }

        public Object getRemovedEventValue()
        {
            return removedEventValue;
        }

        public String getReplacedEventKey()
        {
            return replacedEventKey;
        }

        public Object getReplacedEventValue()
        {
            return replacedEventValue;
        }
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
