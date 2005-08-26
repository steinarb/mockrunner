package com.mockrunner.mock.jms;

import java.util.Iterator;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;

/**
 * Mock implementation of JMS <code>Queue</code>.
 */
public class MockQueue extends MockDestination implements Queue
{
    private String name;
    
    public MockQueue(String name)
    {
        this.name = name;
    }
    
    public String getQueueName() throws JMSException
    {
        return name;
    }
    
    /**
     * Adds a message to this <code>Queue</code> that will
     * be propagated to the corresponding receiver. Only one
     * receiver will get the message. The order is not
     * predictable.
     * @param message the message
     */
    public void addMessage(Message message) throws JMSException
    {
        addReceivedMessage(message);    
        boolean isConsumed = false;
        Iterator sessionsIterator = sessionSet().iterator();
        while(sessionsIterator.hasNext() && !isConsumed)
        {
            MockSession session = (MockSession)sessionsIterator.next();
            MessageListener globalListener = session.getMessageListener();
            if(null != globalListener)
            {
                globalListener.onMessage(message);
                isConsumed = true;
                acknowledgeMessage(message, session);
            }
            else
            {
                List receivers = session.getQueueTransmissionManager().getQueueReceiverList(name);
                for(int ii = 0; ii < receivers.size() && !isConsumed; ii++)
                {
                    MockQueueReceiver receiver = (MockQueueReceiver)receivers.get(ii);
                    if(receiver.canConsume(message))
                    {
                        receiver.receiveMessage(message);
                        isConsumed = true;
                        acknowledgeMessage(message, session);
                    }
                }
            }
        }
        if(!isConsumed)
        {
            addCurrentMessage(message);
        }
    }
}
