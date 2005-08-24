package com.mockrunner.mock.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletException;

import com.mockrunner.util.common.ArrayUtil;
import com.mockrunner.util.common.StreamUtil;

/**
 * Mock implementation of <code>ServletContext</code>.
 */
public class MockServletContext implements ServletContext
{
    private Map attributes;
    private Map requestDispatchers;
    private Map subContexts;
    private Map initParameters;
    private Map mimeTypes;
    private Map realPaths;
    private Map resources;
    private Map resourcePaths;
    private Map resourceStreams;
    private String servletContextName;
    private List attributeListener;
    
    public MockServletContext()
    {
        resetAll();
    }
    
    /**
     * Resets the state of this object to the default values
     */
    public synchronized void resetAll()
    {
        attributes = new HashMap();
        requestDispatchers = new HashMap();
        subContexts = new HashMap();
        initParameters = new HashMap();
        mimeTypes = new HashMap();
        realPaths = new HashMap();
        resources = new HashMap();
        resourcePaths = new HashMap();
        resourceStreams = new HashMap();
        attributeListener = new ArrayList();
    }

    public synchronized void addAttributeListener(ServletContextAttributeListener listener)
    {
        attributeListener.add(listener);
    }
    
    public synchronized void clearAttributes()
    {
        attributes.clear();
    }
        
    public synchronized Object getAttribute(String key)
    {
        return attributes.get(key);
    }

    public synchronized Enumeration getAttributeNames()
    {
        Vector attKeys = new Vector(attributes.keySet());
        return attKeys.elements();
    }

    public synchronized void removeAttribute(String key)
    {
        Object value = attributes.get(key);
        attributes.remove(key);
        if(null != value)
        {
            callAttributeListenersRemovedMethod(key, value);
        }
    }

    public synchronized void setAttribute(String key, Object value)
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
    
    public synchronized RequestDispatcher getNamedDispatcher(String name)
    {
        return getRequestDispatcher(name);
    }

    public synchronized RequestDispatcher getRequestDispatcher(String path)
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
    public synchronized Map getRequestDispatcherMap()
    {
        return Collections.unmodifiableMap(requestDispatchers);
    }
    
    /**
     * Clears the map of <code>RequestDispatcher</code> objects. 
     */
    public synchronized void clearRequestDispatcherMap()
    {
        requestDispatchers.clear();
    }
    
    /**
     * Sets a <code>RequestDispatcher</code> that will be returned when calling
     * {@link #getRequestDispatcher} or {@link #getNamedDispatcher}
     * with the specified path or name.
     * If no <code>RequestDispatcher</code>
     * is set for the specified path, {@link #getRequestDispatcher} and
     * {@link #getNamedDispatcher} automatically create a new one.
     * @param path the path for the <code>RequestDispatcher</code>
     * @param dispatcher the <code>RequestDispatcher</code> object
     */
    public synchronized void setRequestDispatcher(String path, RequestDispatcher dispatcher)
    {
        if(dispatcher instanceof MockRequestDispatcher)
        {
            ((MockRequestDispatcher)dispatcher).setPath(path);
        }
        requestDispatchers.put(path, dispatcher);
    }
    
    public synchronized ServletContext getContext(String url)
    {
        return (ServletContext)subContexts.get(url);
    }
    
    public synchronized void setContext(String url, ServletContext context)
    {
        subContexts.put(url, context);
    }
    
    public synchronized void clearInitParameters()
    {
        initParameters.clear();
    }

    public synchronized String getInitParameter(String name)
    {
        return (String)initParameters.get(name);
    }
    
    public synchronized void setInitParameter(String name, String value) 
    {
        initParameters.put(name, value);
    }

    public synchronized Enumeration getInitParameterNames()
    {
        return new Vector(initParameters.keySet()).elements();
    }

    public synchronized int getMajorVersion()
    {
        return 2;
    }
    
    public synchronized int getMinorVersion()
    {
        return 3;
    }

    public synchronized String getMimeType(String file)
    {
        return (String)mimeTypes.get(file);
    }
    
    public synchronized void setMimeType(String file, String type)
    {
        mimeTypes.put(file, type);
    }

    public synchronized String getRealPath(String path)
    {
        return (String)realPaths.get(path);
    }
    
    public synchronized void setRealPath(String path, String realPath)
    {
        realPaths.put(path, realPath);
    }

    public synchronized URL getResource(String path) throws MalformedURLException
    {
        return (URL)resources.get(path);
    }
    
    public synchronized void setResource(String path, URL url)
    {
        resources.put(path, url);
    }

    public synchronized InputStream getResourceAsStream(String path)
    {
        byte[] data = (byte[])resourceStreams.get(path);
        if(null == data) return null;
        return new ByteArrayInputStream(data);
    }
    
    public synchronized void setResourceAsStream(String path, InputStream inputStream) 
    { 
        setResourceAsStream(path, StreamUtil.getStreamAsByteArray(inputStream)); 
    }
    
    public synchronized void setResourceAsStream(String path, byte[] data)
    {
        byte[] copy = (byte[])ArrayUtil.copyArray(data);
        resourceStreams.put(path, copy);
    }

    public synchronized Set getResourcePaths(String path)
    {
        Set set = (Set)resourcePaths.get(path);
        if(null == set) return null;
        return Collections.unmodifiableSet(set);
    }
    
    public synchronized void addResourcePaths(String path, Collection pathes)
    {
        Set set = (Set)resourcePaths.get(path);
        if(null == set)
        {
            set = new HashSet();
            resourcePaths.put(path, set);
        }
        set.addAll(pathes);
    }
    
    public synchronized void addResourcePath(String path, String resourcePath)
    {
        ArrayList list = new ArrayList();
        list.add(resourcePath);
        addResourcePaths(path, list);
    }

    public synchronized String getServerInfo()
    {
        return "Mockrunner Server";
    }

    public synchronized Servlet getServlet(String arg0) throws ServletException
    {
        return null;
    }

    public synchronized String getServletContextName()
    {
        return servletContextName;
    }
    
    public synchronized void setServletContextName(String servletContextName)
    {
        this.servletContextName = servletContextName;
    }

    public synchronized Enumeration getServletNames()
    {
        return new Vector().elements();
    }

    public synchronized Enumeration getServlets()
    {
        return new Vector().elements();
    }

    public synchronized void log(Exception exc, String message)
    {

    }

    public synchronized void log(String message, Throwable exc)
    {

    }

    public synchronized void log(String message)
    {

    }
    
    private synchronized void handleAttributeListenerCalls(String key, Object value, Object oldValue)
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
    
    private synchronized void callAttributeListenersAddedMethod(String key, Object value)
    {
        for(int ii = 0; ii < attributeListener.size(); ii++)
        {
            ServletContextAttributeEvent event = new ServletContextAttributeEvent(this, key, value);
            ((ServletContextAttributeListener)attributeListener.get(ii)).attributeAdded(event);
        }
    }

    private synchronized void callAttributeListenersReplacedMethod(String key, Object value)
    {
        for(int ii = 0; ii < attributeListener.size(); ii++)
        {
            ServletContextAttributeEvent event = new ServletContextAttributeEvent(this, key, value);
            ((ServletContextAttributeListener)attributeListener.get(ii)).attributeReplaced(event);
        }
    }

    private synchronized void callAttributeListenersRemovedMethod(String key, Object value)
    {
        for(int ii = 0; ii < attributeListener.size(); ii++)
        {
            ServletContextAttributeEvent event = new ServletContextAttributeEvent(this, key, value);
            ((ServletContextAttributeListener)attributeListener.get(ii)).attributeRemoved(event);
        }
    }
}
