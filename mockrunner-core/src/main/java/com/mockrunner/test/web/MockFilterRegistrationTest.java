package com.mockrunner.test.web;

import junit.framework.TestCase;

public class MockFilterRegistrationTest extends TestCase
{
    /*private MockFilterRegistration registration;
    
    protected void setUp() throws Exception
    {
        registration = new MockFilterRegistration("name", "class");
    }

    protected void tearDown() throws Exception
    {
        registration = null;
    }

    public void testAddMappingForServletNames()
    {
        assertTrue(registration.getServletNameMappings().isEmpty());
        assertTrue(registration.getServletNameRegistrationMappings().isEmpty());
        registration.addMappingForServletNames(EnumSet.noneOf(DispatcherType.class), true, new String[] {"servlet1"});
        assertEquals(1, registration.getServletNameMappings().size());
        assertEquals(1, registration.getServletNameRegistrationMappings().size());
        registration.addMappingForServletNames(EnumSet.of(DispatcherType.ERROR, DispatcherType.FORWARD), false, new String[] {"servlet1", "servlet2"});
        assertEquals(3, registration.getServletNameMappings().size());
        assertEquals(2, registration.getServletNameRegistrationMappings().size());
        registration.clearMappingsForServletNames();
        assertTrue(registration.getServletNameMappings().isEmpty());
        assertTrue(registration.getServletNameRegistrationMappings().isEmpty());
    }
    
    public void testAddMappingForServletNamesEmptyNames()
    {
        try
        {
            registration.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, null);
            fail();
        } 
        catch (IllegalArgumentException exc)
        {
            //expected exception
        }
        try
        {
            registration.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), false, new String[0]);
            fail();
        } 
        catch (IllegalArgumentException exc)
        {
            //expected exception
        }
    }
    
    public void testGetServletNameMappings()
    {
        registration.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), true, new String[] {"servlet1"});
        registration.addMappingForServletNames(EnumSet.of(DispatcherType.ERROR, DispatcherType.FORWARD, DispatcherType.ASYNC), false, new String[] {"servlet2",  "servlet3"});
        registration.addMappingForServletNames(null, true, new String[] {"servlet1",  "servlet2",  "servlet3"});
        registration.addMappingForServletNames(EnumSet.noneOf(DispatcherType.class), true, new String[] {"servlet1"});
        Iterator iterator = registration.getServletNameMappings().iterator();
        assertEquals("servlet1", iterator.next());
        assertEquals("servlet2", iterator.next());
        assertEquals("servlet3", iterator.next());
        assertEquals("servlet1", iterator.next());
        assertEquals("servlet2", iterator.next());
        assertEquals("servlet3", iterator.next());
        assertEquals("servlet1", iterator.next());
        assertFalse(iterator.hasNext());
        List list = registration.getServletNameRegistrationMappings();
        assertEquals(4, list.size());
        MockFilterRegistration.ServletNameFilterRegistrationMapping mapping = (MockFilterRegistration.ServletNameFilterRegistrationMapping)list.get(0);
        assertEquals(EnumSet.of(DispatcherType.REQUEST), mapping.getDispatcherTypes());
        assertTrue(mapping.isMatchAfter());
        assertTrue(Arrays.equals(new String[] {"servlet1"}, mapping.getServletNames()));
        mapping = (MockFilterRegistration.ServletNameFilterRegistrationMapping)list.get(1);
        assertEquals(EnumSet.of(DispatcherType.ERROR, DispatcherType.FORWARD, DispatcherType.ASYNC), mapping.getDispatcherTypes());
        assertFalse(mapping.isMatchAfter());
        assertTrue(Arrays.equals(new String[] {"servlet2",  "servlet3"}, mapping.getServletNames()));
        mapping = (MockFilterRegistration.ServletNameFilterRegistrationMapping)list.get(2);
        assertEquals(EnumSet.of(DispatcherType.REQUEST), mapping.getDispatcherTypes());
        assertTrue(mapping.isMatchAfter());
        assertTrue(Arrays.equals(new String[] {"servlet1",  "servlet2",  "servlet3"}, mapping.getServletNames()));
        mapping = (MockFilterRegistration.ServletNameFilterRegistrationMapping)list.get(3);
        assertEquals(EnumSet.noneOf(DispatcherType.class), mapping.getDispatcherTypes());
        assertTrue(mapping.isMatchAfter());
        assertTrue(Arrays.equals(new String[] {"servlet1"}, mapping.getServletNames()));
    }
    
    public void testAddMappingForUrlPatterns()
    {
        assertTrue(registration.getUrlPatternMappings().isEmpty());
        assertTrue(registration.getURLPatternRegistrationMapping().isEmpty());
        registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.ERROR, DispatcherType.FORWARD), false, new String[] {"pattern1", "pattern2"});
        assertEquals(2, registration.getUrlPatternMappings().size());
        assertEquals(1, registration.getURLPatternRegistrationMapping().size());
        registration.addMappingForUrlPatterns(null, true, new String[] {"pattern1", "pattern2"});
        assertEquals(4, registration.getUrlPatternMappings().size());
        assertEquals(2, registration.getURLPatternRegistrationMapping().size());
        registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, new String[] {"pattern45"});
        assertEquals(5, registration.getUrlPatternMappings().size());
        assertEquals(3, registration.getURLPatternRegistrationMapping().size());
        registration.clearMappingsForUrlPatterns();
        assertTrue(registration.getUrlPatternMappings().isEmpty());
        assertTrue(registration.getURLPatternRegistrationMapping().isEmpty());
    }
    
    public void testAddMappingForUrlPatternsEmptyNames()
    {
        try
        {
            registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, null);
            fail();
        } 
        catch (IllegalArgumentException exc)
        {
            //expected exception
        }
        try
        {
            registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, new String[0]);
            fail();
        } 
        catch (IllegalArgumentException exc)
        {
            //expected exception
        }
    }
    
    public void testGetUrlPatternMappings()
    {
        registration.addMappingForUrlPatterns(null, true, new String[] {"pattern1"});
        registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.FORWARD), false, new String[] {"pattern2",  "pattern3"});
        registration.addMappingForUrlPatterns(EnumSet.noneOf(DispatcherType.class), false, new String[] {"pattern1",  "pattern2",  "pattern3"});
        Iterator iterator = registration.getUrlPatternMappings().iterator();
        assertEquals("pattern1", iterator.next());
        assertEquals("pattern2", iterator.next());
        assertEquals("pattern3", iterator.next());
        assertEquals("pattern1", iterator.next());
        assertEquals("pattern2", iterator.next());
        assertEquals("pattern3", iterator.next());
        assertFalse(iterator.hasNext());
        List list = registration.getURLPatternRegistrationMapping();
        assertEquals(3, list.size());
        MockFilterRegistration.URLPatternFilterRegistrationMapping mapping = (MockFilterRegistration.URLPatternFilterRegistrationMapping)list.get(0);
        assertEquals(EnumSet.of(DispatcherType.REQUEST), mapping.getDispatcherTypes());
        assertTrue(mapping.isMatchAfter());
        assertTrue(Arrays.equals(new String[] {"pattern1"}, mapping.getURLPatterns()));
        mapping = (MockFilterRegistration.URLPatternFilterRegistrationMapping)list.get(1);
        assertEquals(EnumSet.of(DispatcherType.FORWARD), mapping.getDispatcherTypes());
        assertFalse(mapping.isMatchAfter());
        assertTrue(Arrays.equals(new String[] {"pattern2",  "pattern3"}, mapping.getURLPatterns()));
        mapping = (MockFilterRegistration.URLPatternFilterRegistrationMapping)list.get(2);
        assertEquals(EnumSet.noneOf(DispatcherType.class), mapping.getDispatcherTypes());
        assertFalse(mapping.isMatchAfter());
        assertTrue(Arrays.equals(new String[] {"pattern1",  "pattern2",  "pattern3"}, mapping.getURLPatterns()));
    }*/
}
