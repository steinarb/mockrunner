package com.mockrunner.test.jms;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.QueueBrowser;
import javax.jms.QueueSender;
import javax.jms.Session;
import javax.jms.TextMessage;

import junit.framework.TestCase;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockBytesMessage;
import com.mockrunner.mock.jms.MockMapMessage;
import com.mockrunner.mock.jms.MockObjectMessage;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueBrowser;
import com.mockrunner.mock.jms.MockQueueConnection;
import com.mockrunner.mock.jms.MockQueueSession;
import com.mockrunner.mock.jms.MockStreamMessage;
import com.mockrunner.mock.jms.MockTextMessage;

public class MockQueueBrowserTest extends TestCase
{
    private MockQueueConnection queueConnection;
    private MockQueueSession queueSession;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        DestinationManager destManager = new DestinationManager();
        ConfigurationManager confManager = new ConfigurationManager();
        queueConnection = new MockQueueConnection(destManager, confManager);
        queueSession = (MockQueueSession)queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
    }
    
    public void testGetEnumerationWithoutMessageSelector() throws Exception
    {
        DestinationManager manager = queueConnection.getDestinationManager();
        manager.createQueue("Queue1");
        MockQueue queue = (MockQueue)queueSession.createQueue("Queue1");
        QueueSender sender = queueSession.createSender(queue);
        sender.send(new MockTextMessage("Text"));
        sender.send(new MockObjectMessage("Object"));
        sender.send(new MockMapMessage());
        sender.send(new MockStreamMessage());
        sender.send(new MockBytesMessage());
        MockQueueBrowser browser = (MockQueueBrowser)queueSession.createBrowser(queue);
        Enumeration messages = browser.getEnumeration();
        TextMessage message1 = (TextMessage)messages.nextElement();
        assertEquals("Text", message1.getText());
        ObjectMessage message2 = (ObjectMessage)messages.nextElement();
        assertEquals("Object", message2.getObject());
        assertTrue(messages.nextElement() instanceof MockMapMessage);
        assertTrue(messages.nextElement() instanceof MockStreamMessage);
        assertTrue(messages.nextElement() instanceof MockBytesMessage);
        assertFalse(messages.hasMoreElements());
        sender.send(new MockTextMessage("Text"));
        sender.send(new MockObjectMessage("Object"));
        browser.close();
        try
        {
            browser.getEnumeration();
            fail();
        }
        catch(JMSException exc)
        {
            //should throw exception
        }
        assertTrue(browser.isClosed());
    }
    
    public void testQueueBrowserWithMessageSelector() throws Exception
    {
        DestinationManager manager = queueConnection.getDestinationManager();
        manager.createQueue("Queue1");
        MockQueue queue = (MockQueue)queueSession.createQueue("Queue1");
        QueueSender sender = queueSession.createSender(queue);
        MockTextMessage message1 = new MockTextMessage("Text1");
        MockTextMessage message2 = new MockTextMessage("Text2");
        MockTextMessage message3 = new MockTextMessage("Text3");
        message1.setIntProperty("number", 24);
        message2.setIntProperty("number", 25);
        message3.setStringProperty("text", "hello");
        sender.send(message1);
        sender.send(message2);
        sender.send(message3);
        QueueBrowser browser = queueSession.createBrowser(queue, "text = 'hello' OR number = 25");
        Enumeration messages = browser.getEnumeration();
        TextMessage filteredMessage1 = (TextMessage)messages.nextElement();
        assertEquals("Text2", filteredMessage1.getText());
        TextMessage filteredMessage2 = (TextMessage)messages.nextElement();
        assertEquals("Text3", filteredMessage2.getText());
        assertFalse(messages.hasMoreElements());
        queueConnection.getConfigurationManager().setUseMessageSelectors(false);
        messages = browser.getEnumeration();
        filteredMessage1 = (TextMessage)messages.nextElement();
        assertEquals("Text1", filteredMessage1.getText());
        filteredMessage2 = (TextMessage)messages.nextElement();
        assertEquals("Text2", filteredMessage2.getText());
        TextMessage filteredMessage3 = (TextMessage)messages.nextElement();
        assertEquals("Text3", filteredMessage3.getText());
        assertFalse(messages.hasMoreElements());
    }
}
