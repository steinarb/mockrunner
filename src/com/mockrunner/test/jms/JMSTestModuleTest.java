package com.mockrunner.test.jms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;

import junit.framework.TestCase;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.mock.jms.JMSMockObjectFactory;

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
        module.verifyNumberTemporaryQueues(0, 2);
        try
        {
            module.verifyNumberTemporaryQueues(0, 3);
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
        module.verifyNumberQueueSenders(0, 1);
        module.verifyQueueSenderPresent(0, "queue");
        try
        {
            module.verifyNumberQueueSenders(0, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberQueueSenders(0, "queue", 2);
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
        module.verifyNumberQueueSenders(0, "queue", 2);
        module.verifyNumberQueueSenders(0, 2);
    }
    
    public void testVerifyQueueReceiver() throws Exception
    {
        mockFactory.getMockQueueConnection().createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getMockQueueConnection().getDestinationManager();
        Queue queue1 = manager.createQueue("queue");
        Queue queue2 = manager.createQueue("otherQueue");
        module.getQueueSession(0).createReceiver(queue1);
        module.verifyNumberQueueReceivers(0, 1);
        module.verifyNumberQueueReceivers(0, "queue", 1);
        module.verifyQueueReceiverPresent(0, "queue");
        try
        {
            module.verifyNumberQueueReceivers(0, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberQueueReceivers(0, "queue", 0);
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
        module.verifyNumberQueueReceivers(0, 2);
        module.verifyNumberQueueReceivers(0, "otherQueue", 1);
        module.verifyQueueReceiverPresent(0, "otherQueue");
    }
    
    public void testVerifyQueueBrowser() throws Exception
    {
        mockFactory.getMockQueueConnection().createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getMockQueueConnection().getDestinationManager();
        manager.createQueue("queue");
        module.getQueueSession(0).createBrowser(manager.getQueue("queue"));
        module.getQueueSession(0).createBrowser(manager.getQueue("queue"));
        module.verifyNumberQueueBrowsers(0, 2);
        module.verifyNumberQueueBrowsers(0, "queue", 2);
        module.verifyQueueBrowserPresent(0, "queue");
        try
        {
            module.verifyNumberQueueBrowsers(0, 3);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberQueueBrowsers(0, "queue", 1);
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
        module.verifyNumberQueueSessions(1);
        try
        {
            module.verifyNumberQueueSessions(2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        mockFactory.getMockQueueConnection().createQueueSession(false, QueueSession.CLIENT_ACKNOWLEDGE);
        assertNotNull(module.getQueueSession(1));
        module.verifyNumberQueueSessions(2);
    }
    
    public void testVerifyNumberMessages() throws Exception
    {
        mockFactory.getMockQueueConnection().createQueueSession(true, QueueSession.CLIENT_ACKNOWLEDGE);
        DestinationManager manager = mockFactory.getMockQueueConnection().getDestinationManager();
        manager.createQueue("queue");
        QueueSender sender1 = module.getQueueSession(0).createSender(manager.getQueue("queue"));
        manager.createQueue("otherQueue");
        QueueSender sender2 = module.getQueueSession(0).createSender(manager.getQueue("otherQueue"));
        QueueSender sender3 = module.getQueueSession(0).createSender(module.getQueueSession(0).createTemporaryQueue());
        QueueReceiver receiver = module.getQueueSession(0).createReceiver(manager.getQueue("otherQueue"));
        receiver.setMessageListener(new MessageListener() { public void onMessage(Message message){} });
        sender1.send(module.getQueueSession(0).createTextMessage());
        sender1.send(module.getQueueSession(0).createTextMessage());
        sender2.send(module.getQueueSession(0).createTextMessage());
        sender2.send(module.getQueueSession(0).createObjectMessage());
        sender2.send(module.getQueueSession(0).createMapMessage());
        sender3.send(module.getQueueSession(0).createMapMessage());
        sender3.send(module.getQueueSession(0).createStreamMessage());
        module.verifyNumberOfTextMessages(0, 3);
        module.verifyNumberOfObjectMessages(0, 1);
        module.verifyNumberOfMapMessages(0, 2);
        module.verifyNumberOfStreamMessages(0, 1);
        module.verifyNumberOfBytesMessages(0, 0);
        module.verifyNumberOfMessages(0, 0);
        try
        {
            module.verifyNumberOfMessages(0, 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberOfMapMessages(1, 2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyNumberOfCurrentMessages("queue", 2);
        module.verifyNumberOfReceivedMessages("queue", 2);
        module.verifyNumberOfCurrentMessages("otherQueue", 0);
        module.verifyNumberOfReceivedMessages("otherQueue", 3);
        module.verifyNumberOfCurrentMessages(0, 0, 2);
        module.verifyNumberOfReceivedMessages(0, 0, 2);
        module.verifyNumberTemporaryQueues(0, 1);
        try
        {
            module.verifyNumberOfReceivedMessages("queue", 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyNumberOfCurrentMessages(0, 0, 0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
}
