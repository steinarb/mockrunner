package com.mockrunner.jms;

import java.util.Iterator;
import java.util.List;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.jms.JMSMockObjectFactory;
import com.mockrunner.mock.jms.MockMessage;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueBrowser;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockQueueSession;
import com.mockrunner.mock.jms.MockTemporaryQueue;
import com.mockrunner.mock.jms.MockTemporaryTopic;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicPublisher;
import com.mockrunner.mock.jms.MockTopicSession;
import com.mockrunner.mock.jms.MockTopicSubscriber;

/**
 * Module for JMS tests.
 * Note that all indices are zero based.
 */
public class JMSTestModule
{
    private JMSMockObjectFactory mockFactory;
  
    public JMSTestModule(JMSMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }
    
    /**
     * Returns the {@link QueueManager}.
     * @return the {@link QueueManager}
     */
    public QueueManager getQueueManager()
    {
        return mockFactory.getMockQueueConnection().getQueueManager();
    }
    
    /**
     * Returns the {@link TopicManager}.
     * @return the {@link TopicManager}
     */
    public TopicManager getTopicManager()
    {
        return mockFactory.getMockTopicConnection().getTopicManager();
    }

    /**
     * Returns the {@link MessageManager} for the specified session
     * or <code>null<(/code> if the session does not exist. The returned
     * {@link MessageManager} is used to keep track of messages sent
     * to queues.
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
     * or <code>null<(/code> if the session does not exist. The returned
     * {@link MessageManager} is used to keep track of messages sent
     * to topics.
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
     * Returns the {@link QueueTransmissionManager} for the specified session
     * or <code>null<(/code> if the session does not exist.
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
     * or <code>null<(/code> if the session does not exist.
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
     * Returns the list of {@link MockQueueSession} obejcts.
     * @return the {@link MockQueueSession} list
     */
    public List getQueueSessionList()
    {
        return mockFactory.getMockQueueConnection().getQueueSessionList();
    }
    
    /**
     * Returns the list of {@link MockTopicSession} obejcts.
     * @return the {@link MockTopicSession} list
     */
    public List getTopicSessionList()
    {
        return mockFactory.getMockTopicConnection().getTopicSessionList();
    }
    
    /**
     * Returns the {@link MockQueueSession} for the specified index
     * or <code>null<(/code> if the session does not exist.
     * @param indexOfSession the index of the session
     * @return the {@link MockQueueSession}
     */
    public MockQueueSession getQueueSession(int indexOfSession)
    {
        return mockFactory.getMockQueueConnection().getQueueSession(indexOfSession);
    }
    
    /**
     * Returns the {@link MockTopicSession} for the specified index
     * or <code>null<(/code> if the session does not exist.
     * @param indexOfSession the index of the session
     * @return the {@link MockTopicSession}
     */
    public MockTopicSession getTopicSession(int indexOfSession)
    {
        return mockFactory.getMockTopicConnection().getTopicSession(indexOfSession);
    }
    
    /**
     * Returns the {@link MockQueue} with the specified name
     * or <code>null<(/code> if no such queue exists.
     * @param name the name of the queue
     * @return the {@link MockQueue}
     */
    public MockQueue getQueue(String name)
    {
        return getQueueManager().getQueue(name);
    }
    
    /**
     * Returns the {@link MockTopic} with the specified name
     * or <code>null<(/code> if no such topic exists.
     * @param name the name of the topic
     * @return the {@link MockTopic}
     */
    public MockTopic getTopic(String name)
    {
        return getTopicManager().getTopic(name);
    }
    
