package com.mockrunner.test.web;

import org.apache.struts.action.ActionForward;

import com.mockrunner.mock.web.MockActionForward;

import junit.framework.TestCase;

public class MockActionForwardTest extends TestCase
{
    public void testCopyConstructor()
    {
        ActionForward forward = new ActionForward("name", "path", true, false);
        MockActionForward mockForward = new MockActionForward(forward);
        assertEquals("name", mockForward.getName());
        assertEquals("path", mockForward.getPath());
        assertTrue(mockForward.getRedirect());
        assertFalse(mockForward.getContextRelative());
    }
    
    public void testVerifyNameAndPath()
    {
        MockActionForward mockForward = new MockActionForward();
        mockForward.setName("name");
        mockForward.setPath("path");
        mockForward.setRedirect(true);
        assertTrue(mockForward.verifyName("name"));
        assertTrue(mockForward.verifyPath("path"));
        assertTrue(mockForward.verifyRedirect(true));
        assertFalse(mockForward.verifyName("path"));
        assertFalse(mockForward.verifyPath("name"));
        assertFalse(mockForward.verifyRedirect(false));
    }
    
    public void testVerifyFreeze()
    {
        MockActionForward mockForward = new MockActionForward("name", "path", true, false);
        mockForward.freeze();
        mockForward.setName("otherName");
        mockForward.setPath("otherPath");
        mockForward.setRedirect(false);
        mockForward.setContextRelative(true);
        mockForward.setModule("aModule");
        assertEquals("otherName", mockForward.getName());
        assertEquals("otherPath", mockForward.getPath());
        assertFalse(mockForward.getRedirect());
        assertTrue(mockForward.getContextRelative());
        assertEquals("aModule", mockForward.getModule());
    }
}