package com.mockrunner.test.connector;

import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;
import com.mockrunner.mock.connector.cci.MockConnectionFactory;

import junit.framework.TestCase;

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
}
