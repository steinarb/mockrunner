package com.mockrunner.mock.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Mock implementation of <code>DatSource</code>.
 */
public class MockDataSource implements DataSource
{
    private Connection connection = null;
    private int loginTimeout = 0;
    private PrintWriter logWriter = null;
    
    /**
     * Set up the connection.
     * @param connection the connection
     */
    public void setupConnection(Connection connection)
    {
        this.connection = connection;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.jdbc.MockConnection}. 
     * If the underlying connection is not an instance of 
     * {@link com.mockrunner.mock.jdbc.MockConnection},
     * <code>null</code> is returned.
     * @return the {@link com.mockrunner.mock.jdbc.MockConnection}
     */
    public MockConnection getMockConnection()
    {
        if(connection instanceof MockConnection)
        {
            return (MockConnection)connection;
        }
        return null;
    }

    public int getLoginTimeout() throws SQLException
    {
        return loginTimeout;
    }

    public void setLoginTimeout(int seconds) throws SQLException
    {
        loginTimeout = seconds;
    }

    public PrintWriter getLogWriter() throws SQLException
    {
        return logWriter;
    }

    public void setLogWriter(PrintWriter out) throws SQLException
    {
        logWriter = out;
    }

    public Connection getConnection() throws SQLException
    {
        return connection;
    }

    public Connection getConnection(String username, String password) throws SQLException
    {
        return connection;
    }
}
