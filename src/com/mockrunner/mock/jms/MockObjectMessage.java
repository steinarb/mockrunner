package com.mockrunner.mock.jms;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 * Mock implementation of JMS <code>ObjectMessage</code>.
 */
public class MockObjectMessage extends MockMessage implements ObjectMessage
{
    private Serializable object;
    
    public MockObjectMessage()
    {
        
    }
    
    public MockObjectMessage(Serializable object)
    {
        this.object = object;
    }

    public void setObject(Serializable object) throws JMSException
    {
        this.object = object;
    }

    public Serializable getObject() throws JMSException
    {
        return object;
    }

    public void clearBody() throws JMSException
    {
        super.clearBody();
        object = null;
    }
}
