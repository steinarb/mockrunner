package com.mockrunner.jms;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;

import com.mockrunner.mock.jms.MockBytesMessage;
import com.mockrunner.mock.jms.MockMapMessage;
import com.mockrunner.mock.jms.MockMessage;
import com.mockrunner.mock.jms.MockObjectMessage;
import com.mockrunner.mock.jms.MockStreamMessage;
import com.mockrunner.mock.jms.MockTextMessage;

/**
 * This class is used to keep track of messages that are
 * created using sessions and topics.
 */
public class MessageManager
{
    private List messages;
    private List byteMessages;
    private List mapMessages;
    private List textMessages;
    private List streamMessages;
    private List objectMessages;
    
    public MessageManager()
    {
        messages = new ArrayList();
        byteMessages = new ArrayList();
        mapMessages = new ArrayList();
        textMessages = new ArrayList();
        streamMessages = new ArrayList();
        objectMessages = new ArrayList();
    }

    /**
     * Creates a new <code>Message</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createMessage}.
     * @return the created <code>Message</code>
     */
    public MockMessage createMessage()
    {
        MockMessage message = new MockMessage();
        messages.add(message);
        return message;
    }
    
    /**
     * Returns a <code>Message</code> by its index resp.
     * <code>null</code>, if no such <code>Message</code> is
     * present.
     * @param index the index of the <code>Message</code>
     * @return the <code>Message</code>
     */
    public MockMessage getMessage(int index)
    {
        if(messages.size() <= index || index < 0) return null;
        return (MockMessage)messages.get(index);
    }
    
    /**
     * Creates a new <code>BytesMessage</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createBytesMessage}.
     * @return the created <code>BytesMessage</code>
     */
    public MockBytesMessage createBytesMessage()
    {
        MockBytesMessage message = new MockBytesMessage();
        byteMessages.add(message);
        return message;
    }

    /**
     * Returns a <code>BytesMessage</code> by its index resp.
     * <code>null</code>, if no such <code>BytesMessage</code> is
     * present.
     * @param index the index of the <code>BytesMessage</code>
     * @return the <code>BytesMessage</code>
     */
    public MockBytesMessage getBytesMessage(int index)
    {
        if(byteMessages.size() <= index || index < 0) return null;
        return (MockBytesMessage)byteMessages.get(index);
    }
    
    /**
     * Creates a new <code>MapMessage</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createMapMessage}.
     * @return the created <code>MapMessage</code>
     */
    public MockMapMessage createMapMessage()
    {
        MockMapMessage message = new MockMapMessage();
        mapMessages.add(message);
        return message;
    }

    /**
     * Returns a <code>MapMessage</code> by its index resp.
     * <code>null</code>, if no such <code>MapMessage</code> is
     * present.
     * @param index the index of the <code>MapMessage</code>
     * @return the <code>MapMessage</code>
     */
    public MockMapMessage getMapMessage(int index)
    {
        if(mapMessages.size() <= index || index < 0) return null;
        return (MockMapMessage)mapMessages.get(index);
    }
    
    /**
     * Creates a new <code>TextMessage</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createTextMessage}.
     * @return the created <code>TextMessage</code>
     */
    public MockTextMessage createTextMessage(String text)
    {
        MockTextMessage message = new MockTextMessage();
        try
        {
            message.setText(text);
        }
        catch(JMSException exc)
        {
            
        }
        textMessages.add(message);
        return message;
    }

    /**
     * Returns a <code>TextMessage</code> by its index resp.
     * <code>null</code>, if no such <code>TextMessage</code> is
     * present.
     * @param index the index of the <code>TextMessage</code>
     * @return the <code>TextMessage</code>
     */
    public MockTextMessage getTextMessage(int index)
    {
        if(textMessages.size() <= index || index < 0) return null;
        return (MockTextMessage)textMessages.get(index);
    }
    
    /**
     * Creates a new <code>StreamMessage</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createStreamMessage}.
     * @return the created <code>StreamMessage</code>
     */
    public MockStreamMessage createStreamMessage()
    {
        MockStreamMessage message = new MockStreamMessage();
        streamMessages.add(message);
        return message;
    }

    /**
     * Returns a <code>StreamMessage</code> by its index resp.
     * <code>null</code>, if no such <code>StreamMessage</code> is
     * present.
     * @param index the index of the <code>StreamMessage</code>
     * @return the <code>StreamMessage</code>
     */
    public MockStreamMessage getStreamMessage(int index)
    {
        if(streamMessages.size() <= index || index < 0) return null;
        return (MockStreamMessage)streamMessages.get(index);
    }
    
    /**
     * Creates a new <code>ObjectMessage</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createObjectMessage}.
     * @return the created <code>ObjectMessage</code>
     */
    public MockObjectMessage createObjectMessage(java.io.Serializable object)
    {
        MockObjectMessage message = new MockObjectMessage();
        try
        {
            message.setObject(object);
        }
        catch(JMSException exc)
        {
            
        }
        objectMessages.add(message);
        return message;
    }

    /**
     * Returns a <code>ObjectMessage</code> by its index resp.
     * <code>null</code>, if no such <code>ObjectMessage</code> is
     * present.
     * @param index the index of the <code>ObjectMessage</code>
     * @return the <code>ObjectMessage</code>
     */
    public MockObjectMessage getObjectMessage(int index)
    {
        if(objectMessages.size() <= index || index < 0) return null;
        return (MockObjectMessage)objectMessages.get(index);
    }
}
