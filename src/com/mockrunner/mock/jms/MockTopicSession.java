package com.mockrunner.mock.jms;

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
    public MockTopicSession(MockConnection connection, boolean transacted, int acknowledgeMode)
    {
        super(connection, transacted, acknowledgeMode);
    }

    public Topic createTopic(String name) throws JMSException
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

    public TopicPublisher createPublisher(Topic topic) throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public TemporaryTopic createTemporaryTopic() throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void unsubscribe(String name) throws JMSException
    {
        // TODO Auto-generated method stub
    }
}
