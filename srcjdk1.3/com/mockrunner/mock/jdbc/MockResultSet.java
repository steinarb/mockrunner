package com.mockrunner.mock.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.jdbc.ParameterUtil;
import com.mockrunner.jdbc.SQLUtil;
import com.mockrunner.util.common.ArrayUtil;
import com.mockrunner.util.common.CaseAwareMap;
import com.mockrunner.util.common.CollectionUtil;
import com.mockrunner.util.common.StreamUtil;
import com.mockrunner.util.common.StringUtil;

/**
 * Mock implementation of <code>ResultSet</code>.
 * Can be used to add simulated database entries.
 * You can add Java objects of any type. This
 * mock implementation does not care about SQL
 * data types. It tries to perform the necessary
 * type conversions for the Java objects (e.g. it will convert a 
 * <code>String</code> "1" to <code>int</code> 1). 
 * Please check out the documentation of <code>ResultSet</code> 
 * for the description of the methods in this interface. 
 * The additional methods are described here.
 */
public class MockResultSet implements ResultSet, Cloneable
{
    private Statement statement;
    private String id;
    private Map columnMap;
    private Map columnMapCopy;
    private Map insertRow;
    private List columnNameList;
    private List updatedRows;
    private List deletedRows;
    private List insertedRows;
    private int cursor;
    private boolean isCursorInInsertRow;
    private boolean wasNull;
    private String cursorName;
    private int fetchSize = 0;
    private int fetchDirection = ResultSet.FETCH_FORWARD;
    private int resultSetType = ResultSet.TYPE_SCROLL_INSENSITIVE;
    private int resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;
    private boolean isDatabaseView;
    private ResultSetMetaData resultSetMetaData;
    private boolean closed;
    private boolean columnsCaseSensitive;
    
    public MockResultSet(String id)
    {
        this(id, "");
    }
    
    public MockResultSet(String id, String cursorName)
    {
        init();
        this.cursorName = cursorName;
        this.id = id;
        columnsCaseSensitive = false;
    }
    
    private void init()
    {
        columnMap = createCaseAwareMap();
        columnNameList = new ArrayList();
        updatedRows = new ArrayList();
        deletedRows = new ArrayList();
        insertedRows = new ArrayList();
        cursor = -1;
        wasNull = false;
        closed = false;
        isCursorInInsertRow = false;
        isDatabaseView = false;
        resultSetMetaData = null;
        copyColumnMap();
        adjustInsertRow();
    }
    
    /**
     * Set if column names are case sensitive. Default is
     * <code>false</code>. Please note, that switching this
     * attribute clears and resets the complete <code>ResultSet</code>.
     * @param columnsCaseSensitive are column names case sensitive
     */
    public void setColumnsCaseSensitive(boolean columnsCaseSensitive)
    {
        this.columnsCaseSensitive = columnsCaseSensitive;
        init();
    }

