package com.mockrunner.example.jms;

import javax.jms.Queue;
import javax.jms.QueueReceiver;

import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.jms.JMSTestCaseAdapter;
import com.mockrunner.servlet.ServletTestModule;

public class PrintMessageServletTest extends JMSTestCaseAdapter
{
    private EJBTestModule ejbModule;
    private ServletTestModule servletModule;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        ejbModule = new EJBTestModule(createEJBMockObjectFactory());
        ejbModule.bindToContext("java:/ConnectionFactory", getJMSMockObjectFactory().getMockQueueConnectionFactory());
        Queue queue = getDestinationManager().createQueue("testQueue");
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
}
