package com.mockrunner.mock.jms;

import java.util.Enumeration;
import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

/**
 * Mock implementation of JMS <code>QueueBrowser</code>.
 */
public class MockQueueBrowser implements QueueBrowser
{
    private MockConnection connection;
    private MockQueue queue;
    private boolean closed;
    private String messageSelector;
    
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
        return new Vector(queue.getCurrentMessageList()).elements();
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        closed = true;
    }  
}
