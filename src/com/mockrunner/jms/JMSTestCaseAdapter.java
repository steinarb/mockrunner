package com.mockrunner.jms;

import java.util.List;

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
    public void setCurrentQueueConnectionIndex(int connectionIndex)
    {
        jmsTestModule.setCurrentQueueConnectionIndex(connectionIndex);
    }

    /**
     * Delegates to {@link JMSTestModule#getCurrentQueueConnection}
     */
    public MockQueueConnection getCurrentQueueConnection()
    {
        return jmsTestModule.getCurrentQueueConnection();
    }

    /**
     * Delegates to {@link JMSTestModule#setCurrentTopicConnectionIndex}
     */
    public void setCurrentTopicConnectionIndex(int connectionIndex)
    {
        jmsTestModule.setCurrentTopicConnectionIndex(connectionIndex);
    }

    /**
     * Delegates to {@link JMSTestModule#getCurrentTopicConnection}
     */
    public MockTopicConnection getCurrentTopicConnection()
    {
        return jmsTestModule.getCurrentTopicConnection();
    }
    
    /**
     * Delegates to {@link JMSTestModule#getDestinationManager}
     */
    public DestinationManager getDestinationManager()
    {
        return jmsTestModule.getDestinationManager();
    } 

    /**
     * Delegates to {@link JMSTestModule#getQueueMessageManager}
     */
    public MessageManager getQueueMessageManager(int indexOfSession)
    {
        return jmsTestModule.getQueueMessageManager(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getTopicMessageManager}
     */
    public MessageManager getTopicMessageManager(int indexOfSession)
    {
        return jmsTestModule.getTopicMessageManager(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getQueueTransmissionManager}
     */
    public QueueTransmissionManager getQueueTransmissionManager(int indexOfSession)
    {
        return jmsTestModule.getQueueTransmissionManager(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getTopicTransmissionManager}
     */
    public TopicTransmissionManager getTopicTransmissionManager(int indexOfSession)
    {
        return jmsTestModule.getTopicTransmissionManager(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getQueueSessionList}
     */
    public List getQueueSessionList()
    {
        return jmsTestModule.getQueueSessionList();
    }

    /**
     * Delegates to {@link JMSTestModule#getTopicSessionList}
     */
    public List getTopicSessionList()
    {
        return jmsTestModule.getTopicSessionList();
    }

    /**
     * Delegates to {@link JMSTestModule#getQueueSession}
     */
    public MockQueueSession getQueueSession(int indexOfSession)
    {
        return jmsTestModule.getQueueSession(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getTopicSession}
     */
    public MockTopicSession getTopicSession(int indexOfSession)
    {
        return jmsTestModule.getTopicSession(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getQueue}
     */
    public MockQueue getQueue(String name)
    {
        return jmsTestModule.getQueue(name);
    }

    /**
     * Delegates to {@link JMSTestModule#getTopic}
     */
    public MockTopic getTopic(String name)
    {
        return jmsTestModule.getTopic(name);
    }

    /**
     * Delegates to {@link JMSTestModule#getTemporaryQueueList}
     */
    public List getTemporaryQueueList(int indexOfSession)
    {
        return jmsTestModule.getTemporaryQueueList(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getTemporaryTopicList}
     */
    public List getTemporaryTopicList(int indexOfSession)
    {
        return jmsTestModule.getTemporaryTopicList(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#getTemporaryQueue}
     */
    public MockTemporaryQueue getTemporaryQueue(int indexOfSession, int indexOfQueue)
    {
        return jmsTestModule.getTemporaryQueue(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link JMSTestModule#getTemporaryTopic}
     */
    public MockTemporaryTopic getTemporaryTopic(int indexOfSession, int indexOfTopic)
    {
        return jmsTestModule.getTemporaryTopic(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link JMSTestModule#getCurrentMessageListFromQueue}
     */
    public List getCurrentMessageListFromQueue(String name)
    {
        return jmsTestModule.getCurrentMessageListFromQueue(name);
    }

    /**
     * Delegates to {@link JMSTestModule#getCurrentMessageListFromTemporaryQueue}
     */
    public List getCurrentMessageListFromTemporaryQueue(int indexOfSession, int indexOfQueue)
    {
        return jmsTestModule.getCurrentMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link JMSTestModule#getReceivedMessageListFromQueue}
     */
    public List getReceivedMessageListFromQueue(String name)
    {
        return jmsTestModule.getReceivedMessageListFromQueue(name);
    }

    /**
     * Delegates to {@link JMSTestModule#getReceivedMessageListFromTemporaryQueue}
     */
    public List getReceivedMessageListFromTemporaryQueue(int indexOfSession, int indexOfQueue)
    {
        return jmsTestModule.getReceivedMessageListFromTemporaryQueue(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link JMSTestModule#getCurrentMessageListFromTopic}
     */
    public List getCurrentMessageListFromTopic(String name)
    {
        return jmsTestModule.getCurrentMessageListFromTopic(name);
    }

    /**
     * Delegates to {@link JMSTestModule#getCurrentMessageListFromTemporaryTopic}
     */
    public List getCurrentMessageListFromTemporaryTopic(int indexOfSession, int indexOfTopic)
    {
        return jmsTestModule.getCurrentMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link JMSTestModule#getReceivedMessageListFromTopic}
     */
    public List getReceivedMessageListFromTopic(String name)
    {
        return jmsTestModule.getReceivedMessageListFromTopic(name);
    }

    /**
     * Delegates to {@link JMSTestModule#getReceivedMessageListFromTemporaryTopic}
     */
    public List getReceivedMessageListFromTemporaryTopic(int indexOfSession, int indexOfTopic)
    {
        return jmsTestModule.getReceivedMessageListFromTemporaryTopic(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueConnectionClosed}
     */
    public void verifyQueueConnectionClosed()
    {
        jmsTestModule.verifyQueueConnectionClosed();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueConnectionStarted}
     */
    public void verifyQueueConnectionStarted()
    {
        jmsTestModule.verifyQueueConnectionStarted();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueConnectionStopped}
     */
    public void verifyQueueConnectionStopped()
    {
        jmsTestModule.verifyQueueConnectionStopped();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicConnectionClosed}
     */
    public void verifyTopicConnectionClosed()
    {
        jmsTestModule.verifyTopicConnectionClosed();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicConnectionStarted}
     */
    public void verifyTopicConnectionStarted()
    {
        jmsTestModule.verifyTopicConnectionStarted();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicConnectionStopped}
     */
    public void verifyTopicConnectionStopped()
    {
        jmsTestModule.verifyTopicConnectionStopped();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionClosed}
     */
    public void verifyQueueSessionClosed(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionCommitted}
     */
    public void verifyQueueSessionCommitted(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionNotCommitted}
     */
    public void verifyQueueSessionNotCommitted(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionNotCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionRolledBack}
     */
    public void verifyQueueSessionRolledBack(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionNotRolledBack}
     */
    public void verifyQueueSessionNotRolledBack(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionNotRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionRecovered}
     */
    public void verifyQueueSessionRecovered(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSessionNotRecovered}
     */
    public void verifyQueueSessionNotRecovered(int indexOfSession)
    {
        jmsTestModule.verifyQueueSessionNotRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionClosed}
     */
    public void verifyTopicSessionClosed(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionCommitted}
     */
    public void verifyTopicSessionCommitted(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionNotCommitted}
     */
    public void verifyTopicSessionNotCommitted(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionNotCommitted(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionRolledBack}
     */
    public void verifyTopicSessionRolledBack(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionNotRolledBack}
     */
    public void verifyTopicSessionNotRolledBack(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionNotRolledBack(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionRecovered}
     */
    public void verifyTopicSessionRecovered(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSessionNotRecovered}
     */
    public void verifyTopicSessionNotRecovered(int indexOfSession)
    {
        jmsTestModule.verifyTopicSessionNotRecovered(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueSessionsClosed}
     */
    public void verifyAllQueueSessionsClosed()
    {
        jmsTestModule.verifyAllQueueSessionsClosed();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueSessionsRecovered}
     */
    public void verifyAllQueueSessionsRecovered()
    {
        jmsTestModule.verifyAllQueueSessionsRecovered();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueSessionsCommitted}
     */
    public void verifyAllQueueSessionsCommitted()
    {
        jmsTestModule.verifyAllQueueSessionsCommitted();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueSessionsRolledBack}
     */
    public void verifyAllQueueSessionsRolledBack()
    {
        jmsTestModule.verifyAllQueueSessionsRolledBack();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTopicSessionsClosed}
     */
    public void verifyAllTopicSessionsClosed()
    {
        jmsTestModule.verifyAllTopicSessionsClosed();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTopicSessionsRecovered}
     */
    public void verifyAllTopicSessionsRecovered()
    {
        jmsTestModule.verifyAllTopicSessionsRecovered();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTopicSessionsCommitted}
     */
    public void verifyAllTopicSessionsCommitted()
    {
        jmsTestModule.verifyAllTopicSessionsCommitted();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTopicSessionsRolledBack}
     */
    public void verifyAllTopicSessionsRolledBack()
    {
        jmsTestModule.verifyAllTopicSessionsRolledBack();
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueSenders(int, int)}
     */
    public void verifyNumberQueueSenders(int indexOfSession, int numberOfSenders)
    {
        jmsTestModule.verifyNumberQueueSenders(indexOfSession, numberOfSenders);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueSenders(int, String, int)}
     */
    public void verifyNumberQueueSenders(int indexOfSession, String queueName, int numberOfSenders)
    {
        jmsTestModule.verifyNumberQueueSenders(indexOfSession, queueName, numberOfSenders);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueSenderClosed}
     */
    public void verifyQueueSenderClosed(int indexOfSession, String queueName, int indexOfSender)
    {
        jmsTestModule.verifyQueueSenderClosed(indexOfSession, queueName, indexOfSender);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueSendersClosed}
     */
    public void verifyAllQueueSendersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllQueueSendersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTopicPublishers(int, int)}
     */
    public void verifyNumberTopicPublishers(int indexOfSession, int numberOfPublishers)
    {
        jmsTestModule.verifyNumberTopicPublishers(indexOfSession, numberOfPublishers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTopicPublishers(int, String, int)}
     */
    public void verifyNumberTopicPublishers(int indexOfSession, String topicName, int numberOfPublishers)
    {
        jmsTestModule.verifyNumberTopicPublishers(indexOfSession, topicName, numberOfPublishers);
    } 

    /**
     * Delegates to {@link JMSTestModule#verifyTopicPublisherClosed}
     */
    public void verifyTopicPublisherClosed(int indexOfSession, String topicName, int indexOfPublisher)
    {
        jmsTestModule.verifyTopicPublisherClosed(indexOfSession, topicName, indexOfPublisher);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTopicPublishersClosed}
     */
    public void verifyAllTopicPublishersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllTopicPublishersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueReceivers(int, int)}
     */
    public void verifyNumberQueueReceivers(int indexOfSession, int numberOfReceivers)
    {
        jmsTestModule.verifyNumberQueueReceivers(indexOfSession, numberOfReceivers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueReceivers(int, String, int)}
     */
    public void verifyNumberQueueReceivers(int indexOfSession, String queueName, int numberOfReceivers)
    {
        jmsTestModule.verifyNumberQueueReceivers(indexOfSession, queueName, numberOfReceivers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueReceiverClosed}
     */
    public void verifyQueueReceiverClosed(int indexOfSession, String queueName, int indexOfReceiver)
    {
        jmsTestModule.verifyQueueReceiverClosed(indexOfSession, queueName, indexOfReceiver);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueReceiversClosed}
     */
    public void verifyAllQueueReceiversClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllQueueReceiversClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTopicSubscribers(int, int)}
     */
    public void verifyNumberTopicSubscribers(int indexOfSession, int numberOfSubscribers)
    {
        jmsTestModule.verifyNumberTopicSubscribers(indexOfSession, numberOfSubscribers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTopicSubscribers(int, String, int)}
     */
    public void verifyNumberTopicSubscribers(int indexOfSession, String topicName, int numberOfSubscribers)
    {
        jmsTestModule.verifyNumberTopicSubscribers(indexOfSession, topicName, numberOfSubscribers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTopicSubscriberClosed}
     */
    public void verifyTopicSubscriberClosed(int indexOfSession, String topicName, int indexOfSubscriber)
    {
        jmsTestModule.verifyTopicSubscriberClosed(indexOfSession, topicName, indexOfSubscriber);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTopicSubscribersClosed}
     */
    public void verifyAllTopicSubscribersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllTopicSubscribersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueBrowsers(int, int)}
     */
    public void verifyNumberQueueBrowsers(int indexOfSession, int numberOfBrowsers)
    {
        jmsTestModule.verifyNumberQueueBrowsers(indexOfSession, numberOfBrowsers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueBrowsers(int, String, int)}
     */
    public void verifyNumberQueueBrowsers(int indexOfSession, String queueName, int numberOfBrowsers)
    {
        jmsTestModule.verifyNumberQueueBrowsers(indexOfSession, queueName, numberOfBrowsers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyQueueBrowserClosed}
     */
    public void verifyQueueBrowserClosed(int indexOfSession, String queueName, int indexOfBrowser)
    {
        jmsTestModule.verifyQueueBrowserClosed(indexOfSession, queueName, indexOfBrowser);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllQueueBrowsersClosed}
     */
    public void verifyAllQueueBrowsersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllQueueBrowsersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyDurableTopicSubscriberPresent}
     */
    public void verifyDurableTopicSubscriberPresent(int indexOfSession, String nameOfSubscriber)
    {
        jmsTestModule.verifyDurableTopicSubscriberPresent(indexOfSession, nameOfSubscriber);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberDurableTopicSubscribers(int, int)}
     */
    public void verifyNumberDurableTopicSubscribers(int indexOfSession, int numberOfSubscribers)
    {
        jmsTestModule.verifyNumberDurableTopicSubscribers(indexOfSession, numberOfSubscribers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberDurableTopicSubscribers(int, String, int)}
     */
    public void verifyNumberDurableTopicSubscribers(int indexOfSession, String topicName, int numberOfSubscribers)
    {
        jmsTestModule.verifyNumberDurableTopicSubscribers(indexOfSession, topicName, numberOfSubscribers);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyDurableTopicSubscriberClosed}
     */
    public void verifyDurableTopicSubscriberClosed(int indexOfSession, String subscriberName)
    {
        jmsTestModule.verifyDurableTopicSubscriberClosed(indexOfSession, subscriberName);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllDurableTopicSubscribersClosed}
     */
    public void verifyAllDurableTopicSubscribersClosed(int indexOfSession)
    {
        jmsTestModule.verifyAllDurableTopicSubscribersClosed(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberQueueSessions}
     */
    public void verifyNumberQueueSessions(int number)
    {
        jmsTestModule.verifyNumberQueueSessions(number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTopicSessions}
     */
    public void verifyNumberTopicSessions(int number)
    {
        jmsTestModule.verifyNumberTopicSessions(number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTemporaryQueues}
     */
    public void verifyNumberTemporaryQueues(int indexOfSession, int numberQueues)
    {
        jmsTestModule.verifyNumberTemporaryQueues(indexOfSession, numberQueues);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberTemporaryTopics}
     */
    public void verifyNumberTemporaryTopics(int indexOfSession, int numberTopics)
    {
        jmsTestModule.verifyNumberTemporaryTopics(indexOfSession, numberTopics);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTemporaryQueueDeleted}
     */
    public void verifyTemporaryQueueDeleted(int indexOfSession, int indexOfQueue)
    {
        jmsTestModule.verifyTemporaryQueueDeleted(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTemporaryQueuesDeleted}
     */
    public void verifyAllTemporaryQueuesDeleted(int indexOfSession)
    {
        jmsTestModule.verifyAllTemporaryQueuesDeleted(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyTemporaryTopicDeleted}
     */
    public void verifyTemporaryTopicDeleted(int indexOfSession, int indexOfTopic)
    {
        jmsTestModule.verifyTemporaryTopicDeleted(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllTemporaryTopicsDeleted}
     */
    public void verifyAllTemporaryTopicsDeleted(int indexOfSession)
    {
        jmsTestModule.verifyAllTemporaryTopicsDeleted(indexOfSession);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyMessageEquals}
     */
    public void verifyMessageEquals(MockMessage message1, MockMessage message2)
    {
        jmsTestModule.verifyMessageEquals(message1, message2);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCurrentQueueMessageEquals(String, int, MockMessage)}
     */
    public void verifyCurrentQueueMessageEquals(String nameOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyCurrentQueueMessageEquals(nameOfQueue, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedQueueMessageEquals(String, int, MockMessage)}
     */
    public void verifyReceivedQueueMessageEquals(String nameOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageEquals(nameOfQueue, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCurrentQueueMessageEquals(int, int, int, MockMessage)}
     */
    public void verifyCurrentQueueMessageEquals(int indexOfSession, int indexOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyCurrentQueueMessageEquals(indexOfSession, indexOfQueue, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedQueueMessageEquals(int, int, int, MockMessage)}
     */
    public void verifyReceivedQueueMessageEquals(int indexOfSession, int indexOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageEquals(indexOfSession, indexOfQueue, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCurrentQueueMessages(String, int)}
     */
    public void verifyNumberOfCurrentQueueMessages(String nameOfQueue, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfCurrentQueueMessages(nameOfQueue, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfReceivedQueueMessages(String, int)}
     */
    public void verifyNumberOfReceivedQueueMessages(String nameOfQueue, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfReceivedQueueMessages(nameOfQueue, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCurrentQueueMessages(int, int, int)}
     */
    public void verifyNumberOfCurrentQueueMessages(int indexOfSession, int indexOfQueue, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfCurrentQueueMessages(indexOfSession, indexOfQueue, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfReceivedQueueMessages(int, int, int)}
     */
    public void verifyNumberOfReceivedQueueMessages(int indexOfSession, int indexOfQueue, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfReceivedQueueMessages(indexOfSession, indexOfQueue, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllReceivedQueueMessagesAcknowledged(String)}
     */
    public void verifyAllReceivedQueueMessagesAcknowledged(String nameOfQueue)
    {
        jmsTestModule.verifyAllReceivedQueueMessagesAcknowledged(nameOfQueue);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllReceivedQueueMessagesAcknowledged(int, int)}
     */
    public void verifyAllReceivedQueueMessagesAcknowledged(int indexOfSession, int indexOfQueue)
    {
        jmsTestModule.verifyAllReceivedQueueMessagesAcknowledged(indexOfSession, indexOfQueue);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedQueueMessageAcknowledged(String, int)}
     */
    public void verifyReceivedQueueMessageAcknowledged(String nameOfQueue, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageAcknowledged(nameOfQueue, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedQueueMessageNotAcknowledged(String, int)}
     */
    public void verifyReceivedQueueMessageNotAcknowledged(String nameOfQueue, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageNotAcknowledged(nameOfQueue, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedQueueMessageAcknowledged(int, int, int)}
     */
    public void verifyReceivedQueueMessageAcknowledged(int indexOfSession, int indexOfQueue, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageAcknowledged(indexOfSession, indexOfQueue, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedQueueMessageNotAcknowledged(int, int, int)}
     */
    public void verifyReceivedQueueMessageNotAcknowledged(int indexOfSession, int indexOfQueue, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedQueueMessageNotAcknowledged(indexOfSession, indexOfQueue, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedQueueMessages}
     */
    public void verifyNumberOfCreatedQueueMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedQueueBytesMessages}
     */
    public void verifyNumberOfCreatedQueueBytesMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueBytesMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedQueueMapMessages}
     */
    public void verifyNumberOfCreatedQueueMapMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueMapMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedQueueTextMessages}
     */
    public void verifyNumberOfCreatedQueueTextMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueTextMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedQueueStreamMessages}
     */
    public void verifyNumberOfCreatedQueueStreamMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueStreamMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedQueueObjectMessages}
     */
    public void verifyNumberOfCreatedQueueObjectMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedQueueObjectMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueMessageAcknowledged}
     */
    public void verifyCreatedQueueMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueMessageNotAcknowledged}
     */
    public void verifyCreatedQueueMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueBytesMessageAcknowledged}
     */
    public void verifyCreatedQueueBytesMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueBytesMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueBytesMessageNotAcknowledged}
     */
    public void verifyCreatedQueueBytesMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueBytesMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueMapMessageAcknowledged}
     */
    public void verifyCreatedQueueMapMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueMapMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueMapMessageNotAcknowledged}
     */
    public void verifyCreatedQueueMapMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueMapMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueTextMessageAcknowledged}
     */
    public void verifyCreatedQueueTextMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueTextMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueTextMessageNotAcknowledged}
     */
    public void verifyCreatedQueueTextMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueTextMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueStreamMessageAcknowledged}
     */
    public void verifyCreatedQueueStreamMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueStreamMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueStreamMessageNotAcknowledged}
     */
    public void verifyCreatedQueueStreamMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueStreamMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueObjectMessageAcknowledged}
     */
    public void verifyCreatedQueueObjectMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueObjectMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedQueueObjectMessageNotAcknowledged}
     */
    public void verifyCreatedQueueObjectMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedQueueObjectMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCurrentTopicMessageEquals(String, int, MockMessage)}
     */
    public void verifyCurrentTopicMessageEquals(String nameOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyCurrentTopicMessageEquals(nameOfTopic, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedTopicMessageEquals(String, int, MockMessage)}
     */
    public void verifyReceivedTopicMessageEquals(String nameOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageEquals(nameOfTopic, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCurrentTopicMessageEquals(int, int, int, MockMessage)}
     */
    public void verifyCurrentTopicMessageEquals(int indexOfSession, int indexOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyCurrentTopicMessageEquals(indexOfSession, indexOfTopic, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedTopicMessageEquals(int, int, int, MockMessage)}
     */
    public void verifyReceivedTopicMessageEquals(int indexOfSession, int indexOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageEquals(indexOfSession, indexOfTopic, indexOfSourceMessage, targetMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCurrentTopicMessages(String, int)}
     */
    public void verifyNumberOfCurrentTopicMessages(String nameOfTopic, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfCurrentTopicMessages(nameOfTopic, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfReceivedTopicMessages(String, int)}
     */
    public void verifyNumberOfReceivedTopicMessages(String nameOfTopic, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfReceivedTopicMessages(nameOfTopic, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCurrentTopicMessages(int, int, int)}
     */
    public void verifyNumberOfCurrentTopicMessages(int indexOfSession, int indexOfTopic, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfCurrentTopicMessages(indexOfSession, indexOfTopic, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfReceivedTopicMessages(int, int, int)}
     */
    public void verifyNumberOfReceivedTopicMessages(int indexOfSession, int indexOfTopic, int numberOfMessages)
    {
        jmsTestModule.verifyNumberOfReceivedTopicMessages(indexOfSession, indexOfTopic, numberOfMessages);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllReceivedTopicMessagesAcknowledged(String)}
     */
    public void verifyAllReceivedTopicMessagesAcknowledged(String nameOfTopic)
    {
        jmsTestModule.verifyAllReceivedTopicMessagesAcknowledged(nameOfTopic);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyAllReceivedTopicMessagesAcknowledged(int, int)}
     */
    public void verifyAllReceivedTopicMessagesAcknowledged(int indexOfSession, int indexOfTopic)
    {
        jmsTestModule.verifyAllReceivedTopicMessagesAcknowledged(indexOfSession, indexOfTopic);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedTopicMessageAcknowledged(String, int)}
     */
    public void verifyReceivedTopicMessageAcknowledged(String nameOfTopic, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageAcknowledged(nameOfTopic, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedTopicMessageNotAcknowledged(String, int)}
     */
    public void verifyReceivedTopicMessageNotAcknowledged(String nameOfTopic, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageNotAcknowledged(nameOfTopic, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedTopicMessageAcknowledged(int, int, int)}
     */
    public void verifyReceivedTopicMessageAcknowledged(int indexOfSession, int indexOfTopic, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageAcknowledged(indexOfSession, indexOfTopic, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyReceivedTopicMessageNotAcknowledged(int, int, int)}
     */
    public void verifyReceivedTopicMessageNotAcknowledged(int indexOfSession, int indexOfTopic, int indexOfMessage)
    {
        jmsTestModule.verifyReceivedTopicMessageNotAcknowledged(indexOfSession, indexOfTopic, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedTopicMessages}
     */
    public void verifyNumberOfCreatedTopicMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedTopicBytesMessages}
     */
    public void verifyNumberOfCreatedTopicBytesMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicBytesMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedTopicMapMessages}
     */
    public void verifyNumberOfCreatedTopicMapMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicMapMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedTopicTextMessages}
     */
    public void verifyNumberOfCreatedTopicTextMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicTextMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedTopicStreamMessages}
     */
    public void verifyNumberOfCreatedTopicStreamMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicStreamMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyNumberOfCreatedTopicObjectMessages}
     */
    public void verifyNumberOfCreatedTopicObjectMessages(int indexOfSession, int number)
    {
        jmsTestModule.verifyNumberOfCreatedTopicObjectMessages(indexOfSession, number);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicMessageAcknowledged}
     */
    public void verifyCreatedTopicMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicMessageNotAcknowledged}
     */
    public void verifyCreatedTopicMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicBytesMessageAcknowledged}
     */
    public void verifyCreatedTopicBytesMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicBytesMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicBytesMessageNotAcknowledged}
     */
    public void verifyCreatedTopicBytesMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicBytesMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicMapMessageAcknowledged}
     */
    public void verifyCreatedTopicMapMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicMapMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicMapMessageNotAcknowledged}
     */
    public void verifyCreatedTopicMapMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicMapMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicTextMessageAcknowledged}
     */
    public void verifyCreatedTopicTextMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicTextMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicTextMessageNotAcknowledged}
     */
    public void verifyCreatedTopicTextMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicTextMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicStreamMessageAcknowledged}
     */
    public void verifyCreatedTopicStreamMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicStreamMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicStreamMessageNotAcknowledged}
     */
    public void verifyCreatedTopicStreamMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicStreamMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicObjectMessageAcknowledged}
     */
    public void verifyCreatedTopicObjectMessageAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicObjectMessageAcknowledged(indexOfSession, indexOfMessage);
    }

    /**
     * Delegates to {@link JMSTestModule#verifyCreatedTopicObjectMessageNotAcknowledged}
     */
    public void verifyCreatedTopicObjectMessageNotAcknowledged(int indexOfSession, int indexOfMessage)
    {
        jmsTestModule.verifyCreatedTopicObjectMessageNotAcknowledged(indexOfSession, indexOfMessage);
    }
}
