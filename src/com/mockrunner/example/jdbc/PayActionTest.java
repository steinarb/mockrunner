package com.mockrunner.example.jdbc;

import com.mockrunner.jdbc.JDBCTestCaseAdapter;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.struts.ActionTestModule;

public class PayActionTest extends JDBCTestCaseAdapter
{
    private ActionTestModule actionModule;
    private StatementResultSetHandler statementHandler;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        actionModule = new ActionTestModule(createWebMockObjectFactory());
        statementHandler = getJDBCMockObjectFactory().getMockConnection().getStatementResultSetHandler();
    }
    
    public void testWrongCustomer()
    {
        MockResultSet result = statementHandler.createResultSet();
        result.addColumn("name");
        statementHandler.prepareResultSet("select name", result);
        actionModule.addRequestParameter("customerId", "1");
        actionModule.actionPerform(PayAction.class, PayForm.class);
        actionModule.verifyActionErrorPresent("unknown.customer.error");
        verifyRolledBack();
        verifyNotCommited();
        verifySQLStatementExecuted("select name");
        verifySQLStatementNotExecuted("delete from openbills");
        verifySQLStatementNotExecuted("insert into paidbills");
        verifyAllStatementsClosed();
        verifyConnectionClosed();
    }
}
