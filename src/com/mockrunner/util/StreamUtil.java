package com.mockrunner.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Simple util class for manipulating streams
 */
public class StreamUtil
{
    /**
     * Returns the contents of the input stream as byte array.
     * @param stream the <code>InputStream</code>
     * @param length the number of bytes to copy
     * @return the stream content as byte array
     */
    public static byte[] getStreamAsByteArray(InputStream stream, int length)
    {
        if(length == 0) return new byte[0];
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try
        {
            int nextValue = stream.read();
            length--;
            while(-1 != nextValue && length >= 0)
            {
                byteStream.write(nextValue);
                nextValue = stream.read();
                length--;
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
     * length the number of chars to copy
     * @return the reader content as <code>String</code>
     */
    public static String getReaderAsString(Reader reader, int length)
    {
        if(length == 0) return "";
        StringBuffer buffer = new StringBuffer();
        try
        {
            int nextValue = reader.read();
            length--;
            while(-1 != nextValue && length >= 0)
            {
                buffer.append((char)nextValue);
                nextValue = reader.read();
                length--;
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
}
