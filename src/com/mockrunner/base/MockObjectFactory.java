package com.mockrunner.base;

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
}
