package com.mockrunner.mock.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

public class MockCallableStatement extends MockPreparedStatement implements CallableStatement
{
    public MockCallableStatement(Connection connection, String sql)
    {
        super(connection, sql);
    }

    public MockCallableStatement(Connection connection, String sql, int resultSetType, int resultSetConcurrency)
    {
        super(connection, sql, resultSetType, resultSetConcurrency);
    }

    public MockCallableStatement(Connection connection, String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
    {
        super(connection, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    public Object getParameter(String name)
    {
        //TODO: implement me
        return null;
    }
        
    public boolean wasNull() throws SQLException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public byte getByte(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public double getDouble(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public float getFloat(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getInt(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public long getLong(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public short getShort(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean getBoolean(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public byte[] getBytes(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public Object getObject(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getString(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public byte getByte(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public double getDouble(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public float getFloat(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getInt(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public long getLong(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public short getShort(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean getBoolean(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public byte[] getBytes(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setByte(String parameterName, byte x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setDouble(String parameterName, double x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setFloat(String parameterName, float x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void registerOutParameter(String parameterName, int sqlType) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setInt(String parameterName, int x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setNull(String parameterName, int sqlType) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setLong(String parameterName, long x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setShort(String parameterName, short x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setBoolean(String parameterName, boolean x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setBytes(String parameterName, byte[] x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public URL getURL(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Array getArray(int i) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Blob getBlob(int i) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Clob getClob(int i) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Date getDate(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Ref getRef(int i) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Time getTime(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public Object getObject(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setObject(String parameterName, Object x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public Object getObject(int i, Map map) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getString(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setString(String parameterName, String x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public URL getURL(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setURL(String parameterName, URL val) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public Array getArray(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Blob getBlob(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Clob getClob(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Date getDate(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setDate(String parameterName, Date x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public Date getDate(int parameterIndex, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Ref getRef(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Time getTime(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setTime(String parameterName, Time x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public Time getTime(int parameterIndex, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setTimestamp(String parameterName, Timestamp x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getObject(String parameterName, Map map) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Date getDate(String parameterName, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Time getTime(String parameterName, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub

    }

}
