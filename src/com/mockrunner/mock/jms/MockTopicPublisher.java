package com.mockrunner.mock.jms;

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
        // TODO Auto-generated method stub

    }

    public void publish(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
    {
        // TODO Auto-generated method stub

    }

    public void publish(Topic topic, Message message) throws JMSException
    {
        // TODO Auto-generated method stub

    }

    public void publish(Topic topic, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
    {
        // TODO Auto-generated method stub
    }
}
