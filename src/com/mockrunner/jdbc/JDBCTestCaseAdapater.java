package com.mockrunner.jdbc;

import java.sql.PreparedStatement;
import java.util.List;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockStatement;

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
     * Delegates to {@link JDBCTestModule#getPreparedStatementObject(PreparedStatement, int)}
     */
    protected Object getPreparedStatementObject(PreparedStatement statement, int indexOfObject)
    {
        return jdbcTestModule.getPreparedStatementObject(statement, indexOfObject);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getPreparedStatementObject(String, int)}
     */
    protected Object getPreparedStatementObject(String sql, int indexOfObject)
    {
        return jdbcTestModule.getPreparedStatementObject(sql, indexOfObject);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#getPreparedStatementObject(int, int)}
     */
    protected Object getPreparedStatementObject(int indexOfStatement, int indexOfObject)
    {
        return jdbcTestModule.getPreparedStatementObject(indexOfStatement, indexOfObject);
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
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObjectPresent(PreparedStatement, int)}
     */
    protected void verifyPreparedStatementObjectPresent(PreparedStatement statement, int indexOfObject)
    {
        jdbcTestModule.verifyPreparedStatementObjectPresent(statement, indexOfObject);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObjectPresent(String, int)}
     */
    protected void verifyPreparedStatementObjectPresent(String sql, int indexOfObject)
    {
        jdbcTestModule.verifyPreparedStatementObjectPresent(sql, indexOfObject);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObjectPresent(int, int)}
     */
    protected void verifyPreparedStatementObjectPresent(int indexOfStatement, int indexOfObject)
    {
        jdbcTestModule.verifyPreparedStatementObjectPresent(indexOfStatement, indexOfObject);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObjectNotPresent(PreparedStatement, int)}
     */
    protected void verifyPreparedStatementObjectNotPresent(PreparedStatement statement, int indexOfObject)
    {
        jdbcTestModule.verifyPreparedStatementObjectNotPresent(statement, indexOfObject);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObjectNotPresent(String, int)}
     */
    protected void verifyPreparedStatementObjectNotPresent(String sql, int indexOfObject)
    {
        jdbcTestModule.verifyPreparedStatementObjectNotPresent(sql, indexOfObject);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObjectNotPresent(int, int)}
     */
    protected void verifyPreparedStatementObjectNotPresent(int indexOfStatement, int indexOfObject)
    {
        jdbcTestModule.verifyPreparedStatementObjectNotPresent(indexOfStatement, indexOfObject);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObject(PreparedStatement, int, Object)}
     */
    protected void verifyPreparedStatementObject(PreparedStatement statement, int indexOfObject, Object object)
    {
        jdbcTestModule.verifyPreparedStatementObject(statement, indexOfObject, object);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObject(String, int, Object)}
     */
    protected void verifyPreparedStatementObject(String sql, int indexOfObject, Object object)
    {
        jdbcTestModule.verifyPreparedStatementObject(sql, indexOfObject, object);
    }
    
    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObject(int, int, Object)}
     */
    protected void verifyPreparedStatementObject(int indexOfStatement, int indexOfObject, Object object)
    {
        jdbcTestModule.verifyPreparedStatementObject(indexOfStatement, indexOfObject, object);
    }
}
