package com.mockrunner.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MessageProducer;

import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockMessageProducer;
import com.mockrunner.mock.jms.MockSession;

/**
 * This class is used to create generic producers.
 * If you use a generic <code>Session</code> and create
 * a <code>MessageProducer</code> with a <code>null</code>
 * destination, you get a generic instance of <code>MessageProducer</code>
 * using this class.
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
     * Returns a <code>MessageProducer</code> by its index resp.
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
