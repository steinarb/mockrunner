package com.mockrunner.test.jms;

import javax.jms.ConnectionConsumer;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.QueueSession;

import com.mockrunner.mock.jms.MockQueueConnection;

import junit.framework.TestCase;

public class MockQueueConnectionTest extends TestCase
{
    private MockQueueConnection connection;
     
    protected void setUp() throws Exception
    {
        super.setUp();
        connection = new MockQueueConnection();
    }

    public void testException() throws Exception
    {
        connection.createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        JMSException exception = new JMSException("MyReason");
        TestExceptionListener listener = new TestExceptionListener();
        connection.setExceptionListener(listener);
        connection.setJMSException(exception);
        try
        {
            connection.createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
            fail();
        }
        catch(JMSException exc)
        {
            assertTrue(exception == exc);
            assertTrue(exception == listener.getException());
        }
        connection.start();
        ConnectionConsumer consumer = connection.createConnectionConsumer(null, null, null, 0);
        consumer.getServerSessionPool();
        exception = new JMSException("MyReason");
        connection.setJMSException(exception);
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
