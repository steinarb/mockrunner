package com.mockrunner.mock.jms;

import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;

/**
 * Mock implementation of JMS <code>ConnectionConsumer</code>.
 */
public class MockConnectionConsumer implements ConnectionConsumer
{
    private MockConnection connection;
    private ServerSessionPool sessionPool;
    private boolean closed;
    
    public MockConnectionConsumer(MockConnection connection, ServerSessionPool serverSessionPool)
    {
        this.connection = connection;
        closed = false;
        sessionPool = serverSessionPool;
        if(null == sessionPool)
        {
            sessionPool = new MockServerSessionPool(connection);
        }
    }
    
    /**
     * Returns if this connection consumer was closed.
     * @return <code>true</code> if this connection consumer is closed
     */
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
