package com.mockrunner.mock.jms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageEOFException;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotReadableException;
import javax.jms.MessageNotWriteableException;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of JMS <code>BytesMessage</code>.
 */
public class MockBytesMessage extends MockMessage implements BytesMessage
{
    private DataOutputStream outStream;
    private ByteArrayOutputStream byteOutStream;
    private DataInputStream inStream;
    private boolean isInWriteMode;
    
    public MockBytesMessage()
    {
        try
        {
            clearBody();
        }
        catch(JMSException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    public long getBodyLength() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        return outStream.size();
    }

    public boolean readBoolean() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.readBoolean();
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public byte readByte() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.readByte();
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public int readUnsignedByte() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.readByte();
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public short readShort() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.readShort();
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public int readUnsignedShort() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.readShort();
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public char readChar() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.readChar();
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public int readInt() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.readInt();
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public long readLong() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.readLong();
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public float readFloat() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.readFloat();
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public double readDouble() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.readDouble();
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public String readUTF() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.readUTF();
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public int readBytes(byte[] data) throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.read(data);
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public int readBytes(byte[] data, int length) throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        try
        {
            return inStream.read(data, 0, length);
        }
        catch(EOFException exc)
        {
            throw new MessageEOFException(exc.getMessage());
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public void writeBoolean(boolean value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        try
        {
            outStream.writeBoolean(value);
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public void writeByte(byte value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        try
        {
            outStream.writeByte(value);
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public void writeShort(short value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        try
        {
            outStream.writeShort(value);
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public void writeChar(char value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        try
        {
            outStream.writeChar(value);
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public void writeInt(int value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        try
        {
            outStream.writeInt(value);
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public void writeLong(long value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        try
        {
            outStream.writeLong(value);
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public void writeFloat(float value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        try
        {
            outStream.writeFloat(value);
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public void writeDouble(double value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        try
        {
            outStream.writeDouble(value);
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public void writeUTF(String value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        try
        {
            outStream.writeUTF(value);
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public void writeBytes(byte[] data) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        try
        {
            outStream.write(data);
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public void writeBytes(byte[] data, int offset, int length) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        try
        {
            outStream.write(data, offset, length);
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
    }

    public void writeObject(Object object) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        if(object instanceof Byte) 
        {
            writeByte(((Byte)object).byteValue());
            return;
        }
        if(object instanceof Short) 
        {
            writeShort(((Short)object).shortValue());
            return;
        }
        if(object instanceof Integer) 
        {
            writeInt(((Integer)object).intValue());
            return;
        }
        if(object instanceof Long) 
        {
            writeLong(((Long)object).longValue());
            return;
        }
        if(object instanceof Float) 
        {
            writeFloat(((Float)object).floatValue());
            return;
        }
        if(object instanceof Double) 
        {
            writeDouble(((Double)object).doubleValue());
            return;
        }
        if(object instanceof Character) 
        {
            writeChar(((Character)object).charValue());
            return;
        }
        if(object instanceof Boolean) 
        {
            writeBoolean(((Boolean)object).booleanValue());
            return;
        }
        if(object instanceof String) 
        {
            writeUTF((String)object);
            return;
        }
        if(object instanceof byte[])
        {
            writeBytes((byte[])object);
            return;
        }
        throw new MessageFormatException(object.getClass().getName() + " is an invalid type");
    }

    public void reset() throws JMSException
    {
        isInWriteMode = false;
        try
        {
            outStream.flush();
        }
        catch(IOException exc)
        {
            throw new JMSException(exc.getMessage());
        }
        inStream = new DataInputStream(new ByteArrayInputStream(byteOutStream.toByteArray()));
    }
    
    public void clearBody() throws JMSException
    {
        super.clearBody();
        isInWriteMode = true;
        byteOutStream = new ByteArrayOutputStream();
        outStream = new DataOutputStream(byteOutStream);
    }
    
    /**
     * Compares the underlying byte data.
     */
    public boolean equals(Object otherObject)
    {
        if(null == otherObject) return false;
        if(!(otherObject instanceof MockBytesMessage)) return false;
        MockBytesMessage otherMessage = (MockBytesMessage)otherObject;
        byte[] firstData = byteOutStream.toByteArray();
        byte[] secondData = otherMessage.byteOutStream.toByteArray();
        return Arrays.equals(firstData, secondData);
    }

    public int hashCode()
    {
        int value = 0;
        byte[] data = byteOutStream.toByteArray();
        for(int ii = 0; ii < data.length; ii++)
        {
            value += data[ii];
        }
        return value;
    }
    
    public Object clone()
    {
        MockBytesMessage message = (MockBytesMessage)super.clone();
        try
        {
            message.clearBody();
            message.outStream.write(byteOutStream.toByteArray());
            if(!isInWriteMode)
            {
                message.reset();
            }
            return message;
        }
        catch(Exception exc)
        {
            throw new RuntimeException(exc.getMessage());
        }
    }
}
