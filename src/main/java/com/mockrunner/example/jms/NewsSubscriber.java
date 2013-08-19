package com.mockrunner.example.jms;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Simple class that connects to a message server and dumps all
 * messages to stdout. The first command line parameter is
 * the INITIAL_CONTEXT_FACTORY of the server, the second one
 * is the PROVIDER_URL. With the third parameter, received messages
 * can be limited to those, that match a specified subject (i.e.
 * a string property with the name <code>subject</code>).
 */
public class NewsSubscriber
{
	private InitialContext context;
	private String subject;
	
	public static void main(String[] args)
	{
		if(args.length < 3)
		{
			throw new RuntimeException("Please specify INITIAL_CONTEXT_FACTORY (first parameter) " + 
			                           "PROVIDER_URL (second parameter) " +
									   "subject (third parameter)");
		}
		try
		{
			new NewsSubscriber(args[0], args[1], args[2]).init();
		}
		catch(Exception exc)
		{
			throw new RuntimeException(exc.getMessage());
		}
	}
	
	public NewsSubscriber(String contextFactory, String providerURL, String subject) throws NamingException
	{
		this.subject = subject;
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
		env.put(Context.PROVIDER_URL, subject);
		context = new InitialContext(env);
	}

	public void init() throws Exception
	{
		TopicConnectionFactory topicFactory = (TopicConnectionFactory)context.lookup("ConnectionFactory");
		TopicConnection topicConnection = topicFactory.createTopicConnection();
		TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = (Topic)context.lookup("topic/newsTopic");
		TopicSubscriber subscriber = topicSession.createSubscriber(topic, "subject = '" + subject + "'", false);
		subscriber.setMessageListener(new InternalSubscriber());
		topicConnection.start();
		Thread.currentThread().join();
	}
	
	private static class InternalSubscriber implements MessageListener
	{
		public void onMessage(Message message)
		{
			if(message instanceof TextMessage)
			{
				try
				{
					System.out.println(((TextMessage)message).getText());
				}
				catch(JMSException exc)
				{
					exc.printStackTrace();
				}
			}
		}
	}
}
