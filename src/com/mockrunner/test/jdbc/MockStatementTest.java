package com.mockrunner.test.jdbc;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.jdbc.CallableStatementResultSetHandler;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockBlob;
import com.mockrunner.mock.jdbc.MockCallableStatement;
import com.mockrunner.mock.jdbc.MockClob;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.MockStatement;
import com.mockrunner.mock.jdbc.MockStruct;

public class MockStatementTest extends BaseTestCase
{
    private StatementResultSetHandler statementHandler;
    private PreparedStatementResultSetHandler preparedStatementHandler;
    private CallableStatementResultSetHandler callableStatementHandler;
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
        preparedStatementHandler = connection.getPreparedStatementResultSetHandler();
        callableStatementHandler = connection.getCallableStatementResultSetHandler();
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
    
    public void testPrepareGeneratedKeys() throws Exception
    {
        statementHandler.prepareGeneratedKeys("insert into othertable", resultSet2);
        statementHandler.prepareGeneratedKeys("insert into table", resultSet3);
        MockStatement statement = (MockStatement)connection.createStatement();
        statement.executeUpdate("inser", new int[1]);
        assertEquals(0, ((MockResultSet)statement.getGeneratedKeys()).getRowCount());
        assertSame(statement, ((MockResultSet)statement.getGeneratedKeys()).getStatement());
        statement.execute("insert into", Statement.NO_GENERATED_KEYS);
        assertEquals(0, ((MockResultSet)statement.getGeneratedKeys()).getRowCount());
        statement.executeUpdate("do insert into table xyz", new String[0]);
        assertTrue(isResultSet3((MockResultSet)statement.getGeneratedKeys()));
        assertSame(statement, ((MockResultSet)statement.getGeneratedKeys()).getStatement());
        statementHandler.setUseRegularExpressions(true);
        statement.executeUpdate("insert into table xyz", new String[0]);
        assertEquals(0, ((MockResultSet)statement.getGeneratedKeys()).getRowCount());
        statementHandler.prepareGlobalGeneratedKeys(resultSet1);
        statement.execute("insert into table xyz", Statement.RETURN_GENERATED_KEYS);
        assertTrue(isResultSet1((MockResultSet)statement.getGeneratedKeys()));
        assertSame(statement, ((MockResultSet)statement.getGeneratedKeys()).getStatement());
        statementHandler.setExactMatch(true);
        statement.executeUpdate("insert into othertable", Statement.RETURN_GENERATED_KEYS);
        assertTrue(isResultSet2((MockResultSet)statement.getGeneratedKeys()));
        assertSame(statement, ((MockResultSet)statement.getGeneratedKeys()).getStatement());
        statement.executeUpdate("insert into othertable", Statement.NO_GENERATED_KEYS);
        assertEquals(0, ((MockResultSet)statement.getGeneratedKeys()).getRowCount());
        statementHandler.clearGlobalGeneratedKeys();
        statement.executeUpdate("abc", Statement.RETURN_GENERATED_KEYS);
        assertEquals(0, ((MockResultSet)statement.getGeneratedKeys()).getRowCount());
        statementHandler.clearGeneratedKeys();
        statement.execute("insert into othertable", Statement.RETURN_GENERATED_KEYS);
        assertEquals(0, ((MockResultSet)statement.getGeneratedKeys()).getRowCount());
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
    
    public void testPrepareResultSetPreparedStatementNullParameter() throws Exception
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
    
    public void testPrepareUpdateCountPreparedStatementNullValue() throws Exception
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
    
    public void testPrepareThrowsSQLExceptionPreparedStatement() throws Exception
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
    
    public void testPrepareResultSetCallableStatement() throws Exception
    {
        callableStatementHandler.prepareGlobalResultSet(resultSet1); 
        callableStatementHandler.prepareResultSet("call", resultSet2);
        Map params = new HashMap();
        params.put(new Integer(1), new MockBlob(new byte[] {1}));
        params.put("param2", "Test");
        callableStatementHandler.prepareResultSet("{call doCall", resultSet3, params);
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("{call doCall(?, ?, ?)}");
        MockResultSet testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet2(testResultSet));
        statement.setBlob(1, new MockBlob(new byte[] {1}));
        statement.setString(2, "Test");
        statement.setString("param2", "Test");
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet3(testResultSet));
        callableStatementHandler.setExactMatchParameter(true);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet2(testResultSet));
        callableStatementHandler.setExactMatch(true);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
        assertFalse(statement.execute());
        callableStatementHandler.prepareReturnsResultSet("{call doCall(?, ?, ?)}", true);
        assertTrue(statement.execute());
        assertTrue(isResultSet1((MockResultSet)statement.getResultSet()));
        callableStatementHandler.setExactMatch(false);
        statement.clearParameters();
        statement.setBlob(1, new MockBlob(new byte[] {1}));
        statement.setString("param2", "Test");
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet3(testResultSet));
        statement = (MockCallableStatement)connection.prepareCall("{CALL doCall(?, ?, ?)}");
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet2(testResultSet));
        callableStatementHandler.setCaseSensitive(true);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
    }
    
    public void testPrepareResultSetCallableStatementNullParameter() throws Exception
    {
        Map params = new HashMap();
        params.put(new Integer(1), new MockBlob(new byte[] {1}));
        params.put("param2", null);
        callableStatementHandler.prepareResultSet("{call doCall", resultSet1, params);
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("{call doCall(?, ?, ?)}");
        MockResultSet testResultSet = (MockResultSet)statement.executeQuery();
        assertNull(testResultSet);
        statement.setBlob(1, new MockBlob(new byte[] {1}));
        statement.setNull("param2", 1);
        statement.setNull("param3", 1);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet1(testResultSet));
        callableStatementHandler.setExactMatchParameter(true);
        testResultSet = (MockResultSet)statement.executeQuery();
        assertNull(testResultSet);
    }
    
    public void testPrepareUpdateCountCallableStatement() throws Exception
    {
        callableStatementHandler.prepareGlobalUpdateCount(8);
        callableStatementHandler.prepareUpdateCount("doTest", 3);
        Map params = new HashMap();
        params.put("1", "Test");
        params.put(new Integer(5), new Long(2));
        params.put("3", new byte[] {1, 2, 3});
        callableStatementHandler.prepareUpdateCount("doTest", 4, params);
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("{call doTEST(?, ?, ?)}");
        int updateCount = statement.executeUpdate();
        assertEquals(3, updateCount);
        statement.setLong(5, 2);
        statement.setString("1", "Test");
        statement.setBytes("3", new byte[] {1, 2, 3});
        updateCount = statement.executeUpdate();
        assertEquals(4, updateCount);
        callableStatementHandler.setExactMatchParameter(true);
        updateCount = statement.executeUpdate();
        assertEquals(4, updateCount);
        statement.setLong(6, 2);
        updateCount = statement.executeUpdate();
        assertEquals(3, updateCount);
        callableStatementHandler.setCaseSensitive(true);
        updateCount = statement.executeUpdate();
        assertEquals(8, updateCount);
    }
    
    public void testPrepareUpdateCountCallableStatementNullParameter() throws Exception
    {
        Map params = new HashMap();
        params.put("1", "Test");
        params.put(new Integer(5), null);
        params.put("3", new byte[] {1, 2, 3});
        callableStatementHandler.prepareUpdateCount("doTest", 4, params);
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("{call doTEST(?, ?, ?)}");
        int updateCount = statement.executeUpdate();
        assertEquals(0, updateCount);
        statement.setString("1", "Test");
        statement.setString(5, null);
        statement.setBytes("3", new byte[] {1, 2, 3});
        updateCount = statement.executeUpdate();
        assertEquals(4, updateCount);
        callableStatementHandler.setExactMatchParameter(true);
        updateCount = statement.executeUpdate();
        assertEquals(4, updateCount);
        statement.setBytes("4", new byte[] {1, 2, 3});
        updateCount = statement.executeUpdate();
        assertEquals(0, updateCount);
    }
    
    public void testPrepareUpdateCountBatchCallableStatement() throws Exception
    {
        callableStatementHandler.prepareGlobalUpdateCount(5);
        callableStatementHandler.prepareUpdateCount("doTest", 4);
        MockStruct struct = new MockStruct("test");
        struct.addAttribute("attribute");
        Map params = new HashMap();
        params.put("1", "Test");
        params.put(new Integer(5), struct);
        params.put(new Integer(6), "xyz");
        callableStatementHandler.prepareUpdateCount("doTest", 3, params);
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("{call doTest(?, ?, ?)}");
        statement.setObject(5, struct.clone());
        statement.setString(6, "xyz");
        statement.setString("1", "Test");
        statement.addBatch();
        statement.setObject(5, new MockStruct("test"));
        statement.addBatch();
        int[] updateCounts = statement.executeBatch();
        assertTrue(updateCounts.length == 2);
        assertEquals(3, updateCounts[0]);
        assertEquals(4, updateCounts[1]);
        callableStatementHandler.prepareReturnsResultSet("call", true);
        try
        {
            statement.executeBatch();
            fail();
        }
        catch(BatchUpdateException exc)
        {
            //should throw Exception
        }
    }
    
    public void testPrepareThrowsSQLExceptionCallableStatement() throws Exception
    {
        SQLException exception = new SQLWarning();
        callableStatementHandler.prepareThrowsSQLException("doValues", exception);
        Map params = new HashMap();
        params.put("1", "Test");
        params.put(new Integer(5), new Long(2));
        params.put("3", new byte[] {1, 2, 3});
        callableStatementHandler.prepareThrowsSQLException("doTest", params);
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("{call doTEST(?, ?, ?)}");
        statement.executeQuery();
        statement.setLong(5, 2);
        statement.setString("1", "Test");
        statement.executeQuery();
        statement.setBytes("3", new byte[] {1, 2, 3});
        Map outParams = new HashMap();
        outParams.put("name", "value");
        callableStatementHandler.prepareOutParameter("doTEST", outParams);
        callableStatementHandler.prepareOutParameter("{call doValues(?, ?, ?)}", outParams);
        try
        {
            statement.executeUpdate();
            fail();
        }
        catch(SQLException exc)
        {
            assertNull(statement.getString("name"));
            assertNotSame(exception, exc);
            assertTrue(exc.getMessage().indexOf("doTest") != -1);
        }
        callableStatementHandler.setExactMatchParameter(true);
        try
        {
            statement.executeUpdate();
            fail();
        }
        catch(SQLException exc)
        {
            assertNull(statement.getString("name"));
            assertNotSame(exception, exc);
            assertTrue(exc.getMessage().indexOf("doTest") != -1);
        }
        callableStatementHandler.setCaseSensitive(true);
        statement.execute();
        assertEquals("value", statement.getString("name"));
        statement = (MockCallableStatement)connection.prepareCall("{call doValues(?, ?, ?)}");
        try
        {
            statement.executeQuery();
            fail();
        }
        catch(SQLException exc)
        {
            assertNull(statement.getString("name"));
            assertSame(exception, exc);
        }
        callableStatementHandler.setExactMatch(true);
        statement.executeQuery();
        assertEquals("value", statement.getString("name"));
    }
    
    public void testPrepareOutParameterCallableStatement() throws Exception
    {
        Map outParams = new HashMap();
        outParams.put("1", "test");
        callableStatementHandler.prepareGlobalOutParameter(outParams);
        outParams.clear();
        outParams.put("TestParam", "xyz");
        outParams.put(new Integer(1), new Integer(2));
        outParams.put(new Integer(3), new byte[] {1, 2, 3});
        callableStatementHandler.prepareOutParameter("doGetParam", outParams);
        outParams.put(new Integer(1), new Integer(5));
        callableStatementHandler.prepareOutParameter("doGetParam", outParams, new Object[] {"1", "2"});
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("{call doGetParam()}");
        statement.execute();
        assertEquals("xyz", statement.getString("TestParam"));
        assertEquals(2, statement.getInt(1));
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, statement.getBytes(3)));
        assertNull(statement.getString("1"));
        statement.setString(1, "1");
        statement.setString(2, "2");
        assertEquals("xyz", statement.getString("TestParam"));
        assertEquals(2, statement.getInt(1));
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, statement.getBytes(3)));
        assertNull(statement.getString("1"));
        statement.execute();
        assertEquals("xyz", statement.getString("TestParam"));
        assertEquals(5, statement.getInt(1));
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, statement.getBytes(3)));
        assertNull(statement.getString("1"));
        statement.setString(3, "3");
        callableStatementHandler.setExactMatchParameter(true);
        statement.executeQuery();
        assertEquals(2, statement.getInt(1));
        assertNull(statement.getString("1"));
        callableStatementHandler.setExactMatch(true);
        statement.executeUpdate();
        assertEquals("test", statement.getString("1"));
        assertFalse(statement.wasNull());
        assertNull(statement.getString("TestParam"));
        assertEquals(0, statement.getInt(1));
        assertTrue(statement.wasNull());
        assertEquals(0, statement.getInt(3));
        assertTrue(statement.wasNull());
        callableStatementHandler.setExactMatch(false);
        assertEquals("test", statement.getString("1"));
        assertNull(statement.getString("TestParam"));
        statement.executeUpdate();
        assertNull(statement.getString("1"));
        assertEquals("xyz", statement.getString("TestParam"));
    }
    
    public void testMustRegisterOutParameters() throws Exception
    {
        Map outParams = new HashMap();
        outParams.put("1", "test");
        callableStatementHandler.prepareGlobalOutParameter(outParams);
        outParams.clear();
        outParams.put("TestParam", "xyz");
        outParams.put(new Integer(1), new Integer(2));
        outParams.put(new Integer(3), new byte[] {1, 2, 3});
        callableStatementHandler.prepareOutParameter("doGetParam", outParams);
        callableStatementHandler.setMustRegisterOutParameters(true);
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("{call doGetParam()}");
        statement.execute();
        assertNull(statement.getString("TestParam"));
        assertEquals(0, statement.getInt(1));
        assertNull(statement.getBytes(3));
        assertNull(statement.getString("1"));
        statement.registerOutParameter(3, 0);
        statement.registerOutParameter("1", 0);
        statement.execute();
        assertNull(statement.getString("TestParam"));
        assertEquals(0, statement.getInt(1));
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, statement.getBytes(3)));
        assertNull(statement.getString("1"));
        statement = (MockCallableStatement)connection.prepareCall("xyz");
        statement.registerOutParameter("1", 0);
        statement.execute();
        assertNull(statement.getString("TestParam"));
        assertEquals(0, statement.getInt(1));
        assertNull(statement.getBytes(3));
        assertEquals("test", statement.getString("1"));
        statement.clearRegisteredOutParameter();
        statement.execute();
        assertNull(statement.getString("1"));
        callableStatementHandler.setMustRegisterOutParameters(false);
        statement.execute();
        assertEquals("test", statement.getString("1"));
    }
    
    public void testGetMoreResultsStatement() throws Exception
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
    
    public void testGetMoreResultsPreparedStatement() throws Exception
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
    
    public void testGetMoreResultsCallableStatement() throws Exception
    {
        callableStatementHandler.prepareResultSet("select", resultSet1);
        callableStatementHandler.prepareUpdateCount("insert", 3);
        MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("select");
        callableStatement.executeQuery();
        MockResultSet currentResult = (MockResultSet)callableStatement.getResultSet();
        assertNotNull(currentResult);
        assertFalse(callableStatement.getMoreResults(1));
        assertTrue(currentResult.isClosed());
        assertNull(callableStatement.getResultSet());
        callableStatement.execute("select");
        assertEquals(resultSet1.getId(), ((MockResultSet)callableStatement.getResultSet()).getId());
        assertEquals(resultSet1.getId(), ((MockResultSet)callableStatement.getResultSet()).getId());
        assertFalse(callableStatement.getMoreResults(1));
        assertNull(callableStatement.getResultSet());
        assertFalse(callableStatement.getMoreResults(3));
        callableStatement = (MockCallableStatement)connection.prepareCall("insert");
        assertEquals(-1, callableStatement.getUpdateCount());
        callableStatement.executeUpdate();
        assertEquals(3, callableStatement.getUpdateCount());
        assertEquals(3, callableStatement.getUpdateCount());
        callableStatement.execute("select");
        assertEquals(-1, callableStatement.getUpdateCount());
        assertNotNull(callableStatement.getResultSet());
        callableStatement.executeUpdate();
        assertNull(callableStatement.getResultSet());
        assertEquals(3, callableStatement.getUpdateCount());
        assertFalse(callableStatement.getMoreResults());
        assertNull(callableStatement.getResultSet());
        assertEquals(-1, callableStatement.getUpdateCount());
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
    
    public void testParameterCopy() throws Exception
    {
        Map params = new HashMap();
        params.put(new Integer(1), "1");
        params.put(new Integer(2), "2");
        callableStatementHandler.prepareResultSet("call1", resultSet1, params);
        MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("call1");
        params.put(new Integer(2), "3");
        callableStatement.setString(1, "1");
        callableStatement.setString(2, "3");
        MockResultSet currentResult = (MockResultSet)callableStatement.executeQuery();
        assertNull(currentResult);
        callableStatement.setString(2, "2");
        currentResult = (MockResultSet)callableStatement.executeQuery();
        assertTrue(isResultSet1(currentResult));
        params = new HashMap();
        params.put(new Integer(1), "1");
        params.put(new Integer(2), "2");
        callableStatementHandler.prepareUpdateCount("call2", 5, params);
        callableStatement = (MockCallableStatement)connection.prepareCall("call2");
        params.put(new Integer(2), "3");
        callableStatement.setString(1, "1");
        callableStatement.setString(2, "3");
        assertEquals(0, callableStatement.executeUpdate());
        callableStatement.setString(2, "2");
        assertEquals(5, callableStatement.executeUpdate());
        params = new HashMap();
        params.put(new Integer(1), "1");
        params.put(new Integer(2), "2");
        callableStatementHandler.prepareThrowsSQLException("call3", params);
        callableStatement = (MockCallableStatement)connection.prepareCall("call3");
        params.put(new Integer(2), "3");
        callableStatement.setString(1, "1");
        callableStatement.setString(2, "3");
        callableStatement.execute();
        callableStatement.setString(2, "2");
        try
        {
            callableStatement.execute();
            fail();
        } 
        catch (SQLException exc)
        {
            //should throw exception
        }
        params = new HashMap();
        params.put(new Integer(1), "1");
        params.put(new Integer(2), "2");
        callableStatementHandler.prepareOutParameter("call4", params, params);
        callableStatement = (MockCallableStatement)connection.prepareCall("call4");
        params.put(new Integer(2), "3");
        callableStatement.setString(1, "1");
        callableStatement.setString(2, "3");
        callableStatement.execute();
        assertNull(callableStatement.getString(1)); 
        assertNull(callableStatement.getString(2)); 
        callableStatement.setString(2, "2");
        callableStatement.execute(); 
        assertEquals("1", callableStatement.getString(1)); 
        assertEquals("2", callableStatement.getString(2)); 
    }
}
