package com.mockrunner.jms;

import java.util.Iterator;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.jms.JMSMockObjectFactory;
import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockMessage;
import com.mockrunner.mock.jms.MockMessageConsumer;
import com.mockrunner.mock.jms.MockMessageProducer;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueBrowser;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueConnectionFactory;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockQueueSession;
import com.mockrunner.mock.jms.MockSession;
import com.mockrunner.mock.jms.MockTemporaryQueue;
import com.mockrunner.mock.jms.MockTemporaryTopic;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicConnection;
import com.mockrunner.mock.jms.MockTopicConnectionFactory;
import com.mockrunner.mock.jms.MockTopicPublisher;
import com.mockrunner.mock.jms.MockTopicSession;
import com.mockrunner.mock.jms.MockTopicSubscriber;

/**
 * Module for JMS tests.
 * Note that all indices are zero based.<br>
 * Note for JMS 1.1:
 * If you use {@link MockQueueConnectionFactory} for creating your
 * connections and sessions, you have to use the methods with <code>Queue</code>
 * in their name. Same with {@link MockTopicConnectionFactory}.
 * The methods without <code>Queue</code> and <code>Topic</code>
 * in the method name are for connections and sessions that were
 * created using the {@link com.mockrunner.mock.jms.MockConnectionFactory}.
 * {@link com.mockrunner.mock.jms.MockConnectionFactory}
 * also implements <code>QueueConnectionFactory</code> and
 * <code>TopicConnectionFactory</code> interfaces and can be used to create 
 * queue and topic connections as well as generic JMS 1.1 connections. 
 * It is recommended to use {@link com.mockrunner.mock.jms.MockQueueConnectionFactory}
 * if you only use queues and  {@link com.mockrunner.mock.jms.MockTopicConnectionFactory}
 * if you only use topics and are not interested in having one factory for both.
 * It is possible to create a {@link MockQueueConnection} or a {@link MockTopicConnection}
 * using the {@link com.mockrunner.mock.jms.MockConnectionFactory}.
 * However, the <code>Queue</code> methods (e.g. {@link #verifyAllQueueReceiversClosed})
 * only work, if you use {@link MockQueueConnectionFactory} and the 
 * <code>Topic</code> methods (e.g. {@link #verifyCreatedTopicMapMessageNotAcknowledged})
 * only work, if you use {@link MockTopicConnectionFactory}.
 */
public class JMSTestModule
{
    private JMSMockObjectFactory mockFactory;
    private int currentQueueConnectionIndex;
    private int currentTopicConnectionIndex;
    private int currentConnectionIndex;
  
    public JMSTestModule(JMSMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
        currentQueueConnectionIndex = -1;
        currentTopicConnectionIndex = -1;
        currentConnectionIndex = -1;
    }
    
    /**
     * Sets the index of the {@link MockQueueConnection} that should be used
     * for the current test. Per default the latest created connection
     * is used.
     * @param connectionIndex the index of the connection
     */
    public void setCurrentQueueConnectionIndex(int connectionIndex)
    {
        this.currentQueueConnectionIndex = connectionIndex;
    }
    
    /**
     * Returns the current {@link MockQueueConnection} based on its
     * index or <code>null</code> if no queue connection
     * was created. The connection has to be created using the
     * {@link MockQueueConnectionFactory}.
     * @return the queue connection
     */
    public MockQueueConnection getCurrentQueueConnection()
    {
        if(0 > currentQueueConnectionIndex)
        { 
            return mockFactory.getMockQueueConnectionFactory().getLatestQueueConnection();
        }
        return mockFactory.getMockQueueConnectionFactory().getQueueConnection(currentQueueConnectionIndex);
    }
    
    /**
     * Sets the index of the {@link MockTopicConnection} that should be used
     * for the current test. Per default the latest created connection
     * is used. 
     * @param connectionIndex the index of the connection
     */
    public void setCurrentTopicConnectionIndex(int connectionIndex)
    {
        this.currentTopicConnectionIndex = connectionIndex;
    }

    /**
     * Returns the current {@link MockTopicConnection} based on its
     * index or <code>null</code> if no topic connection
     * was created. The connection has to be created using the
     * {@link MockTopicConnectionFactory}.
     * @return the topic connection
     */
    public MockTopicConnection getCurrentTopicConnection()
    {
        if(0 > currentTopicConnectionIndex)
        { 
            return mockFactory.getMockTopicConnectionFactory().getLatestTopicConnection();
        }
        return mockFactory.getMockTopicConnectionFactory().getTopicConnection(currentTopicConnectionIndex);
    }
    
    /**
     * Sets the index of the {@link MockConnection} that should be used
     * for the current test. Per default the latest created connection
     * is used.
     * @param connectionIndex the index of the connection
     */
    public void setCurrentConnectionIndex(int connectionIndex)
    {
        this.currentConnectionIndex = connectionIndex;
    }

    /**
     * Returns the current {@link MockConnection} based on its
     * index or <code>null</code> if no connection
     * was created. The connection has to be created using the
     * {@link com.mockrunner.mock.jms.MockConnectionFactory}.
     * @return the topic connection
     */
    public MockConnection getCurrentConnection()
    {
        if(0 > currentConnectionIndex)
        { 
            return mockFactory.getMockConnectionFactory().getLatestConnection();
        }
        return mockFactory.getMockConnectionFactory().getConnection(currentConnectionIndex);
    }
 
    /**
     * Creates a new connection and uses it for creating a new session and receiver.
     * Registers the specified listener. Starts the connection for message
     * receiving. This method is useful for creating test listeners when
     * testing senders. It can be used to register message driven beans.
     * Note that the created connection is the latest created
     * connection and automatically becomes the default connection for the
     * test module. The created session is transacted.
     * This method uses the {@link MockQueueConnectionFactory} for
     * creating the connection and the session. If you want to use the
     * {@link com.mockrunner.mock.jms.MockConnectionFactory} you have to create the connection on your own
     * and call {@link #registerTestMessageListenerForQueue(MockConnection, String, MessageListener)}.
     * @param queueName the name of the queue used for message receiving
     * @param listener the listener that should be registered
     */
    public void registerTestMessageListenerForQueue(String queueName, MessageListener listener)
    {
        try
        {
            MockQueueConnectionFactory factory = mockFactory.getMockQueueConnectionFactory();
            MockQueueConnection connection = (MockQueueConnection)factory.createQueueConnection();
            registerTestMessageListenerForQueue(connection, queueName, listener);
        }
        catch(JMSException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Creates a new session and receiver using the specified connection and
     * registers the specified listener. Starts the connection for message
     * receiving. This method is useful for creating test listeners when
     * testing senders. It can be used to register message driven beans.
     * The created session is transacted.
     * @param connection the connection used for creating the session
     * @param queueName the name of the queue used for message receiving
     * @param listener the listener that should be registered
     */
    public void registerTestMessageListenerForQueue(MockConnection connection, String queueName, MessageListener listener)
    {
        registerTestMessageListenerForQueue(connection, queueName, true, Session.AUTO_ACKNOWLEDGE, listener);
    }
    
    /**
     * Creates a new session and receiver using the specified connection and
     * registers the specified listener. Starts the connection for message
     * receiving. This method is useful for creating test listeners when
     * testing senders. It can be used to register message driven beans.
     * @param connection the connection used for creating the session
     * @param queueName the name of the queue used for message receiving
     * @param transacted should the created session be transacted
     * @param acknowledgeMode the acknowledge mode of the created session
     * @param listener the listener that should be registered
     */
    public void registerTestMessageListenerForQueue(MockConnection connection, String queueName, boolean transacted, int acknowledgeMode, MessageListener listener)
    {
        registerTestMessageListenerForQueue(connection, queueName, transacted, acknowledgeMode, null, listener);
    }
    
    /**
     * Creates a new session and receiver using the specified connection and
     * registers the specified listener. Starts the connection for message
     * receiving. This method is useful for creating test listeners when
     * testing senders. It can be used to register message driven beans.
     * @param connection the connection used for creating the session
     * @param queueName the name of the queue used for message receiving
     * @param transacted should the created session be transacted
     * @param acknowledgeMode the acknowledge mode of the created session
     * @param messageSelector the message selector
     * @param listener the listener that should be registered
     */
    public void registerTestMessageListenerForQueue(MockConnection connection, String queueName, boolean transacted, int acknowledgeMode, String messageSelector, MessageListener listener)
    {
        try
        {
            Queue queue = getDestinationManager().getQueue(queueName);
            MockSession session = (MockSession)connection.createSession(transacted, acknowledgeMode);
            MessageConsumer consumer = session.createConsumer(queue, messageSelector);
            consumer.setMessageListener(listener);
            connection.start();
        }
        catch(JMSException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Creates a new connection and uses it for creating a new session and subscriber.
     * Registers the specified listener. Starts the connection for message
     * receiving. This method is useful for creating test listeners when
     * testing publishers. It can be used to resgister message driven beans.
     * Note that the created connection is the latest created
     * connection and automatically becomes the default connection for the
     * test module. The created session is transacted.
     * This method uses the {@link MockTopicConnectionFactory} for
     * creating the connection and the session. If you want to use the
     * {@link com.mockrunner.mock.jms.MockConnectionFactory} you have to create the connection on your own
     * and call {@link #registerTestMessageListenerForTopic(MockConnection, String, MessageListener)}.
     * @param topicName the name of the topic used for message receiving
     * @param listener the listener that should be registered
     */
    public void registerTestMessageListenerForTopic(String topicName, MessageListener listener)
    {
        try
        {
            MockTopicConnectionFactory factory = mockFactory.getMockTopicConnectionFactory();
            MockTopicConnection connection = (MockTopicConnection)factory.createTopicConnection();
            registerTestMessageListenerForTopic(connection, topicName, listener);
        }
        catch(JMSException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }

    /**
     * Creates a new session and subscriber using the specified connection and
     * registers the specified listener. Starts the connection for message
     * receiving. This method is useful for creating test listeners when
     * testing publishers. It can be used to resgister message driven beans.
     * The created session is transacted.
     * @param connection the connection used for creating the session
     * @param topicName the name of the topic used for message receiving
     * @param listener the listener that should be registered
     */
    public void registerTestMessageListenerForTopic(MockConnection connection, String topicName, MessageListener listener)
    {
        registerTestMessageListenerForTopic(connection, topicName, true, Session.AUTO_ACKNOWLEDGE, listener);
    }
    
    /**
     * Creates a new session and subscriber using the specified connection and
     * registers the specified listener. Starts the connection for message
     * receiving. This method is useful for creating test listeners when
     * testing publishers. It can be used to resgister message driven beans.
     * @param connection the connection used for creating the session
     * @param topicName the name of the topic used for message receiving
     * @param transacted should the created session be transacted
     * @param acknowledgeMode the acknowledge mode of the created session
     * @param listener the listener that should be registered
     */
    public void registerTestMessageListenerForTopic(MockConnection connection, String topicName, boolean transacted, int acknowledgeMode, MessageListener listener)
    {
        registerTestMessageListenerForTopic(connection, topicName, transacted, acknowledgeMode, null, listener);
    }
    
    /**
     * Creates a new session and subscriber using the specified connection and
     * registers the specified listener. Starts the connection for message
     * receiving. This method is useful for creating test listeners when
     * testing publishers. It can be used to resgister message driven beans.
     * @param connection the connection used for creating the session
     * @param topicName the name of the topic used for message receiving
     * @param transacted should the created session be transacted
     * @param acknowledgeMode the acknowledge mode of the created session
     * @param messageSelector the message selector
     * @param listener the listener that should be registered
     */
    public void registerTestMessageListenerForTopic(MockConnection connection, String topicName, boolean transacted, int acknowledgeMode, String messageSelector, MessageListener listener)
    {
        try
        {
            Topic topic = getDestinationManager().getTopic(topicName);
            MockSession session = (MockSession)connection.createSession(transacted, acknowledgeMode);
            MessageConsumer consumer = session.createConsumer(topic, messageSelector);
            consumer.setMessageListener(listener);
            connection.start();
        }
        catch(JMSException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Returns the {@link DestinationManager}.
     * @return the {@link DestinationManager}
     */
    public DestinationManager getDestinationManager()
    {
        return mockFactory.getDestinationManager();
    }
    
    /**
     * Returns the {@link ConfigurationManager}.
     * @return the {@link ConfigurationManager}
     */
    public ConfigurationManager getConfigurationManager()
    {
        return mockFactory.getConfigurationManager();
    }

    /**
     * Returns the {@link MessageManager} for the specified session
     * or <code>null</code> if the session does not exist. The returned
     * {@link MessageManager} is used to keep track of messages sent
     * to queues.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link MessageManager}
     */
    public MessageManager getQueueMessageManager(int indexOfSession)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session) return null;
        return session.getMessageManager();
    }
    
    /**
     * Returns the {@link MessageManager} for the specified session
     * or <code>null</code> if the session does not exist. The returned
     * {@link MessageManager} is used to keep track of messages sent
     * to topics.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link MessageManager}
     */
    public MessageManager getTopicMessageManager(int indexOfSession)
    {
        MockTopicSession session = getTopicSession(indexOfSession);
        if(null == session) return null;
        return session.getMessageManager();
    }
    
    /**
     * Returns the {@link MessageManager} for the specified session
     * or <code>null</code> if the session does not exist. The returned
     * {@link MessageManager} is used to keep track of messages sent
     * to topics.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link MessageManager}
     */
    public MessageManager getMessageManager(int indexOfSession)
    {
        MockSession session = getSession(indexOfSession);
        if(null == session) return null;
        return session.getMessageManager();
    }
    
    /**
     * Returns the {@link QueueTransmissionManager} for the specified session
     * or <code>null</code> if the session does not exist.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link QueueTransmissionManager}
     */
    public QueueTransmissionManager getQueueTransmissionManager(int indexOfSession)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session) return null;
        return session.getQueueTransmissionManager();
    }
    
    /**
     * Returns the {@link TopicTransmissionManager} for the specified session
     * or <code>null</code> if the session does not exist.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link TopicTransmissionManager}
     */
    public TopicTransmissionManager getTopicTransmissionManager(int indexOfSession)
    {
        MockTopicSession session = getTopicSession(indexOfSession);
        if(null == session) return null;
        return session.getTopicTransmissionManager();
    }
    
    /**
     * @deprecated use {@link #getTransmissionManagerWrapper}
     */
    public TransmissionManagerWrapper getTransmissionManager(int indexOfSession)
    {
        return getTransmissionManagerWrapper(indexOfSession);
    }
    
    /**
     * Returns the {@link TransmissionManagerWrapper} for the specified session
     * or <code>null</code> if the session does not exist.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link TransmissionManagerWrapper}
     */
    public TransmissionManagerWrapper getTransmissionManagerWrapper(int indexOfSession)
    {
        MockSession session = getSession(indexOfSession);
        if(null == session) return null;
        return session.getTransmissionManagerWrapper();
    }
    
    /**
     * Returns the {@link TransmissionManagerWrapper} for the specified session
     * or <code>null</code> if the session does not exist.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link TransmissionManagerWrapper}
     */
    public TransmissionManagerWrapper getQueueTransmissionManagerWrapper(int indexOfSession)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session) return null;
        return session.getTransmissionManagerWrapper();
    }
    
    /**
     * Returns the {@link TransmissionManagerWrapper} for the specified session
     * or <code>null</code> if the session does not exist.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link TransmissionManagerWrapper}
     */
    public TransmissionManagerWrapper getTopicTransmissionManagerWrapper(int indexOfSession)
    {
        MockTopicSession session = getTopicSession(indexOfSession);
        if(null == session) return null;
        return session.getTransmissionManagerWrapper();
    }
    
    /**
     * Returns the list of {@link MockQueueSession} objects.
     * The sessions have been created using the current {@link MockQueueConnection}.
     * @return the {@link MockQueueSession} list
     */
    public List getQueueSessionList()
    {
        if(null == getCurrentQueueConnection()) return null;
        return getCurrentQueueConnection().getQueueSessionList();
    }
    
    /**
     * Returns the list of {@link MockTopicSession} objects.
     * The sessions have been created using the current {@link MockTopicConnection}.
     * @return the {@link MockTopicSession} list
     */
    public List getTopicSessionList()
    {
        if(null == getCurrentTopicConnection()) return null;
        return getCurrentTopicConnection().getTopicSessionList();
    }
    
    /**
     * Returns the list of {@link MockSession} objects.
     * The sessions have been created using the current {@link MockConnection}.
     * @return the {@link MockSession} list
     */
    public List getSessionList()
    {
        if(null == getCurrentConnection()) return null;
        return getCurrentConnection().getSessionList();
    }
    
    /**
     * Returns the {@link MockQueueSession} for the specified index
     * or <code>null</code> if the session does not exist.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link MockQueueSession}
     */
    public MockQueueSession getQueueSession(int indexOfSession)
    {
        if(null == getCurrentQueueConnection()) return null;
        return getCurrentQueueConnection().getQueueSession(indexOfSession);
    }
    
    /**
     * Returns the {@link MockTopicSession} for the specified index
     * or <code>null</code> if the session does not exist.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link MockTopicSession}
     */
    public MockTopicSession getTopicSession(int indexOfSession)
    {
        if(null == getCurrentTopicConnection()) return null;
        return getCurrentTopicConnection().getTopicSession(indexOfSession);
    }
    
    /**
     * Returns the {@link MockSession} for the specified index
     * or <code>null</code> if the session does not exist.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link MockSession}
     */
    public MockSession getSession(int indexOfSession)
    {
        if(null == getCurrentConnection()) return null;
        return getCurrentConnection().getSession(indexOfSession);
    }
    
    /**
     * Returns the {@link MockQueue} with the specified name
     * or <code>null</code> if no such queue exists.
     * @param name the name of the queue
     * @return the {@link MockQueue}
     */
    public MockQueue getQueue(String name)
    {
        return getDestinationManager().getQueue(name);
    }
    
    /**
     * Returns the {@link MockTopic} with the specified name
     * or <code>null</code> if no such topic exists.
     * @param name the name of the topic
     * @return the {@link MockTopic}
     */
    public MockTopic getTopic(String name)
    {
        return getDestinationManager().getTopic(name);
    }
    
    /**
     * Returns the list of {@link MockTemporaryQueue} objects
     * for the specified session. The session has to be created using
     * the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link MockTemporaryQueue} list
     */
    public List getTemporaryQueueList(int indexOfSession)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session) return null;
        return session.getTemporaryQueueList();
    }
    
