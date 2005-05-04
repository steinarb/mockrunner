package com.mockrunner.test.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.ejb.BasicEJBTestCaseAdapter;
import com.mockrunner.ejb.Configuration;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;

public class BasicEJBTestCaseAdapterTest extends BasicEJBTestCaseAdapter
{
    protected void setUp() throws Exception
    {
        Configuration configuration = new Configuration();
        configuration.setBindMockUserTransactionToJNDI(false);
        setEJBMockObjectFactory(new EJBMockObjectFactory(configuration));
        super.setUp();
    }
    
    public void testTearDownRevertCalled() throws Exception
    {
        assertEquals(MockContextFactory.class.getName(), System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        super.tearDown();
        assertNull(System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
    }
    
    public void testUserTransactionNotBound() throws Exception
    {
        InitialContext context = new InitialContext();
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
}
