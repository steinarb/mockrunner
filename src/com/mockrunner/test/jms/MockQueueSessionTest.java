package com.mockrunner.test.jms;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import junit.framework.TestCase;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.jms.MessageManager;
import com.mockrunner.jms.QueueTransmissionManager;
import com.mockrunner.jms.TransmissionManagerWrapper;
import com.mockrunner.mock.jms.MockBytesMessage;
import com.mockrunner.mock.jms.MockMapMessage;
import com.mockrunner.mock.jms.MockObjectMessage;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueBrowser;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockQueueSession;
import com.mockrunner.mock.jms.MockStreamMessage;
import com.mockrunner.mock.jms.MockTemporaryQueue;
import com.mockrunner.mock.jms.MockTextMessage;

public class MockQueueSessionTest extends TestCase
{
    private MockQueueConnection connection;
    private MockQueueSession session;
    private MockQueueSession anotherSession;
    private MockQueue queue1;
    private MockQueue queue2;
     
    protected void setUp() throws Exception
    {
        super.setUp();
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        connection = new MockQueueConnection(destManager, confManager);
        session = (MockQueueSession)connection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
        anotherSession = (MockQueueSession)connection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
    }
    
    public void testCreateMessages() throws Exception
    {
        session.createTextMessage("Text1");
        TextMessage message = session.createTextMessage();
        message.setText("Text2");
        session.createObjectMessage("Object1");
        session.createMapMessage();
        session.createMapMessage();
        session.createMapMessage();
        session.createStreamMessage();
        MessageManager manager = session.getMessageManager();
        assertEquals("Text1", manager.getTextMessage(0).getText());
        assertEquals("Text2", manager.getTextMessage(1).getText());
        assertNull(manager.getTextMessage(2));
        assertEquals("Object1", manager.getObjectMessage(0).getObject());
        assertNotNull(manager.getMapMessage(0));
        assertNotNull(manager.getMapMessage(1));
        assertNotNull(manager.getMapMessage(2));
        assertNull(manager.getMapMessage(3));
        assertNotNull(manager.getStreamMessage(0));
        assertNull(manager.getStreamMessage(1));
    }
    
