package com.mockrunner.mock.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.mockrunner.base.NestedApplicationException;

/**
 * Mock implementation of <code>ResultSetMetaData</code>.
 */
public class MockResultSetMetaData implements ResultSetMetaData, Cloneable
{
    private int columnCount;
    private Map<Integer, Integer> columnDisplaySizeMap;
    private Map<Integer, Integer> columnTypeMap;
    private Map<Integer, Integer> precisionMap;
    private Map<Integer, Integer> scaleMap;
    private Map<Integer, Integer> isNullableMap;
    private Map<Integer, Boolean> isAutoIncrementMap;
    private Map<Integer, Boolean> isCaseSensitiveMap;
    private Map<Integer, Boolean> isCurrencyMap;
    private Map<Integer, Boolean> isDefinitelyWritableMap;
    private Map<Integer, Boolean> isReadOnlyMap;
    private Map<Integer, Boolean> isSearchableMap;
    private Map<Integer, Boolean> isSignedMap;
    private Map<Integer, Boolean> isWritableMap;
    private Map<Integer, String> catalogNameMap;
    private Map<Integer, String> columnClassNameMap;
    private Map<Integer, String> columnLabelMap;
    private Map<Integer, String> columnNameMap;
    private Map<Integer, String> columnTypeNameMap;
    private Map<Integer, String> schemaNameMap;
    private Map<Integer, String> tableNameMap;
    
    public MockResultSetMetaData()
    {
        columnCount = 0;
        columnDisplaySizeMap = new HashMap<Integer, Integer>();
        columnTypeMap = new HashMap<Integer, Integer>();
        precisionMap = new HashMap<Integer, Integer>();
        scaleMap = new HashMap<Integer, Integer>();
        isNullableMap = new HashMap<Integer, Integer>();
        isAutoIncrementMap = new HashMap<Integer, Boolean>();
        isCaseSensitiveMap = new HashMap<Integer, Boolean>();
        isCurrencyMap = new HashMap<Integer, Boolean>();
        isDefinitelyWritableMap = new HashMap<Integer, Boolean>();
        isReadOnlyMap = new HashMap<Integer, Boolean>();
        isSearchableMap = new HashMap<Integer, Boolean>();
        isSignedMap = new HashMap<Integer, Boolean>();
        isWritableMap = new HashMap<Integer, Boolean>();
        catalogNameMap = new HashMap<Integer, String>();
        columnClassNameMap = new HashMap<Integer, String>();
        columnLabelMap = new HashMap<Integer, String>();
        columnNameMap = new HashMap<Integer, String>();
        columnTypeNameMap = new HashMap<Integer, String>();
        schemaNameMap = new HashMap<Integer, String>();
        tableNameMap = new HashMap<Integer, String>();
    }
    
    public void setColumnCount(int columnCount)
    {
        this.columnCount = columnCount;
    }
    
    public void setColumnDisplaySize(int column, int displaySize)
    {
        columnDisplaySizeMap.put(column, displaySize);
    }
    
    public void setColumnType(int column, int columnType)
    {
        columnTypeMap.put(column, columnType);
    }
    
    public void setPrecision(int column, int precision)
    {
        precisionMap.put(column, precision);
    }
    
    public void setScale(int column, int scale)
    {
        scaleMap.put(column, scale);
    }
    
    public void setNullable(int column, int nullable)
    {
        isNullableMap.put(column, nullable);
    }
    
    public void setAutoIncrement(int column, boolean autoIncrement)
    {
        isAutoIncrementMap.put(column, autoIncrement);
    }
    
    public void setCaseSensitive(int column, boolean caseSensitive)
    {
        isCaseSensitiveMap.put(column, caseSensitive);
    }
    
    public void setCurrency(int column, boolean currency)
    {
        isCurrencyMap.put(column, currency);
    }
    
    public void setDefinitelyWritable(int column, boolean definitelyWritable)
    {
        isDefinitelyWritableMap.put(column, definitelyWritable);
    }
    
    public void setReadOnly(int column, boolean readOnly)
    {
        isReadOnlyMap.put(column, readOnly);
    }
    
    public void setSearchable(int column, boolean searchable)
    {
        isSearchableMap.put(column, searchable);
    }
    
    public void setSigned(int column, boolean signed)
    {
        isSignedMap.put(column, signed);
    }
    
    public void setWritable(int column, boolean writable)
    {
        isWritableMap.put(column, writable);
    }
    
    public void setCatalogName(int column, String catalogName)
    {
        catalogNameMap.put(column, catalogName);
    }
    
    public void setColumnClassName(int column, String columnClassName)
    {
        columnClassNameMap.put(column, columnClassName);
    }
    
    public void setColumnLabel(int column, String columnLabel)
    {
        columnLabelMap.put(column, columnLabel);
    }
    
    public void setColumnName(int column, String columnName)
    {
        columnNameMap.put(column, columnName);
    }
    
    public void setColumnTypeName(int column, String columnTypeName)
    {
        columnTypeNameMap.put(column, columnTypeName);
    }
    
    public void setSchemaName(int column, String schemaName)
    {
        schemaNameMap.put(column, schemaName);
    }
    
    public void setTableName(int column, String tableName)
    {
        tableNameMap.put(column, tableName);
    }
    
    public int getColumnCount() throws SQLException
    {
        return columnCount;
    }

    public int getColumnDisplaySize(int column) throws SQLException
    {
        Integer columnDisplaySize = columnDisplaySizeMap.get(column);
        if(null == columnDisplaySize) return getColumnCount();
        return columnDisplaySize;
    }

