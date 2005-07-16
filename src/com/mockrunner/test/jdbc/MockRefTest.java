package com.mockrunner.test.jdbc;

import com.mockrunner.mock.jdbc.MockRef;

import junit.framework.TestCase;

public class MockRefTest extends TestCase
{
    public void testClone() throws Exception
    {
        MockRef ref = new MockRef(this);
        MockRef copy = (MockRef)ref.clone();
        assertNotSame(ref, copy);
        assertSame(ref.getObject(), copy.getObject());
        assertSame(this, copy.getObject());
    }
}
