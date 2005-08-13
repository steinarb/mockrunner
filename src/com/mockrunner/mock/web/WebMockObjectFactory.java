package com.mockrunner.mock.web;

import java.lang.reflect.Constructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mockrunner.base.NestedApplicationException;

/**
 * Used to create all types of web mock objects. Maintains
 * the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure that they are all
 * up to date.
 */
public class WebMockObjectFactory
{
    private HttpServletRequest wrappedRequest;
    private HttpServletResponse wrappedResponse;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockServletConfig config;
    private MockServletContext context;
    private MockHttpSession session;
    private MockPageContext pageContext;
    private MockFilterConfig filterConfig;
    private MockFilterChain filterChain;

    /**
     * Creates a new set of mock objects.
     */
    public WebMockObjectFactory()
    {
        createMockObjects();
    }
    
    /**
     * Creates a set of mock objects based on another one.
     * The created mock objects will have their own
     * request and session objects, but they will share
     * one <code>ServletContext</code>.
     * @param factory the other factory
     * @see com.mockrunner.base.BaseTestCase#createWebMockObjectFactory(WebMockObjectFactory)
     */
    public WebMockObjectFactory(WebMockObjectFactory factory)
    {
        createMockObjectsBasedOn(factory);
    }
    
    /**
     * Creates a set of mock objects based on another one.
     * You can specify, if the created mock objects should
     * share the same session. They will share one
     * <code>ServletContext</code> anyway.
     * @param factory the other factory
     * @param createNewSession <code>true</code> creates a new session,
     *                         <code>false</code> uses the session from factory
     * @see com.mockrunner.base.BaseTestCase#createWebMockObjectFactory(WebMockObjectFactory, boolean)
     */
    public WebMockObjectFactory(WebMockObjectFactory factory, boolean createNewSession)
    {
        createMockObjectsBasedOn(factory, createNewSession);
    }
 
    private void createMockObjects()
    {
        createNewMockObjects(true);
        config = createMockServletConfig();
        context = createMockServletContext();
        setUpDependencies();
    }

    private void createMockObjectsBasedOn(WebMockObjectFactory factory)
    {
        createMockObjectsBasedOn(factory, true);
    }
    
    private void createMockObjectsBasedOn(WebMockObjectFactory factory, boolean createNewSession)
    {
        createNewMockObjects(createNewSession);
        if(!createNewSession) session = factory.getMockSession();
        config = factory.getMockServletConfig();
        context = factory.getMockServletContext();
        setUpDependencies();
    }

    private void createNewMockObjects(boolean createNewSession)
    {
        request = createMockRequest();
        response = createMockResponse();
        wrappedRequest = request;
        wrappedResponse = response;
        if(createNewSession) session = createMockSession();
        filterChain = createMockFilterChain();
        filterConfig = createMockFilterConfig();
    }

    private void setUpDependencies()
    {
        config.setServletContext(context);
        request.setSession(session);
        session.setupServletContext(context);
        pageContext = createMockPageContext();
        pageContext.setServletConfig(config);
        pageContext.setServletRequest(request);
        pageContext.setServletResponse(response);
        filterConfig.setupServletContext(context);
    }
    
    /**
     * Refreshes the mock objects dependencies. May be called after setting request
     * and response wrappers.
     */
    public void refresh()
    {
        pageContext = new MockPageContext(config, wrappedRequest, wrappedResponse);
    }
    
    /**
     * Creates the {@link com.mockrunner.mock.web.MockServletContext} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.web.MockServletContext}.
     * @return the {@link com.mockrunner.mock.web.MockServletContext}
     */
    public MockServletContext createMockServletContext()
    {
        return new MockServletContext();
    }

    /**
     * Creates the {@link com.mockrunner.mock.web.MockServletConfig} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.web.MockServletConfig}.
     * @return the {@link com.mockrunner.mock.web.MockServletConfig}
     */
    public MockServletConfig createMockServletConfig()
    {
        return new MockServletConfig();
    }
    
