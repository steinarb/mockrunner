package com.mockrunner.mock.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Used to create all types of JDBC mock objects. 
 * Maintains the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure, they are all
 * up to date.
 * Please note, that this class removes all drivers
 * from the JDBC <code>DriverManager</code> and registers
 * the {@link MockDriver}. All drivers are preserved and
 * can be restored with {@link #restoreDrivers}.
 */
public class JDBCMockObjectFactory
{
    private MockDataSource dataSource;
    private MockDriver driver;
    private MockConnection connection;
    private Set preservedDrivers;
    
    /**
     * Creates a new set of mock objects.
     */
    public JDBCMockObjectFactory()
    {
        dataSource = new MockDataSource();
        driver = new MockDriver();
        connection = new MockConnection();
        preservedDrivers = new HashSet();
        setUpDependencies();
    }
    
    private void setUpDependencies()
    {
        dataSource.setupConnection(connection);
        driver.setupConnection(connection);
        registerMockDriver();
    }

    private void deregisterDrivers()
    {
        try
        {
            Enumeration drivers = DriverManager.getDrivers();
            while(drivers.hasMoreElements())
            {
                DriverManager.deregisterDriver((Driver)drivers.nextElement());
            }
        }
        catch(SQLException exc)
        {
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    private void deregisterMockDrivers()
    {
        try
        {
            Enumeration drivers = DriverManager.getDrivers();
            while(drivers.hasMoreElements())
            {
                Driver currentDriver = (Driver)drivers.nextElement();
                if(currentDriver instanceof MockDriver)
                {
                    DriverManager.deregisterDriver(currentDriver);
                }
            }
        }
        catch(SQLException exc)
        {
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    private void preserveDrivers()
    {
        Enumeration drivers = DriverManager.getDrivers();
        while(drivers.hasMoreElements())
        {
            Driver currentDriver = (Driver)drivers.nextElement();
            if(!(currentDriver instanceof MockDriver))
            {
                preservedDrivers.add(currentDriver);
            }
        }
    }
    
    /**
     * Removes all JDBC drivers from the <code>DriveManager</code> and
     * registers the mock driver. The removed drivers are preserved and
     * can be restored with {@link #restoreDrivers}.
     */
    public void registerMockDriver()
    {
        try
        {
            preserveDrivers();
            deregisterDrivers();
            DriverManager.registerDriver(driver);
        }
        catch(SQLException exc)
        {
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Since <code>JDBCMockObjectFactory</code> removes all the
     * drivers from the <code>DriveManager</code> (so the
     * {@link MockDriver} is guaranteed to be the only one)
     * you can use this method to restore the original drivers.
     * Automatically called by {@link com.mockrunner.base.BaseTestCase#tearDown}.
     */
    public void restoreDrivers()
    {
        deregisterMockDrivers();
        try
        {
            Iterator drivers = preservedDrivers.iterator();
            while(drivers.hasNext())
            {
                DriverManager.registerDriver((Driver)drivers.next());
            }
        }
        catch(SQLException exc)
        {
            throw new RuntimeException(exc.getMessage());
        }
        preservedDrivers.clear();
    }

    /**
     * Returns the {@link com.mockrunner.mock.jdbc.MockDataSource}.
     * @return the {@link com.mockrunner.mock.jdbc.MockDataSource}
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
}
