package com.mockrunner.test.jdbc;

import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockBlob;
import com.mockrunner.mock.jdbc.MockCallableStatement;
import com.mockrunner.mock.jdbc.MockClob;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockStatement;

public class JDBCTestModuleTest extends TestCase
{
    private JDBCMockObjectFactory mockfactory;
    private JDBCTestModule module;

    protected void setUp() throws Exception
    {
        super.setUp();
        mockfactory = new JDBCMockObjectFactory();
        module = new JDBCTestModule(mockfactory);
    }
    
    private void prepareCallableStatements() throws Exception
    {   
        mockfactory.getMockConnection().prepareCall("{call getData(?, ?, ?, ?)}");
        mockfactory.getMockConnection().prepareCall("{call setData(?, ?, ?, ?)}");
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
        statement.setBytes(3, new byte[] {1, 2, 3});
        statement.setBytes(4, new byte[] {});
        module.verifyPreparedStatementParameterPresent(statement, 2);
        module.verifyPreparedStatementParameterPresent(statement, 3);
        module.verifyPreparedStatementParameterPresent(statement, 4);
        module.verifyPreparedStatementParameterNotPresent(statement, 5);
        module.verifyPreparedStatementParameter(0, 3, new byte[] {1, 2, 3});
        module.verifyPreparedStatementParameter(0, 4, new byte[] {});
    }
    
    public void testGetCallableStatementsByIndex() throws Exception
    {
        module.verifyNumberCallableStatements(0);
        prepareCallableStatements();
        module.verifyNumberCallableStatements(2);
        List statements = module.getCallableStatements();
        assertEquals("{call getData(?, ?, ?, ?)}", ((MockCallableStatement)statements.get(0)).getSQL());
        assertEquals("{call setData(?, ?, ?, ?)}", ((MockCallableStatement)statements.get(1)).getSQL());
    }
    
    public void testGetCallableStatementsBySQL() throws Exception
    {
        prepareCallableStatements();
        List statements = module.getCallableStatements("call");
        assertTrue(statements.size() == 2);
        MockCallableStatement statement = module.getCallableStatement("CALL");
        assertEquals("{call getData(?, ?, ?, ?)}", statement.getSQL());
        module.setCaseSensitive(true);
        statement = module.getCallableStatement("CALL");
        assertNull(statement);
        module.setCaseSensitive(false);
        module.setExactMatch(true);
        statement = module.getCallableStatement("CALL");
        assertNull(statement);
        statements = module.getCallableStatements("{call setData(?, ?, ?, ?)}");
        assertTrue(statements.size() == 1);
        module.setExactMatch(false);
        module.verifyNumberCallableStatements(1, "call getData");
        module.verifyNumberCallableStatements(2, "call");
        module.verifyCallableStatementPresent("call setData");
        module.verifyCallableStatementNotPresent("call setXYZ");
    }
    
    public void testGetCallableStatementObjects() throws Exception
    {
        prepareCallableStatements();
        MockCallableStatement statement = module.getCallableStatement("{call setData(?, ?, ?, ?)}");
        statement.setInt("xyz", 1);
        statement.setString("3", "xyz");
        statement.setString(1, "xyz");
        Map namedParameter = statement.getNamedParameterMap();
        Map indexedParameter = statement.getIndexedParameterMap();
        assertTrue(namedParameter.size() == 2);
        assertEquals(new Integer(1), namedParameter.get("xyz"));
        assertEquals("xyz", namedParameter.get("3"));
        assertTrue(indexedParameter.size() == 1);
        assertEquals("xyz", indexedParameter.get(new Integer(1)));
        module.verifyCallableStatementParameterPresent(1, 1);
        try
        {
            module.verifyCallableStatementParameterNotPresent(statement, "3");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw Exception
        }
        module.verifyCallableStatementParameterNotPresent(1, 2);
        module.verifyCallableStatementParameterPresent(statement, "3");
        module.verifyCallableStatementParameterNotPresent(statement, "31"); 
        module.verifyCallableStatementParameter("{call setData(?, ?, ?, ?)}", "xyz", new Integer(1));
        module.verifyCallableStatementParameter(1, 1, "xyz");
        try
        {
            module.verifyCallableStatementParameter(1, 1, "zzz");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw Exception
        }
        statement.setBytes(1, new byte[] {1});
        statement.setBlob(2, new MockBlob(new byte[] {3, 4}));
        statement.setClob(3, new MockClob("test"));
        module.verifyCallableStatementParameter(1, 1, new byte[] {1});
        module.verifyCallableStatementParameter(statement, 2, new MockBlob(new byte[] {3, 4}));
        module.verifyCallableStatementParameter(1, 3, new MockClob("test"));
        try
        {
            module.verifyCallableStatementParameter(1, 1, new byte[] {2});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw Exception
        }
    }
    
    public void testVerifyCallableStatementOutParameterRegistered() throws Exception
    {
        prepareCallableStatements();
        MockCallableStatement statement = module.getCallableStatement("{call getData(?, ?, ?, ?)}");
        statement.registerOutParameter(1, Types.DECIMAL);
        statement.registerOutParameter("test", Types.BLOB);
        statement.registerOutParameter("xyz", Types.BINARY);
        module.verifyCallableStatementOutParameterRegistered(statement, 1);
        module.verifyCallableStatementOutParameterRegistered(statement, "test");
        module.verifyCallableStatementOutParameterRegistered(statement, "xyz");
        try
        {
            module.verifyCallableStatementOutParameterRegistered("{call setData(?, ?, ?, ?)}", "xyz");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw Exception
        }
        try
        {
            module.verifyCallableStatementOutParameterRegistered(1, "test");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw Exception
        }
        module.verifyCallableStatementOutParameterRegistered(0, "test");
    }
    
