package com.mockrunner.mock.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

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
    
    public void setupColumnCount(int columnCount)
    {
        this.columnCount = columnCount;
    }
    
    public void setupColumnDisplaySize(int column, int displaySize)
    {
        columnDisplaySizeMap.put(new Integer(column), new Integer(displaySize));
    }
    
    public void setupColumnType(int column, int columnType)
    {
        columnTypeMap.put(new Integer(column), new Integer(columnType));
    }
    
    public void setupPrecision(int column, int precision)
    {
        precisionMap.put(new Integer(column), new Integer(precision));
    }
    
    public void setupScale(int column, int scale)
    {
        scaleMap.put(new Integer(column), new Integer(scale));
    }
    
    public void setupNullable(int column, int nullable)
    {
        isNullableMap.put(new Integer(column), new Integer(nullable));
    }
    
    public void setupAutoIncrement(int column, boolean autoIncrement)
    {
        isAutoIncrementMap.put(new Integer(column), new Boolean(autoIncrement));
    }
    
    public void setupCaseSensitive(int column, boolean caseSensitive)
    {
        isCaseSensitiveMap.put(new Integer(column), new Boolean(caseSensitive));
    }
    
    public void setupCurrency(int column, boolean currency)
    {
        isCurrencyMap.put(new Integer(column), new Boolean(currency));
    }
    
    public void setupDefinitelyWritable(int column, boolean definitelyWritable)
    {
        isDefinitelyWritableMap.put(new Integer(column), new Boolean(definitelyWritable));
    }
    
    public void setupReadOnly(int column, boolean readOnly)
    {
        isReadOnlyMap.put(new Integer(column), new Boolean(readOnly));
    }
    
    public void setupSearchable(int column, boolean searchable)
    {
        isSearchableMap.put(new Integer(column), new Boolean(searchable));
    }
    
    public void setupSigned(int column, boolean signed)
    {
        isSignedMap.put(new Integer(column), new Boolean(signed));
    }
    
    public void setupWritable(int column, boolean writable)
    {

    }
    
    public void setupCatalogName(int column, String catalogName)
    {

    }
    
    public void setupColumnClassName(int column, String columnClassName)
    {

    }
    
    public void setupColumnLabel(int column, String columnLabel)
    {

    }
    
    public void setupColumnName(int column, String columnName)
    {

    }
    
    public void setupColumnTypeName(int column, String columnTypeName)
    {

    }
    
    public void setupSchemaName(int column, String schemaName)
    {

    }
    
    public void setupTableName(int column, String tableName)
    {

    }
    
    public int getColumnCount() throws SQLException
    {
        return 0;
    }

    public int getColumnDisplaySize(int column) throws SQLException
    {
        return 0;
    }

    public int getColumnType(int column) throws SQLException
    {
        return Types.OTHER;
    }

    public int getPrecision(int column) throws SQLException
    {
        return 0;
    }

    public int getScale(int column) throws SQLException
    {
        return 0;
    }

    public int isNullable(int column) throws SQLException
    {
        return columnNullable;
    }

    public boolean isAutoIncrement(int column) throws SQLException
    {
        return false;
    }

    public boolean isCaseSensitive(int column) throws SQLException
    {
        return true;
    }

    public boolean isCurrency(int column) throws SQLException
    {
        return false;
    }

    public boolean isDefinitelyWritable(int column) throws SQLException
    {
        return true;
    }

    public boolean isReadOnly(int column) throws SQLException
    {
        return false;
    }

    public boolean isSearchable(int column) throws SQLException
    {
        return true;
    }

    public boolean isSigned(int column) throws SQLException
    {
        return false;
    }

    public boolean isWritable(int column) throws SQLException
    {
        return true;
    }

    public String getCatalogName(int column) throws SQLException
    {
        return "";
    }

    public String getColumnClassName(int column) throws SQLException
    {
        return Object.class.getName();
    }

    public String getColumnLabel(int column) throws SQLException
    {
        return getColumnName(column);
    }

    public String getColumnName(int column) throws SQLException
    {
        return "";
    }

    public String getColumnTypeName(int column) throws SQLException
    {
        return "";
    }

    public String getSchemaName(int column) throws SQLException
    {
        return "";
    }

    public String getTableName(int column) throws SQLException
    {
        return "";
    }
}
