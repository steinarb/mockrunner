package com.mockrunner.servlet;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.MockObjectFactory;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.MockFilterChain;

/**
 * Module for servlet and filter tests. Can test
 * single servlets and filters or simulate a filter
 * chain.
 */
public class ServletTestModule extends HTMLOutputModule
{
    private MockObjectFactory mockFactory;
    private HttpServlet servlet;
    private Filter filter;
    private boolean doChain;
      
    public ServletTestModule(MockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
        doChain = false;
    }
    
    /**
     * Creates a servlet and initializes it. <i>servletClass</i> must
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
            servlet = (HttpServlet)servletClass.newInstance();
            servlet.init(mockFactory.getMockServletConfig());
            mockFactory.getMockFilterChain().setServlet(servlet);
            return servlet;
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
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
     * Creates a filter and initializes it. <i>filterClass</i> must
     * be of the type <code>Filter</code>, otherwise a
     * <code>RuntimeException</code> will be thrown.
     * Sets the filter as the current filter without adding
     * it to the filter chain. If you set <i>doChain</i> to
     * <code>true</code> only filters added with one of the
     * <code>add</code> methods will be called while going
     * through the chain. The purpose of this method is to test
     * one filter without interaction with other filters or servlets.
     * @param filterClass the class of the filter
     * @return instance of <code>Filter</code>
     * @throws RuntimeException if <code>filterClass</code> is not an
     *         instance of <code>Filter</code>
     */
    public Filter createFilter(Class filterClass)
    {
        filter = createFilterInstance(filterClass);
        return filter;
    }
    
    /**
     * Returns the current filter.
     * @return the filter
     */
    public Filter getFilter()
    {
        return filter;
    }

    /**
     * Creates a filter and initializes it. <i>filterClass</i> must
     * be of the type <code>Filter</code>, otherwise a
     * <code>RuntimeException</code> will be thrown.
     * Sets the filter as the current filter and adds
     * it to the filter chain. If you set <i>doChain</i> to
     * <code>true</code> (use {@link #setDoChain}) only filters added 
     * with this or one of the other <code>add</code> methods will be 
     * called while going through the chain.
     * @param filterClass the class of the filter
     * @return instance of <code>Filter</code>
     * @throws RuntimeException if <code>filterClass</code> is not an
     *         instance of <code>Filter</code>
     */
    public Filter createAndAddFilter(Class filterClass)
    {
        createFilter(filterClass);
        mockFactory.getMockFilterChain().addFilter(filter);
        return filter;
    }

    /**
     * Creates a filter and initializes it. <i>filterClass</i> must
     * be of the type <code>Filter</code>, otherwise a
     * <code>RuntimeException</code> will be thrown.
     * This method doesn't set the filter as the current filter,
     * it simply adds it to the filter chain. The current filter
     * will be kept untouched. If you set <i>doChain</i> to
     * <code>true</code> (use {@link #setDoChain}) only filters added
     * with this or one of the other <code>add</code> methods will be 
     * called while going through the chain.
     * @param filterClass the class of the filter
     * @throws RuntimeException if <code>filterClass</code> is not an
     *         instance of <code>Filter</code>
     */
    public void addFilter(Class filterClass)
    {
        mockFactory.getMockFilterChain().addFilter(createFilterInstance(filterClass));
    }

