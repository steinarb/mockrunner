package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

/**
 * Mock implementation of JMS <code>TopicSubscriber</code>.
 */
public class MockTopicSubscriber extends MockMessageConsumer implements TopicSubscriber
{
    private MockSession session;
    private MockTopic topic;
    private boolean noLocal;
    private String name;
    private boolean isDurable;
    
    public MockTopicSubscriber(MockConnection connection, MockSession session, MockTopic topic)
    {
        this(connection, session, topic, null, false);
    }

    public MockTopicSubscriber(MockConnection connection, MockSession session, MockTopic topic, String messageSelector, boolean noLocal)
    {
        super(connection, messageSelector);
        this.session = session;
        this.topic = topic;
        this.noLocal = noLocal;
        name = null;
        isDurable = false;
    }
    
    /**
     * Returns if this subscriber is durable.
     * @return <code>true</code> if this subscriber is durable
     */
    public boolean isDurable()
    {
        return isDurable;
    }
    
    /**
     * Set if this subscriber is durable. This is automatically
     * done when creating the subscriber.
     * @param isDurable is this a durable subscriber?
     */
    public void setDurable(boolean isDurable)
    {
        this.isDurable = isDurable;
    }

    /**
     * Returns the name of the subscription if the subscription
     * is durable. Otherwise, this method returns <code>null</code>.
     * @return the name of this subscriber
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of the subscription.
     * @param name the name of this subscriber
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public Topic getTopic() throws JMSException
    {
        getConnection().throwJMSException();
        return topic;
    }

    public boolean getNoLocal() throws JMSException
    {
        getConnection().throwJMSException();
        return noLocal;
    }

    public Message receiveNoWait() throws JMSException
    {
        getConnection().throwJMSException();
        if(isClosed())
        {
            throw new JMSException("Subscriber is closed");
        }
        if(topic.isEmpty()) return null;
        Message message;
        if((!getConnection().getConfigurationManager().getUseMessageSelectors()) || (null == getMessageFilter()))
        {
            message = topic.getMessage();
        }
        else
        {
            message = topic.getMatchingMessage(getMessageFilter());
        }
        if(null == message) return null;
        if(session.isAutoAcknowledge()) message.acknowledge();
        return message;
    }
    
    protected void waitOnMessage(long timeout)
    {
    	try {
    		synchronized(topic) {
        		//TODO: notify on topic, wait on timeout
    			// this behavior causes a 10 ms poll, as opposed to a wait. 
    			topic.wait(10);
    		}
		} catch (InterruptedException ignored) {
		}
    }
}
