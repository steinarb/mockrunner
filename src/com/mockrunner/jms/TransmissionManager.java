package com.mockrunner.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.TopicPublisher;
import javax.jms.TopicSubscriber;

import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueBrowser;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicPublisher;
import com.mockrunner.mock.jms.MockTopicSubscriber;

/**
 * This class is used to create queue senders and receivers
 * and topic publishers and subscribers. It can be also used
 * to access all created classes in tests.
 */
public class TransmissionManager
{
    private MockConnection connection;
    private List queueSenderList;
    private List queueReceiverList;
    private List queueBrowserList;
    private List topicPublisherList;
    private List topicSubscriberList;
    private Map topicDurableSubscriberMap;
    
    public TransmissionManager(MockConnection connection)
    {
        this.connection = connection;
        queueSenderList = new ArrayList();
        queueReceiverList = new ArrayList();
        queueBrowserList = new ArrayList();
        topicPublisherList = new ArrayList();
        topicSubscriberList = new ArrayList();
        topicDurableSubscriberMap = new HashMap();
    }
    
    /**
     * Closes all senders, receivers, browsers, publishers and subscribers.
     */
    public void closeAll()
    {
        closeAllQueueSender();
        closeAllQueueReceiver();
        closeAllQueueBrowser();
        closeAllTopicPublisher();
        closeAllTopicSubscriber();
        closeAllTopicDurableSubscriber();
    }
    
    /**
     * Closes all queue senders.
     */
    public void closeAllQueueSender()
    {
        for(int ii = 0; ii < queueSenderList.size(); ii++)
        {
            QueueSender sender = (QueueSender)queueSenderList.get(ii);
            try
            {
                sender.close();
            }
            catch(JMSException exc)
            {
                
            }
        }
    }
    
    /**
     * Closes all queue receivers.
     */
    public void closeAllQueueReceiver()
    {
        for(int ii = 0; ii < queueReceiverList.size(); ii++)
        {
            QueueReceiver receiver = (QueueReceiver)queueReceiverList.get(ii);
            try
            {
                receiver.close();
            }
            catch(JMSException exc)
            {
        
            }
        }
    }
    
    /**
     * Closes all queue browsers.
     */
    public void closeAllQueueBrowser()
    {
        for(int ii = 0; ii < queueBrowserList.size(); ii++)
        {
            QueueBrowser browser = (QueueBrowser)queueBrowserList.get(ii);
            try
            {
                browser.close();
            }
            catch(JMSException exc)
            {

            }
        }
    }
    
    /**
     * Closes all topic publishers.
     */
    public void closeAllTopicPublisher()
    {
        for(int ii = 0; ii < topicPublisherList.size(); ii++)
        {
            TopicPublisher publisher = (TopicPublisher)topicPublisherList.get(ii);
            try
            {
                publisher.close();
            }
            catch(JMSException exc)
            {

            }
        }
    }
    
    /**
     * Closes all topic subscribers.
     */
    public void closeAllTopicSubscriber()
    {
        for(int ii = 0; ii < topicSubscriberList.size(); ii++)
        {
            TopicSubscriber subscriber = (TopicSubscriber)topicSubscriberList.get(ii);
            try
            {
                subscriber.close();
            }
            catch(JMSException exc)
            {

            }
        }
    }
    
    /**
     * Closes all durable topic subscribers.
     */
    public void closeAllTopicDurableSubscriber()
    {
        Iterator keys = topicDurableSubscriberMap.keySet().iterator();
        while(keys.hasNext())
        {
            TopicSubscriber subscriber = (TopicSubscriber)topicDurableSubscriberMap.get(keys.next());
            try
            {
                subscriber.close();
            }
            catch(JMSException exc)
            {

            }
        }
    }
    
    /**
     * Creates a new <code>QueueSender</code> for the specified
     * <code>Queue</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createSender}.
     * @param queue the <code>Queue</code>
     * @return the created <code>QueueSender</code>
     */
    public MockQueueSender createQueueSender(MockQueue queue)
    {
        MockQueueSender sender = new MockQueueSender(connection, queue);
        queueSenderList.add(sender);
        return sender;
    }

    /**
     * Returns a <code>QueueSender</code> by its index resp.
     * <code>null</code>, if no such <code>QueueSender</code> is
     * present.
     * @param index the index of the <code>QueueSender</code>
     * @return the <code>QueueSender</code>
     */
    public MockQueueSender getQueueSender(int index)
    {
        if(queueSenderList.size() <= index || index < 0) return null;
        return (MockQueueSender)queueSenderList.get(index);
    }
    
