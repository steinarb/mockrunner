package com.mockrunner.mock.jms;

import javax.jms.Connection;
import javax.jms.ConnectionMetaData;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

/**
 * Mock implementation of JMS <code>Connection</code>.
 */
public abstract class MockConnection implements Connection
{
    private ConnectionMetaData metaData;
    private String clientId;
    private boolean started;
    private boolean closed;
    private ExceptionListener listener;
    private JMSException exception;
    
    public MockConnection()
    { 
        metaData = new MockConnectionMetaData();
        started = true;
        closed = false;
        exception = null;
    }
    
    /**
     * Set an exception that will be thrown when calling one
     * of the interface methods. Since the mock implementation
     * cannot fail like a full blown message server you can use
     * this method to simulate server errors. After the exception
     * was thrown it will be deleted.
     * @param exception the exception to throw
     */
    public void setJMSException(JMSException exception)
    {
        this.exception = exception;
    }

    /**
     * Throws a <code>JMSException</code> if one is set with
     * {@link #setJMSException}. Informs the <code>ExceptionListener</code>
     * and deletes the exception after throwing it.
     */
    public void throwJMSException() throws JMSException
    {
        if(null == exception) return;
        JMSException tempException = exception;
        exception = null;
        if(listener != null)
        {
            listener.onException(tempException);
        }
        throw tempException;
    }
    
    /**
     * You can use this to set the <code>ConnectionMetaData</code>.
     * Usually this should not be necessary. Per default an instance
     * of {@link MockConnectionMetaData} is returned when calling
     * {@link #getMetaData}.
     * @param metaData the meta data
     */
    public void setMetaData(ConnectionMetaData metaData)
    {
        this.metaData = metaData;
    }
    
    public ConnectionMetaData getMetaData() throws JMSException
    {
        throwJMSException();
        return metaData;
    }
        
    public String getClientID() throws JMSException
    {
        throwJMSException();
        return clientId;
    }

    public void setClientID(String clientId) throws JMSException
    {
        throwJMSException();
        this.clientId = clientId;
    }  

    public ExceptionListener getExceptionListener() throws JMSException
    {
        throwJMSException();
        return listener;
    }

    public void setExceptionListener(ExceptionListener listener) throws JMSException
    {
        throwJMSException();
        this.listener = listener;
    }

    public void start() throws JMSException
    {
        throwJMSException();
        started = true;
    }

    public void stop() throws JMSException
    {
        throwJMSException();
        started = false;
    }

    public void close() throws JMSException
    {
        throwJMSException();
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
