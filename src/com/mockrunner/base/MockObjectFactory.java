package com.mockrunner.base;

import com.mockobjects.servlet.MockServletConfig;

import com.mockrunner.mock.MockActionMapping;
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
    }

    private void setUpDependencies()
    {
        config.setServletContext(context);
        request.setSession(session);
        session.setupServletContext(context);
        pageContext = new MockPageContext(config, request, response);
    }

    public MockServletConfig getMockServletConfig()
    {
        return config;
    }

    public MockServletContext getMockServletContext()
    {
        return context;
    }

    public MockHttpServletRequest getMockRequest()
    {
        return request;
    }

    public MockHttpServletResponse getMockResponse()
    {
        return response;
    }

    public MockHttpSession getMockSession()
    {
        return session;
    }

    public MockHttpSession getSession()
    {
        return getMockSession();
    }

    public MockActionMapping getMockActionMapping()
    {
        return mapping;
    }

    public MockPageContext getMockPageContext()
    {
        return pageContext;
    }
}
