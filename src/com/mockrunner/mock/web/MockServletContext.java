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
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.mockrunner.util.ArrayUtil;

/**
 * Mock implementation of <code>ServletContext</code>.
 */
public class MockServletContext implements ServletContext
{
    private Map attributes;
    private MockRequestDispatcher dispatcher;
    private Map subContexts;
    private Map initParameters;
    private Map mimeTypes;
    private Map realPaths;
    private Map resources;
    private Map resourcePaths;
    private Map resourceStreams;
    private String servletContextName;
    
    public MockServletContext()
    {
        attributes = new HashMap();
        dispatcher = new MockRequestDispatcher();
        subContexts = new HashMap();
        initParameters = new HashMap();
        mimeTypes = new HashMap();
        realPaths = new HashMap();
        resources = new HashMap();
        resourcePaths = new HashMap();
        resourceStreams = new HashMap();
    }
    
    public void clearAttributes()
    {
        attributes.clear();
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
        attributes.remove(key);
    }

    public void setAttribute(String key, Object value)
    {
        attributes.put(key, value);
    }
    
    public RequestDispatcher getNamedDispatcher(String arg0)
    {
        return dispatcher;
    }

    public RequestDispatcher getRequestDispatcher(String arg0)
    {
        return dispatcher;
    }
    
    public ServletContext getContext(String url)
    {
        return (ServletContext)subContexts.get(url);
    }
    
    public void setContext(String url, ServletContext context)
    {
        subContexts.put(url, context);
    }

    public String getInitParameter(String name)
    {
        return (String)initParameters.get(name);
    }
    
    public void setInitParameter(String name, String value) 
    {
        initParameters.put(name, value);
    }

    public Enumeration getInitParameterNames()
    {
        return new Vector(initParameters.keySet()).elements();
    }

    public int getMajorVersion()
    {
        return 2;
    }
    
    public int getMinorVersion()
    {
        return 3;
    }

    public String getMimeType(String file)
    {
        return (String)mimeTypes.get(file);
    }
    
    public void setMimeType(String file, String type)
    {
        mimeTypes.put(file, type);
    }

    public String getRealPath(String path)
    {
        return (String)realPaths.get(path);
    }
    
    public void setRealPath(String path, String realPath)
    {
        realPaths.put(path, realPath);
    }

    public URL getResource(String path) throws MalformedURLException
    {
        return (URL)resources.get(path);
    }
    
    public void setResource(String path, URL url)
    {
        resources.put(path, url);
    }

    public InputStream getResourceAsStream(String path)
    {
        byte[] data = (byte[])resourceStreams.get(path);
        if(null == data) return null;
        return new ByteArrayInputStream(data);
    }
    
    public void setResourceAsStream(String path, byte[] data)
    {
        byte[] copy = (byte[])ArrayUtil.copyArray(data);
        resourceStreams.put(path, copy);
    }

    public Set getResourcePaths(String path)
    {
        Set set = (Set)resourcePaths.get(path);
        if(null == set) return null;
        return Collections.unmodifiableSet(set);
    }
    
    public void addResourcePaths(String path, Collection pathes)
    {
        Set set = (Set)resourcePaths.get(path);
        if(null == set)
        {
            set = new HashSet();
            resourcePaths.put(path, set);
        }
        set.addAll(pathes);
    }
    
    public void addResourcePath(String path, String resourcePath)
    {
        ArrayList list = new ArrayList();
        list.add(resourcePath);
        addResourcePaths(path, list);
    }

    public String getServerInfo()
    {
        return "Mockrunner Server";
    }

    public Servlet getServlet(String arg0) throws ServletException
    {
        return null;
    }

    public String getServletContextName()
    {
        return servletContextName;
    }
    
    public void setServletContextName(String servletContextName)
    {
        this.servletContextName = servletContextName;
    }

    public Enumeration getServletNames()
    {
        return new Vector().elements();
    }

    public Enumeration getServlets()
    {
        return new Vector().elements();
    }

    public void log(Exception exc, String message)
    {

    }

    public void log(String message, Throwable exc)
    {

    }

    public void log(String message)
    {

    }
}
