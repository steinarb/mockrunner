package com.mockrunner.mock.jms;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotWriteableException;

import com.mockrunner.util.common.ArrayUtil;

/**
 * Mock implementation of JMS <code>MapMessage</code>.
 */
public class MockMapMessage extends MockMessage implements MapMessage
{
    private Map data;
    
    public MockMapMessage()
    {
        data = new HashMap();
    }

    public boolean getBoolean(String name) throws JMSException
    {
        Object value = getObject(name);
        if(null == value)
        {
            return Boolean.valueOf(null).booleanValue();
        }
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

    public byte getByte(String name) throws JMSException
    {
        Object value = getObject(name);
        if(null == value)
        {
            return Byte.valueOf(null).byteValue();
        }
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

    public short getShort(String name) throws JMSException
    {
        Object value = getObject(name);
        if(null == value)
        {
            return Short.valueOf(null).shortValue();
        }
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

    public char getChar(String name) throws JMSException
    {
        Object value = getObject(name);
        if(null == value)
        {
            throw new NullPointerException();
        }
        if(!(value instanceof Character))
        {
            throw new MessageFormatException(value.getClass().getName() + " cannot be converted to char");
        }
        return ((Character)value).charValue();
    }

    public int getInt(String name) throws JMSException
    {
        Object value = getObject(name);
        if(null == value)
        {
            return Integer.valueOf(null).intValue();
        }
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

    public long getLong(String name) throws JMSException
    {
        Object value = getObject(name);
        if(null == value)
        {
            return Long.valueOf(null).longValue();
        }
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

    public float getFloat(String name) throws JMSException
    {
        Object value = getObject(name);
        if(null == value)
        {
            return Float.valueOf(null).floatValue();
        }
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

    public double getDouble(String name) throws JMSException
    {
        Object value = getObject(name);
        if(null == value)
        {
            return Double.valueOf(null).doubleValue();
        }
        if((value instanceof Double) || (value instanceof Float))
        {
            return ((Number)value).doubleValue();
        }
        if(value instanceof String)
        {
            return Double.valueOf((String)value).doubleValue();
        }
        throw new MessageFormatException(value.getClass().getName() + " cannot be converted to double");
    }

    public String getString(String name) throws JMSException
    {
        Object value = getObject(name);
        if(null == value)
        {
            return null;
        }
        if(value instanceof byte[])
        {
            throw new MessageFormatException(value.getClass().getName() + " cannot be converted to String");
        }
        return value.toString();
    }

    public byte[] getBytes(String name) throws JMSException
    {
        Object value = getObject(name);
        if(null == value)
        {
            throw new NullPointerException();
        }
        if(!(value instanceof byte[]))
        {
            throw new MessageFormatException(value.getClass().getName() + " cannot be converted to byte[]");
        }
        return (byte[])value;
    }

    public Object getObject(String name) throws JMSException
    {
        return data.get(name);
    }

    public Enumeration getMapNames() throws JMSException
    {
        return new Vector(data.keySet()).elements();
    }

    public void setBoolean(String name, boolean value) throws JMSException
    {
        setObject(name, new Boolean(value));
    }

    public void setByte(String name, byte value) throws JMSException
    {
        setObject(name, new Byte(value));
    }

    public void setShort(String name, short value) throws JMSException
    {
        setObject(name, new Short(value));
    }

    public void setChar(String name, char value) throws JMSException
    {
        setObject(name, new Character(value));
    }

    public void setInt(String name, int value) throws JMSException
    {
        setObject(name, new Integer(value));
    }

    public void setLong(String name, long value) throws JMSException
    {
        setObject(name, new Long(value));
    }

    public void setFloat(String name, float value) throws JMSException
    {
        setObject(name, new Float(value));
    }

    public void setDouble(String name, double value) throws JMSException
    {
        setObject(name, new Double(value));
    }

    public void setString(String name, String value) throws JMSException
    {
        setObject(name, value);
    }

    public void setBytes(String name, byte[] byteData) throws JMSException
    {
        byte[] copy = (byte[])ArrayUtil.copyArray(byteData);
        setObject(name, copy);
    }

    public void setBytes(String name, byte[] byteData, int offset, int length) throws JMSException
    {
        if(null == byteData)
        {
            setObject(name, null);
            return;
        }
        setBytes(name, (byte[])ArrayUtil.truncateArray(byteData, offset, length));
    }

    public void setObject(String name, Object object) throws JMSException
    {
        if(!isInWriteMode())
        {
            throw new MessageNotWriteableException("Message is in read mode");
        }
        if(null == name || name.length() <= 0)
        {
            throw new IllegalArgumentException("Property names must not be null or empty strings");
        }
        if(null == object) return;
        if((object instanceof Number) || (object instanceof Boolean) || (object instanceof Character) || (object instanceof String) || (object instanceof byte[]))
        {
            data.put(name, object);
            return;
        }
        throw new MessageFormatException(object.getClass().getName() + " not a valid type");
    }

    public boolean itemExists(String name) throws JMSException
    {
        return data.containsKey(name);
    }
    
    public void clearBody() throws JMSException
    {
        super.clearBody();
        data.clear();
    }
    
    /**
     * Returns a copy of the underlying data as a <code>Map</code>
     * regardless if the message is in read or write mode. Primitives
     * are wrapped into their corresponding type.
     * @return the <code>Map</code> data
     */
    public Map getMap()
    {
        Map map = new HashMap();
        copyDataToMap(map);
        return map;
    }
    
    /**
     * Compares the underlying map data.
     */
    public boolean equals(Object otherObject)
    {
        if(null == otherObject) return false;
        if(!(otherObject instanceof MockMapMessage)) return false;
        MockMapMessage otherMessage = (MockMapMessage)otherObject;
        if(data.size() != otherMessage.data.size()) return false;
        Iterator keys = data.keySet().iterator();
        while(keys.hasNext())
        {
            Object nextKey = keys.next();
            Object nextValue = data.get(nextKey);
            Object otherValue = otherMessage.data.get(nextKey);
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
        Iterator values = data.values().iterator();
        while(values.hasNext())
        {
            Object nextValue = values.next();
            if(nextValue instanceof byte[])
            {
                for(int ii = 0; ii < ((byte[])nextValue).length; ii++)
                {
                    value += 31 * ((byte[])nextValue)[ii];
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
        MockMapMessage message = (MockMapMessage)super.clone();
        message.data = new HashMap(data.size());
        copyDataToMap(message.data);
        return message;
    }
    
    private void copyDataToMap(Map target)
    {
        Iterator keys = data.keySet().iterator();
        while(keys.hasNext())
        {
            Object nextKey = keys.next();
            Object nextValue = data.get(nextKey);
            if(nextValue instanceof byte[])
            {
                target.put(nextKey, ArrayUtil.copyArray(nextValue));
            }
            else
            {
                target.put(nextKey, nextValue);
            }
        }
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getClass().getName() + ": " + data.toString());
        return buffer.toString();
    }
}
