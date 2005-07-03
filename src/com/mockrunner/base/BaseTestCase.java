package com.mockrunner.base;

import junit.framework.TestCase;

import com.mockrunner.connector.ConnectorTestModule;
import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;
import com.mockrunner.mock.connector.cci.MockConnectionFactory;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jms.JMSMockObjectFactory;
import com.mockrunner.mock.web.ActionMockObjectFactory;
import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.servlet.ServletTestModule;
import com.mockrunner.struts.ActionTestModule;
import com.mockrunner.tag.TagTestModule;

/**
 * Base class for all standard adapters. Not used for basic adapters.
 */
public abstract class BaseTestCase extends TestCase
{
    private WebMockObjectFactory webMockFactory;
    private ActionMockObjectFactory actionMockFactory;
    private JDBCMockObjectFactory jdbcMockFactory;
    private EJBMockObjectFactory ejbMockFactory;
    private ConnectorMockObjectFactory connectorMockFactory;
    private JMSMockObjectFactory jmsMockFactory;
    
    public BaseTestCase()
    {
        
    }

    public BaseTestCase(String arg0)
    {
        super(arg0);
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        if(null != jdbcMockFactory)
        {
            jdbcMockFactory.restoreDrivers();
            jdbcMockFactory = null;
        }
        if(null != ejbMockFactory)
        {
            ejbMockFactory.resetMockContextFactory();
            ejbMockFactory = null;
        }
        webMockFactory = null;
        actionMockFactory = null;
        jmsMockFactory  = null;
        connectorMockFactory = null;
    }

    /**
     * Creates the mock object factories. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    } 

    /**
     * Creates a {@link WebMockObjectFactory}. 
     * @return the created {@link WebMockObjectFactory}
     */
    protected WebMockObjectFactory createWebMockObjectFactory()
    {
        return new WebMockObjectFactory();
    }

    /**
     * Same as <code>createWebMockObjectFactory(otherFactory, true)</code>
     */
    protected WebMockObjectFactory createWebMockObjectFactory(WebMockObjectFactory otherFactory)
    {
        return new WebMockObjectFactory(otherFactory);
    }
    
    /**
     * Creates a {@link com.mockrunner.mock.web.WebMockObjectFactory} based on another one.
     * The created {@link com.mockrunner.mock.web.WebMockObjectFactory} will have its own
     * request and response objects. If you set <i>createNewSession</i>
     * to <code>true</code> it will also have its own session object.
     * The two factories will share one <code>ServletContext</code>.
     * Especially important for multithreading tests.
     * If you set <i>createNewSession</i> to false, the two factories
     * will share one session. This setting simulates multiple requests
     * from the same client.
     * @param otherFactory the other factory
     * @param createNewSession create a new session for the new factory
     * @return the created {@link com.mockrunner.mock.web.WebMockObjectFactory}
     */
    protected WebMockObjectFactory createWebMockObjectFactory(WebMockObjectFactory otherFactory, boolean createNewSession)
    {
        return new WebMockObjectFactory(otherFactory, createNewSession);
    }

    /**
     * Gets the current {@link WebMockObjectFactory}.
     * @return the {@link WebMockObjectFactory}
     */
    protected WebMockObjectFactory getWebMockObjectFactory()
    {
        synchronized(ActionMockObjectFactory.class) 
        {
            if(webMockFactory == null)
            {
                webMockFactory = getActionMockObjectFactory();
            }
        }
        return webMockFactory;
    }
    
    /**
     * Sets the current {@link WebMockObjectFactory}.
     * @param mockFactory the {@link WebMockObjectFactory}
     */
    protected void setWebMockObjectFactory(WebMockObjectFactory mockFactory)
    {
        this.webMockFactory = mockFactory;
    }
    
    /**
     * Creates a {@link ActionMockObjectFactory}. 
     * @return the created {@link ActionMockObjectFactory}
     */
    protected ActionMockObjectFactory createActionMockObjectFactory()
    {
        return new ActionMockObjectFactory();
    }

