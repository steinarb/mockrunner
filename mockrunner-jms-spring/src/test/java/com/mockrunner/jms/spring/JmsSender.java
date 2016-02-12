package com.mockrunner.jms.spring;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

@Named
public class JmsSender  {
    
    @Resource(name="queue/fileImport")
    Queue queue;
    @Resource
    ConnectionFactory connectionFactory;
    
    public void sendMessage(String text) throws JMSException {
        Session session = this.connectionFactory.createConnection().createSession(true, Session.AUTO_ACKNOWLEDGE);
        TextMessage textMessage = session.createTextMessage(text);
        session.createProducer(queue).send(textMessage);
    }

}
