package com.mockrunner.jms;

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

/**
 * Module for JMS tests.
 * Note that all indices are zero based numbers,
 * i.e. the n-th created session, sender etc.
 */
public class JMSTestModule
{
    private JMSMockObjectFactory mockFactory;
  
    public JMSTestModule(JMSMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }
    
    /**
     * Returns the {@link DestinationManager}.
     * @return the {@link DestinationManager}
     */
    public DestinationManager getDestinationManager()
    {
        return mockFactory.getMockQueueConnection().getDestinationManager();
    }

    /**
     * Returns the {@link MessageManager} for the specified session
     * or <code>null<(/code> if the session does not exist.
     * @param indexOfSession the index of the session
     * @return the {@link MessageManager}
     */
    public MessageManager getMessageManager(int indexOfSession)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session) return null;
        return session.getMessageManager();
    }
    
    /**
     * Returns the {@link TransmissionManager} for the specified session
     * or <code>null<(/code> if the session does not exist.
     * @param indexOfSession the index of the session
     * @return the {@link TransmissionManager}
     */
    public TransmissionManager getTransmissionManager(int indexOfSession)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session) return null;
        return session.getTransmissionManager();
    }
    
    /**
     * Returns the list of {@link MockQueueSession} obejcts.
     * @return the {@link MockQueueSession}
     */
    public List getQueueSessionList()
    {
        return mockFactory.getMockQueueConnection().getQueueSessionList();
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
     * Returns the {@link MockQueue} with the specified name
     * or <code>null<(/code> if no such queue exists.
     * @param name the name of the queue
     * @return the {@link MockQueue}
     */
    public MockQueue getQueue(String name)
    {
        return getDestinationManager().getQueue(name);
    }
    
    /**
     * Returns the list of {@link MockTemporaryQueue} objects
     * for the specified session.
     * @param indexOfSession the index of the session
     * @return the {@link MockTemporaryQueue}
     */
    public List getTemporaryQueueList(int indexOfSession)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session) return null;
        return session.getTemporaryQueueList();
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
     * Verifies that the queue connection is closed.
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
     * Verifies that the queue session with the specified index is
     * closed.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSessionClosed(int indexOfSession)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(!session.isCommitted())
        {
            throw new VerifyFailedException("QueueConnection is not committed.");
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(session.isCommitted())
        {
            throw new VerifyFailedException("QueueConnection is committed.");
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(!session.isRolledBack())
        {
            throw new VerifyFailedException("QueueConnection is not rolled back.");
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(session.isRolledBack())
        {
            throw new VerifyFailedException("QueueConnection is rolled back.");
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(!session.isRecovered())
        {
            throw new VerifyFailedException("QueueConnection is not recovered.");
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(session.isRecovered())
        {
            throw new VerifyFailedException("QueueConnection is recovered.");
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
     * Verifies the number of senders for the specified session.
     * @param indexOfSession the index of the session
     * @param numberOfSenders the expected number of senders
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueSenders(int indexOfSession, int numberOfSenders)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        TransmissionManager manager = getTransmissionManager(indexOfSession);
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(numberOfSenders != manager.getQueueSenderList(queueName).size())
        {
            throw new VerifyFailedException("Expected " + numberOfSenders + " senders for queue " + queueName + ", actually " + manager.getQueueSenderList().size() + " senders present");
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        TransmissionManager manager = getTransmissionManager(indexOfSession);
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        TransmissionManager manager = getTransmissionManager(indexOfSession);
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
     * Verifies the number of receivers for the specified session.
     * @param indexOfSession the index of the session
     * @param numberOfReceivers the expected number of receivers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueReceivers(int indexOfSession, int numberOfReceivers)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        TransmissionManager manager = getTransmissionManager(indexOfSession);
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(numberOfReceivers != manager.getQueueReceiverList(queueName).size())
        {
            throw new VerifyFailedException("Expected " + numberOfReceivers + " receivers for queue " + queueName + ", actually " + manager.getQueueReceiverList().size() + " receivers present");
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        List receivers = manager.getQueueReceiverList(queueName);
        if(indexOfReceiver >= receivers.size())
        {
            throw new VerifyFailedException("QueueReceiver with index " + indexOfReceiver + " is not present.");
        }
        MockQueueReceiver receiver = (MockQueueReceiver)manager.getQueueReceiverList(queueName).get(indexOfReceiver);
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        TransmissionManager manager = getTransmissionManager(indexOfSession);
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
     * Verifies the number of browsers for the specified session.
     * @param indexOfSession the index of the session
     * @param numberOfBrowsers the expected number of browsers
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueBrowsers(int indexOfSession, int numberOfBrowsers)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        TransmissionManager manager = getTransmissionManager(indexOfSession);
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(numberOfBrowsers != manager.getQueueBrowserList(queueName).size())
        {
            throw new VerifyFailedException("Expected " + numberOfBrowsers + " browsers for queue " + queueName + ", actually " + manager.getQueueBrowserList().size() + " browsers present");
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        List browsers = manager.getQueueBrowserList(queueName);
        if(indexOfBrowser >= browsers.size())
        {
            throw new VerifyFailedException("QueueBrowser with index " + indexOfBrowser + " is not present.");
        }
        MockQueueBrowser browser = (MockQueueBrowser)manager.getQueueBrowserList(queueName).get(indexOfBrowser);
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        TransmissionManager manager = getTransmissionManager(indexOfSession);
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
     * Verifies the number of sessions.
     * @param number the expected number of sessions
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueSessions(int number)
    {
        if(number != getQueueSessionList().size())
        {
            throw new VerifyFailedException("Expected " + number + " sessions, actually " + getQueueSessionList().size() + " sessions present");
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
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(numberQueues != getTemporaryQueueList(indexOfSession).size())
        {
            throw new VerifyFailedException("Expected " + numberQueues + " temporary queues, actually " + getTemporaryQueueList(indexOfSession).size() + " temporary queues present");
        }
    }
    
    /**
     * Verifies that the temporary queue with the specified index
     * was closed.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the queue
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTemporaryQueueDeleted(int indexOfSession, int indexOfQueue)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        MockTemporaryQueue queue = getTemporaryQueue(indexOfSession, indexOfQueue);
        if(null == queue)
        {
            throw new VerifyFailedException("TemporaryQueue with index " + indexOfQueue + " is not present.");
        }
        if(!queue.isDeleted())
        {
            throw new VerifyFailedException("Temporary queue with index " + indexOfQueue + " not closed.");
        }
    }
    
    /**
     * Verifies that all temporary queues were deleted.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllTemporaryQueuesDeleted(int indexOfSession)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        List queueList = getTemporaryQueueList(indexOfSession);
        for(int ii = 0; ii < queueList.size(); ii++)
        {
            MockTemporaryQueue currentQueue = (MockTemporaryQueue)queueList.get(ii);
            if(!currentQueue.isDeleted())
            {
                throw new VerifyFailedException("Temporary queue with index " + ii + " not closed.");
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
     * All mock messages provide a suitable implementation of mock message.
     * @param nameOfQueue the name of the queue
     * @param indexOfSourceMessage the index of the message in the queue
     * @param targetMessage the message that will be used for comparison
     */
    public void verifyCurrentQueueMessageEquals(String nameOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
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
     * All mock messages provide a suitable implementation of mock message.
     * @param nameOfQueue the name of the queue
     * @param indexOfSourceMessage the index of the received message
     * @param targetMessage the message that will be used for comparison
     */
    public void verifyReceivedQueueMessageEquals(String nameOfQueue, int indexOfSourceMessage, MockMessage targetMessage)
    {
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
     * All mock messages provide a suitable implementation of mock message.
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
     * All mock messages provide a suitable implementation of mock message.
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
     * {@link MockQueueSession#createMessage}
     * @param indexOfSession the index of the session
     * @param number the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfQueueMessages(int indexOfSession, int number)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(number != getMessageManager(indexOfSession).getMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " messages, received " + getMessageManager(indexOfSession).getMessageList().size() + " messages");
        }
    }
    
    /**
     * Verifies the number of bytes messages created with
     * {@link MockQueueSession#createBytesMessage}
     * @param indexOfSession the index of the session
     * @param number the expected number of bytes messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfQueueBytesMessages(int indexOfSession, int number)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(number != getMessageManager(indexOfSession).getBytesMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " bytes messages, received " + getMessageManager(indexOfSession).getMessageList().size() + " bytes messages");
        }
    }
    
    /**
     * Verifies the number of map messages created with
     * {@link MockQueueSession#createMapMessage}
     * @param indexOfSession the index of the session
     * @param number the expected number of map messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfQueueMapMessages(int indexOfSession, int number)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(number != getMessageManager(indexOfSession).getMapMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " map messages, received " + getMessageManager(indexOfSession).getMessageList().size() + " map messages");
        }
    }
    
    /**
     * Verifies the number of text messages created with
     * {@link MockQueueSession#createTextMessage}
     * @param indexOfSession the index of the session
     * @param number the expected number of text messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfQueueTextMessages(int indexOfSession, int number)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(number != getMessageManager(indexOfSession).getTextMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " text messages, received " + getMessageManager(indexOfSession).getMessageList().size() + " text messages");
        }
    }
    
    /**
     * Verifies the number of stream messages created with
     * {@link MockQueueSession#createStreamMessage}
     * @param indexOfSession the index of the session
     * @param number the expected number of stream messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfQueueStreamMessages(int indexOfSession, int number)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(number != getMessageManager(indexOfSession).getStreamMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " stream messages, received " + getMessageManager(indexOfSession).getMessageList().size() + " stream messages");
        }
    }
    
    /**
     * Verifies the number of object messages created with
     * {@link MockQueueSession#createObjectMessage}
     * @param indexOfSession the index of the session
     * @param number the expected number of object messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfQueueObjectMessages(int indexOfSession, int number)
    {
        MockQueueSession session = getQueueSession(indexOfSession);
        if(null == session)
        {
            throw new VerifyFailedException("QueueSession with index " + indexOfSession + " is not present.");
        }
        if(number != getMessageManager(indexOfSession).getObjectMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " object messages, received " + getMessageManager(indexOfSession).getMessageList().size() + " object messages");
        }
    }
}
