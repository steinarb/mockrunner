package com.mockrunner.mock.jms;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.MessageEOFException;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotReadableException;
import javax.jms.MessageNotWriteableException;
import javax.jms.StreamMessage;

import com.mockrunner.util.common.ArrayUtil;

/**
 * Mock implementation of JMS <code>StreamMessage</code>.
 */
public class MockStreamMessage extends MockMessage implements StreamMessage
{
    private Stack data;
    
    public MockStreamMessage()
    {
        data = new Stack();
    }
    
    public boolean readBoolean() throws JMSException
    {
        if(isInWriteMode())
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        Object value = readObject();
        if(null == value) return Boolean.valueOf(null).booleanValue();
        if(value instanceof Boolean)
        {
            return ((Boolean)value).booleanValue();
        }
        if(value instanceof String)
        {
            return Boolean.valueOf((String)value).booleanValue();
        }
        throw new MessageFormatException(value.getClass().getName() + " cannot be converted to boolean");
    }

    public byte readByte() throws JMSException
    {
        if(isInWriteMode())
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        Object value = readObject();
        if(null == value) return Byte.valueOf(null).byteValue();
        if(value instanceof Byte)
        {
            return ((Byte)value).byteValue();
        }
        if(value instanceof String)
        {
            return Byte.valueOf((String)value).byteValue();
        }
        throw new MessageFormatException(value.getClass().getName() + " cannot be converted to byte");
    }

    public short readShort() throws JMSException
    {
        if(isInWriteMode())
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        Object value = readObject();
        if(null == value) return Short.valueOf(null).shortValue();
        if((value instanceof Byte) || (value instanceof Short))
        {
            return ((Number)value).shortValue();
        }
        if(value instanceof String)
        {
            return Short.valueOf((String)value).shortValue();
        }
        throw new MessageFormatException(value.getClass().getName() + " cannot be converted to short");
    }

    public char readChar() throws JMSException
    {
        if(isInWriteMode())
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        Object value = readObject();
        if(null == value)
        {
            throw new NullPointerException();
        }
        if(value instanceof Character)
        {
            return ((Character)value).charValue();
        }
        throw new MessageFormatException(value.getClass().getName() + " cannot be converted to char");
    }

    public int readInt() throws JMSException
    {
        if(isInWriteMode())
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        Object value = readObject();
        if(null == value) return Integer.valueOf(null).intValue();
        if((value instanceof Byte) || (value instanceof Short) || (value instanceof Integer))
        {
            return ((Number)value).intValue();
        }
        if(value instanceof String)
        {
            return Integer.valueOf((String)value).intValue();
        }
        throw new MessageFormatException(value.getClass().getName() + " cannot be converted to int");
    }

    public long readLong() throws JMSException
    {
        if(isInWriteMode())
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        Object value = readObject();
        if(null == value) return Long.valueOf(null).longValue();
        if((value instanceof Byte) || (value instanceof Short) || (value instanceof Integer) || (value instanceof Long))
        {
            return ((Number)value).longValue();
        }
        if(value instanceof String)
        {
            return Long.valueOf((String)value).longValue();
        }
        throw new MessageFormatException(value.getClass().getName() + " cannot be converted to long");
    }

    public float readFloat() throws JMSException
    {
        if(isInWriteMode())
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        Object value = readObject();
        if(null == value) return Float.valueOf(null).floatValue();
        if(value instanceof Float)
        {
            return ((Float)value).floatValue();
        }
        if(value instanceof String)
        {
            return Float.valueOf((String)value).floatValue();
        }
        throw new MessageFormatException(value.getClass().getName() + " cannot be converted to float");
    }

    public double readDouble() throws JMSException
    {
        if(isInWriteMode())
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        Object value = readObject();
        if(null == value) return Double.valueOf(null).doubleValue();
        if((value instanceof Float) || (value instanceof Double))
        {
            return ((Number)value).doubleValue();
        }
        if(value instanceof String)
        {
            return Double.valueOf((String)value).doubleValue();
        }
        throw new MessageFormatException(value.getClass().getName() + " cannot be converted to double");
    }

