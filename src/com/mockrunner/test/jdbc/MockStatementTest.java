package com.mockrunner.test.jdbc;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.jdbc.MockClob;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
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
            //should throw Exception
        }
        statement.clearBatch();
        updateCounts = statement.executeBatch();
        assertTrue(updateCounts.length == 0);
    }
    
    public void testPrepareResultSetPreparedStatement() throws Exception
    {
        preparedStatementHandler.prepareGlobalResultSet(resultSet1); 
        preparedStatementHandler.prepareResultSet("select xyz", resultSet2);
        List params = new ArrayList();
        params.add(new Integer(2));
        params.add("Test");
        preparedStatementHandler.prepareResultSet("select test", resultSet3, params);
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("select test from x where value = ? and y = ?");
        MockResultSet testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
        statement.setInt(1, 2);
        statement.setString(2, "Test");
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet3(testResultSet));
        statement.setBoolean(3, true);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet3(testResultSet));
        preparedStatementHandler.setExactMatchParameter(true);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
        statement.clearParameters();
        statement.setInt(1, 2);
        statement.setString(2, "Test");
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet3(testResultSet));
        statement.setString(3, "Test");
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
        preparedStatementHandler.prepareResultSet("select test", resultSet3, new Object[] {"xyz", new Long(1)});
        statement.clearParameters();
        statement.setString(1, "ab");
        statement.setLong(2, 1);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
        statement.setString(1, "xyz");
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet3(testResultSet));
        statement.setString(3, "xyz");
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
        preparedStatementHandler.setExactMatchParameter(false);
        statement.clearParameters();
        statement.setString(1, "xyz");
        statement.setLong(2, 1);
        statement.setString(3, "xyz");
        statement.setString(4, "zzz");
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet3(testResultSet));
        statement = (MockPreparedStatement)connection.prepareStatement("select xyzxyz");
        statement.setLong(1, 2);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet2(testResultSet));
        preparedStatementHandler.setExactMatch(true);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
        preparedStatementHandler.prepareResultSet("select xyzxyz", resultSet3, new Object[] {});
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet3(testResultSet));
        preparedStatementHandler.setExactMatchParameter(true);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
        preparedStatementHandler.setExactMatchParameter(false);
        preparedStatementHandler.setExactMatch(false);
        assertTrue(statement.execute());
        assertTrue(isResultSet3((MockResultSet)statement.getResultSet()));
        Map paramMap = new HashMap();
        paramMap.put(new Integer(1), "Test");
        paramMap.put(new Integer(2), new MockClob("Test"));
        preparedStatementHandler.prepareResultSet("select xyzxyz", resultSet3, paramMap);
        statement.clearParameters();
        statement.setString(1, "Test");
        statement.setString(2, "Test");
        statement.setClob(3, new MockClob("Test"));
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet3(testResultSet));
        preparedStatementHandler.setExactMatchParameter(true);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet2(testResultSet));
        preparedStatementHandler.setExactMatch(true);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
    }
    
    public void testPrepareUpdateCountPreparedStatement() throws Exception
    {
        preparedStatementHandler.prepareGlobalUpdateCount(5);
        preparedStatementHandler.prepareUpdateCount("delete xyz", 1);
        List params = new ArrayList();
        params.add(new Integer(1));
        preparedStatementHandler.prepareUpdateCount("INSERT INTO", 3, params);
        preparedStatementHandler.prepareUpdateCount("INSERT INTO", 4, new Object[] {"1", "2"});
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("insert into x(y) values(?)");
        int testUpdateCount = statement.executeUpdate();
        assertEquals(5, testUpdateCount);
        statement.setInt(1, 1);
        statement.setInt(2, 2);
        testUpdateCount = statement.executeUpdate();
        assertEquals(3, testUpdateCount);
        preparedStatementHandler.setExactMatchParameter(true);
        testUpdateCount = statement.executeUpdate();
        assertEquals(5, testUpdateCount);
        statement.clearParameters();
        statement.setString(1, "1");
        statement.setString(2, "2");
        testUpdateCount = statement.executeUpdate();
        assertEquals(4, testUpdateCount);
        preparedStatementHandler.setCaseSensitive(true);
        testUpdateCount = statement.executeUpdate();
        assertEquals(5, testUpdateCount);
        statement = (MockPreparedStatement)connection.prepareStatement("delete xyz where ? = ?");
        testUpdateCount = statement.executeUpdate();
        assertEquals(1, testUpdateCount);
        preparedStatementHandler.setExactMatch(true);
        testUpdateCount = statement.executeUpdate();
        assertEquals(5, testUpdateCount);
        preparedStatementHandler.setExactMatch(false);
        assertFalse(statement.execute());
        assertEquals(1, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        preparedStatementHandler.prepareReturnsResultSet("delete xyz", true);
        assertTrue(statement.execute());
        assertEquals(-1, statement.getUpdateCount());
        assertNull(statement.getResultSet());
    }
    
    public void testPrepareUpdateCountBatchPreparedStatement() throws Exception
    {
        preparedStatementHandler.prepareGlobalUpdateCount(2);
        preparedStatementHandler.prepareUpdateCount("insert into", 3);
        preparedStatementHandler.prepareUpdateCount("insert into", 4, new Object[] {"1", "2"});
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("insert into x(y) values(?)");
        statement.setString(1, "1");
        statement.setString(2, "2");
        statement.addBatch();
        statement.clearParameters();
        statement.addBatch();
        statement.setString(1, "1");
        statement.setInt(2, 3);
        statement.addBatch();
        int[] updateCounts = statement.executeBatch();
        assertTrue(updateCounts.length == 3);
        assertEquals(4, updateCounts[0]);
        assertEquals(3, updateCounts[1]);
        assertEquals(3, updateCounts[2]);
        preparedStatementHandler.prepareReturnsResultSet("insert into", true);
        try
        {
            statement.executeBatch();
            fail();
        }
        catch(BatchUpdateException exc)
        {
            //should throw Exception
        }
        statement = (MockPreparedStatement)connection.prepareStatement("update xyz");
        statement.setString(1, "1");
        statement.setString(2, "2");
        statement.addBatch();
        updateCounts = statement.executeBatch();
        assertTrue(updateCounts.length == 1);
        assertEquals(2, updateCounts[0]);
        Map paramMap = new HashMap();
        paramMap.put(new Integer(1), "1");
        paramMap.put(new Integer(2), "2");
        preparedStatementHandler.prepareUpdateCount("update", 7, paramMap);
        updateCounts = statement.executeBatch();
        assertTrue(updateCounts.length == 1);
        assertEquals(7, updateCounts[0]);
    }
    
    public void testClearResultSetsAndUpdateCounts() throws Exception
    {
        preparedStatementHandler.prepareGlobalUpdateCount(5);
        preparedStatementHandler.prepareUpdateCount("delete xyz", 1);
        preparedStatementHandler.prepareGlobalResultSet(resultSet1); 
        preparedStatementHandler.prepareResultSet("select xyz", resultSet2);
        preparedStatementHandler.prepareResultSet("select test", resultSet3);
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("select test");
        MockResultSet resultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet3(resultSet));
        preparedStatementHandler.clearResultSets();
        resultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(resultSet));
        statement = (MockPreparedStatement)connection.prepareStatement("delete xyz");
        int updateCount = statement.executeUpdate();
        assertEquals(1, updateCount);
        preparedStatementHandler.clearUpdateCounts();
        updateCount = statement.executeUpdate();
        assertEquals(5, updateCount);
    }
}
