package com.mockrunner.mock.jms;

import java.util.Enumeration;
import java.util.Vector;

import javax.jms.ConnectionMetaData;
import javax.jms.JMSException;

/**
 * Mock implementation of JMS <code>ConnectionMetaData</code>.
 */
public class MockConnectionMetaData implements ConnectionMetaData
{
    private Vector properties;
    
    public MockConnectionMetaData()
    {
        properties = new Vector();
    }
    
    public void addJMSXPropertyName(String name)
    {
        properties.add(name);
    }
    
    public int getJMSMajorVersion() throws JMSException
    {
        return 1;
    }

    public int getJMSMinorVersion() throws JMSException
    {
        return 1;
    }

    public String getJMSProviderName() throws JMSException
    {
        return "Mockrunner";
    }

    public String getJMSVersion() throws JMSException
    {
        return "1.1";
    }

    public Enumeration getJMSXPropertyNames() throws JMSException
    {
        return properties.elements();
    }

    public int getProviderMajorVersion() throws JMSException
    {
        return 1;
    }

    public int getProviderMinorVersion() throws JMSException
    {
        return 0;
    }

    public String getProviderVersion() throws JMSException
    {
        return "1.0";
    }
}
