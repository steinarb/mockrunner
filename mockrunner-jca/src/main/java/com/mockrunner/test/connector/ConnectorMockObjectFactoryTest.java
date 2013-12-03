package com.mockrunner.test.connector;

import junit.framework.TestCase;

import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;
import com.mockrunner.mock.connector.cci.MockConnection;
import com.mockrunner.mock.connector.cci.MockConnectionFactory;

public class ConnectorMockObjectFactoryTest extends TestCase
{
    public void testSetup() throws Exception
    {
        ConnectorMockObjectFactory factory = new ConnectorMockObjectFactory();
        MockConnectionFactory connectionFactory = factory.getMockConnectionFactory();
        assertSame(connectionFactory.getConnection(), factory.getMockConnection());
        assertSame(connectionFactory.getMockConnection(), factory.getMockConnection());
        assertSame(factory.getInteractionHandler(), connectionFactory.getMockConnection().getInteractionHandler());
    }
    
    public void testOverrideCreate()
    {
        ConnectorMockObjectFactory factory = new TestConnectorMockObjectFactory();
        assertNotSame(factory.getMockConnection().getClass(), MockConnection.class);
        assertNotSame(factory.getMockConnectionFactory().getClass(), MockConnectionFactory.class);
    }
    
    public static class TestConnectorMockObjectFactory extends ConnectorMockObjectFactory
    {
        public MockConnection createMockConnection()
        {
            return new MockConnection() {};
        }

        public MockConnectionFactory createMockConnectionFactory()
        {
            return new MockConnectionFactory() {};
        }
    }
}
