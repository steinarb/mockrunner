package com.mockrunner.mock.web;

import org.apache.struts.Globals;

/**
 * Used to create all types of struts mock objects. Maintains
 * the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure, they are all
 * up to date.
 */
public class ActionMockObjectFactory extends WebMockObjectFactory
{
    private MockActionMapping mapping;
    private MockActionServlet actionServlet;
    private MockModuleConfig moduleConfig;
    
    /**
     * Creates a new set of mock objects.
     */
    public ActionMockObjectFactory()
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
    public ActionMockObjectFactory(WebMockObjectFactory factory)
    {
        super(factory);
        createMockObjects();
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
    public ActionMockObjectFactory(WebMockObjectFactory factory, boolean createNewSession)
    {
        super(factory, createNewSession);
        createMockObjects();
    }
    
    private void createMockObjects()
    {
        mapping = new MockActionMapping();
        moduleConfig = new MockModuleConfig("testmodule");
        actionServlet = new MockActionServlet();
        actionServlet.setServletConfig(getMockServletConfig());
        actionServlet.setServletContext(getMockServletContext());
        getMockRequest().setAttribute(Globals.MAPPING_KEY, mapping);
        getMockRequest().setAttribute(Globals.MODULE_KEY, moduleConfig);
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
     * Returns the {@link com.mockrunner.mock.web.MockModuleConfig}.
     * @return the {@link com.mockrunner.mock.web.MockModuleConfig}
     */
    public MockModuleConfig getMockModuleConfig()
    {
        return moduleConfig;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.web.MockModuleConfig}.
     * @return the {@link com.mockrunner.mock.web.MockModuleConfig}
     */
    public MockActionServlet getMockActionServlet()
    {
        return actionServlet;
    }
}
