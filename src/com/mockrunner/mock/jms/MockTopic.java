package com.mockrunner.mock.jms;

import java.util.HashSet;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSession;

/**
 * Mock implementation of JMS <code>Topic</code>.
 */
public class MockTopic implements Topic
{
    private MockConnection connection;
    private Set sessions;
    private String name;
    
    public MockTopic(MockConnection connection, String name)
    {
        this.connection = connection;
        this.name = name;
        sessions = new HashSet();
    }
        
    public String getTopicName() throws JMSException
    {
        connection.throwJMSException();
        return name;
    }
    
    /**
     * Adds a <code>QueueSession</code>.
     * @param session the session
     */
    public void addTopicSession(TopicSession session)
    {
        sessions.add(session);
    }
    
    protected MockConnection getConnection()
    {
        return connection;
    }
}
