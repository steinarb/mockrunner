package com.mockrunner.mock.jms;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

/**
 * Mock implementation of JMS <code>QueueBrowser</code>.
 */
public class MockQueueBrowser implements QueueBrowser
{
    private MockQueueConnection connection;
    private MockQueue queue;
    private boolean closed;
    private String messageSelector;

    public MockQueueBrowser(MockQueueConnection connection, MockQueue queue, String messageSelector)
    {
        this.connection = connection;
        this.queue = queue;
        closed = false;
        this.messageSelector = messageSelector;
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
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        closed = true;
    }  
}
