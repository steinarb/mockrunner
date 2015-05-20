package com.mockrunner.test.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockResultSet;

public class PreparedStatementResultSetTest extends BasicJDBCTestCaseAdapter {

    @Test
    public void test() throws Exception {
        PreparedStatementResultSetHandler statementHandler = getJDBCMockObjectFactory()
                .getMockConnection().getPreparedStatementResultSetHandler();
        MockResultSet resultMock = statementHandler.createResultSet();
        resultMock.addColumn("ID", new Object[]{"1"});
        resultMock.addColumn("USERNAME", new Object[]{"foobar"});
        statementHandler.prepareGlobalResultSet(resultMock);
        // statementHandler.prepareResultSet("SELECT * FROM DUAL", resultMock, new HashMap()); // WORKAROUND
        Connection con = DriverManager.getConnection( "a", "b", "c");
        System.out.println(con); //com.mockrunner.mock.jdbc.MockConnection@29d8a2c5
        PreparedStatement stmtObjects = con.prepareStatement(
                  "SELECT ID, USERNAME FROM DUAL WHERE 1=?"); //SELECT * FROM DUAL would work, too.
        ResultSet rs = stmtObjects.executeQuery();
        System.out.println(rs);
        System.out.println(this.getPreparedStatements());
    }

}
