package com.mockrunner.mock.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.util.common.CaseAwareMap;

/**
 * Mock implementation of <code>HttpServletRequest</code>.
 */
public class MockHttpServletRequest implements HttpServletRequest
{
    private Map attributes;
    private Map parameters;
    private Vector locales;
    private Map requestDispatchers;
    private HttpSession session;
    private String method;
    private String authType;
    private Map headers;
    private String contextPath;
    private String pathInfo;
    private String pathTranslated;
    private String queryString;
    private StringBuffer requestUrl;
    private String requestUri;
    private String servletPath;
    private Principal principal;
    private String remoteUser;
    private boolean requestedSessionIdIsFromCookie;
    private String protocol;
    private String serverName;
    private int serverPort;
    private String scheme;
    private String remoteHost;
    private String remoteAddr;
    private Map roles;
    private String characterEncoding;
    private int contentLength;
    private String contentType;
    private List cookies;
    private MockServletInputStream bodyContent;
    private String localAddr;
    private String localName;
    private int localPort;
    private int remotePort;
    private boolean sessionCreated;
    private List attributeListener;
    private boolean isAsyncSupported;

    public MockHttpServletRequest()
    {
        resetAll();
    }

    /**
     * Convenience function to create an mock GET {@link HttpServletRequest}
     * from an URL.
     *
     * @param uri the URL to create an HTTP GET request from
     * @return a mock {@link HttpServletRequest}
     */
    public static MockHttpServletRequest getRequest(URI uri) {
        return createRequest(uri).setMethod("GET");
    }

    /**
     * Convenience function to create an mock POST {@link HttpServletRequest}
     * from an URL.
     *
     * @param uri the URL to create an HTTP GET request from
     * @return a mock {@link HttpServletRequest}
     */
    public static MockHttpServletRequest postRequest(URI uri) {
        return createRequest(uri).setMethod("POST");
    }

