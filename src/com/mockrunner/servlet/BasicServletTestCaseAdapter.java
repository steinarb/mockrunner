package com.mockrunner.servlet;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import com.mockrunner.base.BasicHTMLOutputTestCase;
import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.WebTestModule;
import com.mockrunner.mock.web.WebMockObjectFactory;

/**
 * Delegator for {@link com.mockrunner.servlet.ServletTestModule}. You can
 * subclass this adapter or use {@link com.mockrunner.servlet.ServletTestModule}
 * directly (so your test case can use another base class).
 * <b>This class is generated from the ServletTestModule and should not be
 * edited directly</b>.
 */
public class BasicServletTestCaseAdapter extends BasicHTMLOutputTestCase
{
    private ServletTestModule servletTestModule;
    private WebMockObjectFactory webMockFactory;

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
        webMockFactory = null;
    }

    /**
     * Creates the {@link com.mockrunner.servlet.ServletTestModule}. If you
     * overwrite this method, you must call <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        webMockFactory = createWebMockObjectFactory();
        servletTestModule = createServletTestModule(getWebMockObjectFactory());
    }
    
    /**
     * Creates a {@link WebMockObjectFactory}. 
     * @return the created {@link WebMockObjectFactory}
     */
    protected WebMockObjectFactory createWebMockObjectFactory()
    {
        return new WebMockObjectFactory();
    }

    /**
     * Same as <code>createWebMockObjectFactory(otherFactory, true)</code>
     */
    protected WebMockObjectFactory createWebMockObjectFactory(WebMockObjectFactory otherFactory)
    {
        return new WebMockObjectFactory(otherFactory);
    }
    
    /**
     * Creates a {@link WebMockObjectFactory} based on another on.
     * The created {@link WebMockObjectFactory} will have its own
     * request and response objects. If you set <i>createNewSession</i>
     * to <code>true</code> it will also have its own session object. 
     * The two factories will share one <code>ServletContext</code>. 
     * Especially important for multithreading tests.
     * If you set <i>createNewSession</i> to false, the two factories
     * will share one session. This setting simulates multiple requests
     * from the same client.
     * @param otherFactory the othe factory
     * @param createNewSession create a new session for the new factory
     * @return the created {@link WebMockObjectFactory}
     */
    protected WebMockObjectFactory createWebMockObjectFactory(WebMockObjectFactory otherFactory, boolean createNewSession)
    {
        return new WebMockObjectFactory(otherFactory, createNewSession);
    }

    /**
     * Gets the current {@link WebMockObjectFactory}.
     * @return the {@link WebMockObjectFactory}
     */
    protected WebMockObjectFactory getWebMockObjectFactory()
    {
        return webMockFactory;
    }
    
    /**
     * Sets the current {@link WebMockObjectFactory}.
     * @param mockFactory the {@link WebMockObjectFactory}
     */
    protected void setWebMockObjectFactory(WebMockObjectFactory mockFactory)
    {
        this.webMockFactory = mockFactory;
    }
    
    /**
     * Creates a {@link com.mockrunner.servlet.ServletTestModule} with the specified
     * {@link WebMockObjectFactory}.
     * @return the created {@link com.mockrunner.servlet.ServletTestModule}
     */
    protected ServletTestModule createServletTestModule(WebMockObjectFactory mockFactory)
    {
        return new ServletTestModule(mockFactory);
    }

    /**
     * Creates a {@link com.mockrunner.servlet.ServletTestModule} based on the current
     * {@link WebMockObjectFactory}.
     * Same as <code>createServletTestModule(getWebMockObjectFactory())</code>.
     * @return the created {@link com.mockrunner.servlet.ServletTestModule}
     */
    protected ServletTestModule createServletTestModule()
    {
        return new ServletTestModule(getWebMockObjectFactory());
    }

    /**
     * Returns the {@link com.mockrunner.servlet.ServletTestModule} as
     * {@link com.mockrunner.base.WebTestModule}.
     * @return the {@link com.mockrunner.base.WebTestModule}
     */
    protected WebTestModule getWebTestModule()
    {
        return servletTestModule;
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
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#init}
     */
    protected void init()
    {
        servletTestModule.init();
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
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#service}
     */
    protected void service()
    {
        servletTestModule.service();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#getServlet}
     */
    protected HttpServlet getServlet()
    {
        return servletTestModule.getServlet();
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#setCaseSensitive(boolean)}
     */
    protected void setCaseSensitive(boolean caseSensitive)
    {
        servletTestModule.setCaseSensitive(caseSensitive);
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
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#verifyOutput(String)}
     */
    protected void verifyOutput(String expectedOutput)
    {
        servletTestModule.verifyOutput(expectedOutput);
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#verifyOutputContains(String)}
     */
    protected void verifyOutputContains(String expectedOutput)
    {
        servletTestModule.verifyOutputContains(expectedOutput);
    }

    /**
     * Delegates to {@link com.mockrunner.servlet.ServletTestModule#verifyOutputRegularExpression(String)}
     */
    protected void verifyOutputRegularExpression(String expression)
    {
        servletTestModule.verifyOutputRegularExpression(expression);
    }
}