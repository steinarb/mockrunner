package com.mockrunner.base;

import com.mockobjects.servlet.MockServletConfig;
import com.mockrunner.mock.MockActionMapping;
import com.mockrunner.mock.MockFilterChain;
import com.mockrunner.mock.MockFilterConfig;
import com.mockrunner.mock.MockHttpServletRequest;
import com.mockrunner.mock.MockHttpServletResponse;
import com.mockrunner.mock.MockHttpSession;
import com.mockrunner.mock.MockPageContext;
import com.mockrunner.mock.MockServletContext;

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
    }

    private void setUpDependencies()
    {
        config.setServletContext(context);
        request.setSession(session);
        session.setupServletContext(context);
        pageContext = new MockPageContext(config, request, response);
        filterConfig.setupGetServletContext(context);
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
     * Returns the {@link com.mockrunner.mock.MockServletContext}.
     * @return the {@link com.mockrunner.mock.MockServletContext}
     */
    public MockServletContext getMockServletContext()
    {
        return context;
    }

    /**
     * Returns the {@link com.mockrunner.mock.MockHttpServletRequest}.
     * @return the {@link com.mockrunner.mock.MockHttpServletRequest}
     */
    public MockHttpServletRequest getMockRequest()
    {
        return request;
    }

    /**
     * Returns the {@link com.mockrunner.mock.MockHttpServletResponse}.
     * @return the {@link com.mockrunner.mock.MockHttpServletResponse}
     */
    public MockHttpServletResponse getMockResponse()
    {
        return response;
    }

    /**
     * Returns the {@link com.mockrunner.mock.MockHttpSession}.
     * @return the {@link com.mockrunner.mock.MockHttpSession}
     */
    public MockHttpSession getMockSession()
    {
        return session;
    }

    /**
     * Returns the {@link com.mockrunner.mock.MockHttpSession}.
     * @return the {@link com.mockrunner.mock.MockHttpSession}
     * @deprecated use {@link #getMockSession}
     */
    public MockHttpSession getSession()
    {
        return getMockSession();
    }

    /**
     * Returns the {@link com.mockrunner.mock.MockActionMapping}.
     * @return the {@link com.mockrunner.mock.MockActionMapping}
     */
    public MockActionMapping getMockActionMapping()
    {
        return mapping;
    }

    /**
     * Returns the {@link com.mockrunner.mock.MockPageContext}.
     * @return the {@link com.mockrunner.mock.MockPageContext}
     */
    public MockPageContext getMockPageContext()
    {
        return pageContext;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.MockFilterConfig}.
     * @return the {@link com.mockrunner.mock.MockFilterConfig}
     */
    public MockFilterConfig getMockFilterConfig()
    {
        return filterConfig;
    }

    /**
     * Returns the {@link com.mockrunner.mock.MockFilterChain}.
     * @return the {@link com.mockrunner.mock.MockFilterChain}
     */
    public MockFilterChain getMockFilterChain()
    {
        return filterChain;
    }
}
