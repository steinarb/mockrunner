package com.mockrunner.jdbc;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockCallableStatement;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockSavepoint;
import com.mockrunner.mock.jdbc.MockStatement;
import com.mockrunner.util.ParameterUtil;
import com.mockrunner.util.SearchUtil;

/**
 * Module for JDBC tests.
 */
public class JDBCTestModule
{
    private JDBCMockObjectFactory mockFactory;
    private boolean caseSensitive = false;
    private boolean exactMatch = false;
      
    public JDBCTestModule(JDBCMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }
    
    /**
     * Set if specified SQL statements should be handled case sensitive.
     * Defaults to to <code>false</code>, i.e. <i>INSERT</i> is the same
     * as <i>insert</i>.
     * @param caseSensitive enable or disable case sensitivity
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }
    
    /**
     * Set if specified SQL statements must match exactly.
     * Defaults to <code>false</code>, i.e. the SQL statement used
     * to create a <code>PreparedStatement</code> must contain
     * the specified SQL statement, but does not have to match
     * exactly. If the original statement is <i>insert into mytable values(?, ?, ?)</i>
     * <code>verifyPreparedStatementPresent("insert into mytable")</code>
     * will pass.
     * @param exactMatch enable or disable exact matching
     */
    public void setExactMatch(boolean exactMatch)
    {
        this.exactMatch = exactMatch;
    }
    
    /**
     * Returns the {@link StatementResultSetHandler}. 
     * The {@link StatementResultSetHandler}
     * contains methods that can be used to specify the 
     * {@link com.mockrunner.mock.jdbc.MockResultSet} objects
     * and update counts that a {@link com.mockrunner.mock.jdbc.MockStatement} 
     * should return when executing an SQL statement.
     * @return the {@link StatementResultSetHandler}
     */
    public StatementResultSetHandler getStatementResultSetHandler()
    {
        return mockFactory.getMockConnection().getStatementResultSetHandler();
    }
    
    /**
     * Returns the {@link PreparedStatementResultSetHandler}.
     * The {@link PreparedStatementResultSetHandler}
     * contains methods that can be used to specify the 
     * {@link com.mockrunner.mock.jdbc.MockResultSet} objects
     * and update counts that a {@link com.mockrunner.mock.jdbc.MockPreparedStatement} 
     * should return when executing an SQL statement.
     * @return the {@link PreparedStatementResultSetHandler}
     */
    public PreparedStatementResultSetHandler getPreparedStatementResultSetHandler()
    {
        return mockFactory.getMockConnection().getPreparedStatementResultSetHandler();
    }
    
    /**
     * Returns the {@link CallableStatementResultSetHandler}.
     * The {@link CallableStatementResultSetHandler}
     * contains methods that can be used to specify the 
     * {@link com.mockrunner.mock.jdbc.MockResultSet} objects
     * and update counts that a {@link com.mockrunner.mock.jdbc.MockCallableStatement} 
     * should return when executing an SQL statement.
     * @return the {@link CallableStatementResultSetHandler}
     */
    public CallableStatementResultSetHandler getCallableStatementResultSetHandler()
    {
        return mockFactory.getMockConnection().getCallableStatementResultSetHandler();
    }
    
    /**
     * Returns a {@link com.mockrunner.mock.jdbc.MockStatement} by its index.
     * @param index the index of the <code>Statement</code>
     * @return the <code>Statement</code> or <code>null</code>, if there is no such
     *         <code>Statement</code>
     */
    public MockStatement getStatement(int index)
    {
        List statements = getStatements();
        if(index < statements.size()) return (MockStatement)statements.get(index);
        return null;
    }
    
    /**
     * Returns all {@link com.mockrunner.mock.jdbc.MockStatement}.
     * @return the <code>List</code> of <code>Statement</code> objects
     */
    public List getStatements()
    {
        return mockFactory.getMockConnection().getStatementResultSetHandler().getStatements();
    }
    
    /**
     * Returns a <code>List</code> of all SQL statements that were executed
     * by calling an <code>execute</code> method of a {@link com.mockrunner.mock.jdbc.MockStatement},
     * {@link com.mockrunner.mock.jdbc.MockPreparedStatement} or
     * {@link com.mockrunner.mock.jdbc.MockCallableStatement}.
     * @return the <code>List</code> of <code>Statement</code> objects
     */
    public List getExecutedSQLStatements()
    {
        ArrayList list = new ArrayList();
        list.addAll(mockFactory.getMockConnection().getStatementResultSetHandler().getExecutedStatements());
        list.addAll(mockFactory.getMockConnection().getPreparedStatementResultSetHandler().getExecutedStatements());
        list.addAll(mockFactory.getMockConnection().getCallableStatementResultSetHandler().getExecutedStatements());
        return list;
    }
    
    /**
     * Returns a {@link com.mockrunner.mock.jdbc.MockPreparedStatement} that was 
     * created using a {@link com.mockrunner.mock.jdbc.MockConnection} by its index.
     * @param index the index of the <code>PreparedStatement</code>
     * @return the <code>PreparedStatement</code> or <code>null</code>, if there is no such
     *         <code>PreparedStatement</code>
     */
    public MockPreparedStatement getPreparedStatement(int index)
    {
        List statements = getPreparedStatements();
        if(index < statements.size()) return (MockPreparedStatement)statements.get(index);
        return null;
    }
    
