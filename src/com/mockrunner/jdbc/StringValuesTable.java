package com.mockrunner.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A data structure providing tabular (row and column) access semantics to
 * items within.  While applicable to several usages, the primary purpose is
 * (in conjunction with <code>ArrayResultSetFactory</code>) to provide for easy 
 * set up of unit test fixtures and assertion of outcomes with the same data 
 * structures, without any need for external sources of test data.
 * 
  * @author Erick G. Reid
 */
public class StringValuesTable 
{
    private String name; // the table name  
    private List columnNames = new ArrayList(0); // the columns
    private String[][] stringMatrix; // the table data
    
    /**
     * Creates a new <code>StringValuesTable</code> with the given name,
     * columns and string matrix.
     * 
     * @param name the table name. This argument cannot be <code>null</code>
     *             and must contain at least <code>1</code> non-blank character.
     * @param stringMatrix the string matrix. This argument cannot be <code>null</code>,
     *                     must not contain any null values, and each array in the matrix
     *                     must contain the same number of elements as the first (<code>stringMatrix[0].length == stringMatrix[n].length</code>
     *                     for any given valid row number, <code>n</code>). Further,
     *                     this matrix must, at a minimum represent <code>1</code> row
     *                     and <code>1</code> column of items (<code>stringMatrix.length >= 1</code>,
     *                     and <code>stringMatrix[0].length >= 1</code>).
     */
    public StringValuesTable(String name, String[][] stringMatrix)
    {
        this(name, null, stringMatrix);
    }

    /**
     * Creates a new <code>StringValuesTable</code> with the given name,
     * columns and string matrix.
     * 
     * @param name the table name. This argument cannot be <code>null</code>
     *             and must contain at least <code>1</code> non-blank character.
     * @param columnNames the names for the columns in this <code>
     *                    StringValuesTable</code>. This argument may be <code>null</code> if no column names
     *                    are desired, but if a non-<code>null</code> array reference
     *                    is given, the array cannot contain any <code>null</code> nor
     *                    duplicate elements, and must have the same number of elements
     *                    as there are columns in the given string matrix (<code>stringMatrix[n]</code>
     *                    for any given valid row number, <code>n</code>).
     * @param stringMatrix the string matrix. This argument cannot be <code>null</code>,
     *                     must not contain any null values, and each array in the matrix
     *                     must contain the same number of elements as the first (<code>stringMatrix[0].length == stringMatrix[n].length</code>
     *                     for any given valid row number, <code>n</code>). Further,
     *                     this matrix must, at a minimum represent <code>1</code> row
     *                     and <code>1</code> column of items (<code>stringMatrix.length >= 1</code>,
     *                     and <code>stringMatrix[0].length >= 1</code>).
     */
    public StringValuesTable(String name, String[] columnNames, String[][] stringMatrix)
    {

        if (name != null)
        {
            if (name.trim().length() > 0)
            {
                this.name = name;
                this.stringMatrix = verifyStringMatrix(stringMatrix);
                if (columnNames != null)
                {
                    this.columnNames = Arrays.asList(verifyColumnNames(columnNames, stringMatrix));
                }
                return;
            }
            throw new IllegalArgumentException("invalid table name given");
        }
        throw new IllegalArgumentException("the table name cannot be null");
    }

    /**
     * Returns the contents of the given column.
     * 
     * @param columnName the name of the desired column. This argument cannot be
     *                   <code>null</code> and must be a valid column for this
     *                   <code>StringValuesTable</code>.
     * @return the contents of the given column.
     */
    public String[] getColumn(String columnName)
    {
        if (columnName != null)
        {
            int index = this.columnNames.indexOf(columnName);
            if (index >= 0)
            {
                return doGetColumn(index);
            }
            throw new IllegalArgumentException(columnName
                    + " is not a valid column name");
        }
        throw new IllegalArgumentException("the column name cannot be null");
    }

