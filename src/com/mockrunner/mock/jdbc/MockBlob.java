package com.mockrunner.mock.jdbc;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class MockBlob implements Blob
{

    public long length() throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public byte[] getBytes(long pos, int length) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public InputStream getBinaryStream() throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public long position(byte[] pattern, long start) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public long position(Blob pattern, long start) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public int setBytes(long pos, byte[] bytes) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public OutputStream setBinaryStream(long pos) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void truncate(long len) throws SQLException
    {
        // TODO Auto-generated method stub

    }
}
