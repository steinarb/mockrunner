package com.mockrunner.mock.jms;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;

/**
 * Used to create all types of JMS mock objects. 
 * Maintains the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure that they are all
 * up to date. If you are using JNDI for obtaining the
 * connection factories you have to bind them to the mock JNDI context
 * with {@link com.mockrunner.ejb.EJBTestModule#bindToContext}.
 */
public class JMSMockObjectFactory
{
    private DestinationManager destinationManager;
    private ConfigurationManager configurationManager;
    private MockQueueConnectionFactory queueConnectionFactory;
    private MockTopicConnectionFactory topicConnectionFactory;
    private MockConnectionFactory connectionFactory;
    
    /**
     * Creates a new set of mock objects.
     */
    public JMSMockObjectFactory()
    {
        destinationManager = new DestinationManager();
        configurationManager = new ConfigurationManager();
        queueConnectionFactory = new MockQueueConnectionFactory(destinationManager, configurationManager);
        topicConnectionFactory = new MockTopicConnectionFactory(destinationManager, configurationManager);
        connectionFactory = new MockConnectionFactory(destinationManager, configurationManager);
    }
    
    /**
     * Returns the {@link com.mockrunner.jms.ConfigurationManager}.
     * @return the {@link com.mockrunner.jms.ConfigurationManager}
     */
    public ConfigurationManager getConfigurationManager()
    {
        return configurationManager;
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
    
    /**
     * Returns the {@link com.mockrunner.mock.jms.MockConnectionFactory}.
     * @return the {@link com.mockrunner.mock.jms.MockConnectionFactory}
     */
    public MockConnectionFactory getMockConnectionFactory()
    {
        return connectionFactory;
    }
}
