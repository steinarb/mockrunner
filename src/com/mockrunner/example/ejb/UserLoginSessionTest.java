package com.mockrunner.example.ejb;

import org.mockejb.TransactionPolicy;

import com.mockrunner.ejb.EJBTestCaseAdapter;
import com.mockrunner.example.ejb.interfaces.UserLoginSession;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Example test for {@link UserLoginSessionBean} and {@link UserEntityBean}. 
 * This example demonstrated how to combine the testing of BMP entity beans
 * with the JDBC test framework. We prepare the primary key the database should
 * return and MockEJB handles create and finder calls. No interceptors
 * are involved here.
 * In the first test, we prepare a test user and try to login.
 * In the second test, we create a user and simulate the SQLException,
 * the real database would throw because of the duplicate primary key.
 */
public class UserLoginSessionTest extends EJBTestCaseAdapter
{
    private UserLoginSession bean;
    private PreparedStatementResultSetHandler handler;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        setInterfacePackage("com.mockrunner.example.ejb.interfaces");
        deploySessionBean("com/mockrunner/example/UserLoginSession", UserLoginSessionBean.class, TransactionPolicy.REQUIRED);
        bean = (UserLoginSession)createBean("com/mockrunner/example/UserLoginSession");
        deployEntityBean("java:comp/env/ejb/UserEntity", UserEntityBean.class, TransactionPolicy.REQUIRED);  
        bindToContext("java:comp/env/jdbc/MySQLDB", getJDBCMockObjectFactory().getMockDataSource());
        handler = getJDBCMockObjectFactory().getMockConnection().getPreparedStatementResultSetHandler();
    }
    
    private void prepareFindByPrimaryKeyResult()
    {
        MockResultSet resultSet = handler.createResultSet();
        resultSet.addRow(new Object[] {"TestUser"});
        handler.prepareResultSet("select username from usertable", resultSet);
        createEntityBean("java:comp/env/ejb/UserEntity", new Object[] {"TestUser", "CorrectPassword"}, "TestUser");
    }
    
    private void prepareSQLExceptionForCreate()
    {
        handler.prepareThrowsSQLException("insert into usertable");
    }
    
    public void testLoginUser() throws Exception
    {
        prepareFindByPrimaryKeyResult();
        assertFalse(bean.loginUser("TestUser", "WrongPassword"));
        assertTrue(bean.loginUser("TestUser", "CorrectPassword"));
        assertFalse(bean.loginUser("UnknownUser", "APassword"));
    }
    
    public void testCreateUser() throws Exception
    {
        assertTrue(bean.createUser("TestUser", "APassword"));
        prepareSQLExceptionForCreate();
        assertFalse(bean.createUser("TestUser", "APassword"));
    }
}