    /**
     * Copies this <code>ResultSet</code>. The data of the
     * <code>ResultSet</code> is copied using the
     * {@link com.mockrunner.jdbc.ParameterUtil#copyParameter}
     * method.
     * @return a copy of this <code>ResultSet</code>
     */
    public Object clone()
    {
        try
        {       
            MockResultSet copy = (MockResultSet)super.clone();
            copy.columnNameList = new ArrayList(columnNameList);
            copy.updatedRows = new ArrayList(updatedRows);
            copy.deletedRows = new ArrayList(deletedRows);
            copy.insertedRows = new ArrayList(insertedRows);
            copy.insertRow = copyColumnDataMap(insertRow);
            copy.columnMap = copyColumnDataMap(columnMap);
            copy.columnMapCopy = copyColumnDataMap(columnMapCopy);
            return copy;
        }
        catch(CloneNotSupportedException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Returns the id of this <code>ResultSet</code>. Ids are used
     * to identify <code>ResultSet</code> objects in tests, because
     * they are usually cloned when executing statements, so
     * you cannot rely on the object identity.
     * @return the id of this <code>ResultSet</code>
     */
    public String getId()
    {
        return id;
    }
    
    /**
     * Returns if this <code>ResultSet</code> is closed.
     * @return <code>true</code> if this <code>ResultSet</code> is closed,
     *         <code>false</code> otherwise
     */
    public boolean isClosed()
    {
        return closed;
    }
    
    /**
     * Sets the <code>ResultSetMetaData</code> for this <code>ResultSet</code>.
     * The specified object will be returned when calling {@link #getMetaData}.
     * If no <code>ResultSetMetaData</code> is set, the method {@link #getMetaData}
     * will return an object of {@link MockResultSetMetaData}. The
     * <code>MockResultSetMetaData</code> returns default values for most
     * of its attributes (however the correct number of columns will be
     * returned). Usually you do not have to set the <code>ResultSetMetaData</code>.
     * @param resultSetMetaData the <code>ResultSetMetaData</code>
     */
    public void setResultSetMetaData(ResultSetMetaData resultSetMetaData)
    {
        this.resultSetMetaData = resultSetMetaData;
    }
    
    /**
     * Sets the <code>Statement</code> for this <code>ResultSet</code>.
     * The <code>ResultSet</code> takes the result set type, result
     * set concurrency and the fetch direction from the specified
     * <code>Statement</code>.
     * @param statement the statement
     */
    public void setStatement(Statement statement)
    {
        this.statement = statement;
        try
        {
            fetchDirection = statement.getFetchDirection();
            resultSetType = statement.getResultSetType();
            resultSetConcurrency = statement.getResultSetConcurrency();
            fetchSize = statement.getFetchSize();
            cursorName = ((MockStatement)statement).getCursorName();
        }
        catch(SQLException exc)
        {

        }
    }
    
    /**
     * Sets the cursor name. It's not possible to set
     * this in a real <code>ResultSet</code>.
     * @param cursorName the cursor name
     */
    public void setCursorName(String cursorName)
    {
        this.cursorName = cursorName;
    }
    
    /**
     * Sets the result set type. It's not possible to set
     * this in a real <code>ResultSet</code>, but in tests
     * it can make sense to change it.
     * @param resultSetType the result set type
     */
    public void setResultSetType(int resultSetType)
    {
        this.resultSetType = resultSetType;
    }
    
    /**
     * Sets the result set concurrency. It's not possible to set
     * this in a real <code>ResultSet</code>, but in tests
     * it can make sense to change it.
     * @param resultSetConcurrency the result set concurrency
     */
    public void setResultSetConcurrency(int resultSetConcurrency)
    {
        this.resultSetConcurrency = resultSetConcurrency;
    }
    
    /**
     * The <code>MockResultSet</code> keeps the data that's
     * stored in the simulated database and a copy of the data
     * that represents the current <code>ResultSet</code> data.
     * The <code>update</code> methods only update the 
     * <code>ResultSet</code> data. This data will be persisted
     * when you call {@link #updateRow}. When you set <i>databaseView</i> 
     * to <code>true</code> the <code>get</code> methods will return the 
     * data in the database, otherwise the current <code>ResultSet</code> 
     * data is returned.
     * @param databaseView <code>false</code> = get the data from the 
     *        <code>ResultSet</code>, <code>true</code> = get the data
     *        from the database, default is <code>false</code>
     *
     */
    public void setDatabaseView(boolean databaseView)
    {
        this.isDatabaseView = databaseView;
    }
    
    /**
     * Adds a row to the simulated database table.
     * If there are not enough columns (initially there
     * are no columns, you have to specify them with the
     * <code>addColumn</code> methods) the missing columns will
     * be added automatically. Automatically created columns
     * will get the name <i>ColumnX</i> where <i>X</i> is
     * the column index.
     * @param values the row data as array, the array index
     *        corresponds to the column index, i.e.
     *        values[0] will be stored in the first column
     *        and so on
     */
    public void addRow(Object[] values)
    {
        List valueList = ArrayUtil.getListFromObjectArray(values);
        addRow(valueList);
    }
    
    /**
     * Adds a row to the simulated database table.
     * If there are not enough columns (initially there
     * are no columns, you have to specify them with the
     * <code>addColumn</code> methods) the missing columns will
     * be added automatically. Automatically created columns
     * will get the name <i>ColumnX</i> where <i>X</i> is
     * the column index.
     * @param values the row data as <code>List</code>, the index
     *        in the <code>List</code> corresponds to the column 
     *        index, i.e. values.get(0) will be stored in the first 
     *        column and so on
     */
    public void addRow(List values)
    {
        int missingColumns = values.size() - columnNameList.size();
        for(int yy = 0; yy < missingColumns; yy++)
        {
            addColumn();
        }
        adjustColumns();
        for(int ii = 0; ii < values.size(); ii++)
        {   
           Object nextValue = values.get(ii);
           String nextColumnName = (String)columnNameList.get(ii);
           List nextColumnList = (List)columnMap.get(nextColumnName);
           nextColumnList.add(nextValue);
        }
        adjustColumns();
        copyColumnMap();
        adjustFlags();
    }
    
    /**
     * Adds a column to the simulated database table.
     * The column will get the name <i>ColumnX</i> where 
     * <i>X</i> is the column index. The first added column
     * will have the name <i>Column1</i>. No data will be stored
     * in the column.
     */
    public void addColumn()
    {
        addColumn(determineValidColumnName());
    }
    
    /**
     * Adds a column to the simulated database table.
     * The column will get the specified name.
     * No data will be stored in the column.
     * @param columnName the column name
     */
    public void addColumn(String columnName)
    {
        addColumn(columnName, new ArrayList());
    }
    
    /**
     * Adds a column to the simulated database table.
     * The column will get the name <i>ColumnX</i> where 
     * <i>X</i> is the column index. 
     * The specified data will be stored in the new column. If there
     * are other columns with not enough rows, the other
     * columns will be extended and filled with <code>null</code>
     * values.
     * @param values the column data as array, the array index
     *        corresponds to the row index, i.e.
     *        values[0] will be stored in the first row
     *        and so on
     */
    public void addColumn(Object[] values)
    {
        addColumn(determineValidColumnName(), values);
    }

    /**
     * Adds a column to the simulated database table.
     * The column will get the name <i>ColumnX</i> where 
     * <i>X</i> is the column index. 
     * The specified data will be stored in the new column. If there
     * are other columns with not enough rows, the other
     * columns will be extended and filled with <code>null</code>
     * values.
     * @param values the column data as <code>List</code>, the index
     *        in the <code>List</code> corresponds to the row 
     *        index, i.e. values.get(0) will be stored in the first 
     *        row and so on
     */
    public void addColumn(List values)
    {
        addColumn(determineValidColumnName(), values);
    }
    
    /**
     * Adds a column to the simulated database table.
     * The column will get the specified name.
     * The specified data will be stored in the new column. If there
     * are other columns with not enough rows, the other
     * columns will be extended and filled with <code>null</code>
     * values.
     * @param columnName the column name
     * @param values the column data as array, the array index
     *        corresponds to the row index, i.e.
     *        values[0] will be stored in the first row
     *        and so on
     */
    public void addColumn(String columnName, Object[] values)
    {
        List columnValues = ArrayUtil.getListFromObjectArray(values);
        addColumn(columnName, columnValues);
    }
    
    /**
     * Adds a column to the simulated database table.
     * The column will get the specified name.
     * The specified data will be stored in the new column. If there
     * are other columns with not enough rows, the other
     * columns will be extended and filled with <code>null</code>
     * values.
     * @param columnName the column name
     * @param values the column data as <code>List</code>, the index
     *        in the <code>List</code> corresponds to the row 
     *        index, i.e. values.get(0) will be stored in the first 
     *        row and so on
     */
    public void addColumn(String columnName, List values)
    {
        List column = new ArrayList(values);
        columnMap.put(columnName, column);
        columnNameList.add(columnName);
        adjustColumns();
        adjustInsertRow();
        copyColumnMap();
        adjustFlags();
    }
    
    /**
     * Returns the current number of rows.
     * @return the number of rows
     */
    public int getRowCount()
    {
        if(columnMapCopy.size() == 0) return 0;
        List column = (List)columnMapCopy.values().iterator().next();
        return column.size();
    }
    
    /**
     * Returns the current number of columns.
     * @return the number of columns
     */
    public int getColumnCount()
    {
        return columnMapCopy.size();
    }
    
    /**
     * Returns if the row with the specified number was inserted
     * The first row has the number 1.
     * @param number the number of the row
     * @return <code>true</code> if the row was inserted,
     *         <code>false</code> otherwise
     */
    public boolean rowInserted(int number)
    {
        if(number < 1) return false;
        return ((Boolean)insertedRows.get(number - 1)).booleanValue();
    }
    
    /**
     * Returns if the row with the specified number was deleted
     * The first row has the number 1.
     * @param number the number of the row
     * @return <code>true</code> if the row was deleted,
     *         <code>false</code> otherwise
     */
    public boolean rowDeleted(int number)
    {
        if(number < 1) return false;
        return ((Boolean)deletedRows.get(number - 1)).booleanValue();
    }
    
    /**
     * Returns if the row with the specified number was updated
     * The first row has the number 1.
     * @param number the number of the row
     * @return <code>true</code> if the row was updated,
     *         <code>false</code> otherwise
     */
    public boolean rowUpdated(int number)
    {
        if(number < 1) return false;
        return ((Boolean)updatedRows.get(number - 1)).booleanValue();
    }
    
    /**
     * Returns if the row with the specified number is
     * equal to the specified data. Uses {@link com.mockrunner.jdbc.ParameterUtil#compareParameter}.
     * The first row has the number 1. If the compared parameters are not of
     * the same type (and cannot be equal according to the 
     * {@link com.mockrunner.jdbc.ParameterUtil#compareParameter} method) they
     * will be converted to a string with the <code>toString()</code> method before
     * comparison.
     * @param number the number of the row
     * @param rowData the row data
     * @return <code>true</code> if the row is equal to the specified data,
     *         <code>false</code> otherwise
     */
    public boolean isRowEqual(int number, List rowData)
    {
        List currentRow = getRow(number);
        if(null == currentRow) return false;
        if(currentRow.size() != rowData.size()) return false;
        for(int ii = 0; ii < currentRow.size(); ii++)
        {
            Object source = currentRow.get(ii);
            Object target = rowData.get(ii);
            if(!source.getClass().isAssignableFrom(target.getClass()) && !target.getClass().isAssignableFrom(source.getClass()))
            {
                source = source.toString();
                target = target.toString();
            }
            if(!ParameterUtil.compareParameter(source, target))
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns if the column with the specified number is
     * equal to the specified data. Uses {@link com.mockrunner.jdbc.ParameterUtil#compareParameter}.
     * The first column has the number 1. If the compared parameters are not of
     * the same type (and cannot be equal according to the 
     * {@link com.mockrunner.jdbc.ParameterUtil#compareParameter} method) they
     * will be converted to a string with the <code>toString()</code> method before
     * comparison.
     * @param number the number of the column
     * @param columnData the column data
     * @return <code>true</code> if the column is equal to the specified data,
     *         <code>false</code> otherwise
     */
    public boolean isColumnEqual(int number, List columnData)
    {
        List currentColumn = getColumn(number);
        if(null == currentColumn) return false;
        if(currentColumn.size() != columnData.size()) return false;
        for(int ii = 0; ii < currentColumn.size(); ii++)
        {
            Object source = currentColumn.get(ii);
            Object target = columnData.get(ii);
            if(!source.getClass().isAssignableFrom(target.getClass()) && !target.getClass().isAssignableFrom(source.getClass()))
            {
                source = source.toString();
                target = target.toString();
            }
            if(!ParameterUtil.compareParameter(source, target))
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns if the column with the specified name is
     * equal to the specified data. Uses {@link com.mockrunner.jdbc.ParameterUtil#compareParameter}.
     * The first column has the number 1. If the compared parameters are not of
     * the same type (and cannot be equal according to the 
     * {@link com.mockrunner.jdbc.ParameterUtil#compareParameter} method) they
     * will be converted to a string with the <code>toString()</code> method before
     * comparison.
     * @param name the name of the column
     * @param columnData the column data
     * @return <code>true</code> if the column is equal to the specified data,
     *         <code>false</code> otherwise
     */
    public boolean isColumnEqual(String name, List columnData)
    {
        List currentColumn = getColumn(name);
        if(null == currentColumn) return false;
        if(currentColumn.size() != columnData.size()) return false;
        for(int ii = 0; ii < currentColumn.size(); ii++)
        {
            Object source = currentColumn.get(ii);
            Object target = columnData.get(ii);
            if(!source.getClass().isAssignableFrom(target.getClass()) && !target.getClass().isAssignableFrom(source.getClass()))
            {
                source = source.toString();
                target = target.toString();
            }
            if(!ParameterUtil.compareParameter(source, target))
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns if the specified <code>ResultSet</code> is equal to
     * this <code>ResultSet</code>. If the compared parameters are not of
     * the same type (and cannot be equal according to the 
     * {@link com.mockrunner.jdbc.ParameterUtil#compareParameter} method) they
     * will be converted to a string with the <code>toString()</code> method before
     * comparison.
     * @return <code>true</code> if the two <code>ResultSet</code> objects are equal,
     *         <code>false</code> otherwise
     */
    public boolean isEqual(MockResultSet resultSet)
    {
        if(null == resultSet) return false;
        Map thisMap;
        Map otherMap;
        if(isDatabaseView)
        {
            thisMap = columnMap;    
        }
        else
        {
            thisMap = columnMapCopy;  
        }
        if(resultSet.isDatabaseView)
        {
            otherMap = resultSet.columnMap;
        }
        else
        {
            otherMap = resultSet.columnMapCopy;
        }
        Iterator keys = thisMap.keySet().iterator();
        while(keys.hasNext())
        {
            String currentKey = (String)keys.next();
            List thisList =  (List)thisMap.get(currentKey);
            List otherList =  (List)otherMap.get(currentKey);
            if(null == otherList) return false;
            if(thisList.size() != otherList.size()) return false;
            for(int ii = 0; ii < thisList.size(); ii++)
            {
                Object source = thisList.get(ii);
                Object target = otherList.get(ii);
                if(!source.getClass().isAssignableFrom(target.getClass()) && !target.getClass().isAssignableFrom(source.getClass()))
                {
                    source = source.toString();
                    target = target.toString();
                }
                if(!ParameterUtil.compareParameter(source, target))
                {
                    return false;
                }    
            }
        }
        return true;
    }
    
    /**
     * Returns the row with the specified number.
     * The first row has the number 1.
     * If number is less than 1 or higher than the
     * current row count, <code>null</code> will
     * be returned. The result of this method depends
     * on the setting of <i>databaseView</i>. 
     * See {@link #setDatabaseView}.
     * @param number the number of the row
     * @return the row data as <code>List</code>
     */
    public List getRow(int number)
    {
        if(number > getRowCount()) return null;
        if(number < 1) return null;
        int index = number - 1;
        List list = new ArrayList();
        for(int ii = 0; ii < columnNameList.size(); ii++)
        {
            String nextColumnName = (String)columnNameList.get(ii);
            List nextColumnList;
            if(isDatabaseView)
            {
                nextColumnList = (List)columnMap.get(nextColumnName);
            }
            else
            {
                nextColumnList = (List)columnMapCopy.get(nextColumnName);
            }
            list.add(nextColumnList.get(index));
        }
        return list;
    }
    
    /**
     * Returns the column with the specified number.
     * The first column has the number 1.
     * If number is less than 1 or higher than the
     * current column count, <code>null</code> will
     * be returned.
     * @param number the number of the column
     * @return the column data as <code>List</code>
     */
    public List getColumn(int number)
    {
        if(number > getColumnCount()) return null;
        if(number < 1) return null;
        int index = number - 1;
        String columnName = (String)columnNameList.get(index);
        return getColumn(columnName);
    }
    
    /**
     * Returns the column with the specified name.
     * If a column with that name does not exist, 
     * <code>null</code> will be returned.
     * @param name the name of the column
     * @return the column data as <code>List</code>
     */
    public List getColumn(String name)
    {
        List list = new ArrayList();
        List columnList;
        if(isDatabaseView)
        {
            columnList = (List)columnMap.get(name);
        }
        else
        {
            columnList = (List)columnMapCopy.get(name);
        }
        if(null == columnList) return null;
        list.addAll(columnList);
        return list;
    }
    
    public void close() throws SQLException
    {
        closed = true;
    }

    public boolean wasNull() throws SQLException
    {
        return wasNull;
    }
    
    public Object getObject(int columnIndex) throws SQLException
    {
        checkColumnBounds(columnIndex);
        if(!isCurrentRowValid())
        {
            wasNull = true;
            return null;
        }
        String columnName = (String)columnNameList.get(columnIndex - 1);
        return getObject(columnName);
    }
    
    public Object getObject(String columnName) throws SQLException
    {
        checkColumnName(columnName);
        if(!isCurrentRowValid())
        {
            wasNull = true;
            return null;
        }
        if(rowDeleted()) throw new SQLException("row was deleted");
        List column;
        if(isDatabaseView)
        {
            column = (List)columnMap.get(columnName);
        }
        else
        {
            column = (List)columnMapCopy.get(columnName);
        }
        Object value = column.get(cursor);
        wasNull = (null == value);
        return value;
    }
    
    public Object getObject(int columnIndex, Map map) throws SQLException
    {
        return getObject(columnIndex);
    }

    public Object getObject(String colName, Map map) throws SQLException
    {
        return getObject(colName);
    }

    public String getString(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value) return value.toString();
        return null;
    }
    
    public String getString(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value) return value.toString();
        return null;
    }


    public boolean getBoolean(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Boolean) return ((Boolean)value).booleanValue();
            return new Boolean(value.toString()).booleanValue();
        }
        return false;
    }
    
    public boolean getBoolean(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Boolean) return ((Boolean)value).booleanValue();
            return new Boolean(value.toString()).booleanValue();
        }
        return false;
    }

    public byte getByte(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).byteValue();
            return new Byte(value.toString()).byteValue();
        }
        return 0;
    }
    
    public byte getByte(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).byteValue();
            return new Byte(value.toString()).byteValue();
        }
        return 0;
    }

    public short getShort(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).shortValue();
            return new Short(value.toString()).shortValue();
        }
        return 0;
    }
    
    public short getShort(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).shortValue();
            return new Short(value.toString()).shortValue();
        }
        return 0;
    }

    public int getInt(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).intValue();
            return new Integer(value.toString()).intValue();
        }
        return 0;
    }
    
