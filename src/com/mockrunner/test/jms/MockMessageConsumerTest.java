package com.mockrunner.test.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;

import junit.framework.TestCase;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockMapMessage;
import com.mockrunner.mock.jms.MockMessageConsumer;
import com.mockrunner.mock.jms.MockObjectMessage;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSession;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicConnection;
import com.mockrunner.mock.jms.MockTopicSession;
import com.mockrunner.mock.jms.MockTopicSubscriber;

public class MockMessageConsumerTest extends TestCase
{
    private MockQueue queue;
    private MockTopic topic;
    private MockQueueConnection queueConnection;
    private MockTopicConnection topicConnection;
    private MockQueueSession queueSession;
    private MockTopicSession topicSession;

    protected void setUp() throws Exception
    {
        super.setUp();
        queue = new MockQueue("Queue");
        topic = new MockTopic("Topic");
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        queueConnection = new MockQueueConnection(destManager, confManager);
        topicConnection = new MockTopicConnection(destManager, confManager);
        queueSession = (MockQueueSession)queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        topicSession = (MockTopicSession)topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
    }
    
    public void testCreateSelector() throws Exception
    {
        MockQueueReceiver receiver = new MockQueueReceiver(queueConnection, queueSession, queue, "");
        assertEquals("", receiver.getMessageSelector());
        try
        {
            receiver = new MockQueueReceiver(queueConnection, queueSession, queue, "123");
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        MockTopicSubscriber subscriber = new MockTopicSubscriber(topicConnection, topicSession, topic, "", true);
        assertEquals("", subscriber.getMessageSelector());
        try
        {
            subscriber = new MockTopicSubscriber(topicConnection, topicSession, topic, "1x", false);
            fail();
        }
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
    
    public void testCanConsumeQueueReceiver() throws Exception
    {
        MockQueueReceiver receiver = new MockQueueReceiver(queueConnection, queueSession, queue);
        doTestCanConsumeWithoutSelector(receiver);
        receiver = new MockQueueReceiver(queueConnection, queueSession, queue, "string = 'test' AND number = 3");
        doTestCanConsumeWithSelector(receiver);
        receiver = new MockQueueReceiver(queueConnection, queueSession, queue, "string = 'test'");
        queueConnection.getConfigurationManager().setUseMessageSelectors(false);
        doTestCanConsumeWithSelectorDisabled(receiver);
    }
    
    public void testCanConsumeTopicSubscriber() throws Exception
    {
        MockTopicSubscriber subscriber = new MockTopicSubscriber(topicConnection, topicSession, topic);
        doTestCanConsumeWithoutSelector(subscriber);
        subscriber = new MockTopicSubscriber(topicConnection, topicSession, topic, "string = 'test' AND number = 3", true);
        doTestCanConsumeWithSelector(subscriber);
        subscriber = new MockTopicSubscriber(topicConnection, topicSession, topic, "string = 'test'", false);
        topicConnection.getConfigurationManager().setUseMessageSelectors(false);
        doTestCanConsumeWithSelectorDisabled(subscriber);
    }
    
    public void testReceiveQueueReceiver() throws Exception
    {
        MockQueueReceiver receiver = new MockQueueReceiver(queueConnection, queueSession, queue);
        MockMapMessage message1 = new MockMapMessage();
        MockMapMessage message2 = new MockMapMessage();
        MockMapMessage message3 = new MockMapMessage();
        message2.setJMSCorrelationID("myId");
        message3.setJMSCorrelationID("myId");
        queue.addMessage(message1);
        queue.addMessage(message2);
        queue.addMessage(message3);
        doTestReceiveWithoutSelector(receiver);
        receiver = new MockQueueReceiver(queueConnection, queueSession, queue, "JMSCorrelationID = 'myId'");
        queue.addMessage(message1);
        queue.addMessage(message2);
        queue.addMessage(message3);
        doTestReceiveWithSelector(receiver);
        queueConnection.getConfigurationManager().setUseMessageSelectors(false);
        doTestReceiveWithSelectorDisabled(receiver);
    }
    
    public void testReceiveTopicSubscriber() throws Exception
    {
        MockTopicSubscriber subscriber = new MockTopicSubscriber(topicConnection, topicSession, topic);
        MockMapMessage message1 = new MockMapMessage();
        MockMapMessage message2 = new MockMapMessage();
        MockMapMessage message3 = new MockMapMessage();
        message2.setJMSCorrelationID("myId");
        message3.setJMSCorrelationID("myId");
        topic.addMessage(message1);
        topic.addMessage(message2);
        topic.addMessage(message3);
        doTestReceiveWithoutSelector(subscriber);
        subscriber = new MockTopicSubscriber(topicConnection, topicSession, topic, "JMSCorrelationID = 'myId'", true);
        topic.addMessage(message1);
        topic.addMessage(message2);
        topic.addMessage(message3);
        doTestReceiveWithSelector(subscriber);
        topicConnection.getConfigurationManager().setUseMessageSelectors(false);
        doTestReceiveWithSelectorDisabled(subscriber);
    }

    private void doTestCanConsumeWithoutSelector(MockMessageConsumer consumer) throws JMSException
    {
        MockObjectMessage message = new MockObjectMessage();
        assertFalse(consumer.canConsume(message));
        consumer.setMessageListener(new TestMessageListener());
        assertTrue(consumer.canConsume(message));
        consumer.close();
        assertFalse(consumer.canConsume(message));
    }
    
    private void doTestCanConsumeWithSelector(MockMessageConsumer consumer) throws JMSException
    {
        MockObjectMessage message = new MockObjectMessage();
        assertFalse(consumer.canConsume(message));
        consumer.setMessageListener(new TestMessageListener());
        message.setStringProperty("string", "test");
        assertFalse(consumer.canConsume(message));
        message.setIntProperty("number", 2);
        assertFalse(consumer.canConsume(message));
        message.setIntProperty("number", 3);
        assertTrue(consumer.canConsume(message));
        consumer.close();
        assertFalse(consumer.canConsume(message));
    }

    private void doTestCanConsumeWithSelectorDisabled(MockMessageConsumer consumer) throws JMSException
    {
        MockObjectMessage message = new MockObjectMessage();
        assertFalse(consumer.canConsume(message));
        consumer.setMessageListener(new TestMessageListener());
        assertTrue(consumer.canConsume(message));
        message.setStringProperty("string", "xyz");
        assertTrue(consumer.canConsume(message));
    }
    
    private void doTestReceiveWithoutSelector(MockMessageConsumer consumer) throws JMSException
    {
        assertNotNull(consumer.receive());
        assertNotNull(consumer.receiveNoWait());
        assertNotNull(consumer.receive(1000));
        assertNull(consumer.receive());
    }

    private void doTestReceiveWithSelector(MockMessageConsumer consumer) throws JMSException
    {
        assertNotNull(consumer.receiveNoWait());
        assertNotNull(consumer.receive(1000));
        assertNull(consumer.receive());
    }
    
    private void doTestReceiveWithSelectorDisabled(MockMessageConsumer consumer) throws JMSException
    {
        assertNotNull(consumer.receive());
        assertNull(consumer.receive());
    }
  
    public static class TestMessageListener implements MessageListener
    {
        public void onMessage(Message message)
        {
        }
    }
}
