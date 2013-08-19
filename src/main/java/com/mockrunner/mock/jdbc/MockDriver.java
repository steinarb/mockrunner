package com.mockrunner.mock.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Mock implementation of a JDBC <code>Driver</code>.
 */
public class MockDriver implements Driver
{
    private Connection connection;
    
    public void setupConnection(Connection connection)
    {
        this.connection = connection;
    }
  
    public int getMajorVersion()
    {
        return 1;
    }

    public int getMinorVersion()
    {
        return 0;
    }

    public boolean jdbcCompliant()
    {
        return true;
    }

    public boolean acceptsURL(String url) throws SQLException
    {
        return true;
    }

    public Connection connect(String url, Properties info) throws SQLException
    {
        return connection;
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException
    {
        return new DriverPropertyInfo[0];
    }
}
