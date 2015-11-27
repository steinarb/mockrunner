package com.mockrunner.jms;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockTopic;

/**
 * The <code>DestinationManager</code> can be used
 * to create queues and topics, which is normally an 
 * administrative act. Since queues and topics are ususally
 * acquired using JNDI in a J2EE environment, you can bind
 * the created objects to the mock context with the help
 * of com.mockrunner.ejb.EJBTestModule#bindToContext.
 */
public class DestinationManager implements Serializable
{
    private ConcurrentMap<String, MockQueue> queues;
    private ConcurrentMap<String, MockTopic> topics;

    public DestinationManager()
    {
        queues = new ConcurrentHashMap<String, MockQueue>();
        topics = new ConcurrentHashMap<String, MockTopic>();
    }

    /**
     * Creates a new <code>Queue</code> that is available
     * for {@link com.mockrunner.mock.jms.MockQueueSession#createQueue}
     * calls. Creating queues is an administrative act.
     * Before {@link com.mockrunner.mock.jms.MockQueueSession#createQueue}
     * can be sucessfully called, you have to create a <code>Queue</code>
     * with this method. You can also bind the created queue to the
     * mock JNDI context using com.mockrunner.ejb.EJBTestModule#bindToContext.
     * @param name the name of the <code>Queue</code>
     * @return the created <code>Queue</code>
     */
    public MockQueue createQueue(String name)
    {
        MockQueue queue = new MockQueue(name);
        MockQueue orig = queues.putIfAbsent(name, queue);
        return orig == null ? queue : orig;
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
     * {@link #createQueue}, or creates one if it didn't exist yet.
     * @param name the name of the <code>Queue</code>
     * @return the <code>Queue</code>
     */
    public MockQueue getQueue(String name)
    {
        return createQueue(name);
    }
    
    /**
     * Creates a new <code>Topic</code> that is available
     * for {@link com.mockrunner.mock.jms.MockTopicSession#createTopic}
     * calls. Creating topics is an administrative act.
     * Before {@link com.mockrunner.mock.jms.MockTopicSession#createTopic}
     * can be sucessfully called, you have to create a <code>Topic</code>
     * with this method. You can also bind the created topic to the
     * mock JNDI context using com.mockrunner.ejb.EJBTestModule#bindToContext.
     * @param name the name of the <code>Topic</code>
     * @return the created <code>Topic</code>
     */
    public MockTopic createTopic(String name)
    {
        MockTopic topic = new MockTopic(name);
        MockTopic orig = topics.putIfAbsent(name, topic);
        return orig == null ? topic : orig;
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
     * {@link #createTopic}, or creates one if it didn't exist yet.
     * @param name the name of the <code>Topic</code>
     * @return the <code>Topic</code>
     */
    public MockTopic getTopic(String name)
    {
        return createTopic(name);
    }

    public boolean existsTopic(String topicName) {
        return topics.containsKey(topicName);
    }

    public boolean existsQueue(String queueName) {
        return queues.containsKey(queueName);
    }
}