    /**
     * Adds the specified filter to the filter chain.
     * This method doesn't set the filter as the current filter.
     * Furthermore the filter will not be initialized by calling the
     * <code>init</code> method. You have to do that on your own
     * using the {@link com.mockrunner.mock.MockFilterConfig} returned
     * by {@link com.mockrunner.base.MockObjectFactory#getMockFilterConfig}.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}) 
     * only filters added with this or one of the other <code>add</code> methods 
     * will be called while going through the chain.
     * @param filter the filter
     */
    public void addFilter(Filter filter)
    {
        mockFactory.getMockFilterChain().addFilter(filter);
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
     * Calls the current filters <code>doFilter</code> method.
     * Calls only the current filter without going through the 
     * filter chain, regardless of the <code>doChain</code> setting.
     */
    public void doFilter()
    {
        try
        {
            MockFilterChain newChain = mockFactory.createNewMockFilterChain();
            newChain.addFilter(filter);
            filter.doFilter(mockFactory.getMockRequest(), mockFactory.getMockResponse(), newChain);
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Loops through the filter chain and calls the current servlets
     * <code>service</code> method at the end (only if a current servlet
     * is set). Useful if you want to test the interaction of filters
     * and servlets or if a servlet needs some filters to work properly.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * this method is called before any call of a servlet method. If a filter
     * does not call it's chains <code>doFilter</code> method, the chain
     * breaks and the servlet will not be called (just like it in the
     * real container).
     * Only filters added with one of the <code>add</code> methods will be 
     * called while going through the chain. The current filter is ignored
     * if it is not in the chain.
     */
    public void doChain()
    {
        try
        {
            mockFactory.getMockFilterChain().doFilter(mockFactory.getMockRequest(), mockFactory.getMockResponse());
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
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
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Calls the current servlets <code>doDelete</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doDelete</code>.
     */
    public void doDelete()
    {
        mockFactory.getMockRequest().setupGetMethod("DELETE");
        callService();
    }
    
    /**
     * Calls the current servlets <code>doGet</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doGet</code>.
     */          
    public void doGet()
    {
        mockFactory.getMockRequest().setupGetMethod("GET");
        callService();
    }
    
    /**
     * Calls the current servlets <code>doOptions</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doOptions</code>.
     */          
    public void doOptions()
    {
        mockFactory.getMockRequest().setupGetMethod("OPTIONS");
        callService();
    }
    
    /**
     * Calls the current servlets <code>doPost</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doPost</code>.
     */         
    public void doPost()
    {
        mockFactory.getMockRequest().setupGetMethod("POST");
        callService();
    }
    
    /**
     * Calls the current servlets <code>doPut</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doPut</code>.
     */         
    public void doPut()
    {
        mockFactory.getMockRequest().setupGetMethod("PUT");
        callService();
    }
    
    /**
     * Calls the current servlets <code>doTrace</code> method.
     * If you set <i>doChain</i> to <code>true</code> (use {@link #setDoChain}),
     * the filter chain will be called before <code>doTrace</code>.
     */          
    public void doTrace()
    {
        mockFactory.getMockRequest().setupGetMethod("TRACE");
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
        
        }
        return mockFactory.getMockResponse().getOutputStreamContents();
    }
   
    /**
     * Verifies the servlet output.
     * @param output the expected output.
     * @throws VerifyFailedException if verification fails
     */  
    public void verifyOutput(String output)
    {
        String actualOutput = getOutput();
        if(!output.equals(actualOutput))
        {
            throw new VerifyFailedException("actual output: " + actualOutput + " does not match expected output");
        }
    }
    
    /**
     * Verifies if the servlet output contains the specified data.
     * @param output the data
     * @throws VerifyFailedException if verification fails
     */   
    public void verifyOutputContains(String output)
    {
        String actualOutput = getOutput();
        if(-1 == actualOutput.indexOf(output))
        {
            throw new VerifyFailedException("actual output: " + actualOutput + " does not match expected output");
        }
    }
    
    private void callService()
    {
        try
        {
            if(doChain)
            { 
                doChain(); 
            }
            else
            {
                servlet.service(mockFactory.getMockRequest(), mockFactory.getMockResponse());
            }            
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    private Filter createFilterInstance(Class filterClass)
    {
        if(!Filter.class.isAssignableFrom(filterClass))
        {
            throw new RuntimeException("filterClass must be an instance of javax.servlet.Filter");
        }
        try
        {
            Filter theFilter = (Filter)filterClass.newInstance();
            theFilter.init(mockFactory.getMockFilterConfig());
            return theFilter;
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }   
}