    public int getInt(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).intValue();
            return new Integer(value.toString()).intValue();
        }
        return 0;
    }

    public long getLong(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).longValue();
            return new Long(value.toString()).longValue();
        }
        return 0;
    }
    
    public long getLong(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).longValue();
            return new Long(value.toString()).longValue();
        }
        return 0;
    }

    public float getFloat(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).floatValue();
            return new Float(value.toString()).floatValue();
        }
        return 0;
    }
    
    public float getFloat(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).floatValue();
            return new Float(value.toString()).floatValue();
        }
        return 0;
    }
    
    public double getDouble(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).doubleValue();
            return new Double(value.toString()).doubleValue();
        }
        return 0;
    }
    
    public double getDouble(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).doubleValue();
            return new Double(value.toString()).doubleValue();
        }
        return 0;
    }

    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException
    {
        BigDecimal value = getBigDecimal(columnIndex);
        if(null != value)
        {
            return value.setScale(scale);
        }
        return null;
    }
    
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException
    {
        BigDecimal value = getBigDecimal(columnName);
        if(null != value)
        {
            return value.setScale(scale);
        }
        return null;
    }
    
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return new BigDecimal(((Number)value).doubleValue());
            return new BigDecimal(value.toString());
        }
        return null;
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return new BigDecimal(((Number)value).doubleValue());
            return new BigDecimal(value.toString());
        }
        return null;
    }

    public byte[] getBytes(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof byte[]) return (byte[])value;
            return value.toString().getBytes();
        }
        return null;
    }
    
    public byte[] getBytes(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof byte[]) return (byte[])value;
            return value.toString().getBytes();
        }
        return null;
    }

    public Date getDate(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Date) return (Date)value;
            return Date.valueOf(value.toString());
        }
        return null;
    }
    
    public Date getDate(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Date) return (Date)value;
            return Date.valueOf(value.toString());
        }
        return null;
    }
    
    public Date getDate(int columnIndex, Calendar calendar) throws SQLException
    {
        return getDate(columnIndex);
    }

    public Date getDate(String columnName, Calendar calendar) throws SQLException
    {
        return getDate(columnName);
    }

    public Time getTime(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Time) return (Time)value;
            return Time.valueOf(value.toString());
        }
        return null;
    }
    
    public Time getTime(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Time) return (Time)value;
            return Time.valueOf(value.toString());
        }
        return null;
    }
    
    public Time getTime(int columnIndex, Calendar calendar) throws SQLException
    {
        return getTime(columnIndex);
    }

    public Time getTime(String columnName, Calendar calendar) throws SQLException
    {
        return getTime(columnName);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Timestamp) return (Timestamp)value;
            return Timestamp.valueOf(value.toString());
        }
        return null;
    }
    
    public Timestamp getTimestamp(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Timestamp) return (Timestamp)value;
            return Timestamp.valueOf(value.toString());
        }
        return null;
    }
    
    public Timestamp getTimestamp(int columnIndex, Calendar calendar) throws SQLException
    {
        return getTimestamp(columnIndex);
    }

    public Timestamp getTimestamp(String columnName, Calendar calendar) throws SQLException
    {
        return getTimestamp(columnName);
    }
    
    public URL getURL(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof URL) return (URL)value;
            try
            {
                return new URL(value.toString());
            }
            catch(MalformedURLException exc)
            {
            
            }
        }
        return null;
    }

    public URL getURL(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof URL) return (URL)value;
            try
            {
                return new URL(value.toString());
            }
            catch(MalformedURLException exc)
            {
            
            }
        }
        return null;
    }
    
    public Blob getBlob(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Blob) return (Blob)value;
            return new MockBlob(getBytes(columnIndex));
        }
        return null;
    }
    
    public Blob getBlob(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Blob) return (Blob)value;
            return new MockBlob(getBytes(columnName));
        }
        return null;
    }

    public Clob getClob(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Clob) return (Clob)value;
            return new MockClob(getString(columnIndex));
        }
        return null;
    }
    
    public Clob getClob(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Clob) return (Clob)value;
            return new MockClob(getString(columnName));
        }
        return null;
    }
    
    public Array getArray(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Array) return (Array)value;
            return new MockArray(value);
        }
        return null;
    }
    
    public Array getArray(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Array) return (Array)value;
            return new MockArray(value);
        }
        return null;
    }
    
    public Ref getRef(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Ref) return (Ref)value;
            return new MockRef(value);
        }
        return null;
    }

    public Ref getRef(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Ref) return (Ref)value;
            return new MockRef(value);
        }
        return null;
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException
    {
        return getBinaryStream(columnIndex);
    }
    
    public InputStream getAsciiStream(String columnName) throws SQLException
    {
        return getBinaryStream(columnName);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof InputStream) return (InputStream)value;
            return new ByteArrayInputStream(getBytes(columnIndex));
        }
        return null;
    }

    public InputStream getBinaryStream(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof InputStream) return (InputStream)value;
            return new ByteArrayInputStream(getBytes(columnName));
        }
        return null;
    }
    
    public InputStream getUnicodeStream(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof InputStream) return (InputStream)value;
            try
            {
                return new ByteArrayInputStream(getString(columnIndex).getBytes("UTF-8"));
            }
            catch(UnsupportedEncodingException exc)
            {
            
            }
        }
        return null;
    }

    public InputStream getUnicodeStream(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof InputStream) return (InputStream)value;
            try
            {
                return new ByteArrayInputStream(getString(columnName).getBytes("UTF-8"));
            }
            catch(UnsupportedEncodingException exc)
            {
            
            }
        }
        return null;
    }
    
    public Reader getCharacterStream(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Reader) return (Reader)value;
            return new StringReader(getString(columnIndex));
        }
        return null;
    }

    public Reader getCharacterStream(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Reader) return (Reader)value;
            return new StringReader(getString(columnName));
        }
        return null;
    }


    public SQLWarning getWarnings() throws SQLException
    {
        return null;
    }

    public void clearWarnings() throws SQLException
    {

    }

    public String getCursorName() throws SQLException
    {
        return cursorName;
    }

    public ResultSetMetaData getMetaData() throws SQLException
    {
        if(null != resultSetMetaData) return resultSetMetaData;
        MockResultSetMetaData metaData = new MockResultSetMetaData();
        metaData.setColumnCount(getColumnCount());
        for(int ii = 0; ii < columnNameList.size(); ii++)
        {
            metaData.setColumnName(ii + 1, (String)columnNameList.get(ii));
        }
        return metaData;
    }
    
    public Statement getStatement() throws SQLException
    {
        return statement;
    }

    public boolean isBeforeFirst() throws SQLException
    {
        // Counterintuitively, this method is supposed to return false when the
        // result set is empty.
        return (getRowCount() != 0) && (cursor == -1);
    }

    public boolean isAfterLast() throws SQLException
    {    
        return cursor >= getRowCount();
    }

    public boolean isFirst() throws SQLException
    {
        return cursor == 0;
    }

    public boolean isLast() throws SQLException
    {
        return (cursor != -1) && (cursor == getRowCount() - 1);
    }

    public void beforeFirst() throws SQLException
    {
        if(isCursorInInsertRow) throw new SQLException("cursor is in insert row");
        checkResultSetType();
        cursor = -1;
    }

    public void afterLast() throws SQLException
    {
        if(isCursorInInsertRow) throw new SQLException("cursor is in insert row");
        checkResultSetType();
        if(getRowCount() == 0) return;
        cursor = getRowCount();
    }
    
    public boolean next() throws SQLException
    {
        if(isCursorInInsertRow) throw new SQLException("cursor is in insert row");
        if(getRowCount() == 0) return false;
        cursor++;
        adjustCursor();
        return isCurrentRowValid();
    }


    public boolean first() throws SQLException
    {
        if(isCursorInInsertRow) throw new SQLException("cursor is in insert row");
        checkResultSetType();
        if(getRowCount() == 0) return false;
        cursor = 0;
        return true;
    }

    public boolean last() throws SQLException
    {
        if(isCursorInInsertRow) throw new SQLException("cursor is in insert row");
        checkResultSetType();
        if(getRowCount() == 0) return false;
        cursor = getRowCount() - 1;
        return true;
    }
    
    public boolean absolute(int row) throws SQLException
    {
        if(isCursorInInsertRow) throw new SQLException("cursor is in insert row");
        checkResultSetType();
        if(getRowCount() == 0) return false;
        if(row > 0) cursor = row - 1;
        if(row < 0) cursor = getRowCount() + row;
        adjustCursor();
        return isCurrentRowValid();
    }

    public boolean relative(int rows) throws SQLException
    {
        if(isCursorInInsertRow) throw new SQLException("cursor is in insert row");
        checkResultSetType();
        if(getRowCount() == 0) return false;
        cursor += rows;
        adjustCursor();
        return isCurrentRowValid();
    }

    public int getRow() throws SQLException
    {
        return cursor + 1;
    }

    public boolean previous() throws SQLException
    {
        if(isCursorInInsertRow) throw new SQLException("cursor is in insert row");
        checkResultSetType();
        if(getRowCount() == 0) return false;
        cursor--;
        adjustCursor();
        return isCurrentRowValid();
    }
    
    public void setFetchDirection(int fetchDirection) throws SQLException
    {
        checkFetchDirectionArguments(fetchDirection);
        if(this.fetchDirection == fetchDirection) return;
        if(this.fetchDirection == ResultSet.FETCH_UNKNOWN || fetchDirection == ResultSet.FETCH_UNKNOWN)
        {
            this.fetchDirection = fetchDirection;
            return;
        }
        this.fetchDirection = fetchDirection;
        Iterator columns = columnMapCopy.values().iterator();
        while(columns.hasNext())
        {
            List column = (List)columns.next();
            Collections.reverse(column);
        }
        if(-1 != cursor) cursor = getRowCount() - cursor - 1;
    }

    public int getFetchDirection() throws SQLException
    {
        return fetchDirection;
    }

    public void setFetchSize(int fetchSize) throws SQLException
    {
        this.fetchSize = fetchSize;
    }

    public int getFetchSize() throws SQLException
    {
        return fetchSize;
    }

    public int getType() throws SQLException
    {
        return resultSetType;
    }

    public int getConcurrency() throws SQLException
    {
        return resultSetConcurrency;
    }
    
    public int findColumn(String columnName) throws SQLException
    {
        for(int ii = 0; ii < columnNameList.size(); ii++)
        {
            if(columnName.equals(columnNameList.get(ii))) return ii + 1;
        }
        throw new SQLException("No column with name " + columnName + " found");
    }

    public void updateObject(int columnIndex, Object value) throws SQLException
    {
        checkColumnBounds(columnIndex);
        checkRowBounds();
        if(rowDeleted()) throw new SQLException("row was deleted");
        String columnName = (String)columnNameList.get(columnIndex - 1);
        updateObject(columnName, value);
    }
    
    public void updateObject(int columnIndex, Object value, int scale) throws SQLException
    {
        updateObject(columnIndex, value);
    }
    
    public void updateObject(String columnName, Object value, int scale) throws SQLException
    {
        updateObject(columnName, value);
    }

    public void updateObject(String columnName, Object value) throws SQLException
    {
        checkColumnName(columnName);
        checkRowBounds();
        checkResultSetConcurrency();
        if(rowDeleted()) throw new SQLException("row was deleted");
        if(isCursorInInsertRow)
        {
            List column = (List)insertRow.get(columnName);
            column.set(0, value);
        }
        else
        {
            List column = (List)columnMapCopy.get(columnName);
            column.set(cursor, value);
        }
    }
    
    public void updateString(int columnIndex, String value) throws SQLException
    {
        updateObject(columnIndex, value);
    }

    public void updateString(String columnName, String value) throws SQLException
    {
        updateObject(columnName, value);
    }

    public void updateNull(int columnIndex) throws SQLException
    {
        updateObject(columnIndex, null);
    }
    
    public void updateNull(String columnName) throws SQLException
    {
        updateObject(columnName, null);
    }

    public void updateBoolean(int columnIndex, boolean booleanValue) throws SQLException
    {
        updateObject(columnIndex, new Boolean(booleanValue));
    }
    
    public void updateBoolean(String columnName, boolean booleanValue) throws SQLException
    {
        updateObject(columnName, new Boolean(booleanValue));
    }

    public void updateByte(int columnIndex, byte byteValue) throws SQLException
    {
        updateObject(columnIndex, new Byte(byteValue));
    }
    
    public void updateByte(String columnName, byte byteValue) throws SQLException
    {
        updateObject(columnName, new Byte(byteValue));
    }

    public void updateShort(int columnIndex, short shortValue) throws SQLException
    {
        updateObject(columnIndex, new Short(shortValue));
    }
    
    public void updateShort(String columnName, short shortValue) throws SQLException
    {
        updateObject(columnName, new Short(shortValue));
    }

    public void updateInt(int columnIndex, int intValue) throws SQLException
    {
        updateObject(columnIndex, new Integer(intValue));
    }
    
    public void updateInt(String columnName, int intValue) throws SQLException
    {
        updateObject(columnName, new Integer(intValue));
    }
    
    public void updateLong(int columnIndex, long longValue) throws SQLException
    {
        updateObject(columnIndex, new Long(longValue));
    }
    
    public void updateLong(String columnName, long longValue) throws SQLException
    {
        updateObject(columnName, new Long(longValue));
    }

    public void updateFloat(int columnIndex, float floatValue) throws SQLException
    {
        updateObject(columnIndex, new Float(floatValue));
    }
    
    public void updateFloat(String columnName, float floatValue) throws SQLException
    {
        updateObject(columnName, new Float(floatValue));
    }

    public void updateDouble(int columnIndex, double doubleValue) throws SQLException
    {
        updateObject(columnIndex, new Double(doubleValue));
    }
    
    public void updateDouble(String columnName, double doubleValue) throws SQLException
    {
        updateObject(columnName, new Double(doubleValue));
    }
      
    public void updateBigDecimal(int columnIndex, BigDecimal bigDecimal) throws SQLException
    {
        updateObject(columnIndex, bigDecimal);
    }
    
    public void updateBigDecimal(String columnName, BigDecimal bigDecimal) throws SQLException
    {
        updateObject(columnName, bigDecimal);
    }

    public void updateBytes(int columnIndex, byte[] byteArray) throws SQLException
    {
        updateObject(columnIndex, byteArray);
    }
    
    public void updateBytes(String columnName, byte[] byteArray) throws SQLException
    {
        updateObject(columnName, byteArray);
    }
    
    public void updateDate(int columnIndex, Date date) throws SQLException
    {
        updateObject(columnIndex, date);
    }

    public void updateDate(String columnName, Date date) throws SQLException
    {
        updateObject(columnName, date);
    }
    
    public void updateTime(int columnIndex, Time time) throws SQLException
    {
        updateObject(columnIndex, time);
    }

    public void updateTime(String columnName, Time time) throws SQLException
    {
        updateObject(columnName, time);
    }
    
    public void updateTimestamp(int columnIndex, Timestamp timeStamp) throws SQLException
    {
        updateObject(columnIndex, timeStamp);
    }

    public void updateTimestamp(String columnName, Timestamp timeStamp) throws SQLException
    {
        updateObject(columnName, timeStamp);
    }

    public void updateAsciiStream(int columnIndex, InputStream stream, int length) throws SQLException
    {
        updateBinaryStream(columnIndex, stream, length);
    }
    
    public void updateAsciiStream(String columnName, InputStream stream, int length) throws SQLException
    {
        updateBinaryStream(columnName, stream, length);
    }

    public void updateBinaryStream(int columnIndex, InputStream stream, int length) throws SQLException
    {
        byte[] data = StreamUtil.getStreamAsByteArray(stream, length);
        updateObject(columnIndex, new ByteArrayInputStream(data));
    }
    
    public void updateBinaryStream(String columnName, InputStream stream, int length) throws SQLException
    {
        byte[] data = StreamUtil.getStreamAsByteArray(stream, length);
        updateObject(columnName, new ByteArrayInputStream(data));
    }

    public void updateCharacterStream(int columnIndex, Reader reader, int length) throws SQLException
    {
        String data = StreamUtil.getReaderAsString(reader, length);
        updateObject(columnIndex, new StringReader(data));
    }

    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException
    {
        String data = StreamUtil.getReaderAsString(reader, length);
        updateObject(columnName, new StringReader(data));
    }
    
    public void updateRef(int columnIndex, Ref ref) throws SQLException
    {
        updateObject(columnIndex, ref);
    }

    public void updateRef(String columnName, Ref ref) throws SQLException
    {
        updateObject(columnName, ref);
    }

    public void updateBlob(int columnIndex, Blob blob) throws SQLException
    {
        updateObject(columnIndex, blob);
    }

    public void updateBlob(String columnName, Blob blob) throws SQLException
    {
        updateObject(columnName, blob);
    }

    public void updateClob(int columnIndex, Clob clob) throws SQLException
    {
        updateObject(columnIndex, clob);
    }

    public void updateClob(String columnName, Clob clob) throws SQLException
    {
        updateObject(columnName, clob);
    }

    public void updateArray(int columnIndex, Array array) throws SQLException
    {
        updateObject(columnIndex, array);
    }

    public void updateArray(String columnName, Array array) throws SQLException
    {
        updateObject(columnName, array);
    }
    
    public boolean rowUpdated() throws SQLException
    {
        checkRowBounds();
        return ((Boolean)updatedRows.get(cursor)).booleanValue();
    }

    public boolean rowInserted() throws SQLException
    {
        checkRowBounds();
        return ((Boolean)insertedRows.get(cursor)).booleanValue();
    }

    public boolean rowDeleted() throws SQLException
    {
        checkRowBounds();
        return ((Boolean)deletedRows.get(cursor)).booleanValue();
    }
    
    public void insertRow() throws SQLException
    {
        if(!isCursorInInsertRow) throw new SQLException("cursor is in insert row");
        insertRow(cursor);
    }

    public void updateRow() throws SQLException
    {
        if(isCursorInInsertRow) throw new SQLException("cursor is in insert row");
        if(rowDeleted()) throw new SQLException("row was deleted");
        checkRowBounds();
        updateRow(cursor, true);
        updatedRows.set(cursor, new Boolean(true));
    }

    public void deleteRow() throws SQLException
    {
        if(isCursorInInsertRow) throw new SQLException("cursor is in insert row");
        checkRowBounds();
        deleteRow(cursor);
        deletedRows.set(cursor, new Boolean(true));
    }

    public void refreshRow() throws SQLException
    {
        cancelRowUpdates();
    }

    public void cancelRowUpdates() throws SQLException
    {
        if(isCursorInInsertRow) throw new SQLException("cursor is in insert row");
        if(rowDeleted()) throw new SQLException("row was deleted");
        checkRowBounds();
        updateRow(cursor, false);
        updatedRows.set(cursor, new Boolean(false));
    }

    public void moveToInsertRow() throws SQLException
    {
        isCursorInInsertRow = true;
    }

    public void moveToCurrentRow() throws SQLException
    {
        isCursorInInsertRow = false;
    }
    
    private void checkColumnName(String columnName) throws SQLException
    {
        if(!columnMap.containsKey(columnName))
        {
            throw new SQLException("No column " + columnName);
        }
    }
    
    private void checkColumnBounds(int columnIndex) throws SQLException
    {
        if(!(columnIndex - 1 < columnNameList.size()))
        {
            throw new SQLException("Index " + columnIndex + " out of bounds");
        }
    }
    
    private void checkRowBounds() throws SQLException
    {
        if(!isCurrentRowValid())
        {
            throw new SQLException("Current row invalid");
        }
    }
    
    private boolean isCurrentRowValid()
    {
        return (cursor < getRowCount()) && (-1 != cursor);
    }
    
    private void checkResultSetType() throws SQLException
    {
        if(resultSetType == ResultSet.TYPE_FORWARD_ONLY)
        {
            throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
        }
    }
    
    private void checkResultSetConcurrency() throws SQLException
    {
        if(resultSetConcurrency == ResultSet.CONCUR_READ_ONLY)
        {
            throw new SQLException("ResultSet is CONCUR_READ_ONLY");
        }
    }
    
    private void checkFetchDirectionArguments(int fetchDirection) throws SQLException
    {
        SQLUtil.checkFetchDirection(fetchDirection);
        if(resultSetType == ResultSet.TYPE_FORWARD_ONLY && fetchDirection != ResultSet.FETCH_FORWARD)
        {
            throw new SQLException("resultSetType is TYPE_FORWARD_ONLY, only FETCH_FORWARD allowed");
        }
    }
    
    private void insertRow(int index)
    {
        Iterator columnNames = columnMapCopy.keySet().iterator();
        while(columnNames.hasNext())
        {
            String currentColumnName = (String)columnNames.next();
            List copyColumn = (List)columnMapCopy.get(currentColumnName);
            List databaseColumn = (List)columnMap.get(currentColumnName);
            List sourceColumn = (List)insertRow.get(currentColumnName);
            copyColumn.add(index, sourceColumn.get(0));
            databaseColumn.add(index, sourceColumn.get(0));  
        }
        updatedRows.add(index, new Boolean(false));
        deletedRows.add(index, new Boolean(false));
        insertedRows.add(index, new Boolean(true));
    }
    
    private void deleteRow(int index)
    {
        Iterator columnNames = columnMapCopy.keySet().iterator();
        while(columnNames.hasNext())
        {
            String currentColumnName = (String)columnNames.next();
            List copyColumn = (List)columnMapCopy.get(currentColumnName);
            List databaseColumn = (List)columnMap.get(currentColumnName);
            copyColumn.set(index, null);
            databaseColumn.set(index, null);
        }
    }
    
    private void updateRow(int index, boolean toDatabase)
    {
        Iterator columnNames = columnMapCopy.keySet().iterator();
        while(columnNames.hasNext())
        {
            String currentColumnName = (String)columnNames.next();
            List sourceColumn;
            List targetColumn;
            if(toDatabase)
            {
                sourceColumn = (List)columnMapCopy.get(currentColumnName);
                targetColumn = (List)columnMap.get(currentColumnName);
            }
            else
            {
                sourceColumn = (List)columnMap.get(currentColumnName);
                targetColumn = (List)columnMapCopy.get(currentColumnName);
            } 
            targetColumn.set(index, sourceColumn.get(index));
        }
    }
    
    private void adjustCursor()
    {
        if(cursor < 0) cursor = -1;
        if(cursor >= getRowCount()) cursor = getRowCount();
    }
    
    private void adjustColumns()
    {
        int rowCount = 0;
        Iterator columns = columnMap.values().iterator();
        while(columns.hasNext())
        {
            List nextColumn = (List)columns.next();
            rowCount = Math.max(rowCount, nextColumn.size());
        }
        columns = columnMap.values().iterator();
        while(columns.hasNext())
        {
            List nextColumn = (List)columns.next();
            CollectionUtil.fillList(nextColumn, rowCount);
        }
    }
    
    private void adjustFlags()
    {
        for(int ii = updatedRows.size(); ii < getRowCount(); ii++)
        {
            updatedRows.add(new Boolean(false));
        }
        for(int ii = deletedRows.size(); ii < getRowCount(); ii++)
        {
            deletedRows.add(new Boolean(false));
        }
        for(int ii = insertedRows.size(); ii < getRowCount(); ii++)
        {
            insertedRows.add(new Boolean(false));
        }
    }
    
    private void adjustInsertRow()
    {
        insertRow = createCaseAwareMap();
        Iterator columns = columnMap.keySet().iterator();
        while(columns.hasNext())
        {
            ArrayList list = new ArrayList(1);
            list.add(null);
            insertRow.put((String)columns.next(), list);
        }
    }
    
    private void copyColumnMap()
    {
        columnMapCopy = copyColumnDataMap(columnMap);
    }
    
    private String determineValidColumnName()
    {
        String name = "Column";
        int count = columnNameList.size() + 1;
        while(columnMap.containsKey(name + count))
        {
            count ++;
        }
        return name + count;
    }
    
    private Map copyColumnDataMap(Map columnMap)
    {
        Map copy = createCaseAwareMap();
        Iterator columns = columnMap.keySet().iterator();
        while(columns.hasNext())
        {
            List copyList = new ArrayList();
            String nextKey = (String)columns.next();
            List nextColumnList = (List)columnMap.get(nextKey);
            for(int ii = 0; ii < nextColumnList.size(); ii++)
            {
                Object copyParameter = ParameterUtil.copyParameter(nextColumnList.get(ii));
                copyList.add(copyParameter);
            }
            copy.put(nextKey, copyList);
        }
        return copy;
    }
    
    private Map createCaseAwareMap()
    {
        return new CaseAwareMap(columnsCaseSensitive);
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer("ResultSet " + id + ":\n");
        buffer.append("Number of rows: " + getRowCount() + "\n");
        buffer.append("Number of columns: " + getColumnCount() + "\n");
        buffer.append("Column names:\n");
        StringUtil.appendObjectsAsString(buffer, columnNameList);
        buffer.append("Data:\n");
        for(int ii = 1; ii <= getRowCount(); ii++)
        {
            buffer.append("Row number " + ii + ":\n");
            StringUtil.appendObjectsAsString(buffer, getRow(ii));
        }
        return buffer.toString();
    }
}
