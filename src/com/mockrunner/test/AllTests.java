package com.mockrunner.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.activemq.mockrunner.test.AllActiveMQMockrunnerTests;

import com.mockrunner.test.connector.AllConnectorTests;
import com.mockrunner.test.consistency.AllConsistencyTests;
import com.mockrunner.test.ejb.AllEJBTests;
import com.mockrunner.test.gen.AllGenTests;
import com.mockrunner.test.jdbc.AllJDBCTests;
import com.mockrunner.test.jms.AllJMSTests;
import com.mockrunner.test.util.AllUtilTests;
import com.mockrunner.test.web.AllWebTests;

public class AllTests
{
    public static Test suite()
    {
        Logger.getLogger("").setLevel(Level.OFF);
        TestSuite suite = new TestSuite("Test for com.mockrunner.test");
        //$JUnit-BEGIN$ 
        suite.addTest(AllWebTests.suite());
        suite.addTest(AllJDBCTests.suite());
        suite.addTest(AllEJBTests.suite());
        suite.addTest(AllJMSTests.suite());
        suite.addTest(AllConnectorTests.suite());
        suite.addTest(AllUtilTests.suite());
        suite.addTest(AllActiveMQMockrunnerTests.suite());
        suite.addTest(AllConsistencyTests.suite());
        suite.addTest(AllGenTests.suite());
        suite.addTest(AllExampleTests.suite());
        suite.addTest(new TestSuite(BaseTestCaseTest.class));
        suite.addTest(new TestSuite(ExtendedBaseTestCaseTest.class));
        suite.addTest(new TestSuite(NestedApplicationExceptionTest.class));
        //$JUnit-END$
        return suite;
    }
}
