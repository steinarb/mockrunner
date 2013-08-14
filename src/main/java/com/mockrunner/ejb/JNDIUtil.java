package com.mockrunner.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

import org.mockejb.jndi.MockContextFactory;

import com.mockrunner.base.NestedApplicationException;

/**
 * Util class for creating and managing the JNDI context
 */
public class JNDIUtil
{
    /**
     * Calls <code>MockContextFactory.setAsInitial()</code>, if the
     * <code>MockContextFactory</code> is not already the current
     * context factory.
     */
    public static void initMockContextFactory() throws NamingException
    {
        String factory = System.getProperty(Context.INITIAL_CONTEXT_FACTORY);
        if(null == factory || !factory.equals(MockContextFactory.class.getName()))
        {
            MockContextFactory.setAsInitial();
        }
    }
    
    /**
     * Calls <code>MockContextFactory.revertSetAsInitial()</code>, if the
     * <code>MockContextFactory</code> is the current context factory.
     */
    public static void resetMockContextFactory()
    {
        String factory = System.getProperty(Context.INITIAL_CONTEXT_FACTORY);
        if(null != factory && factory.equals(MockContextFactory.class.getName()))
        {
            MockContextFactory.revertSetAsInitial();
        }
    }
    
    /**
     * Tries to get the JNDI context from the specified configuration.
     * If the configuration returns <code>null</code> for the context,
     * this method initializes the MockEJB JNDI implementation.
     * @param configuration the configuration
     * @return the JNDI context
     */
    public static Context getContext(Configuration configuration)
    {
        Context context = configuration.getContext();
        if(null == context)
        {
            try
            {
                JNDIUtil.initMockContextFactory();
                context = new InitialContext();
            } 
            catch(NamingException exc)
            {
                throw new NestedApplicationException(exc);
            }
            configuration.setContext(context);
        }
        return context;
    }
    
    /**
     * Binds the specified <code>UserTransaction</code> to the specified
     * JNDI context.
     * @param configuration the configuration
     * @param context the JNDI context
     * @param transaction the <code>UserTransaction</code>
     */
    public static void bindUserTransaction(Configuration configuration, Context context, UserTransaction transaction) throws NamingException
    {
        if(configuration.getBindMockUserTransactionToJNDI())
        {
            context.rebind(configuration.getUserTransactionJNDIName(), transaction);
            context.rebind("javax.transaction.UserTransaction", transaction);
            context.rebind("java:comp/UserTransaction", transaction);
        }
    }
}
