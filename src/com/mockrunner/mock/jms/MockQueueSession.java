package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TemporaryQueue;

/**
 * Mock implementation of JMS <code>QueueSession</code>.
 */
public class MockQueueSession extends MockSession implements QueueSession
{
    private List tempQueues;
    
    public MockQueueSession(MockConnection connection, boolean transacted, int acknowledgeMode)
    {
        super(connection, transacted, acknowledgeMode);
        tempQueues = new ArrayList();
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
        MockQueue queue = getConnection().getDestinationManager().getQueue(name);
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
            throw new JMSException("queue must be an instance of MockQueue");
        }
        addSessionToQueue(queue);
        return getTransmissionManager().createQueueReceiver((MockQueue)queue, messageSelector);
    }

    public QueueSender createSender(Queue queue) throws JMSException
    {
        getConnection().throwJMSException();
        if(!(queue instanceof MockQueue))
        {
            throw new JMSException("queue must be an instance of MockQueue");
        }
        addSessionToQueue(queue);
        return getTransmissionManager().createQueueSender((MockQueue)queue);
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
            throw new JMSException("queue must be an instance of MockQueue");
        }
        addSessionToQueue(queue);
        return getTransmissionManager().createQueueBrowser((MockQueue)queue, messageSelector);
    }

    private void addSessionToQueue(Queue queue)
    {
        if(queue instanceof MockQueue)
        {
            ((MockQueue)queue).addQueueSession(this);
        }
    }
}
