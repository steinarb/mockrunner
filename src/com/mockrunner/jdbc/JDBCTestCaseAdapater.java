package com.mockrunner.jdbc;

import java.sql.PreparedStatement;
import java.util.List;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
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
}
