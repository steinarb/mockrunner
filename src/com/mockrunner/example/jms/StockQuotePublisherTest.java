package com.mockrunner.example.jms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.MapMessage;
import javax.jms.TextMessage;

import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.jms.JMSTestCaseAdapter;
import com.mockrunner.mock.jms.MockTopic;

/**
 * Example test for {@link StockQuotePublisher}. Demonstrates
 * the usage of {@link com.mockrunner.jms.JMSTestModule}.
 */
public class StockQuotePublisherTest extends JMSTestCaseAdapter
{
    private EJBTestModule ejbModule;
    private MockTopic topic;
    private StockQuotePublisher sender;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        ejbModule = createEJBTestModule();
        ejbModule.bindToContext("java:/ConnectionFactory", getJMSMockObjectFactory().getMockTopicConnectionFactory());
        topic = getDestinationManager().createTopic("testTopic");
        ejbModule.bindToContext("topic/quoteTopic", topic);
        sender = new StockQuotePublisher();
    }
    
    private void prepareTestRates(Map nasdaqRates, Map dowRates)
    {
        nasdaqRates.put("NasdaqCompany1", "10.3");
        nasdaqRates.put("NasdaqCompany2", "30.1");
        dowRates.put("DowJonesCompany1", "11.5");
    }

    public void testClosedAndCommitted()
    {
        sender.send();
        verifyNumberTopicSessions(1);
        verifyNumberTopicPublishers(0, "testTopic", 1);
        verifyAllTopicSessionsClosed();
        verifyAllTopicPublishersClosed(0);
        verifyTopicConnectionClosed();
        verifyAllTopicSessionsCommitted();
        verifyNumberOfReceivedTopicMessages("testTopic", 3);
    }
    
    public void testVerifyReceivedMessages() throws Exception
    {
        Map nasdaqRates = new HashMap();
        Map dowRates = new HashMap();
        prepareTestRates(nasdaqRates, dowRates);
        sender.setQuotes(nasdaqRates, dowRates);
        sender.send();
        List receivedMessages = getReceivedMessageListFromTopic("testTopic");
        TextMessage timeMessage = (TextMessage)receivedMessages.get(0);
        MapMessage nasdaqMessage = (MapMessage)receivedMessages.get(1);
        MapMessage dowMessage = (MapMessage)receivedMessages.get(2);
        assertEquals(timeMessage.getText(), nasdaqMessage.getJMSCorrelationID());
        assertEquals(timeMessage.getText(), dowMessage.getJMSCorrelationID());
        assertEquals("10.3", nasdaqMessage.getString("NasdaqCompany1"));
        assertEquals("30.1", nasdaqMessage.getString("NasdaqCompany2"));
        assertEquals("11.5", dowMessage.getString("DowJonesCompany1"));
    }
}
