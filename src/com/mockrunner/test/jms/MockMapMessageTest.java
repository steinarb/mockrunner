package com.mockrunner.test.jms;

import java.util.Arrays;

import javax.jms.MessageFormatException;

import com.mockrunner.mock.jms.MockMapMessage;

import junit.framework.TestCase;

public class MockMapMessageTest extends TestCase
{
    public void testReadWrite() throws Exception
    {
        MockMapMessage message = new MockMapMessage();
        message.setBoolean("boolean1", true);
        assertEquals("true", message.getString("boolean1"));
        assertEquals(true, message.getBoolean("boolean1"));
        message.setString("string1", "12.3");
        assertEquals(12.3, message.getFloat("string1"), 0.01);
        assertEquals(12.3, message.getDouble("string1"), 0.01);
        assertEquals("12.3", message.getString("string1"));
        assertEquals("12.3", message.getObject("string1"));
        try
        {
            message.getLong("string1");
            fail();
        }
        catch(NumberFormatException exc)
        {
            //should throw exception
        }
        message.setObject("byteObject1", new Byte((byte)1));
        assertEquals("1", message.getString("byteObject1"));
        assertEquals(1, message.getLong("byteObject1"));
        assertEquals(1, message.getInt("byteObject1"));
        assertEquals(1, message.getShort("byteObject1"));
        assertEquals(1, message.getByte("byteObject1"));
        try
        {
            message.getDouble("byteObject1");
            fail();
        }
        catch(MessageFormatException exc)
        {
            //should throw exception
        }
        message.setInt("int1", 123);
        assertEquals(123, message.getLong("int1")); 
        try
        {
            message.getShort("int1");
            fail();
        }
        catch(MessageFormatException exc)
        {
            //should throw exception
        }
        byte[] data = new byte[] {1, 2, 3};
        message.setBytes("bytes1", data);
        assertTrue(Arrays.equals(message.getBytes("bytes1"), new byte[] {1, 2, 3}));
        message.setBytes("bytes2", data, 1, 2);
        assertTrue(Arrays.equals(message.getBytes("bytes2"), new byte[] {2, 3}));
        try
        {
            message.getString("bytes1");
            fail();
        }
        catch(MessageFormatException exc)
        {
            //should throw exception
        }
        assertTrue(Arrays.equals((byte[])message.getObject("bytes1"), new byte[] {1, 2, 3}));
        message.setObject("null", null);
        try
        {
            message.getInt("null");
            fail();
        }
        catch(NumberFormatException exc)
        {
            //should throw exception
        }
        assertNull(message.getString("null"));
    }
    
    public void testEquals() throws Exception
    {
        MockMapMessage message1 = new MockMapMessage();
        message1.setInt("name1", 1);
        message1.setString("name2", "text");
        message1.setBytes("name3", new byte[] {1, 2, 3});
        MockMapMessage message2 = null;
        assertFalse(message1.equals(message2));
        message2 = new MockMapMessage();
        assertFalse(message1.equals(message2));
        assertTrue(message2.equals(new MockMapMessage()));
        assertEquals(message2.hashCode(), new MockMapMessage().hashCode());
        message2.setInt("name1", 1);
        message2.setString("name2", "text");
        message2.setBytes("name3", new byte[] {1, 2, 1});
        assertFalse(message1.equals(message2));
        assertFalse(message2.equals(message1));
        message2.setBytes("name3", new byte[] {1, 2, 3});
        assertTrue(message1.equals(message2));
        assertTrue(message2.equals(message1));
        assertEquals(message1.hashCode(), message2.hashCode());
        message2.setString("name4", "text");
        assertFalse(message1.equals(message2));
        message2.setString("name4", null);
        assertFalse(message1.equals(message2));
        assertFalse(message2.equals(message1));
        message1.setString("name4", null);
        assertTrue(message1.equals(message2));
        assertTrue(message2.equals(message1));
        assertEquals(message1.hashCode(), message2.hashCode());
        message2.setBytes("name3", new byte[] {});
        assertFalse(message1.equals(message2));
        assertFalse(message2.equals(message1));
    }
}
