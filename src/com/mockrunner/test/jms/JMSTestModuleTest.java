package com.mockrunner.test.jms;

import javax.jms.Queue;
import javax.jms.QueueSession;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.mock.jms.JMSMockObjectFactory;

import junit.framework.TestCase;

public class JMSTestModuleTest extends TestCase
{
    private JMSMockObjectFactory mockFactory;
    private JMSTestModule module;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        mockFactory = new JMSMockObjectFactory();
        module = new JMSTestModule(mockFactory);
    }
    
    public void testGetQueue() throws Exception
    {
        DestinationManager manager = mockFactory.getMockQueueConnection().getDestinationManager();
        manager.createQueue("test1");
        manager.createQueue("test2");
        assertNotNull(module.getQueue("test1"));
        assertNotNull(module.getQueue("test2"));
        assertNull(module.getQueue("xyz"));
        manager.removeQueue("test2");
        assertNull(module.getQueue("test2"));
    }
    
    public void testVerifyTemporaryQueue() throws Exception
    {
        mockFactory.getMockQueueConnection().createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        module.getQueueSession(0).createTemporaryQueue();
        module.getQueueSession(0).createTemporaryQueue();
        assertNotNull(module.getTemporaryQueue(0, 0));
        assertNotNull(module.getTemporaryQueue(0, 1));
        assertNull(module.getTemporaryQueue(0, 2));
        module.verifyTemporaryQueuePresent(0, 0);
        module.verifyTemporaryQueuePresent(0, 1);
        try
        {
            module.verifyTemporaryQueuePresent(0, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    public void testVerifyQueueSender() throws Exception
    {
        mockFactory.getMockQueueConnection().createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getMockQueueConnection().getDestinationManager();
        manager.createQueue("queue");
        module.getQueueSession(0).createSender(manager.getQueue("queue"));
        module.verifyQueueSenderPresent(0, 0);
        module.verifyQueueSenderPresent(0, "queue");
        try
        {
            module.verifyQueueSenderPresent(0, 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyQueueSenderPresent(0, "otherQueue");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.getQueueSession(0).createSender(manager.getQueue("queue"));
        module.verifyQueueSenderPresent(0, 1);
    }
    
    public void testVerifyQueueReceiver() throws Exception
    {
        mockFactory.getMockQueueConnection().createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getMockQueueConnection().getDestinationManager();
        Queue queue1 = manager.createQueue("queue");
        Queue queue2 = manager.createQueue("otherQueue");
        module.getQueueSession(0).createReceiver(queue1);
        module.verifyQueueReceiverPresent(0, 0);
        module.verifyQueueReceiverPresent(0, "queue");
        try
        {
            module.verifyQueueReceiverPresent(0, 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyQueueReceiverPresent(0, "otherQueue");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.getQueueSession(0).createReceiver(queue2);
        module.verifyQueueReceiverPresent(0, 1);
        module.verifyQueueReceiverPresent(0, "otherQueue");
    }
    
    public void testVerifyQueueBrowser() throws Exception
    {
        mockFactory.getMockQueueConnection().createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getMockQueueConnection().getDestinationManager();
        manager.createQueue("queue");
        module.getQueueSession(0).createBrowser(manager.getQueue("queue"));
        module.getQueueSession(0).createBrowser(manager.getQueue("queue"));
        module.verifyQueueBrowserPresent(0, 0);
        module.verifyQueueBrowserPresent(0, 1);
        module.verifyQueueBrowserPresent(0, "queue");
        try
        {
            module.verifyQueueBrowserPresent(0, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyQueueBrowserPresent(0, "otherQueue");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }

    public void testVerifySession() throws Exception
    {
        mockFactory.getMockQueueConnection().createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        assertNotNull(module.getQueueSession(0));
        assertNull(module.getQueueSession(1));
        module.verifyQueueSessionPresent(0);
        try
        {
            module.verifyQueueSessionPresent(1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        mockFactory.getMockQueueConnection().createQueueSession(false, QueueSession.CLIENT_ACKNOWLEDGE);
        assertNotNull(module.getQueueSession(1));
        module.verifyQueueSessionPresent(1);
    }
}
