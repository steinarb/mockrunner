package com.mockrunner.test.jdbc;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mockrunner.base.MockObjectFactory;
import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockStatement;

import junit.framework.TestCase;

public class JDBCTestModuleTest extends TestCase
{
    private MockObjectFactory mockfactory;
    private JDBCTestModule module;

    protected void setUp() throws Exception
    {
        super.setUp();
        mockfactory = new MockObjectFactory();
        module = new JDBCTestModule(mockfactory);
    }
    
    private void preparePreparedStatements() throws Exception
    {   
        mockfactory.getMockConnection().prepareStatement("INSERT INTO TEST (COL1, COL2) VALUES(?, ?)");
        mockfactory.getMockConnection().prepareStatement("insert into test (col1, col2, col3) values(?, ?, ?)");
        mockfactory.getMockConnection().prepareStatement("update mytable set test = test + ? where id = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
    }
    
    private void prepareStatements() throws Exception
    {   
        mockfactory.getMockConnection().createStatement();
        mockfactory.getMockConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }
    
    public void testGetStatements() throws Exception
    {
        List statements = module.getStatements();
        assertNotNull(statements);
        assertEquals(0, statements.size());
        assertNull(module.getStatement(1));
        module.verifyNumberStatements(0);
        prepareStatements();
        statements = module.getStatements();
        assertNotNull(statements);
        assertEquals(2, statements.size());
        assertNotNull(module.getStatement(0));
        assertNotNull(module.getStatement(1));
        module.verifyNumberStatements(2);
    }
    
    public void testGetPreparedStatementsByIndex() throws Exception
    {
        List statements = module.getPreparedStatements();
        assertNotNull(statements);
        assertEquals(0, statements.size());
        assertNull(module.getPreparedStatement(1));
        module.verifyNumberPreparedStatements(0);
        preparePreparedStatements();
        statements = module.getPreparedStatements();
        assertNotNull(statements);
        assertEquals(3, statements.size());
        module.verifyNumberPreparedStatements(3);  
    }
    
    public void testGetPreparedStatementsBySQL() throws Exception
    {
        preparePreparedStatements();
        List statements = module.getPreparedStatements("insert");
        assertNotNull(statements);
        assertEquals(2, statements.size());
        MockPreparedStatement statement = module.getPreparedStatement("insert");
        assertEquals("INSERT INTO TEST (COL1, COL2) VALUES(?, ?)", statement.getSQL());
        module.verifyNumberPreparedStatements(1, "update");
        module.verifyNumberPreparedStatements(1, "UPDATE");
        module.verifyNumberPreparedStatements(2, "insert");
        module.verifyNumberPreparedStatements(3);
        module.verifyPreparedStatementPresent("update");
        module.verifyPreparedStatementNotPresent("select");
        module.setCaseSensitive(true);
        statements = module.getPreparedStatements("insert");
        assertNotNull(statements);
        assertEquals(1, statements.size());
        statement = module.getPreparedStatement("insert");
        assertEquals("insert into test (col1, col2, col3) values(?, ?, ?)", statement.getSQL());
        module.verifyNumberPreparedStatements(1, "update");
        module.verifyNumberPreparedStatements(0, "UPDATE");
        module.verifyNumberPreparedStatements(1, "insert");
        module.verifyNumberPreparedStatements(1, "INSERT");
        module.verifyNumberPreparedStatements(3);
        module.setExactMatch(true);
        statements = module.getPreparedStatements("insert");
        assertNotNull(statements);
        assertEquals(0, statements.size());
        module.verifyNumberPreparedStatements(0, "update");
        module.verifyNumberPreparedStatements(0, "UPDATE");
        module.verifyNumberPreparedStatements(0, "insert");
        module.verifyNumberPreparedStatements(0, "INSERT");
        module.verifyPreparedStatementNotPresent("update");
        module.verifyPreparedStatementPresent("insert into test (col1, col2, col3) values(?, ?, ?)");
    }
    
    public void testGetPreparedStatementObjects() throws Exception
    {
        preparePreparedStatements();
        MockPreparedStatement statement = module.getPreparedStatement("update");  
        statement.setInt(1, 3);
        statement.setLong(2, 10000);
        assertEquals(new Integer(3), statement.getParameter(1));
        assertEquals(new Long(10000), statement.getParameter(2));
        module.verifyPreparedStatementParameterPresent(statement, 1);
        module.verifyPreparedStatementParameterNotPresent("update", 3);
        module.verifyPreparedStatementParameterNotPresent(0, 1);
        module.verifyPreparedStatementParameter(statement, 1, new Integer(3));
        module.verifyPreparedStatementParameter(2, 2, new Long(10000));
        statement = module.getPreparedStatement("INSERT INTO TEST (COL1, COL2) VALUES(?, ?)");  
        statement.setString(1, "test1");
        statement.setString(2, "test2");
        module.verifyPreparedStatementParameterPresent(statement, 2);
        module.verifyPreparedStatementParameterNotPresent(statement, 3);
        module.verifyPreparedStatementParameter(0, 1, "test1");
    }
    
    public void testStatementsClosed() throws Exception
    {
        prepareStatements();
        preparePreparedStatements();
        MockStatement statement = module.getStatement(0);
        MockPreparedStatement preparedStatement = module.getPreparedStatement("update");
        statement.close();
        preparedStatement.close();
        module.verifyStatementClosed(0);
        module.verifyPreparedStatementClosed("update");
        try
        {
            module.verifyAllStatementsClosed();
            fail();
        }
        catch(Exception exc)
        {
            //should throw Exception
        }
        List statements = new ArrayList();
        statements.addAll(module.getStatements());
        statements.addAll(module.getPreparedStatements());
        for(int ii = 0; ii < statements.size(); ii++)
        {
            ((MockStatement)statements.get(ii)).close();
        }
        module.verifyAllStatementsClosed();
        mockfactory.getMockConnection().close();
        module.verifyConnectionClosed();
    }
}
