package com.mockrunner.test.web;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.struts.action.ActionForward;

import com.mockrunner.mock.web.MockActionMapping;

public class MockActionMappingTest extends TestCase
{
    private MockActionMapping mapping;
    
    protected void setUp() throws Exception
    {
        mapping = new MockActionMapping();
    }
    
    public void testSetupAndAddForwards()
    {
        mapping.addForward("name1", "path1");
        mapping.addForward("name2", "path2");
        ActionForward forward1 = mapping.findForward("name1");
        ActionForward forward2 = mapping.findForward("name2");
        ActionForward forward3 = mapping.findForward("name3");
        assertEquals("name1", forward1.getName());
        assertEquals("path1", forward1.getPath());
        assertEquals("name2", forward2.getName());
        assertEquals("path2", forward2.getPath());
        assertEquals("name3", forward3.getName());
        assertEquals("name3", forward3.getPath());
        List forwards = Arrays.asList(mapping.findForwards());
        assertEquals(2, forwards.size());
        assertTrue(forwards.contains("name1"));
        assertTrue(forwards.contains("name2"));
        mapping.setupForwards(new String[] {"name1"});
        forward1 = mapping.findForward("name1");
        assertEquals("name1", forward1.getName());
        assertEquals("name1", forward1.getPath());
        forwards = Arrays.asList(mapping.findForwards());
        assertEquals(2, forwards.size());
        assertTrue(forwards.contains("name1"));
        assertTrue(forwards.contains("name2"));
        mapping.clearForwards();
        forward2 = mapping.findForward("name2");
        assertEquals("name2", forward2.getName());
        assertEquals("name2", forward2.getPath());
        forwards = Arrays.asList(mapping.findForwards());
        assertEquals(0, forwards.size());
    }
}
