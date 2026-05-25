package com.mockrunner.test.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mockrunner.mock.web.MockVariableResolver;

class MockVariableResolverTest
{
    private MockVariableResolver resolver;

    @BeforeEach
    void setUp() throws Exception
    {
        resolver = new MockVariableResolver();
    }

    @AfterEach
    void tearDown() throws Exception
    {
        resolver = null;
    }

    @Test
    void testResolve() throws Exception
    {
        assertNull(resolver.resolveVariable("test"));
        resolver.addVariable("test", 3);
        assertEquals(3, resolver.resolveVariable("test"));
        assertNull(resolver.resolveVariable("test1"));
        resolver.addVariable("test1", "xyz");
        assertEquals(3, resolver.resolveVariable("test"));
        assertEquals("xyz", resolver.resolveVariable("test1"));
        resolver.clearVariables();
        assertNull(resolver.resolveVariable("test"));
        assertNull(resolver.resolveVariable("test1"));
    }
}
