package com.mockrunner.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;

import org.junit.Test;
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
	@Test
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
    
	@Test
    public void testTearDownRevertSetAsInitialCalled() throws Exception
    {
        getEJBMockObjectFactory();
        assertEquals(MockContextFactory.class.getName(), System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        super.tearDown();
        assertNull(System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
    }
    
	@Test
    public void testLazyFactoriesInstance()
    {
        assertSame(getJDBCMockObjectFactory(), getJDBCMockObjectFactory());
        assertSame(getJMSMockObjectFactory(), getJMSMockObjectFactory());
        assertSame(getEJBMockObjectFactory(), getEJBMockObjectFactory());
        assertSame(getWebMockObjectFactory(), getWebMockObjectFactory());
        assertSame(getActionMockObjectFactory(), getActionMockObjectFactory());
        assertSame(getActionMockObjectFactory(), getWebMockObjectFactory());
    }
    
	@Test
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

	@Test
    public void testSetJMSFactory()
    {
        JMSMockObjectFactory jmsFactory = new JMSMockObjectFactory();
        setJMSMockObjectFactory(jmsFactory);
        assertSame(jmsFactory, getJMSMockObjectFactory());
        setJMSMockObjectFactory(null);
        assertNotNull(getJMSMockObjectFactory());
        assertNotSame(jmsFactory, getJMSMockObjectFactory());
    }
    
	@Test
    public void testSetJDBCFactory()
    {
        JDBCMockObjectFactory jdbcFactory = new JDBCMockObjectFactory();
        setJDBCMockObjectFactory(jdbcFactory);
        assertSame(jdbcFactory, getJDBCMockObjectFactory());
        setJDBCMockObjectFactory(null);
        assertNotNull(getJDBCMockObjectFactory());
        assertNotSame(jdbcFactory, getJDBCMockObjectFactory());
    }
    
	@Test
    public void testSetEJBFactory()
    {
        EJBMockObjectFactory ejbFactory = new EJBMockObjectFactory();
        setEJBMockObjectFactory(ejbFactory);
        assertSame(ejbFactory, getEJBMockObjectFactory());
        setEJBMockObjectFactory(null);
        assertNotNull(getEJBMockObjectFactory());
        assertNotSame(ejbFactory, getEJBMockObjectFactory());
    }
    
	@Test
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

        public Logger getParentLogger() throws SQLFeatureNotSupportedException
        {
            return null;
        }
    }
}
