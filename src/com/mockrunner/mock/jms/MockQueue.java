package com.mockrunner.mock.jms;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueSession;

/**
 * Mock implementation of JMS <code>Queue</code>.
 */
public class MockQueue implements Queue
{
    private MockQueueConnection connection;
    private Set sessions;
    private String name;
    
    public MockQueue(MockQueueConnection connection, String name)
    {
        this.name = name;
        this.connection = connection;
        sessions = new HashSet();
    }
    
    public String getQueueName() throws JMSException
    {
        connection.throwJMSException();
        return name;
    }
    
    /**
     * Adds a message to this <code>Queue</code> that will
     * be propagated to the corresponding receiver. Only one
     * receiver will get the message. The order is not
     * predictable.
     * @param message the message
     */
    public void addMessage(Message message) throws JMSException
    {
        Iterator sessionsIterator = sessions.iterator();
        if(sessionsIterator.hasNext())
        {
            MockQueueSession session = (MockQueueSession)sessionsIterator.next();
            MessageListener globalListener = session.getMessageListener();
            if(null != globalListener)
            {
                globalListener.onMessage(message);
            }
            else
            {
                MockQueueReceiver receiver = session.getTransmissionManager().getQueueReceiver(name);
                if(null != receiver)
                {
                    receiver.receiveMessage(message);
                }
            }
        }
    }
    
    /**
     * Adds a <code>QueueSession</code>. This enables the session
     * to access this <code>Queue</code>.
     * @param session the session
     */
    public void addQueueSession(QueueSession session)
    {
        sessions.add(session);
    }
    
    protected MockQueueConnection getConnection()
    {
        return connection;
    }
}
