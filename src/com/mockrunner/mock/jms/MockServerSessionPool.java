package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.ServerSession;
import javax.jms.ServerSessionPool;

/**
 * Mock implementation of JMS <code>ServerSessionPool</code>.
 */
public class MockServerSessionPool implements ServerSessionPool
{
    private MockQueueConnection connection;
    private ServerSession session;
    
    public MockServerSessionPool(MockQueueConnection connection)
    {
        this.connection = connection;
        session = new MockServerSession(connection);
    }
    
    public void setServerSession(ServerSession session)
    {
        this.session = session;
    }
    
    public ServerSession getServerSession() throws JMSException
    {
        connection.throwJMSException();
        return session;
    }
}
