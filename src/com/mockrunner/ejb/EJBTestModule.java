package com.mockrunner.ejb;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mockejb.BasicEjbDescriptor;
import org.mockejb.EntityBeanDescriptor;
import org.mockejb.MDBDescriptor;
import org.mockejb.SessionBeanDescriptor;
import org.mockejb.TransactionManager;
import org.mockejb.TransactionPolicy;
import org.mockejb.interceptor.AspectSystemFactory;
import org.mockejb.interceptor.ClassPointcut;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.mock.ejb.MockUserTransaction;
import com.mockrunner.util.common.ClassUtil;

/**
 * Module for EJB tests.
 */
public class EJBTestModule
{
    private final static Log log = LogFactory.getLog(EJBTestModule.class);
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
     * Sets the suffix of the remote (local respectively) interface. The
     * default is an empty string, i.e. if the implementation class is
     * <code>TestBean</code>, the remote interface is <code>Test</code>
     * @param businessInterfaceSuffix the bean remote interface suffix
     */
    public void setBusinessInterfaceSuffix(String businessInterfaceSuffix)
    {
        this.businessInterfaceSuffix = businessInterfaceSuffix;
    }
    
    /**
     * Sets the suffix of the home (local home respectively) interface. The
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
     * Sets the package for the bean home (local home respectively) interface. Per
     * default, the framework expects that the interfaces are in the
     * same package as the bean implementation classes.
     * @param homeInterfacePackage the package name for home interface
     */
    public void setHomeInterfacePackage(String homeInterfacePackage)
    {
        this.homeInterfacePackage = homeInterfacePackage;
    }
    
    /**
     * Sets the package for the bean remote (local respectively) interface. Per
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
     * Determines the type of bean (session, entity, message driven)
     * using the descriptor.
     * @param descriptor the descriptor
     */
    public void deploy(BasicEjbDescriptor descriptor)
    {
        deploy(descriptor, TransactionPolicy.SUPPORTS);
    }
    
    /**
     * Deploys a bean to the mock container using the specified
     * descriptor. Determines the type of bean (session, entity, message driven)
     * using the descriptor.
     * The specified transaction policy will be automatically set. If the
     * specified transaction policy is <code>null</code>, no transaction policy
     * will be set. This makes sense for BMT EJBs. Please note that the
     * <code>deploy</code> methods of this class without a transaction policy
     * argument automatically set the <i>SUPPORTS</i> policy, which also
     * works fine for BMT EJBs.
     * @param descriptor the descriptor
     * @param policy the transaction policy
     */
    public void deploy(BasicEjbDescriptor descriptor, TransactionPolicy policy)
    {
        try
        {
            if(descriptor instanceof SessionBeanDescriptor)
            {
				mockFactory.getMockContainer().deploy((SessionBeanDescriptor)descriptor);
            }
            else if(descriptor instanceof EntityBeanDescriptor)
            {
				mockFactory.getMockContainer().deploy((EntityBeanDescriptor)descriptor);
            }
			else if(descriptor instanceof MDBDescriptor)
			{
				mockFactory.getMockContainer().deploy((MDBDescriptor)descriptor);
			}
            if(null != policy)
            {
                AspectSystemFactory.getAspectSystem().add(new ClassPointcut(descriptor.getIfaceClass(), false), new TransactionManager(policy));
            }
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
        } 
    }
    
    /**
     * Deploys a stateless session bean to the mock container. You have to specify
     * the implementation class and the JNDI name. The frameworks
     * determines the home and remote interfaces based on the
     * information specified with the <code>setSuffix</code>
     * and <code>setPackage</code> methods.
     * Sets the transaction policy <i>SUPPORTS</i>.
     * @param jndiName the JNDI name
     * @param beanClass the bean implementation class
     */
    public void deploySessionBean(String jndiName, Class beanClass)
    {
		deploySessionBean(jndiName, beanClass, false, TransactionPolicy.SUPPORTS);
    }
    
    /**
     * Deploys a session bean to the mock container. You have to specify
     * the implementation class and the JNDI name. The frameworks
     * determines the home and remote interfaces based on the
     * information specified with the <code>setSuffix</code>
     * and <code>setPackage</code> methods.
     * Sets the transaction policy <i>SUPPORTS</i>.
     * @param jndiName the JNDI name
     * @param beanClass the bean implementation class
     * @param stateful is the bean stateful
     */
    public void deploySessionBean(String jndiName, Class beanClass, boolean stateful)
    {
		deploySessionBean(jndiName, beanClass, stateful, TransactionPolicy.SUPPORTS);
    }
    
