package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

/**
 * Mock implementation of JMS <code>TopicConnectionFactory</code>.
 */
public class MockTopicConnectionFactory implements TopicConnectionFactory
{
    private TopicConnection topicConnection;
    
    public void setTopicConnection(TopicConnection topicConnection)
    {
        this.topicConnection = topicConnection;
    }
    
    public TopicConnection createTopicConnection() throws JMSException
    {
        return topicConnection;
    }

    public TopicConnection createTopicConnection(String name, String password) throws JMSException
    {
        return topicConnection;
    }
}
