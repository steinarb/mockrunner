package com.mockrunner.mock.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

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

    public boolean wasNull() throws SQLException 
    {
        return current.wasNull();
    }
    
    public String getString(int columnIndex) throws SQLException 
    {
        return current.getString(columnIndex);
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

    public byte[] getBytes(int columnIndex) throws SQLException
    {
        return current.getBytes(columnIndex);
    }

    public Date getDate(int columnIndex) throws SQLException
    {
        return current.getDate(columnIndex);
    }

    public Time getTime(int columnIndex) throws SQLException
    {
        return current.getTime(columnIndex);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException
    {
        return current.getTimestamp(columnIndex);
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException
    {
        return current.getAsciiStream(columnIndex);
    }

    public InputStream getUnicodeStream(int columnIndex) throws SQLException
    {
        return current.getUnicodeStream(columnIndex);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException
    {
        return current.getBinaryStream(columnIndex);
    }


    public String getString(String columnName) throws SQLException
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

    public byte[] getBytes(String columnName) throws SQLException
    {
        return current.getBytes(columnName);
    }

    public Date getDate(String columnName) throws SQLException
    {
        return current.getDate(columnName);
    }

    public Time getTime(String columnName) throws SQLException
    {
        return current.getTime(columnName);
    }

    public Timestamp getTimestamp(String columnName) throws SQLException
    {
        return current.getTimestamp(columnName);
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

    public Object getObject(int columnIndex) throws SQLException
    {
        return current.getObject(columnIndex);
    }

    public Object getObject(String columnName) throws SQLException
    {
        return current.getObject(columnName);
    }

    public int findColumn(String columnName) throws SQLException
    {
        return current.findColumn(columnName);
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException
    {
        return current.getCharacterStream(columnIndex);
    }

    public Reader getCharacterStream(String columnName) throws SQLException
    {
        return current.getCharacterStream(columnName);
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException
    {
        return current.getBigDecimal(columnIndex);
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException
    {
        return current.getBigDecimal(columnName);
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

    public void updateNull(int columnIndex) throws SQLException  
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBoolean(int columnIndex, boolean x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateByte(int columnIndex, byte x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateShort(int columnIndex, short x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateInt(int columnIndex, int x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateLong(int columnIndex, long x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateFloat(int columnIndex, float x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateDouble(int columnIndex, double x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateString(int columnIndex, String x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBytes(int columnIndex, byte x[]) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateDate(int columnIndex, Date x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateTime(int columnIndex, Time x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException
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

    public void updateNull(String columnName) throws SQLException  
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBoolean(String columnName, boolean x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateByte(String columnName, byte x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateShort(String columnName, short x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateInt(String columnName, int x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateLong(String columnName, long x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateFloat(String columnName, float x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateDouble(String columnName, double x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateString(String columnName, String x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBytes(String columnName, byte x[]) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateDate(String columnName, Date x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateTime(String columnName, Time x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateTimestamp(String columnName, Timestamp x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException
    {
        current.updateAsciiStream(columnName, x, length);
    }

    public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateObject(String columnName, Object x, int scale) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateObject(String columnName, Object x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
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

    public Object getObject(int i, java.util.Map map) throws SQLException
    {
        return current.getObject(i, map);
    }

    public Ref getRef(int i) throws SQLException
    {
        return current.getRef(i);
    }

    public Blob getBlob(int i) throws SQLException
    {
        return current.getBlob(i);
    }

    public Clob getClob(int i) throws SQLException
    {
        return current.getClob(i);
    }

    public Array getArray(int i) throws SQLException
    {
        return current.getArray(i);
    }

    public Object getObject(String colName, java.util.Map map) throws SQLException
    {
        return current.getObject(colName, map);
    }

    public Ref getRef(String colName) throws SQLException
    {
        return current.getRef(colName);
    }

    public Blob getBlob(String colName) throws SQLException
    {
        return current.getBlob(colName);
    }

    public Clob getClob(String colName) throws SQLException
    {
        return current.getClob(colName);
    }

    public Array getArray(String colName) throws SQLException
    {
        return current.getArray(colName);
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException
    {
        return current.getDate(columnIndex, cal);
    }

    public Date getDate(String columnName, Calendar cal) throws SQLException
    {
        return current.getDate(columnName, cal);
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException
    {
        return current.getTime(columnIndex, cal);
    }

    public Time getTime(String columnName, Calendar cal) throws SQLException
    {
        return current.getTime(columnName, cal);
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException
    {
        return current.getTimestamp(columnIndex, cal);
    }

    public Timestamp getTimestamp(String columnName, Calendar cal)	throws SQLException
    {
        return current.getTimestamp(columnName, cal);
    }

    public URL getURL(int columnIndex) throws SQLException
    {
        return current.getURL(columnIndex);
    }

    public URL getURL(String columnName) throws SQLException
    {
        return current.getURL(columnName);
    }

    public void updateRef(int columnIndex, Ref x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
    
    public void updateRef(String columnName, Ref x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBlob(int columnIndex, Blob x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateBlob(String columnName, Blob x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateClob(int columnIndex, Clob x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateClob(String columnName, Clob x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateArray(int columnIndex, Array x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }

    public void updateArray(String columnName, Array x) throws SQLException
    {
        throw new SQLException("Not allowed for " + PolyResultSet.class.getName());
    }
}


