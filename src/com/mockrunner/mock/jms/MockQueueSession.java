package com.mockrunner.mock.jms;

import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

/**
 * Mock implementation of JMS <code>QueueSession</code>.
 */
public class MockQueueSession implements QueueSession
{
    private MockQueueConnection connection;
    private boolean transacted;
    private int acknowledgeMode;
    
    public MockQueueSession(MockQueueConnection connection, boolean transacted, int acknowledgeMode)
    {
        this.connection = connection;
        this.transacted = transacted;
        this.acknowledgeMode = acknowledgeMode;
    }
    
    public boolean isTransacted()
    {
        return transacted;
    }
    
    public int getAcknowledgeMode()
    {
        return acknowledgeMode;
    }
    
    public Queue createQueue(String name) throws JMSException
    {
        connection.throwJMSException();
        MockQueue queue = connection.getDestinationManager().getQueue(name);
        if(null == queue)
        {
            throw new JMSException("Queue with name " + name + " not found");
        }
        return queue;
    }
    
    public TemporaryQueue createTemporaryQueue() throws JMSException
    {
        connection.throwJMSException();
        return connection.getDestinationManager().createTemporaryQueue();
    }

    public QueueReceiver createReceiver(Queue queue) throws JMSException
    {
        connection.throwJMSException();
        return createReceiver(queue, null);
    }

    public QueueReceiver createReceiver(Queue queue, String messageSelector) throws JMSException
    {
        connection.throwJMSException();
        if(!(queue instanceof MockQueue))
        {
            throw new JMSException("queue must be an instance of MockQueue");
        }
        return connection.getDestinationManager().createQueueReceiver((MockQueue)queue);
    }

    public QueueSender createSender(Queue queue) throws JMSException
    {
        connection.throwJMSException();
        if(!(queue instanceof MockQueue))
        {
            throw new JMSException("queue must be an instance of MockQueue");
        }
        return connection.getDestinationManager().createQueueSender((MockQueue)queue);
    }

    public QueueBrowser createBrowser(Queue arg0) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public QueueBrowser createBrowser(Queue arg0, String arg1) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public BytesMessage createBytesMessage() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public MapMessage createMapMessage() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public Message createMessage() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public ObjectMessage createObjectMessage() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public ObjectMessage createObjectMessage(Serializable arg0) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public StreamMessage createStreamMessage() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public TextMessage createTextMessage() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public TextMessage createTextMessage(String arg0) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public boolean getTransacted() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return false;
    }

    public void commit() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public void rollback() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public void close() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public void recover() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public MessageListener getMessageListener() throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
        return null;
    }

    public void setMessageListener(MessageListener arg0) throws JMSException
    {
        // TODO Auto-generated method stub
        connection.throwJMSException();
    }

    public void run()
    {
        // TODO Auto-generated method stub
    }
}
