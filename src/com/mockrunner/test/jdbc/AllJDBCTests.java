package com.mockrunner.test.jdbc;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllJDBCTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.web.test");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(JDBCTestModuleTest.class));
        suite.addTest(new TestSuite(MockArrayTest.class));
        suite.addTest(new TestSuite(MockBlobTest.class));
        suite.addTest(new TestSuite(MockClobTest.class));
        suite.addTest(new TestSuite(MockResultSetTest.class));
        suite.addTest(new TestSuite(MockStatementTest.class));
        suite.addTest(new TestSuite(FileResultSetFactoryTest.class));
        //$JUnit-END$
        return suite;
    }
}
