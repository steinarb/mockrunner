package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TemporaryTopic;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import com.mockrunner.jms.TopicTransmissionManager;

/**
 * Mock implementation of JMS <code>TopicSession</code>.
 * Please note that message selectors are not supported
 * at the moment.
 */
public class MockTopicSession extends MockSession implements TopicSession
{
    private TopicTransmissionManager topicTransManager;
    private List tempTopics;
    
    public MockTopicSession(MockTopicConnection connection)
    {
        this(connection, false, Session.AUTO_ACKNOWLEDGE);
    }
    
    public MockTopicSession(MockTopicConnection connection, boolean transacted, int acknowledgeMode)
    {
        super(connection, transacted, acknowledgeMode);
        topicTransManager = new TopicTransmissionManager(connection, this);
        tempTopics = new ArrayList();
    }
    
    /**
     * Returns the {@link com.mockrunner.jms.TopicTransmissionManager}.
     * @return the {@link com.mockrunner.jms.TopicTransmissionManager}
     */
    public TopicTransmissionManager getTopicTransmissionManager()
    {
        return topicTransManager;
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
    public MockTemporaryTopic getTemporaryTopic(int index)
    {
        if(tempTopics.size() <= index || index < 0) return null;
        return (MockTemporaryTopic)tempTopics.get(index);
    }

    public Topic createTopic(String name) throws JMSException
    {
        getConnection().throwJMSException();
        MockTopic topic = ((MockTopicConnection)getConnection()).getDestinationManager().getTopic(name);
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
        MockTemporaryTopic topic = new MockTemporaryTopic();
        tempTopics.add(topic);
        addSessionToTopic(topic);
        return topic;
    }
    
    public TopicPublisher createPublisher(Topic topic) throws JMSException
    {
        getConnection().throwJMSException();
        if(!(topic instanceof MockTopic))
        {
            throw new InvalidDestinationException("topic must be an instance of MockTopic");
        }
        addSessionToTopic(topic);
        return topicTransManager.createTopicPublisher((MockTopic)topic);
    }

    public TopicSubscriber createSubscriber(Topic topic) throws JMSException
    {
        getConnection().throwJMSException();
        return createSubscriber(topic, null, false);
    }

    public TopicSubscriber createSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException
    {
        getConnection().throwJMSException();
        if(!(topic instanceof MockTopic))
        {
            throw new InvalidDestinationException("topic must be an instance of MockTopic");
        }
        addSessionToTopic(topic);
        return topicTransManager.createTopicSubscriber((MockTopic)topic, messageSelector, noLocal);
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException
    {
        getConnection().throwJMSException();
        return createDurableSubscriber(topic, name, null, false);
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException
    {
        getConnection().throwJMSException();
        if(!(topic instanceof MockTopic))
        {
            throw new InvalidDestinationException("topic must be an instance of MockTopic");
        }
        addSessionToTopic(topic);
        return topicTransManager.createDurableTopicSubscriber((MockTopic)topic, name, messageSelector, noLocal);
    }
 
    public void unsubscribe(String name) throws JMSException
    {
        getConnection().throwJMSException();
        topicTransManager.removeTopicDurableSubscriber(name);
    }
    
    public void close() throws JMSException
    {
        topicTransManager.closeAll();
        super.close();
    }
    
    private void addSessionToTopic(Topic topic)
    {
        if(topic instanceof MockTopic)
        {
            ((MockTopic)topic).addTopicSession(this);
        }
    }
}
