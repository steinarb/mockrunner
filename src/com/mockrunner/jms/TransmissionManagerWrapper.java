package com.mockrunner.jms;

import java.util.ArrayList;
import java.util.List;

import com.mockrunner.mock.jms.MockMessageConsumer;
import com.mockrunner.mock.jms.MockMessageProducer;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockTopicPublisher;
import com.mockrunner.mock.jms.MockTopicSubscriber;

/**
 * A wrapper around {@link QueueTransmissionManager} and
 * {@link TopicTransmissionManager} and {@link GenericTransmissionManager}. 
 * Can be used to access all senders, publishers, receivers and subscribers
 * transparently.
 */
public class TransmissionManagerWrapper
{ 
    private QueueTransmissionManager queueManager;
    private TopicTransmissionManager topicManager;
    private GenericTransmissionManager genericManager;

    public TransmissionManagerWrapper(QueueTransmissionManager queueManager, TopicTransmissionManager topicManager, GenericTransmissionManager genericManager)
    {
        this.queueManager = queueManager;
        this.topicManager = topicManager;
        this.genericManager = genericManager;
    }

    /**
     * Returns the underlying {@link QueueTransmissionManager}.
     * @return the {@link QueueTransmissionManager}
     */
    public QueueTransmissionManager getQueueTransmissionManager()
    {
        return queueManager;
    }

    /**
     * Returns the underlying {@link TopicTransmissionManager}.
     * @return the {@link TopicTransmissionManager}
     */
    public TopicTransmissionManager getTopicTransmissionManager()
    {
        return topicManager;
    }

    /**
     * Returns the underlying {@link GenericTransmissionManager}.
     * @return the {@link GenericTransmissionManager}
     */
    public GenericTransmissionManager getGenericTransmissionManager() {
        return genericManager;
    }

    /**
     * Returns the {@link com.mockrunner.mock.jms.MockMessageProducer} object
     * with the specified index resp. <code>null</code>, if no such
     * {@link com.mockrunner.mock.jms.MockMessageProducer} exists.
     * @param index the index
     * @return the {@link com.mockrunner.mock.jms.MockMessageProducer} object
     */
    public MockMessageProducer getMessageProducer(int index)
    {
        List messageProducerList = getMessageProducerList();
        if(messageProducerList.size() <= index || index < 0) return null;
        return (MockMessageProducer)messageProducerList.get(index);
    }

    /**
     * Returns a list of all producer objects.
     * @return the list of {@link com.mockrunner.mock.jms.MockMessageProducer} objects
     */
    public List getMessageProducerList()
    {
        List resultList = new ArrayList();
        resultList.addAll(queueManager.getQueueSenderList());
        resultList.addAll(topicManager.getTopicPublisherList());
        resultList.addAll(genericManager.getMessageProducerList());
        return resultList;
    }

    /**
     * Returns a list of all queue senders, i.e. all producer objects, 
     * that are an instance of <code>QueueSender</code> 
     * @return the list of {@link com.mockrunner.mock.jms.MockQueueSener} objects
     */
    public List getQueueSenderList()
    {
        List resultList = new ArrayList();
        resultList.addAll(queueManager.getQueueSenderList());
        List genericList = genericManager.getMessageProducerList();
        for(int ii = 0; ii < genericList.size(); ii++)
        {
            Object next = genericList.get(ii);
            if(next instanceof MockQueueSender)
            {
                resultList.add(next);
            }
        }
        return resultList;
    }

    /**
     * Returns the {@link com.mockrunner.mock.jms.MockQueueSender} object
     * with the specified index resp. <code>null</code>, if no such 
     * {@link com.mockrunner.mock.jms.MockQueueSender} exists. 
     * @param index the index 
     * @return the {@link com.mockrunner.mock.jms.MockQueueSender} object 
     */
    public MockQueueSender getQueueSender(int index)
    {
        List queueSenderList = getQueueSenderList();
        if(queueSenderList.size() <= index || index < 0) return null;
        return (MockQueueSender)queueSenderList.get(index);
    }

