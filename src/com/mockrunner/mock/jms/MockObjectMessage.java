package com.mockrunner.mock.jms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.MessageNotWriteableException;
import javax.jms.ObjectMessage;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of JMS <code>ObjectMessage</code>.
 */
public class MockObjectMessage extends MockMessage implements ObjectMessage
{
    private Serializable object;
    
    public MockObjectMessage()
    {
        this(null);
    }
    
    public MockObjectMessage(Serializable object)
    {
        this.object = object;
    }

    public void setObject(Serializable object) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
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
    
    public Object clone()
    {
        MockObjectMessage message = (MockObjectMessage)super.clone();
        try
        {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutStream = new ObjectOutputStream(byteOutStream);
            objectOutStream.writeObject(object);
            objectOutStream.flush();
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
            ObjectInputStream objectInStream = new ObjectInputStream(byteInStream);
            message.object = (Serializable)objectInStream.readObject();
            return message;
        }
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getClass().getName() + ": ");
        if(null == object)
        {
            buffer.append("null");
        }
        else
        {
            buffer.append(object.toString());
        }
        return buffer.toString();
    }
}
