package com.mockrunner.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
        testData = StreamUtil.getStreamAsByteArray(new ByteArrayInputStream(expectedString.getBytes("ISO-8859-1")), 3);
        testString = new String(testData);
        assertEquals("Thi", testString);
    }
    
    public void testGetReaderAsString()
    {
        String expectedString = "This is a simple test string";
        String testString = StreamUtil.getReaderAsString(new StringReader(expectedString));
        assertEquals(expectedString, testString);
        testString = StreamUtil.getReaderAsString(new StringReader(expectedString),0);
        assertEquals("", testString);
    }
    
    public void testCompareStreamsAndReader() throws Exception
    {
        byte[] sourceArray = new byte[] {1, 2, 3, 4, 5};
        byte[] targetArray = new byte[] {1, 2, 3, 4, 5};
        InputStream sourceStream = new ByteArrayInputStream(sourceArray);
        assertTrue(StreamUtil.compareStreams(sourceStream, new ByteArrayInputStream(targetArray)));
        assertTrue(StreamUtil.compareStreams(sourceStream, new ByteArrayInputStream(targetArray)));
        assertTrue(1 == sourceStream.read());
        assertFalse(StreamUtil.compareStreams(new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 6}), new ByteArrayInputStream(targetArray)));
        assertFalse(StreamUtil.compareStreams(new ByteArrayInputStream(new byte[0]), new ByteArrayInputStream(targetArray)));
        String sourceString = "This is a test";
        String targetString = "This is a test";
        StringReader sourceReader = new StringReader(sourceString);
        assertTrue(StreamUtil.compareReader(sourceReader, new StringReader(targetString)));
        assertTrue('T' == (char)sourceReader.read());
        assertTrue(StreamUtil.compareReader(sourceReader, new StringReader("his is a test")));
        assertFalse(StreamUtil.compareReader(sourceReader, new StringReader("This is a test")));       
        assertFalse(StreamUtil.compareReader(new StringReader("this is a test"), new StringReader("This is a test")));
    }
}
