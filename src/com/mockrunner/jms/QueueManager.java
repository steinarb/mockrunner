package com.mockrunner.jms;

import java.util.HashMap;
import java.util.Map;

import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueConnection;

/**
 * The <code>QueueManager</code> can be used
 * to create queues, which is normally an 
 * administrative act. Before queues can be
 * used, they have to be created using this class.
 */
public class QueueManager
{
    private MockConnection connection;
    private Map queues;

    public QueueManager(MockQueueConnection connection)
    {
        this.connection = connection;
        queues = new HashMap();
    }
    
    /**
     * Creates a new <code>Queue</code> that is available
     * for {@link com.mockrunner.mock.jms.MockQueueSession#createQueue}
     * calls. Creating queues is an administrative act.
     * Before {@link com.mockrunner.mock.jms.MockQueueSession#createQueue}
     * can be sucessfully called, you have to create a <code>Queue</code>
     * with this method.
     * @param name the name of the <code>Queue</code>
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
     * @param name the name of the <code>Queue</code>
     */
    public void removeQueue(String name)
    {
        queues.remove(name);
    }

    /**
     * Returns a <code>Queue</code> that was created with
     * {@link #createQueue} or <code>null</code> if no such
     * <code>Queue</code> is present.
     * @param name the name of the <code>Queue</code>
     * @return the <code>Queue</code>
     */
    public MockQueue getQueue(String name)
    {
        return (MockQueue)queues.get(name);
    }
}
