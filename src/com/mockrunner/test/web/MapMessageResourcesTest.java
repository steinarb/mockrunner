package com.mockrunner.test.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.struts.util.MessageResourcesFactory;

import junit.framework.TestCase;

import com.mockrunner.struts.MapMessageResources;
import com.mockrunner.struts.MapMessageResourcesFactory;

public class MapMessageResourcesTest extends TestCase
{
    private MapMessageResources resources;
    private Map testMap;
    private String tempFactoryClass;
     
    protected void setUp() throws Exception
    {
        super.setUp();
        tempFactoryClass = MessageResourcesFactory.getFactoryClass();
        testMap = new HashMap();
        testMap.put("test.property.one", "TestOne");
        testMap.put("test.property.two", "TestTwo {0} {1}");
        testMap.put("test.property.three", "Test{0}Three");
        resources = new MapMessageResources(testMap);
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        MessageResourcesFactory.setFactoryClass(tempFactoryClass);
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
        resources.putMessage("test.property.four", "xyz");
        assertEquals("xyz", resources.getMessage("test.property.four"));
        assertEquals("TestTwo 0 1", resources.getMessage("test.property.two", "0", "1"));
        assertEquals("TestTestThree", resources.getMessage("test.property.three", "Test"));
        resources.clear();
        assertNull(resources.getMessage("test.property.one"));
        assertFalse(resources.isPresent("test.property.one"));
        assertFalse(resources.isPresent(Locale.GERMAN, "test.property.one"));
    }
    
    public void testMessageResourcesFactory()
    {
        MessageResourcesFactory.setFactoryClass("com.mockrunner.struts.MapMessageResourcesFactory");
        assertTrue(MessageResourcesFactory.createFactory() instanceof MapMessageResourcesFactory);
        MapMessageResourcesFactory factory = (MapMessageResourcesFactory)MessageResourcesFactory.createFactory();
        resources = (MapMessageResources)factory.createResources("");
        assertFalse(resources.isPresent("test.property.one"));
        MapMessageResourcesFactory.setMessageMap(testMap);
        resources = (MapMessageResources)factory.createResources("");
        assertTrue(resources.isPresent("test.property.one"));
        MapMessageResourcesFactory.setMessageMap(null);
        resources = (MapMessageResources)factory.createResources("");
        assertFalse(resources.isPresent("test.property.one"));
    }
    
    public void testLoadFromFile()
    {
        resources.putMessages("src/com/mockrunner/test/web/test.properties");
        assertEquals("test1", resources.getMessage("test.property1"));
        assertEquals("test2", resources.getMessage("test.property2"));
        assertEquals("test3", resources.getMessage("test.property3"));
    }
}
