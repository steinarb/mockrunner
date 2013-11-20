package com.mockrunner.example.jms;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.jms.JMSTestCaseAdapter;
import com.mockrunner.mock.jms.MockTextMessage;

/**
 * Example test for {@link NewsSubscriber}. Demonstrates
 * message selector usage. Only messages with the <code>subject</code>
 * property <code>Java</code> will be received by {@link NewsSubscriber}.
 * There's one problem with {@link NewsSubscriber}. It blocks the current
 * thread. To get around of this, we can use a simple trick. We call the 
 * main method of {@link NewsSubscriber} from a different thread and interrupt 
 * it immediately. Then we can send messages from the main thread, that are 
 * received synchronously by {@link NewsSubscriber}.
 * Please note, that the JMS test framework is not thread safe at the moment
 * and should not be accessed by multiple threads concurrently. In this test
 * the <code>TestNewsSubscriberThread</code> and the main thread do not run
 * concurrently, so it's ok.
 */
public class NewsSubscriberTest extends JMSTestCaseAdapter
{
	private EJBTestModule ejbModule;
	
	protected void setUp() throws Exception
	{
		super.setUp();
		ejbModule = createEJBTestModule();
		ejbModule.bindToContext("ConnectionFactory", getJMSMockObjectFactory().getMockTopicConnectionFactory());
		Topic topic = getDestinationManager().createTopic("topic");
		ejbModule.bindToContext("topic/newsTopic", topic);
	}
	
	public void testMain() throws Exception
	{
		callMain("Java");
		TopicPublisher publisher = createTestPublisher();
		MockTextMessage message1 = new MockTextMessage("message1");
		message1.setStringProperty("subject", "C++");
		MockTextMessage message2 = new MockTextMessage("message2");
		message2.setStringProperty("subject", "Java");
		publisher.publish(message1);
		publisher.publish(message2);
		verifyNumberOfReceivedTopicMessages("topic", 2);
		verifyNumberOfCurrentTopicMessages("topic", 1);
	}

	private TopicPublisher createTestPublisher() throws JMSException
	{
		TopicConnection connection = getJMSMockObjectFactory().getMockTopicConnectionFactory().createTopicConnection();
		TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		return session.createPublisher(getDestinationManager().getTopic("topic"));
	}
	
	private void callMain(String subject) throws Exception
	{
		String contextFactory = "org.mockejb.jndi.MockContextFactory";
		String providerURL = "org.mockejb.jndi";
		TestNewsSubscriberThread thread = new TestNewsSubscriberThread(contextFactory, providerURL, subject);
		thread.start();
		thread.interrupt();
		thread.join();	
	}

	private class TestNewsSubscriberThread extends Thread
	{
		private String contextFactory;
		private String providerURL;
		private String subject;
		
		public TestNewsSubscriberThread(String contextFactory, String providerURL, String subject)
		{
			this.contextFactory = contextFactory;
			this.providerURL = providerURL;
			this.subject = subject;
		}
		
		public void run()
		{
			try
			{
				NewsSubscriber.main(new String[] {contextFactory, providerURL, subject});
			}
			catch(RuntimeException exc)
			{
				
			}
		}
	}
}