    /**
     * Same as <code>createActionMockObjectFactory(otherFactory, true)</code>
     */
    protected ActionMockObjectFactory createActionMockObjectFactory(WebMockObjectFactory otherFactory)
    {
        return new ActionMockObjectFactory(otherFactory);
    }
    
    /**
     * Creates a {@link com.mockrunner.mock.web.ActionMockObjectFactory} based on 
     * another {@link com.mockrunner.mock.web.WebMockObjectFactory}.
     * @param otherFactory the other factory
     * @param createNewSession create a new session for the new factory
     * @return the created {@link com.mockrunner.mock.web.ActionMockObjectFactory}
     * @see #createWebMockObjectFactory(WebMockObjectFactory, boolean)
     */
    protected ActionMockObjectFactory createActionMockObjectFactory(WebMockObjectFactory otherFactory, boolean createNewSession)
    {
        return new ActionMockObjectFactory(otherFactory, createNewSession);
    }

    /**
     * Gets the current {@link ActionMockObjectFactory}.
     * @return the {@link ActionMockObjectFactory}
     */
    protected ActionMockObjectFactory getActionMockObjectFactory()
    {
        synchronized(ActionMockObjectFactory.class) 
        {
            if(actionMockFactory == null)
            {
                actionMockFactory = createActionMockObjectFactory();
            }
        }
        return actionMockFactory;
    }
    
    /**
     * Sets the current {@link ActionMockObjectFactory}.
     * @param mockFactory the {@link ActionMockObjectFactory}
     */
    protected void setActionMockObjectFactory(ActionMockObjectFactory mockFactory)
    {
        this.actionMockFactory = mockFactory;
    }
    
    /**
     * Creates a {@link JDBCMockObjectFactory}. 
     * @return the created {@link JDBCMockObjectFactory}
     */
    protected JDBCMockObjectFactory createJDBCMockObjectFactory()
    {
        return new JDBCMockObjectFactory();
    }
    
    /**
     * Gets the current {@link JDBCMockObjectFactory}.
     * @return the {@link JDBCMockObjectFactory}
     */
    protected JDBCMockObjectFactory getJDBCMockObjectFactory()
    {
        synchronized(JDBCMockObjectFactory.class) 
        {
            if(jdbcMockFactory == null)
            {
                jdbcMockFactory = createJDBCMockObjectFactory();
            }
        }
        return jdbcMockFactory;
    }

    /**
     * Sets the current {@link JDBCMockObjectFactory}.
     * @param mockFactory the {@link JDBCMockObjectFactory}
     */
    protected void setJDBCMockObjectFactory(JDBCMockObjectFactory mockFactory)
    {
        this.jdbcMockFactory = mockFactory;
    }
    
    /**
     * Creates a {@link EJBMockObjectFactory}. 
     * @return the created {@link EJBMockObjectFactory}
     */
    protected EJBMockObjectFactory createEJBMockObjectFactory()
    {
        return new EJBMockObjectFactory();
    }

    /**
     * Gets the current {@link EJBMockObjectFactory}.
     * @return the {@link EJBMockObjectFactory}
     */
    protected EJBMockObjectFactory getEJBMockObjectFactory()
    {
        synchronized(EJBMockObjectFactory.class) 
        {
            if(ejbMockFactory == null)
            {
                ejbMockFactory = createEJBMockObjectFactory();
            }
        }
        return ejbMockFactory;
    }

    /**
     * Sets the current {@link EJBMockObjectFactory}.
     * @param mockFactory the {@link EJBMockObjectFactory}
     */
    protected void setEJBMockObjectFactory(EJBMockObjectFactory mockFactory)
    {
        this.ejbMockFactory = mockFactory;
    }
    
