package com.mockrunner.test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.jdbc.MockDriver;

public class BaseTestCaseTest extends BaseTestCase
{
    protected void setUp() throws Exception
    {
        DriverManager.registerDriver(new TestDriver());
        DriverManager.registerDriver(new TestDriver());
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        Enumeration drivers = DriverManager.getDrivers();
        assertTrue(drivers.hasMoreElements());
        drivers.nextElement();
        assertTrue(drivers.hasMoreElements());
    }
    
    public void testOneDriver()
    {
        Enumeration drivers = DriverManager.getDrivers();
        assertTrue(drivers.nextElement() instanceof MockDriver);
        assertFalse(drivers.hasMoreElements());
    }

    public static class TestDriver implements Driver
    {

        public boolean acceptsURL(String url) throws SQLException
        {
            return false;
        }

        public Connection connect(String url, Properties info) throws SQLException
        {
            return null;
        }

        public int getMajorVersion()
        {
            return 0;
        }

        public int getMinorVersion()
        {
            return 0;
        }

        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException
        {
            return null;
        }

        public boolean jdbcCompliant()
        {
            return false;
        }
    }
}
