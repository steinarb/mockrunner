package com.mockrunner.test.consistency;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllConsistencyTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.test.consistency");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(AdapterConsistencyTest.class));
        suite.addTest(new TestSuite(VersionConsistencyTest.class));
        suite.addTest(new TestSuite(JarFileDependenciesTest.class));
        suite.addTest(new TestSuite(JarReferenceTest.class));
        //$JUnit-END$
        return suite;
    }
}
