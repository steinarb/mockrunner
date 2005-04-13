package com.mockrunner.mock.jms;

import javax.jms.InvalidSelectorException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

import org.activemq.filter.mockrunner.Filter;
import org.activemq.selector.mockrunner.SelectorParser;

/**
 * Mock implementation of JMS <code>MessageConsumer</code>.
 */
public abstract class MockMessageConsumer implements MessageConsumer
{
    private MockConnection connection;
    private String messageSelector;
    private Filter messageSelectorFilter;
    private boolean closed;
    private MessageListener messageListener;
        
    public MockMessageConsumer(MockConnection connection, String messageSelector)
    {
        this.connection = connection;
        this.messageSelector = messageSelector;
        parseMessageSelector();
        closed = false;
        messageListener = null;
    }

    private void parseMessageSelector()
    {
        if(null == messageSelector || messageSelector.length() == 0)
        {
            this.messageSelectorFilter = null;
        }
        else
        {
            try
            {
                this.messageSelectorFilter = new SelectorParser().parse(messageSelector);
            }
            catch(InvalidSelectorException exc)
            {
                throw new RuntimeException("Error parsing message selector: " + exc.getMessage());
            }
        }
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
     * i.e. if a <code>MessageListener</code> is registered,
     * the receiver isn't closed and has an approriate selector.
     * @return <code>true</code> if this receiver can consume the message
     */
    public boolean canConsume(Message message)
    {
        if(messageListener == null) return false;
        if(isClosed()) return false;
        return matchesMessageSelector(message);
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
    
    private boolean matchesMessageSelector(Message message)
    {
        if(!connection.getConfigurationManager().getUseMessageSelectors()) return true;
        if(messageSelectorFilter == null) return true;
        try
        {
            return messageSelectorFilter.matches(message);
        }
        catch(JMSException exc)
        {
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    protected Filter getMessageFilter()
    {
        return messageSelectorFilter;
    }
    
    protected MockConnection getConnection()
    {
        return connection;
    }
}
