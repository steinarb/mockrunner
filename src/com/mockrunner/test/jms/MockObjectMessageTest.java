package com.mockrunner.test.jms;

import com.mockrunner.mock.jms.MockObjectMessage;
import com.mockrunner.mock.jms.MockTextMessage;

import junit.framework.TestCase;

public class MockObjectMessageTest extends TestCase
{
    public void testEquals()
    {
        MockObjectMessage message = new MockObjectMessage("test");
        assertTrue(message.equals(message));
        assertTrue(new MockObjectMessage("test").equals(new MockObjectMessage("test")));
        assertFalse(new MockObjectMessage("test").equals(new MockTextMessage("test")));
        assertTrue(new MockObjectMessage(new Integer(1)).equals(new MockObjectMessage(new Integer(1))));
        assertFalse(new MockObjectMessage(new Double(1.1)).equals(new MockObjectMessage(new Double(1.3))));
        assertFalse(new MockObjectMessage(new Long(3)).equals(null));
        assertFalse(new MockObjectMessage(null).equals(null));
        assertTrue(new MockObjectMessage(null).equals(new MockObjectMessage(null)));
        assertEquals(new MockObjectMessage(new Double(1.1)).hashCode(), new MockObjectMessage(new Double(1.1)).hashCode());
        assertEquals(new MockObjectMessage(null).hashCode(), new MockObjectMessage(null).hashCode());
    }
}