    /**
     * Returns a <code>QueueSender</code> by the name of its
     * corresponding <code>Queue</code>. If there's more than
     * one <code>QueueSender</code> object for the specified name,
     * the first one will be returned.
     * @param queueName the name of the <code>Queue</code>
     * @return the <code>QueueSender</code>
     */
    public MockQueueSender getQueueSender(String queueName)
    {
        List senders = getQueueSenderList(queueName);
        if(senders.size() <= 0) return null;
        return (MockQueueSender)senders.get(0);
    }
    
    /**
     * Returns the list of the <code>QueueSender</code> objects
     * for a specific <code>Queue</code>.
     * @param queueName the name of the <code>Queue</code>
     * @return the list of <code>QueueSender</code> objects
     */
    public List getQueueSenderList(String queueName)
    {
        List resultList = new ArrayList();
        for(int ii = 0; ii < queueSenderList.size(); ii++)
        {
            QueueSender sender = (QueueSender)queueSenderList.get(ii);
            try
            {
                if(sender.getQueue().getQueueName().equals(queueName))
                {
                    resultList.add(sender);
                }
            }
            catch(JMSException exc)
            {
            
            }
        }
        return Collections.unmodifiableList(resultList);
    }
    
    /**
     * Returns the list of all <code>QueueSender</code> objects.
     * @return the list of <code>QueueSender</code> objects
     */
    public List getQueueSenderList()
    {
        return Collections.unmodifiableList(queueSenderList);
    }

    /**
     * Creates a new <code>QueueReceiver</code> for the specified
     * <code>Queue</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createReceiver}.
     * @param queue the <code>Queue</code>
     * @param messageSelector the message selector
     * @return the created <code>QueueReceiver</code>
     */
    public MockQueueReceiver createQueueReceiver(MockQueue queue, String messageSelector)
    {
        MockQueueReceiver receiver = new MockQueueReceiver(connection, queue, messageSelector);
        queueReceiverList.add(receiver);
        return receiver;
    }

    /**
     * Returns a <code>QueueReceiver</code> by its index resp.
     * <code>null</code>, if no such <code>QueueReceiver</code> is
     * present.
     * @param index the index of the <code>QueueReceiver</code>
     * @return the <code>QueueReceiver</code>
     */
    public MockQueueReceiver getQueueReceiver(int index)
    {
        if(queueReceiverList.size() <= index || index < 0) return null;
        return (MockQueueReceiver)queueReceiverList.get(index);
    }
    
    /**
     * Returns a <code>QueueReceiver</code> by the name of its
     * corresponding <code>Queue</code>. If there's more than
     * one <code>QueueReceiver</code> object for the specified name,
     * the first one will be returned.
     * @param queueName the name of the <code>Queue</code>
     * @return the <code>QueueReceiver</code>
     */
    public MockQueueReceiver getQueueReceiver(String queueName)
    {
        List receivers = getQueueReceiverList(queueName);
        if(receivers.size() <= 0) return null;
        return (MockQueueReceiver)receivers.get(0);
    }

    /**
     * Returns the list of the <code>QueueReceiver</code> objects
     * for a specific <code>Queue</code>.
     * @param queueName the name of the <code>Queue</code>
     * @return the list of <code>QueueReceiver</code> objects
     */
    public List getQueueReceiverList(String queueName)
    {
        List resultList = new ArrayList();
        for(int ii = 0; ii < queueReceiverList.size(); ii++)
        {
            QueueReceiver receiver = (QueueReceiver)queueReceiverList.get(ii);
            try
            {
                if(receiver.getQueue().getQueueName().equals(queueName))
                {
                    resultList.add(receiver);
                }
            }
            catch(JMSException exc)
            {
            
            }
        }
        return Collections.unmodifiableList(resultList);
    }
    
    /**
     * Returns the list of <code>QueueReceiver</code> objects.
     * @return the <code>QueueReceiver</code> list
     */
    public List getQueueReceiverList()
    {
        return Collections.unmodifiableList(queueReceiverList);
    }

    /**
     * Creates a new <code>QueueBrowser</code> for the specified
     * <code>Queue</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createBrowser}.
     * @param queue the <code>Queue</code>
     * @param messageSelector the message selector
     * @return the created <code>QueueBrowser</code>
     */
    public MockQueueBrowser createQueueBrowser(MockQueue queue, String messageSelector)
    {
        MockQueueBrowser browser = new MockQueueBrowser(connection, queue, messageSelector);
        queueBrowserList.add(browser);
        return browser;
    }

    /**
     * Returns a <code>QueueBrowser</code> by its index resp.
     * <code>null</code>, if no such <code>QueueBrowser</code> is
     * present.
     * @param index the index of the <code>QueueBrowser</code>
     * @return the <code>QueueBrowser</code>
     */
    public MockQueueBrowser getQueueBrowser(int index)
    {
        if(queueBrowserList.size() <= index || index < 0) return null;
        return (MockQueueBrowser)queueBrowserList.get(index);
    }
    
