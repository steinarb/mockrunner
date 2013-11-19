package com.mockrunner.test.jms;

import org.activemq.filter.mockrunner.Filter;
import org.activemq.selector.mockrunner.SelectorParser;

import com.mockrunner.mock.jms.MockTextMessage;

import junit.framework.TestCase;

public class MessageSelectorTest extends TestCase
{
    private SelectorParser parser;
    private MockTextMessage message;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        parser = new SelectorParser();
        message = new MockTextMessage();
    }
    
    public void testSimpleExpression() throws Exception
    {
        Filter intPropertyFilter = parser.parse("intProperty > 1");
        assertFalse(intPropertyFilter.matches(message));
        message.setIntProperty("intProperty", 3);
        assertTrue(intPropertyFilter.matches(message));
        Filter correlationIDFilter = parser.parse("JMSCorrelationID LIKE '12%4'");
        message.setJMSCorrelationID("12");
        assertFalse(correlationIDFilter.matches(message));
        message.setJMSCorrelationID("12334");
        assertTrue(correlationIDFilter.matches(message));
        message.setJMSCorrelationID("124");
        assertTrue(correlationIDFilter.matches(message));
        Filter stringPropertyFilter = parser.parse("stringProperty NOT LIKE 'tes_t'");
        message.setStringProperty("stringProperty", "te");
        assertTrue(stringPropertyFilter.matches(message));
        message.setStringProperty("stringProperty", "test");
        assertTrue(stringPropertyFilter.matches(message));
        message.setStringProperty("stringProperty", "tesst");
        assertFalse(stringPropertyFilter.matches(message));
        message.setStringProperty("stringProperty", "tessst");
        assertTrue(stringPropertyFilter.matches(message));
    }
    
    public void testComplexExpression() throws Exception
    {
        Filter complexFilter = parser.parse("((JMSMessageID IS NOT NULL) AND (number BETWEEN 1 AND 8) AND (text IN ('1', '2', '3'))) OR (JMSCorrelationID = '42')");
        assertFalse(complexFilter.matches(message));
        message.setJMSMessageID("test");
        assertFalse(complexFilter.matches(message));
        message.setIntProperty("number", 2);
        assertFalse(complexFilter.matches(message));
        message.setStringProperty("text", "3");
        assertTrue(complexFilter.matches(message));
        message.setStringProperty("text", "4");
        assertFalse(complexFilter.matches(message));
        message.setJMSCorrelationID("42");
        assertTrue(complexFilter.matches(message));
        message.setStringProperty("text", "1");
        assertTrue(complexFilter.matches(message));
        message.setJMSCorrelationID("41");
        assertTrue(complexFilter.matches(message));
        message.setIntProperty("number", 0);
        assertFalse(complexFilter.matches(message));
    }
    
    public void testWildcardExpression() throws Exception
    {
        Filter wildcardFilter = parser.parse("stringProperty LIKE '__%XYZ'");
        message.setStringProperty("stringProperty", "");
        assertFalse(wildcardFilter.matches(message));
        message.setStringProperty("stringProperty", "XYZ");
        assertFalse(wildcardFilter.matches(message));
        message.setStringProperty("stringProperty", "12XYZ");
        assertTrue(wildcardFilter.matches(message));
        message.setStringProperty("stringProperty", "cc1234567XYZ");
        assertTrue(wildcardFilter.matches(message));
        message.setStringProperty("stringProperty", "1XYZ");
        assertFalse(wildcardFilter.matches(message));
        wildcardFilter = parser.parse("stringProperty LIKE '__xyz'");
        message.setStringProperty("stringProperty", "xyz");
        assertFalse(wildcardFilter.matches(message));
        message.setStringProperty("stringProperty", "\nbxyz");
        assertTrue(wildcardFilter.matches(message));
    }
}
