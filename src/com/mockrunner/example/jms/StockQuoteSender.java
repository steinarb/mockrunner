package com.mockrunner.example.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.MapMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.InitialContext;

public class StockQuoteSender
{
    private Map nasdaqRates = new HashMap();
    private Map dowRates = new HashMap();
    
    public void setQuotes(Map nasdaqRates, Map dowRates)
    {
        nasdaqRates.clear();
        dowRates.clear();
        nasdaqRates.putAll(nasdaqRates);
        dowRates.putAll(dowRates);
    }
    
    public void send()
    {
        try
        {   
            InitialContext initialContext = new InitialContext();
            TopicConnectionFactory topicFactory = (TopicConnectionFactory)initialContext.lookup("java:/ConnectionFactory");
            TopicConnection topicConnection = topicFactory.createTopicConnection();
            TopicSession topicSession = topicConnection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
            Topic topic = (Topic)initialContext.lookup("topic/quoteTopic");
            TextMessage dateMessage = createInitialDateMessage();
            MapMessage nasdaqMessage = createStockQuoteMessage(nasdaqRates);
            MapMessage dowMessage = createStockQuoteMessage(dowRates);
            nasdaqMessage.setJMSCorrelationID(dateMessage.getText());
            dowMessage.setJMSCorrelationID(dateMessage.getText());
            TopicPublisher topicPublisher = topicSession.createPublisher(topic);
            topicPublisher.publish(dateMessage);
            topicPublisher.publish(nasdaqMessage);
            topicPublisher.publish(dowMessage);
            topicSession.commit();
            topicPublisher.close();
            topicSession.close();
            topicConnection.close();
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }
    
    private TextMessage createInitialDateMessage()
    {
        return null;   
    }

    private MapMessage createStockQuoteMessage(Map rates)
    {
        return null;
    }
}
