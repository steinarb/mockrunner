package com.mockrunner.ejb;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.beanutils.MethodUtils;
import org.mockejb.MockEjbObject;
import org.mockejb.SessionBeanDescriptor;
import org.mockejb.TransactionPolicy;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.mock.ejb.MockUserTransaction;
import com.mockrunner.util.ClassUtil;

/**
 * Module for EJB tests.
 */
public class EJBTestModule
{
    private EJBMockObjectFactory mockFactory;
    private String impSuffix;
    private String homeInterfaceSuffix;
    private String businessInterfaceSuffix;
    private String homeInterfacePackage;
    private String businessInterfacePackage;
    
    public EJBTestModule(EJBMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
        impSuffix = "Bean";
        homeInterfaceSuffix = "Home";
        businessInterfaceSuffix = "";
    }
    
    /**
     * Sets the suffix of the bean implementation class. The
     * default is <i>"Bean"</i>, i.e. if the remote interface has
     * the name <code>Test</code> the implementation class is
     * <code>TestBean</code>.
     * @param impSuffix the bean implementation suffix
     */
    public void setImplementationSuffix(String impSuffix)
    {
        this.impSuffix = impSuffix;
    }
    
    /**
     * Sets the suffix of the remote (resp. local) interface. The
     * default is an empty string, i.e. if the implementation class is
     * <code>TestBean</code>, the remote interface is <code>Test</code>
     * @param businessInterfaceSuffix the bean remote interface suffix
     */
    public void setBusinessInterfaceSuffix(String businessInterfaceSuffix)
    {
        this.businessInterfaceSuffix = businessInterfaceSuffix;
    }
    
    /**
     * Sets the suffix of the home (resp. local home) interface. The
     * default is <i>"Home"</i>, i.e. if the implementation class is
     * <code>TestBean</code>, the home interface is <code>TestHome</code>
     * @param homeInterfaceSuffix the bean home interface suffix
     */
    public void setHomeInterfaceSuffix(String homeInterfaceSuffix)
    {
        this.homeInterfaceSuffix = homeInterfaceSuffix;
    }
    
    /**
     * Sets the package for the bean home and remote interfaces. Per
     * default, the framework expects that the interfaces are in the
     * same package as the bean implementation classes.
     * @param interfacePackage the package name for home and remote interfaces
     */
    public void setInterfacePackage(String interfacePackage)
    {
        setHomeInterfacePackage(interfacePackage);
        setBusinessInterfacePackage(interfacePackage);
    }
    
    /**
     * Sets the package for the bean home (resp. local home) interface. Per
     * default, the framework expects that the interfaces are in the
     * same package as the bean implementation classes.
     * @param homeInterfacePackage the package name for home interface
     */
    public void setHomeInterfacePackage(String homeInterfacePackage)
    {
        this.homeInterfacePackage = homeInterfacePackage;
    }
    
    /**
     * Sets the package for the bean remote (resp. local) interface. Per
     * default, the framework expects that the interfaces are in the
     * same package as the bean implementation classes.
     * @param businessInterfacePackage the package name for remote interface
     */
    public void setBusinessInterfacePackage(String businessInterfacePackage)
    {
        this.businessInterfacePackage = businessInterfacePackage;
    }
    
    /**
     * Deploys a bean to the mock container using the specified
     * descriptor. Sets the transaction policy <i>SUPPORTS</i>.
     * @param descriptor the descriptor
     * @return the <code>MockEjbObject</code> of the deployed bean
     */
    public MockEjbObject deploy(SessionBeanDescriptor descriptor)
    {
        return deploy(descriptor, TransactionPolicy.SUPPORTS);
    }
    
    /**
     * Deploys a bean to the mock container using the specified
     * descriptor. The specified transaction policy will be automatically set.
     * @param descriptor the descriptor
     * @return the <code>MockEjbObject</code> of the deployed bean
     */
    public MockEjbObject deploy(SessionBeanDescriptor descriptor, TransactionPolicy policy)
    {
        MockEjbObject bean;
        try
        {
            bean = mockFactory.getMockContainer().deploy(descriptor);
            bean.setTransactionPolicy(policy);
            return bean;
        }
        catch(NamingException exc)
        {
            exc.printStackTrace();
            return null;
        } 
    }
    
