package com.mockrunner.servlet;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import com.mockrunner.base.BasicHTMLOutputTestCase;
import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.mock.web.WebMockObjectFactory;

/**
 * Delegator for {@link com.mockrunner.servlet.ServletTestModule}. You can
 * subclass this adapter or use {@link com.mockrunner.servlet.ServletTestModule}
 * directly (so your test case can use another base class).
 * This basic adapter can be used if you don't need any other modules. It
 * does not extend {@link com.mockrunner.base.BaseTestCase}. If you want
 * to use several modules in conjunction, consider subclassing
 * {@link com.mockrunner.servlet.ServletTestCaseAdapter}.
 * <b>This class is generated from the {@link com.mockrunner.servlet.ServletTestModule}
 * and should not be edited directly</b>.
 */
public class BasicServletTestCaseAdapter extends BasicHTMLOutputTestCase
{
    private ServletTestModule servletTestModule;
    private WebMockObjectFactory webMockObjectFactory;

    public BasicServletTestCaseAdapter()
    {

    }

    public BasicServletTestCaseAdapter(String name)
    {
        super(name);
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        servletTestModule = null;
        webMockObjectFactory = null;
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
     * Creates a {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * @return the created {@link com.mockrunner.mock.web.WebMockObjectFactory}
     */
    protected WebMockObjectFactory createWebMockObjectFactory()
    {
        return new WebMockObjectFactory();
    }

    /**
     * Same as <code>createWebMockObjectFactory(otherFactory, true)</code>.
     */
    protected WebMockObjectFactory createWebMockObjectFactory(WebMockObjectFactory otherFactory)
    {
        return new WebMockObjectFactory(otherFactory);
    }

    /**
     * Creates a {@link com.mockrunner.mock.web.WebMockObjectFactory} based on another 
     * {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * The created {@link com.mockrunner.mock.web.WebMockObjectFactory} will have its own
     * request and response objects. If you set <i>createNewSession</i>
     * to <code>true</code> it will also have its own session object.
     * The two factories will share one <code>ServletContext</code>.
     * Especially important for multithreading tests.
     * If you set <i>createNewSession</i> to false, the two factories
     * will share one session. This setting simulates multiple requests
     * from the same client.
     * @param otherFactory the other factory
     * @param createNewSession create a new session for the new factory
     * @return the created {@link com.mockrunner.mock.web.WebMockObjectFactory}
     */
    protected WebMockObjectFactory createWebMockObjectFactory(WebMockObjectFactory otherFactory, boolean createNewSession)
    {
        return new WebMockObjectFactory(otherFactory, createNewSession);
    }

    /**
     * Gets the {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * @return the {@link com.mockrunner.mock.web.WebMockObjectFactory}
     */
    protected WebMockObjectFactory getWebMockObjectFactory()
    {
        synchronized(WebMockObjectFactory.class)
        {
            if(webMockObjectFactory == null)
            {
                webMockObjectFactory = createWebMockObjectFactory();
            }
        }
        return webMockObjectFactory;
    }

    /**
     * Sets the {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * @param webMockObjectFactory the {@link com.mockrunner.mock.web.WebMockObjectFactory}
     */
    protected void setWebMockObjectFactory(WebMockObjectFactory webMockObjectFactory)
    {
        this.webMockObjectFactory = webMockObjectFactory;
    }

    /**
     * Creates a {@link com.mockrunner.servlet.ServletTestModule} based on the current
     * {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * Same as <code>createServletTestModule(getWebMockObjectFactory())</code>.
     * @return the created {@link com.mockrunner.servlet.ServletTestModule}
     */
    protected ServletTestModule createServletTestModule()
    {
        return new ServletTestModule(getWebMockObjectFactory());
    }

    /**
     * Creates a {@link com.mockrunner.servlet.ServletTestModule} with the specified
     * {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * @return the created {@link com.mockrunner.servlet.ServletTestModule}
     */
    protected ServletTestModule createServletTestModule(WebMockObjectFactory mockFactory)
    {
        return new ServletTestModule(mockFactory);
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