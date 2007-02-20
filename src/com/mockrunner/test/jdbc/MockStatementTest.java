package com.mockrunner.test.jdbc;

import java.sql.BatchUpdateException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockCallableStatement;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.MockStatement;

public class MockStatementTest extends BaseTestCase
{
    private StatementResultSetHandler statementHandler;
    private MockConnection connection;
    private MockResultSet resultSet1;
    private MockResultSet resultSet2;
    private MockResultSet resultSet3;

    protected void setUp() throws Exception
    {
        super.setUp();
        resultSet1 =  new MockResultSet("");
        resultSet1.addRow(new String[] {"a", "b", "c"});
        resultSet2 =  new MockResultSet("");
        resultSet2.addRow(new String[] {"column11", "column21"});
        resultSet2.addRow(new String[] {"column12", "column22"});
        resultSet3 =  new MockResultSet("");
        resultSet3.addRow(new String[] {"test1", "test2"});
        resultSet3.addRow(new String[] {"test3", "test4"});
        resultSet3.addRow(new String[] {"test5", "test6"});
        connection = getJDBCMockObjectFactory().getMockConnection();
        statementHandler = connection.getStatementResultSetHandler();
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        statementHandler = null;
        connection = null;
        resultSet1 = null;
        resultSet2 = null;
        resultSet3 = null;
    }
    
