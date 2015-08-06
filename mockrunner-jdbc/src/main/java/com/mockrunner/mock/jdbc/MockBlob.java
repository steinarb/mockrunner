package com.mockrunner.mock.jdbc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.util.common.ArrayUtil;
import com.mockrunner.util.common.CollectionUtil;

/**
 * Mock implementation of <code>Blob</code>.
 */
public class MockBlob implements Blob, Cloneable
{
    private List blobData;
    private boolean wasFreeCalled;
    
    public MockBlob(byte[] data)
    {
        blobData = ArrayUtil.getListFromByteArray(data);
        wasFreeCalled = false;
    }
    
    public long length() throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        return blobData.size();
    }

    public byte[] getBytes(long pos, int length) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        length = verifyAndFixLength(pos, length);
        return ArrayUtil.getByteArrayFromList(blobData, (int)(pos - 1), length);
    }

    public InputStream getBinaryStream() throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        return new ByteArrayInputStream(ArrayUtil.getByteArrayFromList(blobData));
    }

    public InputStream getBinaryStream(long pos, long length) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        length = verifyAndFixLength(pos, (int)length);
        return new ByteArrayInputStream(ArrayUtil.getByteArrayFromList(blobData, (int)(pos - 1), (int)length));
    }

    public long position(byte[] pattern, long start) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        byte[] data = ArrayUtil.getByteArrayFromList(blobData);
        int index = ArrayUtil.indexOf(data, pattern, (int)(start - 1));
        if(-1 != index) index += 1;
        return index;
    }

    public long position(Blob pattern, long start) throws SQLException
    {
        return position(pattern.getBytes(1, (int)pattern.length()), start);
    }

    public int setBytes(long pos, byte[] bytes) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        ArrayUtil.addBytesToList(bytes, blobData, (int)(pos - 1));
        return bytes.length;
    }

    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        ArrayUtil.addBytesToList(bytes, offset, len, blobData, (int)(pos - 1));
        return len;
    }

    public OutputStream setBinaryStream(long pos) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        return new BlobOutputStream((int)(pos - 1));
    }

    public void truncate(long len) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        blobData = CollectionUtil.truncateList(blobData, (int)len);
    }
    
    public void free() throws SQLException
    {
        wasFreeCalled = true;
    }

    /**
     * Returns if {@link #free} has been called.
     * @return <code>true</code> if {@link #free} has been called,
     *         <code>false</code> otherwise
     */
    public boolean wasFreeCalled()
    {
        return wasFreeCalled;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(null == obj) return false;
        if(!obj.getClass().equals(this.getClass())) return false;
        MockBlob other = (MockBlob)obj;
        if(wasFreeCalled != other.wasFreeCalled()) return false;
        return blobData.equals(other.blobData);
    }

    @Override
    public int hashCode()
    {
        int hashCode = blobData.hashCode();
        hashCode = (31 * hashCode) + (wasFreeCalled ? 31 : 62);
        return hashCode;
    }

    @Override
    public String toString()
    {
        return "Blob data: " + blobData.toString();     
    }
    
    @Override
    public Object clone()
    {
        try
        {
            MockBlob blob = (MockBlob)super.clone();
            blob.blobData = new ArrayList(blobData);
            return blob;
        }
        catch(CloneNotSupportedException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    private int verifyAndFixLength(long pos, int length)
    {
        if(length < 0)
        {
            throw new IllegalArgumentException("length must be greater or equals 0");
        }
        if((length + (pos - 1)) > blobData.size())
        {
            return blobData.size() - (int)(pos - 1);
        }
        return length;
    }
    
    private class BlobOutputStream extends OutputStream
    {  
        private int index;
        
        public BlobOutputStream(int index)
        {
            this.index = index;
        }
        
        public void write(int byteValue) throws IOException
        {
            byte[] bytes = new byte[] {(byte)byteValue};
            ArrayUtil.addBytesToList(bytes, blobData, index);
            index++;
        }
    }
}
