package com.mockrunner.jms;

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
     * Verifies that a sender with the specified index was
     * created for the specified session.
     * @param indexOfSession the index of the session
     * @param indexOfSender the index of the sender
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSenderPresent(int indexOfSession, int indexOfSender)
    {
        verifyQueueSessionPresent(indexOfSession);
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(null == manager.getQueueSender(indexOfSender))
        {
            throw new VerifyFailedException("A sender with index " + indexOfSender + " does not exist for session " + indexOfSession);
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
        verifyQueueSessionPresent(indexOfSession);
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(null == manager.getQueueSender(queueName))
        {
            throw new VerifyFailedException("A sender for queue " + queueName + " does not exist for session " + indexOfSession);
        }
    }
    
    /**
     * Verifies that a receiver with the specified index was
     * created for the specified session.
     * @param indexOfSession the index of the session
     * @param indexOfReceiver the index of the receiver
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueReceiverPresent(int indexOfSession, int indexOfReceiver)
    {
        verifyQueueSessionPresent(indexOfSession);
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(null == manager.getQueueReceiver(indexOfReceiver))
        {
            throw new VerifyFailedException("A receiver with index " + indexOfReceiver + " does not exist for session " + indexOfSession);
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
        verifyQueueSessionPresent(indexOfSession);
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(null == manager.getQueueReceiver(queueName))
        {
            throw new VerifyFailedException("A receiver for queue " + queueName + " does not exist for session " + indexOfSession);
        }
    }
    
    /**
     * Verifies that a browser with the specified index was
     * created for the specified session.
     * @param indexOfSession the index of the session
     * @param indexOfBrowser the index of the browser
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueBrowserPresent(int indexOfSession, int indexOfBrowser)
    {
        verifyQueueSessionPresent(indexOfSession);
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(null == manager.getQueueBrowser(indexOfBrowser))
        {
            throw new VerifyFailedException("A browser with index " + indexOfBrowser + " does not exist for session " + indexOfSession);
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
        verifyQueueSessionPresent(indexOfSession);
        TransmissionManager manager = getTransmissionManager(indexOfSession);
        if(null == manager.getQueueBrowser(queueName))
        {
            throw new VerifyFailedException("A browser for queue " + queueName + " does not exist for session " + indexOfSession);
        }
    }
    
    /**
     * Verifies that a session with the specified index is present.
     * @param indexOfSession the index of the session
     * @throws VerifyFailedException if verification fails
     */
    public void verifyQueueSessionPresent(int indexOfSession)
    {
        if(null == getQueueSession(indexOfSession))
        {
            throw new VerifyFailedException("A session with index " + indexOfSession + " does not exist");
        }
    }
    
    /**
     * Verifies that a temporary queue with the specified index is present
     * for the specified session.
     * @param indexOfSession the index of the session
     * @param indexOfQueue the index of the temporary queue
     * @throws VerifyFailedException if verification fails
     */
    public void verifyTemporaryQueuePresent(int indexOfSession, int indexOfQueue)
    {
        if(null == getTemporaryQueue(indexOfSession, indexOfQueue))
        {
            throw new VerifyFailedException("A temporary queue with index " + indexOfQueue + " does not exist");
        }
    }
}
