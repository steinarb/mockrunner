package com.mockrunner.test.jms;

import java.util.ArrayList;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import junit.framework.TestCase;

import com.mockrunner.jms.MessageManager;
import com.mockrunner.jms.TopicManager;
import com.mockrunner.jms.TopicTransmissionManager;
import com.mockrunner.mock.jms.MockBytesMessage;
import com.mockrunner.mock.jms.MockMapMessage;
import com.mockrunner.mock.jms.MockObjectMessage;
import com.mockrunner.mock.jms.MockStreamMessage;
import com.mockrunner.mock.jms.MockTextMessage;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicConnection;
import com.mockrunner.mock.jms.MockTopicPublisher;
import com.mockrunner.mock.jms.MockTopicSession;
import com.mockrunner.mock.jms.MockTopicSubscriber;

public class MockTopicSessionTest extends TestCase
{
    private MockTopicConnection connection;
    private MockTopicSession session;
    private MockTopicSession anotherSession;
    private MockTopic topic1;
    private MockTopic topic2;
 
    protected void setUp() throws Exception
    {
        super.setUp();
        connection = new MockTopicConnection();
        session = (MockTopicSession)connection.createTopicSession(false, TopicSession.CLIENT_ACKNOWLEDGE);
        anotherSession = (MockTopicSession)connection.createTopicSession(false, TopicSession.CLIENT_ACKNOWLEDGE);
    }
    
    public void testCreateMessages() throws Exception
    {
        session.createTextMessage("Text1");
        session.createObjectMessage("Object1");
        session.createBytesMessage();  
        session.createStreamMessage();
        MessageManager manager = session.getMessageManager();
        assertEquals("Text1", manager.getTextMessage(0).getText());
        assertEquals("Object1", manager.getObjectMessage(0).getObject());
        assertNotNull(manager.getBytesMessage(0));
        assertNotNull(manager.getStreamMessage(0));
        assertNull(manager.getMapMessage(0));
        assertNull(manager.getBytesMessage(1));
    }
    
