package com.mockrunner.mock.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;


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
    private MockDriver driver;
    private MockConnection connection;
    private MockUserTransaction transaction;
    
    /**
     * Creates a new set of mock objects.
     */
    public JDBCMockObjectFactory()
    {
        dataSource = new MockDataSource();
        driver = new MockDriver();
        connection = new MockConnection();
        transaction = new MockUserTransaction();
        setUpDependencies();
    }
    
    private void setUpDependencies()
    {
        dataSource.setupConnection(connection);
        driver.setupConnection(connection);
        try
        {
            Enumeration drivers = DriverManager.getDrivers();
            while(drivers.hasMoreElements())
            {
                DriverManager.deregisterDriver((Driver)drivers.nextElement());
            }
            DriverManager.registerDriver(driver);
        }
        catch(SQLException exc)
        {
            throw new RuntimeException(exc.getMessage());
        }
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
     * Returns the {@link com.mockrunner.mock.jdbc.MockDriver}.
     * @return the {@link com.mockrunner.mock.jdbc.MockDriver}
     */
    public MockDriver getMockDriver()
    {
        return driver;
    }

    /**
     * Returns the {@link com.mockrunner.mock.jdbc.MockConnection}.
     * @return the {@link com.mockrunner.mock.jdbc.MockConnection}
     */
    public MockConnection getMockConnection()
    {
        return connection;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.jdbc.MockUserTransaction}.
     * @return the {@link com.mockrunner.mock.jdbc.MockUserTransaction}
     */
    public MockUserTransaction getMockUserTransaction()
    {
        return transaction;
    }
}
