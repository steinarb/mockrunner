package com.mockrunner.test.jdbc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.DriverManager;
import java.util.Enumeration;

import org.junit.Before;
import org.junit.Test;

import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockDriver;
import java.sql.Driver;

public class BasicJDBCTestCaseAdapterTest extends BasicJDBCTestCaseAdapter
{
    private JDBCMockObjectFactory factory;
    
    @Before
    public void setUp() throws Exception
    {
        factory = new  JDBCMockObjectFactory();
        setJDBCMockObjectFactory(factory);
        super.setUp();
    }
    
    @Test
    public void testJDBCFactorySet()
    {
        assertSame(factory, getJDBCMockObjectFactory());
        assertSame(getJDBCMockObjectFactory(), getJDBCMockObjectFactory());
    }
    
    @Test
    public void testDriverDeregistered() throws Exception
    {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
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
