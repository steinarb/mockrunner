package com.mockrunner.test.jms;

import java.util.List;

import javax.jms.Session;

import junit.framework.TestCase;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.jms.GenericTransmissionManager;
import com.mockrunner.jms.QueueTransmissionManager;
import com.mockrunner.jms.TopicTransmissionManager;
import com.mockrunner.jms.TransmissionManagerWrapper;
import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockMessageProducer;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueBrowser;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockSession;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicPublisher;
import com.mockrunner.mock.jms.MockTopicSubscriber;

public class TransmissionManagerTest extends TestCase
{
    private MockConnection connection;
    private MockSession session;

    protected void setUp() throws Exception
    {
        super.setUp();
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        connection = new MockConnection(destManager, confManager); 
        session = (MockSession)connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
    }

    public void testQueueTransmissionManagerCreate()
    {
        QueueTransmissionManager manager = session.getQueueTransmissionManager();
        manager.createQueueSender(new MockQueue("Queue1"));
        manager.createQueueSender(new MockQueue("Queue2"));
        manager.createQueueReceiver(new MockQueue("Queue1"), "");
        manager.createQueueBrowser(new MockQueue("Queue1"), "");
        manager.createQueueBrowser(new MockQueue("Queue2"), "");
        manager.createQueueBrowser(new MockQueue("Queue2"), "");
        manager.createQueueBrowser(new MockQueue("Queue2"), "");
        assertEquals(2, manager.getQueueSenderList().size());
        assertEquals(1, manager.getQueueReceiverList().size());
        assertEquals(4, manager.getQueueBrowserList().size());
        assertEquals(1, manager.getQueueSenderList("Queue1").size());
        assertEquals(1, manager.getQueueReceiverList("Queue1").size());
        assertEquals(0, manager.getQueueReceiverList("Queue2").size());
        assertEquals(3, manager.getQueueBrowserList("Queue2").size());
        assertNotNull(manager.getQueueSender(1));
        assertNull(manager.getQueueSender(2));
        assertNotNull(manager.getQueueReceiver(0));
        assertNull(manager.getQueueReceiver(1));
        assertNotNull(manager.getQueueBrowser(0));
        assertNotNull(manager.getQueueBrowser(3));
        assertNull(manager.getQueueBrowser(4));
        assertNotNull(manager.getQueueSender("Queue1"));
        assertNotNull(manager.getQueueReceiver("Queue1"));
        assertNotNull(manager.getQueueBrowser("Queue1"));
        assertNull(manager.getQueueSender("Queue3"));
        assertNull(manager.getQueueReceiver("Queue3"));
        assertNull(manager.getQueueBrowser("Queue3"));
    }

    public void testTopicTransmissionManagerCreate()
    {
        TopicTransmissionManager manager = session.getTopicTransmissionManager();
        manager.createTopicPublisher(new MockTopic("Topic1"));
        manager.createTopicSubscriber(new MockTopic("Topic1"), "", false);
        manager.createTopicSubscriber(new MockTopic("Topic1"), "", true);
        manager.createDurableTopicSubscriber(new MockTopic("Topic1"), "subscription1", "", true);
        manager.createDurableTopicSubscriber(new MockTopic("Topic1"), "subscription2", "", false);
        manager.createDurableTopicSubscriber(new MockTopic("Topic2"), "subscription2", "", true);
        assertEquals(1, manager.getTopicPublisherList().size());
        assertEquals(2, manager.getTopicSubscriberList().size());
        assertEquals(2, manager.getDurableTopicSubscriberMap().size());
        assertEquals(1, manager.getTopicPublisherList("Topic1").size());
        assertEquals(0, manager.getTopicPublisherList("Topic2").size());
        assertEquals(2, manager.getTopicSubscriberList("Topic1").size());
        assertEquals(1, manager.getDurableTopicSubscriberMap("Topic1").size());
        assertEquals(1, manager.getDurableTopicSubscriberMap("Topic2").size());
        assertNotNull(manager.getTopicPublisher(0));
        assertNull(manager.getTopicPublisher(1));
        assertNotNull(manager.getTopicSubscriber(1));
        assertNull(manager.getTopicSubscriber(2));
        assertNotNull(manager.getDurableTopicSubscriber("subscription1"));
        assertNotNull(manager.getDurableTopicSubscriber("subscription2"));
        manager.removeTopicDurableSubscriber("subscription2");
        assertNotNull(manager.getDurableTopicSubscriber("subscription1"));
        assertNull(manager.getDurableTopicSubscriber("subscription2"));
        assertNull(manager.getDurableTopicSubscriber("subscription3"));
        assertNotNull(manager.getTopicPublisher("Topic1"));
        assertNotNull(manager.getTopicSubscriber("Topic1"));
        assertNull(manager.getTopicPublisher("Topic2"));
        assertNull(manager.getTopicSubscriber("Topic2"));
    }

