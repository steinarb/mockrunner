package com.mockrunner.example.jms;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Queue;

import org.mockejb.MockEjbObject;
import org.mockejb.TransactionPolicy;

import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.example.jms.interfaces.PrintSession;
import com.mockrunner.jms.JMSTestCaseAdapter;
import com.mockrunner.mock.jms.MockTextMessage;

/**
 * Example test for {@link PrintSessionBean}. Demonstrates
 * how to deal with EJBs in JMS tests. The sender is a session
 * bean, the receiver is a message driven bean. Please note
 * that you have to deal the the transaction methods of
 * {@link com.mockrunner.ejb.EJBTestModule}, because this example
 * uses a simulated JTA transaction.
 */
public class PrintSessionBeanTest extends JMSTestCaseAdapter
{
    private EJBTestModule ejbModule;
    private PrintSession bean;

    protected void setUp() throws Exception
    {
        super.setUp();
        ejbModule = createEJBTestModule();
        ejbModule.bindToContext("java:/ConnectionFactory", getJMSMockObjectFactory().getMockQueueConnectionFactory());
        Queue queue = getDestinationManager().createQueue("testQueue");
        ejbModule.bindToContext("queue/testQueue", queue);
        ejbModule.setInterfacePackage("com.mockrunner.example.jms.interfaces");
        ejbModule.deploy("com/mockrunner/example/PrintSession", PrintSessionBean.class, TransactionPolicy.REQUIRED);
        bean = (PrintSession)ejbModule.lookupBean("com/mockrunner/example/PrintSession");
        MockEjbObject ejbObject = ejbModule.deployMessageBean(PrintMessageDrivenBean.class, TransactionPolicy.REQUIRED);
        MessageListener listener = ejbModule.createMessageBean(ejbObject);
        registerTestMessageListenerForQueue("testQueue", listener);
    }
    
    public void testSuccessfulDelivery() throws Exception
    {
        bean.sendMessage("123");
        ejbModule.verifyCommitted();
        verifyNumberQueueSessions(1);
        verifyNumberQueueSenders(0, "testQueue", 1);
        verifyAllQueueSessionsClosed();
        verifyAllQueueSendersClosed(0);
        verifyQueueConnectionClosed();
        verifyNumberOfCreatedQueueTextMessages(0, 1);
        verifyNumberOfReceivedQueueMessages("testQueue", 1);
        verifyReceivedQueueMessageEquals("testQueue", 0, new MockTextMessage("123"));
        verifyReceivedQueueMessageAcknowledged("testQueue", 0);
    }
    
    public void testDeliveryMoreMessages() throws Exception
    {
        bean.sendMessage("1");
        ejbModule.verifyCommitted();
        bean.sendMessage("2");
        ejbModule.verifyCommitted();
        bean.sendMessage("3");
        ejbModule.verifyCommitted();
        verifyNumberOfReceivedQueueMessages("testQueue", 3);
        verifyAllReceivedQueueMessagesAcknowledged("testQueue");
        verifyReceivedQueueMessageEquals("testQueue", 0, new MockTextMessage("1"));
        verifyReceivedQueueMessageEquals("testQueue", 1, new MockTextMessage("2"));
        verifyReceivedQueueMessageEquals("testQueue", 2, new MockTextMessage("3"));    
    }
    
    public void testFailure() throws Exception
    {
        getJMSMockObjectFactory().getMockQueueConnectionFactory().setJMSException(new JMSException("TestException"));
        bean.sendMessage("1");
        ejbModule.verifyNotCommitted();
        ejbModule.verifyRolledBack();
    }
}
