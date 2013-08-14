package com.mockrunner.example.jdbc;

import com.mockrunner.jdbc.JDBCTestCaseAdapter;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.struts.ActionTestModule;

/**
 * Example test for {@link PayAction}. Demonstrates the usage of 
 * {@link com.mockrunner.jdbc.JDBCTestModule} 
 * and {@link com.mockrunner.jdbc.JDBCTestCaseAdapter}.
 * This is also a good example how to combine the Struts and
 * the JDBC testframework.
 */
public class PayActionTest extends JDBCTestCaseAdapter
{
    private ActionTestModule actionModule;
    private StatementResultSetHandler statementHandler;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        actionModule = createActionTestModule();
        statementHandler = getJDBCMockObjectFactory().getMockConnection().getStatementResultSetHandler();
    }
    
    private void createValidCustomerResult()
    {
        MockResultSet result = statementHandler.createResultSet();
        result.addColumn("name", new String[] {"MyName"});
        statementHandler.prepareResultSet("select name", result);
    }
    
    private void createValidBillResult()
    {
        MockResultSet result = statementHandler.createResultSet();
        result.addColumn("id", new String[] {"1"});
        result.addColumn("customerid", new String[] {"1"});
        result.addColumn("amount", new Double[] {new Double(100)});
        statementHandler.prepareResultSet("select * from openbills", result);
    }
    
    public void testUnknownCustomer()
    {
        MockResultSet result = statementHandler.createResultSet();
        result.addColumn("name");
        statementHandler.prepareResultSet("select name", result);
        actionModule.addRequestParameter("customerId", "1");
        actionModule.actionPerform(PayAction.class, PayForm.class);
        actionModule.verifyActionErrorPresent("unknown.customer.error");
        verifyRolledBack();
        verifyNotCommitted();
        verifySQLStatementExecuted("select name");
        verifySQLStatementNotExecuted("delete from openbills");
        verifySQLStatementNotExecuted("insert into paidbills");
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionClosed();
    }
    
    public void testUnknownBill()
    {
        createValidCustomerResult();
        MockResultSet result = statementHandler.createResultSet();
        result.addColumn("id");
        result.addColumn("customerid");
        result.addColumn("amount");
        statementHandler.prepareResultSet("select * from openbills", result);
        actionModule.addRequestParameter("customerId", "1");
        actionModule.addRequestParameter("billId", "1");
        actionModule.actionPerform(PayAction.class, PayForm.class);
        actionModule.verifyActionErrorPresent("unknown.bill.error");
        verifyRolledBack();
        verifyNotCommitted();
        verifySQLStatementExecuted("select * from openbills");
        verifySQLStatementNotExecuted("delete from openbills");
        verifySQLStatementNotExecuted("insert into paidbills");
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionClosed();
    }
    
    public void testCustomerIdMismatch()
    {
        createValidCustomerResult();
        createValidBillResult();
        actionModule.addRequestParameter("customerId", "2");
        actionModule.addRequestParameter("billId", "1");
        actionModule.actionPerform(PayAction.class, PayForm.class);
        actionModule.verifyActionErrorPresent("wrong.bill.for.customer");
        verifyRolledBack();
        verifyNotCommitted();
        verifySQLStatementExecuted("select * from openbills");
        verifySQLStatementNotExecuted("delete from openbills");
        verifySQLStatementNotExecuted("insert into paidbills");
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionClosed();
    }
    
    public void testAmountMismatch()
    {
        createValidCustomerResult();
        createValidBillResult();
        actionModule.addRequestParameter("customerId", "1");
        actionModule.addRequestParameter("billId", "1");
        actionModule.addRequestParameter("amount", "200");
        actionModule.actionPerform(PayAction.class, PayForm.class);
        actionModule.verifyActionErrorPresent("wrong.amount.for.bill");
        verifyRolledBack();
        verifyNotCommitted();
        verifySQLStatementExecuted("select * from openbills");
        verifySQLStatementNotExecuted("delete from openbills");
        verifySQLStatementNotExecuted("insert into paidbills");
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionClosed();
    }
    
    public void testValidTransaction()
    {
        createValidCustomerResult();
        createValidBillResult();
        actionModule.addRequestParameter("customerId", "1");
        actionModule.addRequestParameter("billId", "1");
        actionModule.addRequestParameter("amount", "100");
        actionModule.actionPerform(PayAction.class, PayForm.class);
        actionModule.verifyNoActionErrors();
        verifyCommitted();
        verifyNotRolledBack();
        verifySQLStatementExecuted("delete from openbills where id='1'");
        verifySQLStatementExecuted("insert into paidbills values('1','1',100.0)");
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionClosed();
    }
}
