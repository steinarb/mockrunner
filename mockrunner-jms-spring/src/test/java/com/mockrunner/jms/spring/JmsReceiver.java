package com.mockrunner.jms.spring;

import javax.ejb.*;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/fileImport")
})
public class JmsReceiver implements MessageListener {

    Message receivedMessage;

    @Override
    public void onMessage(Message message) {
        this.receivedMessage = message;
        
    }
}
