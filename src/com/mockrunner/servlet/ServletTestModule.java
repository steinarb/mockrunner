package com.mockrunner.servlet;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.mock.web.WebMockObjectFactory;

/**
 * Module for servlet and filter tests. Can test
 * single servlets and filters and simulate a filter
 * chain.
 */
public class ServletTestModule extends HTMLOutputModule
{
    private final static Log log = LogFactory.getLog(ServletTestModule.class);
    private WebMockObjectFactory mockFactory;
    private HttpServlet servlet;
    private boolean doChain;
      
    public ServletTestModule(WebMockObjectFactory mockFactory)
    {
        super(mockFactory);
        this.mockFactory = mockFactory;
        doChain = false;
    }
    
    /**
     * Creates a servlet and initializes it. <code>servletClass</code> must
     * be of the type <code>HttpServlet</code>, otherwise a
     * <code>RuntimeException</code> will be thrown.
     * Sets the specified servlet as the current servlet and
     * initializes the filter chain with it.
     * @param servletClass the class of the servlet
     * @return instance of <code>HttpServlet</code>
     * @throws RuntimeException if <code>servletClass</code> is not an
     *         instance of <code>HttpServlet</code>
     */
    public HttpServlet createServlet(Class servletClass)
    {
        if(!HttpServlet.class.isAssignableFrom(servletClass))
        {
            throw new RuntimeException("servletClass must be an instance of javax.servlet.http.HttpServlet");
        }
        try
        {
            HttpServlet theServlet = (HttpServlet)servletClass.newInstance();
            setServlet(theServlet, true);
            return theServlet;
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Sets the specified servlet as the current servlet without initializing it. 
     * You have to set the <code>ServletConfig</code> on your own.
     * Usually you can use 
     * {@link com.mockrunner.mock.web.WebMockObjectFactory#getMockServletConfig}.
     * @param servlet the servlet
     */
    public void setServlet(HttpServlet servlet)
    {
        setServlet(servlet, false);
    }
    
    /**
     * Sets the specified servlet as the current servlet.
     * Initializes it, if <code>doInit</code> is <code>true</code>.
     * @param servlet the servlet
     * @param doInit should <code>init</code> be called
     */
    public void setServlet(HttpServlet servlet, boolean doInit)
    {
        try
        {
	        this.servlet = servlet;
	        if(doInit)
	        {
	            servlet.init(mockFactory.getMockServletConfig());
	        }
	        mockFactory.getMockFilterChain().setServlet(servlet);
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Returns the current servlet.
     * @return the servlet
     */
    public HttpServlet getServlet()
    {
        return servlet;
    }
    
    /**
     * Creates a filter, initializes it and adds it to the
     * filter chain. <code>filterClass</code> must be of the type 
     * <code>Filter</code>, otherwise a <code>RuntimeException</code> 
     * will be thrown. You can loop through the filter chain with
     * {@link #doFilter}. If you set <code>doChain</code> to
     * <code>true</code> every call of one of the servlet methods 
     * will go through the filter chain before calling the servlet 
     * method.
     * @param filterClass the class of the filter
     * @return instance of <code>Filter</code>
     * @throws RuntimeException if <code>filterClass</code> is not an
     *         instance of <code>Filter</code>
     */
    public Filter createFilter(Class filterClass)
    {
        if(!Filter.class.isAssignableFrom(filterClass))
        {
            throw new RuntimeException("filterClass must be an instance of javax.servlet.Filter");
        }
        try
        {
            Filter theFilter = (Filter)filterClass.newInstance();
            addFilter(theFilter, true);
            return theFilter;
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Adds the specified filter to the filter chain without
     * initializing it. 
     * You have to set the <code>FilterConfig</code> on your own.
     * Usually you can use 
     * {@link com.mockrunner.mock.web.WebMockObjectFactory#getMockFilterConfig}.
     * @param filter the filter
     */
    public void addFilter(Filter filter)
    {
        addFilter(filter, false);
    }
    
    /**
     * Adds the specified filter it to the filter chain. Initializes it,
     * if <code>doInit</code> is <code>true</code>.
     * @param filter the filter
     * @param doInit should <code>init</code> be called
     */
    public void addFilter(Filter filter, boolean doInit)
    {
        if(doInit)
        {
            try
            {
                filter.init(mockFactory.getMockFilterConfig());
            }
            catch(Exception exc)
            {
                log.error(exc.getMessage(), exc);
                throw new NestedApplicationException(exc);
            }
        }
        mockFactory.getMockFilterChain().addFilter(filter);
    }
    
    /**
     * Deletes all filters in the filter chain.
     */
    public void releaseFilters()
    {
        mockFactory.getMockFilterChain().release();
        mockFactory.getMockFilterChain().setServlet(servlet);
    }

    /**
     * If <code>doChain</code> is set to <code>true</code>
     * (default is <code>false</code>) every call of
     * one of the servlet methods will go through the filter chain
     * before calling the servlet method.
     * @param doChain <code>true</code> if the chain should be called
     */
    public void setDoChain(boolean doChain)
    {
        this.doChain = doChain;
    }
    
    /**
     * Loops through the filter chain and calls the current servlets
     * <code>service</code> method at the end (only if a current servlet
     * is set). You can use it to test single filters or the interaction 
     * of filters and servlets.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * this method is called before any call of a servlet method. If a filter
     * does not call it's chains <code>doFilter</code> method, the chain
     * breaks and the servlet will not be called (just like it in the
     * real container).
     */
    public void doFilter()
    {
        try
        {
            mockFactory.getMockFilterChain().doFilter(mockFactory.getWrappedRequest(), mockFactory.getWrappedResponse());
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Calls the current servlets <code>init</code> method. Is automatically
     * done when calling {@link #createServlet}.
     */
    public void init()
    {
        try
        {
            servlet.init();
        }
        catch(ServletException exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Calls the current servlets <code>doDelete</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doDelete</code>.
     */
    public void doDelete()
    {
        mockFactory.getMockRequest().setMethod("DELETE");
        callService();
    }
    
    /**
     * Calls the current servlets <code>doGet</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doGet</code>.
     */          
    public void doGet()
    {
        mockFactory.getMockRequest().setMethod("GET");
        callService();
    }
    
    /**
     * Calls the current servlets <code>doOptions</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doOptions</code>.
     */          
    public void doOptions()
    {
        mockFactory.getMockRequest().setMethod("OPTIONS");
        callService();
    }
    
    /**
     * Calls the current servlets <code>doPost</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doPost</code>.
     */         
    public void doPost()
    {
        mockFactory.getMockRequest().setMethod("POST");
        callService();
    }
    
    /**
     * Calls the current servlets <code>doPut</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doPut</code>.
     */         
    public void doPut()
    {
        mockFactory.getMockRequest().setMethod("PUT");
        callService();
    }
    
    /**
     * Calls the current servlets <code>doTrace</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doTrace</code>.
     */          
    public void doTrace()
    {
        mockFactory.getMockRequest().setMethod("TRACE");
        callService();
    }
    
    /**
     * Calls the current servlets <code>doHead</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doHead</code>.
     */          
    public void doHead()
    {
        mockFactory.getMockRequest().setMethod("HEAD");
        callService();
    }
    
    /**
     * Calls the current servlets <code>service</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>service</code>.
     */          
    public void service()
    {
        callService();
    }
    
    /**
     * Returns the last request from the filter chain. Since
     * filters can replace the request with a request wrapper,
     * this method makes only sense after calling at least
     * one filter, i.e. after calling {@link #doFilter} or
     * after calling one servlet method with <i>doChain</i> 
     * set to <code>true</code>.
     * @return the filtered request
     */  
    public ServletRequest getFilteredRequest()
    {
        return mockFactory.getMockFilterChain().getLastRequest();
    }
    
    /**
     * Returns the last response from the filter chain. Since
     * filters can replace the response with a response wrapper,
     * this method makes only sense after calling at least
     * one filter, i.e. after calling {@link #doFilter} or
     * after calling one servlet method with <i>doChain</i> 
     * set to <code>true</code>.
     * @return the filtered response
     */  
    public ServletResponse getFilteredResponse()
    {
        return mockFactory.getMockFilterChain().getLastResponse();
    }
    
    /**
     * Returns the servlet output as a string. Flushes the output
     * before returning it.
     * @return the servlet output
     */
    public String getOutput()
    {
        try
        {
            mockFactory.getMockResponse().getWriter().flush();    
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
        }
        return mockFactory.getMockResponse().getOutputStreamContent();
    }
    
    /**
     * Clears the output content
     */ 
    public void clearOutput()
    {
        mockFactory.getMockResponse().resetBuffer();
    }
    
    private void callService()
    {
        try
        {
            if(doChain)
            { 
                doFilter(); 
            }
            else
            {
                servlet.service(mockFactory.getWrappedRequest(), mockFactory.getWrappedResponse());
            }            
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }  
}
