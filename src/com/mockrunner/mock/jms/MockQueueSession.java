package com.mockrunner.mock.jms;

import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

/**
 * Mock implementation of JMS <code>QueueSession</code>.
 * Please note that message selectors are not supported
 * at the moment.
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
        getConnection().throwJMSException();
        return createReceiver(queue, null);
    }

    public QueueReceiver createReceiver(Queue queue, String messageSelector) throws JMSException
    {
        getConnection().throwJMSException();
        if(!(queue instanceof MockQueue))
        {
            throw new InvalidDestinationException("queue must be an instance of MockQueue");
        }
        addSessionToQueue(queue);
        return getQueueTransmissionManager().createQueueReceiver((MockQueue)queue, messageSelector);
    }

    public QueueSender createSender(Queue queue) throws JMSException
    {
        getConnection().throwJMSException();
        if(!(queue instanceof MockQueue))
        {
            throw new InvalidDestinationException("queue must be an instance of MockQueue");
        }
        addSessionToQueue(queue);
        return getQueueTransmissionManager().createQueueSender((MockQueue)queue);
    }

    public QueueBrowser createBrowser(Queue queue) throws JMSException
    {
        getConnection().throwJMSException();
        return createBrowser(queue, null);
    }

    public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException
    {
        getConnection().throwJMSException();
        if(!(queue instanceof MockQueue))
        {
            throw new InvalidDestinationException("queue must be an instance of MockQueue");
        }
        addSessionToQueue(queue);
        return getQueueTransmissionManager().createQueueBrowser((MockQueue)queue, messageSelector);
    }
    
    public void close() throws JMSException
    {
        getQueueTransmissionManager().closeAll();
        super.close();
    } 
}