    /**
     * Returns a <code>QueueBrowser</code> by the name of its
     * corresponding <code>Queue</code>. If there's more than
     * one <code>QueueBrowser</code> object for the specified name,
     * the first one will be returned.
     * @param queueName the name of the <code>Queue</code>
     * @return the <code>QueueBrowser</code>
     */
    public MockQueueBrowser getQueueBrowser(String queueName)
    {
        List browsers = getQueueBrowserList(queueName);
        if(browsers.size() <= 0) return null;
        return (MockQueueBrowser)browsers.get(0);
    }

    /**
     * Returns the list of the <code>QueueBrowser</code> objects
     * for a specific <code>Queue</code>.
     * @param queueName the name of the <code>Queue</code>
     * @return the list of <code>QueueBrowser</code> objects
     */
    public List getQueueBrowserList(String queueName)
    {
        List resultList = new ArrayList();
        for(int ii = 0; ii < queueBrowserList.size(); ii++)
        {
            QueueBrowser browser = (QueueBrowser)queueBrowserList.get(ii);
            try
            {
                if(browser.getQueue().getQueueName().equals(queueName))
                {
                    resultList.add(browser);
                }
            }
            catch(JMSException exc)
            {
            
            }
        }
        return Collections.unmodifiableList(resultList);
    }
    
    /**
     * Returns the list of <code>QueueBrowser</code> objects.
     * @return the <code>QueueBrowser</code> list
     */
    public List getQueueBrowserList()
    {
        return Collections.unmodifiableList(queueBrowserList);
    }
    
    /**
     * Creates a new <code>TopicPublisher</code> for the specified
     * <code>Topic</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockTopicSession#createPublisher}.
     * @param topic the <code>Topic</code>
     * @return the created <code>TopicPublisher</code>
     */
    public MockTopicPublisher createTopicPublisher(MockTopic topic)
    {
        MockTopicPublisher publisher = new MockTopicPublisher(connection, topic);
        topicPublisherList.add(publisher);
        return publisher;
    }

    /**
     * Returns a <code>TopicPublisher</code> by its index resp.
     * <code>null</code>, if no such <code>TopicPublisher</code> is
     * present.
     * @param index the index of the <code>TopicPublisher</code>
     * @return the <code>TopicPublisher</code>
     */
    public MockTopicPublisher getTopicPublisher(int index)
    {
        if(topicPublisherList.size() <= index || index < 0) return null;
        return (MockTopicPublisher)topicPublisherList.get(index);
    }

    /**
     * Returns a <code>TopicPublisher</code> by the name of its
     * corresponding <code>Topic</code>. If there's more than
     * one <code>TopicPublisher</code> object for the specified name,
     * the first one will be returned.
     * @param topicName the name of the <code>Topic</code>
     * @return the <code>TopicPublisher</code>
     */
    public MockTopicPublisher getTopicPublisher(String topicName)
    {
        List publishers = getTopicPublisherList(topicName);
        if(publishers.size() <= 0) return null;
        return (MockTopicPublisher)publishers.get(0);
    }

    /**
     * Returns the list of the <code>TopicPublisher</code> objects
     * for a specific <code>Topic</code>.
     * @param topicName the name of the <code>Topic</code>
     * @return the list of <code>TopicPublisher</code> objects
     */
    public List getTopicPublisherList(String topicName)
    {
        List resultList = new ArrayList();
        for(int ii = 0; ii < topicPublisherList.size(); ii++)
        {
            TopicPublisher publisher = (TopicPublisher)topicPublisherList.get(ii);
            try
            {
                if(publisher.getTopic().getTopicName().equals(topicName))
                {
                    resultList.add(publisher);
                }
            }
            catch(JMSException exc)
            {
        
            }
        }
        return Collections.unmodifiableList(resultList);
    }

    /**
     * Returns the list of all <code>TopicPublisher</code> objects.
     * @return the list of <code>TopicPublisher</code> objects
     */
    public List getTopicPublisherList()
    {
        return Collections.unmodifiableList(topicPublisherList);
    }
    
    /**
     * Creates a new <code>TopicSubscriber</code> for the specified
     * <code>Topic</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockTopicSession#createSubscriber}.
     * @param topic the <code>Topic</code>
     * @return the created <code>TopicSubscriber</code>
     */
    public MockTopicSubscriber createTopicSubscriber(MockTopic topic, String messageSelector, boolean noLocal)
    {
        MockTopicSubscriber subscriber = new MockTopicSubscriber(connection, topic, messageSelector, noLocal);
        subscriber.setDurable(false);
        topicSubscriberList.add(subscriber);
        return subscriber;
    }

