package com.mockrunner.test.jdbc;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.jdbc.CallableStatementResultSetHandler;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockCallableStatement;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockResultSet;

public class AbstractParameterResultSetHandlerTest extends BaseTestCase
{
	private MockConnection connection;
	private PreparedStatementResultSetHandler preparedStatementHandler;
	private CallableStatementResultSetHandler callableStatementHandler;

	protected void setUp() throws Exception
	{
		super.setUp();
		connection = getJDBCMockObjectFactory().getMockConnection();
		preparedStatementHandler = connection.getPreparedStatementResultSetHandler();
		callableStatementHandler = connection.getCallableStatementResultSetHandler();
	}
	
	public void testGetParameterMapForExecutedStatementNull() throws Exception
	{
		MockPreparedStatement preparedStatement = (MockPreparedStatement)connection.prepareStatement("select");
		preparedStatement.execute();
		assertNull(preparedStatementHandler.getParameterListForExecutedStatement("select abc"));
		MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("select");
		callableStatement.execute();
		assertNull(callableStatementHandler.getParameterListForExecutedStatement("select abc"));
	}
	
	public void testGetParameterMapForExecutedStatementEmptyMapQuery() throws Exception
	{
		MockPreparedStatement preparedStatement = (MockPreparedStatement)connection.prepareStatement("select");
		preparedStatement.execute();
		assertTrue(preparedStatementHandler.getExecutedStatements().contains("select"));
		assertNotNull(preparedStatementHandler.getParameterListForExecutedStatement("select"));
		assertEquals(1, preparedStatementHandler.getParameterListForExecutedStatement("select").size());
		Map parameterMap = (Map)preparedStatementHandler.getParameterListForExecutedStatement("select").get(0);
		assertEquals(0, parameterMap.size());
		preparedStatement.setByte(1, (byte)2);
		assertEquals(0, parameterMap.size());
		MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("select");
		callableStatement.execute();
		assertTrue(callableStatementHandler.getExecutedStatements().contains("select"));
		assertNotNull(callableStatementHandler.getParameterListForExecutedStatement("select"));
		assertEquals(1, callableStatementHandler.getParameterListForExecutedStatement("select").size());
		parameterMap = (Map)callableStatementHandler.getParameterListForExecutedStatement("select").get(0);
		assertEquals(0, parameterMap.size());
	}
	
	public void testGetParameterMapForExecutedStatementEmptyMapUpdate() throws Exception
	{
		MockPreparedStatement preparedStatement = (MockPreparedStatement)connection.prepareStatement("update");
		preparedStatement.execute();
		assertTrue(preparedStatementHandler.getExecutedStatements().contains("update"));
		assertNotNull(preparedStatementHandler.getParameterListForExecutedStatement("update"));
		assertEquals(1, preparedStatementHandler.getParameterListForExecutedStatement("update").size());
		Map parameterMap = (Map)preparedStatementHandler.getParameterListForExecutedStatement("update").get(0);
		assertEquals(0, parameterMap.size());
		preparedStatement.setString(1, "test");
		assertEquals(0, parameterMap.size());
		MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("insert");
		callableStatement.execute();
		assertTrue(callableStatementHandler.getExecutedStatements().contains("insert"));
		assertNotNull(callableStatementHandler.getParameterListForExecutedStatement("insert"));
		assertEquals(1, callableStatementHandler.getParameterListForExecutedStatement("insert").size());
		parameterMap = (Map)callableStatementHandler.getParameterListForExecutedStatement("insert").get(0);
		assertEquals(0, parameterMap.size());
	}
	
	public void testGetParameterMapForExecutedStatementQuery() throws Exception
	{
		preparedStatementHandler.prepareResultSet("select", new MockResultSet("id"));
		MockPreparedStatement preparedStatement = (MockPreparedStatement)connection.prepareStatement("select");
		preparedStatement.setString(1, "test");
		preparedStatement.setInt(2, 3);
		preparedStatement.executeQuery();
		assertTrue(preparedStatementHandler.getExecutedStatements().contains("select"));
		Map parameterMap = (Map)preparedStatementHandler.getParameterListForExecutedStatement("select").get(0);
		assertEquals(2, parameterMap.size());
		assertEquals("test", parameterMap.get(new Integer(1)));
		assertEquals(new Integer(3), parameterMap.get(new Integer(2)));
		preparedStatement.setString(1, "test");
		assertEquals("test", parameterMap.get(new Integer(1)));
		callableStatementHandler.prepareResultSet("select", new MockResultSet("id"));
		MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("select");
		callableStatement.setBoolean(1, true);
		callableStatement.execute();
		parameterMap = (Map)callableStatementHandler.getParameterListForExecutedStatement("select").get(0);
		assertEquals(1, parameterMap.size());
		assertEquals(new Boolean(true), parameterMap.get(new Integer(1)));
	}
	