    /**
     * Deploys a stateless session bean to the mock container. You have to specify
     * the implementation class and the JNDI name. The frameworks
     * determines the home and remote interfaces based on the
     * information specified with the <code>setSuffix</code>
     * and <code>setPackage</code> methods.
     * The specified transaction policy will be automatically set.
     * @param jndiName the JNDI name
     * @param beanClass the bean implementation class
     * @param policy the transaction policy
     */
    public void deploySessionBean(String jndiName, Class beanClass, TransactionPolicy policy)
    {
		deploySessionBean(jndiName, beanClass, false, policy);
    }
    
    /**
     * Deploys a session bean to the mock container. You have to specify
     * the implementation class and the JNDI name. The frameworks
     * determines the home and remote interfaces based on the
     * information specified with the <code>setSuffix</code>
     * and <code>setPackage</code> methods.
     * The specified transaction policy will be automatically set.
     * @param jndiName the JNDI name
     * @param beanClass the bean implementation class
     * @param stateful is the bean stateful
     * @param policy the transaction policy
     */
    public void deploySessionBean(String jndiName, Class beanClass, boolean stateful, TransactionPolicy policy)
    {
        SessionBeanDescriptor descriptor = new SessionBeanDescriptor(jndiName, getHomeClass(beanClass), getRemoteClass(beanClass), beanClass);
        descriptor.setStateful(stateful);
        deploy(descriptor, policy);
    }
    
	/**
	 * Deploys a stateless session bean to the mock container. You have to specify
	 * the implementation class and the JNDI name. The frameworks
	 * determines the home and remote interfaces based on the
	 * information specified with the <code>setSuffix</code>
	 * and <code>setPackage</code> methods.
	 * Sets the transaction policy <i>SUPPORTS</i>.
	 * @param jndiName the JNDI name
	 * @param bean the bean implementation
	 */
	public void deploySessionBean(String jndiName, Object bean)
	{
		deploySessionBean(jndiName, bean, false, TransactionPolicy.SUPPORTS);
	}

	/**
	 * Deploys a session bean to the mock container. You have to specify
	 * the implementation class and the JNDI name. The frameworks
	 * determines the home and remote interfaces based on the
	 * information specified with the <code>setSuffix</code>
	 * and <code>setPackage</code> methods.
	 * Sets the transaction policy <i>SUPPORTS</i>.
	 * @param jndiName the JNDI name
	 * @param bean the bean implementation
	 * @param stateful is the bean stateful
	 */
	public void deploySessionBean(String jndiName, Object bean, boolean stateful)
	{
		deploySessionBean(jndiName, bean, stateful, TransactionPolicy.SUPPORTS);
	}

	/**
	 * Deploys a stateless session bean to the mock container. You have to specify
	 * the implementation class and the JNDI name. The frameworks
	 * determines the home and remote interfaces based on the
	 * information specified with the <code>setSuffix</code>
	 * and <code>setPackage</code> methods.
	 * The specified transaction policy will be automatically set.
	 * @param jndiName the JNDI name
	 * @param bean the bean implementation
	 * @param policy the transaction policy
	 */
	public void deploySessionBean(String jndiName, Object bean, TransactionPolicy policy)
	{
		deploySessionBean(jndiName, bean, false, policy);
	}

	/**
	 * Deploys a session bean to the mock container. You have to specify
	 * the implementation class and the JNDI name. The frameworks
	 * determines the home and remote interfaces based on the
	 * information specified with the <code>setSuffix</code>
	 * and <code>setPackage</code> methods.
	 * The specified transaction policy will be automatically set.
	 * @param jndiName the JNDI name
	 * @param bean the bean implementation
	 * @param stateful is the bean stateful
	 * @param policy the transaction policy
	 */
	public void deploySessionBean(String jndiName, Object bean, boolean stateful, TransactionPolicy policy)
	{
		SessionBeanDescriptor descriptor = new SessionBeanDescriptor(jndiName, getHomeClass(bean.getClass()), getRemoteClass(bean.getClass()), bean);
		descriptor.setStateful(stateful);
		deploy(descriptor, policy);
	}
    
	/**
	 * Deploys an entity bean to the mock container. You have to specify
	 * the implementation class and the JNDI name. The frameworks
	 * determines the home and remote interfaces based on the
	 * information specified with the <code>setSuffix</code>
	 * and <code>setPackage</code> methods.
	 * Sets the transaction policy <i>SUPPORTS</i>.
	 * @param jndiName the JNDI name
	 * @param beanClass the bean implementation class
	 */
	public void deployEntityBean(String jndiName, Class beanClass)
	{
		deployEntityBean(jndiName, beanClass, TransactionPolicy.SUPPORTS);
	}
    
