package com.mockrunner.mock.jms;

import java.util.List;

import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

import com.mockrunner.jms.ConfigurationManager;
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
    public MockTopicConnection(DestinationManager destinationManager, ConfigurationManager configurationManager)
    {
        super(destinationManager, configurationManager);
    }
    
    /**
     * Returns the list of {@link MockTopicSession} objects that were created 
     * with {@link #createTopicSession}.
     * @return the list
     */
    public List getTopicSessionList()
    {
        return super.getSessionList();
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
        return (MockTopicSession)super.getSession(index);
    }
    
    public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException
    {
        return createTopicSession(transacted, acknowledgeMode);
    }

    public TopicSession createTopicSession(boolean transacted, int acknowledgeMode) throws JMSException
    {
        throwJMSException();
        MockTopicSession session = new MockTopicSession(this, transacted, acknowledgeMode);
        sessions().add(session);
        return session;
    }

    public ConnectionConsumer createConnectionConsumer(Topic topic, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
    {
        return super.createConnectionConsumer(topic, messageSelector, sessionPool, maxMessages);
    }
}
