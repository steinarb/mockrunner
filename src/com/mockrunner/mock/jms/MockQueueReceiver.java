package com.mockrunner.mock.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueReceiver;

/**
 * Mock implementation of JMS <code>QueueReceiver</code>.
 */
public class MockQueueReceiver implements QueueReceiver
{
    private MockQueueConnection connection;
    private MockQueue queue;
    private String messageSelector;
    private boolean closed;
    private MessageListener messageListener;
    private List messages;
    private List receivedMessages;

    public MockQueueReceiver(MockQueueConnection connection, MockQueue queue, String messageSelector)
    {
        this.connection = connection;
        this.queue = queue;
        this.messageSelector = messageSelector;
        closed = false;
        messageListener = null;
        messages = new ArrayList();
        receivedMessages = new ArrayList();
    }
    
    public boolean isClosed()
    {
        return closed;
    }
    
    public void receiveMessage(Message message)
    {
        receivedMessages.add(message);
        if(null != messageListener)
        {
            messageListener.onMessage(message);
        }
        else
        {
            messages.add(message);
        }
    }
    
    public List getReceivedMessageList()
    {
        return Collections.unmodifiableList(receivedMessages);
    }

    public Queue getQueue() throws JMSException
    {
        connection.throwJMSException();
        return queue;
    }

    public String getMessageSelector() throws JMSException
    {
        connection.throwJMSException();
        return messageSelector;
    }

    public MessageListener getMessageListener() throws JMSException
    {
        connection.throwJMSException();
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) throws JMSException
    {
        connection.throwJMSException();
        this.messageListener = messageListener;
    }

    public Message receive() throws JMSException
    {
        connection.throwJMSException();
        if(messages.size() <= 0) return null;
        return (Message)messages.remove(0);
    }

    public Message receive(long timeout) throws JMSException
    {
        connection.throwJMSException();
        return receive();
    }

    public Message receiveNoWait() throws JMSException
    {
        connection.throwJMSException();
        return receive();
    }

    public void close() throws JMSException
    {
        connection.throwJMSException();
        closed = true;
    }
}
