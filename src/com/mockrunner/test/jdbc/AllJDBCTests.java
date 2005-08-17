package com.mockrunner.test.jdbc;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllJDBCTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.test.jdbc");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(JDBCTestModuleTest.class));
        suite.addTest(new TestSuite(MockArrayTest.class));
        suite.addTest(new TestSuite(MockBlobTest.class));
        suite.addTest(new TestSuite(MockClobTest.class));
        suite.addTest(new TestSuite(MockStructTest.class));
        suite.addTest(new TestSuite(MockResultSetTest.class));
        suite.addTest(new TestSuite(MockStatementTest.class));
        suite.addTest(new TestSuite(FileResultSetFactoryTest.class));
        suite.addTest(new TestSuite(JDBCMockObjectFactoryTest.class));
        suite.addTest(new TestSuite(AbstractResultSetHandlerTest.class));
        suite.addTest(new TestSuite(AbstractParameterResultSetHandlerTest.class));
        suite.addTest(new TestSuite(AbstractOutParameterResultSetHandlerTest.class));
        suite.addTest(new TestSuite(ParameterSetsTest.class));
        suite.addTest(new TestSuite(SQLStatementMatcherTest.class));
        suite.addTest(new TestSuite(MockConnectionTest.class));
        suite.addTest(new TestSuite(MockDatabaseMetaDataTest.class));
        suite.addTest(new TestSuite(ParameterUtilTest.class));
        suite.addTest(new TestSuite(XMLResultSetFactoryTest.class));
        suite.addTest(new TestSuite(BasicJDBCTestCaseAdapterTest.class));
        suite.addTest(new TestSuite(PolyResultSetTest.class));
        suite.addTest(new TestSuite(MockResultSetMetaDataTest.class));
        suite.addTest(new TestSuite(MockParameterMetaDataTest.class));
        suite.addTest(new TestSuite(MockRefTest.class));
        suite.addTest(new TestSuite(ArrayResultSetFactoryTest.class));
        //$JUnit-END$
        return suite;
    }
}