    private boolean isEmpty(MockResultSet resultSet)
    {
        return resultSet.getRowCount() == 0;
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
        statementHandler.prepareResultSet("select test from", resultSet1);
        statementHandler.prepareResultSet("select test1, test2", resultSet2);
        MockStatement statement = (MockStatement)connection.createStatement();
        statement.setCursorName("cursor");
        MockResultSet testResultSet = (MockResultSet)statement.executeQuery("SELECT test1, test2 FROM y WHERE value = 1");
        assertEquals("cursor", testResultSet.getCursorName());
        assertTrue(isResultSet2(testResultSet));
        statementHandler.setCaseSensitive(true);
        testResultSet = (MockResultSet)statement.executeQuery("SELECT test1, test2 FROM y WHERE value = 1");
        assertTrue(isResultSet3(testResultSet));
        statementHandler.setCaseSensitive(false);
        testResultSet = (MockResultSet)statement.executeQuery("select test from x where value = true");
        assertTrue(isResultSet1(testResultSet));
        statementHandler.setExactMatch(true);
        testResultSet = (MockResultSet)statement.executeQuery("select test from x where value = true");
        assertTrue(isResultSet3(testResultSet));
        statementHandler.setExactMatch(false);
        assertTrue(statement.execute("select test from x where value = true"));
        assertTrue(isResultSet1((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        statementHandler.prepareReturnsResultSet("select test from", false);
        assertFalse(statement.execute("select test from"));
        assertNull(statement.getResultSet());
        assertEquals(0, statement.getUpdateCount());
    }
    
    public void testPrepareMultipleResultSets() throws Exception
    {
        statementHandler.prepareGlobalResultSet(resultSet3);
        statementHandler.prepareResultSets("select test from", new MockResultSet[] {resultSet1, resultSet2, resultSet3});
        MockStatement statement = (MockStatement)connection.createStatement();
        MockResultSet testResultSet = (MockResultSet)statement.executeQuery("xyz");
        assertTrue(isResultSet3(testResultSet));
        assertFalse(statement.getMoreResults());
        assertFalse(statement.getMoreResults());
        testResultSet = (MockResultSet)statement.executeQuery("SELECT TEST FROM");
        assertTrue(isResultSet1(testResultSet));
        assertTrue(isResultSet1((MockResultSet)statement.getResultSet()));
        assertNotSame(resultSet1, (MockResultSet)statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        assertTrue(statement.getMoreResults());
        assertTrue(isResultSet2((MockResultSet)statement.getResultSet()));
        assertNotSame(resultSet2, (MockResultSet)statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        assertTrue(statement.getMoreResults());
        assertTrue(isResultSet3((MockResultSet)statement.getResultSet()));
        assertNotSame(resultSet3, (MockResultSet)statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertFalse(statement.getMoreResults());
        assertNull(statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
    }
    
    public void testPrepareMultipleResultSetsClose() throws Exception
    {
        statementHandler.prepareResultSets("select test", new MockResultSet[] {resultSet1, resultSet2, resultSet3});
        MockStatement statement = (MockStatement)connection.createStatement();
        statement.executeQuery("select test");
        MockResultSet testResultSet1 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults();
        MockResultSet testResultSet2 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults();
        MockResultSet testResultSet3 = (MockResultSet)statement.getResultSet();
        assertTrue(testResultSet1.isClosed());
        assertTrue(testResultSet2.isClosed());
        assertFalse(testResultSet3.isClosed());
        statement.getMoreResults();
        assertTrue(testResultSet3.isClosed());
        statement.execute("select test");
        testResultSet1 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.KEEP_CURRENT_RESULT);
        testResultSet2 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.KEEP_CURRENT_RESULT);
        testResultSet3 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
        assertFalse(testResultSet1.isClosed());
        assertFalse(testResultSet2.isClosed());
        assertTrue(testResultSet3.isClosed());
    }
    
    public void testCurrentResultSetsCloseOnExecute() throws Exception
    {
        statementHandler.prepareResultSets("select test", new MockResultSet[] {resultSet1, resultSet2, resultSet3});
        MockStatement statement = (MockStatement)connection.createStatement();
        statement.executeQuery("select test");
        MockResultSet testResultSet1 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.KEEP_CURRENT_RESULT);
        MockResultSet testResultSet2 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.KEEP_CURRENT_RESULT);
        MockResultSet testResultSet3 = (MockResultSet)statement.getResultSet();
        statement.executeQuery("select xyz");
        assertTrue(testResultSet1.isClosed());
        assertTrue(testResultSet2.isClosed());
        assertTrue(testResultSet3.isClosed());
    }
    
    public void testPrepareResultSetsStatementSet() throws Exception
    {
        MockResultSet resultSet = new MockResultSet("id");
        statementHandler.prepareGlobalResultSet(resultSet);
        statementHandler.prepareResultSets("select test", new MockResultSet[] {resultSet1, resultSet2, resultSet3});
        MockStatement statement = (MockStatement)connection.createStatement();
        statement.executeQuery("select test");
        MockResultSet testResultSet1 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults();
        MockResultSet testResultSet2 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults();
        MockResultSet testResultSet3 = (MockResultSet)statement.getResultSet();
        MockResultSet testResultSet = (MockResultSet)statement.executeQuery("xyz");
        assertSame(statement, testResultSet.getStatement());
        assertSame(statement, testResultSet1.getStatement());
        assertSame(statement, testResultSet2.getStatement());
        assertSame(statement, testResultSet3.getStatement());
    }
    
    public void testPrepareResultSetsNullValues() throws Exception
    {
        statementHandler.prepareResultSets("select1", new MockResultSet[] {});
        MockStatement statement = (MockStatement)connection.createStatement();
        MockResultSet testResultSet = (MockResultSet)statement.executeQuery("select1");
        assertNull(testResultSet);
        assertFalse(statement.getMoreResults());
        assertEquals(-1, statement.getUpdateCount());
        statementHandler.prepareResultSet("select2", null);
        testResultSet = (MockResultSet)statement.executeQuery("select2");
        assertNull(testResultSet);
        assertFalse(statement.getMoreResults());
        assertFalse(statement.getMoreResults());
        assertNull(statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
    }
    
    public void testPrepareMultipleGlobalResultSets() throws Exception
    {
        statementHandler.prepareGlobalResultSets(new MockResultSet[] {resultSet3, resultSet2, resultSet1});
        statementHandler.prepareResultSet("select test from", resultSet2);
        MockStatement statement = (MockStatement)connection.createStatement();
        MockResultSet testResultSet = (MockResultSet)statement.executeQuery("select test from");
        assertTrue(isResultSet2(testResultSet));
        assertTrue(isResultSet2((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertNull(statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        testResultSet = (MockResultSet)statement.executeQuery("xyz");
        assertTrue(isResultSet3(testResultSet));
        assertTrue(isResultSet3((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertTrue(statement.getMoreResults());
        assertTrue(isResultSet2((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertTrue(statement.getMoreResults());
        assertTrue(isResultSet1((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertNull(statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
    }
    
    public void testPrepareMultipleGlobalResultSetsClose() throws Exception
    {
        statementHandler.prepareGlobalResultSets(new MockResultSet[] {resultSet1, resultSet2, resultSet3});
        MockStatement statement = (MockStatement)connection.createStatement();
        statement.executeQuery("select test");
        MockResultSet testResultSet1 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
        MockResultSet testResultSet2 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
        MockResultSet testResultSet3 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.KEEP_CURRENT_RESULT);
        assertTrue(testResultSet1.isClosed());
        assertTrue(testResultSet2.isClosed());
        assertFalse(testResultSet3.isClosed());
    }
    
    public void testPrepareGeneratedKeys() throws Exception
    {
        statementHandler.prepareGeneratedKeys("insert into othertable", resultSet2);
        statementHandler.prepareGeneratedKeys("insert into table", resultSet3);
        MockStatement statement = (MockStatement)connection.createStatement();
        statement.executeUpdate("inser", new int[1]);
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
        assertSame(statement, ((MockResultSet)statement.getGeneratedKeys()).getStatement());
        statement.execute("insert into", Statement.NO_GENERATED_KEYS);
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
        statement.executeUpdate("do insert into table xyz", new String[0]);
        assertTrue(isResultSet3((MockResultSet)statement.getGeneratedKeys()));
        assertSame(statement, ((MockResultSet)statement.getGeneratedKeys()).getStatement());
        statementHandler.setUseRegularExpressions(true);
        statement.executeUpdate("insert into table xyz", new String[0]);
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
        statementHandler.prepareGlobalGeneratedKeys(resultSet1);
        statement.execute("insert into table xyz", Statement.RETURN_GENERATED_KEYS);
        assertTrue(isResultSet1((MockResultSet)statement.getGeneratedKeys()));
        assertSame(statement, ((MockResultSet)statement.getGeneratedKeys()).getStatement());
        statement.execute("insert into table xyz");
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
        statementHandler.setExactMatch(true);
        statement.executeUpdate("insert into othertable", Statement.RETURN_GENERATED_KEYS);
        assertTrue(isResultSet2((MockResultSet)statement.getGeneratedKeys()));
        assertSame(statement, ((MockResultSet)statement.getGeneratedKeys()).getStatement());
        statement.executeUpdate("insert into othertable", Statement.NO_GENERATED_KEYS);
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
        statementHandler.clearGlobalGeneratedKeys();
        statement.executeUpdate("abc", Statement.RETURN_GENERATED_KEYS);
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
        statementHandler.clearGeneratedKeys();
        statement.execute("insert into othertable", Statement.RETURN_GENERATED_KEYS);
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
    }
    
    public void testPrepareGeneratedKeysBatch() throws Exception
    {
        statementHandler.prepareGeneratedKeys("insert into othertable", resultSet2);
        statementHandler.prepareGeneratedKeys("insert into table", resultSet3);
        MockStatement statement = (MockStatement)connection.createStatement();
        statement.executeUpdate("insert into othertable", Statement.RETURN_GENERATED_KEYS);
        assertTrue(isResultSet2((MockResultSet)statement.getGeneratedKeys()));
        statement.addBatch("insert into othertable");
        statement.executeBatch();
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
    }
    
    public void testPrepareUpdateCount() throws Exception
    {
        statementHandler.prepareGlobalUpdateCount(2);
        statementHandler.prepareUpdateCount("insert into", 3);
        MockStatement statement = (MockStatement)connection.createStatement();
        int testUpdateCount = statement.executeUpdate("update xy");
        assertEquals(2, testUpdateCount);
        testUpdateCount = statement.executeUpdate("insert into x(y) values(1)");
        assertEquals(3, testUpdateCount);
        statementHandler.setExactMatch(true);
        testUpdateCount = statement.executeUpdate("insert into x(y) values(1)");
        assertEquals(2, testUpdateCount);
        statementHandler.setExactMatch(false);
        statementHandler.setCaseSensitive(true);
        testUpdateCount = statement.executeUpdate("INSERT into xy");
        assertEquals(2, testUpdateCount);
        statementHandler.setCaseSensitive(false);
        testUpdateCount = statement.executeUpdate("INSERT into xy");
        assertEquals(3, testUpdateCount);
        assertFalse(statement.execute("DELETE"));
        assertEquals(2, statement.getUpdateCount());
        assertNull(statement.getResultSet());
    }
    
    public void testPrepareMultipleUpdateCounts() throws Exception
    {
        statementHandler.prepareGlobalUpdateCount(2);
        statementHandler.prepareUpdateCounts("insert into", new int[] {1, 2, 3});
        MockStatement statement = (MockStatement)connection.createStatement();
        int testUpdateCount = statement.executeUpdate("update xy");
        assertEquals(2, testUpdateCount);
        assertEquals(2, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(-1, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        testUpdateCount = statement.executeUpdate("insert INTO");
        assertEquals(1, testUpdateCount);
        assertEquals(1, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(2, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(3, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(-1, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        statement.execute("insert into");
        assertEquals(1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertEquals(2, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertEquals(3, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertEquals(-1, statement.getUpdateCount());
    }
    
    public void testPrepareUpdateCountBatch() throws Exception
    {
        statementHandler.prepareGlobalUpdateCount(2);
        statementHandler.prepareUpdateCount("insert into", 3);
        MockStatement statement = (MockStatement)connection.createStatement();
        statement.addBatch("insert into x(y) values(1)");
        statement.addBatch("DELETE xyz where");
        statement.addBatch("update xy");
        int[] updateCounts = statement.executeBatch();
        assertTrue(updateCounts.length == 3);
        assertEquals(3, updateCounts[0]);
        assertEquals(2, updateCounts[1]);
        assertEquals(2, updateCounts[2]);
        statement.addBatch("select xy");
        try
        {
            statement.executeBatch();
            fail();
        }
        catch(BatchUpdateException exc)
        {
            assertEquals(3, exc.getUpdateCounts().length);
            assertEquals(3, exc.getUpdateCounts()[0]);
            assertEquals(2, exc.getUpdateCounts()[1]);
            assertEquals(2, exc.getUpdateCounts()[2]);
        }
        statement.clearBatch();
        updateCounts = statement.executeBatch();
        assertTrue(updateCounts.length == 0);
    }
    
    public void testPrepareUpdateCountBatchFailureWithoutContinue() throws Exception
    {
        statementHandler.prepareGlobalUpdateCount(2);
        statementHandler.prepareUpdateCount("insert into", 3);
        MockStatement statement = (MockStatement)connection.createStatement();
        statement.addBatch("insert into x(y) values(1)");
        statement.addBatch("DELETE xyz where");
        statement.addBatch("update xy");
        statementHandler.prepareThrowsSQLException("DELETE", new SQLException("reason", "state", 25));
        try
        {
            statement.executeBatch();
            fail();
        }
        catch(BatchUpdateException exc)
        {
            assertEquals(1, exc.getUpdateCounts().length);
            assertEquals(3, exc.getUpdateCounts()[0]);
            assertEquals(1, statementHandler.getExecutedStatements().size());
            assertEquals("insert into x(y) values(1)", statementHandler.getExecutedStatements().get(0));
            assertEquals("reason", exc.getMessage());
            assertEquals("state", exc.getSQLState());
            assertEquals(25, exc.getErrorCode());
        }
        statementHandler.prepareThrowsSQLException("insert into", new BatchUpdateException(new int[9]));
        try
        {
            statement.executeBatch();
            fail();
        }
        catch(BatchUpdateException exc)
        {
            assertEquals(9, exc.getUpdateCounts().length);
        }
    }
    
    public void testPrepareUpdateCountBatchFailureWithContinue() throws Exception
    {
        statementHandler.prepareGlobalUpdateCount(2);
        statementHandler.prepareUpdateCount("insert into", 3);
        MockStatement statement = (MockStatement)connection.createStatement();
        statement.addBatch("insert into x(y) values(1)");
        statement.addBatch("DELETE xyz where");
        statement.addBatch("update xy");
        statementHandler.prepareThrowsSQLException("DELETE");
        statementHandler.setContinueProcessingOnBatchFailure(true);
        try
        {
            statement.executeBatch();
            fail();
        }
        catch(BatchUpdateException exc)
        {
            assertEquals(3, exc.getUpdateCounts().length);
            assertEquals(3, exc.getUpdateCounts()[0]);
            assertEquals(-3, exc.getUpdateCounts()[1]);
            assertEquals(2, exc.getUpdateCounts()[2]);
            assertEquals(2, statementHandler.getExecutedStatements().size());
            assertEquals("insert into x(y) values(1)", statementHandler.getExecutedStatements().get(0));
            assertEquals("update xy", statementHandler.getExecutedStatements().get(1));
        }
        statementHandler.prepareThrowsSQLException("update xy", new SQLException("reason", "state", 25));
        try
        {
            statement.executeBatch();
            fail();
        }
        catch(BatchUpdateException exc)
        {
            assertEquals(3, exc.getUpdateCounts().length);
            assertEquals(3, exc.getUpdateCounts()[0]);
            assertEquals(-3, exc.getUpdateCounts()[1]);
            assertEquals(-3, exc.getUpdateCounts()[2]);
            assertEquals(3, statementHandler.getExecutedStatements().size());
            assertEquals("reason", exc.getMessage());
            assertEquals("state", exc.getSQLState());
            assertEquals(25, exc.getErrorCode());
        }
        statement.addBatch("select");
        try
        {
            statement.executeBatch();
            fail();
        }
        catch(BatchUpdateException exc)
        {
            assertEquals(4, exc.getUpdateCounts().length);
            assertEquals(3, exc.getUpdateCounts()[0]);
            assertEquals(-3, exc.getUpdateCounts()[1]);
            assertEquals(-3, exc.getUpdateCounts()[2]);
            assertEquals(-3, exc.getUpdateCounts()[3]);
            assertEquals(4, statementHandler.getExecutedStatements().size());
            assertEquals("SQL select in the list of batches returned a ResultSet.", exc.getMessage());
        }
    }
    
    public void testPrepareMultipleGlobalUpdateCounts() throws Exception
    {
        statementHandler.prepareGlobalUpdateCounts(new int[] {5, 4});
        statementHandler.prepareUpdateCount("insert into", 7);
        MockStatement statement = (MockStatement)connection.createStatement();
        int testUpdateCount = statement.executeUpdate("insert into");
        assertEquals(7, testUpdateCount);
        assertEquals(7, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(-1, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        testUpdateCount = statement.executeUpdate("XYZ");
        assertEquals(5, testUpdateCount);
        assertEquals(5, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(4, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(-1, statement.getUpdateCount());
        assertNull(statement.getResultSet());
    }
    
    public void testGetMoreResultsSingleResultSetAndUpdateCount() throws Exception
    {
        statementHandler.prepareGlobalResultSet(resultSet1);
        statementHandler.prepareGlobalUpdateCount(2);
        MockStatement statement = (MockStatement)connection.createStatement();
        assertFalse(statement.getMoreResults());
        assertNull(statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        statement.execute("select");
        MockResultSet currentResult = (MockResultSet)statement.getResultSet();
        assertEquals(resultSet1.getId(), currentResult.getId());
        assertEquals(resultSet1.getId(), currentResult.getId());
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertTrue(currentResult.isClosed());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(-1, statement.getUpdateCount());
        statementHandler.prepareResultSet("select", resultSet2);
        statement.executeQuery("select");
        assertNotNull(statement.getResultSet());
        statement.executeUpdate("insert");
        assertNull(statement.getResultSet());
        assertEquals(2, statement.getUpdateCount());
        assertEquals(2, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertEquals(-1, statement.getUpdateCount());
    }
    
    public void testPrepareThrowsSQLException() throws Exception
    {
        SQLException exception = new SQLWarning();
        statementHandler.prepareThrowsSQLException("insert into test", exception);
        statementHandler.prepareThrowsSQLException("select from");
        MockStatement statement = (MockStatement)connection.createStatement();
        statementHandler.prepareGlobalGeneratedKeys(resultSet1);
        statement.executeUpdate("update xy", Statement.RETURN_GENERATED_KEYS);
        assertTrue(isResultSet1((MockResultSet)statement.getGeneratedKeys()));
        statementHandler.prepareGlobalGeneratedKeys(resultSet2);
        try
        {
            statement.executeUpdate("insert into test values", Statement.RETURN_GENERATED_KEYS);
            fail();
        }
        catch(SQLException exc)
        {
            assertTrue(isResultSet1((MockResultSet)statement.getGeneratedKeys()));
            assertSame(exception, exc);
        }
        statementHandler.setExactMatch(true);
        statement.executeUpdate("insert into test values");
        statementHandler.setExactMatch(false);
        statementHandler.setCaseSensitive(true);
        statement.executeUpdate("iNsert into test values");
        try
        {
            statement.executeQuery("select from");
            fail();
        }
        catch(SQLException exc)
        {
            assertNotSame(exception, exc);
            assertTrue(exc.getMessage().indexOf("select from") != -1);
        }
    }
    
    public void testResultSetType() throws Exception
    {
        MockStatement statement1 = (MockStatement)connection.createStatement();
        statement1.setFetchDirection(ResultSet.FETCH_REVERSE);
        statement1.setFetchSize(3);
        MockPreparedStatement statement2 = (MockPreparedStatement)connection.prepareStatement("select2", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        MockCallableStatement statement3 = (MockCallableStatement)connection.prepareCall("select3", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.CLOSE_CURSORS_AT_COMMIT);
        MockResultSet resultSet = new MockResultSet("id1");
        resultSet.setResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE);
        resultSet.setResultSetHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
        connection.getStatementResultSetHandler().prepareResultSet("select1", resultSet);
        connection.getPreparedStatementResultSetHandler().prepareResultSet("select2", resultSet);
        connection.getCallableStatementResultSetHandler().prepareResultSet("select3", resultSet);
        MockResultSet returnedResultSet1 = (MockResultSet)statement1.executeQuery("select1");
        MockResultSet returnedResultSet2 = (MockResultSet)statement2.executeQuery("select2");
        MockResultSet returnedResultSet3 = (MockResultSet)statement3.executeQuery("select3");
        assertEquals(ResultSet.TYPE_FORWARD_ONLY, returnedResultSet1.getType());
        assertEquals(ResultSet.CONCUR_READ_ONLY, returnedResultSet1.getConcurrency());
        assertEquals(ResultSet.FETCH_REVERSE, returnedResultSet1.getFetchDirection());
        assertEquals(ResultSet.HOLD_CURSORS_OVER_COMMIT, returnedResultSet1.getHoldability());
        assertEquals(3, returnedResultSet1.getFetchSize());
        assertEquals(ResultSet.TYPE_SCROLL_INSENSITIVE, returnedResultSet2.getType());
        assertEquals(ResultSet.CONCUR_UPDATABLE, returnedResultSet2.getConcurrency());
        assertEquals(ResultSet.FETCH_FORWARD, returnedResultSet2.getFetchDirection());
        assertEquals(ResultSet.HOLD_CURSORS_OVER_COMMIT, returnedResultSet2.getHoldability());
        assertEquals(ResultSet.TYPE_SCROLL_SENSITIVE, returnedResultSet3.getType());
        assertEquals(ResultSet.CONCUR_UPDATABLE, returnedResultSet3.getConcurrency());
        assertEquals(ResultSet.FETCH_FORWARD, returnedResultSet3.getFetchDirection());
        assertEquals(ResultSet.CLOSE_CURSORS_AT_COMMIT, returnedResultSet3.getHoldability());
    }
}
