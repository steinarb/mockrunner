package com.mockrunner.test.jms;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllJMSTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.jms.test");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(MockQueueConnectionTest.class));
        suite.addTest(new TestSuite(MockMessageTest.class));
        suite.addTest(new TestSuite(MockBytesMessageTest.class));
        suite.addTest(new TestSuite(MockStreamMessageTest.class));
        suite.addTest(new TestSuite(MockMapMessageTest.class));
        suite.addTest(new TestSuite(MockQueueSessionTest.class));
        suite.addTest(new TestSuite(JMSTestModuleTest.class));
        //$JUnit-END$
        return suite;
    }
}
