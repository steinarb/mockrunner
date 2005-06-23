package com.mockrunner.jms;

import java.util.List;

import javax.jms.MessageListener;

import junit.framework.TestCase;

import com.mockrunner.mock.jms.JMSMockObjectFactory;
import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockMessage;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueSession;
import com.mockrunner.mock.jms.MockSession;
import com.mockrunner.mock.jms.MockTemporaryQueue;
import com.mockrunner.mock.jms.MockTemporaryTopic;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicConnection;
import com.mockrunner.mock.jms.MockTopicSession;

/**
 * Delegator for {@link com.mockrunner.jms.JMSTestModule}. You can
 * subclass this adapter or use {@link com.mockrunner.jms.JMSTestModule}
 * directly (so your test case can use another base class).
 * This basic adapter can be used if you don't need any other modules. It
 * does not extend {@link com.mockrunner.base.BaseTestCase}. If you want
 * to use several modules in conjunction, consider subclassing
 * {@link com.mockrunner.jms.JMSTestCaseAdapter}.
 * <b>This class is generated from the {@link com.mockrunner.jms.JMSTestModule}
 * and should not be edited directly</b>.
 */
public class BasicJMSTestCaseAdapter extends TestCase
{
    private JMSTestModule jmsTestModule;
    private JMSMockObjectFactory jmsMockObjectFactory;

    public BasicJMSTestCaseAdapter()
    {

    }

    public BasicJMSTestCaseAdapter(String name)
    {
        super(name);
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        jmsTestModule = null;
        jmsMockObjectFactory = null;
    }

    /**
     * Creates the {@link com.mockrunner.jms.JMSTestModule}. If you
     * overwrite this method, you must call <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        jmsTestModule = createJMSTestModule(getJMSMockObjectFactory());
    }

    /**
     * Creates a {@link com.mockrunner.mock.jms.JMSMockObjectFactory}.
     * @return the created {@link com.mockrunner.mock.jms.JMSMockObjectFactory}
     */
    protected JMSMockObjectFactory createJMSMockObjectFactory()
    {
        return new JMSMockObjectFactory();
    }

    /**
     * Gets the {@link com.mockrunner.mock.jms.JMSMockObjectFactory}.
     * @return the {@link com.mockrunner.mock.jms.JMSMockObjectFactory}
     */
    protected JMSMockObjectFactory getJMSMockObjectFactory()
    {
        synchronized(JMSMockObjectFactory.class)
        {
            if(jmsMockObjectFactory == null)
            {
                jmsMockObjectFactory = createJMSMockObjectFactory();
            }
        }
        return jmsMockObjectFactory;
    }

    /**
     * Sets the {@link com.mockrunner.mock.jms.JMSMockObjectFactory}.
     * @param jmsMockObjectFactory the {@link com.mockrunner.mock.jms.JMSMockObjectFactory}
     */
    protected void setJMSMockObjectFactory(JMSMockObjectFactory jmsMockObjectFactory)
    {
        this.jmsMockObjectFactory = jmsMockObjectFactory;
    }

    /**
     * Creates a {@link com.mockrunner.jms.JMSTestModule} based on the current
     * {@link com.mockrunner.mock.jms.JMSMockObjectFactory}.
     * Same as <code>createJMSTestModule(getJMSMockObjectFactory())</code>.
     * @return the created {@link com.mockrunner.jms.JMSTestModule}
     */
    protected JMSTestModule createJMSTestModule()
    {
        return new JMSTestModule(getJMSMockObjectFactory());
    }

    /**
     * Creates a {@link com.mockrunner.jms.JMSTestModule} with the specified
     * {@link com.mockrunner.mock.jms.JMSMockObjectFactory}.
     * @return the created {@link com.mockrunner.jms.JMSTestModule}
     */
    protected JMSTestModule createJMSTestModule(JMSMockObjectFactory mockFactory)
    {
        return new JMSTestModule(mockFactory);
    }

    /**
     * Gets the {@link com.mockrunner.jms.JMSTestModule}.
     * @return the {@link com.mockrunner.jms.JMSTestModule}
     */
    protected JMSTestModule getJMSTestModule()
    {
        return jmsTestModule;
    }

