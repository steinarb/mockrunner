package com.mockrunner.test.jdbc;

import java.sql.DriverManager;
import java.util.Enumeration;

import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;

import junit.framework.TestCase;

public class JDBCMockObjectFactoryTest extends TestCase
{
    public void testCreate() throws Exception
    {
        JDBCMockObjectFactory factory =  new JDBCMockObjectFactory();
        assertTrue(factory.getMockDriver().connect("", null) == factory.getMockDataSource().getConnection());
        assertNotNull(DriverManager.getConnection("test"));
        assertTrue(factory.getMockDriver().connect("", null) == DriverManager.getConnection("test"));
        Enumeration drivers = DriverManager.getDrivers();
        drivers.nextElement();
        assertFalse(drivers.hasMoreElements());
    }
}
