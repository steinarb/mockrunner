package com.mockrunner.test.jdbc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	JDBCTestModuleTest.class, MockArrayTest.class, MockBlobTest.class,
	MockClobTest.class, MockStructTest.class, MockResultSetTest.class,
	MockStatementTest.class, MockPreparedStatementTest.class, MockCallableStatementTest.class,
	FileResultSetFactoryTest.class, JDBCMockObjectFactoryTest.class, AbstractResultSetHandlerTest.class,
	AbstractParameterResultSetHandlerTest.class, AbstractOutParameterResultSetHandlerTest.class,
	ParameterSetsTest.class, SQLStatementMatcherTest.class, MockConnectionTest.class, MockDatabaseMetaDataTest.class,
	ParameterUtilTest.class, XMLResultSetFactoryTest.class, BasicJDBCTestCaseAdapterTest.class,
	PolyResultSetTest.class, MockResultSetMetaDataTest.class, MockParameterMetaDataTest.class,
	MockRefTest.class, MockRowIdTest.class, MockSQLXMLTest.class, 
	ArrayResultSetFactoryTest.class, StringValuesTableTest.class
})
public class AllJDBCTests
{
}
