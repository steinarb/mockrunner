package com.mockrunner.test.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.mockrunner.mock.web.MockServletConfig;

import junit.framework.TestCase;

public class MockServletConfigTest extends TestCase
{
    public void testInitParameters()
    {
        MockServletConfig config = new MockServletConfig();
        config.setInitParameter("key1", "value1");
        config.setInitParameter("key2", "value2");
        config.setInitParameter("key3", "value3");
        assertEquals("value1", config.getInitParameter("key1"));
        assertEquals("value2", config.getInitParameter("key2"));
        assertEquals("value3", config.getInitParameter("key3"));
        Enumeration params = config.getInitParameterNames();
        List list = new ArrayList();
        list.add(params.nextElement());
        list.add(params.nextElement());
        list.add(params.nextElement());
        assertFalse(params.hasMoreElements());
        assertTrue(list.contains("key1"));
        assertTrue(list.contains("key2"));
        assertTrue(list.contains("key3"));
        config.clearInitParameters();
        assertNull(config.getInitParameter("key1"));
        params = config.getInitParameterNames();
        assertFalse(params.hasMoreElements());
    }
}
