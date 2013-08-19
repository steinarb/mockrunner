package com.mockrunner.jms;

import java.util.HashMap;
import java.util.Map;

import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockTopic;

/**
 * The <code>DestinationManager</code> can be used
 * to create queues and topics, which is normally an 
 * administrative act. Since queues and topics are ususally
 * acquired using JNDI in a J2EE environment, you can bind
 * the created objects to the mock context with the help
 * of {@link com.mockrunner.ejb.EJBTestModule#bindToContext}.
 */
public class DestinationManager
{
    private Map queues;
    private Map topics;

    public DestinationManager()
    {
        queues = new HashMap();
        topics = new HashMap();
    }

    /**
     * Creates a new <code>Queue</code> that is available
     * for {@link com.mockrunner.mock.jms.MockQueueSession#createQueue}
     * calls. Creating queues is an administrative act.
     * Before {@link com.mockrunner.mock.jms.MockQueueSession#createQueue}
     * can be sucessfully called, you have to create a <code>Queue</code>
     * with this method. You can also bind the created queue to the
     * mock JNDI context using {@link com.mockrunner.ejb.EJBTestModule#bindToContext}.
     * @param name the name of the <code>Queue</code>
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
    
    /**
     * Creates a new <code>Topic</code> that is available
     * for {@link com.mockrunner.mock.jms.MockTopicSession#createTopic}
     * calls. Creating topics is an administrative act.
     * Before {@link com.mockrunner.mock.jms.MockTopicSession#createTopic}
     * can be sucessfully called, you have to create a <code>Topic</code>
     * with this method. You can also bind the created topic to the
     * mock JNDI context using {@link com.mockrunner.ejb.EJBTestModule#bindToContext}.
     * @param name the name of the <code>Topic</code>
     * @return the created <code>Topic</code>
     */
    public MockTopic createTopic(String name)
    {
        MockTopic topic = new MockTopic(name);
        topics.put(name, topic);
        return topic;
    }

    /**
     * Removes a formerly created <code>Topic</code>.
     * @param name the name of the <code>Topic</code>
     */
    public void removeTopic(String name)
    {
        topics.remove(name);
    }

    /**
     * Returns a <code>Topic</code> that was created with
     * {@link #createTopic} or <code>null</code> if no such
     * <code>Topic</code> is present.
     * @param name the name of the <code>Topic</code>
     * @return the <code>Topic</code>
     */
    public MockTopic getTopic(String name)
    {
        return (MockTopic)topics.get(name);
    }
}