    public void testQueueTransmissionManagerClose()
    {
        QueueTransmissionManager manager = session.getQueueTransmissionManager();
        MockQueueSender sender1 = manager.createQueueSender(new MockQueue("Queue1"));
        MockQueueSender sender2 = manager.createQueueSender(new MockQueue("Queue2"));
        MockQueueReceiver receiver1 = manager.createQueueReceiver(new MockQueue("Queue1"), "");
        MockQueueReceiver receiver2 = manager.createQueueReceiver(new MockQueue("Queue1"), "");
        MockQueueBrowser browser = manager.createQueueBrowser(new MockQueue("Queue1"), "");
        manager.closeAllQueueSenders();
        manager.closeAllQueueReceivers();
        manager.closeAllQueueBrowsers();
        assertTrue(sender1.isClosed());
        assertTrue(sender2.isClosed());
        assertTrue(receiver1.isClosed());
        assertTrue(receiver2.isClosed());
        assertTrue(browser.isClosed());
        sender1 = manager.createQueueSender(new MockQueue("Queue1"));
        sender2 = manager.createQueueSender(new MockQueue("Queue2"));
        receiver1 = manager.createQueueReceiver(new MockQueue("Queue1"), "");
        receiver2 = manager.createQueueReceiver(new MockQueue("Queue1"), "");
        browser = manager.createQueueBrowser(new MockQueue("Queue1"), "");
        manager.closeAll(); assertTrue(sender1.isClosed());
        assertTrue(sender2.isClosed()); assertTrue(receiver1.isClosed());
        assertTrue(receiver2.isClosed()); assertTrue(browser.isClosed());
    }

    public void testTopicTransmissionManagerClose()
    {
        TopicTransmissionManager manager = session.getTopicTransmissionManager();
        MockTopicPublisher publisher1 = manager.createTopicPublisher(new MockTopic("Topic1"));
        MockTopicPublisher publisher2 = manager.createTopicPublisher(new MockTopic("Topic2"));
        MockTopicSubscriber subscriber1 = manager.createTopicSubscriber(new MockTopic("Topic1"), "", false);;
        MockTopicSubscriber durableSubscriber1 = manager.createDurableTopicSubscriber(new MockTopic("Topic1"), "subscription1", "", true);
        MockTopicSubscriber durableSubscriber2 = manager.createDurableTopicSubscriber(new MockTopic("Topic1"), "subscription2", "", false);
        MockTopicSubscriber durableSubscriber3 = manager.createDurableTopicSubscriber(new MockTopic("Topic2"), "subscription3", "", true);
        manager.closeAllTopicPublishers();
        manager.closeAllTopicSubscribers();
        manager.closeAllTopicDurableSubscribers();
        assertTrue(publisher1.isClosed());
        assertTrue(publisher2.isClosed());
        assertTrue(subscriber1.isClosed());
        assertTrue(durableSubscriber1.isClosed());
        assertTrue(durableSubscriber2.isClosed());
        assertTrue(durableSubscriber3.isClosed());
        publisher1 = manager.createTopicPublisher(new
        MockTopic("Topic1")); publisher2 =
        manager.createTopicPublisher(new MockTopic("Topic2")); subscriber1
        = manager.createTopicSubscriber(new MockTopic("Topic1"), "", false);;
        durableSubscriber1 = manager.createDurableTopicSubscriber(new MockTopic("Topic1"), "subscription1", "", true);
        durableSubscriber2 = manager.createDurableTopicSubscriber(new MockTopic("Topic1"), "subscription2", "", false);
        durableSubscriber3 = manager.createDurableTopicSubscriber(new MockTopic("Topic2"), "subscription3", "", true);
        manager.closeAll();
        assertTrue(publisher1.isClosed());
        assertTrue(publisher2.isClosed());
        assertTrue(subscriber1.isClosed());
        assertTrue(durableSubscriber1.isClosed());
        assertTrue(durableSubscriber2.isClosed());
        assertTrue(durableSubscriber3.isClosed());
    }

