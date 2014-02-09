package com.mockrunner.test.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.ejb.Configuration;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.mock.ejb.MockUserTransaction;
import com.mockrunner.test.ejb.TestJNDI.NullContext;

public class EJBMockObjectFactoryTest
{
    private Properties savedProperties;
    private Context context;
    
    @Before
    public void setUp() throws Exception
    {
        savedProperties = new Properties();
        TestJNDI.saveProperties(savedProperties);
        MockContextFactory.setAsInitial();
        context = new InitialContext();
    }
    
    @After
    public void tearDown() throws Exception
    {
        MockContextFactory.revertSetAsInitial();
        TestJNDI.restoreProperties(savedProperties);
    }

    @Test
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
    
    @Test
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

    @Test
    public void testInitializeUserTransaction() throws Exception
    {
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
    
    @Test
    public void testOverrideCreate()
    {
        EJBMockObjectFactory factory = new TestEJBMockObjectFactory();
        assertNotSame(factory.getMockUserTransaction().getClass(), MockUserTransaction.class);
    }
    
    @Test
    public void testSetConfiguration() throws Exception
    {
        EJBMockObjectFactory factory = new EJBMockObjectFactory();
        MockUserTransaction transaction = factory.getMockUserTransaction();
        assertSame(context.lookup("javax.transaction.UserTransaction"), transaction);
        assertSame(context.lookup("java:comp/UserTransaction"), transaction);
        TestJNDI.unbind(context);
        Configuration configuration = new Configuration("myJNDIName");
        factory = new EJBMockObjectFactory(configuration);
        transaction = factory.getMockUserTransaction();
        assertSame(context.lookup("myJNDIName"), transaction);
        assertSame(context.lookup("javax.transaction.UserTransaction"), transaction);
        assertSame(context.lookup("java:comp/UserTransaction"), transaction);
        TestJNDI.unbind(context);
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
        NullContext testContext = new NullContext();
        factory = new EJBMockObjectFactory(configuration);
        assertSame(factory.getContext(), configuration.getContext());
        configuration.setContext(testContext);
        factory = new EJBMockObjectFactory(configuration);
        assertSame(configuration.getContext(), factory.getContext());
        assertSame(testContext, factory.getContext());
    }
    
    public static class TestEJBMockObjectFactory extends EJBMockObjectFactory
    {
        public MockUserTransaction createMockUserTransaction()
        {
            return new MockUserTransaction() {};
        }  
    }
}
