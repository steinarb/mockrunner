package com.mockrunner.mock.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.transaction.UserTransaction;

import org.mockejb.MockContainer;
import org.mockejb.MockContext;

/**
 * Used to create all types of EJB mock objects. 
 * Maintains the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure, they are all
 * up to date.
 * This factory takes the <code>UserTransaction</code>
 * from the MockEJB mock context. If there's no transaction
 * deployed to the mock context, the factory will create and deploy a
 * {@link com.mockrunner.mock.ejb.MockUserTransaction}.
 * If the deployed transaction is no
 * {@link com.mockrunner.mock.ejb.MockUserTransaction}
 * the method {@link #getMockUserTransaction} returns <code>null</code>.
 * Use {@link #getUserTransaction} instead in this case.
 */
public class EJBMockObjectFactory
{
    private UserTransaction transaction;
    
    /**
     * Creates a new set of mock objects.
     */
    public EJBMockObjectFactory()
    {
        activateMockContainer();
        createTransactionFromContext();
    }
    
    private void activateMockContainer()
    {
        if(!MockContainer.isActive()) return;
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.mockejb.MockContextFactory");
    }

    private void createTransactionFromContext()
    {
        try
        {
            InitialContext context = new InitialContext();
            try
            {
                transaction = (UserTransaction)context.lookup("javax.transaction.UserTransaction");
            }
            catch(NameNotFoundException nameExc)
            {
                transaction = new MockUserTransaction();
                MockContext.add("javax.transaction.UserTransaction", transaction);
            }
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
            transaction = new MockUserTransaction();
        }
        if(transaction instanceof MockUserTransaction)
        {
            ((MockUserTransaction)transaction).reset();
        }
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.ejb.MockUserTransaction}.
     * @return the {@link com.mockrunner.mock.ejb.MockUserTransaction}
     */
    public MockUserTransaction getMockUserTransaction()
    {
        if(!(transaction instanceof MockUserTransaction)) return null;
        return (MockUserTransaction)transaction;
    }

    /**
     * Returns the <code>UserTransaction</code>.
     * @return the <code>UserTransaction</code>
     */
    public UserTransaction getUserTransaction()
    {
        return transaction;
    }
}
