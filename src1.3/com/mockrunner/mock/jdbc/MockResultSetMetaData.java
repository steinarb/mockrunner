package com.mockrunner.mock.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * Mock implementation of <code>ResultSetMetaData</code>.
 */
public class MockResultSetMetaData implements ResultSetMetaData
{
    private int columnCount;
    private Map columnDisplaySizeMap;
    private Map columnTypeMap;
    private Map precisionMap;
    private Map scaleMap;
    private Map isNullableMap;
    private Map isAutoIncrementMap;
    private Map isCaseSensitiveMap;
    private Map isCurrencyMap;
    private Map isDefinitelyWritableMap;
    private Map isReadOnlyMap;
    private Map isSearchableMap;
    private Map isSignedMap;
    private Map isWritableMap;
    private Map catalogNameMap;
    private Map columnClassNameMap;
    private Map columnLabelMap;
    private Map columnNameMap;
    private Map columnTypeNameMap;
    private Map schemaNameMap;
    private Map tableNameMap;
    
    public MockResultSetMetaData()
    {
        columnCount = 0;
        columnDisplaySizeMap = new HashMap();
        columnTypeMap = new HashMap();
        precisionMap = new HashMap();
        scaleMap = new HashMap();
        isNullableMap = new HashMap();
        isAutoIncrementMap = new HashMap();
        isCaseSensitiveMap = new HashMap();
        isCurrencyMap = new HashMap();
        isDefinitelyWritableMap = new HashMap();;
        isReadOnlyMap = new HashMap();
        isSearchableMap = new HashMap();
        isSignedMap = new HashMap();
        isWritableMap = new HashMap();
        catalogNameMap = new HashMap();
        columnClassNameMap = new HashMap();
        columnLabelMap = new HashMap();
        columnNameMap = new HashMap();
        columnTypeNameMap = new HashMap();
        schemaNameMap = new HashMap();
        tableNameMap = new HashMap();
    }
    
    public void setColumnCount(int columnCount)
    {
        this.columnCount = columnCount;
    }
    
    public void setColumnDisplaySize(int column, int displaySize)
    {
        columnDisplaySizeMap.put(new Integer(column), new Integer(displaySize));
    }
    
    public void setColumnType(int column, int columnType)
    {
        columnTypeMap.put(new Integer(column), new Integer(columnType));
    }
    
    public void setPrecision(int column, int precision)
    {
        precisionMap.put(new Integer(column), new Integer(precision));
    }
    
    public void setScale(int column, int scale)
    {
        scaleMap.put(new Integer(column), new Integer(scale));
    }
    
    public void setNullable(int column, int nullable)
    {
        isNullableMap.put(new Integer(column), new Integer(nullable));
    }
    
    public void setAutoIncrement(int column, boolean autoIncrement)
    {
        isAutoIncrementMap.put(new Integer(column), new Boolean(autoIncrement));
    }
    
    public void setCaseSensitive(int column, boolean caseSensitive)
    {
        isCaseSensitiveMap.put(new Integer(column), new Boolean(caseSensitive));
    }
    
    public void setCurrency(int column, boolean currency)
    {
        isCurrencyMap.put(new Integer(column), new Boolean(currency));
    }
    
    public void setDefinitelyWritable(int column, boolean definitelyWritable)
    {
        isDefinitelyWritableMap.put(new Integer(column), new Boolean(definitelyWritable));
    }
    
    public void setReadOnly(int column, boolean readOnly)
    {
        isReadOnlyMap.put(new Integer(column), new Boolean(readOnly));
    }
    
    public void setSearchable(int column, boolean searchable)
    {
        isSearchableMap.put(new Integer(column), new Boolean(searchable));
    }
    
    public void setSigned(int column, boolean signed)
    {
        isSignedMap.put(new Integer(column), new Boolean(signed));
    }
    
    public void setWritable(int column, boolean writable)
    {
        isWritableMap.put(new Integer(column), new Boolean(writable));
    }
    
    public void setCatalogName(int column, String catalogName)
    {
        catalogNameMap.put(new Integer(column), catalogName);
    }
    
    public void setColumnClassName(int column, String columnClassName)
    {
        columnClassNameMap.put(new Integer(column), columnClassName);
    }
    
    public void setColumnLabel(int column, String columnLabel)
    {
        columnLabelMap.put(new Integer(column), columnLabel);
    }
    
    public void setColumnName(int column, String columnName)
    {
        columnNameMap.put(new Integer(column), columnName);
    }
    
    public void setColumnTypeName(int column, String columnTypeName)
    {
        columnTypeNameMap.put(new Integer(column), columnTypeName);
    }
    
    public void setSchemaName(int column, String schemaName)
    {
        schemaNameMap.put(new Integer(column), schemaName);
    }
    