    /**
     * Returns a {@link com.mockrunner.mock.jdbc.MockPreparedStatement} that was 
     * created using a {@link com.mockrunner.mock.jdbc.MockConnection} by its SQL statement.
     * If there are more than one {@link com.mockrunner.mock.jdbc.MockPreparedStatement}
     * objects with the specified SQL, the first one will be returned.
     * Please note that you can modify the search parameters with 
     * {@link #setCaseSensitive} and {@link #setExactMatch}.
     * @param sql the SQL statement used to create the <code>PreparedStatement</code>
     * @return the <code>PreparedStatement</code> or <code>null</code>, if there is no macth
     */
    public MockPreparedStatement getPreparedStatement(String sql)
    {
        List list = getPreparedStatements(sql);
        if(null != list && list.size() > 0)
        {
            return (MockPreparedStatement)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns all {@link com.mockrunner.mock.jdbc.MockPreparedStatement}.
     * @return the <code>List</code> of <code>PreparedStatement</code> objects
     */
    public List getPreparedStatements()
    {
        return mockFactory.getMockConnection().getPreparedStatementResultSetHandler().getPreparedStatements();
    }
    
    /**
     * Returns all {@link com.mockrunner.mock.jdbc.MockPreparedStatement} with
     * the specified SQL statement as a <code>List</code>. If there are no matches, an empty
     * <code>List</code> will be returned. Please note that you can modify
     * the search parameters with {@link #setCaseSensitive} and {@link #setExactMatch}.
     * @param sql the SQL statement used to create the <code>PreparedStatement</code>
     * @return the <code>List</code> of <code>PreparedStatement</code> objects
     */
    public List getPreparedStatements(String sql)
    {
        Map sqlStatements = mockFactory.getMockConnection().getPreparedStatementResultSetHandler().getPreparedStatementMap();
        return SearchUtil.getMatchingObjects(sqlStatements, sql, caseSensitive, exactMatch, false); 
    }
    
    /**
     * Returns a {@link com.mockrunner.mock.jdbc.MockCallableStatement} that was 
     * created using a {@link com.mockrunner.mock.jdbc.MockConnection} by its index.
     * @param index the index of the <code>CallableStatement</code>
     * @return the <code>CallableStatement</code> or <code>null</code>, if there is no such
     *         <code>CallableStatement</code>
     */
    public MockCallableStatement getCallableStatement(int index)
    {
        List statements = getCallableStatements();
        if(index < statements.size()) return (MockCallableStatement)statements.get(index);
        return null;
    }
    
    /**
     * Returns a {@link com.mockrunner.mock.jdbc.MockCallableStatement} that was 
     * created using a {@link com.mockrunner.mock.jdbc.MockConnection} by its SQL statement
     * (the stored procedure name).
     * If there are more than one {@link com.mockrunner.mock.jdbc.MockCallableStatement}
     * objects with the specified SQL, the first one will be returned.
     * Please note that you can modify the search parameters with 
     * {@link #setCaseSensitive} and {@link #setExactMatch}.
     * @param sql the SQL statement used to create the <code>CallableStatement</code>
     * @return the <code>CallableStatement</code> or <code>null</code>, if there is no macth
     */
    public MockCallableStatement getCallableStatement(String sql)
    {
        List list = getCallableStatements(sql);
        if(null != list && list.size() > 0)
        {
            return (MockCallableStatement)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns all {@link com.mockrunner.mock.jdbc.MockCallableStatement}.
     * @return the <code>List</code> of <code>CallableStatement</code> objects
     */
    public List getCallableStatements()
    {
        return mockFactory.getMockConnection().getCallableStatementResultSetHandler().getCallableStatements();
    }
    
    /**
     * Returns all {@link com.mockrunner.mock.jdbc.MockCallableStatement} with
     * the specified SQL statement (name of the stored procedure) as a <code>List</code>. 
     * If there are no matches, an empty <code>List</code> will be returned. 
     * Please note that you can modify the search parameters with {@link #setCaseSensitive} 
     * and {@link #setExactMatch}.
     * @param sql the SQL statement used to create the <code>CallableStatement</code>
     * @return the <code>List</code> of <code>CallableStatement</code> objects
     */
    public List getCallableStatements(String sql)
    {
        Map sqlStatements = mockFactory.getMockConnection().getCallableStatementResultSetHandler().getCallableStatementMap();
        return SearchUtil.getMatchingObjects(sqlStatements, sql, caseSensitive, exactMatch, false); 
    }
    
    /**
     * Returns a parameter that was added to a <code>PreparedStatement</code>
     * using its <code>set</code> methods. Simple data types are returned as
     * the corresponsing wrapper type.
     * @param statement the <code>PreparedStatement</code>
     * @param indexOfParameter the index used to set the parameter
     * @return the corresponding object
     */
    public Object getPreparedStatementParameter(PreparedStatement statement, int indexOfParameter)
    {
        if(null == statement) return null;
        return ((MockPreparedStatement)statement).getParameter(indexOfParameter);
    }
    
    /**
     * Returns a parameter that was added to a <code>PreparedStatement</code>
     * using its <code>set</code> methods. Uses the first <code>PreparedStatement</code>
     * with the specified SQL statement. Simple data types are returned as
     * the corresponsing wrapper type.
     * @param sql the SQL statement
     * @param indexOfParameter the index used to set the object
     * @return the corresponding object
     */
    public Object getPreparedStatementParameter(String sql, int indexOfParameter)
    {
        return getPreparedStatementParameter(getPreparedStatement(sql), indexOfParameter);
    }
    
    /**
     * Returns an object that was added to a <code>PreparedStatement</code>
     * using its <code>set</code> methods. Simple data types are returned as
     * the corresponsing wrapper type.
     * @param indexOfStatement the index of the statement
     * @param indexOfParameter the index used to set the object
     * @return the corresponding object
     */
    public Object getPreparedStatementParameter(int indexOfStatement, int indexOfParameter)
    {
        return getPreparedStatementParameter(getPreparedStatement(indexOfStatement), indexOfParameter);
    }
    
    /**
     * Returns a parameter that was added to a <code>CallableStatement</code>
     * using its <code>set</code> methods. Simple data types are returned as
     * the corresponsing wrapper type.
     * @param statement the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the parameter
     * @return the corresponding object
     */
    public Object getCallableStatementParameter(CallableStatement statement, int indexOfParameter)
    {
        if(null == statement) return null;
        return ((MockCallableStatement)statement).getParameter(indexOfParameter);
    }

    /**
     * Returns a parameter that was added to a <code>CallableStatement</code>
     * using its <code>set</code> methods. Uses the first <code>CallableStatement</code>
     * with the specified SQL statement. Simple data types are returned as
     * the corresponsing wrapper type.
     * @param sql the SQL statement
     * @param indexOfParameter the index used to set the object
     * @return the corresponding object
     */
    public Object getCallableStatementParameter(String sql, int indexOfParameter)
    {
        return getCallableStatementParameter(getCallableStatement(sql), indexOfParameter);
    }

    /**
     * Returns an object that was added to a <code>CallableStatement</code>
     * using its <code>set</code> methods. Simple data types are returned as
     * the corresponsing wrapper type.
     * @param indexOfStatement the index of the statement
     * @param indexOfParameter the index used to set the object
     * @return the corresponding object
     */
    public Object getCallableStatementParameter(int indexOfStatement, int indexOfParameter)
    {
        return getCallableStatementParameter(getCallableStatement(indexOfStatement), indexOfParameter);
    }
    
    /**
     * Returns a parameter that was added to a <code>CallableStatement</code>
     * using its <code>set</code> methods.
     * @param statement the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the parameter
     * @return the corresponding object
     */
    public Object getCallableStatementParameter(CallableStatement statement, String nameOfParameter)
    {
        if(null == statement) return null;
        return ((MockCallableStatement)statement).getParameter(nameOfParameter);
    }

    /**
     * Returns a parameter that was added to a <code>CallableStatement</code>
     * using its <code>set</code> methods. Uses the first <code>CallableStatement</code>
     * with the specified SQL statement.
     * @param sql the SQL statement
     * @param indexOfParameter the index used to set the object
     * @return the corresponding object
     */
    public Object getCallableStatementParameter(String sql, String nameOfParameter)
    {
        return getCallableStatementParameter(getCallableStatement(sql), nameOfParameter);
    }

    /**
     * Returns an object that was added to a <code>CallableStatement</code>
     * using its <code>set</code> methods.
     * @param indexOfStatement the index of the statement
     * @param indexOfParameter the index used to set the object
     * @return the corresponding object
     */
    public Object getCallableStatementParameter(int indexOfStatement, String nameOfParameter)
    {
        return getCallableStatementParameter(getCallableStatement(indexOfStatement), nameOfParameter);
    }
    
    /**
     * Returns a list of all <code>Savepoint</code> objects.
     * @return the <code>List</code> of {@link com.mockrunner.mock.jdbc.MockSavepoint} objects
     */
    public List getSavepoints()
    {
        return new ArrayList(mockFactory.getMockConnection().getSavepointMap().values());
    }
    
    /**
     * Returns the <code>Savepoint</code> with the specified index.
     * The index is the number of the created <code>Savepoint</code>
     * starting with 0 for the first <code>Savepoint</code>.
     * @param index the index
     * @return the {@link com.mockrunner.mock.jdbc.MockSavepoint}
     */
    public MockSavepoint getSavepoint(int index)
    {
        List savepoints = getSavepoints();
        for(int ii = 0; ii < savepoints.size(); ii++)
        {
            MockSavepoint currentSavepoint = (MockSavepoint)savepoints.get(ii);
            if(currentSavepoint.getNumber() == index) return currentSavepoint;
        }
        return null;
    }
    
    /**
     * Returns the first <code>Savepoint</code> with the specified name.
     * Unnamed <code>Savepoint</code> objects get the name <i>""</i>.
     * @param name the name
     * @return the {@link com.mockrunner.mock.jdbc.MockSavepoint}
     */
    public MockSavepoint getSavepoint(String name)
    {
        List savepoints = getSavepoints();
        for(int ii = 0; ii < savepoints.size(); ii++)
        {
            MockSavepoint currentSavepoint = (MockSavepoint)savepoints.get(ii);
            try
            {
                if(currentSavepoint.getSavepointName().equals(name)) return currentSavepoint;
            }
            catch(SQLException exc)
            {
                throw new RuntimeException(exc.getMessage());
            }
        }
        return null;
    }
    
    /**
     * Verifies that an SQL statement was executed.
     * @throws VerifyFailedException if verification fails
     */
    public void verifySQLStatementExecuted(String sql)
    {
        if(!SearchUtil.contains(getExecutedSQLStatements(), sql, caseSensitive, exactMatch, false))
        {
            throw new VerifyFailedException("Statement " + sql + " not executed.");
        }
    }
    
    /**
     * Verifies that an SQL statement was not executed.
     * @throws VerifyFailedException if verification fails
     */
    public void verifySQLStatementNotExecuted(String sql)
    {
        if(SearchUtil.contains(getExecutedSQLStatements(), sql, caseSensitive, exactMatch, false))
        {
            throw new VerifyFailedException("Statement " + sql + " not executed.");
        }
    }
    
    /**
     * Verifies that the connection is closed.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyConnectionClosed()
    {
        try
        {
            if(!mockFactory.getMockConnection().isClosed())
            {
                throw new VerifyFailedException("Connection not closed.");
            }
        }
        catch(SQLException exc)
        {
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Verifies that all statements, all prepared statements and
     * all callable statements are closed.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyAllStatementsClosed()
    {
        List statements = getStatements();
        for(int ii = 0; ii < statements.size(); ii++)
        {
            MockStatement statement = (MockStatement)statements.get(ii);
            if(!statement.isClosed())
            {
                throw new VerifyFailedException("Statement with index " + ii + " not closed.");
            }
        }
        statements = getPreparedStatements();
        for(int ii = 0; ii < statements.size(); ii++)
        {
            MockPreparedStatement statement = (MockPreparedStatement)statements.get(ii);
            if(!statement.isClosed())
            {
                throw new VerifyFailedException("Prepared statement with index " + ii + " (SQL " + statement.getSQL() + ") not closed.");
            }
        }
        statements = getCallableStatements();
        for(int ii = 0; ii < statements.size(); ii++)
        {
            MockPreparedStatement statement = (MockCallableStatement)statements.get(ii);
            if(!statement.isClosed())
            {
                throw new VerifyFailedException("Callable statement with index " + ii + " (SQL " + statement.getSQL() + ") not closed.");
            }
        }
    }
    
    /**
     * Verifies that the changes were commited, i.e. the <code>commit</code>
     * method of <code>Connection</code> was at least called once.
     * Makes only sense, if the <code>Connection</code> is not in
     * autocommit mode. Automatic commits are not recognized.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCommited()
    {
        int number = mockFactory.getMockConnection().getNumberCommits();
        if(number <= 0)
        {
            throw new VerifyFailedException("Connection received no commits.");
        }
    }
    
    /**
     * Verifies that the changes were rollbacked, i.e. the <code>rollback</code>
     * method of <code>Connection</code> was at least called once.
     * Makes only sense, if the <code>Connection</code> is not in
     * autocommit mode.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyRollbacked()
    {
        int number = mockFactory.getMockConnection().getNumberRollbacks();
        if(number <= 0)
        {
            throw new VerifyFailedException("Connection received no rollbakcs.");
        }
    }
    
    /**
     * Verifies the number of <code>commit</code> calls.
     * Makes only sense, if the <code>Connection</code> is not in
     * autocommit mode.
     * @param number the expected number of commits
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberCommits(int number)
    {
        int actualNumber = mockFactory.getMockConnection().getNumberCommits();
        if(actualNumber != number)
        {
            throw new VerifyFailedException("Connection received " + actualNumber + "commits, expected " + number);
        }
    }
    
    /**
     * Verifies the number of <code>rollback</code> calls.
     * Makes only sense, if the <code>Connection</code> is not in
     * autocommit mode.
     * @param number the expected number of rollbacks
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberRollbacks(int number)
    {
        int actualNumber = mockFactory.getMockConnection().getNumberRollbacks();
        if(actualNumber != number)
        {
            throw new VerifyFailedException("Connection received " + actualNumber + "rollbacks, expected " + number);
        }
    }
    
    /**
     * Verifies the number of statements.
     * @param number the expected number
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberStatements(int number)
    {
        verifyNumberStatements(number, getStatements());
    }
    
    /**
     * Verifies the number of prepared statements.
     * @param number the expected number
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberPreparedStatements(int number)
    {
        verifyNumberStatements(number, getPreparedStatements());
    }
    
    /**
     * Verifies the number of prepared statements with the specified
     * SQL.
     * @param number the expected number
     * @param sql the SQL
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberPreparedStatements(int number, String sql)
    {
        verifyNumberStatements(number, getPreparedStatements(sql));
    }
    
    /**
     * Verifies the number of callable statements.
     * @param number the expected number
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberCallableStatements(int number)
    {
        verifyNumberStatements(number, getCallableStatements());
    }

    /**
     * Verifies the number of callable statements with the specified
     * SQL.
     * @param number the expected number
     * @param sql the SQL
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberCallableStatements(int number, String sql)
    {
        verifyNumberStatements(number, getCallableStatements(sql));
    }
    
    private void verifyNumberStatements(int number, List statements)
    {
        if(null == statements || statements.size() == 0)
        {
            if(number == 0) return;
            throw new VerifyFailedException("Expected " + number + " pstatements, received 0 prepared statements");
        }
        if(statements.size() != number)
        {
            throw new VerifyFailedException("Expected " + number + " statements, received " + statements.size()+ "prepared statements");
        }
    }
    
    /**
     * Verifies that a statement is closed.
     * @param index the index of the statement
     * @throws VerifyFailedException if verification fails
     */
    public void verifyStatementClosed(int index)
    {
        MockStatement statement = getStatement(index);
        if(null == statement)
        {
            throw new VerifyFailedException("No statement with index " + index + " present.");
        }
        if(!statement.isClosed())
        {
            throw new VerifyFailedException("Statement with index " + index + " not closed.");
        }
    }
    
    /**
     * Verifies that a prepared statement is closed.
     * @param index the index of the prepared statement
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementClosed(int index)
    {
        MockPreparedStatement statement = getPreparedStatement(index);
        if(null == statement)
        {
            throw new VerifyFailedException("No prepared statement with index " + index + " present.");
        }
        if(!statement.isClosed())
        {
            throw new VerifyFailedException("Prepared statement with index " + index + " not closed.");
        }
    }
    
    /**
     * Verifies that a prepared statement is closed.
     * @param sql the SQL statement
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementClosed(String sql)
    {
        MockPreparedStatement statement = getPreparedStatement(sql);
        if(null == statement)
        {
            throw new VerifyFailedException("No prepared statement with SQL " + sql + " present.");
        }
        if(!statement.isClosed())
        {
            throw new VerifyFailedException("Prepared statement with SQL " + sql + " not closed.");
        }
    }
    
    /**
     * Verifies that a callable statement is closed.
     * @param index the index of the callable statement
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementClosed(int index)
    {
        MockCallableStatement statement = getCallableStatement(index);
        if(null == statement)
        {
            throw new VerifyFailedException("No callable statement with index " + index + " present.");
        }
        if(!statement.isClosed())
        {
            throw new VerifyFailedException("Callable statement with index " + index + " not closed.");
        }
    }

    /**
     * Verifies that a callable statement is closed.
     * @param sql the SQL statement
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementClosed(String sql)
    {
        MockCallableStatement statement = getCallableStatement(sql);
        if(null == statement)
        {
            throw new VerifyFailedException("No callable statement with SQL " + sql + " present.");
        }
        if(!statement.isClosed())
        {
            throw new VerifyFailedException("Callable statement with SQL " + sql + " not closed.");
        }
    }
    
    /**
     * Verifies that a <code>PreparedStatement</code> with the specified 
     * SQL statement is present.
     * @param sql the SQL statement
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementPresent(String sql)
    {
        if(null == getPreparedStatement(sql))
        {
            throw new VerifyFailedException("Prepared statement with SQL " +  sql + " present.");
        }
    }
    
    /**
     * Verifies that a <code>PreparedStatement</code> with the specified 
     * SQL statement is not present.
     * @param sql the SQL statement
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementNotPresent(String sql)
    {
        if(null != getPreparedStatement(sql) )
        {
            throw new VerifyFailedException("Prepared statement with SQL " +  sql + " not present.");
        }
    }
    
    /**
     * Verifies that a <code>CallableStatement</code> with the specified 
     * SQL statement is present.
     * @param sql the SQL statement
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementPresent(String sql)
    {
        if(null == getCallableStatement(sql))
        {
            throw new VerifyFailedException("Callable statement with SQL " +  sql + " present.");
        }
    }
    
    /**
     * Verifies that a <code>CallableStatement</code> with the specified 
     * SQL statement is not present.
     * @param sql the SQL statement
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementNotPresent(String sql)
    {
        if(null != getCallableStatement(sql))
        {
            throw new VerifyFailedException("Callable statement with SQL " +  sql + " not present.");
        }
    }
    
    /**
     * Verifies that a parameter was added to a <code>PreparedStatement</code> with
     * the specified index.
     * @param statement the <code>PreparedStatement</code>
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementParameterPresent(PreparedStatement statement, int indexOfParameter)
    {
        if(null == getPreparedStatementParameter(statement, indexOfParameter))
        {
            throw new VerifyFailedException("Prepared statement parameter with index " + indexOfParameter + " not present.");
        }
    }

    /**
     * Verifies that a parameter was added to a <code>PreparedStatement</code> with
     * the specified index. Uses the first <code>PreparedStatement</code> with
     * the specified SQL.
     * @param sql the SQL statement of the <code>PreparedStatement</code>
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementParameterPresent(String sql, int indexOfParameter)
    {
        if(null == getPreparedStatementParameter(sql, indexOfParameter))
        {
            throw new VerifyFailedException("Prepared statement parameter with index " + indexOfParameter + " not present.");
        }
    }
    
    /**
     * Verifies that a parameter was added to a <code>PreparedStatement</code> with
     * the specified index.
     * @param indexOfStatement the index of the statement
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementParameterPresent(int indexOfStatement, int indexOfParameter)
    {
        if(null == getPreparedStatementParameter(indexOfStatement, indexOfParameter))
        {
            throw new VerifyFailedException("Prepared statement parameter with index " + indexOfParameter + " not present.");
        }
    }
    
    /**
     * Verifies that a parameter with the specified index is not present.
     * @param statement the <code>PreparedStatement</code>
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementParameterNotPresent(PreparedStatement statement, int indexOfParameter)
    {
        if(null != getPreparedStatementParameter(statement, indexOfParameter))
        {
            throw new VerifyFailedException("Prepared statement parameter with index " + indexOfParameter + " present.");
        }
    }

    /**
     * Verifies that a parameter with the specified index is not present.
     * Uses the first <code>PreparedStatement</code> with the specified SQL.
     * @param sql the SQL statement of the <code>PreparedStatement</code>
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementParameterNotPresent(String sql, int indexOfParameter)
    {
        if(null != getPreparedStatementParameter(sql, indexOfParameter))
        {
            throw new VerifyFailedException("Prepared statement parameter with index " + indexOfParameter + " present.");
        }
    }

    /**
     * Verifies that a parameter with the specified index is not present.
     * @param indexOfStatement the index of the statement
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementParameterNotPresent(int indexOfStatement, int indexOfParameter)
    {
        if(null != getPreparedStatementParameter(indexOfStatement, indexOfParameter))
        {
            throw new VerifyFailedException("Prepared statement parameter with index " + indexOfParameter + " present.");
        }
    }
    
    /**
     * Verifies that a parameter was added to a <code>CallableStatement</code> with
     * the specified index.
     * @param statement the <code>CallableStatement</code>
     * @param indexOfObject the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameterPresent(CallableStatement statement, int indexOfParameter)
    {
        if(null == getCallableStatementParameter(statement, indexOfParameter))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + indexOfParameter + " not present.");
        }
    }

    /**
     * Verifies that a parameter was added to a <code>CallableStatement</code> with
     * the specified index. Uses the first <code>CallableStatement</code> with
     * the specified SQL.
     * @param sql the SQL statement of the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameterPresent(String sql, int indexOfParameter)
    {
        if(null == getCallableStatementParameter(sql, indexOfParameter))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + indexOfParameter + " not present.");
        }
    }
    
    /**
     * Verifies that a parameter was added to a <code>CallableStatement</code> with
     * the specified index.
     * @param indexOfStatement the index of the statement
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameterPresent(int indexOfStatement, int indexOfParameter)
    {
        if(null == getCallableStatementParameter(indexOfStatement, indexOfParameter))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + indexOfParameter + " not present.");
        }
    }
    
    /**
     * Verifies that a parameter with the specified index is not present.
     * @param statement the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameterNotPresent(CallableStatement statement, int indexOfParameter)
    {
        if(null != getCallableStatementParameter(statement, indexOfParameter))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + indexOfParameter + " present.");
        }
    }

    /**
     * Verifies that a parameter with the specified index is not present.
     * Uses the first <code>CallableStatement</code> with the specified SQL.
     * @param sql the SQL statement of the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameterNotPresent(String sql, int indexOfParameter)
    {
        if(null != getCallableStatementParameter(sql, indexOfParameter))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + indexOfParameter + " present.");
        }
    }

    /**
     * Verifies that a parameter with the specified index is not present.
     * @param indexOfStatement the index of the statement
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameterNotPresent(int indexOfStatement, int indexOfParameter)
    {
        if(null != getCallableStatementParameter(indexOfStatement, indexOfParameter))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + indexOfParameter + " present.");
        }
    }
    
    /**
     * Verifies that a parameter was added to a <code>CallableStatement</code> with
     * the specified index.
     * @param statement the <code>CallableStatement</code>
     * @param indexOfObject the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameterPresent(CallableStatement statement, String nameOfParameter)
    {
        if(null == getCallableStatementParameter(statement, nameOfParameter))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + nameOfParameter + " not present.");
        }
    }

    /**
     * Verifies that a parameter was added to a <code>CallableStatement</code> with
     * the specified index. Uses the first <code>CallableStatement</code> with
     * the specified SQL.
     * @param sql the SQL statement of the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameterPresent(String sql, String nameOfParameter)
    {
        if(null == getCallableStatementParameter(sql, nameOfParameter))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + nameOfParameter + " not present.");
        }
    }
    
    /**
     * Verifies that a parameter was added to a <code>CallableStatement</code> with
     * the specified index.
     * @param indexOfStatement the index of the statement
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameterPresent(int indexOfStatement, String nameOfParameter)
    {
        if(null == getCallableStatementParameter(indexOfStatement, nameOfParameter))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + nameOfParameter + " not present.");
        }
    }
    
    /**
     * Verifies that a parameter with the specified index is not present.
     * @param statement the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameterNotPresent(CallableStatement statement, String nameOfParameter)
    {
        if(null != getCallableStatementParameter(statement, nameOfParameter))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + nameOfParameter + " present.");
        }
    }

    /**
     * Verifies that a parameter with the specified index is not present.
     * Uses the first <code>CallableStatement</code> with the specified SQL.
     * @param sql the SQL statement of the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameterNotPresent(String sql, String nameOfParameter)
    {
        if(null != getCallableStatementParameter(sql, nameOfParameter))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + nameOfParameter + " present.");
        }
    }

    /**
     * Verifies that a parameter with the specified index is not present.
     * @param indexOfStatement the index of the statement
     * @param indexOfParameter the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameterNotPresent(int indexOfStatement, String nameOfParameter)
    {
        if(null != getCallableStatementParameter(indexOfStatement, nameOfParameter))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + nameOfParameter + " present.");
        }
    }
    
    /**
     * Verifies that a parameter from the specified <code>PreparedStatement</code> is equal
     * to the specified object. Please use the corresponding wrapper type for
     * primitive data types.
     * @param statement the <code>PreparedStatement</code>
     * @param indexOfParameter the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementParameter(PreparedStatement statement, int indexOfParameter, Object object)
    {
        verifyPreparedStatementParameterPresent(statement, indexOfParameter);
        Object actualObject = getPreparedStatementParameter(statement, indexOfParameter);
        if(!ParameterUtil.compareParameter(actualObject, object))
        {
            throw new VerifyFailedException("Prepared statement parameter with index " + indexOfParameter + " has the value " +
                                             actualObject.toString() + ", expected " + object.toString());
        }
    }
    
    /**
     * Verifies that a parameter from the <code>PreparedStatement</code> with the
     * specified SQL statement is equal to the specified object.
     * Uses the first <code>PreparedStatement</code> with the specified SQL.
     * Please use the corresponding wrapper type for primitive data types.
     * @param sql the SQL statement of the <code>PreparedStatement</code>
     * @param indexOfParameter the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementParameter(String sql, int indexOfParameter, Object object)
    {
        verifyPreparedStatementParameterPresent(sql, indexOfParameter);
        Object actualObject = getPreparedStatementParameter(sql, indexOfParameter);
        if(!ParameterUtil.compareParameter(actualObject, object))
        {
            throw new VerifyFailedException("Prepared statement parameter with index " + indexOfParameter + " has the value " +
                                             actualObject.toString() + ", expected " + object.toString());
        }
    }
    
    /**
     * Verifies that a parameter from the <code>PreparedStatement</code> with the
     * specified SQL statement is equal to the specified object.
     * Please use the corresponding wrapper type for primitive data types.
     * @param sql the SQL statement of the <code>PreparedStatement</code>
     * @param indexOfParameter the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementParameter(int indexOfStatement, int indexOfParameter, Object object)
    {
        verifyPreparedStatementParameterPresent(indexOfStatement, indexOfParameter);
        Object actualObject = getPreparedStatementParameter(indexOfStatement, indexOfParameter);
        if(!ParameterUtil.compareParameter(actualObject, object))
        {
            throw new VerifyFailedException("Prepared statement parameter with index " + indexOfParameter + " has the value " +
                                             actualObject.toString() + ", expected " + object.toString());
        }
    }
    
    /**
     * Verifies that a parameter from the specified <code>CallableStatement</code> is equal
     * to the specified object. Please use the corresponding wrapper type 
     * for primitive data types.
     * @param statement the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameter(CallableStatement statement, int indexOfParameter, Object object)
    {
        verifyCallableStatementParameterPresent(statement, indexOfParameter);
        Object actualObject = getCallableStatementParameter(statement, indexOfParameter);
        if(!ParameterUtil.compareParameter(actualObject, object))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + indexOfParameter + " has the value " +
                                             actualObject.toString() + ", expected " + object.toString());
        }
    }
    
    /**
     * Verifies that a parameter from the <code>CallableStatement</code> with the
     * specified SQL statement is equal to the specified object.
     * Uses the first <code>CallableStatement</code> with the specified SQL.
     * Please use the corresponding wrapper type for primitive data types.
     * @param sql the SQL statement of the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameter(String sql, int indexOfParameter, Object object)
    {
        verifyCallableStatementParameterPresent(sql, indexOfParameter);
        Object actualObject = getCallableStatementParameter(sql, indexOfParameter);
        if(!ParameterUtil.compareParameter(actualObject, object))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + indexOfParameter + " has the value " +
                                             actualObject.toString() + ", expected " + object.toString());
        }
    }
    
    /**
     * Verifies that a parameter from the <code>CallableStatement</code> with the
     * specified SQL statement is equal to the specified object.
     * Please use the corresponding wrapper type for primitive data types.
     * @param sql the SQL statement of the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameter(int indexOfStatement, int indexOfParameter, Object object)
    {
        verifyCallableStatementParameterPresent(indexOfStatement, indexOfParameter);
        Object actualObject = getCallableStatementParameter(indexOfStatement, indexOfParameter);
        if(!ParameterUtil.compareParameter(actualObject, object))
        {
            throw new VerifyFailedException("Callable statement parameter with index " + indexOfParameter + " has the value " +
                                             actualObject.toString() + ", expected " + object.toString());
        }
    }
    
    /**
     * Verifies that a parameter from the specified <code>CallableStatement</code> is equal
     * to the specified object. Please use the corresponding wrapper type 
     * for primitive data types.
     * @param statement the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameter(CallableStatement statement, String nameOfParameter, Object object)
    {
        verifyCallableStatementParameterPresent(statement, nameOfParameter);
        Object actualObject = getCallableStatementParameter(statement, nameOfParameter);
        if(!ParameterUtil.compareParameter(actualObject, object))
        {
            throw new VerifyFailedException("Callable statement parameter with name " + nameOfParameter + " has the value " +
                                             actualObject.toString() + ", expected " + object.toString());
        }
    }
    
    /**
     * Verifies that a parameter from the <code>CallableStatement</code> with the
     * specified SQL statement is equal to the specified object.
     * Uses the first <code>CallableStatement</code> with the specified SQL.
     * Please use the corresponding wrapper type for primitive data types.
     * @param sql the SQL statement of the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameter(String sql, String nameOfParameter, Object object)
    {
        verifyCallableStatementParameterPresent(sql, nameOfParameter);
        Object actualObject = getCallableStatementParameter(sql, nameOfParameter);
        if(!ParameterUtil.compareParameter(actualObject, object))
        {
            throw new VerifyFailedException("Callable statement parameter with name " + nameOfParameter + " has the value " +
                                             actualObject.toString() + ", expected " + object.toString());
        }
    }
    
    /**
     * Verifies that a parameter from the <code>CallableStatement</code> with the
     * specified SQL statement is equal to the specified object.
     * Please use the corresponding wrapper type for primitive data types.
     * @param sql the SQL statement of the <code>CallableStatement</code>
     * @param indexOfParameter the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementParameter(int indexOfStatement, String nameOfParameter, Object object)
    {
        verifyCallableStatementParameterPresent(indexOfStatement, nameOfParameter);
        Object actualObject = getCallableStatementParameter(indexOfStatement, nameOfParameter);
        if(!ParameterUtil.compareParameter(actualObject, object))
        {
            throw new VerifyFailedException("Callable statement parameter with name " + nameOfParameter + " has the value " +
                                             actualObject.toString() + ", expected " + object.toString());
        }
    }
    
    /**
     * Verifies that an out parameter was registered on the specified
     * <code>CallableStatement</code>.
     * @param statement the <code>CallableStatement</code>
     * @param indexOfParameter the index of the parameter
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementOutParameterRegistered(CallableStatement statement, int indexOfParameter)
    {
        if(!((MockCallableStatement)statement).isOutParameterRegistered(indexOfParameter))
        {
            throw new VerifyFailedException("Out parameter with index " + indexOfParameter +
                                            " not registered in callable statement ");
        }
    }

    /**
     * Verifies that an out parameter was registered on the
     * <code>CallableStatement</code> with the specified SQL.
     * @param sql the SQL statement
     * @param indexOfParameter the index of the parameter
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementOutParameterRegistered(String sql, int indexOfParameter)
    {
        MockCallableStatement statement = getCallableStatement(sql);
        if(null == statement)
        {
            throw new VerifyFailedException("No callable statement " + sql + " present");
        }
        if(!statement.isOutParameterRegistered(indexOfParameter))
        {
            throw new VerifyFailedException("Out parameter with index " + indexOfParameter +
                                            " not registered in callable statement " + sql);
        }
    }

    /**
     * Verifies that an out parameter was registered on the
     * <code>CallableStatement</code> with the specified index.
     * @param indexOfStatement the index of the <code>CallableStatement</code>
     * @param indexOfParameter the index of the parameter
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementOutParameterRegistered(int indexOfStatement, int indexOfParameter)
    {
        MockCallableStatement statement = getCallableStatement(indexOfStatement);
        if(null == statement)
        {
            throw new VerifyFailedException("No callable statement with index " + indexOfStatement + " present");
        }
        if(!statement.isOutParameterRegistered(indexOfParameter))
        {
            throw new VerifyFailedException("Out parameter with index " + indexOfParameter +
                                            " not registered in callable statement with index " + indexOfStatement);
        }
    }
    
    /**
     * Verifies that an out parameter was registered on the specified
     * <code>CallableStatement</code>.
     * @param statement the <code>CallableStatement</code>
     * @param nameOfParameter the name of the parameter
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementOutParameterRegistered(CallableStatement statement, String nameOfParameter)
    {
        if(!((MockCallableStatement)statement).isOutParameterRegistered(nameOfParameter))
        {
            throw new VerifyFailedException("Out parameter with name " + nameOfParameter +
                                            " not registered in callable statement ");
        }
    }
    
    /**
     * Verifies that an out parameter was registered on the
     * <code>CallableStatement</code> with the specified SQL.
     * @param sql the SQL statement
     * @param nameOfParameter the name of the parameter
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementOutParameterRegistered(String sql, String nameOfParameter)
    {
        MockCallableStatement statement = getCallableStatement(sql);
        if(null == statement)
        {
            throw new VerifyFailedException("No callable statement " + sql + " present");
        }
        if(!statement.isOutParameterRegistered(nameOfParameter))
        {
            throw new VerifyFailedException("Out parameter with name " + nameOfParameter +
                                            " not registered in callable statement " + sql);
        }
    }
    
    /**
     * Verifies that an out parameter was registered on the
     * <code>CallableStatement</code> with the specified index.
     * @param indexOfStatement the index of the <code>CallableStatement</code>
     * @param nameOfParameter the name of the parameter
     * @throws VerifyFailedException if verification fails
     */
    public void verifyCallableStatementOutParameterRegistered(int indexOfStatement, String nameOfParameter)
    {
        MockCallableStatement statement = getCallableStatement(indexOfStatement);
        if(null == statement)
            {
                throw new VerifyFailedException("No callable statement with index " + indexOfStatement + " present");
            }
        if(!statement.isOutParameterRegistered(nameOfParameter))
        {
            throw new VerifyFailedException("Out parameter with name " + nameOfParameter +
                                            " not registered in callable statement with index " + indexOfStatement);
        }
    }
    
    /**
     * Verifies that a <code>Savepoint</code> with the specified index
     * is present. The index is the number of the created <code>Savepoint</code>
     * starting with 0 for the first <code>Savepoint</code>.
     * @param index the index of the <code>Savepoint</code>
     */
    public void verifySavepointPresent(int index)
    {
        MockSavepoint savepoint = getSavepoint(index);
        if(null == savepoint)
        {
            throw new VerifyFailedException("No savepoint with index " + index + " present.");
        }
    }
    
    /**
     * Verifies that a <code>Savepoint</code> with the specified name
     * is present.
     * @param name the name of the <code>Savepoint</code>
     */
    public void verifySavepointPresent(String name)
    {
        MockSavepoint savepoint = getSavepoint(name);
        if(null == savepoint)
        {
            throw new VerifyFailedException("No savepoint with name " + name + " present.");
        }
    }
    
    /**
     * Verifies that the <code>Savepoint</code> with the specified index
     * is released. The index is the number of the created <code>Savepoint</code>
     * starting with 0 for the first <code>Savepoint</code>.
     * @param index the index of the <code>Savepoint</code>
     */
    public void verifySavepointReleased(int index)
    {
        verifySavepointPresent(index);
        if(!getSavepoint(index).isReleased())
        {
            throw new VerifyFailedException("Savepoint with index " + index + " not released.");
        }
    }
    
    /**
     * Verifies that the <code>Savepoint</code> with the specified name
     * is released.
     * @param name the name of the <code>Savepoint</code>
     */
    public void verifySavepointReleased(String name)
    {
        verifySavepointPresent(name);
        if(!getSavepoint(name).isReleased())
        {
            throw new VerifyFailedException("Savepoint with name " + name + " not released.");
        }
    }
    
    /**
     * Verifies that the <code>Savepoint</code> with the specified index
     * is not released. The index is the number of the created <code>Savepoint</code>
     * starting with 0 for the first <code>Savepoint</code>.
     * @param index the index of the <code>Savepoint</code>
     */
    public void verifySavepointNotReleased(int index)
    {
        verifySavepointPresent(index);
        if(getSavepoint(index).isReleased())
        {
            throw new VerifyFailedException("Savepoint with index " + index + " is released.");
        }
    }

    /**
     * Verifies that the <code>Savepoint</code> with the specified name
     * is not released.
     * @param name the name of the <code>Savepoint</code>
     */
    public void verifySavepointNotReleased(String name)
    {
        verifySavepointPresent(name);
        if(getSavepoint(name).isReleased())
        {
            throw new VerifyFailedException("Savepoint with name " + name + " is released.");
        }
    }
    
    /**
     * Verifies that the <code>Savepoint</code> with the specified index
     * is rollbacked. The index is the number of the created <code>Savepoint</code>
     * starting with 0 for the first <code>Savepoint</code>.
     * @param index the index of the <code>Savepoint</code>
     */
    public void verifySavepointRollbacked(int index)
    {
        verifySavepointPresent(index);
        if(!getSavepoint(index).isRollbacked())
        {
            throw new VerifyFailedException("Savepoint with index " + index + " not rollbacked.");
        }
    }

    /**
     * Verifies that the <code>Savepoint</code> with the specified name
     * is rollbacked.
     * @param name the name of the <code>Savepoint</code>
     */
    public void verifySavepointRollbacked(String name)
    {
        verifySavepointPresent(name);
        if(!getSavepoint(name).isRollbacked())
        {
            throw new VerifyFailedException("Savepoint with name " + name + " not rollbacked.");
        }
    }

    /**
     * Verifies that the <code>Savepoint</code> with the specified index
     * is not rollbacked. The index is the number of the created <code>Savepoint</code>
     * starting with 0 for the first <code>Savepoint</code>.
     * @param index the index of the <code>Savepoint</code>
     */
    public void verifySavepointNotRollbacked(int index)
    {
        verifySavepointPresent(index);
        if(getSavepoint(index).isRollbacked())
        {
            throw new VerifyFailedException("Savepoint with index " + index + " is rollbacked.");
        }
    }

    /**
     * Verifies that the <code>Savepoint</code> with the specified name
     * is not rollbacked.
     * @param name the name of the <code>Savepoint</code>
     */
    public void verifySavepointNotRollbacked(String name)
    {
        verifySavepointPresent(name);
        if(getSavepoint(name).isRollbacked())
        {
            throw new VerifyFailedException("Savepoint with name " + name + " is rollbacked.");
        }
    }
}
