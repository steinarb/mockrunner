package com.mockrunner.test.jms;

import junit.framework.TestCase;

import com.mockrunner.mock.jms.JMSMockObjectFactory;
import com.mockrunner.mock.jms.MockConnectionFactory;
import com.mockrunner.mock.jms.MockQueueConnectionFactory;
import com.mockrunner.mock.jms.MockTopicConnectionFactory;

public class JMSMockObjectFactoryTest extends TestCase
{
    public void testSetup() throws Exception
    {
        JMSMockObjectFactory factory = new JMSMockObjectFactory();
        MockConnectionFactory mockFactory = factory.getMockConnectionFactory();
        MockQueueConnectionFactory mockQueueFactory = factory.getMockQueueConnectionFactory();
        MockTopicConnectionFactory mockTopicFactory = factory.getMockTopicConnectionFactory();
        assertNotSame(mockFactory, mockQueueFactory);
        assertNotSame(mockFactory, mockTopicFactory);
        assertNotSame(mockQueueFactory, mockTopicFactory); 
    }
    
    public void testOverrideCreate()
    {
        JMSMockObjectFactory factory = new TestJMSMockObjectFactory();
        assertNotSame(factory.getMockConnectionFactory().getClass(), MockConnectionFactory.class);
        assertNotSame(factory.getMockQueueConnectionFactory().getClass(), MockQueueConnectionFactory.class);
        assertNotSame(factory.getMockTopicConnectionFactory().getClass(), MockTopicConnectionFactory.class);
    }
    
    public static class TestJMSMockObjectFactory extends JMSMockObjectFactory
    {
        public MockConnectionFactory createMockConnectionFactory()
        {
            return new MockConnectionFactory(getDestinationManager(), getConfigurationManager()) {};
        }

        public MockQueueConnectionFactory createMockQueueConnectionFactory()
        {
            return new MockQueueConnectionFactory(getDestinationManager(), getConfigurationManager()) {};
        }

        public MockTopicConnectionFactory createMockTopicConnectionFactory()
        {
            return new MockTopicConnectionFactory(getDestinationManager(), getConfigurationManager()) {};
        }
    }
}
