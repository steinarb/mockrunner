package com.mockrunner.test.web;

import static java.util.Collections.list;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockRequestDispatcher;

class MockHttpServletRequestTest
{
    private MockHttpServletRequest request;

    @BeforeEach
    protected void setUp()
    {
        request = new MockHttpServletRequest();
    }

    @AfterEach
    protected void tearDown()
    {
        request = null;
    }

    @Test
    void testResetAll() throws Exception
    {
        request
            .addAttribute("key", "value")
            .addHeader("header", "headervalue")
            .setContentLength(5)
            .resetAll();
        assertThat(request.getAttribute("key")).isNull();;
        assertThat(request.getHeader("header")).isNull();
        assertThat(request.getContentLength()).isEqualTo(-1);
    }

    @Test
    void testAttributeListenerCalled()
    {
        TestAttributeListener listener1 = new TestAttributeListener();
        TestAttributeListener listener2 = new TestAttributeListener();
        TestAttributeListener listener3 = new TestAttributeListener();
        request
            .addAttributeListener(listener1)
            .addAttributeListener(listener2)
            .addAttributeListener(listener3)
            .addAttribute("key", "value");
        assertThat(listener1.wasAttributeAddedCalled()).isTrue();
        assertThat(listener2.wasAttributeAddedCalled()).isTrue();
        assertThat(listener3.wasAttributeAddedCalled()).isTrue();
        assertThat(listener1.wasAttributeReplacedCalled()).isFalse();
        assertThat(listener2.wasAttributeReplacedCalled()).isFalse();
        assertThat(listener3.wasAttributeReplacedCalled()).isFalse();
        listener1.reset();
        listener2.reset();
        listener3.reset();
        request.setAttribute("key", "value1");
        assertThat(listener1.wasAttributeAddedCalled()).isFalse();
        assertThat(listener2.wasAttributeAddedCalled()).isFalse();
        assertThat(listener3.wasAttributeAddedCalled()).isFalse();
        assertThat(listener1.wasAttributeReplacedCalled()).isTrue();
        assertThat(listener2.wasAttributeReplacedCalled()).isTrue();
        assertThat(listener3.wasAttributeReplacedCalled()).isTrue();
        request.removeAttribute("key");
        assertThat(listener1.wasAttributeRemovedCalled()).isTrue();
        assertThat(listener2.wasAttributeRemovedCalled()).isTrue();
        assertThat(listener3.wasAttributeRemovedCalled()).isTrue();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    void testFluentApi() throws Exception {
        request
            .setupAddParameter("param1", "value1")
            .clearParameters()
            .setupAddParameter("param2", "value2")
            .setupAddParameter("param3", "value3");
        assertThat(request.getParameterMap().size()).isEqualTo(2);
        request
            .addAttribute("attribute1", "avalue1")
            .clearAttributes()
            .addAttribute("attribute2", "avalue2")
            .addAttribute("attribute3", "avalue3");
        Enumeration attributeNames = request.getAttributeNames();
        assertThat(list(attributeNames)).hasSize(2);
        Locale defaultLocale = request.getLocale();
        assertThat(defaultLocale).isNotNull();
        assertThat(Collections.list(request.getLocales())).isEmpty();
        Locale nbNo = Locale.forLanguageTag("nb-NO");
        request.addLocale(nbNo);
        // Verify default locale has changed and that getLocales() is non-empty
        assertNotEquals(defaultLocale, request.getLocale());
        assertEquals(nbNo, request.getLocale());
        assertThat(list(request.getLocales())).hasSize(1);

        Locale nnNo = Locale.forLanguageTag("nn-NO");
        request.addLocales(Arrays.asList(nnNo, Locale.CANADA_FRENCH));
        // Verify that default locale hasn't changed and that the list of locales is changed by two
        assertEquals(nbNo, request.getLocale());
        assertThat(Collections.list(request.getLocales())).hasSize(3);

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

    @Test
    void testFluentCreators() {
        final String url = "http://localhost:8181/myapp/servlet/argument?arg1=value";
        MockHttpServletRequest getRequest = MockHttpServletRequest.getRequest(URI.create(url));
        assertEquals(url, getRequest.getRequestURL().toString());
        assertEquals("GET", getRequest.getMethod());
        MockHttpServletRequest postJsonRequest = MockHttpServletRequest.postJsonRequest(URI.create(url));
        assertEquals(url, postJsonRequest.getRequestURL().toString());
        assertEquals("POST", postJsonRequest.getMethod());
    }

    @Test
    void testAttributeListenerValues()
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

    @Test
    void testAttributeListenerNullValue()
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    void testGetAttributeNames()
    {
        Enumeration enumeration = request.getAttributeNames();
        assertThat(enumeration.hasMoreElements()).isFalse();
        request.setAttribute("key", null);
        enumeration = request.getAttributeNames();
        assertThat(enumeration.hasMoreElements()).isFalse();
        request
            .addAttribute("key1", "value1")
            .addAttribute("key2", "value2");
        assertEquals("value1", request.getAttribute("key1"));
        assertEquals("value2", request.getAttribute("key2"));
        enumeration = request.getAttributeNames();
        List testList = new ArrayList();
        testList.add(enumeration.nextElement());
        testList.add(enumeration.nextElement());
        assertThat(enumeration.hasMoreElements()).isFalse();
        assertThat(testList.contains("key1")).isTrue();
        assertThat(testList.contains("key2")).isTrue();
        request.setAttribute("key2", null);
        assertThat(request.getAttribute("key2")).isNull();;
        enumeration = request.getAttributeNames();
        testList = new ArrayList();
        testList.add(enumeration.nextElement());
        assertThat(enumeration.hasMoreElements()).isFalse();
        assertThat(testList).contains("key1");
        request.setAttribute("key1", null);
        assertThat(request.getAttribute("key1")).isNull();;
        enumeration = request.getAttributeNames();
        assertThat(enumeration.hasMoreElements()).isFalse();
    }

    @Test
    void testAddRequestParameter() throws Exception
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    void testHeaders()
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    void testHeadersCaseInsensitive()
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

    @Test
    void testCookies()
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

    @Test
    void testBodyContent() throws Exception
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

    @Test
    void testRequestDispatcher() throws Exception
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

    @Test
    void testSessionCreation() throws Exception
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

    @Test
    void testSessionInvalidate() throws Exception
    {
        request.setSession(new MockHttpSession());
        request.getSession().invalidate();
        assertFalse(((MockHttpSession)request.getSession(false)).isValid());
        assertTrue(((MockHttpSession)request.getSession(true)).isValid());
    }

    @Test
    void testIsUserInRole()
    {
        request
            .setUserInRole("role1", true)
            .setUserInRole("role2", false);
        assertTrue(request.isUserInRole("role1"));
        assertFalse(request.isUserInRole("role2"));
        assertFalse(request.isUserInRole("role3"));
    }

    @Test
    void testThatSetContentTypeAlsoSetsHeaderValue() {
        String contentType = "application/octet-stream";
        MockHttpServletRequest request = new MockHttpServletRequest().setContentType(contentType);
        assertThat(request.getHeader(MockHttpServletRequest.CONTENT_TYPE)).isEqualTo(contentType);
    }

    private class TestAttributeListener implements ServletRequestAttributeListener {
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

    private class TestAttributeOrderListener implements ServletRequestAttributeListener {
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

    private class TestRequestDispatcher implements RequestDispatcher {

        public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException
        {

        }

        public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException
        {

        }
    }
}
