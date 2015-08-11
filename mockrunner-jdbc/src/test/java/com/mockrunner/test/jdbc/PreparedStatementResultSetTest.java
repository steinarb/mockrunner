package com.mockrunner.test.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockParameterMap;
import com.mockrunner.mock.jdbc.MockResultSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PreparedStatementResultSetTest extends BasicJDBCTestCaseAdapter {

    @Test
    public void test() throws Exception {
        PreparedStatementResultSetHandler statementHandler = getJDBCMockObjectFactory()
                .getMockConnection().getPreparedStatementResultSetHandler();
        MockResultSet resultMock = statementHandler.createResultSet();
        resultMock.addColumn("ID", new Object[]{"1"});
        resultMock.addColumn("USERNAME", new Object[]{"foobar"});
        statementHandler.prepareGlobalResultSet(resultMock);
        // statementHandler.prepareResultSet("SELECT * FROM DUAL", resultMock, new MockParameterMap()); // WORKAROUND
        Connection con = DriverManager.getConnection( "a", "b", "c");
        System.out.println(con); //com.mockrunner.mock.jdbc.MockConnection@29d8a2c5
        PreparedStatement stmtObjects = con.prepareStatement(
                  "SELECT ID, USERNAME FROM DUAL WHERE 1=?"); //SELECT * FROM DUAL would work, too.
        ResultSet rs = stmtObjects.executeQuery();
        System.out.println(rs);
        System.out.println(this.getPreparedStatements());
        System.out.println("Executed Statements:" + statementHandler.getExecutedStatements());
        assertTrue(rs.next());
        assertEquals(rs.getString("ID"), "1");
        assertEquals(rs.getString("USERNAME"), "foobar");
    }

    @Test
    public void testWOParameters() throws Exception {
        MockConnection conn = getJDBCMockObjectFactory().getMockConnection();
        PreparedStatement preparedStatement1 = conn.prepareStatement("select id, username from users", new String[]{"id", "username"});
        PreparedStatementResultSetHandler statementHandler = conn.getPreparedStatementResultSetHandler();
        MockResultSet resultSet1 = statementHandler.createResultSet();
        resultSet1.addColumn("id", new Integer[]{1, 2, 3});
        resultSet1.addColumn("username", new String[]{"foo", "bar", "baz"});
        statementHandler.prepareResultSet("select id, username from users", resultSet1);
        
        ResultSet rs = preparedStatement1.executeQuery();
        assertTrue(rs.next());
        assertEquals(rs.getInt("id"), 1);
        assertEquals(rs.getString("username"), "foo");
        rs.close();
        
        statementHandler.removeResultSet("select id, username from users");
        ResultSet rs2 = preparedStatement1.executeQuery();
        assertFalse(rs2.next());
        rs2.close();
    }
    
    @Test
    public void testWParameters() throws Exception {
        MockConnection conn = getJDBCMockObjectFactory().getMockConnection();
        PreparedStatement preparedStatement1 = conn.prepareStatement("select id, username from users where id<?");
        PreparedStatementResultSetHandler statementHandler = conn.getPreparedStatementResultSetHandler();
        MockResultSet resultSet1 = statementHandler.createResultSet();
        resultSet1.addColumn("id", new Integer[]{3, 2, 1});
        resultSet1.addColumn("username", new String[]{"foo", "bar", "baz"});
        MockResultSet resultSet2 = statementHandler.createResultSet();
        resultSet2.addColumn("id", new Integer[]{2, 1});
        resultSet2.addColumn("username", new String[]{"bar", "baz"});
        statementHandler.prepareResultSet("select id, username from users where id<?", resultSet1, new Object[]{3});
        statementHandler.prepareResultSet("select id, username from users where id<?", resultSet2, new Object[]{2});
        
        preparedStatement1.setInt(1, 3);
        ResultSet rs = preparedStatement1.executeQuery();
        assertTrue(rs.next());
        assertEquals(rs.getInt("id"), 3);
        assertEquals(rs.getString("username"), "foo");
        rs.close();
        statementHandler.removeResultSet("select id, username from users where id<?", new MockParameterMap(new Object[]{3}));
        ResultSet rs2 = preparedStatement1.executeQuery();
        assertFalse(rs2.next());
        rs2.close();

        preparedStatement1.setInt(1, 2);
        ResultSet rs3 = preparedStatement1.executeQuery();
        assertTrue(rs3.next());
        assertEquals(rs3.getInt("id"), 2);
        assertEquals(rs3.getString("username"), "bar");
        rs3.close();
        
    }
}
