package com.mockrunner.example.ejb;

import org.mockejb.TransactionPolicy;

import com.mockrunner.ejb.EJBTestCaseAdapter;
import com.mockrunner.example.ejb.interfaces.PaySession;
import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Example test for {@link PaySessionBean}. This example demonstrates
 * how to use {@link com.mockrunner.jdbc.JDBCTestModule} and 
 * {@link com.mockrunner.ejb.EJBTestModule} in conjunction. 
 * The tests are similar to {@link com.mockrunner.example.jdbc.PayActionTest} 
 * but instead of an action we test an EJB. This example works with the simulated 
 * JDBC environment of Mockrunner.
 */
public class PaySessionTest extends EJBTestCaseAdapter
{
    private JDBCTestModule jdbcModule;
    private PaySession bean;
    private StatementResultSetHandler statementHandler;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        jdbcModule = createJDBCTestModule();
        setInterfacePackage("com.mockrunner.example.ejb.interfaces");
        deploySessionBean("com/mockrunner/example/PaySession", PaySessionBean.class, TransactionPolicy.REQUIRED);
        bindToContext("java:comp/env/jdbc/MySQLDB", getJDBCMockObjectFactory().getMockDataSource());
        bean = (PaySession)createBean("com/mockrunner/example/PaySession");
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

    public void testUnknownCustomer() throws Exception
    {
        MockResultSet result = statementHandler.createResultSet();
        result.addColumn("name");
        statementHandler.prepareResultSet("select name", result);
        try
        {
            bean.payBill("1", "1", 100);
            fail();
        }
        catch(PaySessionException exc)
        {
            assertEquals(PaySessionException.UNKNOWN_CUSTOMER, exc.getCode());
        }
        verifyMarkedForRollback();
        verifyRolledBack();
        jdbcModule.verifySQLStatementExecuted("select name");
        jdbcModule.verifySQLStatementNotExecuted("delete from openbills");
        jdbcModule.verifySQLStatementNotExecuted("insert into paidbills");
        jdbcModule.verifyAllResultSetsClosed();
        jdbcModule.verifyAllStatementsClosed();
        jdbcModule.verifyConnectionClosed();
    }
    
    public void testUnknownBill() throws Exception
    {
        createValidCustomerResult();
        MockResultSet result = statementHandler.createResultSet();
        result.addColumn("id");
        result.addColumn("customerid");
        result.addColumn("amount");
        statementHandler.prepareResultSet("select * from openbills", result);
        try
        {
            bean.payBill("1", "1", 100);
            fail();
        }
        catch(PaySessionException exc)
        {
            assertEquals(PaySessionException.UNKNOWN_BILL, exc.getCode());
        }
        verifyMarkedForRollback();
        verifyRolledBack();
        jdbcModule.verifySQLStatementExecuted("select * from openbills");
        jdbcModule.verifySQLStatementNotExecuted("delete from openbills");
        jdbcModule.verifySQLStatementNotExecuted("insert into paidbills");
        jdbcModule.verifyAllResultSetsClosed();
        jdbcModule.verifyAllStatementsClosed();
        jdbcModule.verifyConnectionClosed();
    }
    
    public void testCustomerIdMismatch() throws Exception
    {
        createValidCustomerResult();
        createValidBillResult();
        try
        {
            bean.payBill("2", "1", 100);
            fail();
        }
        catch(PaySessionException exc)
        {
            assertEquals(PaySessionException.WRONG_BILL_FOR_CUSTOMER, exc.getCode());
        }
        verifyMarkedForRollback();
        verifyRolledBack();
        jdbcModule.verifySQLStatementExecuted("select * from openbills");
        jdbcModule.verifySQLStatementNotExecuted("delete from openbills");
        jdbcModule.verifySQLStatementNotExecuted("insert into paidbills");
        jdbcModule.verifyAllResultSetsClosed();
        jdbcModule.verifyAllStatementsClosed();
        jdbcModule.verifyConnectionClosed();
    }
    
    public void testAmountMismatch() throws Exception
    {
        createValidCustomerResult();
        createValidBillResult();
        try
        {
            bean.payBill("1", "1", 200);
            fail();
        }
        catch(PaySessionException exc)
        {
            assertEquals(PaySessionException.WRONG_AMOUNT_FOR_BILL, exc.getCode());
        }
        verifyMarkedForRollback();
        verifyRolledBack();
        jdbcModule.verifySQLStatementExecuted("select * from openbills");
        jdbcModule.verifySQLStatementNotExecuted("delete from openbills");
        jdbcModule.verifySQLStatementNotExecuted("insert into paidbills");
        jdbcModule.verifyAllResultSetsClosed();
        jdbcModule.verifyAllStatementsClosed();
        jdbcModule.verifyConnectionClosed();
    }

    public void testValidTransaction() throws Exception
    {
        createValidCustomerResult();
        createValidBillResult();
        bean.payBill("1", "1", 100);
        verifyNotMarkedForRollback();
        verifyCommitted();
        jdbcModule.verifySQLStatementExecuted("delete from openbills where id='1'");
        jdbcModule.verifySQLStatementExecuted("insert into paidbills values('1','1',100.0)");
        jdbcModule.verifyAllResultSetsClosed();
        jdbcModule.verifyAllStatementsClosed();
        jdbcModule.verifyConnectionClosed();
    }
}
