package com.mockrunner.mock.jms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import com.mockrunner.jms.MessageManager;
import com.mockrunner.jms.QueueTransmissionManager;
import com.mockrunner.jms.TopicTransmissionManager;
import com.mockrunner.jms.TransmissionManagerWrapper;

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
    private QueueTransmissionManager queueTransManager;
    private TopicTransmissionManager topicTransManager;
    private TransmissionManagerWrapper transManager;
    private MessageManager messageManager;
    private MessageListener messageListener;
    private List tempQueues;
    private List tempTopics;
    private boolean transacted;
    private int acknowledgeMode;
    private int numberCommits;
    private int numberRollbacks;
    private boolean recovered;
    private boolean closed;
    
    public MockSession(MockConnection connection, boolean transacted, int acknowledgeMode)
    {
        this.connection = connection;
        this.transacted = transacted;
        this.acknowledgeMode = acknowledgeMode;
        queueTransManager = new QueueTransmissionManager(connection, this);
        topicTransManager = new TopicTransmissionManager(connection, this);
        transManager = new TransmissionManagerWrapper(queueTransManager, topicTransManager);
        messageManager = new MessageManager();
        tempQueues = new ArrayList();
        tempTopics = new ArrayList();
        messageListener = null;
        numberCommits = 0;
        numberRollbacks = 0;
        recovered = false;
        closed = false;
    }
    
    /**
     * Returns the {@link com.mockrunner.jms.QueueTransmissionManager}.
     * @return the {@link com.mockrunner.jms.QueueTransmissionManager}
     */
    public QueueTransmissionManager getQueueTransmissionManager()
    {
        return queueTransManager;
    }
    
    /**
     * Returns the {@link com.mockrunner.jms.TopicTransmissionManager}.
     * @return the {@link com.mockrunner.jms.TopicTransmissionManager}
     */
    public TopicTransmissionManager getTopicTransmissionManager()
    {
        return topicTransManager;
    }
    
    /**
     * Returns the {@link com.mockrunner.jms.TransmissionManagerWrapper}.
     * @return the {@link com.mockrunner.jms.TransmissionManagerWrapper}
     */
    public TransmissionManagerWrapper getTransmissionManager()
    {
        return transManager;
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
     * Returns the list of temporary topics.
     * @return the <code>TemporaryTopic</code> list
     */
    public List getTemporaryTopicList()
    {
        return Collections.unmodifiableList(tempTopics);
    }

    /**
     * Returns a <code>TemporaryTopic</code> by its index. The
     * index represent the number of the topic. Returns <code>null</code>
     * if no such <code>TemporaryTopic</code> is present.
     * @param index the index
     * @return the <code>TemporaryTopic</code>
     */
    public MockTemporaryTopic getTemporaryTopic(int index)
    {
        if(tempTopics.size() <= index || index < 0) return null;
        return (MockTemporaryTopic)tempTopics.get(index);
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
        return (numberCommits > 0);
    }
    
    /**
     * Returns the number of commits.
     * @return the number of commits
     */
    public int getNumberCommits()
    {
        return numberCommits;
    }

    /**
     * Returns if the current transaction was rolled back.
     * @return <code>true</code> if the transaction was rolled back
     */
    public boolean isRolledBack()
    {
        return (numberRollbacks > 0);
    }
    
    /**
     * Returns the number of rollbacks.
     * @return the number of rollbacks
     */
    public int getNumberRollbacks()
    {
        return numberRollbacks;
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
     * exist in 1.0.2) this method returns hardcoded <code>0</code>,
     * if the session is transacted.
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
        numberCommits++;
    }

    public void rollback() throws JMSException
    {
        connection.throwJMSException();
        recover();
        numberRollbacks++;
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        if(getTransacted() && !isCommitted())
        {
            rollback();
        }
        getQueueTransmissionManager().closeAll();
        getTopicTransmissionManager().closeAll();
        closed = true;
    }

    public void recover() throws JMSException
    {
        connection.throwJMSException();
        recovered = true;
    }
    
    public void unsubscribe(String name) throws JMSException
    {
        getConnection().throwJMSException();
        topicTransManager.removeTopicDurableSubscriber(name);
    }
    
    public Queue createQueue(String name) throws JMSException
    {
        getConnection().throwJMSException();
        MockQueue queue = ((MockConnection)getConnection()).getDestinationManager().getQueue(name);
        if(null == queue)
        {
            throw new JMSException("Queue with name " + name + " not found");
        }
        addSessionToQueue(queue);
        return queue;
    }

    public TemporaryQueue createTemporaryQueue() throws JMSException
    {
        getConnection().throwJMSException();
        MockTemporaryQueue queue = new MockTemporaryQueue();
        tempQueues.add(queue);
        addSessionToQueue(queue);
        return queue;
    }
    
    public Topic createTopic(String name) throws JMSException
    {
        getConnection().throwJMSException();
        MockTopic topic = ((MockConnection)getConnection()).getDestinationManager().getTopic(name);
        if(null == topic)
        {
            throw new JMSException("Topic with name " + name + " not found");
        }
        addSessionToTopic(topic);
        return topic;
    }

    public TemporaryTopic createTemporaryTopic() throws JMSException
    {
        getConnection().throwJMSException();
        MockTemporaryTopic topic = new MockTemporaryTopic();
        tempTopics.add(topic);
        addSessionToTopic(topic);
        return topic;
    }
    
    public MessageConsumer createConsumer(Destination destination) throws JMSException
    {
        getConnection().throwJMSException();
        return createConsumer(destination, null);
    }
    
    public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException
    {
        getConnection().throwJMSException();
        return createConsumer(destination, messageSelector, false);
    }
    
    public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal) throws JMSException
    {
		if(null == destination)
		{
			throw new RuntimeException("destination must not be null");
		}
        if(destination instanceof MockQueue)
        {
            getConnection().throwJMSException();      
            addSessionToQueue((Queue)destination);
            return getQueueTransmissionManager().createQueueReceiver((MockQueue)destination, messageSelector);
        }
        else if(destination instanceof MockTopic)
        {
            getConnection().throwJMSException();
            addSessionToTopic((Topic)destination);
            return getTopicTransmissionManager().createTopicSubscriber((MockTopic)destination, messageSelector, noLocal);
        }
        else
        {
            throw new InvalidDestinationException("destination must be an instance of MockQueue or MockTopic");
        }
    }
    
    public MessageProducer createProducer(Destination destination) throws JMSException
    {
        if(null == destination)
        {
        	throw new RuntimeException("destination must not be null");
        }
        if(destination instanceof MockQueue)
        {
            getConnection().throwJMSException();
            addSessionToQueue((Queue)destination);
            return getQueueTransmissionManager().createQueueSender((MockQueue)destination);
        }
        else if(destination instanceof MockTopic)
        {
            getConnection().throwJMSException();
            addSessionToTopic((Topic)destination);
            return getTopicTransmissionManager().createTopicPublisher((MockTopic)destination);
        }
        else
        {
            throw new InvalidDestinationException("destination must be an instance of MockQueue or MockTopic");
        }
    }
    
    public QueueBrowser createBrowser(Queue queue) throws JMSException
    {
        getConnection().throwJMSException();
        return createBrowser(queue, null);
    }

    public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException
    {
        getConnection().throwJMSException();
        if(!(queue instanceof MockQueue))
        {
            throw new InvalidDestinationException("queue must be an instance of MockQueue");
        }
        addSessionToQueue(queue);
        return queueTransManager.createQueueBrowser((MockQueue)queue, messageSelector);
    }
    
    public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException
    {
        getConnection().throwJMSException();
        return createDurableSubscriber(topic, name, null, false);
    }

    public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException
    {
        getConnection().throwJMSException();
        if(!(topic instanceof MockTopic))
        {
            throw new InvalidDestinationException("topic must be an instance of MockTopic");
        }
        addSessionToTopic(topic);
        return topicTransManager.createDurableTopicSubscriber((MockTopic)topic, name, messageSelector, noLocal);
    }
    
    protected MockConnection getConnection()
    {
        return connection;
    }
    
    protected void addSessionToQueue(Queue queue)
    {
        if(queue instanceof MockQueue)
        {
            ((MockQueue)queue).addSession(this);
        }
    }
    
    protected void addSessionToTopic(Topic topic)
    {
        if(topic instanceof MockTopic)
        {
            ((MockTopic)topic).addSession(this);
        }
    }
}
