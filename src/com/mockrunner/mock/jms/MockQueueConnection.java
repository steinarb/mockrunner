package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.List;

import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;

import com.mockrunner.jms.DestinationManager;
import com.mockrunner.jms.MessageManager;
import com.mockrunner.jms.TransmissionManager;

/**
 * Mock implementation of JMS <code>QueueConnection</code>.
 * Please note: The interfaces <code>ConnectionConsumer</code>,
 * <code>ServerSessionPool</code> and <code>ServerSession</code>
 * are not meant for application use. Mockrunner provides very
 * simple mock implementations but usually you won't need them.
 */
public class MockQueueConnection implements QueueConnection
{
    private DestinationManager destinationManager;
    private TransmissionManager transmissionManager;
    private MessageManager messageManager;
    private String clientId;
    private ConnectionMetaData metaData;
    private boolean started;
    private boolean closed;
    private List queueSessions;
    private ExceptionListener listener;
    private JMSException exception;
    
    public MockQueueConnection()
    {
        destinationManager = new DestinationManager(this);
        transmissionManager = new TransmissionManager(this);
        messageManager = new MessageManager();
        metaData = new MockConnectionMetaData();
        started = false;
        closed = false;
        queueSessions = new ArrayList();
        exception = null;
    }
    
    /**
     * Returns the {@link DestinationManager} for this connection.
     * @return the {@link DestinationManager}
     */
    public DestinationManager getDestinationManager()
    {
        return destinationManager;
    }
    
    /**
     * Returns the {@link TransmissionManager} for this connection.
     * @return the {@link TransmissionManager}
     */
    public TransmissionManager getTransmissionManager()
    {
        return transmissionManager;
    }
    
    /**
     * Returns the {@link MessageManager} for this connection.
     * @return the {@link MessageManager}
     */
    public MessageManager getMessageManager()
    {
        return messageManager;
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
        return new MockConnectionConsumer(this);
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

    public ConnectionMetaData getMetaData() throws JMSException
    {
        throwJMSException();
        return metaData;
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
