package com.mockrunner.jdbc;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.List;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.jdbc.MockCallableStatement;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockResultSet;
//import com.mockrunner.mock.jdbc.MockSavepoint;
import com.mockrunner.mock.jdbc.MockStatement;

/**
 * Delegator for {@link JDBCTestModule}. You can
 * subclass this adapter or use {@link JDBCTestModule}
 * directly (so your test case can use another base
 * class).
 */
public class JDBCTestCaseAdapter extends BaseTestCase
{
    private JDBCTestModule jdbcTestModule;
    
    public JDBCTestCaseAdapter()
    {

    }

    public JDBCTestCaseAdapter(String arg0)
    {
        super(arg0);
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        jdbcTestModule = null;
    }

    /**
     * Creates the <code>JDBCTestModule</code>. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        jdbcTestModule = createJDBCTestModule(getJDBCMockObjectFactory());
    }

    /**
     * Gets the <code>JDBCTestModule</code>. 
     * @return the <code>JDBCTestModule</code>
     */
    protected JDBCTestModule getJDBCTestModule()
    {
        return jdbcTestModule;
    }

    /**
     * Sets the <code>JDBCTestModule</code>. 
     * @param jdbcTestModule the <code>JDBCTestModule</code>
     */
    protected void setJDBCTestModule(JDBCTestModule jdbcTestModule)
    {
        this.jdbcTestModule = jdbcTestModule;
    }
    
