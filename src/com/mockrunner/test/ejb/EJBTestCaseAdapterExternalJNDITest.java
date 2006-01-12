package com.mockrunner.test.ejb;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.mockrunner.ejb.Configuration;
import com.mockrunner.ejb.EJBTestCaseAdapter;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.test.ejb.TestJNDI.TestContextFactory;

public class EJBTestCaseAdapterExternalJNDITest extends EJBTestCaseAdapter
{
    protected void setUp() throws Exception
    {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, TestContextFactory.class.getName());
        InitialContext context = new InitialContext(env);
        Configuration configuration = new Configuration();
        configuration.setContext(context);
        setEJBMockObjectFactory(new EJBMockObjectFactory(configuration));
        super.setUp();
    }
    
    public void testLookupAndBindToContext()
    {
        assertEquals("TestObject", lookup("testName"));
        bindToContext("testName", "testValue");
        assertEquals("TestObject", lookup("testName"));
    }
}
