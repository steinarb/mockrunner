package com.mockrunner.test.jms;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import junit.framework.TestCase;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.mock.jms.JMSMockObjectFactory;
import com.mockrunner.mock.jms.MockBytesMessage;
import com.mockrunner.mock.jms.MockMapMessage;
import com.mockrunner.mock.jms.MockMessage;
import com.mockrunner.mock.jms.MockObjectMessage;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueBrowser;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockQueueSession;
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
    
    protected void setUp() throws Exception
    {
        super.setUp();
        mockFactory = new JMSMockObjectFactory();
        module = new JMSTestModule(mockFactory);
        queueConnection = (MockQueueConnection)mockFactory.getMockQueueConnectionFactory().createQueueConnection();
        topicConnection = (MockTopicConnection)mockFactory.getMockTopicConnectionFactory().createTopicConnection();
    }
    
    public void testSetAndGetCurrentConnection() throws Exception
    {
        assertEquals(queueConnection, module.getCurrentQueueConnection());
        assertEquals(topicConnection, module.getCurrentTopicConnection());
        module.setCurrentQueueConnectionIndex(1);
        module.setCurrentTopicConnectionIndex(1);
        assertNull(module.getCurrentQueueConnection());
        assertNull(module.getCurrentTopicConnection());
        MockQueueConnection queueConnection1 = (MockQueueConnection)mockFactory.getMockQueueConnectionFactory().createQueueConnection();
        module.setCurrentQueueConnectionIndex(-1);
        assertEquals(queueConnection1, module.getCurrentQueueConnection());
        assertNull(module.getCurrentTopicConnection());
        MockTopicConnection topicConnection1 = (MockTopicConnection)mockFactory.getMockTopicConnectionFactory().createTopicConnection();
        module.setCurrentTopicConnectionIndex(-2);
        assertEquals(topicConnection1, module.getCurrentTopicConnection());
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
        TopicSession topicSession = topicConnection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        assertEquals(topicSession, module.getTopicSession(0));
        module.setCurrentTopicConnectionIndex(1);
        assertNull(module.getTopicSession(0));
        QueueSession queueSession0 = queueConnection1.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        QueueSession queueSession1 = queueConnection1.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        assertEquals(queueSession0, module.getQueueSession(0));
        assertEquals(queueSession1, module.getQueueSession(1));
        module.setCurrentQueueConnectionIndex(0);
        assertNull(module.getQueueSession(0));
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
        module.getTopicSession(2);
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
    
    public void testVerifyQueueClosed() throws Exception
    {
        MockQueueSession session1 = (MockQueueSession)queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockQueueSession session2 = (MockQueueSession)queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        MockQueueSession session3 = (MockQueueSession)queueConnection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getDestinationManager();
        Queue queue = manager.createQueue("queue");
        MockQueueSender sender1 = (MockQueueSender)session1.createSender(queue);
        MockQueueSender sender2 = (MockQueueSender)session1.createSender(queue);
        MockQueueSender sender3 = (MockQueueSender)session2.createSender(queue);
        MockQueueReceiver receiver1 = (MockQueueReceiver)session3.createReceiver(queue);
        MockQueueReceiver receiver2 = (MockQueueReceiver)session3.createReceiver(queue);
        MockQueueBrowser browser1 = (MockQueueBrowser)session2.createBrowser(queue);
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
        MockTopicPublisher publisher2 = (MockTopicPublisher)session2.createPublisher(topic);
        MockTopicSubscriber subscriber1 = (MockTopicSubscriber)session3.createSubscriber(topic);
        MockTopicSubscriber subscriber2 = (MockTopicSubscriber)session3.createSubscriber(topic);
        MockTopicSubscriber durableSubscriber = (MockTopicSubscriber)session3.createDurableSubscriber(topic, "myDurable");
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
