package com.mockrunner.jms;

import java.util.List;

import javax.jms.MessageListener;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.jms.MockMessage;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueSession;
import com.mockrunner.mock.jms.MockTemporaryQueue;
import com.mockrunner.mock.jms.MockTemporaryTopic;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicConnection;
import com.mockrunner.mock.jms.MockTopicSession;

/**
 * Delegator for {@link JMSTestModule}. You can
 * subclass this adapter or use {@link JMSTestModule}
 * directly (so your test case can use another base
 * class).
 */
public class JMSTestCaseAdapter extends BaseTestCase
{
    private JMSTestModule jmsTestModule;
    
    public JMSTestCaseAdapter()
    {

    }

    public JMSTestCaseAdapter(String arg0)
    {
        super(arg0);
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        jmsTestModule = null;
    }

    /**
     * Creates the <code>JMSTestModule</code>. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        jmsTestModule = createJMSTestModule(getJMSMockObjectFactory());
    }

    /**
     * Gets the <code>JMSTestModule</code>. 
     * @return the <code>JMSTestModule</code>
     */
    protected JMSTestModule getJMSTestModule()
    {
        return jmsTestModule;
    }

    /**
     * Sets the <code>JMSTestModule</code>. 
     * @param jmsTestModule the <code>JMSTestModule</code>
     */
    protected void setJMSTestModule(JMSTestModule jmsTestModule)
    {
        this.jmsTestModule = jmsTestModule;
    }
    
    /**
     * Delegates to {@link JMSTestModule#setCurrentQueueConnectionIndex}
     */
    protected void setCurrentQueueConnectionIndex(int connectionIndex)
    {
        jmsTestModule.setCurrentQueueConnectionIndex(connectionIndex);
    }

    /**
     * Delegates to {@link JMSTestModule#getCurrentQueueConnection}
     */
    protected MockQueueConnection getCurrentQueueConnection()
    {
        return jmsTestModule.getCurrentQueueConnection();
    }

    /**
     * Delegates to {@link JMSTestModule#setCurrentTopicConnectionIndex}
     */
    protected void setCurrentTopicConnectionIndex(int connectionIndex)
    {
        jmsTestModule.setCurrentTopicConnectionIndex(connectionIndex);
    }

    /**
     * Delegates to {@link JMSTestModule#getCurrentTopicConnection}
     */
    protected MockTopicConnection getCurrentTopicConnection()
    {
        return jmsTestModule.getCurrentTopicConnection();
    }
    
    /**
     * Delegates to {@link JMSTestModule#registerTestMessageListenerForQueue(String, MessageListener)}
     */
    public void registerTestMessageListenerForQueue(String queueName, MessageListener listener)
    {
        jmsTestModule.registerTestMessageListenerForQueue(queueName, listener);
    }

    /**
     * Delegates to {@link JMSTestModule#registerTestMessageListenerForQueue(MockQueueConnection, String, MessageListener)}
     */
    public void registerTestMessageListenerForQueue(MockQueueConnection connection, String queueName, MessageListener listener)
    {
        jmsTestModule.registerTestMessageListenerForQueue(connection, queueName,listener);
    }

    /**
     * Delegates to {@link JMSTestModule#registerTestMessageListenerForTopic(String, MessageListener)}
     */
    public void registerTestMessageListenerForTopic(String topicName, MessageListener listener)
    {
        jmsTestModule.registerTestMessageListenerForTopic(topicName, listener);
    }

    /**
     * Delegates to {@link JMSTestModule#registerTestMessageListenerForTopic(MockTopicConnection, String, MessageListener)}
     */
    public void registerTestMessageListenerForTopic(MockTopicConnection connection, String topicName, MessageListener listener)
    {
        jmsTestModule.registerTestMessageListenerForTopic(connection, topicName, listener);
    }
       
    /**
     * Delegates to {@link JMSTestModule#getDestinationManager}
     */
    protected DestinationManager getDestinationManager()
    {
        return jmsTestModule.getDestinationManager();
    } 

