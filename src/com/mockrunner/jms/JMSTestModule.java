package com.mockrunner.jms;

import java.util.List;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.jms.JMSMockObjectFactory;
import com.mockrunner.mock.jms.MockQueue;
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
     * Verifies the number of senders for the specified session.
     * @param indexOfSession the index of the session
     * @param numberOfSenders the expected number of senders
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberQueueSenders(int indexOfSession, int numberOfSenders)
    {
        verifyNumberQueueSessions(indexOfSession + 1);
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
        verifyNumberQueueSessions(indexOfSession + 1);
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(numberOfSenders != manager.getQueueSenderList(queueName).size())
        {
            throw new VerifyFailedException("Expected " + numberOfSenders + " senders for queue " + queueName + ", actually " + manager.getQueueSenderList().size() + " senders present");
        }
    }
    
    /**
     * Verifies that a sender for the specified queue name was
     * created for the specified session.
     * @param indexOfSession the index of the session
     * @param queueName the name of the queue
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSenderPresent(int indexOfSession, String queueName)
    {
        verifyNumberQueueSessions(indexOfSession + 1);
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(null == manager.getQueueSender(queueName))
        {
            throw new VerifyFailedException("A sender for queue " + queueName + " does not exist for session " + indexOfSession);
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
        verifyNumberQueueSessions(indexOfSession + 1);
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
        verifyNumberQueueSessions(indexOfSession + 1);
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(numberOfReceivers != manager.getQueueReceiverList(queueName).size())
        {
            throw new VerifyFailedException("Expected " + numberOfReceivers + " receivers for queue " + queueName + ", actually " + manager.getQueueReceiverList().size() + " receivers present");
        }
    }

    /**
     * Verifies that a receiver for the specified queue name was
     * created for the specified session.
     * @param indexOfSession the index of the session
     * @param queueName the name of the queue
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueReceiverPresent(int indexOfSession, String queueName)
    {
        verifyNumberQueueSessions(indexOfSession + 1);
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(null == manager.getQueueReceiver(queueName))
        {
            throw new VerifyFailedException("A receiver for queue " + queueName + " does not exist for session " + indexOfSession);
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
        verifyNumberQueueSessions(indexOfSession + 1);
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
        verifyNumberQueueSessions(indexOfSession + 1);
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(numberOfBrowsers != manager.getQueueBrowserList(queueName).size())
        {
            throw new VerifyFailedException("Expected " + numberOfBrowsers + " browsers for queue " + queueName + ", actually " + manager.getQueueBrowserList().size() + " browsers present");
        }
    }

    /**
     * Verifies that a browser for the specified queue name was
     * created for the specified session.
     * @param indexOfSession the index of the session
     * @param queueName the name of the queue
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueBrowserPresent(int indexOfSession, String queueName)
    {
        verifyNumberQueueSessions(indexOfSession + 1);
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(null == manager.getQueueBrowser(queueName))
        {
            throw new VerifyFailedException("A browser for queue " + queueName + " does not exist for session " + indexOfSession);
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
        verifyNumberQueueSessions(indexOfSession + 1);
        if(numberQueues != getTemporaryQueueList(indexOfSession).size())
        {
            throw new VerifyFailedException("Expected " + numberQueues + " temporary queues, actually " + getTemporaryQueueList(indexOfSession).size() + " temporary queues present");
        }
    }
    
    /**
     * Verifies the number of messages in a queue.
     * @param nameOfQueue the name of the queue
     * @param numberOfMessages the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberOfCurrentMessages(String nameOfQueue, int numberOfMessages)
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
    public void verifyNumberOfReceivedMessages(String nameOfQueue, int numberOfMessages)
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
    public void verifyNumberOfCurrentMessages(int indexOfSession, int indexOfQueue, int numberOfMessages)
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
    public void verifyNumberOfReceivedMessages(int indexOfSession, int indexOfQueue, int numberOfMessages)
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
    public void verifyNumberOfMessages(int indexOfSession, int number)
    {
        verifyNumberQueueSessions(indexOfSession + 1);
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
    public void verifyNumberOfBytesMessages(int indexOfSession, int number)
    {
        verifyNumberQueueSessions(indexOfSession + 1);
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
    public void verifyNumberOfMapMessages(int indexOfSession, int number)
    {
        verifyNumberQueueSessions(indexOfSession + 1);
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
    public void verifyNumberOfTextMessages(int indexOfSession, int number)
    {
        verifyNumberQueueSessions(indexOfSession + 1);
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
    public void verifyNumberOfStreamMessages(int indexOfSession, int number)
    {
        verifyNumberQueueSessions(indexOfSession + 1);
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
    public void verifyNumberOfObjectMessages(int indexOfSession, int number)
    {
        verifyNumberQueueSessions(indexOfSession + 1);
        if(number != getMessageManager(indexOfSession).getObjectMessageList().size())
        {
            throw new VerifyFailedException("Expected " + number + " object messages, received " + getMessageManager(indexOfSession).getMessageList().size() + " object messages");
        }
    }
}