    public void testCreateQueues() throws Exception
    {
        try
        {
            session.createQueue("Queue1");
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
        DestinationManager manager = connection.getDestinationManager();
        Queue managerQueue1 = manager.createQueue("Queue1");
        Queue managerQueue2 = manager.getQueue("Queue1");
        Queue queue = session.createQueue("Queue1");
        assertTrue(managerQueue1 == managerQueue2);
        assertTrue(queue == managerQueue1);
        assertEquals("Queue1", queue.getQueueName());
        manager.createQueue("Queue2");
        assertNotNull(session.createQueue("Queue2"));
        manager.removeQueue("Queue1");
        try
        {
            session.createQueue("Queue1");
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
        session.createTemporaryQueue();
        TemporaryQueue tempQueue = session.createTemporaryQueue();
        session.createTemporaryQueue();
        assertNotNull(session.getTemporaryQueue(0));
        assertNotNull(session.getTemporaryQueue(1));
        assertNotNull(session.getTemporaryQueue(2));
        assertNull(session.getTemporaryQueue(3));
        assertTrue(tempQueue == session.getTemporaryQueue(1));
    }
    
    public void testCreateSenderAndReceiver() throws Exception
    {
        DestinationManager manager = connection.getDestinationManager();
        manager.createQueue("Queue1");
        queue1 = manager.createQueue("Queue1");
        queue2 = manager.createQueue("Queue2");
        QueueSender sender = session.createSender(queue1);
        session.createSender(queue2);
        session.createSender(queue1);
        QueueTransmissionManager queueTransManager = session.getQueueTransmissionManager();
        TransmissionManagerWrapper transManager = session.getTransmissionManagerWrapper();
        assertNotNull(queueTransManager.getQueueSender(0));
        assertNotNull(queueTransManager.getQueueSender(1));
        assertNotNull(queueTransManager.getQueueSender(2));
        assertNull(queueTransManager.getQueueSender(3));
        assertEquals(3, queueTransManager.getQueueSenderList().size());
        assertNotNull(transManager.getMessageProducer(0));
        assertNotNull(transManager.getMessageProducer(1));
        assertNotNull(transManager.getMessageProducer(2));
        assertNull(transManager.getMessageProducer(3));
        assertEquals(3, transManager.getMessageProducerList().size());
        assertTrue(sender == queueTransManager.getQueueSender(0));
        assertTrue(sender == queueTransManager.getQueueSender("Queue1"));
        assertTrue(queue1 == queueTransManager.getQueueSender(0).getQueue());
        assertTrue(queue2 == queueTransManager.getQueueSender(1).getQueue());
        assertTrue(queue1 == queueTransManager.getQueueSender(2).getQueue());
        assertTrue(queue1 == queueTransManager.getQueueSender("Queue1").getQueue());
        assertEquals(0, queueTransManager.getQueueReceiverList().size());
        assertEquals(0, transManager.getMessageConsumerList().size());
        session.createReceiver(queue1);
        QueueReceiver receiver = session.createReceiver(queue2);
        assertNotNull(queueTransManager.getQueueReceiver(0));
        assertNotNull(queueTransManager.getQueueReceiver(1));
        assertNotNull(transManager.getMessageConsumer(0));
        assertNotNull(transManager.getMessageConsumer(1));
        assertTrue(receiver == queueTransManager.getQueueReceiver("Queue2"));
        assertTrue("Queue2" == queueTransManager.getQueueReceiver("Queue2").getQueue().getQueueName());
        assertNull(queueTransManager.getQueueReceiver(2));
        assertNull(transManager.getMessageConsumer(2));
        assertTrue(receiver == queueTransManager.getQueueReceiver(1));
        assertTrue(queue1 == queueTransManager.getQueueReceiver(0).getQueue());
        assertTrue(queue2 == queueTransManager.getQueueReceiver(1).getQueue());
        assertEquals(2, queueTransManager.getQueueReceiverList().size());
        assertEquals(2, transManager.getMessageConsumerList().size());
        QueueBrowser browser = session.createBrowser(queue2);
        assertNotNull(queueTransManager.getQueueBrowser(0));
        assertNull(queueTransManager.getQueueBrowser(1));
        assertTrue(browser == queueTransManager.getQueueBrowser(0));
        assertTrue(browser == queueTransManager.getQueueBrowser("Queue2"));
        assertTrue(queue2 == queueTransManager.getQueueBrowser(0).getQueue());
        assertEquals(1, queueTransManager.getQueueBrowserList().size());
    }
    
    public void testTransmissionGlobalListener() throws Exception
    {
        DestinationManager manager = connection.getDestinationManager();
        manager.createQueue("Queue1");
        MockQueue queue = (MockQueue)session.createQueue("Queue1");
        QueueSender sender = session.createSender(queue);
        TestMessageListener globalListener = new TestMessageListener();
        session.setMessageListener(globalListener);
        sender.send(new MockTextMessage("Text1"));
        assertEquals("Text1", ((TextMessage)globalListener.getMessage()).getText());
        QueueReceiver receiver = session.createReceiver(queue);
        TestMessageListener listener = new TestMessageListener();
        receiver.setMessageListener(listener);
        sender.send(new MockObjectMessage("Object1"));
        assertEquals("Object1", ((ObjectMessage)globalListener.getMessage()).getObject());
        assertNull(listener.getMessage());
    }
    
    public void testTransmissionMultipleReceiversWithListener() throws Exception
    {
        DestinationManager manager = connection.getDestinationManager();
        manager.createQueue("Queue1");
        MockQueue queue = (MockQueue)session.createQueue("Queue1");
        QueueSender sender = session.createSender(queue);
        MockQueueReceiver receiver1 = (MockQueueReceiver)session.createReceiver(queue);
        TestMessageListener listener1 = new TestMessageListener();
        receiver1.setMessageListener(listener1);
        MockQueueReceiver receiver2 = (MockQueueReceiver)session.createReceiver(queue);
        TestMessageListener listener2 = new TestMessageListener();
        receiver2.setMessageListener(listener2);
        MockQueueReceiver receiver3 = (MockQueueReceiver)session.createReceiver(queue);
        TestMessageListener listener3 = new TestMessageListener();
        receiver3.setMessageListener(listener3);
        sender.send(new MockTextMessage("Text1"));
        assertEquals("Text1", ((TextMessage)listener1.getMessage()).getText());
        assertNull(listener2.getMessage());
        assertNull(listener3.getMessage());
        assertEquals(1, queue.getReceivedMessageList().size());
        assertEquals(0, queue.getCurrentMessageList().size());
        assertNull(queue.getMessage());
        assertTrue(queue.isEmpty());
        sender.send(new MockTextMessage("Text2"));
        assertEquals("Text2", ((TextMessage)listener1.getMessage()).getText());
        sender.send(new MockTextMessage("Text3"));
        assertEquals("Text3", ((TextMessage)listener1.getMessage()).getText());
        assertEquals(3, queue.getReceivedMessageList().size());
        assertEquals(0, queue.getCurrentMessageList().size());
        manager.createQueue("Queue2");
        MockQueue anotherQueue = (MockQueue)session.createQueue("Queue2");
        sender = session.createSender(anotherQueue);
        MockQueueReceiver receiver4 = (MockQueueReceiver)session.createReceiver(anotherQueue);
        TestMessageListener listener4 = new TestMessageListener();
        receiver4.setMessageListener(listener4);
        sender.send(new MockTextMessage("Text4"));
        sender.send(new MockTextMessage("Text5"));
        assertEquals(3, queue.getReceivedMessageList().size());
        assertEquals(2, anotherQueue.getReceivedMessageList().size());
        assertEquals(0, anotherQueue.getCurrentMessageList().size());
        assertEquals("Text5", ((TextMessage)listener4.getMessage()).getText());
    }
    
    public void testTransmissionMultipleReceiversWithoutListener() throws Exception
    {
        DestinationManager manager = connection.getDestinationManager();
        manager.createQueue("Queue1");
        MockQueue queue = (MockQueue)session.createQueue("Queue1");
        QueueSender sender = session.createSender(queue);
        MockQueueReceiver receiver1 = (MockQueueReceiver)session.createReceiver(queue);
        MockQueueReceiver receiver2 = (MockQueueReceiver)session.createReceiver(queue);
        MockQueueReceiver receiver3 = (MockQueueReceiver)session.createReceiver(queue);
        sender.send(queue, new MockTextMessage("Text1"), 1, 2, 3);
        sender.send(new MockTextMessage("Text2"));
        assertEquals(2, queue.getReceivedMessageList().size());
        assertEquals(2, queue.getCurrentMessageList().size());
        assertEquals(new MockTextMessage("Text1"), receiver1.receive());
        assertEquals(new MockTextMessage("Text2"), receiver3.receiveNoWait());
        assertEquals(0, queue.getCurrentMessageList().size());
        assertNull(queue.getMessage());
        assertTrue(queue.isEmpty());
        assertNull(receiver2.receive(3));
        manager.createQueue("Queue2");
        MockQueue anotherQueue = (MockQueue)session.createQueue("Queue2");
        MockQueueReceiver receiver4 = (MockQueueReceiver)session.createReceiver(anotherQueue);
        sender.send(new MockTextMessage("Text3"));
        assertEquals(3, queue.getReceivedMessageList().size());
        assertEquals(1, queue.getCurrentMessageList().size());
        assertEquals(0, anotherQueue.getReceivedMessageList().size());
        sender = session.createSender(anotherQueue);
        sender.send(new MockTextMessage("Text4"));
        assertEquals(1, anotherQueue.getReceivedMessageList().size());
        assertEquals(new MockTextMessage("Text4"), receiver4.receive());
        sender = session.createSender(queue);
        TestMessageListener listener = new TestMessageListener();
        receiver3.setMessageListener(listener);
        sender.send(new MockTextMessage("Text5"));
        assertEquals(new MockTextMessage("Text5"), listener.getMessage());
    }
    
    public void testTransmissionResetCalled() throws Exception
    {
        DestinationManager manager = connection.getDestinationManager();
        manager.createQueue("Queue1");
        MockQueue queue = (MockQueue)session.createQueue("Queue1");
        QueueSender sender = session.createSender(queue);
        BytesMessage bytesMessage = new MockBytesMessage();
        StreamMessage streamMessage = new MockStreamMessage();
        bytesMessage.writeInt(1);
        streamMessage.writeLong(2);
        sender.send(bytesMessage);
        sender.send(streamMessage);
        bytesMessage = (BytesMessage)queue.getMessage();
        streamMessage = (StreamMessage)queue.getMessage();
        assertEquals(1, bytesMessage.readInt());
        assertEquals(2, streamMessage.readLong());
    }
    
    public void testTransmissionSenderOrReceiverClosed() throws Exception
    {
        DestinationManager manager = connection.getDestinationManager();
        manager.createQueue("Queue");
        MockQueue queue = (MockQueue)session.createQueue("Queue");
        MockQueueReceiver receiver1 = (MockQueueReceiver)session.createReceiver(queue);
        TestMessageListener listener1 = new TestMessageListener();
        receiver1.setMessageListener(listener1);
        QueueSender sender = session.createSender(queue);
        sender.send(new MockTextMessage("Text"));
        assertNull(receiver1.receive());
        assertEquals(new MockTextMessage("Text"), listener1.getMessage());
        listener1.reset();
        receiver1.close();
        sender.send(new MockTextMessage("Text"));
        assertNull(listener1.getMessage());
        try
        {
            receiver1.receive();
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
        MockQueueReceiver receiver2 = (MockQueueReceiver)session.createReceiver(queue); 
        assertEquals(new MockTextMessage("Text"), receiver2.receive());
        TestMessageListener listener2 = new TestMessageListener();
        receiver2.setMessageListener(listener2);
        sender.send(new MockTextMessage("Text"));
        assertEquals(new MockTextMessage("Text"), listener2.getMessage());
        sender.close();
        try
        {
            sender.send(new MockTextMessage("Text"));
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
    }
    
    public void testTransmissionWithMessageSelector() throws Exception
    {
        DestinationManager manager = connection.getDestinationManager();
        manager.createQueue("Queue");
        MockQueue queue = (MockQueue)session.createQueue("Queue");
        MockQueueReceiver receiver1 = (MockQueueReceiver)session.createReceiver(queue, "text = 'test'");
        TestListMessageListener listener1 = new TestListMessageListener();
        receiver1.setMessageListener(listener1);
        QueueSender sender = session.createSender(queue);
        MockBytesMessage message1 = new MockBytesMessage();
        sender.send(message1);
        MockBytesMessage message2 = new MockBytesMessage();
        message2.setStringProperty("text", "test");
        sender.send(message2);
        assertEquals(1, listener1.getMessageList().size());
        assertSame(message2, listener1.getMessageList().get(0));
        assertNull(receiver1.receive());
        MockQueueReceiver receiver2 = (MockQueueReceiver)session.createReceiver(queue);
        assertSame(message1, receiver2.receiveNoWait());
        receiver2.setMessageListener(listener1);
        listener1.clearMessageList();
        sender.send(message1);
        sender.send(message2);
        assertEquals(2, listener1.getMessageList().size());
    }
    
    public void testTransmissionMessageAcknowledged() throws Exception
    {
        MockQueueSession session1 = (MockQueueSession)connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        DestinationManager manager = connection.getDestinationManager();
        manager.createQueue("Queue");
        MockQueue queue = (MockQueue)session1.createQueue("Queue");
        QueueSender sender = session1.createSender(queue);
        MockTextMessage message = new MockTextMessage("Text");
        queue.reset();
        sender.send(message);
        message = (MockTextMessage)queue.getReceivedMessageList().get(0);
        MockQueueReceiver receiver = (MockQueueReceiver)session1.createReceiver(queue);
        assertFalse(message.isAcknowledged());
        receiver.receiveNoWait();
        assertTrue(message.isAcknowledged());
        TestMessageListener listener = new TestMessageListener();
        receiver.setMessageListener(listener);
        message = new MockTextMessage("Text");
        queue.reset();
        sender.send(message);
        message = (MockTextMessage)queue.getReceivedMessageList().get(0);
        assertTrue(message.isAcknowledged());
        receiver.setMessageListener(null);
        session1.setMessageListener(listener);
        message = new MockTextMessage("Text");
        queue.reset();
        sender.send(message);
        message = (MockTextMessage)queue.getReceivedMessageList().get(0);
        assertTrue(message.isAcknowledged());
        session1.setMessageListener(null);
        MockQueueSession session2 = (MockQueueSession)connection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
        receiver = (MockQueueReceiver)session2.createReceiver(queue);
        receiver.setMessageListener(listener);
        sender = session2.createSender(queue);
        message = new MockTextMessage("Text");
        queue.reset();
        sender.send(message);
        message = (MockTextMessage)queue.getReceivedMessageList().get(0);
        assertFalse(message.isAcknowledged());
        receiver.setMessageListener(null);
        message = new MockTextMessage("Text");
        queue.reset();
        sender.send(message);
        message = (MockTextMessage)queue.getReceivedMessageList().get(0);
        assertFalse(message.isAcknowledged());
        receiver.receive();
        assertFalse(message.isAcknowledged());
        session1.setMessageListener(listener);
        message = new MockTextMessage("Text");
        queue.reset();
        sender.send(message);
        message = (MockTextMessage)queue.getReceivedMessageList().get(0);
        assertTrue(message.isAcknowledged());
    }
    
    public void testTransmissionMultipleSessions() throws Exception
    {
        DestinationManager manager = connection.getDestinationManager();
        manager.createQueue("Queue1");
        MockQueue queue = (MockQueue)session.createQueue("Queue1");
        MockQueue sameQueue = (MockQueue)anotherSession.createQueue("Queue1");
        TestListMessageListener listener = new TestListMessageListener();
        session.setMessageListener(listener);
        MockQueueReceiver receiver = (MockQueueReceiver)anotherSession.createReceiver(queue);
        receiver.setMessageListener(listener);
        QueueSender sender = anotherSession.createSender(queue);
        sender.send(new MockTextMessage("Text1"));
        assertEquals(1, queue.getReceivedMessageList().size());
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(1, listener.getMessageList().size());
        assertEquals(new MockTextMessage("Text1"), listener.getMessageList().get(0));
        MockQueueReceiver receiver2 = (MockQueueReceiver)session.createReceiver(queue);
        receiver2.setMessageListener(listener);
        session.setMessageListener(null);
        sender.send(new MockTextMessage("Text2"));
        assertEquals(2, queue.getReceivedMessageList().size());
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(2, listener.getMessageList().size());
        assertEquals(new MockTextMessage("Text2"), listener.getMessageList().get(1));
        MockQueueReceiver receiver3 = (MockQueueReceiver)session.createReceiver(queue);
        receiver3.setMessageListener(listener);
        sender = anotherSession.createSender(sameQueue);
        sender.send(new MockObjectMessage(new Integer(1)));
        assertEquals(3, queue.getReceivedMessageList().size());
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(3, listener.getMessageList().size());
        Object object = listener.getMessageList().get(2);
        assertEquals(new Integer(1), ((MockObjectMessage)object).getObject());
    }
    
    public void testQueueBrowser() throws Exception
    {
        DestinationManager manager = connection.getDestinationManager();
        manager.createQueue("Queue1");
        MockQueue queue = (MockQueue)session.createQueue("Queue1");
        QueueSender sender = session.createSender(queue);
        sender.send(new MockTextMessage("Text"));
        sender.send(new MockObjectMessage("Object"));
        sender.send(new MockMapMessage());
        sender.send(new MockStreamMessage());
        sender.send(new MockBytesMessage());
        QueueBrowser browser = session.createBrowser(queue);
        Enumeration messages = browser.getEnumeration();
        TextMessage message1 = (TextMessage)messages.nextElement();
        assertEquals("Text", message1.getText());
        ObjectMessage message2 = (ObjectMessage)messages.nextElement();
        assertEquals("Object", message2.getObject());
        assertTrue(messages.nextElement() instanceof MockMapMessage);
        assertTrue(messages.nextElement() instanceof MockStreamMessage);
        assertTrue(messages.nextElement() instanceof MockBytesMessage);
        assertFalse(messages.hasMoreElements());
        sender.send(new MockTextMessage("Text"));
        sender.send(new MockObjectMessage("Object"));
        browser.close();
        try
        {
            browser.getEnumeration();
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }     
    }
    
    public void testCloseSession() throws Exception
    {
        DestinationManager manager = connection.getDestinationManager();
        manager.createQueue("Queue");
        MockQueueSession session = (MockQueueSession)connection.createQueueSession(false, QueueSession.CLIENT_ACKNOWLEDGE);
        MockQueue queue = (MockQueue)session.createQueue("Queue");
        MockQueueReceiver receiver1 = (MockQueueReceiver)session.createReceiver(queue);
        MockQueueSender sender1 = (MockQueueSender)session.createSender(queue);
        MockQueueSender sender2 = (MockQueueSender)session.createSender(queue);
        MockQueueBrowser browser1 = (MockQueueBrowser)session.createBrowser(queue);
        session.close();
        assertTrue(session.isClosed());
        assertFalse(session.isRolledBack());
        assertTrue(receiver1.isClosed());
        assertTrue(sender1.isClosed());
        assertTrue(sender2.isClosed());
        assertTrue(browser1.isClosed());
        session = (MockQueueSession)connection.createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        queue = (MockQueue)session.createQueue("Queue");
        receiver1 = (MockQueueReceiver)session.createReceiver(queue);
        sender1 = (MockQueueSender)session.createSender(queue);
        sender2 = (MockQueueSender)session.createSender(queue);
        browser1 = (MockQueueBrowser)session.createBrowser(queue);
        session.close();
        assertTrue(session.isClosed());
        assertTrue(session.isRolledBack());
        assertTrue(receiver1.isClosed());
        assertTrue(sender1.isClosed());
        assertTrue(sender2.isClosed());
        assertTrue(browser1.isClosed());
        session = (MockQueueSession)connection.createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        session.commit();
        session.close();
        assertTrue(session.isClosed());
        assertFalse(session.isRolledBack());
    }
    
    public void testCloseSessionRemove() throws Exception
    {
        DestinationManager manager = connection.getDestinationManager();
        MockQueue queue1 = manager.createQueue("Queue1");
        MockQueue queue2 = manager.createQueue("Queue2");
        MockQueueSession session = (MockQueueSession)connection.createQueueSession(false, QueueSession.CLIENT_ACKNOWLEDGE);
        session.createQueue("Queue1");
        MockTemporaryQueue tempQueue = (MockTemporaryQueue)session.createTemporaryQueue();
        assertTrue(queue1.sessionSet().contains(session));
        assertFalse(queue2.sessionSet().contains(session));
        assertTrue(tempQueue.sessionSet().contains(session));
        session.close();
        assertFalse(queue1.sessionSet().contains(session));
        assertFalse(queue2.sessionSet().contains(session));
        assertFalse(tempQueue.sessionSet().contains(session));
    }
    
    public void testTransmissionWithNullDestination() throws Exception
    {
        MockQueueSession session = (MockQueueSession)connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        DestinationManager manager = connection.getDestinationManager();
        MockQueue queue = (MockQueue)manager.createQueue("Queue");
        QueueSender sender = session.createSender(null);
        MockTextMessage message = new MockTextMessage("Text");
        sender.send(queue, message);
        assertEquals(1, queue.getReceivedMessageList().size());
        assertEquals(1, queue.getCurrentMessageList().size());
    }
    
    public static class TestListMessageListener implements MessageListener
    {
        private List messages = new ArrayList();
    
        public List getMessageList()
        {
            return messages;
        }
        
        public void clearMessageList()
        {
            messages.clear();
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
