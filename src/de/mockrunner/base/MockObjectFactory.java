package de.mockrunner.base;

import com.mockobjects.servlet.MockServletConfig;

import de.mockrunner.mock.MockActionMapping;
import de.mockrunner.mock.MockHttpServletRequest;
import de.mockrunner.mock.MockHttpServletResponse;
import de.mockrunner.mock.MockHttpSession;
import de.mockrunner.mock.MockPageContext;
import de.mockrunner.mock.MockServletContext;

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
     * Constructor calls {@link #createMockObjects}
     */
    public MockObjectFactory()
    {
        createMockObjects();
    }
    
    /**
     * Constructor calls {@link #createMockObjectsBasedOn(MockObjectFactory)}
     */
    public MockObjectFactory(MockObjectFactory factory)
    {
        createMockObjectsBasedOn(factory);
    }

    /**
     * Creates a new set of mock objects.
     */
    public void createMockObjects()
    {
        createNewMockObjects();
        config = new MockServletConfig();
        context = new MockServletContext();
        setUpDependencies();
    }

    /**
     * Creates a set of mock objects based on another one.
     * The created mock objects will have their own
     * request and session objects, but they will share
     * one <code>ServletContext</code>.
     * @param factory the other factory
     * @see BaseTestCase#createMockObjectFactory(MockObjectFactory)
     */
    public void createMockObjectsBasedOn(MockObjectFactory factory)
    {
        createNewMockObjects();
        config = factory.getMockServletConfig();
        context = factory.getMockServletContext();
        setUpDependencies();
    }

    private void createNewMockObjects()
    {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
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