    public void testGenericTransmissionManager()
    {
        GenericTransmissionManager manager = session.getGenericTransmissionManager();
        MockMessageProducer producer1 = manager.createMessageProducer();
        MockMessageProducer producer2 = manager.createQueueSender();
        MockMessageProducer producer3 = manager.createTopicPublisher();
        assertSame(producer1, manager.getMessageProducer(0));
        assertSame(producer2, manager.getMessageProducer(1));
        assertSame(producer3, manager.getMessageProducer(2)); List
        producerList = manager.getMessageProducerList(); assertEquals(3,
        producerList.size());
        assertTrue(producerList.contains(producer1));
        assertTrue(producerList.contains(producer2));
        assertTrue(producerList.contains(producer3)); manager.closeAll();
        assertTrue(producer1.isClosed());
        assertTrue(producer2.isClosed());
        assertTrue(producer3.isClosed());
    }

    public void testTransmissionManagerWrapperProducerAndConsumer()
    {
        QueueTransmissionManager queueManager = session.getQueueTransmissionManager();
        MockQueueSender sender1 = queueManager.createQueueSender(new MockQueue("Queue1"));
        MockQueueSender sender2 = queueManager.createQueueSender(new MockQueue("Queue2"));
        MockQueueReceiver receiver1 = queueManager.createQueueReceiver(new MockQueue("Queue1"), "");
        MockQueueReceiver receiver2 = queueManager.createQueueReceiver(new MockQueue("Queue1"), "");
        queueManager.createQueueBrowser(new MockQueue("Queue1"), "");
        TopicTransmissionManager topicManager = session.getTopicTransmissionManager();
        MockTopicPublisher publisher1 = topicManager.createTopicPublisher(new MockTopic("Topic1"));
        MockTopicSubscriber subscriber1 = topicManager.createTopicSubscriber(new MockTopic("Topic1"), "", false);
        MockTopicSubscriber subscriber2 = topicManager.createTopicSubscriber(new MockTopic("Topic1"), "", true);
        MockTopicSubscriber durableSubscriber1 = topicManager.createDurableTopicSubscriber(new MockTopic("Topic1"), "subscription1", "", true);
        GenericTransmissionManager genericManager = session.getGenericTransmissionManager();
        MockMessageProducer producer1 = genericManager.createMessageProducer();
        MockMessageProducer producer2 = genericManager.createMessageProducer();
        TransmissionManagerWrapper manager = new TransmissionManagerWrapper(queueManager, topicManager, genericManager);
        assertEquals(5, manager.getMessageProducerList().size());
        assertEquals(5, manager.getMessageConsumerList().size());
        assertNotNull(manager.getMessageProducer(2));
        assertNull(manager.getMessageProducer(6));
        assertNotNull(manager.getMessageConsumer(4));
        assertNull(manager.getMessageConsumer(5));
        assertTrue(manager.getMessageProducerList().contains(sender1));
        assertTrue(manager.getMessageProducerList().contains(sender2));
        assertTrue(manager.getMessageProducerList().contains(publisher1));
        assertTrue(manager.getMessageProducerList().contains(producer1));
        assertTrue(manager.getMessageProducerList().contains(producer2));
        assertTrue(manager.getMessageConsumerList().contains(receiver1));
        assertTrue(manager.getMessageConsumerList().contains(receiver2));
        assertTrue(manager.getMessageConsumerList().contains(subscriber1));
        assertTrue(manager.getMessageConsumerList().contains(subscriber2));
        assertTrue(manager.getMessageConsumerList().contains(durableSubscriber1));
    }

