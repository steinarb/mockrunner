package com.mockrunner.mock.jms;

import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import com.mockrunner.jms.MessageManager;

/**
 * Mock implementation of JMS <code>Session</code>.
 * 
 * Please note that this implementation does not
 * implement transaction isolation at the moment.
 * Messages are immediately sent. If acknowledge
 * mode is AUTO_ACKNOWLEDGE or DUPS_OK_ACKNOWLEDGE,
 * the message will be automatically acknowledged,
 * otherwise, it will not be acknowledged. According
 * to JMS specification, the acknowledged mode must
 * be ignored for transacted session. This is currently
 * not implemented. However, the framework keeps track if a
 * transaction is committed or rolled back, so
 * you can test this and rely on the container for
 * the rest.
 */
public class MockSession implements Session
{
    private MockConnection connection;
    private MessageManager messageManager;
    private MessageListener messageListener;
    private boolean transacted;
    private int acknowledgeMode;
    private boolean committed;
    private boolean rolledback;
    private boolean recovered;
    private boolean closed;
    
    public MockSession(MockConnection connection, boolean transacted, int acknowledgeMode)
    {
        this.connection = connection;
        this.transacted = transacted;
        this.acknowledgeMode = acknowledgeMode;
        messageManager = new MessageManager();
        messageListener = null;
        committed = false;
        rolledback = false;
        recovered = false;
        closed = false;
    }
    
    /**
     * Returns the {@link MessageManager} for this session.
     * @return the {@link MessageManager}
     */
    public MessageManager getMessageManager()
    {
        return messageManager;
    }
    
    /**
     * Returns if this session was closed.
     * @return <code>true</code> if this session is closed
     */
    public boolean isClosed()
    {
        return closed;
    }

    /**
     * Returns if this session was recovered.
     * @return <code>true</code> if this session was recovered
     */
    public boolean isRecovered()
    {
        return recovered;
    }

    /**
     * Returns if the current transaction was committed.
     * @return <code>true</code> if the transaction was committed
     */
    public boolean isCommitted()
    {
        return committed;
    }

    /**
     * Returns if the current transaction was rolled back.
     * @return <code>true</code> if the transaction was rolled back
     */
    public boolean isRolledBack()
    {
        return rolledback;
    }
    
    /**
     * Returns if messages should be automatically acknowledged,
     * i.e. if the acknowledge mode is not <code>CLIENT_ACKNOWLEDGE</code>.
     * @return <code>true</code> if messages are automatically acknowledged
     */
    public boolean isAutoAcknowledge()
    {
        return acknowledgeMode != CLIENT_ACKNOWLEDGE;
    }
    
    /**
     * Note: Returns <code>0</code> if the session is transacted.
     * This method does not exist in JMS 1.0.2. In JMS 1.1 it
     * should return <code>Session.SESSION_TRANSACTED</code>
     * which is specified as <code>0</code>. In order to avoid
     * different versions for JMS 1.0.2 and 1.1 
     * (<code>Session.SESSION_TRANSACTED</code> does not
     * exist in 1.0.2) this method returns hardcoded <code>0</code>.
     * @return the acknowledge mode
     */
    public int getAcknowledgeMode() throws JMSException
    {
        if(getTransacted()) return 0;
        return acknowledgeMode;
    }
    
    public boolean getTransacted() throws JMSException
    {
        connection.throwJMSException();
        return transacted;
    }
    
    public BytesMessage createBytesMessage() throws JMSException
    {
        connection.throwJMSException();
        return getMessageManager().createBytesMessage();
    }

    public MapMessage createMapMessage() throws JMSException
    {
        connection.throwJMSException();
        return getMessageManager().createMapMessage();
    }

    public Message createMessage() throws JMSException
    {
        connection.throwJMSException();
        return getMessageManager().createMessage();
    }

    public ObjectMessage createObjectMessage() throws JMSException
    {
        connection.throwJMSException();
        return createObjectMessage(null);
    }

    public ObjectMessage createObjectMessage(Serializable object) throws JMSException
    {
        connection.throwJMSException();
        return getMessageManager().createObjectMessage(object);
    }

    public StreamMessage createStreamMessage() throws JMSException
    {
        connection.throwJMSException();
        return getMessageManager().createStreamMessage();
    }

    public TextMessage createTextMessage() throws JMSException
    {
        connection.throwJMSException();
        return createTextMessage(null);
    }

    public TextMessage createTextMessage(String text) throws JMSException
    {
        connection.throwJMSException();
        return getMessageManager().createTextMessage(text);
    }
    
    public MessageListener getMessageListener() throws JMSException
    {
        connection.throwJMSException();
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) throws JMSException
    {
        connection.throwJMSException();
        this.messageListener = messageListener;
    }
    
    public void run()
    {
    
    }
        
    public void commit() throws JMSException
    {
        connection.throwJMSException();
        committed = true;
    }

    public void rollback() throws JMSException
    {
        connection.throwJMSException();
        recover();
        rolledback = true;
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        if(getTransacted() && !isCommitted())
        {
            rollback();
        }
        closed = true;
    }

    public void recover() throws JMSException
    {
        connection.throwJMSException();
        recovered = true;
    }
    
    protected MockConnection getConnection()
    {
        return connection;
    }
}
