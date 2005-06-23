package com.mockrunner.servlet;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.HTMLOutputTestCase;

/**
 * Delegator for {@link com.mockrunner.servlet.ServletTestModule}. You can
 * subclass this adapter or use {@link com.mockrunner.servlet.ServletTestModule}
 * directly (so your test case can use another base class).
 * This adapter extends {@link com.mockrunner.base.BaseTestCase}.
 * It can be used if you want to use several modules in conjunction.
 * <b>This class is generated from the {@link com.mockrunner.servlet.ServletTestModule}
 * and should not be edited directly</b>.
 */
public class ServletTestCaseAdapter extends HTMLOutputTestCase
{
    private ServletTestModule servletTestModule;

    public ServletTestCaseAdapter()
    {

    }

    public ServletTestCaseAdapter(String name)
    {
        super(name);
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        servletTestModule = null;
    }

    /**
     * Creates the {@link com.mockrunner.servlet.ServletTestModule}. If you
     * overwrite this method, you must call <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        servletTestModule = createServletTestModule(getWebMockObjectFactory());
    }

    /**
     * Returns the {@link com.mockrunner.servlet.ServletTestModule} as
     * {@link com.mockrunner.base.HTMLOutputModule}.
     * @return the {@link com.mockrunner.base.HTMLOutputModule}
     */
    protected HTMLOutputModule getHTMLOutputModule()
    {
        return servletTestModule;
    }

    /**
     * Gets the {@link com.mockrunner.servlet.ServletTestModule}.
     * @return the {@link com.mockrunner.servlet.ServletTestModule}
     */
    protected ServletTestModule getServletTestModule()
    {
        return servletTestModule;
    }

    /**
     * Sets the {@link com.mockrunner.servlet.ServletTestModule}.
     * @param servletTestModule the {@link com.mockrunner.servlet.ServletTestModule}
     */
    protected void setServletTestModule(ServletTestModule servletTestModule)
    {
        this.servletTestModule = servletTestModule;
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#setServlet(HttpServlet, boolean)}
     */
    protected void setServlet(HttpServlet servlet, boolean doInit)
    {
        servletTestModule.setServlet(servlet, doInit);
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#setServlet(HttpServlet)}
     */
    protected void setServlet(HttpServlet servlet)
    {
        servletTestModule.setServlet(servlet);
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#doGet}
     */
    protected void doGet()
    {
        servletTestModule.doGet();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#doPost}
     */
    protected void doPost()
    {
        servletTestModule.doPost();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#doHead}
     */
    protected void doHead()
    {
        servletTestModule.doHead();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#doPut}
     */
    protected void doPut()
    {
        servletTestModule.doPut();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#doDelete}
     */
    protected void doDelete()
    {
        servletTestModule.doDelete();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#doOptions}
     */
    protected void doOptions()
    {
        servletTestModule.doOptions();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#doTrace}
     */
    protected void doTrace()
    {
        servletTestModule.doTrace();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#getServlet}
     */
    protected HttpServlet getServlet()
    {
        return servletTestModule.getServlet();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#createServlet(Class)}
     */
    protected HttpServlet createServlet(Class servletClass)
    {
        return servletTestModule.createServlet(servletClass);
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#createFilter(Class)}
     */
    protected Filter createFilter(Class filterClass)
    {
        return servletTestModule.createFilter(filterClass);
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#addFilter(Filter, boolean)}
     */
    protected void addFilter(Filter filter, boolean doInit)
    {
        servletTestModule.addFilter(filter, doInit);
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#addFilter(Filter)}
     */
    protected void addFilter(Filter filter)
    {
        servletTestModule.addFilter(filter);
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#releaseFilters}
     */
    protected void releaseFilters()
    {
        servletTestModule.releaseFilters();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#setDoChain(boolean)}
     */
    protected void setDoChain(boolean doChain)
    {
        servletTestModule.setDoChain(doChain);
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#doFilter}
     */
    protected void doFilter()
    {
        servletTestModule.doFilter();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#getFilteredRequest}
     */
    protected ServletRequest getFilteredRequest()
    {
        return servletTestModule.getFilteredRequest();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#getFilteredResponse}
     */
    protected ServletResponse getFilteredResponse()
    {
        return servletTestModule.getFilteredResponse();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#clearOutput}
     */
    protected void clearOutput()
    {
        servletTestModule.clearOutput();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#init}
     */
    protected void init()
    {
        servletTestModule.init();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#service}
     */
    protected void service()
    {
        servletTestModule.service();
    }
}