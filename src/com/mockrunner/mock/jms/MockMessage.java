package com.mockrunner.mock.jms;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageFormatException;

import com.mockrunner.util.ArrayUtil;

/**
 * Mock implementation of JMS <code>Message</code>.
 */
public class MockMessage implements Message
{
    private String messageId;
    private long timestamp;
    private byte[] correlationId;
    private Destination replyTo;
    private Destination destination;
    private int deliveryMode;
    private boolean redelivered;
    private String type;
    private long expiration;
    private int priority;
    private boolean acknowledged;
    private Map properties;
    
    public MockMessage()
    {
        messageId = String.valueOf(Math.random());
        timestamp = System.currentTimeMillis();
        deliveryMode = DeliveryMode.NON_PERSISTENT;
        redelivered = false;
        expiration = 0;
        priority = 4;
        acknowledged = false;
        properties = new HashMap();
    }
    
    public boolean isAcknowledged()
    {
        return acknowledged;
    }
    
    public String getJMSMessageID() throws JMSException
    {
        return messageId;
    }

    public void setJMSMessageID(String messageId) throws JMSException
    {
        this.messageId = messageId;
    }

    public long getJMSTimestamp() throws JMSException
    {
        return timestamp;
    }

    public void setJMSTimestamp(long timestamp) throws JMSException
    {
        this.timestamp = timestamp;
    }

    public byte[] getJMSCorrelationIDAsBytes() throws JMSException
    {
        return correlationId;
    }

    public void setJMSCorrelationIDAsBytes(byte[] correlationId) throws JMSException
    {
        this.correlationId = (byte[])ArrayUtil.copyArray(correlationId);
    }

    public void setJMSCorrelationID(String correlationId) throws JMSException
    {
        this.correlationId = (byte[])ArrayUtil.copyArray(correlationId.getBytes());
    }

    public String getJMSCorrelationID() throws JMSException
    {
        return new String(correlationId);
    }

    public Destination getJMSReplyTo() throws JMSException
    {
        return replyTo;
    }

    public void setJMSReplyTo(Destination replyTo) throws JMSException
    {
        this.replyTo = replyTo;
    }

    public Destination getJMSDestination() throws JMSException
    {
        return destination;
    }

    public void setJMSDestination(Destination destination) throws JMSException
    {
        this.destination = destination;
    }

    public int getJMSDeliveryMode() throws JMSException
    {
        return deliveryMode;
    }

    public void setJMSDeliveryMode(int deliveryMode) throws JMSException
    {
        this.deliveryMode = deliveryMode;
    }

    public boolean getJMSRedelivered() throws JMSException
    {
        return redelivered;
    }

    public void setJMSRedelivered(boolean redelivered) throws JMSException
    {
        this.redelivered = redelivered;
    }

    public String getJMSType() throws JMSException
    {
        return type;
    }

    public void setJMSType(String type) throws JMSException
    {
        this.type = type;
    }

    public long getJMSExpiration() throws JMSException
    {
        return expiration;
    }

    public void setJMSExpiration(long expiration) throws JMSException
    {
        this.expiration = expiration;
    }

    public int getJMSPriority() throws JMSException
    {
        return priority;
    }

    public void setJMSPriority(int priority) throws JMSException
    {
        this.priority = priority;
    }

    public void clearProperties() throws JMSException
    {
        properties.clear();
    }

    public boolean propertyExists(String name) throws JMSException
    {
        return properties.containsKey(name);
    }

    public boolean getBooleanProperty(String name) throws JMSException
    {
        Object value = getObjectProperty(name);
        if(value == null)
        {
            throw new MessageFormatException("Property " + name + " is null");
        }
        if(value instanceof String)
        {
            return Boolean.valueOf((String)value).booleanValue();
        }
        if(value instanceof Boolean)
        {
            return ((Boolean)value).booleanValue();
        }
        throw new MessageFormatException("Cannot convert property " + name + " of type " + value.getClass().getName() + " to boolean");
    }

    public byte getByteProperty(String name) throws JMSException
    {
        Object value = getObjectProperty(name);
        if(value == null)
        {
            throw new MessageFormatException("Property " + name + " is null");
        }
        if(value instanceof String)
        {
            return Byte.valueOf((String)value).byteValue();
        }
        if(value instanceof Byte)
        {
            return ((Number)value).byteValue();
        }
        throw new MessageFormatException("Cannot convert property " + name + " of type " + value.getClass().getName() + " to byte");
    }

