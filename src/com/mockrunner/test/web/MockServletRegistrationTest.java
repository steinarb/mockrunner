package com.mockrunner.test.web;

import junit.framework.TestCase;

public class MockServletRegistrationTest extends TestCase
{
    /*private MockServletRegistration registration;
    
    protected void setUp() throws Exception
    {
        registration = new MockServletRegistration("name", "class");
    }

    protected void tearDown() throws Exception
    {
        registration = null;
    }

    public void testGetters()
    {
        registration.setRunAsRole("role");
        registration.setLoadOnStartup(5);
        MultipartConfigElement element = new MultipartConfigElement("");
        registration.setMultipartConfig(element);
        assertEquals("role", registration.getRunAsRole());
        assertEquals(5, registration.getLoadOnStartup());
        assertSame(element, registration.getMultipartConfig());
    }
    
    public void testServletSecurity()
    {
        ServletSecurityElement element1 = new ServletSecurityElement();
        assertTrue(registration.setServletSecurity(element1).isEmpty());
        assertSame(element1, registration.getServletSecurity());
        Set securityMapping = new HashSet();
        ServletSecurityElement element2 = new ServletSecurityElement();
        registration.setServletSecurityMappings(securityMapping);
        assertSame(securityMapping, registration.setServletSecurity(element2));
        assertSame(element2, registration.getServletSecurity());
    }
   
    public void testAddMappings()
    {
        assertTrue(registration.getMappings().isEmpty());
        assertTrue(registration.addMapping(new String[] {"mapping1", "mapping2"}).isEmpty());
        assertEquals(2, registration.getMappings().size());
        assertTrue(registration.getMappings().contains("mapping1"));
        assertTrue(registration.getMappings().contains("mapping2"));
        Set conflicts = registration.addMapping(new String[] {"mapping3", "mapping2"});
        assertEquals(1, conflicts.size());
        assertTrue(conflicts.contains("mapping2"));
        assertEquals(2, registration.getMappings().size());
        assertTrue(registration.getMappings().contains("mapping1"));
        assertTrue(registration.getMappings().contains("mapping2"));
        assertTrue(registration.addMapping(new String[] {"mapping3"}).isEmpty());
        assertEquals(3, registration.getMappings().size());
        assertTrue(registration.getMappings().contains("mapping1"));
        assertTrue(registration.getMappings().contains("mapping2"));
        assertTrue(registration.getMappings().contains("mapping3"));
    }
    
    public void testAddMappingsEmptyMapping()
    {
        try
        {
            registration.addMapping(null);
            fail();
        } 
        catch (IllegalArgumentException exc)
        {
            //expected exception
        }
        try
        {
            registration.addMapping(new String[0]);
            fail();
        } 
        catch (IllegalArgumentException exc)
        {
            //expected exception
        }
    }*/
}
