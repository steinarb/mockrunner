package com.mockrunner.test.jms;

import javax.jms.JMSException;
import javax.jms.Message;

import junit.framework.TestCase;

import org.activemq.filter.mockrunner.Filter;
import org.activemq.selector.mockrunner.SelectorParser;

import com.mockrunner.mock.jms.MockDestination;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockTextMessage;
import com.mockrunner.mock.jms.MockTopic;

public class MockDestinationTest extends TestCase
{
    private Filter filter;

    protected void setUp() throws Exception
    {
        super.setUp();
        SelectorParser parser = new SelectorParser();
        filter = parser.parse("number = 1");
    }
    
    public void testGetMatchingMessagesFromQueue() throws Exception
    {
        MockQueue queue = new MockQueue("Queue");
        doTestGetMatchingMessage(queue);
    }
    
    public void testGetMatchingMessagesFromTopic() throws Exception
    {
        MockTopic topic = new MockTopic("Topic");
        doTestGetMatchingMessage(topic);
    }
    
    private void doTestGetMatchingMessage(MockDestination destination) throws Exception
    {
        MockTextMessage message1 = new MockTextMessage();
        MockTextMessage message2 = new MockTextMessage();
        MockTextMessage message3 = new MockTextMessage();
        message2.setIntProperty("number", 1);
        destination.addMessage(message1);
        destination.addMessage(message2);
        destination.addMessage(message3);
        Message message = destination.getMatchingMessage(filter);
        assertSame(message2, message);
        assertEquals(2, destination.getCurrentMessageList().size());
        message = destination.getMatchingMessage(new TestTrueFilter());
        assertSame(message1, message);
        assertEquals(1, destination.getCurrentMessageList().size());
        message = destination.getMatchingMessage(new TestTrueFilter());
        assertSame(message3, message);
        assertEquals(0, destination.getCurrentMessageList().size());
        assertTrue(destination.isEmpty());
        message1 = new MockTextMessage();
        message2 = new MockTextMessage();
        message3 = new MockTextMessage();
        destination.addMessage(message1);
        destination.addMessage(message2);
        destination.addMessage(message3);
        message = destination.getMatchingMessage(new TestFalseFilter());
        assertNull(message);
        destination.getMessage();
        destination.getMessage();
        destination.getMessage();
        message = destination.getMatchingMessage(new TestTrueFilter());
        assertNull(message);
    }
    
    private static class TestTrueFilter implements Filter
    {
        public boolean matches(Message message) throws JMSException
        {
            return true;
        }
        
        public boolean isWildcard()
        {
            return true;
        }
    }
    
    private static class TestFalseFilter implements Filter
    {
        public boolean matches(Message message) throws JMSException
        {
            return false;
        }
        
        public boolean isWildcard()
        {
            return true;
        }
    }
}
