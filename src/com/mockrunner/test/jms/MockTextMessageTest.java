package com.mockrunner.test.jms;

import com.mockrunner.mock.jms.MockTextMessage;

import junit.framework.TestCase;

public class MockTextMessageTest extends TestCase
{
    public void testEquals()
    {
        assertTrue(new MockTextMessage("test").equals(new MockTextMessage("test")));
        assertFalse(new MockTextMessage("test").equals(new MockTextMessage("test1")));
        assertFalse(new MockTextMessage("test").equals(null));
        assertFalse(new MockTextMessage(null).equals(null));
        assertTrue(new MockTextMessage(null).equals(new MockTextMessage(null)));
        assertEquals(new MockTextMessage("test").hashCode(), new MockTextMessage("test").hashCode());
        assertEquals(new MockTextMessage(null).hashCode(), new MockTextMessage(null).hashCode());
    }
}
