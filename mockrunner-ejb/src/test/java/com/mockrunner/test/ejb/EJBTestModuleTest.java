package com.mockrunner.test.ejb;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockejb.TransactionPolicy;

import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.mock.ejb.EJBMockObjectFactory;
import com.mockrunner.mock.ejb.MockUserTransaction;
//import com.mockrunner.mock.jms.JMSMockObjectFactory;
//import com.mockrunner.mock.jms.MockQueue;
//import com.mockrunner.mock.jms.MockQueueConnection;
//import com.mockrunner.mock.jms.MockQueueConnectionFactory;
//import com.mockrunner.mock.jms.MockQueueReceiver;
//import com.mockrunner.mock.jms.MockQueueSession;
//import com.mockrunner.mock.jms.MockTextMessage;
//import com.mockrunner.mock.jms.MockTopic;
//import com.mockrunner.mock.jms.MockTopicConnection;
//import com.mockrunner.mock.jms.MockTopicConnectionFactory;
//import com.mockrunner.mock.jms.MockTopicSession;
//import com.mockrunner.mock.jms.MockTopicSubscriber;

public class EJBTestModuleTest
{
    private EJBMockObjectFactory ejbMockFactory;
    private EJBTestModule ejbModule;
//    private JMSMockObjectFactory jmsMockFactory;
    
    @Before
    public void setUp() throws Exception
    {
        ejbMockFactory = new EJBMockObjectFactory();
        ejbModule = new EJBTestModule(ejbMockFactory);
//        jmsMockFactory = new JMSMockObjectFactory();
    }

    @After
	public void tearDown() throws Exception
	{
		ejbMockFactory.resetMockContextFactory();
	}

