package com.mockrunner.test.ejb;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.mock.ejb.MockUserTransaction;

import junit.framework.TestCase;

public class EJBMockObjectFactoryTest extends TestCase
{
    protected void tearDown() throws Exception
    {
        super.tearDown();
        unbind();
    }
    
    public void testIntialize() throws Exception
    {
        MockContextFactory.setAsInitial();
        InitialContext context = new InitialContext();
        unbind();
        EJBMockObjectFactory factory = new EJBMockObjectFactory();
        MockUserTransaction transaction = factory.getMockUserTransaction();
        assertNotNull(transaction);
        assertSame(context.lookup("javax.transaction.UserTransaction"), transaction);
        assertSame(context.lookup("java:comp/UserTransaction"), transaction);
        assertNotNull(factory.getMockContainer());
        transaction.rollback();
        factory = new EJBMockObjectFactory();
        assertEquals(context.lookup("javax.transaction.UserTransaction"), transaction);
        assertEquals(context.lookup("java:comp/UserTransaction"), transaction);
        assertFalse(transaction.wasRollbackCalled());
    }
    
    private void unbind() throws Exception
    {
        InitialContext context = new InitialContext();
        try
        {
            context.unbind("javax.transaction.UserTransaction");
        } 
        catch (NamingException exc)
        {
            //ignore
        }
        try
        {
            context.unbind("java:comp/UserTransaction");
        } 
        catch (NamingException exc)
        {
            //ignore
        }
    }
}
