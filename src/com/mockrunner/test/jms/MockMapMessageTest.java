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
}
