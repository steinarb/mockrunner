package com.mockrunner.test.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import junit.framework.TestCase;

import com.mockrunner.struts.MapMessageResources;

public class MapMessageResourcesTest extends TestCase
{
    private MapMessageResources resources;
     
    protected void setUp() throws Exception
    {
        super.setUp();
        Map testMap = new HashMap();
        testMap.put("test.property.one", "TestOne");
        testMap.put("test.property.two", "TestTwo {0} {1}");
        testMap.put("test.property.three", "Test{0}Three");
        resources = new MapMessageResources(testMap);
    }

    public void testGetMessages()
    {
        assertEquals("TestOne", resources.getMessage("test.property.one"));
        assertEquals("TestTwo {0} {1}", resources.getMessage("test.property.two"));
        assertEquals("Test{0}Three", resources.getMessage(Locale.ITALIAN, "test.property.three"));
        assertNull(resources.getMessage("test.property.four"));
        assertTrue(resources.isPresent("test.property.one"));
        assertTrue(resources.isPresent(Locale.CHINESE, "test.property.two"));
        assertFalse(resources.isPresent("test.property.four"));
        resources.setMessage("test.property.four", "xyz");
        assertEquals("xyz", resources.getMessage("test.property.four"));
        assertEquals("TestTwo 0 1", resources.getMessage("test.property.two", "0", "1"));
        assertEquals("TestTestThree", resources.getMessage("test.property.three", "Test"));
        resources.clear();
        assertNull(resources.getMessage("test.property.one"));
        assertFalse(resources.isPresent("test.property.one"));
        assertFalse(resources.isPresent(Locale.GERMAN, "test.property.one"));
    }
}
