package com.mockrunner.test.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.mockejb.MockEjbObject;
import org.mockejb.TransactionPolicy;

import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;

import junit.framework.TestCase;

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
            module.setInterfacePackage("com.mockrunner.test");
            module.deploy("test", TestBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.setInterfacesPackages("com.mockrunner.test.ejb");
            module.setHomeSuffix("Factory");
            module.deploy("test", TestBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.setHomeSuffix("Home");
            module.setInterfaceSuffix("Business");
            module.deploy("test", TestBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            module.setInterfaceSuffix("");
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
    
    public void testTransactions() throws Exception
    {
        MockEjbObject ejbObject = module.deploy("mybean", TestBean.class);
        ejbObject.setTransactionPolicy(TransactionPolicy.REQUIRED);
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
    }
}
