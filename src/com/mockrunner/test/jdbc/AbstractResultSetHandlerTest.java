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
        MockResultSet result0 = new MockResultSet("id0");
        MockResultSet result1 = new MockResultSet("id1");
        MockResultSet[] results = new MockResultSet[] {result0, result1};
        statementHandler.prepareResultSets("select abc", results);
        assertSame(result0, statementHandler.getResultSet("select abc"));
    }
    
    public void testGetResultSets()
    {
        MockResultSet result0 = new MockResultSet("id0");
        MockResultSet result1 = new MockResultSet("id1");
        MockResultSet[] results = new MockResultSet[] {result0, result1};
        statementHandler.prepareResultSets("select column from table", results);
        statementHandler.prepareResultSet("select .*", result0);
        MockResultSet[] returnedResults = statementHandler.getResultSets("select column from table");
        assertNotSame(returnedResults, results);
        assertEquals(2, returnedResults.length);
        assertSame(result0, returnedResults[0]);
        assertSame(result1, returnedResults[1]);
        assertNull(statementHandler.getResultSet("select xyz from table"));
        returnedResults = statementHandler.getResultSets("select .*");
        assertEquals(1, returnedResults.length);
        assertSame(result0, returnedResults[0]);
    }
    
    public void testHasMultipleResultSets()
    {
        MockResultSet result0 = new MockResultSet("id0");
        MockResultSet result1 = new MockResultSet("id1");
        statementHandler.prepareResultSets("select column from table", new MockResultSet[] {result0, result1});
        statementHandler.prepareResultSets("select column from abc", new MockResultSet[] {result1});
        statementHandler.prepareResultSet("select .*", result0);
        assertTrue(statementHandler.hasMultipleResultSets("select column from table"));
        assertTrue(statementHandler.hasMultipleResultSets("select column from abc"));
        assertFalse(statementHandler.hasMultipleResultSets("select .*"));
        assertFalse(statementHandler.hasMultipleResultSets("do nothing"));
    }
    
    public void testGetGlobalResultSet()
    {
        MockResultSet result0 = new MockResultSet("id0");
        MockResultSet result1 = new MockResultSet("id1");
        MockResultSet[] results = new MockResultSet[] {result0, result1};
        statementHandler.prepareGlobalResultSet(result0);
        assertSame(result0, statementHandler.getGlobalResultSet());
        MockResultSet[] returnedResults = statementHandler.getGlobalResultSets();
        assertEquals(1, returnedResults.length);
        assertSame(result0, returnedResults[0]);
        statementHandler.prepareGlobalResultSets(results);
        assertSame(result0, statementHandler.getGlobalResultSet());
        returnedResults = statementHandler.getGlobalResultSets();
        assertNotSame(results, returnedResults);
        assertEquals(2, returnedResults.length);
        assertSame(result0, returnedResults[0]);
        assertSame(result1, returnedResults[1]);
    }
    
    public void testHasMutipleGlobalResultSets()
    {
        MockResultSet result0 = new MockResultSet("id0");
        MockResultSet result1 = new MockResultSet("id1");
        statementHandler.prepareGlobalResultSet(result0);
        assertFalse(statementHandler.hasMultipleGlobalResultSets());
        statementHandler.prepareGlobalResultSets(new MockResultSet[] {result1});
        assertTrue(statementHandler.hasMultipleGlobalResultSets());
        statementHandler.prepareGlobalResultSets(new MockResultSet[] {result0, result1});
        assertTrue(statementHandler.hasMultipleGlobalResultSets());
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
        statementHandler.prepareUpdateCounts("update", new int[] {1, 2});
        assertEquals(new Integer(1), statementHandler.getUpdateCount("update"));
    }
    
    public void testGetUpdateCounts()
    {
        int[] updateCounts = new int[] {1, 2};
        statementHandler.prepareUpdateCounts(".*", updateCounts);
        statementHandler.prepareUpdateCount("insert xyz", 4);
        Integer[] returnedUpdateCounts = statementHandler.getUpdateCounts(".*");
        assertEquals(2, returnedUpdateCounts.length);
        assertEquals(new Integer(1), returnedUpdateCounts[0]);
        assertEquals(new Integer(2), returnedUpdateCounts[1]);
        returnedUpdateCounts = statementHandler.getUpdateCounts("insert xyz");
        assertEquals(1, returnedUpdateCounts.length);
        assertEquals(new Integer(4), returnedUpdateCounts[0]);
        assertNull(statementHandler.getUpdateCounts("do nothing"));
    }
    
    public void testHasMultipleUpdateCounts()
    {
        statementHandler.prepareUpdateCount("update", 3);
        statementHandler.prepareUpdateCounts("insert into xyz", new int [] {4});
        statementHandler.prepareUpdateCounts("insert into abc", new int [] {1, 2, 4});
        assertFalse(statementHandler.hasMultipleUpdateCounts("update"));
        assertFalse(statementHandler.hasMultipleUpdateCounts("do nothing"));
        assertTrue(statementHandler.hasMultipleUpdateCounts("insert into xyz"));
        assertTrue(statementHandler.hasMultipleUpdateCounts("insert into abc"));
    }
    
    public void testGetGlobalUpdateCount()
    {
        int[] updateCounts = new int[] {1, 2};
        statementHandler.prepareGlobalUpdateCount(4);
        assertEquals(4, statementHandler.getGlobalUpdateCount());
        int[] returnedUpdateCounts = statementHandler.getGlobalUpdateCounts();
        assertEquals(1, returnedUpdateCounts.length);
        assertEquals(4, returnedUpdateCounts[0]);
        statementHandler.prepareGlobalUpdateCounts(updateCounts);
        assertEquals(1, statementHandler.getGlobalUpdateCount());
        returnedUpdateCounts = statementHandler.getGlobalUpdateCounts();
        assertNotSame(updateCounts, returnedUpdateCounts);
        assertEquals(2, returnedUpdateCounts.length);
        assertEquals(1, returnedUpdateCounts[0]);
        assertEquals(2, returnedUpdateCounts[1]);
    }
    
    public void testHasMutipleGlobalUpdateCounts()
    {
        statementHandler.prepareGlobalUpdateCount(4);
        assertFalse(statementHandler.hasMultipleGlobalUpdateCounts());
        statementHandler.prepareGlobalUpdateCounts(new int[] {1});
        assertTrue(statementHandler.hasMultipleGlobalUpdateCounts());
        statementHandler.prepareGlobalUpdateCounts(new int[] {7, 8});
        assertTrue(statementHandler.hasMultipleGlobalUpdateCounts());
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
    
    public void testClearMethods()
    {
        statementHandler.prepareResultSet("select", new MockResultSet("id"));
        statementHandler.prepareUpdateCount("select", 3);
        statementHandler.prepareThrowsSQLException("select");
        statementHandler.prepareReturnsResultSet("select", true);
        statementHandler.prepareGeneratedKeys("select", new MockResultSet("id"));
        statementHandler.clearResultSets();
        statementHandler.clearUpdateCounts();
        statementHandler.clearThrowsSQLException();
        statementHandler.clearReturnsResultSet();
        statementHandler.clearGeneratedKeys();
        assertNull(statementHandler.getResultSet("select"));
        assertNull(statementHandler.getUpdateCount("select"));
        assertFalse(statementHandler.getThrowsSQLException("select"));
        assertNull(statementHandler.getReturnsResultSet("select"));
        assertNull(statementHandler.getGeneratedKeys("select"));
    }
}
