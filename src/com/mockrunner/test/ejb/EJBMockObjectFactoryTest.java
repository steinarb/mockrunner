package com.mockrunner.test.ejb;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.ejb.Configuration;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.mock.ejb.MockUserTransaction;

import junit.framework.TestCase;

public class EJBMockObjectFactoryTest extends TestCase
{
    protected void setUp() throws Exception
    {
        super.setUp();
        MockContextFactory.setAsInitial();
        unbind();
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        unbind();
        MockContextFactory.revertSetAsInitial();
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
}
