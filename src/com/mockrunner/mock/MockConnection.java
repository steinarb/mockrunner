package com.mockrunner.mock;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.mockobjects.sql.MockCallableStatement;
import com.mockobjects.sql.MockStatement;

/**
 * Mock implementation of <code>Connection</code>.
 */
public class MockConnection extends com.mockobjects.sql.MockConnection2
{
    private Map preparedStatements = new HashMap();
    
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
        MockPreparedStatement statement = new MockPreparedStatement();
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
}
