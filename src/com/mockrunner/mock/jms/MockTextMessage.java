package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.MessageNotWriteableException;
import javax.jms.TextMessage;

/**
 * Mock implementation of JMS <code>TextMessage</code>.
 */
public class MockTextMessage extends MockMessage implements TextMessage
{
    private String text;
    
    public MockTextMessage()
    {
        this(null);
    }

    public MockTextMessage(String text)
    {
        this.text = text;
    }

    public void setText(String text) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        this.text = text;
    }

    public String getText() throws JMSException
    {
        return text;
    }
    
    public String toString()
    {
        try
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append(this.getClass().getName() + ": " + getText());
            return buffer.toString();
        }
        catch(JMSException exc)
        {
            return exc.getMessage();
        }
    }

    public void clearBody() throws JMSException
    {
        super.clearBody();
        text = null;
    }
    
    /**
     * Compares the underlying String. If the Strings of 
     * both messages are <code>null</code>, this
     * method returns <code>true</code>.
     */
    public boolean equals(Object otherObject)
    {
        if(null == otherObject) return false;
        if(!(otherObject instanceof MockTextMessage)) return false;
        MockTextMessage otherMessage = (MockTextMessage)otherObject;
        if(null == text && null == otherMessage.text) return true;
        return text.equals(otherMessage.text);
    }

    public int hashCode()
    {
        if(null == text) return 0;
        return text.hashCode();
    }
    
    public Object clone()
    {
        return super.clone();
    }
}
