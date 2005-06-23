package com.mockrunner.jdbc;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.jdbc.MockCallableStatement;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.MockSavepoint;
import com.mockrunner.mock.jdbc.MockStatement;

/**
 * Delegator for {@link com.mockrunner.jdbc.JDBCTestModule}. You can
 * subclass this adapter or use {@link com.mockrunner.jdbc.JDBCTestModule}
 * directly (so your test case can use another base class).
 * This adapter extends {@link com.mockrunner.base.BaseTestCase}.
 * It can be used if you want to use several modules in conjunction.
 * <b>This class is generated from the {@link com.mockrunner.jdbc.JDBCTestModule}
 * and should not be edited directly</b>.
 */
public class JDBCTestCaseAdapter extends BaseTestCase
{
    private JDBCTestModule jdbcTestModule;

    public JDBCTestCaseAdapter()
    {

    }

    public JDBCTestCaseAdapter(String name)
    {
        super(name);
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        jdbcTestModule = null;
    }

    /**
     * Creates the {@link com.mockrunner.jdbc.JDBCTestModule}. If you
     * overwrite this method, you must call <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        jdbcTestModule = createJDBCTestModule(getJDBCMockObjectFactory());
    }

    /**
     * Gets the {@link com.mockrunner.jdbc.JDBCTestModule}.
     * @return the {@link com.mockrunner.jdbc.JDBCTestModule}
     */
    protected JDBCTestModule getJDBCTestModule()
    {
        return jdbcTestModule;
    }

