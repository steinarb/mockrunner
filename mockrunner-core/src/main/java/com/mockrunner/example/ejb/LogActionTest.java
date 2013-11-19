package com.mockrunner.example.ejb;

import org.mockejb.TransactionPolicy;

import com.mockrunner.ejb.EJBTestModule;
import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.struts.ActionTestCaseAdapter;

/**
 * Example test for {@link LogAction}. This example demonstrates
 * how to use {@link com.mockrunner.struts.ActionTestModule},
 * {@link com.mockrunner.jdbc.JDBCTestModule} and 
 * {@link com.mockrunner.ejb.EJBTestModule} in conjunction.
 * This example works with the simulated JDBC environment of Mockrunner.
 */
public class LogActionTest extends ActionTestCaseAdapter
{
    private JDBCTestModule jdbcModule;
    private EJBTestModule ejbModule;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        jdbcModule = createJDBCTestModule();
        ejbModule = createEJBTestModule();
        ejbModule.setInterfacePackage("com.mockrunner.example.ejb.interfaces");
        ejbModule.deploySessionBean("com/mockrunner/example/LogSession", LogSessionBean.class, TransactionPolicy.REQUIRED);
        ejbModule.bindToContext("java:comp/env/jdbc/MySQLDB", getJDBCMockObjectFactory().getMockDataSource());
    }

    public void testLogActionSuccess()
    {
        addRequestParameter("message", "testmessage");
        actionPerform(LogAction.class);
        jdbcModule.verifySQLStatementExecuted("insert into logtable");
        jdbcModule.verifyPreparedStatementParameter("insert into logtable", 2, "testmessage");
        ejbModule.verifyNotMarkedForRollback();
        ejbModule.verifyCommitted();
        jdbcModule.verifyAllStatementsClosed();
        jdbcModule.verifyConnectionClosed();
        verifyNoActionErrors();
        verifyForward("success");
    }
}
