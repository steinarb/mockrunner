package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

import com.mockrunner.jms.DestinationManager;

/**
 * Mock implementation of JMS <code>TopicConnection</code>.
 * Please note: The interfaces <code>ConnectionConsumer</code>,
 * <code>ServerSessionPool</code> and <code>ServerSession</code>
 * are not meant for application use. Mockrunner provides very
 * simple mock implementations but usually you won't need them.
 */
public class MockTopicConnection extends MockConnection implements TopicConnection
{
    private List topicSessions;
    
    public MockTopicConnection(DestinationManager destinationManager)
    {
        super(destinationManager);
        topicSessions = new ArrayList();
    }
    
    /**
     * Returns the list of {@link MockTopicSession} objects that were created 
     * with {@link #createTopicSession}.
     * @return the list
     */
    public List getTopicSessionList()
    {
        return Collections.unmodifiableList(topicSessions);
    }

    /**
     * Returns a {@link MockTopicSession} that was created with
     * {@link #createTopicSession}. If there's no such
     * {@link MockTopicSession}, <code>null</code> is returned.
     * @param index the index of the session object
     * @return the session object
     */
    public MockTopicSession getTopicSession(int index)
    {
        if(topicSessions.size() <= index || index < 0) return null;
        return (MockTopicSession)topicSessions.get(index);
    }

    public TopicSession createTopicSession(boolean transacted, int acknowledgeMode) throws JMSException
    {
        throwJMSException();
        MockTopicSession session = new MockTopicSession(this, transacted, acknowledgeMode);
        topicSessions.add(session);
        return session;
    }

    public ConnectionConsumer createConnectionConsumer(Topic topic, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
    {
        throwJMSException();
        return new MockConnectionConsumer(this, sessionPool);
    }

    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException 
    {
        return createConnectionConsumer(topic, messageSelector, sessionPool, maxMessages);
    }
    
    public void close() throws JMSException
    {
        for(int ii = 0; ii < topicSessions.size(); ii++)
        {
            Session session = (Session)topicSessions.get(ii);
            session.close();
        }
        super.close();
    }
}
