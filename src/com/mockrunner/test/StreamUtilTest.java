package com.mockrunner.test;

import java.io.ByteArrayInputStream;

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
}
