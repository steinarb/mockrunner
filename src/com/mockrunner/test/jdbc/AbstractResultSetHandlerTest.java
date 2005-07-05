package com.mockrunner.test.jdbc;

import java.sql.BatchUpdateException;
import java.sql.SQLException;

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
    
    public void testGetGeneratedKeys()
    {
        MockResultSet result = new MockResultSet("id");
        statementHandler.prepareGeneratedKeys("insert into table", result);
        statementHandler.prepareGeneratedKeys("insert .*", result);
        assertSame(result, statementHandler.getGeneratedKeys("insert into table abc"));
        assertSame(result, statementHandler.getGeneratedKeys("insert .*"));
        assertNull(statementHandler.getGeneratedKeys("insert into othertable"));
        statementHandler.setUseRegularExpressions(true);
        assertSame(result, statementHandler.getGeneratedKeys("insert into table"));
        assertSame(result, statementHandler.getGeneratedKeys("INSERt regular"));
        statementHandler.setCaseSensitive(true);
        assertNull(statementHandler.getGeneratedKeys("INSERt regular"));
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
	    SQLException exc = new BatchUpdateException();
        statementHandler.prepareThrowsSQLException("[abc] statement", exc);
        statementHandler.prepareThrowsSQLException("[abc] statementxyz");
	    assertFalse(statementHandler.getThrowsSQLException("a stAtement"));
        assertNull(statementHandler.getSQLException("a stAtement"));
	    statementHandler.setUseRegularExpressions(true);
	    assertTrue(statementHandler.getThrowsSQLException("a stAtement"));
        assertSame(exc, statementHandler.getSQLException("a stAtement"));
	    statementHandler.setCaseSensitive(true);
	    assertFalse(statementHandler.getThrowsSQLException("a stAtement"));
        assertNull(statementHandler.getSQLException("a stAtement"));
	    assertTrue(statementHandler.getThrowsSQLException("b statement"));
        assertSame(exc, statementHandler.getSQLException("b statement"));
        assertTrue(statementHandler.getThrowsSQLException("b statementxyz"));
        assertNotSame(exc, statementHandler.getSQLException("b statementxyz"));
        String message = statementHandler.getSQLException("b statementxyz").getMessage();
        assertTrue(message.indexOf("[abc] statementxyz") != -1);
	}
}