    /**
     * Delegates to {@link JMSTestModule#getQueueMessageManager}
     */
    protected MessageManager getQueueMessageManager(int indexOfSession)
    {
        return jmsTestModule.getQueueMessageManager(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getTopicMessageManager}
     */
    protected MessageManager getTopicMessageManager(int indexOfSession)
    {
        return jmsTestModule.getTopicMessageManager(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getQueueTransmissionManager}
     */
    protected QueueTransmissionManager getQueueTransmissionManager(int indexOfSession)
    {
        return jmsTestModule.getQueueTransmissionManager(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getTopicTransmissionManager}
     */
    protected TopicTransmissionManager getTopicTransmissionManager(int indexOfSession)
    {
        return jmsTestModule.getTopicTransmissionManager(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getQueueSessionList}
     */
    protected List getQueueSessionList()
    {
        return jmsTestModule.getQueueSessionList();
    }

    /**
     * Delegates to {@link JMSTestModule#getTopicSessionList}
     */
    protected List getTopicSessionList()
    {
        return jmsTestModule.getTopicSessionList();
    }

    /**
     * Delegates to {@link JMSTestModule#getQueueSession}
     */
    protected MockQueueSession getQueueSession(int indexOfSession)
    {
        return jmsTestModule.getQueueSession(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getTopicSession}
     */
    protected MockTopicSession getTopicSession(int indexOfSession)
    {
        return jmsTestModule.getTopicSession(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getQueue}
     */
    protected MockQueue getQueue(String name)
    {
        return jmsTestModule.getQueue(name);
    }

    /**
     * Delegates to {@link JMSTestModule#getTopic}
     */
    protected MockTopic getTopic(String name)
    {
        return jmsTestModule.getTopic(name);
    }

    /**
     * Delegates to {@link JMSTestModule#getTemporaryQueueList}
     */
    protected List getTemporaryQueueList(int indexOfSession)
    {
        return jmsTestModule.getTemporaryQueueList(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getTemporaryTopicList}
     */
    protected List getTemporaryTopicList(int indexOfSession)
    {
        return jmsTestModule.getTemporaryTopicList(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getTemporaryQueue}
     */
    protected MockTemporaryQueue getTemporaryQueue(int indexOfSession, int indexOfQueue)
    {
        return jmsTestModule.getTemporaryQueue(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link JMSTestModule#getTemporaryTopic}
     */
    protected MockTemporaryTopic getTemporaryTopic(int indexOfSession, int indexOfTopic)
    {
        return jmsTestModule.getTemporaryTopic(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link JMSTestModule#getCurrentMessageListFromQueue}
     */
    protected List getCurrentMessageListFromQueue(String name)
    {
        return jmsTestModule.getCurrentMessageListFromQueue(name);
    }

    /**
     * Delegates to {@link JMSTestModule#getCurrentMessageListFromTemporaryQueue}
     */
    protected List getCurrentMessageListFromTemporaryQueue(int indexOfSession, int indexOfQueue)
    {
        return jmsTestModule.getCurrentMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link JMSTestModule#getReceivedMessageListFromQueue}
     */
    protected List getReceivedMessageListFromQueue(String name)
    {
        return jmsTestModule.getReceivedMessageListFromQueue(name);
    }

    /**
     * Delegates to {@link JMSTestModule#getReceivedMessageListFromTemporaryQueue}
     */
    protected List getReceivedMessageListFromTemporaryQueue(int indexOfSession, int indexOfQueue)
    {
        return jmsTestModule.getReceivedMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link JMSTestModule#getCurrentMessageListFromTopic}
     */
    protected List getCurrentMessageListFromTopic(String name)
    {
        return jmsTestModule.getCurrentMessageListFromTopic(name);
    }

    /**
     * Delegates to {@link JMSTestModule#getCurrentMessageListFromTemporaryTopic}
     */
    protected List getCurrentMessageListFromTemporaryTopic(int indexOfSession, int indexOfTopic)
    {
        return jmsTestModule.getCurrentMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link JMSTestModule#getReceivedMessageListFromTopic}
     */
    protected List getReceivedMessageListFromTopic(String name)
    {
        return jmsTestModule.getReceivedMessageListFromTopic(name);
    }

    /**
     * Delegates to {@link JMSTestModule#getReceivedMessageListFromTemporaryTopic}
     */
    protected List getReceivedMessageListFromTemporaryTopic(int indexOfSession, int indexOfTopic)
    {
        return jmsTestModule.getReceivedMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueConnectionClosed}
     */
    protected void verifyQueueConnectionClosed()
    {
        jmsTestModule.verifyQueueConnectionClosed();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueConnectionStarted}
     */
    protected void verifyQueueConnectionStarted()
    {
        jmsTestModule.verifyQueueConnectionStarted();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueConnectionStopped}
     */
    protected void verifyQueueConnectionStopped()
    {
        jmsTestModule.verifyQueueConnectionStopped();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicConnectionClosed}
     */
    protected void verifyTopicConnectionClosed()
    {
        jmsTestModule.verifyTopicConnectionClosed();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicConnectionStarted}
     */
    protected void verifyTopicConnectionStarted()
    {
        jmsTestModule.verifyTopicConnectionStarted();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicConnectionStopped}
     */
    protected void verifyTopicConnectionStopped()
    {
        jmsTestModule.verifyTopicConnectionStopped();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionClosed}
     */
    protected void verifyQueueSessionClosed(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionCommitted}
     */
    protected void verifyQueueSessionCommitted(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionNotCommitted}
     */
    protected void verifyQueueSessionNotCommitted(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionNotCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionRolledBack}
     */
    protected void verifyQueueSessionRolledBack(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionNotRolledBack}
     */
    protected void verifyQueueSessionNotRolledBack(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionNotRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionRecovered}
     */
    protected void verifyQueueSessionRecovered(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionNotRecovered}
     */
    protected void verifyQueueSessionNotRecovered(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionNotRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionClosed}
     */
    protected void verifyTopicSessionClosed(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionCommitted}
     */
    protected void verifyTopicSessionCommitted(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionNotCommitted}
     */
    protected void verifyTopicSessionNotCommitted(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionNotCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionRolledBack}
     */
    protected void verifyTopicSessionRolledBack(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionNotRolledBack}
     */
    protected void verifyTopicSessionNotRolledBack(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionNotRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionRecovered}
     */
    protected void verifyTopicSessionRecovered(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionNotRecovered}
     */
    protected void verifyTopicSessionNotRecovered(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionNotRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueSessionsClosed}
     */
    protected void verifyAllQueueSessionsClosed()
    {
        jmsTestModule.verifyAllQueueSessionsClosed();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueSessionsRecovered}
     */
    protected void verifyAllQueueSessionsRecovered()
    {
        jmsTestModule.verifyAllQueueSessionsRecovered();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueSessionsCommitted}
     */
    protected void verifyAllQueueSessionsCommitted()
    {
        jmsTestModule.verifyAllQueueSessionsCommitted();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueSessionsRolledBack}
     */
    protected void verifyAllQueueSessionsRolledBack()
    {
        jmsTestModule.verifyAllQueueSessionsRolledBack();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTopicSessionsClosed}
     */
    protected void verifyAllTopicSessionsClosed()
    {
        jmsTestModule.verifyAllTopicSessionsClosed();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTopicSessionsRecovered}
     */
    protected void verifyAllTopicSessionsRecovered()
    {
        jmsTestModule.verifyAllTopicSessionsRecovered();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTopicSessionsCommitted}
     */
    protected void verifyAllTopicSessionsCommitted()
    {
        jmsTestModule.verifyAllTopicSessionsCommitted();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTopicSessionsRolledBack}
     */
    protected void verifyAllTopicSessionsRolledBack()
    {
        jmsTestModule.verifyAllTopicSessionsRolledBack();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueSenders(int, int)}
     */
    protected void verifyNumberQueueSenders(int indexOfSession, int numberOfSenders)
    {
        jmsTestModule.verifyNumberQueueSenders(indexOfSession, numberOfSenders);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueSenders(int, String, int)}
     */
    protected void verifyNumberQueueSenders(int indexOfSession, String queueName, int numberOfSenders)
    {
        jmsTestModule.verifyNumberQueueSenders(indexOfSession, queueName, numberOfSenders);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSenderClosed}
     */
    protected void verifyQueueSenderClosed(int indexOfSession, String queueName, int indexOfSender)
    {
        jmsTestModule.verifyQueueSenderClosed(indexOfSession, queueName, indexOfSender);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueSendersClosed}
     */
    protected void verifyAllQueueSendersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllQueueSendersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTopicPublishers(int, int)}
     */
    protected void verifyNumberTopicPublishers(int indexOfSession, int numberOfPublishers)
    {
        jmsTestModule.verifyNumberTopicPublishers(indexOfSession, numberOfPublishers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTopicPublishers(int, String, int)}
     */
    protected void verifyNumberTopicPublishers(int indexOfSession, String topicName, int numberOfPublishers)
    {
        jmsTestModule.verifyNumberTopicPublishers(indexOfSession, topicName, numberOfPublishers);
    } 

    /**
     * Delegates to {@link JMSTestModule#verifyTopicPublisherClosed}
     */
    protected void verifyTopicPublisherClosed(int indexOfSession, String topicName, int indexOfPublisher)
    {
        jmsTestModule.verifyTopicPublisherClosed(indexOfSession, topicName, indexOfPublisher);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTopicPublishersClosed}
     */
    protected void verifyAllTopicPublishersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllTopicPublishersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueReceivers(int, int)}
     */
    protected void verifyNumberQueueReceivers(int indexOfSession, int numberOfReceivers)
    {
        jmsTestModule.verifyNumberQueueReceivers(indexOfSession, numberOfReceivers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueReceivers(int, String, int)}
     */
    protected void verifyNumberQueueReceivers(int indexOfSession, String queueName, int numberOfReceivers)
    {
        jmsTestModule.verifyNumberQueueReceivers(indexOfSession, queueName, numberOfReceivers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueReceiverClosed}
     */
    protected void verifyQueueReceiverClosed(int indexOfSession, String queueName, int indexOfReceiver)
    {
        jmsTestModule.verifyQueueReceiverClosed(indexOfSession, queueName, indexOfReceiver);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueReceiversClosed}
     */
    protected void verifyAllQueueReceiversClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllQueueReceiversClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTopicSubscribers(int, int)}
     */
    protected void verifyNumberTopicSubscribers(int indexOfSession, int numberOfSubscribers)
    {
        jmsTestModule.verifyNumberTopicSubscribers(indexOfSession, numberOfSubscribers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTopicSubscribers(int, String, int)}
     */
    protected void verifyNumberTopicSubscribers(int indexOfSession, String topicName, int numberOfSubscribers)
    {
        jmsTestModule.verifyNumberTopicSubscribers(indexOfSession, topicName, numberOfSubscribers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSubscriberClosed}
     */
    protected void verifyTopicSubscriberClosed(int indexOfSession, String topicName, int indexOfSubscriber)
    {
        jmsTestModule.verifyTopicSubscriberClosed(indexOfSession, topicName, indexOfSubscriber);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTopicSubscribersClosed}
     */
    protected void verifyAllTopicSubscribersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllTopicSubscribersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueBrowsers(int, int)}
     */
    protected void verifyNumberQueueBrowsers(int indexOfSession, int numberOfBrowsers)
    {
        jmsTestModule.verifyNumberQueueBrowsers(indexOfSession, numberOfBrowsers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueBrowsers(int, String, int)}
     */
    protected void verifyNumberQueueBrowsers(int indexOfSession, String queueName, int numberOfBrowsers)
    {
        jmsTestModule.verifyNumberQueueBrowsers(indexOfSession, queueName, numberOfBrowsers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueBrowserClosed}
     */
    protected void verifyQueueBrowserClosed(int indexOfSession, String queueName, int indexOfBrowser)
    {
        jmsTestModule.verifyQueueBrowserClosed(indexOfSession, queueName, indexOfBrowser);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueBrowsersClosed}
     */
    protected void verifyAllQueueBrowsersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllQueueBrowsersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyDurableTopicSubscriberPresent}
     */
    protected void verifyDurableTopicSubscriberPresent(int indexOfSession, String nameOfSubscriber)
    {
        jmsTestModule.verifyDurableTopicSubscriberPresent(indexOfSession, nameOfSubscriber);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberDurableTopicSubscribers(int, int)}
     */
    protected void verifyNumberDurableTopicSubscribers(int indexOfSession, int numberOfSubscribers)
    {
        jmsTestModule.verifyNumberDurableTopicSubscribers(indexOfSession, numberOfSubscribers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberDurableTopicSubscribers(int, String, int)}
     */
    protected void verifyNumberDurableTopicSubscribers(int indexOfSession, String topicName, int numberOfSubscribers)
    {
        jmsTestModule.verifyNumberDurableTopicSubscribers(indexOfSession, topicName, numberOfSubscribers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyDurableTopicSubscriberClosed}
     */
    protected void verifyDurableTopicSubscriberClosed(int indexOfSession, String subscriberName)
    {
        jmsTestModule.verifyDurableTopicSubscriberClosed(indexOfSession, subscriberName);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllDurableTopicSubscribersClosed}
     */
    protected void verifyAllDurableTopicSubscribersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllDurableTopicSubscribersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueSessions}
     */
    protected void verifyNumberQueueSessions(int number)
    {
        jmsTestModule.verifyNumberQueueSessions(number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTopicSessions}
     */
    protected void verifyNumberTopicSessions(int number)
    {
        jmsTestModule.verifyNumberTopicSessions(number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTemporaryQueues}
     */
    protected void verifyNumberTemporaryQueues(int indexOfSession, int numberQueues)
    {
        jmsTestModule.verifyNumberTemporaryQueues(indexOfSession, numberQueues);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTemporaryTopics}
     */
    protected void verifyNumberTemporaryTopics(int indexOfSession, int numberTopics)
    {
        jmsTestModule.verifyNumberTemporaryTopics(indexOfSession, numberTopics);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTemporaryQueueDeleted}
     */
    protected void verifyTemporaryQueueDeleted(int indexOfSession, int indexOfQueue)
    {
        jmsTestModule.verifyTemporaryQueueDeleted(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTemporaryQueuesDeleted}
     */
    protected void verifyAllTemporaryQueuesDeleted(int indexOfSession)
    {
        jmsTestModule.verifyAllTemporaryQueuesDeleted(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTemporaryTopicDeleted}
     */
    protected void verifyTemporaryTopicDeleted(int indexOfSession, int indexOfTopic)
    {
        jmsTestModule.verifyTemporaryTopicDeleted(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTemporaryTopicsDeleted}
     */
    protected void verifyAllTemporaryTopicsDeleted(int indexOfSession)
    {
        jmsTestModule.verifyAllTemporaryTopicsDeleted(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyMessageEquals}
     */
    protected void verifyMessageEquals(MockMessage message1, MockMessage message2)
    {
        jmsTestModule.verifyMessageEquals(message1, message2);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCurrentQueueMessageEquals(String, int, MockMessage)}
     */
    protected void verifyCurrentQueueMessageEquals(String nameOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyCurrentQueueMessageEquals(nameOfQueue, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedQueueMessageEquals(String, int, MockMessage)}
     */
    protected void verifyReceivedQueueMessageEquals(String nameOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageEquals(nameOfQueue, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCurrentQueueMessageEquals(int, int, int, MockMessage)}
     */
    protected void verifyCurrentQueueMessageEquals(int indexOfSession, int indexOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyCurrentQueueMessageEquals(indexOfSession, indexOfQueue, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedQueueMessageEquals(int, int, int, MockMessage)}
     */
    protected void verifyReceivedQueueMessageEquals(int indexOfSession, int indexOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageEquals(indexOfSession, indexOfQueue, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCurrentQueueMessages(String, int)}
     */
    protected void verifyNumberOfCurrentQueueMessages(String nameOfQueue, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfCurrentQueueMessages(nameOfQueue, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfReceivedQueueMessages(String, int)}
     */
    protected void verifyNumberOfReceivedQueueMessages(String nameOfQueue, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfReceivedQueueMessages(nameOfQueue, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCurrentQueueMessages(int, int, int)}
     */
    protected void verifyNumberOfCurrentQueueMessages(int indexOfSession, int indexOfQueue, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfCurrentQueueMessages(indexOfSession, indexOfQueue, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfReceivedQueueMessages(int, int, int)}
     */
    protected void verifyNumberOfReceivedQueueMessages(int indexOfSession, int indexOfQueue, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfReceivedQueueMessages(indexOfSession, indexOfQueue, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllReceivedQueueMessagesAcknowledged(String)}
     */
    protected void verifyAllReceivedQueueMessagesAcknowledged(String nameOfQueue)
    {
        jmsTestModule.verifyAllReceivedQueueMessagesAcknowledged(nameOfQueue);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllReceivedQueueMessagesAcknowledged(int, int)}
     */
    protected void verifyAllReceivedQueueMessagesAcknowledged(int indexOfSession, int indexOfQueue)
    {
        jmsTestModule.verifyAllReceivedQueueMessagesAcknowledged(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedQueueMessageAcknowledged(String, int)}
     */
    protected void verifyReceivedQueueMessageAcknowledged(String nameOfQueue, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageAcknowledged(nameOfQueue, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedQueueMessageNotAcknowledged(String, int)}
     */
    protected void verifyReceivedQueueMessageNotAcknowledged(String nameOfQueue, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageNotAcknowledged(nameOfQueue, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedQueueMessageAcknowledged(int, int, int)}
     */
    protected void verifyReceivedQueueMessageAcknowledged(int indexOfSession, int indexOfQueue, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageAcknowledged(indexOfSession, indexOfQueue, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedQueueMessageNotAcknowledged(int, int, int)}
     */
    protected void verifyReceivedQueueMessageNotAcknowledged(int indexOfSession, int indexOfQueue, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageNotAcknowledged(indexOfSession, indexOfQueue, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedQueueMessages}
     */
    protected void verifyNumberOfCreatedQueueMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedQueueBytesMessages}
     */
    protected void verifyNumberOfCreatedQueueBytesMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueBytesMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedQueueMapMessages}
     */
    protected void verifyNumberOfCreatedQueueMapMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueMapMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedQueueTextMessages}
     */
    protected void verifyNumberOfCreatedQueueTextMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueTextMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedQueueStreamMessages}
     */
    protected void verifyNumberOfCreatedQueueStreamMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueStreamMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedQueueObjectMessages}
     */
    protected void verifyNumberOfCreatedQueueObjectMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueObjectMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueMessageAcknowledged}
     */
    protected void verifyCreatedQueueMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueMessageNotAcknowledged}
     */
    protected void verifyCreatedQueueMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueBytesMessageAcknowledged}
     */
    protected void verifyCreatedQueueBytesMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueBytesMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueBytesMessageNotAcknowledged}
     */
    protected void verifyCreatedQueueBytesMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueBytesMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueMapMessageAcknowledged}
     */
    protected void verifyCreatedQueueMapMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueMapMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueMapMessageNotAcknowledged}
     */
    protected void verifyCreatedQueueMapMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueMapMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueTextMessageAcknowledged}
     */
    protected void verifyCreatedQueueTextMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueTextMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueTextMessageNotAcknowledged}
     */
    protected void verifyCreatedQueueTextMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueTextMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueStreamMessageAcknowledged}
     */
    protected void verifyCreatedQueueStreamMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueStreamMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueStreamMessageNotAcknowledged}
     */
    protected void verifyCreatedQueueStreamMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueStreamMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueObjectMessageAcknowledged}
     */
    protected void verifyCreatedQueueObjectMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueObjectMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueObjectMessageNotAcknowledged}
     */
    protected void verifyCreatedQueueObjectMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueObjectMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCurrentTopicMessageEquals(String, int, MockMessage)}
     */
    protected void verifyCurrentTopicMessageEquals(String nameOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyCurrentTopicMessageEquals(nameOfTopic, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedTopicMessageEquals(String, int, MockMessage)}
     */
    protected void verifyReceivedTopicMessageEquals(String nameOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageEquals(nameOfTopic, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCurrentTopicMessageEquals(int, int, int, MockMessage)}
     */
    protected void verifyCurrentTopicMessageEquals(int indexOfSession, int indexOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyCurrentTopicMessageEquals(indexOfSession, indexOfTopic, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedTopicMessageEquals(int, int, int, MockMessage)}
     */
    protected void verifyReceivedTopicMessageEquals(int indexOfSession, int indexOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageEquals(indexOfSession, indexOfTopic, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCurrentTopicMessages(String, int)}
     */
    protected void verifyNumberOfCurrentTopicMessages(String nameOfTopic, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfCurrentTopicMessages(nameOfTopic, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfReceivedTopicMessages(String, int)}
     */
    protected void verifyNumberOfReceivedTopicMessages(String nameOfTopic, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfReceivedTopicMessages(nameOfTopic, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCurrentTopicMessages(int, int, int)}
     */
    protected void verifyNumberOfCurrentTopicMessages(int indexOfSession, int indexOfTopic, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfCurrentTopicMessages(indexOfSession, indexOfTopic, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfReceivedTopicMessages(int, int, int)}
     */
    protected void verifyNumberOfReceivedTopicMessages(int indexOfSession, int indexOfTopic, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfReceivedTopicMessages(indexOfSession, indexOfTopic, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllReceivedTopicMessagesAcknowledged(String)}
     */
    protected void verifyAllReceivedTopicMessagesAcknowledged(String nameOfTopic)
    {
        jmsTestModule.verifyAllReceivedTopicMessagesAcknowledged(nameOfTopic);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllReceivedTopicMessagesAcknowledged(int, int)}
     */
    protected void verifyAllReceivedTopicMessagesAcknowledged(int indexOfSession, int indexOfTopic)
    {
        jmsTestModule.verifyAllReceivedTopicMessagesAcknowledged(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedTopicMessageAcknowledged(String, int)}
     */
    protected void verifyReceivedTopicMessageAcknowledged(String nameOfTopic, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageAcknowledged(nameOfTopic, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedTopicMessageNotAcknowledged(String, int)}
     */
    protected void verifyReceivedTopicMessageNotAcknowledged(String nameOfTopic, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageNotAcknowledged(nameOfTopic, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedTopicMessageAcknowledged(int, int, int)}
     */
    protected void verifyReceivedTopicMessageAcknowledged(int indexOfSession, int indexOfTopic, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageAcknowledged(indexOfSession, indexOfTopic, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedTopicMessageNotAcknowledged(int, int, int)}
     */
    protected void verifyReceivedTopicMessageNotAcknowledged(int indexOfSession, int indexOfTopic, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageNotAcknowledged(indexOfSession, indexOfTopic, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedTopicMessages}
     */
    protected void verifyNumberOfCreatedTopicMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedTopicBytesMessages}
     */
    protected void verifyNumberOfCreatedTopicBytesMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicBytesMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedTopicMapMessages}
     */
    protected void verifyNumberOfCreatedTopicMapMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicMapMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedTopicTextMessages}
     */
    protected void verifyNumberOfCreatedTopicTextMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicTextMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedTopicStreamMessages}
     */
    protected void verifyNumberOfCreatedTopicStreamMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicStreamMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedTopicObjectMessages}
     */
    protected void verifyNumberOfCreatedTopicObjectMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicObjectMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicMessageAcknowledged}
     */
    protected void verifyCreatedTopicMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicMessageNotAcknowledged}
     */
    protected void verifyCreatedTopicMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicBytesMessageAcknowledged}
     */
    protected void verifyCreatedTopicBytesMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicBytesMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicBytesMessageNotAcknowledged}
     */
    protected void verifyCreatedTopicBytesMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicBytesMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicMapMessageAcknowledged}
     */
    protected void verifyCreatedTopicMapMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicMapMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicMapMessageNotAcknowledged}
     */
    protected void verifyCreatedTopicMapMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicMapMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicTextMessageAcknowledged}
     */
    protected void verifyCreatedTopicTextMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicTextMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicTextMessageNotAcknowledged}
     */
    protected void verifyCreatedTopicTextMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicTextMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicStreamMessageAcknowledged}
     */
    protected void verifyCreatedTopicStreamMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicStreamMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicStreamMessageNotAcknowledged}
     */
    protected void verifyCreatedTopicStreamMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicStreamMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicObjectMessageAcknowledged}
     */
    protected void verifyCreatedTopicObjectMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicObjectMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicObjectMessageNotAcknowledged}
     */
    protected void verifyCreatedTopicObjectMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicObjectMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }
}
