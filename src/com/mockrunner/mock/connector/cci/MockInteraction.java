package com.mockrunner.mock.connector.cci;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.Interaction;
import javax.resource.cci.InteractionSpec;
import javax.resource.cci.Record;
import javax.resource.cci.ResourceWarning;

/**
 * Mock implementation of <code>Interaction</code>.
 * The <code>execute</code> calls are delegated to
 * {@link com.mockrunner.connector.InteractionHandler}.
 */
public class MockInteraction implements Interaction 
{
	private MockConnection mockConnection;

	public MockInteraction(MockConnection mockConnection) 
    {
		this.mockConnection = mockConnection;
	}

	public void clearWarnings() throws ResourceException 
    {

	}

	public void close() throws ResourceException 
    {
		mockConnection.close();
	}

    /**
     * Calls {@link com.mockrunner.connector.InteractionHandler#execute(InteractionSpec, Record)}.
     */
	public Record execute(InteractionSpec is, Record record) throws ResourceException 
    {
        return mockConnection.getInteractionHandler().execute(is, record);
	}

    /**
     * Calls {@link com.mockrunner.connector.InteractionHandler#execute(InteractionSpec, Record, Record)}.
     */
	public boolean execute(InteractionSpec is, Record request, Record response) throws ResourceException 
    {
		return mockConnection.getInteractionHandler().execute(is, request, response);
	}

	public Connection getConnection() 
    {
		return mockConnection;
	}

    /**
     * Returns <code>null</code>, warnings not supported yet.
     */
	public ResourceWarning getWarnings() throws ResourceException 
    {
		return null;
	}
}
