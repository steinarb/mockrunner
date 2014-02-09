package com.mockrunner.test.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.ejb.Configuration;
import com.mockrunner.ejb.EJBTestCaseAdapter;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;

public class EJBTestCaseAdapterTest extends EJBTestCaseAdapter
{
	@Before
    public void setUp() throws Exception
    {
        Configuration configuration = new Configuration();
        configuration.setBindMockUserTransactionToJNDI(false);
        setEJBMockObjectFactory(new EJBMockObjectFactory(configuration));
        super.setUp();
    }
    
	@Test
    public void testTearDownRevertCalled() throws Exception
    {
        assertEquals(MockContextFactory.class.getName(), System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        super.tearDown();
        assertNull(System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
    }
    
	@Test
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