    public void testGetExecutedSQLStatements() throws Exception
    {
        prepareStatements();
        preparePreparedStatements();
        prepareCallableStatements();
        MockStatement statement = module.getStatement(0);
        statement.execute("select");
        statement.execute("UPDATE");
        MockPreparedStatement preparedStatement = module.getPreparedStatement("insert");
        preparedStatement.execute();
        MockCallableStatement callableStatement = module.getCallableStatement("call");
        callableStatement.executeUpdate();
        List sqlStatements = module.getExecutedSQLStatements();
        assertTrue(sqlStatements.size() == 4);
        assertTrue(sqlStatements.contains("select"));
        assertTrue(sqlStatements.contains("UPDATE"));
        assertTrue(sqlStatements.contains("INSERT INTO TEST (COL1, COL2) VALUES(?, ?)"));
        assertTrue(sqlStatements.contains("{call getData(?, ?, ?, ?)}"));
        module.verifySQLStatementExecuted("select");
        module.verifySQLStatementExecuted("update");
        module.verifySQLStatementExecuted("INSERT");
        module.verifySQLStatementExecuted("{call");
        module.verifySQLStatementNotExecuted("{call}");
        module.setCaseSensitive(true);
        module.verifySQLStatementExecuted("UPDATE");
        module.verifySQLStatementNotExecuted("update");
        module.setExactMatch(true);
        module.verifySQLStatementExecuted("{call getData(?, ?, ?, ?)}");
        module.verifySQLStatementNotExecuted("call");
        module.setCaseSensitive(false);
        module.verifySQLStatementExecuted("{CALL getData(?, ?, ?, ?)}");
    }
    
    public void testStatementsClosed() throws Exception
    {
        prepareStatements();
        preparePreparedStatements();
        prepareCallableStatements();
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
        statements.addAll(module.getCallableStatements());
        for(int ii = 0; ii < statements.size(); ii++)
        {
            ((MockStatement)statements.get(ii)).close();
        }
        module.verifyAllStatementsClosed();
        mockfactory.getMockConnection().close();
        module.verifyConnectionClosed();
    }
    
    public void testSavepoints() throws Exception
    {
        Savepoint savepoint0 = mockfactory.getMockConnection().setSavepoint();
        Savepoint savepoint1 = mockfactory.getMockConnection().setSavepoint("test");
        Savepoint savepoint2 = mockfactory.getMockConnection().setSavepoint("xyz");
        Savepoint savepoint3 = mockfactory.getMockConnection().setSavepoint();
        module.verifySavepointNotReleased(0);
        module.verifySavepointNotReleased(1);
        module.verifySavepointNotReleased(2);
        module.verifySavepointNotReleased(3);
        module.verifySavepointNotRollbacked(0);
        module.verifySavepointNotRollbacked("test");
        module.verifySavepointNotRollbacked(2);
        module.verifySavepointNotRollbacked(3);
        mockfactory.getMockConnection().releaseSavepoint(savepoint2);
        mockfactory.getMockConnection().rollback(savepoint3);
        module.verifySavepointNotReleased(0);
        module.verifySavepointNotReleased(1);
        module.verifySavepointReleased("xyz");
        module.verifySavepointNotReleased(3);
        module.verifySavepointNotRollbacked(0);
        module.verifySavepointNotRollbacked(1);
        module.verifySavepointNotRollbacked("xyz");
        module.verifySavepointRollbacked(3);
        try
        {
            module.verifySavepointReleased("test");
            fail();
        }
        catch(Exception exc)
        {
            //should throw Exception
        }
        try
        {
            module.verifySavepointNotRollbacked(3);
            fail();
        }
        catch(Exception exc)
        {
            //should throw Exception
        }
        List savepoints = module.getSavepoints();
        int[] ids = new int[4];
        for(int ii = 0; ii < savepoints.size(); ii++)
        {
            ids[ii] += 1;
        }
        assertTrue(ids[0] == 1);
        assertTrue(ids[1] == 1);
        assertTrue(ids[2] == 1);
        assertTrue(ids[3] == 1);
        Savepoint savepoint = module.getSavepoint("xyz");
        assertTrue(savepoint == savepoint2);
    }
    
    public void testVerifyNumberCommitsAndRollbacks() throws Exception
    {
        try
        {
            module.verifyCommited();
            fail();
        }
        catch(Exception exc)
        {
            //should throw Exception
        }
        try
        {
            module.verifyRolledback();
            fail();
        }
        catch(Exception exc)
        {
            //should throw Exception
        }
        Savepoint savepoint = mockfactory.getMockConnection().setSavepoint();
        mockfactory.getMockConnection().commit();
        mockfactory.getMockConnection().rollback();
        mockfactory.getMockConnection().rollback(savepoint);
        module.verifyCommited();
        module.verifyRolledback();
        module.verifyNumberCommits(1);
        module.verifyNumberRollbacks(2);
    }
}
