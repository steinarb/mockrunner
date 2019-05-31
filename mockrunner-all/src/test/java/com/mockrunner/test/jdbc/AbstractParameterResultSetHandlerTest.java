package com.mockrunner.test.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.BatchUpdateException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.jdbc.CallableStatementResultSetHandler;
import com.mockrunner.jdbc.ParameterSets;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockCallableStatement;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockParameterMap;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockResultSet;

public class AbstractParameterResultSetHandlerTest extends BaseTestCase
{
    private MockConnection connection;
    private PreparedStatementResultSetHandler preparedStatementHandler;
    private CallableStatementResultSetHandler callableStatementHandler;

    @Before
    public void setUp() throws Exception
    {
        connection = getJDBCMockObjectFactory().getMockConnection();
        preparedStatementHandler = connection.getPreparedStatementResultSetHandler();
        callableStatementHandler = connection.getCallableStatementResultSetHandler();
    }
    
    @Test
    public void testGetResultSet() throws Exception
    {
        MockResultSet result = new MockResultSet("id");
        preparedStatementHandler.prepareResultSet("select [x]", result, new String[] {"a", "b"});
        assertNull(preparedStatementHandler.getResultSet("select x"));
        preparedStatementHandler.setUseRegularExpressions(true);
        assertNull(preparedStatementHandler.getResultSet("select x"));
        MockParameterMap parameter = new MockParameterMap();
        parameter.put(1, "a");
        parameter.put(2, "b");
        assertSame(result, preparedStatementHandler.getResultSet("select x", parameter));
        preparedStatementHandler.setUseRegularExpressions(false);
        preparedStatementHandler.setExactMatchParameter(true);
        MockResultSet result1 = new MockResultSet("id1");
        MockResultSet result2 = new MockResultSet("id2");
        preparedStatementHandler.prepareResultSets("xyz", new MockResultSet[] {result1, result2}, new String[] {"a"});
        parameter = new MockParameterMap();
        assertNull(preparedStatementHandler.getResultSet("xyz", parameter));
        parameter = new MockParameterMap();
        parameter.put(1, "a");
        assertSame(result1, preparedStatementHandler.getResultSet("xyz", parameter));
    }
    
    @Test
    public void testGetResultSets() throws Exception
    {
        MockResultSet result1 = new MockResultSet("id1");
        MockResultSet result2 = new MockResultSet("id2");
        MockResultSet[] results = new MockResultSet[] {result1, result2};
        List<Object> parameterList = new ArrayList<Object>();
        parameterList.add(5);
        parameterList.add("6");
        parameterList.add("7");
        callableStatementHandler.prepareResultSets("select from", results, parameterList);
        assertNull(callableStatementHandler.getResultSets("select from"));
        MockParameterMap parameter = new MockParameterMap();
        parameter.put(1, 5);
        parameter.put(2, "6");
        assertNull(callableStatementHandler.getResultSets("select from", parameter));
        parameter.put(3, "7");
        MockResultSet[] returnedResults = callableStatementHandler.getResultSets("select from", parameter);
        assertNotSame(results, returnedResults);
        assertEquals(2, returnedResults.length);
        assertSame(result1, returnedResults[0]);
        assertSame(result2, returnedResults[1]);
        callableStatementHandler.prepareResultSets("select abc", new MockResultSet[] {result2}, new String[0]);
        returnedResults = callableStatementHandler.getResultSets("select abc", new MockParameterMap());
        assertEquals(1, returnedResults.length);
        assertSame(result2, returnedResults[0]);
        parameter = new MockParameterMap();
        parameter.put("abc", "1");
        parameter.put("def", "2");
        callableStatementHandler.prepareResultSets("select 123", new MockResultSet[] {result1}, parameter);
        returnedResults = callableStatementHandler.getResultSets("select 123", parameter);
        assertEquals(1, returnedResults.length);
        assertSame(result1, returnedResults[0]);
    }
    
