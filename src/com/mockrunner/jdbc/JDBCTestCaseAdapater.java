package com.mockrunner.jdbc;

import java.sql.PreparedStatement;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.jdbc.MockPreparedStatement;

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
     * Delegates to {@link JDBCTestModule#getPreparedStatement}
     */
    public MockPreparedStatement getPreparedStatement(String sql)
    {
        return jdbcTestModule.getPreparedStatement(sql);
    }

    /**
     * Delegates to {@link JDBCTestModule#getPreparedStatementObject(PreparedStatement, int)}
     */
    public Object getPreparedStatementObject(PreparedStatement statement, int index)
    {
        return jdbcTestModule.getPreparedStatementObject(statement, index);
    }

    /**
     * Delegates to {@link JDBCTestModule#getPreparedStatementObject(String, int)}
     */
    public Object getPreparedStatementObject(String sql, int index)
    {
        return jdbcTestModule.getPreparedStatementObject(sql, index);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementPresent}
     */
    public void verifyPreparedStatementPresent(String sql)
    {
        jdbcTestModule.verifyPreparedStatementPresent(sql);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObjectPresent(PreparedStatement, int)}
     */
    public void verifyPreparedStatementObjectPresent(PreparedStatement statement, int index)
    {
        jdbcTestModule.verifyPreparedStatementObjectPresent(statement, index);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObjectPresent(String, int)}
     */
    public void verifyPreparedStatementObjectPresent(String sql, int index)
    {
        jdbcTestModule.verifyPreparedStatementObjectPresent(sql, index);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObject(PreparedStatement, int, Object)}
     */
    public void verifyPreparedStatementObject(PreparedStatement statement, int index, Object object)
    {
        jdbcTestModule.verifyPreparedStatementObject(statement, index, object);
    }

    /**
     * Delegates to {@link JDBCTestModule#verifyPreparedStatementObject(PreparedStatement, int, Object)}
     */
    public void verifyPreparedStatementObject(String sql, int index, Object object)
    {
        jdbcTestModule.verifyPreparedStatementObject(sql, index, object);
    }
}
