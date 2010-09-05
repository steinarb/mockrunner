package com.mockrunner.test.web;

import junit.framework.TestCase;

public class MockRegistrationTest extends TestCase
{
    /*private MockRegistration registration;
    
    protected void setUp() throws Exception
    {
        registration = new MockRegistration("name", "class");
    }

    protected void tearDown() throws Exception
    {
        registration = null;
    }

    public void testGetters()
    {
        assertEquals("name", registration.getName());
        assertEquals("class", registration.getClassName());
        registration.setName("anotherName");
        registration.setClassName("anotherClassName");
        assertEquals("anotherName", registration.getName());
        assertEquals("anotherClassName", registration.getClassName());
    }
    
    public void testInitParamters()
    {
        assertTrue(registration.getInitParameters().isEmpty());
        assertNull(registration.getInitParameter("name1"));
        assertTrue(registration.setInitParameter("name1", "value1"));
        assertEquals("value1", registration.getInitParameter("name1"));
        assertFalse(registration.setInitParameter("name1", "value2"));
        assertEquals("value1", registration.getInitParameter("name1"));
        assertTrue(registration.setInitParameter("name2", "value2"));
        assertEquals(2, registration.getInitParameters().size());
        assertEquals("value1", registration.getInitParameters().get("name1"));
        assertEquals("value2", registration.getInitParameters().get("name2"));
    }
    
    public void testInitParamtersMap()
    {
        registration.setInitParameter("name1", "value1");
        registration.setInitParameter("name2", "value2");
        registration.setInitParameter("name3", "value3");
        Map paramters = new HashMap();
        paramters.put("name4", "value4");
        paramters.put("name5", "value5");
        Set conflicting = registration.setInitParameters(paramters);
        assertTrue(conflicting.isEmpty());
        assertEquals(5, registration.getInitParameters().size());
        assertEquals("value1", registration.getInitParameter("name1"));
        assertEquals("value2", registration.getInitParameter("name2"));
        assertEquals("value3", registration.getInitParameter("name3"));
        assertEquals("value4", registration.getInitParameter("name4"));
        assertEquals("value5", registration.getInitParameter("name5"));
        paramters = new HashMap();
        paramters.put("name4", "value44");
        paramters.put("name2", "value25");
        paramters.put("name6", "value6");
        conflicting = registration.setInitParameters(paramters);
        assertEquals(2, conflicting.size());
        assertTrue(conflicting.contains("name2"));
        assertTrue(conflicting.contains("name4"));
        assertEquals(5, registration.getInitParameters().size());
        assertEquals("value1", registration.getInitParameter("name1"));
        assertEquals("value2", registration.getInitParameter("name2"));
        assertEquals("value3", registration.getInitParameter("name3"));
        assertEquals("value4", registration.getInitParameter("name4"));
        assertEquals("value5", registration.getInitParameter("name5"));
    }
    
    public void testInitParamtersMapIllegalArgument()
    {
        Map paramters = new HashMap();
        paramters.put("name1", null);
        try
        {
            registration.setInitParameters(paramters);
            fail();
        } 
        catch (IllegalArgumentException exc)
        {
            //expected exception
        }
        paramters = new HashMap();
        paramters.put(null, "value1");
        try
        {
            registration.setInitParameters(paramters);
            fail();
        } 
        catch (IllegalArgumentException exc)
        {
            //expected exception
        }
    }*/
}