    @Test
    public void testHasMultipleResultSets() throws Exception
    {
        MockResultSet result1 = new MockResultSet("id1");
        MockResultSet result2 = new MockResultSet("id2");
        List<Object> parameterList = new ArrayList<Object>();
        parameterList.add(5);
        MockParameterMap parameterMap = new MockParameterMap();
        parameterMap.put(1, 5);
        callableStatementHandler.prepareResultSet("select from", result1, parameterList);
        assertFalse(callableStatementHandler.hasMultipleResultSets("select from", new MockParameterMap()));
        assertFalse(callableStatementHandler.hasMultipleResultSets("select from", parameterMap));
        callableStatementHandler.prepareResultSets("select 123", new MockResultSet[] {result1}, parameterList);
        assertFalse(callableStatementHandler.hasMultipleResultSets("select 123", new MockParameterMap()));
        assertTrue(callableStatementHandler.hasMultipleResultSets("select 123", parameterMap));
        callableStatementHandler.prepareResultSets("select 123", new MockResultSet[] {result1, result2}, parameterList);
        assertFalse(callableStatementHandler.hasMultipleResultSets("select 123", new MockParameterMap()));
        assertTrue(callableStatementHandler.hasMultipleResultSets("select 123", parameterMap));
        parameterMap.put(2, 5);
        assertTrue(callableStatementHandler.hasMultipleResultSets("select 123", parameterMap));
        callableStatementHandler.setExactMatchParameter(true);
        assertFalse(callableStatementHandler.hasMultipleResultSets("select 123", parameterMap));
    }
    
    @Test
    public void testGetUpdateCount() throws Exception
    {
        callableStatementHandler.prepareUpdateCount("insert.*", 2, new MockParameterMap());
        assertNull(callableStatementHandler.getUpdateCount("insert.*"));
        assertEquals(new Integer(2), callableStatementHandler.getUpdateCount("insert.*", new MockParameterMap()));
        callableStatementHandler.setUseRegularExpressions(true);
        assertEquals(new Integer(2), callableStatementHandler.getUpdateCount("INSERT INTO", new MockParameterMap()));
        MockParameterMap parameter = new MockParameterMap();
        parameter.put(1, "a");
        parameter.put("2", "b");
        assertEquals(new Integer(2), callableStatementHandler.getUpdateCount("INSERT INTO", parameter));
        callableStatementHandler.setExactMatchParameter(true);
        assertNull(callableStatementHandler.getUpdateCount("INSERT INTO", parameter));
        callableStatementHandler.prepareUpdateCounts("update", new Integer[] {1, 3}, new MockParameterMap());
        assertEquals(new Integer(1), callableStatementHandler.getUpdateCount("update", new MockParameterMap()));
    }
    
    @Test
    public void testGetUpdateCounts() throws Exception
    {
        MockParameterMap parameter = new MockParameterMap();
        parameter.put(1, "1");
        preparedStatementHandler.prepareUpdateCounts("insert into", new Integer[] {3}, parameter);
        assertNull(preparedStatementHandler.getUpdateCounts("insert into", new MockParameterMap()));
        Integer[] returnedUpdateCounts = preparedStatementHandler.getUpdateCounts("insert into", parameter);
        assertEquals(1, returnedUpdateCounts.length);
        assertEquals(new Integer(3), returnedUpdateCounts[0]);
        preparedStatementHandler.prepareUpdateCounts("insert abc", new Integer[] {5, 6, 7}, new ArrayList<Object>());
        returnedUpdateCounts = preparedStatementHandler.getUpdateCounts("insert abc", parameter);
        assertEquals(3, returnedUpdateCounts.length);
        assertEquals(new Integer(5), returnedUpdateCounts[0]);
        assertEquals(new Integer(6), returnedUpdateCounts[1]);
        assertEquals(new Integer(7), returnedUpdateCounts[2]);
        preparedStatementHandler.setExactMatchParameter(true);
        assertNull(preparedStatementHandler.getUpdateCounts("insert abc", parameter));
    }
    
