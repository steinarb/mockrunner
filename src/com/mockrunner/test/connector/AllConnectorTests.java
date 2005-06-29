package com.mockrunner.test.connector;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllConnectorTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.test.connector");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(BasicConnectorTestCaseAdapterTest.class));
        suite.addTest(new TestSuite(ConnectorMockObjectFactoryTest.class));
        suite.addTest(new TestSuite(InteractionHandlerTest.class));
        suite.addTest(new TestSuite(MockConnectionFactoryTest.class));
        //$JUnit-END$
        return suite;
    }
}
