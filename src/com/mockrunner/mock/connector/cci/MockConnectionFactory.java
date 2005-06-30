package com.mockrunner.mock.connector.cci;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ConnectionSpec;
import javax.resource.cci.RecordFactory;
import javax.resource.cci.ResourceAdapterMetaData;

/**
 * Mock implementation of <code>ConnectionFactory</code>
 */
public class MockConnectionFactory implements ConnectionFactory
{
    private Connection connection;
    private ResourceAdapterMetaData metaData;
    private Reference reference;

    public MockConnectionFactory()
    {
        metaData = new MockResourceAdapterMetaData();
    }

    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }

    public Connection getConnection() throws ResourceException
    {
        return connection;
    }

    public MockConnection getMockConnection()
    {
        if(connection instanceof MockConnection)
        {
            return (MockConnection)connection;
        }
        return null;
    }

    public Connection getConnection(ConnectionSpec cs) throws ResourceException
    {
        return connection;
    }

    public RecordFactory getRecordFactory() throws ResourceException
    {
        return null;
    }

    public ResourceAdapterMetaData getMetaData() throws ResourceException
    {
        return metaData;
    }

    public void setReference(Reference reference)
    {
        this.reference = reference;
    }

    public Reference getReference() throws NamingException
    {
        return reference;
    }
    
    /**
     * Sets the resource adapter meta data
     * @param metaData the resource adapter meta data
     */
    public void setMetaData(ResourceAdapterMetaData metaData) throws ResourceException
    {
        this.metaData = metaData;
    }
}
