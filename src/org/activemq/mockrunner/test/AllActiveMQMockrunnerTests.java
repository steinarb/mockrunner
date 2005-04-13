package org.activemq.mockrunner.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllActiveMQMockrunnerTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for org.codehaus.activemq.mockrunner.test");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(SelectorParserTest.class));
        //$JUnit-END$
        return suite;
    }
}
