package com.mockrunner.test.connector;

import com.mockrunner.connector.BasicConnectorTestCaseAdapter;
import com.mockrunner.mock.connector.cci.ConnectorMockObjectFactory;

public class BasicConnectorTestCaseAdapterTest extends BasicConnectorTestCaseAdapter
{
    public void testSetJMSFactory()
    {
        ConnectorMockObjectFactory connectorFactory = new ConnectorMockObjectFactory();
        setConnectorMockObjectFactory(connectorFactory);
        assertSame(connectorFactory, getConnectorMockObjectFactory());
        setConnectorMockObjectFactory(null);
        assertNotNull(getConnectorMockObjectFactory());
        assertNotSame(connectorFactory, getConnectorMockObjectFactory());
    }
}
