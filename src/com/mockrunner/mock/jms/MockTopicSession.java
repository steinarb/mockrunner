package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

/**
 * Mock implementation of JMS <code>TopicSession</code>.
 * Please note that message selectors are not supported
 * at the moment.
 */
public class MockTopicSession extends MockSession implements TopicSession
{
    public MockTopicSession(MockTopicConnection connection)
    {
        this(connection, false, Session.AUTO_ACKNOWLEDGE);
    }
    
    public MockTopicSession(MockTopicConnection connection, boolean transacted, int acknowledgeMode)
    {
        super(connection, transacted, acknowledgeMode);
    }

    public TopicPublisher createPublisher(Topic topic) throws JMSException
    {
        return (TopicPublisher)createProducer(topic);
    }

    public TopicSubscriber createSubscriber(Topic topic) throws JMSException
    {
        return (TopicSubscriber)createConsumer(topic);
    }

    public TopicSubscriber createSubscriber(Topic topic, String messageSelector, boolean noLocal) throws JMSException
    {
        return (TopicSubscriber)createConsumer(topic, messageSelector, noLocal);
    }
}