    /**
     * Returns a <code>TopicSubscriber</code> by its index resp.
     * <code>null</code>, if no such <code>TopicSubscriber</code> is
     * present.
     * @param index the index of the <code>TopicSubscriber</code>
     * @return the <code>TopicSubscriber</code>
     */
    public MockTopicSubscriber getTopicSubscriber(int index)
    {
        if(topicSubscriberList.size() <= index || index < 0) return null;
        return (MockTopicSubscriber)topicSubscriberList.get(index);
    }

    /**
     * Returns a <code>TopicSubscriber</code> by the name of its
     * corresponding <code>Topic</code>. If there's more than
     * one <code>TopicSubscriber</code> object for the specified name,
     * the first one will be returned.
     * @param topicName the name of the <code>Topic</code>
     * @return the <code>TopicSubscriber</code>
     */
    public MockTopicSubscriber getTopicSubscriber(String topicName)
    {
        List subscribers = getTopicSubscriberList(topicName);
        if(subscribers.size() <= 0) return null;
        return (MockTopicSubscriber)subscribers.get(0);
    }

    /**
     * Returns the list of the <code>TopicSubscriber</code> objects
     * for a specific <code>Topic</code>.
     * @param topicName the name of the <code>Topic</code>
     * @return the list of <code>TopicSubscriber</code> objects
     */
    public List getTopicSubscriberList(String topicName)
    {
        List resultList = new ArrayList();
        for(int ii = 0; ii < topicSubscriberList.size(); ii++)
        {
            TopicSubscriber subscriber = (TopicSubscriber)topicSubscriberList.get(ii);
            try
            {
                if(subscriber.getTopic().getTopicName().equals(topicName))
                {
                    resultList.add(subscriber);
                }
            }
            catch(JMSException exc)
            {
    
            }
        }
        return Collections.unmodifiableList(resultList);
    }

    /**
     * Returns the list of all <code>TopicSubscriber</code> objects.
     * @return the list of <code>TopicSubscriber</code> objects
     */
    public List getTopicSubscriberList()
    {
        return Collections.unmodifiableList(topicSubscriberList);
    }
    
    /**
     * Creates a new durable <code>TopicSubscriber</code> for the specified
     * <code>Topic</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockTopicSession#createDurableSubscriber}.
     * @param topic the <code>Topic</code>
     * @return the created <code>TopicSubscriber</code>
     */
    public MockTopicSubscriber createDurableTopicSubscriber(MockTopic topic, String name, String messageSelector, boolean noLocal)
    {
        MockTopicSubscriber subscriber = new MockTopicSubscriber(connection, topic, messageSelector, noLocal);
        subscriber.setDurable(true);
        subscriber.setName(name);
        topicDurableSubscriberMap.put(name, subscriber);
        return subscriber;
    }

    /**
     * Returns a durable <code>TopicSubscriber</code> by its name resp.
     * <code>null</code>, if no such durable <code>TopicSubscriber</code> is
     * present.
     * @param name the name of the <code>TopicSubscriber</code>
     * @return the <code>TopicSubscriber</code>
     */
    public MockTopicSubscriber getDurableTopicSubscriber(String name)
    {
        return (MockTopicSubscriber)topicDurableSubscriberMap.get(name);
    }
    
    /**
     * Deletes a durable <code>TopicSubscriber</code>.
     * @param name the name of the <code>TopicSubscriber</code>
     */
    public void removeTopicDurableSubscriber(String name)
    {
        topicDurableSubscriberMap.remove(name);
    }
    
    /**
     * Returns the map of all durable <code>TopicSubscriber</code> objects
     * for a specific <code>Topic</code>.
     * @param topicName the name of the <code>Topic</code>
     * @return the map of <code>TopicSubscriber</code> objects
     */
    public Map getDurableTopicSubscriberMap(String topicName)
    {
        Map resultMap = new HashMap();
        Iterator subscriberNames = topicDurableSubscriberMap.keySet().iterator();
        while(subscriberNames.hasNext())
        {
            Object nextName = subscriberNames.next();
            MockTopicSubscriber subscriber = (MockTopicSubscriber)topicDurableSubscriberMap.get(nextName);
            try
            {
                if(null != subscriber && subscriber.getTopic().getTopicName().equals(topicName))
                {
                    resultMap.put(nextName, subscriber);
                }
            }
            catch(JMSException exc)
            {
                
            }
        }
        return resultMap;
    }

    /**
     * Returns the map of all durable <code>TopicSubscriber</code> objects.
     * @return the map of <code>TopicSubscriber</code> objects
     */
    public Map getDurableTopicSubscriberMap()
    {
        return Collections.unmodifiableMap(topicDurableSubscriberMap);
    }
}
