package com.mockrunner.mock.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.mockobjects.sql.MockCallableStatement;
import com.mockobjects.sql.MockDatabaseMetaData;
import com.mockobjects.sql.MockStatement;

/**
 * Mock implementation of <code>Connection</code>.
 */
public class MockConnection implements Connection
{
    private Map preparedStatements = new HashMap();
    private boolean closed = false;
    private boolean autoCommit = false;
    private boolean readOnly = false;
    private int holdability = ResultSet.HOLD_CURSORS_OVER_COMMIT;
    private int level = Connection.TRANSACTION_READ_UNCOMMITTED;
    private Map typeMap = new HashMap();
    private String catalog = null;
        
    public MockPreparedStatement getPreparedStatement(String sql)
    {
        return (MockPreparedStatement)preparedStatements.get(sql);
    }
    
    public Map getPreparedStatementMap()
    {
        return preparedStatements;
    }
    
    public Statement createStatement() throws SQLException
    {
        return new MockStatement();
    }

    public Statement createStatement(int arg0, int arg1, int arg2) throws SQLException
    {
        return new MockStatement();
    }

    public Statement createStatement(int arg0, int arg1) throws SQLException
    {
        return new MockStatement();
    }

    public CallableStatement prepareCall(String arg0, int arg1, int arg2, int arg3) throws SQLException
    {
        return new MockCallableStatement();
    }

    public CallableStatement prepareCall(String arg0, int arg1, int arg2) throws SQLException
    {
        return new MockCallableStatement();
    }

    public CallableStatement prepareCall(String arg0) throws SQLException
    {
        return new MockCallableStatement();
    }
    
    public PreparedStatement prepareStatement(String sql) throws SQLException
    {
        MockPreparedStatement statement = new MockPreparedStatement(this);
        preparedStatements.put(sql, statement);
        return statement;
    }

    public PreparedStatement prepareStatement(String sql, int arg1, int arg2, int arg3) throws SQLException
    {
        return prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(String sql, int arg1, int arg2) throws SQLException
    {
        return prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(String sql, int arg1) throws SQLException
    {
        return prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(String sql, int[] arg1) throws SQLException
    {
        return prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(String sql, String[] arg1) throws SQLException
    {
        return prepareStatement(sql);
    }
    
    public void close() throws SQLException
    {
        closed = true;
    }

    public boolean getAutoCommit() throws SQLException
    {
        return autoCommit;
    }

    public String getCatalog() throws SQLException
    {
        return catalog;
    }

    public int getHoldability() throws SQLException
    {
        return holdability;
    }

    public DatabaseMetaData getMetaData() throws SQLException
    {
        return new MockDatabaseMetaData();
    }

    public int getTransactionIsolation() throws SQLException
    {
        return level;
    }

    public Map getTypeMap() throws SQLException
    {
        return typeMap;
    }

    public SQLWarning getWarnings() throws SQLException
    {
        return null;
    }

    public boolean isClosed() throws SQLException
    {
        return closed;
    }

    public boolean isReadOnly() throws SQLException
    {
        return readOnly;
    }

    public String nativeSQL(String sql) throws SQLException
    {
        return sql;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException
    {
        this.autoCommit = autoCommit;
    }

    public void setCatalog(String catalog) throws SQLException
    {
        this.catalog = catalog;
    }

    public void setHoldability(int holdability) throws SQLException
    {
        this.holdability = holdability;
    }

    public void setReadOnly(boolean readOnly) throws SQLException
    {
        this.readOnly = readOnly;
    }

    public Savepoint setSavepoint() throws SQLException
    {
        return new MockSavepoint();
    }

    public Savepoint setSavepoint(String name) throws SQLException
    {
        return new MockSavepoint(name);
    }

    public void setTransactionIsolation(int level) throws SQLException
    {
        this.level = level;
    }

    public void setTypeMap(Map typeMap) throws SQLException
    {
        this.typeMap = typeMap;
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException
    {

    }

    public void commit() throws SQLException
    {

    }

    public void rollback() throws SQLException
    {

    }

    public void rollback(Savepoint savepoint) throws SQLException
    {

    }

    public void clearWarnings() throws SQLException
    {

    }
}
