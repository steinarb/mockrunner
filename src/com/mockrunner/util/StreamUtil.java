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
     * @return the stream content as byte array
     */
    public static byte[] getStreamAsByteArray(InputStream stream)
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try
        {
            int nextValue = stream.read();
            while(-1 != nextValue)
            {
                byteStream.write(nextValue);
                nextValue = stream.read();
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
        StringBuffer buffer = new StringBuffer();
        try
        {
            int nextValue = reader.read();
            while(-1 != nextValue)
            {
                buffer.append((char)nextValue);
                nextValue = reader.read();
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
