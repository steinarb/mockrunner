package com.mockrunner.example.jdbc;

import com.mockrunner.jdbc.JDBCTestCaseAdapter;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Example test for {@link Bank}.
 * Please note that the framework does not execute any SQL statements.
 * We have to specify the <code>MockResultSet</code> of the <i>select</i>
 * statement. Since there is only one <i>select</i>, we can set
 * the <code>MockResultSet</code> as global one. We use
 * the Java API to add one row to the <code>MockResultSet</code>.
 * It's also possible to read the test tables from text files.
 * We test a valid transaction (there's enough money on the source account)
 * and the failure case.
 * You do not have to specify the exact SQL statements,
 * <i>select balance</i> is ok for <i>select balance from account where id=1</i>.
 * You can specify the search parameters of SQL statements with
 * {@link com.mockrunner.jdbc.JDBCTestModule#setExactMatch} and
 * {@link com.mockrunner.jdbc.JDBCTestModule#setCaseSensitive}.
 * The default is <code>false</code> for both.
 **/
public class BankTest extends JDBCTestCaseAdapter
{
    protected void setUp() throws Exception
    {
        super.setUp();
        StatementResultSetHandler statementHandler = getJDBCMockObjectFactory().getMockConnection().getStatementResultSetHandler();
        MockResultSet result = statementHandler.createResultSet();
        result.addRow(new Integer[] {new Integer(10000)});
        statementHandler.prepareGlobalResultSet(result);
    }
    
    public void testTransferOk() throws Exception
    {
        Bank bank = new Bank();
        bank.connect();
        bank.transfer(1, 2, 5000);
        verifySQLStatementExecuted("select balance");
        verifySQLStatementExecuted("update account");
        verifyPreparedStatementParameter("update account", 1, new Integer(5000));
        verifyCommited();
        verifyNotRolledback();
        verifyAllStatementsClosed();
    }
    
    public void testTransferFailure() throws Exception
    {
        Bank bank = new Bank();
        bank.connect();
        bank.transfer(1, 2, 20000);
        verifySQLStatementExecuted("select balance");
        verifySQLStatementNotExecuted("update account");
        verifyNotCommited();
        verifyRolledback();
        verifyAllStatementsClosed();
    }
}
