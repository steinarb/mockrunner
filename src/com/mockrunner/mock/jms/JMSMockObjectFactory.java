package com.mockrunner.mock.jms;

import com.mockrunner.jms.DestinationManager;

/**
 * Used to create all types of JMS mock objects. 
 * Maintains the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure, they are all
 * up to date.
 */
public class JMSMockObjectFactory
{
    private DestinationManager destinationManager;
    private MockQueueConnectionFactory queueConnectionFactory;
    private MockTopicConnectionFactory topicConnectionFactory;
    
    /**
     * Creates a new set of mock objects.
     */
    public JMSMockObjectFactory()
    {
        destinationManager = new DestinationManager();
        queueConnectionFactory = new MockQueueConnectionFactory(destinationManager);
        topicConnectionFactory = new MockTopicConnectionFactory(destinationManager);
    }
    
    /**
     * Returns the {@link com.mockrunner.jms.DestinationManager}.
     * @return the {@link com.mockrunner.jms.DestinationManager}
     */
    public DestinationManager getDestinationManager()
    {
        return destinationManager;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.jms.MockQueueConnectionFactory}.
     * @return the {@link com.mockrunner.mock.jms.MockQueueConnectionFactory}
     */
    public MockQueueConnectionFactory getMockQueueConnectionFactory()
    {
        return queueConnectionFactory;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.jms.MockTopicConnectionFactory}.
     * @return the {@link com.mockrunner.mock.jms.MockTopicConnectionFactory}
     */
    public MockTopicConnectionFactory getMockTopicConnectionFactory()
    {
        return topicConnectionFactory;
    }
}
