package com.mockrunner.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MessageProducer;

import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockMessageProducer;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockSession;
import com.mockrunner.mock.jms.MockTopicPublisher;

/**
 * This class is used to create generic producers that are not
 * associated with a destination.
 * If you create a <code>MessageProducer</code> with a <code>null</code>
 * destination, this class is used to create a <code>MessageProducer</code>
 * which is not associated with any destination.
 * If the session used to create the producer is a <code>QueueSession</code>,
 * you'll get a <code>QueueSender</code>. For a <code>TopicSession</code>,
 * you'll get a <code>TopicPublisher</code>. For a generic session,
 * you'll get a generic instance of <code>MessageProducer</code>.
 */
public class GenericTransmissionManager
{
    private MockConnection connection;
    private MockSession session;
    private List messageProducerList;
    
    public GenericTransmissionManager(MockConnection connection, MockSession session)
    {
        this.connection = connection;
        this.session = session;
        messageProducerList = new ArrayList();
    }

    /**
     * Closes all producers.
     */
    public void closeAll()
    {
        closeAllMessageProducers();
    }

    /**
     * Closes all producers.
     */
    public void closeAllMessageProducers()
    {
        for(int ii = 0; ii < messageProducerList.size(); ii++)
        {
            MessageProducer producer = (MessageProducer)messageProducerList.get(ii);
            try
            {
                producer.close();
            }
            catch(JMSException exc)
            {
            
            }
        }
    }
    
    /**
     * Creates a new <code>MessageProducer</code>.
     * @return the created <code>MessageProducer</code>
     */
    public MockMessageProducer createMessageProducer()
    {
        MockMessageProducer producer = new MockMessageProducer(connection, session, null);
        messageProducerList.add(producer);
        return producer;
    }
    
    /**
     * Creates a new <code>QueueSender</code>.
     * @return the created <code>QueueSender</code>
     */
    public MockQueueSender createQueueSender()
    {
        MockQueueSender producer = new MockQueueSender(connection, session, null);
        messageProducerList.add(producer);
        return producer;
    }
    
    /**
     * Creates a new <code>TopicPublisher</code>.
     * @return the created <code>TopicPublisher</code>
     */
    public MockTopicPublisher createTopicPublisher()
    {
        MockTopicPublisher producer = new MockTopicPublisher(connection, session, null);
        messageProducerList.add(producer);
        return producer;
    }

    /**
     * Returns a <code>MessageProducer</code> by its index or
     * <code>null</code>, if no such <code>MessageProducer</code> is
     * present.
     * @param index the index of the <code>MessageProducer</code>
     * @return the <code>MessageProducer</code>
     */
    public MockMessageProducer getMessageProducer(int index)
    {
        if(messageProducerList.size() <= index || index < 0) return null;
        return (MockMessageProducer)messageProducerList.get(index);
    }
    
    /**
     * Returns the list of all <code>MessageProducer</code> objects.
     * @return the list of <code>MessageProducer</code> objects
     */
    public List getMessageProducerList()
    {
        return Collections.unmodifiableList(messageProducerList);
    }
}
