package com.mockrunner.ejb;

import javax.transaction.UserTransaction;

import org.mockejb.MockContext;

import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockUserTransaction;

/**
 * Module for JDBC tests.
 * Please note:<br>
 * <code>EJBTestModule</code> automatically creates an <code>UserTransaction</code>
 * in a static initializer. This is necessary because MockEJB stores
 * the <code>UserTransaction</code> in a static field and always
 * uses this instance. So the Mockrunner practice to create
 * the mock objects in the <code>setUp</code> method cannot
 * be used. If you want to use your own instance of <code>UserTransaction</code>
 * you can set it with {@link #setUserTransaction}. This has to be done before the 
 * first instance of <code>EJBTestModule</code> is created, because the constructor
 * adds the <code>UserTransaction</code> to the MockContext of MockEJB.
 */
public class EJBTestModule
{
    private static UserTransaction transaction;
    private JDBCMockObjectFactory mockFactory;
    
    static
    {
        if(null != transaction)
        {
            transaction = new MockUserTransaction();
        }  
    }
    
    /**
     * Set a <code>UserTransaction</code> that will be deployed to MockEJB
     * in the constructor.
     * Note that the <code>MockUserTransaction</code> returned by
     * {@link JDBCMockObjectFactory#getMockUserTransaction} is not
     * suitable, because Mockrunner recreates all the mock objects
     * in the <code>setUp</code> method.
     * @param userTransaction the <code>UserTransaction</code>
     */
    public static void setUserTransaction(UserTransaction userTransaction)
    {
        transaction = userTransaction;      
    }
    
    /**
     * Returns the <code>UserTransaction</code> that will be deployed to MockEJB
     * in the constructor.
     * @return the <code>UserTransaction</code>
     */
    public static UserTransaction getUserTransaction()
    {
        return transaction;
    }
    
    /**
     * The constructor adds the <code>UserTransaction</code> to the MockContext 
     * of MockEJB. If the <code>UserTransaction</code> is an instance of
     * {@link com.mockrunner.mock.jdbc.MockUserTransaction} it will be
     * reseted. The static initializer of this class automatically
     * creates an instance of {@link com.mockrunner.mock.jdbc.MockUserTransaction}
     * if you do not set your own <code>UserTransaction</code> object.
     */
    public EJBTestModule(JDBCMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
        if(transaction != null)
        {
            MockContext.add("javax.transaction.UserTransaction", transaction);
        }
        if(transaction != null && transaction instanceof MockUserTransaction)
        {
            ((MockUserTransaction)transaction).reset();
        }
    }
}
