package com.mockrunner.test.jdbc;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.MockStatement;
import com.mockrunner.mock.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.StatementResultSetHandler;

public class MockStatementTest extends BaseTestCase
{
    private StatementResultSetHandler statementHandler;
    private PreparedStatementResultSetHandler preparedStatementHandler;
    private MockConnection connection;
    private MockResultSet resultSet1;
    private MockResultSet resultSet2;
    private MockResultSet resultSet3;

    protected void setUp() throws Exception
    {
        super.setUp();
        resultSet1 =  new MockResultSet();
        resultSet1.addRow(new String[] {"a", "b", "c"});
        resultSet2 =  new MockResultSet();
        resultSet2.addRow(new String[] {"column11", "column21"});
        resultSet2.addRow(new String[] {"column12", "column22"});
        resultSet3 =  new MockResultSet();
        resultSet3.addRow(new String[] {"test1", "test2"});
        resultSet3.addRow(new String[] {"test3", "test4"});
        resultSet3.addRow(new String[] {"test5", "test6"});
        connection = getMockObjectFactory().getMockConnection();
        statementHandler = connection.getStatementResultSetHandler();
        preparedStatementHandler = connection.getPreparedStatementResultSetHandler();
    }
    
    private boolean isResultSet1(MockResultSet resultSet)
    {
        return resultSet.getRowCount() == 1;
    }
    
    private boolean isResultSet2(MockResultSet resultSet)
    {
        return resultSet.getRowCount() == 2;
    }
    
    private boolean isResultSet3(MockResultSet resultSet)
    {
        return resultSet.getRowCount() == 3;
    }

    public void testPrepareResultSet() throws Exception
    {
        statementHandler.prepareGlobalResultSet(resultSet3);
        statementHandler.prepareResultSet("select test from x where value = true", resultSet1);
        statementHandler.prepareResultSet("select test1, test2 from y where value = 1", resultSet2);
        MockStatement statement = (MockStatement)connection.createStatement();
        statement.setCursorName("cursor");
        MockResultSet testResultSet = (MockResultSet)statement.executeQuery("SELECT test1, test2 FROM y WHERE value = 1");
        assertEquals("cursor", testResultSet.getCursorName());
        assertTrue(isResultSet2(testResultSet));
        statementHandler.setCaseSensitive(true);
        testResultSet = (MockResultSet)statement.executeQuery("SELECT test1, test2 FROM y WHERE value = 1");
        assertTrue(isResultSet3(testResultSet));
        statementHandler.setCaseSensitive(false);
        testResultSet = (MockResultSet)statement.executeQuery("select test from");
        assertTrue(isResultSet1(testResultSet));
        statementHandler.setExactMatch(true);
        testResultSet = (MockResultSet)statement.executeQuery("select test from");
        assertTrue(isResultSet3(testResultSet));
        statementHandler.setExactMatch(false);
        assertTrue(statement.execute("select test from"));
        assertTrue(isResultSet1((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        statementHandler.prepareReturnsResultSet("select test from", false);
        assertFalse(statement.execute("select test fr"));
        assertNull(statement.getResultSet());
        assertEquals(0, statement.getUpdateCount());
    }
    
    public void testPrepareUpdateCount() throws Exception
    {
        statementHandler.prepareGlobalUpdateCount(2);
        statementHandler.prepareUpdateCount("insert into x(y) values(1)", 3);
        MockStatement statement = (MockStatement)connection.createStatement();
        int testUpdateCount = statement.executeUpdate("update xy");
        assertEquals(2, testUpdateCount);
        testUpdateCount = statement.executeUpdate("insert into");
        assertEquals(3, testUpdateCount);
        statementHandler.setExactMatch(true);
        testUpdateCount = statement.executeUpdate("insert into");
        assertEquals(2, testUpdateCount);
        statementHandler.setExactMatch(false);
        statementHandler.setCaseSensitive(true);
        testUpdateCount = statement.executeUpdate("INSERT into");
        assertEquals(2, testUpdateCount);
        statementHandler.setCaseSensitive(false);
        testUpdateCount = statement.executeUpdate("INSERT into");
        assertEquals(3, testUpdateCount);
        assertFalse(statement.execute("DELETE"));
        assertEquals(2, statement.getUpdateCount());
        assertNull(statement.getResultSet());
    }
}
