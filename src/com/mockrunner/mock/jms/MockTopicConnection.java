package com.mockrunner.mock.jms;

import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

/**
 * Mock implementation of JMS <code>TopicConnection</code>.
 */
public class MockTopicConnection extends MockConnection implements TopicConnection
{
    public TopicSession createTopicSession(boolean transacted, int acknowledgeMode) throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ConnectionConsumer createConnectionConsumer(Topic topic, java.lang.String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, java.lang.String subscriptionName, java.lang.String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException 
    {
        // TODO Auto-generated method stub
        return null;
    }
}