    @Test
    public void testHasMultipleUpdateCounts() throws Exception
    {
        MockParameterMap parameter = new MockParameterMap();
        parameter.put(1, "1");
        preparedStatementHandler.prepareUpdateCount("insert into", 3, new Object[] {"1"});
        assertFalse(preparedStatementHandler.hasMultipleUpdateCounts("insert into", new MockParameterMap()));
        assertFalse(preparedStatementHandler.hasMultipleUpdateCounts("insert into", parameter));
        preparedStatementHandler.prepareUpdateCounts("insert 123", new Integer[] {3}, new Object[] {"1"});
        assertFalse(preparedStatementHandler.hasMultipleUpdateCounts("insert 123", new MockParameterMap()));
        assertTrue(preparedStatementHandler.hasMultipleUpdateCounts("insert 123", parameter));
        preparedStatementHandler.prepareUpdateCounts("insert 123", new Integer[] {3, 5}, new Object[] {"1"});
        assertFalse(preparedStatementHandler.hasMultipleUpdateCounts("insert 123", new MockParameterMap()));
        assertTrue(preparedStatementHandler.hasMultipleUpdateCounts("insert 123", parameter));
        preparedStatementHandler.setExactMatchParameter(true);
        assertTrue(preparedStatementHandler.hasMultipleUpdateCounts("insert 123", parameter));
        parameter.put(2, "1");
        assertFalse(preparedStatementHandler.hasMultipleUpdateCounts("insert 123", parameter));
    }
    
    @Test
    public void testGetThrowsSQLException()
    {
        SQLException exc = new BatchUpdateException();
        preparedStatementHandler.prepareThrowsSQLException(".*", exc, new MockParameterMap());
        preparedStatementHandler.prepareThrowsSQLException(".*", new Object[] {"1"});
        assertFalse(preparedStatementHandler.getThrowsSQLException("select * from", new MockParameterMap()));
        assertNull(preparedStatementHandler.getSQLException("select * from", new MockParameterMap()));
        preparedStatementHandler.setUseRegularExpressions(true);
        assertTrue(preparedStatementHandler.getThrowsSQLException("select * from", new MockParameterMap()));
        assertSame(exc, preparedStatementHandler.getSQLException("select * from", new MockParameterMap()));
        assertFalse(preparedStatementHandler.getThrowsSQLException("select * from"));
        assertNull(preparedStatementHandler.getSQLException("select * from"));
        MockParameterMap parameters = new MockParameterMap();
        parameters.put(1, "1");
        assertTrue(preparedStatementHandler.getThrowsSQLException("select * from", parameters));
        assertSame(exc, preparedStatementHandler.getSQLException("select * from", parameters));
        preparedStatementHandler.setExactMatchParameter(true);
        assertNotSame(exc, preparedStatementHandler.getSQLException("select * from", parameters));
        String message = preparedStatementHandler.getSQLException("select * from", parameters).getMessage();
        assertTrue(message.contains(".*"));
        preparedStatementHandler.prepareThrowsSQLException("abc", exc, new Object[] {"1"});
        preparedStatementHandler.setUseRegularExpressions(false);
        preparedStatementHandler.setExactMatchParameter(false);
        preparedStatementHandler.setExactMatch(true);
        parameters.put(2, "2");
        assertSame(exc, preparedStatementHandler.getSQLException("abc", parameters));
        assertNull(preparedStatementHandler.getSQLException("abcxyz", parameters));
    }
    
    @Test
    public void testGetGeneratedKeys() throws Exception
    {
        MockResultSet resultSet1 = new MockResultSet("id1");
        MockResultSet resultSet2 = new MockResultSet("id2");
        preparedStatementHandler.prepareGeneratedKeys("select * from", resultSet1, new Object[] {"1"});
        MockParameterMap parameters = new MockParameterMap();
        parameters.put(1, "1");
        parameters.put("2", "2");
        preparedStatementHandler.prepareGeneratedKeys("insert into", resultSet2, parameters);
        MockParameterMap actualParameters = new MockParameterMap();
        actualParameters.put(1, "1");
        actualParameters.put("2", "3");
        actualParameters.put("3", "3");
        assertNull(preparedStatementHandler.getGeneratedKeys("insert into", actualParameters));
        actualParameters.put("2", "2");
        assertSame(resultSet2, preparedStatementHandler.getGeneratedKeys("insert into", actualParameters));
        preparedStatementHandler.setExactMatchParameter(true);
        assertNull(preparedStatementHandler.getGeneratedKeys("insert into", actualParameters));
        actualParameters.remove("3");
        assertSame(resultSet2, preparedStatementHandler.getGeneratedKeys("insert into", actualParameters));
        actualParameters = new MockParameterMap();
        actualParameters.put(1, "1");
        assertNull(preparedStatementHandler.getGeneratedKeys("insert into", actualParameters));
        assertSame(resultSet1, preparedStatementHandler.getGeneratedKeys("selECt * from", actualParameters));
        preparedStatementHandler.setCaseSensitive(true);
        assertNull(preparedStatementHandler.getGeneratedKeys("selECt * from", actualParameters));
        assertSame(resultSet1, preparedStatementHandler.getGeneratedKeys("select * from", actualParameters));
    }
    
