package com.mockrunner.base;

import junit.framework.TestCase;

import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jms.JMSMockObjectFactory;
import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.servlet.ServletTestModule;
import com.mockrunner.struts.ActionTestModule;
import com.mockrunner.tag.TagTestModule;

/**
 * Base class for all adapters.
 */
public abstract class BaseTestCase extends TestCase
{
    private WebMockObjectFactory webMockFactory;
    private JDBCMockObjectFactory jdbcMockFactory;
    private EJBMockObjectFactory ejbMockFactory;
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
        jdbcMockFactory.restoreDrivers();
        MockContextFactory.revertSetAsInitial();
        webMockFactory = null;
        jdbcMockFactory = null;
        ejbMockFactory = null;
        jmsMockFactory  = null;
    }

    /**
     * Creates the mock object factories. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        webMockFactory = createWebMockObjectFactory();
        jdbcMockFactory = createJDBCMockObjectFactory();
        ejbMockFactory = createEJBMockObjectFactory();
        jmsMockFactory = createJMSMockObjectFactory();
    } 

    /**
     * Creates a {@link WebMockObjectFactory}. 
     * @return the created {@link WebMockObjectFactory}
     */
    protected WebMockObjectFactory createWebMockObjectFactory()
    {
        WebMockObjectFactory factory = new WebMockObjectFactory();
        return factory;
    }

    /**
     * Same as <code>createWebMockObjectFactory(otherFactory, true)</code>
     */
    protected WebMockObjectFactory createWebMockObjectFactory(WebMockObjectFactory otherFactory)
    {
        WebMockObjectFactory factory = new WebMockObjectFactory(otherFactory);
        return factory;
    }
    
    /**
     * Creates a {@link WebMockObjectFactory} based on another on.
     * The created {@link WebMockObjectFactory} will have its own
     * request and response objects. If you set <i>createNewSession</i>
     * to <code>true</code> it will also have its own session object. 
     * The two factories will share one <code>ServletContext</code>. 
     * Especially important for multithreading tests.
     * If you set <i>createNewSession</i> to false, the two factories
     * will share one session. This setting simulates multiple requests
     * from the same client.
     * @param otherFactory the othe factory
     * @param createNewSession create a new session for the new factory
     * @return the created {@link WebMockObjectFactory}
     */
    protected WebMockObjectFactory createWebMockObjectFactory(WebMockObjectFactory otherFactory, boolean createNewSession)
    {
        WebMockObjectFactory factory = new WebMockObjectFactory(otherFactory, createNewSession);
        return factory;
    }

    /**
     * Gets the current {@link WebMockObjectFactory}.
     * @return the {@link WebMockObjectFactory}
     */
    protected WebMockObjectFactory getWebMockObjectFactory()
    {
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
     * Creates a {@link JDBCMockObjectFactory}. 
     * @return the created {@link JDBCMockObjectFactory}
     */
    protected JDBCMockObjectFactory createJDBCMockObjectFactory()
    {
        JDBCMockObjectFactory factory = new JDBCMockObjectFactory();
        return factory;
    }
    
    /**
     * Gets the current {@link JDBCMockObjectFactory}.
     * @return the {@link JDBCMockObjectFactory}
     */
    protected JDBCMockObjectFactory getJDBCMockObjectFactory()
    {
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
        EJBMockObjectFactory factory = new EJBMockObjectFactory();
        return factory;
    }

    /**
     * Gets the current {@link EJBMockObjectFactory}.
     * @return the {@link EJBMockObjectFactory}
     */
    protected EJBMockObjectFactory getEJBMockObjectFactory()
    {
        return ejbMockFactory;
    }

    /**
     * Sets the current {@link JDBCMockObjectFactory}.
     * @param mockFactory the {@link JDBCMockObjectFactory}
     */
    protected void setEJBMockObjectFactory(EJBMockObjectFactory mockFactory)
    {
        this.ejbMockFactory = mockFactory;
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
     * Creates an <code>ActionTestModule</code> with the specified
     * {@link WebMockObjectFactory}.
     * @param mockFactory the {@link WebMockObjectFactory}
     * @return the created <code>ActionTestModule</code>
     */
    protected ActionTestModule createActionTestModule(WebMockObjectFactory mockFactory)
    {
        return new ActionTestModule(mockFactory);
    }
    
    /**
     * Creates an <code>ActionTestModule</code> based on the current
     * {@link WebMockObjectFactory}.
     * Same as <code>createActionTestModule(getWebMockObjectFactory())</code>.
     * @return the created <code>ActionTestModule</code>
     */
    protected ActionTestModule createActionTestModule()
    {
        return new ActionTestModule(getWebMockObjectFactory());
    }
    
    /**
     * Creates a <code>TagTestModule</code> with the specified
     * {@link WebMockObjectFactory}.
     * @return the created <code>TagTestModule</code>
     */
    protected TagTestModule createTagTestModule(WebMockObjectFactory mockFactory)
    {
        return new TagTestModule(mockFactory);
    }
    
    /**
     * Creates a <code>TagTestModule</code> based on the current
     * {@link WebMockObjectFactory}.
     * Same as <code>createTagTestModule(getWebMockObjectFactory())</code>.
     * @return the created <code>TagTestModule</code>
     */
    protected TagTestModule createTagTestModule()
    {
        return new TagTestModule(getWebMockObjectFactory());
    }
    
    /**
     * Creates a <code>ServletTestModule</code> with the specified
     * {@link WebMockObjectFactory}.
     * @return the created <code>ServletTestModule</code>
     */
    protected ServletTestModule createServletTestModule(WebMockObjectFactory mockFactory)
    {
        return new ServletTestModule(mockFactory);
    }

    /**
     * Creates a <code>ServletTestModule</code> based on the current
     * {@link WebMockObjectFactory}.
     * Same as <code>createServletTestModule(getWebMockObjectFactory())</code>.
     * @return the created <code>ServletTestModule</code>
     */
    protected ServletTestModule createServletTestModule()
    {
        return new ServletTestModule(getWebMockObjectFactory());
    }
    
    /**
     * Creates a <code>JDBCTestModule</code> with the specified
     * {@link JDBCMockObjectFactory}.
     * @return the created <code>JDBCTestModule</code>
     */
    protected JDBCTestModule createJDBCTestModule(JDBCMockObjectFactory mockFactory)
    {
        return new JDBCTestModule(mockFactory);
    }

    /**
     * Creates a <code>JDBCTestModule</code> based on the current
     * {@link JDBCMockObjectFactory}.
     * Same as <code>createJDBCTestModule(getJDBCMockObjectFactory())</code>.
     * @return the created <code>JDBCTestModule</code>
     */
    protected JDBCTestModule createJDBCTestModule()
    {
        return new JDBCTestModule(getJDBCMockObjectFactory());
    }
    
    /**
     * Creates an <code>EJBTestModule</code> with the specified
     * {@link EJBMockObjectFactory}.
     * @return the created <code>EJBTestModule</code>
     */
    protected EJBTestModule createEJBTestModule(EJBMockObjectFactory mockFactory)
    {
        return new EJBTestModule(mockFactory);
    }

    /**
     * Creates an <code>EJBTestModule</code> based on the current
     * {@link EJBMockObjectFactory}.
     * Same as <code>createEJBTestModule(getEJBMockObjectFactory())</code>.
     * @return the created <code>EJBTestModule</code>
     */
    protected EJBTestModule createEJBTestModule()
    {
        return new EJBTestModule(getEJBMockObjectFactory());
    }
    
    /**
     * Creates a <code>JMSTestModule</code> with the specified
     * {@link JMSMockObjectFactory}.
     * @return the created <code>JMSTestModule</code>
     */
    protected JMSTestModule createJMSTestModule(JMSMockObjectFactory mockFactory)
    {
        return new JMSTestModule(mockFactory);
    }

    /**
     * Creates a <code>JMSTestModule</code> based on the current
     * {@link JMSMockObjectFactory}.
     * Same as <code>createJMSTestModule(getJMSMockObjectFactory())</code>.
     * @return the created <code>JMSTestModule</code>
     */
    protected JMSTestModule createJMSTestModule()
    {
        return new JMSTestModule(getJMSMockObjectFactory());
    }
}
