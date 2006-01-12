package com.mockrunner.test.ejb;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.ejb.EJBTestCaseAdapter;
import com.mockrunner.test.ejb.TestJNDI.TestContextFactory;

public class EJBTestCaseAdapterDelegateEnvJNDITest extends EJBTestCaseAdapter
{
    protected void setUp() throws Exception
    {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, TestContextFactory.class.getName());
        MockContextFactory.setDelegateEnvironment(env);
        super.setUp();
    }
    
    protected void tearDown() throws Exception
    {
        MockContextFactory.setDelegateContext(null);
        MockContextFactory.setDelegateEnvironment(null);
        super.tearDown();
    }

    public void testLookupExternal() throws Exception
    {
        InitialContext context = new InitialContext();
        Object object = context.lookup("test");
        assertEquals("TestObject", object);
    }
}
