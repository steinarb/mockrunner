package com.mockrunner.test.jms;

import javax.jms.Destination;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;

import junit.framework.TestCase;

import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockMessageProducer;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockTextMessage;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicConnection;
import com.mockrunner.mock.jms.MockTopicPublisher;

public class MockMessageProducerTest extends TestCase
{
    private MockQueue queue;
    private MockTopic topic;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        queue = new MockQueue("Queue");
        topic = new MockTopic("Topic");
    }
  
    public void testSendWithQueueSender() throws Exception
    {
        DestinationManager manager = new DestinationManager();
        MockQueueSender sender = new MockQueueSender(new MockQueueConnection(manager), queue);  
        testSendMessage(sender);
        assertEquals(1, queue.getCurrentMessageList().size());
        assertEquals(1, queue.getReceivedMessageList().size());
        assertEquals(0, topic.getCurrentMessageList().size());
        assertEquals(0, topic.getReceivedMessageList().size());
        assertEquals(new MockTextMessage("aMessage"), queue.getMessage());
        testSendWithInvalidParameters(sender);
    }
    
    public void testSendWithTopicPublisher() throws Exception
    {
        DestinationManager manager = new DestinationManager();
        MockTopicPublisher publisher = new MockTopicPublisher(new MockTopicConnection(manager), topic);
        testSendMessage(publisher);
        assertEquals(1, topic.getCurrentMessageList().size());
        assertEquals(1, topic.getReceivedMessageList().size());
        assertEquals(0, queue.getCurrentMessageList().size());
        assertEquals(0, queue.getReceivedMessageList().size());
        assertEquals(new MockTextMessage("aMessage"), topic.getMessage());
        testSendWithInvalidParameters(publisher);
    }
    
    private void testSendWithInvalidParameters(MockMessageProducer producer) throws Exception
    {
        try
        {
            producer.send(null, null);
            fail();
        }
        catch(InvalidDestinationException exc)
        {
            //should throw exception
        }
        try
        {
            producer.send(new Destination(){}, new MockTextMessage(""));
            fail();
        }
        catch(InvalidDestinationException exc)
        {
            //should throw exception
        }
        producer.close();
        try
        {
            producer.send(new MockTextMessage("aMessage"));
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
    }
    
    private void testSendMessage(MockMessageProducer producer) throws Exception
    {
        producer.send(new MockTextMessage("aMessage"));
    }
}