	/**
	 * Creates a {@link ConnectorMockObjectFactory}. 
	 * @return the created {@link ConnectorMockObjectFactory}
	 */
	protected ConnectorMockObjectFactory createConnectorMockObjectFactory()
	{
	    return new ConnectorMockObjectFactory();
	}

	/**
	 * Gets the current {@link ConnectorMockObjectFactory}.
	 * @return the {@link ConnectorMockObjectFactory}
	 */
	protected ConnectorMockObjectFactory getConnectorMockObjectFactory()
	{
	    synchronized(MockConnectionFactory.class) 
		{
		    if(connectorMockFactory == null)
			{
			    connectorMockFactory = createConnectorMockObjectFactory();
		    }
		}
		return connectorMockFactory;
	}

	/**
	 * Sets the current {@link ConnectorMockObjectFactory}.
	 * @param mockFactory the {@link ConnectorMockObjectFactory}
	 */
	protected void setConnectorMockObjectFactory(ConnectorMockObjectFactory mockFactory)
	{
	    this.connectorMockFactory = mockFactory;
	}
    
    /**
     * Creates a {@link JMSMockObjectFactory}. 
     * @return the created {@link JMSMockObjectFactory}
     */
    protected JMSMockObjectFactory createJMSMockObjectFactory()
    {
        return new JMSMockObjectFactory();
    }

    /**
     * Gets the current {@link JMSMockObjectFactory}.
     * @return the {@link JMSMockObjectFactory}
     */
    protected JMSMockObjectFactory getJMSMockObjectFactory()
    {
        synchronized(JMSMockObjectFactory.class) 
        {
            if(jmsMockFactory == null)
            {
                jmsMockFactory = createJMSMockObjectFactory();
            }
        }
        return jmsMockFactory;
    }

    /**
     * Sets the current {@link JMSMockObjectFactory}.
     * @param mockFactory the {@link JMSMockObjectFactory}
     */
    protected void setJMSMockObjectFactory(JMSMockObjectFactory mockFactory)
    {
        this.jmsMockFactory = mockFactory;
    }
    
    /**
     * Creates an {@link com.mockrunner.struts.ActionTestModule} with the specified
     * {@link WebMockObjectFactory}.
     * @param mockFactory the {@link ActionMockObjectFactory}
     * @return the created {@link com.mockrunner.struts.ActionTestModule}
     */
    protected ActionTestModule createActionTestModule(ActionMockObjectFactory mockFactory)
    {
        return new ActionTestModule(mockFactory);
    }
    
    /**
     * Creates an {@link com.mockrunner.struts.ActionTestModule} based on the current
     * {@link WebMockObjectFactory}.
     * Same as <code>createActionTestModule(getActionMockObjectFactory())</code>.
     * @return the created {@link com.mockrunner.struts.ActionTestModule}
     */
    protected ActionTestModule createActionTestModule()
    {
        return new ActionTestModule(getActionMockObjectFactory());
    }
    
    /**
     * Creates a {@link com.mockrunner.tag.TagTestModule} with the specified
     * {@link WebMockObjectFactory}.
     * @return the created {@link com.mockrunner.tag.TagTestModule}
     */
    protected TagTestModule createTagTestModule(WebMockObjectFactory mockFactory)
    {
        return new TagTestModule(mockFactory);
    }
    
    /**
     * Creates a {@link com.mockrunner.tag.TagTestModule} based on the current
     * {@link WebMockObjectFactory}.
     * Same as <code>createTagTestModule(getWebMockObjectFactory())</code>.
     * @return the created {@link com.mockrunner.tag.TagTestModule}
     */
    protected TagTestModule createTagTestModule()
    {
        return new TagTestModule(getWebMockObjectFactory());
    }
    
    /**
     * Creates a {@link com.mockrunner.servlet.ServletTestModule} with the specified
     * {@link WebMockObjectFactory}.
     * @return the created {@link com.mockrunner.servlet.ServletTestModule}
     */
    protected ServletTestModule createServletTestModule(WebMockObjectFactory mockFactory)
    {
        return new ServletTestModule(mockFactory);
    }

