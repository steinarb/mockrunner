package de.mockrunner.example.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllExampleTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for de.mockrunner.example.test");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(AuthenticationActionTest.class));
        suite.addTest(new TestSuite(ConstrainedNumericTextTagTest.class));
        //$JUnit-END$
        return suite;
    }
}
