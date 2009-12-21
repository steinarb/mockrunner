package com.mockrunner.mock.jms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.jms.InvalidSelectorException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

import org.activemq.filter.mockrunner.Filter;
import org.activemq.selector.mockrunner.SelectorParser;

/**
 * Mock implementation of JMS <code>QueueBrowser</code>.
 */
public class MockQueueBrowser implements QueueBrowser, Serializable
{
    private MockConnection connection;
    private MockQueue queue;
    private boolean closed;
    private String messageSelector;
    private Filter messageSelectorFilter;
    
    public MockQueueBrowser(MockConnection connection, MockQueue queue)
    {
        this(connection, queue, null);
    }

    public MockQueueBrowser(MockConnection connection, MockQueue queue, String messageSelector)
    {
        this.connection = connection;
        this.queue = queue;
        closed = false;
        this.messageSelector = messageSelector;
        parseMessageSelector();
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
     * Returns if this browser was closed.
     * @return <code>true</code> if this browser is closed
     */
    public boolean isClosed()
    {
        return closed;
    }

    public Queue getQueue() throws JMSException
    {
        connection.throwJMSException();
        return queue;
    }

    public String getMessageSelector() throws JMSException
    {
        connection.throwJMSException();
        return messageSelector;
    }

    public Enumeration getEnumeration() throws JMSException
    {
        connection.throwJMSException();
        if(isClosed())
        {
            throw new JMSException("Browser is closed");
        } 
        return new Vector(getFilteredMessageList()).elements();
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        closed = true;
    }
    
    private List getFilteredMessageList()
    {
        List messages = queue.getCurrentMessageList();
        if(null == messageSelectorFilter) return messages;
        if(!connection.getConfigurationManager().getUseMessageSelectors()) return messages;
        List filteredMessages = new ArrayList();
        for(int ii = 0; ii < messages.size(); ii++)
        {
            Message nextMessage = (Message)messages.get(ii);
            try
            {
                if(messageSelectorFilter.matches(nextMessage))
                {
                    filteredMessages.add(nextMessage);
                }
            } 
            catch(JMSException exc)
            {
                throw new RuntimeException(exc.getMessage());
            }
        }
        return filteredMessages;
    }
}
