package com.mockrunner.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.test");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(ActionTestModuleTest.class));
        suite.addTest(new TestSuite(MockHttpSessionTest.class));
        suite.addTest(new TestSuite(MockObjectFactoryTest.class));
        suite.addTest(com.mockrunner.example.test.AllExampleTests.suite());
        //$JUnit-END$
        return suite;
    }
}