    @Test
    public void testGetParameterMapForExecutedStatementNull() throws Exception
    {
        MockPreparedStatement preparedStatement = (MockPreparedStatement)connection.prepareStatement("select");
        preparedStatement.execute();
        assertNull(preparedStatementHandler.getParametersForExecutedStatement("select abc"));
        MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("select");
        callableStatement.execute();
        assertNull(callableStatementHandler.getParametersForExecutedStatement("select abc"));
    }
    
    @Test
    public void testGetParameterMapForExecutedStatementEmptyMapQuery() throws Exception
    {
        MockPreparedStatement preparedStatement = (MockPreparedStatement)connection.prepareStatement("select");
        preparedStatement.execute();
        assertTrue(preparedStatementHandler.getExecutedStatements().contains("select"));
        assertNotNull(preparedStatementHandler.getParametersForExecutedStatement("select"));
        assertEquals(1, preparedStatementHandler.getParametersForExecutedStatement("select").getNumberParameterSets());
        MockParameterMap parameterMap = preparedStatementHandler.getParametersForExecutedStatement("select").getParameterSet(0);
        assertEquals(0, parameterMap.size());
        preparedStatement.setByte(1, (byte)2);
        assertEquals(0, parameterMap.size());
        MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("select");
        callableStatement.execute();
        assertTrue(callableStatementHandler.getExecutedStatements().contains("select"));
        assertNotNull(callableStatementHandler.getParametersForExecutedStatement("select"));
        assertEquals(1, callableStatementHandler.getParametersForExecutedStatement("select").getNumberParameterSets());
        parameterMap = callableStatementHandler.getParametersForExecutedStatement("select").getParameterSet(0);
        assertEquals(0, parameterMap.size());
    }
    
    @Test
    public void testGetParameterMapForExecutedStatementEmptyMapUpdate() throws Exception
    {
        MockPreparedStatement preparedStatement = (MockPreparedStatement)connection.prepareStatement("update");
        preparedStatement.execute();
        assertTrue(preparedStatementHandler.getExecutedStatements().contains("update"));
        assertNotNull(preparedStatementHandler.getParametersForExecutedStatement("update"));
        assertEquals(1, preparedStatementHandler.getParametersForExecutedStatement("update").getNumberParameterSets());
        MockParameterMap parameterMap = preparedStatementHandler.getParametersForExecutedStatement("update").getParameterSet(0);
        assertEquals(0, parameterMap.size());
        preparedStatement.setString(1, "test");
        assertEquals(0, parameterMap.size());
        MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("insert");
        callableStatement.execute();
        assertTrue(callableStatementHandler.getExecutedStatements().contains("insert"));
        assertNotNull(callableStatementHandler.getParametersForExecutedStatement("insert"));
        assertEquals(1, callableStatementHandler.getParametersForExecutedStatement("insert").getNumberParameterSets());
        parameterMap = callableStatementHandler.getParametersForExecutedStatement("insert").getParameterSet(0);
        assertEquals(0, parameterMap.size());
    }
    
