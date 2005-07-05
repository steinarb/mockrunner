package com.mockrunner.mock.jdbc;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.jdbc.AbstractResultSetHandler;
import com.mockrunner.jdbc.SQLUtil;

/**
 * Mock implementation of <code>Statement</code>.
 */
public class MockStatement implements Statement
{
    private AbstractResultSetHandler resultSetHandler;
    private ResultSet currentResultSet = null;
    private int currentUpdateCount = -1;
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
    private MockResultSet lastGeneratedKeys = null;
    private boolean closed = false;
    private Connection connection;
    
    public MockStatement(Connection connection)
    {
        this.connection = connection;
        this.resultSetType = ResultSet.TYPE_FORWARD_ONLY;
        this.resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;
        try
        {
            this.resultSetHoldability = connection.getMetaData().getResultSetHoldability();
        }
        catch(SQLException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    public MockStatement(Connection connection, int resultSetType, int resultSetConcurrency)
    {
        this.connection = connection;
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        try
        {
            this.resultSetHoldability = connection.getMetaData().getResultSetHoldability();
        }
        catch(SQLException exc)
        {
            throw new NestedApplicationException(exc);
        } 
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
    
    protected void setResultSet(ResultSet resultSet)
    {
        this.currentUpdateCount = -1;
        this.currentResultSet = resultSet;
    }
    
    protected void setUpdateCount(int updateCount)
    {
        this.currentResultSet = null;
        this.currentUpdateCount = updateCount;
    }
    
    public String getCursorName()
    {
        return cursorName;
    }
    
    public ResultSet executeQuery(String sql) throws SQLException
    {
        SQLException exception = resultSetHandler.getSQLException(sql);
        if(null != exception)
        {
            throw exception;
        }
        resultSetHandler.addExecutedStatement(sql);
        MockResultSet result = resultSetHandler.getResultSet(sql);
        if(null != result)
        {
            result = cloneResultSet(result);
            resultSetHandler.addReturnedResultSet(result);
            setResultSet(result);
            return result;
        }
        result = cloneResultSet(resultSetHandler.getGlobalResultSet());
        resultSetHandler.addReturnedResultSet(result);
        setResultSet(result);
        return result;
    }

    public int executeUpdate(String sql) throws SQLException
    {
        SQLException exception = resultSetHandler.getSQLException(sql);
        if(null != exception)
        {
            throw exception;
        }
        resultSetHandler.addExecutedStatement(sql);
        Integer returnValue = resultSetHandler.getUpdateCount(sql);
        if(null != returnValue)
        {
            int updateCount = returnValue.intValue();
            setUpdateCount(updateCount);
            return updateCount;
        }
        int updateCount = resultSetHandler.getGlobalUpdateCount();
        setUpdateCount(updateCount);
        return updateCount;
    }
    
    public boolean execute(String sql) throws SQLException
    {
        boolean callExecuteQuery = isQuery(sql);
        if(callExecuteQuery)
        {
            executeQuery(sql);
        }
        else
        {
            executeUpdate(sql);
        }
        return callExecuteQuery;
    }
    
    public int[] executeBatch() throws SQLException
    {
        int[] results = new int[batches.size()];
        for(int ii = 0; ii < results.length; ii++)
        {
            String nextSQL = (String)batches.get(ii);
            if(isQuery(nextSQL))
            {
                throw new BatchUpdateException("SQL " + batches.get(ii) + " in the list of batches returned a ResultSet.", null);
            }
            results[ii] = executeUpdate(nextSQL);
        }
        return results;
    }
    
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException
    {
        int updateCount = executeUpdate(sql);
        setGeneratedKeysResultSet(sql, autoGeneratedKeys);
        return updateCount;
    }

    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException
    {
        return executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public int executeUpdate(String sql, String[] columnNames) throws SQLException
    {
        return executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException
    {
        boolean isQuery = execute(sql);
        setGeneratedKeysResultSet(sql, autoGeneratedKeys);
        return isQuery;
    }

    public boolean execute(String sql, int[] columnIndexes) throws SQLException
    {
        return execute(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public boolean execute(String sql, String[] columnNames) throws SQLException
    {
        return execute(sql, Statement.RETURN_GENERATED_KEYS);
    }
    
    private void setGeneratedKeysResultSet(String sql, int autoGeneratedKeys) throws SQLException
    {
        if(Statement.RETURN_GENERATED_KEYS != autoGeneratedKeys && Statement.NO_GENERATED_KEYS != autoGeneratedKeys)
        {
            throw new SQLException("autoGeneratedKeys must be either Statement.RETURN_GENERATED_KEYS or Statement.NO_GENERATED_KEYS");
        }
        if(Statement.RETURN_GENERATED_KEYS == autoGeneratedKeys)
        {
            lastGeneratedKeys = resultSetHandler.getGeneratedKeys(sql);
            if(null == lastGeneratedKeys)
            {
                lastGeneratedKeys = resultSetHandler.getGlobalGeneratedKeys();
            }
        }
        else
        {
            lastGeneratedKeys = null;
        }
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
        return currentResultSet;
    }

    public int getUpdateCount() throws SQLException
    {
        return currentUpdateCount;
    }

    public boolean getMoreResults() throws SQLException
    {
        if(null != currentResultSet)
        {
            currentResultSet.close();
        }
        currentResultSet = null;
        currentUpdateCount = -1;
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

    public Connection getConnection() throws SQLException
    {
        return connection;
    }

    public boolean getMoreResults(int current) throws SQLException
    {
        return getMoreResults();
    }

    public ResultSet getGeneratedKeys() throws SQLException
    {
        if(null == lastGeneratedKeys)
        {
            MockResultSet resultSet = new MockResultSet("Last statement did not generate any keys");
            resultSet.setStatement(this);
            return resultSet;
        }
        return cloneResultSet(lastGeneratedKeys);
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
