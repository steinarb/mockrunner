package com.mockrunner.test.jdbc;

import java.sql.DriverManager;
import java.util.Enumeration;

import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockDriver;

public class BasicJDBCTestCaseAdapterTest extends BasicJDBCTestCaseAdapter
{
    private JDBCMockObjectFactory factory;
    
    protected void setUp() throws Exception
    {
        factory = new  JDBCMockObjectFactory();
        setJDBCMockObjectFactory(factory);
        super.setUp();
    }
    
    public void testJDBCFactorySet()
    {
        assertSame(factory, getJDBCMockObjectFactory());
        assertSame(getJDBCMockObjectFactory(), getJDBCMockObjectFactory());
    }
    
    public void testDriverDeregistered() throws Exception
    {
        Enumeration drivers = DriverManager.getDrivers();
        assertTrue(drivers.nextElement() instanceof MockDriver);
        assertFalse(drivers.hasMoreElements());
        super.tearDown();
        drivers = DriverManager.getDrivers();
        while(drivers.hasMoreElements())
        {
            assertFalse(drivers.nextElement() instanceof MockDriver);
        }
    }
}