    /**
     * Returns the list of {@link MockTemporaryQueue} objects
     * for the specified session.
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
     * for the specified session.
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
     * temporary queue exists.
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
     * temporary queue exists.
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
     * resp. <code>null</code> if no such queue exists.
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
     * temporary queue resp. <code>null</code> if no such queue exists.
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
     * resp. <code>null</code> if no such queue exists.
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
     * temporary queue resp. <code>null</code> if no such queue exists.
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
     * resp. <code>null</code> if no such topic exists.
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
     * temporary topic resp. <code>null</code> if no such topic exists.
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
     * resp. <code>null</code> if no such topic exists.
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
     * temporary topic resp. <code>null</code> if no such topic exists.
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
     * Verifies that the queue connection is closed.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueConnectionClosed()
    {
        if(!mockFactory.getMockQueueConnection().isClosed())
        {
            throw new VerifyFailedException("QueueConnection is not closed.");
        }
    }
    
    /**
     * Verifies that the queue connection is started.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueConnectionStarted()
    {
        if(!mockFactory.getMockQueueConnection().isStarted())
        {
            throw new VerifyFailedException("QueueConnection is not started.");
        }
    }
    
    /**
     * Verifies that the queue connection is started.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueConnectionStopped()
    {
        if(!mockFactory.getMockQueueConnection().isStopped())
        {
            throw new VerifyFailedException("QueueConnection is not stopped.");
        }
    }
    
    /**
     * Verifies that the topic connection is closed.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicConnectionClosed()
    {
        if(!mockFactory.getMockTopicConnection().isClosed())
        {
            throw new VerifyFailedException("TopicConnection is not closed.");
        }
    }

    /**
     * Verifies that the topic connection is started.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicConnectionStarted()
    {
        if(!mockFactory.getMockTopicConnection().isStarted())
        {
            throw new VerifyFailedException("TopicConnection is not started.");
        }
    }

    /**
     * Verifies that the topic connection is started.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTopicConnectionStopped()
    {
        if(!mockFactory.getMockTopicConnection().isStopped())
        {
            throw new VerifyFailedException("TopicConnection is not stopped.");
        }
    }
    
    /**
     * Verifies that the queue session with the specified index is
     * closed.
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
     * Verifies that the queue session with the specified index was
     * rolled back.
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
     * Verifies that the queue session with the specified index was
     * recovered.
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
     * Verifies that the topic session with the specified index was
     * rolled back.
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
     * Verifies that the topic session with the specified index was
     * recovered.
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
     * Verifies that all queue sessions are closed.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllQueueSessionsClosed()
    {
        List queueSessions = getQueueSessionList();
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
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllQueueSessionsRecovered()
    {
        List queueSessions = getQueueSessionList();
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
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllQueueSessionsCommitted()
    {
        List queueSessions = getQueueSessionList();
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
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllQueueSessionsRolledBack()
    {
        List queueSessions = getQueueSessionList();
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
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTopicSessionsClosed()
    {
        List topicSessions = getTopicSessionList();
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
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTopicSessionsRecovered()
    {
        List topicSessions = getTopicSessionList();
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
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTopicSessionsCommitted()
    {
        List topicSessions = getTopicSessionList();
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
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTopicSessionsRolledBack()
    {
        List topicSessions = getTopicSessionList();
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
     * Verifies the number of senders for the specified session.
     * @param indexOfSession the index of the session
     * @param numberOfSenders the expected number of senders
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueSenders(int indexOfSession, int numberOfSenders)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        QueueTransmissionManager manager = getQueueTransmissionManager(indexOfSession);
        if(numberOfSenders != manager.getQueueSenderList().size())
        {
            throw new VerifyFailedException("Expected " + numberOfSenders + " senders, actually " + manager.getQueueSenderList().size() + " senders present");
        }
    }
    
    /**
     * Verifies the number of senders for the specified session and
     * the sepcified queue name.
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
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllQueueSendersClosed(int indexOfSession)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        QueueTransmissionManager manager = getQueueTransmissionManager(indexOfSession);
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
     * @param indexOfSession the index of the session
     * @param numberOfPublishers the expected number of publishers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberTopicPublishers(int indexOfSession, int numberOfPublishers)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
        if(numberOfPublishers != manager.getTopicPublisherList().size())
        {
            throw new VerifyFailedException("Expected " + numberOfPublishers + " publishers, actually " + manager.getTopicPublisherList().size() + " publishers present");
        }
    }

    /**
     * Verifies the number of publishers for the specified session and
     * the sepcified topic name.
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
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTopicPublishersClosed(int indexOfSession)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        TopicTransmissionManager manager = getTopicTransmissionManager(indexOfSession);
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
     * Verifies the number of receivers for the specified session.
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
     * the sepcified queue name.
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
     * the sepcified topic name.
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
     * the sepcified queue name.
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
     * the sepcified topic name.
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
     * Verifies the number of temporary queues.
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
     * suitable implementation of mock message.
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
     */
    public void verifyCurrentQueueMessageEquals(String nameOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        QueueManager queueManager = getQueueManager();
        if(null == queueManager.getQueue(nameOfQueue))
        {
            throw new VerifyFailedException("Queue with name " + nameOfQueue + " is not present.");
        }
        List messageList = getCurrentMessageListFromQueue(nameOfQueue);
        if(null == messageList)
        {
            throw new VerifyFailedException("No queue with name " + nameOfQueue + " exists");
        }
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
     */
    public void verifyReceivedQueueMessageEquals(String nameOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
        QueueManager queueManager = getQueueManager();
        if(null == queueManager.getQueue(nameOfQueue))
        {
            throw new VerifyFailedException("Queue with name " + nameOfQueue + " is not present.");
        }
        List messageList = getReceivedMessageListFromQueue(nameOfQueue);
        if(null == messageList)
        {
            throw new VerifyFailedException("No queue with name " + nameOfQueue + " exists");
        }
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
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @param indexOfSourceMessage the index of the message in the queue
     * @param targetMessage the message that will be used for comparison
     */
    public void verifyCurrentQueueMessageEquals(int indexOfSession, int indexOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
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
     * Verifies that a received message is equal to the specified message 
     * by calling the <code>equals()</code> method. 
     * All mock messages provide a suitable implementation of <code>equals()</code>.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @param indexOfSourceMessage the index of the received message
     * @param targetMessage the message that will be used for comparison
     */
    public void verifyReceivedQueueMessageEquals(int indexOfSession, int indexOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
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
        if(null == list)
        {
            throw new VerifyFailedException("No queue with name " + nameOfQueue + " exists");
        }
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
        if(null == list)
        {
            throw new VerifyFailedException("No queue with name " + nameOfQueue + " exists");
        }
        if(numberOfMessages != list.size())
        {
            throw new VerifyFailedException("Expected " + numberOfMessages + " messages received by queue " + nameOfQueue + ", received " + list.size() + " messages");
        }
    }
    
    /**
     * Verifies the number of messages in a temporary queue.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCurrentQueueMessages(int indexOfSession, int indexOfQueue, int numberOfMessages)
    {
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
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfReceivedQueueMessages(int indexOfSession, int indexOfQueue, int numberOfMessages)
    {
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
     * Verifies the number of messages created with
     * {@link MockQueueSession#createMessage}. Only
     * recognizes messages that were sent to queues.
     * @param indexOfSession the index of the session
     * @param number the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfQueueMessages(int indexOfSession, int number)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(number != getQueueMessageManager(indexOfSession).getMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " messages, received " + getQueueMessageManager(indexOfSession).getMessageList().size() + " messages");
        }
    }
    
    /**
     * Verifies the number of bytes messages created with
     * {@link MockQueueSession#createBytesMessage}. Only
     * recognizes messages that were sent to queues.
     * @param indexOfSession the index of the session
     * @param number the expected number of bytes messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfQueueBytesMessages(int indexOfSession, int number)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(number != getQueueMessageManager(indexOfSession).getBytesMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " bytes messages, received " + getQueueMessageManager(indexOfSession).getBytesMessageList().size() + " bytes messages");
        }
    }
    
    /**
     * Verifies the number of map messages created with
     * {@link MockQueueSession#createMapMessage}. Only
     * recognizes messages that were sent to queues.
     * @param indexOfSession the index of the session
     * @param number the expected number of map messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfQueueMapMessages(int indexOfSession, int number)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(number != getQueueMessageManager(indexOfSession).getMapMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " map messages, received " + getQueueMessageManager(indexOfSession).getMapMessageList().size() + " map messages");
        }
    }
    
    /**
     * Verifies the number of text messages created with
     * {@link MockQueueSession#createTextMessage}. Only
     * recognizes messages that were sent to queues.
     * @param indexOfSession the index of the session
     * @param number the expected number of text messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfQueueTextMessages(int indexOfSession, int number)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(number != getQueueMessageManager(indexOfSession).getTextMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " text messages, received " + getQueueMessageManager(indexOfSession).getTextMessageList().size() + " text messages");
        }
    }
    
    /**
     * Verifies the number of stream messages created with
     * {@link MockQueueSession#createStreamMessage}. Only
     * recognizes messages that were sent to queues.
     * @param indexOfSession the index of the session
     * @param number the expected number of stream messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfQueueStreamMessages(int indexOfSession, int number)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(number != getQueueMessageManager(indexOfSession).getStreamMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " stream messages, received " + getQueueMessageManager(indexOfSession).getStreamMessageList().size() + " stream messages");
        }
    }
    
    /**
     * Verifies the number of object messages created with
     * {@link MockQueueSession#createObjectMessage}. Only
     * recognizes messages that were sent to queues.
     * @param indexOfSession the index of the session
     * @param number the expected number of object messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfQueueObjectMessages(int indexOfSession, int number)
    {
        checkAndGetQueueSessionByIndex(indexOfSession);
        if(number != getQueueMessageManager(indexOfSession).getObjectMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " object messages, received " + getQueueMessageManager(indexOfSession).getObjectMessageList().size() + " object messages");
        }
    }
    
    /**
     * Verifies that a message in the specified topic is equal to
     * the specified message by calling the <code>equals()</code> method. 
     * All mock messages provide a suitable implementation of <code>equals()</code>.
     * @param nameOfTopic the name of the topic
     * @param indexOfSourceMessage the index of the message in the topic
     * @param targetMessage the message that will be used for comparison
     */
    public void verifyCurrentTopicMessageEquals(String nameOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        checkTopicByName(nameOfTopic);
        List messageList = getCurrentMessageListFromTopic(nameOfTopic);
        if(null == messageList)
        {
            throw new VerifyFailedException("No topic with name " + nameOfTopic + " exists");
        }
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
     */
    public void verifyReceivedTopicMessageEquals(String nameOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
        checkTopicByName(nameOfTopic);
        List messageList = getReceivedMessageListFromTopic(nameOfTopic);
        if(null == messageList)
        {
            throw new VerifyFailedException("No topic with name " + nameOfTopic + " exists");
        }
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
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topic
     * @param indexOfSourceMessage the index of the message in the topic
     * @param targetMessage the message that will be used for comparison
     */
    public void verifyCurrentTopicMessageEquals(int indexOfSession, int indexOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
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
     * Verifies that a received message is equal to the specified message 
     * by calling the <code>equals()</code> method. 
     * All mock messages provide a suitable implementation of <code>equals()</code>.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topic
     * @param indexOfSourceMessage the index of the received message
     * @param targetMessage the message that will be used for comparison
     */
    public void verifyReceivedTopicMessageEquals(int indexOfSession, int indexOfTopic, int indexOfSourceMessage, MockMessage targetMessage)
    {
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
        if(null == list)
        {
            throw new VerifyFailedException("No topic with name " + nameOfTopic + " exists");
        }
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
        if(null == list)
        {
            throw new VerifyFailedException("No topic with name " + nameOfTopic + " exists");
        }
        if(numberOfMessages != list.size())
        {
            throw new VerifyFailedException("Expected " + numberOfMessages + " messages received by topic " + nameOfTopic + ", received " + list.size() + " messages");
        }
    }

    /**
     * Verifies the number of messages in a temporary topic.
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topic
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCurrentTopicMessages(int indexOfSession, int indexOfTopic, int numberOfMessages)
    {
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
     * @param indexOfSession the index of the session
     * @param indexOfTopic the index of the temporary topiv
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfReceivedTopicMessages(int indexOfSession, int indexOfTopic, int numberOfMessages)
    {
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
     * Verifies the number of messages created with
     * {@link MockTopicSession#createMessage}. Only
     * recognizes messages that were sent to topics.
     * @param indexOfSession the index of the session
     * @param number the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfTopicMessages(int indexOfSession, int number)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(number != getTopicMessageManager(indexOfSession).getMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " messages, received " + getTopicMessageManager(indexOfSession).getMessageList().size() + " messages");
        }
    }

    /**
     * Verifies the number of bytes messages created with
     * {@link MockTopicSession#createBytesMessage}. Only
     * recognizes messages that were sent to topics.
     * @param indexOfSession the index of the session
     * @param number the expected number of bytes messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfTopicBytesMessages(int indexOfSession, int number)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(number != getTopicMessageManager(indexOfSession).getBytesMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " bytes messages, received " + getTopicMessageManager(indexOfSession).getBytesMessageList().size() + " bytes messages");
        }
    }

    /**
     * Verifies the number of map messages created with
     * {@link MockTopicSession#createMapMessage}. Only
     * recognizes messages that were sent to topics.
     * @param indexOfSession the index of the session
     * @param number the expected number of map messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfTopicMapMessages(int indexOfSession, int number)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(number != getTopicMessageManager(indexOfSession).getMapMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " map messages, received " + getTopicMessageManager(indexOfSession).getMapMessageList().size() + " map messages");
        }
    }

    /**
     * Verifies the number of text messages created with
     * {@link MockTopicSession#createTextMessage}. Only
     * recognizes messages that were sent to topics.
     * @param indexOfSession the index of the session
     * @param number the expected number of text messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfTopicTextMessages(int indexOfSession, int number)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(number != getTopicMessageManager(indexOfSession).getTextMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " text messages, received " + getTopicMessageManager(indexOfSession).getTextMessageList().size() + " text messages");
        }
    }

    /**
     * Verifies the number of stream messages created with
     * {@link MockTopicSession#createStreamMessage}. Only
     * recognizes messages that were sent to topics.
     * @param indexOfSession the index of the session
     * @param number the expected number of stream messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfTopicStreamMessages(int indexOfSession, int number)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(number != getTopicMessageManager(indexOfSession).getStreamMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " stream messages, received " + getTopicMessageManager(indexOfSession).getStreamMessageList().size() + " stream messages");
        }
    }

    /**
     * Verifies the number of object messages created with
     * {@link MockTopicSession#createObjectMessage}. Only
     * recognizes messages that were sent to topics.
     * @param indexOfSession the index of the session
     * @param number the expected number of object messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfTopicObjectMessages(int indexOfSession, int number)
    {
        checkAndGetTopicSessionByIndex(indexOfSession);
        if(number != getTopicMessageManager(indexOfSession).getObjectMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " object messages, received " + getTopicMessageManager(indexOfSession).getObjectMessageList().size() + " object messages");
        }
    }
    
    private MockQueueSession checkAndGetQueueSessionByIndex(int indexOfSession)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " does not exist.");
        }
        return session;
    }
    
    private MockTopicSession checkAndGetTopicSessionByIndex(int indexOfSession)
    {
        MockTopicSession session = getTopicSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("TopicSession with index " + indexOfSession + " does not exist.");
        }
        return session;
    }
    
    private void checkQueueByName(String queueName)
    {
        QueueManager queueManager = getQueueManager();
        if(null == queueManager.getQueue(queueName))
        {
            throw new VerifyFailedException("Queue with name " + queueName + " is not present.");
        }
    }
    
    private void checkTopicByName(String topicName)
    {
        TopicManager topicManager = getTopicManager();
        if(null == topicManager.getTopic(topicName))
        {
            throw new VerifyFailedException("Topic with name " + topicName + " is not present.");
        }
    }
}
