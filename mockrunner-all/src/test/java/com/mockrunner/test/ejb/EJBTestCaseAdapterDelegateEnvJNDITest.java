package com.mockrunner.test.ejb;

import static org.junit.Assert.assertEquals;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.ejb.EJBTestCaseAdapter;
import com.mockrunner.test.ejb.TestJNDI.TestContextFactory;

public class EJBTestCaseAdapterDelegateEnvJNDITest extends EJBTestCaseAdapter
{
	@Before
    public void setUp() throws Exception
    {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, TestContextFactory.class.getName());
        MockContextFactory.setDelegateEnvironment(env);
        super.setUp();
    }
    
	@After
    public void tearDown() throws Exception
    {
        MockContextFactory.setDelegateContext(null);
        MockContextFactory.setDelegateEnvironment(null);
        super.tearDown();
    }

	@Test
    public void testLookupExternal() throws Exception
    {
        InitialContext context = new InitialContext();
        Object object = context.lookup("test");
        assertEquals("TestObject", object);
    }
}
