package com.mockrunner.test.ejb;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.junit.Before;
import org.junit.Test;

import com.mockrunner.ejb.Configuration;
import com.mockrunner.ejb.EJBTestCaseAdapter;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.test.ejb.TestJNDI.TestContextFactory;

public class EJBTestCaseAdapterExternalJNDITest extends EJBTestCaseAdapter
{
	@Before
    public void setUp() throws Exception
    {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, TestContextFactory.class.getName());
        InitialContext context = new InitialContext(env);
        Configuration configuration = new Configuration();
        configuration.setContext(context);
        setEJBMockObjectFactory(new EJBMockObjectFactory(configuration));
        super.setUp();
    }
    
	@Test
    public void testLookupAndBindToContext()
    {
        assertEquals("TestObject", lookup("testName"));
        bindToContext("testName", "testValue");
        assertEquals("TestObject", lookup("testName"));
        deploySessionBean("testName", TestSessionBean.class);
        assertEquals("TestObject", lookup("testName"));
    }
    
    public static class TestSessionBean implements SessionBean
    {   
        public void test()
        {
        
        }
        
        public void ejbCreate() throws CreateException
        {
    
        }
        
        public void ejbCreate(Integer testInt) throws CreateException
        {

        }
        
        public void ejbCreate(int testInt, Boolean testBoolean) throws CreateException
        {

        }
        
        public void ejbCreateWithSuffix(int testInt, Boolean testBoolean) throws CreateException
        {

        }
        
        public void ejbActivate() throws EJBException, RemoteException
        {

        }

        public void ejbPassivate() throws EJBException, RemoteException
        {

        }

        public void ejbRemove() throws EJBException, RemoteException
        {

        }

        public void setSessionContext(SessionContext context) throws EJBException, RemoteException
        {
            
        }
    }
    
    public static interface TestSession extends javax.ejb.EJBObject
    {
        public void test(boolean setRollbackOnly) throws RemoteException;
    }
    
    public static interface TestSessionHome extends javax.ejb.EJBHome
    {
        public TestSession create() throws CreateException, RemoteException;
    }
}
