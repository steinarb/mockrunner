package com.mockrunner.mock.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** 
 * Contains a list of <code>ResultSet</code> objects where the <code>next()</code> 
 * method iterates through all <code>ResultSet</code> objects in the list.
 * The get-methods and <code>next()</code> can be used. All update-methods
 * and scroll-methods throw an <code>SQLException</code>.
 */
public class PolyResultSet implements ResultSet 
{
    private List resultSets;
    private int position;
    private ResultSet current;

    public PolyResultSet(List resultSets) 
    {
        this.resultSets = resultSets;
    }

    public List getUnderlyingResultSetList()
    {
        return Collections.unmodifiableList(resultSets);
    }
    
    public boolean next() throws SQLException 
    {
        if((current != null) && current.next()) 
        {
            return true;
        } 
        else 
        {
            while(position < resultSets.size()) 
            {
                current = (ResultSet)resultSets.get(position++);
                if(current.next()) return true;
            }
        }
        return false;
    }

    /** 
     * Does nothing.
     */
    public void close() throws SQLException 
    {
        
    }

    public boolean isClosed() throws SQLException
    {
        return false;
    }

    public boolean wasNull() throws SQLException 
    {
        return current.wasNull();
    }
    
    public String getString(int columnIndex) throws SQLException 
    {
        return current.getString(columnIndex);
    }
    
    public String getNString(int columnIndex) throws SQLException
    {
        return current.getNString(columnIndex);
    }

    public boolean getBoolean(int columnIndex) throws SQLException
    {
        return current.getBoolean(columnIndex);
    }
    
    public byte getByte(int columnIndex) throws SQLException
    {
        return current.getByte(columnIndex);
    }
    
    public short getShort(int columnIndex) throws SQLException
    {
        return current.getShort(columnIndex);
    }
    
    public int getInt(int columnIndex) throws SQLException
    {
        return current.getInt(columnIndex);
    }
    
    public long getLong(int columnIndex) throws SQLException
    {
        return current.getLong(columnIndex);
    }
    
    public float getFloat(int columnIndex) throws SQLException
    {
        return current.getFloat(columnIndex);
    }
    
    public double getDouble(int columnIndex) throws SQLException
    {
        return current.getDouble(columnIndex);
    }
    
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException
    {
        return current.getBigDecimal(columnIndex);
    }
    
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException
    {
        return current.getBigDecimal(columnIndex);
    }
    
    public byte[] getBytes(int columnIndex) throws SQLException
    {
        return current.getBytes(columnIndex);
    }
    
    public Date getDate(int columnIndex) throws SQLException
    {
        return current.getDate(columnIndex);
    }
    
    public Date getDate(int columnIndex, Calendar cal) throws SQLException
    {
        return current.getDate(columnIndex, cal);
    }
    
    public Time getTime(int columnIndex) throws SQLException
    {
        return current.getTime(columnIndex);
    }
    
    public Time getTime(int columnIndex, Calendar cal) throws SQLException
    {
        return current.getTime(columnIndex, cal);
    }
    
    public Timestamp getTimestamp(int columnIndex) throws SQLException
    {
        return current.getTimestamp(columnIndex);
    }
    
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException
    {
        return current.getTimestamp(columnIndex, cal);
    }
    
