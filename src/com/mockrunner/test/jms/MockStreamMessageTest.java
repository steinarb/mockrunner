package com.mockrunner.test.jms;

import java.util.Arrays;

import javax.jms.MessageEOFException;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotReadableException;
import javax.jms.MessageNotWriteableException;

import junit.framework.TestCase;

import com.mockrunner.mock.jms.MockStreamMessage;

public class MockStreamMessageTest extends TestCase
{
    public void testReadWrite() throws Exception
    {
        MockStreamMessage message = new MockStreamMessage();
        message.writeString("test");
        try
        {
            message.readString();
            fail();
        }
        catch(MessageNotReadableException exc)
        {
            //should throw exception
        }
        message.reset();
        try
        {
            message.writeByte((byte)1);
            fail();
        }
        catch(MessageNotWriteableException exc)
        {
            //should throw exception
        }
        assertEquals("test", message.readString());
        try
        {
            message.readInt();
            fail();
        }
        catch(MessageEOFException exc)
        {
            //should throw exception
        }
        message = new MockStreamMessage();
        message.writeString("123");
        message.writeString("456");
        message.writeString("789");
        message.writeString("111");
        message.writeString("222");
        message.writeString("333");
        message.reset();
        assertEquals(123, message.readByte());
        assertEquals(456, message.readShort());
        assertEquals(789, message.readInt());
        assertEquals(111, message.readLong());
        assertEquals(222, message.readFloat(), 0);
        assertEquals(333, message.readDouble(), 0);
        try
        {
            message.readBoolean();
            fail();
        }
        catch(MessageEOFException exc)
        {
            //should throw exception
        }
        message = new MockStreamMessage();
        message.writeLong(1);
        message.reset();
        try
        {
            message.readInt();
            fail();
        }
        catch(MessageFormatException exc)
        {
            //should throw exception
        }
        message = new MockStreamMessage();
        message.writeByte((byte)1);
        message.writeByte((byte)1);
        message.writeByte((byte)1);
        message.writeByte((byte)1);
        message.reset();
        assertEquals(1, message.readByte());
        assertEquals(1, message.readShort());
        assertEquals(1, message.readInt());
        assertEquals(1, message.readLong());
        message = new MockStreamMessage();
        message.writeBoolean(false);
        message.reset();
        try
        {
            message.readFloat();
            fail();
        }
        catch(MessageFormatException exc)
        {
            //should throw exception
        }
        message = new MockStreamMessage();
        message.writeBoolean(false);
        message.writeBoolean(true);
        message.reset();
        assertEquals(false, message.readBoolean());
        assertEquals("true", message.readString());
        message.clearBody();
        message.writeDouble(123.4);
        message.reset();
        assertEquals("123.4", message.readString());
        message.clearBody();
        message.writeString("a");
        message.reset();
        try
        {
            message.readChar();
            fail();
        }
        catch(MessageFormatException exc)
        {
            //should throw exception
        }
        message.clearBody();
        message.writeChar('a');
        message.reset();
        assertEquals("a", message.readString());
        message = new MockStreamMessage();
        byte[] data = new byte[] {1, 2, 3, 4};
        message.writeBytes(data);
        message.reset();
        data = new byte[4];
        int number = message.readBytes(data);
        assertEquals(4, number);
        assertTrue(Arrays.equals(data, new byte[] {1, 2, 3, 4}));
        message = new MockStreamMessage();
        data = new byte[] {1, 2, 3, 4};
        message.writeBytes(data);
        message.reset();
        data = new byte[5];
        number = message.readBytes(data);
        assertEquals(4, number);
        assertTrue(Arrays.equals(data, new byte[] {1, 2, 3, 4, 0}));
        data = new byte[] {1, 2, 3, 4};
        message.clearBody();
        message.writeBytes(data, 3, 1);
        message.reset();
        data = new byte[5];
        number = message.readBytes(data);
        assertEquals(1, number);
        assertTrue(Arrays.equals(data, new byte[] {4, 0, 0, 0, 0}));
        data = new byte[] {1, 2, 3, 4};
        message.clearBody();
        message.writeBytes(data);
        message.reset();
        data = new byte[1]; 
        number = message.readBytes(data);
        assertEquals(1, number);
        assertTrue(Arrays.equals(data, new byte[] { 1 }));
        data = new byte[] {1, 2, 3, 4};
        message.clearBody();
        message.writeBytes(data);
        message.reset();
        data = new byte[0];
        number = message.readBytes(data);
        assertEquals(0, number);
        data = new byte[] {1, 2, 3, 4};
        message.clearBody();
        message.writeBytes(data);
        message.reset();
        number = message.readBytes(null);
        assertEquals(-1, number);
        message = new MockStreamMessage();
        message.writeObject(null);
        message.writeObject(null);
        message.writeObject(null);
        message.writeObject(new Integer(3));
        message.writeObject("4");
        message.writeObject(new Byte((byte)5));
        message.reset();
        try
        {
            message.readChar();
            fail();
        }
        catch(NullPointerException exc)
        {
            //should throw exception
        }
        try
        {
            message.readDouble();
            fail();
        }
        catch(NullPointerException exc)
        {
            //should throw exception
        }
        assertNull(message.readObject());
        assertEquals(3, message.readLong());
        assertEquals(4, message.readByte());
        try
        {
            message.readFloat();
            fail();
        }
        catch(MessageFormatException exc)
        {
            //should throw exception
        }
        
        try
        {
            message.readFloat();
            fail();
        }
        catch(MessageEOFException exc)
        {
            //should throw exception
        }
    }
}