    public void setTableName(int column, String tableName)
    {
        tableNameMap.put(new Integer(column), tableName);
    }
    
    public int getColumnCount() throws SQLException
    {
        return columnCount;
    }

    public int getColumnDisplaySize(int column) throws SQLException
    {
        Integer columnDisplaySize = (Integer)columnDisplaySizeMap.get(new Integer(column));
        if(null == columnDisplaySize) return getColumnCount();
        return columnDisplaySize.intValue();
    }

    public int getColumnType(int column) throws SQLException
    {
        Integer columnType = (Integer)columnTypeMap.get(new Integer(column));
        if(null == columnType) return Types.OTHER;
        return columnType.intValue();
    }

    public int getPrecision(int column) throws SQLException
    {
        Integer precision = (Integer)precisionMap.get(new Integer(column));
        if(null == precision) return 0;
        return precision.intValue();
    }

    public int getScale(int column) throws SQLException
    {
        Integer scale = (Integer)scaleMap.get(new Integer(column));
        if(null == scale) return 0;
        return scale.intValue();
    }

    public int isNullable(int column) throws SQLException
    {
        Integer isNullable = (Integer)isNullableMap.get(new Integer(column));
        if(null == isNullable) return columnNullable;
        return isNullable.intValue();
    }

    public boolean isAutoIncrement(int column) throws SQLException
    {
        Boolean isAutoIncrement = (Boolean)isAutoIncrementMap.get(new Integer(column));
        if(null == isAutoIncrement) return false;
        return isAutoIncrement.booleanValue();
    }

    public boolean isCaseSensitive(int column) throws SQLException
    {
        Boolean isCaseSensitive = (Boolean)isCaseSensitiveMap.get(new Integer(column));
        if(null == isCaseSensitive) return true;
        return isCaseSensitive.booleanValue();
    }

    public boolean isCurrency(int column) throws SQLException
    {
        Boolean isCurrency = (Boolean)isCurrencyMap.get(new Integer(column));
        if(null == isCurrency) return false;
        return isCurrency.booleanValue();
    }

    public boolean isDefinitelyWritable(int column) throws SQLException
    {
        Boolean isDefinitelyWritable = (Boolean)isDefinitelyWritableMap.get(new Integer(column));
        if(null == isDefinitelyWritable) return true;
        return isDefinitelyWritable.booleanValue();
    }

    public boolean isReadOnly(int column) throws SQLException
    {
        Boolean isReadOnly = (Boolean)isReadOnlyMap.get(new Integer(column));
        if(null == isReadOnly) return false;
        return isReadOnly.booleanValue();
    }

    public boolean isSearchable(int column) throws SQLException
    {
        Boolean isSearchable = (Boolean)isSearchableMap.get(new Integer(column));
        if(null == isSearchable) return true;
        return isSearchable.booleanValue();
    }

    public boolean isSigned(int column) throws SQLException
    {
        Boolean isSigned = (Boolean)isSignedMap.get(new Integer(column));
        if(null == isSigned) return false;
        return isSigned.booleanValue();
    }

    public boolean isWritable(int column) throws SQLException
    {
        Boolean isWritable = (Boolean)isWritableMap.get(new Integer(column));
        if(null == isWritable) return true;
        return isWritable.booleanValue();
    }

    public String getCatalogName(int column) throws SQLException
    {
        String catalogName = (String)catalogNameMap.get(new Integer(column));
        if(null == catalogName) return "";
        return catalogName;
    }

    public String getColumnClassName(int column) throws SQLException
    {
        String columnClassName = (String)columnClassNameMap.get(new Integer(column));
        if(null == columnClassName) return Object.class.getName();
        return columnClassName;
    }

    public String getColumnLabel(int column) throws SQLException
    {
        String columnLabel = (String)columnLabelMap.get(new Integer(column));
        if(null == columnLabel) return getColumnName(column);
        return columnLabel;
    }

    public String getColumnName(int column) throws SQLException
    {
        String columnName = (String)columnNameMap.get(new Integer(column));
        if(null == columnName) return "";
        return columnName;
    }

    public String getColumnTypeName(int column) throws SQLException
    {
        String columnTypeName = (String)columnTypeNameMap.get(new Integer(column));
        if(null == columnTypeName) return "";
        return columnTypeName;
    }

    public String getSchemaName(int column) throws SQLException
    {
        String schemaName = (String)schemaNameMap.get(new Integer(column));
        if(null == schemaName) return "";
        return schemaName;
    }

    public String getTableName(int column) throws SQLException
    {
        String tableName = (String)tableNameMap.get(new Integer(column));
        if(null == tableName) return "";
        return tableName;
    }
}