    /**
     * Returns a list of all topic publishers, i.e. all producer objects,
     * that are an instance of <code>TopicPublisher</code> 
     * @return the list of {@link com.mockrunner.mock.jms.MockTopicPublisher} objects
     */
    public List getTopicPublisherList()
    {
        List resultList = new ArrayList();
        resultList.addAll(topicManager.getTopicPublisherList());
        List genericList = genericManager.getMessageProducerList();
        for(int ii = 0; ii < genericList.size(); ii++)
        {
            Object next = genericList.get(ii);
            if(next instanceof MockTopicPublisher)
            {
                resultList.add(next);
            }
        }
        return resultList;
    }

    /**
     * Returns the {@link com.mockrunner.mock.jms.MockTopicPublisher} object
     * with the specified index resp. <code>null</code>, if no such
     * {@link com.mockrunner.mock.jms.MockTopicPublisher} exists.
     * @param index the index
     * @return the {@link com.mockrunner.mock.jms.MockTopicPublisher} object
     */
    public MockTopicPublisher getTopicPublisher(int index)
    {
        List topicPublisherList = getTopicPublisherList();
        if(topicPublisherList.size() <= index || index < 0) return null;
        return (MockTopicPublisher)topicPublisherList.get(index);
    }

    /**
     * Returns the {@link com.mockrunner.mock.jms.MockMessageConsumer} object
     * with the specified index resp. <code>null</code>, if no such
     * {@link com.mockrunner.mock.jms.MockMessageConsumer} exists.
     * @param index the index
     * @return the {@link com.mockrunner.mock.jms.MockMessageConsumer} object
     */
    public MockMessageConsumer getMessageConsumer(int index)
    {
        List messageConsumerList = getMessageConsumerList();
        if(messageConsumerList.size() <= index || index < 0) return null;
        return (MockMessageConsumer)messageConsumerList.get(index);
    }

    /**
     * Returns a list of all consumer objects. Includes durable subscribers.
     * @return the list of {@link com.mockrunner.mock.jms.MockMessageConsumer} objects
     */
    public List getMessageConsumerList()
    {
        List resultList = new ArrayList();
        resultList.addAll(queueManager.getQueueReceiverList());
        resultList.addAll(topicManager.getTopicSubscriberList());
        resultList.addAll(topicManager.getDurableTopicSubscriberMap().values());
        return resultList;
    }

    /**
     * Returns a list of all queue receivers, i.e. all receiver objects, 
     * that are an instance of <code>QueueReceiver</code> 
     * @return the list of {@link com.mockrunner.mock.jms.MockQueueReceiver} objects
     */
    public List getQueueReceiverList()
    {
        List resultList = new ArrayList();
        resultList.addAll(queueManager.getQueueReceiverList());
        return resultList;
    }

    /**
     * Returns the {@link com.mockrunner.mock.jms.MockQueueReceiver} object 
     * with the specified index resp. <code>null</code>, if no such
     * {@link com.mockrunner.mock.jms.MockQueueReceiver} exists. 
     * @param index the index 
     * @return the {@link com.mockrunner.mock.jms.MockQueueReceiver} object 
     */
    public MockQueueReceiver getQueueReceiver(int index)
    {
        List queueReceiverList = getQueueReceiverList();
        if(queueReceiverList.size() <= index || index < 0) return null;
        return (MockQueueReceiver)queueReceiverList.get(index);
    }

    /**
     * Returns a list of all topic subscribers, i.e. all subscriber objects,
     * that are an instance of <code>TopicSubscriber</code>
     * @return the list of {@link com.mockrunner.mock.jms.MockTopicSubscriber} objects
     */
    public List getTopicSubscriberList()
    {
        List resultList = new ArrayList();
        resultList.addAll(topicManager.getTopicSubscriberList());
        resultList.addAll(topicManager.getDurableTopicSubscriberMap().values());
        return resultList;
    }

    /**
     * Returns the {@link com.mockrunner.mock.jms.MockTopicSubscriber} object
     * with the specified index resp. <code>null</code>, if no such
     * {@link com.mockrunner.mock.jms.MockTopicSubscriber} exists.
     * @param index the index
     * @return the {@link com.mockrunner.mock.jms.MockTopicSubscriber} object
     */
    public MockTopicSubscriber getTopicSubscriber(int index)
    {
        List topicSubscriberList = getTopicSubscriberList();
        if(topicSubscriberList.size() <= index || index < 0) return null;
        return (MockTopicSubscriber)topicSubscriberList.get(index);
    }
}
