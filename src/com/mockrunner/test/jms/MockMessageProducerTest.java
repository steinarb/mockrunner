package com.mockrunner.test.jms;

import java.util.Set;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageNotWriteableException;
import javax.jms.Session;

import junit.framework.TestCase;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockBytesMessage;
import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockMapMessage;
import com.mockrunner.mock.jms.MockMessageProducer;
import com.mockrunner.mock.jms.MockObjectMessage;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockSession;
import com.mockrunner.mock.jms.MockStreamMessage;
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
    
    public void testReadOnly() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockQueueConnection connection = new MockQueueConnection(destManager, confManager);
        MockQueueSender sender = new MockQueueSender(connection, new MockSession(connection, true, Session.CLIENT_ACKNOWLEDGE), queue);
        MockTopicPublisher publisher = new MockTopicPublisher(connection, new MockSession(connection, true, Session.CLIENT_ACKNOWLEDGE), topic);
        MockTextMessage textMessage = new MockTextMessage();
        MockMapMessage mapMessage = new MockMapMessage();
        MockBytesMessage bytesMessage = new MockBytesMessage();
        MockStreamMessage streamMessage = new MockStreamMessage();
        MockObjectMessage objectMessage = new MockObjectMessage();
        sender.send(textMessage);
        sender.send(mapMessage);
        publisher.publish(bytesMessage);
        publisher.publish(streamMessage);
        publisher.publish(objectMessage);
        try
        {
            textMessage.setText("test");
            fail();
        } 
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        try
        {
            mapMessage.setObject("name", "value");
            fail();
        } 
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        try
        {
            bytesMessage.writeInt(1);
            fail();
        } 
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        try
        {
            streamMessage.writeLong(2);
            fail();
        } 
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        try
        {
            objectMessage.setObject("Object");
            fail();
        } 
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        try
        {
            textMessage.setStringProperty("test", "test");
            fail();
        } 
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        textMessage.setReadOnly(false);
        textMessage.setText("test");
        try
        {
            textMessage.setStringProperty("test", "test");
            fail();
        } 
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        textMessage.setReadOnlyProperties(false);
        textMessage.setStringProperty("test", "test");
    }
    
    public void testReadOnlyDoCloneOnSend() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockQueueConnection connection = new MockQueueConnection(destManager, confManager);
        MockQueueSender sender = new MockQueueSender(connection, new MockSession(connection, true, Session.CLIENT_ACKNOWLEDGE), queue);
        MockTopicPublisher publisher = new MockTopicPublisher(connection, new MockSession(connection, true, Session.CLIENT_ACKNOWLEDGE), topic);
        MockTextMessage textMessage = new MockTextMessage();
        MockMapMessage mapMessage = new MockMapMessage();
        MockBytesMessage bytesMessage = new MockBytesMessage();
        MockStreamMessage streamMessage = new MockStreamMessage();
        MockObjectMessage objectMessage = new MockObjectMessage();
        confManager.setDoCloneOnSend(true);
        publisher.publish(textMessage);
        publisher.publish(mapMessage);
        sender.send(bytesMessage);
        sender.send(streamMessage);
        sender.send(objectMessage);
        textMessage.setText("test");
        mapMessage.setObject("name", "value");
        bytesMessage.writeInt(1);
        streamMessage.writeLong(2);
        objectMessage.setObject("Object");
        textMessage.setStringProperty("test", "test");
    }
  
    public void testSendWithQueueSender() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockQueueConnection connection = new MockQueueConnection(destManager, confManager);
        MockQueueSender sender = new MockQueueSender(connection, new MockSession(connection, true, Session.CLIENT_ACKNOWLEDGE), queue);
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
        MockTopicConnection connection = new MockTopicConnection(destManager, confManager);
        MockTopicPublisher publisher = new MockTopicPublisher(connection, new MockSession(connection, true, Session.CLIENT_ACKNOWLEDGE), topic);
        doTestSendMessage(publisher);
        assertEquals(1, topic.getCurrentMessageList().size());
        assertEquals(1, topic.getReceivedMessageList().size());
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(0, queue.getReceivedMessageList().size());
        assertEquals(new MockTextMessage("aMessage"), topic.getMessage());
        doTestSendWithInvalidParameters(publisher);
    }
    
    public void testSendWithMessageProducerToTopic() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockConnection connection = new MockConnection(destManager, confManager);
        MockSession session = new MockSession(connection, true, Session.CLIENT_ACKNOWLEDGE);
        MockMessageProducer producer = new MockMessageProducer(connection, session, topic);
        doTestSendMessage(producer);
        assertEquals(1, topic.getCurrentMessageList().size());
        assertEquals(1, topic.getReceivedMessageList().size());
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(0, queue.getReceivedMessageList().size());
        assertEquals(new MockTextMessage("aMessage"), topic.getMessage());
        doTestSendWithInvalidParameters(producer);
        session.setMessageListener(new TestMessageListener());
        topic.addMessage(new MockTextMessage("anotherMessage"));
        assertEquals(0, topic.getCurrentMessageList().size());
        assertEquals(2, topic.getReceivedMessageList().size());
    }
    
    public void testSendWithMessageProducerToQueue() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockConnection connection = new MockConnection(destManager, confManager);
        MockSession session = new MockSession(connection, true, Session.CLIENT_ACKNOWLEDGE);
        MockMessageProducer producer = new MockMessageProducer(connection, session, queue);
        doTestSendMessage(producer);
        assertEquals(0, topic.getCurrentMessageList().size());
        assertEquals(0, topic.getReceivedMessageList().size());
        assertEquals(1, queue.getCurrentMessageList().size());
        assertEquals(1, queue.getReceivedMessageList().size());
        assertEquals(new MockTextMessage("aMessage"), queue.getMessage());
        doTestSendWithInvalidParameters(producer);
        session.setMessageListener(new TestMessageListener());
        queue.addMessage(new MockTextMessage("anotherMessage"));
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(2, queue.getReceivedMessageList().size());
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
    
    public void testAddSessionOnSend() throws Exception
    {
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        MockConnection connection = new MockConnection(destManager, confManager);
        TestQueue testQueue = new TestQueue();
        TestTopic testTopic = new TestTopic();
        MockSession session1 = new MockSession(connection, true, Session.CLIENT_ACKNOWLEDGE);
        MockSession session2 = new MockSession(connection, true, Session.CLIENT_ACKNOWLEDGE);
        MockMessageProducer producer1 = new MockMessageProducer(connection, session1, null);
        MockMessageProducer producer2 = new MockMessageProducer(connection, session2, null);
        producer1.send(testQueue, new MockTextMessage());
        producer1.send(testTopic, new MockTextMessage());
        producer2.send(testQueue, new MockTextMessage());
        producer2.send(testTopic, new MockTextMessage());
        assertEquals(2, testQueue.getSessions().size());
        assertEquals(2, testTopic.getSessions().size());
        assertTrue(testQueue.getSessions().contains(session1));
        assertTrue(testQueue.getSessions().contains(session2));
    }
    
    private class TestQueue extends MockQueue
    { 
        public TestQueue()
        {
            super("");
        }
        
        public void addMessage(Message message) throws JMSException
        {

        }
        
        public String getQueueName() throws JMSException
        {
            return null;
        }
        
        public Set getSessions()
        {
            return super.sessionSet();
        }
    }
    
    private class TestTopic extends MockTopic
    { 
        public TestTopic()
        {
            super("");
        }
        
        public void addMessage(Message message) throws JMSException
        {

        }
       
        public String getTopicName() throws JMSException
        {
            // TODO Auto-generated method stub
            return null;
        }
        
        public Set getSessions()
        {
            return super.sessionSet();
        }
    }
    
    private class TestMessageListener implements MessageListener
    {
        public void onMessage(Message message)
        {

        }
    }
}
