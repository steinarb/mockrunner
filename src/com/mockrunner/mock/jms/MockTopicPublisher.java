package com.mockrunner.mock.jms;

import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicPublisher;

/**
 * Mock implementation of JMS <code>TopicPublisher</code>.
 */
public class MockTopicPublisher extends MockMessageProducer implements TopicPublisher
{
    private MockTopic topic;

    public MockTopicPublisher(MockConnection connection, MockTopic topic)
    {
        super(connection);
        this.topic = topic;
    }

    public Topic getTopic() throws JMSException
    {
        getConnection().throwJMSException();
        return topic;
    }

    public void publish(Message message) throws JMSException
    {
        getConnection().throwJMSException();
        if(isClosed())
        {
            throw new JMSException("Publisher is closed");
        }
        if(null == topic)
        {
            throw new InvalidDestinationException("Topic must not be null");
        }
        setJMSProperties(message);
        topic.addMessage(message);
    }

    public void publish(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
    {
        getConnection().throwJMSException();
        if(isClosed())
        {
            throw new JMSException("Publisher is closed");
        }
        if(null == topic)
        {
            throw new InvalidDestinationException("Topic must not be null");
        }
        setJMSProperties(message, deliveryMode, priority, timeToLive);
        topic.addMessage(message);
    }

    public void publish(Topic topic, Message message) throws JMSException
    {
        getConnection().throwJMSException();
        if(isClosed())
        {
            throw new JMSException("Publisher is closed");
        }
        if(null == topic)
        {
            throw new InvalidDestinationException("Topic must not be null");
        }
        setJMSProperties(message);
        if(topic instanceof MockTopic)
        {
            ((MockTopic)topic).addMessage(message);
        }
    }

    public void publish(Topic topic, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
    {
        getConnection().throwJMSException();
        if(isClosed())
        {
            throw new JMSException("Publisher is closed");
        }
        if(null == topic)
        {
            throw new InvalidDestinationException("Topic must not be null");
        }
        setJMSProperties(message, deliveryMode, priority, timeToLive);
        if(topic instanceof MockTopic)
        {
            ((MockTopic)topic).addMessage(message);
        }
    }
}
