package com.mockrunner.test.jdbc;

import com.mockrunner.jdbc.FileResultSetFactory;
import com.mockrunner.mock.jdbc.MockResultSet;

import junit.framework.TestCase;

public class FileResultSetFactoryTest extends TestCase
{
    public void testCreate() throws Exception
    {
        FileResultSetFactory factory = new FileResultSetFactory("src/com/mockrunner/test/jdbc/testresult.txt");
        MockResultSet resultSet = factory.create();
        assertTrue(resultSet.getRowCount() == 5);
        assertTrue(resultSet.getColumnCount() == 3);
        resultSet.next();
        assertEquals("TestColumn1", resultSet.getString(1));
        assertEquals("TestColumn2", resultSet.getString(2));
        assertEquals("TestColumn3", resultSet.getString(3));
        resultSet.next();
        assertEquals(1, resultSet.getInt(1));
        assertEquals(3, resultSet.getLong("Column2"));
        assertEquals(4, resultSet.getShort(3));
        resultSet.next();
        assertEquals("Entry1", resultSet.getObject(1));
        assertEquals("Entry2", resultSet.getString(2));
        assertEquals("Entry3", resultSet.getObject("Column3"));
        resultSet.next();
        assertEquals(25.3, resultSet.getDouble("Column1"), 0.01);
        assertEquals(26.7, resultSet.getDouble(2), 0.01);
        assertEquals(12.3, resultSet.getFloat(3), 0.01);
        resultSet.next();
        assertEquals("Test", resultSet.getString(1));
        assertEquals(null, resultSet.getString(2));
        assertEquals("Test", resultSet.getString(3));
        factory.setFirstLineContainsColumnNames(true);
        resultSet = factory.create();
        assertTrue(resultSet.getRowCount() == 4);
        assertTrue(resultSet.getColumnCount() == 3);
        resultSet.next();
        assertEquals(1, resultSet.getInt("TestColumn1"));
        assertEquals(3, resultSet.getLong("TestColumn2"));
        assertEquals(4, resultSet.getShort("TestColumn3"));
        resultSet.next();
        assertEquals("Entry1", resultSet.getObject(1));
        assertEquals("Entry2", resultSet.getString(2));
        assertEquals("Entry3", resultSet.getObject(3));
    }
}