    /**
     * Convenience function to create an mock POST {@link HttpServletRequest}
     * from an URL with Content-Type set to "application/json" and
     * Character-Encoding set to "UTF-8".
     *
     * @param uri the URL to create an HTTP GET request from
     * @return a mock {@link HttpServletRequest}
     */
    public static MockHttpServletRequest postJsonRequest(URI uri) {
        final String contentType = "application/json";
        final String encoding = "UTF-8";
        try {
            return createRequest(uri)
                .setMethod("POST")
                .setContentType(contentType)
                .addCharacterEncoding(encoding)
                .addHeader("Content-Type", contentType)
                .addHeader("Content-Transfer-Encoding", encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to create POST JSON MockHttpServletRequest", e);
        }
    }

    /**
     * Convenience function to create an mock {@link HttpServletRequest}
     * from an URL.
     *
     * @param uri the URL to create an HTTP GET request from
     * @return a mock {@link HttpServletRequest}
     */
    public static MockHttpServletRequest createRequest(URI uri) {
        try {
            URL url = uri.toURL();
            return new MockHttpServletRequest()
                .setProtocol(url.getProtocol())
                .setScheme(uri.getScheme())
                .setRequestURL(url.toString())
                .setRequestURI(uri.getPath())
                .setPathInfo(url.getPath());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to create MockHttpServletRequest", e);
        }
    }

    /**
     * Resets the state of this object to the default values
     */
    public void resetAll()
    {
        attributes = new HashMap();
        parameters = new HashMap();
        locales = new Vector();
        requestDispatchers = new HashMap();
        method = "GET";
        headers = new CaseAwareMap();
        requestedSessionIdIsFromCookie = true;
        protocol = "HTTP/1.1";
        serverName = "localhost";
        serverPort = 8080;
        scheme = "http";
        remoteHost = "localhost";
        remoteAddr = "127.0.0.1";
        roles = new HashMap();
        contentLength = -1;
        cookies = null;
        localAddr = "127.0.0.1";
        localName = "localhost";
        localPort = 8080;
        remotePort = 5000;
        sessionCreated = false;
        attributeListener = new ArrayList();
        bodyContent = new MockServletInputStream(new byte[0]);
        isAsyncSupported = false;
    }

    public MockHttpServletRequest addAttributeListener(ServletRequestAttributeListener listener)
    {
        attributeListener.add(listener);
        return this;
    }

    public String getParameter(String key)
    {
        String[] values = getParameterValues(key);
        if (null != values && 0 < values.length)
        {
            return values[0];
        }
        return null;
    }

    /**
     * Clears the parameters.
     * @return a reference to the request (fluent API)
     */
    public MockHttpServletRequest clearParameters()
    {
        parameters.clear();
        return this;
    }

    public String[] getParameterValues(String key)
    {
        return (String[])parameters.get(key);
    }

    /**
     * Adds a request multivalue parameter.
     * @param key the parameter key
     * @param values the parameters values
     * @return a reference to the request (fluent API)
     */
    public MockHttpServletRequest setupAddParameter(String key, String[] values)
    {
        parameters.put(key, values);
        return this;
    }

    /**
     * Adds a request parameter.
     * @param key the parameter key
     * @param value the parameters value
     * @return a reference to the request (fluent API)
     */
    public MockHttpServletRequest setupAddParameter(String key, String value)
    {
        setupAddParameter(key, new String[] { value });
        return this;
    }

    public Enumeration getParameterNames()
    {
        Vector parameterKeys = new Vector(parameters.keySet());
        return parameterKeys.elements();
    }

    public Map getParameterMap()
    {
        return Collections.unmodifiableMap(parameters);
    }

    public MockHttpServletRequest clearAttributes()
    {
        attributes.clear();
        return this;
    }

    public Object getAttribute(String key)
    {
        return attributes.get(key);
    }

    public Enumeration getAttributeNames()
    {
        Vector attKeys = new Vector(attributes.keySet());
        return attKeys.elements();
    }

    public void removeAttribute(String key)
    {
        Object value = attributes.get(key);
        attributes.remove(key);
        if(null != value)
        {
            callAttributeListenersRemovedMethod(key, value);
        }
    }

    public void setAttribute(String key, Object value)
    {
        Object oldValue = attributes.get(key);
        if(null == value)
        {
            attributes.remove(key);
        }
        else
        {
            attributes.put(key, value);
        }
        handleAttributeListenerCalls(key, value, oldValue);
    }

    public MockHttpServletRequest addAttribute(String key, Object value)
    {
        setAttribute(key, value);
        return this;
    }

    public HttpSession getSession()
    {
        return getSession(true);
    }

    public HttpSession getSession(boolean create)
    {
        if(!create && !sessionCreated) return null;
        if(create)
        {
            sessionCreated = true;
            if(session instanceof MockHttpSession)
            {
                if(!((MockHttpSession)session).isValid())
                {
                    ((MockHttpSession)session).resetAll();
                }
            }
        }
        return session;
    }

    /**
     * Sets the <code>HttpSession</code>.
     * @param session the <code>HttpSession</code>
     * @return a reference to the request (fluent API)
     */
    public MockHttpServletRequest setSession(HttpSession session)
    {
        this.session = session;
        return this;
    }

    public RequestDispatcher getRequestDispatcher(String path)
    {
        RequestDispatcher dispatcher = (RequestDispatcher)requestDispatchers.get(path);
        if(null == dispatcher)
        {
            dispatcher = new MockRequestDispatcher();
            setRequestDispatcher(path, dispatcher);
        }
        return dispatcher;
    }

    /**
     * Returns the map of <code>RequestDispatcher</code> objects. The specified path
     * maps to the corresponding <code>RequestDispatcher</code> object.
     * @return the map of <code>RequestDispatcher</code> objects
     */
    public Map getRequestDispatcherMap()
    {
        return Collections.unmodifiableMap(requestDispatchers);
    }

    /**
     * Clears the map of <code>RequestDispatcher</code> objects.
     * @return a reference to the request (fluent API)
     */
    public MockHttpServletRequest clearRequestDispatcherMap()
    {
        requestDispatchers.clear();
        return this;
    }

    /**
     * Sets a <code>RequestDispatcher</code> that will be returned when calling
     * {@link #getRequestDispatcher} with the specified path. If no <code>RequestDispatcher</code>
     * is set for the specified path, {@link #getRequestDispatcher} automatically creates a
     * new one.
     * @param path the path for the <code>RequestDispatcher</code>
     * @param dispatcher the <code>RequestDispatcher</code> object
     * @return a reference to the request (fluent API)
     */
    public MockHttpServletRequest setRequestDispatcher(String path, RequestDispatcher dispatcher)
    {
        if(dispatcher instanceof MockRequestDispatcher)
        {
            ((MockRequestDispatcher)dispatcher).setPath(path);
        }
        requestDispatchers.put(path, dispatcher);
        return this;
    }

    public Locale getLocale()
    {
        if(locales.size() < 1) return Locale.getDefault();
        return (Locale)locales.get(0);
    }

    public Enumeration getLocales()
    {
        return locales.elements();
    }

    public MockHttpServletRequest addLocale(Locale locale)
    {
        locales.add(locale);
        return this;
    }

    public MockHttpServletRequest addLocales(List localeList)
    {
        locales.addAll(localeList);
        return this;
    }

    public String getMethod()
    {
        return method;
    }

    public MockHttpServletRequest setMethod(String method)
    {
        this.method = method;
        return this;
    }

    public String getAuthType()
    {
        return authType;
    }

    public MockHttpServletRequest setAuthType(String authType)
    {
        this.authType = authType;
        return this;
    }

    public long getDateHeader(String key)
    {
        String header = getHeader(key);
        if(null == header) return -1;
        try
        {
            Date dateValue = new SimpleDateFormat(WebConstants.DATE_FORMAT_HEADER, Locale.US).parse(header);
            return dateValue.getTime();
        }
        catch (ParseException exc)
        {
            throw new IllegalArgumentException(exc.getMessage());
        }
    }

    public String getHeader(String key)
    {
        List headerList = (List)headers.get(key);
        if(null == headerList || 0 == headerList.size()) return null;
        return (String)headerList.get(0);
    }

    public Enumeration getHeaderNames()
    {
        return new Vector(headers.keySet()).elements();
    }

    public Enumeration getHeaders(String key)
    {
        List headerList = (List)headers.get(key);
        if(null == headerList) return new Vector().elements();
        return new Vector(headerList).elements();
    }

    public int getIntHeader(String key)
    {
        String header = getHeader(key);
        if(null == header) return -1;
        return new Integer(header);
    }

    public MockHttpServletRequest addHeader(String key, String value)
    {
        List valueList = (List) headers.get(key);
        if (null == valueList)
        {
            valueList = new ArrayList();
            headers.put(key, valueList);
        }
        valueList.add(value);
        return this;
    }

    public void setHeader(String key, String value)
    {
        List valueList = new ArrayList();
        headers.put(key, valueList);
        valueList.add(value);
    }

    public MockHttpServletRequest clearHeaders()
    {
        headers.clear();
        return this;
    }

    public String getContextPath()
    {
        return contextPath;
    }

    public MockHttpServletRequest setContextPath(String contextPath)
    {
        this.contextPath = contextPath;
        return this;
    }

    public String getPathInfo()
    {
        return pathInfo;
    }

    public MockHttpServletRequest setPathInfo(String pathInfo)
    {
        this.pathInfo = pathInfo;
        return this;
    }

    public String getPathTranslated()
    {
        return pathTranslated;
    }

    public MockHttpServletRequest setPathTranslated(String pathTranslated)
    {
        this.pathTranslated = pathTranslated;
        return this;
    }

    public String getQueryString()
    {
        return queryString;
    }

    public MockHttpServletRequest setQueryString(String queryString)
    {
        this.queryString = queryString;
        return this;
    }

    public String getRequestURI()
    {
        return requestUri;
    }

    public MockHttpServletRequest setRequestURI(String requestUri)
    {
        this.requestUri = requestUri;
        return this;
    }

    public StringBuffer getRequestURL()
    {
        return requestUrl;
    }

    public MockHttpServletRequest setRequestURL(String requestUrl)
    {
        this.requestUrl = new StringBuffer(requestUrl);
        return this;
    }

    public String getServletPath()
    {
        return servletPath;
    }

    public MockHttpServletRequest setServletPath(String servletPath)
    {
        this.servletPath = servletPath;
        return this;
    }

    public Principal getUserPrincipal()
    {
        return principal;
    }

    public MockHttpServletRequest setUserPrincipal(Principal principal)
    {
        this.principal = principal;
        return this;
    }

    public String getRemoteUser()
    {
        return remoteUser;
    }

    public MockHttpServletRequest setRemoteUser(String remoteUser)
    {
        this.remoteUser = remoteUser;
        return this;
    }

    public Cookie[] getCookies()
    {
        if(null == cookies) return null;
        return (Cookie[])cookies.toArray(new Cookie[cookies.size()]);
    }

    public MockHttpServletRequest addCookie(Cookie cookie)
    {
        if(null == cookies)
        {
            cookies = new ArrayList();
        }
        cookies.add(cookie);
        return this;
    }

    public String getRequestedSessionId()
    {
        HttpSession session = getSession();
        if(null == session) return null;
        return session.getId();
    }

    public boolean isRequestedSessionIdFromCookie()
    {
        return requestedSessionIdIsFromCookie;
    }

    public boolean isRequestedSessionIdFromUrl()
    {
        return isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdFromURL()
    {
        return !requestedSessionIdIsFromCookie;
    }

    public MockHttpServletRequest setRequestedSessionIdFromCookie(boolean requestedSessionIdIsFromCookie)
    {
        this.requestedSessionIdIsFromCookie = requestedSessionIdIsFromCookie;
        return this;
    }

    public boolean isRequestedSessionIdValid()
    {
        HttpSession session = getSession();
        return null != session;
    }

    public boolean isUserInRole(String role)
    {
        if(!roles.containsKey(role)) return false;
        return (Boolean) roles.get(role);
    }

    public MockHttpServletRequest setUserInRole(String role, boolean isInRole)
    {
        roles.put(role, isInRole);
        return this;
    }

    public String getCharacterEncoding()
    {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException
    {
        this.characterEncoding = characterEncoding;
    }

    public MockHttpServletRequest addCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException
    {
        setCharacterEncoding(characterEncoding);
        return this;
    }

    public int getContentLength()
    {
        return contentLength;
    }

    public MockHttpServletRequest setContentLength(int contentLength)
    {
        this.contentLength = contentLength;
        return this;
    }

    public String getContentType()
    {
        return contentType;
    }

    public MockHttpServletRequest setContentType(String contentType)
    {
        this.contentType = contentType;
        return this;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public MockHttpServletRequest setProtocol(String protocol)
    {
        this.protocol = protocol;
        return this;
    }

    public String getServerName()
    {
        return serverName;
    }

    public MockHttpServletRequest setServerName(String serverName)
    {
        this.serverName = serverName;
        return this;
    }

    public int getServerPort()
    {
        return serverPort;
    }

    public MockHttpServletRequest setServerPort(int serverPort)
    {
        this.serverPort = serverPort;
        return this;
    }

    public String getScheme()
    {
        return scheme;
    }

    public MockHttpServletRequest setScheme(String scheme)
    {
        this.scheme = scheme;
        return this;
    }

    public String getRemoteAddr()
    {
        return remoteAddr;
    }

    public MockHttpServletRequest setRemoteAddr(String remoteAddr)
    {
        this.remoteAddr = remoteAddr;
        return this;
    }

    public String getRemoteHost()
    {
        return remoteHost;
    }

    public MockHttpServletRequest setRemoteHost(String remoteHost)
    {
        this.remoteHost = remoteHost;
        return this;
    }

    public BufferedReader getReader() throws IOException
    {
        return new BufferedReader(new InputStreamReader(bodyContent));
    }

    public ServletInputStream getInputStream() throws IOException
    {
        return bodyContent;
    }

    public MockHttpServletRequest setBodyContent(byte[] data)
    {
        bodyContent = new MockServletInputStream(data);
        return this;
    }

    public MockHttpServletRequest setBodyContent(String bodyContent)
    {
        String encoding = (null == characterEncoding) ? "ISO-8859-1" : characterEncoding;
        try
        {
            setBodyContent(bodyContent.getBytes(encoding));
        }
        catch(UnsupportedEncodingException exc)
        {
            throw new NestedApplicationException(exc);
        }
        return this;
    }

    public String getRealPath(String path)
    {
        HttpSession session = getSession();
        if(null == session) return null;
        return session.getServletContext().getRealPath(path);
    }

    public boolean isSecure()
    {
        String scheme = getScheme();
        if(null == scheme) return false;
        return scheme.equals("https");
    }

    public String getLocalAddr()
    {
        return localAddr;
    }

    public MockHttpServletRequest setLocalAddr(String localAddr)
    {
        this.localAddr = localAddr;
        return this;
    }

    public String getLocalName()
    {
        return localName;
    }

    public MockHttpServletRequest setLocalName(String localName)
    {
        this.localName = localName;
        return this;
    }

    public int getLocalPort()
    {
        return localPort;
    }

    public MockHttpServletRequest setLocalPort(int localPort)
    {
        this.localPort = localPort;
        return this;
    }

    public int getRemotePort()
    {
        return remotePort;
    }

    public MockHttpServletRequest setRemotePort(int remotePort)
    {
        this.remotePort = remotePort;
        return this;
    }

    public boolean isAsyncSupported()
    {
        return isAsyncSupported;
    }

    public MockHttpServletRequest setAsyncSupported(boolean isAsyncSupported)
    {
        this.isAsyncSupported = isAsyncSupported;
        return this;
    }

    private void handleAttributeListenerCalls(String key, Object value, Object oldValue)
    {
        if(null != oldValue)
        {
            if(value != null)
            {
                callAttributeListenersReplacedMethod(key, oldValue);
            }
            else
            {
                callAttributeListenersRemovedMethod(key, oldValue);
            }
        }
        else
        {
            if(value != null)
            {
                callAttributeListenersAddedMethod(key, value);
            }

        }
    }

    private void callAttributeListenersAddedMethod(String key, Object value)
    {
        for (Object anAttributeListener : attributeListener) {
            ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(getServletContext(), this, key,
                                                                                  value);
            ((ServletRequestAttributeListener) anAttributeListener).attributeAdded(event);
        }
    }

    private void callAttributeListenersReplacedMethod(String key, Object value)
    {
        for (Object anAttributeListener : attributeListener) {
            ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(getServletContext(), this, key,
                                                                                  value);
            ((ServletRequestAttributeListener) anAttributeListener).attributeReplaced(event);
        }
    }

    private void callAttributeListenersRemovedMethod(String key, Object value)
    {
        for (Object anAttributeListener : attributeListener) {
            ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(getServletContext(), this, key,
                                                                                  value);
            ((ServletRequestAttributeListener) anAttributeListener).attributeRemoved(event);
        }
    }

    private ServletContext getServletContext()
    {
        if(null == session) return new MockServletContext();
        return session.getServletContext();
    }
}