    /**
     * Returns the contents of the given column.
     * 
     * @param columnNumber the index of the desired column (<code>1</code>-based).
     *                     This argument must be a valid column index for this
     *                     <code>StringValuesTable</code>.
     * @return the contents of the given column.
     */
    public String[] getColumn(int columnNumber)
    {
        return doGetColumn(--columnNumber);
    }

    /**
     * Returns the column names. This array may be empty if column names are not
     * being used.
     * 
     * @return the column names.
     */
    public String[] getColumnNames()
    {
        return (String[]) this.columnNames.toArray(new String[this.columnNames.size()]);
    }

    /**
     * Returns the item found in the string matrix at the given coordinate.
     * 
     * @param rowNumber the number of the desired row (<code>1</code>-based). This
     *                  argument must be a valid row number for this
     *                  <code>StringValuesTable</code>.
     * @param columnName the name of the desired column. This argument cannot be
     *                   <code>null</code> and must be a valid column for this
     *                   <code>StringValuesTable</code>.
     * @return the item found in the string matrix at the given coordinate.
     */
    public String getItem(int rowNumber, String columnName)
    {
        if (columnName != null)
        {
            int index = this.columnNames.indexOf(columnName);
            if (index >= 0)
            {
                return doGetRow(rowNumber)[index];
            }
            throw new IllegalArgumentException(columnName + " is not a valid column index");
        }
        throw new IllegalArgumentException("the column name cannot be null");
    }

    /**
     * Returns the item found in the string matrix at the given coordinate.
     * 
     * @param rowNumber the number of the desired row (<code>1</code>-based). This
     *                  argument must be a valid row number for this
     *                  <code>StringValuesTable</code>.
     * @param columnNumber the index of the desired column (<code>1</code>-based).
     *                     This argument must be a valid column index for this
     *                     <code>StringValuesTable</code>.
     * @return the item found in the string matrix at the given coordinate.
     */
    public String getItem(int rowNumber, int columnNumber)
    {
        if (isColumnNumberValid(columnNumber))
        {
            return doGetRow(rowNumber)[--columnNumber];
        }
        throw new IllegalArgumentException(columnNumber + " is not a valid column index");
    }

    /**
     * Returns the table name.
     * 
     * @return the table name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Returns the number of columns found in the string matrix for this
     * <code>StringValuesTable</code>.
     * 
     * @return the number of columns found in the string matrix for this
     *         <code>StringValuesTable</code>.
     */
    public int getNumberOfColumns()
    {
        return this.stringMatrix[0].length;
    }

    /**
     * Returns the number of rows found in the string matrix for this
     * <code>StringValuesTable</code>.
     * 
     * @return the number of rows found in the string matrix for this
     *         <code>StringValuesTable</code>.
     */
    public int getNumberOfRows()
    {
        return this.stringMatrix.length;
    }

    /**
     * Returns the elements of the given row.
     * 
     * @param rowNumber the number of the desired row (<code>1</code>-based). This
     *                  argument must be a valid row number for this
     *                  <code>StringValuesTable</code>.
     * @return the elements of the given row.
     */
    public String[] getRow(int rowNumber)
    {
        return doGetRow(rowNumber);
    }

    /**
     * Returns <code>true</code> if the given column name is valid for this
     * <code>StringValuesTable</code>; returns <code>false</code>
     * otherwise.
     * 
     * @param columnName the column name to verify.
     * @return <code>true</code> if the given column name is valid for this
     *         <code>StringValuesTable</code>.
     */
    public boolean isValidColumnName(String columnName)
    {
        return columnName == null ? false : isColumnNumberValid(this.columnNames.indexOf(columnName) + 1);
    }

    /**
     * Returns <code>true</code> if the given column number is valid for this
     * <code>StringValuesTable</code>; returns <code>false</code>
     * otherwise.
     * 
     * @param columnNumber the column number to verify.
     * @return <code>true</code> if the given column number is valid for this
     *         <code>StringValuesTable</code>.
     */
    public boolean isValidColumnNumber(int columnNumber)
    {
        return isColumnNumberValid(columnNumber);
    }