    /**
     * Deploys a stateless bean to the mock container. You have to specify
     * the implementation class and the JNDI name. The frameworks
     * determines the home and remote interfaces based on the
     * information specified with the <code>setSuffix</code>
     * and <code>setPackage</code> methods.
     * Sets the transaction policy <i>SUPPORTS</i>.
     * @param jndiName the JNDI name
     * @param beanClass the bean implementation class
     * @return the <code>MockEjbObject</code> of the deployed bean
     */
    public MockEjbObject deploy(String jndiName, Class beanClass)
    {
        return deploy(jndiName, beanClass, false, TransactionPolicy.SUPPORTS);
    }
    
    /**
     * Deploys a bean to the mock container. You have to specify
     * the implementation class and the JNDI name. The frameworks
     * determines the home and remote interfaces based on the
     * information specified with the <code>setSuffix</code>
     * and <code>setPackage</code> methods.
     * Sets the transaction policy <i>SUPPORTS</i>.
     * @param jndiName the JNDI name
     * @param beanClass the bean implementation class
     * @param stateful is the bean stateful
     * @return the <code>MockEjbObject</code> of the deployed bean
     */
    public MockEjbObject deploy(String jndiName, Class beanClass, boolean stateful)
    {
        return deploy(jndiName, beanClass, stateful, TransactionPolicy.SUPPORTS);
    }
    
    /**
     * Deploys a stateless bean to the mock container. You have to specify
     * the implementation class and the JNDI name. The frameworks
     * determines the home and remote interfaces based on the
     * information specified with the <code>setSuffix</code>
     * and <code>setPackage</code> methods.
     * The specified transaction policy will be automatically set.
     * @param jndiName the JNDI name
     * @param beanClass the bean implementation class
     * @param policy the transaction policy
     * @return the <code>MockEjbObject</code> of the deployed bean
     */
    public MockEjbObject deploy(String jndiName, Class beanClass, TransactionPolicy policy)
    {
        return deploy(jndiName, beanClass, false, policy);
    }
    
    /**
     * Deploys a bean to the mock container. You have to specify
     * the implementation class and the JNDI name. The frameworks
     * determines the home and remote interfaces based on the
     * information specified with the <code>setSuffix</code>
     * and <code>setPackage</code> methods.
     * The specified transaction policy will be automatically set.
     * @param jndiName the JNDI name
     * @param beanClass the bean implementation class
     * @param stateful is the bean stateful
     * @param policy the transaction policy
     * @return the <code>MockEjbObject</code> of the deployed bean
     */
    public MockEjbObject deploy(String jndiName, Class beanClass, boolean stateful, TransactionPolicy policy)
    {
        SessionBeanDescriptor descriptor = new SessionBeanDescriptor(jndiName, getHomeClass(beanClass), getRemoteClass(beanClass), beanClass);
        descriptor.setStateful(stateful);
        MockEjbObject bean = deploy(descriptor);
        bean.setTransactionPolicy(policy);
        return bean;
    }
    
    /**
     * Adds an object to the mock context by calling <code>rebind</code>
     * @param name the name of the object
     * @param object the object to add
     */
    public void bindToContext(String name, Object object)
    {
        try
        {
            InitialContext context = new InitialContext();
            context.rebind(name, object);
        }
        catch(NamingException exc)
        {
            throw new RuntimeException("Object with name " + name + " not found.");
        }
    }
    
    /**
     * Lookup an object. If the object is not bound to the <code>InitialContext</code>,
     * a <code>RuntimeException</code> will be thrown.
     * @param name the name of the object
     * @return the object
     * @throws RuntimeException if an object with the specified name cannot be found.
     */
    public Object lookup(String name)
    {
        try
        {
            InitialContext context = new InitialContext();
            return context.lookup(name);
        }
        catch(NamingException exc)
        {
            throw new RuntimeException("Object with name " + name + " not found.");
        }
    }
    
