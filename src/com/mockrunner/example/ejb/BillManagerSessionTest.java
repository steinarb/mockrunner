package com.mockrunner.example.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mockejb.TransactionPolicy;
import org.mockejb.interceptor.AspectSystem;
import org.mockejb.interceptor.AspectSystemFactory;
import org.mockejb.interceptor.Interceptor;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.MethodPatternPointcut;

import com.mockrunner.ejb.EJBTestCaseAdapter;
import com.mockrunner.example.ejb.interfaces.BillEntityHome;
import com.mockrunner.example.ejb.interfaces.BillManagerSession;
import com.mockrunner.jdbc.StatementResultSetHandler;

public class BillManagerSessionTest extends EJBTestCaseAdapter
{
    private BillManagerSession bean;
    private StatementResultSetHandler statementHandler;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        setInterfacePackage("com.mockrunner.example.ejb.interfaces");
        deploySessionBean("com/mockrunner/example/BillManagerSession", BillManagerSessionBean.class, TransactionPolicy.REQUIRED);
        bean = (BillManagerSession)lookupBean("com/mockrunner/example/BillManagerSession");
        deployEntityBean("java:comp/env/ejb/BillEntity", BillEntityBean.class, TransactionPolicy.REQUIRED);
        
    }
    
    public void testMarkAsPaid() throws Exception
    {
        AspectSystem aspectSystem =  AspectSystemFactory.getAspectSystem();
        FindUnpaidInterceptor interceptor = new FindUnpaidInterceptor();
        aspectSystem.add(new MethodPatternPointcut("BillEntity\\.findUnpaid"), interceptor);
        
    }
    
    private class FindUnpaidInterceptor implements Interceptor
    { 
        public void intercept(InvocationContext invocationContext) throws Exception
        {
            invocationContext.setReturnObject(create());
        }
        
        private Collection create() throws Exception
        {
            BillEntityHome home = (BillEntityHome)lookup("java:comp/env/ejb/BillEntity");
            List list = new ArrayList();
            list.add(home.create(new Integer(1)));
            list.add(home.create(new Integer(2)));
            return list;
        }
    }
}
