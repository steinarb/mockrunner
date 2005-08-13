package com.mockrunner.test.jms;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllJMSTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.test.jms");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(MockConnectionTest.class));
        suite.addTest(new TestSuite(MockMessageTest.class));
        suite.addTest(new TestSuite(MockTextMessageTest.class));
        suite.addTest(new TestSuite(MockObjectMessageTest.class));
        suite.addTest(new TestSuite(MockBytesMessageTest.class));
        suite.addTest(new TestSuite(MockStreamMessageTest.class));
        suite.addTest(new TestSuite(MockMapMessageTest.class));
        suite.addTest(new TestSuite(MockQueueSessionTest.class));
        suite.addTest(new TestSuite(MockTopicSessionTest.class));
        suite.addTest(new TestSuite(MockSessionTest.class));
        suite.addTest(new TestSuite(MockMessageProducerTest.class));
        suite.addTest(new TestSuite(MockQueueTest.class));
        suite.addTest(new TestSuite(MockTopicTest.class));
        suite.addTest(new TestSuite(TransmissionManagerTest.class));
        suite.addTest(new TestSuite(JMSTestModuleTest.class));
        suite.addTest(new TestSuite(MessageSelectorTest.class));
        suite.addTest(new TestSuite(MockMessageConsumerTest.class));
        suite.addTest(new TestSuite(MockDestinationTest.class));
        suite.addTest(new TestSuite(BasicJMSTestCaseAdapterTest.class));
        suite.addTest(new TestSuite(JMSMockObjectFactoryTest.class));
        //$JUnit-END$
        return suite;
    }
}
