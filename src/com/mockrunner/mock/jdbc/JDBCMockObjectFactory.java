package com.mockrunner.mock.jdbc;

import com.mockobjects.sql.MockDataSource;

/**
 * Used to create all types of JDBC and EJB mock objects. 
 * Maintains the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure, they are all
 * up to date.
 */
public class JDBCMockObjectFactory
{
    private MockDataSource dataSource;
    private MockConnection connection;
    
    /**
     * Creates a new set of mock objects.
     */
    public JDBCMockObjectFactory()
    {
        dataSource = new MockDataSource();
        connection = new MockConnection();
        dataSource.setupConnection(connection);
    }

    /**
     * Returns the <code>MockDataSource</code>
     * @return the <code>MockDataSource</code>
     */
    public MockDataSource getMockDataSource()
    {
        return dataSource;
    }

    /**
     * Returns the {@link com.mockrunner.mock.jdbc.MockConnection}.
     * @return the {@link com.mockrunner.mock.jdbc.MockConnection}
     */
    public MockConnection getMockConnection()
    {
        return connection;
    }
}
