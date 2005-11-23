package com.mockrunner.test.jdbc;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.jdbc.CallableStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockBlob;
import com.mockrunner.mock.jdbc.MockCallableStatement;
import com.mockrunner.mock.jdbc.MockConnection;
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
            //should throw Exception
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
}
