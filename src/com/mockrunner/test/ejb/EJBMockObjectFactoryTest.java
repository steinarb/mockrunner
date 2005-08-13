package com.mockrunner.test.ejb;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import junit.framework.TestCase;

import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.ejb.Configuration;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.mock.ejb.MockUserTransaction;

public class EJBMockObjectFactoryTest extends TestCase
{
    private Properties savedProperties;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        saveProperties();
        MockContextFactory.setAsInitial();
        unbind();
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        unbind();
        MockContextFactory.revertSetAsInitial();
        restoreProperties();
    }
    
    private void saveProperties()
    {
        savedProperties = new Properties();
        String factory = System.getProperty(Context.INITIAL_CONTEXT_FACTORY);
        if(null != factory && !factory.equals(MockContextFactory.class.getName()))
        {
            savedProperties.setProperty(Context.INITIAL_CONTEXT_FACTORY, factory);
        }
        String urlPrefix = System.getProperty(Context.URL_PKG_PREFIXES);
        if(null != urlPrefix && !urlPrefix.equals("org.mockejb.jndi"))
        {
            savedProperties.setProperty(Context.URL_PKG_PREFIXES, factory);
        }
    }
    
    private void restoreProperties()
    {
        if(null != savedProperties.getProperty(Context.INITIAL_CONTEXT_FACTORY))
        {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, savedProperties.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        }
        else
        {
            System.getProperties().remove(Context.INITIAL_CONTEXT_FACTORY);
        }
        if(null != savedProperties.getProperty(Context.URL_PKG_PREFIXES))
        {
            System.setProperty(Context.URL_PKG_PREFIXES, savedProperties.getProperty(Context.URL_PKG_PREFIXES));
        }
        else
        {
            System.getProperties().remove(Context.URL_PKG_PREFIXES);
        }
    }
    
    public void testInitMockContextFactory() throws Exception
    {
        EJBMockObjectFactory factory = new EJBMockObjectFactory();
        System.getProperties().remove(Context.INITIAL_CONTEXT_FACTORY);
        factory.initMockContextFactory();
        assertEquals(MockContextFactory.class.getName(), System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        assertEquals("org.mockejb.jndi", System.getProperty(Context.URL_PKG_PREFIXES));
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "test");
        factory.initMockContextFactory();
        assertEquals(MockContextFactory.class.getName(), System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        assertEquals("org.mockejb.jndi", System.getProperty(Context.URL_PKG_PREFIXES));
        System.setProperty(Context.URL_PKG_PREFIXES, "test");
        factory.initMockContextFactory();
        assertEquals(MockContextFactory.class.getName(), System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        assertEquals("test", System.getProperty(Context.URL_PKG_PREFIXES));
    }
    
    public void testResetMockContextFactory() throws Exception
    {
        EJBMockObjectFactory factory = new EJBMockObjectFactory();
        System.getProperties().remove(Context.INITIAL_CONTEXT_FACTORY);
        factory.resetMockContextFactory();
        assertNull(System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "test");
        factory.resetMockContextFactory();
        assertEquals("test", System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockContextFactory.class.getName());
        factory.resetMockContextFactory();
        assertNull(System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
    }

    public void testIntialize() throws Exception
    {
        InitialContext context = new InitialContext();
        EJBMockObjectFactory factory = new EJBMockObjectFactory();
        MockUserTransaction transaction = factory.getMockUserTransaction();
        assertNotNull(transaction);
        assertSame(context.lookup("javax.transaction.UserTransaction"), transaction);
        assertSame(context.lookup("java:comp/UserTransaction"), transaction);
        assertNotNull(factory.getMockContainer());
        transaction.rollback();
        factory = new EJBMockObjectFactory();
        assertSame(context.lookup("javax.transaction.UserTransaction"), transaction);
        assertSame(context.lookup("java:comp/UserTransaction"), transaction);
        assertFalse(transaction.wasRollbackCalled());
    }
    

    public void testOverrideCreate()
    {
        EJBMockObjectFactory factory = new TestEJBMockObjectFactory();
        assertNotSame(factory.getMockUserTransaction().getClass(), MockUserTransaction.class);
    }
    
    
    public void testSetConfiguration() throws Exception
    {
        InitialContext context = new InitialContext();
        EJBMockObjectFactory factory = new EJBMockObjectFactory();
        MockUserTransaction transaction = factory.getMockUserTransaction();
        assertSame(context.lookup("javax.transaction.UserTransaction"), transaction);
        assertSame(context.lookup("java:comp/UserTransaction"), transaction);
        unbind();
        Configuration configuration = new Configuration("myJNDIName");
        factory = new EJBMockObjectFactory(configuration);
        transaction = factory.getMockUserTransaction();
        assertSame(context.lookup("myJNDIName"), transaction);
        assertSame(context.lookup("javax.transaction.UserTransaction"), transaction);
        assertSame(context.lookup("java:comp/UserTransaction"), transaction);
        unbind();
        configuration.setBindMockUserTransactionToJNDI(false);
        factory = new EJBMockObjectFactory(configuration);
        transaction = factory.getMockUserTransaction();
        assertNotNull(transaction);
        try
        {
            context.lookup("myJNDIName");
            fail();
        } 
        catch(NameNotFoundException exc)
        {
            //should throw exception
        }
        try
        {
            context.lookup("javax.transaction.UserTransaction");
            fail();
        } 
        catch(NameNotFoundException exc)
        {
            //should throw exception
        }
        try
        {
            context.lookup("java:comp/UserTransaction");
            fail();
        } 
        catch(NameNotFoundException exc)
        {
            //should throw exception
        }
    }
    
    private void unbind() throws Exception
    {
        InitialContext context = new InitialContext();
        try
        {
            context.unbind("myJNDIName");
        } 
        catch(NamingException exc)
        {
            //ignore
        }
        try
        {
            context.unbind("javax.transaction.UserTransaction");
        } 
        catch(NamingException exc)
        {
            //ignore
        }
        try
        {
            context.unbind("java:comp/UserTransaction");
        } 
        catch(NamingException exc)
        {
            //ignore
        }
    }
    
    public static class TestEJBMockObjectFactory extends EJBMockObjectFactory
    {
        public MockUserTransaction createMockUserTransaction()
        {
            return new MockUserTransaction() {};
        }  
    }
}
