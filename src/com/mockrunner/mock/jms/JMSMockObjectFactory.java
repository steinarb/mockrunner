package com.mockrunner.mock.jms;

/**
 * Used to create all types of JMS mock objects. 
 * Maintains the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure, they are all
 * up to date.
 */
public class JMSMockObjectFactory
{
    private MockQueueConnectionFactory queueConnectionFactory;
    private MockQueueConnection queueConnection;
    
    /**
     * Creates a new set of mock objects.
     */
    public JMSMockObjectFactory()
    {
        queueConnectionFactory = new MockQueueConnectionFactory();
        queueConnection = new MockQueueConnection();
        setUpDependencies();
    }
    
    private void setUpDependencies()
    {
        queueConnectionFactory.setQueueConnection(queueConnection);
    }
}
