package com.mockrunner.example.ejb;

import org.mockejb.MockContainer;
import org.mockejb.MockContext;
import org.mockejb.MockEjbObject;
import org.mockejb.SessionBeanDescriptor;

import com.mockrunner.example.ejb.interfaces.LogSession;
import com.mockrunner.example.ejb.interfaces.LogSessionHome;
import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.struts.ActionTestCaseAdapter;

/**
 * Example test for {@link LogAction}. This example demonstrates
 * how to use {@link com.mockrunner.struts.ActionTestModule},
 * {@link com.mockrunner.jdbc.JDBCTestModule} and the MockEJB
 * framework in conjunction. Note that you have to deploy a
 * <code>UserTransaction</code> and the {@link LogSessionBean} into the
 * mock container. This example works with the simulated JDBC
 * environment of Mockrunner. Since the <code>UserTransaction</code>
 * is only a mock implementation, it will not work with a real
 * database.
 */
public class LogActionTest extends ActionTestCaseAdapter
{
    private JDBCTestModule jdbcModule;
    private MockEjbObject bean;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        jdbcModule = createJDBCTestModule();
        SessionBeanDescriptor beanDescriptor = new SessionBeanDescriptor("com/mockrunner/example/LogSession", LogSessionHome.class, LogSession.class, LogSessionBean.class);
        bean = MockContainer.deploy(beanDescriptor);
        MockContext.add("javax.transaction.UserTransaction", getJDBCMockObjectFactory().getMockUserTransaction());
        MockContext.add("java:comp/env/jdbc/MySQLDB", getJDBCMockObjectFactory().getMockDataSource());
    }
    
    public void testLogActionSuccess()
    {
        addRequestParameter("message", "testmessage");
        actionPerform(LogAction.class);
        jdbcModule.verifySQLStatementExecuted("insert into logtable");
        jdbcModule.verifyPreparedStatementParameter("insert into logtable", 2, Thread.currentThread().getName());
        jdbcModule.verifyPreparedStatementParameter("insert into logtable", 3, "testmessage");
        assertFalse(bean.getEjbContext().getRollbackOnly());
        jdbcModule.verifyAllStatementsClosed();
        jdbcModule.verifyConnectionClosed();
        verifyNoActionErrors();
        verifyForward("success");
    }
}
