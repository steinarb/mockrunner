package com.mockrunner.mock.jdbc;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

public class MockClob implements Clob
{
    public StringBuffer clobData;
    
    public MockClob(String data)
    {
        clobData = new StringBuffer(data);
    }

    public long length() throws SQLException
    {
        return clobData.length();
    }

    public void truncate(long len) throws SQLException
    {
        clobData.setLength((int)len);
    }

    public InputStream getAsciiStream() throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OutputStream setAsciiStream(long pos) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Reader getCharacterStream() throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Writer setCharacterStream(long pos) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSubString(long pos, int length) throws SQLException
    {
        return clobData.substring((int)(pos - 1), (int)(pos - 1) + length);
    }

    public int setString(long pos, String str) throws SQLException
    {
        return setString(pos, str, 0, str.length());
    }

    public int setString(long pos, String str, int offset, int len) throws SQLException
    {
        str = str.substring(offset, offset + len);
        clobData.replace((int)(pos - 1), (int)(pos - 1)+ str.length(), str);
        return len;
    }

    public long position(String searchstr, long start) throws SQLException
    {
        int index = clobData.indexOf(searchstr, (int)(start - 1));
        if(-1 != index) index += 1;
        return index;
    }

    public long position(Clob searchClob, long start) throws SQLException
    {
        return position(searchClob.getSubString(1, (int)searchClob.length()), start);
    }
}
