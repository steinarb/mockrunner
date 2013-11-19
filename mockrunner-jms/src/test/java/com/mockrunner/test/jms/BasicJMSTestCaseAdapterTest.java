package com.mockrunner.test.jms;

import com.mockrunner.jms.BasicJMSTestCaseAdapter;
import com.mockrunner.mock.jms.JMSMockObjectFactory;

public class BasicJMSTestCaseAdapterTest extends BasicJMSTestCaseAdapter
{
    public void testSetJMSFactory()
    {
        JMSMockObjectFactory jmsFactory = new JMSMockObjectFactory();
        setJMSMockObjectFactory(jmsFactory);
        assertSame(jmsFactory, getJMSMockObjectFactory());
        setJMSMockObjectFactory(null);
        assertNotNull(getJMSMockObjectFactory());
        assertNotSame(jmsFactory, getJMSMockObjectFactory());
    }
}
