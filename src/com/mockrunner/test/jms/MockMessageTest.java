package com.mockrunner.test.jms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

import javax.jms.DeliveryMode;
import javax.jms.MessageFormatException;

import com.mockrunner.mock.jms.MockMessage;
import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockTopic;

import junit.framework.TestCase;

public class MockMessageTest extends TestCase
{
    public void testPropertyTypes() throws Exception
    {
        MockMessage message = new MockMessage();
        message.setStringProperty("string1", "123.4");
        assertEquals("123.4", message.getObjectProperty("string1"));
        assertEquals("123.4", message.getStringProperty("string1"));
        assertEquals(123.4, message.getDoubleProperty("string1"), 0.01);
        assertEquals(123.4, message.getFloatProperty("string1"), 0.01);
        try
        {
            message.getLongProperty("string1");
            fail();
        }
        catch(NumberFormatException exc)
        {
            //should throw Exception
        }
        message.setDoubleProperty("double1", 123.4);
        assertEquals(new Double(123.4), message.getObjectProperty("double1"));
        assertEquals("123.4", message.getStringProperty("double1"));
        assertEquals(123.4, message.getDoubleProperty("double1"), 0.01);
        message.setFloatProperty("float1", 123.4f);
        assertEquals(new Float(123.4), message.getObjectProperty("float1"));
        assertEquals("123.4", message.getStringProperty("float1"));
        assertEquals(123.4, message.getFloatProperty("float1"), 0.01);
        try
        {
            message.getLongProperty("float1");
            fail();
        }
        catch(MessageFormatException exc)
        {
            //should throw Exception
        }
        try
        {
            message.getFloatProperty("double1");
            fail();
        }
        catch(MessageFormatException exc)
        {
            //should throw Exception
        }
        message.setByteProperty("byte1", (byte)123);
        assertEquals(new Byte((byte)123), message.getObjectProperty("byte1"));
        assertEquals("123", message.getStringProperty("byte1"));
        assertEquals((byte)123, message.getByteProperty("byte1"));
        assertEquals(123, message.getLongProperty("byte1"));
        assertEquals(123, message.getIntProperty("byte1"));
        assertEquals(123, message.getShortProperty("byte1"));
        assertEquals(123, message.getLongProperty("byte1"));
        try
        {
            message.getBooleanProperty("byte1");
            fail();
        }
        catch(MessageFormatException exc)
        {
            //should throw Exception
        }
        message.setIntProperty("int1", 12345);
        assertEquals(new Integer(12345), message.getObjectProperty("int1"));
        assertEquals("12345", message.getStringProperty("int1"));
        assertEquals(12345, message.getLongProperty("int1"));
        assertEquals(12345, message.getIntProperty("int1"));
        try
        {
            message.getShortProperty("int1");
            fail();
        }
        catch(MessageFormatException exc)
        {
            //should throw Exception
        }
        message.setBooleanProperty("boolean1", true);
        assertEquals(new Boolean(true), message.getObjectProperty("boolean1"));
        assertEquals("true", message.getStringProperty("boolean1"));
        assertEquals(true, message.getBooleanProperty("boolean1"));
        try
        {
            message.getDoubleProperty("boolean1");
            fail();
        }
        catch(MessageFormatException exc)
        {
            //should throw Exception
        }
        Enumeration names = message.getPropertyNames();
        ArrayList nameList = new ArrayList();
        while(names.hasMoreElements())
        {
            nameList.add(names.nextElement());
        }
        assertTrue(nameList.size() == 6);
        assertTrue(nameList.contains("string1"));
        assertTrue(nameList.contains("double1"));
        assertTrue(nameList.contains("float1"));
        assertTrue(nameList.contains("int1"));
        assertTrue(nameList.contains("byte1"));
        assertTrue(nameList.contains("boolean1"));
        message.setObjectProperty("null", null);
        try
        {
            message.getDoubleProperty("null");
        }
        catch(MessageFormatException exc)
        {
            //should throw Exception
        }
        assertNull(message.getObjectProperty("null"));
    }
    
    public void testSetAndGetCorrelationID() throws Exception
    {
        MockMessage message = new MockMessage();
        assertNull(message.getJMSCorrelationID());
        assertNull(message.getJMSCorrelationIDAsBytes());
        message.setJMSCorrelationIDAsBytes("ABC".getBytes("ISO-8859-1"));
        assertEquals("ABC", message.getJMSCorrelationID());
        assertTrue(Arrays.equals("ABC".getBytes("ISO-8859-1"), message.getJMSCorrelationIDAsBytes()));
        message.setJMSCorrelationIDAsBytes(null);
        assertNull(message.getJMSCorrelationID());
        assertNull(message.getJMSCorrelationIDAsBytes());
        message.setJMSCorrelationID("test");
        assertEquals("test", message.getJMSCorrelationID());
        assertTrue(Arrays.equals("test".getBytes("ISO-8859-1"), message.getJMSCorrelationIDAsBytes()));
    }
    
    public void testClone() throws Exception
    {
        MockQueue queue = new MockQueue("MyQueue");
        MockTopic topic = new MockTopic("MyTopic");
        MockMessage message = new MockMessage();
        message.setStringProperty("string", "test");
        message.setIntProperty("int", 12345);
        message.setBooleanProperty("boolean", true);
        message.setJMSCorrelationID("testID");
        message.setJMSPriority(3);
        message.setJMSDestination(queue);
        message.setJMSReplyTo(topic);
        message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
        MockMessage newMessage = (MockMessage)message.clone();
        assertNotSame(message, newMessage);
        assertEquals("test", newMessage.getStringProperty("string"));
        assertEquals(12345, newMessage.getIntProperty("int"));
        assertEquals(true, newMessage.getBooleanProperty("boolean"));
        assertEquals("testID", newMessage.getJMSCorrelationID());
        assertEquals(3, newMessage.getJMSPriority());
        assertEquals(DeliveryMode.NON_PERSISTENT, newMessage.getJMSDeliveryMode());
        assertSame(queue, newMessage.getJMSDestination());
        assertSame(topic, newMessage.getJMSReplyTo());
    }
}
