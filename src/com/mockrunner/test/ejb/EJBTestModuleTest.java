package com.mockrunner.test.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import junit.framework.TestCase;

import org.mockejb.MockEjbObject;
import org.mockejb.TransactionPolicy;

import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;

public class EJBTestModuleTest extends TestCase
{
    private EJBMockObjectFactory mockfactory;
    private EJBTestModule module;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        mockfactory = new EJBMockObjectFactory();
        module = new EJBTestModule(mockfactory);
    }
    
    public void testLookupBean() throws Exception
    {
        module.deploy("com/MyLookupTest", TestBean.class);
        try
        {
            module.lookupBean("com/MyLookupTestTest");
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        Object bean = module.lookupBean("com/MyLookupTest");
        assertTrue(bean instanceof Test);
        bean = module.lookupBean("com/MyLookupTest", new Object[] {new Integer(1)});
        assertTrue(bean instanceof Test);
        bean = module.lookupBean("com/MyLookupTest", new Object[] {new Integer(1), new Boolean(true)});
        assertTrue(bean instanceof Test);
        bean = module.lookupBean("com/MyLookupTest", "createWithPostfix", new Object[] {new Integer(1), new Boolean(true)});
        assertTrue(bean instanceof Test);
        try
        {
            module.lookupBean("com/MyLookupTestTest", new Object[] {new Boolean(true), new Integer(1)});
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
        
    public void testDeploy() throws Exception
    {
        try
        {
            module.deploy("test", EJBTestModuleTest.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.setHomeInterfacePackage("com.mockrunner.test");
            module.deploy("test", TestBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.setHomeInterfacePackage("com.mockrunner.test.ejb");
            module.setBusinessInterfacePackage("com.mockrunner.test");
            module.deploy("test", TestBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.setInterfacePackage("com.mockrunner.test.ejb");
            module.setHomeInterfaceSuffix("Factory");
            module.deploy("test", TestBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.setHomeInterfaceSuffix("Home");
            module.setBusinessInterfaceSuffix("Business");
            module.deploy("test", TestBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.setBusinessInterfaceSuffix("");
            module.setImplementationSuffix("Impl");
            module.deploy("test", TestBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        module.setImplementationSuffix("Bean");
        module.deploy("test", TestBean.class);
        InitialContext context = new InitialContext();
        Object home = context.lookup("test");
        assertNotNull(home);
        assertTrue(home instanceof TestHome);
    }
    
    public void testDeployMessageBean()
    {
        MockEjbObject ejbObject = module.deployMessageBean(TestMessageBean.class);
        MessageListener listener = module.createMessageBean(ejbObject);
        assertNotNull(listener);
    }
    
    public void testTransactions() throws Exception
    {
        module.deploy("mybean", TestBean.class, TransactionPolicy.REQUIRED);
        InitialContext context = new InitialContext();
        Object home = context.lookup("mybean");
        TestHome testHome = (TestHome)PortableRemoteObject.narrow(home, TestHome.class );
        Test testBean = (Test)testHome.create();
        testBean.test(false);
        module.verifyCommitted();
        module.verifyNotMarkedForRollback();
        module.verifyNotRolledBack();
        module.resetUserTransaction();
        testBean.test(true);
        module.verifyNotCommitted();
        module.verifyMarkedForRollback();
        module.verifyRolledBack();
    }
    
    public static class TestMessageBean implements MessageDrivenBean, MessageListener
    {
        public void onMessage(Message message)
        {

        }  
   
        public void setMessageDrivenContext(MessageDrivenContext context) throws EJBException
        {

        }
    
        public void ejbCreate()
        {

        }
   
        public void ejbRemove()
        {

        }
    }
    
    public static class TestBean implements SessionBean
    {
        private SessionContext sessionContext;
        
        public void test(boolean rollback)
        {
            if(rollback) sessionContext.setRollbackOnly();
        }
        
        public void ejbCreate() throws CreateException
        {
    
        }
        
        public void ejbCreate(int testInt) throws CreateException
        {

        }
        
        public void ejbCreate(int testInt, Boolean testBoolean) throws CreateException
        {

        }
        
        public void ejbCreateWithPostfix(int testInt, Boolean testBoolean) throws CreateException
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
            sessionContext = context;
        }
    }
    
    public static interface Test extends javax.ejb.EJBObject
    {
        public void test(boolean rollback) throws RemoteException;
    }
    
    public static interface TestHome extends javax.ejb.EJBHome
    {
        public Test create() throws CreateException, RemoteException;
        
        public Test create(int testInt) throws CreateException, RemoteException;
        
        public Test create(int testInt, Boolean testBoolean) throws CreateException, RemoteException;
    
        public Test createWithPostfix(int testInt, Boolean testBoolean) throws CreateException, RemoteException;
    }
}