    @Test
    public void testCreateSessionBean() throws Exception
    {
        ejbModule.deploySessionBean("com/MyLookupTest", TestSessionBean.class);
        try
        {
            ejbModule.createBean("com/MyLookupTestTest");
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        Object bean = ejbModule.createBean("com/MyLookupTest");
        assertTrue(bean instanceof TestSession);
        bean = ejbModule.createBean("com/MyLookupTest", new Object[] {new Integer(1)});
        assertTrue(bean instanceof TestSession);
        bean = ejbModule.createBean("com/MyLookupTest", new Object[] {new Integer(1), Boolean.TRUE});
        assertTrue(bean instanceof TestSession);
        bean = ejbModule.createBean("com/MyLookupTest", "createWithSuffix", new Object[] {new Integer(1), Boolean.TRUE});
        assertTrue(bean instanceof TestSession);
        assertNull(ejbModule.createBean("com/MyLookupTest", "createWithPostfiy", new Object[] {new Integer(1), Boolean.TRUE}));
        try
        {
            ejbModule.createBean("com/MyLookupTestTest", new Object[] {Boolean.TRUE, new Integer(1)});
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
    
    @Test
    public void testCreateEntityBean() throws Exception
    {
        ejbModule.setBusinessInterfaceSuffix("Bean");
        ejbModule.setImplementationSuffix("EJB");
        ejbModule.deployEntityBean("com/AnEntityBean", TestEntityEJB.class);
        try
        {
            ejbModule.createBean("com/AnEntity");
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        Object bean = ejbModule.createEntityBean("com/AnEntityBean", "myPk");
        assertTrue(bean instanceof TestEntityBean);
        TestEntityHome home = (TestEntityHome)ejbModule.lookup("com/AnEntityBean");
        assertSame(bean, home.findByPrimaryKey("myPk"));
        bean = ejbModule.createEntityBean("com/AnEntityBean", "createWithName", new Object[] {"xyz"}, "anotherPk");
        assertSame(bean, home.findByPrimaryKey("anotherPk"));
        bean = ejbModule.createEntityBean("com/AnEntityBean", new Object[] {new Short((short)1)}, "thirdPk");
        assertSame(bean, home.findByPrimaryKey("thirdPk"));
        assertNull(ejbModule.createEntityBean("com/AnEntityBean", new Object[] {"xyz"}, "thirdPk"));
        assertSame(bean, home.findByPrimaryKey("thirdPk"));
    }
    
    @Test
    public void testCreateWithNullParameters() throws Exception
    {
        ejbModule.deploySessionBean("com/MyLookupTest", TestSessionBean.class);
        try
        {
            ejbModule.createBean("com/MyLookupTest", new Object[] {null});
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        Object bean = ejbModule.createBean("com/MyLookupTest", "create", new Object[] {null}, new Class[] {Integer.class});
        assertTrue(bean instanceof TestSession);
        bean = ejbModule.createBean("com/MyLookupTest", "create", new Object[] {new Integer(1), null}, new Class[] {Integer.TYPE, Boolean.class});
        assertTrue(bean instanceof TestSession);
        bean = ejbModule.createBean("com/MyLookupTest", "create", new Object[] {new Integer(1), null}, new Class[] {Integer.class, Boolean.class});
        assertNull(bean);
        ejbModule.setBusinessInterfaceSuffix("Bean");
        ejbModule.setImplementationSuffix("EJB");
        ejbModule.deployEntityBean("com/AnEntityBean", TestEntityEJB.class);
        try
        {
            ejbModule.createEntityBean("com/AnEntityBean", "createWithName", new Object[] {null}, "Pk");
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        bean = ejbModule.createEntityBean("com/AnEntityBean", "createWithName", new Object[] {"xyz"}, new Class[] {String.class}, "Pk");
        assertTrue(bean instanceof TestEntityBean);
    }
    
    @Test
    public void testFindByPrimaryKey() throws Exception
    {
        ejbModule.setBusinessInterfaceSuffix("Bean");
        ejbModule.setImplementationSuffix("EJB");
        ejbModule.deployEntityBean("com/AnEntityBean", TestEntityEJB.class);
        Object bean1 = ejbModule.createEntityBean("com/AnEntityBean", "myPk");
        Object bean2 = ejbModule.createEntityBean("com/AnEntityBean", "createWithName", new Object[] {"xyz"}, "anotherPk");
        assertSame(bean1, ejbModule.findByPrimaryKey("com/AnEntityBean", "myPk"));
        assertSame(bean2, ejbModule.findByPrimaryKey("com/AnEntityBean", "anotherPk"));
        try
        {
            ejbModule.findByPrimaryKey("com/AnEntity", "myPk");
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            ejbModule.findByPrimaryKey("com/AnEntity", "xyz");
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
        
    @Test
    public void testDeploySessionBeanClass() throws Exception
    {
        try
        {
            ejbModule.deploySessionBean("test", EJBTestModuleTest.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            ejbModule.setHomeInterfacePackage("com.mockrunner.test");
            ejbModule.deploySessionBean("test", TestSessionBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            ejbModule.setHomeInterfacePackage("com.mockrunner.test.ejb");
            ejbModule.setBusinessInterfacePackage("com.mockrunner.test");
            ejbModule.deploySessionBean("test", TestSessionBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            ejbModule.setInterfacePackage("com.mockrunner.test.ejb");
            ejbModule.setHomeInterfaceSuffix("Factory");
            ejbModule.deploySessionBean("test", TestSessionBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            ejbModule.setHomeInterfaceSuffix("Home");
            ejbModule.setBusinessInterfaceSuffix("Business");
            ejbModule.deploySessionBean("test", TestSessionBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        try
        {
            ejbModule.setBusinessInterfaceSuffix("");
            ejbModule.setImplementationSuffix("Impl");
            ejbModule.deploySessionBean("test", TestSessionBean.class);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        ejbModule.setImplementationSuffix("Bean");
        ejbModule.deploySessionBean("test", TestSessionBean.class);
        InitialContext context = new InitialContext();
        Object home = context.lookup("test");
        assertNotNull(home);
        assertTrue(home instanceof TestSessionHome);
    }
    
    @Test
	public void testDeploySessionBeanObject() throws Exception
	{
		try
		{
			ejbModule.deploySessionBean("test", "abc");
			fail();
		}
		catch(RuntimeException exc)
		{
			//should throw exception
		}
		try
		{
			ejbModule.setHomeInterfacePackage("com.mockrunner.test");
			ejbModule.deploySessionBean("test", new TestSessionBean());
			fail();
		}
		catch(RuntimeException exc)
		{
			//should throw exception
		}
		try
		{
			ejbModule.setInterfacePackage("com.mockrunner.test.ejb");
			ejbModule.setHomeInterfaceSuffix("xyz");
			ejbModule.deploySessionBean("test", new TestSessionBean());
			fail();
		}
		catch(RuntimeException exc)
		{
			//should throw exception
		}
		ejbModule.setHomeInterfaceSuffix("Home");
		ejbModule.setImplementationSuffix("Bean");
		ejbModule.deploySessionBean("test", new TestSessionBean());
		InitialContext context = new InitialContext();
		Object home = context.lookup("test");
		assertNotNull(home);
		assertTrue(home instanceof TestSessionHome);
	}
    
    @Test
	public void testDeployEntityBean() throws Exception
	{
		try
		{
			ejbModule.deployEntityBean("test", String.class);
			fail();
		}
		catch(RuntimeException exc)
		{
			//should throw exception
		}
		try
		{
			ejbModule.setHomeInterfacePackage("com.mockrunner");
			ejbModule.deployEntityBean("test", TestEntityEJB.class);
			fail();
		}
		catch(RuntimeException exc)
		{
			//should throw exception
		}
		try
		{
			ejbModule.deployEntityBean("test", TestEntityEJB.class);
			fail();
		}
		catch(RuntimeException exc)
		{
			//should throw exception
		}
		try
		{
			ejbModule.setBusinessInterfaceSuffix("");
			ejbModule.setImplementationSuffix("EJB");
			ejbModule.deployEntityBean("test", TestEntityEJB.class);
			fail();
		}
		catch(RuntimeException exc)
		{
			//should throw exception
		}
		ejbModule.setImplementationSuffix("EJB");
		ejbModule.setBusinessInterfaceSuffix("Bean");
		ejbModule.setHomeInterfacePackage("com.mockrunner.test.ejb");
		ejbModule.deployEntityBean("test", TestEntityEJB.class);
		InitialContext context = new InitialContext();
		Object home = context.lookup("test");
		assertNotNull(home);
		assertTrue(home instanceof TestEntityHome);
	}
	
	/*
	public void testDeployMessageBeanBoundToContext() throws Exception
	{
		MockQueueConnectionFactory queueFactory = jmsMockFactory.getMockQueueConnectionFactory();
		MockQueue queue = jmsMockFactory.getDestinationManager().createQueue("queue");
		ejbModule.deployMessageBean("factoryJNDIQueueFactory", "destinationJNDIQueue", queueFactory, queue, new TestMessageBean());
		assertSame(queueFactory, ejbModule.lookup("factoryJNDIQueueFactory"));
		assertSame(queue, ejbModule.lookup("destinationJNDIQueue"));
		MockTopicConnectionFactory topicFactory = jmsMockFactory.getMockTopicConnectionFactory();
		MockTopic topic = jmsMockFactory.getDestinationManager().createTopic("topic");
		ejbModule.deployMessageBean("factoryJNDITopicFactory", "destinationJNDITopic", topicFactory, topic, new TestMessageBean());
		assertSame(topicFactory, ejbModule.lookup("factoryJNDITopicFactory"));
		assertSame(topic, ejbModule.lookup("destinationJNDITopic"));
	}
	*/
	
	/*
	public void testDeployMessageBeanQueueConnectionCreated() throws Exception
	{
		MockQueueConnectionFactory queueFactory = jmsMockFactory.getMockQueueConnectionFactory();
		MockQueue queue = jmsMockFactory.getDestinationManager().createQueue("queue");
		Object messageBean = new TestMessageBean();
		ejbModule.deployMessageBean("factoryJNDIQueueFactory", "destinationJNDIQueue", queueFactory, queue, messageBean);
		assertNotNull(queueFactory.getConnection(0));
		assertTrue(queueFactory.getConnection(0) instanceof MockQueueConnection);
		MockQueueConnection queueConnection = queueFactory.getLatestQueueConnection();
		assertNotNull(queueConnection.getQueueSession(0));
		assertTrue(queueConnection.getQueueSession(0) instanceof MockQueueSession);
		MockQueueSession queueSession = (MockQueueSession)queueConnection.getQueueSession(0);
		MockQueueReceiver queueReceiver = queueSession.getQueueTransmissionManager().getQueueReceiver("queue");
		assertNotNull(queueReceiver);
		assertNotNull(queueReceiver.getMessageListener());
	}
	*/
	
	/*
	public void testDeployMessageBeanTopicConnectionCreated() throws Exception
	{
		MockTopicConnectionFactory topicFactory = jmsMockFactory.getMockTopicConnectionFactory();
		MockTopic topic = jmsMockFactory.getDestinationManager().createTopic("topic");
		Object messageBean = new TestMessageBean();
		ejbModule.deployMessageBean("factoryJNDITopicFactory", "destinationJNDITopic", topicFactory, topic, messageBean);
		assertNotNull(topicFactory.getConnection(0));
		assertTrue(topicFactory.getConnection(0) instanceof MockTopicConnection);
		MockTopicConnection topicConnection = topicFactory.getLatestTopicConnection();
		assertNotNull(topicConnection.getTopicSession(0));
		assertTrue(topicConnection.getTopicSession(0) instanceof MockTopicSession);
		MockTopicSession topicSession = (MockTopicSession)topicConnection.getTopicSession(0);
		MockTopicSubscriber topicSubscriber = topicSession.getTopicTransmissionManager().getTopicSubscriber(0);
		assertNotNull(topicSubscriber);
		assertNotNull(topicSubscriber.getMessageListener());
	}
	*/
	
	/*
	public void testDeployMessageBeanSendMessage() throws Exception
	{
		MockQueueConnectionFactory queueFactory = jmsMockFactory.getMockQueueConnectionFactory();
		MockQueue queue = jmsMockFactory.getDestinationManager().createQueue("queue");
		TestMessageBean messageBean = new TestMessageBean();
		ejbModule.deployMessageBean("factoryJNDIQueueFactory", "destinationJNDIQueue", queueFactory, queue, messageBean);
		QueueSession session = queueFactory.createQueueConnection().createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		QueueSender sender = session.createSender(queue);
		MockTextMessage message = new MockTextMessage("message");
		sender.send(message);
		assertSame(message, messageBean.getMessage());
	}
	*/
		
    @Test
    public void testTransaction() throws Exception
    {
        ejbModule.deploySessionBean("mybean", new TestSessionBean(), TransactionPolicy.REQUIRED);
        InitialContext context = new InitialContext();
        Object home = context.lookup("mybean");
        TestSessionHome testHome = (TestSessionHome)PortableRemoteObject.narrow(home, TestSessionHome.class);
        TestSession testBean = testHome.create();
        testBean.test(false);
        ejbModule.verifyCommitted();
        ejbModule.verifyNotMarkedForRollback();
        ejbModule.verifyNotRolledBack();
        ejbModule.resetUserTransaction();
        testBean.test(true);
        ejbModule.verifyNotCommitted();
        ejbModule.verifyMarkedForRollback();
        ejbModule.verifyRolledBack();
		ejbModule.setBusinessInterfaceSuffix("Bean");
		ejbModule.setImplementationSuffix("EJB");
		ejbModule.deployEntityBean("myEntityBean", TestEntityEJB.class, TransactionPolicy.REQUIRED);
		ejbModule.resetUserTransaction();
		home = context.lookup("myEntityBean");
		TestEntityHome testEntityHome = (TestEntityHome)PortableRemoteObject.narrow(home, TestEntityHome.class);
		TestEntityBean testEntity = testEntityHome.create();
		testEntity.setName("aName");
		ejbModule.verifyCommitted();
		ejbModule.verifyNotMarkedForRollback();
		ejbModule.verifyNotRolledBack();
    }
    
    @Test
    public void testNoTransactionPolicy() throws Exception
    {
        ejbModule.deploySessionBean("mybean", new TestSessionBean(), null);
        TestSession testBean = (TestSession)ejbModule.createBean("mybean");
        testBean.test(false);
        MockUserTransaction transaction = ejbMockFactory.getMockUserTransaction();
        assertFalse(transaction.wasBeginCalled());
        ejbModule.verifyNotCommitted();
        ejbModule.verifyNotMarkedForRollback();
        ejbModule.verifyNotRolledBack();
        ejbModule.resetUserTransaction();
        testBean.testBMT(false);
        assertTrue(transaction.wasBeginCalled());
        ejbModule.verifyCommitted();
        ejbModule.verifyNotMarkedForRollback();
        ejbModule.verifyNotRolledBack();
        ejbModule.resetUserTransaction();
        testBean.testBMT(true);
        assertTrue(transaction.wasBeginCalled());
        ejbModule.verifyNotCommitted();
        ejbModule.verifyNotMarkedForRollback();
        ejbModule.verifyRolledBack();
    }
    
    /*
	public void testTransactionMessageBean() throws Exception
	{
		MockTopicConnectionFactory topicFactory = jmsMockFactory.getMockTopicConnectionFactory();
		MockTopic topic = jmsMockFactory.getDestinationManager().createTopic("topic");
		Object messageBean = new TestMessageBean();
		ejbModule.deployMessageBean("factoryJNDITopicFactory", "destinationJNDITopic", topicFactory, topic, messageBean, TransactionPolicy.REQUIRED);
		TopicSession session = topicFactory.createTopicConnection().createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		TopicPublisher publisher = session.createPublisher(topic);
		MockTextMessage message = new MockTextMessage("message");
		publisher.publish(message);
		ejbModule.verifyCommitted();
		ejbModule.verifyNotMarkedForRollback();
		ejbModule.verifyNotRolledBack();
		ejbModule.resetUserTransaction();
		message = new MockTextMessage("doRollback");
		publisher.publish(message);
		ejbModule.verifyNotCommitted();
		ejbModule.verifyMarkedForRollback();
		ejbModule.verifyRolledBack();
	}
	*/
    
    public static class TestSessionBean implements SessionBean
    {
        private SessionContext sessionContext;
        
        public void test(boolean setRollbackOnly)
        {
            if(setRollbackOnly) sessionContext.setRollbackOnly();
        }
        
        public void testBMT(boolean rollback)
        {
            try
            {
                sessionContext.getUserTransaction().begin();
                if(rollback)
                {
                    sessionContext.getUserTransaction().rollback();
                }
                else
                {
                    sessionContext.getUserTransaction().commit();
                }
            } 
            catch(Exception exc)
            {
                throw new RuntimeException(exc);
            }
        }
        
        public void ejbCreate() throws CreateException
        {
    
        }
        
        public void ejbCreate(Integer testInt) throws CreateException
        {

        }
        
        public void ejbCreate(int testInt, Boolean testBoolean) throws CreateException
        {

        }
        
        public void ejbCreateWithSuffix(int testInt, Boolean testBoolean) throws CreateException
        {

        }
        
        public void ejbActivate() throws EJBException, RemoteException
        {

        }

        public void ejbPassivate() throws EJBException, RemoteException
        {

        }

        public void ejbRemove() throws EJBException, RemoteException
        {

        }

        public void setSessionContext(SessionContext context) throws EJBException, RemoteException
        {
            sessionContext = context;
        }
    }
    
    public interface TestSession extends javax.ejb.EJBObject
    {
        void test(boolean setRollbackOnly) throws RemoteException;
        
        void testBMT(boolean rollback) throws RemoteException;
    }
    
    public interface TestSessionHome extends javax.ejb.EJBHome
    {
        TestSession create() throws CreateException, RemoteException;
        
        TestSession create(Integer testInt) throws CreateException, RemoteException;
        
        TestSession create(int testInt, Boolean testBoolean) throws CreateException, RemoteException;
    
        TestSession createWithSuffix(int testInt, Boolean testBoolean) throws CreateException, RemoteException;
    }
    
	public static abstract class TestEntityEJB implements EntityBean
	{
		public abstract String getName();
		public abstract void setName(String name);
    
		public String ejbCreate() throws CreateException
		{
			return "testPk";
		}
		
		public void ejbPostCreate() throws CreateException
		{

		}
        
        public String ejbCreate(Short param) throws CreateException
        {
            return "testPk";
        }
        
        public void ejbPostCreate(Short param) throws CreateException
        {

        }
		
		public String ejbCreateWithName(String name) throws CreateException
		{
			return name;
		}
		
		public void ejbPostCreateWithName(String name) throws CreateException
		{

		}
    
		public void ejbActivate() throws EJBException, RemoteException
		{

		}

		public void ejbPassivate() throws EJBException, RemoteException
		{

		}

		public void ejbRemove() throws EJBException, RemoteException
		{

		}

		public void setEntityContext(EntityContext context) throws EJBException, RemoteException
		{
			
		}
		
		public void unsetEntityContext() throws EJBException, RemoteException
		{
			
		}
		
		public void ejbLoad() throws EJBException, RemoteException
		{

		}

		public void ejbStore() throws EJBException, RemoteException
		{

		}
	}

	public interface TestEntityBean extends javax.ejb.EJBObject
	{
		String getName() throws RemoteException;
		void setName(String name) throws RemoteException;
	}
    
    public interface SuperEntityHome extends javax.ejb.EJBHome
    {
        
    }

	public interface TestEntityHome extends SuperEntityHome
	{
		TestEntityBean create() throws CreateException, RemoteException;
        TestEntityBean create(Short param) throws CreateException, RemoteException;
		TestEntityBean createWithName(String name) throws CreateException, RemoteException;
        TestEntityBean findByPrimaryKey(String pk) throws FinderException, RemoteException;
    }
	
	public static class TestMessageBean implements MessageDrivenBean, MessageListener
	{
		private MessageDrivenContext messageContext;
		private Message message;
		
		public void onMessage(Message message)
		{
			try
			{
				if((message instanceof TextMessage) && (((TextMessage)message).getText().equals("doRollback")))
				{
					messageContext.setRollbackOnly();
				}
			}
			catch(JMSException exc)
			{
				throw new RuntimeException(exc.getMessage());
			}
			this.message = message;
		}
		
		public Message getMessage()
		{
			return message;
		}
		
		public void ejbCreate()
		{
	    
		}
	   
		public void ejbRemove()
		{
	    
		}

		public void setMessageDrivenContext(MessageDrivenContext context) throws EJBException
		{
			messageContext = context;
		}
	}
}
