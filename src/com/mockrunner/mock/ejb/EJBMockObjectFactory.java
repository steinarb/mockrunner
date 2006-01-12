package com.mockrunner.mock.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mockejb.MockContainer;
import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.ejb.Configuration;

/**
 * Used to create all types of EJB mock objects. 
 * Maintains the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure that they are all
 * up to date.
 * This factory takes the <code>UserTransaction</code> from the JNDI context. 
 * If there's no transaction bound to the  context, the factory will create a
 * {@link com.mockrunner.mock.ejb.MockUserTransaction} and bind it to the context.
 * If the bound transaction is no
 * {@link com.mockrunner.mock.ejb.MockUserTransaction},
 * the method {@link #getMockUserTransaction} returns <code>null</code>.
 * Use {@link #getUserTransaction} instead in this case.
 * You can configure the JNDI name of the <code>UserTransaction</code> and
 * the JNDI <code>Context</code> with the class 
 * {@link com.mockrunner.ejb.Configuration}.
 */
public class EJBMockObjectFactory
{
    private final static Log log = LogFactory.getLog(EJBMockObjectFactory.class);
    private Configuration configuration;
    private UserTransaction transaction;
    private MockContainer container;
    private Context context;
    
    /**
     * Creates a new set of mock objects.
     */
    public EJBMockObjectFactory()
    { 
        this(new Configuration());
    }
    
    /**
     * Creates a new set of mock objects based on the specified configuration.
     */
    public EJBMockObjectFactory(Configuration configuration)
    { 
        this.configuration = configuration;
        initializeContext();
        initializeEJBContainer();
        initializeUserTransaction();
    }

    private void initializeContext()
    {
        context = configuration.getContext();
        if(null == context)
        {
            try
            {
                initMockContextFactory();
                context = new InitialContext();
            } 
            catch(NamingException exc)
            {
                throw new NestedApplicationException(exc);
            }
            configuration.setContext(context);
        }
    }
    
    private void initializeUserTransaction()
    {
        try
        {
            try
            {
                transaction = (UserTransaction)context.lookup(configuration.getUserTransactionJNDIName());
            }
            catch(NameNotFoundException nameExc)
            {
                transaction = createMockUserTransaction();
                if(configuration.getBindMockUserTransactionToJNDI())
                {
                    context.rebind(configuration.getUserTransactionJNDIName(), transaction);
                    context.rebind("javax.transaction.UserTransaction", transaction);
                    context.rebind("java:comp/UserTransaction", transaction);
                }
            }
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            transaction = createMockUserTransaction();
        }
        if(transaction instanceof MockUserTransaction)
        {
            ((MockUserTransaction)transaction).reset();
        }
    }
    
    protected void initializeEJBContainer()
    {
        container = new MockContainer(context); 
    }

    /**
     * Creates the {@link com.mockrunner.mock.ejb.MockUserTransaction} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.ejb.MockUserTransaction}.
     * @return the {@link com.mockrunner.mock.ejb.MockUserTransaction}
     */
    public MockUserTransaction createMockUserTransaction()
    {
        return new MockUserTransaction();
    }
    
    /**
     * Calls <code>MockContextFactory.setAsInitial()</code>, if 
     * <code>MockContextFactory</code> is not already the current
     * context factory.
     */
    public void initMockContextFactory() throws NamingException
    {
        String factory = System.getProperty(Context.INITIAL_CONTEXT_FACTORY);
        if(null == factory || !factory.equals(MockContextFactory.class.getName()))
        {
            MockContextFactory.setAsInitial();
        }
    }
    
    /**
     * Calls <code>MockContextFactory.revertSetAsInitial()</code>, if 
     * <code>MockContextFactory</code> is the current context factory.
     */
    public void resetMockContextFactory()
    {
        String factory = System.getProperty(Context.INITIAL_CONTEXT_FACTORY);
        if(null != factory && factory.equals(MockContextFactory.class.getName()))
        {
            MockContextFactory.revertSetAsInitial();
        }
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.ejb.MockUserTransaction}.
     * If the bound transaction is no {@link com.mockrunner.mock.ejb.MockUserTransaction},
     * this method returns <code>null</code>.
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
    
    /**
     * Returns the JNDI context that is used by this factory. If you do not set
     * a <code>Context</code> using {@link com.mockrunner.ejb.Configuration#setContext}}, 
     * the JNDI implementation of MockEJB is used.
     * @return the JNDI context
     */
    public Context getContext()
    {
        return context;
    }
}
