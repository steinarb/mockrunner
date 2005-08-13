package com.mockrunner.mock.web;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;

import com.mockrunner.struts.ActionMappingProxyGenerator;

/**
 * Used to create all types of struts mock objects. Maintains
 * the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure that they are all
 * up to date.
 */
public class ActionMockObjectFactory extends WebMockObjectFactory
{
    private MockActionMapping mockMapping;
    private ActionMapping mapping;
    private MockActionServlet mockActionServlet;
    private MockModuleConfig mockModuleConfig;
    
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
        mockMapping = createMockActionMapping();
        mapping = mockMapping;
        mockModuleConfig = createMockModuleConfig();
        mockActionServlet = createMockActionServlet();
        mockActionServlet.setServletConfig(getMockServletConfig());
        mockActionServlet.setServletContext(getMockServletContext());
        refresh();
    }

    /**
     * Refreshes the mock objects dependencies. May be called after setting request
     * and response wrappers.
     */
    public void refresh()
    {
        super.refresh();
        getWrappedRequest().setAttribute(Globals.MAPPING_KEY, mapping);
        getWrappedRequest().setAttribute(Globals.MODULE_KEY, mockModuleConfig);
    }
    
    /**
     * Creates the {@link com.mockrunner.mock.web.MockActionServlet} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.web.MockActionServlet}.
     * @return the {@link com.mockrunner.mock.web.MockActionServlet}
     */
    public MockActionServlet createMockActionServlet()
    {
        return new MockActionServlet();
    }

    /**
     * Creates the {@link com.mockrunner.mock.web.MockModuleConfig} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.web.MockModuleConfig}.
     * @return the {@link com.mockrunner.mock.web.MockModuleConfig}
     */
    public MockModuleConfig createMockModuleConfig()
    {
        return new MockModuleConfig("testmodule");
    }

    /**
     * Creates the {@link com.mockrunner.mock.web.MockActionMapping} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.web.MockActionMapping}.
     * @return the {@link com.mockrunner.mock.web.MockActionMapping}
     */
    public MockActionMapping createMockActionMapping()
    {
        return new MockActionMapping();
    }
    
    /**
     * Prepares an <code>ActionMapping</code>. If your actions rely
     * on a custom subclass of <code>ActionMapping</code>, use this
     * method to prepare it. Since {@link com.mockrunner.struts.ActionTestModule}
     * relies on the behaviour of {@link com.mockrunner.mock.web.MockActionMapping},
     * this method creates a subclass CGLib proxy of the specified mapping class.
     * You can cast the returned <code>ActionMapping</code> to your custom
     * mapping class and the subclass proxy will redirect the necessary
     * methods to the {@link com.mockrunner.mock.web.MockActionMapping}.
     * Redirected are methods for retrieving forwards. If an <code>ActionMapping</code>
     * is prepared, {@link #getActionMapping} returns the prepared mapping while
     * {@link #getMockActionMapping} returns the the underlying {@link com.mockrunner.mock.web.MockActionMapping}.
     * This method relies on CGLib. CGLib is not required by the Struts test framework
     * if this method is not used.
     * @param mappingClass the class of the custom action mapping
     * @return an instance of the custom action mapping class
     */
    public ActionMapping prepareActionMapping(Class mappingClass)
    {
        ActionMappingProxyGenerator generator = new ActionMappingProxyGenerator(mockMapping);
        mapping = generator.createActionMappingProxy(mappingClass);
        refresh();
        return mapping;
    }
    
    /**
     * Resets <code>ActionMapping</code> configuration, i.e. sets
     * the current <code>ActionMapping</code> returned by {@link #getActionMapping}
     * to the mock action mapping returned by {@link #getMockActionMapping}.
     */
    public void resetActionMapping()
    {
        mapping = mockMapping;
    }
    
    /**
     * Returns the <code>ActionMapping</code>. Unless you prepare an
     * <code>ActionMapping</code> using {@link #prepareActionMapping},
     * this method returns the same object as {@link #getMockActionMapping}.
     * If an <code>ActionMapping</code> is prepared, this method returns
     * the prepared <code>ActionMapping</code> while {@link #getMockActionMapping}
     * returns the underlying {@link com.mockrunner.mock.web.MockActionMapping}.
     * @return the <code>ActionMapping</code>
     */
    public ActionMapping getActionMapping()
    {
        return mapping;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.web.MockActionMapping}.
     * @return the {@link com.mockrunner.mock.web.MockActionMapping}
     */
    public MockActionMapping getMockActionMapping()
    {
        return mockMapping;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.web.MockModuleConfig}.
     * @return the {@link com.mockrunner.mock.web.MockModuleConfig}
     */
    public MockModuleConfig getMockModuleConfig()
    {
        return mockModuleConfig;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.web.MockActionServlet}.
     * @return the {@link com.mockrunner.mock.web.MockActionServlet}
     */
    public MockActionServlet getMockActionServlet()
    {
        return mockActionServlet;
    }
}
