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
    private int jmsMajorVersion;
    private int jmsMinorVersion;
    private String jmsProviderName;
    private String jmsVersion;
    private int providerMajorVersion;
    private int providerMinorVersion;
    private String providerVersion;
    
    public MockConnectionMetaData()
    {
        properties = new Vector();
        jmsMajorVersion = 1;
        jmsMinorVersion = 1;
        jmsProviderName = "Mockrunner";
        jmsVersion = "1.1";
        providerMajorVersion = 1;
        providerMinorVersion = 0;
        providerVersion = "1.0";
    }
    
    public void addJMSXPropertyName(String name)
    {
        properties.add(name);
    }
    
    public void setJMSMajorVersion(int jmsMajorVersion)
    {
        this.jmsMajorVersion = jmsMajorVersion;
    }

    public void setJMSMinorVersion(int jmsMinorVersion)
    {
        this.jmsMinorVersion = jmsMinorVersion;
    }
    
    public void setJMSProviderName(String jmsProviderName)
    {
        this.jmsProviderName = jmsProviderName;
    }

    public void setJMSVersion(String jmsVersion)
    {
        this.jmsVersion =jmsVersion;
    }
    
    public void setProviderMajorVersion(int providerMajorVersion)
    {
        this.providerMajorVersion = providerMajorVersion;
    }

    public void setProviderMinorVersion(int providerMinorVersion)
    {
        this.providerMinorVersion = providerMinorVersion;
    }

    public void setProviderVersion(String providerVersion)
    {
        this.providerVersion = providerVersion;
    }
    
    public int getJMSMajorVersion() throws JMSException
    {
        return jmsMajorVersion;
    }

    public int getJMSMinorVersion() throws JMSException
    {
        return jmsMinorVersion;
    }

    public String getJMSProviderName() throws JMSException
    {
        return jmsProviderName;
    }

    public String getJMSVersion() throws JMSException
    {
        return jmsVersion;
    }

    public Enumeration getJMSXPropertyNames() throws JMSException
    {
        return properties.elements();
    }

    public int getProviderMajorVersion() throws JMSException
    {
        return providerMajorVersion;
    }

    public int getProviderMinorVersion() throws JMSException
    {
        return providerMinorVersion;
    }

    public String getProviderVersion() throws JMSException
    {
        return providerVersion;
    }
}
