package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

/**
 * Mock implementation of JMS <code>TopicSession</code>.
 */
public class MockTopicSession extends MockSession implements TopicSession
{
    private List tempTopics;
    
    public MockTopicSession(MockConnection connection, boolean transacted, int acknowledgeMode)
    {
        super(connection, transacted, acknowledgeMode);
        tempTopics = new ArrayList();
    }
    
    /**
     * Returns the list of temporary topics.
     * @return the <code>TemporaryTopic</code> list
     */
    public List getTemporaryTopicList()
    {
        return Collections.unmodifiableList(tempTopics);
    }

    /**
     * Returns a <code>TemporaryTopic</code> by its index. The
     * index represent the number of the topic. Returns <code>null</code>
     * if no such <code>TemporaryTopic</code> is present.
     * @param index the index
     * @return the <code>TemporaryTopic</code>
     */
    public MockTemporaryTopic getTemporaryQueue(int index)
    {
        if(tempTopics.size() <= index || index < 0) return null;
        return (MockTemporaryTopic)tempTopics.get(index);
    }

    public Topic createTopic(String name) throws JMSException
    {
        getConnection().throwJMSException();
        MockTopic topic = getConnection().getDestinationManager().getTopic(name);
        if(null == topic)
        {
            throw new JMSException("Topic with name " + name + " not found");
        }
        addSessionToTopic(topic);
        return topic;
    }
    
    public TemporaryTopic createTemporaryTopic() throws JMSException
    {
        getConnection().throwJMSException();
        MockTemporaryTopic topic = new MockTemporaryTopic(getConnection());
        tempTopics.add(topic);
        addSessionToTopic(topic);
        return topic;
    }
    
    public TopicPublisher createPublisher(Topic topic) throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public TopicSubscriber createSubscriber(Topic topic) throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public TopicSubscriber createSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }
 
    public void unsubscribe(String name) throws JMSException
    {
        // TODO Auto-generated method stub
    }
    
    private void addSessionToTopic(Topic topic)
    {
        if(topic instanceof MockTopic)
        {
            ((MockTopic)topic).addTopicSession(this);
        }
    }
}
