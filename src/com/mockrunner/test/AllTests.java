package com.mockrunner.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.codehaus.activemq.mockrunner.test.AllActiveMQMockrunnerTests;

import com.mockrunner.test.consistency.AllConsistencyTests;
import com.mockrunner.test.ejb.AllEJBTests;
import com.mockrunner.test.gen.util.AllGenUtilTests;
import com.mockrunner.test.jdbc.AllJDBCTests;
import com.mockrunner.test.jms.AllJMSTests;
import com.mockrunner.test.util.AllUtilTests;
import com.mockrunner.test.web.AllWebTests;

public class AllTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.test");
        //$JUnit-BEGIN$ 
        suite.addTest(AllWebTests.suite());
        suite.addTest(AllJDBCTests.suite());
        suite.addTest(AllEJBTests.suite());
        suite.addTest(AllJMSTests.suite());
        suite.addTest(AllUtilTests.suite());
        suite.addTest(AllActiveMQMockrunnerTests.suite());
        suite.addTest(AllConsistencyTests.suite());
        suite.addTest(AllGenUtilTests.suite());
        suite.addTest(AllExampleTests.suite());
        suite.addTest(new TestSuite(BaseTestCaseTest.class));
        suite.addTest(new TestSuite(NestedApplicationExceptionTest.class));
        //$JUnit-END$
        return suite;
    }
}
