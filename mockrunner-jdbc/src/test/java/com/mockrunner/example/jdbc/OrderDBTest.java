package com.mockrunner.example.jdbc;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;
import com.mockrunner.jdbc.CallableStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Example test for {@link OrderDB}. Demonstrates the usage of 
 * {@link com.mockrunner.jdbc.JDBCTestModule} 
 * and {@link com.mockrunner.jdbc.BasicJDBCTestCaseAdapter} with
 * stored procdures.
 */
public class OrderDBTest extends BasicJDBCTestCaseAdapter
{
    private CallableStatementResultSetHandler statementHandler;
    
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        statementHandler = getJDBCMockObjectFactory().getMockConnection().getCallableStatementResultSetHandler();
    }
    
    private void prepareResult()
    {
        MockResultSet result = statementHandler.createResultSet();
        result.addColumn("name", new String[] {"MyName1", "MyName2", "MyName3"});
        statementHandler.prepareResultSet("call getnames", result);
    }
    
    public void breakingTestCallStoredProc() throws Exception
    {
        prepareResult();
        List result = OrderDB.getNames(Date.valueOf("2004-01-01"));
        assertEquals("MyName1", result.get(0));
        assertEquals("MyName2", result.get(1));
        assertEquals("MyName3", result.get(2));
        verifyCallableStatementParameter("call getnames", 1, Date.valueOf("2004-01-01"));
        verifyCallableStatementClosed("call getnames");
        verifyAllResultSetsClosed();
        verifyConnectionClosed();
    }
    
    @Test
    public void testDummy() {
    	
    }
}
