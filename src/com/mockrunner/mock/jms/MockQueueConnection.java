package com.mockrunner.mock.jms;

import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;

/**
 * Mock implementation of JMS <code>QueueConnection</code>.
 */
public class MockQueueConnection implements QueueConnection
{
    private String clientId;
    private ConnectionMetaData metaData;
    private boolean started;
    private boolean closed;
    
    public MockQueueConnection()
    {
        metaData = new MockConnectionMetaData();
        started = false;
        closed = false;
    }
    
    public void setMetaData(ConnectionMetaData metaData)
    {
        this.metaData = metaData;
    }
    
    public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException
    {
        return null;
    }

    public ConnectionConsumer createConnectionConsumer(Queue queue, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getClientID() throws JMSException
    {
        return clientId;
    }

    public void setClientID(String clientId) throws JMSException
    {
        this.clientId = clientId;
    }

    public ConnectionMetaData getMetaData() throws JMSException
    {
        return metaData;
    }

    public ExceptionListener getExceptionListener() throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setExceptionListener(ExceptionListener listener) throws JMSException
    {
        // TODO Auto-generated method stub
    }

    public void start() throws JMSException
    {
        started = true;
    }

    public void stop() throws JMSException
    {
        started = false;
    }

    public void close() throws JMSException
    {
        closed = true;
    }
    
    public boolean isStarted()
    {
        return started;
    }
    
    public boolean isStopped()
    {
        return !isStarted();
    }
    
    public boolean isClosed()
    {
        return closed;
    }
}
