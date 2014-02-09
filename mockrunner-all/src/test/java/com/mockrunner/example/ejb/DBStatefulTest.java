package com.mockrunner.example.ejb;

import org.junit.Before;
import org.junit.Test;

import com.mockrunner.ejb.EJBTestCaseAdapter;
import com.mockrunner.example.ejb.interfaces.DBStateful;
import com.mockrunner.jdbc.JDBCTestModule;

/**
 * Example test for {@link DBStatefulBean}. This example demonstrates
 * how to test stateful beans and how to deal with BMT.
 */
public class DBStatefulTest extends EJBTestCaseAdapter
{
    private JDBCTestModule jdbcModule;
    private DBStateful bean;
    
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        jdbcModule = createJDBCTestModule();
        setInterfacePackage("com.mockrunner.example.ejb.interfaces");
        //true = stateful, null = no TransactionPolicy = BMT
        deploySessionBean("com/mockrunner/example/DBStateful", DBStatefulBean.class, true, null);
        bindToContext("java:/MySQLDB", getJDBCMockObjectFactory().getMockDataSource());
        bean = (DBStateful)createBean("com/mockrunner/example/DBStateful");
    }
    
    @Test
    public void testCommit() throws Exception
    {
        bean.beginTransaction();
        bean.executeSQL("drop database");
        bean.endTransaction(true);
        jdbcModule.verifyAllStatementsClosed();
        jdbcModule.verifyConnectionClosed();
        jdbcModule.verifySQLStatementExecuted("drop database");
        verifyCommitted();
        verifyNotRolledBack();
    }
    
    @Test
    public void testRollback() throws Exception
    {
        bean.beginTransaction();
        bean.executeSQL("drop database");
        bean.endTransaction(false);
        jdbcModule.verifyAllStatementsClosed();
        jdbcModule.verifyConnectionClosed();
        jdbcModule.verifySQLStatementExecuted("drop database");
        verifyRolledBack();
        verifyNotCommitted();
    }
}