    /**
     * Lookup an EJB. The method looks up the home interface, calls
     * the <code>create</code> method and returns the result, which
     * you can cast to the remote interface. This method only works
     * with <code>create</code> methods that have an empty parameter list.
     * The <code>create</code> method must have the name <code>create</code>
     * with no postfix.
     * It works with the mock container but may fail with a real remote container.
     * This method throws a <code>RuntimeException</code> if no object with the 
     * specified name can be found. If the found object is no EJB home interface,
     * or if the corresponding <code>create</code> method cannot be found, this
     * method returns <code>null</code>.
     * @param name the name of the bean
     * @return the bean
     * @throws RuntimeException in case of error
     */
    public Object lookupBean(String name)
    {
        return lookupBean(name, new Object[0]);
    }
    
    /**
     * Lookup an EJB. The method looks up the home interface, calls
     * the <code>create</code> method with the specified parameters
     * and returns the result, which you can cast to the remote interface.
     * The <code>create</code> method must have the name <code>create</code>
     * with no postfix.
     * This method works with the mock container but may fail with
     * a real remote container.
     * This method throws a <code>RuntimeException</code> if no object with the 
     * specified name can be found. If the found object is no EJB home interface,
     * or if the corresponding <code>create</code> method cannot be found, this
     * method returns <code>null</code>.
     * @param name the name of the bean
     * @param parameters the parameters, <code>null</code> parameters are not allowed,
     *  primitive types are automatically unwrapped
     * @return the bean 
     * @throws RuntimeException in case of error
     */
    public Object lookupBean(String name, Object[] parameters)
    {
        return lookupBean(name, "create", parameters);
    }
    
    /**
     * Lookup an EJB. The method looks up the home interface, calls
     * the <code>create</code> method with the specified parameters
     * and returns the result, which you can cast to the remote interface.
     * This method works with the mock container but may fail with
     * a real remote container.
     * This method throws a <code>RuntimeException</code> if no object with the 
     * specified name can be found. If the found object is no EJB home interface,
     * or if the corresponding <code>create</code> method cannot be found, this
     * method returns <code>null</code>.
     * @param name the name of the bean
     * @param createMethod the name of the create method
     * @param parameters the parameters, <code>null</code> parameters are not allowed,
     *  primitive types are automatically unwrapped
     * @return the bean 
     * @throws RuntimeException in case of error
     */
    public Object lookupBean(String name, String createMethod, Object[] parameters)
    {
        Object object = lookup(name);
        if(null == object) return null;
        if(!(object instanceof EJBHome || object instanceof EJBLocalHome)) return null;
        try
        {
            return MethodUtils.invokeMethod(object, createMethod, parameters);
        }
        catch(Exception exc)
        {
            return null;
        }
    }
    
