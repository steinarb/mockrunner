package com.mockrunner.test.jdbc;

import java.util.Arrays;

import junit.framework.TestCase;

import com.mockrunner.jdbc.ArrayResultSetFactory;
import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Exercises the published contract for <code>ArrayResultSetFactory</code> instances.
 * 
 * @author Erick G. Reid
 */
public class ArrayResultSetFactoryTest extends TestCase 
{
    private ArrayResultSetFactory arrayResultSetFactory;
    private String[] columnNames;
    private String[][] dataMatrix;

    public void setUp() throws Exception
    {
        super.setUp();
        columnNames = new String[] {"id", "name", "address"};
        dataMatrix = new String[][] {{"1", "moe", "123 main"}, 
                                     {"2", "larry", "123 main"},
                                     {"3", "curly", "123 main"}};
        arrayResultSetFactory = new ArrayResultSetFactory(columnNames, dataMatrix);
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
        arrayResultSetFactory = null;
    }

    /**
     * Ensures that the contructors published for
     * <code>ArrayResultSetFactory</code> fulfill their contract.
     * 
     * @throws Exception if an error occurs during testing.
     */
    public void testConstructors() throws Exception
    {
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(null);
            fail("an IllegalArgumentException should have been thrown");
        } 
        catch(IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(null, null);
            fail("an IllegalArgumentException should have been thrown");
        } 
        catch(IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(columnNames, null);
            fail("an IllegalArgumentException should have been thrown");
        } 
        catch(IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(new String[] {"", "", null}, dataMatrix);
            fail("an IllegalArgumentException should have thrown");
        } 
        catch(IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(columnNames, new String[][] {null, {"", "", ""}});
            fail("an IllegalArgumentException should have thrown");
        } 
        catch(IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(columnNames, new String[][] {{"", null, ""}});
            fail("an IllegalArgumentException should have thrown");
        } 
        catch(IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(columnNames, new String[][] {{"", ""}, {"", "", ""}});
            fail("an IllegalArgumentException should have thrown");
        } 
        catch (IllegalArgumentException expected)
        {
            //expected exception
        }
        try
        {
            arrayResultSetFactory = new ArrayResultSetFactory(columnNames, new String[][] {{"", "", ""}, {"", ""}});
            fail("an IllegalArgumentException should have thrown");
        } 
        catch (IllegalArgumentException expected)
        {
            // expected exception
        }
        arrayResultSetFactory = new ArrayResultSetFactory(columnNames, dataMatrix);
    }

    /**
     * Ensures that <code>MockResultSet create(id)</code> fulfills its
     * published contract.
     * 
     * @throws Exception if an error occurs during testing.
     */
    public void testCreate() throws Exception
    {
        MockResultSet mockResultSet = null;
        try
        {
            mockResultSet = arrayResultSetFactory.create(null);
            fail("an IllegalArgumentException should have thrown");
        } 
        catch(IllegalArgumentException expected)
        {
            //expected exception
        }
        mockResultSet = arrayResultSetFactory.create("");
        assertEquals(columnNames.length, mockResultSet.getColumnCount());
        for(int ii = 0; ii < columnNames.length; ii++)
        {
            assertNotNull(mockResultSet.getColumn(columnNames[ii]));
        }
        for(int ii = 0; ii < dataMatrix.length; ii++)
        {
            assertEquals(Arrays.asList(dataMatrix[ii]), mockResultSet.getRow(ii + 1));
        }
    }
}
