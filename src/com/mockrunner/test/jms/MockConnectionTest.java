package com.mockrunner.test.jms;

import javax.jms.ConnectionConsumer;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.QueueSession;

import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockQueueSession;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicConnection;
import com.mockrunner.mock.jms.MockTopicPublisher;
import com.mockrunner.mock.jms.MockTopicSession;
import com.mockrunner.mock.jms.MockTopicSubscriber;

import junit.framework.TestCase;

public class MockConnectionTest extends TestCase
{
    private MockQueueConnection queueConnection;
    private MockTopicConnection topicConnection;
     
    protected void setUp() throws Exception
    {
        super.setUp();
        queueConnection = new MockQueueConnection();
        topicConnection = new MockTopicConnection();
    }
    
    public void testClose() throws Exception
    {
        MockQueueSession queueSession1 = (MockQueueSession)queueConnection.createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        MockQueueSession queueSession2 = (MockQueueSession)queueConnection.createQueueSession(false, QueueSession.CLIENT_ACKNOWLEDGE);
        MockQueueSession queueSession3 = (MockQueueSession)queueConnection.createQueueSession(false, QueueSession.CLIENT_ACKNOWLEDGE);
        MockQueueSender sender1 = (MockQueueSender)queueSession1.createSender(new MockQueue(queueConnection, ""));
        MockQueueSender sender2 = (MockQueueSender)queueSession1.createSender(new MockQueue(queueConnection, ""));
        MockQueueReceiver receiver1 = (MockQueueReceiver)queueSession1.createReceiver(new MockQueue(queueConnection, ""));
        queueConnection.close();
        assertTrue(queueConnection.isClosed());
        assertTrue(queueSession1.isClosed());
        assertTrue(queueSession2.isClosed());
        assertTrue(queueSession3.isClosed());
        assertTrue(sender1.isClosed());
        assertTrue(sender2.isClosed());
        assertTrue(receiver1.isClosed());
        assertTrue(queueSession1.isRolledBack());
        assertFalse(queueSession2.isRolledBack());
        assertFalse(queueSession3.isRolledBack());
        assertTrue(queueSession1.isRecovered());
        assertFalse(queueSession2.isRecovered());
        assertFalse(queueSession3.isRecovered());
        MockTopicSession topicSession1 = (MockTopicSession)topicConnection.createTopicSession(false, QueueSession.CLIENT_ACKNOWLEDGE);
        MockTopicSession topicSession2 = (MockTopicSession)topicConnection.createTopicSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        MockTopicPublisher publisher1 = (MockTopicPublisher)topicSession2.createPublisher(new MockTopic(topicConnection, ""));
        MockTopicSubscriber subscriber1 = (MockTopicSubscriber)topicSession2.createSubscriber(new MockTopic(topicConnection, ""));
        MockTopicSubscriber subscriber2 = (MockTopicSubscriber)topicSession2.createSubscriber(new MockTopic(topicConnection, ""));
        topicConnection.close();
        assertTrue(topicConnection.isClosed());
        assertTrue(topicSession1.isClosed());
        assertTrue(topicSession2.isClosed());
        assertTrue(publisher1.isClosed());
        assertTrue(subscriber1.isClosed());
        assertTrue(subscriber2.isClosed());
        assertFalse(topicSession1.isRolledBack());
        assertTrue(topicSession2.isRolledBack());
        assertFalse(topicSession1.isRecovered());
        assertTrue(topicSession2.isRecovered());
    }

    public void testException() throws Exception
    {
        queueConnection.createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        JMSException exception = new JMSException("MyReason");
        TestExceptionListener listener = new TestExceptionListener();
        queueConnection.setExceptionListener(listener);
        queueConnection.setJMSException(exception);
        try
        {
            queueConnection.createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
            fail();
        }
        catch(JMSException exc)
        {
            assertTrue(exception == exc);
            assertTrue(exception == listener.getException());
        }
        queueConnection.start();
        ConnectionConsumer consumer = queueConnection.createConnectionConsumer(null, null, null, 0);
        consumer.getServerSessionPool();
        exception = new JMSException("MyReason");
        queueConnection.setJMSException(exception);
        try
        {
            consumer.getServerSessionPool();
            fail();
        }
        catch(JMSException exc)
        {
            assertTrue(exception == exc);
            assertTrue(exception == listener.getException());
        }
        consumer.getServerSessionPool();
    }
    
    private static class TestExceptionListener implements ExceptionListener
    {
        private JMSException exception;
        
        public void onException(JMSException exception)
        {
            this.exception = exception;
        }
        
        public JMSException getException()
        {
            return exception;
        }
    }
}
