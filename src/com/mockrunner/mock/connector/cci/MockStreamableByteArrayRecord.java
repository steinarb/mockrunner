package com.mockrunner.mock.connector.cci;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.resource.cci.Streamable;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.util.common.ArrayUtil;
import com.mockrunner.util.common.StreamUtil;

/**
 * Streamable <code>Record</code> backed by a byte array.
 */
public class MockStreamableByteArrayRecord extends MockRecord implements Streamable
{
    private byte[] content;
    
    public MockStreamableByteArrayRecord()
    {
        this(MockStreamableByteArrayRecord.class.getName());
    }
    
    public MockStreamableByteArrayRecord(String name)
    {
        this(name, name);
    }

    public MockStreamableByteArrayRecord(String name, String description)
    {
        super(name, description);
        content = null;
    }
    
    /**
     * Returns a copy of the underlying byte array.
     * @return the underlying data
     */
    public byte[] getContent()
    {
        if(null == content) return null;
        return (byte[])ArrayUtil.copyArray(content);
    }

    /**
     * Sets the content of this record. The specified array
     * will be copied.
     * @param content the content
     */
    public void setContent(byte[] content)
    {
        if(null == content)
        {
            this.content = null;
        }
        else
        {
            this.content = (byte[])ArrayUtil.copyArray(content);
        }
    }

    public void read(InputStream stream) throws IOException
    {
        if(null == stream)
        {
            this.content = null;
        }
        else
        {
            this.content = StreamUtil.getStreamAsByteArray(stream);
        }
    }

    public void write(OutputStream stream) throws IOException
    {
        if(null != stream && null != this.content)
        {
            stream.write(this.content);
            stream.flush();
        }  
    }
    
    public boolean equals(Object object)
    {
        if(!super.equals(object)) return false;
        MockStreamableByteArrayRecord other = (MockStreamableByteArrayRecord)object;
        return ArrayUtil.areArraysEqual(content, other.content);
    }

    public int hashCode()
    {
        return super.hashCode() + 31 * ArrayUtil.computeHashCode(content);
    }

    public Object clone()
    {
        try
        {
            MockStreamableByteArrayRecord clone = (MockStreamableByteArrayRecord)super.clone();
            clone.setContent(this.content);
            return clone;
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
}
