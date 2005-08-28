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
        suite.addTest(new TestSuite(ConnectorTestModuleTest.class));
        suite.addTest(new TestSuite(InteractionHandlerTest.class));
        suite.addTest(new TestSuite(MockConnectionFactoryTest.class));
        suite.addTest(new TestSuite(MockRecordTest.class));
        suite.addTest(new TestSuite(MockIndexedRecordTest.class));
        suite.addTest(new TestSuite(MockMappedRecordTest.class));
        suite.addTest(new TestSuite(MockRecordFactoryTest.class));
        suite.addTest(new TestSuite(MockConnectionTest.class));
        suite.addTest(new TestSuite(MockLocalTransactionTest.class));
        suite.addTest(new TestSuite(MockResultSetInfoTest.class));
        suite.addTest(new TestSuite(MockStreamableByteArrayRecordTest.class));
        suite.addTest(new TestSuite(StreamableRecordByteArrayInteractionTest.class));
        suite.addTest(new TestSuite(MappedRecordInteractionTest.class));
        suite.addTest(new TestSuite(IndexedRecordInteractionTest.class));
        suite.addTest(new TestSuite(GenericFailureInteractionTest.class));
        suite.addTest(new TestSuite(MockInteractionTest.class));
        //$JUnit-END$
        return suite;
    }
}
