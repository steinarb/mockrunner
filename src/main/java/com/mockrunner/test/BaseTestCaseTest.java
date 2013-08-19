package com.mockrunner.test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import javax.naming.Context;

import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockDriver;
import com.mockrunner.mock.jms.JMSMockObjectFactory;
import com.mockrunner.mock.web.ActionMockObjectFactory;
import com.mockrunner.mock.web.WebMockObjectFactory;

public class BaseTestCaseTest extends BaseTestCase
{
    public void testOneDriver() throws Exception
    {
        DriverManager.registerDriver(new TestDriver());
        DriverManager.registerDriver(new TestDriver());
        getJDBCMockObjectFactory();
        Enumeration drivers = DriverManager.getDrivers();
        assertTrue(drivers.nextElement() instanceof MockDriver);
        assertFalse(drivers.hasMoreElements());
        super.tearDown();
        drivers = DriverManager.getDrivers();
        assertTrue(drivers.hasMoreElements());
        drivers.nextElement();
        assertTrue(drivers.hasMoreElements());
    }
    
    public void testTearDownRevertSetAsInitialCalled() throws Exception
    {
        getEJBMockObjectFactory();
        assertEquals(MockContextFactory.class.getName(), System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        super.tearDown();
        assertNull(System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
    }
    
    public void testLazyFactoriesInstance()
    {
        assertSame(getJDBCMockObjectFactory(), getJDBCMockObjectFactory());
        assertSame(getJMSMockObjectFactory(), getJMSMockObjectFactory());
        assertSame(getEJBMockObjectFactory(), getEJBMockObjectFactory());
        assertSame(getWebMockObjectFactory(), getWebMockObjectFactory());
        assertSame(getActionMockObjectFactory(), getActionMockObjectFactory());
        assertSame(getActionMockObjectFactory(), getWebMockObjectFactory());
    }
    
    public void testSetActionAndWebFactories()
    {
        WebMockObjectFactory webFactory = new WebMockObjectFactory();
        setWebMockObjectFactory(webFactory);
        assertSame(webFactory, getWebMockObjectFactory());
        assertNotSame(webFactory, getActionMockObjectFactory());
        ActionMockObjectFactory actionFactory = new ActionMockObjectFactory();
        setActionMockObjectFactory(actionFactory);
        assertSame(actionFactory, getActionMockObjectFactory());
        setWebMockObjectFactory(null);
        assertSame(actionFactory, getWebMockObjectFactory());
    }
    
    public void testSetJMSFactory()
    {
        JMSMockObjectFactory jmsFactory = new JMSMockObjectFactory();
        setJMSMockObjectFactory(jmsFactory);
        assertSame(jmsFactory, getJMSMockObjectFactory());
        setJMSMockObjectFactory(null);
        assertNotNull(getJMSMockObjectFactory());
        assertNotSame(jmsFactory, getJMSMockObjectFactory());
    }
    
    public void testSetJDBCFactory()
    {
        JDBCMockObjectFactory jdbcFactory = new JDBCMockObjectFactory();
        setJDBCMockObjectFactory(jdbcFactory);
        assertSame(jdbcFactory, getJDBCMockObjectFactory());
        setJDBCMockObjectFactory(null);
        assertNotNull(getJDBCMockObjectFactory());
        assertNotSame(jdbcFactory, getJDBCMockObjectFactory());
    }
    
    public void testSetEJBFactory()
    {
        EJBMockObjectFactory ejbFactory = new EJBMockObjectFactory();
        setEJBMockObjectFactory(ejbFactory);
        assertSame(ejbFactory, getEJBMockObjectFactory());
        setEJBMockObjectFactory(null);
        assertNotNull(getEJBMockObjectFactory());
        assertNotSame(ejbFactory, getEJBMockObjectFactory());
    }
    
    public void testSetConnectorFactory()
    {
        ConnectorMockObjectFactory connectorFactory = new ConnectorMockObjectFactory();
        setConnectorMockObjectFactory(connectorFactory);
        assertSame(connectorFactory, getConnectorMockObjectFactory());
        setConnectorMockObjectFactory(null);
        assertNotNull(getConnectorMockObjectFactory());
        assertNotSame(connectorFactory, getConnectorMockObjectFactory());
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
