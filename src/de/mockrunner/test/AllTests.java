package de.mockrunner.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for de.mockrunner.test");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(ActionTestModuleTest.class));
        suite.addTest(new TestSuite(MockHttpSessionTest.class));
        suite.addTest(de.mockrunner.example.test.AllExampleTests.suite());
        //$JUnit-END$
        return suite;
    }
}
