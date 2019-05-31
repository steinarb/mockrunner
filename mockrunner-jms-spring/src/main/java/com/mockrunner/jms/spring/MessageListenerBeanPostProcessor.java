package com.mockrunner.jms.spring;

import java.util.Map.Entry;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * Scans all beans for {@link MessageListener}'s and registers them with MockRunner-JMS
 * 
 * @author the-alchemist
 *
 */
class MessageListenerBeanPostProcessor implements BeanPostProcessor {
    private final ConnectionFactory connectionFactory;
    private final ListableBeanFactory beanFactory;

    public MessageListenerBeanPostProcessor(
            ListableBeanFactory beanFactory,
            ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        this.beanFactory = beanFactory;
    }

    /**
     * Finds the {@link ActivationConfigProperty#propertyName()} that is {@code destination}
     * 
     * Uses the {@link ActivationConfigProperty#propertyValue()} to figure out the name of the queue to use
     * @param bean
     * @return
     */
    private String findQueueNameForMessageListener(Object bean) {
        Class<?> objClz = bean.getClass();
        /*
         * workaround if the bean is a proxy, otherwise the annotations are null
         */
        if (org.springframework.aop.support.AopUtils.isAopProxy(bean)) {

            objClz = org.springframework.aop.support.AopUtils.getTargetClass(bean);
        }
        MessageDriven messageDrivenAnn = AnnotationUtils.getAnnotation(objClz, MessageDriven.class);
        for (ActivationConfigProperty activationConfigProperty : messageDrivenAnn.activationConfig()) {
            if(activationConfigProperty.propertyName().equals("destination")) {
                return activationConfigProperty.propertyValue();
            }
        }
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(MessageListener.class.isAssignableFrom(bean.getClass())) {
            String queueName = findQueueNameForMessageListener(bean);
            if(queueName != null) {
                this.registerTestMessageListenerForQueue(queueName, (MessageListener) bean);
            }
            else {
                throw new IllegalStateException(String.format("type %s does not have the @MessageDriven annotation with @ActivationConfigProperty(propertyName = \"destination\", ....)", bean.getClass()));
            }
        }
        return bean;
    }

    private void registerTestMessageListenerForQueue(String queueName, MessageListener ml) {
        try {
            Connection queueConnection = this.connectionFactory.createConnection();
            Session queueSession = queueConnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Queue queue = getQueue(queueName);
            if(queue == null) {
                throw new IllegalStateException(String.format("No queue registered with name=%s; you sure you registered one with Spring with @Bean(name=\"queueName\"?", queueName));
            } else {
                MessageConsumer messageConsumer = queueSession.createConsumer(queue);
                messageConsumer.setMessageListener(ml);
                queueConnection.start();
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    private Queue getQueue(String queueName) throws JMSException {
         for (Entry<String, Queue> entry : beanFactory.getBeansOfType(Queue.class).entrySet()) {
            if(entry.getValue().getQueueName().equals(queueName)) {
                return entry.getValue();
            }
        }
         return null;
    }
}