	/**
	 * Deploys an entity bean to the mock container. You have to specify
	 * the implementation class and the JNDI name. The frameworks
	 * determines the home and remote interfaces based on the
	 * information specified with the <code>setSuffix</code>
	 * and <code>setPackage</code> methods.
	 * The specified transaction policy will be automatically set.
	 * @param jndiName the JNDI name
	 * @param beanClass the bean implementation class
	 * @param policy the transaction policy
	 */
	public void deployEntityBean(String jndiName, Class beanClass, TransactionPolicy policy)
	{
		EntityBeanDescriptor descriptor = new EntityBeanDescriptor(jndiName, getHomeClass(beanClass), getRemoteClass(beanClass), beanClass);
		deploy(descriptor, policy);
	}
	
	/**
	 * Deploys a message driven bean to the mock container.
	 * You have to specify JNDI names for connection factory and
	 * destination. For creating connection factory and destination 
	 * objects you can use {@link com.mockrunner.mock.jms.JMSMockObjectFactory}
	 * and {@link com.mockrunner.jms.DestinationManager}.
	 * The specified objects are automatically bound to JNDI using
	 * the specified names. The mock container automatically creates
	 * a connection and session.
	 * Sets the transaction policy <i>NOT_SUPPORTED</i>.
	 * @param connectionFactoryJndiName the JNDI name of the connection factory
	 * @param destinationJndiName the JNDI name of the destination
	 * @param connectionFactory the connection factory
	 * @param destination the destination
	 * @param bean the message driven bean instance
	 */
	public void deployMessageBean(String connectionFactoryJndiName, String destinationJndiName, ConnectionFactory connectionFactory, Destination destination, Object bean)
	{
		deployMessageBean(connectionFactoryJndiName, destinationJndiName, connectionFactory, destination, bean, TransactionPolicy.NOT_SUPPORTED);
	}
	
	/**
	 * Deploys a message driven bean to the mock container.
	 * You have to specify JNDI names for connection factory and
	 * destination. For creating connection factory and destination 
	 * objects you can use {@link com.mockrunner.mock.jms.JMSMockObjectFactory}
	 * and {@link com.mockrunner.jms.DestinationManager}.
	 * The specified objects are automatically bound to JNDI using
	 * the specified names. The mock container automatically creates
	 * a connection and session.
	 * The specified transaction policy will be automatically set.
	 * @param connectionFactoryJndiName the JNDI name of the connection factory
	 * @param destinationJndiName the JNDI name of the destination
	 * @param connectionFactory the connection factory
	 * @param destination the destination
	 * @param bean the message driven bean instance
	 * @param policy the transaction policy
	 */
	public void deployMessageBean(String connectionFactoryJndiName, String destinationJndiName, ConnectionFactory connectionFactory, Destination destination, Object bean, TransactionPolicy policy)
	{
		bindToContext(connectionFactoryJndiName, connectionFactory);
		bindToContext(destinationJndiName, destination);
		MDBDescriptor descriptor = new MDBDescriptor(connectionFactoryJndiName, destinationJndiName, bean);
		descriptor.setIsAlreadyBound(true);
		descriptor.setIsTopic(destination instanceof Topic);
		deploy(descriptor, policy);
	}
    
    /**
     * Adds an object to the mock context by calling <code>rebind</code>
     * @param name JNDI name of the object
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
     * @param name JNDI name of the object
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
     * @deprecated use {@link #createBean(String)}
     */
    public Object lookupBean(String name)
    {
        return createBean(name);
    }
    
    /**
     * Create an EJB. The method looks up the home interface, calls
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
     * @param name JNDI name of the bean
     * @return the bean
     * @throws RuntimeException in case of error
     */
    public Object createBean(String name)
    {
        return createBean(name, new Object[0]);
    }
    
    /**
     * @deprecated use {@link #createBean(String, Object[])}
     */
    public Object lookupBean(String name, Object[] parameters)
    {
        return createBean(name, parameters);
    }
    
