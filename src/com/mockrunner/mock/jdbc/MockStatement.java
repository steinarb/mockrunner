package com.mockrunner.mock.jdbc;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mockrunner.util.SQLUtil;

/**
 * Mock implementation of <code>Statement</code>.
 */
public class MockStatement implements Statement
{
    private AbstractResultSetHandler resultSetHandler;
    private ResultSet nextResultSet = null;
    private int nextUpdateCount = -1;
    private List batches = new ArrayList();
    private String cursorName = "";
    private int querySeconds = 0;
    private int maxRows = 0;
    private int maxFieldSize = 0;
    private int fetchDirection = ResultSet.FETCH_FORWARD;
    private int fetchSize = 0;
    private int resultSetType = ResultSet.TYPE_FORWARD_ONLY;
    private int resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;
    private int resultSetHoldability = ResultSet.HOLD_CURSORS_OVER_COMMIT;
    private boolean closed = false;
    private Connection connection;
    
    public MockStatement(Connection connection)
    {
        this(connection, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }
    
    public MockStatement(Connection connection, int resultSetType, int resultSetConcurrency)
    {
        this(connection, resultSetType, resultSetConcurrency, ResultSet.HOLD_CURSORS_OVER_COMMIT);
    }
    
    public MockStatement(Connection connection, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
    {
        this.connection = connection;
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        this.resultSetHoldability = resultSetHoldability;
    }
    
    public boolean isClosed()
    {
        return closed;
    }
    
    public void setResultSetHandler(AbstractResultSetHandler resultSetHandler)
    {
        this.resultSetHandler = resultSetHandler;
    }
    
    protected void setNextResultSet(ResultSet resultSet)
    {
        this.nextResultSet = resultSet;
    }
    
    protected void setNextUpdateCount(int updateCount)
    {
        this.nextUpdateCount = updateCount;
    }
    
    public String getCursorName()
    {
        return cursorName;
    }
    
    public ResultSet executeQuery(String sql) throws SQLException
    {
        resultSetHandler.addExecutedStatement(sql);
        MockResultSet result = resultSetHandler.getResultSet(sql);
        if(null != result)
        {
            return cloneResultSet(result);
        }
        return cloneResultSet(resultSetHandler.getGlobalResultSet());
    }

    public int executeUpdate(String sql) throws SQLException
    {
        resultSetHandler.addExecutedStatement(sql);
        Integer returnValue = resultSetHandler.getUpdateCount(sql);
        if(null != returnValue)
        {
            return returnValue.intValue();
        }
        return resultSetHandler.getGlobalUpdateCount();
    }

    public void close() throws SQLException
    {
        closed = true;
    }

    public int getMaxFieldSize() throws SQLException
    {
        return maxFieldSize;
    }

    public void setMaxFieldSize(int maxFieldSize) throws SQLException
    {
        this.maxFieldSize = maxFieldSize;
    }

    public int getMaxRows() throws SQLException
    {
        return maxRows;
    }

    public void setMaxRows(int maxRows) throws SQLException
    {
        this.maxRows = maxRows;
    }

    public void setEscapeProcessing(boolean enable) throws SQLException
    {

    }

    public int getQueryTimeout() throws SQLException
    {
        return querySeconds;
    }

    public void setQueryTimeout(int querySeconds) throws SQLException
    {
        this.querySeconds = querySeconds;
    }

    public void cancel() throws SQLException
    {

    }

    public SQLWarning getWarnings() throws SQLException
    {
        return null;
    }

    public void clearWarnings() throws SQLException
    {

    }

    public void setCursorName(String cursorName) throws SQLException
    {
        this.cursorName = cursorName;
    }

    public boolean execute(String sql) throws SQLException
    {
        boolean callExecuteQuery = isQuery(sql);
        if(callExecuteQuery)
        {
            setNextResultSet(executeQuery(sql));
        }
        else
        {
            setNextUpdateCount(executeUpdate(sql));
        }
        return callExecuteQuery;
    }
    
    protected boolean isQuery(String sql)
    {
        boolean isQuery;
        Boolean returnsResultSet = resultSetHandler.getReturnsResultSet(sql);
        if(null != returnsResultSet)
        {
            isQuery = returnsResultSet.booleanValue();
        }
        else
        {
            isQuery = SQLUtil.isSelect(sql);
        }
        return isQuery;
    }

    public ResultSet getResultSet() throws SQLException
    {
        ResultSet tempResultSet = nextResultSet;
        nextResultSet = null;
        return tempResultSet;
    }

    public int getUpdateCount() throws SQLException
    {
        int tempUpdateCount = nextUpdateCount;
        nextUpdateCount = -1;
        return tempUpdateCount;
    }

    public boolean getMoreResults() throws SQLException
    {
        return false;
    }

    public void setFetchDirection(int fetchDirection) throws SQLException
    {
        this.fetchDirection = fetchDirection;
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

    public void addBatch(String sql) throws SQLException
    {
        batches.add(sql);
    }

    public void clearBatch() throws SQLException
    {
        batches.clear();
    }

    public int[] executeBatch() throws SQLException
    {
        int[] results = new int[batches.size()];
        for(int ii = 0; ii < results.length; ii++)
        {
            String nextSQL = (String)batches.get(ii);
            if(isQuery(nextSQL))
            {
                throw new BatchUpdateException("SQL " + batches.get(ii) + " in ths list of batches returned a ResultSet.", null);
            }
            results[ii] = executeUpdate(nextSQL);
        }
        return results;
    }

    public Connection getConnection() throws SQLException
    {
        return connection;
    }

    public boolean getMoreResults(int current) throws SQLException
    {
        return false;
    }

    public ResultSet getGeneratedKeys() throws SQLException
    {
        return null;
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException
    {
        return executeUpdate(sql);
    }

    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException
    {
        return executeUpdate(sql);
    }

    public int executeUpdate(String sql, String[] columnNames) throws SQLException
    {
        return executeUpdate(sql);
    }

    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException
    {
        return execute(sql);
    }

    public boolean execute(String sql, int[] columnIndexes) throws SQLException
    {
        return execute(sql);
    }

    public boolean execute(String sql, String[] columnNames) throws SQLException
    {
        return execute(sql);
    }
    
    public int getResultSetType() throws SQLException
    {
        return resultSetType;
    }
    
    public int getResultSetConcurrency() throws SQLException
    {
        return resultSetConcurrency;
    }
    
    public int getResultSetHoldability() throws SQLException
    {
        return resultSetHoldability;
    }
    
    protected MockResultSet cloneResultSet(MockResultSet resultSet) throws SQLException
    {
        if(null == resultSet) return null;
        MockResultSet clone = (MockResultSet)resultSet.clone();
        clone.setStatement(this);
        return clone;
    }
}
