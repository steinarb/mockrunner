package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TemporaryQueue;

import com.mockrunner.jms.QueueTransmissionManager;

/**
 * Mock implementation of JMS <code>QueueSession</code>.
 */
public class MockQueueSession extends MockSession implements QueueSession
{
    private QueueTransmissionManager queueTransManager;
    private List tempQueues;
    
    public MockQueueSession(MockQueueConnection connection)
    {
        this(connection, false, Session.AUTO_ACKNOWLEDGE);
    }
    
    public MockQueueSession(MockQueueConnection connection, boolean transacted, int acknowledgeMode)
    {
        super(connection, transacted, acknowledgeMode);
        queueTransManager = new QueueTransmissionManager(connection, this);
        tempQueues = new ArrayList();
    }
    
    /**
     * Returns the {@link com.mockrunner.jms.QueueTransmissionManager}.
     * @return the {@link com.mockrunner.jms.QueueTransmissionManager}
     */
    public QueueTransmissionManager getQueueTransmissionManager()
    {
        return queueTransManager;
    }
    
    /**
     * Returns the list of temporary queues.
     * @return the <code>TemporaryQueue</code> list
     */
    public List getTemporaryQueueList()
    {
        return Collections.unmodifiableList(tempQueues);
    }
    
    /**
     * Returns a <code>TemporaryQueue</code> by its index. The
     * index represent the number of the queue. Returns <code>null</code>
     * if no such <code>TemporaryQueue</code> is present.
     * @param index the index
     * @return the <code>TemporaryQueue</code>
     */
    public MockTemporaryQueue getTemporaryQueue(int index)
    {
        if(tempQueues.size() <= index || index < 0) return null;
        return (MockTemporaryQueue)tempQueues.get(index);
    }
    
    public Queue createQueue(String name) throws JMSException
    {
        getConnection().throwJMSException();
        MockQueue queue = ((MockQueueConnection)getConnection()).getQueueManager().getQueue(name);
        if(null == queue)
        {
            throw new JMSException("Queue with name " + name + " not found");
        }
        addSessionToQueue(queue);
        return queue;
    }
    
    public TemporaryQueue createTemporaryQueue() throws JMSException
    {
        getConnection().throwJMSException();
        MockTemporaryQueue queue = new MockTemporaryQueue(getConnection());
        tempQueues.add(queue);
        addSessionToQueue(queue);
        return queue;
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
        return queueTransManager.createQueueReceiver((MockQueue)queue, messageSelector);
    }

    public QueueSender createSender(Queue queue) throws JMSException
    {
        getConnection().throwJMSException();
        if(!(queue instanceof MockQueue))
        {
            throw new InvalidDestinationException("queue must be an instance of MockQueue");
        }
        addSessionToQueue(queue);
        return queueTransManager.createQueueSender((MockQueue)queue);
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
        return queueTransManager.createQueueBrowser((MockQueue)queue, messageSelector);
    }
    
    public void close() throws JMSException
    {
        queueTransManager.closeAll();
        super.close();
    }

    private void addSessionToQueue(Queue queue)
    {
        if(queue instanceof MockQueue)
        {
            ((MockQueue)queue).addQueueSession(this);
        }
    }
}
