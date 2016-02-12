package com.mockrunner.jms.spring;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.mockrunner.jms.DestinationManager;
import com.mockrunner.jms.JMSTestModule;


@ContextConfiguration
public class MockRunnerJMSConfiguration_ReceiverTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Configuration
    @Import({MockRunnerJMSConfiguration.class})
    static class MyConfig {
        
        @Bean
        JmsReceiver sender() {
            return new JmsReceiver();
        }
        
        @Bean(name="queue/fileImport")
        Queue fakeQueue(DestinationManager dm) {
            return dm.createQueue("queue/fileImport");
        }

    }
    
    @Inject
    JmsReceiver receiver;
    @Inject
    Queue queue;
    @Inject
    JMSTestModule jmsTestModule;
    
    @Test
    public void testVerifyReceivingAMessageWorks() throws Exception {
        /*
         * when
         */
        Session session = this.jmsTestModule.getCurrentQueueConnection().createSession(true, Session.AUTO_ACKNOWLEDGE);
        TextMessage message = session.createTextMessage("does this work?");
        session.createProducer(queue).send(message);
        /*
         * then
         */
        assertEquals("does this work?", ((TextMessage) this.receiver.receivedMessage).getText());
    }

}
