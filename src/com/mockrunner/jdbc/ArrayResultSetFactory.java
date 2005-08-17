package com.mockrunner.jdbc;

import com.mockrunner.jdbc.ResultSetFactory;
import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * A <code>ResultSetFactory</code> implementation which will produce 
 * <code>MockResultSet</code> instances based on information given as 
 * <code>String</code> arrays.
 * 
 * @author Erick G. Reid
 */
public class ArrayResultSetFactory implements ResultSetFactory 
{
    private String[] columnNames;
    private String[][] dataMatrix;

    /**
     * Creates a new <code>ArrayResultSetFactory</code> with the given arrays
     * for data representation.
     * 
     * @param dataMatrix the data representation for the result sets this factory will
     *                   produce. This argument cannot be <code>null</code>, must
     *                   not contain any null values, and each array in the matrix must
     *                   contain the same number of elements as the first.
     */
    public ArrayResultSetFactory(String[][] dataMatrix)
    {
        this.dataMatrix = verifyDataMatrixArray(dataMatrix);
    }

    /**
     * Creates a new <code>ArrayResultSetFactory</code> with the given set of
     * column names and the given arrays for data representation.
     * 
     * @param columnNames the column names for the result sets this factory will
     *                    produce. This argument cannot be <code>null</code> and must
     *                    not contain any <code>null</code> elements.
     * @param dataMatrix the data representation for the result sets this factory will
     *                   produce. This argument cannot be <code>null</code>, must
     *                   not contain any null values, and each array in the matrix must
     *                   contain the same number of elements as the first.
     */
    public ArrayResultSetFactory(String[] columnNames, String[][] dataMatrix)
    {
        this.columnNames = verifyColumnNames(columnNames);
        if(dataMatrix != null && dataMatrix[0] != null && dataMatrix[0].length != this.columnNames.length)
        {

            throw new IllegalArgumentException("matrix arrays must all contain " + columnNames.length + " elements");
        }
        this.dataMatrix = verifyDataMatrixArray(dataMatrix);
    }

    /**
     * Returns a <code>MockResultSet</code> with the given ID, containing
     * values based on the elements given at construction.
     * 
     * @param id the ID for the result set. This argument cannot be<code>null</code>.
     */
    public MockResultSet create(String id)
    {
        if(id != null)
        {
            MockResultSet resultSet = new MockResultSet(id);
            if(this.columnNames != null)
            {
                for (int ii = 0; ii < this.columnNames.length; ii++)
                {
                    resultSet.addColumn(this.columnNames[ii]);
                }
            }
            for(int jj = 0; jj < this.dataMatrix.length; jj++)
            {
                resultSet.addRow(this.dataMatrix[jj]);
            }
            return resultSet;
        }
        throw new IllegalArgumentException("the result set ID cannot be null");
    }

    /**
     * Returns the given matrix if it is found to indeed be valid according to
     * the published contract.
     */
    private String[][] verifyDataMatrixArray(String[][] dataRows)
    {
        if(dataRows != null)
        {
            for(int ii = 0; ii < dataRows.length; ii++)
            {
                if(dataRows[ii] == null)
                {
                    throw new IllegalArgumentException("the data matrix cannot contain any null arrays");
                }
                if(dataRows[ii].length != dataRows[0].length)
                {
                    throw new IllegalArgumentException("arrays in the data matrix must all contain " + dataRows[0].length + " elements");
                }
                for(int jj = 0; jj < dataRows[ii].length; jj++)
                {
                    if(dataRows[ii][jj] == null)
                    {
                        throw new IllegalArgumentException("arrays in the data matrix must not contain null elements");
                    }
                }
            }
            return dataRows;
        }
        throw new IllegalArgumentException("the data matrix cannot be null");
    }

    /**
     * Returns the given array if it is found to indeed be valid according to the
     * published contract.
     */
    private String[] verifyColumnNames(String[] columnNames)
    {
        if(columnNames != null)
        {
            for(int ii = 0; ii < columnNames.length; ii++)
            {
                if (columnNames[ii] == null)
                {
                    throw new IllegalArgumentException("the column names array must not contain null elements");
                }
            }
            return columnNames;
        }
        throw new IllegalArgumentException("the column names array cannot be null");
    }
}
