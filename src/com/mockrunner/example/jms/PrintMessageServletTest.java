package com.mockrunner.example.jms;

import javax.jms.JMSException;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;

import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.jms.JMSTestCaseAdapter;
import com.mockrunner.mock.jms.MockObjectMessage;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockTextMessage;
import com.mockrunner.servlet.ServletTestModule;

public class PrintMessageServletTest extends JMSTestCaseAdapter
{
    private EJBTestModule ejbModule;
    private ServletTestModule servletModule;
    private MockQueue queue;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        ejbModule = new EJBTestModule(createEJBMockObjectFactory());
        ejbModule.bindToContext("java:/ConnectionFactory", getJMSMockObjectFactory().getMockQueueConnectionFactory());
        queue = getDestinationManager().createQueue("testQueue");
        ejbModule.bindToContext("queue/testQueue", queue);
        servletModule = new ServletTestModule(createWebMockObjectFactory());
        servletModule.createServlet(PrintMessageServlet.class);
    }

    public void testInitPrintMessageReceiver() throws Exception
    {
        verifyQueueConnectionStarted();
        verifyNumberQueueSessions(1);
        verifyNumberQueueReceivers(0, "testQueue", 1);
        QueueReceiver receiver = getQueueTransmissionManager(0).getQueueReceiver("testQueue");
        assertTrue(receiver.getMessageListener() instanceof PrintMessageListener);
    }
    
    public void testSuccessfulDelivery() throws Exception
    {
        servletModule.addRequestParameter("customerId", "1");
        servletModule.doGet();
        verifyNumberQueueSessions(1);
        verifyNumberQueueSenders(0, "testQueue", 1);
        verifyAllQueueSessionsClosed();
        verifyAllQueueSendersClosed(0);
        verifyQueueConnectionClosed();
        verifyNumberOfCreatedQueueTextMessages(0, 1);
        verifyNumberOfReceivedQueueMessages("testQueue", 1);
        verifyReceivedQueueMessageEquals("testQueue", 0, new MockTextMessage("1"));
        verifyReceivedQueueMessageAcknowledged("testQueue", 0);
    }
    
    public void testDeliveryMoreMessages() throws Exception
    {
        servletModule.addRequestParameter("customerId", "1");
        servletModule.doGet();
        servletModule.addRequestParameter("customerId", "2");
        servletModule.doGet();
        servletModule.addRequestParameter("customerId", "3");
        servletModule.doGet();
        verifyNumberOfReceivedQueueMessages("testQueue", 3);
        verifyAllReceivedQueueMessagesAcknowledged("testQueue");
        QueueSender sender = getQueueTransmissionManager(0).createQueueSender(queue);
        sender.send(new MockObjectMessage(new Integer(3)));
        verifyNumberOfReceivedQueueMessages("testQueue", 4);
        verifyReceivedQueueMessageNotAcknowledged("testQueue", 3);
    }
    
    public void testServletResponse() throws Exception
    {
        servletModule.setCaseSensitive(false);
        servletModule.addRequestParameter("customerId", "1");
        servletModule.doGet();
        servletModule.verifyOutputContains("successfully");
        servletModule.clearOutput();
        getCurrentQueueConnection().setJMSException(new JMSException("TestException"));
        servletModule.doGet();
        servletModule.verifyOutputContains("error");
    }
}
