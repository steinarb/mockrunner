package com.mockrunner.mock.ejb;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mockejb.MockContainer;
import org.mockejb.jndi.MockContextFactory;

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
    private final static Log log = LogFactory.getLog(EJBMockObjectFactory.class);
    private UserTransaction transaction;
    private MockContainer container;
    
    /**
     * Creates a new set of mock objects.
     */
    public EJBMockObjectFactory()
    { 
        initializeMockEJB();
    }

    private void initializeMockEJB()
    {
        try
        {
            MockContextFactory.setAsInitial();
            InitialContext context = new InitialContext();
            container = new MockContainer(context);
            try
            {
                transaction = (UserTransaction)context.lookup("javax.transaction.UserTransaction");
            }
            catch(NameNotFoundException nameExc)
            {
                transaction = new MockUserTransaction();
                context.rebind("javax.transaction.UserTransaction", transaction);
            }
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
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
    
    /**
     * Returns the MockEJB <code>MockContainer</code>.
     * @return the <code>MockContainer</code>
     */
    public MockContainer getMockContainer()
    {
        return container;
    }
}
