package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

/**
 * Mock implementation of JMS <code>QueueSession</code>.
 */
public class MockQueueSession extends MockSession implements QueueSession
{
    public MockQueueSession(MockQueueConnection connection)
    {
        this(connection, false, Session.AUTO_ACKNOWLEDGE);
    }
    
    public MockQueueSession(MockQueueConnection connection, boolean transacted, int acknowledgeMode)
    {
        super(connection, transacted, acknowledgeMode);
    }

    public QueueReceiver createReceiver(Queue queue) throws JMSException
    {
        return (QueueReceiver)createConsumer(queue);
    }

    public QueueReceiver createReceiver(Queue queue, String messageSelector) throws JMSException
    { 
        return (QueueReceiver)createConsumer(queue, messageSelector);
    }

    public QueueSender createSender(Queue queue) throws JMSException
    {
        return (QueueSender)createProducer(queue);
    }
    
    protected MessageProducer createProducerForNullDestination()
    {
        return getGenericTransmissionManager().createQueueSender();
    }
}
