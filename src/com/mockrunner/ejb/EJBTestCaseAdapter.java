package com.mockrunner.ejb;

import org.mockejb.MockEjbObject;
import org.mockejb.SessionBeanDescriptor;
import org.mockejb.TransactionPolicy;

import com.mockrunner.base.BaseTestCase;

/**
 * Delegator for {@link EJBTestModule}. You can
 * subclass this adapter or use {@link EJBTestModule}
 * directly (so your test case can use another base
 * class).
 */
public class EJBTestCaseAdapter extends BaseTestCase
{
    private EJBTestModule ejbTestModule;
    
    public EJBTestCaseAdapter()
    {

    }

    public EJBTestCaseAdapter(String arg0)
    {
        super(arg0);
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        ejbTestModule = null;
    }

    /**
     * Creates the <code>EJBTestModule</code>. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        ejbTestModule = createEJBTestModule(getEJBMockObjectFactory());
    }
    
    /**
     * Gets the <code>EJBTestModule</code>. 
     * @return the <code>EJBTestModule</code>
     */
    protected EJBTestModule getEJBTestModule()
    {
        return ejbTestModule;
    }

    /**
     * Sets the <code>EJBTestModule</code>. 
     * @param ejbTestModule the <code>EJBTestModule</code>
     */
    protected void setEJBTestModule(EJBTestModule ejbTestModule)
    {
        this.ejbTestModule = ejbTestModule;
    }
    
    /**
     * Delegates to {@link EJBTestModule#setImplementationSuffix}
     */
    public void setImplementationSuffix(String impSuffix)
    {
        ejbTestModule.setImplementationSuffix(impSuffix);
    }

    /**
     * Delegates to {@link EJBTestModule#setBusinessInterfaceSuffix}
     */
    public void setBusinessInterfaceSuffix(String businessInterfaceSuffix)
    {
        ejbTestModule.setBusinessInterfaceSuffix(businessInterfaceSuffix);
    }

    /**
     * Delegates to {@link EJBTestModule#setHomeInterfaceSuffix}
     */
    public void setHomeInterfaceSuffix(String homeInterfaceSuffix)
    {
        ejbTestModule.setHomeInterfaceSuffix(homeInterfaceSuffix);
    }

    /**
     * Delegates to {@link EJBTestModule#setInterfacePackage}
     */
    public void setInterfacePackage(String interfacePackage)
    {
        ejbTestModule.setInterfacePackage(interfacePackage);
    }

    /**
     * Delegates to {@link EJBTestModule#setHomeInterfacePackage}
     */
    public void setHomeInterfacePackage(String homeInterfacePackage)
    {
        ejbTestModule.setHomeInterfacePackage(homeInterfacePackage);
    }

    /**
     * Delegates to {@link EJBTestModule#setBusinessInterfacePackage}
     */
    public void setBusinessInterfacePackage(String businessInterfacePackage)
    {
        ejbTestModule.setBusinessInterfacePackage(businessInterfacePackage);
    }

    /**
     * Delegates to {@link EJBTestModule#deploy(SessionBeanDescriptor)}
     */
    public MockEjbObject deploy(SessionBeanDescriptor descriptor)
    {
        return ejbTestModule.deploy(descriptor);
    }

    /**
     * Delegates to {@link EJBTestModule#deploy(SessionBeanDescriptor, TransactionPolicy)}
     */
    public MockEjbObject deploy(SessionBeanDescriptor descriptor, TransactionPolicy policy)
    {
        return ejbTestModule.deploy(descriptor, policy);
    }

    /**
     * Delegates to {@link EJBTestModule#deploy(String, Class)}
     */
    public MockEjbObject deploy(String jndiName, Class beanClass)
    {
        return ejbTestModule.deploy(jndiName, beanClass);
    }
    
    /**
     * Delegates to {@link EJBTestModule#deploy(String, Class, boolean)}
     */
    public MockEjbObject deploy(String jndiName, Class beanClass, boolean stateful)
    {
        return ejbTestModule.deploy(jndiName, beanClass, stateful);
    }

    /**
     * Delegates to {@link EJBTestModule#deploy(String, Class, TransactionPolicy)}
     */
    public MockEjbObject deploy(String jndiName, Class beanClass, TransactionPolicy policy)
    {
        return ejbTestModule.deploy(jndiName, beanClass, policy);
    }
    
    /**
     * Delegates to {@link EJBTestModule#deploy(String, Class, boolean, TransactionPolicy)}
     */
    public MockEjbObject deploy(String jndiName, Class beanClass, boolean stateful, TransactionPolicy policy)
    {
        return ejbTestModule.deploy(jndiName, beanClass, stateful, policy);
    }

    /**
     * Delegates to {@link EJBTestModule#bindToContext}
     */
    public void bindToContext(String name, Object object)
    {
        ejbTestModule.bindToContext(name, object);
    }

    /**
     * Delegates to {@link EJBTestModule#lookup}
     */
    public Object lookup(String name)
    {
        return ejbTestModule.lookup(name);
    }

    /**
     * Delegates to {@link EJBTestModule#lookupBean(String)}
     */
    public Object lookupBean(String name)
    {
        return ejbTestModule.lookupBean(name);
    }

    /**
     * Delegates to {@link EJBTestModule#lookupBean(String, Object[])}
     */
    public Object lookupBean(String name, Object[] parameters)
    {
        return ejbTestModule.lookupBean(name, parameters);
    }

    /**
     * Delegates to {@link EJBTestModule#resetUserTransaction}
     */
    public void resetUserTransaction()
    {
        ejbTestModule.resetUserTransaction();
    }

    /**
     * Delegates to {@link EJBTestModule#verifyCommitted}
     */
    public void verifyCommitted()
    {
        ejbTestModule.verifyCommitted();
    }

    /**
     * Delegates to {@link EJBTestModule#verifyNotCommitted}
     */
    public void verifyNotCommitted()
    {
        ejbTestModule.verifyNotCommitted();
    }

    /**
     * Delegates to {@link EJBTestModule#verifyRolledBack}
     */
    public void verifyRolledBack()
    {
        ejbTestModule.verifyRolledBack();
    }

    /**
     * Delegates to {@link EJBTestModule#verifyNotRolledBack}
     */
    public void verifyNotRolledBack()
    {
        ejbTestModule.verifyNotRolledBack();
    }

    /**
     * Delegates to {@link EJBTestModule#verifyMarkedForRollback}
     */
    public void verifyMarkedForRollback()
    {
        ejbTestModule.verifyMarkedForRollback();
    }

    /**
     * Delegates to {@link EJBTestModule#verifyNotMarkedForRollback}
     */
    public void verifyNotMarkedForRollback()
    {
        ejbTestModule.verifyNotMarkedForRollback();
    }
}
