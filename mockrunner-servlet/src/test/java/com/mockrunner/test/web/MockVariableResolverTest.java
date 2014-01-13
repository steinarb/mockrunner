package com.mockrunner.test.web;

import com.mockrunner.mock.web.MockVariableResolver;

import junit.framework.TestCase;

public class MockVariableResolverTest extends TestCase
{
    private MockVariableResolver resolver;

    protected void setUp() throws Exception
    {
        resolver = new MockVariableResolver();
    }

    protected void tearDown() throws Exception
    {
        resolver = null;
    }
    
    public void testResolve() throws Exception
    {
        assertNull(resolver.resolveVariable("test"));
        resolver.addVariable("test", new Integer(3));
        assertEquals(new Integer(3), resolver.resolveVariable("test"));
        assertNull(resolver.resolveVariable("test1"));
        resolver.addVariable("test1", "xyz");
        assertEquals(new Integer(3), resolver.resolveVariable("test"));
        assertEquals("xyz", resolver.resolveVariable("test1"));
        resolver.clearVariables();
        assertNull(resolver.resolveVariable("test"));
        assertNull(resolver.resolveVariable("test1"));
    }
}
