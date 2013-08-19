package com.mockrunner.test.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.jdbc.CallableStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockBlob;
import com.mockrunner.mock.jdbc.MockCallableStatement;
import com.mockrunner.mock.jdbc.MockClob;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockNClob;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.MockStruct;

public class MockCallableStatementTest extends BaseTestCase
{
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
        callableStatementHandler = connection.getCallableStatementResultSetHandler();
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        callableStatementHandler = null;
        connection = null;
        resultSet1 = null;
        resultSet2 = null;
        resultSet3 = null;
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
        statement.setNString("param2", "Test");
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
    
    public void testPrepareMultipleResultSets() throws Exception
    {
        Map parameters = new HashMap();
        parameters.put("param1", "value1");
        parameters.put(new Integer(2), new Integer(5));
        callableStatementHandler.prepareGlobalResultSets(new MockResultSet[] {resultSet3}); 
        callableStatementHandler.prepareResultSets("call", new MockResultSet[] {resultSet2, resultSet3});
        callableStatementHandler.prepareResultSets("call", new MockResultSet[] {resultSet1, resultSet2, resultSet3}, parameters);
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("xyz");
        MockResultSet resultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet3(resultSet));
        assertTrue(isResultSet3((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertNull(statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        statement = (MockCallableStatement)connection.prepareCall("do call");
        statement.setInt(2, 5);
        resultSet = (MockResultSet)statement.executeQuery();
        assertTrue(isResultSet2(resultSet));
        assertTrue(isResultSet2((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertTrue(statement.getMoreResults());
        assertTrue(isResultSet3((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertNull(statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        statement.setString("param1", "value1");
        callableStatementHandler.prepareReturnsResultSet("do call", true);
        statement.execute();
        assertTrue(isResultSet1((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertTrue(statement.getMoreResults());
        assertTrue(isResultSet2((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertTrue(statement.getMoreResults());
        assertTrue(isResultSet3((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        assertNull(statement.getResultSet());
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
        statement.setString("param1", "value2");
        statement.execute();
        assertTrue(isResultSet2((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertTrue(statement.getMoreResults());
        assertTrue(isResultSet3((MockResultSet)statement.getResultSet()));
        assertEquals(-1, statement.getUpdateCount());
        assertFalse(statement.getMoreResults());
    }
    
    public void testPrepareMultipleResultSetsClose() throws Exception
    {
        callableStatementHandler.prepareResultSets("call", new MockResultSet[] {resultSet2, resultSet3, resultSet1}, new HashMap());
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("CALL");
        statement.setString("param1", "value1");
        statement.executeQuery();
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
        statement.getMoreResults(Statement.CLOSE_CURRENT_RESULT);
        assertFalse(testResultSet1.isClosed());
        assertFalse(testResultSet2.isClosed());
        assertTrue(testResultSet3.isClosed());
    }
    
    public void testCurrentResultSetsCloseOnExecute() throws Exception
    {
        callableStatementHandler.prepareResultSets("call", new MockResultSet[] {resultSet1, resultSet2}, new HashMap());
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("CALL");
        statement.setString("param1", "value1");
        statement.executeQuery();
        MockResultSet testResultSet1 = (MockResultSet)statement.getResultSet();
        statement.getMoreResults(Statement.KEEP_CURRENT_RESULT);
        MockResultSet testResultSet2 = (MockResultSet)statement.getResultSet();
        statement.execute();
        assertTrue(testResultSet1.isClosed());
        assertTrue(testResultSet2.isClosed());
    }
    
    public void testPrepareResultSetNullParameter() throws Exception
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
    
    public void testPrepareUpdateCount() throws Exception
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
    
    public void testPrepareMultipleUpdateCounts() throws Exception
    {
        callableStatementHandler.prepareGlobalUpdateCounts(new int[] {4, 5, 6});
        callableStatementHandler.prepareUpdateCounts("doTest", new int[] {7, 8, 9});
        callableStatementHandler.prepareUpdateCounts("doTest", new int[] {10, 11}, new Object[] {"1", new Long(2)});
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("call");
        int updateCount = statement.executeUpdate();
        assertEquals(4, updateCount);
        assertEquals(4, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(5, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(6, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(-1, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        statement = (MockCallableStatement)connection.prepareCall("call dotest");
        statement.execute();
        assertEquals(7, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(8, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(9, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(-1, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        statement.setString(1, "1");
        statement.setLong(2, 2);
        updateCount = statement.executeUpdate();
        assertEquals(10, updateCount);
        assertEquals(10, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(11, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
        assertEquals(-1, statement.getUpdateCount());
        assertNull(statement.getResultSet());
        assertFalse(statement.getMoreResults());
    }
    
    public void testPrepareUpdateCountNullParameter() throws Exception
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
    
    public void testPrepareUpdateCountBatch() throws Exception
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
            assertEquals(0, exc.getUpdateCounts().length);
        }
    }
    
    public void testPrepareUpdateCountBatchFailureWithoutContinue() throws Exception
    {
        callableStatementHandler.prepareGlobalUpdateCount(5);
        callableStatementHandler.prepareUpdateCount("doTest", 4);
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("{call doTest(?, ?, ?)}");
        statement.setString(6, "xyz");
        statement.setString("1", "Test");
        statement.addBatch();
        statement.setObject(5, new MockStruct("test"));
        statement.addBatch();
        statement.addBatch();
        Map paramMap = new HashMap();
        paramMap.put(new Integer(5), new MockStruct("test"));
        callableStatementHandler.prepareThrowsSQLException("doTest", new SQLException("reason", "state", 25), paramMap);
        try
        {
            statement.executeBatch();
            fail();
        }
        catch(BatchUpdateException exc)
        {
            assertEquals(1, callableStatementHandler.getExecutedStatements().size());
            assertEquals("{call doTest(?, ?, ?)}", callableStatementHandler.getExecutedStatements().get(0));
            assertEquals(1, exc.getUpdateCounts().length);
            assertEquals(4, exc.getUpdateCounts()[0]);
            assertEquals("reason", exc.getMessage());
            assertEquals("state", exc.getSQLState());
            assertEquals(25, exc.getErrorCode());
        }
        callableStatementHandler.prepareThrowsSQLException("doTest", new BatchUpdateException(new int[9]));
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
        callableStatementHandler.prepareGlobalUpdateCount(5);
        callableStatementHandler.prepareUpdateCount("doTest", 4);
        callableStatementHandler.setContinueProcessingOnBatchFailure(true);
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("{call doTest(?, ?, ?)}");
        statement.setString(6, "xyz");
        statement.setString("1", "Test");
        statement.addBatch();
        statement.clearParameters();
        statement.setObject(5, new MockStruct("test"));
        statement.addBatch();
        statement.clearParameters();
        statement.addBatch();
        Map paramMap = new HashMap();
        paramMap.put(new Integer(5), new MockStruct("test"));
        callableStatementHandler.prepareThrowsSQLException("doTest", new SQLException("reason", "state", 25), paramMap);
        try
        {
            statement.executeBatch();
            fail();
        }
        catch(BatchUpdateException exc)
        {
            assertEquals(2, callableStatementHandler.getExecutedStatements().size());
            assertEquals("{call doTest(?, ?, ?)}", callableStatementHandler.getExecutedStatements().get(0));
            assertEquals(3, exc.getUpdateCounts().length);
            assertEquals(4, exc.getUpdateCounts()[0]);
            assertEquals(-3, exc.getUpdateCounts()[1]);
            assertEquals(4, exc.getUpdateCounts()[2]);
            assertEquals("reason", exc.getMessage());
            assertEquals("state", exc.getSQLState());
            assertEquals(25, exc.getErrorCode());
        }
        callableStatementHandler.prepareThrowsSQLException("doTest", new SQLException("xyz", "abc", 1), new HashMap());
        callableStatementHandler.setExactMatchParameter(true);
        try
        {
            statement.executeBatch();
            fail();
        }
        catch(BatchUpdateException exc)
        {
            assertEquals(3, exc.getUpdateCounts().length);
            assertEquals(4, exc.getUpdateCounts()[0]);
            assertEquals(-3, exc.getUpdateCounts()[1]);
            assertEquals(-3, exc.getUpdateCounts()[2]);
            assertEquals("xyz", exc.getMessage());
            assertEquals("abc", exc.getSQLState());
            assertEquals(1, exc.getErrorCode());
        }
    }
    
    public void testPrepareThrowsSQLException() throws Exception
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
    
    public void testPrepareOutParameter() throws Exception
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
        outParams.put("anotherParam", new MockClob("test"));
        callableStatementHandler.prepareOutParameter("doGetParam", outParams, new Object[] {"1", "2"});
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("{call doGetParam()}");
        statement.execute();
        assertEquals("xyz", statement.getString("TestParam"));
        assertEquals("xyz", statement.getNString("TestParam"));
        assertEquals("xyz", statement.getObject("TestParam"));
        assertEquals(2, statement.getInt(1));
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, statement.getBytes(3)));
        assertNull(statement.getString("1"));
        statement.setNString(1, "1");
        statement.setString(2, "2");
        assertEquals("xyz", statement.getString("TestParam"));
        assertEquals(2, statement.getInt(1));
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, statement.getBytes(3)));
        assertNull(statement.getString("1"));
        statement.execute();
        assertEquals("xyz", statement.getNString("TestParam"));
        assertEquals(5, statement.getInt(1));
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, statement.getBytes(3)));
        assertEquals(new MockNClob("test"), statement.getNClob("anotherParam"));
        assertEquals(new MockClob("test"), statement.getClob("anotherParam"));
        assertNull(statement.getString("1"));
        statement.setString(3, "3");
        callableStatementHandler.setExactMatchParameter(true);
        statement.executeQuery();
        assertEquals(2, statement.getInt(1));
        assertNull(statement.getString("1"));
        assertNull(statement.getNClob("anotherParam"));
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
    
    public void testMockResultSetOutParameter() throws Exception
    {
        Map outParams = new HashMap();
        MockResultSet resultSet = new MockResultSet("id");
        outParams.put("TestParam", resultSet);
        callableStatementHandler.prepareOutParameter("doGetParam", outParams, new Object[] {"1", "2"});
        MockCallableStatement statement = (MockCallableStatement)connection.prepareCall("{call doGetParam()}");
        statement.setString(1, "1");
        statement.setString(2, "2");
        statement.execute();
        assertSame(resultSet, statement.getObject("TestParam"));
        statement.setString(3, "3");
        statement.execute();
        assertSame(resultSet, statement.getObject("TestParam"));
        callableStatementHandler.setExactMatchParameter(true);
        statement.execute();
        assertNull(statement.getObject("TestParam"));
    }

    public void testGetMoreResultsSingleResultSetAndUpdateCount() throws Exception
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
    
    public void testClearParameters() throws Exception
    {
        MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("call");
        callableStatement.setBoolean(0, true);
        callableStatement.setInt(1, 1);
        callableStatement.setString("name", "abc");
        callableStatement.clearParameters();
        assertEquals(0, callableStatement.getIndexedParameterMap().size());
        assertEquals(0, callableStatement.getNamedParameterMap().size());
        assertEquals(0, callableStatement.getParameterMap().size());
    }
    
    public void testSetStreamParameters() throws Exception
    {
        MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("call");
        ByteArrayInputStream updateStream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5});
        callableStatement.setAsciiStream("column", updateStream, (long)2);
        InputStream inputStream = (InputStream)callableStatement.getParameterMap().get("column");
        assertEquals(1, inputStream.read());
        assertEquals(2, inputStream.read());
        assertEquals(-1, inputStream.read());
        updateStream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5});
        callableStatement.setAsciiStream(1, updateStream);
        inputStream = (InputStream)callableStatement.getParameterMap().get(new Integer(1));
        assertEquals(1, inputStream.read());
        assertEquals(2, inputStream.read());
        assertEquals(3, inputStream.read());
        assertEquals(4, inputStream.read());
        assertEquals(5, inputStream.read());
        assertEquals(-1, inputStream.read());
        updateStream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5});
        callableStatement.setBinaryStream("column", updateStream, (long)3);
        inputStream = (InputStream)callableStatement.getParameterMap().get("column");
        assertEquals(1, inputStream.read());
        assertEquals(2, inputStream.read());
        assertEquals(3, inputStream.read());
        assertEquals(-1, inputStream.read());
        StringReader updateReader = new StringReader("test");
        callableStatement.setCharacterStream(1, updateReader);
        Reader inputReader = (Reader)callableStatement.getParameterMap().get(new Integer(1));
        assertEquals('t', (char)inputReader.read());
        assertEquals('e', (char)inputReader.read());
        assertEquals('s', (char)inputReader.read());
        assertEquals('t', (char)inputReader.read());
        assertEquals(-1, inputReader.read());
        updateReader = new StringReader("test");
        callableStatement.setCharacterStream("column", updateReader, 1);
        inputReader = (Reader)callableStatement.getParameterMap().get("column");
        assertEquals('t', (char)inputReader.read());
        assertEquals(-1, inputReader.read());
        updateReader = new StringReader("test");
        callableStatement.setNCharacterStream("column", updateReader, 2);
        inputReader = (Reader)callableStatement.getParameterMap().get("column");
        assertEquals('t', (char)inputReader.read());
        assertEquals('e', (char)inputReader.read());
        assertEquals(-1, inputReader.read());
    }
    
    public void testSetBlobAndClobParameters() throws Exception
    {
        MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("call");
        callableStatement.setBlob(1, new MockBlob(new byte[] {1, 2, 3}));
        assertEquals(new MockBlob(new byte[] {1, 2, 3}), callableStatement.getParameterMap().get(new Integer(1)));
        callableStatement.setBlob("column", new ByteArrayInputStream(new byte[] {1, 2, 3}));
        assertEquals(new MockBlob(new byte[] {1, 2, 3}), callableStatement.getParameterMap().get("column"));
        callableStatement.setBlob(1, new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5}), 3);
        assertEquals(new MockBlob(new byte[] {1, 2, 3}), callableStatement.getParameterMap().get(new Integer(1)));
        callableStatement.setClob("column", new MockClob("test"));
        assertEquals(new MockClob("test"), callableStatement.getParameterMap().get("column"));
        callableStatement.setClob(2, new StringReader("test"));
        assertEquals(new MockClob("test"), callableStatement.getParameterMap().get(new Integer(2)));
        callableStatement.setClob("column", new StringReader("testxyz"), 4);
        assertEquals(new MockClob("test"), callableStatement.getParameterMap().get("column"));
        callableStatement.setNClob(3, new MockNClob("test"));
        assertEquals(new MockNClob("test"), callableStatement.getParameterMap().get(new Integer(3)));
        callableStatement.setNClob(3, new StringReader("test"));
        assertEquals(new MockNClob("test"), callableStatement.getParameterMap().get(new Integer(3)));
        callableStatement.setNClob(3, new StringReader("testxyz"), 4);
        assertEquals(new MockNClob("test"), callableStatement.getParameterMap().get(new Integer(3)));
    }
}
