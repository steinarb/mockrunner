package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.Topic;

/**
 * Mock implementation of JMS <code>Topic</code>.
 */
public class MockTopic implements Topic
{
    private MockConnection connection;
    private String name;
    
    public MockTopic(MockConnection connection, String name)
    {
        this.connection = connection;
        this.name = name;
    }
        
    public String getTopicName() throws JMSException
    {
        connection.throwJMSException();
        return name;
    }
}