    /**
     * Delegates to {@link JDBCTestModule#setCaseSensitive}
     */
    protected void setCaseSensitive(boolean caseSensitive)
    {
        jdbcTestModule.setCaseSensitive(caseSensitive);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#setExactMatch}
     */
    protected void setExactMatch(boolean exactMatch)
    {
        jdbcTestModule.setExactMatch(exactMatch);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getStatementResultSetHandler}
     */
    protected StatementResultSetHandler getStatementResultSetHandler()
    {
        return jdbcTestModule.getStatementResultSetHandler();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getPreparedStatementResultSetHandler}
     */
    protected PreparedStatementResultSetHandler getPreparedStatementResultSetHandler()
    {
        return jdbcTestModule.getPreparedStatementResultSetHandler();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getCallableStatementResultSetHandler}
     */
    protected CallableStatementResultSetHandler getCallableStatementResultSetHandler()
    {
        return jdbcTestModule.getCallableStatementResultSetHandler();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getStatement}
     */
    protected MockStatement getStatement(int index)
    {
        return jdbcTestModule.getStatement(index);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getStatements}
     */
    protected List getStatements()
    {
        return jdbcTestModule.getStatements();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getExecutedSQLStatements}
     */
    protected List getExecutedSQLStatements()
    {
        return jdbcTestModule.getExecutedSQLStatements();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getReturnedResultSet}
     */
    protected MockResultSet getReturnedResultSet(String id)
    {
        return jdbcTestModule.getReturnedResultSet(id);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getReturnedResultSets(String)}
     */    
    protected List getReturnedResultSets(String id)
    {
        return jdbcTestModule.getReturnedResultSets(id);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getReturnedResultSets}
     */
    protected List getReturnedResultSets()
    {
        return jdbcTestModule.getReturnedResultSets();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getPreparedStatement(int)}
     */
    protected MockPreparedStatement getPreparedStatement(int index)
    {
        return jdbcTestModule.getPreparedStatement(index);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getPreparedStatement(String)}
     */
    protected MockPreparedStatement getPreparedStatement(String sql)
    {
        return jdbcTestModule.getPreparedStatement(sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getPreparedStatements}
     */
    protected List getPreparedStatements()
    {
        return jdbcTestModule.getPreparedStatements();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getPreparedStatements(String)}
     */
    protected List getPreparedStatements(String sql)
    {
        return jdbcTestModule.getPreparedStatements(sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getCallableStatement(int)}
     */
    protected MockCallableStatement getCallableStatement(int index)
    {
        return jdbcTestModule.getCallableStatement(index);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getCallableStatement(String)}
     */
    protected MockCallableStatement getCallableStatement(String sql)
    {
        return jdbcTestModule.getCallableStatement(sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getCallableStatements()}
     */
    protected List getCallableStatements()
    {
        return jdbcTestModule.getCallableStatements();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getCallableStatements(String)}
     */
    protected List getCallableStatements(String sql)
    {
        return jdbcTestModule.getCallableStatements(sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getPreparedStatementParameter(PreparedStatement, int)}
     */
    protected Object getPreparedStatementParameter(PreparedStatement statement, int indexOfParameter)
    {
        return jdbcTestModule.getPreparedStatementParameter(statement, indexOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getPreparedStatementParameter(String, int)}
     */
    protected Object getPreparedStatementParameter(String sql, int indexOfParameter)
    {
        return jdbcTestModule.getPreparedStatementParameter(sql, indexOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getPreparedStatementParameter(int, int)}
     */
    protected Object getPreparedStatementParameter(int indexOfStatement, int indexOfParameter)
    {
        return jdbcTestModule.getPreparedStatementParameter(indexOfStatement, indexOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getCallableStatementParameter(CallableStatement, int)}
     */
    protected Object getCallableStatementParameter(CallableStatement statement, int indexOfParameter)
    {
        return jdbcTestModule.getCallableStatementParameter(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#getCallableStatementParameter(String, int)}
     */
    protected Object getCallableStatementParameter(String sql, int indexOfParameter)
    {
        return jdbcTestModule.getCallableStatementParameter(sql, indexOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#getCallableStatementParameter(CallableStatement, String)}
     */
    protected Object getCallableStatementParameter(CallableStatement statement, String nameOfParameter)
    {
        return jdbcTestModule.getCallableStatementParameter(statement, nameOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#getCallableStatementParameter(String, String)}
     */
    protected Object getCallableStatementParameter(String sql, String nameOfParameter)
    {
        return jdbcTestModule.getCallableStatementParameter(sql, nameOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#getCallableStatementParameter(int, String)}
     */
    protected Object getCallableStatementParameter(int indexOfStatement, String nameOfParameter)
    {
        return jdbcTestModule.getCallableStatementParameter(indexOfStatement, nameOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#getCallableStatementParameter(int, int)}
     */
    protected Object getCallableStatementParameter(int indexOfStatement, int indexOfParameter)
    {
        return jdbcTestModule.getCallableStatementParameter(indexOfStatement, indexOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getSavepoints}
     */
    /*protected List getSavepoints()
    {
        return jdbcTestModule.getSavepoints();
    }*/
    
    /**
     * Delegates to {@link JDBCTestModule#getSavepoint(int)}
     */ 
    /*protected MockSavepoint getSavepoint(int index)
    {   
        return jdbcTestModule.getSavepoint(index);
    }*/
    
    /**
     * Delegates to {@link JDBCTestModule#getSavepoint(String)}
     */
    /*protected MockSavepoint getSavepoint(String name)
    {
        return jdbcTestModule.getSavepoint(name);
    }*/
    
    /**
     * Delegates to {@link JDBCTestModule#verifySQLStatementExecuted}
     */
    protected void verifySQLStatementExecuted(String sql)
    {
        jdbcTestModule.verifySQLStatementExecuted(sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifySQLStatementNotExecuted}
     */
    protected void verifySQLStatementNotExecuted(String sql)
    {
        jdbcTestModule.verifySQLStatementNotExecuted(sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyConnectionClosed}
     */
    protected void verifyConnectionClosed()
    {
        jdbcTestModule.verifyConnectionClosed();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyAllStatementsClosed}
     */
    protected void verifyAllStatementsClosed()
    {
        jdbcTestModule.verifyAllStatementsClosed();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetClosed}
     */
    protected void verifyResultSetClosed(String id)
    {
        jdbcTestModule.verifyResultSetClosed(id);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRowInserted(MockResultSet, int)}
     */
    protected void verifyResultSetRowInserted(MockResultSet resultSet, int number)
    {
        jdbcTestModule.verifyResultSetRowInserted(resultSet, number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRowInserted(String, int)}
     */
    protected void verifyResultSetRowInserted(String id, int number)
    {
        jdbcTestModule.verifyResultSetRowInserted(id, number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRowNotInserted(MockResultSet, int)}
     */
    protected void verifyResultSetRowNotInserted(MockResultSet resultSet, int number)
    {
        jdbcTestModule.verifyResultSetRowNotInserted(resultSet, number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRowNotInserted(String, int)}
     */
    protected void verifyResultSetRowNotInserted(String id, int number)
    {
        jdbcTestModule.verifyResultSetRowNotInserted(id, number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRowUpdated(MockResultSet, int)}
     */
    protected void verifyResultSetRowUpdated(MockResultSet resultSet, int number)
    {
        jdbcTestModule.verifyResultSetRowUpdated(resultSet, number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRowUpdated(String, int)}
     */
    protected void verifyResultSetRowUpdated(String id, int number)
    {
        jdbcTestModule.verifyResultSetRowUpdated(id, number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRowNotUpdated(MockResultSet, int)}
     */
    protected void verifyResultSetRowNotUpdated(MockResultSet resultSet, int number)
    {
        jdbcTestModule.verifyResultSetRowNotUpdated(resultSet, number);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRowNotUpdated(String, int)}
     */
    protected void verifyResultSetRowNotUpdated(String id, int number)
    {
        jdbcTestModule.verifyResultSetRowNotUpdated(id, number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRowDeleted(MockResultSet, int)}
     */
    protected void verifyResultSetRowDeleted(MockResultSet resultSet, int number)
    {
        jdbcTestModule.verifyResultSetRowDeleted(resultSet, number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRowDeleted(String, int)}
     */
    protected void verifyResultSetRowDeleted(String id, int number)
    {
        jdbcTestModule.verifyResultSetRowDeleted(id, number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRowNotDeleted(MockResultSet, int)}
     */
    protected void verifyResultSetRowNotDeleted(MockResultSet resultSet, int number)
    {
        jdbcTestModule.verifyResultSetRowNotDeleted(resultSet, number);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRowNotDeleted(String, int)}
     */
    protected void verifyResultSetRowNotDeleted(String id, int number)
    {
        jdbcTestModule.verifyResultSetRowNotDeleted(id, number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyAllResultSetsClosed}
     */
    protected void verifyAllResultSetsClosed()
    {
        jdbcTestModule.verifyAllResultSetsClosed();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCommitted}
     */
    protected void verifyCommitted()
    {
        jdbcTestModule.verifyCommitted();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCommited}
     * @deprecated use {@link #verifyCommitted}
     */
    protected void verifyCommited()
    {
        jdbcTestModule.verifyCommited();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyNotCommitted}
     */
    protected void verifyNotCommitted()
    {
        jdbcTestModule.verifyNotCommitted();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyNotCommited}
     * @deprecated use {@link #verifyNotCommitted}
     */
    protected void verifyNotCommited()
    {
        jdbcTestModule.verifyNotCommited();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyRolledBack}
     */
    protected void verifyRolledBack()
    {
        jdbcTestModule.verifyRolledBack();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyNotRolledBack}
     */
    protected void verifyNotRolledBack()
    {
        jdbcTestModule.verifyNotRolledBack();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyNumberCommits}
     */
    protected void verifyNumberCommits(int number)
    {
        jdbcTestModule.verifyNumberCommits(number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyNumberRollbacks}
     */
    protected void verifyNumberRollbacks(int number)
    {
        jdbcTestModule.verifyNumberRollbacks(number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyNumberStatements}
     */
    protected void verifyNumberStatements(int number)
    {
        jdbcTestModule.verifyNumberStatements(number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyNumberPreparedStatements(int)}
     */
    protected void verifyNumberPreparedStatements(int number)
    {
        jdbcTestModule.verifyNumberPreparedStatements(number);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyNumberPreparedStatements(int, String)}
     */
    protected void verifyNumberPreparedStatements(int number, String sql)
    {
        jdbcTestModule.verifyNumberPreparedStatements(number, sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyNumberCallableStatements(int)}
     */
    protected void verifyNumberCallableStatements(int number)
    {
        jdbcTestModule.verifyNumberCallableStatements(number);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyNumberCallableStatements(int, String)}
     */
    protected void verifyNumberCallableStatements(int number, String sql)
    {
        jdbcTestModule.verifyNumberCallableStatements(number, sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyStatementClosed}
     */
    protected void verifyStatementClosed(int index)
    {
        jdbcTestModule.verifyStatementClosed(index);
    }   
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementClosed(int)}
     */
    protected void verifyPreparedStatementClosed(int index)
    {
        jdbcTestModule.verifyPreparedStatementClosed(index);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementClosed(String)}
     */
    protected void verifyPreparedStatementClosed(String sql)
    {
        jdbcTestModule.verifyPreparedStatementClosed(sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementClosed(int)}
     */
    protected void verifyCallableStatementClosed(int index)
    {
        jdbcTestModule.verifyCallableStatementClosed(index);
    }
  
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementClosed(String)}
     */
    protected void verifyCallableStatementClosed(String sql)
    {
        jdbcTestModule.verifyCallableStatementClosed(sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRow(MockResultSet, int, List)}
     */
    protected void verifyResultSetRow(MockResultSet resultSet, int number, List rowData)
    {
        jdbcTestModule.verifyResultSetRow(resultSet, number, rowData);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRow(MockResultSet, int, Object[])}
     */
    protected void verifyResultSetRow(MockResultSet resultSet, int number, Object[] rowData)
    {
        jdbcTestModule.verifyResultSetRow(resultSet, number, rowData);
    }
  
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRow(String, int, List)}
     */
    protected void verifyResultSetRow(String id, int number, List rowData)
    {
        jdbcTestModule.verifyResultSetRow(id, number, rowData);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetRow(String, int, Object[])}
     */
    protected void verifyResultSetRow(String id, int number, Object[] rowData)
    {
        jdbcTestModule.verifyResultSetRow(id, number, rowData);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetColumn(MockResultSet, int, List)}
     */
    protected void verifyResultSetColumn(MockResultSet resultSet, int number, List columnData)
    {
        jdbcTestModule.verifyResultSetColumn(resultSet, number, columnData);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetColumn(MockResultSet, int, Object[])}
     */
    protected void verifyResultSetColumn(MockResultSet resultSet, int number, Object[] columnData)
    {
        jdbcTestModule.verifyResultSetColumn(resultSet, number, columnData);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetColumn(String, int, List)}
     */
    protected void verifyResultSetColumn(String id, int number, List columnData)
    {
        jdbcTestModule.verifyResultSetColumn(id, number, columnData);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetColumn(String, int, Object[])}
     */
    protected void verifyResultSetColumn(String id, int number, Object[] columnData)
    {
        jdbcTestModule.verifyResultSetColumn(id, number, columnData);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetColumn(MockResultSet, int, List)}
     */
    protected void verifyResultSetColumn(MockResultSet resultSet, String name, List columnData)
    {
        jdbcTestModule.verifyResultSetColumn(resultSet, name, columnData);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetColumn(MockResultSet, int, Object[])}
     */
    protected void verifyResultSetColumn(MockResultSet resultSet, String name, Object[] columnData)
    {
        jdbcTestModule.verifyResultSetColumn(resultSet, name, columnData);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetColumn(String, int, List)}
     */
    protected void verifyResultSetColumn(String id, String name, List columnData)
    {
        jdbcTestModule.verifyResultSetColumn(id, name, columnData);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetColumn(String, int, Object[])}
     */
    protected void verifyResultSetColumn(String id, String name, Object[] columnData)
    {
        jdbcTestModule.verifyResultSetColumn(id, name, columnData);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetEquals(MockResultSet, MockResultSet)}
     */
    protected void verifyResultSetEquals(MockResultSet source, MockResultSet target)
    {
        jdbcTestModule.verifyResultSetEquals(source, target);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyResultSetEquals(String, MockResultSet)}
     */
    protected void verifyResultSetEquals(String id, MockResultSet target)
    {
        jdbcTestModule.verifyResultSetEquals(id, target);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementPresent}
     */
    protected void verifyPreparedStatementPresent(String sql)
    {
        jdbcTestModule.verifyPreparedStatementPresent(sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementNotPresent}
     */
    protected void verifyPreparedStatementNotPresent(String sql)
    {
        jdbcTestModule.verifyPreparedStatementNotPresent(sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementPresent}
     */
    protected void verifyCallableStatementPresent(String sql)
    {
        jdbcTestModule.verifyCallableStatementPresent(sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementNotPresent}
     */   
    protected void verifyCallableStatementNotPresent(String sql)
    {
        jdbcTestModule.verifyCallableStatementNotPresent(sql);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementParameterPresent(PreparedStatement, int)}
     */
    protected void verifyPreparedStatementParameterPresent(PreparedStatement statement, int indexOfParameter)
    {
        jdbcTestModule.verifyPreparedStatementParameterPresent(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementParameterPresent(String, int)}
     */
    protected void verifyPreparedStatementParameterPresent(String sql, int indexOfParameter)
    {
        jdbcTestModule.verifyPreparedStatementParameterPresent(sql, indexOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementParameterPresent(int, int)}
     */
    protected void verifyPreparedStatementParameterPresent(int indexOfStatement, int indexOfParameter)
    {
        jdbcTestModule.verifyPreparedStatementParameterPresent(indexOfStatement, indexOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementParameterNotPresent(PreparedStatement, int)}
     */
    protected void verifyPreparedStatementParameterNotPresent(PreparedStatement statement, int indexOfParameter)
    {
        jdbcTestModule.verifyPreparedStatementParameterNotPresent(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementParameterNotPresent(String, int)}
     */
    protected void verifyPreparedStatementParameterNotPresent(String sql, int indexOfParameter)
    {
        jdbcTestModule.verifyPreparedStatementParameterNotPresent(sql, indexOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementParameterNotPresent(int, int)}
     */
    protected void verifyPreparedStatementParameterNotPresent(int indexOfStatement, int indexOfParameter)
    {
        jdbcTestModule.verifyPreparedStatementParameterNotPresent(indexOfStatement, indexOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameterPresent(CallableStatement, int)}
     */
    protected void verifyCallableStatementParameterPresent(CallableStatement statement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameterPresent(String, int)}
     */
    protected void verifyCallableStatementParameterPresent(String sql, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(sql, indexOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameterPresent(int, int)}
     */
    protected void verifyCallableStatementParameterPresent(int indexOfStatement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(indexOfStatement, indexOfParameter);
    }
       
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameterNotPresent(CallableStatement, int)}
     */ 
    protected void verifyCallableStatementParameterNotPresent(CallableStatement statement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameterNotPresent(String, int)}
     */ 
    protected void verifyCallableStatementParameterNotPresent(String sql, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(sql, indexOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameterNotPresent(int, int)}
     */
    protected void verifyCallableStatementParameterNotPresent(int indexOfStatement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(indexOfStatement, indexOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameterPresent(CallableStatement, String)}
     */
    protected void verifyCallableStatementParameterPresent(CallableStatement statement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(statement, nameOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameterPresent(String, String)}
     */
    protected void verifyCallableStatementParameterPresent(String sql, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(sql, nameOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameterPresent(int, String)}
     */
    protected void verifyCallableStatementParameterPresent(int indexOfStatement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(indexOfStatement, nameOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameterNotPresent(CallableStatement, String)}
     */
    protected void verifyCallableStatementParameterNotPresent(CallableStatement statement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(statement, nameOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameterNotPresent(String, String)}
     */
    protected void verifyCallableStatementParameterNotPresent(String sql, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(sql, nameOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameterNotPresent(int, String)}
     */
    protected void verifyCallableStatementParameterNotPresent(int indexOfStatement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(indexOfStatement, nameOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementParameter(PreparedStatement, int, Object)}
     */
    protected void verifyPreparedStatementParameter(PreparedStatement statement, int indexOfParameter, Object object)
    {
        jdbcTestModule.verifyPreparedStatementParameter(statement, indexOfParameter, object);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementParameter(String, int, Object)}
     */
    protected void verifyPreparedStatementParameter(String sql, int indexOfParameter, Object object)
    {
        jdbcTestModule.verifyPreparedStatementParameter(sql, indexOfParameter, object);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementParameter(int, int, Object)}
     */
    protected void verifyPreparedStatementParameter(int indexOfStatement, int indexOfParameter, Object object)
    {
        jdbcTestModule.verifyPreparedStatementParameter(indexOfStatement, indexOfParameter, object);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameter(CallableStatement, int, Object)}
     */
    protected void verifyCallableStatementParameter(CallableStatement statement, int indexOfParameter, Object object)
    {
        jdbcTestModule.verifyCallableStatementParameter(statement, indexOfParameter, object);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameter(String, int, Object)}
     */
    protected void verifyCallableStatementParameter(String sql, int indexOfParameter, Object object)
    {
        jdbcTestModule.verifyCallableStatementParameter(sql, indexOfParameter, object);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameter(int, int, Object)}
     */
    protected void verifyCallableStatementParameter(int indexOfStatement, int indexOfParameter, Object object)
    {
        jdbcTestModule.verifyCallableStatementParameter(indexOfStatement, indexOfParameter, object);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameter(CallableStatement, String, Object)}
     */
    protected void verifyCallableStatementParameter(CallableStatement statement, String nameOfParameter, Object object)
    {
        jdbcTestModule.verifyCallableStatementParameter(statement, nameOfParameter, object);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameter(String, String, Object)}
     */
    protected void verifyCallableStatementParameter(String sql, String nameOfParameter, Object object)
    {
        jdbcTestModule.verifyCallableStatementParameter(sql, nameOfParameter, object);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementParameter(int, String, Object)}
     */
    protected void verifyCallableStatementParameter(int indexOfStatement, String nameOfParameter, Object object)
    {
        jdbcTestModule.verifyCallableStatementParameter(indexOfStatement, nameOfParameter, object);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementOutParameterRegistered(CallableStatement, int)}
     */
    protected void verifyCallableStatementOutParameterRegistered(CallableStatement statement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementOutParameterRegistered(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementOutParameterRegistered(String, int)}
     */
    protected void verifyCallableStatementOutParameterRegistered(String sql, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementOutParameterRegistered(sql, indexOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementOutParameterRegistered(int, int)}
     */
    protected void verifyCallableStatementOutParameterRegistered(int indexOfStatement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementOutParameterRegistered(indexOfStatement, indexOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementOutParameterRegistered(CallableStatement, String)}
     */
    protected void verifyCallableStatementOutParameterRegistered(CallableStatement statement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementOutParameterRegistered(statement, nameOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementOutParameterRegistered(String, String)}
     */
    protected void verifyCallableStatementOutParameterRegistered(String sql, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementOutParameterRegistered(sql, nameOfParameter);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyCallableStatementOutParameterRegistered(int, String)}
     */
    protected void verifyCallableStatementOutParameterRegistered(int indexOfStatement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementOutParameterRegistered(indexOfStatement, nameOfParameter);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifySavepointPresent(int)}
     */
    /*protected void verifySavepointPresent(int index)
    {
        jdbcTestModule.verifySavepointPresent(index);
    }*/
    
    /**
     * Delegates to {@link JDBCTestModule#verifySavepointPresent(String)}
     */   
    /*protected void verifySavepointPresent(String name)
    {
        jdbcTestModule.verifySavepointPresent(name);
    }*/
    
    /**
     * Delegates to {@link JDBCTestModule#verifySavepointReleased(int)}
     */
    /*protected void verifySavepointReleased(int index)
    {
        jdbcTestModule.verifySavepointReleased(index);
    }*/
    
    /**
     * Delegates to {@link JDBCTestModule#verifySavepointReleased(String)}
     */      
    /*protected void verifySavepointReleased(String name)
    {
        jdbcTestModule.verifySavepointReleased(name);
    }*/
  
    /**
     * Delegates to {@link JDBCTestModule#verifySavepointNotReleased(int)}
     */
    /*protected void verifySavepointNotReleased(int index)
    {
        jdbcTestModule.verifySavepointNotReleased(index);
    }*/

    /**
     * Delegates to {@link JDBCTestModule#verifySavepointNotReleased(String)}
     */
    /*protected void verifySavepointNotReleased(String name)
    {
        jdbcTestModule.verifySavepointNotReleased(name);
    }*/
    
    /**
     * Delegates to {@link JDBCTestModule#verifySavepointRollbacked(int)}
     */
    /*protected void verifySavepointRollbacked(int index)
    {
        jdbcTestModule.verifySavepointRollbacked(index);
    }*/

    /**
     * Delegates to {@link JDBCTestModule#verifySavepointRollbacked(String)}
     */
    /*protected void verifySavepointRollbacked(String name)
    {
        jdbcTestModule.verifySavepointRollbacked(name);
    }*/

    /**
     * Delegates to {@link JDBCTestModule#verifySavepointNotRollbacked(int)}
     */
    /*protected void verifySavepointNotRollbacked(int index)
    {
        jdbcTestModule.verifySavepointNotRollbacked(index);
    }*/

    /**
     * Delegates to {@link JDBCTestModule#verifySavepointNotRollbacked(String)}
     */
    /*protected void verifySavepointNotRollbacked(String name)
    {
        jdbcTestModule.verifySavepointNotRollbacked(name);
    }*/
}