    /**
     * Creates the {@link com.mockrunner.mock.web.MockHttpServletResponse} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.web.MockHttpServletResponse}.
     * @return the {@link com.mockrunner.mock.web.MockHttpServletResponse}
     */
    public MockHttpServletResponse createMockResponse()
    {
        return new MockHttpServletResponse();
    }

    /**
     * Creates the {@link com.mockrunner.mock.web.MockHttpServletRequest} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.web.MockHttpServletRequest}.
     * @return the {@link com.mockrunner.mock.web.MockHttpServletRequest}
     */
    public MockHttpServletRequest createMockRequest()
    {
        return new MockHttpServletRequest();
    }
    
    /**
     * Creates the {@link com.mockrunner.mock.web.MockHttpSession} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.web.MockHttpSession}.
     * @return the {@link com.mockrunner.mock.web.MockHttpSession}
     */
    public MockHttpSession createMockSession()
    {
        return new MockHttpSession();
    }
    
    /**
     * Creates the {@link com.mockrunner.mock.web.MockPageContext} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.web.MockPageContext}.
     * @return the {@link com.mockrunner.mock.web.MockPageContext}
     */
    public MockPageContext createMockPageContext()
    {
        return new MockPageContext();
    }
    
    /**
     * Creates the {@link com.mockrunner.mock.web.MockFilterConfig} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.web.MockFilterConfig}.
     * @return the {@link com.mockrunner.mock.web.MockFilterConfig}
     */
    public MockFilterConfig createMockFilterConfig()
    {
        return new MockFilterConfig();
    }

    /**
     * Creates the {@link com.mockrunner.mock.web.MockFilterChain} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.web.MockFilterChain}.
     * @return the {@link com.mockrunner.mock.web.MockFilterChain}
     */
    public MockFilterChain createMockFilterChain()
    {
        return new MockFilterChain();
    }
    
    /**
     * Returns the <code>MockServletConfig</code>
     * @return the <code>MockServletConfig</code>
     */
    public MockServletConfig getMockServletConfig()
    {
        return config;
    }

    /**
     * Returns the {@link com.mockrunner.mock.web.MockServletContext}.
     * @return the {@link com.mockrunner.mock.web.MockServletContext}
     */
    public MockServletContext getMockServletContext()
    {
        return context;
    }

    /**
     * Returns the {@link com.mockrunner.mock.web.MockHttpServletRequest}.
     * @return the {@link com.mockrunner.mock.web.MockHttpServletRequest}
     */
    public MockHttpServletRequest getMockRequest()
    {
        return request;
    }

    /**
     * Returns the {@link com.mockrunner.mock.web.MockHttpServletResponse}.
     * @return the {@link com.mockrunner.mock.web.MockHttpServletResponse}
     */
    public MockHttpServletResponse getMockResponse()
    {
        return response;
    }
    
    /**
     * Returns the wrapped <code>HttpServletRequest</code>. If no
     * wrapper is specified, this method returns the mock request itself.
     * @return the wrapped <code>HttpServletRequest</code>
     */
    public HttpServletRequest getWrappedRequest()
    {
        return wrappedRequest;
    }

    /**
     * Returns the wrapped <code>HttpServletResponse</code>. If no
     * wrapper is specified, this method returns the mock response itself.
     * @return the wrapped <code>HttpServletRequest</code>
     */
    public HttpServletResponse getWrappedResponse()
    {
        return wrappedResponse;
    }

    /**
     * Returns the {@link com.mockrunner.mock.web.MockHttpSession}.
     * @return the {@link com.mockrunner.mock.web.MockHttpSession}
     */
    public MockHttpSession getMockSession()
    {
        return session;
    }

    /**
     * Returns the {@link com.mockrunner.mock.web.MockHttpSession}.
     * @return the {@link com.mockrunner.mock.web.MockHttpSession}
     * @deprecated use {@link #getMockSession}
     */
    public MockHttpSession getSession()
    {
        return getMockSession();
    }

