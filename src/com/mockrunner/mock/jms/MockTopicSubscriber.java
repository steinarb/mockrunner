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
    private MockTopic topic;
    private boolean noLocal;
    private String name;
    private boolean isDurable;

    public MockTopicSubscriber(MockConnection connection, MockTopic topic, String messageSelector, boolean noLocal)
    {
        super(connection, messageSelector);
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
     * Returns the name of this subscriber. Usually only durable
     * subscribers have a name. If no name is specified, this
     * method returns <code>null</code>.
     * @return the name of this subscriber
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of this subscriber.
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

    public Message receive() throws JMSException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
