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
        //$JUnit-END$
        return suite;
    }
}
