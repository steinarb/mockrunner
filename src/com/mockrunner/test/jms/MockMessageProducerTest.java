package com.mockrunner.test.jms;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Session;

import junit.framework.TestCase;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockMessageProducer;
import com.mockrunner.mock.jms.MockObjectMessage;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockSession;
import com.mockrunner.mock.jms.MockTextMessage;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicConnection;
import com.mockrunner.mock.jms.MockTopicPublisher;

public class MockMessageProducerTest extends TestCase
{
    private MockQueue queue;
    private MockTopic topic;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        queue = new MockQueue("Queue");
        topic = new MockTopic("Topic");
    }
  
    public void testSendWithQueueSender() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockQueueSender sender = new MockQueueSender(new MockQueueConnection(destManager, confManager), queue);
        doTestSendMessage(sender);
        assertEquals(1, queue.getCurrentMessageList().size());
        assertEquals(1, queue.getReceivedMessageList().size());
        assertEquals(0, topic.getCurrentMessageList().size());
        assertEquals(0, topic.getReceivedMessageList().size());
        assertEquals(new MockTextMessage("aMessage"), queue.getMessage());
        doTestSendWithInvalidParameters(sender);
    }
    
    public void testSendWithTopicPublisher() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockTopicPublisher publisher = new MockTopicPublisher(new MockTopicConnection(destManager, confManager), topic);
        doTestSendMessage(publisher);
        assertEquals(1, topic.getCurrentMessageList().size());
        assertEquals(1, topic.getReceivedMessageList().size());
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(0, queue.getReceivedMessageList().size());
        assertEquals(new MockTextMessage("aMessage"), topic.getMessage());
        doTestSendWithInvalidParameters(publisher);
    }
    
    private void doTestSendWithInvalidParameters(MockMessageProducer producer) throws Exception
    {
        try
        {
            producer.send(null, null);
            fail();
        }
        catch(InvalidDestinationException exc)
        {
            //should throw exception
        }
        try
        {
            producer.send(new Destination(){}, new MockTextMessage(""));
            fail();
        }
        catch(InvalidDestinationException exc)
        {
            //should throw exception
        }
        producer.close();
        try
        {
            producer.send(new MockTextMessage("aMessage"));
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
    }
    
    private void doTestSendMessage(MockMessageProducer producer) throws Exception
    {
        producer.send(new MockTextMessage("aMessage"));
    }
    
    public void testCloneMessageWithSession() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockConnection connection = new MockConnection(destManager, confManager);
        MockSession session =(MockSession)connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        doTestCloneMessage(session, destManager, confManager);
    }

    public void testCloneMessageWithQueueSession() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockQueueConnection connection = new MockQueueConnection(destManager, confManager);
        MockSession session =(MockSession)connection.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
        doTestCloneMessage(session, destManager, confManager);
    }

    public void testCloneMessageWithTopicSession() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockTopicConnection connection = new MockTopicConnection(destManager, confManager);
        MockSession session =(MockSession)connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        doTestCloneMessage(session, destManager, confManager);
    }

    private void doTestCloneMessage(MockSession session, DestinationManager destManager, ConfigurationManager confManager) throws Exception
    {
        MockQueue queue = destManager.createQueue("Queue");
        MockTopic topic = destManager.createTopic("Topic");
        MockMessageProducer producer1 = (MockMessageProducer)session.createProducer(queue);
        MockMessageProducer producer2 = (MockMessageProducer)session.createProducer(topic);
        MockTextMessage textMessage = new MockTextMessage();
        textMessage.setText("test");
        MockObjectMessage objectMessage = new MockObjectMessage();
        objectMessage.setObject(new Long(3));
        producer1.send(textMessage);
        producer1.send(textMessage);
        producer2.send(textMessage);
        producer2.send(objectMessage, DeliveryMode.PERSISTENT, 2, 0);
        assertSame(textMessage, queue.getMessage());
        assertSame(textMessage, queue.getMessage());
        assertSame(textMessage, topic.getMessage());
        assertSame(objectMessage, topic.getMessage());
        confManager.setDoCloneOnSend(true);
        producer1.send(textMessage);
        producer1.send(textMessage, DeliveryMode.PERSISTENT, 8, 0);
        producer2.send(textMessage);
        producer2.send(objectMessage, DeliveryMode.PERSISTENT, 5, 0);
        MockTextMessage receivedQueueMessage1 = (MockTextMessage)queue.getMessage();
        MockTextMessage receivedQueueMessage2 = (MockTextMessage)queue.getMessage();
        MockTextMessage receivedTopicMessage1 = (MockTextMessage)topic.getMessage();
        MockObjectMessage receivedTopicMessage2 = (MockObjectMessage)topic.getMessage();
        assertNotSame(textMessage, receivedQueueMessage1);
        assertNotSame(textMessage, receivedQueueMessage2);
        assertNotSame(textMessage, receivedTopicMessage1);
        assertNotSame(objectMessage, receivedTopicMessage2);
        assertNotSame(receivedQueueMessage1, receivedQueueMessage2);
        assertNotSame(receivedQueueMessage1, receivedTopicMessage1);
        assertEquals(textMessage, receivedQueueMessage1);
        assertEquals(textMessage, receivedQueueMessage2);
        assertEquals(textMessage, receivedTopicMessage1);
        assertEquals(objectMessage, receivedTopicMessage2);
        assertEquals(receivedQueueMessage1, receivedQueueMessage2);
        assertEquals(receivedQueueMessage1, receivedTopicMessage1);
    }
}
