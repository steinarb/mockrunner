package com.mockrunner.test.jms;

import java.util.Arrays;

import javax.jms.MessageEOFException;
import javax.jms.MessageNotReadableException;
import javax.jms.MessageNotWriteableException;

import com.mockrunner.mock.jms.MockBytesMessage;

import junit.framework.TestCase;

public class MockBytesMessageTest extends TestCase
{   
    public void testReadWrite() throws Exception
    {
        MockBytesMessage message = new MockBytesMessage();
        message.writeChar('t');
        message.writeUTF("est");
        message.writeByte((byte)1);
        message.writeLong(2345);
        message.reset();
        try
        {
            message.writeDouble(678.9);
            fail();
        }
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        assertEquals('t', message.readChar());
        assertEquals("est", message.readUTF());
        assertEquals(1, message.readByte());
        assertEquals(2345, message.readLong());
        try
        {
            message.readByte();
            fail();
        }
        catch(MessageEOFException exc)
        {
            //should throw exception
        }
        byte[] data = new byte[] {1, 2, 3, 4, 5};
        message = new MockBytesMessage();
        message.writeBytes(data);
        message.writeBytes(data, 1, 2);
        message.reset();
        try
        {
            message.writeBytes(data);
            fail();
        }
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        data = new byte[7];
        message.readBytes(data);
        assertTrue(Arrays.equals(data, new byte[] {1, 2, 3, 4, 5, 2, 3}));
        try
        {
            message.readByte();
            fail();
        }
        catch(MessageEOFException exc)
        {
            //should throw exception
        }
        message.clearBody();
        try
        {
            message.readByte();
            fail();
        }
        catch(MessageNotReadableException exc)
        {
            //should throw exception
        }
        message.writeDouble(1.2);
        try
        {
            message.readDouble();
            fail();
        }
        catch(MessageNotReadableException exc)
        {
            //should throw exception
        }
        message.reset();
        assertEquals(1.2, message.readDouble(), 0);
        message = new MockBytesMessage();
        message.writeObject(new Boolean(true));
        message.writeObject(new Long(1));
        message.writeObject("xyz");
        message.writeObject(new byte[] { 1 });
        message.reset();
        assertEquals(true, message.readBoolean());
        assertEquals(1, message.readLong());
        assertEquals("xyz", message.readUTF());
        data = new byte[1];
        message.readBytes(data, 1);
        assertTrue(Arrays.equals(data, new byte[] { 1 }));
    }
}
