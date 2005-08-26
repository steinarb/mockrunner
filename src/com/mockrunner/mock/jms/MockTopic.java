package com.mockrunner.mock.jms;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;

/**
 * Mock implementation of JMS <code>Topic</code>.
 */
public class MockTopic extends MockDestination implements Topic
{
    private String name;
    
    public MockTopic(String name)
    {
        this.name = name;
    }
        
    public String getTopicName() throws JMSException
    {
        return name;
    }
    
    /**
     * Adds a message to this <code>Topic</code> that will
     * be propagated to the corresponding receivers.
     * @param message the message
     */
    public void addMessage(Message message) throws JMSException
    {
        addReceivedMessage(message);    
        boolean isConsumed = false;
        Iterator sessionsIterator = sessionSet().iterator();
        while(sessionsIterator.hasNext())
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
                List subscribers = session.getTopicTransmissionManager().getTopicSubscriberList(name);
                for(int ii = 0; ii < subscribers.size(); ii++)
                {
                    MockTopicSubscriber subscriber = (MockTopicSubscriber)subscribers.get(ii);
                    if(subscriber.canConsume(message))
                    {
                        subscriber.receiveMessage(message);
                        isConsumed = true;
                        acknowledgeMessage(message, session);
                    }
                }
                Map durableSubscribers = session.getTopicTransmissionManager().getDurableTopicSubscriberMap(name);
                Iterator keys = durableSubscribers.keySet().iterator();
                while(keys.hasNext())
                {
                    MockTopicSubscriber subscriber = (MockTopicSubscriber)durableSubscribers.get(keys.next());
                    if(subscriber.canConsume(message))
                    {
                        subscriber.receiveMessage(message);
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
