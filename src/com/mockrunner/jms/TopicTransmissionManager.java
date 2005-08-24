package com.mockrunner.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.TopicPublisher;
import javax.jms.TopicSubscriber;

import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockSession;
import com.mockrunner.mock.jms.MockTopic;
import com.mockrunner.mock.jms.MockTopicPublisher;
import com.mockrunner.mock.jms.MockTopicSubscriber;

/**
 * This class is used to create topic publishers and subscribers. 
 * It can be also used to access all created classes in tests.
 */
public class TopicTransmissionManager
{
    private MockConnection connection;
    private MockSession session;
    private List topicPublisherList;
    private List topicSubscriberList;
    private Map topicDurableSubscriberMap;

    public TopicTransmissionManager(MockConnection connection, MockSession session)
    {
        this.connection = connection;
        this.session = session;
        topicPublisherList = new ArrayList();
        topicSubscriberList = new ArrayList();
        topicDurableSubscriberMap = new HashMap();
    }

    /**
     * Closes all senders, receivers, browsers, publishers and subscribers.
     */
    public void closeAll()
    {
        closeAllTopicPublishers();
        closeAllTopicSubscribers();
        closeAllTopicDurableSubscribers();
    }
   
    /**
     * Closes all topic publishers.
     */
    public void closeAllTopicPublishers()
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
    public void closeAllTopicSubscribers()
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
    public void closeAllTopicDurableSubscribers()
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
     * Creates a new <code>TopicPublisher</code> for the specified
     * <code>Topic</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockTopicSession#createPublisher}.
     * @param topic the <code>Topic</code>
     * @return the created <code>TopicPublisher</code>
     */
    public MockTopicPublisher createTopicPublisher(MockTopic topic)
    {
        MockTopicPublisher publisher = new MockTopicPublisher(connection, session, topic);
        topicPublisherList.add(publisher);
        return publisher;
    }

    /**
     * Returns a <code>TopicPublisher</code> by its index or
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
     * @param messageSelector the message selector
     * @param noLocal the no local flag
     * @return the created <code>TopicSubscriber</code>
     */
    public MockTopicSubscriber createTopicSubscriber(MockTopic topic, String messageSelector, boolean noLocal)
    {
        MockTopicSubscriber subscriber = new MockTopicSubscriber(connection, session, topic, messageSelector, noLocal);
        subscriber.setDurable(false);
        topicSubscriberList.add(subscriber);
        return subscriber;
    }

    /**
     * Returns a <code>TopicSubscriber</code> by its index or
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
     * @param name the name of the <code>TopicSubscriber</code>
     * @param messageSelector the message selector
     * @param noLocal the no local flag
     * @return the created <code>TopicSubscriber</code>
     */
    public MockTopicSubscriber createDurableTopicSubscriber(MockTopic topic, String name, String messageSelector, boolean noLocal)
    {
        MockTopicSubscriber subscriber = new MockTopicSubscriber(connection, session, topic, messageSelector, noLocal);
        subscriber.setDurable(true);
        subscriber.setName(name);
        topicDurableSubscriberMap.put(name, subscriber);
        return subscriber;
    }

    /**
     * Returns a durable <code>TopicSubscriber</code> by its name or
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
