package com.mockrunner.test.jms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;

import junit.framework.TestCase;

import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockMessage;
import com.mockrunner.mock.jms.MockTextMessage;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicConnection;
import com.mockrunner.mock.jms.MockTopicSession;
import com.mockrunner.mock.jms.MockTopicSubscriber;

public class MockTopicTest extends TestCase
{
    private MockTopicConnection connection;
    private MockTopic topic;

    protected void setUp() throws Exception
    {
        super.setUp();
        DestinationManager manager = new DestinationManager();
        connection = new MockTopicConnection(manager);
        topic = new MockTopic("TestTopic");
    }

    public void testGetMessageList() throws Exception
    {
        assertTrue(topic.isEmpty());
        assertEquals(0, topic.getCurrentMessageList().size());
        assertEquals(0, topic.getReceivedMessageList().size());
        assertNull(topic.getMessage());
        topic.addMessage(new MockTextMessage("test1"));
        topic.addMessage(new MockTextMessage("test2"));
        topic.addMessage(new MockTextMessage("test3"));
        assertFalse(topic.isEmpty());
        assertEquals(3, topic.getCurrentMessageList().size());
        assertEquals(3, topic.getReceivedMessageList().size());
        assertEquals("test1", topic.getMessage().toString());
        assertFalse(topic.isEmpty());
        assertEquals(2, topic.getCurrentMessageList().size());
        assertEquals(3, topic.getReceivedMessageList().size());
        assertEquals("test2", topic.getMessage().toString());
        assertFalse(topic.isEmpty());
        assertEquals(1, topic.getCurrentMessageList().size());
        assertEquals(3, topic.getReceivedMessageList().size());
        assertEquals("test3", topic.getMessage().toString());
        assertTrue(topic.isEmpty());
        assertEquals(0, topic.getCurrentMessageList().size());
        assertEquals(3, topic.getReceivedMessageList().size());
        assertNull(topic.getMessage());
    }

    public void testAddMessage() throws Exception
    {
        MockTopicSession session = new MockTopicSession(connection, false, Session.CLIENT_ACKNOWLEDGE);
        topic.addSession(session);
        topic.addMessage(new MockTextMessage("test"));
        assertEquals(1, topic.getCurrentMessageList().size());
        assertEquals(1, topic.getReceivedMessageList().size());
        assertEquals("test", topic.getMessage().toString());
        TestMessageListener listener1 = new TestMessageListener();
        TestMessageListener listener2 = new TestMessageListener();
        MockTopicSubscriber  subscriber1 = (MockTopicSubscriber)session.createSubscriber(topic);
        session.setMessageListener(listener1);
        subscriber1.setMessageListener(listener2);
        topic.reset();
        topic.addMessage(new MockTextMessage("test"));
        assertEquals(0, topic.getCurrentMessageList().size());
        assertEquals(1, topic.getReceivedMessageList().size());
        assertNull(topic.getMessage());
        assertEquals("test", listener1.getMessage().toString());
        assertFalse(((MockMessage)listener1.getMessage()).isAcknowledged());
        assertNull(listener2.getMessage());
        session.setMessageListener(null);
        topic.reset();
        listener1.reset();
        listener2.reset();
        MockTopicSubscriber subscriber2 = (MockTopicSubscriber)session.createSubscriber(topic);
        subscriber2.setMessageListener(listener1);
        topic.addMessage(new MockTextMessage("test"));
        assertEquals(0, topic.getCurrentMessageList().size());
        assertEquals(1, topic.getReceivedMessageList().size());
        assertNull(topic.getMessage());
        assertEquals("test", listener1.getMessage().toString());
        assertEquals("test", listener2.getMessage().toString());
        assertFalse(((MockMessage)listener1.getMessage()).isAcknowledged());
        assertFalse(((MockMessage)listener2.getMessage()).isAcknowledged());
        topic.reset();
        listener1.reset();
        listener2.reset();
        subscriber2.close();
        topic.addMessage(new MockTextMessage("test"));
        assertEquals(0, topic.getCurrentMessageList().size());
        assertEquals(1, topic.getReceivedMessageList().size());
        assertNull(topic.getMessage());
        assertEquals("test", listener2.getMessage().toString());
        assertNull(listener1.getMessage());
        topic.reset();
        listener1.reset();
        listener2.reset();
        subscriber1.close();
        topic.addMessage(new MockTextMessage("test"));
        assertEquals(1, topic.getCurrentMessageList().size());
        assertEquals(1, topic.getReceivedMessageList().size());
        assertEquals("test", topic.getMessage().toString());
        assertNull(topic.getMessage());
        assertEquals(0, topic.getCurrentMessageList().size());
        topic.reset();
        listener1.reset();
        listener2.reset();
        subscriber1 = (MockTopicSubscriber)session.createSubscriber(topic);
        subscriber2 = (MockTopicSubscriber)session.createDurableSubscriber(topic, "durable");
        subscriber1.setMessageListener(listener1);
        subscriber2.setMessageListener(listener2);
        topic.addMessage(new MockTextMessage("test"));
        assertEquals(0, topic.getCurrentMessageList().size());
        assertEquals(1, topic.getReceivedMessageList().size());
        assertEquals("test", listener1.getMessage().toString());
        assertEquals("test", listener2.getMessage().toString()); 
        assertFalse(((MockMessage)listener1.getMessage()).isAcknowledged());
        assertFalse(((MockMessage)listener2.getMessage()).isAcknowledged());
        listener1.reset();
        listener2.reset();
        subscriber1 = (MockTopicSubscriber)session.createSubscriber(topic);
        subscriber1.setMessageListener(listener1);
        topic.addMessage(new MockTextMessage("test"));
        assertEquals(0, topic.getCurrentMessageList().size());
        assertEquals(2, topic.getReceivedMessageList().size());
    }
    
    public void testAddMessageAutoAcknowledge() throws Exception
    {
        MockTopicSession session = new MockTopicSession(connection, false, Session.AUTO_ACKNOWLEDGE);
        doTestAcknowledge(session);    
    }
    
    public void testAddMessageDupOkAcknowledge() throws Exception
    {
        MockTopicSession session = new MockTopicSession(connection, false, Session.DUPS_OK_ACKNOWLEDGE);
        doTestAcknowledge(session);    
    }
    
    private void doTestAcknowledge(MockTopicSession session) throws Exception
    {
        topic.addSession(session);
        MockTextMessage message = new MockTextMessage("text");
        topic.addMessage(message);
        assertFalse(message.isAcknowledged());
        message = new MockTextMessage("text");
        TestMessageListener listener = new TestMessageListener();
        session.setMessageListener(listener);
        topic.addMessage(message);
        assertTrue(message.isAcknowledged());
        session.setMessageListener(null);
        message = new MockTextMessage("text");
        MockTopicSubscriber subscriber = (MockTopicSubscriber)session.createSubscriber(topic);
        subscriber.setMessageListener(listener);
        topic.addMessage(message);
        assertTrue(message.isAcknowledged());
        subscriber.setMessageListener(null);
        message = new MockTextMessage("text");
        topic.addMessage(message);
        assertFalse(message.isAcknowledged());
        subscriber = (MockTopicSubscriber)session.createDurableSubscriber(topic, "myDurable");
        subscriber.setMessageListener(listener);
        message = new MockTextMessage("text");
        topic.addMessage(message);
        assertTrue(message.isAcknowledged());
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
