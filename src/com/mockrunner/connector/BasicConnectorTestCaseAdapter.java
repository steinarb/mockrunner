package com.mockrunner.connector;

import java.util.List;

import junit.framework.TestCase;

import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;

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
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#verifyConnectionClosed}
     */
    protected void verifyConnectionClosed()
    {
        connectorTestModule.verifyConnectionClosed();
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#getInteractionHandler}
     */
    protected InteractionHandler getInteractionHandler()
    {
        return connectorTestModule.getInteractionHandler();
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#getInteractionList}
     */
    protected List getInteractionList()
    {
        return connectorTestModule.getInteractionList();
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#getCreatedIndexedRecords(String)}
     */
    protected List getCreatedIndexedRecords(String recordName)
    {
        return connectorTestModule.getCreatedIndexedRecords(recordName);
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#getCreatedIndexedRecords}
     */
    protected List getCreatedIndexedRecords()
    {
        return connectorTestModule.getCreatedIndexedRecords();
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#getCreatedMappedRecords(String)}
     */
    protected List getCreatedMappedRecords(String recordName)
    {
        return connectorTestModule.getCreatedMappedRecords(recordName);
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#getCreatedMappedRecords}
     */
    protected List getCreatedMappedRecords()
    {
        return connectorTestModule.getCreatedMappedRecords();
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#verifyAllInteractionsClosed}
     */
    protected void verifyAllInteractionsClosed()
    {
        connectorTestModule.verifyAllInteractionsClosed();
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#verifyInteractionClosed(int)}
     */
    protected void verifyInteractionClosed(int index)
    {
        connectorTestModule.verifyInteractionClosed(index);
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#verifyNumberCreatedIndexedRecords(int)}
     */
    protected void verifyNumberCreatedIndexedRecords(int expected)
    {
        connectorTestModule.verifyNumberCreatedIndexedRecords(expected);
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#verifyNumberCreatedIndexedRecords(String, int)}
     */
    protected void verifyNumberCreatedIndexedRecords(String recordName, int expected)
    {
        connectorTestModule.verifyNumberCreatedIndexedRecords(recordName, expected);
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#verifyNumberCreatedMappedRecords(String, int)}
     */
    protected void verifyNumberCreatedMappedRecords(String recordName, int expected)
    {
        connectorTestModule.verifyNumberCreatedMappedRecords(recordName, expected);
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#verifyNumberCreatedMappedRecords(int)}
     */
    protected void verifyNumberCreatedMappedRecords(int expected)
    {
        connectorTestModule.verifyNumberCreatedMappedRecords(expected);
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#verifyLocalTransactionCommitted}
     */
    protected void verifyLocalTransactionCommitted()
    {
        connectorTestModule.verifyLocalTransactionCommitted();
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#verifyLocalTransactionNotCommitted}
     */
    protected void verifyLocalTransactionNotCommitted()
    {
        connectorTestModule.verifyLocalTransactionNotCommitted();
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#verifyLocalTransactionRolledBack}
     */
    protected void verifyLocalTransactionRolledBack()
    {
        connectorTestModule.verifyLocalTransactionRolledBack();
    }

    /**
     * Delegates to {@link com.mockrunner.connector.ConnectorTestModule#verifyLocalTransactionNotRolledBack}
     */
    protected void verifyLocalTransactionNotRolledBack()
    {
        connectorTestModule.verifyLocalTransactionNotRolledBack();
    }
}