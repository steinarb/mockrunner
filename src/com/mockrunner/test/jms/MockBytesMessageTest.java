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
    
    public void testEquals() throws Exception
    {
        MockBytesMessage message1 = new MockBytesMessage();
        message1.writeChar('t');
        message1.writeUTF("est");
        message1.writeByte((byte)1);
        message1.writeLong(2345);
        MockBytesMessage message2 = new MockBytesMessage();
        message2.writeChar('t');
        message2.writeUTF("est");
        message2.writeByte((byte)1);
        assertFalse(message1.equals(message2));
        assertFalse(message2.equals(message1));
        message2.writeLong(2345);
        assertTrue(message1.equals(message2));
        assertTrue(message2.equals(message1));
        assertEquals(message1.hashCode(), message2.hashCode());
        message1.reset();
        assertTrue(message1.equals(message2));
        assertTrue(message2.equals(message1));
        assertTrue(new MockBytesMessage().equals(new MockBytesMessage()));
        assertEquals(new MockBytesMessage().hashCode(), new MockBytesMessage().hashCode());
        message1 = new MockBytesMessage();
        message1.writeUTF("test");
        message1.writeInt(3);
        message2 = new MockBytesMessage();
        message2.writeInt(3);
        message2.writeUTF("test");
        assertFalse(message1.equals(message2));
    }
}
