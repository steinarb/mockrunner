package com.mockrunner.base;

import java.lang.reflect.Constructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mockobjects.servlet.MockServletConfig;
import com.mockobjects.sql.MockDataSource;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.web.MockActionMapping;
import com.mockrunner.mock.web.MockFilterChain;
import com.mockrunner.mock.web.MockFilterConfig;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockPageContext;
import com.mockrunner.mock.web.MockServletContext;

/**
 * Used to create all types of mock objects. Maintains
 * the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure, they are all
 * up to date.
 */
public class MockObjectFactory
{
    private HttpServletRequest wrappedRequest;
    private HttpServletResponse wrappedResponse;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockServletConfig config;
    private MockServletContext context;
    private MockHttpSession session;
    private MockActionMapping mapping;
    private MockPageContext pageContext;
    private MockFilterConfig filterConfig;
    private MockFilterChain filterChain;
    private MockDataSource dataSource;
    private MockConnection connection;

    /**
     * Creates a new set of mock objects.
     */
    public MockObjectFactory()
    {
        createMockObjects();
    }
    
    /**
     * Creates a set of mock objects based on another one.
     * The created mock objects will have their own
     * request and session objects, but they will share
     * one <code>ServletContext</code>.
     * @param factory the other factory
     * @see BaseTestCase#createMockObjectFactory(MockObjectFactory)
     */
    public MockObjectFactory(MockObjectFactory factory)
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
     * @see BaseTestCase#createMockObjectFactory(MockObjectFactory, boolean)
     */
    public MockObjectFactory(MockObjectFactory factory, boolean createNewSession)
    {
        createMockObjectsBasedOn(factory, createNewSession);
    }
 
    private void createMockObjects()
    {
        createNewMockObjects(true);
        config = new MockServletConfig();
        context = new MockServletContext();
        setUpDependencies();
    }

    private void createMockObjectsBasedOn(MockObjectFactory factory)
    {
        createMockObjectsBasedOn(factory, true);
    }
    
    private void createMockObjectsBasedOn(MockObjectFactory factory, boolean createNewSession)
    {
        createNewMockObjects(createNewSession);
        if(!createNewSession) session = factory.getMockSession();
        config = factory.getMockServletConfig();
        context = factory.getMockServletContext();
        setUpDependencies();
    }

    private void createNewMockObjects(boolean createNewSession)
    {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        wrappedRequest = request;
        wrappedResponse = response;
        if(createNewSession) session = new MockHttpSession();
        mapping = new MockActionMapping();
        filterChain = new MockFilterChain();
        filterConfig = new MockFilterConfig();
        dataSource = new MockDataSource();
        connection = new MockConnection();
    }

    private void setUpDependencies()
    {
        config.setServletContext(context);
        request.setSession(session);
        session.setupServletContext(context);
        pageContext = new MockPageContext(config, request, response);
        filterConfig.setupServletContext(context);
        dataSource.setupConnection(connection);
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
     * Returns the {@link com.mockrunner.mock.web.MockActionMapping}.
     * @return the {@link com.mockrunner.mock.web.MockActionMapping}
     */
    public MockActionMapping getMockActionMapping()
    {
        return mapping;
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
     * Returns the <code>MockDataSource</code>
     * @return the <code>MockDataSource</code>
     */
    public MockDataSource getMockDataSource()
    {
        return dataSource;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.jdbc.MockConnection}.
     * @return the {@link com.mockrunner.mock.jdbc.MockConnection}
     */
    public MockConnection getMockConnection()
    {
        return connection;
    }
    
    /**
     * Can be used to add a wrapper around the mock request. All the
     * test modules are using the wrapped request returned by
     * {@link #getWrappedRequest}. The method {@link #getMockRequest}
     * returns the mock request without any wrapper.
     * @param wrapper the wrapper, must be of type <code>HttpServletRequestWrapper</code>
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
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Can be used to add a wrapper around the mock response. All the
     * test modules are using the wrapped response returned by
     * {@link #getWrappedResponse}. The method {@link #getMockResponse}
     * returns the mock request without any wrapper.
     * @param wrapper the wrapper, must be of type <code>HttpServletResponseWrapper</code>
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
            throw new RuntimeException(exc.getMessage());
        }
    }
}