    /**
     * Returns the list of {@link MockTemporaryTopic} objects
     * for the specified session. The session has to be created using
     * the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @return the {@link MockTemporaryTopic} list
     */
    public List getTemporaryTopicList(int indexOfSession)
    {
        MockTopicSession session = getTopicSession(indexOfSession);
        if(null == session) return null;
        return session.getTemporaryTopicList();
    }
    
    /**
     * Returns the {@link MockTemporaryQueue} with the specified index
     * for the specified session. Returns <code>null</code> if no such
     * temporary queue exists. The session has to be created using
     * the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @return the {@link MockTemporaryQueue}
     */
    public MockTemporaryQueue getTemporaryQueue(int indexOfSession, int indexOfQueue)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session) return null;
        return session.getTemporaryQueue(indexOfQueue);
    }
    
    /**
     * Returns the {@link MockTemporaryTopic} with the specified index
     * for the specified session. Returns <code>null</code> if no such
     * temporary queue exists. The session has to be created using
     * the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary queue
     * @return the {@link MockTemporaryTopic}
     */
    public MockTemporaryTopic getTemporaryTopic(int indexOfSession, int indexOfTopic)
    {
        MockTopicSession session = getTopicSession(indexOfSession);
        if(null == session) return null;
        return session.getTemporaryTopic(indexOfTopic);
    }
    
    /**
     * Returns the list of messages that are currently present in the queue
     * or <code>null</code> if no such queue exists.
     * @param name the name of the queue
     * @return the list of messages
     */
    public List getCurrentMessageListFromQueue(String name)
    {
        MockQueue queue = getQueue(name);
        if(null == queue) return null;
        return queue.getCurrentMessageList();
    }
    
    /**
     * Returns the list of messages that are currently present in the 
     * temporary queue or <code>null</code> if no such queue exists.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @return the list of messages
     */
    public List getCurrentMessageListFromTemporaryQueue(int indexOfSession, int indexOfQueue)
    {
        MockTemporaryQueue queue = getTemporaryQueue(indexOfSession, indexOfQueue);
        if(null == queue) return null;
        return queue.getCurrentMessageList();
    }
    
    /**
     * Returns the list of messages that were received by the queue
     * or <code>null</code> if no such queue exists.
     * @param name the name of the queue
     * @return the list of messages
     */
    public List getReceivedMessageListFromQueue(String name)
    {
        MockQueue queue = getQueue(name);
        if(null == queue) return null;
        return queue.getReceivedMessageList();
    }

    /**
     * Returns the list of messages that were received by the 
     * temporary queue or <code>null</code> if no such queue exists.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @return the list of messages
     */
    public List getReceivedMessageListFromTemporaryQueue(int indexOfSession, int indexOfQueue)
    {
        MockTemporaryQueue queue = getTemporaryQueue(indexOfSession, indexOfQueue);
        if(null == queue) return null;
        return queue.getReceivedMessageList();
    }
    
    /**
     * Returns the list of messages that are currently present in the topic
     * or <code>null</code> if no such topic exists.
     * @param name the name of the queue
     * @return the list of messages
     */
    public List getCurrentMessageListFromTopic(String name)
    {
        MockTopic topic = getTopic(name);
        if(null == topic) return null;
        return topic.getCurrentMessageList();
    }

    /**
     * Returns the list of messages that are currently present in the 
     * temporary topic or <code>null</code> if no such topic exists.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topic
     * @return the list of messages
     */
    public List getCurrentMessageListFromTemporaryTopic(int indexOfSession, int indexOfTopic)
    {
        MockTemporaryTopic topic = getTemporaryTopic(indexOfSession, indexOfTopic);
        if(null == topic) return null;
        return topic.getCurrentMessageList();
    }

    /**
     * Returns the list of messages that were received by the topic
     * or <code>null</code> if no such topic exists.
     * @param name the name of the topic
     * @return the list of messages
     */
    public List getReceivedMessageListFromTopic(String name)
    {
        MockTopic topic = getTopic(name);
        if(null == topic) return null;
        return topic.getReceivedMessageList();
    }

    /**
     * Returns the list of messages that were received by the 
     * temporary topic or <code>null</code> if no such topic exists.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topic
     * @return the list of messages
     */
    public List getReceivedMessageListFromTemporaryTopic(int indexOfSession, int indexOfTopic)
    {
        MockTemporaryTopic topic = getTemporaryTopic(indexOfSession, indexOfTopic);
        if(null == topic) return null;
        return topic.getReceivedMessageList();
    }
    
    /**
     * Verifies that the current {@link MockQueueConnection} is closed.
     * The connection has to be created using the current {@link MockQueueConnectionFactory}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueConnectionClosed()
    {
        if(null == getCurrentQueueConnection())
        {
            throw new VerifyFailedException("No QueueConnection present.");
        }
        if(!getCurrentQueueConnection().isClosed())
        {
            throw new VerifyFailedException("QueueConnection is not closed.");
        }
    }
    
    /**
     * Verifies that the current {@link MockQueueConnection} is started.
     * The connection has to be created using the current {@link MockQueueConnectionFactory}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueConnectionStarted()
    {
        if(null == getCurrentQueueConnection())
        {
            throw new VerifyFailedException("No QueueConnection present.");
        }
        if(!getCurrentQueueConnection().isStarted())
        {
            throw new VerifyFailedException("QueueConnection is not started.");
        }
    }
    
    /**
     * Verifies that the current {@link MockQueueConnection} is stopped.
     * The connection has to be created using the current {@link MockQueueConnectionFactory}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueConnectionStopped()
    {
        if(null == getCurrentQueueConnection())
        {
            throw new VerifyFailedException("No QueueConnection present.");
        }
        if(!getCurrentQueueConnection().isStopped())
        {
            throw new VerifyFailedException("QueueConnection is not stopped.");
        }
    }
    
    /**
     * Verifies that the current {@link MockTopicConnection} is closed.
     * The connection has to be created using the current {@link MockTopicConnectionFactory}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicConnectionClosed()
    {
        if(null == getCurrentTopicConnection())
        {
            throw new VerifyFailedException("No TopicConnection present.");
        }
        if(!getCurrentTopicConnection().isClosed())
        {
            throw new VerifyFailedException("TopicConnection is not closed.");
        }
    }

    /**
     * Verifies that the current {@link MockTopicConnection} is started.
     * The connection has to be created using the current {@link MockTopicConnectionFactory}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicConnectionStarted()
    {
        if(null == getCurrentTopicConnection())
        {
            throw new VerifyFailedException("No TopicConnection present.");
        }
        if(!getCurrentTopicConnection().isStarted())
        {
            throw new VerifyFailedException("TopicConnection is not started.");
        }
    }

    /**
     * Verifies that the current {@link MockTopicConnection} is stopped.
     * The connection has to be created using the current {@link MockTopicConnectionFactory}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicConnectionStopped()
    {
        if(null == getCurrentTopicConnection())
        {
            throw new VerifyFailedException("No TopicConnection present.");
        }
        if(!getCurrentTopicConnection().isStopped())
        {
            throw new VerifyFailedException("TopicConnection is not stopped.");
        }
    }
    
    /**
     * Verifies that the current {@link MockConnection} is closed.
     * The connection has to be created using the current {@link com.mockrunner.mock.jms.MockConnectionFactory}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyConnectionClosed()
    {
        if(null == getCurrentConnection())
        {
            throw new VerifyFailedException("No Connection present.");
        }
        if(!getCurrentConnection().isClosed())
        {
            throw new VerifyFailedException("Connection is not closed.");
        }
    }

    /**
     * Verifies that the current {@link MockConnection} is started.
     * The connection has to be created using the current {@link com.mockrunner.mock.jms.MockConnectionFactory}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyConnectionStarted()
    {
        if(null == getCurrentConnection())
        {
            throw new VerifyFailedException("No Connection present.");
        }
        if(!getCurrentConnection().isStarted())
        {
            throw new VerifyFailedException("Connection is not started.");
        }
    }

    /**
     * Verifies that the current {@link MockConnection} is stopped.
     * The connection has to be created using the current {@link com.mockrunner.mock.jms.MockConnectionFactory}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyConnectionStopped()
    {
        if(null == getCurrentConnection())
        {
            throw new VerifyFailedException("No Connection present.");
        }
        if(!getCurrentConnection().isStopped())
        {
            throw new VerifyFailedException("Connection is not stopped.");
        }
    }
    
    /**
     * Verifies that the queue session with the specified index is
     * closed.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSessionClosed(int indexOfSession)
    {
        MockQueueSession session = checkAndGetQueueSessionByIndex(indexOfSession);
        if(!session.isClosed())
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not closed.");
        }
    }
    
    /**
     * Verifies that the queue session with the specified index was
     * committed.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSessionCommitted(int indexOfSession)
    {
        MockQueueSession session = checkAndGetQueueSessionByIndex(indexOfSession);
        if(!session.isCommitted())
        {
            throw new VerifyFailedException("QueueSession is not committed.");
        }
    }
    
    /**
     * Verifies that the queue session with the specified index was
     * not committed.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSessionNotCommitted(int indexOfSession)
    {
        MockQueueSession session = checkAndGetQueueSessionByIndex(indexOfSession);
        if(session.isCommitted())
        {
            throw new VerifyFailedException("QueueSession is committed.");
        }
    }
    
    /**
     * Verifies the number of commits of the queue session with the specified index.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfCommits the expected number of commits
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSessionNumberCommits(int indexOfSession, int numberOfCommits)
    {
        MockQueueSession session = checkAndGetQueueSessionByIndex(indexOfSession);
        if(numberOfCommits != session.getNumberCommits())
        {
            throw new VerifyFailedException("QueueSession was commited " + session.getNumberCommits() + " times, expected " + numberOfCommits + " times");
        }
    }
    
    /**
     * Verifies that the queue session with the specified index was
     * rolled back.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSessionRolledBack(int indexOfSession)
    {
        MockQueueSession session = checkAndGetQueueSessionByIndex(indexOfSession);
        if(!session.isRolledBack())
        {
            throw new VerifyFailedException("QueueSession is not rolled back.");
        }
    }
    
    /**
     * Verifies that the queue session with the specified index was
     * not rolled back.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSessionNotRolledBack(int indexOfSession)
    {
        MockQueueSession session = checkAndGetQueueSessionByIndex(indexOfSession);
        if(session.isRolledBack())
        {
            throw new VerifyFailedException("QueueSession is rolled back.");
        }
    }
    
    /**
     * Verifies the number of rollbacks of the queue session with the specified index.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfRollbacks the expected number of rollbacks
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSessionNumberRollbacks(int indexOfSession, int numberOfRollbacks)
    {
        MockQueueSession session = checkAndGetQueueSessionByIndex(indexOfSession);
        if(numberOfRollbacks != session.getNumberRollbacks())
        {
            throw new VerifyFailedException("QueueSession was rolled back " + session.getNumberRollbacks() + " times, expected " + numberOfRollbacks + " times");
        }
    }
    
    /**
     * Verifies that the queue session with the specified index was
     * recovered.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSessionRecovered(int indexOfSession)
    {
        MockQueueSession session = checkAndGetQueueSessionByIndex(indexOfSession);
        if(!session.isRecovered())
        {
            throw new VerifyFailedException("QueueSession is not recovered.");
        }
    }
    
    /**
     * Verifies that the queue session with the specified index was
     * not recovered.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSessionNotRecovered(int indexOfSession)
    {
        MockQueueSession session = checkAndGetQueueSessionByIndex(indexOfSession);
        if(session.isRecovered())
        {
            throw new VerifyFailedException("QueueSession is recovered.");
        }
    }
    
    /**
     * Verifies that the topic session with the specified index is
     * closed.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicSessionClosed(int indexOfSession)
    {
        MockTopicSession session = checkAndGetTopicSessionByIndex(indexOfSession);
        if(!session.isClosed())
        {
            throw new VerifyFailedException("TopicSession with index " + indexOfSession + " is not closed.");
        }
    }

    /**
     * Verifies that the topic session with the specified index was
     * committed.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicSessionCommitted(int indexOfSession)
    {
        MockTopicSession session = checkAndGetTopicSessionByIndex(indexOfSession);
        if(!session.isCommitted())
        {
            throw new VerifyFailedException("TopicSession is not committed.");
        }
    }

    /**
     * Verifies that the topic session with the specified index was
     * not committed.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicSessionNotCommitted(int indexOfSession)
    {
        MockTopicSession session = checkAndGetTopicSessionByIndex(indexOfSession);
        if(session.isCommitted())
        {
            throw new VerifyFailedException("TopicSession is committed.");
        }
    }
    
    /**
     * Verifies the number of commits of the topic session with the specified index.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfCommits the expected number of commits
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicSessionNumberCommits(int indexOfSession, int numberOfCommits)
    {
        MockTopicSession session = checkAndGetTopicSessionByIndex(indexOfSession);
        if(numberOfCommits != session.getNumberCommits())
        {
            throw new VerifyFailedException("TopicSession was commited " + session.getNumberCommits() + " times, expected " + numberOfCommits + " times");
        }
    }

    /**
     * Verifies that the topic session with the specified index was
     * rolled back.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicSessionRolledBack(int indexOfSession)
    {
        MockTopicSession session = checkAndGetTopicSessionByIndex(indexOfSession);
        if(!session.isRolledBack())
        {
            throw new VerifyFailedException("TopicSession is not rolled back.");
        }
    }

    /**
     * Verifies that the topic session with the specified index was
     * not rolled back.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicSessionNotRolledBack(int indexOfSession)
    {
        MockTopicSession session = checkAndGetTopicSessionByIndex(indexOfSession);
        if(session.isRolledBack())
        {
            throw new VerifyFailedException("TopicSession is rolled back.");
        }
    }
    
    /**
     * Verifies the number of rollbacks of the topic session with the specified index.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfRollbacks the expected number of rollbacks
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicSessionNumberRollbacks(int indexOfSession, int numberOfRollbacks)
    {
        MockTopicSession session = checkAndGetTopicSessionByIndex(indexOfSession);
        if(numberOfRollbacks != session.getNumberRollbacks())
        {
            throw new VerifyFailedException("TopicSession was rolled back " + session.getNumberRollbacks() + " times, expected " + numberOfRollbacks + " times");
        }
    }

    /**
     * Verifies that the topic session with the specified index was
     * recovered.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicSessionRecovered(int indexOfSession)
    {
        MockTopicSession session = checkAndGetTopicSessionByIndex(indexOfSession);
        if(!session.isRecovered())
        {
            throw new VerifyFailedException("TopicSession is not recovered.");
        }
    }

    /**
     * Verifies that the topic session with the specified index was
     * not recovered.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicSessionNotRecovered(int indexOfSession)
    {
        MockTopicSession session = checkAndGetTopicSessionByIndex(indexOfSession);
        if(session.isRecovered())
        {
            throw new VerifyFailedException("TopicSession is recovered.");
        }
    }
    
    /**
     * Verifies that the session with the specified index is
     * closed.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifySessionClosed(int indexOfSession)
    {
        MockSession session = checkAndGetSessionByIndex(indexOfSession);
        if(!session.isClosed())
        {
            throw new VerifyFailedException("Session with index " + indexOfSession + " is not closed.");
        }
    }

    /**
     * Verifies that the session with the specified index was
     * committed.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifySessionCommitted(int indexOfSession)
    {
        MockSession session = checkAndGetSessionByIndex(indexOfSession);
        if(!session.isCommitted())
        {
            throw new VerifyFailedException("Session is not committed.");
        }
    }

    /**
     * Verifies that the session with the specified index was
     * not committed.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifySessionNotCommitted(int indexOfSession)
    {
        MockSession session = checkAndGetSessionByIndex(indexOfSession);
        if(session.isCommitted())
        {
            throw new VerifyFailedException("Session is committed.");
        }
    }
    
    /**
     * Verifies the number of commits of session with the specified index.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfCommits the expected number of commits
     * @throws VerifyFailedException if verification fails
     */
    public void verifySessionNumberCommits(int indexOfSession, int numberOfCommits)
    {
        MockSession session = checkAndGetSessionByIndex(indexOfSession);
        if(numberOfCommits != session.getNumberCommits())
        {
            throw new VerifyFailedException("Session was commited " + session.getNumberCommits() + " times, expected " + numberOfCommits + " times");
        }
    }

    /**
     * Verifies that the session with the specified index was
     * rolled back.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifySessionRolledBack(int indexOfSession)
    {
        MockSession session = checkAndGetSessionByIndex(indexOfSession);
        if(!session.isRolledBack())
        {
            throw new VerifyFailedException("Session is not rolled back.");
        }
    }

    /**
     * Verifies that the session with the specified index was
     * not rolled back.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifySessionNotRolledBack(int indexOfSession)
    {
        MockSession session = checkAndGetSessionByIndex(indexOfSession);
        if(session.isRolledBack())
        {
            throw new VerifyFailedException("Session is rolled back.");
        }
    }
    
    /**
     * Verifies the number of rollbacks of session with the specified index.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfRollbacks the expected number of rollbacks
     * @throws VerifyFailedException if verification fails
     */
    public void verifySessionNumberRollbacks(int indexOfSession, int numberOfRollbacks)
    {
        MockSession session = checkAndGetSessionByIndex(indexOfSession);
        if(numberOfRollbacks != session.getNumberRollbacks())
        {
            throw new VerifyFailedException("Session was rolled back " + session.getNumberRollbacks() + " times, expected " + numberOfRollbacks + " times");
        }
    }

    /**
     * Verifies that the session with the specified index was
     * recovered.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifySessionRecovered(int indexOfSession)
    {
        MockSession session = checkAndGetSessionByIndex(indexOfSession);
        if(!session.isRecovered())
        {
            throw new VerifyFailedException("Session is not recovered.");
        }
    }

    /**
     * Verifies that the session with the specified index was
     * not recovered.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifySessionNotRecovered(int indexOfSession)
    {
        MockSession session = checkAndGetSessionByIndex(indexOfSession);
        if(session.isRecovered())
        {
            throw new VerifyFailedException("Session is recovered.");
        }
    }
    
    /**
     * Verifies that all queue sessions are closed.
     * The sessions have to be created using the current {@link MockQueueConnection}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllQueueSessionsClosed()
    {
        List queueSessions = getQueueSessionList();
        if(null == queueSessions) return;
        for(int ii = 0; ii < queueSessions.size(); ii++)
        {
            MockQueueSession currentSession = (MockQueueSession)queueSessions.get(ii);
            if(!currentSession.isClosed())
            {
                throw new VerifyFailedException("QueueSession with index " + ii + " is not closed.");
            }
        }
    }
    
    /**
     * Verifies that all queue sessions are recovered.
     * The sessions have to be created using the current {@link MockQueueConnection}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllQueueSessionsRecovered()
    {
        List queueSessions = getQueueSessionList();
        if(null == queueSessions) return;
        for(int ii = 0; ii < queueSessions.size(); ii++)
        {
            MockQueueSession currentSession = (MockQueueSession)queueSessions.get(ii);
            if(!currentSession.isRecovered())
            {
                throw new VerifyFailedException("QueueSession with index " + ii + " is not recovered.");
            }
        }
    }

    /**
     * Verifies that all queue sessions were commited.
     * The sessions have to be created using the current {@link MockQueueConnection}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllQueueSessionsCommitted()
    {
        List queueSessions = getQueueSessionList();
        if(null == queueSessions) return;
        for(int ii = 0; ii < queueSessions.size(); ii++)
        {
            MockQueueSession currentSession = (MockQueueSession)queueSessions.get(ii);
            if(!currentSession.isCommitted())
            {
                throw new VerifyFailedException("QueueSession with index " + ii + " is not committed.");
            }
        }
    }

    /**
     * Verifies that all queue sessions were rolled back.
     * The sessions have to be created using the current {@link MockQueueConnection}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllQueueSessionsRolledBack()
    {
        List queueSessions = getQueueSessionList();
        if(null == queueSessions) return;
        for(int ii = 0; ii < queueSessions.size(); ii++)
        {
            MockQueueSession currentSession = (MockQueueSession)queueSessions.get(ii);
            if(!currentSession.isRolledBack())
            {
                throw new VerifyFailedException("QueueSession with index " + ii + " is not rolled back.");
            }
        }   
    }
    
    /**
     * Verifies that all topic sessions are closed.
     * The sessions have to be created using the current {@link MockTopicConnection}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTopicSessionsClosed()
    {
        List topicSessions = getTopicSessionList();
        if(null == topicSessions) return;
        for(int ii = 0; ii < topicSessions.size(); ii++)
        {
            MockTopicSession currentSession = (MockTopicSession)topicSessions.get(ii);
            if(!currentSession.isClosed())
            {
                throw new VerifyFailedException("TopicSession with index " + ii + " is not closed.");
            }
        }
    }

    /**
     * Verifies that all topic sessions are recovered.
     * The sessions have to be created using the current {@link MockTopicConnection}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTopicSessionsRecovered()
    {
        List topicSessions = getTopicSessionList();
        if(null == topicSessions) return;
        for(int ii = 0; ii < topicSessions.size(); ii++)
        {
            MockTopicSession currentSession = (MockTopicSession)topicSessions.get(ii);
            if(!currentSession.isRecovered())
            {
                throw new VerifyFailedException("TopicSession with index " + ii + " is not recovered.");
            }
        }
    }

    /**
     * Verifies that all topic sessions were commited.
     * The sessions have to be created using the current {@link MockTopicConnection}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTopicSessionsCommitted()
    {
        List topicSessions = getTopicSessionList();
        if(null == topicSessions) return;
        for(int ii = 0; ii < topicSessions.size(); ii++)
        {
            MockTopicSession currentSession = (MockTopicSession)topicSessions.get(ii);
            if(!currentSession.isCommitted())
            {
                throw new VerifyFailedException("TopicSession with index " + ii + " is not committed.");
            }
        }
    }

    /**
     * Verifies that all topic sessions were rolled back.
     * The sessions have to be created using the current {@link MockTopicConnection}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTopicSessionsRolledBack()
    {
        List topicSessions = getTopicSessionList();
        if(null == topicSessions) return;
        for(int ii = 0; ii < topicSessions.size(); ii++)
        {
            MockTopicSession currentSession = (MockTopicSession)topicSessions.get(ii);
            if(!currentSession.isRolledBack())
            {
                throw new VerifyFailedException("TopicSession with index " + ii + " is not rolled back.");
            }
        }   
    }
    
    /**
     * Verifies that all sessions are closed.
     * The sessions have to be created using the current {@link MockConnection}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllSessionsClosed()
    {
        List sessions = getSessionList();
        if(null == sessions) return;
        for(int ii = 0; ii < sessions.size(); ii++)
        {
            MockSession currentSession = (MockSession)sessions.get(ii);
            if(!currentSession.isClosed())
            {
                throw new VerifyFailedException("Session with index " + ii + " is not closed.");
            }
        }
    }

    /**
     * Verifies that all sessions are recovered.
     * The sessions have to be created using the current {@link MockConnection}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllSessionsRecovered()
    {
        List sessions = getSessionList();
        if(null == sessions) return;
        for(int ii = 0; ii < sessions.size(); ii++)
        {
            MockSession currentSession = (MockSession)sessions.get(ii);
            if(!currentSession.isRecovered())
            {
                throw new VerifyFailedException("Session with index " + ii + " is not recovered.");
            }
        }
    }

    /**
     * Verifies that all sessions were commited.
     * The sessions have to be created using the current {@link MockConnection}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllSessionsCommitted()
    {
        List sessions = getSessionList();
        if(null == sessions) return;
        for(int ii = 0; ii < sessions.size(); ii++)
        {
            MockSession currentSession = (MockSession)sessions.get(ii);
            if(!currentSession.isCommitted())
            {
                throw new VerifyFailedException("Session with index " + ii + " is not committed.");
            }
        }
    }

    /**
     * Verifies that all topic sessions were rolled back.
     * The sessions have to be created using the current {@link MockConnection}.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllSessionsRolledBack()
    {
        List sessions = getSessionList();
        if(null == sessions) return;
        for(int ii = 0; ii < sessions.size(); ii++)
        {
            MockSession currentSession = (MockSession)sessions.get(ii);
            if(!currentSession.isRolledBack())
            {
                throw new VerifyFailedException("Session with index " + ii + " is not rolled back.");
            }
        }   
    }
    
    /**
     * Verifies the number of producers for the specified session.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfProducers the expected number of producers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberMessageProducers(int indexOfSession, int numberOfProducers)
    {
        checkAndGetSessionByIndex(indexOfSession);
        TransmissionManagerWrapper manager = getTransmissionManagerWrapper(indexOfSession);
        if(numberOfProducers != manager.getMessageProducerList().size())
        {
            throw new VerifyFailedException("Expected " + numberOfProducers + " producers, actually " + manager.getMessageProducerList().size() + " producers present");
        }
    }
    
    /**
     * Verifies that all producers for the specified session are closed.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllMessageProducersClosed(int indexOfSession)
    {
        checkAndGetSessionByIndex(indexOfSession);
        TransmissionManagerWrapper manager = getTransmissionManagerWrapper(indexOfSession);
        List producers = manager.getMessageProducerList();
        for(int ii = 0; ii < producers.size(); ii++)
        {
            MockMessageProducer currentProducer = (MockMessageProducer)producers.get(ii);
            if(!currentProducer.isClosed())
            {
                throw new VerifyFailedException("MessageProducer with index " + ii + " not closed.");
            }
        }
    }
    
    /**
     * Verifies the number of senders for the specified session.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfSenders the expected number of senders
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueSenders(int indexOfSession, int numberOfSenders)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        TransmissionManagerWrapper manager = getQueueTransmissionManagerWrapper(indexOfSession);
        if(numberOfSenders != manager.getQueueSenderList().size())
        {
            throw new VerifyFailedException("Expected " + numberOfSenders + " senders, actually " + manager.getQueueSenderList().size() + " senders present");
        }
    }
    
    /**
     * Verifies the number of senders for the specified session and
     * the specified queue name.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param queueName the name of the queue
     * @param numberOfSenders the expected number of senders
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueSenders(int indexOfSession, String queueName, int numberOfSenders)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        checkQueueByName(queueName);
        QueueTransmissionManager manager = getQueueTransmissionManager(indexOfSession);
        if(numberOfSenders != manager.getQueueSenderList(queueName).size())
        {
            throw new VerifyFailedException("Expected " + numberOfSenders + " senders for queue " + queueName + ", actually " + manager.getQueueSenderList(queueName).size() + " senders present");
        }
    }

    /**
     * Verifies that the specified sender is closed.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param queueName the name of the queue
     * @param indexOfSender the index of the sender
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSenderClosed(int indexOfSession, String queueName, int indexOfSender)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        checkQueueByName(queueName);
        QueueTransmissionManager manager = getQueueTransmissionManager(indexOfSession);
        List senders = manager.getQueueSenderList(queueName);
        if(indexOfSender >= senders.size())
        {
            throw new VerifyFailedException("QueueSender with index " + indexOfSender + " is not present.");
        }
        MockQueueSender sender = (MockQueueSender)senders.get(indexOfSender);
        if(!sender.isClosed())
        {
            throw new VerifyFailedException("QueueSender of queue " + queueName + " with index " + indexOfSender + " not closed.");
        }
    }
    
    /**
     * Verifies that all senders for the specified session are closed.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllQueueSendersClosed(int indexOfSession)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        TransmissionManagerWrapper manager = getQueueTransmissionManagerWrapper(indexOfSession);
        List senders = manager.getQueueSenderList();
        for(int ii = 0; ii < senders.size(); ii++)
        {
            MockQueueSender currentSender = (MockQueueSender)senders.get(ii);
            if(!currentSender.isClosed())
            {
                throw new VerifyFailedException("QueueSender with index " + ii + " not closed.");
            }
        }
    }
    
    /**
     * Verifies the number of publishers for the specified session.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfPublishers the expected number of publishers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberTopicPublishers(int indexOfSession, int numberOfPublishers)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        TransmissionManagerWrapper manager = getTopicTransmissionManagerWrapper(indexOfSession);
        if(numberOfPublishers != manager.getTopicPublisherList().size())
        {
            throw new VerifyFailedException("Expected " + numberOfPublishers + " publishers, actually " + manager.getTopicPublisherList().size() + " publishers present");
        }
    }

    /**
     * Verifies the number of publishers for the specified session and
     * the specified topic name.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param topicName the name of the topic
     * @param numberOfPublishers the expected number of publishers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberTopicPublishers(int indexOfSession, String topicName, int numberOfPublishers)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        checkTopicByName(topicName);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
        if(numberOfPublishers != manager.getTopicPublisherList(topicName).size())
        {
            throw new VerifyFailedException("Expected " + numberOfPublishers + " publishers for topic " + topicName + ", actually " + manager.getTopicPublisherList(topicName).size() + " publishers present");
        }
    } 

    /**
     * Verifies that the specified publisher is closed.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param topicName the name of the topic
     * @param indexOfPublisher the index of the publisher
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicPublisherClosed(int indexOfSession, String topicName, int indexOfPublisher)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        checkTopicByName(topicName);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
        List publishers = manager.getTopicPublisherList(topicName);
        if(indexOfPublisher >= publishers.size())
        {
            throw new VerifyFailedException("TopicPublisher with index " + indexOfPublisher + " is not present.");
        }
        MockTopicPublisher publisher = (MockTopicPublisher)publishers.get(indexOfPublisher);
        if(!publisher.isClosed())
        {
            throw new VerifyFailedException("TopicPublisher of topic " + topicName + " with index " + indexOfPublisher + " not closed.");
        }
    }

    /**
     * Verifies that all publishers for the specified session are closed.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTopicPublishersClosed(int indexOfSession)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        TransmissionManagerWrapper manager = getTopicTransmissionManagerWrapper(indexOfSession);
        List publishers = manager.getTopicPublisherList();
        for(int ii = 0; ii < publishers.size(); ii++)
        {
            MockTopicPublisher currentPublisher = (MockTopicPublisher)publishers.get(ii);
            if(!currentPublisher.isClosed())
            {
                throw new VerifyFailedException("TopicPublisher with index " + ii + " not closed.");
            }
        }
    }
    
    /**
     * Verifies the number of consumers for the specified session.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfConsumers the expected number of consumers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberMessageConsumers(int indexOfSession, int numberOfConsumers)
    {
        checkAndGetSessionByIndex(indexOfSession);
        TransmissionManagerWrapper manager = getTransmissionManagerWrapper(indexOfSession);
        if(numberOfConsumers != manager.getMessageConsumerList().size())
        {
            throw new VerifyFailedException("Expected " + numberOfConsumers + " consumers, actually " + manager.getMessageConsumerList().size() + " consumers present");
        }
    }
    
    /**
     * Verifies that all consumers for the specified session are closed.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllMessageConsumersClosed(int indexOfSession)
    {
        checkAndGetSessionByIndex(indexOfSession);
        TransmissionManagerWrapper manager = getTransmissionManagerWrapper(indexOfSession);
        List consumers = manager.getMessageConsumerList();
        for(int ii = 0; ii < consumers.size(); ii++)
        {
            MockMessageConsumer currentConsumer = (MockMessageConsumer)consumers.get(ii);
            if(!currentConsumer.isClosed())
            {
                throw new VerifyFailedException("MessageConsumer with index " + ii + " not closed.");
            }
        }
    }
    
    /**
     * Verifies the number of receivers for the specified session.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfReceivers the expected number of receivers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueReceivers(int indexOfSession, int numberOfReceivers)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        QueueTransmissionManager manager = getQueueTransmissionManager(indexOfSession);
        if(numberOfReceivers != manager.getQueueReceiverList().size())
        {
            throw new VerifyFailedException("Expected " + numberOfReceivers + " receivers, actually " + manager.getQueueReceiverList().size() + " receivers present");
        }
    }
    
    /**
     * Verifies the number of receivers for the specified session and
     * the specified queue name.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param queueName the name of the queue
     * @param numberOfReceivers the expected number of receivers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueReceivers(int indexOfSession, String queueName, int numberOfReceivers)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        checkQueueByName(queueName);
        QueueTransmissionManager manager = getQueueTransmissionManager(indexOfSession);
        if(numberOfReceivers != manager.getQueueReceiverList(queueName).size())
        {
            throw new VerifyFailedException("Expected " + numberOfReceivers + " receivers for queue " + queueName + ", actually " + manager.getQueueReceiverList(queueName).size() + " receivers present");
        }
    }
    
    /**
     * Verifies that the specified receiver is closed.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param queueName the name of the queue
     * @param indexOfReceiver the index of the receiver
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueReceiverClosed(int indexOfSession, String queueName, int indexOfReceiver)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        checkQueueByName(queueName);
        QueueTransmissionManager manager = getQueueTransmissionManager(indexOfSession);
        List receivers = manager.getQueueReceiverList(queueName);
        if(indexOfReceiver >= receivers.size())
        {
            throw new VerifyFailedException("QueueReceiver with index " + indexOfReceiver + " is not present.");
        }
        MockQueueReceiver receiver = (MockQueueReceiver)receivers.get(indexOfReceiver);
        if(!receiver.isClosed())
        {
            throw new VerifyFailedException("QueueReceiver of queue " + queueName + " with index " + indexOfReceiver + " not closed.");
        }
    }

    /**
     * Verifies that all receivers for the specified session are closed.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllQueueReceiversClosed(int indexOfSession)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        QueueTransmissionManager manager = getQueueTransmissionManager(indexOfSession);
        List receivers = manager.getQueueReceiverList();
        for(int ii = 0; ii < receivers.size(); ii++)
        {
            MockQueueReceiver currentReceiver = (MockQueueReceiver)receivers.get(ii);
            if(!currentReceiver.isClosed())
            {
                throw new VerifyFailedException("QueueReceiver with index " + ii + " not closed.");
            }
        }
    }
    
    /**
     * Verifies the number of subscribers for the specified session.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfSubscribers the expected number of subscribers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberTopicSubscribers(int indexOfSession, int numberOfSubscribers)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
        if(numberOfSubscribers != manager.getTopicSubscriberList().size())
        {
            throw new VerifyFailedException("Expected " + numberOfSubscribers + " subscribers, actually " + manager.getTopicSubscriberList().size() + " subscribers present");
        }
    }

    /**
     * Verifies the number of subscribers for the specified session and
     * the specified topic name.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param topicName the name of the topic
     * @param numberOfSubscribers the expected number of subscribers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberTopicSubscribers(int indexOfSession, String topicName, int numberOfSubscribers)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        checkTopicByName(topicName);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
        if(numberOfSubscribers != manager.getTopicSubscriberList(topicName).size())
        {
            throw new VerifyFailedException("Expected " + numberOfSubscribers + " subscribers for topic " + topicName + ", actually " + manager.getTopicSubscriberList(topicName).size() + " subscribers present");
        }
    }

    /**
     * Verifies that the specified subscriber is closed.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param topicName the name of the topic
     * @param indexOfSubscriber the index of the receiver
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicSubscriberClosed(int indexOfSession, String topicName, int indexOfSubscriber)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        checkTopicByName(topicName);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
        List subscribers = manager.getTopicSubscriberList(topicName);
        if(indexOfSubscriber >= subscribers.size())
        {
            throw new VerifyFailedException("TopicSubscriber with index " + indexOfSubscriber + " is not present.");
        }
        MockTopicSubscriber subscriber = (MockTopicSubscriber)subscribers.get(indexOfSubscriber);
        if(!subscriber.isClosed())
        {
            throw new VerifyFailedException("TopicSubscriber of topic " + topicName + " with index " + indexOfSubscriber + " not closed.");
        }
    }

    /**
     * Verifies that all subscribers for the specified session are closed.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTopicSubscribersClosed(int indexOfSession)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
        List subscribers = manager.getTopicSubscriberList();
        for(int ii = 0; ii < subscribers.size(); ii++)
        {
            MockTopicSubscriber currentSubscriber = (MockTopicSubscriber)subscribers.get(ii);
            if(!currentSubscriber.isClosed())
            {
                throw new VerifyFailedException("TopicSubscriber with index " + ii + " not closed.");
            }
        }
    }
    
    /**
     * Verifies the number of browsers for the specified session.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfBrowsers the expected number of browsers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueBrowsers(int indexOfSession, int numberOfBrowsers)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        QueueTransmissionManager manager = getQueueTransmissionManager(indexOfSession);
        if(numberOfBrowsers != manager.getQueueBrowserList().size())
        {
            throw new VerifyFailedException("Expected " + numberOfBrowsers + " browsers, actually " + manager.getQueueBrowserList().size() + " browsers present");
        }
    }
    
    /**
     * Verifies the number of browsers for the specified session and
     * the specified queue name.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param queueName the name of the queue
     * @param numberOfBrowsers the expected number of browsers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueBrowsers(int indexOfSession, String queueName, int numberOfBrowsers)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        checkQueueByName(queueName);
        QueueTransmissionManager manager = getQueueTransmissionManager(indexOfSession);
        if(numberOfBrowsers != manager.getQueueBrowserList(queueName).size())
        {
            throw new VerifyFailedException("Expected " + numberOfBrowsers + " browsers for queue " + queueName + ", actually " + manager.getQueueBrowserList(queueName).size() + " browsers present");
        }
    }
    
    /**
     * Verifies that the specified browser is closed.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param queueName the name of the queue
     * @param indexOfBrowser the index of the browser
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueBrowserClosed(int indexOfSession, String queueName, int indexOfBrowser)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        checkQueueByName(queueName);
        QueueTransmissionManager manager = getQueueTransmissionManager(indexOfSession);
        List browsers = manager.getQueueBrowserList(queueName);
        if(indexOfBrowser >= browsers.size())
        {
            throw new VerifyFailedException("QueueBrowser with index " + indexOfBrowser + " is not present.");
        }
        MockQueueBrowser browser = (MockQueueBrowser)browsers.get(indexOfBrowser);
        if(!browser.isClosed())
        {
            throw new VerifyFailedException("QueueBrowser of queue " + queueName + " with index " + indexOfBrowser + " not closed.");
        }
    }

    /**
     * Verifies that all browsers for the specified session are closed.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllQueueBrowsersClosed(int indexOfSession)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        QueueTransmissionManager manager = getQueueTransmissionManager(indexOfSession);
        List browsers = manager.getQueueBrowserList();
        for(int ii = 0; ii < browsers.size(); ii++)
        {
            MockQueueBrowser currentBrowser = (MockQueueBrowser)browsers.get(ii);
            if(!currentBrowser.isClosed())
            {
                throw new VerifyFailedException("QueueBrowser with index " + ii + " not closed.");
            }
        }
    }
    
    /**
     * Verifies that a durable subscriber exists.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param nameOfSubscriber the name of the durable subscriber
     * @throws VerifyFailedException if verification fails
     */
    public void verifyDurableTopicSubscriberPresent(int indexOfSession, String nameOfSubscriber)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
        if(null == manager.getDurableTopicSubscriber(nameOfSubscriber))
        {
            throw new VerifyFailedException("Durable subscriber with name " + nameOfSubscriber + " not present.");
        }
    }
    
    /**
     * Verifies the number of durable subscribers for the specified session.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param numberOfSubscribers the expected number of durable subscribers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberDurableTopicSubscribers(int indexOfSession, int numberOfSubscribers)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
        if(numberOfSubscribers != manager.getDurableTopicSubscriberMap().size())
        {
            throw new VerifyFailedException("Expected " + numberOfSubscribers + " durable subscribers, actually " + manager.getDurableTopicSubscriberMap().size() + " durable subscribers present");
        }
    }
    
    /**
     * Verifies the number of durable subscribers for the specified session and
     * the specified topic name.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param topicName the name of the topic
     * @param numberOfSubscribers the expected number of durable subscribers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberDurableTopicSubscribers(int indexOfSession, String topicName, int numberOfSubscribers)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        checkTopicByName(topicName);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
        if(numberOfSubscribers != manager.getDurableTopicSubscriberMap(topicName).size())
        {
            throw new VerifyFailedException("Expected " + numberOfSubscribers + " durable subscribers for topic " + topicName + ", actually " + manager.getDurableTopicSubscriberMap(topicName).size() + " durable subscribers present");
        }
    }
    
    /**
     * Verifies that the specified durable subscriber is closed.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param subscriberName the name of the durable subscriber
     * @throws VerifyFailedException if verification fails
     */
    public void verifyDurableTopicSubscriberClosed(int indexOfSession, String subscriberName)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
        MockTopicSubscriber subscriber = (MockTopicSubscriber)manager.getDurableTopicSubscriber(subscriberName);
        if(null == subscriber)
        {
            throw new VerifyFailedException("Durable TopicSubscriber with name " + subscriberName + " not present.");
        }
        if(!subscriber.isClosed())
        {
            throw new VerifyFailedException("Durable TopicSubscriber with name " + subscriberName + " not closed.");
        }
    }
    
    /**
     * Verifies that all durable subscribers for the specified session are closed.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllDurableTopicSubscribersClosed(int indexOfSession)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
        Iterator keys = manager.getDurableTopicSubscriberMap().keySet().iterator();
        while(keys.hasNext())
        {
            MockTopicSubscriber currentSubscriber = (MockTopicSubscriber)manager.getDurableTopicSubscriberMap().get(keys.next());
            if(!currentSubscriber.isClosed())
            {
                throw new VerifyFailedException("Durable TopicSubscriber with name " + currentSubscriber.getName() + " not closed.");
            }
        }
    }

    /**
     * Verifies the number of queue sessions.
     * The sessions have to be created using the current {@link MockQueueConnection}.
     * @param number the expected number of queue sessions
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueSessions(int number)
    {
        if(number != getQueueSessionList().size())
        {
            throw new VerifyFailedException("Expected " + number + " queue sessions, actually " + getQueueSessionList().size() + " sessions present");
        }
    }
    
    /**
     * Verifies the number of topic sessions.
     * The sessions have to be created using the current {@link MockTopicConnection}.
     * @param number the expected number of topic sessions
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberTopicSessions(int number)
    {
        if(number != getTopicSessionList().size())
        {
            throw new VerifyFailedException("Expected " + number + " topic sessions, actually " + getTopicSessionList().size() + " sessions present");
        }
    }
    
    /**
     * Verifies the number of sessions.
     * The sessions have to be created using the current {@link MockConnection}.
     * @param number the expected number of sessions
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberSessions(int number)
    {
        if(number != getSessionList().size())
        {
            throw new VerifyFailedException("Expected " + number + " sessions, actually " + getSessionList().size() + " sessions present");
        }
    }
    
    /**
     * Verifies the number of temporary queues.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param numberQueues the expected number of temporary queues
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberTemporaryQueues(int indexOfSession, int numberQueues)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(numberQueues != getTemporaryQueueList(indexOfSession).size())
        {
            throw new VerifyFailedException("Expected " + numberQueues + " temporary queues, actually " + getTemporaryQueueList(indexOfSession).size() + " temporary queues present");
        }
    }
    
    /**
     * Verifies the number of temporary topics.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param numberTopics the expected number of temporary topics
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberTemporaryTopics(int indexOfSession, int numberTopics)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(numberTopics != getTemporaryTopicList(indexOfSession).size())
        {
            throw new VerifyFailedException("Expected " + numberTopics + " temporary topics, actually " + getTemporaryTopicList(indexOfSession).size() + " temporary topics present");
        }
    }
    
    /**
     * Verifies that the temporary queue with the specified index
     * was deleted.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the queue
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTemporaryQueueDeleted(int indexOfSession, int indexOfQueue)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        MockTemporaryQueue queue = getTemporaryQueue(indexOfSession, indexOfQueue);
        if(null == queue)
        {
            throw new VerifyFailedException("TemporaryQueue with index " + indexOfQueue + " is not present.");
        }
        if(!queue.isDeleted())
        {
            throw new VerifyFailedException("TemporaryQueue with index " + indexOfQueue + " not deleted.");
        }
    }
    
    /**
     * Verifies that all temporary queues were deleted.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTemporaryQueuesDeleted(int indexOfSession)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List queueList = getTemporaryQueueList(indexOfSession);
        for(int ii = 0; ii < queueList.size(); ii++)
        {
            MockTemporaryQueue currentQueue = (MockTemporaryQueue)queueList.get(ii);
            if(!currentQueue.isDeleted())
            {
                throw new VerifyFailedException("TemporaryQueue with index " + ii + " not deleted.");
            }
        }
    }
    
    /**
     * Verifies that the temporary topic with the specified index
     * was closed.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the topic
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTemporaryTopicDeleted(int indexOfSession, int indexOfTopic)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        MockTemporaryTopic topic = getTemporaryTopic(indexOfSession, indexOfTopic);
        if(null == topic)
        {
            throw new VerifyFailedException("TemporaryTopic with index " + indexOfTopic + " is not present.");
        }
        if(!topic.isDeleted())
        {
            throw new VerifyFailedException("TemporaryTopic with index " + indexOfTopic + " not deleted.");
        }
    }

    /**
     * Verifies that all temporary topics were deleted.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTemporaryTopicsDeleted(int indexOfSession)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List topicList = getTemporaryTopicList(indexOfSession);
        for(int ii = 0; ii < topicList.size(); ii++)
        {
            MockTemporaryTopic currentTopic = (MockTemporaryTopic)topicList.get(ii);
            if(!currentTopic.isDeleted())
            {
                throw new VerifyFailedException("TemporaryTopic with index " + ii + " not deleted.");
            }
        }
    }
    
    /**
     * Verifies that the specified messages are equal by calling the
     * <code>equals()</code> method. All mock messages provide a
     * suitable implementation of <code>equals()</code>.
     * @param message1 the first message
     * @param message2 the second message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyMessageEquals(MockMessage message1, MockMessage message2)
    {
        if(null == message1)
        {
            throw new VerifyFailedException("message1 is null");
        }
        if(null == message2)
        {
            throw new VerifyFailedException("message2 is null");
        }
        if(!message1.equals(message2))
        {
            throw new VerifyFailedException("messages not equal: message1: " + message1.toString() + ", message2: " + message2.toString());
        }
    }
    
    /**
     * Verifies that a message in the specified queue is equal to
     * the specified message by calling the <code>equals()</code> method. 
     * All mock messages provide a suitable implementation of <code>equals()</code>.
     * @param nameOfQueue the name of the queue
     * @param indexOfSourceMessage the index of the message in the queue
     * @param targetMessage the message that will be used for comparison
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCurrentQueueMessageEquals(String nameOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        checkQueueByName(nameOfQueue);
        List messageList = getCurrentMessageListFromQueue(nameOfQueue);
        if(indexOfSourceMessage >= messageList.size())
        {
            throw new VerifyFailedException("Queue " + nameOfQueue + " contains only " + messageList.size() + " messages");
        }
        MockMessage sourceMessage = (MockMessage)messageList.get(indexOfSourceMessage);
        verifyMessageEquals(sourceMessage, targetMessage);
    }
    
    /**
     * Verifies that a received message is equal to the specified message 
     * by calling the <code>equals()</code> method. 
     * All mock messages provide a suitable implementation of <code>equals()</code>.
     * @param nameOfQueue the name of the queue
     * @param indexOfSourceMessage the index of the received message
     * @param targetMessage the message that will be used for comparison
     * @throws VerifyFailedException if verification fails
     */
    public void verifyReceivedQueueMessageEquals(String nameOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        checkQueueByName(nameOfQueue);
        List messageList = getReceivedMessageListFromQueue(nameOfQueue);
        if(indexOfSourceMessage >= messageList.size())
        {
            throw new VerifyFailedException("Queue " + nameOfQueue + " received only " + messageList.size() + " messages");
        }
        MockMessage sourceMessage = (MockMessage)messageList.get(indexOfSourceMessage);
        verifyMessageEquals(sourceMessage, targetMessage);
    }
    
    /**
     * Verifies that a message in the specified temporary queue is equal to
     * the specified message by calling the <code>equals()</code> method. 
     * All mock messages provide a suitable implementation of <code>equals()</code>.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @param indexOfSourceMessage the index of the message in the queue
     * @param targetMessage the message that will be used for comparison
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCurrentQueueMessageEquals(int indexOfSession, int indexOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getCurrentMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
        if(null == messageList)
        {
            throw new VerifyFailedException("Temporary queue with index " + indexOfQueue + " of session with index " + indexOfSession +  " does not exist");
        }
        if(indexOfSourceMessage >= messageList.size())
        {
            throw new VerifyFailedException("Temporary queue with index " + indexOfQueue + " contains only " + messageList.size() + " messages");
        }
        MockMessage sourceMessage = (MockMessage)messageList.get(indexOfSourceMessage);
        verifyMessageEquals(sourceMessage, targetMessage);
    }

    /**
     * Verifies that a message received by a temporary queue is equal to the specified message 
     * by calling the <code>equals()</code> method. 
     * All mock messages provide a suitable implementation of <code>equals()</code>.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @param indexOfSourceMessage the index of the received message
     * @param targetMessage the message that will be used for comparison
     * @throws VerifyFailedException if verification fails
     */
    public void verifyReceivedQueueMessageEquals(int indexOfSession, int indexOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getReceivedMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
        if(null == messageList)
        {
            throw new VerifyFailedException("Temporary queue with index " + indexOfQueue + " of session with index " + indexOfSession +  " does not exist");
        }
        if(indexOfSourceMessage >= messageList.size())
        {
            throw new VerifyFailedException("Temporary queue with index " + indexOfQueue + " received only " + messageList.size() + " messages");
        }
        MockMessage sourceMessage = (MockMessage)messageList.get(indexOfSourceMessage);
        verifyMessageEquals(sourceMessage, targetMessage);
    }
    
    /**
     * Verifies the number of messages in a queue.
     * @param nameOfQueue the name of the queue
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCurrentQueueMessages(String nameOfQueue, int numberOfMessages)
    {
        checkQueueByName(nameOfQueue);
        List list = getCurrentMessageListFromQueue(nameOfQueue);
        if(numberOfMessages != list.size())
        {
            throw new VerifyFailedException("Expected " + numberOfMessages + " messages in queue " + nameOfQueue + ", received " + list.size() + " messages");
        }
    }
    
    /**
     * Verifies the number of messages received by a queue.
     * @param nameOfQueue the name of the queue
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfReceivedQueueMessages(String nameOfQueue, int numberOfMessages)
    {
        checkQueueByName(nameOfQueue);
        List list = getReceivedMessageListFromQueue(nameOfQueue);
        if(numberOfMessages != list.size())
        {
            throw new VerifyFailedException("Expected " + numberOfMessages + " messages received by queue " + nameOfQueue + ", received " + list.size() + " messages");
        }
    }
    
    /**
     * Verifies the number of messages in a temporary queue.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCurrentQueueMessages(int indexOfSession, int indexOfQueue, int numberOfMessages)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List list = getCurrentMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
        if(null == list)
        {
            throw new VerifyFailedException("Temporary queue with index " + indexOfQueue + " of session with index " + indexOfSession +  " does not exist");
        }
        if(numberOfMessages != list.size())
        {
            throw new VerifyFailedException("Expected " + numberOfMessages + " messages, received " + list.size() + " messages");
        }
    }

    /**
     * Verifies the number of messages received by a temporary queue.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfReceivedQueueMessages(int indexOfSession, int indexOfQueue, int numberOfMessages)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List list = getReceivedMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
        if(null == list)
        {
            throw new VerifyFailedException("Temporary queue with index " + indexOfQueue + " of session with index " + indexOfSession +  " does not exist");
        }
        if(numberOfMessages != list.size())
        {
            throw new VerifyFailedException("Expected " + numberOfMessages + " messages, received " + list.size() + " messages");
        }
    }
    
    /**
     * Verifies that all received messages of the specified queue 
     * are acknowledged.
     * @param nameOfQueue the name of the queue
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllReceivedQueueMessagesAcknowledged(String nameOfQueue)
    {
        checkQueueByName(nameOfQueue);
        List messageList = getReceivedMessageListFromQueue(nameOfQueue);
        for(int ii = 0; ii < messageList.size(); ii++)
        {
            MockMessage currentMessage = (MockMessage)messageList.get(ii);
            if(!currentMessage.isAcknowledged())
            {
                throw new VerifyFailedException("Message " + ii + " of queue " + nameOfQueue + " is not acknowledged");
            }
        }
    }
    
    /**
     * Verifies that all received messages of the specified temporary queue 
     * are acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllReceivedQueueMessagesAcknowledged(int indexOfSession, int indexOfQueue)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getReceivedMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
        if(null == messageList)
        {
            throw new VerifyFailedException("Temporary queue with index " + indexOfQueue + " of session with index " + indexOfSession +  " does not exist");
        }
        for(int ii = 0; ii < messageList.size(); ii++)
        {
            MockMessage currentMessage = (MockMessage)messageList.get(ii);
            if(!currentMessage.isAcknowledged())
            {
                throw new VerifyFailedException("Message " + ii + " of temporary queue " + indexOfQueue + " is not acknowledged");
            }
        }
    }
    
    /**
     * Verifies that a received message is acknowledged.
     * @param nameOfQueue the name of the queue
     * @param indexOfMessage the index of the received message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyReceivedQueueMessageAcknowledged(String nameOfQueue, int indexOfMessage)
    {
        checkQueueByName(nameOfQueue);
        List messageList = getReceivedMessageListFromQueue(nameOfQueue);
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Queue " + nameOfQueue + " received only " + messageList.size() + " messages");
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of queue " + nameOfQueue + " is not acknowledged");
        }
    }
    
    /**
     * Verifies that a received message is not acknowledged.
     * @param nameOfQueue the name of the queue
     * @param indexOfMessage the index of the received message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyReceivedQueueMessageNotAcknowledged(String nameOfQueue, int indexOfMessage)
    {
        checkQueueByName(nameOfQueue);
        List messageList = getReceivedMessageListFromQueue(nameOfQueue);
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Queue " + nameOfQueue + " received only " + messageList.size() + " messages");
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of queue " + nameOfQueue + " is acknowledged");
        }
    }
    
    /**
     * Verifies that message received by a temporary queue is acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @param indexOfMessage the index of the received message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyReceivedQueueMessageAcknowledged(int indexOfSession, int indexOfQueue, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getReceivedMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
        if(null == messageList)
        {
            throw new VerifyFailedException("Temporary queue with index " + indexOfQueue + " of session with index " + indexOfSession +  " does not exist");
        }
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Temporary queue with index " + indexOfQueue + " received only " + messageList.size() + " messages");
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of temporary queue " + indexOfQueue + " is not acknowledged");
        }
    }
    
    /**
     * Verifies that a received by a temporary queue is not acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @param indexOfMessage the index of the received message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyReceivedQueueMessageNotAcknowledged(int indexOfSession, int indexOfQueue, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getReceivedMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
        if(null == messageList)
        {
            throw new VerifyFailedException("Temporary queue with index " + indexOfQueue + " of session with index " + indexOfSession +  " does not exist");
        }
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Temporary queue with index " + indexOfQueue + " received only " + messageList.size() + " messages");
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of temporary queue " + indexOfQueue + " is acknowledged");
        }
    }
    
    /**
     * Verifies the number of messages created with
     * {@link MockQueueSession#createMessage}.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedQueueMessages(int indexOfSession, int number)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(number != getQueueMessageManager(indexOfSession).getMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " messages, received " + getQueueMessageManager(indexOfSession).getMessageList().size() + " messages");
        }
    }
    
    /**
     * Verifies the number of bytes messages created with
     * {@link MockQueueSession#createBytesMessage}.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of bytes messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedQueueBytesMessages(int indexOfSession, int number)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(number != getQueueMessageManager(indexOfSession).getBytesMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " bytes messages, received " + getQueueMessageManager(indexOfSession).getBytesMessageList().size() + " bytes messages");
        }
    }
    
    /**
     * Verifies the number of map messages created with
     * {@link MockQueueSession#createMapMessage}.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of map messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedQueueMapMessages(int indexOfSession, int number)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(number != getQueueMessageManager(indexOfSession).getMapMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " map messages, received " + getQueueMessageManager(indexOfSession).getMapMessageList().size() + " map messages");
        }
    }
    
    /**
     * Verifies the number of text messages created with
     * {@link MockQueueSession#createTextMessage}.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of text messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedQueueTextMessages(int indexOfSession, int number)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(number != getQueueMessageManager(indexOfSession).getTextMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " text messages, received " + getQueueMessageManager(indexOfSession).getTextMessageList().size() + " text messages");
        }
    }
    
    /**
     * Verifies the number of stream messages created with
     * {@link MockQueueSession#createStreamMessage}.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of stream messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedQueueStreamMessages(int indexOfSession, int number)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(number != getQueueMessageManager(indexOfSession).getStreamMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " stream messages, received " + getQueueMessageManager(indexOfSession).getStreamMessageList().size() + " stream messages");
        }
    }
    
    /**
     * Verifies the number of object messages created with
     * {@link MockQueueSession#createObjectMessage}.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of object messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedQueueObjectMessages(int indexOfSession, int number)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(number != getQueueMessageManager(indexOfSession).getObjectMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " object messages, received " + getQueueMessageManager(indexOfSession).getObjectMessageList().size() + " object messages");
        }
    }
    
    /**
     * Verifies that a message created with {@link MockQueueSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedQueueMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getQueueMessageManager(indexOfSession).getMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }
    
    /**
     * Verifies that a message created with {@link MockQueueSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedQueueMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getQueueMessageManager(indexOfSession).getMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        } 
    }
    
    /**
     * Verifies that a bytes message created with {@link MockQueueSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedQueueBytesMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getQueueMessageManager(indexOfSession).getBytesMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " bytes messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }
    
    /**
     * Verifies that a bytes message created with {@link MockQueueSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedQueueBytesMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getQueueMessageManager(indexOfSession).getBytesMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " bytes messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }
    
    /**
     * Verifies that a map message created with {@link MockQueueSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedQueueMapMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getQueueMessageManager(indexOfSession).getMapMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " map messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }
    
    /**
     * Verifies that a map message created with {@link MockQueueSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedQueueMapMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getQueueMessageManager(indexOfSession).getMapMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " map messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }
    
    /**
     * Verifies that a text message created with {@link MockQueueSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedQueueTextMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getQueueMessageManager(indexOfSession).getTextMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " text messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }
    
    /**
     * Verifies that a text message created with {@link MockQueueSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedQueueTextMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getQueueMessageManager(indexOfSession).getTextMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " text messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }
    
    /**
     * Verifies that a stream message created with {@link MockQueueSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedQueueStreamMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getQueueMessageManager(indexOfSession).getStreamMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " stream messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }
    
    /**
     * Verifies that a stream message created with {@link MockQueueSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedQueueStreamMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getQueueMessageManager(indexOfSession).getStreamMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " stream messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }
    
    /**
     * Verifies that a object message created with {@link MockQueueSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedQueueObjectMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getQueueMessageManager(indexOfSession).getObjectMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " object messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }
    
    /**
     * Verifies that a object message created with {@link MockQueueSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockQueueConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedQueueObjectMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        List messageList = getQueueMessageManager(indexOfSession).getObjectMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " object messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }
    
    /**
     * Verifies that a message in the specified topic is equal to
     * the specified message by calling the <code>equals()</code> method. 
     * All mock messages provide a suitable implementation of <code>equals()</code>.
     * @param nameOfTopic the name of the topic
     * @param indexOfSourceMessage the index of the message in the topic
     * @param targetMessage the message that will be used for comparison
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCurrentTopicMessageEquals(String nameOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        checkTopicByName(nameOfTopic);
        List messageList = getCurrentMessageListFromTopic(nameOfTopic);
        if(indexOfSourceMessage >= messageList.size())
        {
            throw new VerifyFailedException("Topic " + nameOfTopic + " contains only " + messageList.size() + " messages");
        }
        MockMessage sourceMessage = (MockMessage)messageList.get(indexOfSourceMessage);
        verifyMessageEquals(sourceMessage, targetMessage);
    }

    /**
     * Verifies that a received message is equal to the specified message 
     * by calling the <code>equals()</code> method. 
     * All mock messages provide a suitable implementation of <code>equals()</code>.
     * @param nameOfTopic the name of the topic
     * @param indexOfSourceMessage the index of the received message
     * @param targetMessage the message that will be used for comparison
     * @throws VerifyFailedException if verification fails
     */
    public void verifyReceivedTopicMessageEquals(String nameOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        checkTopicByName(nameOfTopic);
        List messageList = getReceivedMessageListFromTopic(nameOfTopic);
        if(indexOfSourceMessage >= messageList.size())
        {
            throw new VerifyFailedException("Topic " + nameOfTopic + " received only " + messageList.size() + " messages");
        }
        MockMessage sourceMessage = (MockMessage)messageList.get(indexOfSourceMessage);
        verifyMessageEquals(sourceMessage, targetMessage);
    }

    /**
     * Verifies that a message in the specified temporary topic is equal to
     * the specified message by calling the <code>equals()</code> method. 
     * All mock messages provide a suitable implementation of <code>equals()</code>.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topic
     * @param indexOfSourceMessage the index of the message in the topic
     * @param targetMessage the message that will be used for comparison
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCurrentTopicMessageEquals(int indexOfSession, int indexOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getCurrentMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
        if(null == messageList)
        {
            throw new VerifyFailedException("Temporary topic with index " + indexOfTopic + " of session with index " + indexOfSession +  " does not exist");
        }
        if(indexOfSourceMessage >= messageList.size())
        {
            throw new VerifyFailedException("Temporary topic with index " + indexOfTopic + " contains only " + messageList.size() + " messages");
        }
        MockMessage sourceMessage = (MockMessage)messageList.get(indexOfSourceMessage);
        verifyMessageEquals(sourceMessage, targetMessage);
    }

    /**
     * Verifies that a message received by a temporary topic is equal to the specified message 
     * by calling the <code>equals()</code> method. 
     * All mock messages provide a suitable implementation of <code>equals()</code>.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topic
     * @param indexOfSourceMessage the index of the received message
     * @param targetMessage the message that will be used for comparison
     * @throws VerifyFailedException if verification fails
     */
    public void verifyReceivedTopicMessageEquals(int indexOfSession, int indexOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getReceivedMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
        if(null == messageList)
        {
            throw new VerifyFailedException("Temporary topic with index " + indexOfTopic + " of session with index " + indexOfSession +  " does not exist");
        }
        if(indexOfSourceMessage >= messageList.size())
        {
            throw new VerifyFailedException("Temporary topic with index " + indexOfTopic + " received only " + messageList.size() + " messages");
        }
        MockMessage sourceMessage = (MockMessage)messageList.get(indexOfSourceMessage);
        verifyMessageEquals(sourceMessage, targetMessage);
    }

    /**
     * Verifies the number of messages in a topic.
     * @param nameOfTopic the name of the topic
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCurrentTopicMessages(String nameOfTopic, int numberOfMessages)
    {
        checkTopicByName(nameOfTopic);
        List list = getCurrentMessageListFromTopic(nameOfTopic);
        if(numberOfMessages != list.size())
        {
            throw new VerifyFailedException("Expected " + numberOfMessages + " messages in topic " + nameOfTopic + ", received " + list.size() + " messages");
        }
    }

    /**
     * Verifies the number of messages received by a topic.
     * @param nameOfTopic the name of the topic
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfReceivedTopicMessages(String nameOfTopic, int numberOfMessages)
    {
        checkTopicByName(nameOfTopic);
        List list = getReceivedMessageListFromTopic(nameOfTopic);
        if(numberOfMessages != list.size())
        {
            throw new VerifyFailedException("Expected " + numberOfMessages + " messages received by topic " + nameOfTopic + ", received " + list.size() + " messages");
        }
    }

    /**
     * Verifies the number of messages in a temporary topic.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topic
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCurrentTopicMessages(int indexOfSession, int indexOfTopic, int numberOfMessages)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List list = getCurrentMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
        if(null == list)
        {
            throw new VerifyFailedException("Temporary topic with index " + indexOfTopic + " of session with index " + indexOfSession +  " does not exist");
        }
        if(numberOfMessages != list.size())
        {
            throw new VerifyFailedException("Expected " + numberOfMessages + " messages, received " + list.size() + " messages");
        }
    }

    /**
     * Verifies the number of messages received by a temporary topic.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topic
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfReceivedTopicMessages(int indexOfSession, int indexOfTopic, int numberOfMessages)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List list = getReceivedMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
        if(null == list)
        {
            throw new VerifyFailedException("Temporary topic with index " + indexOfTopic + " of session with index " + indexOfSession +  " does not exist");
        }
        if(numberOfMessages != list.size())
        {
            throw new VerifyFailedException("Expected " + numberOfMessages + " messages, received " + list.size() + " messages");
        }
    }
    
    /**
     * Verifies that all received messages of the specified topic 
     * are acknowledged.
     * @param nameOfTopic the name of the topic
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllReceivedTopicMessagesAcknowledged(String nameOfTopic)
    {
        checkTopicByName(nameOfTopic);
        List messageList = getReceivedMessageListFromTopic(nameOfTopic);
        for(int ii = 0; ii < messageList.size(); ii++)
        {
            MockMessage currentMessage = (MockMessage)messageList.get(ii);
            if(!currentMessage.isAcknowledged())
            {
                throw new VerifyFailedException("Message " + ii + " of topic " + nameOfTopic + " is not acknowledged");
            }
        }
    }
    
    /**
     * Verifies that all received messages of the specified temporary topic 
     * are acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topic
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllReceivedTopicMessagesAcknowledged(int indexOfSession, int indexOfTopic)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getReceivedMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
        if(null == messageList)
        {
            throw new VerifyFailedException("Temporary topic with index " + indexOfTopic + " of session with index " + indexOfSession +  " does not exist");
        }
        for(int ii = 0; ii < messageList.size(); ii++)
        {
            MockMessage currentMessage = (MockMessage)messageList.get(ii);
            if(!currentMessage.isAcknowledged())
            {
                throw new VerifyFailedException("Message " + ii + " of temporary topic " + indexOfTopic + " is not acknowledged");
            }
        }
    }
    
    /**
     * Verifies that a received message is acknowledged.
     * @param nameOfTopic the name of the topic
     * @param indexOfMessage the index of the received message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyReceivedTopicMessageAcknowledged(String nameOfTopic, int indexOfMessage)
    {
        checkTopicByName(nameOfTopic);
        List messageList = getReceivedMessageListFromTopic(nameOfTopic);
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Topic " + nameOfTopic + " received only " + messageList.size() + " messages");
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of topic " + nameOfTopic + " is not acknowledged");
        }
    }

    /**
     * Verifies that a received message is not acknowledged.
     * @param nameOfTopic the name of the topic
     * @param indexOfMessage the index of the received message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyReceivedTopicMessageNotAcknowledged(String nameOfTopic, int indexOfMessage)
    {
        checkTopicByName(nameOfTopic);
        List messageList = getReceivedMessageListFromTopic(nameOfTopic);
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Topic " + nameOfTopic + " received only " + messageList.size() + " messages");
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of topic " + nameOfTopic + " is acknowledged");
        }
    }

    /**
     * Verifies that a message received by a temporary topic is acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topic
     * @param indexOfMessage the index of the received message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyReceivedTopicMessageAcknowledged(int indexOfSession, int indexOfTopic, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getReceivedMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
        if(null == messageList)
        {
            throw new VerifyFailedException("Temporary topic with index " + indexOfTopic + " of session with index " + indexOfSession +  " does not exist");
        }
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Temporary topic with index " + indexOfTopic + " received only " + messageList.size() + " messages");
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of temporary topic " + indexOfTopic + " is not acknowledged");
        }
    }

    /**
     * Verifies that a message received by a temporary topic is not acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topic
     * @param indexOfMessage the index of the received message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyReceivedTopicMessageNotAcknowledged(int indexOfSession, int indexOfTopic, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getReceivedMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
        if(null == messageList)
        {
            throw new VerifyFailedException("Temporary topic with index " + indexOfTopic + " of session with index " + indexOfSession +  " does not exist");
        }
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Temporary topic with index " + indexOfTopic + " received only " + messageList.size() + " messages");
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of temporary topic " + indexOfTopic + " is acknowledged");
        }
    }

    /**
     * Verifies the number of messages created with 
     * {@link MockTopicSession#createMessage}.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedTopicMessages(int indexOfSession, int number)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(number != getTopicMessageManager(indexOfSession).getMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " messages, received " + getTopicMessageManager(indexOfSession).getMessageList().size() + " messages");
        }
    }

    /**
     * Verifies the number of bytes messages created with
     * {@link MockTopicSession#createBytesMessage}.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of bytes messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedTopicBytesMessages(int indexOfSession, int number)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(number != getTopicMessageManager(indexOfSession).getBytesMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " bytes messages, received " + getTopicMessageManager(indexOfSession).getBytesMessageList().size() + " bytes messages");
        }
    }

    /**
     * Verifies the number of map messages created with
     * {@link MockTopicSession#createMapMessage}.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of map messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedTopicMapMessages(int indexOfSession, int number)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(number != getTopicMessageManager(indexOfSession).getMapMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " map messages, received " + getTopicMessageManager(indexOfSession).getMapMessageList().size() + " map messages");
        }
    }

    /**
     * Verifies the number of text messages created with
     * {@link MockTopicSession#createTextMessage}.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of text messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedTopicTextMessages(int indexOfSession, int number)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(number != getTopicMessageManager(indexOfSession).getTextMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " text messages, received " + getTopicMessageManager(indexOfSession).getTextMessageList().size() + " text messages");
        }
    }

    /**
     * Verifies the number of stream messages created with
     * {@link MockTopicSession#createStreamMessage}.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of stream messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedTopicStreamMessages(int indexOfSession, int number)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(number != getTopicMessageManager(indexOfSession).getStreamMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " stream messages, received " + getTopicMessageManager(indexOfSession).getStreamMessageList().size() + " stream messages");
        }
    }

    /**
     * Verifies the number of object messages created with
     * {@link MockTopicSession#createObjectMessage}.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of object messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedTopicObjectMessages(int indexOfSession, int number)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(number != getTopicMessageManager(indexOfSession).getObjectMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " object messages, received " + getTopicMessageManager(indexOfSession).getObjectMessageList().size() + " object messages");
        }
    }
    
    /**
     * Verifies that a message created with {@link MockTopicSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTopicMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getTopicMessageManager(indexOfSession).getMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }

    /**
     * Verifies that a message created with {@link MockTopicSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTopicMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getTopicMessageManager(indexOfSession).getMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }

    /**
     * Verifies that a bytes message created with {@link MockTopicSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTopicBytesMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getTopicMessageManager(indexOfSession).getBytesMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " bytes messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }

    /**
     * Verifies that a bytes message created with {@link MockTopicSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTopicBytesMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getTopicMessageManager(indexOfSession).getBytesMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " bytes messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }

    /**
     * Verifies that a map message created with {@link MockTopicSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTopicMapMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getTopicMessageManager(indexOfSession).getMapMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " map messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }

    /**
     * Verifies that a map message created with {@link MockTopicSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTopicMapMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getTopicMessageManager(indexOfSession).getMapMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " map messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }

    /**
     * Verifies that a text message created with {@link MockTopicSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTopicTextMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getTopicMessageManager(indexOfSession).getTextMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " text messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }

    /**
     * Verifies that a text message created with {@link MockTopicSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTopicTextMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getTopicMessageManager(indexOfSession).getTextMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " text messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }

    /**
     * Verifies that a stream message created with {@link MockTopicSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTopicStreamMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getTopicMessageManager(indexOfSession).getStreamMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " stream messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }

    /**
     * Verifies that a stream message created with {@link MockTopicSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTopicStreamMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getTopicMessageManager(indexOfSession).getStreamMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " stream messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }

    /**
     * Verifies that a object message created with {@link MockTopicSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTopicObjectMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getTopicMessageManager(indexOfSession).getObjectMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " object messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }

    /**
     * Verifies that a object message created with {@link MockTopicSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockTopicConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTopicObjectMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        List messageList = getTopicMessageManager(indexOfSession).getObjectMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " object messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }
    
    /**
     * Verifies the number of messages created with
     * {@link MockSession#createMessage}.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedMessages(int indexOfSession, int number)
    {
        checkAndGetSessionByIndex(indexOfSession);
        if(number != getMessageManager(indexOfSession).getMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " messages, received " + getMessageManager(indexOfSession).getMessageList().size() + " messages");
        }
    }

    /**
     * Verifies the number of bytes messages created with
     * {@link MockSession#createBytesMessage}.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of bytes messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedBytesMessages(int indexOfSession, int number)
    {
        checkAndGetSessionByIndex(indexOfSession);
        if(number != getMessageManager(indexOfSession).getBytesMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " bytes messages, received " + getMessageManager(indexOfSession).getBytesMessageList().size() + " bytes messages");
        }
    }

    /**
     * Verifies the number of map messages created with
     * {@link MockSession#createMapMessage}.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of map messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedMapMessages(int indexOfSession, int number)
    {
        checkAndGetSessionByIndex(indexOfSession);
        if(number != getMessageManager(indexOfSession).getMapMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " map messages, received " + getMessageManager(indexOfSession).getMapMessageList().size() + " map messages");
        }
    }

    /**
     * Verifies the number of text messages created with
     * {@link MockSession#createTextMessage}.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of text messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedTextMessages(int indexOfSession, int number)
    {
        checkAndGetSessionByIndex(indexOfSession);
        if(number != getMessageManager(indexOfSession).getTextMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " text messages, received " + getMessageManager(indexOfSession).getTextMessageList().size() + " text messages");
        }
    }

    /**
     * Verifies the number of stream messages created with
     * {@link MockSession#createStreamMessage}.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of stream messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedStreamMessages(int indexOfSession, int number)
    {
        checkAndGetSessionByIndex(indexOfSession);
        if(number != getMessageManager(indexOfSession).getStreamMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " stream messages, received " + getMessageManager(indexOfSession).getStreamMessageList().size() + " stream messages");
        }
    }

    /**
     * Verifies the number of object messages created with
     * {@link MockSession#createObjectMessage}.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param number the expected number of object messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCreatedObjectMessages(int indexOfSession, int number)
    {
        checkAndGetSessionByIndex(indexOfSession);
        if(number != getMessageManager(indexOfSession).getObjectMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " object messages, received " + getMessageManager(indexOfSession).getObjectMessageList().size() + " object messages");
        }
    }
    
    /**
     * Verifies that a message created with {@link MockSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetSessionByIndex(indexOfSession);
        List messageList = getMessageManager(indexOfSession).getMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }

    /**
     * Verifies that a message created with {@link MockSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetSessionByIndex(indexOfSession);
        List messageList = getMessageManager(indexOfSession).getMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }

    /**
     * Verifies that a bytes message created with {@link MockSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedBytesMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetSessionByIndex(indexOfSession);
        List messageList = getMessageManager(indexOfSession).getBytesMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " bytes messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }

    /**
     * Verifies that a bytes message created with {@link MockSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedBytesMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetSessionByIndex(indexOfSession);
        List messageList = getMessageManager(indexOfSession).getBytesMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " bytes messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }

    /**
     * Verifies that a map message created with {@link MockSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedMapMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetSessionByIndex(indexOfSession);
        List messageList = getMessageManager(indexOfSession).getMapMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " map messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }

    /**
     * Verifies that a map message created with {@link MockSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedMapMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetSessionByIndex(indexOfSession);
        List messageList = getMessageManager(indexOfSession).getMapMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " map messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }

    /**
     * Verifies that a text message created with {@link MockSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTextMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetSessionByIndex(indexOfSession);
        List messageList = getMessageManager(indexOfSession).getTextMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " text messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }

    /**
     * Verifies that a text message created with {@link MockSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedTextMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetSessionByIndex(indexOfSession);
        List messageList = getMessageManager(indexOfSession).getTextMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " text messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }

    /**
     * Verifies that a stream message created with {@link MockSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedStreamMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetSessionByIndex(indexOfSession);
        List messageList = getMessageManager(indexOfSession).getStreamMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " stream messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }

    /**
     * Verifies that a stream message created with {@link MockSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedStreamMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetSessionByIndex(indexOfSession);
        List messageList = getMessageManager(indexOfSession).getStreamMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " stream messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }

    /**
     * Verifies that a object message created with {@link MockSession#createMessage} 
     * is acknowledged.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedObjectMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetSessionByIndex(indexOfSession);
        List messageList = getMessageManager(indexOfSession).getObjectMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " object messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(!message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is not acknowledged");
        }
    }

    /**
     * Verifies that a object message created with {@link MockSession#createMessage} 
     * is not acknowledged.
     * The session has to be created using the current {@link MockConnection}.
     * @param indexOfSession the index of the session
     * @param indexOfMessage the index of the message
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCreatedObjectMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        checkAndGetSessionByIndex(indexOfSession);
        List messageList = getMessageManager(indexOfSession).getObjectMessageList();
        if(indexOfMessage >= messageList.size())
        {
            throw new VerifyFailedException("Only " + messageList.size() + " object messages created for session " + indexOfSession);
        }
        MockMessage message = (MockMessage)messageList.get(indexOfMessage);
        if(message.isAcknowledged())
        {
            throw new VerifyFailedException("Message " + indexOfMessage + " of session " + indexOfSession + " is acknowledged");
        }
    }
    
    private MockQueueSession checkAndGetQueueSessionByIndex(int indexOfSession)
    {
        if(null == getCurrentQueueConnection())
        {
            throw new VerifyFailedException("No QueueConnection present.");
        }
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " does not exist.");
        }
        return session;
    }
    
    private MockTopicSession checkAndGetTopicSessionByIndex(int indexOfSession)
    {
        if(null == getCurrentTopicConnection())
        {
            throw new VerifyFailedException("No TopicConnection present.");
        }
        MockTopicSession session = getTopicSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("TopicSession with index " + indexOfSession + " does not exist.");
        }
        return session;
    }
    
    private MockSession checkAndGetSessionByIndex(int indexOfSession)
    {
        if(null == getCurrentConnection())
        {
            throw new VerifyFailedException("No Connection present.");
        }
        MockSession session = getSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("Session with index " + indexOfSession + " does not exist.");
        }
        return session;
    }
    
    private void checkQueueByName(String queueName)
    {
        DestinationManager destinationManager = getDestinationManager();
        if(null == destinationManager.getQueue(queueName))
        {
            throw new VerifyFailedException("Queue with name " + queueName + " is not present.");
        }
    }
    
    private void checkTopicByName(String topicName)
    {
        DestinationManager destinationManager = getDestinationManager();
        if(null == destinationManager.getTopic(topicName))
        {
            throw new VerifyFailedException("Topic with name " + topicName + " is not present.");
        }
    }
}
