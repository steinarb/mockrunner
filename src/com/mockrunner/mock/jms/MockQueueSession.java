package com.mockrunner.mock.jms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import com.mockrunner.jms.MessageManager;
import com.mockrunner.jms.TransmissionManager;

/**
 * Mock implementation of JMS <code>QueueSession</code>.
 */
public class MockQueueSession implements QueueSession
{
    private MockQueueConnection connection;
    private TransmissionManager transmissionManager;
    private MessageManager messageManager;
    private boolean transacted;
    private int acknowledgeMode;
    private boolean committed;
    private boolean rolledback;
    private boolean recovered;
    private boolean closed;
    private MessageListener messageListener;
    private List tempQueues;
    
    public MockQueueSession(MockQueueConnection connection, boolean transacted, int acknowledgeMode)
    {
        this.connection = connection;
        this.transacted = transacted;
        this.acknowledgeMode = acknowledgeMode;
        transmissionManager = new TransmissionManager(connection);
        messageManager = new MessageManager();
        committed = false;
        rolledback = false;
        recovered = false;
        closed = false;
        messageListener = null;
        tempQueues = new ArrayList();
    }
    
    /**
     * Returns the {@link TransmissionManager} for this connection.
     * @return the {@link TransmissionManager}
     */
    public TransmissionManager getTransmissionManager()
    {
        return transmissionManager;
    }

    /**
     * Returns the {@link MessageManager} for this connection.
     * @return the {@link MessageManager}
     */
    public MessageManager getMessageManager()
    {
        return messageManager;
    }
    
    /**
     * Returns the list of temporary queues.
     * @return the <code>TemporaryQueue</code> list
     */
    public List getTemporaryQueueList()
    {
        return Collections.unmodifiableList(tempQueues);
    }
    
    /**
     * Returns a <code>TemporaryQueue</code> by its index. The
     * index represent the number of the queue. Returns <code>null</code>
     * if no such <code>TemporaryQueue</code> is present.
     * @param index the index
     * @return the <code>TemporaryQueue</code>
     */
    public MockTemporaryQueue getTemporaryQueue(int index)
    {
        if(tempQueues.size() <= index || index < 0) return null;
        return (MockTemporaryQueue)tempQueues.get(index);
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
     * Returns the acknowledge mode for this session.
     * @return the acknowledge mode
     */
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
        addSessionToQueue(queue);
        return queue;
    }
    
    public TemporaryQueue createTemporaryQueue() throws JMSException
    {
        connection.throwJMSException();
        MockTemporaryQueue queue = new MockTemporaryQueue(connection);
        tempQueues.add(queue);
        addSessionToQueue(queue);
        return queue;
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
        addSessionToQueue(queue);
        return getTransmissionManager().createQueueReceiver((MockQueue)queue, messageSelector);
    }

    public QueueSender createSender(Queue queue) throws JMSException
    {
        connection.throwJMSException();
        if(!(queue instanceof MockQueue))
        {
            throw new JMSException("queue must be an instance of MockQueue");
        }
        addSessionToQueue(queue);
        return getTransmissionManager().createQueueSender((MockQueue)queue);
    }

    public QueueBrowser createBrowser(Queue queue) throws JMSException
    {
        connection.throwJMSException();
        return createBrowser(queue, null);
    }

    public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException
    {
        connection.throwJMSException();
        if(!(queue instanceof MockQueue))
        {
            throw new JMSException("queue must be an instance of MockQueue");
        }
        addSessionToQueue(queue);
        return getTransmissionManager().createQueueBrowser((MockQueue)queue, messageSelector);
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

    public boolean getTransacted() throws JMSException
    {
        connection.throwJMSException();
        return transacted;
    }

    public void commit() throws JMSException
    {
        connection.throwJMSException();
        committed = true;
    }

    public void rollback() throws JMSException
    {
        connection.throwJMSException();
        rolledback = true;
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        closed = true;
    }

    public void recover() throws JMSException
    {
        connection.throwJMSException();
        recovered = true;
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
    
    private void addSessionToQueue(Queue queue)
    {
        if(queue instanceof MockQueue)
        {
            ((MockQueue)queue).addQueueSession(this);
        }
    }
}
