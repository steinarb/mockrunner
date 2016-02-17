package com.mockrunner.jms.spring;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
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
public class MockRunnerJMSConfiguration_SenderTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Configuration
    @Import({MockRunnerJMSConfiguration.class})
    static class MyConfig {
        
        @Bean
        JmsSender sender() {
            return new JmsSender();
        }
        
        @Bean(name="queue/fileImport")
        Queue fakeQueue(DestinationManager dm) {
            return dm.createQueue("queue/fileImport");
        }

    }
    
    @Inject
    JmsSender sender;
    @Inject
    Queue queue;
    @Inject
    JMSTestModule jmsTestModule;
    
    @Test
    public void verifySendingAMessageWorks() throws Exception {
        /*
         * when
         */
        final AtomicReference<Message> messageHolder = new AtomicReference<>();
        this.jmsTestModule.registerTestMessageListenerForQueue(this.queue.getQueueName(), new MessageListener() {
            
            @Override
            public void onMessage(Message message) {
                messageHolder.set(message);
                
            }
        });
        this.sender.sendMessage("does this work?");
        /*
         * then
         */
        assertNotNull(messageHolder.get());
        assertEquals("does this work?", ((TextMessage) messageHolder.get()).getText());
    }

}