    @Test
    public void testGetParameterMapForExecutedStatementQuery() throws Exception
    {
        preparedStatementHandler.prepareResultSet("select", new MockResultSet("id"));
        MockPreparedStatement preparedStatement = (MockPreparedStatement)connection.prepareStatement("select");
        preparedStatement.setString(1, "test");
        preparedStatement.setInt(2, 3);
        preparedStatement.executeQuery();
        assertTrue(preparedStatementHandler.getExecutedStatements().contains("select"));
        MockParameterMap parameterMap = preparedStatementHandler.getParametersForExecutedStatement("select").getParameterSet(0);
        assertEquals(2, parameterMap.size());
        assertEquals("test", parameterMap.get(1));
        assertEquals(3, parameterMap.get(2));
        preparedStatement.setString(1, "test");
        assertEquals("test", parameterMap.get(1));
        callableStatementHandler.prepareResultSet("select", new MockResultSet("id"));
        MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("select");
        callableStatement.setBoolean(1, true);
        callableStatement.execute();
        parameterMap = callableStatementHandler.getParametersForExecutedStatement("select").getParameterSet(0);
        assertEquals(1, parameterMap.size());
        assertEquals(Boolean.TRUE, parameterMap.get(1));
    }
    
    @Test
    public void testGetParameterMapForExecutedStatementUpdate() throws Exception
    {
        preparedStatementHandler.prepareResultSet("delete", new MockResultSet("id"));
        MockPreparedStatement preparedStatement = (MockPreparedStatement)connection.prepareStatement("delete");
        preparedStatement.setBytes(1, new byte[] {1, 2, 3});
        preparedStatement.execute();
        assertTrue(preparedStatementHandler.getExecutedStatements().contains("delete"));
        MockParameterMap parameterMap = preparedStatementHandler.getParametersForExecutedStatement("delete").getParameterSet(0);
        assertEquals(1, parameterMap.size());
        assertTrue(Arrays.equals(new byte[] {1, 2, 3}, (byte[])parameterMap.get(1)));
        callableStatementHandler.prepareResultSet("insert", new MockResultSet("id"));
        MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("insert");
        callableStatement.setDate("1", new Date(1));
        callableStatement.setString(2, "test");
        callableStatement.executeUpdate();
        parameterMap = callableStatementHandler.getParametersForExecutedStatement("insert").getParameterSet(0);
        assertEquals(2, parameterMap.size());
        assertEquals(new Date(1), parameterMap.get("1"));
        assertEquals("test", parameterMap.get(2));
    }
    
    @Test
    public void testGetExecutedStatementParameterPreparedStatement() throws Exception
    {
        MockPreparedStatement preparedStatement1 = (MockPreparedStatement)connection.prepareStatement("delete");
        MockPreparedStatement preparedStatement2 = (MockPreparedStatement)connection.prepareStatement("update");
        preparedStatement2.setString(1, "1");
        preparedStatement2.setString(2, "2");
        preparedStatement1.execute();
        preparedStatement2.execute();
        Map<String, ParameterSets> parameterMap = preparedStatementHandler.getExecutedStatementParameterMap();
        assertEquals(2, parameterMap.size());
        MockParameterMap deleteParameters = parameterMap.get("delete").getParameterSet(0);
        assertEquals(0, deleteParameters.size());
        MockParameterMap updateParameters = parameterMap.get("update").getParameterSet(0);
        assertEquals(2, updateParameters.size());
        assertEquals("1", updateParameters.get(1));
        assertEquals("2", updateParameters.get(2));
    }
    
