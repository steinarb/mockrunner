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
    private MockQueueConnection queueConnection;
    private MockTopicConnectionFactory topicConnectionFactory;
    private MockTopicConnection topicConnection;
    
    /**
     * Creates a new set of mock objects.
     */
    public JMSMockObjectFactory()
    {
        destinationManager = new DestinationManager();
        queueConnectionFactory = new MockQueueConnectionFactory();
        queueConnection = new MockQueueConnection(destinationManager);
        topicConnectionFactory = new MockTopicConnectionFactory();
        topicConnection = new MockTopicConnection(destinationManager);
        setUpDependencies();
    }
    
    private void setUpDependencies()
    {
        queueConnectionFactory.setQueueConnection(queueConnection);
        topicConnectionFactory.setTopicConnection(topicConnection);
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
     * Returns the {@link com.mockrunner.mock.jms.MockQueueConnection}.
     * @return the {@link com.mockrunner.mock.jms.MockQueueConnection}
     */
    public MockQueueConnection getMockQueueConnection()
    {
        return queueConnection;
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
     * Returns the {@link com.mockrunner.mock.jms.MockTopicConnection}.
     * @return the {@link com.mockrunner.mock.jms.MockTopicConnection}
     */
    public MockTopicConnection getMockTopicConnection()
    {
        return topicConnection;
    }

    /**
     * Returns the {@link com.mockrunner.mock.jms.MockTopicConnectionFactory}.
     * @return the {@link com.mockrunner.mock.jms.MockTopicConnectionFactory}
     */
    public MockQueueConnectionFactory getMockTopicConnectionFactory()
    {
        return queueConnectionFactory;
    }
}