    public String readString() throws JMSException
    {
        if(isInWriteMode())
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        Object value = readObject();
        if(null == value) return null;
        if(value instanceof byte[])
        {
            throw new MessageFormatException(value.getClass().getName() + " cannot be converted to String");
        }
        return value.toString();
    }

    public int readBytes(byte[] byteData) throws JMSException
    {
        if(isInWriteMode())
        {
            throw new MessageNotReadableException("Message is in write mode");
        }
        if(data.empty())
        {
            throw new MessageEOFException("No more data");
        }
        if(null == byteData) return -1;
        if(0 == byteData.length) return 0;
        Object value = readObject();
        if(null == value)
        {
            throw new NullPointerException();
        }
        if(!(value instanceof byte[]))
        {
            throw new MessageFormatException(value.getClass().getName() + " cannot be converted to byte[]");
        }
        int length = Math.min(byteData.length, ((byte[])value).length);
        System.arraycopy(value, 0, byteData, 0, length);
        return length;
    }

    public Object readObject() throws JMSException
    {
        if(isInWriteMode())
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
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Boolean(value));
    }

    public void writeByte(byte value) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Byte(value));
    }

    public void writeShort(short value) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Short(value));
    }

    public void writeChar(char value) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Character(value));
    }

    public void writeInt(int value) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Integer(value));
    }

    public void writeLong(long value) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Long(value));
    }

    public void writeFloat(float value) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Float(value));
    }

    public void writeDouble(double value) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(new Double(value));
    }

    public void writeString(String value) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(value);
    }

    public void writeBytes(byte[] data) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        writeObject(data);
    }

    public void writeBytes(byte[] data, int offset, int length) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        if(null == data)
        {
            writeObject(null);
            return;
        }
        writeObject(ArrayUtil.truncateArray(data, offset, length));
    }

    public void writeObject(Object object) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        if(null == object)
        {
            data.push(object);
            return;
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
        setReadOnly(true);
        Collections.reverse(data);
    }

    public void clearBody() throws JMSException
    {
        super.clearBody();
        data = new Stack();
    }
    
    /**
     * Compares the underlying stream data.
     */
    public boolean equals(Object otherObject)
    {
        if(null == otherObject) return false;
        if(!(otherObject instanceof MockStreamMessage)) return false;
        MockStreamMessage otherMessage = (MockStreamMessage)otherObject;
        if(data.size() != otherMessage.data.size()) return false;
        Vector otherData = otherMessage.data;
        if(isInWriteMode() != otherMessage.isInWriteMode())
        {
            otherData = new Vector(otherData);
            Collections.reverse(otherData);
        }
        for(int ii = 0; ii < data.size(); ii++)
        {
            Object nextValue = data.get(ii);
            Object otherValue = otherData.get(ii);
            if(null == nextValue)
            {
                if(null != otherValue) return false;
            }
            else if(nextValue instanceof byte[])
            {
                if(null == otherValue) return false;
                if(!(otherValue instanceof byte[])) return false;
                if(!Arrays.equals((byte[])nextValue, (byte[])otherValue)) return false;
            }
            else
            {
                if(!nextValue.equals(otherValue)) return false;
            }
        }
        return true;
    }

    public int hashCode()
    {
        int value = 0;
        for(int ii = 0; ii < data.size(); ii++)
        {
            Object nextValue = data.get(ii);
            if(nextValue instanceof byte[])
            {
                for(int yy = 0; yy < ((byte[])nextValue).length; yy++)
                {
                    value += 31 * ((byte[])nextValue)[yy];
                }
            }
            else if(nextValue != null)
            {
                value += 31 * nextValue.hashCode();
            }
        }
        return value;
    }
    
    public Object clone()
    {
        MockStreamMessage message = (MockStreamMessage)super.clone();
        message.data = new Stack();
        for(int ii = 0; ii < data.size(); ii++)
        {
            Object nextValue = data.get(ii);
            if(nextValue instanceof byte[])
            {
                message.data.add(ArrayUtil.copyArray(nextValue));
            }
            else
            {
                message.data.add(nextValue);
            }
        }
        return message;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getClass().getName() + ": " + data.toString());
        return buffer.toString();
    }
}
