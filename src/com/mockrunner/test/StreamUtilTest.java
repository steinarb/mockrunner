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
        byte[] testData = StreamUtil.getStreamAsByteArray(new ByteArrayInputStream(expectedString.getBytes("ISO-8859-1")));
        String testString = new String(testData);
        assertEquals(expectedString, testString);
    }
    
    public void testGetReaderAsString()
    {
        String expectedString = "This is a simple test string";
        String testString = StreamUtil.getReaderAsString(new StringReader(expectedString));
        assertEquals(expectedString, testString);
    }
}
