package com.mockrunner.test.jms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.jms.MessageNotWriteableException;

import org.junit.Test;

import junit.framework.TestCase;

import com.mockrunner.mock.jms.MockTextMessage;

public class MockTextMessageTest
{
	@Test
    public void testEquals()
    {
        MockTextMessage message = new MockTextMessage("test");
        assertTrue(message.equals(message));
        assertTrue(new MockTextMessage("test").equals(new MockTextMessage("test")));
        assertFalse(new MockTextMessage("test").equals(new MockTextMessage("test1")));
        assertFalse(new MockTextMessage("test").equals(null));
        assertFalse(new MockTextMessage(null).equals(null));
        assertTrue(new MockTextMessage(null).equals(new MockTextMessage(null)));
        assertEquals(new MockTextMessage("test").hashCode(), new MockTextMessage("test").hashCode());
        assertEquals(new MockTextMessage(null).hashCode(), new MockTextMessage(null).hashCode());
    }
    
	@Test
    public void testReadOnly() throws Exception
    {
        MockTextMessage message = new MockTextMessage("test");
        message.setText("test2");
        assertEquals("test2", message.getText());
        message.setReadOnly(true);
        try
        {
            message.setText("test3");
            fail();
        } 
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        assertEquals("test2", message.getText());
        message.clearBody();
        message.setText("test3");
        assertEquals("test3", message.getText());
    }
    
	@Test
    public void testClone() throws Exception
    {
        MockTextMessage message = new MockTextMessage();
        message.setText("aText");
        message.setStringProperty("string", "test");
        message.setJMSPriority(3);
        MockTextMessage newMessage = (MockTextMessage)message.clone();
        assertNotSame(message, newMessage);
        assertEquals(message, newMessage);
        assertEquals("aText", newMessage.getText());
        assertEquals("test", newMessage.getStringProperty("string"));
        assertEquals(3, newMessage.getJMSPriority());
    }
    
	@Test
    public void testToString() throws Exception
    {
        MockTextMessage message = new MockTextMessage();
        assertEquals(MockTextMessage.class.getName() + ": null", message.toString());
        message.setText("Hello World");
        assertEquals(MockTextMessage.class.getName() + ": Hello World", message.toString());
    }
}
