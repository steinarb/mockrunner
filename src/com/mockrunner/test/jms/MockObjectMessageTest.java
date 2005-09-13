package com.mockrunner.test.jms;

import java.io.Serializable;

import javax.jms.MessageNotWriteableException;

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
    
    public void testReadOnly() throws Exception
    {
        MockObjectMessage message = new MockObjectMessage("test");
        message.setObject(new Integer(2));
        assertEquals(new Integer(2), message.getObject());
        message.setReadOnly(true);
        try
        {
            message.setObject(new Integer(3));
            fail();
        } 
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        assertEquals(new Integer(2), message.getObject());
        message.clearBody();
        message.setObject(new Integer(3));
        assertEquals(new Integer(3), message.getObject());
    }
    
    public void testClone() throws Exception
    {
        MockObjectMessage message = new MockObjectMessage();
        message.setObject("aText");
        message.setStringProperty("string", "test");
        MockObjectMessage newMessage = (MockObjectMessage)message.clone();
        assertNotSame(message, newMessage);
        assertEquals(message, newMessage);
        assertEquals("aText", newMessage.getObject());
        assertEquals("test", newMessage.getStringProperty("string"));
        message.setObject(new Integer(7));
        newMessage = (MockObjectMessage)message.clone();
        assertEquals(new Integer(7), newMessage.getObject());
        TestObject testObject = new TestObject();
        message.setObject(testObject);
        newMessage = (MockObjectMessage)message.clone();
        assertNotSame(testObject, newMessage.getObject());
    }
    
    public void testToString() throws Exception
    {
        MockObjectMessage message = new MockObjectMessage();
        assertEquals(MockObjectMessage.class.getName() + ": null", message.toString());
        message.setObject(new Integer(3));
        assertEquals(MockObjectMessage.class.getName() + ": 3", message.toString());
        message.setObject(new TestObject());
        assertEquals(MockObjectMessage.class.getName() + ": TestObject", message.toString());
    }
    
    public static class TestObject implements Serializable
    {
        public String toString()
        {    
            return "TestObject";
        }
        
    }
}
