package com.mockrunner.test.jms;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import junit.framework.TestCase;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.jms.GenericTransmissionManager;
import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.jms.QueueTransmissionManager;
import com.mockrunner.jms.TopicTransmissionManager;
import com.mockrunner.jms.TransmissionManagerWrapper;
import com.mockrunner.mock.jms.JMSMockObjectFactory;
import com.mockrunner.mock.jms.MockBytesMessage;
import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockConnectionFactory;
import com.mockrunner.mock.jms.MockMapMessage;
import com.mockrunner.mock.jms.MockMessage;
import com.mockrunner.mock.jms.MockMessageConsumer;
import com.mockrunner.mock.jms.MockMessageProducer;
import com.mockrunner.mock.jms.MockObjectMessage;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockQueueSession;
import com.mockrunner.mock.jms.MockSession;
import com.mockrunner.mock.jms.MockTemporaryQueue;
import com.mockrunner.mock.jms.MockTemporaryTopic;
import com.mockrunner.mock.jms.MockTextMessage;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicConnection;
import com.mockrunner.mock.jms.MockTopicPublisher;
import com.mockrunner.mock.jms.MockTopicSession;
import com.mockrunner.mock.jms.MockTopicSubscriber;

public class JMSTestModuleTest extends TestCase
{
    private JMSMockObjectFactory mockFactory;
    private JMSTestModule module;
    private MockQueueConnection queueConnection;
    private MockTopicConnection topicConnection;
    private MockConnection connection;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        mockFactory = new JMSMockObjectFactory();
        module = new JMSTestModule(mockFactory);
        queueConnection = (MockQueueConnection)mockFactory.getMockQueueConnectionFactory().createQueueConnection();
        topicConnection = (MockTopicConnection)mockFactory.getMockTopicConnectionFactory().createTopicConnection();
        connection = (MockConnection)mockFactory.getMockConnectionFactory().createConnection();
    }
    
    public void testSetAndGetCurrentQueueConnection() throws Exception
    {
        assertEquals(queueConnection, module.getCurrentQueueConnection());
        module.setCurrentQueueConnectionIndex(1);
        assertNull(module.getCurrentQueueConnection());
        MockQueueConnection queueConnection1 = (MockQueueConnection)mockFactory.getMockQueueConnectionFactory().createQueueConnection();
        module.setCurrentQueueConnectionIndex(-1);
        assertEquals(queueConnection1, module.getCurrentQueueConnection());
        queueConnection1.close();
        module.verifyQueueConnectionClosed();
        module.setCurrentQueueConnectionIndex(0);
        try
        {
            module.verifyQueueConnectionClosed();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        queueConnection1.start();
        module.verifyQueueConnectionStopped();
        module.setCurrentQueueConnectionIndex(1);
        try
        {
            module.verifyQueueConnectionStopped();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        QueueSession queueSession0 = queueConnection1.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueSession queueSession1 = queueConnection1.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        assertEquals(queueSession0, module.getQueueSession(0));
        assertEquals(queueSession1, module.getQueueSession(1));
        module.setCurrentQueueConnectionIndex(0);
        assertNull(module.getQueueSession(0));
    }
    
    public void testSetAndGetCurrentTopicConnection() throws Exception
    {
        assertEquals(topicConnection, module.getCurrentTopicConnection());
        module.setCurrentTopicConnectionIndex(1);
        assertNull(module.getCurrentTopicConnection());
        MockTopicConnection topicConnection1 = (MockTopicConnection)mockFactory.getMockTopicConnectionFactory().createTopicConnection();
        module.setCurrentTopicConnectionIndex(-2);
        assertEquals(topicConnection1, module.getCurrentTopicConnection());
        topicConnection.close();
        try
        {
            module.verifyTopicConnectionClosed();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setCurrentTopicConnectionIndex(0);
        module.verifyTopicConnectionClosed();
        TopicSession topicSession = topicConnection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals(topicSession, module.getTopicSession(0));
        module.setCurrentTopicConnectionIndex(1);
        assertNull(module.getTopicSession(0));
    }
    
    public void testSetAndGetCurrentConnection() throws Exception
    {
        assertEquals(connection, module.getCurrentConnection());
        module.setCurrentConnectionIndex(1);
        assertNull(module.getCurrentConnection());
        MockConnection connection1 = (MockConnection)mockFactory.getMockConnectionFactory().createConnection();
        module.setCurrentTopicConnectionIndex(-2);
        assertEquals(connection1, module.getCurrentConnection());
        connection.close();
        try
        {
            module.verifyConnectionClosed();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setCurrentConnectionIndex(0);
        module.verifyConnectionClosed();
        Session session1 = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        Session session2 = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        Session session3 = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        assertEquals(session1, module.getSession(0));
        assertEquals(session2, module.getSession(1));
        assertEquals(session3, module.getSession(2));
        module.setCurrentConnectionIndex(1);
        assertNull(module.getTopicSession(0));
    }
    
    public void testRegisterQueueMessageListener() throws Exception
    {
        DestinationManager destManager = mockFactory.getDestinationManager();
        destManager.createQueue("queue");
        TestMessageListener listener = new TestMessageListener(true);
        module.registerTestMessageListenerForQueue("queue", listener);
        QueueConnection currentQueueConnection = module.getCurrentQueueConnection();
        assertFalse(currentQueueConnection == queueConnection);
        QueueTransmissionManager transManager = module.getQueueTransmissionManager(0);
        QueueReceiver receiver = transManager.getQueueReceiver(0);
        assertTrue(listener == receiver.getMessageListener());
        module.verifyQueueConnectionStarted();
        listener = new TestMessageListener(true);
        module.registerTestMessageListenerForQueue(queueConnection, "queue", listener);
        assertTrue(module.getQueueSession(0).getTransacted());
        assertTrue(module.getQueueSession(0).isAutoAcknowledge());
        module.verifyNumberQueueSessions(1);
        module.setCurrentQueueConnectionIndex(0);
        module.verifyNumberQueueSessions(1);
        transManager = module.getQueueTransmissionManager(0);
        receiver = transManager.getQueueReceiver(0);
        assertTrue(listener == receiver.getMessageListener());
        module.verifyQueueConnectionStarted();
        assertTrue(queueConnection.isStarted());
        module.registerTestMessageListenerForQueue(queueConnection, "queue", false, Session.CLIENT_ACKNOWLEDGE, listener);
        assertFalse(module.getQueueSession(1).getTransacted());
        assertFalse(module.getQueueSession(1).isAutoAcknowledge());
        module.registerTestMessageListenerForQueue(queueConnection, "queue", false, Session.CLIENT_ACKNOWLEDGE, "number = 1", listener);
        QueueTransmissionManager queueManager = module.getQueueSession(2).getQueueTransmissionManager();
        MockQueueReceiver queueReceiver = queueManager.getQueueReceiver(0);
        assertEquals("number = 1", queueReceiver.getMessageSelector());
    }
    
    public void testRegisterTopicMessageListener() throws Exception
    {
        DestinationManager destManager = mockFactory.getDestinationManager();
        destManager.createTopic("topic");
        TestMessageListener listener = new TestMessageListener(true);
        module.registerTestMessageListenerForTopic("topic", listener);
        TopicConnection currentTopicConnection = module.getCurrentTopicConnection();
        assertFalse(currentTopicConnection == topicConnection);
        TopicTransmissionManager transManager = module.getTopicTransmissionManager(0);
        TopicSubscriber subscriber = transManager.getTopicSubscriber(0);
        assertTrue(listener == subscriber.getMessageListener());
        module.verifyTopicConnectionStarted();
        module.registerTestMessageListenerForTopic((MockTopicConnection)currentTopicConnection, "topic", listener);
        assertTrue(module.getTopicSession(0).getTransacted());
        assertTrue(module.getTopicSession(0).isAutoAcknowledge());
        module.verifyNumberTopicSessions(2);
        transManager = module.getTopicTransmissionManager(1);
        subscriber = transManager.getTopicSubscriber(0);
        assertTrue(listener == subscriber.getMessageListener());
        module.verifyTopicConnectionStarted();
        assertFalse(topicConnection.isStarted());
        module.registerTestMessageListenerForTopic(topicConnection, "topic", listener);
        module.verifyNumberTopicSessions(2);
        module.setCurrentTopicConnectionIndex(0);
        module.verifyNumberTopicSessions(1);
        transManager = module.getTopicTransmissionManager(0);
        subscriber = transManager.getTopicSubscriber(0);
        assertTrue(listener == subscriber.getMessageListener());
        module.registerTestMessageListenerForTopic(topicConnection, "topic", false, Session.DUPS_OK_ACKNOWLEDGE, listener);
        assertFalse(module.getTopicSession(1).getTransacted());
        assertTrue(module.getTopicSession(1).isAutoAcknowledge());
        module.registerTestMessageListenerForTopic(topicConnection, "topic", false, Session.CLIENT_ACKNOWLEDGE, "number = 2", listener);
        TopicTransmissionManager topicManager = module.getTopicSession(2).getTopicTransmissionManager();
        MockTopicSubscriber topicSubscriber = topicManager.getTopicSubscriber(0);
        assertEquals("number = 2", topicSubscriber.getMessageSelector());
    }
    
    public void testRegisterMessageListener() throws Exception
    {
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createQueue("queue");
        TestMessageListener listener = new TestMessageListener(true);
        module.registerTestMessageListenerForQueue(connection, "queue", false, Session.CLIENT_ACKNOWLEDGE, listener);
        assertEquals(0, module.getQueueSessionList().size());
        assertEquals(0, module.getTopicSessionList().size());
        assertEquals(1, module.getSessionList().size());
        module.registerTestMessageListenerForQueue(topicConnection, "queue", false, Session.CLIENT_ACKNOWLEDGE, listener);
        assertEquals(0, module.getQueueSessionList().size());
        assertEquals(1, module.getTopicSessionList().size());
        assertEquals(1, module.getSessionList().size());
        assertFalse(module.getSession(0) instanceof TopicSession);
        assertFalse(module.getSession(0) instanceof QueueSession);
    }
    
    
    public void testGetQueue() throws Exception
    {
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createQueue("test1");
        manager.createQueue("test2");
        assertNotNull(module.getQueue("test1"));
        assertNotNull(module.getQueue("test2"));
        assertNull(module.getQueue("xyz"));
        QueueSession session = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        session.createQueue("test2");
        manager.removeQueue("test2");
        assertNull(module.getQueue("test2"));
        try
        {
            session.createQueue("test2");
            fail();
        }
        catch (JMSException e)
        {
            //should throw exception
        }
    }
    
    public void testGetTopic() throws Exception
    {
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createTopic("myTopic1");
        manager.createTopic("myTopic2");
        assertNotNull(module.getTopic("myTopic1"));
        assertNotNull(module.getTopic("myTopic2"));
        assertNull(module.getTopic("xyz"));
        TopicSession session = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        session.createTopic("myTopic1");
        manager.removeTopic("myTopic1");
        assertNull(module.getTopic("myTopic1"));
        try
        {
            session.createTopic("myTopic1");
            fail();
        }
        catch (JMSException e)
        {
            //should throw exception
        }
    }
    
    public void testGetQueueAndTopicDifferentSessions() throws Exception
    {
        DestinationManager manager = mockFactory.getDestinationManager();
        Queue queue1 = manager.createQueue("queue1");
        Queue queue2 = manager.createQueue("queue2");
        Queue queue3 = manager.createQueue("queue3");
        Topic topic1 = manager.createTopic("topic1");
        Topic topic2 = manager.createTopic("topic2");
        MockSession session1 = (MockSession)topicConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MockSession session2 = (MockSession)queueConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MockSession session3 = (MockSession)connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        assertSame(topic1, session1.createTopic("topic1"));
        assertSame(topic2, session2.createTopic("topic2"));
        assertSame(queue2, session3.createQueue("queue2"));
        assertSame(queue1, session2.createQueue("queue1"));
        assertSame(queue3, session1.createQueue("queue3"));
        try
        {
            session1.createQueue("queue4");
            fail();
        }
        catch (JMSException e)
        {
            //should throw exception
        }
        try
        {
            session3.createTopic("topic3");
            fail();
        }
        catch (JMSException e)
        {
            //should throw exception
        }
    }
    
    public void testVerifyTemporaryQueue() throws Exception
    {
        queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockTemporaryQueue queue1 = (MockTemporaryQueue)module.getQueueSession(0).createTemporaryQueue();
        MockTemporaryQueue queue2 = (MockTemporaryQueue)module.getQueueSession(0).createTemporaryQueue();
        assertNotNull(module.getTemporaryQueue(0, 0));
        assertNotNull(module.getTemporaryQueue(0, 1));
        assertNull(module.getTemporaryQueue(0, 2));
        module.verifyNumberTemporaryQueues(0, 2);
        try
        {
            module.verifyNumberTemporaryQueues(0, 3);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberTemporaryQueues(1, 3);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyTemporaryQueueDeleted(0, 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllTemporaryQueuesDeleted(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        queue1.delete();
        module.verifyTemporaryQueueDeleted(0, 0);
        try
        {
            module.verifyAllTemporaryQueuesDeleted(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        queue2.delete();
        module.verifyTemporaryQueueDeleted(0, 1);
        module.verifyAllTemporaryQueuesDeleted(0);
    }
    
    public void testVerifyTemporaryTopic() throws Exception
    {
        topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockTemporaryTopic topic1 = (MockTemporaryTopic)module.getTopicSession(0).createTemporaryTopic();
        MockTemporaryTopic topic2 = (MockTemporaryTopic)module.getTopicSession(0).createTemporaryTopic();
        assertNotNull(module.getTemporaryTopic(0, 0));
        assertNotNull(module.getTemporaryTopic(0, 1));
        assertNull(module.getTemporaryTopic(0, 2));
        module.verifyNumberTemporaryTopics(0, 2);
        try
        {
            module.verifyNumberTemporaryTopics(0, 3);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberTemporaryTopics(1, 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyTemporaryTopicDeleted(0, 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllTemporaryTopicsDeleted(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        topic2.delete();
        module.verifyTemporaryTopicDeleted(0, 1);
        try
        {
            module.verifyAllTemporaryQueuesDeleted(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        topic1.delete();
        module.verifyTemporaryTopicDeleted(0, 0);
        module.verifyAllTemporaryTopicsDeleted(0);
    }
    
    public void testTemporaryQueueAndTopicDifferentSessions() throws Exception
    {
        queueConnection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        topicConnection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockTemporaryTopic topic1 = (MockTemporaryTopic)module.getQueueSession(0).createTemporaryTopic();
        MockTemporaryTopic topic2 = (MockTemporaryTopic)module.getQueueSession(0).createTemporaryTopic();
        module.verifyNumberTemporaryTopics(0, 0);
        try
        {
            module.verifyNumberTemporaryTopics(0, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        assertEquals(2, module.getQueueSession(0).getTemporaryTopicList().size());
        assertSame(topic1, module.getQueueSession(0).getTemporaryTopic(0));
        assertSame(topic2, module.getQueueSession(0).getTemporaryTopic(1));
        MockTemporaryQueue queue = (MockTemporaryQueue)module.getSession(0).createTemporaryQueue();
        module.verifyNumberTemporaryQueues(0, 0);
        assertEquals(1, module.getSession(0).getTemporaryQueueList().size());
        assertSame(queue, module.getSession(0).getTemporaryQueue(0));
        assertNull(module.getSession(0).getTemporaryQueue(1));
    }
    
    public void testVerifyQueueSender() throws Exception
    {
        queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createQueue("queue");
        module.getQueueSession(0).createSender(manager.getQueue("queue"));
        module.verifyNumberQueueSenders(0, 1);
        try
        {
            module.verifyNumberQueueSenders(0, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberQueueSenders(0, "queue", 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberQueueSenders(1, "queue", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.getQueueSession(0).createSender(manager.getQueue("queue"));
        module.verifyNumberQueueSenders(0, "queue", 2);
        module.verifyNumberQueueSenders(0, 2);
        try
        {
            module.verifyNumberQueueSenders(0, "otherQueue", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        manager.createQueue("otherQueue");
        module.verifyNumberQueueSenders(0, "otherQueue", 0);
        QueueTransmissionManager queueManager = module.getQueueTransmissionManager(0);
        assertEquals(2, queueManager.getQueueSenderList().size());
        assertEquals(2, queueManager.getQueueSenderList("queue").size());
        assertEquals(0, queueManager.getQueueSenderList("otherQueue").size());
    }
    
    public void testVerifyTopicPublishers() throws Exception
    {
        topicConnection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createTopic("topic");
        module.getTopicSession(0).createPublisher(manager.getTopic("topic"));
        module.verifyNumberTopicPublishers(0, 1);
        try
        {
            module.verifyNumberTopicPublishers(0, 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberTopicPublishers(0, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberTopicPublishers(0, "topic", 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberTopicPublishers(1, "topic", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.getTopicSession(0).createPublisher(manager.getTopic("topic"));
        module.verifyNumberTopicPublishers(0, "topic", 2);
        module.verifyNumberTopicPublishers(0, 2);
        try
        {
            module.verifyNumberTopicPublishers(0, "topic", 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberTopicPublishers(0, "myTopic", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        manager.createTopic("myTopic");
        module.verifyNumberTopicPublishers(0, "myTopic", 0);
        TopicTransmissionManager topicManager = module.getTopicTransmissionManager(0);
        assertEquals(2, topicManager.getTopicPublisherList().size());
        assertEquals(2, topicManager.getTopicPublisherList("topic").size());
        assertEquals(0, topicManager.getTopicPublisherList("myTopic").size());
    }
    
    public void testVerifyMessageProducers() throws Exception
    {
        connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        queueConnection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
        topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createQueue("queue");
        manager.createTopic("topic");
        module.getSession(0).createProducer(manager.getQueue("queue"));
        module.getSession(0).createProducer(manager.getTopic("topic"));
        module.getSession(0).createProducer(null);
        module.verifyNumberQueueSenders(0, 0);
        module.verifyNumberTopicPublishers(0, 0);
        module.verifyNumberMessageProducers(0, 3);
        try
        {
            module.verifyNumberMessageProducers(0, 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        TransmissionManagerWrapper manager1 = module.getTransmissionManagerWrapper(0);
        QueueTransmissionManager manager2 = manager1.getQueueTransmissionManager();
        TopicTransmissionManager manager3 = manager1.getTopicTransmissionManager();
        GenericTransmissionManager manager4 = manager1.getGenericTransmissionManager();
        QueueTransmissionManager queueManager = module.getQueueTransmissionManager(0);
        TopicTransmissionManager topicManager = module.getTopicTransmissionManager(0);
        assertEquals(3, manager1.getMessageProducerList().size());
        assertEquals(1, manager2.getQueueSenderList().size());
        assertEquals(1, manager3.getTopicPublisherList().size());
        assertEquals(1, manager4.getMessageProducerList().size());
        assertEquals(0, queueManager.getQueueSenderList().size());
        assertEquals(0, topicManager.getTopicPublisherList().size());
        assertTrue(manager1.getMessageProducer(0) instanceof MockQueueSender);
        assertTrue(manager1.getMessageProducer(1) instanceof MockTopicPublisher);
    }
    
    public void testVerifyQueueReceiver() throws Exception
    {
        queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        Queue queue1 = manager.createQueue("queue");
        Queue queue2 = manager.createQueue("otherQueue");
        module.getQueueSession(0).createReceiver(queue1);
        module.verifyNumberQueueReceivers(0, 1);
        module.verifyNumberQueueReceivers(0, "queue", 1);
        try
        {
            module.verifyNumberQueueReceivers(0, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberQueueReceivers(0, "queue", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyNumberQueueReceivers(0, "otherQueue", 0);
        module.getQueueSession(0).createReceiver(queue2);
        module.verifyNumberQueueReceivers(0, 2);
        module.verifyNumberQueueReceivers(0, "otherQueue", 1);
        try
        {
            module.verifyNumberQueueReceivers(0, "noQueue", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        QueueTransmissionManager queueManager = module.getQueueTransmissionManager(0);
        assertEquals(2, queueManager.getQueueReceiverList().size());
        assertEquals(1, queueManager.getQueueReceiverList("otherQueue").size());
    }
    
    public void testVerifyTopicSubscriber() throws Exception
    {
        topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        Topic topic1 = manager.createTopic("topic1");
        Topic topic2 = manager.createTopic("topic2");
        module.getTopicSession(0).createSubscriber(topic1);
        module.verifyNumberTopicSubscribers(0, 1);
        module.verifyNumberTopicSubscribers(0, "topic1", 1);
        try
        {
            module.verifyNumberTopicSubscribers(0, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberTopicSubscribers(0, 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberTopicSubscribers(0, "topic1", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyNumberTopicSubscribers(0, "topic2", 0);
        module.getTopicSession(0).createSubscriber(topic2);
        module.verifyNumberTopicSubscribers(0, 2);
        module.verifyNumberTopicSubscribers(0, "topic2", 1);
        try
        {
            module.verifyNumberTopicSubscribers(0, "noTopic", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        TopicTransmissionManager topicManager = module.getTopicTransmissionManager(0);
        assertEquals(2, topicManager.getTopicSubscriberList().size());
        assertEquals(1, topicManager.getTopicSubscriberList("topic1").size());
        assertEquals(0, topicManager.getDurableTopicSubscriberMap("topic1").size());
    }
    
    public void testVerifyDurableTopicSubscriber() throws Exception
    {
        topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        module.verifyNumberDurableTopicSubscribers(0, 0);
        try
        {
            module.verifyDurableTopicSubscriberPresent(0, "durableSubscriber");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        DestinationManager manager = mockFactory.getDestinationManager();
        Topic topic1 = manager.createTopic("topic1");
        Topic topic2 = manager.createTopic("topic2");
        module.verifyNumberDurableTopicSubscribers(0, "topic1", 0);
        module.getTopicSession(0).createDurableSubscriber(topic1, "durableSubscriber");
        module.verifyDurableTopicSubscriberPresent(0, "durableSubscriber");
        module.verifyNumberDurableTopicSubscribers(0, 1);
        module.verifyNumberDurableTopicSubscribers(0, "topic1", 1);
        try
        {
            module.verifyNumberDurableTopicSubscribers(0, "anotherDurableSubscriber", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.getTopicSession(0).createDurableSubscriber(topic1, "durableSubscriber");
        module.verifyDurableTopicSubscriberPresent(0, "durableSubscriber");
        module.verifyNumberDurableTopicSubscribers(0, 1);
        module.verifyNumberDurableTopicSubscribers(0, "topic1", 1);
        module.getTopicSession(0).createDurableSubscriber(topic2, "anotherDurableSubscriber");
        module.verifyDurableTopicSubscriberPresent(0, "anotherDurableSubscriber");
        module.verifyNumberDurableTopicSubscribers(0, 2);
        module.verifyNumberDurableTopicSubscribers(0, "topic1", 1);
        module.verifyNumberDurableTopicSubscribers(0, "topic2", 1);
        try
        {
            module.verifyNumberDurableTopicSubscribers(0, "topic1", 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        TopicTransmissionManager topicManager = module.getTopicTransmissionManager(0);
        assertEquals(0, topicManager.getTopicSubscriberList().size());
        assertEquals(2, topicManager.getDurableTopicSubscriberMap().size());
        assertEquals(1, topicManager.getDurableTopicSubscriberMap("topic1").size());
        assertEquals(1, topicManager.getDurableTopicSubscriberMap("topic2").size());
        assertEquals(0, topicManager.getDurableTopicSubscriberMap("topic3").size());
        assertNotNull(topicManager.getDurableTopicSubscriberMap("topic2").get("anotherDurableSubscriber"));
    }
    
    public void testVerifyMessageConsumer() throws Exception
    {
        connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        queueConnection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
        topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createQueue("queue");
        manager.createTopic("topic");
        module.getSession(0).createConsumer(manager.getQueue("queue"));
        module.getSession(0).createConsumer(manager.getTopic("topic"));
        module.getSession(0).createDurableSubscriber(manager.getTopic("topic"), "subscription");
        module.verifyNumberQueueReceivers(0, 0);
        module.verifyNumberTopicSubscribers(0, 0);
        module.verifyNumberDurableTopicSubscribers(0, 0);
        module.verifyNumberMessageConsumers(0, 3);
        try
        {
            module.verifyNumberMessageProducers(0, 4);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        TransmissionManagerWrapper manager1 = module.getTransmissionManagerWrapper(0);
        QueueTransmissionManager manager2 = manager1.getQueueTransmissionManager();
        TopicTransmissionManager manager3 = manager1.getTopicTransmissionManager();
        QueueTransmissionManager queueManager = module.getQueueTransmissionManager(0);
        TopicTransmissionManager topicManager = module.getTopicTransmissionManager(0);
        assertEquals(3, manager1.getMessageConsumerList().size());
        assertEquals(1, manager2.getQueueReceiverList().size());
        assertEquals(1, manager3.getTopicSubscriberList().size());
        assertEquals(1, manager3.getDurableTopicSubscriberMap().size());
        assertEquals(0, queueManager.getQueueReceiverList().size());
        assertEquals(0, topicManager.getTopicSubscriberList().size());
        assertEquals(0, topicManager.getDurableTopicSubscriberMap().size());
        assertTrue(manager1.getMessageConsumer(0) instanceof MockQueueReceiver);
        assertTrue(manager1.getMessageConsumer(1) instanceof MockTopicSubscriber);
    }
        
    public void testVerifyQueueBrowser() throws Exception
    {
        queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createQueue("queue");
        module.getQueueSession(0).createBrowser(manager.getQueue("queue"));
        module.getQueueSession(0).createBrowser(manager.getQueue("queue"));
        module.verifyNumberQueueBrowsers(0, 2);
        module.verifyNumberQueueBrowsers(0, "queue", 2);
        try
        {
            module.verifyNumberQueueBrowsers(0, 3);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberQueueBrowsers(0, "queue", 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberQueueBrowsers(0, "queue", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberQueueBrowsers(0, "otherQueue", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        manager.createQueue("otherQueue");
        module.verifyNumberQueueBrowsers(0, "otherQueue", 0);
    }
    
    public void testVerifyQueueBrowserDifferentSessions() throws Exception
    {
        queueConnection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        topicConnection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createQueue("queue");
        QueueBrowser browser1 = (QueueBrowser)module.getSession(0).createBrowser(manager.getQueue("queue"));
        QueueBrowser browser2 = (QueueBrowser)module.getSession(0).createBrowser(manager.getQueue("queue"));
        module.verifyNumberQueueBrowsers(0, 0);
        module.verifyNumberQueueBrowsers(0, "queue", 0);
        try
        {
            module.verifyNumberQueueBrowsers(0, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        QueueTransmissionManager transManager = module.getTransmissionManagerWrapper(0).getQueueTransmissionManager();
        assertEquals(2, transManager.getQueueBrowserList().size());
        assertSame(browser1, transManager.getQueueBrowser(0));
        assertSame(browser2, transManager.getQueueBrowser(1));
        QueueBrowser browser3 = (QueueBrowser)module.getTopicSession(0).createBrowser(manager.getQueue("queue"));
        module.verifyNumberQueueBrowsers(0, 0);
        transManager = module.getTopicSession(0).getQueueTransmissionManager();
        assertEquals(1, transManager.getQueueBrowserList().size());
        assertSame(browser3, transManager.getQueueBrowser(0));
    }

    public void testVerifyQueueSession() throws Exception
    {
        assertNull(module.getQueueSession(0));
        queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        assertNotNull(module.getQueueSession(0));
        assertNull(module.getQueueSession(1));
        module.verifyNumberQueueSessions(1);
        try
        {
            module.verifyNumberQueueSessions(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        queueConnection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
        assertNotNull(module.getQueueSession(1));
        module.verifyNumberQueueSessions(2);
    }
    
    public void testVerifyTopicSession() throws Exception
    {
        module.verifyNumberTopicSessions(0);
        assertNull(module.getTopicSession(0));
        topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        assertNotNull(module.getTopicSession(0));
        assertNull(module.getTopicSession(1));
        module.verifyNumberTopicSessions(1);
        try
        {
            module.verifyNumberTopicSessions(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberTopicSessions(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        topicConnection.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);
        assertNotNull(module.getTopicSession(1));
        assertNull(module.getTopicSession(2));
    }
    
    public void testVerifySession() throws Exception
    {
        module.verifyNumberSessions(0);
        module.verifyNumberTopicSessions(0);
        module.verifyNumberQueueSessions(0);
        assertNull(module.getSession(0));
        connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        assertNotNull(module.getSession(0));
        assertNull(module.getQueueSession(0));
        assertNull(module.getTopicSession(0));
        try
        {
            module.verifyNumberSessions(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberSessions(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        topicConnection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        assertNotNull(module.getSession(0));
        assertNull(module.getQueueSession(0));
        assertNotNull(module.getTopicSession(0));
        try
        {
            module.verifyNumberQueueSessions(1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        connection.createSession(true, Session.DUPS_OK_ACKNOWLEDGE);
        assertEquals(2, module.getSessionList().size());
        assertEquals(1, module.getTopicSessionList().size());
        assertEquals(0, module.getQueueSessionList().size());
    }
    
    public void testVerifyNumberQueueMessages() throws Exception
    {
        queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createQueue("queue");
        QueueSender sender1 = module.getQueueSession(0).createSender(manager.getQueue("queue"));
        manager.createQueue("otherQueue");
        QueueSender sender2 = module.getQueueSession(0).createSender(manager.getQueue("otherQueue"));
        QueueSender sender3 = module.getQueueSession(0).createSender(module.getQueueSession(0).createTemporaryQueue());
        QueueReceiver receiver = module.getQueueSession(0).createReceiver(manager.getQueue("otherQueue"));
        receiver.setMessageListener(new MessageListener() { public void onMessage(Message message){} });
        sender1.send(module.getQueueSession(0).createTextMessage());
        sender1.send(module.getQueueSession(0).createTextMessage());
        sender2.send(module.getQueueSession(0).createTextMessage());
        sender2.send(module.getQueueSession(0).createObjectMessage());
        sender2.send(module.getQueueSession(0).createMapMessage());
        sender3.send(module.getQueueSession(0).createMapMessage());
        sender3.send(module.getQueueSession(0).createStreamMessage());
        module.verifyNumberOfCreatedQueueTextMessages(0, 3);
        module.verifyNumberOfCreatedQueueObjectMessages(0, 1);
        module.verifyNumberOfCreatedQueueMapMessages(0, 2);
        module.verifyNumberOfCreatedQueueStreamMessages(0, 1);
        module.verifyNumberOfCreatedQueueBytesMessages(0, 0);
        module.verifyNumberOfCreatedQueueMessages(0, 0);
        try
        {
            module.verifyNumberOfCreatedQueueMessages(0, 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberOfCreatedQueueMapMessages(1, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyNumberOfCurrentQueueMessages("queue", 2);
        module.verifyNumberOfReceivedQueueMessages("queue", 2);
        module.verifyNumberOfCurrentQueueMessages("otherQueue", 0);
        module.verifyNumberOfReceivedQueueMessages("otherQueue", 3);
        module.verifyNumberOfCurrentQueueMessages(0, 0, 2);
        module.verifyNumberOfReceivedQueueMessages(0, 0, 2);
        module.verifyNumberTemporaryQueues(0, 1);
        try
        {
            module.verifyNumberOfReceivedQueueMessages("queue", 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberOfCurrentQueueMessages(0, 0, 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    public void testVerifyNumberTopicMessages() throws Exception
    {
        topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createTopic("topic");
        TopicPublisher publisher1 = module.getTopicSession(0).createPublisher(manager.getTopic("topic"));
        manager.createTopic("otherTopic");
        TopicPublisher publisher2 = module.getTopicSession(0).createPublisher(manager.getTopic("otherTopic"));
        TopicPublisher publisher3 = module.getTopicSession(0).createPublisher(module.getTopicSession(0).createTemporaryTopic());
        TopicSubscriber subscriber = module.getTopicSession(0).createSubscriber(manager.getTopic("otherTopic"));
        subscriber.setMessageListener(new MessageListener() { public void onMessage(Message message){} });
        publisher1.publish(module.getTopicSession(0).createTextMessage());
        publisher2.publish(module.getTopicSession(0).createTextMessage());
        publisher2.publish(module.getTopicSession(0).createObjectMessage());
        publisher2.publish(module.getTopicSession(0).createObjectMessage());
        publisher2.publish(module.getTopicSession(0).createMapMessage());
        publisher3.publish(module.getTopicSession(0).createMapMessage());
        publisher3.publish(module.getTopicSession(0).createBytesMessage());
        module.verifyNumberOfCreatedTopicTextMessages(0, 2);
        module.verifyNumberOfCreatedTopicObjectMessages(0, 2);
        module.verifyNumberOfCreatedTopicMapMessages(0, 2);
        module.verifyNumberOfCreatedTopicBytesMessages(0, 1);
        module.verifyNumberOfCreatedTopicStreamMessages(0, 0);
        module.verifyNumberOfCreatedTopicMessages(0, 0);
        try
        {
            module.verifyNumberOfCreatedTopicMessages(0, 3);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberOfCreatedTopicMapMessages(1, 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyNumberOfCurrentTopicMessages("topic", 1);
        module.verifyNumberOfReceivedTopicMessages("topic", 1);
        module.verifyNumberOfCurrentTopicMessages("otherTopic", 0);
        module.verifyNumberOfReceivedTopicMessages("otherTopic", 4);
        module.verifyNumberOfCurrentTopicMessages(0, 0, 2);
        module.verifyNumberOfReceivedTopicMessages(0, 0, 2);
        module.verifyNumberTemporaryTopics(0, 1);
        try
        {
            module.verifyNumberOfReceivedTopicMessages("topic", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberOfCurrentQueueMessages(0, 0, 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    public void testVerifyNumberMessages() throws Exception
    {
        connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createQueue("queue");
        MockMessageProducer producer1 = (MockMessageProducer)module.getSession(0).createProducer(manager.getQueue("queue"));
        manager.createTopic("topic");
        MockMessageProducer producer2 = (MockMessageProducer)module.getSession(0).createProducer(manager.getTopic("topic"));
        MockMessageProducer producer3 = (MockMessageProducer)module.getSession(0).createProducer(module.getSession(0).createTemporaryQueue());
        MessageConsumer consumer = module.getSession(0).createConsumer(manager.getTopic("topic"));
        consumer.setMessageListener(new MessageListener() { public void onMessage(Message message){} });
        producer1.send(module.getSession(0).createTextMessage());
        producer1.send(module.getSession(0).createTextMessage());
        producer2.send(module.getSession(0).createTextMessage());
        producer2.send(module.getSession(0).createObjectMessage());
        producer2.send(module.getSession(0).createMapMessage());
        producer3.send(module.getSession(0).createMapMessage());
        producer3.send(module.getSession(0).createStreamMessage());
        module.verifyNumberOfCreatedTextMessages(0, 3);
        module.verifyNumberOfCreatedObjectMessages(0, 1);
        module.verifyNumberOfCreatedMapMessages(0, 2);
        module.verifyNumberOfCreatedStreamMessages(0, 1);
        module.verifyNumberOfCreatedBytesMessages(0, 0);
        module.verifyNumberOfCreatedMessages(0, 0);
        try
        {
            module.verifyNumberOfCreatedMessages(0, 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberOfCreatedMapMessages(1, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyNumberOfCurrentQueueMessages("queue", 2);
        module.verifyNumberOfReceivedQueueMessages("queue", 2);
        module.verifyNumberOfCurrentTopicMessages("topic", 0);
        module.verifyNumberOfReceivedTopicMessages("topic", 3);
        try
        {
            module.verifyNumberOfCurrentQueueMessages(0, 0, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        assertEquals(2, module.getSession(0).getTemporaryQueue(0).getCurrentMessageList().size());
    }
    
    public void testVerifyNumberMessagesDifferentSessions() throws Exception
    {
        queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createQueue("queue");
        MockMessageProducer queueProducer = (MockMessageProducer)module.getQueueSession(0).createProducer(module.getQueueSession(0).createTemporaryTopic());
        MockMessageProducer topicProducer = (MockMessageProducer)module.getTopicSession(0).createProducer(module.getTopicSession(0).createTemporaryQueue());
        MockMessageProducer producer = (MockMessageProducer)module.getSession(0).createProducer(manager.getQueue("queue"));
        queueProducer.send(new MockTextMessage("testQueue"));
        topicProducer.send(new MockTextMessage("testTopic"));
        producer.send(new MockTextMessage("test"));
        module.verifyNumberOfCurrentQueueMessages("queue", 1);
        module.verifyNumberOfReceivedQueueMessages("queue", 1);
        try
        {
            module.verifyNumberOfCurrentQueueMessages(0, 0, 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberOfCurrentTopicMessages(0, 0, 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        assertEquals(1, module.getQueueSession(0).getTemporaryTopic(0).getCurrentMessageList().size());
        assertEquals(1, module.getTopicSession(0).getTemporaryQueue(0).getCurrentMessageList().size());
        assertEquals(1, module.getQueueSession(0).getTemporaryTopic(0).getReceivedMessageList().size());
        assertEquals(1, module.getTopicSession(0).getTemporaryQueue(0).getReceivedMessageList().size());
    }
    
    public void testVerifyQueueMessageEquals() throws Exception
    {
        queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createQueue("queue");
        QueueSender sender = module.getQueueSession(0).createSender(manager.getQueue("queue"));
        TextMessage message1 = module.getQueueSession(0).createTextMessage();
        message1.setText("text1");
        ObjectMessage message2 = module.getQueueSession(0).createObjectMessage();
        message2.setObject(new Integer(1));
        MapMessage message3 = module.getQueueSession(0).createMapMessage();
        message3.setFloat("float1", 1.2f);
        message3.setString("string1", "teststring");
        sender.send(message1);
        sender.send(message2);
        sender.send(message3);
        module.verifyCurrentQueueMessageEquals("queue", 0, new MockTextMessage("text1"));
        module.verifyCurrentQueueMessageEquals("queue", 1, new MockObjectMessage(new Integer(1)));
        module.verifyReceivedQueueMessageEquals("queue", 0, new MockTextMessage("text1"));
        module.verifyReceivedQueueMessageEquals("queue", 1, new MockObjectMessage(new Integer(1)));
        MockMapMessage testMessage = new MockMapMessage();
        testMessage.setFloat("float1", 1.2f);
        testMessage.setString("string1", "teststring");
        module.verifyCurrentQueueMessageEquals("queue", 2, testMessage);
        module.verifyReceivedQueueMessageEquals("queue", 2, testMessage);
        try
        {
            module.verifyReceivedQueueMessageEquals("queue", 1, new MockTextMessage("text1"));
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyCurrentQueueMessageEquals("queue", 3, testMessage);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        testMessage.setString("string2", "teststring");
        try
        {
            module.verifyCurrentQueueMessageEquals("queue", 2, testMessage);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        QueueReceiver receiver = module.getQueueSession(0).createReceiver(manager.getQueue("queue"));
        receiver.receive();
        testMessage = new MockMapMessage();
        testMessage.setFloat("float1", 1.2f);
        testMessage.setString("string1", "teststring");
        try
        {
            module.verifyCurrentQueueMessageEquals("queue", 0, new MockTextMessage("text1"));
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyCurrentQueueMessageEquals("queue", 0, new MockObjectMessage(new Integer(1)));
        module.verifyCurrentQueueMessageEquals("queue", 1, testMessage);      
        module.verifyReceivedQueueMessageEquals("queue", 0, new MockTextMessage("text1"));
        module.verifyReceivedQueueMessageEquals("queue", 1, new MockObjectMessage(new Integer(1)));
        module.verifyReceivedQueueMessageEquals("queue", 2, testMessage);
        receiver.receive();
        try
        {
            module.verifyCurrentQueueMessageEquals("queue", 0, new MockObjectMessage(new Integer(1)));
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyCurrentQueueMessageEquals("queue", 0, testMessage);      
        module.verifyReceivedQueueMessageEquals("queue", 0, new MockTextMessage("text1"));
        module.verifyReceivedQueueMessageEquals("queue", 1, new MockObjectMessage(new Integer(1)));
        module.verifyReceivedQueueMessageEquals("queue", 2, testMessage);
        TemporaryQueue tempQueue = module.getQueueSession(0).createTemporaryQueue();
        sender = module.getQueueSession(0).createSender(tempQueue);
        sender.send(message1);
        sender.send(message2);
        sender.send(message3);
        module.verifyCurrentQueueMessageEquals(0, 0, 0, new MockTextMessage("text1"));
        module.verifyCurrentQueueMessageEquals(0, 0, 1, new MockObjectMessage(new Integer(1)));
        module.verifyCurrentQueueMessageEquals(0, 0, 2, testMessage);
        module.verifyReceivedQueueMessageEquals(0, 0, 0, new MockTextMessage("text1"));
        module.verifyReceivedQueueMessageEquals(0, 0, 1, new MockObjectMessage(new Integer(1)));
        module.verifyReceivedQueueMessageEquals(0, 0, 2, testMessage);
        try
        {
            module.verifyCurrentQueueMessageEquals(0, 0, 0, new MockTextMessage("text2"));
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        receiver = module.getQueueSession(0).createReceiver(tempQueue);
        receiver.receive();
        try
        {
            module.verifyCurrentQueueMessageEquals(0, 0, 0, new MockTextMessage("text1"));
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyCurrentQueueMessageEquals(0, 0, 0, new MockObjectMessage(new Integer(1)));
        module.verifyCurrentQueueMessageEquals(0, 0, 1, testMessage);
        module.verifyReceivedQueueMessageEquals(0, 0, 0, new MockTextMessage("text1"));
        module.verifyReceivedQueueMessageEquals(0, 0, 1, new MockObjectMessage(new Integer(1)));
        module.verifyReceivedQueueMessageEquals(0, 0, 2, testMessage);
    }
    
    public void testVerifyTopicMessageEquals() throws Exception
    {
        topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createTopic("topic");
        TopicPublisher publisher = module.getTopicSession(0).createPublisher(manager.getTopic("topic"));
        ObjectMessage message1 = module.getTopicSession(0).createObjectMessage();
        message1.setObject("testObject");
        ObjectMessage message2 = module.getTopicSession(0).createObjectMessage();
        message2.setObject(new Integer(1));
        BytesMessage message3 = module.getTopicSession(0).createBytesMessage();
        message3.writeInt(1);
        message3.writeInt(2);
        message3.writeInt(3);
        publisher.publish(message1);
        publisher.publish(message2);
        publisher.publish(message3);
        module.verifyCurrentTopicMessageEquals("topic", 0, new MockObjectMessage("testObject"));
        module.verifyCurrentTopicMessageEquals("topic", 1, new MockObjectMessage(new Integer(1)));
        module.verifyReceivedTopicMessageEquals("topic", 0, new MockObjectMessage("testObject"));
        module.verifyReceivedTopicMessageEquals("topic", 1, new MockObjectMessage(new Integer(1)));
        MockBytesMessage testMessage = new MockBytesMessage();
        testMessage.writeInt(1);
        testMessage.writeInt(2);
        testMessage.writeInt(3);
        module.verifyCurrentTopicMessageEquals("topic", 2, testMessage);
        module.verifyReceivedTopicMessageEquals("topic", 2, testMessage);
        try
        {
            module.verifyReceivedTopicMessageEquals("topic", 1, new MockObjectMessage("testObject"));
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyCurrentTopicMessageEquals("topic", 5, testMessage);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        testMessage.writeInt(4);
        try
        {
            module.verifyReceivedTopicMessageEquals("topic", 2, testMessage);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        TopicSubscriber subscriber = module.getTopicSession(0).createSubscriber(manager.getTopic("topic"));
        subscriber.receive();
        testMessage = new MockBytesMessage();
        testMessage.writeInt(1);
        testMessage.writeInt(2);
        testMessage.writeInt(3);
        try
        {
            module.verifyCurrentTopicMessageEquals("topic", 0, new MockObjectMessage("testObject"));
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyCurrentTopicMessageEquals("topic", 0, new MockObjectMessage(new Integer(1)));
        module.verifyCurrentTopicMessageEquals("topic", 1, testMessage);      
        module.verifyReceivedTopicMessageEquals("topic", 0, new MockObjectMessage("testObject"));
        module.verifyReceivedTopicMessageEquals("topic", 1, new MockObjectMessage(new Integer(1)));
        module.verifyReceivedTopicMessageEquals("topic", 2, testMessage);
        subscriber.receive();
        try
        {
            module.verifyCurrentTopicMessageEquals("topic", 0, new MockObjectMessage(new Integer(1)));
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyCurrentTopicMessageEquals("topic", 0, testMessage);      
        module.verifyReceivedTopicMessageEquals("topic", 0, new MockObjectMessage("testObject"));
        module.verifyReceivedTopicMessageEquals("topic", 1, new MockObjectMessage(new Integer(1)));
        module.verifyReceivedTopicMessageEquals("topic", 2, testMessage);
        TemporaryTopic tempTopic = module.getTopicSession(0).createTemporaryTopic();
        publisher = module.getTopicSession(0).createPublisher(tempTopic);
        publisher.publish(message1);
        publisher.publish(message2);
        publisher.publish(message3);
        module.verifyCurrentTopicMessageEquals(0, 0, 0, new MockObjectMessage("testObject"));
        module.verifyCurrentTopicMessageEquals(0, 0, 1, new MockObjectMessage(new Integer(1)));
        module.verifyCurrentTopicMessageEquals(0, 0, 2, testMessage);
        module.verifyReceivedTopicMessageEquals(0, 0, 0, new MockObjectMessage("testObject"));
        module.verifyReceivedTopicMessageEquals(0, 0, 1, new MockObjectMessage(new Integer(1)));
        module.verifyReceivedTopicMessageEquals(0, 0, 2, testMessage);
        try
        {
            module.verifyCurrentTopicMessageEquals(0, 0, 0, new MockObjectMessage("TestObject"));
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        subscriber = module.getTopicSession(0).createSubscriber(tempTopic);
        subscriber.receive();
        try
        {
            module.verifyCurrentTopicMessageEquals(0, 0, 0, new MockObjectMessage("testObject"));
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyCurrentTopicMessageEquals(0, 0, 0, new MockObjectMessage(new Integer(1)));
        module.verifyCurrentTopicMessageEquals(0, 0, 1, testMessage);
        module.verifyReceivedTopicMessageEquals(0, 0, 0, new MockObjectMessage("testObject"));
        module.verifyReceivedTopicMessageEquals(0, 0, 1, new MockObjectMessage(new Integer(1)));
        module.verifyReceivedTopicMessageEquals(0, 0, 2, testMessage);
        subscriber.receive();
        module.verifyCurrentTopicMessageEquals(0, 0, 0, testMessage);
        module.verifyReceivedTopicMessageEquals(0, 0, 0, new MockObjectMessage("testObject"));
        module.verifyReceivedTopicMessageEquals(0, 0, 1, new MockObjectMessage(new Integer(1)));
        module.verifyReceivedTopicMessageEquals(0, 0, 2, testMessage);
    }
    
    public void testVerifyMessageEqualsDifferentSessions() throws Exception
    {
        queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        manager.createQueue("queue");
        manager.createTopic("topic");
        MockMessageProducer producer1 = (MockMessageProducer)module.getQueueSession(0).createProducer(manager.getQueue("queue"));
        MockMessageProducer producer2 = (MockMessageProducer)module.getTopicSession(0).createProducer(manager.getQueue("queue"));
        MockMessageProducer producer3 = (MockMessageProducer)module.getSession(0).createProducer(manager.getQueue("queue"));
        MockMapMessage mapMessage = new MockMapMessage();
        mapMessage.setInt("prop", 1);
        producer1.send(new MockTextMessage("text"));
        producer2.send(new MockObjectMessage(new Integer(1)));
        producer3.send(mapMessage);
        module.verifyCurrentQueueMessageEquals("queue", 0, new MockTextMessage("text"));
        module.verifyCurrentQueueMessageEquals("queue", 1, new MockObjectMessage(new Integer(1)));
        module.verifyCurrentQueueMessageEquals("queue", 2, mapMessage);
        producer3 = (MockMessageProducer)module.getSession(0).createProducer(manager.getTopic("topic"));
        producer3.send(mapMessage);
        module.verifyCurrentTopicMessageEquals("topic", 0, mapMessage);
        MockTemporaryQueue queue = (MockTemporaryQueue)module.getSession(0).createTemporaryQueue();
        producer3 = (MockMessageProducer)module.getSession(0).createProducer(queue);
        producer3.send(new MockTextMessage("text"));
        try
        {
            module.verifyReceivedQueueMessageEquals(0, 0, 0, new MockTextMessage("text"));
            fail();
        }
        catch(VerifyFailedException e)
        {
            //should throw exception
        }
        assertEquals(1, queue.getReceivedMessageList().size());
    }
    
    public void testVerifyQueueClosed() throws Exception
    {
        MockQueueSession session1 = (MockQueueSession)queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockQueueSession session2 = (MockQueueSession)queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockQueueSession session3 = (MockQueueSession)queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        Queue queue = manager.createQueue("queue");
        MockQueueSender sender1 = (MockQueueSender)session1.createSender(queue);
        session1.createSender(queue);
        session2.createSender(queue);
        MockQueueReceiver receiver1 = (MockQueueReceiver)session3.createReceiver(queue);
        MockQueueReceiver receiver2 = (MockQueueReceiver)session3.createReceiver(queue);
        session2.createBrowser(queue);
        sender1.close();
        receiver1.close();
        receiver2.close();
        module.verifyQueueSenderClosed(0, "queue", 0);
        module.verifyQueueReceiverClosed(2, "queue", 0);
        module.verifyQueueReceiverClosed(2, "queue", 1);
        module.verifyAllQueueReceiversClosed(2);
        module.verifyAllQueueBrowsersClosed(0);
        module.verifyAllQueueSendersClosed(2);
        module.verifyAllQueueReceiversClosed(1);
        try
        {
            module.verifyQueueConnectionClosed();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyQueueSessionClosed(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllQueueSessionsClosed();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyQueueBrowserClosed(1, "queue", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllQueueBrowsersClosed(1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        queueConnection.close();
        module.verifyQueueConnectionClosed();
        module.verifyAllQueueSessionsClosed();  
        module.verifyAllQueueReceiversClosed(2);
        module.verifyAllQueueBrowsersClosed(1);
        module.verifyQueueBrowserClosed(1, "queue", 0);
        module.verifyAllQueueSendersClosed(0);
        module.verifyAllQueueSendersClosed(1);
    }
    
    public void testVerifyTopicClosed() throws Exception
    {
        MockTopicSession session1 = (MockTopicSession)topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockTopicSession session2 = (MockTopicSession)topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockTopicSession session3 = (MockTopicSession)topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        Topic topic = manager.createTopic("topic");
        MockTopicPublisher publisher1 = (MockTopicPublisher)session1.createPublisher(topic);
        session2.createPublisher(topic);
        MockTopicSubscriber subscriber1 = (MockTopicSubscriber)session3.createSubscriber(topic);
        session3.createSubscriber(topic);
        session3.createDurableSubscriber(topic, "myDurable");
        publisher1.close();
        subscriber1.close();
        module.verifyTopicPublisherClosed(0, "topic", 0);
        module.verifyTopicSubscriberClosed(2, "topic", 0);
        module.verifyAllTopicPublishersClosed(2);
        module.verifyAllDurableTopicSubscribersClosed(0);
        try
        {
            module.verifyAllDurableTopicSubscribersClosed(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyTopicConnectionClosed();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyTopicSessionClosed(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllTopicSessionsClosed();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyTopicPublisherClosed(1, "topic", 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllTopicPublishersClosed(1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        topicConnection.close();
        module.verifyTopicConnectionClosed();
        module.verifyAllTopicSessionsClosed();
        module.verifyDurableTopicSubscriberClosed(2, "myDurable");
        module.verifyAllTopicSubscribersClosed(0);
        module.verifyAllTopicSubscribersClosed(1);
        module.verifyAllTopicSubscribersClosed(2);
        module.verifyAllTopicPublishersClosed(0);
        module.verifyAllTopicPublishersClosed(1);
        module.verifyAllTopicPublishersClosed(2);
        module.verifyAllDurableTopicSubscribersClosed(0);
        module.verifyAllDurableTopicSubscribersClosed(1);
        module.verifyAllDurableTopicSubscribersClosed(2);
    }
    
    public void testVerifyMessageProducersAndConsumersClosed() throws Exception
    {
        MockSession session1 = (MockSession)connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockSession session2 = (MockSession)connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockSession session3 = (MockSession)connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        Topic topic = manager.createTopic("topic");
        Queue queue = manager.createQueue("queue");
        MockMessageProducer producer1 = (MockMessageProducer)session1.createProducer(topic);
        session2.createProducer(null);
        MockMessageConsumer consumer1 = (MockMessageConsumer)session3.createConsumer(queue);
        session3.createConsumer(topic);
        session3.createDurableSubscriber(topic, "myDurable");
        producer1.close();
        module.verifyAllMessageProducersClosed(0);
        try
        {
            module.verifyAllMessageProducersClosed(1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyAllMessageConsumersClosed(0);
        consumer1.close();
        try
        {
            module.verifyAllMessageConsumersClosed(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        session1.close();
        session2.close();
        try
        {
            module.verifyAllSessionsClosed();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        queueConnection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        topicConnection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        session3.close();
        module.verifyAllSessionsClosed();
        module.verifyAllMessageProducersClosed(0);
        module.verifyAllMessageProducersClosed(1);
        module.verifyAllMessageProducersClosed(2);
        module.verifyAllMessageConsumersClosed(0);
        module.verifyAllMessageConsumersClosed(1);
        module.verifyAllMessageConsumersClosed(2);
    }
    
    public void testVerifyQueueSessionComitted() throws Exception
    {
        MockQueueSession session1 = (MockQueueSession)queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockQueueSession session2 = (MockQueueSession)queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockQueueSession session3 = (MockQueueSession)queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        session1.commit();
        session2.rollback();
        module.verifyQueueSessionCommitted(0);
        module.verifyQueueSessionNotRecovered(0);
        module.verifyQueueSessionNotRolledBack(0);
        module.verifyQueueSessionNotCommitted(1);
        module.verifyQueueSessionRecovered(1);
        module.verifyQueueSessionRolledBack(1);
        module.verifyQueueSessionNotCommitted(2);
        module.verifyQueueSessionNotRecovered(2);
        module.verifyQueueSessionNotRolledBack(2);
        try
        {
            module.verifyQueueSessionNotCommitted(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyQueueSessionRecovered(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyQueueSessionRolledBack(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllQueueSessionsCommitted();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        session2.commit();
        session3.commit();
        module.verifyQueueSessionCommitted(1);
        module.verifyAllQueueSessionsCommitted();
        try
        {
            module.verifyQueueSessionNotCommitted(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllQueueSessionsRecovered();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        } 
    }
    
    public void testVerifyTopicSessionComitted() throws Exception
    {
        MockTopicSession session1 = (MockTopicSession)topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockTopicSession session2 = (MockTopicSession)topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockTopicSession session3 = (MockTopicSession)topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        session1.commit();
        session2.rollback();
        module.verifyTopicSessionCommitted(0);
        module.verifyTopicSessionNotRecovered(0);
        module.verifyTopicSessionNotRolledBack(0);
        module.verifyTopicSessionNotCommitted(1);
        module.verifyTopicSessionRecovered(1);
        module.verifyTopicSessionRolledBack(1);
        module.verifyTopicSessionNotCommitted(2);
        module.verifyTopicSessionNotRecovered(2);
        module.verifyTopicSessionNotRolledBack(2);
        try
        {
            module.verifyTopicSessionRecovered(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyTopicSessionNotRolledBack(1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyTopicSessionRolledBack(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllTopicSessionsRolledBack();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        session2.commit();
        session3.commit();
        module.verifyTopicSessionCommitted(1);
        module.verifyTopicSessionCommitted(2);
        module.verifyAllTopicSessionsCommitted();
        module.verifyAllQueueSessionsCommitted();
        try
        {
            module.verifyTopicSessionNotCommitted(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllTopicSessionsRecovered();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        } 
    }
    
    public void testVerifySessionComitted() throws Exception
    {
        MockSession session1 = (MockSession)connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockSession session2 = (MockSession)connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockSession session3 = (MockSession)connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        queueConnection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        topicConnection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        session1.commit();
        session2.rollback();
        module.verifySessionCommitted(0);
        module.verifySessionNotRecovered(0);
        module.verifySessionNotRolledBack(0);
        module.verifySessionNotCommitted(1);
        module.verifySessionRecovered(1);
        module.verifySessionRolledBack(1);
        module.verifySessionNotCommitted(2);
        module.verifySessionNotRecovered(2);
        module.verifySessionNotRolledBack(2);
        try
        {
            module.verifySessionNotCommitted(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifySessionRecovered(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifySessionRolledBack(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllSessionsCommitted();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        session2.commit();
        session3.commit();
        module.verifySessionCommitted(1);
        module.verifyAllSessionsCommitted();
        try
        {
            module.verifySessionNotCommitted(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllSessionsRecovered();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllQueueSessionsCommitted();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllTopicSessionsCommitted();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    public void testVerifyNumberCommitsAndRollbacks() throws Exception
    {
        MockSession session1 = (MockSession)queueConnection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockSession session2 = (MockSession)connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockSession session3 = (MockSession)topicConnection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockSession session4 = (MockSession)topicConnection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        session1.commit();
        session1.commit();
        session2.rollback();
        session3.commit();
        session3.rollback();
        session3.rollback();
        session3.rollback();
        session4.commit();
        module.verifyQueueSessionNumberCommits(0, 2);
        module.verifyQueueSessionNumberRollbacks(0, 0);
        module.verifyTopicSessionNumberCommits(0, 1);
        module.verifyTopicSessionNumberRollbacks(0, 3);
        module.verifyTopicSessionNumberCommits(1, 1);
        module.verifyTopicSessionNumberRollbacks(1, 0);
        module.verifySessionNumberCommits(0, 0);
        module.verifySessionNumberRollbacks(0, 1);
        try
        {
            module.verifyQueueSessionNumberCommits(0, 1);
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifySessionNumberCommits(0, 2);
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyTopicSessionNumberRollbacks(1, 3);
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyQueueSessionNumberRollbacks(2, 0);
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    public void testVerifyQueueMessagesAcknowledged() throws Exception
    {
        MockQueueSession session1 = (MockQueueSession)queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockQueueSession session2 = (MockQueueSession)queueConnection.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        Queue queue = manager.createQueue("queue");
        MockQueueSender sender1 = (MockQueueSender)session1.createSender(queue);
        MockQueueSender sender2 = (MockQueueSender)session2.createSender(queue);
        MockQueueReceiver receiver1 = (MockQueueReceiver)session1.createReceiver(queue);
        MockQueueReceiver receiver2 = (MockQueueReceiver)session2.createReceiver(queue);
        MockMessage message1 = (MockMessage)session1.createTextMessage();
        MockMessage message2 = (MockMessage)session1.createMapMessage();
        MockMessage message3 = (MockMessage)session1.createObjectMessage();
        MockMessage message4 = (MockMessage)session2.createBytesMessage();
        MockMessage message5 = (MockMessage)session2.createStreamMessage();
        MockMessage message6 = (MockMessage)session2.createMessage();
        module.verifyCreatedQueueTextMessageNotAcknowledged(0, 0);
        module.verifyCreatedQueueMapMessageNotAcknowledged(0, 0);
        module.verifyCreatedQueueObjectMessageNotAcknowledged(0, 0);
        module.verifyCreatedQueueBytesMessageNotAcknowledged(1, 0);
        module.verifyCreatedQueueStreamMessageNotAcknowledged(1, 0);
        module.verifyCreatedQueueMessageNotAcknowledged(1, 0);
        module.verifyAllReceivedQueueMessagesAcknowledged("queue");
        try
        {
            module.verifyCreatedQueueTextMessageAcknowledged(0, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        try
        {
            module.verifyCreatedQueueMessageAcknowledged(1, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        try
        {
            module.verifyCreatedQueueMessageAcknowledged(1, 2);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        try
        {
            module.verifyCreatedQueueMessageNotAcknowledged(2, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        sender1.send(message1);
        sender1.send(message2);
        sender1.send(message3);
        sender2.send(message4);
        sender2.send(message5);
        sender2.send(message6);
        module.verifyCreatedQueueTextMessageNotAcknowledged(0, 0);
        module.verifyCreatedQueueMapMessageNotAcknowledged(0, 0);
        module.verifyCreatedQueueObjectMessageNotAcknowledged(0, 0);
        module.verifyCreatedQueueBytesMessageNotAcknowledged(1, 0);
        module.verifyCreatedQueueStreamMessageNotAcknowledged(1, 0);
        module.verifyCreatedQueueMessageNotAcknowledged(1, 0);
        try
        {
            module.verifyAllReceivedQueueMessagesAcknowledged("queue");
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        receiver1.receive();
        receiver1.receive();
        receiver1.receive();
        receiver2.receive();
        receiver2.receive();
        receiver2.receive();
        module.verifyCreatedQueueTextMessageNotAcknowledged(0, 0);
        module.verifyCreatedQueueMapMessageNotAcknowledged(0, 0);
        module.verifyCreatedQueueObjectMessageNotAcknowledged(0, 0);
        module.verifyCreatedQueueBytesMessageAcknowledged(1, 0);
        module.verifyCreatedQueueStreamMessageAcknowledged(1, 0);
        module.verifyCreatedQueueMessageAcknowledged(1, 0);
        ((MockQueue)queue).reset();
        message1 = (MockMessage)session1.createObjectMessage();
        message2 = (MockMessage)session1.createMessage();
        receiver1.setMessageListener(new TestMessageListener(true));
        sender1.send(message1);
        sender1.send(message2);
        module.verifyAllReceivedQueueMessagesAcknowledged("queue");
        module.verifyCreatedQueueObjectMessageAcknowledged(0, 1);
        module.verifyCreatedQueueMessageAcknowledged(0, 0);
        message1 = (MockMessage)session2.createTextMessage();
        message2 = (MockMessage)session2.createTextMessage();
        receiver2.setMessageListener(new TestMessageListener(false));
        sender2.send(message1);
        sender2.send(message2);
        module.verifyAllReceivedQueueMessagesAcknowledged("queue");
        module.verifyCreatedQueueTextMessageAcknowledged(1, 1);
        module.verifyCreatedQueueTextMessageAcknowledged(1, 1);
        TemporaryQueue tempQueue = session2.createTemporaryQueue();
        message1 = (MockMessage)session2.createTextMessage();
        MockQueueSender sender3 = (MockQueueSender)session2.createSender(tempQueue);
        MockQueueReceiver receiver3 = (MockQueueReceiver)session2.createReceiver(tempQueue);
        sender3.send(message1);
        module.verifyReceivedQueueMessageNotAcknowledged(1, 0, 0);
        try
        {
            module.verifyReceivedQueueMessageAcknowledged(1, 0, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        try
        {
            module.verifyAllReceivedQueueMessagesAcknowledged(1, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        receiver3.receive();
        module.verifyReceivedQueueMessageAcknowledged(1, 0, 0);
        receiver3.setMessageListener(new TestMessageListener(false));
        message1 = (MockMessage)session2.createTextMessage();
        sender3.send(message1);
        module.verifyReceivedQueueMessageAcknowledged(1, 0, 1);
        module.verifyAllReceivedQueueMessagesAcknowledged(1, 0);
    }
    
    public void testVerifyTopicMessagesAcknowledged() throws Exception
    {
        MockTopicSession session1 = (MockTopicSession)topicConnection.createTopicSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockTopicSession session2 = (MockTopicSession)topicConnection.createTopicSession(true, Session.DUPS_OK_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        Topic topic = manager.createTopic("topic");
        MockTopicPublisher publisher1 = (MockTopicPublisher)session1.createPublisher(topic);
        MockTopicPublisher publisher2 = (MockTopicPublisher)session2.createPublisher(topic);
        MockTopicSubscriber subscriber1 = (MockTopicSubscriber)session1.createSubscriber(topic);
        MockTopicSubscriber subscriber2 = (MockTopicSubscriber)session2.createSubscriber(topic);
        MockMessage message1 = (MockMessage)session1.createTextMessage();
        MockMessage message2 = (MockMessage)session2.createMapMessage();
        MockMessage message3 = (MockMessage)session2.createObjectMessage();
        MockMessage message4 = (MockMessage)session2.createBytesMessage();
        MockMessage message5 = (MockMessage)session2.createStreamMessage();
        MockMessage message6 = (MockMessage)session2.createMessage();
        module.verifyCreatedTopicTextMessageNotAcknowledged(0, 0);
        module.verifyCreatedTopicMapMessageNotAcknowledged(1, 0);
        module.verifyCreatedTopicObjectMessageNotAcknowledged(1, 0);
        module.verifyCreatedTopicBytesMessageNotAcknowledged(1, 0);
        module.verifyCreatedTopicStreamMessageNotAcknowledged(1, 0);
        module.verifyCreatedTopicMessageNotAcknowledged(1, 0);
        module.verifyAllReceivedTopicMessagesAcknowledged("topic");
        try
        {
            module.verifyCreatedTopicTextMessageAcknowledged(1, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        try
        {
            module.verifyCreatedTopicMessageAcknowledged(1, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        try
        {
            module.verifyCreatedTopicMessageAcknowledged(1, 1);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        try
        {
            module.verifyCreatedTopicMessageNotAcknowledged(2, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        publisher1.publish(message1);
        publisher2.publish(message2);
        publisher2.publish(message3);
        publisher2.publish(message4);
        publisher2.publish(message5);
        publisher2.publish(message6);
        module.verifyCreatedTopicTextMessageNotAcknowledged(0, 0);
        module.verifyCreatedTopicMapMessageNotAcknowledged(1, 0);
        module.verifyCreatedTopicObjectMessageNotAcknowledged(1, 0);
        module.verifyCreatedTopicBytesMessageNotAcknowledged(1, 0);
        module.verifyCreatedTopicStreamMessageNotAcknowledged(1, 0);
        module.verifyCreatedTopicMessageNotAcknowledged(1, 0);
        try
        {
            module.verifyAllReceivedTopicMessagesAcknowledged("topic");
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        subscriber1.receive();
        subscriber2.receive();
        subscriber2.receive();
        subscriber2.receive();
        subscriber2.receive();
        module.verifyCreatedTopicTextMessageNotAcknowledged(0, 0);
        module.verifyCreatedTopicMapMessageAcknowledged(1, 0);
        module.verifyCreatedTopicObjectMessageAcknowledged(1, 0);
        module.verifyCreatedTopicBytesMessageAcknowledged(1, 0);
        module.verifyCreatedTopicStreamMessageAcknowledged(1, 0);
        module.verifyCreatedTopicMessageNotAcknowledged(1, 0);
        ((MockTopic)topic).reset();
        message1 = (MockTextMessage)session1.createTextMessage();
        message2 = (MockTextMessage)session1.createTextMessage();
        subscriber1.setMessageListener(new TestMessageListener(true));
        publisher1.publish(message1);
        publisher1.publish(message2);
        module.verifyAllReceivedTopicMessagesAcknowledged("topic");
        module.verifyCreatedTopicTextMessageAcknowledged(0, 1);
        module.verifyCreatedTopicTextMessageAcknowledged(0, 2);
        message1 = (MockMessage)session2.createTextMessage();
        message2 = (MockMessage)session2.createTextMessage();
        subscriber2.setMessageListener(new TestMessageListener(false));
        publisher2.publish(message1);
        publisher2.publish(message2);
        module.verifyAllReceivedTopicMessagesAcknowledged("topic");
        module.verifyCreatedTopicTextMessageAcknowledged(1, 0);
        module.verifyCreatedTopicTextMessageAcknowledged(1, 1);
        TemporaryTopic tempTopic = session2.createTemporaryTopic();
        message1 = (MockMessage)session2.createObjectMessage();
        MockTopicPublisher publisher3 = (MockTopicPublisher)session2.createPublisher(tempTopic);
        MockTopicSubscriber subscriber3 = (MockTopicSubscriber)session2.createSubscriber(tempTopic);
        publisher3.publish(message1);
        module.verifyReceivedTopicMessageNotAcknowledged(1, 0, 0);
        try
        {
            module.verifyReceivedTopicMessageAcknowledged(1, 0, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        try
        {
            module.verifyAllReceivedTopicMessagesAcknowledged(1, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        subscriber3.receive();
        module.verifyReceivedTopicMessageAcknowledged(1, 0, 0);
        subscriber3.setMessageListener(new TestMessageListener(false));
        message1 = (MockMessage)session2.createTextMessage();
        publisher3.publish(message1);
        module.verifyReceivedTopicMessageAcknowledged(1, 0, 1);
        module.verifyAllReceivedTopicMessagesAcknowledged(1, 0);
    }
    
    public void testVerifyMessagesAcknowledged() throws Exception
    {
        MockSession session1 = (MockSession)connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        MockSession session2 = (MockSession)connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockSession session3 = (MockSession)connection.createSession(true, Session.DUPS_OK_ACKNOWLEDGE);
        queueConnection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        topicConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        Queue queue = manager.createQueue("queue");
        Topic topic = manager.createTopic("topic");
        MockMessage message1 = (MockMessage)session1.createTextMessage();
        MockMessage message2 = (MockMessage)session2.createMapMessage();
        MockMessage message3 = (MockMessage)session2.createObjectMessage();
        MockMessage message4 = (MockMessage)session3.createBytesMessage();
        MockMessage message5 = (MockMessage)session3.createStreamMessage();
        MockMessage message6 = (MockMessage)session3.createMessage();
        module.verifyCreatedTextMessageNotAcknowledged(0, 0);
        module.verifyCreatedMapMessageNotAcknowledged(1, 0);
        module.verifyCreatedObjectMessageNotAcknowledged(1, 0);
        module.verifyCreatedBytesMessageNotAcknowledged(2, 0);
        module.verifyCreatedStreamMessageNotAcknowledged(2, 0);
        module.verifyCreatedMessageNotAcknowledged(2, 0);
        module.verifyAllReceivedQueueMessagesAcknowledged("queue");
        module.verifyAllReceivedTopicMessagesAcknowledged("topic");
        try
        {
            module.verifyCreatedTextMessageAcknowledged(1, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        try
        {
            module.verifyCreatedMessageAcknowledged(1, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        try
        {
            module.verifyCreatedMessageAcknowledged(1, 1);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        try
        {
            module.verifyCreatedMessageNotAcknowledged(3, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        MockMessageProducer producer1 = (MockMessageProducer)session1.createProducer(queue);
        MockMessageProducer producer2 = (MockMessageProducer)session2.createProducer(topic);
        MockMessageProducer producer3 = (MockMessageProducer)session3.createProducer(topic);
        MockMessageConsumer consumer1 = (MockMessageConsumer)session1.createConsumer(queue);
        MockMessageConsumer consumer2 = (MockMessageConsumer)session2.createConsumer(topic);
        producer1.send(message1);
        producer1.send(message2);
        producer2.send(message3);
        producer2.send(message4);
        producer3.send(message5);
        producer3.send(message6);
        module.verifyCreatedTextMessageNotAcknowledged(0, 0);
        module.verifyCreatedMapMessageNotAcknowledged(1, 0);
        module.verifyCreatedObjectMessageNotAcknowledged(1, 0);
        module.verifyCreatedBytesMessageNotAcknowledged(2, 0);
        module.verifyCreatedStreamMessageNotAcknowledged(2, 0);
        module.verifyCreatedMessageNotAcknowledged(2, 0);
        try
        {
            module.verifyAllReceivedQueueMessagesAcknowledged("queue");
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        try
        {
            module.verifyAllReceivedTopicMessagesAcknowledged("topic");
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        consumer1.receive();
        consumer1.receive();
        module.verifyAllReceivedQueueMessagesAcknowledged("queue");
        consumer2.receive();
        consumer2.receive();
        consumer2.receive();
        consumer2.receive();
        try
        {
            module.verifyAllReceivedTopicMessagesAcknowledged("topic");
            fail();
        }
        catch (VerifyFailedException exc)
        {
           //should throw exception
        }
        message3.acknowledge();
        message4.acknowledge();
        message5.acknowledge();
        message6.acknowledge();
        module.verifyAllReceivedTopicMessagesAcknowledged("topic");
        module.verifyCreatedTextMessageAcknowledged(0, 0);
        module.verifyCreatedMapMessageAcknowledged(1, 0);
        module.verifyCreatedObjectMessageAcknowledged(1, 0);
        module.verifyCreatedBytesMessageAcknowledged(2, 0);
        module.verifyCreatedStreamMessageAcknowledged(2, 0);
        module.verifyCreatedMessageAcknowledged(2, 0);
    }
    
    public void testGenericFactory() throws Exception
    {
        MockConnectionFactory factory = (MockConnectionFactory)mockFactory.getMockConnectionFactory();
        QueueConnection queueConnection = factory.createQueueConnection();
        TopicConnection topicConnection = factory.createTopicConnection();
        MockConnection connection = (MockConnection)factory.createConnection();
        queueConnection.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
        topicConnection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        topicConnection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        module.verifyNumberSessions(1);
        module.setCurrentConnectionIndex(1);
        module.verifyNumberSessions(1);
        module.setCurrentConnectionIndex(2);
        module.verifyNumberSessions(2);
        module.setCurrentConnectionIndex(3);
        module.verifyNumberSessions(1);
        module.setCurrentConnectionIndex(0);
        module.verifyNumberSessions(0);
    }
    
    public void testVerifyQueueSenderWithGenericProducers() throws Exception
    {
        DestinationManager manager = mockFactory.getDestinationManager();
        Queue queue = manager.createQueue("queue");
        MockQueueSession session = (MockQueueSession)queueConnection.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
        QueueSender sender1 = session.createSender(queue);
        QueueSender sender2 = (QueueSender)session.createProducer(queue);
        QueueSender sender3 = (QueueSender)session.createProducer(null);
        QueueSender sender4 = session.createSender(queue);
        QueueSender sender5 = session.createSender(null);
        module.verifyNumberQueueSenders(0, 5);
        module.verifyNumberQueueSenders(0, "queue", 3);
        sender3.close();
        sender4.close();
        try
        {
            module.verifyAllQueueSendersClosed(0);
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        session.close();
        module.verifyAllQueueSendersClosed(0);
        TransmissionManagerWrapper wrapper = module.getQueueTransmissionManagerWrapper(0);
        assertEquals(5, wrapper.getMessageProducerList().size());
        assertEquals(5, wrapper.getQueueSenderList().size());
        assertTrue(wrapper.getQueueSenderList().contains(sender1));
        assertTrue(wrapper.getQueueSenderList().contains(sender2));
        assertTrue(wrapper.getQueueSenderList().contains(sender3));
        assertTrue(wrapper.getQueueSenderList().contains(sender4));
        assertTrue(wrapper.getQueueSenderList().contains(sender5));
    }
    
    public void testVerifyTopicPublishersWithGenericProducers() throws Exception
    {
        DestinationManager manager = mockFactory.getDestinationManager();
        Topic topic = manager.createTopic("topic");
        MockTopicSession session1 = (MockTopicSession)topicConnection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        MockTopicSession session2 = (MockTopicSession)topicConnection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        TopicPublisher publisher1 = (TopicPublisher)session1.createProducer(topic);
        TopicPublisher publisher2 = session1.createPublisher(null);
        TopicPublisher publisher3 = session2.createPublisher(null);
        TopicPublisher publisher4 = (TopicPublisher)session2.createProducer(null);
        TopicPublisher publisher5 = (TopicPublisher)session2.createProducer(topic);
        module.verifyNumberTopicPublishers(0, 2);
        module.verifyNumberTopicPublishers(1, 3);
        module.verifyNumberTopicPublishers(0, "topic", 1);
        module.verifyNumberTopicPublishers(1, "topic", 1);
        publisher1.close();
        try
        {
            module.verifyAllTopicPublishersClosed(0);
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        publisher2.close();
        module.verifyAllTopicPublishersClosed(0);
        session2.close();
        module.verifyAllTopicPublishersClosed(1);
        TransmissionManagerWrapper wrapper = module.getTopicTransmissionManagerWrapper(1);
        assertEquals(3, wrapper.getMessageProducerList().size());
        assertEquals(3, wrapper.getTopicPublisherList().size());
        assertTrue(wrapper.getTopicPublisherList().contains(publisher3));
        assertTrue(wrapper.getTopicPublisherList().contains(publisher4));
        assertTrue(wrapper.getTopicPublisherList().contains(publisher5));
    }
    
    public void testSessionsClosedNoConnection() throws Exception
    {
        JMSMockObjectFactory mockFactory = new JMSMockObjectFactory();
        JMSTestModule module = new JMSTestModule(mockFactory);
        module.verifyAllQueueSessionsClosed();
        module.verifyAllQueueSessionsCommitted();
        module.verifyAllQueueSessionsRecovered();
        module.verifyAllQueueSessionsRolledBack();
        module.verifyAllTopicSessionsClosed();
        module.verifyAllTopicSessionsCommitted();
        module.verifyAllTopicSessionsRecovered();
        module.verifyAllTopicSessionsRolledBack();
        module.verifyAllSessionsClosed();
        module.verifyAllSessionsCommitted();
        module.verifyAllSessionsRecovered();
        module.verifyAllSessionsRolledBack();
    }
    
    public static class TestMessageListener implements MessageListener
    {
        private Message message;
        private boolean doAcknowledge;
    
        public TestMessageListener(boolean doAcknowledge)
        {
            this.doAcknowledge = doAcknowledge;
        }

        public Message getMessage()
        {
            return message;
        }
    
        public void reset()
        {
            message = null;
        }
    
        public void onMessage(Message message)
        {
            this.message = message;
            try
            {
                if(doAcknowledge) message.acknowledge();
            }
            catch(JMSException exc)
            {
                exc.printStackTrace();
                throw new RuntimeException("Unexpected failure");
            }
        }
    }
}