	public void testGetParameterMapForExecutedStatementUpdate() throws Exception
	{
		preparedStatementHandler.prepareResultSet("delete", new MockResultSet("id"));
		MockPreparedStatement preparedStatement = (MockPreparedStatement)connection.prepareStatement("delete");
		preparedStatement.setBytes(1, new byte[] {1, 2, 3});
		preparedStatement.execute();
		assertTrue(preparedStatementHandler.getExecutedStatements().contains("delete"));
		Map parameterMap = (Map)preparedStatementHandler.getParameterListForExecutedStatement("delete").get(0);
		assertEquals(1, parameterMap.size());
		assertTrue(Arrays.equals(new byte[] {1, 2, 3}, (byte[])parameterMap.get(new Integer(1))));
		callableStatementHandler.prepareResultSet("insert", new MockResultSet("id"));
		MockCallableStatement callableStatement = (MockCallableStatement)connection.prepareCall("insert");
		callableStatement.setDate("1", new Date(1));
		callableStatement.setString(2, "test");
		callableStatement.executeUpdate();
		parameterMap = (Map)callableStatementHandler.getParameterListForExecutedStatement("insert").get(0);
		assertEquals(2, parameterMap.size());
		assertEquals(new Date(1), parameterMap.get("1"));
		assertEquals("test", parameterMap.get(new Integer(2)));
	}
	
	public void testGetExecutedStatementParameterPreparedStatement() throws Exception
	{
		MockPreparedStatement preparedStatement1 = (MockPreparedStatement)connection.prepareStatement("delete");
		MockPreparedStatement preparedStatement2 = (MockPreparedStatement)connection.prepareStatement("update");
		preparedStatement2.setString(1, "1");
		preparedStatement2.setString(2, "2");
		preparedStatement1.execute();
		preparedStatement2.execute();
		Map parameterMap = preparedStatementHandler.getExecutedStatementParameter();
		assertEquals(2, parameterMap.size());
		Map deleteParameters = (Map)((List)parameterMap.get("delete")).get(0);
		assertEquals(0, deleteParameters.size());
		Map updateParameters = (Map)((List)parameterMap.get("update")).get(0);
		assertEquals(2, updateParameters.size());
		assertEquals("1", updateParameters.get(new Integer(1)));
		assertEquals("2", updateParameters.get(new Integer(2)));
	}
	
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
		Map parameterMap = callableStatementHandler.getExecutedStatementParameter();
		assertEquals(3, parameterMap.size());
		Map insertParameters = (Map)((List)parameterMap.get("insert")).get(0);
		assertEquals(1, insertParameters.size());
		assertEquals(new Integer(1), insertParameters.get("1"));
		Map selectXParameters = (Map)((List)parameterMap.get("select x")).get(0);
		assertEquals(2, selectXParameters.size());
		assertEquals("1", selectXParameters.get("1"));
		assertEquals("2", selectXParameters.get("2"));
		Map selectYParameters = (Map)((List)parameterMap.get("select y")).get(0);
		assertEquals(3, selectYParameters.size());
		assertEquals(new Integer(1), selectYParameters.get("1"));
		assertEquals(new Integer(2), selectYParameters.get("2"));
		assertEquals(new Integer(3), selectYParameters.get("3"));
	}
	
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
		Map parameterMap = preparedStatementHandler.getExecutedStatementParameter();
		assertEquals(2, parameterMap.size());
		List listFor1 = (List)parameterMap.get("select");
		assertEquals(3, listFor1.size());
		Map mapFor1 = (Map)listFor1.get(0);
		assertEquals(0, mapFor1.size());
		mapFor1 = (Map)listFor1.get(1);
		assertEquals(2, mapFor1.size());
		assertEquals("test", mapFor1.get(new Integer(1)));
		assertEquals(new Integer(3), mapFor1.get(new Integer(2)));
		mapFor1 = (Map)listFor1.get(2);
		assertEquals(1, mapFor1.size());
		assertEquals("xyz", mapFor1.get(new Integer(1)));
		List listFor2 = (List)parameterMap.get("insert");
		assertEquals(2, listFor2.size());
		Map mapFor2 = (Map)listFor2.get(0);
		assertEquals(0, mapFor2.size());
		mapFor2 = (Map)listFor2.get(1);
		assertEquals("anothertest", mapFor2.get(new Integer(1)));
	}
}
