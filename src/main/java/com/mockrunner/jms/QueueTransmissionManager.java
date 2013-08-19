package com.mockrunner.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;

import com.mockrunner.mock.jms.MockConnection;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockQueueBrowser;
import com.mockrunner.mock.jms.MockQueueReceiver;
import com.mockrunner.mock.jms.MockQueueSender;
import com.mockrunner.mock.jms.MockSession;

/**
 * This class is used to create queue senders and receivers.
 * It can be also used to access all created classes in tests.
 */
public class QueueTransmissionManager
{
    private MockConnection connection;
    private MockSession session;
    private List queueSenderList;
    private List queueReceiverList;
    private List queueBrowserList;
    
    public QueueTransmissionManager(MockConnection connection, MockSession session)
    {
        this.connection = connection;
        this.session = session;
        queueSenderList = new ArrayList();
        queueReceiverList = new ArrayList();
        queueBrowserList = new ArrayList();
    }

    /**
     * Closes all senders, receivers, browsers, publishers and subscribers.
     */
    public void closeAll()
    {
        closeAllQueueSenders();
        closeAllQueueReceivers();
        closeAllQueueBrowsers();
    }

    /**
     * Closes all queue senders.
     */
    public void closeAllQueueSenders()
    {
        for(int ii = 0; ii < queueSenderList.size(); ii++)
        {
            QueueSender sender = (QueueSender)queueSenderList.get(ii);
            try
            {
                sender.close();
            }
            catch(JMSException exc)
            {
            
            }
        }
    }

    /**
     * Closes all queue receivers.
     */
    public void closeAllQueueReceivers()
    {
        for(int ii = 0; ii < queueReceiverList.size(); ii++)
        {
            QueueReceiver receiver = (QueueReceiver)queueReceiverList.get(ii);
            try
            {
                receiver.close();
            }
            catch(JMSException exc)
            {
    
            }
        }
    }

    /**
     * Closes all queue browsers.
     */
    public void closeAllQueueBrowsers()
    {
        for(int ii = 0; ii < queueBrowserList.size(); ii++)
        {
            QueueBrowser browser = (QueueBrowser)queueBrowserList.get(ii);
            try
            {
                browser.close();
            }
            catch(JMSException exc)
            {

            }
        }
    }
    
    /**
     * Creates a new <code>QueueSender</code> for the specified
     * <code>Queue</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createSender}.
     * @param queue the <code>Queue</code>
     * @return the created <code>QueueSender</code>
     */
    public MockQueueSender createQueueSender(MockQueue queue)
    {
        MockQueueSender sender = new MockQueueSender(connection, session, queue);
        queueSenderList.add(sender);
        return sender;
    }

    /**
     * Returns a <code>QueueSender</code> by its index or
     * <code>null</code>, if no such <code>QueueSender</code> is
     * present.
     * @param index the index of the <code>QueueSender</code>
     * @return the <code>QueueSender</code>
     */
    public MockQueueSender getQueueSender(int index)
    {
        if(queueSenderList.size() <= index || index < 0) return null;
        return (MockQueueSender)queueSenderList.get(index);
    }
    
    /**
     * Returns a <code>QueueSender</code> by the name of its
     * corresponding <code>Queue</code>. If there's more than
     * one <code>QueueSender</code> object for the specified name,
     * the first one will be returned.
     * @param queueName the name of the <code>Queue</code>
     * @return the <code>QueueSender</code>
     */
    public MockQueueSender getQueueSender(String queueName)
    {
        List senders = getQueueSenderList(queueName);
        if(senders.size() <= 0) return null;
        return (MockQueueSender)senders.get(0);
    }
    
    /**
     * Returns the list of the <code>QueueSender</code> objects
     * for a specific <code>Queue</code>.
     * @param queueName the name of the <code>Queue</code>
     * @return the list of <code>QueueSender</code> objects
     */
    public List getQueueSenderList(String queueName)
    {
        List resultList = new ArrayList();
        for(int ii = 0; ii < queueSenderList.size(); ii++)
        {
            QueueSender sender = (QueueSender)queueSenderList.get(ii);
            try
            {
                if(sender.getQueue().getQueueName().equals(queueName))
                {
                    resultList.add(sender);
                }
            }
            catch(JMSException exc)
            {
            
            }
        }
        return Collections.unmodifiableList(resultList);
    }
    
    /**
     * Returns the list of all <code>QueueSender</code> objects.
     * @return the list of <code>QueueSender</code> objects
     */
    public List getQueueSenderList()
    {
        return Collections.unmodifiableList(queueSenderList);
    }

    /**
     * Creates a new <code>QueueReceiver</code> for the specified
     * <code>Queue</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createReceiver}.
     * @param queue the <code>Queue</code>
     * @param messageSelector the message selector
     * @return the created <code>QueueReceiver</code>
     */
    public MockQueueReceiver createQueueReceiver(MockQueue queue, String messageSelector)
    {
        MockQueueReceiver receiver = new MockQueueReceiver(connection, session, queue, messageSelector);
        queueReceiverList.add(receiver);
        return receiver;
    }

