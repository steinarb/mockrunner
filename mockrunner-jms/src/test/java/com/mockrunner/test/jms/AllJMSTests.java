package com.mockrunner.test.jms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	MockConnectionTest.class, MockMessageTest.class, MockTextMessageTest.class,
	MockObjectMessageTest.class, MockBytesMessageTest.class,
	MockStreamMessageTest.class, MockMapMessageTest.class,
	MockQueueSessionTest.class, MockTopicSessionTest.class,
	MockSessionTest.class, MockMessageProducerTest.class,
	MockQueueTest.class, MockTopicTest.class, TransmissionManagerTest.class,
	JMSTestModuleTest.class, MessageSelectorTest.class, MockMessageConsumerTest.class,
	MockDestinationTest.class, BasicJMSTestCaseAdapterTest.class,
	JMSMockObjectFactoryTest.class, MockQueueBrowserTest.class, ConcurrencyTest.class
})
public class AllJMSTests
{
}