    public void testCreateTopics() throws Exception
    {
        try
        {
            session.createTopic("Topic1");
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
        TopicManager manager = connection.getTopicManager();
        Topic managerTopic1 = manager.createTopic("Topic1");
        Topic topic = session.createTopic("Topic1");
        assertTrue(topic == managerTopic1);
        assertEquals("Topic1", topic.getTopicName());
        manager.createTopic("Topic2");
        assertTrue(manager.getTopic("Topic2") == session.createTopic("Topic2"));
        manager.removeTopic("Topic2");
        try
        {
            session.createTopic("Topic2");
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
        session.createTemporaryTopic();
        TemporaryTopic tempTopic = session.createTemporaryTopic();
        assertNotNull(session.getTemporaryTopic(0));
        assertNotNull(session.getTemporaryTopic(1));
        assertNull(session.getTemporaryTopic(3));
        assertTrue(tempTopic == session.getTemporaryTopic(1));
    }
    
    public void testCreatePublisherAndSubscriber() throws Exception
    {
        TopicManager topicManager = connection.getTopicManager();
        TopicTransmissionManager transManager = session.getTopicTransmissionManager();
        topic1 = topicManager.createTopic("Topic1");
        topic2 = topicManager.createTopic("Topic2");
        assertEquals(0, transManager.getTopicPublisherList().size());
        TopicPublisher publisher1 = session.createPublisher(topic1);
        TopicPublisher publisher2 = session.createPublisher(topic2);  
        assertNotNull(transManager.getTopicPublisher(0));
        assertNotNull(transManager.getTopicPublisher(1));
        assertNull(transManager.getTopicPublisher(2));
        assertEquals(2, transManager.getTopicPublisherList().size());
        assertTrue(publisher1 == transManager.getTopicPublisher(0));
        assertTrue(publisher2 == transManager.getTopicPublisher("Topic2"));
        assertTrue(topic1 == transManager.getTopicPublisher("Topic1").getTopic());
        assertTrue(topic2 == transManager.getTopicPublisher(1).getTopic());
        assertEquals(0, transManager.getTopicSubscriberList().size());
        TopicSubscriber subscriber = session.createSubscriber(topic1);
        assertFalse(((MockTopicSubscriber)subscriber).isDurable());
        session.createSubscriber(topic2);
        assertNotNull(transManager.getTopicSubscriber(0));
        assertNotNull(transManager.getTopicSubscriber(1));
        assertTrue(subscriber == transManager.getTopicSubscriber("Topic1"));
        assertTrue("Topic2" == transManager.getTopicSubscriber(1).getTopic().getTopicName());
        assertNull(transManager.getTopicSubscriber(2));
        assertTrue(subscriber == transManager.getTopicSubscriber(0));
        assertFalse(subscriber == transManager.getTopicSubscriber(1));
        assertTrue(topic1 == transManager.getTopicSubscriber(0).getTopic());
        assertTrue(topic2 == transManager.getTopicSubscriber(1).getTopic());
        assertEquals(2, transManager.getTopicSubscriberList().size());
    }
    
    public void testCreateDurableSubscriber() throws Exception
    {
        TopicManager topicManager = connection.getTopicManager();
        TopicTransmissionManager transManager = session.getTopicTransmissionManager();
        topic1 = topicManager.createTopic("Topic1");
        assertEquals(0, transManager.getDurableTopicSubscriberMap().size());
        TopicSubscriber subscriber1 = session.createDurableSubscriber(topic1, "Durable1");
        TopicSubscriber subscriber2 = session.createDurableSubscriber(topic1, "Durable2", null, true);
        assertEquals(2, transManager.getDurableTopicSubscriberMap().size());
        assertFalse(((MockTopicSubscriber)subscriber1).getNoLocal());
        assertTrue(((MockTopicSubscriber)subscriber1).isDurable());
        assertTrue(((MockTopicSubscriber)subscriber2).getNoLocal());
        assertTrue(((MockTopicSubscriber)subscriber2).isDurable());
        assertTrue(subscriber1 == transManager.getDurableTopicSubscriber("Durable1"));
        assertTrue(subscriber2 == transManager.getDurableTopicSubscriber("Durable2"));
        assertEquals("Durable1", transManager.getDurableTopicSubscriber("Durable1").getName());
        assertTrue(topic1 == transManager.getDurableTopicSubscriber("Durable2").getTopic());
        session.unsubscribe("Durable2");
        assertEquals(1, transManager.getDurableTopicSubscriberMap().size());
        assertTrue(subscriber1 == transManager.getDurableTopicSubscriberMap().get("Durable1"));
        assertNull(transManager.getDurableTopicSubscriberMap().get("Durable2"));
        assertNull(transManager.getDurableTopicSubscriber("Durable2"));
        TopicSubscriber subscriber3 = session.createDurableSubscriber(topic1, "Durable1");
        assertFalse(((MockTopicSubscriber)subscriber3).getNoLocal());
        assertTrue(((MockTopicSubscriber)subscriber3).isDurable());
        assertFalse(subscriber1 == transManager.getDurableTopicSubscriber("Durable1"));
        assertTrue(subscriber3 == transManager.getDurableTopicSubscriber("Durable1"));
        session.unsubscribe("Durable1");
        assertEquals(0, transManager.getDurableTopicSubscriberMap().size());
    }
    
    public void testTransmissionGlobalListener() throws Exception
    {
        TopicManager topicManager = connection.getTopicManager();
        topicManager.createTopic("Topic1");
        topic1 = (MockTopic)session.createTopic("Topic1");
        TopicPublisher publisher = session.createPublisher(topic1);
        TestMessageListener globalListener = new TestMessageListener();
        session.setMessageListener(globalListener);
        publisher.publish(new MockTextMessage("Text1"));
        assertEquals("Text1", ((TextMessage)globalListener.getMessage()).getText());
        TopicSubscriber subscriber1 = session.createSubscriber(topic1);
        TopicSubscriber subscriber2 = session.createDurableSubscriber(topic1, "durable");
        TestMessageListener listener1 = new TestMessageListener();
        TestMessageListener listener2 = new TestMessageListener();
        subscriber1.setMessageListener(listener1);
        subscriber2.setMessageListener(listener2);
        publisher.publish(new MockTextMessage("Text2"));
        assertEquals("Text2", ((TextMessage)globalListener.getMessage()).getText());
        assertNull(listener1.getMessage());
        assertNull(listener2.getMessage());
    }
    
    public void testTransmission() throws Exception
    {
        TopicManager topicManager = connection.getTopicManager();
        topicManager.createTopic("Topic1");
        topicManager.createTopic("Topic2");
        topic1 = (MockTopic)session.createTopic("Topic1");
        topic2 = (MockTopic)session.createTopic("Topic2");
        TopicPublisher publisher1 = session.createPublisher(topic1);
        TopicPublisher publisher2 = session.createPublisher(topic2);
        MockTopicSubscriber subscriber1 = (MockTopicSubscriber)session.createSubscriber(topic1);
        MockTopicSubscriber subscriber2 = (MockTopicSubscriber)session.createSubscriber(topic1);
        MockTopicSubscriber subscriber3 = (MockTopicSubscriber)session.createSubscriber(topic2);
        TestMessageListener listener1 = new TestMessageListener();
        TestMessageListener listener2 = new TestMessageListener();
        TestMessageListener listener3 = new TestMessageListener();
        subscriber1.setMessageListener(listener1);
        subscriber2.setMessageListener(listener2);
        subscriber3.setMessageListener(listener3);   
        publisher1.publish(new MockTextMessage("Text1"));
        assertEquals("Text1", ((TextMessage)listener1.getMessage()).getText());
        assertEquals("Text1", ((TextMessage)listener2.getMessage()).getText());
        assertNull(listener3.getMessage());
        assertEquals(1, topic1.getReceivedMessageList().size());
        assertEquals(0, topic1.getCurrentMessageList().size());
        assertNull(topic1.getMessage());
        assertTrue(topic1.isEmpty());
        assertEquals(0, topic2.getReceivedMessageList().size());
        assertEquals(0, topic2.getCurrentMessageList().size());
        assertNull(topic2.getMessage());
        assertTrue(topic2.isEmpty());
        publisher1.publish(new MockTextMessage("Text2"));
        assertEquals("Text2", ((TextMessage)listener1.getMessage()).getText());
        assertEquals("Text2", ((TextMessage)listener2.getMessage()).getText());
        assertNull(listener3.getMessage());
        publisher1.publish(new MockTextMessage("Text3"));
        assertEquals("Text3", ((TextMessage)listener1.getMessage()).getText());
        assertEquals("Text3", ((TextMessage)listener2.getMessage()).getText());
        assertNull(listener3.getMessage());
        assertEquals(3, topic1.getReceivedMessageList().size());
        assertEquals(0, topic1.getCurrentMessageList().size());
        assertEquals(0, topic2.getReceivedMessageList().size());
        assertEquals(0, topic2.getCurrentMessageList().size());
        publisher2.publish(new MockTextMessage("Text4"));
        assertEquals("Text3", ((TextMessage)listener1.getMessage()).getText());
        assertEquals("Text3", ((TextMessage)listener2.getMessage()).getText());
        assertEquals("Text4", ((TextMessage)listener3.getMessage()).getText());
        assertEquals(3, topic1.getReceivedMessageList().size());
        assertEquals(0, topic1.getCurrentMessageList().size());
        assertEquals(1, topic2.getReceivedMessageList().size());
        assertEquals(0, topic2.getCurrentMessageList().size());
        topicManager.createTopic("NewTopic");
        topic2 = (MockTopic)session.createTopic("NewTopic");
        publisher2 = session.createPublisher(topic2);
        publisher2.publish(new MockTextMessage("Text5"));
        assertEquals(1, topic2.getReceivedMessageList().size());
        assertEquals(1, topic2.getCurrentMessageList().size());
        assertEquals("Text5", ((TextMessage)topic2.getMessage()).getText());
        assertNull(topic2.getMessage());
        assertEquals(1, topic2.getReceivedMessageList().size());
        assertEquals(0, topic2.getCurrentMessageList().size());
        subscriber2 = (MockTopicSubscriber)session.createSubscriber(topic2);
        publisher2.publish(new MockTextMessage("Text6"));
        publisher2.publish(new MockTextMessage("Text7"));
        publisher2.publish(new MockTextMessage("Text8"));
        assertEquals("Text6", ((TextMessage)subscriber2.receive()).getText());
        assertEquals("Text7", ((TextMessage)subscriber2.receive()).getText());
        assertEquals("Text8", ((TextMessage)topic2.getMessage()).getText());
        assertNull(topic2.getMessage());
        assertEquals(4, topic2.getReceivedMessageList().size());
        assertEquals(0, topic2.getCurrentMessageList().size());
        subscriber2 = (MockTopicSubscriber)session.createSubscriber(topic2);
        subscriber2.setMessageListener(listener2);
        publisher2.publish(new MockTextMessage("Text9"));
        assertEquals(5, topic2.getReceivedMessageList().size());
        assertEquals(0, topic2.getCurrentMessageList().size());
        assertEquals("Text9", ((TextMessage)listener2.getMessage()).getText());
        assertNull(topic2.getMessage());
    }
    
    public void testTransmissionDurableSubscriber() throws Exception
    {
        TopicManager topicManager = connection.getTopicManager();
        topicManager.createTopic("Topic1");
        topicManager.createTopic("Topic2");
        topic1 = (MockTopic)session.createTopic("Topic1");
        topic2 = (MockTopic)session.createTopic("Topic2");
        TopicPublisher publisher1 = session.createPublisher(topic1);
        TopicPublisher publisher2 = session.createPublisher(topic2);
        MockTopicSubscriber subscriber1 = (MockTopicSubscriber)session.createSubscriber(topic1);
        MockTopicSubscriber subscriber2 = (MockTopicSubscriber)session.createSubscriber(topic2);
        MockTopicSubscriber durableSubscriber1 = (MockTopicSubscriber)session.createDurableSubscriber(topic1, "durable1");
        MockTopicSubscriber durableSubscriber2 = (MockTopicSubscriber)session.createDurableSubscriber(topic1, "durable2");
        TestMessageListener listener1 = new TestMessageListener();
        TestMessageListener listener2 = new TestMessageListener();
        TestMessageListener durableListener1 = new TestMessageListener();
        TestMessageListener durableListener2 = new TestMessageListener();
        subscriber1.setMessageListener(listener1);
        subscriber2.setMessageListener(listener2);
        durableSubscriber1.setMessageListener(durableListener1);
        durableSubscriber2.setMessageListener(durableListener2);
        publisher1.publish(new MockTextMessage("Text1"));
        assertEquals(1, topic1.getReceivedMessageList().size());
        assertEquals(0, topic1.getCurrentMessageList().size());
        assertEquals(0, topic2.getReceivedMessageList().size());
        assertEquals(0, topic2.getCurrentMessageList().size());
        assertEquals("Text1", ((TextMessage)listener1.getMessage()).getText());
        assertEquals("Text1", ((TextMessage)durableListener1.getMessage()).getText());
        assertEquals("Text1", ((TextMessage)durableListener2.getMessage()).getText());
        assertNull(listener2.getMessage());
        publisher2.publish(new MockTextMessage("Text2"));
        assertEquals(1, topic1.getReceivedMessageList().size());
        assertEquals(0, topic1.getCurrentMessageList().size());
        assertEquals(1, topic2.getReceivedMessageList().size());
        assertEquals(0, topic2.getCurrentMessageList().size());
        assertEquals("Text1", ((TextMessage)listener1.getMessage()).getText());
        assertEquals("Text1", ((TextMessage)durableListener1.getMessage()).getText());
        assertEquals("Text1", ((TextMessage)durableListener2.getMessage()).getText());
        assertEquals("Text2", ((TextMessage)listener2.getMessage()).getText());
        session.unsubscribe("durable2");
        publisher1.publish(new MockTextMessage("Text3"));
        assertEquals(2, topic1.getReceivedMessageList().size());
        assertEquals(0, topic1.getCurrentMessageList().size());
        assertEquals(1, topic2.getReceivedMessageList().size());
        assertEquals(0, topic2.getCurrentMessageList().size());
        assertEquals("Text3", ((TextMessage)listener1.getMessage()).getText());
        assertEquals("Text3", ((TextMessage)durableListener1.getMessage()).getText());
        assertEquals("Text1", ((TextMessage)durableListener2.getMessage()).getText());
        assertEquals("Text2", ((TextMessage)listener2.getMessage()).getText());
    }
                
    public void testTransmissionResetCalled() throws Exception
    {
        TopicManager topicManager = connection.getTopicManager();
        topicManager.createTopic("Topic1");
        topic1 = (MockTopic)session.createTopic("Topic1");
        TopicPublisher publisher = session.createPublisher(topic1);
        BytesMessage bytesMessage = new MockBytesMessage();
        StreamMessage streamMessage = new MockStreamMessage();
        bytesMessage.writeDouble(123.3);
        streamMessage.writeLong(234);
        try
        {
            bytesMessage.readDouble();
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
        try
        {
            streamMessage.readInt();
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
        publisher.publish(bytesMessage);
        publisher.publish(streamMessage);
        assertEquals(123.3, bytesMessage.readDouble(), 0);
        assertEquals(234, streamMessage.readLong());
    }
    
    public void testTransmissionSenderOrReceiverClosed() throws Exception
    {
        TopicManager topicManager = connection.getTopicManager();
        topicManager.createTopic("Topic1");
        topic1 = (MockTopic)session.createTopic("Topic1");
        MockTopicPublisher publisher = (MockTopicPublisher)session.createPublisher(topic1);
        TopicSubscriber subscriber1 = session.createSubscriber(topic1);
        TestMessageListener listener1 = new TestMessageListener();
        subscriber1.setMessageListener(listener1);
        publisher.publish(new MockTextMessage("Text"));
        assertNull(subscriber1.receive());
        assertEquals("Text", listener1.getMessage().toString());
        listener1.reset();
        subscriber1.close();
        publisher.publish(new MockTextMessage("Text"));
        assertNull(listener1.getMessage());
        try
        {
            subscriber1.receive();
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
        TopicSubscriber subscriber2 = session.createSubscriber(topic1); 
        assertEquals("Text", subscriber2.receive().toString());
        TestMessageListener listener2 = new TestMessageListener();
        subscriber2.setMessageListener(listener2);
        publisher.publish(new MockTextMessage("Text"));
        assertEquals("Text", listener2.getMessage().toString());
        publisher.close();
        try
        {
            publisher.publish(new MockTextMessage("Text"));
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
    }
    
    public void testTransmissionMessageAcknowledged() throws Exception
    {
        MockTopicSession session1 = (MockTopicSession)connection.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);
        MockTopicSession session2 = (MockTopicSession)connection.createTopicSession(false, Session.DUPS_OK_ACKNOWLEDGE);
        TopicManager topicManager = connection.getTopicManager();
        topicManager.createTopic("Topic");
        Topic topic = (MockTopic)session1.createTopic("Topic");
        MockTopicPublisher publisher = (MockTopicPublisher)session2.createPublisher(topic);
        MockMapMessage message1 = new MockMapMessage();
        MockStreamMessage message2 = new MockStreamMessage();
        publisher.publish(message1);
        publisher.publish(message2);
        assertFalse(message1.isAcknowledged());
        assertFalse(message2.isAcknowledged());
        MockTopicSubscriber subscriber1 = (MockTopicSubscriber)session1.createSubscriber(topic);
        MockTopicSubscriber subscriber2 = (MockTopicSubscriber)session2.createDurableSubscriber(topic, "mySubscription");
        subscriber1.receive(1);
        assertFalse(message1.isAcknowledged());
        assertFalse(message2.isAcknowledged());
        subscriber2.receiveNoWait();
        assertFalse(message1.isAcknowledged());
        assertTrue(message2.isAcknowledged());
        TestMessageListener listener = new TestMessageListener();
        subscriber1.setMessageListener(listener);
        message2 = new MockStreamMessage();
        publisher.publish(message2);
        assertFalse(message2.isAcknowledged());
        subscriber2.setMessageListener(listener);
        message2 = new MockStreamMessage();
        publisher.publish(message2);
        assertTrue(message2.isAcknowledged());
        subscriber1.setMessageListener(null);
        subscriber2.setMessageListener(null);
        session1.setMessageListener(listener);
        message2 = new MockStreamMessage();
        publisher.publish(message2);
        assertFalse(message2.isAcknowledged());
        session2.setMessageListener(listener);
        message2 = new MockStreamMessage();
        publisher.publish(message2);
        assertTrue(message2.isAcknowledged());
    }
    
    public void testTransmissionConnectionStopped() throws Exception
    {
        TopicManager topicManager = connection.getTopicManager();
        topicManager.createTopic("Topic1");
        topic1 = (MockTopic)session.createTopic("Topic1");
        MockTopicSubscriber subscriber = (MockTopicSubscriber)session.createSubscriber(topic1);
        TestMessageListener listener = new TestMessageListener();
        subscriber.setMessageListener(listener);
        TopicPublisher publisher = session.createPublisher(topic1);
        connection.stop();
        publisher.publish(new MockTextMessage("Text"));
        assertNull(listener.getMessage());
        connection.start();  
        publisher.publish(new MockTextMessage("Text"));
        assertEquals("Text", listener.getMessage().toString());
    }
    
    public void testTransmissionMultipleSessions() throws Exception
    {
        TopicManager topicManager = connection.getTopicManager();
        topicManager.createTopic("Topic1");
        topic1 = (MockTopic)session.createTopic("Topic1");
        topic2 = (MockTopic)anotherSession.createTopic("Topic1");
        TestListMessageListener listener = new TestListMessageListener();
        session.setMessageListener(listener);
        MockTopicSubscriber subscriber1 = (MockTopicSubscriber)anotherSession.createSubscriber(topic1);
        subscriber1.setMessageListener(listener);
        TopicPublisher publisher = anotherSession.createPublisher(topic1);
        publisher.publish(new MockTextMessage("Text1"));
        assertEquals(1, topic1.getReceivedMessageList().size());
        assertEquals(0, topic1.getCurrentMessageList().size());
        assertEquals(2, listener.getMessageList().size());
        assertEquals("Text1", listener.getMessageList().get(0).toString());
        MockTopicSubscriber subscriber2 = (MockTopicSubscriber)session.createSubscriber(topic1);
        subscriber2.setMessageListener(listener);
        session.setMessageListener(null);
        publisher.publish(new MockTextMessage("Text2"));
        assertEquals(2, topic1.getReceivedMessageList().size());
        assertEquals(0, topic1.getCurrentMessageList().size());
        assertEquals(4, listener.getMessageList().size());
        assertEquals("Text1", listener.getMessageList().get(0).toString());
        assertEquals("Text1", listener.getMessageList().get(1).toString());
        assertEquals("Text2", listener.getMessageList().get(2).toString());
        assertEquals("Text2", listener.getMessageList().get(3).toString());
        MockTopicSubscriber subscriber3 = (MockTopicSubscriber)session.createSubscriber(topic1);
        subscriber3.setMessageListener(listener);
        publisher = anotherSession.createPublisher(topic2);
        publisher.publish(new MockObjectMessage(new Integer(1)));
        assertEquals(3, topic1.getReceivedMessageList().size());
        assertEquals(3, topic2.getReceivedMessageList().size());
        assertEquals(0, topic1.getCurrentMessageList().size());
        assertEquals(0, topic2.getCurrentMessageList().size());
        assertEquals(7, listener.getMessageList().size());
        topicManager.createTopic("Topic2");
        topic2 = (MockTopic)anotherSession.createTopic("Topic2");
        publisher = anotherSession.createPublisher(topic2);
        publisher.publish(new MockTextMessage("Text2"));
        assertEquals(3, topic1.getReceivedMessageList().size());
        assertEquals(0, topic1.getCurrentMessageList().size());
        assertEquals(1, topic2.getReceivedMessageList().size());
        assertEquals(1, topic2.getCurrentMessageList().size());
        assertEquals(7, listener.getMessageList().size());
    }
    
    public static class TestListMessageListener implements MessageListener
    {
        private List messages = new ArrayList();

        public List getMessageList()
        {
            return messages;
        }

        public void onMessage(Message message)
        {
            messages.add(message);
        }
    }
    
    public static class TestMessageListener implements MessageListener
    {
        private Message message;
    
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
        }
    }
}
