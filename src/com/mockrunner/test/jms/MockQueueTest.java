package com.mockrunner.test.jms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.QueueSession;

import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSession;
import com.mockrunner.mock.jms.MockTextMessage;

import junit.framework.TestCase;

public class MockQueueTest extends TestCase
{
    private MockQueueConnection connection;
    private MockQueue queue;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        connection = new MockQueueConnection();
        queue = new MockQueue(connection, "TestQueue");
    }

    public void testGetMessageList() throws Exception
    {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(0, queue.getReceivedMessageList().size());
        assertNull(queue.getMessage());
        queue.addMessage(new MockTextMessage("test1"));
        queue.addMessage(new MockTextMessage("test2"));
        queue.addMessage(new MockTextMessage("test3"));
        assertFalse(queue.isEmpty());
        assertEquals(3, queue.getCurrentMessageList().size());
        assertEquals(3, queue.getReceivedMessageList().size());
        assertEquals("test1", queue.getMessage().toString());
        assertFalse(queue.isEmpty());
        assertEquals(2, queue.getCurrentMessageList().size());
        assertEquals(3, queue.getReceivedMessageList().size());
        assertEquals("test2", queue.getMessage().toString());
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getCurrentMessageList().size());
        assertEquals(3, queue.getReceivedMessageList().size());
        assertEquals("test3", queue.getMessage().toString());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(3, queue.getReceivedMessageList().size());
        assertNull(queue.getMessage());
    }
    
    public void testAddMessage() throws Exception
    {
        MockQueueSession session = new MockQueueSession(connection, false, QueueSession.CLIENT_ACKNOWLEDGE);
        queue.addQueueSession(session);
        queue.addMessage(new MockTextMessage("test"));
        assertEquals(1, queue.getCurrentMessageList().size());
        assertEquals(1, queue.getReceivedMessageList().size());
        assertEquals("test", queue.getMessage().toString());
        TestMessageListener listener1 = new TestMessageListener();
        TestMessageListener listener2 = new TestMessageListener();
        MockQueueReceiver receiver1 = (MockQueueReceiver)session.createReceiver(queue);
        session.setMessageListener(listener1);
        receiver1.setMessageListener(listener2);
        queue.reset();
        queue.addMessage(new MockTextMessage("test"));
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(1, queue.getReceivedMessageList().size());
        assertNull(queue.getMessage());
        assertEquals("test", listener1.getMessage().toString());
        assertNull(listener2.getMessage());
        session.setMessageListener(null);
        queue.reset();
        listener1.reset();
        listener2.reset();
        queue.addMessage(new MockTextMessage("test"));
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(1, queue.getReceivedMessageList().size());
        assertNull(queue.getMessage());
        assertEquals("test", listener2.getMessage().toString());
        assertNull(listener1.getMessage());
        queue.reset();
        listener1.reset();
        listener2.reset();
        MockQueueReceiver receiver2 = (MockQueueReceiver)session.createReceiver(queue);
        receiver1.setMessageListener(null);
        receiver2.setMessageListener(listener2);
        queue.addMessage(new MockTextMessage("test"));
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(1, queue.getReceivedMessageList().size());
        assertNull(queue.getMessage());
        assertEquals("test", listener2.getMessage().toString());
        assertNull(listener1.getMessage());
        queue.reset();
        listener1.reset();
        listener2.reset();
        receiver1.setMessageListener(listener1);
        receiver1.close();
        queue.addMessage(new MockTextMessage("test"));
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(1, queue.getReceivedMessageList().size());
        assertNull(queue.getMessage());
        assertEquals("test", listener2.getMessage().toString());
        assertNull(listener1.getMessage());
        queue.reset();
        listener1.reset();
        listener2.reset();
        receiver2.close();
        connection.stop();
        queue.addMessage(new MockTextMessage("test"));
        assertEquals(1, queue.getCurrentMessageList().size());
        assertEquals(1, queue.getReceivedMessageList().size());
        assertNull(listener1.getMessage());
        assertNull(listener2.getMessage());
        assertEquals("test", queue.getMessage().toString());
        connection.start();
        queue.reset();
        listener1.reset();
        listener2.reset();
        receiver2.close();
        queue.addMessage(new MockTextMessage("test"));
        assertEquals(1, queue.getCurrentMessageList().size());
        assertEquals(1, queue.getReceivedMessageList().size());
        assertNull(listener1.getMessage());
        assertNull(listener2.getMessage());
        assertEquals("test", queue.getMessage().toString());
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
