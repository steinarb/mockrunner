package com.mockrunner.test;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jms.JMSMockObjectFactory;
import com.mockrunner.mock.web.ActionMockObjectFactory;
import com.mockrunner.mock.web.WebMockObjectFactory;

public class ExtendedBaseTestCaseTest extends BaseTestCase
{
    private ActionMockObjectFactory actionMockFactory;
    private JDBCMockObjectFactory jdbcMockFactory;
    private EJBMockObjectFactory ejbMockFactory;
    private ConnectorMockObjectFactory connectorMockFactory;
    private JMSMockObjectFactory jmsMockFactory;

    protected void setUp() throws Exception
    {
        super.setUp();
        actionMockFactory = new ActionMockObjectFactory() {};
        jdbcMockFactory = new JDBCMockObjectFactory() {};
        ejbMockFactory = new EJBMockObjectFactory() {};
        connectorMockFactory = new ConnectorMockObjectFactory() {};
        jmsMockFactory = new JMSMockObjectFactory() {};
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        actionMockFactory = null;
        jdbcMockFactory = null;
        ejbMockFactory = null;
        connectorMockFactory = null;
        jmsMockFactory = null;
    }
    
    public void testGetFactories()
    {
        assertSame(actionMockFactory, getActionMockObjectFactory());
        assertSame(actionMockFactory, getWebMockObjectFactory());
        assertSame(jdbcMockFactory, getJDBCMockObjectFactory());
        assertSame(ejbMockFactory, getEJBMockObjectFactory());
        assertSame(connectorMockFactory, getConnectorMockObjectFactory());
        assertSame(jmsMockFactory, getJMSMockObjectFactory());
    }

    protected ActionMockObjectFactory createActionMockObjectFactory()
    {
        return actionMockFactory;
    }

    protected ConnectorMockObjectFactory createConnectorMockObjectFactory()
    {
        return connectorMockFactory;
    }

    protected EJBMockObjectFactory createEJBMockObjectFactory()
    {
        return ejbMockFactory;
    }

    protected JDBCMockObjectFactory createJDBCMockObjectFactory()
    {
        return jdbcMockFactory;
    }

    protected JMSMockObjectFactory createJMSMockObjectFactory()
    {
        return jmsMockFactory;
    }

    protected WebMockObjectFactory createWebMockObjectFactory()
    {
        return actionMockFactory;
    }
}
