package com.mockrunner.mock.jms;

import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;

/**
 * Mock implementation of JMS <code>ConnectionConsumer</code>.
 */
public class MockConnectionConsumer implements ConnectionConsumer
{
    private MockQueueConnection connection;
    private ServerSessionPool sessionPool;
    private boolean closed;
    
    public MockConnectionConsumer(MockQueueConnection connection)
    {
        this.connection = connection;
        sessionPool = new MockServerSessionPool(connection);
        closed = false;
    }
    
    public boolean isClosed()
    {
        return closed;
    }
    
    public void setServerSessionPool(ServerSessionPool serverSessionPool)
    {
        sessionPool = serverSessionPool;
    }
    
    public ServerSessionPool getServerSessionPool() throws JMSException
    {
        connection.throwJMSException();
        return sessionPool;
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        closed = true;
    }
}
