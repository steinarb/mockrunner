package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

/**
 * Mock implementation of JMS <code>MessageConsumer</code>.
 */
public abstract class MockMessageConsumer implements MessageConsumer
{
    private MockConnection connection;
    private String messageSelector;
    private boolean closed;
    private MessageListener messageListener;
        
    public MockMessageConsumer(MockConnection connection, String messageSelector)
    {
        this.connection = connection;
        this.messageSelector = messageSelector;
        closed = false;
        messageListener = null;
    }

    /**
     * Returns if this consumer was closed.
     * @return <code>true</code> if this consumer is closed
     */
    public boolean isClosed()
    {
        return closed;
    }
    
    /**
     * Returns if this consumer can consume an incoming message,
     * i.e. if a <code>MessageListener</code> is registered and
     * the receiver isn't closed.
     * @return <code>true</code> if this receiver can consume the message
     */
    public boolean canConsume()
    {
        return (messageListener != null) && (!isClosed());
    }
    
    /**
     * Adds a message that is immediately propagated to the
     * message listener. If there's no message listener,
     * nothing happens.
     * @param message the message
     */
    public void receiveMessage(Message message)
    {
        if(null == messageListener) return;
        messageListener.onMessage(message);
    }

    public String getMessageSelector() throws JMSException
    {
        connection.throwJMSException();
        return messageSelector;
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

    public Message receive(long timeout) throws JMSException
    {
        connection.throwJMSException();
        return receive();
    }

    public Message receiveNoWait() throws JMSException
    {
        connection.throwJMSException();
        return receive();
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        closed = true;
    }
    
    protected MockConnection getConnection()
    {
        return connection;
    }
}
