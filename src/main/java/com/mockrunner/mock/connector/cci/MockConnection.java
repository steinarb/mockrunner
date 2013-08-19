package com.mockrunner.mock.connector.cci;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionMetaData;
import javax.resource.cci.Interaction;
import javax.resource.cci.LocalTransaction;
import javax.resource.cci.ResultSetInfo;

import com.mockrunner.connector.InteractionHandler;

/**
 * Mock implementation of <code>Connection</code>.
 */
public class MockConnection implements Connection
{
    private boolean closed;
    private InteractionHandler interactionHandler;
    private ConnectionMetaData metaData;
    private LocalTransaction localTransaction;
    private ResultSetInfo resultSetInfo;
    private List interactions;
    
    public MockConnection()
    {
        closed = false;
        metaData = new MockConnectionMetaData();
        localTransaction = new MockLocalTransaction();
        resultSetInfo = new MockResultSetInfo();
        interactions = new ArrayList();
    }

    /**
     * Returns the {@link MockLocalTransaction}. If the underlying
     * <code>LocalTransaction</code> is not an instance of
     * {@link MockLocalTransaction}, this method returns <code>null</code>.
     * Otherwise it returns the same object as {@link #getLocalTransaction}.
     * @return the {@link MockLocalTransaction}
     */
    public MockLocalTransaction getMockLocalTransaction()
    {
        if(localTransaction instanceof MockLocalTransaction)
        {
            return (MockLocalTransaction) localTransaction;
        }
        return null;
    }

    /**
     * Returns the list of all created <code>Interaction</code> objects.
     * 
     * @return the list <code>Interaction</code> objects
     */
    public List getInteractionList()
    {
        return Collections.unmodifiableList(interactions);
    }

    public void close() throws ResourceException
    {
        for(int ii = 0; ii < interactions.size(); ii++)
        {
            ((Interaction)interactions.get(ii)).close();
        }
        closed = true;
    }

    public Interaction createInteraction() throws ResourceException
    {
        Interaction interaction = new MockInteraction(this);
        interactions.add(interaction);
        return interaction;
    }

    public LocalTransaction getLocalTransaction() throws ResourceException
    {
        return localTransaction;
    }

    public ConnectionMetaData getMetaData() throws ResourceException
    {
        return metaData;
    }

    public ResultSetInfo getResultSetInfo() throws ResourceException
    {
        return resultSetInfo;
    }

    /**
     * Returns if this <code>Connection</code> is closed.
     * @return <code>true</code> if this <code>Interaction</code> is closed,
     *         <code>false</code> otherwise
     */
    public boolean isClosed()
    {
        return closed;
    }

    public InteractionHandler getInteractionHandler()
    {
        return interactionHandler;
    }

    /**
     * Sets this connections <code>ResultSetInfo</code>
     * @param resultSetInfo the <code>ResultSetInfo</code>
     */
    public void setResultSetInfo(ResultSetInfo resultSetInfo)
    {
        this.resultSetInfo = resultSetInfo;
    }

    /**
     * Sets this connections <code>InteractionHandler</code>
     * @param interactionHandler the <code>InteractionHandler</code>
     */
    public void setInteractionHandler(InteractionHandler interactionHandler)
    {
        this.interactionHandler = interactionHandler;
    }

    /**
     * Sets this connections meta data
     * @param metaData the meta data
     */
    public void setMetaData(ConnectionMetaData metaData)
    {
        this.metaData = metaData;
    }

    /**
     * Sets the <code>LocalTransaction</code>
     * @param localTransaction the <code>LocalTransaction</code>
     */
    public void setLocalTransaction(LocalTransaction localTransaction)
    {
        this.localTransaction = localTransaction;
    }
}
