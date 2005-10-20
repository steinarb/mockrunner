package com.mockrunner.mock.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of <code>FilterChain</code>.
 */
public class MockFilterChain implements FilterChain
{
    private final static Log log = LogFactory.getLog(MockFilterChain.class);
    private List filters = new ArrayList();
    private Servlet servlet;
    private Iterator iterator;
    private List requestList = new ArrayList();
    private List responseList = new ArrayList();
    
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException
    {
        requestList.add(request);
        responseList.add(response);
        if(null == iterator)
        {
            iterator = filters.iterator();
        }
        if(iterator.hasNext())
        {
            Filter nextFilter = (Filter)iterator.next();
            nextFilter.doFilter(request, response, this);
        }
        else
        {
            iterator = null;
            if(null == servlet) return;
            servlet.service(request, response);
        }
    }

    /**
     * Adds a filter to the chain.
     * @param filter the filter
     */
    public void addFilter(Filter filter) 
    {
        filters.add(filter);
    }
    
    /**
     * Adds a filter to the chain. The filter must implement
     * <code>javax.servlet.Filter</code>.
     * @param filterClass the filter class
     * @throws IllegalArgumentException if the specified class does not implement
     *         <code>javax.servlet.Filter</code>
     */
    public void addFilter(Class filterClass) 
    {
        if(!Filter.class.isAssignableFrom(filterClass))
        {
            throw new IllegalArgumentException("filterClass must be an instance of javax.servlet.Filter");
        }
        try
        {
            filters.add(filterClass.newInstance());
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Sets the servlet that is called at the end of the chain.
     * @param servlet the servlet
     */
    public void setServlet(Servlet servlet) 
    {
        this.servlet = servlet;
    }

    /**
     * Clears all filters and sets the current servlet to <code>null</code>.
     */
    public void release()
    {
        filters.clear();
        setServlet(null);
    }
    
    /**
     * Returns the list of all request objects used to call
     * {@link #doFilter} when iterating through the chain.
     * @return the request list
     */
    public List getRequestList()
    {
        return Collections.unmodifiableList(requestList);
    }
    
    /**
     * Returns the list of all response objects used to call
     * {@link #doFilter} when iterating through the chain.
     * @return the response list
     */
    public List getResponseList()
    {
        return Collections.unmodifiableList(responseList);
    }
    
    /**
     * Returns the last request, usually the request that was
     * used to call the final servlet. Returns <code>null</code>
     * if no request is specified, e.g. if the chain wasn't called.
     * Otherwise returns the last entry of the list returned by
     * {@link #getRequestList}.
     * @return the last request
     */
    public ServletRequest getLastRequest()
    {
        if(requestList.isEmpty()) return null;
        return (ServletRequest)requestList.get(requestList.size() - 1);
    }

    /**
     * Returns the last response, usually the response that was
     * used to call the final servlet. Returns <code>null</code>
     * if no response is specified, e.g. if the chain wasn't called.
     * Otherwise returns the last entry of the list returned by
     * {@link #getResponseList}.
     * @return the last response
     */
    public ServletResponse getLastResponse()
    {
        if(responseList.isEmpty()) return null;
        return (ServletResponse)responseList.get(responseList.size() - 1);
    }
}
