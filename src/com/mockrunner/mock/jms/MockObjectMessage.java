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
    
    /**
     * Calls the <code>equals</code> method of the underlying
     * object. If both objects are <code>null</code>, this
     * method returns <code>true</code>.
     */
    public boolean equals(Object otherObject)
    {
        if(null == otherObject) return false;
        if(!(otherObject instanceof MockObjectMessage)) return false;
        MockObjectMessage otherMessage = (MockObjectMessage)otherObject;
        if(null == object && null == otherMessage.object) return true;
        return object.equals(otherMessage.object);
    }

    public int hashCode()
    {
        if(null == object) return 0;
        return object.hashCode();
    }
}
