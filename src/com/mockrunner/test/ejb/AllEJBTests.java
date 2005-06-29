package com.mockrunner.test.ejb;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllEJBTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.test.ejb");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(EJBTestModuleTest.class));
        suite.addTest(new TestSuite(EJBMockObjectFactoryTest.class));
        suite.addTest(new TestSuite(MockUserTransactionTest.class));
        suite.addTest(new TestSuite(EJBTestCaseAdapterTest.class));
        suite.addTest(new TestSuite(BasicEJBTestCaseAdapterTest.class));
        //$JUnit-END$
        return suite;
    }
}
