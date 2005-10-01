package com.mockrunner.test.jdbc;

import junit.framework.TestCase;

import com.mockrunner.mock.jdbc.MockRef;

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
    
    public void testEquals() throws Exception
    {
        MockRef nullRef = new MockRef(null);
        assertFalse(nullRef.equals(null));
        assertTrue(nullRef.equals(nullRef));
        MockRef ref = new MockRef("test");
        assertFalse(ref.equals(nullRef));
        assertFalse(nullRef.equals(ref));
        MockRef other = new MockRef("test");
        assertTrue(ref.equals(other));
        assertTrue(other.equals(ref));
        assertEquals(ref.hashCode(), other.hashCode());
        other = new MockRef("test") {};
        assertFalse(other.equals(ref));
        assertFalse(ref.equals(other));
        other = new MockRef("test");
        other.setBaseTypeName("baseTypeName");
        assertFalse(other.equals(ref));
        assertFalse(ref.equals(other));
        ref.setBaseTypeName("baseTypeName");
        assertTrue(ref.equals(other));
        assertTrue(other.equals(ref));
        assertEquals(ref.hashCode(), other.hashCode());
        other = new MockRef(new Integer(3));
        other.setBaseTypeName("baseTypeName");
        assertFalse(ref.equals(other));
        assertFalse(other.equals(ref));
        ref = new MockRef(new Integer(3));
        assertFalse(ref.equals(other));
        assertFalse(other.equals(ref));
        ref.setBaseTypeName("baseTypeName");
        assertTrue(ref.equals(other));
        assertTrue(other.equals(ref));
        assertEquals(ref.hashCode(), other.hashCode());
    }
}