    /**
     * Create an EJB. The method looks up the home interface, calls
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
     * This method does not allow <code>null</code> as a parameter, because
     * the type of the parameter cannot be determined in this case.
     * @param name JNDI name of the bean
     * @param parameters the parameters, <code>null</code> parameters are not allowed,
     *  primitive types are automatically unwrapped
     * @return the bean 
     * @throws RuntimeException in case of error
     */
    public Object createBean(String name, Object[] parameters)
    {
        return createBean(name, "create", parameters);
    }
    
    /**
     * @deprecated use {@link #createBean(String, String, Object[])}
     */
    public Object lookupBean(String name, String createMethod, Object[] parameters)
    {
        return createBean(name, createMethod, parameters);
    }
    
    /**
     * Create an EJB. The method looks up the home interface, calls
     * the <code>create</code> method with the specified parameters
     * and returns the result, which you can cast to the remote interface.
     * This method works with the mock container but may fail with
     * a real remote container.
     * This method throws a <code>RuntimeException</code> if no object with the 
     * specified name can be found. If the found object is no EJB home interface,
     * or if the corresponding <code>create</code> method cannot be found, this
     * method returns <code>null</code>.
     * This method does not allow <code>null</code> as a parameter, because
     * the type of the parameter cannot be determined in this case.
     * @param name JNDI name of the bean
     * @param createMethod the name of the create method
     * @param parameters the parameters, <code>null</code> parameters are not allowed,
     *  primitive types are automatically unwrapped
     * @return the bean 
     * @throws RuntimeException in case of error
     */
    public Object createBean(String name, String createMethod, Object[] parameters)
    {
        Object home = lookupHome(name);
        return invokeHomeMethod(home, createMethod, parameters, null);
    }
    
    /**
     * Create an EJB. The method looks up the home interface, calls
     * the <code>create</code> method with the specified parameters
     * and returns the result, which you can cast to the remote interface.
     * This method works with the mock container but may fail with
     * a real remote container.
     * This method throws a <code>RuntimeException</code> if no object with the 
     * specified name can be found. If the found object is no EJB home interface,
     * or if the corresponding <code>create</code> method cannot be found, this
     * method returns <code>null</code>.
     * This method does allow <code>null</code> as a parameter.
     * @param name JNDI name of the bean
     * @param createMethod the name of the create method
     * @param parameters the parameters, <code>null</code> is allowed as a parameter
     * @param parameterTypes the type of the specified parameters
     * @return the bean 
     * @throws RuntimeException in case of error
     */
    public Object createBean(String name, String createMethod, Object[] parameters, Class[] parameterTypes)
    {
        Object home = lookupHome(name);
        return invokeHomeMethod(home, createMethod, parameters, parameterTypes);
    }
    
    /**
     * Create an entity EJB. The method looks up the home interface, calls
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
     * The created entity EJB is added to the mock database automatically
     * using the provided primary key.
     * @param name JNDI name of the bean
     * @param primaryKey the primary key
     * @return the bean
     * @throws RuntimeException in case of error
     */
    public Object createEntityBean(String name, Object primaryKey)
    {
        return createEntityBean(name, new Object[0], primaryKey);
    }
    
    /**
     * Create an entity EJB. The method looks up the home interface, calls
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
     * The created entity EJB is added to the mock database automatically
     * using the provided primary key.
     * This method does not allow <code>null</code> as a parameter, because
     * the type of the parameter cannot be determined in this case.
     * @param name JNDI name of the bean
     * @param parameters the parameters, <code>null</code> parameters are not allowed,
     *  primitive types are automatically unwrapped
     * @param primaryKey the primary key
     * @return the bean 
     * @throws RuntimeException in case of error
     */
    public Object createEntityBean(String name, Object[] parameters, Object primaryKey)
    {
        return createEntityBean(name, "create", parameters, primaryKey);
    }
    
    /**
     * Create an entity EJB. The method looks up the home interface, calls
     * the <code>create</code> method with the specified parameters
     * and returns the result, which you can cast to the remote interface.
     * This method works with the mock container but may fail with
     * a real remote container.
     * This method throws a <code>RuntimeException</code> if no object with the 
     * specified name can be found. If the found object is no EJB home interface,
     * or if the corresponding <code>create</code> method cannot be found, this
     * method returns <code>null</code>.
     * The created entity EJB is added to the mock database automatically
     * using the provided primary key.
     * This method does not allow <code>null</code> as a parameter, because
     * the type of the parameter cannot be determined in this case.
     * @param name JNDI name of the bean
     * @param createMethod the name of the create method
     * @param parameters the parameters, <code>null</code> parameters are not allowed,
     *  primitive types are automatically unwrapped
     * @param primaryKey the primary key
     * @return the bean 
     * @throws RuntimeException in case of error
     */
    public Object createEntityBean(String name, String createMethod, Object[] parameters, Object primaryKey)
    {
        return createEntityBean(name, createMethod, parameters, (Class[])null, primaryKey);
    }
    
