package com.mockrunner.jms.spring;

import javax.inject.Inject;
import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.jms.JMSTestModule;
import com.mockrunner.mock.jms.JMSMockObjectFactory;

/**
 * Support for using {@link Inject}ing <a href="http://mockrunner.github.io/mockrunner/examplesjms.html">MockRunner</a> mocks into tests
 *
 * @author The-Alchemist
 */
@Configuration
public class MockRunnerJMSConfiguration {
    
    @Bean
    DestinationManager destinationManager() {
        return mockFactory().getDestinationManager();
    }

    @Bean
    ConfigurationManager configurationManager() {
        return mockFactory().getConfigurationManager();
    }
    
    @Bean
    JMSMockObjectFactory mockFactory() {
        return new JMSMockObjectFactory();
    }
    
    @Bean
    JMSTestModule jmsTestModule () {
        return new JMSTestModule(mockFactory());
    }
    
    @Bean
    ConnectionFactory connectionFactory() {
        return mockFactory().getMockQueueConnectionFactory();
    }

    @Bean
    static BeanPostProcessor mdbListener(ListableBeanFactory beanFactory, ConnectionFactory connectionFactory) {
        return new MessageListenerBeanPostProcessor(beanFactory, connectionFactory);
    }
}
