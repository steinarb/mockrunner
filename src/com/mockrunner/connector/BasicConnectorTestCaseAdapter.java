package com.mockrunner.connector;

import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;

import junit.framework.TestCase;

/**
 * Delegator for {@link com.mockrunner.connector.ConnectorTestModule}. You can
 * subclass this adapter or use {@link com.mockrunner.connector.ConnectorTestModule}
 * directly (so your test case can use another base class).
 * This basic adapter can be used if you don't need any other modules. It
 * does not extend {@link com.mockrunner.base.BaseTestCase}. If you want
 * to use several modules in conjunction, consider subclassing
 * {@link com.mockrunner.connector.ConnectorTestCaseAdapter}.
 * <b>This class is generated from the {@link com.mockrunner.connector.ConnectorTestModule}
 * and should not be edited directly</b>.
 */
public class BasicConnectorTestCaseAdapter extends TestCase
{
    private ConnectorTestModule connectorTestModule;
    private ConnectorMockObjectFactory connectorMockObjectFactory;

    public BasicConnectorTestCaseAdapter()
    {

    }

    public BasicConnectorTestCaseAdapter(String name)
    {
        super(name);
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        connectorTestModule = null;
        connectorMockObjectFactory = null;
    }

    /**
     * Creates the {@link com.mockrunner.connector.ConnectorTestModule}. If you
     * overwrite this method, you must call <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        connectorTestModule = createConnectorTestModule(getConnectorMockObjectFactory());
    }

    /**
     * Creates a {@link com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory}.
     * @return the created {@link com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory}
     */
    protected ConnectorMockObjectFactory createConnectorMockObjectFactory()
    {
        return new ConnectorMockObjectFactory();
    }

    /**
     * Gets the {@link com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory}.
     * @return the {@link com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory}
     */
    protected ConnectorMockObjectFactory getConnectorMockObjectFactory()
    {
        synchronized(ConnectorMockObjectFactory.class)
        {
            if(connectorMockObjectFactory == null)
            {
                connectorMockObjectFactory = createConnectorMockObjectFactory();
            }
        }
        return connectorMockObjectFactory;
    }

    /**
     * Sets the {@link com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory}.
     * @param connectorMockObjectFactory the {@link com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory}
     */
    protected void setConnectorMockObjectFactory(ConnectorMockObjectFactory connectorMockObjectFactory)
    {
        this.connectorMockObjectFactory = connectorMockObjectFactory;
    }

    /**
     * Creates a {@link com.mockrunner.connector.ConnectorTestModule} based on the current
     * {@link com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory}.
     * Same as <code>createConnectorTestModule(getConnectorMockObjectFactory())</code>.
     * @return the created {@link com.mockrunner.connector.ConnectorTestModule}
     */
    protected ConnectorTestModule createConnectorTestModule()
    {
        return new ConnectorTestModule(getConnectorMockObjectFactory());
    }

    /**
     * Creates a {@link com.mockrunner.connector.ConnectorTestModule} with the specified
     * {@link com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory}.
     * @return the created {@link com.mockrunner.connector.ConnectorTestModule}
     */
    protected ConnectorTestModule createConnectorTestModule(ConnectorMockObjectFactory mockFactory)
    {
        return new ConnectorTestModule(mockFactory);
    }

    /**
     * Gets the {@link com.mockrunner.connector.ConnectorTestModule}.
     * @return the {@link com.mockrunner.connector.ConnectorTestModule}
     */
    protected ConnectorTestModule getConnectorTestModule()
    {
        return connectorTestModule;
    }

    /**
     * Sets the {@link com.mockrunner.connector.ConnectorTestModule}.
     * @param connectorTestModule the {@link com.mockrunner.connector.ConnectorTestModule}
     */
    protected void setConnectorTestModule(ConnectorTestModule connectorTestModule)
    {
        this.connectorTestModule = connectorTestModule;
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#getInteractionHandler}
     */
    protected InteractionHandler getInteractionHandler()
    {
        return connectorTestModule.getInteractionHandler();
    }
}