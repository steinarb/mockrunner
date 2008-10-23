package com.mockrunner.test.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import com.mockrunner.mock.jdbc.MockBlob;
import com.mockrunner.mock.jdbc.MockClob;
import com.mockrunner.mock.jdbc.MockNClob;
import com.mockrunner.mock.jdbc.MockRef;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.MockResultSetMetaData;
import com.mockrunner.mock.jdbc.MockRowId;
import com.mockrunner.mock.jdbc.MockSQLXML;
import com.mockrunner.mock.jdbc.MockStruct;

public class MockResultSetTest extends TestCase
{
    private MockResultSet resultSet;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        resultSet = new MockResultSet("");
    }
    
    public void testGetColumnCountMetaData() throws Exception
    {   
        assertEquals(0, resultSet.getMetaData().getColumnCount());
        resultSet.addColumn("firstColumn");
        resultSet.addColumn("secondColumn");
        resultSet.addColumn("thirdColumn");
        assertEquals(3, resultSet.getMetaData().getColumnCount());
        assertEquals("firstColumn", resultSet.getMetaData().getColumnName(1));
        assertEquals("secondColumn", resultSet.getMetaData().getColumnName(2));
        assertEquals("thirdColumn", resultSet.getMetaData().getColumnName(3));
    }

    public void testAddRow() throws Exception
    {
        resultSet.addColumn("firstColumn");
        resultSet.addColumn("secondColumn");
        resultSet.addColumn("thirdColumn");
        List row = new ArrayList();
        row.add("value1");
        row.add("value2");
        row.add("value3");
        row.add("value4");
        resultSet.addRow(row);
        resultSet.addRow(new String[] {"test1", "test2"});
        assertTrue(resultSet.getRowCount() == 2);
        resultSet.next();
        assertFalse(resultSet.rowUpdated());
        assertFalse(resultSet.rowDeleted());
        assertFalse(resultSet.rowInserted());
        assertEquals("value1", resultSet.getString(1));
        assertEquals("value2", resultSet.getString(2));
        assertEquals("value3", resultSet.getString(3));
        assertEquals("value4", resultSet.getString(4));
        assertEquals("value1", resultSet.getString("firstColumn"));
        assertEquals("value2", resultSet.getString("secondColumn"));
        assertEquals("value3", resultSet.getString("thirdColumn"));
        assertEquals("value4", resultSet.getString("Column4"));
        resultSet.next();
        assertFalse(resultSet.rowUpdated());
        assertFalse(resultSet.rowDeleted());
        assertFalse(resultSet.rowInserted());
        assertEquals("test1", resultSet.getString(1));
        assertEquals("test2", resultSet.getString(2));
        assertEquals(null, resultSet.getString(3));
        assertEquals(null, resultSet.getString(4));
        assertEquals("test1", resultSet.getString("firstColumn"));
        assertEquals("test2", resultSet.getString("secondColumn"));
        assertEquals(null, resultSet.getString("thirdColumn"));
        assertEquals(null, resultSet.getString("Column4"));
    }
    
    public void testAddColumn() throws Exception
    {
        resultSet.addColumn("intColumn", new Integer[] {new Integer(1), new Integer(2), new Integer(3)});
        List column = new ArrayList();
        column.add("value1");
        column.add("value2");
        column.add("value3");
        column.add("value4");
        resultSet.addColumn("stringColumn", column);
        resultSet.addColumn();
        assertTrue(resultSet.getRowCount() == 4);
        resultSet.next();
        assertFalse(resultSet.rowUpdated());
        assertFalse(resultSet.rowDeleted());
        assertFalse(resultSet.rowInserted());
        assertEquals(1, resultSet.getInt(1));
        assertEquals("value1", resultSet.getString(2));
        assertEquals(null, resultSet.getObject(3));
        assertEquals(1, resultSet.getInt("intColumn"));
        assertEquals("value1", resultSet.getString("stringColumn"));
        assertEquals(null, resultSet.getString("Column3"));
        resultSet.next();
        assertFalse(resultSet.rowUpdated());
        assertFalse(resultSet.rowDeleted());
        assertFalse(resultSet.rowInserted());
        assertEquals(2, resultSet.getInt(1));
        assertEquals("value2", resultSet.getString(2));
        assertEquals(null, resultSet.getObject(3));
        assertEquals(2, resultSet.getInt("intColumn"));
        assertEquals("value2", resultSet.getString("stringColumn"));
        assertEquals(null, resultSet.getString("Column3"));
        resultSet.next();
        assertFalse(resultSet.rowUpdated());
        assertFalse(resultSet.rowDeleted());
        assertFalse(resultSet.rowInserted());
        assertEquals(3, resultSet.getInt(1));
        assertEquals("value3", resultSet.getString(2));
        assertEquals(null, resultSet.getObject(3));
        assertEquals(3, resultSet.getInt("intColumn"));
        assertEquals("value3", resultSet.getString("stringColumn"));
        assertEquals(null, resultSet.getString("Column3"));
        resultSet.next();
        assertFalse(resultSet.rowUpdated());
        assertFalse(resultSet.rowDeleted());
        assertFalse(resultSet.rowInserted());
        assertEquals(0, resultSet.getInt(1));
        assertEquals("value4", resultSet.getString(2));
        assertEquals(null, resultSet.getObject(3));
        assertEquals(0, resultSet.getInt("intColumn"));
        assertEquals("value4", resultSet.getString("stringColumn"));
        assertEquals(null, resultSet.getString("Column3"));
    }
    
    public void testGetValues() throws Exception
    {
        List column = new ArrayList();
        column.add("1.2");
        column.add(new Double(3.4));
        column.add("1900-12-12");
        column.add("value");
        column.add(null);
        column.add("value");
        resultSet.addColumn("column", column);
        try
        {
            resultSet.getObject(1);
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        resultSet.next();
        assertEquals(1.2f, resultSet.getFloat(1), 0.0);
        assertEquals(new BigDecimal("1.2"), resultSet.getBigDecimal(1));
        assertEquals(new BigDecimal(new BigInteger("120000"), 5), resultSet.getBigDecimal(1, 5));
        assertEquals(new BigDecimal("1.2"), resultSet.getBigDecimal("column"));
        assertEquals(new BigDecimal(new BigInteger("120000"), 5), resultSet.getBigDecimal("column", 5));
        assertFalse(resultSet.wasNull());
        assertEquals(1.2, resultSet.getDouble("column"), 0.0);
        assertTrue(Arrays.equals(new byte[] {49, 46, 50}, resultSet.getBytes(1)));
        assertFalse(resultSet.wasNull());
        resultSet.next();
        assertEquals("3.4", resultSet.getNString("column"));
        assertEquals(3.4, resultSet.getDouble(1), 0.0);
        assertEquals(3, resultSet.getInt(1));
        resultSet.next();
        assertEquals("1900-12-12", resultSet.getDate(1).toString());
        char[] charData = new char[10];
        resultSet.getCharacterStream(1).read(charData);
        assertEquals("1900-12-12", new String(charData));
        assertFalse(resultSet.wasNull());
        resultSet.next();
        byte[] byteData = new byte[5];
        resultSet.getBinaryStream("column").read(byteData);
        assertEquals("value", new String(byteData, "ISO-8859-1"));
        assertFalse(resultSet.wasNull());
        resultSet.next();
        assertEquals(0, resultSet.getShort(1));
        assertTrue(resultSet.wasNull());
        assertEquals(null, resultSet.getString(1));
        assertTrue(resultSet.wasNull());
        resultSet.next();
        assertEquals("value", resultSet.getObject("column"));
        assertEquals("value", resultSet.getNString("column"));
        Clob clob = resultSet.getClob("column");
        assertEquals("value", clob.getSubString(1, 5));
        assertFalse(resultSet.wasNull());
        resultSet.next();
        try
        {
            resultSet.getObject(1);
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw SQLException
        }
    }
    
    public void testUpdateValues() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        List column = new ArrayList();
        column.add(null);
        column.add(new Double(3.4));
        column.add("value");
        resultSet.addColumn("column1", column);
        column = new ArrayList();
        column.add(new Integer(2));
        column.add("test");
        resultSet.addColumn("column2", column);
        try
        {
            resultSet.updateObject(1, "value");
            fail();
        } 
        catch (SQLException exc)
        {
            //should throw SQLException
        }
        resultSet.next();
        resultSet.updateNull(2);
        assertNull(resultSet.getObject(1));
        assertNull(resultSet.getObject(2));
        resultSet.updateInt(1, 3);
        assertEquals(new Integer(3), resultSet.getObject(1));
        assertNull(resultSet.getObject(2));
        resultSet.next();
        resultSet.updateBytes(2, new byte[] {1, 2, 3});
        assertEquals(new Double(3.4), resultSet.getObject(1));
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, resultSet.getBlob("column2").getBytes(1, 3)));
        resultSet.next();
        resultSet.updateObject(1, "3");
        assertEquals(3, resultSet.getLong(1));
        resultSet.updateNString(1, "1");
        assertEquals(1, resultSet.getLong(1));
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5});
        resultSet.updateBinaryStream(1, stream, 3);
        InputStream inputStream = resultSet.getBinaryStream(1);
        assertEquals(1, inputStream.read());
        assertEquals(2, inputStream.read());
        assertEquals(3, inputStream.read());
        assertEquals(-1, inputStream.read());
        resultSet.updateRowId(1, new MockRowId(new byte[] {1, 2, 3}));
        assertEquals(new MockRowId(new byte[] {1, 2, 3}), resultSet.getRowId(1));
    }
    
    public void testUpdateStreams() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.addColumn("column", new Object[] { "value" });
        resultSet.next();
        ByteArrayInputStream updateStream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5});
        resultSet.updateAsciiStream(1, updateStream, (long)2);
        InputStream inputStream = resultSet.getAsciiStream("column");
        assertEquals(1, inputStream.read());
        assertEquals(2, inputStream.read());
        assertEquals(-1, inputStream.read());
        updateStream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5});
        resultSet.updateAsciiStream(1, updateStream);
        inputStream = resultSet.getAsciiStream(1);
        assertEquals(1, inputStream.read());
        assertEquals(2, inputStream.read());
        assertEquals(3, inputStream.read());
        assertEquals(4, inputStream.read());
        assertEquals(5, inputStream.read());
        assertEquals(-1, inputStream.read());
        updateStream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5});
        resultSet.updateBinaryStream("column", updateStream, (long)3);
        inputStream = resultSet.getBinaryStream("column");
        assertEquals(1, inputStream.read());
        assertEquals(2, inputStream.read());
        assertEquals(3, inputStream.read());
        assertEquals(-1, inputStream.read());
        StringReader updateReader = new StringReader("test");
        resultSet.updateCharacterStream(1, updateReader);
        Reader inputReader = resultSet.getCharacterStream("column");
        assertEquals('t', (char)inputReader.read());
        assertEquals('e', (char)inputReader.read());
        assertEquals('s', (char)inputReader.read());
        assertEquals('t', (char)inputReader.read());
        assertEquals(-1, inputReader.read());
        updateReader = new StringReader("test");
        resultSet.updateCharacterStream(1, updateReader, 1);
        inputReader = resultSet.getNCharacterStream(1);
        assertEquals('t', (char)inputReader.read());
        assertEquals(-1, inputReader.read());
        updateReader = new StringReader("test");
        resultSet.updateNCharacterStream(1, updateReader, 2);
        inputReader = resultSet.getCharacterStream(1);
        assertEquals('t', (char)inputReader.read());
        assertEquals('e', (char)inputReader.read());
        assertEquals(-1, inputReader.read());
    }
    
    public void testUpdateBlobAndClob() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.addColumn("column", new Object[] { "value" });
        resultSet.next();
        resultSet.updateBlob(1, new MockBlob(new byte[] {1, 2, 3}));
        assertEquals(new MockBlob(new byte[] {1, 2, 3}), resultSet.getBlob("column"));
        resultSet.updateBlob("column", new ByteArrayInputStream(new byte[] {1, 2, 3}));
        assertEquals(new MockBlob(new byte[] {1, 2, 3}), resultSet.getBlob(1));
        resultSet.updateBlob("column", new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5}), 3);
        assertEquals(new MockBlob(new byte[] {1, 2, 3}), resultSet.getBlob(1));
        resultSet.updateClob(1, new MockClob("test"));
        assertEquals(new MockClob("test"), resultSet.getClob("column"));
        resultSet.updateClob("column", new StringReader("test"));
        assertEquals(new MockClob("test"), resultSet.getClob(1));
        resultSet.updateClob("column", new StringReader("testxyz"), 4);
        assertEquals(new MockClob("test"), resultSet.getClob(1));
        resultSet.updateNClob("column", new MockNClob("test"));
        assertEquals(new MockNClob("test"), resultSet.getNClob("column"));
        resultSet.updateClob(1, new MockClob("test"));
        assertEquals(new MockNClob("test"), resultSet.getNClob(1));
        resultSet.updateNClob("column", new StringReader("test"));
        assertEquals(new MockNClob("test"), resultSet.getNClob("column"));
        resultSet.updateNClob(1, new StringReader("test"), 4);
        assertEquals(new MockNClob("test"), resultSet.getNClob(1));
    }
    
    public void testUpdateSQLXML() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.addColumn("column", new Object[] { "value" });
        resultSet.next();
        resultSet.updateSQLXML(1, new MockSQLXML("<abc></abc>"));
        assertEquals(new MockSQLXML("<abc></abc>"), resultSet.getSQLXML("column"));
        resultSet.updateSQLXML("column", new MockSQLXML("<abc>abc</abc>"));
        assertEquals(new MockSQLXML("<abc>abc</abc>"), resultSet.getSQLXML(1));
    }
    
    public void testError() throws Exception
    {
        resultSet.setResultSetType(ResultSet.TYPE_FORWARD_ONLY);
        List column = new ArrayList();
        column.add("test");
        resultSet.addColumn("column1", column);
        try
        {
            resultSet.first();            
            fail("not scrollable");
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        try
        {
            resultSet.next();
            resultSet.updateInt(1, 1);
            fail("not updatable");
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        try
        {
            resultSet.updateInt("column2", 1);
            fail("invalid columnname");
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        try
        {
            resultSet.updateInt(2, 1);
            fail("invalid columnindex");
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        try
        {
            resultSet.next();
            resultSet.updateInt(1, 1);
            fail("row invalid");
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
    }
    
    public void testFindColumn() throws Exception
    {
        resultSet.addColumn("test");
        resultSet.addColumn("testxy");
        assertEquals(1, resultSet.findColumn("test"));
        assertEquals(2, resultSet.findColumn("testxy"));
        try
        {
            resultSet.findColumn("test1");
            fail("column invalid");
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
    }
    
    public void testEmptyResultSet() throws Exception
    {
        resultSet.addColumn("test");

        assertFalse(resultSet.isFirst());
        assertFalse(resultSet.isAfterLast());
        assertFalse(resultSet.isBeforeFirst());
        assertFalse(resultSet.isLast());
        
        assertFalse(resultSet.next());
        assertFalse(resultSet.first());
        assertFalse(resultSet.last());
        assertFalse(resultSet.previous());
        assertFalse(resultSet.absolute(1));
        assertFalse(resultSet.relative(1));

        assertFalse(resultSet.isFirst());
        assertFalse(resultSet.isAfterLast());
        assertFalse(resultSet.isBeforeFirst());
        assertFalse(resultSet.isLast());
        
        try
        {
            resultSet.getString("test");
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        try
        {
            resultSet.getString(1);
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw SQLException
        }
    }
    
    public void testInsertOnEmptyResultSet() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.addColumn("test");
        resultSet.moveToInsertRow();
        resultSet.updateString("test", "value");
        resultSet.insertRow();
        resultSet.moveToCurrentRow();
        assertEquals("value", resultSet.getString("test"));
        assertEquals(1, resultSet.getRowCount());
        assertTrue(resultSet.isFirst());
        assertTrue(resultSet.isLast());
    }
    
    public void testCursorPosition() throws Exception
    {
        resultSet.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
        resultSet.addRow(new String[] {"1", "2", "3"});
        resultSet.addRow(new String[] {"4", "5", "6"});
        resultSet.addRow(new String[] {"7", "8", "9"});
        assertTrue(resultSet.isBeforeFirst());
        assertFalse(resultSet.isAfterLast());
        assertTrue(resultSet.last());
        assertTrue(resultSet.isLast());
        assertTrue(resultSet.first());
        assertTrue(resultSet.isFirst());
        resultSet.afterLast();
        assertTrue(resultSet.isAfterLast());
        assertFalse(resultSet.isBeforeFirst());
        resultSet.beforeFirst();
        assertTrue(resultSet.isBeforeFirst());
        assertFalse(resultSet.isAfterLast());
        assertTrue(resultSet.next());
        assertTrue(resultSet.isFirst());
        assertFalse(resultSet.previous());
        assertTrue(resultSet.isBeforeFirst());
        assertTrue(resultSet.absolute(3));
        assertTrue(resultSet.isLast());
        assertTrue(resultSet.absolute(1));
        assertTrue(resultSet.isFirst());
        assertEquals(1, resultSet.getInt(1));
        assertTrue(resultSet.relative(1));
        assertEquals(4, resultSet.getInt(1));
        assertTrue(resultSet.relative(1));
        assertTrue(resultSet.isLast());
        assertEquals(7, resultSet.getInt(1));
        assertTrue(resultSet.relative(-2));
        assertTrue(resultSet.isFirst());
        assertTrue(resultSet.last());
        assertFalse(resultSet.next());
    }
    
    public void testCursorNotAllowed() throws Exception
    {
        resultSet.setResultSetType(ResultSet.TYPE_FORWARD_ONLY);
        resultSet.addRow(new String[] {"1", "2", "3"});
        resultSet.addRow(new String[] {"4", "5", "6"});
        assertTrue(resultSet.isBeforeFirst());
        try
        {
            resultSet.first();
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        assertTrue(resultSet.isBeforeFirst());
        resultSet.next();
        resultSet.next();
        try
        {
            resultSet.previous();
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        try
        {
            resultSet.last();
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        try
        {
            resultSet.relative(-1);
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw SQLException
        }
    }
    
    public void testSetFetchDirection() throws Exception
    {
        try
        {
            resultSet.setFetchDirection(ResultSet.FETCH_REVERSE + 1000);
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw exception
        }
        resultSet.setResultSetType(ResultSet.TYPE_FORWARD_ONLY);
        try
        {
            resultSet.setFetchDirection(ResultSet.FETCH_REVERSE);
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw exception
        }
        resultSet.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
        resultSet.addRow(new String[] {"1", "2", "3"});
        resultSet.addRow(new String[] {"4", "5", "6"});
        resultSet.addRow(new String[] {"7", "8", "9"});
        resultSet.addRow(new String[] {"10", "11", "12"});
        resultSet.last();
        resultSet.setFetchDirection(ResultSet.FETCH_REVERSE);
        assertEquals(ResultSet.FETCH_REVERSE, resultSet.getFetchDirection());
        assertTrue(resultSet.isFirst());
        resultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
        assertEquals(ResultSet.FETCH_FORWARD, resultSet.getFetchDirection());
        resultSet.absolute(3);
        assertEquals(7, resultSet.getInt(1));
        resultSet.setFetchDirection(ResultSet.FETCH_REVERSE);
        assertEquals(ResultSet.FETCH_REVERSE, resultSet.getFetchDirection());
        assertEquals(7, resultSet.getInt(1));
        resultSet.previous();
        assertEquals(10, resultSet.getInt(1));
        assertTrue(resultSet.isFirst());
        resultSet.setFetchDirection(ResultSet.FETCH_REVERSE);
        assertEquals(ResultSet.FETCH_REVERSE, resultSet.getFetchDirection());
        resultSet.absolute(3);
        assertEquals(4, resultSet.getInt(1));
        resultSet.setFetchDirection(ResultSet.FETCH_UNKNOWN);
        assertEquals(ResultSet.FETCH_UNKNOWN, resultSet.getFetchDirection());
        resultSet.absolute(3);
        assertEquals(4, resultSet.getInt(1));
        resultSet.setFetchDirection(ResultSet.FETCH_REVERSE);
        assertEquals(ResultSet.FETCH_REVERSE, resultSet.getFetchDirection());
        resultSet.absolute(3);
        assertEquals(4, resultSet.getInt(1));
        resultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
        assertEquals(ResultSet.FETCH_FORWARD, resultSet.getFetchDirection());
        resultSet.absolute(3);
        assertEquals(7, resultSet.getInt(1));
    }
    
    public void testInsertDeleteRows() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
        resultSet.addRow(new String[] {"1", "2", "3"});
        resultSet.addRow(new String[] {"4", "5", "6"});
        resultSet.addRow(new String[] {"7", "8", "9"});
        resultSet.addRow(new String[] {"10", "11", "12"});
        resultSet.absolute(3);
        resultSet.moveToInsertRow();
        resultSet.updateString(1, "x");
        resultSet.updateString(2, "y");
        resultSet.updateString(3, "z");
        resultSet.insertRow();
        resultSet.moveToCurrentRow();
        assertEquals("x", resultSet.getString(1));
        assertEquals("y", resultSet.getString(2));
        assertEquals("z", resultSet.getString(3));
        resultSet.refreshRow();
        assertEquals("x", resultSet.getString(1));
        assertEquals("y", resultSet.getString(2));
        assertEquals("z", resultSet.getString(3));
        resultSet.next();
        assertEquals("7", resultSet.getString(1));
        resultSet.previous();
        resultSet.previous();
        assertEquals("4", resultSet.getString(1));
        resultSet.deleteRow();
        try
        {
            resultSet.getString(1);
            fail("was deleted");
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        resultSet.first();
        assertFalse(resultSet.rowInserted());
        assertFalse(resultSet.rowDeleted());
        resultSet.next();
        assertFalse(resultSet.rowInserted());
        assertTrue(resultSet.rowDeleted());
        resultSet.next();
        assertTrue(resultSet.rowInserted());
        assertFalse(resultSet.rowDeleted());
        resultSet.next();
        assertFalse(resultSet.rowInserted());
        assertFalse(resultSet.rowDeleted());
    }
    
    public void testInsertRowFailure() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
        resultSet.addRow(new String[] {"1", "2", "3"});
        resultSet.next();
        try
        {
            resultSet.insertRow();
            fail("cursor not in insert row");
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        resultSet.moveToInsertRow();
        try
        {
            resultSet.deleteRow();
            fail("cursor is in insert row");
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        try
        {
            resultSet.updateRow();
            fail("cursor is in insert row");
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        try
        {
            resultSet.cancelRowUpdates();
            fail("cursor is in insert row");
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
    }
    
    public void testUpdateDeleteInvalidRow() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
        resultSet.addRow(new String[] {"1", "2", "3"});
        try
        {
            resultSet.updateRow();
            fail();
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        try
        {
            resultSet.rowUpdated();
            fail();
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        resultSet.next();
        resultSet.next();
        try
        {
            resultSet.deleteRow();
            fail();
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        try
        {
            resultSet.rowDeleted();
            fail();
        }
        catch(SQLException exc)
        {
            //should throw SQLException
        }
    }
    
    public void testCloneOnUpdateRow() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
        resultSet.addRow(new Object[] {new MockBlob(new byte[] {1, 2, 3}), new MockClob("test")});
        resultSet.next();
        Blob blob = resultSet.getBlob(1);
        Clob clob = resultSet.getClob(2);
        blob.setBytes(1, new byte[] {4, 5, 6});
        clob.setString(1, "anothertest");
        assertEquals(new MockBlob(new byte[] {4, 5, 6}), resultSet.getBlob(1));
        assertEquals(new MockClob("anothertest"), resultSet.getClob(2));
        resultSet.setDatabaseView(true);
        assertEquals(new MockBlob(new byte[] {1, 2, 3}), resultSet.getBlob(1));
        assertEquals(new MockClob("test"), resultSet.getClob(2));
        resultSet.updateRow();
        assertEquals(new MockBlob(new byte[] {4, 5, 6}), resultSet.getBlob(1));
        assertEquals(new MockClob("anothertest"), resultSet.getClob(2));
        resultSet.setDatabaseView(false);
        assertEquals(new MockBlob(new byte[] {4, 5, 6}), resultSet.getBlob(1));
        assertEquals(new MockClob("anothertest"), resultSet.getClob(2));
    }
    
    public void testCloneOnInsertRow() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
        resultSet.addColumn("column", new Object[] {"1"});
        resultSet.next();
        resultSet.moveToInsertRow();
        resultSet.updateRef("column", new MockRef("test"));
        resultSet.insertRow();
        resultSet.moveToCurrentRow();
        assertEquals(new MockRef("test"), resultSet.getRef("column"));
        resultSet.setDatabaseView(true);
        assertEquals(new MockRef("test"), resultSet.getRef("column"));
        resultSet.setDatabaseView(false);
        Ref ref = resultSet.getRef("column");
        ref.setObject("anothertest");
        assertEquals(new MockRef("anothertest"), resultSet.getRef("column"));
        resultSet.setDatabaseView(true);
        assertEquals(new MockRef("test"), resultSet.getRef("column"));
    }
    
    public void testDatabaseView() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
        resultSet.addRow(new String[] {"1", "2", "3"});
        resultSet.addRow(new String[] {"4", "5", "6"});
        resultSet.addRow(new String[] {"7", "8", "9"});
        resultSet.addRow(new String[] {"10", "11", "12"});
        resultSet.absolute(3);
        resultSet.updateInt(1, 3);
        assertEquals(3, resultSet.getInt(1));
        assertEquals(8, resultSet.getInt(2));
        assertEquals(9, resultSet.getInt(3));
        resultSet.setDatabaseView(true);
        assertEquals(7, resultSet.getInt(1));
        assertEquals(8, resultSet.getInt(2));
        assertEquals(9, resultSet.getInt(3));
        resultSet.updateRow();
        assertEquals(3, resultSet.getInt(1));
        assertEquals(8, resultSet.getInt(2));
        assertEquals(9, resultSet.getInt(3));
        resultSet.setDatabaseView(false);
        resultSet.first();
        assertFalse(resultSet.rowUpdated());
        resultSet.next();
        assertFalse(resultSet.rowUpdated());
        resultSet.next();
        assertTrue(resultSet.rowUpdated());
        resultSet.next();
        assertFalse(resultSet.rowUpdated());
        resultSet.updateInt(2, 4);
        resultSet.cancelRowUpdates();
        assertEquals(10, resultSet.getInt(1));
        assertEquals(11, resultSet.getInt(2));
        assertEquals(12, resultSet.getInt(3));
        resultSet.setDatabaseView(true);
        assertEquals(10, resultSet.getInt(1));
        assertEquals(11, resultSet.getInt(2));
        assertEquals(12, resultSet.getInt(3));
        assertFalse(resultSet.rowUpdated());
    }
    
    public void testIsEqual() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.addColumn("col1");
        resultSet.addRow(new String[] {"test1", "test2"});
        resultSet.addRow(new String[] {"test3", "test4"});
        resultSet.addRow(new String[] {"test5", "test6"});
        List row1 = resultSet.getRow(1);
        assertEquals("test1", row1.get(0));
        assertEquals("test2", row1.get(1));
        List row3 = resultSet.getRow(3);
        assertEquals("test5", row3.get(0));
        assertEquals("test6", row3.get(1));
        List column2 = resultSet.getColumn(2);
        assertEquals("test2", column2.get(0));
        assertEquals("test4", column2.get(1));
        assertEquals("test6", column2.get(2));
        List column1 = resultSet.getColumn("col1");
        assertEquals("test1", column1.get(0));
        assertEquals("test3", column1.get(1));
        assertEquals("test5", column1.get(2));
        MockResultSet otherResult = new MockResultSet("");
        otherResult.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        otherResult.addRow(new String[] {"test1", "test2"});
        otherResult.addRow(new String[] {"test3", "test4"});
        otherResult.addRow(new String[] {"test5", "test6"});
        assertFalse(resultSet.isEqual(otherResult));
        assertFalse(otherResult.isEqual(resultSet));
        List testList = new ArrayList();
        testList.add("test3");
        testList.add("test4");
        assertTrue(otherResult.isRowEqual(2, testList));
        assertFalse(otherResult.isRowEqual(1, testList));
        assertFalse(otherResult.isColumnEqual(1, testList));
        otherResult.addColumn("col1");
        otherResult = new MockResultSet("");
        otherResult.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        otherResult.addColumn("col1");
        otherResult.addRow(new String[] {"test1", "test2"});
        otherResult.addRow(new String[] {"test3", "test4"});
        otherResult.addRow(new String[] {"test5", "test6"});
        assertTrue(resultSet.isEqual(otherResult));
        assertTrue(otherResult.isEqual(resultSet));
        testList = new ArrayList();
        testList.add("test1");
        testList.add("test3");
        testList.add("test5");
        assertTrue(otherResult.isColumnEqual(1, testList));
        assertTrue(otherResult.isColumnEqual("col1", testList));
        resultSet.next();
        resultSet.next();
        resultSet.updateClob(1, new MockClob("Test"));
        testList = new ArrayList();
        testList.add("test1");
        testList.add(new MockClob("Test"));
        testList.add("test5");
        assertTrue(resultSet.isColumnEqual("col1", testList));
        resultSet.setDatabaseView(true);
        assertFalse(resultSet.isColumnEqual("col1", testList));
        resultSet.updateRow();
        assertTrue(resultSet.isColumnEqual("col1", testList));
        otherResult.next();
        otherResult.next();
        otherResult.updateClob(1, new MockClob("Test"));
        assertTrue(resultSet.isEqual(otherResult));
        assertTrue(otherResult.isEqual(resultSet));
        otherResult.setDatabaseView(true);
        assertFalse(resultSet.isEqual(otherResult));
        assertFalse(otherResult.isEqual(resultSet));
        otherResult.updateRow();
        assertTrue(resultSet.isEqual(otherResult));
        assertTrue(otherResult.isEqual(resultSet));
        otherResult = new MockResultSet("");
        otherResult.addRow(new Integer[] {new Integer(1), new Integer(2), new Integer(3)});
        otherResult.addRow(new Integer[] {new Integer(4), new Integer(5), new Integer(6)});
        otherResult.addRow(new Integer[] {new Integer(7), new Integer(8), new Integer(9)});
        testList = new ArrayList();
        testList.add("1");
        testList.add("4");
        testList.add("7");
        assertTrue(otherResult.isColumnEqual(1, testList));
        testList = new ArrayList();
        testList.add("7");
        testList.add("8");
        testList.add("9");
        assertTrue(otherResult.isRowEqual(3, testList));
        otherResult = new MockResultSet("");
        otherResult.addRow(new Object[] {new Integer[] {new Integer(1), new Integer(2)}});
        testList = new ArrayList();
        testList.add(new Integer[] {new Integer(1), new Integer(2)});
        assertTrue(otherResult.isRowEqual(1, testList));    
    }
    
    public void testIsEqualNotAssignableAndNull() throws Exception
    {
        resultSet.addColumn("col1");
        resultSet.addColumn("col2");
        resultSet.addRow(new Object[] {new Integer(1), null});
        List rowList = new ArrayList();
        rowList.add("1");
        rowList.add(null);
        assertTrue(resultSet.isRowEqual(1, rowList));
        List columnList1 = new ArrayList();
        columnList1.add("1");
        List columnList2 = new ArrayList();
        columnList2.add(null);
        assertTrue(resultSet.isColumnEqual(1, columnList1));
        assertTrue(resultSet.isColumnEqual(2, columnList2));
        assertTrue(resultSet.isColumnEqual("col1", columnList1));
        assertTrue(resultSet.isColumnEqual("col2", columnList2));
        MockResultSet otherResult = new MockResultSet("");
        otherResult.addColumn("col1");
        otherResult.addColumn("col2");
        otherResult.addRow(new String[] {"1", null});
        assertTrue(resultSet.isEqual(otherResult));
    }
    
    public void testRowsInsertedDeletedUpdated() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.addRow(new String[] {"test1", "test2"});
        resultSet.addRow(new String[] {"test3", "test4"});
        resultSet.addRow(new String[] {"test5", "test6"});
        assertFalse(resultSet.rowInserted(1));
        assertFalse(resultSet.rowInserted(2));
        assertFalse(resultSet.rowInserted(3));
        assertFalse(resultSet.rowDeleted(1));
        assertFalse(resultSet.rowDeleted(2));
        assertFalse(resultSet.rowDeleted(3));
        assertFalse(resultSet.rowUpdated(1));
        assertFalse(resultSet.rowUpdated(2));
        assertFalse(resultSet.rowUpdated(3));
        resultSet.next();
        resultSet.next();
        resultSet.deleteRow();
        assertFalse(resultSet.rowInserted(1));
        assertFalse(resultSet.rowInserted(2));
        assertFalse(resultSet.rowInserted(3));
        assertFalse(resultSet.rowDeleted(1));
        assertTrue(resultSet.rowDeleted(2));
        assertFalse(resultSet.rowDeleted(3));
        assertFalse(resultSet.rowUpdated(1));
        assertFalse(resultSet.rowUpdated(2));
        assertFalse(resultSet.rowUpdated(3));
        resultSet.next();
        resultSet.updateRow();
        assertFalse(resultSet.rowInserted(1));
        assertFalse(resultSet.rowInserted(2));
        assertFalse(resultSet.rowInserted(3));
        assertFalse(resultSet.rowDeleted(1));
        assertTrue(resultSet.rowDeleted(2));
        assertFalse(resultSet.rowDeleted(3));
        assertFalse(resultSet.rowUpdated(1));
        assertFalse(resultSet.rowUpdated(2));
        assertTrue(resultSet.rowUpdated(3));
        resultSet.moveToInsertRow();
        resultSet.updateString(1, "xyz");
        resultSet.updateString(2, "xyz");
        resultSet.insertRow();
        resultSet.moveToCurrentRow();
        assertFalse(resultSet.rowInserted(1));
        assertFalse(resultSet.rowInserted(2));
        assertTrue(resultSet.rowInserted(3));
        assertFalse(resultSet.rowInserted(4));
        assertFalse(resultSet.rowDeleted(1));
        assertTrue(resultSet.rowDeleted(2));
        assertFalse(resultSet.rowDeleted(3));
        assertFalse(resultSet.rowDeleted(4));
        assertFalse(resultSet.rowUpdated(1));
        assertFalse(resultSet.rowUpdated(2));
        assertFalse(resultSet.rowUpdated(3));
        assertTrue(resultSet.rowUpdated(4));
    }
    
    public void testRowsInsertDeleteUpdateNotAllowed() throws Exception
    {
        resultSet.addRow(new String[] {"test1", "test2"});
        assertFalse(resultSet.rowInserted(1));
        assertFalse(resultSet.rowDeleted(1));
        assertFalse(resultSet.rowUpdated(1));
        resultSet.next();
        try
        {
            resultSet.deleteRow();
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        assertFalse(resultSet.rowInserted(1));
        assertFalse(resultSet.rowDeleted(1));
        assertFalse(resultSet.rowUpdated(1));
        try
        {
            resultSet.updateRow();
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        assertFalse(resultSet.rowInserted(1));
        assertFalse(resultSet.rowDeleted(1));
        assertFalse(resultSet.rowUpdated(1));
        resultSet.moveToInsertRow();
        try
        {
            resultSet.insertRow();
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw SQLException
        }
        assertFalse(resultSet.rowInserted(1));
        assertFalse(resultSet.rowDeleted(1));
        assertFalse(resultSet.rowUpdated(1));
    }
    
    public void testCloneAndIsEqual() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.addRow(new String[] {"test1", "test2"});
        resultSet.addRow(new String[] {"test3", "test4"});
        resultSet.addRow(new Object[] {new MockClob("test5"), new MockStruct("test6")});
        MockResultSet cloneResult = (MockResultSet)resultSet.clone();
        assertTrue(resultSet.isEqual(cloneResult));
        resultSet.next();
        resultSet.next();
        resultSet.next();
        resultSet.updateClob(1, new MockClob("test"));
        assertFalse(resultSet.isEqual(cloneResult));
        resultSet.setDatabaseView(true);
        assertTrue(resultSet.isEqual(cloneResult));
        resultSet.updateRow();
        assertFalse(resultSet.isEqual(cloneResult));
        resultSet.setDatabaseView(false);
        MockClob clob = (MockClob)resultSet.getClob(1);
        assertEquals("test", clob.getSubString(1, 4));
        clob.setString(1, "xyzx");
        assertEquals("xyzx", clob.getSubString(1, 4));
        List list = new ArrayList();
        list.add(new MockClob("xyzx"));
        list.add(new MockStruct("test6"));
        assertTrue(resultSet.isRowEqual(3, list));
        assertFalse(cloneResult.isRowEqual(3, list));
        list = new ArrayList();
        list.add(new MockClob("test5"));
        list.add(new MockStruct("test6"));
        assertTrue(cloneResult.isRowEqual(3, list));
    }
    
    public void testCloneDeepCopyMetaData() throws Exception
    {
        MockResultSetMetaData metaData1 = new MockResultSetMetaData();
        resultSet.setResultSetMetaData(metaData1);
        MockResultSet cloneResult = (MockResultSet)resultSet.clone();
        assertTrue(resultSet.isEqual(cloneResult));
        assertNotSame(metaData1, cloneResult.getMetaData());
    }
    
    public void testCaseInsensitiveColumns() throws Exception
    {
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.addColumn("first", new String[] {"1", "2", "3"});
        resultSet.addColumn("second", new String[] {"4", "5", "6"});
        resultSet.addColumn("third", new String[] {"7", "true", "9"});
        resultSet.next();
        assertEquals("1", resultSet.getString("FIRST"));
        assertEquals("4", resultSet.getString("second"));
        assertEquals(7, resultSet.getInt("Third"));
        resultSet.next();
        assertEquals("2", resultSet.getObject("FirsT"));
        assertEquals("5", resultSet.getString("sEcond"));
        assertEquals(true, resultSet.getBoolean("THIRD"));
        resultSet.next();
        resultSet.setDatabaseView(true);
        assertEquals("3", resultSet.getString("FIrST"));
        assertEquals(6.0, resultSet.getDouble("second"), 0);
        assertEquals(9, resultSet.getInt("ThiRd"));
        resultSet.moveToInsertRow();
        resultSet.updateString("FIRST", "x");
        resultSet.insertRow();
        resultSet.moveToCurrentRow();
        assertEquals("x", resultSet.getString("first"));
    }
    
    public void testCaseSensitiveColumns() throws Exception
    {
        resultSet.setColumnsCaseSensitive(true);
        resultSet.addColumn("first", new String[] {"1"});
        resultSet.next();
        assertEquals("1", resultSet.getString("first"));
        try
        {
            resultSet.getString("FIRST");
            fail();
        } 
        catch (SQLException e)
        {
            //expected exception
        }
    }
}