    /**
     * Create an entity EJB. The method looks up the home interface, calls
     * the <code>create</code> method with the specified parameters
     * and returns the result, which you can cast to the remote interface.
     * This method works with the mock container but may fail with
     * a real remote container.
     * This method throws a <code>RuntimeException</code> if no object with the 
     * specified name can be found. If the found object is no EJB home interface,
     * or if the corresponding <code>create</code> method cannot be found, this
     * method returns <code>null</code>.
     * The created entity EJB is added to the mock database automatically
     * using the provided primary key.
     * This method does allow <code>null</code> as a parameter.
     * @param name JNDI name of the bean
     * @param createMethod the name of the create method
     * @param parameters the parameters, <code>null</code> is allowed as a parameter
     * @param primaryKey the primary key
     * @return the bean 
     * @throws RuntimeException in case of error
     */
    public Object createEntityBean(String name, String createMethod, Object[] parameters, Class[] parameterTypes, Object primaryKey)
    {
        Object home = lookupHome(name);
        Object remote = invokeHomeMethod(home, createMethod, parameters, parameterTypes);
        Class[] interfaces = home.getClass().getInterfaces();
        Class homeInterface = getHomeInterfaceClass(interfaces);
        if(null != homeInterface && null != remote)
        {
            mockFactory.getMockContainer().getEntityDatabase().add(homeInterface, primaryKey, remote);
        }
        return remote;
    }
    
    /**
     * Finds an entity EJB by its primary key. The method looks up the home interface, 
     * calls the <code>findByPrimaryKey</code> method and returns the result, 
     * which you can cast to the remote interface.
     * This method works with the mock container but may fail with
     * a real remote container.
     * This method throws a <code>RuntimeException</code> if no object with the 
     * specified name can be found. If the found object is no EJB home interface,
     * or if the <code>findByPrimaryKey</code> method cannot be found, this
     * method returns <code>null</code>.
     * If the mock container throws an exception because the primary key
     * cannot be found in the entity database, this method returns <code>null</code>.
     * @param name JNDI name of the bean
     * @param primaryKey the primary key
     * @return the bean 
     * @throws RuntimeException in case of error
     */
    public Object findByPrimaryKey(String name, Object primaryKey)
    {
        Object home = lookupHome(name);
        return invokeHomeMethod(home, "findByPrimaryKey", new Object[] {primaryKey}, null);
    }
    
    private Class getHomeInterfaceClass(Class[] interfaces)
    {
        for(int ii = 0; ii < interfaces.length; ii++)
        {
            Class current = interfaces[ii];
            if(EJBHome.class.isAssignableFrom(current) || EJBLocalHome.class.isAssignableFrom(current))
            {
                 return current;
            }
        }
        return null;
    }

    private Object lookupHome(String name)
    {
        Object object = lookup(name);
        if(null == object) return null;
        if(!(object instanceof EJBHome || object instanceof EJBLocalHome)) return null;
        return object;
    }
    
    private Object invokeHomeMethod(Object home, String methodName, Object[] parameters, Class[] parameterTypes)
    {
        if(null == parameterTypes)
        {
            checkNullParameters(methodName, parameters);
        }
        try
        {
            if(null == parameterTypes)
            {
                return MethodUtils.invokeMethod(home, methodName, parameters);
            }
            else
            {
                return MethodUtils.invokeExactMethod(home, methodName, parameters, parameterTypes);
            }
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            return null;
        }
    }
    
    private void checkNullParameters(String createMethod, Object[] parameters)
    {
        for(int ii = 0; ii < parameters.length; ii++)
        {
            if(null == parameters[ii])
            {
                String message = "Calling method " + createMethod + " failed. ";
                message += "Null is not allowed if the parameter types are not specified.";
                throw new IllegalArgumentException(message);
            }
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
            return Class.forName(getClassName(classPackage, className), true, beanClass.getClassLoader());
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
            return Class.forName(getClassName(classPackage, className), true, beanClass.getClassLoader());
        }
        catch(ClassNotFoundException exc)
        {
            throw new RuntimeException("Interface not found: " + exc.getMessage());
        }
    }
    
    private String getClassName(String packageName, String className)
    {
        if(null == packageName || packageName.length() == 0) return className;
        return packageName + "." + className;
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
