package com.mockrunner.mock.jdbc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of <code>Clob</code>.
 */
public class MockClob implements Clob, Cloneable
{
    private StringBuffer clobData;
    private boolean wasFreeCalled;
    
    public MockClob(String data)
    {
        clobData = new StringBuffer(data);
        wasFreeCalled = false;
    }

    public long length() throws SQLException
    {
        return clobData.length();
    }

    public void truncate(long len) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        clobData.setLength((int)len);
    }

    public InputStream getAsciiStream() throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        try
        {
            return new ByteArrayInputStream(clobData.toString().getBytes("ISO-8859-1"));
        } 
        catch(UnsupportedEncodingException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }

    public OutputStream setAsciiStream(long pos) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        return new ClobOutputStream((int)(pos - 1));
    }

    public Reader getCharacterStream() throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        return new StringReader(clobData.toString());
    }

    public Reader getCharacterStream(long pos, long length) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        length = verifyAndFixLength(pos, (int)length);
        return new StringReader(getSubString(pos, (int)length));
    }

    public Writer setCharacterStream(long pos) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        return new ClobWriter((int)(pos - 1));
    }

    public String getSubString(long pos, int length) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        length = verifyAndFixLength(pos, length);
        return clobData.substring((int)(pos - 1), (int)(pos - 1) + length);
    }

    public int setString(long pos, String str) throws SQLException
    {
        return setString(pos, str, 0, str.length());
    }

    public int setString(long pos, String str, int offset, int len) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        str = str.substring(offset, offset + len);
        clobData.replace((int)(pos - 1), (int)(pos - 1) + str.length(), str);
        return len;
    }

    public long position(String searchstr, long start) throws SQLException
    {
        if(wasFreeCalled)
        {
            throw new SQLException("free() was called");
        }
        int index = clobData.toString().indexOf(searchstr, (int)(start - 1));
        if(-1 != index) index += 1;
        return index;
    }

    public long position(Clob searchClob, long start) throws SQLException
    {
        return position(searchClob.getSubString(1, (int)searchClob.length()), start);
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

    public boolean equals(Object obj)
    {
        if(null == obj) return false;
        if(!obj.getClass().equals(this.getClass())) return false;
        MockClob other = (MockClob)obj;
        if(wasFreeCalled != other.wasFreeCalled()) return false;
        return clobData.toString().equals(other.clobData.toString());
    }

    public int hashCode()
    {
        int hashCode = clobData.toString().hashCode();
        hashCode = (31 * hashCode) + (wasFreeCalled ? 31 : 62);
        return hashCode;
    }

    public String toString()
    {
        return "Clob data: " + clobData.toString();
    }
    
    public Object clone()
    {
        try
        {
            MockClob clone = (MockClob)super.clone();
            clone.clobData = new StringBuffer(clobData.toString());
            return clone;
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
        if((length + (pos - 1)) > clobData.length())
        {
            return clobData.length() - (int)(pos - 1);
        }
        return length;
    }
    
    private class ClobWriter extends Writer
    {  
        private int index;
        
        public ClobWriter(int index)
        {
            this.index = index;
        }
        
        public void close() throws IOException
        {

        }

        public void flush() throws IOException
        {

        }

        public void write(char[] cbuf, int off, int len) throws IOException
        {
            try
            {
                setString(index + 1, new String(cbuf, off, len));
            }
            catch(SQLException exc)
            {
                throw new IOException(exc.getMessage());
            }
            index++;
        }
    }
    
    private class ClobOutputStream extends OutputStream
    {  
        private int index;
    
        public ClobOutputStream(int index)
        {
            this.index = index;
        }
    
        public void write(int byteValue) throws IOException
        {
            byte[] bytes = new byte[] {(byte)byteValue};
            try
            {
                setString(index + 1, new String(bytes));
            }
            catch(SQLException exc)
            {
                throw new IOException(exc.getMessage());
            }
            index++;
        }
    }
}
