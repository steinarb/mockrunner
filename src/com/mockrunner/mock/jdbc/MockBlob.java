package com.mockrunner.mock.jdbc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import com.mockrunner.util.ArrayUtil;

/**
 * Mock implementation of a JDBC 3.0 <code>Blob</code>.
 */
public class MockBlob implements Blob
{
    private List blobData;
    
    public MockBlob(byte[] data)
    {
        blobData = ArrayUtil.getListFromByteArray(data);
    }
    
    public long length() throws SQLException
    {
        return blobData.size();
    }

    public byte[] getBytes(long pos, int length) throws SQLException
    {
        if(pos < 0) pos = 0;
        return ArrayUtil.getByteArrayFromList(blobData, (int)(pos - 1), length);
    }

    public InputStream getBinaryStream() throws SQLException
    {
        return new ByteArrayInputStream(ArrayUtil.getByteArrayFromList(blobData));
    }

    public long position(byte[] pattern, long start) throws SQLException
    {
        byte[] data = ArrayUtil.getByteArrayFromList(blobData);
        int index = ArrayUtil.contains(data, pattern, (int)(start - 1));
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
        ArrayUtil.addBytesToList(bytes, blobData, (int)(pos - 1));
        return bytes.length;
    }

    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException
    {
        if(pos < 0) pos = 0;
        ArrayUtil.addBytesToList(bytes, offset, len, blobData, (int)(pos - 1));
        return len;
    }

    public OutputStream setBinaryStream(long pos) throws SQLException
    {
        return new BlobOutputStream((int)(pos - 1));
    }

    public void truncate(long len) throws SQLException
    {
        blobData = ArrayUtil.truncateList(blobData, (int)len);
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
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Blob data: ");
        for(int ii = 0; ii < blobData.size(); ii++)
        {
            buffer.append("[" + blobData.get(ii).toString() + "] ");
        }
        return buffer.toString();
    }
}
