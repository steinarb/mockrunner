package com.mockrunner.example.ejb;

import java.util.ArrayList;
import java.util.List;

import org.mockejb.TransactionPolicy;
import org.mockejb.interceptor.AspectSystem;
import org.mockejb.interceptor.AspectSystemFactory;
import org.mockejb.interceptor.Interceptor;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.MethodPatternPointcut;

import com.mockrunner.ejb.EJBTestCaseAdapter;
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
public class BillManagerSessionTest extends EJBTestCaseAdapter
{
    private BillManagerSession bean;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        setInterfacePackage("com.mockrunner.example.ejb.interfaces");
        deploySessionBean("com/mockrunner/example/BillManagerSession", BillManagerSessionBean.class, TransactionPolicy.REQUIRED);
        bean = (BillManagerSession)createBean("com/mockrunner/example/BillManagerSession");
        deployEntityBean("java:comp/env/ejb/BillEntity", BillEntityBean.class, TransactionPolicy.REQUIRED);  
    }
    
    public void testMarkAsPaid() throws Exception
    {
        AspectSystem aspectSystem =  AspectSystemFactory.getAspectSystem();
        FindUnpaidInterceptor interceptor = new FindUnpaidInterceptor();
        aspectSystem.add(new MethodPatternPointcut("BillEntityHome\\.findUnpaid"), interceptor);
        bean.markAsPaid();
        BillEntity entity1 = (BillEntity)findByPrimaryKey("java:comp/env/ejb/BillEntity", new Integer(1));
        BillEntity entity2 = (BillEntity)findByPrimaryKey("java:comp/env/ejb/BillEntity", new Integer(2));
        assertTrue(entity1.getPaid());
        assertTrue(entity2.getPaid());
    }
    
    private class FindUnpaidInterceptor implements Interceptor
    { 
        public void intercept(InvocationContext invocationContext) throws Exception
        {
            List unpaidObjects = new ArrayList();
            BillEntity entity1 = (BillEntity)createEntityBean("java:comp/env/ejb/BillEntity", new Object[] {new Integer(1)}, new Integer(1));
            BillEntity entity2 = (BillEntity)createEntityBean("java:comp/env/ejb/BillEntity", new Object[] {new Integer(2)}, new Integer(2));
            System.out.println(entity1);
            entity1.setPaid(false);
            entity2.setPaid(false);
            unpaidObjects.add(entity1);
            unpaidObjects.add(entity2);
            invocationContext.setReturnObject(unpaidObjects);
        }
    }
}