    public InputStream getAsciiStream(int columnIndex) throws SQLException
    {
        return current.getAsciiStream(columnIndex);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException
    {
        return current.getBinaryStream(columnIndex);
    }
    
    public InputStream getUnicodeStream(int columnIndex) throws SQLException
    {
        return current.getUnicodeStream(columnIndex);
    }
    
    public Reader getCharacterStream(int columnIndex) throws SQLException
    {
        return current.getCharacterStream(columnIndex);
    }
    
    public Reader getNCharacterStream(int columnIndex) throws SQLException
    {
        return current.getCharacterStream(columnIndex);
    }
    
    public Ref getRef(int columnIndex) throws SQLException
    {
        return current.getRef(columnIndex);
    }

    public RowId getRowId(int columnIndex) throws SQLException
    {
        return current.getRowId(columnIndex);
    }

    public Blob getBlob(int columnIndex) throws SQLException
    {
        return current.getBlob(columnIndex);
    }

    public Clob getClob(int columnIndex) throws SQLException
    {
        return current.getClob(columnIndex);
    }
    
    public NClob getNClob(int columnIndex) throws SQLException
    {
        return current.getNClob(columnIndex);
    }

    public SQLXML getSQLXML(int columnIndex) throws SQLException
    {
        return current.getSQLXML(columnIndex);
    }

    public Array getArray(int columnIndex) throws SQLException
    {
        return current.getArray(columnIndex);
    }
    
    public URL getURL(int columnIndex) throws SQLException
    {
        return current.getURL(columnIndex);
    }
    
    public Object getObject(int columnIndex) throws SQLException
    {
        return current.getObject(columnIndex);
    }
    
    public Object getObject(int columnIndex, Map map) throws SQLException
    {
        return current.getObject(columnIndex, map);
    }
    
    public String getString(String columnName) throws SQLException
    {
        return current.getString(columnName);
    }

    public String getNString(String columnName) throws SQLException
    {
        return current.getString(columnName);
    }

    public boolean getBoolean(String columnName) throws SQLException
    {
        return current.getBoolean(columnName);
    }

    public byte getByte(String columnName) throws SQLException
    {
        return current.getByte(columnName);
    }

    public short getShort(String columnName) throws SQLException
    {
        return current.getShort(columnName);
    }

    public int getInt(String columnName) throws SQLException
    {
        return current.getInt(columnName);
    }

    public long getLong(String columnName) throws SQLException
    {
        return current.getLong(columnName);
    }

    public float getFloat(String columnName) throws SQLException
    {
        return current.getFloat(columnName);
    }

    public double getDouble(String columnName) throws SQLException
    {
        return current.getDouble(columnName);
    }

    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException
    {
        return current.getBigDecimal(columnName);
    }
    
    public BigDecimal getBigDecimal(String columnName) throws SQLException
    {
        return current.getBigDecimal(columnName);
    }

    public byte[] getBytes(String columnName) throws SQLException
    {
        return current.getBytes(columnName);
    } 

    public Date getDate(String columnName) throws SQLException
    {
        return current.getDate(columnName);
    }
    
    public Date getDate(String columnName, Calendar cal) throws SQLException
    {
        return current.getDate(columnName, cal);
    }

    public Time getTime(String columnName) throws SQLException
    {
        return current.getTime(columnName);
    }
    
    public Time getTime(String columnName, Calendar cal) throws SQLException
    {
        return current.getTime(columnName, cal);
    }

    public Timestamp getTimestamp(String columnName) throws SQLException
    {
        return current.getTimestamp(columnName);
    }
    
    public Timestamp getTimestamp(String columnName, Calendar cal)  throws SQLException
    {
        return current.getTimestamp(columnName, cal);
    }

    public InputStream getAsciiStream(String columnName) throws SQLException
    {
        return current.getAsciiStream(columnName);
    }
    
    public InputStream getUnicodeStream(String columnName) throws SQLException
    {
        return current.getUnicodeStream(columnName);
    }

    public InputStream getBinaryStream(String columnName) throws SQLException
    {
        return current.getBinaryStream(columnName);
    }
    
    public Reader getCharacterStream(String columnName) throws SQLException
    {
        return current.getCharacterStream(columnName);
    }
    
    public Reader getNCharacterStream(String columnName) throws SQLException
    {
        return current.getCharacterStream(columnName);
    }
    
    public Ref getRef(String columnName) throws SQLException
    {
        return current.getRef(columnName);
    }

    public RowId getRowId(String columnName) throws SQLException
    {
        return current.getRowId(columnName);
    }

    public Blob getBlob(String columnName) throws SQLException
    {
        return current.getBlob(columnName);
    }

    public Clob getClob(String columnName) throws SQLException
    {
        return current.getClob(columnName);
    }

    public NClob getNClob(String columnName) throws SQLException
    {
        return current.getNClob(columnName);
    }

    public SQLXML getSQLXML(String columnName) throws SQLException
    {
        return current.getSQLXML(columnName);
    }

    public Array getArray(String colName) throws SQLException
    {
        return current.getArray(colName);
    }
    
    public URL getURL(String columnName) throws SQLException
    {
        return current.getURL(columnName);
    }
    
    public Object getObject(String columnName) throws SQLException
    {
        return current.getObject(columnName);
    }
    
    public Object getObject(String columnName, Map map) throws SQLException
    {
        return current.getObject(columnName, map);
    }

    public SQLWarning getWarnings() throws SQLException
    {
        return current.getWarnings();
    }

    public void clearWarnings() throws SQLException
    {
        current.clearWarnings();
    }

    public String getCursorName() throws SQLException
    {
        return current.getCursorName();
    }

    public ResultSetMetaData getMetaData() throws SQLException
    {
        return current.getMetaData();
    }

    public int findColumn(String columnName) throws SQLException
    {
        return current.findColumn(columnName);
    }

    public boolean isBeforeFirst() throws SQLException
    {
        return current.isBeforeFirst();
    }
      
    public boolean isAfterLast() throws SQLException
    {
        return current.isAfterLast();
    }
 
    public boolean isFirst() throws SQLException
    {
        return current.isFirst();
    }
 
    public boolean isLast() throws SQLException
    {
        return current.isLast();
    }

    public void beforeFirst() throws SQLException
    {
        current.beforeFirst();
    }

    public void afterLast() throws SQLException
    {
        current.afterLast();
    }

    public boolean first() throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public boolean last() throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public int getRow() throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public boolean absolute( int row ) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public boolean relative( int rows ) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public boolean previous() throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void setFetchDirection(int direction) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public int getFetchDirection() throws SQLException
    {
        return current.getFetchDirection();
    }

    public void setFetchSize(int rows) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public int getFetchSize() throws SQLException
    {
        return current.getFetchSize();
    }

    public int getType() throws SQLException
    {
        return current.getType();
    }

    public int getConcurrency() throws SQLException
    {
        return current.getConcurrency();
    }

    public int getHoldability() throws SQLException
    {
        return current.getHoldability();
    }

    public boolean rowUpdated() throws SQLException
    {
        return current.rowUpdated();
    }

    public boolean rowInserted() throws SQLException
    {
        return current.rowInserted();
    }
   
    public boolean rowDeleted() throws SQLException
    {
        return current.rowDeleted();
    }

    public void insertRow() throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateRow() throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void deleteRow() throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void refreshRow() throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void cancelRowUpdates() throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void moveToInsertRow() throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void moveToCurrentRow() throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public Statement getStatement() throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateNull(int columnIndex) throws SQLException  
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBoolean(int columnIndex, boolean value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateByte(int columnIndex, byte value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateShort(int columnIndex, short value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateInt(int columnIndex, int value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateLong(int columnIndex, long value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateFloat(int columnIndex, float value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateDouble(int columnIndex, double value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBigDecimal(int columnIndex, BigDecimal value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateString(int columnIndex, String value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateNString(int columnIndex, String value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBytes(int columnIndex, byte value[]) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateDate(int columnIndex, Date value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateTime(int columnIndex, Time value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateTimestamp(int columnIndex, Timestamp value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateAsciiStream(int columnIndex, InputStream stream, int length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateAsciiStream(int columnIndex, InputStream stream, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateAsciiStream(int columnIndex, InputStream stream) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateBinaryStream(int columnIndex, InputStream stream, int length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateBinaryStream(int columnIndex, InputStream stream, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateBinaryStream(int columnIndex, InputStream stream) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateCharacterStream(int columnIndex, Reader reader, int length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateCharacterStream(int columnIndex, Reader reader, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateCharacterStream(int columnIndex, Reader reader) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateRef(int columnIndex, Ref value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateRowId(int columnIndex, RowId x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBlob(int columnIndex, InputStream stream, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateBlob(int columnIndex, Blob value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateBlob(int columnIndex, InputStream stream) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateClob(int columnIndex, Clob value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateClob(int columnIndex, Reader reader) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateNClob(int columnIndex, Reader reader) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateArray(int columnIndex, Array value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateObject(int columnIndex, Object x, int scale) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateObject(int columnIndex, Object x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateAsciiStream(String columnName, InputStream stream, int length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateAsciiStream(String columnName, InputStream stream) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateAsciiStream(String columnName, InputStream stream, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBinaryStream(String columnName, InputStream stream, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBinaryStream(String columnName, InputStream stream) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
 
    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateCharacterStream(String columnName, Reader reader, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateCharacterStream(String columnName, Reader reader) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateNCharacterStream(String columnName, Reader reader, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateNCharacterStream(String columnName, Reader reader) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateNull(String columnName) throws SQLException  
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBoolean(String columnName, boolean value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateByte(String columnName, byte value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateShort(String columnName, short value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateInt(String columnName, int value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateLong(String columnName, long value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateFloat(String columnName, float value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateDouble(String columnName, double value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBigDecimal(String columnName, BigDecimal value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateString(String columnName, String value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateNString(String columnName, String value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBytes(String columnName, byte value[]) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateDate(String columnName, Date value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateTime(String columnName, Time value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateTimestamp(String columnName, Timestamp value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBinaryStream(String columnName, InputStream value, int length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateRef(String columnName, Ref value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateRowId(String columnName, RowId value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBlob(String columnName, Blob value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBlob(String columnName, InputStream stream, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBlob(String columnName, InputStream stream) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateClob(String columnName, Clob value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateClob(String columnName, Reader reader, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateClob(String columnName, Reader reader) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateNClob(String columnName, NClob nClob) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateNClob(String columnName, Reader reader, long length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateNClob(String columnName, Reader reader) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateSQLXML(String columnName, SQLXML xmlObject) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateArray(String columnName, Array value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateObject(String columnName, Object value, int scale) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateObject(String columnName, Object value) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public boolean isWrapperFor(Class iface) throws SQLException
    {
        return false;
    }

    public Object unwrap(Class iface) throws SQLException
    {
        throw new SQLException("No object found for " + iface);
    }
}


