package com.mockrunner.jdbc;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.List;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.jdbc.CallableStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockCallableStatement;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockSavepoint;
import com.mockrunner.mock.jdbc.MockStatement;
import com.mockrunner.mock.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.StatementResultSetHandler;

public class JDBCTestCaseAdapater extends BaseTestCase
{
    private JDBCTestModule jdbcTestModule;
    
    public JDBCTestCaseAdapater()
    {

    }

    public JDBCTestCaseAdapater(String arg0)
    {
        super(arg0);
    }

    /**
     * Creates the <code>JDBCTestModule</code>. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        jdbcTestModule = createJDBCTestModule(getMockObjectFactory());
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
    protected void setPreparedStatementCaseSensitive(boolean caseSensitive)
    {
        jdbcTestModule.setCaseSensitive(caseSensitive);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#setExactMatch}
     */
    protected void setPreparedStatementExactMatch(boolean exactMatch)
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
    protected List getSavepoints()
    {
        return jdbcTestModule.getSavepoints();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getSavepoint(int)}
     */ 
    protected MockSavepoint getSavepoint(int index)
    {   
        return jdbcTestModule.getSavepoint(index);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getSavepoint(String)}
     */
    protected MockSavepoint getSavepoint(String name)
    {
        return jdbcTestModule.getSavepoint(name);
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
     * Delegates to {@link JDBCTestModule#verifyCommited}
     */
    protected void verifyCommited()
    {
        jdbcTestModule.verifyCommited();
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyRollbacked}
     */
    protected void verifyRollbacked()
    {
        jdbcTestModule.verifyRollbacked();
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
    
    protected void verifyCallableStatementParameterPresent(CallableStatement statement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(statement, indexOfParameter);
    }

    protected void verifyCallableStatementParameterPresent(String sql, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(sql, indexOfParameter);
    }
    
    protected void verifyCallableStatementParameterPresent(int indexOfStatement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(indexOfStatement, indexOfParameter);
    }
    
       
    protected void verifyCallableStatementParameterNotPresent(CallableStatement statement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(statement, indexOfParameter);
    }

    protected void verifyCallableStatementParameterNotPresent(String sql, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(sql, indexOfParameter);
    }

    protected void verifyCallableStatementParameterNotPresent(int indexOfStatement, int indexOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(indexOfStatement, indexOfParameter);
    }
    
    protected void verifyCallableStatementParameterPresent(CallableStatement statement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(statement, nameOfParameter);
    }

    protected void verifyCallableStatementParameterPresent(String sql, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(sql, nameOfParameter);
    }
    
    protected void verifyCallableStatementParameterPresent(int indexOfStatement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterPresent(indexOfStatement, nameOfParameter);
    }
    
    protected void verifyCallableStatementParameterNotPresent(CallableStatement statement, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(statement, nameOfParameter);
    }

    protected void verifyCallableStatementParameterNotPresent(String sql, String nameOfParameter)
    {
        jdbcTestModule.verifyCallableStatementParameterNotPresent(sql, nameOfParameter);
    }

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
     * Delegates to {@link JDBCTestModule#verifySavepointPresent(int)}
     */
    protected void verifySavepointPresent(int index)
    {
        jdbcTestModule.verifySavepointPresent(index);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifySavepointPresent(String)}
     */   
    protected void verifySavepointPresent(String name)
    {
        jdbcTestModule.verifySavepointPresent(name);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifySavepointReleased(int)}
     */
    protected void verifySavepointReleased(int index)
    {
        jdbcTestModule.verifySavepointReleased(index);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifySavepointReleased(String)}
     */      
    protected void verifySavepointReleased(String name)
    {
        jdbcTestModule.verifySavepointReleased(name);
    }
  
    /**
     * Delegates to {@link JDBCTestModule#verifySavepointNotReleased(int)}
     */
    protected void verifySavepointNotReleased(int index)
    {
        jdbcTestModule.verifySavepointNotReleased(index);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifySavepointNotReleased(String)}
     */
    protected void verifySavepointNotReleased(String name)
    {
        jdbcTestModule.verifySavepointNotReleased(name);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifySavepointRollbacked(int)}
     */
    protected void verifySavepointRollbacked(int index)
    {
        jdbcTestModule.verifySavepointRollbacked(index);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifySavepointRollbacked(String)}
     */
    protected void verifySavepointRollbacked(String name)
    {
        jdbcTestModule.verifySavepointRollbacked(name);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifySavepointNotRollbacked(int)}
     */
    protected void verifySavepointNotRollbacked(int index)
    {
        jdbcTestModule.verifySavepointNotRollbacked(index);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifySavepointNotRollbacked(String)}
     */
    protected void verifySavepointNotRollbacked(String name)
    {
        jdbcTestModule.verifySavepointNotRollbacked(name);
    }
}
