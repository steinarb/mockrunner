package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;

/**
 * Mock implementation of JMS <code>Connection</code>.
 * Please note: The interfaces <code>ConnectionConsumer</code>,
 * <code>ServerSessionPool</code> and <code>ServerSession</code>
 * are not meant for application use. Mockrunner provides very
 * simple mock implementations but usually you won't need them.
 */
public class MockConnection implements Connection
{
    private ConnectionMetaData metaData;
    private List sessions;
    private String clientId;
    private boolean started;
    private boolean closed;
    private ExceptionListener listener;
    private JMSException exception;
    private DestinationManager destinationManager;
    private ConfigurationManager configurationManager;
    
    public MockConnection(DestinationManager destinationManager, ConfigurationManager configurationManager)
    { 
        metaData = new MockConnectionMetaData();
        started = false;
        closed = false;
        exception = null;
        this.destinationManager = destinationManager;
        this.configurationManager = configurationManager;
        sessions = new ArrayList();
    }
    
    /**
     * Returns the {@link com.mockrunner.jms.DestinationManager}.
     * @return the {@link com.mockrunner.jms.DestinationManager}
     */
    public DestinationManager getDestinationManager()
    {
        return destinationManager;
    }
    
    /**
     * Returns the {@link com.mockrunner.jms.ConfigurationManager}.
     * @return the {@link com.mockrunner.jms.ConfigurationManager}
     */
    public ConfigurationManager getConfigurationManager()
    {
        return configurationManager;
    }
    
    /**
     * Returns the list of {@link MockSession} objects.
     * @return the list
     */
    public List getSessionList()
    {
        return Collections.unmodifiableList(sessions);
    }

    /**
     * Returns a {@link MockSession}. If there's no such
     * {@link MockSession}, <code>null</code> is returned.
     * @param index the index of the session object
     * @return the session object
     */
    public MockSession getSession(int index)
    {
        if(sessions.size() <= index || index < 0) return null;
        return (MockSession)sessions.get(index);
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
    
    public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException
    {
        throwJMSException();
        MockSession session = new MockSession(this, transacted, acknowledgeMode);
        sessions().add(session);
        return session;
    }
    
    public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
    {
        throwJMSException();
        return new MockConnectionConsumer(this, sessionPool);
    }

    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
    {
        return createConnectionConsumer(topic, messageSelector, sessionPool, maxMessages);
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
        for(int ii = 0; ii < sessions.size(); ii++)
        {
            Session session = (Session)sessions.get(ii);
            session.close();
        }
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
    
    protected List sessions()
    {
        return sessions;
    }
}
