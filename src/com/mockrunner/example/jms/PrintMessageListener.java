package com.mockrunner.example.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Listener for {@link com.mockrunner.example.jms.PrintMessageServlet}.
 * If the message is a text message, the customer data will be printed
 * and the message will be acknowledged. The print code is omitted for 
 * simplicity.
 */
public class PrintMessageListener implements MessageListener
{
    public void onMessage(Message message)
    {
        if(message instanceof TextMessage)
        {
            //do print
        }
        try
        {
            message.acknowledge();
        }
        catch(JMSException exc)
        {
            exc.printStackTrace();
        }
    }
}
