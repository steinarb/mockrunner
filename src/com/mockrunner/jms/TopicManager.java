package com.mockrunner.jms;

import java.util.HashMap;
import java.util.Map;

import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicConnection;

/**
 * The <code>TopicManager</code> can be used
 * to create topics, which is normally an 
 * administrative act. Before topics can be
 * used, they have to be created using this class.
 */
public class TopicManager
{
    private MockConnection connection;
    private Map topics;

    public TopicManager(MockTopicConnection connection)
    {
        this.connection = connection;
        topics = new HashMap();
    }
    
    /**
     * Creates a new <code>Topic</code> that is available
     * for {@link com.mockrunner.mock.jms.MockTopicSession#createTopic}
     * calls. Creating topics is an administrative act.
     * Before {@link com.mockrunner.mock.jms.MockTopicSession#createTopic}
     * can be sucessfully called, you have to create a <code>Topic</code>
     * with this method.
     * @param name the name of the <code>Topic</code>
     * @return the created <code>Topic</code>
     */
    public MockTopic createTopic(String name)
    {
        MockTopic topic = new MockTopic(connection, name);
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
