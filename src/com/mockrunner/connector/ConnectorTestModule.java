package com.mockrunner.connector;

import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;

/**
 * Module for JCA tests.
 */
public class ConnectorTestModule 
{
	private ConnectorMockObjectFactory mockFactory;

	public ConnectorTestModule(ConnectorMockObjectFactory mockFactory)
	{
		this.mockFactory = mockFactory;
	}
    
    public InteractionHandler getInteractionHandler()
    {
        return mockFactory.getInteractionHandler();
    }
}
