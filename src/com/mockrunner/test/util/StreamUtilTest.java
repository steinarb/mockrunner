package com.mockrunner.test.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

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
        assertTrue(StreamUtil.compareReaders(sourceReader, new StringReader(targetString)));
        assertTrue('T' == (char)sourceReader.read());
        assertTrue(StreamUtil.compareReaders(sourceReader, new StringReader("his is a test")));
        assertFalse(StreamUtil.compareReaders(sourceReader, new StringReader("This is a test")));       
        assertFalse(StreamUtil.compareReaders(new StringReader("this is a test"), new StringReader("This is a test")));
    }
    
    public void testCopyStreamAndReader() throws Exception
    {
        InputStream sourceStream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5});
        InputStream copyStream = StreamUtil.copyStream(sourceStream);
        assertTrue(StreamUtil.compareStreams(sourceStream, copyStream));
        assertEquals(1, copyStream.read());
        assertFalse(StreamUtil.compareStreams(sourceStream, copyStream));
        Reader sourceReader = new StringReader("This is a String");
        Reader copyReader = StreamUtil.copyReader(sourceReader);
        assertTrue(StreamUtil.compareReaders(sourceReader, copyReader));
        assertEquals('T', copyReader.read());
    }
    
    public void testCopyStream() throws Exception
    {
        byte[] sourceArray = new byte[] {1, 2, 3, 4, 5};
        ByteArrayInputStream sourceStream = new ByteArrayInputStream(sourceArray);
        ByteArrayOutputStream destStream = new ByteArrayOutputStream();
        StreamUtil.copyStream(sourceStream, destStream);
        assertTrue(Arrays.equals(sourceArray, destStream.toByteArray()));
        sourceStream = new ByteArrayInputStream(new byte[0]);
        destStream = new ByteArrayOutputStream();
        assertTrue(Arrays.equals(new byte[0], destStream.toByteArray()));
    }
}