    public void testTransmissionManagerWrapperQueueSender()
    {
        QueueTransmissionManager queueManager = session.getQueueTransmissionManager();
        MockQueueSender sender1 = queueManager.createQueueSender(new MockQueue("Queue1"));
        MockQueueSender sender2 = queueManager.createQueueSender(new MockQueue("Queue2"));
        GenericTransmissionManager genericManager = session.getGenericTransmissionManager();
        MockMessageProducer producer1 = genericManager.createMessageProducer();
        MockMessageProducer producer2 = genericManager.createTopicPublisher();
        MockMessageProducer producer3 = genericManager.createQueueSender(); 
        TopicTransmissionManager topicManager = session.getTopicTransmissionManager();
        TransmissionManagerWrapper manager = new TransmissionManagerWrapper(queueManager, topicManager, genericManager);
        assertEquals(5, manager.getMessageProducerList().size());
        assertEquals(3, manager.getQueueSenderList().size());
        assertNotNull(manager.getQueueSender(2));
        assertNull(manager.getQueueSender(3));
        assertNotNull(manager.getMessageProducer(4));
        assertNull(manager.getMessageProducer(5));
        assertTrue(manager.getMessageProducerList().contains(sender1));
        assertTrue(manager.getMessageProducerList().contains(sender2));
        assertTrue(manager.getMessageProducerList().contains(producer1));
        assertTrue(manager.getMessageProducerList().contains(producer2));
        assertTrue(manager.getMessageProducerList().contains(producer3));
        assertTrue(manager.getQueueSenderList().contains(sender1));
        assertTrue(manager.getQueueSenderList().contains(sender2));
        assertTrue(manager.getQueueSenderList().contains(producer3));
    }

    public void testTransmissionManagerWrapperTopicPublisher()
    {
        TopicTransmissionManager topicManager = session.getTopicTransmissionManager();
        MockTopicPublisher publisher1 = topicManager.createTopicPublisher(new MockTopic("Topic1"));
        GenericTransmissionManager genericManager = session.getGenericTransmissionManager();
        MockMessageProducer producer1 = genericManager.createMessageProducer();
        MockMessageProducer producer2 = genericManager.createTopicPublisher();
        MockMessageProducer producer3 = genericManager.createQueueSender();
        QueueTransmissionManager queueManager = session.getQueueTransmissionManager();
        TransmissionManagerWrapper manager = new TransmissionManagerWrapper(queueManager, topicManager, genericManager);
        assertEquals(4, manager.getMessageProducerList().size());
        assertEquals(2, manager.getTopicPublisherList().size());
        assertNotNull(manager.getTopicPublisher(1));
        assertNull(manager.getTopicPublisher(2));
        assertNotNull(manager.getMessageProducer(3));
        assertNull(manager.getMessageProducer(4));
        assertTrue(manager.getMessageProducerList().contains(publisher1));
        assertTrue(manager.getMessageProducerList().contains(producer1));
        assertTrue(manager.getMessageProducerList().contains(producer2));
        assertTrue(manager.getMessageProducerList().contains(producer3));
        assertTrue(manager.getTopicPublisherList().contains(publisher1));
        assertTrue(manager.getTopicPublisherList().contains(producer2));
    }
}
