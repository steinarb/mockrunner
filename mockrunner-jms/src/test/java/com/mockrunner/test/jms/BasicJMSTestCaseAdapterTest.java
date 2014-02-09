package com.mockrunner.test.jms;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.mockrunner.jms.BasicJMSTestCaseAdapter;
import com.mockrunner.mock.jms.JMSMockObjectFactory;

public class BasicJMSTestCaseAdapterTest extends BasicJMSTestCaseAdapter
{
	@Test
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
