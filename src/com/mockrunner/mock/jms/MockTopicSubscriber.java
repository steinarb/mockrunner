package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

/**
 * Mock implementation of JMS <code>TopicSubscriber</code>.
 */
public class MockTopicSubscriber extends MockMessageConsumer implements TopicSubscriber
{
    private MockTopic topic;
    private boolean noLocal;

    public MockTopicSubscriber(MockConnection connection, MockTopic topic, String messageSelector, boolean noLocal)
    {
        super(connection, messageSelector);
        this.topic = topic;
        this.noLocal = noLocal;
    }

    public Topic getTopic() throws JMSException
    {
        getConnection().throwJMSException();
        return topic;
    }

    public boolean getNoLocal() throws JMSException
    {
        getConnection().throwJMSException();
        return noLocal;
    }

    public Message receive() throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
