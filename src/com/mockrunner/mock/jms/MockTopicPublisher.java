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
    public MockTopicPublisher(MockConnection connection, MockSession session, MockTopic topic)
    {
        super(connection, session, topic);
    }

    public Topic getTopic() throws JMSException
    {
        return (Topic)getDestination();
    }

    public void publish(Message message) throws JMSException
    {
        super.send(message);
    }

    public void publish(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
    {
        super.send(message, deliveryMode, priority, timeToLive);
    }

    public void publish(Topic topic, Message message) throws JMSException
    {
        super.send(topic, message);
    }

    public void publish(Topic topic, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException
    {
        super.send(topic, message, deliveryMode, priority, timeToLive);
    }
}
