package com.mockrunner.test.ejb;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import com.mockrunner.ejb.Configuration;
import com.mockrunner.ejb.EJBTestCaseAdapter;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;

public class EJBTestCaseAdapterTest extends EJBTestCaseAdapter
{
    protected void setUp() throws Exception
    {
        Configuration configuration = new Configuration();
        configuration.setBindMockUserTransactionToJNDI(false);
        setEJBMockObjectFactory(new EJBMockObjectFactory(configuration));
        super.setUp();
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
