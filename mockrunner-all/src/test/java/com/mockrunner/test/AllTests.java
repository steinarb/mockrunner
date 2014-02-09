package com.mockrunner.test;

import org.activemq.mockrunner.test.AllActiveMQMockrunnerTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mockrunner.test.connector.AllConnectorTests;
import com.mockrunner.test.consistency.AllConsistencyTests;
import com.mockrunner.test.ejb.AllEJBTests;
import com.mockrunner.test.gen.AllGenTests;
import com.mockrunner.test.jdbc.AllJDBCTests;
import com.mockrunner.test.jms.AllJMSTests;
import com.mockrunner.test.util.AllUtilTests;
import com.mockrunner.test.web.AllWebTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AllWebTests.class, AllJDBCTests.class, AllEJBTests.class, AllJMSTests.class, AllConnectorTests.class,
	AllUtilTests.class, AllActiveMQMockrunnerTests.class, AllConsistencyTests.class, AllGenTests.class,
	AllExampleTests.class, BaseTestCaseTest.class, ExtendedBaseTestCaseTest.class,
	NestedApplicationExceptionTest.class
})
public class AllTests
{
}
