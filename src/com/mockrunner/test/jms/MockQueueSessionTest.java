package com.mockrunner.test.jms;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import junit.framework.TestCase;

import com.mockrunner.jms.DestinationManager;
import com.mockrunner.jms.MessageManager;
import com.mockrunner.jms.TransmissionManager;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueSession;

public class MockQueueSessionTest extends TestCase
{
    private MockQueueConnection connection;
    private MockQueueSession session;
     
    protected void setUp() throws Exception
    {
        super.setUp();
        connection = new MockQueueConnection();
        session = (MockQueueSession)connection.createQueueSession(false, QueueSession.CLIENT_ACKNOWLEDGE);
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
        MessageManager manager = connection.getMessageManager();
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
        assertTrue(queue == managerQueue1);
        assertTrue(queue == managerQueue2);
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
        assertNotNull(manager.getTemporaryQueue(0));
        assertNotNull(manager.getTemporaryQueue(1));
        assertNotNull(manager.getTemporaryQueue(2));
        assertNull(manager.getTemporaryQueue(3));
        assertTrue(tempQueue == manager.getTemporaryQueue(1));
    }
    
    public void testCreateSenderAndReceiver() throws Exception
    {
        DestinationManager destManager = connection.getDestinationManager();
        Queue queue1 = destManager.createQueue("Queue1");
        Queue queue2 = destManager.createQueue("Queue2");
        QueueSender sender = session.createSender(queue1);
        session.createSender(queue2);
        session.createSender(queue1);
        TransmissionManager transManager = connection.getTransmissionManager();
        assertNotNull(transManager.getQueueSender(0));
        assertNotNull(transManager.getQueueSender(1));
        assertNotNull(transManager.getQueueSender(2));
        assertNull(transManager.getQueueSender(3));
        assertTrue(sender == transManager.getQueueSender(0));
        assertTrue(queue1 == transManager.getQueueSender(0).getQueue());
        assertTrue(queue2 == transManager.getQueueSender(1).getQueue());
        assertTrue(queue1 == transManager.getQueueSender(2).getQueue());
        session.createReceiver(queue1);
        QueueReceiver receiver = session.createReceiver(queue2);
        assertNotNull(transManager.getQueueReceiver(0));
        assertNotNull(transManager.getQueueReceiver(1));
        assertNull(transManager.getQueueReceiver(2));
        assertTrue(receiver == transManager.getQueueReceiver(1));
        assertTrue(queue1 == transManager.getQueueReceiver(0).getQueue());
        assertTrue(queue2 == transManager.getQueueReceiver(1).getQueue());
        QueueBrowser browser = session.createBrowser(queue2);
        assertNotNull(transManager.getQueueBrowser(0));
        assertNull(transManager.getQueueBrowser(1));
        assertTrue(browser == transManager.getQueueBrowser(0));
        assertTrue(queue2 == transManager.getQueueBrowser(0).getQueue());
    }
}
