package com.mockrunner.test.jdbc;

import junit.framework.TestCase;

import com.mockrunner.jdbc.StringValuesTable;

/**
 * Exercises the published contract for <code>StringValuesTable</code> 
 * instances.
* 
 * @author Erick G. Reid
 */
public class StringValuesTableTest extends TestCase 
{
    private StringValuesTable stringValuesTable;
    private String tableName;
    private String[] columnNames;
    private String[][] stringMatrix;

    /**
     * Set up the test fixture.
     */
    public void setUp() throws Exception
    {
        super.setUp();
        tableName = "initech-employees";
        columnNames = new String[] { "id", "name", "address" };
        stringMatrix = new String[][] { new String[] { "1", "gibbons", "peter" }, new String[] { "2", "lumbergh", "bill" }, new String[] { "3", "waddams", "milton" }, };
        stringValuesTable = new StringValuesTable(tableName, columnNames, stringMatrix);
    }

    /**
     * Tear down the test fixture.
     */
    public void tearDown() throws Exception
    {
        super.tearDown();
        columnNames = null;
        stringMatrix = null;
        stringValuesTable = null;
    }

    /**
     * Ensures that the contructors published for <code>StringValuesTable</code>
     * fulfill their contract.
     * 
     * @throws Exception is an error occurs during testing.
     */
    public void testConstructors() throws Exception
    {
        try
        {
            stringValuesTable = new StringValuesTable(null, columnNames, stringMatrix);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            stringValuesTable = new StringValuesTable("", columnNames, stringMatrix);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            stringValuesTable = new StringValuesTable(" ", columnNames, stringMatrix);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            stringValuesTable = new StringValuesTable(tableName, columnNames, null);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            // test null column name:
            //
            stringValuesTable = new StringValuesTable(tableName, new String[] { "id", null, "lastname" }, stringMatrix);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            // test invalid number of columns:
            //
            stringValuesTable = new StringValuesTable(tableName, new String[] { "id", "lastname" }, stringMatrix);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            // test duplicate columns:
            //
            stringValuesTable = new StringValuesTable(tableName, new String[] { "id", "id", "lastname" }, stringMatrix);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            // test invalid number of columns:
            //
            stringValuesTable = new StringValuesTable(tableName, columnNames, new String[][] { new String[] { "1", "gibbons", "peter" }, new String[] { "2", "lumbergh", }, new String[] { "3", "waddams", "milton" }, });
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            // test invalid number of columns:
            //
            stringValuesTable = new StringValuesTable(tableName, null, new String[][] { new String[] { "1", "gibbons", "peter" }, new String[] { "2", "lumbergh", }, new String[] { "3", "waddams", "milton" }, });
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            // test null matrix element:
            //
            stringValuesTable = new StringValuesTable(tableName, columnNames, new String[][] { new String[] { "1", "gibbons", "peter" }, new String[] { "2", "lumbergh", null }, new String[] { "3", "waddams", "milton" }, });
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            // test invalid number of columns:
            //
            stringValuesTable = new StringValuesTable(tableName, null, new String[][] { new String[] { "1", "gibbons", "peter" }, new String[] { "2", "lumbergh", null }, new String[] { "3", "waddams", "milton" }, });
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            // test violate minimum matrix size constraint:
            //
            stringValuesTable = new StringValuesTable(tableName, null, new String[0][0]);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
    }

    /**
     * Ensures that published contract for
     * <code>String[] getColumn(String)</code> is upheld.
     * 
     * @throws Exception is an error occurs during testing.
     */
    public void testGetColumnByName() throws Exception
    {
        try
        {
            stringValuesTable.getColumn(null);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
        }
        try
        {
            stringValuesTable.getColumn("");
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        // assert expected values from valid calls:
        //
        for (int column = 0; column < columnNames.length; column++)
        {
            String[] values = stringValuesTable.getColumn(columnNames[column]);
            for (int row = 0; row < values.length; row++)
            {
                assertEquals(values[row], stringMatrix[row][column]);
            }
        }
        // exercise tables with no columns:
        //
        stringValuesTable = new StringValuesTable(tableName, null, stringMatrix);
        try
        {
            stringValuesTable.getColumn("id");
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        stringValuesTable = new StringValuesTable(tableName, stringMatrix);
        try
        {
            stringValuesTable.getColumn("id");
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
        }
    }

    /**
     * Ensures that published contract for <code>String[] getColumn(int)</code>
     * is upheld.
     * 
     * @throws Exception is an error occurs during testing.
     */
    public void testGetColumnByNumber() throws Exception
    {
        try
        {
            stringValuesTable.getColumn(0);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            stringValuesTable.getColumn(-1);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            stringValuesTable.getColumn(columnNames.length + 1);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        // assert expected values from valid calls:
        //
        for (int column = 1; column <= columnNames.length; column++)
        {
            String[] values = stringValuesTable.getColumn(column);
            for (int row = 0; row < values.length; row++)
            {
                assertEquals(values[row], stringMatrix[row][column - 1]);
            }
        }
    }

    /**
     * Ensures that published contract for
     * <code>String[] getColumnNames()</code> is upheld.
     * 
     * @throws Exception is an error occurs during testing.
     */
    public void testGetColumnNames() throws Exception
    {
        String[] columnNames = stringValuesTable.getColumnNames();
        assertEquals(columnNames.length, columnNames.length);
        for (int i = 0; i < columnNames.length; i++)
        {
            assertEquals(columnNames[i], columnNames[i]);
        }
        // exercise tables with no columns:
        //
        stringValuesTable = new StringValuesTable(tableName, null, stringMatrix);
        assertTrue(stringValuesTable.getColumnNames().length == 0);
        stringValuesTable = new StringValuesTable(tableName, stringMatrix);
        assertTrue(stringValuesTable.getColumnNames().length == 0);
    }

    /**
     * Ensures that published contract for
     * <code>String getItem(int, String)</code> is upheld.
     * 
     * @throws Exception is an error occurs during testing.
     */
    public void testGetItemColumnName() throws Exception
    {
        try
        {
            stringValuesTable.getItem(1, null);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            stringValuesTable.getItem(1, "");
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            stringValuesTable.getItem(-1, columnNames[0]);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        // assert expected values from valid calls:
        //
        for (int row = 1; row <= stringMatrix.length; row++)
        {
            for (int column = 1; column <= columnNames.length; column++)
            {
                assertEquals(stringMatrix[row - 1][column - 1], stringValuesTable.getItem(row, columnNames[column - 1]));
            }
        }
    }

    /**
     * Ensures that published contract for <code>String getItem(int, int)</code>
     * is upheld.
     * 
     * @throws Exception is an error occurs during testing.
     */
    public void testGetItemColumnNumber() throws Exception
    {
        try
        {
            stringValuesTable.getItem(1, -1);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            stringValuesTable.getItem(1, 0);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            stringValuesTable.getItem(-1, 1);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        // assert expected values from valid calls:
        //
        for (int row = 1; row <= stringMatrix.length; row++)
        {
            for (int column = 1; column <= columnNames.length; column++)
            {
                assertEquals(stringMatrix[row - 1][column - 1], stringValuesTable.getItem(row, column));
            }
        }
    }

    /**
     * Ensures that published contract for <code>String getName()</code> is
     * upheld.
     * 
     * @throws Exception is an error occurs during testing.
     */
    public void testGetName() throws Exception
    {
        assertEquals(tableName, stringValuesTable.getName());
    }

    /**
     * Ensures that published contract for <code>int getNumberOfColumns()</code>
     * is upheld.
     * 
     * @throws Exception is an error occurs during testing.
     */
    public void testGetNumberOfColumns() throws Exception
    {
        assertEquals(columnNames.length, stringValuesTable.getNumberOfColumns());
        stringValuesTable = new StringValuesTable(tableName, null, stringMatrix);
        assertEquals(stringMatrix[0].length, stringValuesTable.getNumberOfColumns());
        stringValuesTable = new StringValuesTable(tableName, stringMatrix);
        assertEquals(stringMatrix[0].length, stringValuesTable.getNumberOfColumns());
    }

    /**
     * Ensures that published contract for <code>int getNumberOfRows()</code> 
     * is upheld.
     *
     * @throws Exception is an error occurs during testing.
     */
    public void testGetNumberOfRows() throws Exception
    {
        assertEquals(stringMatrix.length, stringValuesTable.getNumberOfRows());
    }

    /**
     * Ensures that published contract for <code>String[] getRow(int)</code> 
     * is upheld.
     *
     * @throws Exception is an error occurs during testing.
     */
    public void testGetRow() throws Exception
    {
        try
        {
            stringValuesTable.getRow(0);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            stringValuesTable.getRow(-1);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            stringValuesTable.getColumn(stringMatrix.length + 1);
            fail("an IllegalArgumentException should have thrown");
        }
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        // assert expected values from valid calls:
        //
        for (int row = 1; row <= stringMatrix.length; row++)
        {
            String[] values = stringValuesTable.getRow(row);
            for (int column = 0; column < values.length; column++)
            {
                assertEquals(stringMatrix[row - 1][column], values[column]);
            }
        }
    }

    /**
     * Ensures that published contract for 
     * <code>boolean isValidColumnName(String)</code> is upheld.
     *
     * @throws Exception is an error occurs during testing.
     */
    public void testIsValidColumnName() throws Exception
    {
        assertFalse(stringValuesTable.isValidColumnName(null));
        assertFalse(stringValuesTable.isValidColumnName(""));
        assertFalse(stringValuesTable.isValidColumnName(columnNames[0] + "foo"));
        for (int ii = 0; ii < columnNames.length; ii++)
        {
            assertTrue(stringValuesTable.isValidColumnName(columnNames[ii]));
        }
    }

    /**
     * Ensures that published contract for 
     * <code>boolean isValidColumnNumber(int)</code> is upheld.
     *
     * @throws Exception is an error occurs during testing.
     */
    public void testIsValidColumnNumber() throws Exception
    {
        assertFalse(stringValuesTable.isValidColumnNumber(0));
        assertFalse(stringValuesTable.isValidColumnNumber(-1));
        assertFalse(stringValuesTable.isValidColumnNumber(columnNames.length + 1));
        for (int ii = 1; ii <= stringValuesTable.getColumnNames().length; ii++)
        {
            assertTrue(stringValuesTable.isValidColumnNumber(ii));
        }
    }

    /**
     * Ensures that published contract for 
     * <code>boolean isValidRowNumber(int)</code> is upheld.
     *
     * @throws Exception is an error occurs during testing.
     */
    public void testIsValidRowNumber() throws Exception
    {
        assertFalse(stringValuesTable.isValidRowNumber(0));
        assertFalse(stringValuesTable.isValidRowNumber(-1));
        assertFalse(stringValuesTable.isValidRowNumber(stringMatrix.length + 1));
        for (int ii = 1; ii <= stringValuesTable.getNumberOfRows(); ii++)
        {
            assertTrue(stringValuesTable.isValidRowNumber(ii));
        }
    }
}

