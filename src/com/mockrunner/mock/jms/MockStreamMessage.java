package com.mockrunner.mock.jms;

import java.util.Stack;

import javax.jms.JMSException;
import javax.jms.MessageEOFException;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotReadableException;
import javax.jms.MessageNotWriteableException;
import javax.jms.StreamMessage;

import com.mockrunner.util.ArrayUtil;

/**
 * Mock implementation of JMS <code>StreamMessage</code>.
 */
public class MockStreamMessage extends MockMessage implements StreamMessage
{
    private Stack data;
    private boolean isInWriteMode;
    
    public MockStreamMessage()
    {
        data = new Stack();
        isInWriteMode = true;
    }
    
    public boolean readBoolean() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        return false;
    }

    public byte readByte() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        return 0;
    }

    public short readShort() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        return 0;
    }

    public char readChar() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        return 0;
    }

    public int readInt() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        return 0;
    }

    public long readLong() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        return 0;
    }

    public float readFloat() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        return 0;
    }

    public double readDouble() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        return 0;
    }

    public String readString() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        Object value = readObject();
        if(null == value) return null;
        if((value instanceof Character ) || (value instanceof byte[]))
        {
            throw new MessageFormatException(value.getClass().getName() + " cannot be converted to String");
        }
        return value.toString();
    }

    public int readBytes(byte[] arg0) throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        return 0;
    }

    public Object readObject() throws JMSException
    {
        if(isInWriteMode)
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        return data.pop();
    }

    public void writeBoolean(boolean value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Boolean(value));
    }

    public void writeByte(byte value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Byte(value));
    }

    public void writeShort(short value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Short(value));
    }

    public void writeChar(char value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Character(value));
    }

    public void writeInt(int value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Integer(value));
    }

    public void writeLong(long value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Long(value));
    }

    public void writeFloat(float value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Float(value));
    }

    public void writeDouble(double value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Double(value));
    }

    public void writeString(String value) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(value);
    }

    public void writeBytes(byte[] data) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(data);
    }

    public void writeBytes(byte[] data, int offset, int length) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(ArrayUtil.truncateArray(data, offset, length));
    }

    public void writeObject(Object object) throws JMSException
    {
        if(!isInWriteMode)
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        if((object instanceof String) || (object instanceof Number) || (object instanceof Character) || (object instanceof Boolean))
        {
            data.push(object);
            return;
        }
        if(object instanceof byte[])
        {
            byte[] arrayData = (byte[])ArrayUtil.copyArray(object);
            data.push(arrayData);
            return;
        }
        throw new MessageFormatException(object.getClass() + " not a valid type");
    }

    public void reset() throws JMSException
    {
        isInWriteMode = false;
    }

    public void clearBody() throws JMSException
    {
        super.clearBody();
        data = new Stack();
        isInWriteMode = true;
    }
}
