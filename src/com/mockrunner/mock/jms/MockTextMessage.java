package com.mockrunner.mock.jms;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * Mock implementation of JMS <code>TextMessage</code>.
 */
public class MockTextMessage extends MockMessage implements TextMessage
{
    private String text;

    public void setText(String text) throws JMSException
    {
        this.text = text;
    }

    public String getText() throws JMSException
    {
        return text;
    }

    public void clearBody() throws JMSException
    {
        super.clearBody();
        text = null;
    }
}
