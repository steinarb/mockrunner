package com.mockrunner.mock.jms;

import javax.jms.InvalidDestinationException;
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
        getConnection().throwJMSException();
        if(!(topic instanceof MockTopic))
        {
            throw new InvalidDestinationException("topic must be an instance of MockTopic");
        }
        addSessionToTopic(topic);
        return getTopicTransmissionManager().createTopicPublisher((MockTopic)topic);
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
        return getTopicTransmissionManager().createTopicSubscriber((MockTopic)topic, messageSelector, noLocal);
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
        return getTopicTransmissionManager().createDurableTopicSubscriber((MockTopic)topic, name, messageSelector, noLocal);
    }
 
    public void unsubscribe(String name) throws JMSException
    {
        getConnection().throwJMSException();
        getTopicTransmissionManager().removeTopicDurableSubscriber(name);
    }
    
    public void close() throws JMSException
    {
        getTopicTransmissionManager().closeAll();
        super.close();
    }
}
