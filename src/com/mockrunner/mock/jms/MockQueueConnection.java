package com.mockrunner.mock.jms;

import java.util.List;

import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;

/**
 * Mock implementation of JMS <code>QueueConnection</code>.
 * Please note: The interfaces <code>ConnectionConsumer</code>,
 * <code>ServerSessionPool</code> and <code>ServerSession</code>
 * are not meant for application use. Mockrunner provides very
 * simple mock implementations but usually you won't need them.
 */
public class MockQueueConnection extends MockConnection implements QueueConnection
{
    public MockQueueConnection(DestinationManager destinationManager, ConfigurationManager configurationManager)
    {
        super(destinationManager, configurationManager);
    }
    
    /**
     * Returns the list of {@link MockQueueSession} objects that were created 
     * with {@link #createQueueSession}.
     * @return the list
     */
    public List getQueueSessionList()
    {
        return super.getSessionList();
    }

    /**
     * Returns a {@link MockQueueSession} that was created with
     * {@link #createQueueSession}. If there's no such
     * {@link MockQueueSession}, <code>null</code> is returned.
     * @param index the index of the session object
     * @return the session object
     */
    public MockQueueSession getQueueSession(int index)
    {
        return (MockQueueSession)super.getSession(index);
    }
    
    public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException
    {
        return createQueueSession(transacted, acknowledgeMode);
    }
    
    public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException
    {
        throwJMSException();
        MockQueueSession session = new MockQueueSession(this, transacted, acknowledgeMode);
        sessions().add(session);
        return session;
    }

    public ConnectionConsumer createConnectionConsumer(Queue queue, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
    {
        return super.createConnectionConsumer(queue, messageSelector, sessionPool, maxMessages);
    }
}