    public int getColumnType(int column) throws SQLException
    {
        Integer columnType = columnTypeMap.get(column);
        if(null == columnType) return Types.OTHER;
        return columnType;
    }

    public int getPrecision(int column) throws SQLException
    {
        Integer precision = precisionMap.get(column);
        if(null == precision) return 0;
        return precision;
    }

    public int getScale(int column) throws SQLException
    {
        Integer scale = scaleMap.get(column);
        if(null == scale) return 0;
        return scale;
    }

    public int isNullable(int column) throws SQLException
    {
        Integer isNullable = isNullableMap.get(column);
        if(null == isNullable) return columnNullable;
        return isNullable;
    }

    public boolean isAutoIncrement(int column) throws SQLException
    {
        Boolean isAutoIncrement = isAutoIncrementMap.get(column);
        if(null == isAutoIncrement) return false;
        return isAutoIncrement;
    }

    public boolean isCaseSensitive(int column) throws SQLException
    {
        Boolean isCaseSensitive = isCaseSensitiveMap.get(column);
        if(null == isCaseSensitive) return true;
        return isCaseSensitive;
    }

    public boolean isCurrency(int column) throws SQLException
    {
        Boolean isCurrency = isCurrencyMap.get(column);
        if(null == isCurrency) return false;
        return isCurrency;
    }

    public boolean isDefinitelyWritable(int column) throws SQLException
    {
        Boolean isDefinitelyWritable = isDefinitelyWritableMap.get(column);
        if(null == isDefinitelyWritable) return true;
        return isDefinitelyWritable;
    }

    public boolean isReadOnly(int column) throws SQLException
    {
        Boolean isReadOnly = isReadOnlyMap.get(column);
        if(null == isReadOnly) return false;
        return isReadOnly;
    }

    public boolean isSearchable(int column) throws SQLException
    {
        Boolean isSearchable = isSearchableMap.get(column);
        if(null == isSearchable) return true;
        return isSearchable;
    }

    public boolean isSigned(int column) throws SQLException
    {
        Boolean isSigned = isSignedMap.get(column);
        if(null == isSigned) return false;
        return isSigned;
    }

    public boolean isWritable(int column) throws SQLException
    {
        Boolean isWritable = isWritableMap.get(column);
        if(null == isWritable) return true;
        return isWritable;
    }

    public String getCatalogName(int column) throws SQLException
    {
        String catalogName = catalogNameMap.get(column);
        if(null == catalogName) return "";
        return catalogName;
    }

    public String getColumnClassName(int column) throws SQLException
    {
        String columnClassName = columnClassNameMap.get(column);
        if(null == columnClassName) return Object.class.getName();
        return columnClassName;
    }

    public String getColumnLabel(int column) throws SQLException
    {
        String columnLabel = columnLabelMap.get(column);
        if(null == columnLabel) return getColumnName(column);
        return columnLabel;
    }

    public String getColumnName(int column) throws SQLException
    {
        String columnName = columnNameMap.get(column);
        if(null == columnName) return "";
        return columnName;
    }

    public String getColumnTypeName(int column) throws SQLException
    {
        String columnTypeName = columnTypeNameMap.get(column);
        if(null == columnTypeName) return "";
        return columnTypeName;
    }

    public String getSchemaName(int column) throws SQLException
    {
        String schemaName = schemaNameMap.get(column);
        if(null == schemaName) return "";
        return schemaName;
    }

    public String getTableName(int column) throws SQLException
    {
        String tableName = tableNameMap.get(column);
        if(null == tableName) return "";
        return tableName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        try
        {       
            MockResultSetMetaData copy = (MockResultSetMetaData)super.clone();
            copy.columnDisplaySizeMap = new HashMap<Integer, Integer>(columnDisplaySizeMap);
            copy.columnTypeMap = new HashMap<Integer, Integer>(columnTypeMap);
            copy.precisionMap = new HashMap<Integer, Integer>(precisionMap);
            copy.scaleMap = new HashMap<Integer, Integer>(scaleMap);
            copy.isNullableMap = new HashMap<Integer, Integer>(isNullableMap);
            copy.isAutoIncrementMap = new HashMap<Integer, Boolean>(isAutoIncrementMap);
            copy.isCurrencyMap = new HashMap<Integer, Boolean>(isCurrencyMap);
            copy.isDefinitelyWritableMap = new HashMap<Integer, Boolean>(isDefinitelyWritableMap);
            copy.isReadOnlyMap = new HashMap<Integer, Boolean>(isReadOnlyMap);
            copy.isSearchableMap = new HashMap<Integer, Boolean>(isSearchableMap);
            copy.isSignedMap = new HashMap<Integer, Boolean>(isSignedMap);
            copy.isWritableMap = new HashMap<Integer, Boolean>(isWritableMap);
            copy.catalogNameMap = new HashMap<Integer, String>(catalogNameMap);
            copy.columnClassNameMap = new HashMap<Integer, String>(columnClassNameMap);
            copy.columnLabelMap = new HashMap<Integer, String>(columnLabelMap);
            copy.columnNameMap = new HashMap<Integer, String>(columnNameMap);
            copy.columnTypeNameMap = new HashMap<Integer, String>(columnTypeNameMap);
            copy.schemaNameMap = new HashMap<Integer, String>(schemaNameMap);
            copy.tableNameMap = new HashMap<Integer, String>(tableNameMap);
            return copy;
        }
        catch(CloneNotSupportedException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
        return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException
    {
        throw new SQLException("No object found for " + iface);
    }
}
