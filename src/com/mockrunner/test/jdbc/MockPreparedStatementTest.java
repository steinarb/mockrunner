package com.mockrunner.test.jdbc;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockClob;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockResultSet;

public class MockPreparedStatementTest extends BaseTestCase
{
    private PreparedStatementResultSetHandler preparedStatementHandler;
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
        preparedStatementHandler = connection.getPreparedStatementResultSetHandler();
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        preparedStatementHandler = null;
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
    
    public void testPrepareMultipleResultSets() throws Exception
    {
        preparedStatementHandler.prepareResultSet("select xyz", resultSet2);
        preparedStatementHandler.prepareResultSets("select xyz", new MockResultSet[] {resultSet1, resultSet2, resultSet3}, new Object[] {"1", new Integer(2)});
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("select xyz from x where value = ? and y = ?");
        statement.setString(1, "1");
        MockResultSet testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet2(testResultSet));
        assertTrue(isResultSet2((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertNull(statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        statement.setInt(2, 2);
        statement.setInt(3, 2);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
        assertTrue(isResultSet1((MockResultSet)statement.getResultSet()));
        assertNotSame(resultSet1, statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        assertTrue(statement.getMoreResults());
        assertTrue(isResultSet2((MockResultSet)statement.getResultSet()));
        assertNotSame(resultSet2, statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        assertTrue(statement.getMoreResults());
        assertTrue(isResultSet3((MockResultSet)statement.getResultSet()));
        assertNotSame(resultSet3, statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertNull(statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        preparedStatementHandler.setExactMatchParameter(true);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet2(testResultSet));
        assertTrue(isResultSet2((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
    }
    
    public void testPrepareMultipleResultSetsClose() throws Exception
    {
        Map parameters = new HashMap();
        parameters.put(new Integer(1), new Long(1));
        parameters.put(new Integer(2), new Long(2));
        preparedStatementHandler.prepareResultSets("select xyz", new MockResultSet[] {resultSet3, resultSet2, resultSet1}, parameters);
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("select xyz from x where value = ? and y = ?");
        statement.setLong(1, 1);
        statement.setLong(2, 2);
        statement.setString(3, "3");
        statement.execute();
        MockResultSet testResultSet1 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults();
        MockResultSet testResultSet2 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults();
        MockResultSet testResultSet3 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults();
        assertTrue(testResultSet1.isClosed());
        assertTrue(testResultSet2.isClosed());
        assertTrue(testResultSet3.isClosed());
        statement.executeQuery();
        testResultSet1 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.KEEP_CURRENT_RESULT);
        testResultSet2 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.KEEP_CURRENT_RESULT);
        testResultSet3 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.KEEP_CURRENT_RESULT);
        assertFalse(testResultSet1.isClosed());
        assertFalse(testResultSet2.isClosed());
        assertFalse(testResultSet3.isClosed());
        statement.execute();
        testResultSet1 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.KEEP_CURRENT_RESULT);
        testResultSet2 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.CLOSE_ALL_RESULTS);
        testResultSet3 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.KEEP_CURRENT_RESULT);
        assertFalse(testResultSet1.isClosed());
        assertTrue(testResultSet2.isClosed());
        assertFalse(testResultSet3.isClosed());
    }
    
    public void testPrepareResultSetsStatementSet() throws Exception
    {
        preparedStatementHandler.prepareResultSet("select xyz", resultSet1);
        preparedStatementHandler.prepareResultSets("select xyz", new MockResultSet[] {resultSet3, resultSet2}, new Object[] {"1"});
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("select xyz from x where value = ? and y = ?");
        MockResultSet testResultSet1 = (MockResultSet)statement.executeQuery();
        statement.setString(1, "1");
        statement.execute();
        MockResultSet testResultSet2 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults();
        MockResultSet testResultSet3 = (MockResultSet)statement.getResultSet();
        assertSame(statement, testResultSet1.getStatement());
        assertSame(statement, testResultSet2.getStatement());
        assertSame(statement, testResultSet3.getStatement());
    }
    
    public void testPrepareResultSetsNullValues() throws Exception
    {
        List parameters = new ArrayList();
        parameters.add("25");
        preparedStatementHandler.prepareResultSets("select1", new MockResultSet[] {}, parameters);
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("select1");
        statement.setString(1, "25");
        MockResultSet testResultSet = (MockResultSet)statement.executeQuery();
        assertNull(testResultSet);
        assertNull(statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertNull(statement.getResultSet());
        preparedStatementHandler.prepareResultSet("select2", null, parameters);
        statement = (MockPreparedStatement)connection.prepareStatement("select2");
        statement.setString(1, "25");
        testResultSet = (MockResultSet)statement.executeQuery();
        assertNull(testResultSet);
        assertNull(statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertNull(statement.getResultSet());
    }
    
    public void testPrepareResultSetNullParameter() throws Exception
    {
        List params = new ArrayList();
        params.add(new Integer(2));
        params.add(null);
        preparedStatementHandler.prepareResultSet("select test", resultSet1, params);
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("select test from x where value = ? and y = ?");
        MockResultSet testResultSet = (MockResultSet)statement.executeQuery();
        assertNull(testResultSet);
        statement.setInt(1, 2);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertNull(testResultSet);
        statement.setString(2, null);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
        preparedStatementHandler.setExactMatchParameter(true);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
        statement.setString(3, null);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertNull(testResultSet);
    }
    
    public void testPrepareUpdateCount() throws Exception
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
    
    public void testPrepareMultipleUpdateCounts() throws Exception
    {
        List parameter = new ArrayList();
        parameter.add("1");
        parameter.add(new Integer(2));
        preparedStatementHandler.prepareUpdateCount("insert into", 5);
        preparedStatementHandler.prepareUpdateCounts("insert into", new int[] {1, 2, 3}, parameter);
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("insert into x(y) values(?)");
        statement.setString(1, "1");
        statement.execute();
        assertEquals(5, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(-1, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        statement.setInt(2, 2);
        int updateCount = statement.executeUpdate();
        assertEquals(1, updateCount);
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
        assertFalse(statement.getMoreResults());
    }
    
    public void testPrepareUpdateCountNullParameter() throws Exception
    {
        preparedStatementHandler.prepareUpdateCount("INSERT INTO", 4, new Object[] {null, "2"});
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("insert into x(y) values(?)");
        int testUpdateCount = statement.executeUpdate();
        assertEquals(0, testUpdateCount);
        statement.setNull(1, 1);
        testUpdateCount = statement.executeUpdate();
        assertEquals(0, testUpdateCount);
        statement.setString(2, "2");
        testUpdateCount = statement.executeUpdate();
        assertEquals(4, testUpdateCount);
        statement.setNull(3, 1);
        testUpdateCount = statement.executeUpdate();
        assertEquals(4, testUpdateCount);
        preparedStatementHandler.setExactMatchParameter(true);
        testUpdateCount = statement.executeUpdate();
        assertEquals(0, testUpdateCount);
    }
        
    public void testPrepareUpdateCountBatch() throws Exception
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
        preparedStatementHandler.prepareThrowsSQLException("update", paramMap);
        try
        {
            statement.executeBatch();
            fail();
        }
        catch(SQLException exc)
        {
            //should throw Exception
        }
    }
    
    public void testPrepareThrowsSQLException() throws Exception
    {
        SQLException exception = new SQLWarning();
        preparedStatementHandler.prepareThrowsSQLException("insert into");
        preparedStatementHandler.prepareUpdateCount("insert into", 3, new ArrayList());
        List params = new ArrayList();
        params.add("test");
        preparedStatementHandler.prepareThrowsSQLException("UPDATE", exception, params);
        preparedStatementHandler.prepareThrowsSQLException("UPDATE", new Object[] {"1", "2"});
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("insert into x(y) values(?)");
        try
        {
            statement.execute();
            fail();
        }
        catch(SQLException exc)
        {
            assertNotSame(exception, exc);
            assertTrue(exc.getMessage().indexOf("insert into") != -1);
        }
        preparedStatementHandler.setExactMatch(true);
        statement.execute();
        statement = (MockPreparedStatement)connection.prepareStatement("update");
        statement.execute();
        statement.setString(1, "test");
        try
        {
            statement.execute();
            fail();
        }
        catch(SQLException exc)
        {
            assertSame(exception, exc);
        }
        preparedStatementHandler.setCaseSensitive(true);
        statement.execute();
        preparedStatementHandler.setCaseSensitive(false);
        statement.setString(1, "1");
        statement.setString(2, "2");
        statement.setString(3, "3");
        try
        {
            statement.execute();
            fail();
        }
        catch(SQLException exc)
        {
            assertNotSame(exception, exc);
            assertTrue(exc.getMessage().indexOf("UPDATE") != -1);
        }
        preparedStatementHandler.setExactMatchParameter(true);
        statement.execute();
    }
    
    public void testPrepareGeneratedKeys() throws Exception
    {
        List params = new ArrayList();
        params.add("1");
        params.add(new Long(2));
        preparedStatementHandler.prepareGeneratedKeys("delete xyz", resultSet1);
        preparedStatementHandler.prepareGeneratedKeys("insert into", resultSet2);
        preparedStatementHandler.prepareGeneratedKeys("insert into", resultSet3, params);
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("delete xyz", Statement.RETURN_GENERATED_KEYS);
        statement.executeUpdate("delete xyz");
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
        statement.executeUpdate();
        assertTrue(isResultSet1((MockResultSet)statement.getGeneratedKeys()));
        statement.executeQuery();
        assertTrue(isResultSet1((MockResultSet)statement.getGeneratedKeys()));
        statement.execute();
        assertTrue(isResultSet1((MockResultSet)statement.getGeneratedKeys()));
        statement = (MockPreparedStatement)connection.prepareStatement("insert into xyz", Statement.RETURN_GENERATED_KEYS);
        statement.execute();
        assertTrue(isResultSet2((MockResultSet)statement.getGeneratedKeys()));
        statement.setString(1, "1");
        statement.executeQuery();
        assertTrue(isResultSet2((MockResultSet)statement.getGeneratedKeys()));
        statement.setLong(2, 2);
        statement.execute();
        assertTrue(isResultSet3((MockResultSet)statement.getGeneratedKeys()));
        statement.executeUpdate("delete xyz");
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
        statement.setLong(2, 1);
        statement.executeUpdate();
        assertTrue(isResultSet2((MockResultSet)statement.getGeneratedKeys()));
        statement.executeQuery("select");
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
        preparedStatementHandler.setExactMatch(true);
        statement.executeUpdate();
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
        preparedStatementHandler.setExactMatch(false);
        preparedStatementHandler.setExactMatchParameter(true);
        statement.setLong(2, 2);
        statement.execute();
        assertTrue(isResultSet3((MockResultSet)statement.getGeneratedKeys()));
        statement.setString(3, "3");
        statement.executeQuery();
        assertTrue(isResultSet2((MockResultSet)statement.getGeneratedKeys()));
        statement = (MockPreparedStatement)connection.prepareStatement("insert into xyz", Statement.NO_GENERATED_KEYS);
        statement.execute();
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
        statement.execute("insert into xyz", Statement.RETURN_GENERATED_KEYS);
        assertTrue(isResultSet2((MockResultSet)statement.getGeneratedKeys()));
        statement.executeQuery();
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
    }
    
    public void testPrepareGeneratedKeysBatch() throws Exception
    {
        List params = new ArrayList();
        params.add("1");
        params.add(new Long(2));
        preparedStatementHandler.prepareGeneratedKeys("insert into", resultSet2, new Object[] {"2"});
        preparedStatementHandler.prepareGeneratedKeys("insert into", resultSet3, params);
        MockPreparedStatement statement = (MockPreparedStatement)connection.prepareStatement("insert into", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, "2");
        statement.addBatch();
        statement.executeBatch();
        assertTrue(isResultSet2((MockResultSet)statement.getGeneratedKeys()));
        statement.setString(1, "3");
        statement.addBatch();
        statement.executeBatch();
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
        statement.setString(1, "1");
        statement.setLong(2, 2);
        statement.setString(3, "1");
        statement.addBatch();
        statement.executeBatch();
        assertTrue(isResultSet3((MockResultSet)statement.getGeneratedKeys()));
        statement = (MockPreparedStatement)connection.prepareStatement("insert into", Statement.NO_GENERATED_KEYS);
        statement.setString(1, "2");
        statement.addBatch();
        statement.executeBatch();
        assertTrue(isEmpty((MockResultSet)statement.getGeneratedKeys()));
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
    
    public void testGetMoreResultsSingleResultSetAndUpdateCount() throws Exception
    {
        preparedStatementHandler.prepareResultSet("select", resultSet1, new ArrayList());
        preparedStatementHandler.prepareUpdateCount("insert", 3, new ArrayList());
        MockPreparedStatement preparedStatement = (MockPreparedStatement)connection.prepareStatement("select");
        assertFalse(preparedStatement.getMoreResults());
        preparedStatement.execute();
        MockResultSet currentResult = (MockResultSet)preparedStatement.getResultSet();
        assertNotNull(currentResult);
        assertFalse(preparedStatement.getMoreResults());
        assertTrue(currentResult.isClosed());
        assertNull(preparedStatement.getResultSet());
        assertFalse(preparedStatement.getMoreResults());
        preparedStatement = (MockPreparedStatement)connection.prepareStatement("insert");
        assertEquals(-1, preparedStatement.getUpdateCount());
        preparedStatement.executeUpdate();
        assertEquals(3, preparedStatement.getUpdateCount());
        assertEquals(3, preparedStatement.getUpdateCount());
        assertFalse(preparedStatement.getMoreResults());
        assertEquals(-1, preparedStatement.getUpdateCount());
        preparedStatementHandler.prepareResultSet("selectother", resultSet1);
        preparedStatement.execute();
        preparedStatement.execute("selectother");
        assertEquals(-1, preparedStatement.getUpdateCount());
        assertNotNull(preparedStatement.getResultSet());
        assertFalse(preparedStatement.getMoreResults());
        assertEquals(-1, preparedStatement.getUpdateCount());
        assertNull(preparedStatement.getResultSet());
    }
    
    public void testGetGeneratedKeysFailure() throws Exception
    {
        MockPreparedStatement preparedStatement = (MockPreparedStatement)connection.prepareStatement("insert");
        try
        {
            preparedStatement.execute("insert", 50000);
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw exception
        }
        try
        {
            preparedStatement.executeUpdate("insert", 50000);
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw exception
        }
        preparedStatement.executeUpdate("insert", Statement.RETURN_GENERATED_KEYS);
        MockResultSet keys = (MockResultSet)preparedStatement.getGeneratedKeys();
        assertSame(preparedStatement, keys.getStatement());
        preparedStatementHandler.prepareGlobalGeneratedKeys(resultSet2);
        preparedStatement.executeUpdate("insert", new int[0]);
        keys = (MockResultSet)preparedStatement.getGeneratedKeys();
        assertTrue(isResultSet2(keys));
    }
}
