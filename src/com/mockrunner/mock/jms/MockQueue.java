package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueSession;

/**
 * Mock implementation of JMS <code>Queue</code>.
 */
public class MockQueue implements Queue, Cloneable
{
    private MockQueueConnection connection;
    private QueueSession session;
    private String name;
    
    public MockQueue(MockQueueConnection connection, String name)
    {
        this.name = name;
        this.connection = connection;
    }
    
    public String getQueueName() throws JMSException
    {
        connection.throwJMSException();
        return name;
    }
    
    public void addMessage(Message message) throws JMSException
    {
        MessageListener globalListener = session.getMessageListener();
        if(null != globalListener)
        {
            globalListener.onMessage(message);
            return;
        }
        MockQueueReceiver receiver = connection.getTransmissionManager().getQueueReceiver(0);
        if(null != receiver)
        {
            receiver.receiveMessage(message);
        }
    }
    
    public void setQueueSession(QueueSession session)
    {
        this.session = session;
    }
    
    protected MockQueueConnection getConnection()
    {
        return connection;
    }
    
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
