package com.mockrunner.mock.connector.cci;

import com.mockrunner.connector.InteractionHandler;

/**
 * Used to create all types of JCA mock objects. 
 * Maintains the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure, they are all
 * up to date. If you are using JNDI for obtaining the
 * factories you have to bind them to the mock context
 * with {@link com.mockrunner.ejb.EJBTestModule#bindToContext}.
 */
public class ConnectorMockObjectFactory
{
    private MockConnectionFactory connectionFactory;
    private MockConnection connection;
    private InteractionHandler interactionHandler;
    
    public ConnectorMockObjectFactory()
    {
        connectionFactory = new MockConnectionFactory();
        connection = new MockConnection();
        connectionFactory.setConnection(connection);
        interactionHandler = new InteractionHandler();
        connection.setInteractionHandler(interactionHandler);
    }

    public MockConnection getMockConnection()
    {
        return connection;
    }

    public MockConnectionFactory getMockConnectionFactory()
    {
        return connectionFactory;
    }

    public InteractionHandler getInteractionHandler()
    {
        return interactionHandler;
    }
}