    @Test
    public void testGetExecutedStatementParameterCallableStatement() throws Exception
    {
        MockCallableStatement callableStatement1 = (MockCallableStatement)connection.prepareCall("insert");
        MockCallableStatement callableStatement2 = (MockCallableStatement)connection.prepareCall("select x");
        MockCallableStatement callableStatement3 = (MockCallableStatement)connection.prepareCall("select y");
        callableStatement1.setInt("1", 1);
        callableStatement2.setString("1", "1");
        callableStatement2.setString("2", "2");
        callableStatement3.setInt("1", 1);
        callableStatement3.setInt("2", 2);
        callableStatement3.setInt("3", 3);
        callableStatement1.execute();
        callableStatement2.execute();
        callableStatement3.execute();
        Map<String, ParameterSets> parameterMap = callableStatementHandler.getExecutedStatementParameterMap();
        assertEquals(3, parameterMap.size());
        MockParameterMap insertParameters = parameterMap.get("insert").getParameterSet(0);
        assertEquals(1, insertParameters.size());
        assertEquals(1, insertParameters.get("1"));
        MockParameterMap selectXParameters = parameterMap.get("select x").getParameterSet(0);
        assertEquals(2, selectXParameters.size());
        assertEquals("1", selectXParameters.get("1"));
        assertEquals("2", selectXParameters.get("2"));
        MockParameterMap selectYParameters = parameterMap.get("select y").getParameterSet(0);
        assertEquals(3, selectYParameters.size());
        assertEquals(1, selectYParameters.get("1"));
        assertEquals(2, selectYParameters.get("2"));
        assertEquals(3, selectYParameters.get("3"));
    }
    
    @Test
    public void testGetExecutedStatementParameterMultipleMaps() throws Exception
    {
        MockPreparedStatement preparedStatement1 = (MockPreparedStatement)connection.prepareStatement("select");
        preparedStatement1.execute();
        preparedStatement1.setString(1, "test");
        preparedStatement1.setInt(2, 3);
        preparedStatement1.execute();
        preparedStatement1.clearParameters();
        preparedStatement1.setString(1, "xyz");
        preparedStatement1.execute();
        MockPreparedStatement preparedStatement2 = (MockPreparedStatement)connection.prepareStatement("insert");
        preparedStatement2.execute();
        preparedStatement2.setString(1, "anothertest");
        preparedStatement2.execute();
        Map<String, ParameterSets> parameterMap = preparedStatementHandler.getExecutedStatementParameterMap();
        assertEquals(2, parameterMap.size());
        ParameterSets setsFor1 = parameterMap.get("select");
        assertEquals(3, setsFor1.getNumberParameterSets());
        MockParameterMap mapFor1 = setsFor1.getParameterSet(0);
        assertEquals(0, mapFor1.size());
        mapFor1 = setsFor1.getParameterSet(1);
        assertEquals(2, mapFor1.size());
        assertEquals("test", mapFor1.get(1));
        assertEquals(3, mapFor1.get(2));
        mapFor1 = setsFor1.getParameterSet(2);
        assertEquals(1, mapFor1.size());
        assertEquals("xyz", mapFor1.get(1));
        ParameterSets setsFor2 = parameterMap.get("insert");
        assertEquals(2, setsFor2.getNumberParameterSets());
        MockParameterMap mapFor2 = setsFor2.getParameterSet(0);
        assertEquals(0, mapFor2.size());
        mapFor2 = setsFor2.getParameterSet(1);
        assertEquals("anothertest", mapFor2.get(1));
    }
    
    @Test
    public void testPreparedSQLOrdered()
    {
        MockResultSet result1 = new MockResultSet("id1");
        MockResultSet result2 = new MockResultSet("id2");
        preparedStatementHandler.prepareResultSet("select", result1, new MockParameterMap());
        preparedStatementHandler.prepareResultSet("SelecT", result2, new MockParameterMap());
        preparedStatementHandler.prepareUpdateCount("SelecT", 3, new MockParameterMap());
        preparedStatementHandler.prepareUpdateCount("select2", 2, new MockParameterMap());
        preparedStatementHandler.prepareGeneratedKeys("seLECT", result1, new MockParameterMap());
        preparedStatementHandler.prepareGeneratedKeys("select", result2, new MockParameterMap());
        assertSame(result2, preparedStatementHandler.getResultSet("select", new MockParameterMap()));
        assertSame(result2, preparedStatementHandler.getResultSets("select", new MockParameterMap())[0]);
        assertEquals(new Integer(3), preparedStatementHandler.getUpdateCount("SELECT", new MockParameterMap()));
        assertEquals(new Integer(3), preparedStatementHandler.getUpdateCounts("selecT", new MockParameterMap())[0]);
        assertSame(result1, preparedStatementHandler.getGeneratedKeys("select", new MockParameterMap()));
    }
}