    /**
     * Sets the {@link com.mockrunner.jms.JMSTestModule}.
     * @param jmsTestModule the {@link com.mockrunner.jms.JMSTestModule}
     */
    protected void setJMSTestModule(JMSTestModule jmsTestModule)
    {
        this.jmsTestModule = jmsTestModule;
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getSession(int)}
     */
    protected MockSession getSession(int indexOfSession)
    {
        return jmsTestModule.getSession(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyConnectionClosed}
     */
    protected void verifyConnectionClosed()
    {
        jmsTestModule.verifyConnectionClosed();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#setCurrentQueueConnectionIndex(int)}
     */
    protected void setCurrentQueueConnectionIndex(int connectionIndex)
    {
        jmsTestModule.setCurrentQueueConnectionIndex(connectionIndex);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getCurrentQueueConnection}
     */
    protected MockQueueConnection getCurrentQueueConnection()
    {
        return jmsTestModule.getCurrentQueueConnection();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#setCurrentTopicConnectionIndex(int)}
     */
    protected void setCurrentTopicConnectionIndex(int connectionIndex)
    {
        jmsTestModule.setCurrentTopicConnectionIndex(connectionIndex);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getCurrentTopicConnection}
     */
    protected MockTopicConnection getCurrentTopicConnection()
    {
        return jmsTestModule.getCurrentTopicConnection();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#setCurrentConnectionIndex(int)}
     */
    protected void setCurrentConnectionIndex(int connectionIndex)
    {
        jmsTestModule.setCurrentConnectionIndex(connectionIndex);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getCurrentConnection}
     */
    protected MockConnection getCurrentConnection()
    {
        return jmsTestModule.getCurrentConnection();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#registerTestMessageListenerForQueue(MockConnection, String, MessageListener)}
     */
    protected void registerTestMessageListenerForQueue(MockConnection connection, String queueName, MessageListener listener)
    {
        jmsTestModule.registerTestMessageListenerForQueue(connection, queueName, listener);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#registerTestMessageListenerForQueue(MockConnection, String, boolean, int, String, MessageListener)}
     */
    protected void registerTestMessageListenerForQueue(MockConnection connection, String queueName, boolean transacted, int acknowledgeMode, String messageSelector, MessageListener listener)
    {
        jmsTestModule.registerTestMessageListenerForQueue(connection, queueName, transacted, acknowledgeMode, messageSelector, listener);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#registerTestMessageListenerForQueue(String, MessageListener)}
     */
    protected void registerTestMessageListenerForQueue(String queueName, MessageListener listener)
    {
        jmsTestModule.registerTestMessageListenerForQueue(queueName, listener);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#registerTestMessageListenerForQueue(MockConnection, String, boolean, int, MessageListener)}
     */
    protected void registerTestMessageListenerForQueue(MockConnection connection, String queueName, boolean transacted, int acknowledgeMode, MessageListener listener)
    {
        jmsTestModule.registerTestMessageListenerForQueue(connection, queueName, transacted, acknowledgeMode, listener);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getDestinationManager}
     */
    protected DestinationManager getDestinationManager()
    {
        return jmsTestModule.getDestinationManager();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#registerTestMessageListenerForTopic(MockConnection, String, boolean, int, String, MessageListener)}
     */
    protected void registerTestMessageListenerForTopic(MockConnection connection, String topicName, boolean transacted, int acknowledgeMode, String messageSelector, MessageListener listener)
    {
        jmsTestModule.registerTestMessageListenerForTopic(connection, topicName, transacted, acknowledgeMode, messageSelector, listener);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#registerTestMessageListenerForTopic(MockConnection, String, boolean, int, MessageListener)}
     */
    protected void registerTestMessageListenerForTopic(MockConnection connection, String topicName, boolean transacted, int acknowledgeMode, MessageListener listener)
    {
        jmsTestModule.registerTestMessageListenerForTopic(connection, topicName, transacted, acknowledgeMode, listener);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#registerTestMessageListenerForTopic(String, MessageListener)}
     */
    protected void registerTestMessageListenerForTopic(String topicName, MessageListener listener)
    {
        jmsTestModule.registerTestMessageListenerForTopic(topicName, listener);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#registerTestMessageListenerForTopic(MockConnection, String, MessageListener)}
     */
    protected void registerTestMessageListenerForTopic(MockConnection connection, String topicName, MessageListener listener)
    {
        jmsTestModule.registerTestMessageListenerForTopic(connection, topicName, listener);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getTopic(String)}
     */
    protected MockTopic getTopic(String name)
    {
        return jmsTestModule.getTopic(name);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getConfigurationManager}
     */
    protected ConfigurationManager getConfigurationManager()
    {
        return jmsTestModule.getConfigurationManager();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getQueueMessageManager(int)}
     */
    protected MessageManager getQueueMessageManager(int indexOfSession)
    {
        return jmsTestModule.getQueueMessageManager(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getQueueSession(int)}
     */
    protected MockQueueSession getQueueSession(int indexOfSession)
    {
        return jmsTestModule.getQueueSession(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getMessageManager(int)}
     */
    protected MessageManager getMessageManager(int indexOfSession)
    {
        return jmsTestModule.getMessageManager(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getTopicMessageManager(int)}
     */
    protected MessageManager getTopicMessageManager(int indexOfSession)
    {
        return jmsTestModule.getTopicMessageManager(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getTopicSession(int)}
     */
    protected MockTopicSession getTopicSession(int indexOfSession)
    {
        return jmsTestModule.getTopicSession(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getQueueTransmissionManager(int)}
     */
    protected QueueTransmissionManager getQueueTransmissionManager(int indexOfSession)
    {
        return jmsTestModule.getQueueTransmissionManager(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getTopicTransmissionManager(int)}
     */
    protected TopicTransmissionManager getTopicTransmissionManager(int indexOfSession)
    {
        return jmsTestModule.getTopicTransmissionManager(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getTransmissionManager(int)}
     * @deprecated
     */
    protected TransmissionManagerWrapper getTransmissionManager(int indexOfSession)
    {
        return jmsTestModule.getTransmissionManager(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getTransmissionManagerWrapper(int)}
     */
    protected TransmissionManagerWrapper getTransmissionManagerWrapper(int indexOfSession)
    {
        return jmsTestModule.getTransmissionManagerWrapper(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getQueueTransmissionManagerWrapper(int)}
     */
    protected TransmissionManagerWrapper getQueueTransmissionManagerWrapper(int indexOfSession)
    {
        return jmsTestModule.getQueueTransmissionManagerWrapper(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getTopicTransmissionManagerWrapper(int)}
     */
    protected TransmissionManagerWrapper getTopicTransmissionManagerWrapper(int indexOfSession)
    {
        return jmsTestModule.getTopicTransmissionManagerWrapper(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getQueueSessionList}
     */
    protected List getQueueSessionList()
    {
        return jmsTestModule.getQueueSessionList();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getTopicSessionList}
     */
    protected List getTopicSessionList()
    {
        return jmsTestModule.getTopicSessionList();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getSessionList}
     */
    protected List getSessionList()
    {
        return jmsTestModule.getSessionList();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getTemporaryQueueList(int)}
     */
    protected List getTemporaryQueueList(int indexOfSession)
    {
        return jmsTestModule.getTemporaryQueueList(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getTemporaryTopicList(int)}
     */
    protected List getTemporaryTopicList(int indexOfSession)
    {
        return jmsTestModule.getTemporaryTopicList(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getTemporaryQueue(int, int)}
     */
    protected MockTemporaryQueue getTemporaryQueue(int indexOfSession, int indexOfQueue)
    {
        return jmsTestModule.getTemporaryQueue(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getTemporaryTopic(int, int)}
     */
    protected MockTemporaryTopic getTemporaryTopic(int indexOfSession, int indexOfTopic)
    {
        return jmsTestModule.getTemporaryTopic(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getCurrentMessageListFromQueue(String)}
     */
    protected List getCurrentMessageListFromQueue(String name)
    {
        return jmsTestModule.getCurrentMessageListFromQueue(name);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getCurrentMessageListFromTemporaryQueue(int, int)}
     */
    protected List getCurrentMessageListFromTemporaryQueue(int indexOfSession, int indexOfQueue)
    {
        return jmsTestModule.getCurrentMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getReceivedMessageListFromQueue(String)}
     */
    protected List getReceivedMessageListFromQueue(String name)
    {
        return jmsTestModule.getReceivedMessageListFromQueue(name);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getReceivedMessageListFromTemporaryQueue(int, int)}
     */
    protected List getReceivedMessageListFromTemporaryQueue(int indexOfSession, int indexOfQueue)
    {
        return jmsTestModule.getReceivedMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getCurrentMessageListFromTopic(String)}
     */
    protected List getCurrentMessageListFromTopic(String name)
    {
        return jmsTestModule.getCurrentMessageListFromTopic(name);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getCurrentMessageListFromTemporaryTopic(int, int)}
     */
    protected List getCurrentMessageListFromTemporaryTopic(int indexOfSession, int indexOfTopic)
    {
        return jmsTestModule.getCurrentMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getReceivedMessageListFromTopic(String)}
     */
    protected List getReceivedMessageListFromTopic(String name)
    {
        return jmsTestModule.getReceivedMessageListFromTopic(name);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getReceivedMessageListFromTemporaryTopic(int, int)}
     */
    protected List getReceivedMessageListFromTemporaryTopic(int indexOfSession, int indexOfTopic)
    {
        return jmsTestModule.getReceivedMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueConnectionClosed}
     */
    protected void verifyQueueConnectionClosed()
    {
        jmsTestModule.verifyQueueConnectionClosed();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueConnectionStarted}
     */
    protected void verifyQueueConnectionStarted()
    {
        jmsTestModule.verifyQueueConnectionStarted();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueConnectionStopped}
     */
    protected void verifyQueueConnectionStopped()
    {
        jmsTestModule.verifyQueueConnectionStopped();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicConnectionClosed}
     */
    protected void verifyTopicConnectionClosed()
    {
        jmsTestModule.verifyTopicConnectionClosed();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicConnectionStarted}
     */
    protected void verifyTopicConnectionStarted()
    {
        jmsTestModule.verifyTopicConnectionStarted();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicConnectionStopped}
     */
    protected void verifyTopicConnectionStopped()
    {
        jmsTestModule.verifyTopicConnectionStopped();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyConnectionStarted}
     */
    protected void verifyConnectionStarted()
    {
        jmsTestModule.verifyConnectionStarted();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyConnectionStopped}
     */
    protected void verifyConnectionStopped()
    {
        jmsTestModule.verifyConnectionStopped();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueSessionClosed(int)}
     */
    protected void verifyQueueSessionClosed(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionClosed(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueSessionCommitted(int)}
     */
    protected void verifyQueueSessionCommitted(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueSessionNotCommitted(int)}
     */
    protected void verifyQueueSessionNotCommitted(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionNotCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueSessionNumberCommits(int, int)}
     */
    protected void verifyQueueSessionNumberCommits(int indexOfSession, int numberOfCommits)
    {
        jmsTestModule.verifyQueueSessionNumberCommits(indexOfSession, numberOfCommits);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueSessionRolledBack(int)}
     */
    protected void verifyQueueSessionRolledBack(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueSessionNotRolledBack(int)}
     */
    protected void verifyQueueSessionNotRolledBack(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionNotRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueSessionNumberRollbacks(int, int)}
     */
    protected void verifyQueueSessionNumberRollbacks(int indexOfSession, int numberOfRollbacks)
    {
        jmsTestModule.verifyQueueSessionNumberRollbacks(indexOfSession, numberOfRollbacks);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueSessionRecovered(int)}
     */
    protected void verifyQueueSessionRecovered(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueSessionNotRecovered(int)}
     */
    protected void verifyQueueSessionNotRecovered(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionNotRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicSessionClosed(int)}
     */
    protected void verifyTopicSessionClosed(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionClosed(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicSessionCommitted(int)}
     */
    protected void verifyTopicSessionCommitted(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicSessionNotCommitted(int)}
     */
    protected void verifyTopicSessionNotCommitted(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionNotCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicSessionNumberCommits(int, int)}
     */
    protected void verifyTopicSessionNumberCommits(int indexOfSession, int numberOfCommits)
    {
        jmsTestModule.verifyTopicSessionNumberCommits(indexOfSession, numberOfCommits);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicSessionRolledBack(int)}
     */
    protected void verifyTopicSessionRolledBack(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicSessionNotRolledBack(int)}
     */
    protected void verifyTopicSessionNotRolledBack(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionNotRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicSessionNumberRollbacks(int, int)}
     */
    protected void verifyTopicSessionNumberRollbacks(int indexOfSession, int numberOfRollbacks)
    {
        jmsTestModule.verifyTopicSessionNumberRollbacks(indexOfSession, numberOfRollbacks);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicSessionRecovered(int)}
     */
    protected void verifyTopicSessionRecovered(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicSessionNotRecovered(int)}
     */
    protected void verifyTopicSessionNotRecovered(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionNotRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifySessionClosed(int)}
     */
    protected void verifySessionClosed(int indexOfSession)
    {
        jmsTestModule.verifySessionClosed(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifySessionCommitted(int)}
     */
    protected void verifySessionCommitted(int indexOfSession)
    {
        jmsTestModule.verifySessionCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifySessionNotCommitted(int)}
     */
    protected void verifySessionNotCommitted(int indexOfSession)
    {
        jmsTestModule.verifySessionNotCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifySessionNumberCommits(int, int)}
     */
    protected void verifySessionNumberCommits(int indexOfSession, int numberOfCommits)
    {
        jmsTestModule.verifySessionNumberCommits(indexOfSession, numberOfCommits);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifySessionRolledBack(int)}
     */
    protected void verifySessionRolledBack(int indexOfSession)
    {
        jmsTestModule.verifySessionRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifySessionNotRolledBack(int)}
     */
    protected void verifySessionNotRolledBack(int indexOfSession)
    {
        jmsTestModule.verifySessionNotRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifySessionNumberRollbacks(int, int)}
     */
    protected void verifySessionNumberRollbacks(int indexOfSession, int numberOfRollbacks)
    {
        jmsTestModule.verifySessionNumberRollbacks(indexOfSession, numberOfRollbacks);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifySessionRecovered(int)}
     */
    protected void verifySessionRecovered(int indexOfSession)
    {
        jmsTestModule.verifySessionRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifySessionNotRecovered(int)}
     */
    protected void verifySessionNotRecovered(int indexOfSession)
    {
        jmsTestModule.verifySessionNotRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllQueueSessionsClosed}
     */
    protected void verifyAllQueueSessionsClosed()
    {
        jmsTestModule.verifyAllQueueSessionsClosed();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllQueueSessionsRecovered}
     */
    protected void verifyAllQueueSessionsRecovered()
    {
        jmsTestModule.verifyAllQueueSessionsRecovered();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllQueueSessionsCommitted}
     */
    protected void verifyAllQueueSessionsCommitted()
    {
        jmsTestModule.verifyAllQueueSessionsCommitted();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllQueueSessionsRolledBack}
     */
    protected void verifyAllQueueSessionsRolledBack()
    {
        jmsTestModule.verifyAllQueueSessionsRolledBack();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllTopicSessionsClosed}
     */
    protected void verifyAllTopicSessionsClosed()
    {
        jmsTestModule.verifyAllTopicSessionsClosed();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllTopicSessionsRecovered}
     */
    protected void verifyAllTopicSessionsRecovered()
    {
        jmsTestModule.verifyAllTopicSessionsRecovered();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllTopicSessionsCommitted}
     */
    protected void verifyAllTopicSessionsCommitted()
    {
        jmsTestModule.verifyAllTopicSessionsCommitted();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllTopicSessionsRolledBack}
     */
    protected void verifyAllTopicSessionsRolledBack()
    {
        jmsTestModule.verifyAllTopicSessionsRolledBack();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllSessionsClosed}
     */
    protected void verifyAllSessionsClosed()
    {
        jmsTestModule.verifyAllSessionsClosed();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllSessionsRecovered}
     */
    protected void verifyAllSessionsRecovered()
    {
        jmsTestModule.verifyAllSessionsRecovered();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllSessionsCommitted}
     */
    protected void verifyAllSessionsCommitted()
    {
        jmsTestModule.verifyAllSessionsCommitted();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllSessionsRolledBack}
     */
    protected void verifyAllSessionsRolledBack()
    {
        jmsTestModule.verifyAllSessionsRolledBack();
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberMessageProducers(int, int)}
     */
    protected void verifyNumberMessageProducers(int indexOfSession, int numberOfProducers)
    {
        jmsTestModule.verifyNumberMessageProducers(indexOfSession, numberOfProducers);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllMessageProducersClosed(int)}
     */
    protected void verifyAllMessageProducersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllMessageProducersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberQueueSenders(int, int)}
     */
    protected void verifyNumberQueueSenders(int indexOfSession, int numberOfSenders)
    {
        jmsTestModule.verifyNumberQueueSenders(indexOfSession, numberOfSenders);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberQueueSenders(int, String, int)}
     */
    protected void verifyNumberQueueSenders(int indexOfSession, String queueName, int numberOfSenders)
    {
        jmsTestModule.verifyNumberQueueSenders(indexOfSession, queueName, numberOfSenders);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueSenderClosed(int, String, int)}
     */
    protected void verifyQueueSenderClosed(int indexOfSession, String queueName, int indexOfSender)
    {
        jmsTestModule.verifyQueueSenderClosed(indexOfSession, queueName, indexOfSender);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllQueueSendersClosed(int)}
     */
    protected void verifyAllQueueSendersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllQueueSendersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberTopicPublishers(int, String, int)}
     */
    protected void verifyNumberTopicPublishers(int indexOfSession, String topicName, int numberOfPublishers)
    {
        jmsTestModule.verifyNumberTopicPublishers(indexOfSession, topicName, numberOfPublishers);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberTopicPublishers(int, int)}
     */
    protected void verifyNumberTopicPublishers(int indexOfSession, int numberOfPublishers)
    {
        jmsTestModule.verifyNumberTopicPublishers(indexOfSession, numberOfPublishers);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicPublisherClosed(int, String, int)}
     */
    protected void verifyTopicPublisherClosed(int indexOfSession, String topicName, int indexOfPublisher)
    {
        jmsTestModule.verifyTopicPublisherClosed(indexOfSession, topicName, indexOfPublisher);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllTopicPublishersClosed(int)}
     */
    protected void verifyAllTopicPublishersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllTopicPublishersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberMessageConsumers(int, int)}
     */
    protected void verifyNumberMessageConsumers(int indexOfSession, int numberOfConsumers)
    {
        jmsTestModule.verifyNumberMessageConsumers(indexOfSession, numberOfConsumers);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllMessageConsumersClosed(int)}
     */
    protected void verifyAllMessageConsumersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllMessageConsumersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberQueueReceivers(int, String, int)}
     */
    protected void verifyNumberQueueReceivers(int indexOfSession, String queueName, int numberOfReceivers)
    {
        jmsTestModule.verifyNumberQueueReceivers(indexOfSession, queueName, numberOfReceivers);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberQueueReceivers(int, int)}
     */
    protected void verifyNumberQueueReceivers(int indexOfSession, int numberOfReceivers)
    {
        jmsTestModule.verifyNumberQueueReceivers(indexOfSession, numberOfReceivers);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueReceiverClosed(int, String, int)}
     */
    protected void verifyQueueReceiverClosed(int indexOfSession, String queueName, int indexOfReceiver)
    {
        jmsTestModule.verifyQueueReceiverClosed(indexOfSession, queueName, indexOfReceiver);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllQueueReceiversClosed(int)}
     */
    protected void verifyAllQueueReceiversClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllQueueReceiversClosed(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberTopicSubscribers(int, String, int)}
     */
    protected void verifyNumberTopicSubscribers(int indexOfSession, String topicName, int numberOfSubscribers)
    {
        jmsTestModule.verifyNumberTopicSubscribers(indexOfSession, topicName, numberOfSubscribers);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberTopicSubscribers(int, int)}
     */
    protected void verifyNumberTopicSubscribers(int indexOfSession, int numberOfSubscribers)
    {
        jmsTestModule.verifyNumberTopicSubscribers(indexOfSession, numberOfSubscribers);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTopicSubscriberClosed(int, String, int)}
     */
    protected void verifyTopicSubscriberClosed(int indexOfSession, String topicName, int indexOfSubscriber)
    {
        jmsTestModule.verifyTopicSubscriberClosed(indexOfSession, topicName, indexOfSubscriber);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllTopicSubscribersClosed(int)}
     */
    protected void verifyAllTopicSubscribersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllTopicSubscribersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberQueueBrowsers(int, String, int)}
     */
    protected void verifyNumberQueueBrowsers(int indexOfSession, String queueName, int numberOfBrowsers)
    {
        jmsTestModule.verifyNumberQueueBrowsers(indexOfSession, queueName, numberOfBrowsers);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberQueueBrowsers(int, int)}
     */
    protected void verifyNumberQueueBrowsers(int indexOfSession, int numberOfBrowsers)
    {
        jmsTestModule.verifyNumberQueueBrowsers(indexOfSession, numberOfBrowsers);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyQueueBrowserClosed(int, String, int)}
     */
    protected void verifyQueueBrowserClosed(int indexOfSession, String queueName, int indexOfBrowser)
    {
        jmsTestModule.verifyQueueBrowserClosed(indexOfSession, queueName, indexOfBrowser);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllQueueBrowsersClosed(int)}
     */
    protected void verifyAllQueueBrowsersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllQueueBrowsersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyDurableTopicSubscriberPresent(int, String)}
     */
    protected void verifyDurableTopicSubscriberPresent(int indexOfSession, String nameOfSubscriber)
    {
        jmsTestModule.verifyDurableTopicSubscriberPresent(indexOfSession, nameOfSubscriber);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberDurableTopicSubscribers(int, String, int)}
     */
    protected void verifyNumberDurableTopicSubscribers(int indexOfSession, String topicName, int numberOfSubscribers)
    {
        jmsTestModule.verifyNumberDurableTopicSubscribers(indexOfSession, topicName, numberOfSubscribers);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberDurableTopicSubscribers(int, int)}
     */
    protected void verifyNumberDurableTopicSubscribers(int indexOfSession, int numberOfSubscribers)
    {
        jmsTestModule.verifyNumberDurableTopicSubscribers(indexOfSession, numberOfSubscribers);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyDurableTopicSubscriberClosed(int, String)}
     */
    protected void verifyDurableTopicSubscriberClosed(int indexOfSession, String subscriberName)
    {
        jmsTestModule.verifyDurableTopicSubscriberClosed(indexOfSession, subscriberName);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllDurableTopicSubscribersClosed(int)}
     */
    protected void verifyAllDurableTopicSubscribersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllDurableTopicSubscribersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberQueueSessions(int)}
     */
    protected void verifyNumberQueueSessions(int number)
    {
        jmsTestModule.verifyNumberQueueSessions(number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberTopicSessions(int)}
     */
    protected void verifyNumberTopicSessions(int number)
    {
        jmsTestModule.verifyNumberTopicSessions(number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberSessions(int)}
     */
    protected void verifyNumberSessions(int number)
    {
        jmsTestModule.verifyNumberSessions(number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberTemporaryQueues(int, int)}
     */
    protected void verifyNumberTemporaryQueues(int indexOfSession, int numberQueues)
    {
        jmsTestModule.verifyNumberTemporaryQueues(indexOfSession, numberQueues);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberTemporaryTopics(int, int)}
     */
    protected void verifyNumberTemporaryTopics(int indexOfSession, int numberTopics)
    {
        jmsTestModule.verifyNumberTemporaryTopics(indexOfSession, numberTopics);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTemporaryQueueDeleted(int, int)}
     */
    protected void verifyTemporaryQueueDeleted(int indexOfSession, int indexOfQueue)
    {
        jmsTestModule.verifyTemporaryQueueDeleted(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllTemporaryQueuesDeleted(int)}
     */
    protected void verifyAllTemporaryQueuesDeleted(int indexOfSession)
    {
        jmsTestModule.verifyAllTemporaryQueuesDeleted(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyTemporaryTopicDeleted(int, int)}
     */
    protected void verifyTemporaryTopicDeleted(int indexOfSession, int indexOfTopic)
    {
        jmsTestModule.verifyTemporaryTopicDeleted(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllTemporaryTopicsDeleted(int)}
     */
    protected void verifyAllTemporaryTopicsDeleted(int indexOfSession)
    {
        jmsTestModule.verifyAllTemporaryTopicsDeleted(indexOfSession);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyMessageEquals(MockMessage, MockMessage)}
     */
    protected void verifyMessageEquals(MockMessage message1, MockMessage message2)
    {
        jmsTestModule.verifyMessageEquals(message1, message2);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCurrentQueueMessageEquals(int, int, int, MockMessage)}
     */
    protected void verifyCurrentQueueMessageEquals(int indexOfSession, int indexOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyCurrentQueueMessageEquals(indexOfSession, indexOfQueue, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCurrentQueueMessageEquals(String, int, MockMessage)}
     */
    protected void verifyCurrentQueueMessageEquals(String nameOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyCurrentQueueMessageEquals(nameOfQueue, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyReceivedQueueMessageEquals(String, int, MockMessage)}
     */
    protected void verifyReceivedQueueMessageEquals(String nameOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageEquals(nameOfQueue, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyReceivedQueueMessageEquals(int, int, int, MockMessage)}
     */
    protected void verifyReceivedQueueMessageEquals(int indexOfSession, int indexOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageEquals(indexOfSession, indexOfQueue, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCurrentQueueMessages(String, int)}
     */
    protected void verifyNumberOfCurrentQueueMessages(String nameOfQueue, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfCurrentQueueMessages(nameOfQueue, numberOfMessages);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCurrentQueueMessages(int, int, int)}
     */
    protected void verifyNumberOfCurrentQueueMessages(int indexOfSession, int indexOfQueue, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfCurrentQueueMessages(indexOfSession, indexOfQueue, numberOfMessages);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfReceivedQueueMessages(int, int, int)}
     */
    protected void verifyNumberOfReceivedQueueMessages(int indexOfSession, int indexOfQueue, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfReceivedQueueMessages(indexOfSession, indexOfQueue, numberOfMessages);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfReceivedQueueMessages(String, int)}
     */
    protected void verifyNumberOfReceivedQueueMessages(String nameOfQueue, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfReceivedQueueMessages(nameOfQueue, numberOfMessages);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllReceivedQueueMessagesAcknowledged(String)}
     */
    protected void verifyAllReceivedQueueMessagesAcknowledged(String nameOfQueue)
    {
        jmsTestModule.verifyAllReceivedQueueMessagesAcknowledged(nameOfQueue);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllReceivedQueueMessagesAcknowledged(int, int)}
     */
    protected void verifyAllReceivedQueueMessagesAcknowledged(int indexOfSession, int indexOfQueue)
    {
        jmsTestModule.verifyAllReceivedQueueMessagesAcknowledged(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyReceivedQueueMessageAcknowledged(int, int, int)}
     */
    protected void verifyReceivedQueueMessageAcknowledged(int indexOfSession, int indexOfQueue, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageAcknowledged(indexOfSession, indexOfQueue, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyReceivedQueueMessageAcknowledged(String, int)}
     */
    protected void verifyReceivedQueueMessageAcknowledged(String nameOfQueue, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageAcknowledged(nameOfQueue, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyReceivedQueueMessageNotAcknowledged(int, int, int)}
     */
    protected void verifyReceivedQueueMessageNotAcknowledged(int indexOfSession, int indexOfQueue, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageNotAcknowledged(indexOfSession, indexOfQueue, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyReceivedQueueMessageNotAcknowledged(String, int)}
     */
    protected void verifyReceivedQueueMessageNotAcknowledged(String nameOfQueue, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageNotAcknowledged(nameOfQueue, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedQueueMessages(int, int)}
     */
    protected void verifyNumberOfCreatedQueueMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedQueueBytesMessages(int, int)}
     */
    protected void verifyNumberOfCreatedQueueBytesMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueBytesMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedQueueMapMessages(int, int)}
     */
    protected void verifyNumberOfCreatedQueueMapMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueMapMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedQueueTextMessages(int, int)}
     */
    protected void verifyNumberOfCreatedQueueTextMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueTextMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedQueueStreamMessages(int, int)}
     */
    protected void verifyNumberOfCreatedQueueStreamMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueStreamMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedQueueObjectMessages(int, int)}
     */
    protected void verifyNumberOfCreatedQueueObjectMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueObjectMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedQueueMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedQueueMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedQueueMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedQueueMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedQueueBytesMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedQueueBytesMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueBytesMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedQueueBytesMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedQueueBytesMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueBytesMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedQueueMapMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedQueueMapMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueMapMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedQueueMapMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedQueueMapMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueMapMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedQueueTextMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedQueueTextMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueTextMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedQueueTextMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedQueueTextMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueTextMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedQueueStreamMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedQueueStreamMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueStreamMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedQueueStreamMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedQueueStreamMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueStreamMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedQueueObjectMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedQueueObjectMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueObjectMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedQueueObjectMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedQueueObjectMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueObjectMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCurrentTopicMessageEquals(String, int, MockMessage)}
     */
    protected void verifyCurrentTopicMessageEquals(String nameOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyCurrentTopicMessageEquals(nameOfTopic, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCurrentTopicMessageEquals(int, int, int, MockMessage)}
     */
    protected void verifyCurrentTopicMessageEquals(int indexOfSession, int indexOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyCurrentTopicMessageEquals(indexOfSession, indexOfTopic, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyReceivedTopicMessageEquals(String, int, MockMessage)}
     */
    protected void verifyReceivedTopicMessageEquals(String nameOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageEquals(nameOfTopic, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyReceivedTopicMessageEquals(int, int, int, MockMessage)}
     */
    protected void verifyReceivedTopicMessageEquals(int indexOfSession, int indexOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageEquals(indexOfSession, indexOfTopic, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCurrentTopicMessages(int, int, int)}
     */
    protected void verifyNumberOfCurrentTopicMessages(int indexOfSession, int indexOfTopic, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfCurrentTopicMessages(indexOfSession, indexOfTopic, numberOfMessages);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCurrentTopicMessages(String, int)}
     */
    protected void verifyNumberOfCurrentTopicMessages(String nameOfTopic, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfCurrentTopicMessages(nameOfTopic, numberOfMessages);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfReceivedTopicMessages(int, int, int)}
     */
    protected void verifyNumberOfReceivedTopicMessages(int indexOfSession, int indexOfTopic, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfReceivedTopicMessages(indexOfSession, indexOfTopic, numberOfMessages);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfReceivedTopicMessages(String, int)}
     */
    protected void verifyNumberOfReceivedTopicMessages(String nameOfTopic, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfReceivedTopicMessages(nameOfTopic, numberOfMessages);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllReceivedTopicMessagesAcknowledged(String)}
     */
    protected void verifyAllReceivedTopicMessagesAcknowledged(String nameOfTopic)
    {
        jmsTestModule.verifyAllReceivedTopicMessagesAcknowledged(nameOfTopic);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyAllReceivedTopicMessagesAcknowledged(int, int)}
     */
    protected void verifyAllReceivedTopicMessagesAcknowledged(int indexOfSession, int indexOfTopic)
    {
        jmsTestModule.verifyAllReceivedTopicMessagesAcknowledged(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyReceivedTopicMessageAcknowledged(int, int, int)}
     */
    protected void verifyReceivedTopicMessageAcknowledged(int indexOfSession, int indexOfTopic, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageAcknowledged(indexOfSession, indexOfTopic, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyReceivedTopicMessageAcknowledged(String, int)}
     */
    protected void verifyReceivedTopicMessageAcknowledged(String nameOfTopic, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageAcknowledged(nameOfTopic, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyReceivedTopicMessageNotAcknowledged(String, int)}
     */
    protected void verifyReceivedTopicMessageNotAcknowledged(String nameOfTopic, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageNotAcknowledged(nameOfTopic, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyReceivedTopicMessageNotAcknowledged(int, int, int)}
     */
    protected void verifyReceivedTopicMessageNotAcknowledged(int indexOfSession, int indexOfTopic, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageNotAcknowledged(indexOfSession, indexOfTopic, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedTopicMessages(int, int)}
     */
    protected void verifyNumberOfCreatedTopicMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedTopicBytesMessages(int, int)}
     */
    protected void verifyNumberOfCreatedTopicBytesMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicBytesMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedTopicMapMessages(int, int)}
     */
    protected void verifyNumberOfCreatedTopicMapMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicMapMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedTopicTextMessages(int, int)}
     */
    protected void verifyNumberOfCreatedTopicTextMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicTextMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedTopicStreamMessages(int, int)}
     */
    protected void verifyNumberOfCreatedTopicStreamMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicStreamMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedTopicObjectMessages(int, int)}
     */
    protected void verifyNumberOfCreatedTopicObjectMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicObjectMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTopicMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedTopicMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTopicMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedTopicMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTopicBytesMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedTopicBytesMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicBytesMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTopicBytesMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedTopicBytesMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicBytesMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTopicMapMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedTopicMapMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicMapMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTopicMapMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedTopicMapMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicMapMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTopicTextMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedTopicTextMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicTextMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTopicTextMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedTopicTextMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicTextMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTopicStreamMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedTopicStreamMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicStreamMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTopicStreamMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedTopicStreamMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicStreamMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTopicObjectMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedTopicObjectMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicObjectMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTopicObjectMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedTopicObjectMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicObjectMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedMessages(int, int)}
     */
    protected void verifyNumberOfCreatedMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedBytesMessages(int, int)}
     */
    protected void verifyNumberOfCreatedBytesMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedBytesMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedMapMessages(int, int)}
     */
    protected void verifyNumberOfCreatedMapMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedMapMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedTextMessages(int, int)}
     */
    protected void verifyNumberOfCreatedTextMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTextMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedStreamMessages(int, int)}
     */
    protected void verifyNumberOfCreatedStreamMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedStreamMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyNumberOfCreatedObjectMessages(int, int)}
     */
    protected void verifyNumberOfCreatedObjectMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedObjectMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedBytesMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedBytesMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedBytesMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedBytesMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedBytesMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedBytesMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedMapMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedMapMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedMapMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedMapMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedMapMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedMapMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTextMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedTextMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTextMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedTextMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedTextMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTextMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedStreamMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedStreamMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedStreamMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedStreamMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedStreamMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedStreamMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedObjectMessageAcknowledged(int, int)}
     */
    protected void verifyCreatedObjectMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedObjectMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#verifyCreatedObjectMessageNotAcknowledged(int, int)}
     */
    protected void verifyCreatedObjectMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedObjectMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link com.mockrunner.jms.JMSTestModule#getQueue(String)}
     */
    protected MockQueue getQueue(String name)
    {
        return jmsTestModule.getQueue(name);
    }
}