    /**
     * Returns the {@link com.mockrunner.mock.web.MockPageContext}.
     * @return the {@link com.mockrunner.mock.web.MockPageContext}
     */
    public MockPageContext getMockPageContext()
    {
        return pageContext;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.web.MockFilterConfig}.
     * @return the {@link com.mockrunner.mock.web.MockFilterConfig}
     */
    public MockFilterConfig getMockFilterConfig()
    {
        return filterConfig;
    }

    /**
     * Returns the {@link com.mockrunner.mock.web.MockFilterChain}.
     * @return the {@link com.mockrunner.mock.web.MockFilterChain}
     */
    public MockFilterChain getMockFilterChain()
    {
        return filterChain;
    }
 
    /**
     * Can be used to add a request wrapper. All the
     * test modules are using the wrapped request returned by
     * {@link #getWrappedRequest}. The method {@link #getMockRequest}
     * returns the mock request without any wrapper.
     * Usually the wrapper is of type <code>javax.servlet.http.HttpServletRequestWrapper</code>.
     * That's not absolutely necessary but the wrapper must define a constructor
     * that takes a single <code>javax.servlet.http.HttpServletRequest</code> argument
     * and must implement <code>javax.servlet.http.HttpServletRequest</code>.
     * @param wrapper the wrapper class
     */
    public void addRequestWrapper(Class wrapper)
    {
        try
        {
            Constructor constructor = wrapper.getConstructor(new Class[] {HttpServletRequest.class});
            wrappedRequest = (HttpServletRequest)constructor.newInstance(new Object[] {wrappedRequest});
        }
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Can be used to add a request wrapper. 
     * All the test modules are using the wrapped request returned by
     * {@link #getWrappedRequest}. The method {@link #getMockRequest}
     * returns the mock request without any wrapper. Usually the wrapper is
     * an instance of <code>javax.servlet.http.HttpServletRequestWrapper</code>
     * and wraps the current request but that's not absolutely necessary. 
     * However, be careful if you want to add custom mock versions of 
     * <code>javax.servlet.http.HttpServletRequest</code>.
     * @param wrapper the request wrapper
     */
    public void addRequestWrapper(HttpServletRequest wrapper)
    {
        wrappedRequest = wrapper;
    }
    
    /**
     * Can be used to add a response wrapper. All the
     * test modules are using the wrapped response returned by
     * {@link #getWrappedResponse}. The method {@link #getMockResponse}
     * returns the mock response without any wrapper.
     * Usually the wrapper is of type <code>javax.servlet.http.HttpServletResponseWrapper</code>.
     * That's not absolutely necessary but the wrapper must define a constructor
     * that takes a single <code>javax.servlet.http.HttpServletResponse</code> argument
     * and must implement <code>javax.servlet.http.HttpServletResponse</code>.
     * @param wrapper the wrapper class
     */
    public void addResponseWrapper(Class wrapper)
    {
        try
        {
            Constructor constructor = wrapper.getConstructor(new Class[] {HttpServletResponse.class});
            wrappedResponse = (HttpServletResponse)constructor.newInstance(new Object[] {wrappedResponse});
        }
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Can be used to add a response wrapper. 
     * All the test modules are using the wrapped response returned by
     * {@link #getWrappedResponse}. The method {@link #getMockResponse}
     * returns the mock response without any wrapper. Usually the wrapper is
     * an instance of <code>javax.servlet.http.HttpServletResponseWrapper</code>
     * and wraps the current response but that's not absolutely necessary. 
     * However, be careful if you want to add custom mock versions of 
     * <code>javax.servlet.http.HttpServletResponse</code>.
     * @param wrapper the wrapper
     */
    public void addResponseWrapper(HttpServletResponse wrapper)
    {
        wrappedResponse = wrapper;
    }
}
