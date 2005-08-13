package com.mockrunner.mock.connector.cci;

import com.mockrunner.connector.InteractionHandler;

/**
 * Used to create all types of JCA mock objects. 
 * Maintains the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure that they are all
 * up to date. If you are using JNDI for obtaining the
 * <code>MockConnectionFactory</code> you have to bind them to 
 * the mock JNDI context with {@link com.mockrunner.ejb.EJBTestModule#bindToContext}.
 */
public class ConnectorMockObjectFactory
{
    private MockConnectionFactory connectionFactory;
    private MockConnection connection;
    private InteractionHandler interactionHandler;
    
    public ConnectorMockObjectFactory()
    {
        connectionFactory = createMockConnectionFactory();
        connection = createMockConnection();
        connectionFactory.setConnection(connection);
        interactionHandler = new InteractionHandler();
        connection.setInteractionHandler(interactionHandler);
    }

    /**
     * Creates the {@link com.mockrunner.mock.connector.cci.MockConnection} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.connector.cci.MockConnection}.
     * @return the {@link com.mockrunner.mock.connector.cci.MockConnection}
     */
    public MockConnection createMockConnection()
    {
        return new MockConnection();
    }

    /**
     * Creates the {@link com.mockrunner.mock.connector.cci.MockConnectionFactory} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.connector.cci.MockConnectionFactory}.
     * @return the {@link com.mockrunner.mock.connector.cci.MockConnectionFactory}
     */
    public MockConnectionFactory createMockConnectionFactory()
    {
        return new MockConnectionFactory();
    }

    /**
     * Returns the {@link com.mockrunner.mock.connector.cci.MockConnection}.
     * @return the {@link com.mockrunner.mock.connector.cci.MockConnection}
     */
    public MockConnection getMockConnection()
    {
        return connection;
    }

    /**
     * Returns the {@link com.mockrunner.mock.connector.cci.MockConnectionFactory}.
     * @return the {@link com.mockrunner.mock.connector.cci.MockConnectionFactory}
     */
    public MockConnectionFactory getMockConnectionFactory()
    {
        return connectionFactory;
    }

    /**
     * Returns the {@link com.mockrunner.connector.InteractionHandler}.
     * @return the {@link com.mockrunner.connector.InteractionHandler}
     */
    public InteractionHandler getInteractionHandler()
    {
        return interactionHandler;
    }
}