    /**
     * Sets the {@link com.mockrunner.jdbc.JDBCTestModule}.
     * @param jdbcTestModule the {@link com.mockrunner.jdbc.JDBCTestModule}
     */
    protected void setJDBCTestModule(JDBCTestModule jdbcTestModule)
    {
        this.jdbcTestModule = jdbcTestModule;
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#setCaseSensitive(boolean)}
     */
    protected void setCaseSensitive(boolean caseSensitive)
    {
        jdbcTestModule.setCaseSensitive(caseSensitive);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCommitted}
     */
    protected void verifyCommitted()
    {
        jdbcTestModule.verifyCommitted();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyNotCommitted}
     */
    protected void verifyNotCommitted()
    {
        jdbcTestModule.verifyNotCommitted();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyRolledBack}
     */
    protected void verifyRolledBack()
    {
        jdbcTestModule.verifyRolledBack();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyNotRolledBack}
     */
    protected void verifyNotRolledBack()
    {
        jdbcTestModule.verifyNotRolledBack();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#setExactMatch(boolean)}
     */
    protected void setExactMatch(boolean exactMatch)
    {
        jdbcTestModule.setExactMatch(exactMatch);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#setUseRegularExpressions(boolean)}
     */
    protected void setUseRegularExpressions(boolean useRegularExpressions)
    {
        jdbcTestModule.setUseRegularExpressions(useRegularExpressions);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getStatementResultSetHandler}
     */
    protected StatementResultSetHandler getStatementResultSetHandler()
    {
        return jdbcTestModule.getStatementResultSetHandler();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getPreparedStatementResultSetHandler}
     */
    protected PreparedStatementResultSetHandler getPreparedStatementResultSetHandler()
    {
        return jdbcTestModule.getPreparedStatementResultSetHandler();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getCallableStatementResultSetHandler}
     */
    protected CallableStatementResultSetHandler getCallableStatementResultSetHandler()
    {
        return jdbcTestModule.getCallableStatementResultSetHandler();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getStatement(int)}
     */
    protected MockStatement getStatement(int index)
    {
        return jdbcTestModule.getStatement(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getStatements}
     */
    protected List getStatements()
    {
        return jdbcTestModule.getStatements();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getExecutedSQLStatements}
     */
    protected List getExecutedSQLStatements()
    {
        return jdbcTestModule.getExecutedSQLStatements();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getExecutedSQLStatementParameter}
     */
    protected Map getExecutedSQLStatementParameter()
    {
        return jdbcTestModule.getExecutedSQLStatementParameter();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getExecutedSQLStatementParameterSets(String)}
     */
    protected ParameterSets getExecutedSQLStatementParameterSets(String sql)
    {
        return jdbcTestModule.getExecutedSQLStatementParameterSets(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getReturnedResultSet(String)}
     */
    protected MockResultSet getReturnedResultSet(String id)
    {
        return jdbcTestModule.getReturnedResultSet(id);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getReturnedResultSets(String)}
     */
    protected List getReturnedResultSets(String id)
    {
        return jdbcTestModule.getReturnedResultSets(id);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getReturnedResultSets}
     */
    protected List getReturnedResultSets()
    {
        return jdbcTestModule.getReturnedResultSets();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getPreparedStatement(String)}
     */
    protected MockPreparedStatement getPreparedStatement(String sql)
    {
        return jdbcTestModule.getPreparedStatement(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getPreparedStatement(int)}
     */
    protected MockPreparedStatement getPreparedStatement(int index)
    {
        return jdbcTestModule.getPreparedStatement(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getPreparedStatements}
     */
    protected List getPreparedStatements()
    {
        return jdbcTestModule.getPreparedStatements();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getPreparedStatements(String)}
     */
    protected List getPreparedStatements(String sql)
    {
        return jdbcTestModule.getPreparedStatements(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getCallableStatement(String)}
     */
    protected MockCallableStatement getCallableStatement(String sql)
    {
        return jdbcTestModule.getCallableStatement(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getCallableStatement(int)}
     */
    protected MockCallableStatement getCallableStatement(int index)
    {
        return jdbcTestModule.getCallableStatement(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getCallableStatements(String)}
     */
    protected List getCallableStatements(String sql)
    {
        return jdbcTestModule.getCallableStatements(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getCallableStatements}
     */
    protected List getCallableStatements()
    {
        return jdbcTestModule.getCallableStatements();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getPreparedStatementParameter(PreparedStatement, int)}
     */
    protected Object getPreparedStatementParameter(PreparedStatement statement, int indexOfParameter)
    {
        return jdbcTestModule.getPreparedStatementParameter(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getPreparedStatementParameter(int, int)}
     */
    protected Object getPreparedStatementParameter(int indexOfStatement, int indexOfParameter)
    {
        return jdbcTestModule.getPreparedStatementParameter(indexOfStatement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getPreparedStatementParameter(String, int)}
     */
    protected Object getPreparedStatementParameter(String sql, int indexOfParameter)
    {
        return jdbcTestModule.getPreparedStatementParameter(sql, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getCallableStatementParameter(String, String)}
     */
    protected Object getCallableStatementParameter(String sql, String nameOfParameter)
    {
        return jdbcTestModule.getCallableStatementParameter(sql, nameOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getCallableStatementParameter(int, String)}
     */
    protected Object getCallableStatementParameter(int indexOfStatement, String nameOfParameter)
    {
        return jdbcTestModule.getCallableStatementParameter(indexOfStatement, nameOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getCallableStatementParameter(CallableStatement, int)}
     */
    protected Object getCallableStatementParameter(CallableStatement statement, int indexOfParameter)
    {
        return jdbcTestModule.getCallableStatementParameter(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getCallableStatementParameter(String, int)}
     */
    protected Object getCallableStatementParameter(String sql, int indexOfParameter)
    {
        return jdbcTestModule.getCallableStatementParameter(sql, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getCallableStatementParameter(int, int)}
     */
    protected Object getCallableStatementParameter(int indexOfStatement, int indexOfParameter)
    {
        return jdbcTestModule.getCallableStatementParameter(indexOfStatement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getCallableStatementParameter(CallableStatement, String)}
     */
    protected Object getCallableStatementParameter(CallableStatement statement, String nameOfParameter)
    {
        return jdbcTestModule.getCallableStatementParameter(statement, nameOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getSavepoints}
     */
    protected List getSavepoints()
    {
        return jdbcTestModule.getSavepoints();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getSavepoint(int)}
     */
    protected MockSavepoint getSavepoint(int index)
    {
        return jdbcTestModule.getSavepoint(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#getSavepoint(String)}
     */
    protected MockSavepoint getSavepoint(String name)
    {
        return jdbcTestModule.getSavepoint(name);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySQLStatementExecuted(String)}
     */
    protected void verifySQLStatementExecuted(String sql)
    {
        jdbcTestModule.verifySQLStatementExecuted(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySQLStatementNotExecuted(String)}
     */
    protected void verifySQLStatementNotExecuted(String sql)
    {
        jdbcTestModule.verifySQLStatementNotExecuted(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySQLStatementParameterNumber(String, int, int)}
     */
    protected void verifySQLStatementParameterNumber(String sql, int indexOfParameterSet, int number)
    {
        jdbcTestModule.verifySQLStatementParameterNumber(sql, indexOfParameterSet, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySQLStatementParameter(String, int, String, Object)}
     */
    protected void verifySQLStatementParameter(String sql, int indexOfParameterSet, String nameOfParameter, Object expectedParameter)
    {
        jdbcTestModule.verifySQLStatementParameter(sql, indexOfParameterSet, nameOfParameter, expectedParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySQLStatementParameter(String, int, Map)}
     */
    protected void verifySQLStatementParameter(String sql, int indexOfParameterSet, Map parameterMap)
    {
        jdbcTestModule.verifySQLStatementParameter(sql, indexOfParameterSet, parameterMap);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySQLStatementParameter(String, int, int, Object)}
     */
    protected void verifySQLStatementParameter(String sql, int indexOfParameterSet, int indexOfParameter, Object expectedParameter)
    {
        jdbcTestModule.verifySQLStatementParameter(sql, indexOfParameterSet, indexOfParameter, expectedParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyConnectionClosed}
     */
    protected void verifyConnectionClosed()
    {
        jdbcTestModule.verifyConnectionClosed();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyAllStatementsClosed}
     */
    protected void verifyAllStatementsClosed()
    {
        jdbcTestModule.verifyAllStatementsClosed();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetClosed(String)}
     */
    protected void verifyResultSetClosed(String id)
    {
        jdbcTestModule.verifyResultSetClosed(id);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRowInserted(String, int)}
     */
    protected void verifyResultSetRowInserted(String id, int number)
    {
        jdbcTestModule.verifyResultSetRowInserted(id, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRowInserted(MockResultSet, int)}
     */
    protected void verifyResultSetRowInserted(MockResultSet resultSet, int number)
    {
        jdbcTestModule.verifyResultSetRowInserted(resultSet, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRowNotInserted(String, int)}
     */
    protected void verifyResultSetRowNotInserted(String id, int number)
    {
        jdbcTestModule.verifyResultSetRowNotInserted(id, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRowNotInserted(MockResultSet, int)}
     */
    protected void verifyResultSetRowNotInserted(MockResultSet resultSet, int number)
    {
        jdbcTestModule.verifyResultSetRowNotInserted(resultSet, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRowUpdated(MockResultSet, int)}
     */
    protected void verifyResultSetRowUpdated(MockResultSet resultSet, int number)
    {
        jdbcTestModule.verifyResultSetRowUpdated(resultSet, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRowUpdated(String, int)}
     */
    protected void verifyResultSetRowUpdated(String id, int number)
    {
        jdbcTestModule.verifyResultSetRowUpdated(id, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRowNotUpdated(MockResultSet, int)}
     */
    protected void verifyResultSetRowNotUpdated(MockResultSet resultSet, int number)
    {
        jdbcTestModule.verifyResultSetRowNotUpdated(resultSet, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRowNotUpdated(String, int)}
     */
    protected void verifyResultSetRowNotUpdated(String id, int number)
    {
        jdbcTestModule.verifyResultSetRowNotUpdated(id, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRowDeleted(String, int)}
     */
    protected void verifyResultSetRowDeleted(String id, int number)
    {
        jdbcTestModule.verifyResultSetRowDeleted(id, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRowDeleted(MockResultSet, int)}
     */
    protected void verifyResultSetRowDeleted(MockResultSet resultSet, int number)
    {
        jdbcTestModule.verifyResultSetRowDeleted(resultSet, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRowNotDeleted(String, int)}
     */
    protected void verifyResultSetRowNotDeleted(String id, int number)
    {
        jdbcTestModule.verifyResultSetRowNotDeleted(id, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRowNotDeleted(MockResultSet, int)}
     */
    protected void verifyResultSetRowNotDeleted(MockResultSet resultSet, int number)
    {
        jdbcTestModule.verifyResultSetRowNotDeleted(resultSet, number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyAllResultSetsClosed}
     */
    protected void verifyAllResultSetsClosed()
    {
        jdbcTestModule.verifyAllResultSetsClosed();
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyNumberCommits(int)}
     */
    protected void verifyNumberCommits(int number)
    {
        jdbcTestModule.verifyNumberCommits(number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyNumberRollbacks(int)}
     */
    protected void verifyNumberRollbacks(int number)
    {
        jdbcTestModule.verifyNumberRollbacks(number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyNumberStatements(int)}
     */
    protected void verifyNumberStatements(int number)
    {
        jdbcTestModule.verifyNumberStatements(number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyNumberPreparedStatements(int, String)}
     */
    protected void verifyNumberPreparedStatements(int number, String sql)
    {
        jdbcTestModule.verifyNumberPreparedStatements(number, sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyNumberPreparedStatements(int)}
     */
    protected void verifyNumberPreparedStatements(int number)
    {
        jdbcTestModule.verifyNumberPreparedStatements(number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyNumberCallableStatements(int, String)}
     */
    protected void verifyNumberCallableStatements(int number, String sql)
    {
        jdbcTestModule.verifyNumberCallableStatements(number, sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyNumberCallableStatements(int)}
     */
    protected void verifyNumberCallableStatements(int number)
    {
        jdbcTestModule.verifyNumberCallableStatements(number);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyStatementClosed(int)}
     */
    protected void verifyStatementClosed(int index)
    {
        jdbcTestModule.verifyStatementClosed(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementClosed(int)}
     */
    protected void verifyPreparedStatementClosed(int index)
    {
        jdbcTestModule.verifyPreparedStatementClosed(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementClosed(String)}
     */
    protected void verifyPreparedStatementClosed(String sql)
    {
        jdbcTestModule.verifyPreparedStatementClosed(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementClosed(int)}
     */
    protected void verifyCallableStatementClosed(int index)
    {
        jdbcTestModule.verifyCallableStatementClosed(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementClosed(String)}
     */
    protected void verifyCallableStatementClosed(String sql)
    {
        jdbcTestModule.verifyCallableStatementClosed(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRow(MockResultSet, int, List)}
     */
    protected void verifyResultSetRow(MockResultSet resultSet, int number, List rowData)
    {
        jdbcTestModule.verifyResultSetRow(resultSet, number, rowData);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRow(MockResultSet, int, Object[])}
     */
    protected void verifyResultSetRow(MockResultSet resultSet, int number, Object[] rowData)
    {
        jdbcTestModule.verifyResultSetRow(resultSet, number, rowData);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRow(String, int, List)}
     */
    protected void verifyResultSetRow(String id, int number, List rowData)
    {
        jdbcTestModule.verifyResultSetRow(id, number, rowData);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetRow(String, int, Object[])}
     */
    protected void verifyResultSetRow(String id, int number, Object[] rowData)
    {
        jdbcTestModule.verifyResultSetRow(id, number, rowData);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetColumn(MockResultSet, String, Object[])}
     */
    protected void verifyResultSetColumn(MockResultSet resultSet, String name, Object[] columnData)
    {
        jdbcTestModule.verifyResultSetColumn(resultSet, name, columnData);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetColumn(MockResultSet, int, List)}
     */
    protected void verifyResultSetColumn(MockResultSet resultSet, int number, List columnData)
    {
        jdbcTestModule.verifyResultSetColumn(resultSet, number, columnData);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetColumn(MockResultSet, int, Object[])}
     */
    protected void verifyResultSetColumn(MockResultSet resultSet, int number, Object[] columnData)
    {
        jdbcTestModule.verifyResultSetColumn(resultSet, number, columnData);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetColumn(String, int, List)}
     */
    protected void verifyResultSetColumn(String id, int number, List columnData)
    {
        jdbcTestModule.verifyResultSetColumn(id, number, columnData);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetColumn(String, int, Object[])}
     */
    protected void verifyResultSetColumn(String id, int number, Object[] columnData)
    {
        jdbcTestModule.verifyResultSetColumn(id, number, columnData);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetColumn(MockResultSet, String, List)}
     */
    protected void verifyResultSetColumn(MockResultSet resultSet, String name, List columnData)
    {
        jdbcTestModule.verifyResultSetColumn(resultSet, name, columnData);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetColumn(String, String, List)}
     */
    protected void verifyResultSetColumn(String id, String name, List columnData)
    {
        jdbcTestModule.verifyResultSetColumn(id, name, columnData);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetColumn(String, String, Object[])}
     */
    protected void verifyResultSetColumn(String id, String name, Object[] columnData)
    {
        jdbcTestModule.verifyResultSetColumn(id, name, columnData);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetEquals(MockResultSet, MockResultSet)}
     */
    protected void verifyResultSetEquals(MockResultSet source, MockResultSet target)
    {
        jdbcTestModule.verifyResultSetEquals(source, target);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyResultSetEquals(String, MockResultSet)}
     */
    protected void verifyResultSetEquals(String id, MockResultSet target)
    {
        jdbcTestModule.verifyResultSetEquals(id, target);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementPresent(String)}
     */
    protected void verifyPreparedStatementPresent(String sql)
    {
        jdbcTestModule.verifyPreparedStatementPresent(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementNotPresent(String)}
     */
    protected void verifyPreparedStatementNotPresent(String sql)
    {
        jdbcTestModule.verifyPreparedStatementNotPresent(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementPresent(String)}
     */
    protected void verifyCallableStatementPresent(String sql)
    {
        jdbcTestModule.verifyCallableStatementPresent(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementNotPresent(String)}
     */
    protected void verifyCallableStatementNotPresent(String sql)
    {
        jdbcTestModule.verifyCallableStatementNotPresent(sql);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementParameterPresent(PreparedStatement, int)}
     */
    protected void verifyPreparedStatementParameterPresent(PreparedStatement statement, int indexOfParameter)
    {
        jdbcTestModule.verifyPreparedStatementParameterPresent(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementParameterPresent(String, int)}
     */
    protected void verifyPreparedStatementParameterPresent(String sql, int indexOfParameter)
    {
        jdbcTestModule.verifyPreparedStatementParameterPresent(sql, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementParameterPresent(int, int)}
     */
    protected void verifyPreparedStatementParameterPresent(int indexOfStatement, int indexOfParameter)
    {
        jdbcTestModule.verifyPreparedStatementParameterPresent(indexOfStatement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementParameterNotPresent(PreparedStatement, int)}
     */
    protected void verifyPreparedStatementParameterNotPresent(PreparedStatement statement, int indexOfParameter)
    {
        jdbcTestModule.verifyPreparedStatementParameterNotPresent(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementParameterNotPresent(String, int)}
     */
    protected void verifyPreparedStatementParameterNotPresent(String sql, int indexOfParameter)
    {
        jdbcTestModule.verifyPreparedStatementParameterNotPresent(sql, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementParameterNotPresent(int, int)}
     */
    protected void verifyPreparedStatementParameterNotPresent(int indexOfStatement, int indexOfParameter)
    {
        jdbcTestModule.verifyPreparedStatementParameterNotPresent(indexOfStatement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameterPresent(CallableStatement, String)}
     */
    protected void verifyCallableStatementParameterPresent(CallableStatement statement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(statement, nameOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameterPresent(CallableStatement, int)}
     */
    protected void verifyCallableStatementParameterPresent(CallableStatement statement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameterPresent(String, int)}
     */
    protected void verifyCallableStatementParameterPresent(String sql, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(sql, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameterPresent(int, int)}
     */
    protected void verifyCallableStatementParameterPresent(int indexOfStatement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(indexOfStatement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameterPresent(String, String)}
     */
    protected void verifyCallableStatementParameterPresent(String sql, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(sql, nameOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameterPresent(int, String)}
     */
    protected void verifyCallableStatementParameterPresent(int indexOfStatement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(indexOfStatement, nameOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameterNotPresent(CallableStatement, int)}
     */
    protected void verifyCallableStatementParameterNotPresent(CallableStatement statement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameterNotPresent(int, int)}
     */
    protected void verifyCallableStatementParameterNotPresent(int indexOfStatement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(indexOfStatement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameterNotPresent(String, int)}
     */
    protected void verifyCallableStatementParameterNotPresent(String sql, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(sql, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameterNotPresent(int, String)}
     */
    protected void verifyCallableStatementParameterNotPresent(int indexOfStatement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(indexOfStatement, nameOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameterNotPresent(CallableStatement, String)}
     */
    protected void verifyCallableStatementParameterNotPresent(CallableStatement statement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(statement, nameOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameterNotPresent(String, String)}
     */
    protected void verifyCallableStatementParameterNotPresent(String sql, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(sql, nameOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementParameter(PreparedStatement, int, Object)}
     */
    protected void verifyPreparedStatementParameter(PreparedStatement statement, int indexOfParameter, Object object)
    {
        jdbcTestModule.verifyPreparedStatementParameter(statement, indexOfParameter, object);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementParameter(String, int, Object)}
     */
    protected void verifyPreparedStatementParameter(String sql, int indexOfParameter, Object object)
    {
        jdbcTestModule.verifyPreparedStatementParameter(sql, indexOfParameter, object);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyPreparedStatementParameter(int, int, Object)}
     */
    protected void verifyPreparedStatementParameter(int indexOfStatement, int indexOfParameter, Object object)
    {
        jdbcTestModule.verifyPreparedStatementParameter(indexOfStatement, indexOfParameter, object);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameter(CallableStatement, int, Object)}
     */
    protected void verifyCallableStatementParameter(CallableStatement statement, int indexOfParameter, Object object)
    {
        jdbcTestModule.verifyCallableStatementParameter(statement, indexOfParameter, object);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameter(int, String, Object)}
     */
    protected void verifyCallableStatementParameter(int indexOfStatement, String nameOfParameter, Object object)
    {
        jdbcTestModule.verifyCallableStatementParameter(indexOfStatement, nameOfParameter, object);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameter(String, int, Object)}
     */
    protected void verifyCallableStatementParameter(String sql, int indexOfParameter, Object object)
    {
        jdbcTestModule.verifyCallableStatementParameter(sql, indexOfParameter, object);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameter(int, int, Object)}
     */
    protected void verifyCallableStatementParameter(int indexOfStatement, int indexOfParameter, Object object)
    {
        jdbcTestModule.verifyCallableStatementParameter(indexOfStatement, indexOfParameter, object);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameter(CallableStatement, String, Object)}
     */
    protected void verifyCallableStatementParameter(CallableStatement statement, String nameOfParameter, Object object)
    {
        jdbcTestModule.verifyCallableStatementParameter(statement, nameOfParameter, object);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementParameter(String, String, Object)}
     */
    protected void verifyCallableStatementParameter(String sql, String nameOfParameter, Object object)
    {
        jdbcTestModule.verifyCallableStatementParameter(sql, nameOfParameter, object);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementOutParameterRegistered(String, int)}
     */
    protected void verifyCallableStatementOutParameterRegistered(String sql, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementOutParameterRegistered(sql, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementOutParameterRegistered(CallableStatement, int)}
     */
    protected void verifyCallableStatementOutParameterRegistered(CallableStatement statement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementOutParameterRegistered(statement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementOutParameterRegistered(int, int)}
     */
    protected void verifyCallableStatementOutParameterRegistered(int indexOfStatement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementOutParameterRegistered(indexOfStatement, indexOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementOutParameterRegistered(CallableStatement, String)}
     */
    protected void verifyCallableStatementOutParameterRegistered(CallableStatement statement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementOutParameterRegistered(statement, nameOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementOutParameterRegistered(String, String)}
     */
    protected void verifyCallableStatementOutParameterRegistered(String sql, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementOutParameterRegistered(sql, nameOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifyCallableStatementOutParameterRegistered(int, String)}
     */
    protected void verifyCallableStatementOutParameterRegistered(int indexOfStatement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementOutParameterRegistered(indexOfStatement, nameOfParameter);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointPresent(int)}
     */
    protected void verifySavepointPresent(int index)
    {
        jdbcTestModule.verifySavepointPresent(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointPresent(String)}
     */
    protected void verifySavepointPresent(String name)
    {
        jdbcTestModule.verifySavepointPresent(name);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointReleased(int)}
     */
    protected void verifySavepointReleased(int index)
    {
        jdbcTestModule.verifySavepointReleased(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointReleased(String)}
     */
    protected void verifySavepointReleased(String name)
    {
        jdbcTestModule.verifySavepointReleased(name);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointNotReleased(int)}
     */
    protected void verifySavepointNotReleased(int index)
    {
        jdbcTestModule.verifySavepointNotReleased(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointNotReleased(String)}
     */
    protected void verifySavepointNotReleased(String name)
    {
        jdbcTestModule.verifySavepointNotReleased(name);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointRolledBack(int)}
     */
    protected void verifySavepointRolledBack(int index)
    {
        jdbcTestModule.verifySavepointRolledBack(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointRolledBack(String)}
     */
    protected void verifySavepointRolledBack(String name)
    {
        jdbcTestModule.verifySavepointRolledBack(name);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointNotRolledBack(String)}
     */
    protected void verifySavepointNotRolledBack(String name)
    {
        jdbcTestModule.verifySavepointNotRolledBack(name);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointNotRolledBack(int)}
     */
    protected void verifySavepointNotRolledBack(int index)
    {
        jdbcTestModule.verifySavepointNotRolledBack(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointRollbacked(int)}
     * @deprecated
     */
    protected void verifySavepointRollbacked(int index)
    {
        jdbcTestModule.verifySavepointRollbacked(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointRollbacked(String)}
     * @deprecated
     */
    protected void verifySavepointRollbacked(String name)
    {
        jdbcTestModule.verifySavepointRollbacked(name);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointNotRollbacked(int)}
     * @deprecated
     */
    protected void verifySavepointNotRollbacked(int index)
    {
        jdbcTestModule.verifySavepointNotRollbacked(index);
    }

    /**
     * Delegates to {@link com.mockrunner.jdbc.JDBCTestModule#verifySavepointNotRollbacked(String)}
     * @deprecated
     */
    protected void verifySavepointNotRollbacked(String name)
    {
        jdbcTestModule.verifySavepointNotRollbacked(name);
    }
}