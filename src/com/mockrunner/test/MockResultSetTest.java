package com.mockrunner.test;

import java.util.ArrayList;
import java.util.List;

import com.mockrunner.mock.jdbc.MockResultSet;

import junit.framework.TestCase;

public class MockResultSetTest extends TestCase
{
    private MockResultSet resultSet;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        resultSet = new MockResultSet();
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
}