    public short getShortProperty(String name) throws JMSException
    {
        Object value = getObjectProperty(name);
        if(value == null)
        {
            throw new MessageFormatException("Property " + name + " is null");
        }
        if(value instanceof String)
        {
            return Short.valueOf((String)value).shortValue();
        }
        if((value instanceof Short) || (value instanceof Byte))
        {
            return ((Number)value).shortValue();
        }
        throw new MessageFormatException("Cannot convert property " + name + " of type " + value.getClass().getName() + " to short");
    }

    public int getIntProperty(String name) throws JMSException
    {
        Object value = getObjectProperty(name);
        if(value == null)
        {
            throw new MessageFormatException("Property " + name + " is null");
        }
        if(value instanceof String)
        {
            return Integer.valueOf((String)value).intValue();
        }
        if((value instanceof Integer) || (value instanceof Short) || (value instanceof Byte))
        {
            return ((Number)value).intValue();
        }
        throw new MessageFormatException("Cannot convert property " + name + " of type " + value.getClass().getName() + " to int");
    }

    public long getLongProperty(String name) throws JMSException
    {
        Object value = getObjectProperty(name);
        if(value == null)
        {
            throw new MessageFormatException("Property " + name + " is null");
        }
        if(value instanceof String)
        {
            return Long.valueOf((String)value).longValue();
        }
        if((value instanceof Long) || (value instanceof Integer) || (value instanceof Short) || (value instanceof Byte))
        {
            return ((Number)value).longValue();
        }
        throw new MessageFormatException("Cannot convert property " + name + " of type " + value.getClass().getName() + " to long");
    }

    public float getFloatProperty(String name) throws JMSException
    {
        Object value = getObjectProperty(name);
        if(value == null)
        {
            throw new MessageFormatException("Property " + name + " is null");
        }
        if(value instanceof String)
        {
            return Float.valueOf((String)value).floatValue();
        }
        if(value instanceof Float)
        {
            return ((Number)value).floatValue();
        }
        throw new MessageFormatException("Cannot convert property " + name + " of type " + value.getClass().getName() + " to float");
    }

    public double getDoubleProperty(String name) throws JMSException
    {
        Object value = getObjectProperty(name);
        if(value == null)
        {
            throw new MessageFormatException("Property " + name + " is null");
        }
        if(value instanceof String)
        {
           return Double.valueOf((String)value).doubleValue();
        }
        if((value instanceof Double) || (value instanceof Float))
        {
            return ((Number)value).doubleValue();
        }
        throw new MessageFormatException("Cannot convert property " + name + " of type " + value.getClass().getName() + " to double");
    }

    public String getStringProperty(String name) throws JMSException
    {
        Object value = getObjectProperty(name);
        if(null == value) return null;
        return value.toString();
    }

    public Object getObjectProperty(String name) throws JMSException
    {
        return properties.get(name);
    }

    public Enumeration getPropertyNames() throws JMSException
    {
        return new Vector(properties.keySet()).elements();
    }

    public void setBooleanProperty(String name, boolean value) throws JMSException
    {
        setObjectProperty(name, new Boolean(value));
    }

    public void setByteProperty(String name, byte value) throws JMSException
    {
        setObjectProperty(name, new Byte(value));
    }

    public void setShortProperty(String name, short value) throws JMSException
    {
        setObjectProperty(name, new Short(value));
    }

    public void setIntProperty(String name, int value) throws JMSException
    {
        setObjectProperty(name, new Integer(value));
    }

    public void setLongProperty(String name, long value) throws JMSException
    {
        setObjectProperty(name, new Long(value));
    }

    public void setFloatProperty(String name, float value) throws JMSException
    {
        setObjectProperty(name, new Float(value));
    }

    public void setDoubleProperty(String name, double value) throws JMSException
    {
        setObjectProperty(name, new Double(value));
    }

    public void setStringProperty(String name, String value) throws JMSException
    {
        setObjectProperty(name, value);
    }

    public void setObjectProperty(String name, Object object) throws JMSException
    {
        if(null == object)
        {
            properties.put(name, object);
            return;
        }
        if((object instanceof String) || (object instanceof Number) || (object instanceof Boolean))
        {
            properties.put(name, object);
            return;
        }
        throw new MessageFormatException(object.getClass() + " not a valid type");
    }

    public void acknowledge() throws JMSException
    {
        acknowledged = true;
    }

    public void clearBody() throws JMSException
    {

    }
}
