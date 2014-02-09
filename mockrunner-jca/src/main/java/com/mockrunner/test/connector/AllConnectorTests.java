package com.mockrunner.test.connector;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	BasicConnectorTestCaseAdapterTest.class, ConnectorMockObjectFactoryTest.class,
	ConnectorTestModuleTest.class, InteractionHandlerTest.class,
	MockConnectionFactoryTest.class, MockRecordTest.class, MockIndexedRecordTest.class,
	MockMappedRecordTest.class, MockRecordFactoryTest.class, MockConnectionTest.class,
	MockLocalTransactionTest.class, MockResultSetInfoTest.class, MockStreamableByteArrayRecordTest.class,
	StreamableRecordByteArrayInteractionTest.class, MappedRecordInteractionTest.class,
	IndexedRecordInteractionTest.class, GenericFailureInteractionTest.class,
	MockInteractionTest.class
})
public class AllConnectorTests
{
}
