package com.mockrunner.jms;

import java.util.ArrayList;
import java.util.List;

import com.mockrunner.mock.jms.MockMessageConsumer;
import com.mockrunner.mock.jms.MockMessageProducer;

/**
 * A wrapper around {@link QueueTransmissionManager} and
 * {@link TopicTransmissionManager}. Can be used to
 * access all senders, publishers, receivers and subscribers
 * transparently as producers and consumers.
 */
public class TransmissionManagerWrapper
{ 
    private QueueTransmissionManager queueManager;
    private TopicTransmissionManager topicManager;
    
    public TransmissionManagerWrapper(QueueTransmissionManager queueManager, TopicTransmissionManager topicManager)
    {
        this.queueManager = queueManager;
        this.topicManager = topicManager;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.jms.MockMessageProducer} objects
     * with the specified index resp. <code>null</code>, if no such
     * {@link com.mockrunner.mock.jms.MockMessageProducer} exists.
     * @param index the index
     * @return the {@link com.mockrunner.mock.jms.MockMessageProducer} object
     */
    public MockMessageProducer getMessageProducer(int index)
    {
        if(getMessageProducerList().size() <= index || index < 0) return null;
        return (MockMessageProducer)getMessageProducerList().get(index);
    }

    /**
     * Returns a list of all senders and publishers
     * as {@link com.mockrunner.mock.jms.MockMessageProducer}
     * objects.
     * @return the list of {@link com.mockrunner.mock.jms.MockMessageProducer} objects
     */
    public List getMessageProducerList()
    {
        List resultList = new ArrayList();
        resultList.addAll(queueManager.getQueueSenderList());
        resultList.addAll(topicManager.getTopicPublisherList());
        return resultList;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.jms.MockMessageConsumer} objects
     * with the specified index resp. <code>null</code>, if no such
     * {@link com.mockrunner.mock.jms.MockMessageConsumer} exists.
     * @param index the index
     * @return the {@link com.mockrunner.mock.jms.MockMessageConsumer} object
     */
    public MockMessageConsumer getMessageConsumer(int index)
    {
        if(getMessageConsumerList().size() <= index || index < 0) return null;
        return (MockMessageConsumer)getMessageConsumerList().get(index);
    }

    /**
     * Returns a list of all receivers and subcribers
     * as {@link com.mockrunner.mock.jms.MockMessageConsumer}
     * objects. Includes durable subscribers.
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
}
