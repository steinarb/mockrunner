package com.mockrunner.ejb;

/**
 * Global configuration options regarding EJB and JNDI.
 * Usually you do not have to change these options.
 */
public class Configuration
{
    private String userTransactionJNDIName;
    private boolean bindMockUserTransactionToJNDI;
    
    public Configuration()
    {
        this("javax.transaction.UserTransaction");
    }
    
    public Configuration(String userTransactionJNDIName)
    {
        this(userTransactionJNDIName, true);
    }
    
    public Configuration(String userTransactionJNDIName, boolean bindMockUserTransactionToJNDI)
    {
        this.userTransactionJNDIName = userTransactionJNDIName;
        this.bindMockUserTransactionToJNDI = bindMockUserTransactionToJNDI;
    }
    
    /**
     * Get if the mock transaction should be bound to JNDI.
     * @return if the mock transaction should be bound to JNDI
     */
    public boolean getBindMockUserTransactionToJNDI()
    {
        return bindMockUserTransactionToJNDI;
    }
    
    /**
     * Set if the mock transaction should be bound to JNDI.
     * When the {@link com.mockrunner.mock.ejb.EJBMockObjectFactory}
     * creates a {@link com.mockrunner.mock.ejb.MockUserTransaction},
     * it tries to rebind the transaction to the JNDI tree with the
     * specified name {@link #setUserTransactionJNDIName}, the name
     * <code>javax.transaction.UserTransaction</code> (which is used
     * by MockEJB and Weblogic) and the name 
     * <code>java:comp/UserTransaction</code> (which is the standard name), 
     * if this option is <code>true</code>. 
     * If this option is <code>false</code>, a mock transaction is created 
     * but not bound to JNDI.
     * Default is <code>true</code>.
     * @param bindMockUserTransactionToJNDI should the mock transaction be bound to JNDI
     */
    public void setBindMockUserTransactionToJNDI(boolean bindMockUserTransactionToJNDI)
    {
        this.bindMockUserTransactionToJNDI = bindMockUserTransactionToJNDI;
    }
    
    /**
     * Get the JNDI name for the user transaction.
     * @return the JNDI name for the user transaction
     */
    public String getUserTransactionJNDIName()
    {
        return userTransactionJNDIName;
    }
    
    /**
     * Set the JNDI name for the user transaction. The
     * {@link com.mockrunner.mock.ejb.EJBMockObjectFactory} tries to
     * obtain a <code>UserTransaction</code> from JNDI using this
     * name. If the lookup fails, a {@link com.mockrunner.mock.ejb.MockUserTransaction}
     * is created and bound to JNDI, if {@link #setBindMockUserTransactionToJNDI} is
     * set to <code>true</code>.
     * Default is <code>javax.transaction.UserTransaction</code>.
     * @param userTransactionJNDIName the JNDI name for the user transaction
     */
    public void setUserTransactionJNDIName(String userTransactionJNDIName)
    {
        this.userTransactionJNDIName = userTransactionJNDIName;
    }
}