    /**
     * Returns a <code>QueueReceiver</code> by its index or
     * <code>null</code>, if no such <code>QueueReceiver</code> is
     * present.
     * @param index the index of the <code>QueueReceiver</code>
     * @return the <code>QueueReceiver</code>
     */
    public MockQueueReceiver getQueueReceiver(int index)
    {
        if(queueReceiverList.size() <= index || index < 0) return null;
        return (MockQueueReceiver)queueReceiverList.get(index);
    }
    
    /**
     * Returns a <code>QueueReceiver</code> by the name of its
     * corresponding <code>Queue</code>. If there's more than
     * one <code>QueueReceiver</code> object for the specified name,
     * the first one will be returned.
     * @param queueName the name of the <code>Queue</code>
     * @return the <code>QueueReceiver</code>
     */
    public MockQueueReceiver getQueueReceiver(String queueName)
    {
        List receivers = getQueueReceiverList(queueName);
        if(receivers.size() <= 0) return null;
        return (MockQueueReceiver)receivers.get(0);
    }

    /**
     * Returns the list of the <code>QueueReceiver</code> objects
     * for a specific <code>Queue</code>.
     * @param queueName the name of the <code>Queue</code>
     * @return the list of <code>QueueReceiver</code> objects
     */
    public List getQueueReceiverList(String queueName)
    {
        List resultList = new ArrayList();
        for(int ii = 0; ii < queueReceiverList.size(); ii++)
        {
            QueueReceiver receiver = (QueueReceiver)queueReceiverList.get(ii);
            try
            {
                if(receiver.getQueue().getQueueName().equals(queueName))
                {
                    resultList.add(receiver);
                }
            }
            catch(JMSException exc)
            {
            
            }
        }
        return Collections.unmodifiableList(resultList);
    }
    
    /**
     * Returns the list of <code>QueueReceiver</code> objects.
     * @return the <code>QueueReceiver</code> list
     */
    public List getQueueReceiverList()
    {
        return Collections.unmodifiableList(queueReceiverList);
    }

    /**
     * Creates a new <code>QueueBrowser</code> for the specified
     * <code>Queue</code>. Usually this method is called
     * by {@link com.mockrunner.mock.jms.MockQueueSession#createBrowser}.
     * @param queue the <code>Queue</code>
     * @param messageSelector the message selector
     * @return the created <code>QueueBrowser</code>
     */
    public MockQueueBrowser createQueueBrowser(MockQueue queue, String messageSelector)
    {
        MockQueueBrowser browser = new MockQueueBrowser(connection, queue, messageSelector);
        queueBrowserList.add(browser);
        return browser;
    }

    /**
     * Returns a <code>QueueBrowser</code> by its index or
     * <code>null</code>, if no such <code>QueueBrowser</code> is
     * present.
     * @param index the index of the <code>QueueBrowser</code>
     * @return the <code>QueueBrowser</code>
     */
    public MockQueueBrowser getQueueBrowser(int index)
    {
        if(queueBrowserList.size() <= index || index < 0) return null;
        return (MockQueueBrowser)queueBrowserList.get(index);
    }
    
    /**
     * Returns a <code>QueueBrowser</code> by the name of its
     * corresponding <code>Queue</code>. If there's more than
     * one <code>QueueBrowser</code> object for the specified name,
     * the first one will be returned.
     * @param queueName the name of the <code>Queue</code>
     * @return the <code>QueueBrowser</code>
     */
    public MockQueueBrowser getQueueBrowser(String queueName)
    {
        List browsers = getQueueBrowserList(queueName);
        if(browsers.size() <= 0) return null;
        return (MockQueueBrowser)browsers.get(0);
    }

    /**
     * Returns the list of the <code>QueueBrowser</code> objects
     * for a specific <code>Queue</code>.
     * @param queueName the name of the <code>Queue</code>
     * @return the list of <code>QueueBrowser</code> objects
     */
    public List getQueueBrowserList(String queueName)
    {
        List resultList = new ArrayList();
        for(int ii = 0; ii < queueBrowserList.size(); ii++)
        {
            QueueBrowser browser = (QueueBrowser)queueBrowserList.get(ii);
            try
            {
                if(browser.getQueue().getQueueName().equals(queueName))
                {
                    resultList.add(browser);
                }
            }
            catch(JMSException exc)
            {
            
            }
        }
        return Collections.unmodifiableList(resultList);
    }
    
    /**
     * Returns the list of <code>QueueBrowser</code> objects.
     * @return the <code>QueueBrowser</code> list
     */
    public List getQueueBrowserList()
    {
        return Collections.unmodifiableList(queueBrowserList);
    }
}
