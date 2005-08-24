package com.mockrunner.example.jdbc;

import java.sql.SQLException;

import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Example test for {@link Bank}. Demonstrates the usage of 
 * {@link com.mockrunner.jdbc.JDBCTestModule} 
 * and {@link com.mockrunner.jdbc.BasicJDBCTestCaseAdapter}.
 * Please note that the framework does not execute any SQL statements.
 * You have to specify the <code>MockResultSet</code> of the <i>select</i>
 * statement. Since there is only one <i>select</i> in this test, the
 * <code>MockResultSet</code> is set as the global one.
 * The Java API is used to add one row to the <code>MockResultSet</code>.
 * It's also possible to read the test tables from text files.
 * This test covers a valid transaction test 
 * (there's enough money on the source account) and the failure case.
 * You do not have to specify the exact SQL statements,
 * <i>select balance</i> is ok for <i>select balance from account where id=1</i>.
 * You can specify the search parameters of SQL statements with
 * {@link com.mockrunner.jdbc.JDBCTestModule#setExactMatch} and
 * {@link com.mockrunner.jdbc.JDBCTestModule#setCaseSensitive}.
 * The default is <code>false</code> for both.
 */
public class BankTest extends BasicJDBCTestCaseAdapter
{
    private void prepareEmptyResultSet()
    {
        StatementResultSetHandler statementHandler = getJDBCMockObjectFactory().getMockConnection().getStatementResultSetHandler();
        MockResultSet result = statementHandler.createResultSet();
        statementHandler.prepareGlobalResultSet(result);
    }
    
    private void prepareResultSet()
    {
        StatementResultSetHandler statementHandler = getJDBCMockObjectFactory().getMockConnection().getStatementResultSetHandler();
        MockResultSet result = statementHandler.createResultSet();
        result.addRow(new Integer[] {new Integer(10000)});
        statementHandler.prepareGlobalResultSet(result);
    }
    
    public void testWrongId() throws SQLException
    {
        prepareEmptyResultSet();
        Bank bank = new Bank();
        bank.connect();
        bank.transfer(1, 2, 5000);
        bank.disconnect();
        verifySQLStatementExecuted("select balance");
        verifySQLStatementNotExecuted("update account");
        verifyNotCommitted();
        verifyRolledBack();
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionClosed();
    }
    
    public void testTransferOk() throws SQLException
    {
        prepareResultSet();
        Bank bank = new Bank();
        bank.connect();
        bank.transfer(1, 2, 5000);
        bank.disconnect();
        verifySQLStatementExecuted("select balance");
        verifySQLStatementExecuted("update account");
        verifySQLStatementParameter("update account", 0, 1, new Integer(-5000));
        verifySQLStatementParameter("update account", 0, 2, new Integer(1));
        verifySQLStatementParameter("update account", 1, 1, new Integer(5000));
        verifySQLStatementParameter("update account", 1, 2, new Integer(2));
        verifyCommitted();
        verifyNotRolledBack();
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionClosed(); 
    }
    
    public void testTransferFailure() throws SQLException
    {
        prepareResultSet();
        Bank bank = new Bank();
        bank.connect();
        bank.transfer(1, 2, 20000);
        bank.disconnect();
        verifySQLStatementExecuted("select balance");
        verifySQLStatementNotExecuted("update account");
        verifyNotCommitted();
        verifyRolledBack();
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionClosed();
    }
}
