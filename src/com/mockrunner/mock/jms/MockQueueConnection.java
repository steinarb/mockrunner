package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;

/**
 * Mock implementation of JMS <code>QueueConnection</code>.
 * Please note: The interfaces <code>ConnectionConsumer</code>,
 * <code>ServerSessionPool</code> and <code>ServerSession</code>
 * are not meant for application use. Mockrunner provides very
 * simple mock implementations but usually you won't need them.
 */
public class MockQueueConnection extends MockConnection implements QueueConnection
{
    private List queueSessions;
    
    public MockQueueConnection()
    {
        queueSessions = new ArrayList();
    }
    
    /**
     * Returns the list of {@link MockQueueSession} objects that were created 
     * with {@link #createQueueSession}.
     * @return the list
     */
    public List getQueueSessionList()
    {
        return Collections.unmodifiableList(queueSessions);
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
        if(queueSessions.size() <= index || index < 0) return null;
        return (MockQueueSession)queueSessions.get(index);
    }
    
    public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException
    {
        throwJMSException();
        MockQueueSession session = new MockQueueSession(this, transacted, acknowledgeMode);
        queueSessions.add(session);
        return session;
    }

    public ConnectionConsumer createConnectionConsumer(Queue queue, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
    {
        throwJMSException();
        return new MockConnectionConsumer(this, sessionPool);
    }
    
    public void close() throws JMSException
    {
        for(int ii = 0; ii < queueSessions.size(); ii++)
        {
            Session session = (Session)queueSessions.get(ii);
            session.close();
        }
        super.close();
    }
}
