package com.mockrunner.ejb;

import org.mockejb.MockContainer;
import org.mockejb.MockContext;
import org.mockejb.MockEjbObject;
import org.mockejb.SessionBeanDescriptor;

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
    private String homeSuffix;
    private String interfaceSuffix;
    private String homeInterfacePackage;
    private String interfacePackage;
    
    public EJBTestModule(EJBMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
        impSuffix = "Bean";
        homeSuffix = "Home";
        interfaceSuffix = "";
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
     * @param remoteSuffix the bean implementation suffix
     */
    public void setInterfaceSuffix(String remoteSuffix)
    {
        this.interfaceSuffix = remoteSuffix;
    }
    
    /**
     * Sets the suffix of the home (resp. local home) interface. The
     * default is <i>"Home"</i>, i.e. if the implementation class is
     * <code>TestBean</code>, the remote interface is <code>TestHome</code>
     * @param remoteSuffix the bean implementation suffix
     */
    public void setHomeSuffix(String homeSuffix)
    {
        this.homeSuffix = homeSuffix;
    }
    
    /**
     * Sets the package for the bean home and remote interfaces. Per
     * default, the framework expects that the interfaces are in the
     * same package as the bean implementation classes.
     * @param interfacePackage the package name for home and remote interfaces
     */
    public void setInterfacesPackages(String interfacePackage)
    {
        setHomeInterfacePackage(interfacePackage);
        setInterfacePackage(interfacePackage);
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
     * @param remoteInterfacePackage the package name for home interface
     */
    public void setInterfacePackage(String remoteInterfacePackage)
    {
        this.interfacePackage = remoteInterfacePackage;
    }
    
    /**
     * Deploys a bean to the mock container using the specified
     * descriptor.
     * @param descriptor the descriptor
     * @return the <code>MockEjbObject</code> of the deployed bean
     */
    public MockEjbObject deploy(SessionBeanDescriptor descriptor)
    {
        return MockContainer.deploy(descriptor);
    }
    
    /**
     * Deploys a bean to the mock container. You have to specify
     * the implementation class and the JNDI name. The frameworks
     * determines the home and remote interfaces based on the
     * information specified with the <code>setSuffix</code>
     * and <code>setPackage</code> methods.
     * @param jndiName the JNDI name
     * @param beanClass the bean implementation class
     * @return the <code>MockEjbObject</code> of the deployed bean
     */
    public MockEjbObject deploy(String jndiName, Class beanClass)
    {
        SessionBeanDescriptor descriptor = new SessionBeanDescriptor(jndiName, getHomeClass(beanClass), getRemoteClass(beanClass), beanClass);
        return deploy(descriptor);
    }
    
    /**
     * Adds an object to the mock context.
     * @param name the name of the object
     * @param object the object to add
     */
    public void addToContext(String name, Object object)
    {
        MockContext.add(name, object);
    }
    
    /**
     * Resets the {@link com.mockrunner.mock.ejb.MockUserTransaction}.
     * Note: If you do not use the {@link com.mockrunner.mock.ejb.MockUserTransaction}
     * implementation, this method does nothing.
     * @param name the name of the object
     * @param object the object to add
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
     * implementation, this method does nothing.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCommitted()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction) return;
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
     * implementation, this method does nothing.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNotCommitted()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction) return;
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
     * implementation, this method does nothing.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyRolledBack()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction) return;
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
     * implementation, this method does nothing.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNotRolledBack()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction) return;
        if(transaction.wasRollbackCalled())
        {
            throw new VerifyFailedException("Transaction was rolled back");
        }
    }
    
    /**
     * Verifies that the transaction was marked for rollback using
     * the method <code>setRollbackOnly()</code>.
     * Note: If you do not use the {@link com.mockrunner.mock.ejb.MockUserTransaction}
     * implementation, this method does nothing.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyMarkedForRollback()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction) return;
        if(!transaction.wasRollbackOnlyCalled())
        {
            throw new VerifyFailedException("Transaction was not marked for rollback");
        }
    }

    /**
     * Verifies that the transaction was not marked for rollback.
     * Note: If you do not use the {@link com.mockrunner.mock.ejb.MockUserTransaction}
     * implementation, this method does nothing.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNotMarkedForRollback()
    {
        MockUserTransaction transaction = mockFactory.getMockUserTransaction();
        if(null == transaction) return;
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
        if(null != homeSuffix)
        {
            className += homeSuffix;
        }
        if(null != homeInterfacePackage)
        {
            classPackage = homeInterfacePackage;
        }
        try
        {
            return Class.forName(classPackage + "." + className);
        }
        catch (ClassNotFoundException exc)
        {
            throw new RuntimeException("Home interface not found: " + exc.getMessage());
        }
    }
    
    private Class getRemoteClass(Class beanClass)
    {
        String classPackage = ClassUtil.getPackageName(beanClass);
        String className = ClassUtil.getClassName(beanClass);
        className = truncateImplClassName(className);
        if(null != interfaceSuffix)
        {
            className += interfaceSuffix;
        }
        if(null != interfacePackage)
        {
            classPackage = interfacePackage;
        }
        try
        {
            return Class.forName(classPackage + "." + className);
        }
        catch (ClassNotFoundException exc)
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
