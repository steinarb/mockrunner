package com.mockrunner.mock.jdbc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import com.mockrunner.util.CollectionUtil;

/**
 * Mock implementation of a JDBC 3.0 <code>Blob</code>.
 */
public class MockBlob implements Blob
{
    private List blobData;
    
    public MockBlob(byte[] data)
    {
        blobData = CollectionUtil.getListFromByteArray(data);
    }
    
    public long length() throws SQLException
    {
        return blobData.size();
    }

    public byte[] getBytes(long pos, int length) throws SQLException
    {
        if(pos < 0) pos = 0;
        return CollectionUtil.getByteArrayFromList(blobData, (int)(pos - 1), length);
    }

    public InputStream getBinaryStream() throws SQLException
    {
        return new ByteArrayInputStream(CollectionUtil.getByteArrayFromList(blobData));
    }

    public long position(byte[] pattern, long start) throws SQLException
    {
        byte[] data = CollectionUtil.getByteArrayFromList(blobData);
        int index = CollectionUtil.contains(data, pattern, (int)(start - 1));
        if(-1 != index) index += 1;
        return index;
    }

    public long position(Blob pattern, long start) throws SQLException
    {
        return position(pattern.getBytes(1, (int)pattern.length()), start);
    }

    public int setBytes(long pos, byte[] bytes) throws SQLException
    {
        if(pos < 0) pos = 0;
        CollectionUtil.addBytesToList(bytes, blobData, (int)(pos - 1));
        return bytes.length;
    }

    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException
    {
        if(pos < 0) pos = 0;
        CollectionUtil.addBytesToList(bytes, offset, len, blobData, (int)(pos - 1));
        return len;
    }

    public OutputStream setBinaryStream(long pos) throws SQLException
    {
        return new BlobOutputStream((int)(pos - 1));
    }

    public void truncate(long len) throws SQLException
    {
        blobData = CollectionUtil.truncateList(blobData, (int)len);
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
            CollectionUtil.addBytesToList(bytes, blobData, index);
            index++;
        }
    }
}
