package com.mockrunner.test.jms;

import java.util.List;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.InvalidDestinationException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.QueueSender;
import javax.jms.Session;

import junit.framework.TestCase;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.jms.GenericTransmissionManager;
import com.mockrunner.jms.QueueTransmissionManager;
import com.mockrunner.jms.TopicTransmissionManager;
import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockMessage;
import com.mockrunner.mock.jms.MockMessageProducer;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockQueueSession;
import com.mockrunner.mock.jms.MockSession;
import com.mockrunner.mock.jms.MockTextMessage;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicConnection;
import com.mockrunner.mock.jms.MockTopicPublisher;
import com.mockrunner.mock.jms.MockTopicSession;
import com.mockrunner.mock.jms.MockTopicSubscriber;

public class MockSessionTest extends TestCase
{
    public void testTransmissionJMSHeaders() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockQueueConnection connection = new MockQueueConnection(destManager, confManager);
        MockQueueSession session = (MockQueueSession)connection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
        destManager.createQueue("Queue1");
        MockQueue queue = (MockQueue)session.createQueue("Queue1");
        QueueSender sender = session.createSender(queue);
        MockMessage message = new MockTextMessage("Text1");
        message.setJMSTimestamp(0);
        message.setJMSMessageID("xyz");
        sender.setDisableMessageTimestamp(true);
        sender.setDisableMessageID(true);
        sender.setTimeToLive(0);
        sender.setPriority(9);
        sender.setDeliveryMode(DeliveryMode.PERSISTENT);
        sender.send(message);
        message = (MockMessage)queue.getMessage();
        assertEquals(0, message.getJMSTimestamp());
        assertEquals("xyz", message.getJMSMessageID());
        assertEquals(0, message.getJMSExpiration());
        assertEquals(9, message.getJMSPriority());
        assertEquals(DeliveryMode.PERSISTENT, message.getJMSDeliveryMode());
        assertEquals(queue, message.getJMSDestination());
        message = new MockTextMessage("Text1");
        sender.setDisableMessageTimestamp(false);
        sender.setDisableMessageID(false);
        sender.setPriority(7);
        sender.send(message);
        message = (MockMessage)queue.getMessage();
        assertFalse(0 == message.getJMSTimestamp());
        assertFalse("xyz".equals(message.getJMSMessageID()));
        assertEquals(7, message.getJMSPriority());
        assertEquals(queue, message.getJMSDestination());
        message = new MockTextMessage("Text1");
        sender.setTimeToLive(10000);
        sender.send(message);
        message = (MockMessage)queue.getMessage();
        assertEquals(message.getJMSTimestamp() + 10000, message.getJMSExpiration());
        assertEquals(queue, message.getJMSDestination());
        message = new MockTextMessage("Text1");
        sender.setTimeToLive(0);
        sender.send(message);
        message = (MockMessage)queue.getMessage();
        assertEquals(0, message.getJMSExpiration());
        assertEquals(queue, message.getJMSDestination());
        message = new MockTextMessage("Text1");
        destManager.createTopic("Topic1");
        MockTopic topic = (MockTopic)session.createTopic("Topic1");
        ((MockMessageProducer)sender).send(topic, message);
        assertEquals(topic, message.getJMSDestination());
    }
    
    public void testGetAcknowledgeMode() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockQueueConnection connection = new MockQueueConnection(destManager, confManager);
        MockSession session =(MockQueueSession)connection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
        assertEquals(Session.CLIENT_ACKNOWLEDGE, session.getAcknowledgeMode());
        session =(MockQueueSession)connection.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        assertEquals(Session.DUPS_OK_ACKNOWLEDGE, session.getAcknowledgeMode());
        session =(MockQueueSession)connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        assertEquals(Session.AUTO_ACKNOWLEDGE, session.getAcknowledgeMode());
        session =(MockQueueSession)connection.createQueueSession(false, 1234);
        assertEquals(1234, session.getAcknowledgeMode());
        session =(MockQueueSession)connection.createQueueSession(true, Session.CLIENT_ACKNOWLEDGE);
        assertEquals(0, session.getAcknowledgeMode());
        session =(MockQueueSession)connection.createQueueSession(true, 1234);
        assertEquals(0, session.getAcknowledgeMode());
    }
    
    public void testCreateProducerWithQueueSession() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockQueueConnection connection = new MockQueueConnection(destManager, confManager);
        MockSession session =(MockSession)connection.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
        doTestCreateProducer(session, destManager);
    }
    
    public void testCreateProducerWithTopicSession() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockTopicConnection connection = new MockTopicConnection(destManager, confManager);
        MockSession session =(MockSession)connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        doTestCreateProducer(session, destManager);
    }
    
    public void testCreateProducerWithSession() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockConnection connection = new MockConnection(destManager, confManager);
        MockSession session =(MockSession)connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        doTestCreateProducer(session, destManager);
    }
    
    public void testCreateNullProducer() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockQueueConnection queueConnection = new MockQueueConnection(destManager, confManager);
        MockQueueSession queueSession =(MockQueueSession)queueConnection.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
        MockTopicConnection topicConnection = new MockTopicConnection(destManager, confManager);
        MockTopicSession topicSession =(MockTopicSession)topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        MockConnection connection = new MockConnection(destManager, confManager);
        MockSession session =(MockSession)connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        assertTrue(queueSession.createProducer(null) instanceof MockQueueSender);
        assertTrue(topicSession.createProducer(null) instanceof MockTopicPublisher);
        assertTrue(queueSession.createSender(null) instanceof MockQueueSender);
        assertTrue(topicSession.createPublisher(null) instanceof MockTopicPublisher);
        assertTrue(session.createProducer(null) instanceof MockMessageProducer);
        assertFalse(session.createProducer(null) instanceof MockQueueSender);
        assertFalse(session.createProducer(null) instanceof MockTopicPublisher);
        QueueTransmissionManager queueManager = queueSession.getQueueTransmissionManager();
        assertEquals(0, queueManager.getQueueSenderList().size());
        TopicTransmissionManager topicManager = topicSession.getTopicTransmissionManager();
        assertEquals(0, topicManager.getTopicPublisherList().size());
        GenericTransmissionManager genericManager = queueSession.getGenericTransmissionManager();
        assertEquals(2, genericManager.getMessageProducerList().size());
        genericManager = topicSession.getGenericTransmissionManager();
        assertEquals(2, genericManager.getMessageProducerList().size());
        genericManager = session.getGenericTransmissionManager();
        assertEquals(3, genericManager.getMessageProducerList().size());
    }
    
    public void testCreateConsumerWithQueueSession() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockQueueConnection connection = new MockQueueConnection(destManager, confManager);
        MockSession session =(MockSession)connection.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
        doTestCreateConsumer(session, destManager);
    }

    public void testCreateComsumerWithTopicSession() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockTopicConnection connection = new MockTopicConnection(destManager, confManager);
        MockSession session =(MockSession)connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        doTestCreateConsumer(session, destManager);
    }
    
    public void testCreateComsumerWithSession() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockConnection connection = new MockConnection(destManager, confManager);
        MockSession session =(MockSession)connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        doTestCreateConsumer(session, destManager);
    }
    
    private void doTestCreateProducer(MockSession session, DestinationManager manager) throws Exception
    {
        MockQueue queue = manager.createQueue("Queue");
        MockTopic topic = manager.createTopic("Topic");
        MessageProducer producer1 = session.createProducer(queue);
        assertTrue(producer1 instanceof MockQueueSender);
        MessageProducer producer2 = session.createProducer(topic);
        assertTrue(producer2 instanceof MockTopicPublisher);
        ((MockQueueSender)producer1).send(queue, new MockTextMessage("mytext1"));
        ((MockQueueSender)producer1).send(queue, new MockTextMessage("mytext2"));
        ((MockTopicPublisher)producer2).send(topic, new MockTextMessage("mytext3"));
        List messageQueueList = queue.getCurrentMessageList();
        assertEquals(2, messageQueueList.size());
        assertTrue(messageQueueList.contains(new MockTextMessage("mytext1")));
        assertTrue(messageQueueList.contains(new MockTextMessage("mytext2")));
        List messageTopicList = topic.getCurrentMessageList();
        assertEquals(1, messageTopicList.size());
        assertTrue(messageTopicList.contains(new MockTextMessage("mytext3")));
        try
        {
            session.createProducer(new Destination(){});
            fail();
        }
        catch(InvalidDestinationException exc)
        {
            //should throw exception
        }
    }
    
    private void doTestCreateConsumer(MockSession session, DestinationManager manager) throws Exception
    {
        MockQueue queue = manager.createQueue("Queue");
        MockTopic topic = manager.createTopic("Topic");
        try
        {
            session.createConsumer(null, "", false);
            fail();
        }
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        MessageConsumer consumer1 = session.createConsumer(queue, "", false);
        assertTrue(consumer1 instanceof MockQueueReceiver);
        MessageConsumer consumer2 = session.createConsumer(topic);
        assertTrue(consumer2 instanceof MockTopicSubscriber);
        TestMessageListener listener = new TestMessageListener();
        session.setMessageListener(listener);
        topic.addMessage(new MockTextMessage("mytext1"));
        assertEquals(0, topic.getCurrentMessageList().size());
        assertEquals(1, topic.getReceivedMessageList().size());
        assertEquals(new MockTextMessage("mytext1"), listener.getMessage());
        session.setMessageListener(null);
        listener = new TestMessageListener();
        consumer1.setMessageListener(listener);
        topic.addMessage(new MockTextMessage("mytext2"));
        assertEquals(1, topic.getCurrentMessageList().size());
        assertEquals(2, topic.getReceivedMessageList().size());
        assertNull(listener.getMessage());
        queue.addMessage(new MockTextMessage("mytext3"));
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(1, queue.getReceivedMessageList().size());
        assertEquals(new MockTextMessage("mytext3"), listener.getMessage());
        MessageProducer producer = session.createProducer(topic);
        listener = new TestMessageListener();
        consumer2.setMessageListener(listener);
        ((MockTopicPublisher)producer).send(topic, new MockTextMessage("mytext4"));
        assertEquals(1, topic.getCurrentMessageList().size());
        assertEquals(3, topic.getReceivedMessageList().size());
        assertEquals(new MockTextMessage("mytext4"), listener.getMessage());
        try
        {
            session.createConsumer(new Destination(){}, "");
            fail();
        }
        catch(InvalidDestinationException exc)
        {
            //should throw exception
        }
    }

    public static class TestMessageListener implements MessageListener
    {
        private Message message;

        public Message getMessage()
        {
            return message;
        }

        public void onMessage(Message message)
        {
            this.message = message;
        }
    }
}
