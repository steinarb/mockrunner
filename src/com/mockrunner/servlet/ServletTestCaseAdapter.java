package com.mockrunner.servlet;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.HTMLOutputTestCase;

/**
 * Delegator for {@link ServletTestModule}. You can
 * subclass this adapter or use {@link ServletTestModule}
 * directly (so your test case can use another base
 * class).
 */
public class ServletTestCaseAdapter extends HTMLOutputTestCase
{
    private ServletTestModule servletTestModule;
    
    public ServletTestCaseAdapter()
    {

    }

    public ServletTestCaseAdapter(String arg0)
    {
        super(arg0);
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        servletTestModule = null;
    }
    
    /**
     * Creates the <code>ServletTestModule</code>. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        servletTestModule = createServletTestModule(getWebMockObjectFactory());
    }
    
    /**
     * Returns the <code>ServletTestModule</code> as 
     * <code>HTMLOutputModule</code>.
     * @return the <code>HTMLOutputModule</code>
     */
    protected HTMLOutputModule getHTMLOutputModule()
    {
        return servletTestModule;
    }
    
    /**
     * Gets the <code>ServletTestModule</code>. 
     * @return the <code>ServletTestModule</code>
     */
    protected ServletTestModule getServletTestModule()
    {
        return servletTestModule;
    }
    
    /**
     * Sets the <code>ServletTestModule</code>. 
     * @param servletTestModule the <code>ServletTestModule</code>
     */
    protected void setServletTestModule(ServletTestModule servletTestModule)
    {
        this.servletTestModule = servletTestModule;
    }
    
    /**
     * Delegates to {@link ServletTestModule#setCaseSensitive}
     */
    protected void setCaseSensitive(boolean caseSensitive)
    {
        servletTestModule.setCaseSensitive(caseSensitive);
    }
    
    /**
     * Delegates to {@link ServletTestModule#createServlet}
     */
    protected HttpServlet createServlet(Class servletClass)
    {
        return servletTestModule.createServlet(servletClass);
    }
    
    /**
     * Delegates to {@link ServletTestModule#setServlet(HttpServlet)}
     */
    protected void setServlet(HttpServlet servlet)
    {
        servletTestModule.setServlet(servlet);
    }
    
    /**
     * Delegates to {@link ServletTestModule#setServlet(HttpServlet, boolean)}
     */
    protected void setServlet(HttpServlet servlet, boolean doInit)
    {
        servletTestModule.setServlet(servlet, doInit);
    }
    
    /**
     * Delegates to {@link ServletTestModule#getServlet}
     */
    protected HttpServlet getServlet()
    {
        return servletTestModule.getServlet();
    }
    
    /**
     * Delegates to {@link ServletTestModule#createFilter}
     */
    protected Filter createFilter(Class filterClass)
    {
        return servletTestModule.createFilter(filterClass);
    }
    
    /**
     * Delegates to {@link ServletTestModule#addFilter(Filter)}
     */
    protected void addFilter(Filter filter)
    {
        servletTestModule.addFilter(filter);
    }
    
    /**
     * Delegates to {@link ServletTestModule#addFilter(Filter, boolean)}
     */
    protected void addFilter(Filter filter, boolean doInit)
    {
        servletTestModule.addFilter(filter, doInit);
    }
    
    /**
     * Delegates to {@link ServletTestModule#releaseFilters}
     */
    protected void releaseFilters()
    {
        servletTestModule.releaseFilters();
    }

    /**
     * Delegates to {@link ServletTestModule#setDoChain}
     */
    protected void setDoChain(boolean doChain)
    {
        servletTestModule.setDoChain(doChain);
    }
    
    /**
     * Delegates to {@link ServletTestModule#addRequestParameter(String)}
     */
    protected void addRequestParameter(String key)
    {
        servletTestModule.addRequestParameter(key);
    }

    /**
     * Delegates to {@link ServletTestModule#addRequestParameter(String, String)}
     */
    protected void addRequestParameter(String key, String value)
    {
        servletTestModule.addRequestParameter(key, value);
    }
    
    /**
     * Delegates to {@link ServletTestModule#doFilter}
     */
    protected void doFilter()
    {
        servletTestModule.doFilter();
    }

    /**
     * Delegates to {@link ServletTestModule#init}
     */
    protected void init()
    {
        servletTestModule.init();
    }

    /**
     * Delegates to {@link ServletTestModule#doDelete}
     */
    protected void doDelete()
    {
        servletTestModule.doDelete();
    }
    
    /**
     * Delegates to {@link ServletTestModule#doGet}
     */
    protected void doGet()
    {
        servletTestModule.doGet();
    }
    
    /**
     * Delegates to {@link ServletTestModule#doOptions}
     */
    protected void doOptions()
    {
        servletTestModule.doOptions();
    }
     
    /**
     * Delegates to {@link ServletTestModule#doPost}
     */   
    protected void doPost()
    {
        servletTestModule.doPost();
    }
    
    /**
     * Delegates to {@link ServletTestModule#doPut}
     */ 
    protected void doPut()
    {
        servletTestModule.doPut();
    }
    
    /**
     * Delegates to {@link ServletTestModule#doTrace}
     */
    protected void doTrace()
    {
        servletTestModule.doTrace();
    }
    
    /**
     * Delegates to {@link ServletTestModule#doHead}
     */
    protected void doHead()
    {
        servletTestModule.doHead();
    }
    
    /**
     * Delegates to {@link ServletTestModule#service}
     */
    protected void service()
    {
        servletTestModule.service();
    }
    
    /**
     * Delegates to {@link ServletTestModule#getFilteredRequest}
     */
    protected ServletRequest getFilteredRequest()
    {
        return servletTestModule.getFilteredRequest();
    }
    
    /**
     * Delegates to {@link ServletTestModule#getFilteredResponse}
     */
    protected ServletResponse getFilteredResponse()
    {
        return servletTestModule.getFilteredResponse();
    }

    /**
     * Delegates to {@link ServletTestModule#clearOutput}
     */
    protected void clearOutput()
    {
        servletTestModule.clearOutput();
    }
    
    /**
     * Delegates to {@link ServletTestModule#verifyOutput}
     */ 
    protected void verifyOutput(String output)
    {
        servletTestModule.verifyOutput(output);
    }
   
    /**
     * Delegates to {@link ServletTestModule#verifyOutputContains}
     */
    protected void verifyOutputContains(String output)
    {
        servletTestModule.verifyOutputContains(output);
    }
    
    /**
     * Delegates to {@link ServletTestModule#verifyOutputRegularExpression}
     */
    protected void verifyOutputRegularExpression(String output)
    {
        servletTestModule.verifyOutputRegularExpression(output);
    }
}