    /**
     * Resets the {@link com.mockrunner.mock.ejb.MockUserTransaction}.
     * Note: If you do not use the {@link com.mockrunner.mock.ejb.MockUserTransaction}
     * implementation, this method does nothing.
     */
    public void resetUserTransaction()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction) return;
        transaction.reset();
    }
    
    /**
     * Verifies that the transaction was committed. If you are using
     * container managed transactions, you have to set an appropriate 
     * transaction policy, e.g. <i>REQUIRED</i>. Otherwise the container
     * will not commit the mock transaction.
     * Note: If you do not use the {@link com.mockrunner.mock.ejb.MockUserTransaction}
     * implementation, this method throws a <code>VerifyFailedException</code>.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCommitted()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction)
        {
            throw new VerifyFailedException("MockTransaction is null.");
        }
        if(!transaction.wasCommitCalled())
        {
            throw new VerifyFailedException("Transaction was not committed.");
        }
    }
    
    /**
     * Verifies that the transaction was not committed. If you are using
     * container managed transactions, you have to set an appropriate 
     * transaction policy, e.g. <i>REQUIRED</i>.
     * Note: If you do not use the {@link com.mockrunner.mock.ejb.MockUserTransaction}
     * implementation, this method throws a <code>VerifyFailedException</code>.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNotCommitted()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction)
        {
            throw new VerifyFailedException("MockTransaction is null.");
        }
        if(transaction.wasCommitCalled())
        {
            throw new VerifyFailedException("Transaction was committed.");
        }
    }
    
    /**
     * Verifies that the transaction was rolled back. If you are using
     * container managed transactions, you have to set an appropriate 
     * transaction policy, e.g. <i>REQUIRED</i>. Otherwise the container
     * will not rollback the mock transaction.
     * Note: If you do not use the {@link com.mockrunner.mock.ejb.MockUserTransaction}
     * implementation, this method throws a <code>VerifyFailedException</code>.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyRolledBack()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction)
        {
            throw new VerifyFailedException("MockTransaction is null.");
        }
        if(!transaction.wasRollbackCalled())
        {
            throw new VerifyFailedException("Transaction was not rolled back");
        }
    }

    /**
     * Verifies that the transaction was not rolled back. If you are using
     * container managed transactions, you have to set an appropriate 
     * transaction policy, e.g. <i>REQUIRED</i>.
     * Note: If you do not use the {@link com.mockrunner.mock.ejb.MockUserTransaction}
     * implementation, this method throws a <code>VerifyFailedException</code>.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNotRolledBack()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction)
        {
            throw new VerifyFailedException("MockTransaction is null.");
        }
        if(transaction.wasRollbackCalled())
        {
            throw new VerifyFailedException("Transaction was rolled back");
        }
    }
    
    /**
     * Verifies that the transaction was marked for rollback using
     * the method <code>setRollbackOnly()</code>.
     * Note: If you do not use the {@link com.mockrunner.mock.ejb.MockUserTransaction}
     * implementation, this method throws a <code>VerifyFailedException</code>.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyMarkedForRollback()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction)
        {
            throw new VerifyFailedException("MockTransaction is null.");
        }
        if(!transaction.wasRollbackOnlyCalled())
        {
            throw new VerifyFailedException("Transaction was not marked for rollback");
        }
    }

    /**
     * Verifies that the transaction was not marked for rollback.
     * Note: If you do not use the {@link com.mockrunner.mock.ejb.MockUserTransaction}
     * implementation, this method throws a <code>VerifyFailedException</code>.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNotMarkedForRollback()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction)
        {
            throw new VerifyFailedException("MockTransaction is null.");
        }
        if(transaction.wasRollbackOnlyCalled())
        {
            throw new VerifyFailedException("Transaction was marked for rollback");
        }
    }
    
    private Class getHomeClass(Class beanClass)
    {
        String classPackage = ClassUtil.getPackageName(beanClass);
        String className = ClassUtil.getClassName(beanClass);
        className = truncateImplClassName(className);
        if(null != homeInterfaceSuffix && 0 != homeInterfaceSuffix.length())
        {
            className += homeInterfaceSuffix;
        }
        if(null != homeInterfacePackage && 0 != homeInterfacePackage.length())
        {
            classPackage = homeInterfacePackage;
        }
        try
        {
            return Class.forName(classPackage + "." + className);
        }
        catch(ClassNotFoundException exc)
        {
            throw new RuntimeException("Home interface not found: " + exc.getMessage());
        }
    }
    
    private Class getRemoteClass(Class beanClass)
    {
        String classPackage = ClassUtil.getPackageName(beanClass);
        String className = ClassUtil.getClassName(beanClass);
        className = truncateImplClassName(className);
        if(null != businessInterfaceSuffix && 0 != businessInterfaceSuffix.length())
        {
            className += businessInterfaceSuffix;
        }
        if(null != businessInterfacePackage && 0 != businessInterfacePackage.length())
        {
            classPackage = businessInterfacePackage;
        }
        try
        {
            return Class.forName(classPackage + "." + className);
        }
        catch(ClassNotFoundException exc)
        {
            throw new RuntimeException("Interface not found: " + exc.getMessage());
        }
    }

    private String truncateImplClassName(String className)
    {
        if(null != impSuffix && className.endsWith(impSuffix))
        {
            className = className.substring(0, className.length() - impSuffix.length());
        }
        return className;
    }
}
