package com.mockrunner.test;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import junit.framework.TestCase;

import com.mockrunner.util.StreamUtil;

public class StreamUtilTest extends TestCase
{
    public void testGetStreamAsByteArray() throws Exception
    {
        String expectedString = "This is a simple test string";
        byte[] testData = StreamUtil.getStreamAsByteArray(new ByteArrayInputStream(expectedString.getBytes("ISO-8859-1")), expectedString.length());
        String testString = new String(testData);
        assertEquals(expectedString, testString);
        testData = StreamUtil.getStreamAsByteArray(new ByteArrayInputStream(expectedString.getBytes("ISO-8859-1")), 3);
        testString = new String(testData);
        assertEquals("Thi", testString);
    }
    
    public void testGetReaderAsString()
    {
        String expectedString = "This is a simple test string";
        String testString = StreamUtil.getReaderAsString(new StringReader(expectedString), expectedString.length());
        assertEquals(expectedString, testString);
        testString = StreamUtil.getReaderAsString(new StringReader(expectedString),0);
        assertEquals("", testString);
    }
}
