package com.mockrunner.jms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockTemporaryQueue;

/**
 * Manages the <code>Queue</code> and <code>Topic</code> objects
 * that can be used as destinations in JMS tests.
 */
public class DestinationManager
{
    private Map queues;
    private List tempQueues;
    
    public DestinationManager()
    {
        queues = new HashMap();
        tempQueues = new ArrayList();
    }
    
    /**
     * Creates a new <code>Queue</code> that is available
     * for {@link com.mockrunner.mock.jms.MockQueueSession#createQueue}
     * calls.
     * @param name the name of the Queue
     * @return the created <code>Queue</code>
     */
    public MockQueue createQueue(String name)
    {
        MockQueue queue = new MockQueue(name);
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
        MockTemporaryQueue queue = new MockTemporaryQueue();
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
}
