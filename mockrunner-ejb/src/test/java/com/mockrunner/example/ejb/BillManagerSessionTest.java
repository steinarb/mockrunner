package com.mockrunner.example.ejb;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockejb.TransactionPolicy;
import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.AspectSystem;
import org.mockejb.interceptor.AspectSystemFactory;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.MethodPatternPointcut;
import org.mockejb.interceptor.Pointcut;

import com.mockrunner.ejb.BasicEJBTestCaseAdapter;
import com.mockrunner.example.ejb.interfaces.BillEntity;
import com.mockrunner.example.ejb.interfaces.BillManagerSession;

/**
 * Example test for {@link BillManagerSessionBean} and {@link BillEntityBean}. 
 * This example demonstrates how to test session and CMP entity beans.
 * It shows how to use {@link com.mockrunner.ejb.EJBTestModule#createEntityBean}
 * and {@link com.mockrunner.ejb.EJBTestModule#findByPrimaryKey}.
 * You don't have to intercept these methods, just the
 * <code>findUnpaid</code> method.
 */
public class BillManagerSessionTest extends BasicEJBTestCaseAdapter
{
    private BillManagerSession bean;
    
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        setInterfacePackage("com.mockrunner.example.ejb.interfaces");
        deploySessionBean("com/mockrunner/example/BillManagerSession", BillManagerSessionBean.class, TransactionPolicy.REQUIRED);
        bean = (BillManagerSession)createBean("com/mockrunner/example/BillManagerSession");
        deployEntityBean("java:comp/env/ejb/BillEntity", BillEntityBean.class, TransactionPolicy.REQUIRED);  
    }
    
    @Test
    public void testMarkAsPaid() throws Exception
    {
        AspectSystem aspectSystem =  AspectSystemFactory.getAspectSystem();
        aspectSystem.add(new FindUnpaidAspect());
        bean.markAsPaid();
        BillEntity entity1 = (BillEntity)findByPrimaryKey("java:comp/env/ejb/BillEntity", 1);
        BillEntity entity2 = (BillEntity)findByPrimaryKey("java:comp/env/ejb/BillEntity", 2);
        assertTrue(entity1.getPaid());
        assertTrue(entity2.getPaid());
    }
    
    private class FindUnpaidAspect implements Aspect
    { 
        public Pointcut getPointcut()
        {
            return new MethodPatternPointcut("BillEntityHome\\.findUnpaid");
        }
        
        public void intercept(InvocationContext invocationContext) throws Exception
        {
            List unpaidObjects = new ArrayList();
            BillEntity entity1 = (BillEntity)createEntityBean("java:comp/env/ejb/BillEntity", new Object[] {new Integer(1)}, 1);
            BillEntity entity2 = (BillEntity)createEntityBean("java:comp/env/ejb/BillEntity", new Object[] {new Integer(2)}, 2);
            entity1.setPaid(false);
            entity2.setPaid(false);
            unpaidObjects.add(entity1);
            unpaidObjects.add(entity2);
            invocationContext.setReturnObject(unpaidObjects);
        }
    }
}
