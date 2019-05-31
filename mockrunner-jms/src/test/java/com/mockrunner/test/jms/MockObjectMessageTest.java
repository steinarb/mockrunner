package com.mockrunner.test.jms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Serializable;

import javax.jms.MessageNotWriteableException;

import org.junit.Test;

import com.mockrunner.mock.jms.MockObjectMessage;
import com.mockrunner.mock.jms.MockTextMessage;

public class MockObjectMessageTest
{
	@Test
    public void testEquals()
    {
        MockObjectMessage message = new MockObjectMessage("test");
        assertTrue(message.equals(message));
        assertTrue(new MockObjectMessage("test").equals(new MockObjectMessage("test")));
        assertFalse(new MockObjectMessage("test").equals(new MockTextMessage("test")));
        assertTrue(new MockObjectMessage(1).equals(new MockObjectMessage(1)));
        assertFalse(new MockObjectMessage(1.1).equals(new MockObjectMessage(1.3)));
        assertFalse(new MockObjectMessage(3L).equals(null));
        assertFalse(new MockObjectMessage(null).equals(null));
        assertTrue(new MockObjectMessage(null).equals(new MockObjectMessage(null)));
        assertEquals(new MockObjectMessage(1.1).hashCode(), new MockObjectMessage(1.1).hashCode());
        assertEquals(new MockObjectMessage(null).hashCode(), new MockObjectMessage(null).hashCode());
    }
    
	@Test
    public void testReadOnly() throws Exception
    {
        MockObjectMessage message = new MockObjectMessage("test");
        message.setObject(2);
        assertEquals(2, message.getObject());
        message.setReadOnly(true);
        try
        {
            message.setObject(3);
            fail();
        } 
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        assertEquals(2, message.getObject());
        message.clearBody();
        message.setObject(3);
        assertEquals(3, message.getObject());
    }
    
	@Test
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
        message.setObject(7);
        newMessage = (MockObjectMessage)message.clone();
        assertEquals(7, newMessage.getObject());
        TestObject testObject = new TestObject();
        message.setObject(testObject);
        newMessage = (MockObjectMessage)message.clone();
        assertNotSame(testObject, newMessage.getObject());
    }
    
	@Test
    public void testToString() throws Exception
    {
        MockObjectMessage message = new MockObjectMessage();
        assertEquals(MockObjectMessage.class.getName() + ": null", message.toString());
        message.setObject(3);
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
