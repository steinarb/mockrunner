package com.mockrunner.test.jdbc;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;

public class AbstractResultSetHandlerTest extends BaseTestCase
{
    private MockConnection connection;
	private StatementResultSetHandler statementHandler;

	protected void setUp() throws Exception
	{
		super.setUp();
		connection = getJDBCMockObjectFactory().getMockConnection();
		statementHandler = connection.getStatementResultSetHandler();
	}
	
	public void testGetResultSet()
	{
	    MockResultSet result = new MockResultSet("id");
	    statementHandler.prepareResultSet("select column from table", result);
	    statementHandler.prepareResultSet("select .*", result);
	    assertSame(result, statementHandler.getResultSet("select column from table"));
	    assertNull(statementHandler.getResultSet("select xyz from table"));
	    statementHandler.setUseRegularExpressions(true);
	    assertSame(result, statementHandler.getResultSet("select column from table"));
	    assertSame(result, statementHandler.getResultSet("SELECT xyz from table"));
	    statementHandler.setCaseSensitive(true);
	    assertNull(statementHandler.getResultSet("SELECT xyz from table"));
	}
	
	public void testGetUpdateCount()
	{
	    statementHandler.prepareUpdateCount(".*", 3);
	    statementHandler.prepareUpdateCount("insert xyz", 4);
	    assertEquals(new Integer(4), statementHandler.getUpdateCount("insert xyz"));
	    assertEquals(new Integer(3), statementHandler.getUpdateCount(".*"));
	    statementHandler.setUseRegularExpressions(true);
	    assertEquals(new Integer(3), statementHandler.getUpdateCount("insert xyz"));
	    assertEquals(new Integer(3), statementHandler.getUpdateCount("insert"));
	    statementHandler.setExactMatch(true);
	    assertEquals(new Integer(4), statementHandler.getUpdateCount("insert Xyz"));
	    assertNull(statementHandler.getUpdateCount("insert"));
	}
	
	public void testGetReturnsResultSet()
	{
	    statementHandler.prepareReturnsResultSet("select.*", false);
	    assertNull(statementHandler.getReturnsResultSet("select abc"));
	    statementHandler.setUseRegularExpressions(true);
	    assertEquals(new Boolean(false), statementHandler.getReturnsResultSet("select abc"));
	}
	
	public void testGetThrowsSQLException()
	{
	    statementHandler.prepareThrowsSQLException("[abc] statement");
	    assertFalse(statementHandler.getThrowsSQLException("a stAtement"));
	    statementHandler.setUseRegularExpressions(true);
	    assertTrue(statementHandler.getThrowsSQLException("a stAtement"));
	    statementHandler.setCaseSensitive(true);
	    assertFalse(statementHandler.getThrowsSQLException("a stAtement"));
	    assertTrue(statementHandler.getThrowsSQLException("b statement"));
	}
}
