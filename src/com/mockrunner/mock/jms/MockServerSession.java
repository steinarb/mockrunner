package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.QueueSession;
import javax.jms.ServerSession;
import javax.jms.Session;

/**
 * Mock implementation of JMS <code>ServerSession</code>.
 * The <code>ServerSession</code> is not meant for application
 * use. This simple implementation only returns a new
 * {@link MockQueueSession} when calling {@link #getSession}.
 */
public class MockServerSession implements ServerSession
{
    private MockQueueConnection connection;
    private Session session;
    private boolean started;
    
    public MockServerSession(MockQueueConnection connection)
    {
        this.connection = connection;
        session = new MockQueueSession(connection, false, QueueSession.AUTO_ACKNOWLEDGE);
        started = false;
    }
    
    public void setSession(Session session)
    {
        this.session = session;
    }
    
    public Session getSession() throws JMSException
    {
        connection.throwJMSException();
        return session;
    }

    public void start() throws JMSException
    {
        connection.throwJMSException();
        started = true;
    }
}
