package com.mockrunner.util.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mockrunner.base.NestedApplicationException;

/**
 * Simple util class for manipulating streams
 */
public class StreamUtil
{
    private final static Log log = LogFactory.getLog(StreamUtil.class);
    
    /**
     * Returns the contents of the input stream as byte array.
     * @param stream the <code>InputStream</code>
     * @return the stream content as byte array
     */
    public static byte[] getStreamAsByteArray(InputStream stream)
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try
        {
            byte[] bytes = new byte[256];
            int read = 0;
            while(0 <= (read = stream.read(bytes)))
            {
                byteStream.write(bytes, 0, read);
            }
            byteStream.flush();
        }
        catch(IOException exc)
        {
            log.error("Exception while reading from stream", exc);
        }
        return byteStream.toByteArray();
    }
    
    /**
     * Returns the contents of the input stream as byte array.
     * @param stream the <code>InputStream</code>
     * @param length the number of bytes to copy, if length is negative,
     *        the number is unlimited
     * @return the stream content as byte array
     */
    public static byte[] getStreamAsByteArray(InputStream stream, int length)
    {
        byte[] bytes = new byte[length];
        int offset = 0;
        int read = 0;
        try
        {
            while (offset < bytes.length && 0 <= (read = stream.read(bytes, offset, bytes.length - offset))) 
            {
                offset += read;
            }
        } 
        catch(IOException exc)
        {
            log.error("Exception while reading from stream", exc);
        }
        return bytes;
    }
    
    /**
     * Returns the contents of the reader as a string.
     * @param reader the <code>Reader</code>
     * @return the reader content as <code>String</code>
     */
    public static String getReaderAsString(Reader reader)
    {
        StringWriter writer = new StringWriter();
        try
        {
            char[] chars = new char[256];
            int read = 0;
            while(0 <= (read = reader.read(chars)))
            {
                writer.write(chars, 0, read);
            }
            writer.flush();
        }
        catch(IOException exc)
        {
            log.error("Exception while reading from reader", exc);
        }
        return writer.toString();
    }
    
    /**
     * Returns the contents of the reader as a string.
     * @param reader the <code>Reader</code>
     * @param length the number of chars to copy, if negative,
     *        the number is unlimited
     * @return the reader content as <code>String</code>
     */
    public static String getReaderAsString(Reader reader, int length)
    {
        char[] chars = new char[length];
        int offset = 0;
        int read = 0;
        try
        {
            while (offset < chars.length && 0 <= (read = reader.read(chars, offset, chars.length - offset))) 
            {
                offset += read;
            }
        } 
        catch(IOException exc)
        {
            log.error("Exception while reading from reader", exc);
        }
        return String.valueOf(chars);
    }
    
    /**
     * Returns a copy of the specified stream. If the specified stream supports
     * marking, it will be reset after the copy.
     * @param sourceStream the stream to copy
     * @return a copy of the stream
     */
    public static InputStream copyStream(InputStream sourceStream)
    {
        try
        {
            if(sourceStream.markSupported()) sourceStream.mark(sourceStream.available());
            byte[] sourceData = getStreamAsByteArray(sourceStream);
            if(sourceStream.markSupported()) sourceStream.reset();
            return new ByteArrayInputStream(sourceData);
        }
        catch(IOException exc)
        {
            log.error("Exception while copying stream", exc);
            return null;
        }
    }
  
    /**
     * Returns a copy of the specified reader. If the specified reader supports
     * marking, it will be reset after the copy.
     * @param sourceReader the stream to reader
     * @return a copy of the reader
     */
    public static Reader copyReader(Reader sourceReader)
    {
        try
        {
            if(sourceReader.markSupported()) sourceReader.mark(Integer.MAX_VALUE);
            String sourceData = getReaderAsString(sourceReader);
            if(sourceReader.markSupported()) sourceReader.reset();
            return new StringReader(sourceData);
        }
        catch(IOException exc)
        {
            log.error("Exception while copying reader", exc);
            return null;
        }
    }
    
    /**
     * Compares the content of the streams. If the streams support
     * marking, they will be reset after the comparison.
     * @param sourceStream the source stream
     * @param targetStream the target stream
     * @return <code>true</code>, if the streams are identical, <code>false</code> otherwise
     */
    public static boolean compareStreams(InputStream sourceStream, InputStream targetStream)
    {
        try
        {
            if(sourceStream.markSupported()) sourceStream.mark(sourceStream.available());
            if(targetStream.markSupported()) targetStream.mark(targetStream.available());
            byte[] sourceArray = getStreamAsByteArray(sourceStream);
            byte[] targetArray = getStreamAsByteArray(targetStream);
            if(sourceStream.markSupported()) sourceStream.reset();
            if(targetStream.markSupported()) targetStream.reset();
            return Arrays.equals(sourceArray, targetArray);
        }
        catch(IOException exc)
        {
            log.error("Exception while comparing streams", exc);
            return false;
        }
    }
    
    /**
     * Compares the content of the readers. If the readers support
     * marking, they will be reset after the comparison.
     * @param sourceReader the source stream
     * @param targetReader the target stream
     * @return <code>true</code>, if the streams are identical, <code>false</code> otherwise
     */
    public static boolean compareReaders(Reader sourceReader, Reader targetReader)
    {
        try
        {
            if(sourceReader.markSupported()) sourceReader.mark(Integer.MAX_VALUE);
            if(targetReader.markSupported()) targetReader.mark(Integer.MAX_VALUE);
            String sourceString = getReaderAsString(sourceReader);
            String targetString = getReaderAsString(targetReader);
            if(sourceReader.markSupported()) sourceReader.reset();
            if(targetReader.markSupported()) targetReader.reset();
            return sourceString.equals(targetString);
        }
        catch(IOException exc)
        {
            log.error("Exception while comparing readers", exc);
            return false;
        }
    }
    
    /**
     * Reads the lines from the specified reader and adds them to a <code>List</code>.
     * @param reader the reader
     * @return the <code>List</code> with the lines
     */
    public static List<String> getLinesFromReader(Reader reader)
    {
        List<String> resultList = new ArrayList<String>();
        try
        {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while(line != null)
            {
                resultList.add(line);
                line = bufferedReader.readLine();
            }
        } 
        catch(IOException exc)
        {
            throw new NestedApplicationException(exc);
        }
        return resultList;
    }
}
