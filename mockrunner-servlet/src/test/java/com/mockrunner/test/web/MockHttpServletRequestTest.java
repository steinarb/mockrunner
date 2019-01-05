package com.mockrunner.test.web;

import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;

import org.apache.tools.ant.types.selectors.SelectorUtils;

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
        request
            .addAttribute("key", "value")
            .addHeader("header", "headervalue")
            .setContentLength(5)
            .resetAll();
        assertNull(request.getAttribute("key"));
        assertNull(request.getHeader("header"));
        assertEquals(-1, request.getContentLength());
    }

    public void testAttributeListenerCalled()
    {
        TestAttributeListener listener1 = new TestAttributeListener();
        TestAttributeListener listener2 = new TestAttributeListener();
        TestAttributeListener listener3 = new TestAttributeListener();
        request
            .addAttributeListener(listener1)
            .addAttributeListener(listener2)
            .addAttributeListener(listener3)
            .addAttribute("key", "value");
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

    @SuppressWarnings("rawtypes")
    public void testFluentApi() throws Exception {
        request
            .setupAddParameter("param1", "value1")
            .clearParameters()
            .setupAddParameter("param2", "value2")
            .setupAddParameter("param3", "value3");
        assertEquals(2, request.getParameterMap().size());
        request
            .addAttribute("attribute1", "avalue1")
            .clearAttributes()
            .addAttribute("attribute2", "avalue2")
            .addAttribute("attribute3", "avalue3");
        Enumeration attributeNames = request.getAttributeNames();
        int attributeNamesSize = getEnumerationSize(attributeNames);
        assertEquals(2, attributeNamesSize);
        Locale defaultLocale = request.getLocale();
        assertNotNull(defaultLocale);
        assertEquals("Expect an empty locales list", 0, getEnumerationSize(request.getLocales()));
        Locale nbNo = Locale.forLanguageTag("nb-NO");
        request.addLocale(nbNo);
        // Verify default locale has changed and that getLocales() is non-empty
        assertNotEquals(defaultLocale, request.getLocale());
        assertEquals(nbNo, request.getLocale());
        assertEquals(1, getEnumerationSize(request.getLocales()));

        Locale nnNo = Locale.forLanguageTag("nn-NO");
        request.addLocales(Arrays.asList(nnNo, Locale.CANADA_FRENCH));
        // Verify that default locale hasn't changed and that the list of locales is changed by two
        assertEquals(nbNo, request.getLocale());
        assertEquals(3, getEnumerationSize(request.getLocales()));

        String basicAuth = "Basic";
        String post = "POST";
        String contextPath = "/myapp";
        String pathinfo = "/servletarg";
        String pathTranslated = "/myapp/servlet/servletarg";
        String queryString = "arg=value";
        String requestUri = "/myapp/servlet/servletarg?arg=value";
        String requestUrl = "http://localhost:8181/myapp/servlet/servletarg?arg=value";
        String servletPath = "/servlet";
        request
            .setAuthType(basicAuth)
            .setMethod(post)
            .setContextPath(contextPath)
            .setPathInfo(pathinfo)
            .setPathTranslated(pathTranslated)
            .setQueryString(queryString)
            .setRequestURI(requestUri)
            .setRequestURL(requestUrl)
            .setServletPath(servletPath);
        assertEquals(basicAuth, request.getAuthType());
        assertEquals(post, request.getMethod());
        assertEquals(contextPath, request.getContextPath());
        assertEquals(pathinfo, request.getPathInfo());
        assertEquals(pathTranslated, request.getPathTranslated());
        assertEquals(queryString, request.getQueryString());
        assertEquals(requestUri, request.getRequestURI());
        assertEquals(requestUrl, request.getRequestURL().toString());
        assertEquals(servletPath, request.getServletPath());

        Principal principal = new Principal() {
                @Override
                public String getName() {
                    return "jad";
                }
            };
        String remoteUser = "jad";
        String utf8 = "UTF-8";
        String contentType = "application/json";
        String https = "https";
        String servername = "localhost";
        int serverPort = 8181;
        String scheme = "HTTPS";
        String remoteAddr = "192.168.10";
        String remoteHost = "client.home.lan";
        request
            .setUserPrincipal(principal)
            .setRemoteUser(remoteUser)
            .setRequestedSessionIdFromCookie(true)
            .addCharacterEncoding(utf8)
            .setContentType(contentType)
            .setProtocol(https)
            .setServerName(servername)
            .setServerPort(serverPort)
            .setScheme(scheme)
            .setRemoteAddr(remoteAddr)
            .setRemoteHost(remoteHost);
        assertEquals(principal, request.getUserPrincipal());
        assertEquals(remoteUser, request.getRemoteUser());
        assertEquals(utf8, request.getCharacterEncoding());
        assertEquals(contentType, request.getContentType());
        assertEquals(https, request.getProtocol());
        assertEquals(servername, request.getServerName());
        assertEquals(serverPort, request.getServerPort());
        assertEquals(scheme, request.getScheme());
        assertEquals(remoteAddr, request.getRemoteAddr());
        assertEquals(remoteHost, request.getRemoteHost());
    }

    public void testFluentCreators() {
        final String url = "http://localhost:8181/myapp/servlet/argument?arg1=value";
        MockHttpServletRequest getRequest = MockHttpServletRequest.getRequest(URI.create(url));
        assertEquals(url, getRequest.getRequestURL().toString());
        assertEquals("GET", getRequest.getMethod());
        MockHttpServletRequest postJsonRequest = MockHttpServletRequest.postJsonRequest(URI.create(url));
        assertEquals(url, postJsonRequest.getRequestURL().toString());
        assertEquals("POST", postJsonRequest.getMethod());
    }

    public void testAttributeListenerValues()
    {
        TestAttributeOrderListener listener = new TestAttributeOrderListener();
        request
            .addAttributeListener(listener)
            .addAttribute("key", "value");
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
        request
            .addAttributeListener(listener)
            .addAttribute("key", null);
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
        request
            .addAttribute("key1", "value1")
            .addAttribute("key2", "value2");
        assertEquals("value1", request.getAttribute("key1"));
        assertEquals("value2", request.getAttribute("key2"));
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
        request
            .addHeader("testHeader", "xyz")
            .addHeader("testHeader", "abc");
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
        assertEquals(1076108388000L, date);
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
        assertFalse(request.getHeaders("doesnotexist").hasMoreElements());
    }

    public void testHeadersCaseInsensitive()
    {
        request
            .addHeader("testHeader", "xyz")
            .addHeader("TESTHeader", "abc");
        Enumeration headers = request.getHeaders("testHeader");
        List list = new ArrayList();
        list.add(headers.nextElement());
        list.add(headers.nextElement());
        assertFalse(headers.hasMoreElements());
        assertTrue(list.contains("xyz"));
        assertTrue(list.contains("abc"));
        request
            .addHeader("MYHEADER1", "xyz")
            .addHeader("myHeader2", "abc");
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
        assertNull(request.getCookies());
        request
            .addCookie(new Cookie("name1", "value1"))
            .addCookie(new Cookie("name2", "value2"))
            .addCookie(new Cookie("name3", "value3"));
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
        request.setBodyContent("test\nanothertest???");
        BufferedReader reader = request.getReader();
        assertEquals("test", reader.readLine());
        assertEquals("anothertest???", reader.readLine());
        assertEquals(-1, reader.read());
        request.setBodyContent(new byte[] {0, -128, 3, 127, 55});
        ServletInputStream stream = request.getInputStream();
        assertEquals(0, stream.read());
        assertEquals(-128, (byte)stream.read());
        assertEquals(3, stream.read());
        assertEquals(127, stream.read());
        assertEquals(55, stream.read());
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
        request.setSession(null);
        assertNull(request.getSession(false));
        assertNull(request.getSession(true));
        assertNull(request.getSession());
        request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        assertNull(request.getSession(false));
        assertNotNull(request.getSession());
        assertNotNull(request.getSession(false));
        assertSame(session, request.getSession(false));
        assertNotNull(request.getSession(true));
        assertSame(session, request.getSession(true));
        request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());
        assertNotNull(request.getSession(true));
        assertNotNull(request.getSession(false));
    }

    public void testSessionInvalidate() throws Exception
    {
        request.setSession(new MockHttpSession());
        request.getSession().invalidate();
        assertFalse(((MockHttpSession)request.getSession(false)).isValid());
        assertTrue(((MockHttpSession)request.getSession(true)).isValid());
    }

    public void testIsUserInRole()
    {
        request
            .setUserInRole("role1", true)
            .setUserInRole("role2", false);
        assertTrue(request.isUserInRole("role1"));
        assertFalse(request.isUserInRole("role2"));
        assertFalse(request.isUserInRole("role3"));
    }

    private int getEnumerationSize(Enumeration enumeration) {
        int size = 0;
        while(enumeration.hasMoreElements()) {
            ++size;
            enumeration.nextElement();
        }

        return size;
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
