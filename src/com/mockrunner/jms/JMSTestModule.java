package com.mockrunner.jms;

import com.mockrunner.mock.jms.JMSMockObjectFactory;

/**
 * Module for JMS tests.
 */
public class JMSTestModule
{
    private JMSMockObjectFactory mockFactory;
  
    public JMSTestModule(JMSMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }
}
