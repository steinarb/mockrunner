package com.mockrunner.jms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockTemporaryQueue;

public class DestinationManager
{
    private MockQueueConnection connection;
    private Map queues;
    private List tempQueues;
    private List queueSender;
    private List queueReceiver;
    
    public DestinationManager(MockQueueConnection connection)
    {
        queues = new HashMap();
        tempQueues = new ArrayList();
        queueSender = new ArrayList();
        queueReceiver = new ArrayList();
        this.connection = connection;
    }
    
    /**
     * Creates a new <code>Queue</code> that is available
     * for {@link com.mockrunner.mock.jms.MockQueueSession#createQueue}
     * calls. Before {@link com.mockrunner.mock.jms.MockQueueSession#createQueue}
     * can be sucessfully called, you have to create a <code>Queue</code>
     * with this method.
     * @param name the name of the Queue
     * @return the created <code>Queue</code>
     */
    public MockQueue createQueue(String name)
    {
        MockQueue queue = new MockQueue(connection, name);
        queues.put(name, queue);
        return queue;
    }
    
    /**
     * Removes a formerly created <code>Queue</code>.
     * @param name the name of the Queue
     */
    public void removeQueue(String name)
    {
        queues.remove(name);
    }
    
    /**
     * Returns a <code>Queue</code> that was created with
     * {@link #createQueue} or <code>null</code> if no such
     * <code>Queue</code> is present.
     * @param name the name of the Queue
     * @return the <code>Queue</code>
     */
    public MockQueue getQueue(String name)
    {
        return (MockQueue)queues.get(name);
    }
    
    /**
     * Creates a new <code>TemporaryQueue</code>. Unlike normal
     * queues, you do not have to create temporary queues directly.
     * The {@link com.mockrunner.mock.jms.MockQueueSession#createTemporaryQueue}
     * method calls this method to create the queue. Creating temporary queues
     * is not an administrative act, so we do not have to simulate the
     * available temporary queues.
     * @return the created <code>TemporaryQueue</code>
     */
    public MockTemporaryQueue createTemporaryQueue()
    {
        MockTemporaryQueue queue = new MockTemporaryQueue(connection);
        tempQueues.add(queue);
        return queue;
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
        return (MockTemporaryQueue)tempQueues.get(index);
    }
    
    /**
     * Creates a new <code>QueueSender</code> for the specified
     * <code>Queue</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createSender}.
     * @param queue the <code>Queue</code>
     * @return the created <code>QueueSender</code>
     */
    public MockQueueSender createQueueSender(MockQueue queue)
    {
        MockQueueSender sender = new MockQueueSender(connection, queue);
        queueSender.add(sender);
        return sender;
    }

    /**
     * Returns a <code>QueueSender</code> by its index resp.
     * <code>null</code>, if no such <code>QueueSender</code> is
     * present.
     * @param index the index of the <code>QueueSender</code>
     * @return the <code>QueueSender</code>
     */
    public MockQueueSender getQueueSender(int index)
    {
        return (MockQueueSender)queueSender.get(index);
    }
    
    /**
     * Creates a new <code>QueueReceiver</code> for the specified
     * <code>Queue</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createReceiver}.
     * @param queue the <code>Queue</code>
     * @return the created <code>QueueReceiver</code>
     */
    public MockQueueReceiver createQueueReceiver(MockQueue queue)
    {
        MockQueueReceiver receiver = new MockQueueReceiver(connection, queue);
        queueReceiver.add(receiver);
        return receiver;
    }

    /**
     * Returns a <code>QueueReceiver</code> by its index resp.
     * <code>null</code>, if no such <code>QueueReceiver</code> is
     * present.
     * @param index the index of the <code>QueueReceiver</code>
     * @return the <code>QueueReceiver</code>
     */
    public MockQueueReceiver getQueueReceiver(int index)
    {
        return (MockQueueReceiver)queueReceiver.get(index);
    }
}
