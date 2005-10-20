package com.mockrunner.test.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.mockrunner.mock.web.MockFilterConfig;

public class MockFilterConfigTest extends TestCase
{
    public void testInitParameters()
    {
        MockFilterConfig config = new MockFilterConfig();
        config.setInitParameter("key1", "value1");
        Map parameters = new HashMap();
        parameters.put("key2", "value2");
        parameters.put("key3", "value3");
        config.setInitParameters(parameters);
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