    /**
     * Returns <code>true</code> if the given row number is valid for this
     * <code>StringValuesTable</code>; returns <code>false</code>
     * otherwise.
     * 
     * @param row the row number to verify.
     * @return <code>true</code> if the given index is valid for this
     *         <code>StringValuesTable</code>.
     */
    public boolean isValidRowNumber(int row)
    {
        return --row >= 0 && row < this.stringMatrix.length;
    }

    /**
     * Returns the tabular data for this <code>StringValuesTable</code>.
     * 
     * @return the tabular data for this <code>StringValuesTable</code>.
     */
    public String[][] getStringMatrix()
    {
        return this.stringMatrix;
    }

    /**
     * Returns the given array if it is found to indeed be valid according to
     * the published contract.
     */
    public synchronized static String[] verifyColumnNames(final String[] columnNames, final String[][] stringMatrix)
    {
        // note: the string matrix must already have been verified at this
        // point...

        if (columnNames != null)
        {
            if (columnNames.length == stringMatrix[0].length)
            {
                String name = null;
                Set names = new HashSet();
                for (int i = 0; i < columnNames.length; i++)
                {
                    name = columnNames[i];
                    if (name == null)
                    {
                        throw new IllegalArgumentException("the column names array must not contain null elements");
                    }
                    else
                    {
                        if (names.contains(name))
                        {
                            throw new IllegalArgumentException("the column names array must not contain duplicate elements");
                        }
                        names.add(name);
                    }
                }
                return columnNames;
            }
            throw new IllegalArgumentException(columnNames.length + " columns were given where " + stringMatrix[0].length
                                               + (stringMatrix[0].length == 1 ? " is" : " are") + " required");
        }
        throw new IllegalArgumentException("the column names array cannot be null");
    }

    /**
     * Returns the given matrix if it is found to indeed be valid according to
     * the published contract.
     */
    public synchronized static String[][] verifyStringMatrix(final String[][] stringMatrix)
    {
        if (stringMatrix != null)
        {
            if (stringMatrix.length > 0)
            {
                if (stringMatrix[0] != null && stringMatrix[0].length > 0)
                {
                    for (int ii = 0; ii < stringMatrix.length; ii++)
                    {
                        if (stringMatrix[ii] == null)
                        {
                            throw new IllegalArgumentException("the string matrix cannot contain any null arrays");
                        }
                        if (stringMatrix[ii].length != stringMatrix[0].length)
                        {
                            throw new IllegalArgumentException("arrays in the string matrix must all contain "
                                                               + stringMatrix[0].length + " elements");
                        }
                        for (int jj = 0; jj < stringMatrix[ii].length; jj++)
                        {
                            if (stringMatrix[ii][jj] == null)
                            {
                                throw new IllegalArgumentException("arrays in the string matrix must not contain null elements");
                            }
                        }
                    }
                    return stringMatrix;
                }
                throw new IllegalArgumentException("the string matrix must contain at least 1 column of items");
            }
            throw new IllegalArgumentException("the string matrix must contain at least 1 row of items");
        }
        throw new IllegalArgumentException("the string matrix cannot be null");
    }

    /**
     * Returns an array of items from each row of the given column.
     */
    private String[] doGetColumn(int index)
    {
        if (index >= 0 && index < this.stringMatrix[0].length)
        {
            String[] data = new String[this.stringMatrix.length];
            for (int row = 0; row < this.stringMatrix.length; row++)
            {
                data[row] = this.stringMatrix[row][index];
            }
            return data;
        }
        throw new IllegalArgumentException(++index + " is not a valid column index");
    }

    /**
     * Returns the elements of the given row.
     */
    private String[] doGetRow(int rowNumber)
    {
        if (--rowNumber >= 0 && rowNumber < this.stringMatrix.length)
        {
            return this.stringMatrix[rowNumber];
        }
        throw new IllegalArgumentException(++rowNumber + " is not a valid row number");
    }

    /**
     * Returns <code>true</code> if the given column number is valid
     */
    private boolean isColumnNumberValid(int columnNumber)
    {
        return --columnNumber >= 0 && columnNumber < this.stringMatrix[0].length;
    }
}

