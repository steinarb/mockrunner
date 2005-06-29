package com.mockrunner.connector;

import com.mockrunner.base.BaseTestCase;

/**
 * Delegator for {@link com.mockrunner.connector.ConnectorTestModule}. You can
 * subclass this adapter or use {@link com.mockrunner.connector.ConnectorTestModule}
 * directly (so your test case can use another base class).
 * This adapter extends {@link com.mockrunner.base.BaseTestCase}.
 * It can be used if you want to use several modules in conjunction.
 * <b>This class is generated from the {@link com.mockrunner.connector.ConnectorTestModule}
 * and should not be edited directly</b>.
 */
public class ConnectorTestCaseAdapter extends BaseTestCase
{
    private ConnectorTestModule connectorTestModule;

    public ConnectorTestCaseAdapter()
    {

    }

    public ConnectorTestCaseAdapter(String name)
    {
        super(name);
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        connectorTestModule = null;
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