    /**
     * Creates a {@link com.mockrunner.servlet.ServletTestModule} based on the current
     * {@link WebMockObjectFactory}.
     * Same as <code>createServletTestModule(getWebMockObjectFactory())</code>.
     * @return the created {@link com.mockrunner.servlet.ServletTestModule}
     */
    protected ServletTestModule createServletTestModule()
    {
        return new ServletTestModule(getWebMockObjectFactory());
    }
    
    /**
     * Creates a {@link com.mockrunner.jdbc.JDBCTestModule} with the specified
     * {@link JDBCMockObjectFactory}.
     * @return the created {@link com.mockrunner.jdbc.JDBCTestModule}
     */
    protected JDBCTestModule createJDBCTestModule(JDBCMockObjectFactory mockFactory)
    {
        return new JDBCTestModule(mockFactory);
    }

    /**
     * Creates a {@link com.mockrunner.jdbc.JDBCTestModule} based on the current
     * {@link JDBCMockObjectFactory}.
     * Same as <code>createJDBCTestModule(getJDBCMockObjectFactory())</code>.
     * @return the created {@link com.mockrunner.jdbc.JDBCTestModule}
     */
    protected JDBCTestModule createJDBCTestModule()
    {
        return new JDBCTestModule(getJDBCMockObjectFactory());
    }
    
    /**
     * Creates an {@link com.mockrunner.ejb.EJBTestModule} with the specified
     * {@link EJBMockObjectFactory}.
     * @return the created {@link com.mockrunner.ejb.EJBTestModule}
     */
    protected EJBTestModule createEJBTestModule(EJBMockObjectFactory mockFactory)
    {
        return new EJBTestModule(mockFactory);
    }

    /**
     * Creates an {@link com.mockrunner.ejb.EJBTestModule} based on the current
     * {@link EJBMockObjectFactory}.
     * Same as <code>createEJBTestModule(getEJBMockObjectFactory())</code>.
     * @return the created {@link com.mockrunner.ejb.EJBTestModule}
     */
    protected EJBTestModule createEJBTestModule()
    {
        return new EJBTestModule(getEJBMockObjectFactory());
    }
    
	/**
	 * Creates an {@link com.mockrunner.connector.ConnectorTestModule} with the specified
	 * {@link ConnectorMockObjectFactory}.
	 * @return the created {@link com.mockrunner.connector.ConnectorTestModule}
	 */
	protected ConnectorTestModule createConnectorTestModule(ConnectorMockObjectFactory mockFactory)
	{
	    return new ConnectorTestModule(mockFactory);
	}

	/**
	 * Creates an {@link com.mockrunner.connector.ConnectorTestModule} based on the current
	 * {@link ConnectorMockObjectFactory}.
	 * Same as <code>createConnectorTestModule(getConnectorMockConnectionFactory())</code>.
	 * @return the created {@link com.mockrunner.connector.ConnectorTestModule}
	 */
	protected ConnectorTestModule createConnectorTestModule()
	{
	    return new ConnectorTestModule(getConnectorMockObjectFactory());
	}
    
    /**
     * Creates a {@link com.mockrunner.jms.JMSTestModule} with the specified
     * {@link JMSMockObjectFactory}.
     * @return the created {@link com.mockrunner.jms.JMSTestModule}
     */
    protected JMSTestModule createJMSTestModule(JMSMockObjectFactory mockFactory)
    {
        return new JMSTestModule(mockFactory);
    }

    /**
     * Creates a {@link com.mockrunner.jms.JMSTestModule} based on the current
     * {@link JMSMockObjectFactory}.
     * Same as <code>createJMSTestModule(getJMSMockObjectFactory())</code>.
     * @return the created {@link com.mockrunner.jms.JMSTestModule}
     */
    protected JMSTestModule createJMSTestModule()
    {
        return new JMSTestModule(getJMSMockObjectFactory());
    }
}

