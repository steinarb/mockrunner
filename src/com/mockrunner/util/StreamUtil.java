package com.mockrunner.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;

/**
 * Simple util class for manipulating streams
 */
public class StreamUtil
{
    /**
     * Returns the contents of the input stream as byte array.
     * @param stream the <code>InputStream</code>
     * @return the stream content as byte array
     */
    public static byte[] getStreamAsByteArray(InputStream stream)
    {
        return getStreamAsByteArray(stream, -1);
    }
    
    /**
     * Returns the contents of the input stream as byte array.
     * @param stream the <code>InputStream</code>
     * @param length the number of bytes to copy, if length < 0,
     *        the number is unlimited
     * @return the stream content as byte array
     */
    public static byte[] getStreamAsByteArray(InputStream stream, int length)
    {
        if(length == 0) return new byte[0];
        boolean checkLength = true;
        if(length < 0)
        {
            length = Integer.MAX_VALUE;
            checkLength = false;
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try
        {
            int nextValue = stream.read();
            if(checkLength) length--;
            while(-1 != nextValue && length >= 0)
            {
                byteStream.write(nextValue);
                nextValue = stream.read();
                if(checkLength) length--;
            }
        }
        catch(IOException exc)
        {
       
        }
        finally
        {
            return byteStream.toByteArray();
        }
    }
    
    /**
     * Returns the contents of the reader as a string.
     * @param reader the <code>Reader</code>
     * @return the reader content as <code>String</code>
     */
    public static String getReaderAsString(Reader reader)
    {
        return getReaderAsString(reader, -1);
    }
    
    /**
     * Returns the contents of the reader as a string.
     * @param reader the <code>Reader</code>
     * @param length the number of chars to copy, if length < 0,
     *        the number is unlimited
     * @return the reader content as <code>String</code>
     */
    public static String getReaderAsString(Reader reader, int length)
    {
        if(length == 0) return "";
        boolean checkLength = true;
        if(length < 0)
        {
            length = Integer.MAX_VALUE;
            checkLength = false;
        }
        StringBuffer buffer = new StringBuffer();
        try
        {
            int nextValue = reader.read();
            if(checkLength) length--;
            while(-1 != nextValue && length >= 0)
            {
                buffer.append((char)nextValue);
                nextValue = reader.read();
                if(checkLength) length--;
            }
        }
        catch(IOException exc)
        {
   
        }
        finally
        {
            return buffer.toString();
        }
    }
    
    /**
     * Compares the content of the streams. If the streams support
     * marking, they will be reseted after the comparison.
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
            return false;
        }
    }
    
    /**
     * Compares the content of the readers. If the readers support
     * marking, they will be reseted after the comparison.
     * @param sourceStream the source stream
     * @param targetStream the target stream
     * @return <code>true</code>, if the streams are identical, <code>false</code> otherwise
     */
    public static boolean compareReader(Reader sourceReader, Reader targetReader)
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
            return false;
        }
    }
}
