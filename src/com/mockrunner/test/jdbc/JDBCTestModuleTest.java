package com.mockrunner.test.jdbc;

import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.jdbc.ParameterSets;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockBlob;
import com.mockrunner.mock.jdbc.MockCallableStatement;
import com.mockrunner.mock.jdbc.MockClob;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockResultSet;
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
    
    public void testGetPreparedStatementsBySQLRegEx() throws Exception
    {
        module.setUseRegularExpressions(true);
        preparePreparedStatements();
        List statements = module.getPreparedStatements("insert");
        assertNotNull(statements);
        assertEquals(0, statements.size());
        statements = module.getPreparedStatements("insert into.*");
        assertEquals(2, statements.size());
        module.verifyNumberPreparedStatements(0, "update");
        module.verifyNumberPreparedStatements(2, "insert (.*) test.*");
        module.verifyNumberPreparedStatements(2, "insert (.*) TEST.*");
        module.setCaseSensitive(true);
        module.verifyNumberPreparedStatements(0, "insert (.*) TEST.*");
    }
    
    public void testGetPreparedStatementObjects() throws Exception
    {
        preparePreparedStatements();
        MockPreparedStatement statement = module.getPreparedStatement("update");
        statement.setInt(1, 3);
        statement.setLong(2, 10000);
        statement.setNull(3, 1);
        assertEquals(new Integer(3), statement.getParameter(1));
        assertEquals(new Long(10000), statement.getParameter(2));
        assertNull(statement.getParameter(3));
        assertTrue(statement.getParameterMap().containsKey(new Integer(3)));
        module.verifyPreparedStatementParameterPresent(statement, 1);
        module.verifyPreparedStatementParameterPresent("update", 3);
        module.verifyPreparedStatementParameterNotPresent("update", 4);
        module.verifyPreparedStatementParameterNotPresent(0, 1);
        module.verifyPreparedStatementParameter(statement, 1, new Integer(3));
        module.verifyPreparedStatementParameter(2, 2, new Long(10000));
        module.verifyPreparedStatementParameter(statement, 3, null);
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
    
    public void testGetCallableStatementsBySQLRegEx() throws Exception
    {
        module.setUseRegularExpressions(true);
        prepareCallableStatements();
        List statements = module.getCallableStatements("call");
        assertTrue(statements.size() == 0);
        MockCallableStatement statement = module.getCallableStatement(".*CALL.*");
        assertEquals("{call getData(?, ?, ?, ?)}", statement.getSQL());
        module.verifyCallableStatementNotPresent("call setData");
        module.verifyCallableStatementPresent("{call setData.*}");
    }
    
    public void testGetCallableStatementObjects() throws Exception
    {
        prepareCallableStatements();
        MockCallableStatement statement = module.getCallableStatement("{call setData(?, ?, ?, ?)}");
        statement.setInt("xyz", 1);
        statement.setString("3", null);
        statement.setString(1, "xyz");
        Map namedParameter = statement.getNamedParameterMap();
        Map indexedParameter = statement.getIndexedParameterMap();
        assertTrue(namedParameter.size() == 2);
        assertEquals(new Integer(1), namedParameter.get("xyz"));
        assertNull(namedParameter.get("3"));
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
        module.verifyCallableStatementParameter(1, "3", null);
        try
        {
            module.verifyCallableStatementParameter(1, 1, "zzz");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw Exception
        }
        try
        {
            module.verifyCallableStatementParameter(1, 5, null);
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
    
    public void testGetExecutedSQLStatementsRegEx() throws Exception
    {
        module.setUseRegularExpressions(true);
        prepareStatements();
        preparePreparedStatements();
        prepareCallableStatements();
        MockStatement statement = module.getStatement(0);
        statement.execute("select");
        statement.execute("UPDATE");
        MockPreparedStatement preparedStatement = module.getPreparedStatement("insert.*");
        preparedStatement.execute();
        MockCallableStatement callableStatement = module.getCallableStatement("{call.*");
        callableStatement.executeUpdate();
        module.verifySQLStatementExecuted("select");
        module.verifySQLStatementExecuted("update.*");
        module.verifySQLStatementExecuted("INSERT into .*");
        module.verifySQLStatementExecuted("{call.*");
        module.verifySQLStatementNotExecuted("{call}");
        module.setCaseSensitive(true);
        module.verifySQLStatementExecuted("UPDATE.*");
        module.verifySQLStatementNotExecuted("update");
        module.setExactMatch(true);
        module.verifySQLStatementNotExecuted("UPDATE.*");
        module.verifySQLStatementExecuted("UPDATE");
    }
    
    public void testReturnedResultSetsClosed() throws Exception
    {
        prepareStatements();
        preparePreparedStatements();
        prepareCallableStatements();
        MockResultSet resultSet1 = module.getStatementResultSetHandler().createResultSet("1");
        MockResultSet resultSet2 = module.getStatementResultSetHandler().createResultSet("2");
        MockResultSet resultSet3 = module.getStatementResultSetHandler().createResultSet("3");
        MockResultSet resultSet4 = module.getStatementResultSetHandler().createResultSet("4");
        MockResultSet resultSet5 = module.getStatementResultSetHandler().createResultSet("5");
        module.getStatementResultSetHandler().prepareGlobalResultSet(resultSet1);
        module.getStatementResultSetHandler().prepareResultSet("select id", resultSet2);
        module.getStatementResultSetHandler().prepareResultSet("select xyz", resultSet3);
        module.getPreparedStatementResultSetHandler().prepareResultSet("select name", resultSet4, new String[] {"test"});
        module.getCallableStatementResultSetHandler().prepareResultSet("call set", resultSet5, new String[] {"xyz"});
        MockStatement statement = module.getStatement(0);
        statement.executeQuery("select name");
        statement.executeQuery("select id");
        List list = module.getReturnedResultSets();
        assertTrue(list.size() == 2);
        assertEquals("1", ((MockResultSet)list.get(0)).getId());
        assertEquals("2", ((MockResultSet)list.get(1)).getId());
        MockPreparedStatement preparedStatement = module.getPreparedStatement("insert");
        preparedStatement.execute();
        list = module.getReturnedResultSets();
        assertTrue(list.size() == 2);
        assertEquals("1", ((MockResultSet)list.get(0)).getId());
        assertEquals("2", ((MockResultSet)list.get(1)).getId());
        preparedStatement = (MockPreparedStatement)mockfactory.getMockConnection().prepareStatement("SELECT NAME");
        preparedStatement.setString(1, "test");
        preparedStatement.executeQuery();
        list = module.getReturnedResultSets();
        assertTrue(list.size() == 3);
        assertEquals("1", ((MockResultSet)list.get(0)).getId());
        assertEquals("2", ((MockResultSet)list.get(1)).getId());
        assertEquals("4", ((MockResultSet)list.get(2)).getId());
        MockCallableStatement callableStatement = module.getCallableStatement("call set");
        callableStatement.setString(1, "test");
        callableStatement.executeQuery();
        list = module.getReturnedResultSets();
        assertTrue(list.size() == 3);
        assertEquals("1", ((MockResultSet)list.get(0)).getId());
        assertEquals("2", ((MockResultSet)list.get(1)).getId());
        assertEquals("4", ((MockResultSet)list.get(2)).getId());
        callableStatement.setString(1, "xyz");
        callableStatement.executeQuery();
        list = module.getReturnedResultSets();
        assertTrue(list.size() == 4);
        assertEquals("1", ((MockResultSet)list.get(0)).getId());
        assertEquals("2", ((MockResultSet)list.get(1)).getId());
        assertEquals("4", ((MockResultSet)list.get(2)).getId());
        assertEquals("5", ((MockResultSet)list.get(3)).getId());
        ((MockResultSet)list.get(0)).close();
        module.verifyResultSetClosed("1");
        try
        {
            module.verifyResultSetClosed("2");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyAllResultSetsClosed();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        ((MockResultSet)list.get(1)).close();
        ((MockResultSet)list.get(2)).close();
        ((MockResultSet)list.get(3)).close();
        module.verifyAllResultSetsClosed();
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
        mockfactory.getMockConnection().setSavepoint();
        mockfactory.getMockConnection().setSavepoint("test");
        Savepoint savepoint2 = mockfactory.getMockConnection().setSavepoint("xyz");
        Savepoint savepoint3 = mockfactory.getMockConnection().setSavepoint();
        module.verifySavepointNotReleased(0);
        module.verifySavepointNotReleased(1);
        module.verifySavepointNotReleased(2);
        module.verifySavepointNotReleased(3);
        module.verifySavepointNotRolledBack(0);
        module.verifySavepointNotRolledBack("test");
        module.verifySavepointNotRolledBack(2);
        module.verifySavepointNotRolledBack(3);
        mockfactory.getMockConnection().releaseSavepoint(savepoint2);
        mockfactory.getMockConnection().rollback(savepoint3);
        module.verifySavepointNotReleased(0);
        module.verifySavepointNotReleased(1);
        module.verifySavepointReleased("xyz");
        module.verifySavepointNotReleased(3);
        module.verifySavepointNotRolledBack(0);
        module.verifySavepointNotRolledBack(1);
        module.verifySavepointNotRolledBack("xyz");
        module.verifySavepointRolledBack(3);
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
            module.verifySavepointNotRolledBack(3);
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
            module.verifyCommitted();
            fail();
        }
        catch(Exception exc)
        {
            //should throw Exception
        }
        try
        {
            module.verifyRolledBack();
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
        module.verifyCommitted();
        module.verifyRolledBack();
        module.verifyNumberCommits(1);
        module.verifyNumberRollbacks(2);
    }
    
    public void testVerifyResultSet()
    {
        MockResultSet resultSet1 = module.getStatementResultSetHandler().createResultSet("test");
        resultSet1.addRow(new Integer[] {new Integer(1), new Integer(2), new Integer(3)});
        resultSet1.addRow(new Integer[] {new Integer(4), new Integer(5), new Integer(6)});
        resultSet1.addRow(new Integer[] {new Integer(7), new Integer(8), new Integer(9)});
        module.getStatementResultSetHandler().addReturnedResultSet(resultSet1);
        MockResultSet resultSet2 = module.getStatementResultSetHandler().createResultSet("xyz");
        resultSet2.addColumn("column", new String[] {"1", "2", "3"});
        module.getStatementResultSetHandler().addReturnedResultSet(resultSet2);
        module.verifyResultSetRow("test", 2, new Integer[] {new Integer(4), new Integer(5), new Integer(6)});
        try
        {
            module.verifyResultSetRow(resultSet1, 3, new Integer[] {new Integer(4), new Integer(5), new Integer(6)});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyResultSetColumn("test", 1, new Integer[] {new Integer(1), new Integer(4), new Integer(7)});
        module.verifyResultSetColumn(resultSet2, 1, new String[] {"1", "2", "3"});
        module.verifyResultSetColumn(resultSet2, "column", new String[] {"1", "2", "3"});
        module.verifyResultSetRow("xyz", 3, new String[] {"3"});
        try
        {
            module.verifyResultSetRow(resultSet2, 3, new String[] {"3", "4"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyResultSetColumn("xyz", "testColumn", new String[] {"1"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyResultSetColumn("xyz", 2, new String[] {"1", "2", "3"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyResultSetRow(resultSet2, 5, new String[] {"1", "2", "3"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyResultSetEquals(resultSet1, resultSet2);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyResultSetEquals(resultSet1, resultSet1);
        module.verifyResultSetEquals(resultSet2, resultSet2);
        resultSet2 = module.getStatementResultSetHandler().createResultSet("test2");
        resultSet2.addRow(new Integer[] {new Integer(1), new Integer(2), new Integer(3)});
        resultSet2.addRow(new Integer[] {new Integer(4), new Integer(5), new Integer(6)});
        resultSet2.addRow(new Integer[] {new Integer(7), new Integer(8), new Integer(9)});
        module.getStatementResultSetHandler().addReturnedResultSet(resultSet2);
        module.getStatementResultSetHandler().addReturnedResultSet(resultSet1);
        module.verifyResultSetEquals(resultSet1, resultSet2);
        module.verifyResultSetEquals("test", resultSet2);
        module.verifyResultSetEquals("test2", resultSet1);
    }
    
    public void testVerifyResultSetRowModified() throws Exception
    {
        MockResultSet resultSet = module.getStatementResultSetHandler().createResultSet("test");
        resultSet.addRow(new Integer[] {new Integer(1), new Integer(2), new Integer(3)});
        resultSet.addRow(new Integer[] {new Integer(4), new Integer(5), new Integer(6)});
        resultSet.addRow(new Integer[] {new Integer(7), new Integer(8), new Integer(9)});
        module.getStatementResultSetHandler().addReturnedResultSet(resultSet);
        module.verifyResultSetRowNotDeleted(resultSet, 1);
        module.verifyResultSetRowNotDeleted("test", 2);
        module.verifyResultSetRowNotInserted("test", 2);
        module.verifyResultSetRowNotUpdated(resultSet, 3);
        try
        {
            module.verifyResultSetRowUpdated(resultSet, 1);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        resultSet.setResultSetConcurrency(ResultSet.CONCUR_UPDATABLE);
        resultSet.next();
        resultSet.updateRow();
        module.verifyResultSetRowUpdated(resultSet, 1);
        resultSet.next();
        resultSet.deleteRow();
        module.verifyResultSetRowDeleted(resultSet, 2);
        resultSet.next();
        resultSet.moveToInsertRow();
        resultSet.updateString(1, "test");
        resultSet.insertRow();
        resultSet.moveToCurrentRow();
        module.verifyResultSetRowInserted("test", 3);
        resultSet.first();
        resultSet.moveToInsertRow();
        resultSet.updateString(1, "test");
        resultSet.insertRow();
        resultSet.moveToCurrentRow();
        module.verifyResultSetRowInserted("test", 1);
        module.verifyResultSetRowDeleted(resultSet, 3);
        module.verifyResultSetRowNotUpdated(resultSet, 4);
    }
    
    public void testGetExecutedSQLStatementParameter() throws Exception
    {
		prepareStatements();
		preparePreparedStatements();
		prepareCallableStatements();
		module.getPreparedStatement(0).setString(1, "test");
		module.getPreparedStatement(0).setShort(2, (short)2);
		module.getPreparedStatement(1).setBytes(1, new byte[]{1});
		module.getCallableStatement(1).setBoolean("name", false);
		module.getStatement(0).execute("select mydata");
		module.getStatement(1).execute("select mydata");
		module.getPreparedStatement(0).execute();
		module.getPreparedStatement(1).execute();
		module.getPreparedStatement(2).execute();
		module.getCallableStatement(0).execute();
		module.getCallableStatement(1).execute();
		Map parameterMap = module.getExecutedSQLStatementParameter();
		assertEquals(5, parameterMap.size());
		Map preparedStatementMap1 = ((ParameterSets)parameterMap.get("INSERT INTO TEST (COL1, COL2) VALUES(?, ?)")).getParameterSet(0);
		assertEquals(2, preparedStatementMap1.size());
		assertEquals("test", preparedStatementMap1.get(new Integer(1)));
		assertEquals(new Short((short)2), preparedStatementMap1.get(new Integer(2)));
		Map preparedStatementMap2 = ((ParameterSets)parameterMap.get("insert into test (col1, col2, col3) values(?, ?, ?)")).getParameterSet(0);
		assertEquals(1, preparedStatementMap2.size());
		assertTrue(Arrays.equals(new byte[]{1}, (byte[])preparedStatementMap2.get(new Integer(1))));
		Map preparedStatementMap3 = ((ParameterSets)parameterMap.get("update mytable set test = test + ? where id = ?")).getParameterSet(0);
		assertEquals(0, preparedStatementMap3.size());
		Map callableStatementMap1 = (Map)((ParameterSets)parameterMap.get("{call getData(?, ?, ?, ?)}")).getParameterSet(0);
		assertEquals(0, callableStatementMap1.size());
		Map callableStatementMap2 = (Map)((ParameterSets)parameterMap.get("{call setData(?, ?, ?, ?)}")).getParameterSet(0);
		assertEquals(1, callableStatementMap2.size());
		assertEquals(new Boolean(false), callableStatementMap2.get("name"));
    }
    
    public void testGetExecutedSQLStatementParameterSets() throws Exception
    {
		preparePreparedStatements();
		prepareCallableStatements();
		module.getPreparedStatement(0).setString(1, "test");
		module.getPreparedStatement(0).setShort(2, (short)2);
		module.getPreparedStatement(1).setBytes(1, new byte[]{1});
		module.getCallableStatement(1).setBoolean("name", false);
		module.getPreparedStatement(0).execute();
		module.getPreparedStatement(1).execute();
		module.getPreparedStatement(2).execute();
		module.getCallableStatement(0).execute();
		module.getCallableStatement(1).execute();
		module.getPreparedStatement(0).setString(1, "test1");
		module.getPreparedStatement(0).setShort(2, (short)3);
		module.getPreparedStatement(0).execute();
		ParameterSets sets1 = module.getExecutedSQLStatementParameterSets("INSERT INTO TEST (COL1, COL2)");
		assertEquals(2, sets1.getNumberParameterSets());
		Map parameterSet1 = sets1.getParameterSet(0);
		assertEquals(2, parameterSet1.size());
		assertEquals("test", parameterSet1.get(new Integer(1)));
		assertEquals(new Short((short)2), parameterSet1.get(new Integer(2)));
		Map parameterSet2 = sets1.getParameterSet(1);
		assertEquals(2, parameterSet2.size());
		assertEquals("test1", parameterSet2.get(new Integer(1)));
		assertEquals(new Short((short)3), parameterSet2.get(new Integer(2)));
		module.setUseRegularExpressions(true);
		ParameterSets sets2 = module.getExecutedSQLStatementParameterSets("insert into test \\(col1, col2, col3\\) .*");
		assertEquals(1, sets2.getNumberParameterSets());
		parameterSet1 = sets2.getParameterSet(0);
		assertEquals(1, parameterSet1.size());
		assertTrue(Arrays.equals(new byte[]{1}, (byte[])parameterSet1.get(new Integer(1))));
		ParameterSets sets3 = module.getExecutedSQLStatementParameterSets("{call setData\\(\\?, \\?, \\?, \\?\\)}");
		assertEquals(1, sets3.getNumberParameterSets());
		parameterSet1 = sets3.getParameterSet(0);
		assertEquals(1, parameterSet1.size());
		assertEquals(new Boolean(false), parameterSet1.get("name"));
		ParameterSets sets4 = module.getExecutedSQLStatementParameterSets("{call getData\\(\\?, \\?, \\?, \\?\\)}");
		assertEquals(1, sets4.getNumberParameterSets());
		parameterSet1 = sets4.getParameterSet(0);
		assertEquals(0, parameterSet1.size());
		assertNull(module.getExecutedSQLStatementParameterSets("{call xyz"));
    }
    
    public void testSQLStatementParameterNoParameterSets() throws Exception
    {
        prepareStatements();
        module.getStatement(0).execute("test");
        try
        {
            module.verifySQLStatementParameterNumber("test", 0, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
            //should throw exception
        }
        preparePreparedStatements();
        module.getPreparedStatement(0).execute();
        try
        {
            module.verifySQLStatementParameterNumber("INSERT INTO TEST (COL1, COL2) VALUES(?,", 1, 0);
            fail();
        }
        catch (VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifySQLStatementParameter("INSERT INTO TEST (COL1, COL2) VALUES(?,", 1, new HashMap());
            fail();
        }
        catch (VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
	public void testSQLStatementParameterNumber() throws Exception
	{
        preparePreparedStatements();
		prepareCallableStatements();
		module.getPreparedStatement(0).setString(1, "test");
		module.getPreparedStatement(0).setString(2, "test");
		module.getCallableStatement(0).setString("name", "test");
		module.getCallableStatement(1).setString(1, "test");
		module.getPreparedStatement(0).execute();
		module.getPreparedStatement(1).execute();
		module.getPreparedStatement(2).execute();
		module.getCallableStatement(0).execute();
		module.getCallableStatement(1).execute();
		module.verifySQLStatementParameterNumber("INSERT INTO TEST (COL1, COL2) VALUES(?,", 0, 2);
		module.verifySQLStatementParameterNumber("insert into test (col1, col2, col3) values(?, ?, ?)", 0, 0);
		module.verifySQLStatementParameterNumber("update mytable set test = test + ? where id = ?", 0, 0);
		module.verifySQLStatementParameterNumber("{call getData(?, ?, ?, ?)}", 0, 1);
		module.verifySQLStatementParameterNumber("{call setData(?, ", 0, 1);
		try
		{
			module.verifySQLStatementParameterNumber("{call getData(?, ?, ?, ?)}", 0, 3);
			fail();
		}
		catch (VerifyFailedException exc)
		{
            //should throw exception
		}
		try
		{
			module.verifySQLStatementParameterNumber("insert into test (col1, col2, col3) values(?, ?, ?)", 0, 1);
			fail();
		}
		catch (VerifyFailedException exc)
		{
			//should throw exception
		}
		try
		{
			module.verifySQLStatementParameterNumber("xyz", 0, 0);
			fail();
		}
		catch(VerifyFailedException exc)
		{
			//should throw exception
		}
	}
	
	public void testSQLStatementParameterPreparedStatement() throws Exception
	{
		preparePreparedStatements();
		module.getPreparedStatement(1).setString(1, "test1");
		module.getPreparedStatement(1).setInt(2, 3);
		module.getPreparedStatement(0).execute();
		module.getPreparedStatement(1).execute();
		module.getPreparedStatement(2).execute();
		Map emptyMap = new HashMap();
		Map okTestMap = new HashMap();
		okTestMap.put(new Integer(1), "test1");
		okTestMap.put(new Integer(2), new Integer(3));
		Map failureTestMap1 = new HashMap();
		failureTestMap1.put(new Integer(1), "test1");
		failureTestMap1.put(new Integer(2), new Integer(2));
		Map failureTestMap2 = new HashMap();
		failureTestMap2.put(new Integer(1), "test1");
		failureTestMap2.put(new Integer(2), new Integer(3));
		failureTestMap2.put(new Integer(3), new Integer(3));
		module.verifySQLStatementParameter("update mytable set test = test", 0, emptyMap);
		try
		{
		    module.setUseRegularExpressions(true);
		    module.verifySQLStatementParameter("update mytable set test = test", 0, emptyMap);
			fail();
		}
		catch(VerifyFailedException exc)
		{
			//should throw exception
		}
		module.verifySQLStatementParameter("update mytable set test = test.*", 0, emptyMap);
		module.setUseRegularExpressions(false);
		module.verifySQLStatementParameter("insert into test (col1, col2, col3)", 0, okTestMap);
        module.verifySQLStatementParameter("insert into test (col1, col2, col3)", 0, 2, new Integer(3));
		try
		{
			module.verifySQLStatementParameter("insert into test (col1, col2, col3) values(?, ?, ?)", 0, failureTestMap1);
			fail();
		}
		catch(VerifyFailedException exc)
		{
			//should throw exception
		}
        try
        {
            module.verifySQLStatementParameter("insert into test (col1, col2, col3) values(?, ?, ?)", 0, 1, "test2");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
		try
		{
			module.verifySQLStatementParameter("insert into test (col1, col2, col3) values(?, ?, ?)", 0, failureTestMap2);
			fail();
		}
		catch(VerifyFailedException exc)
		{
			//should throw exception
		}
		try
		{
			module.verifySQLStatementParameter("INSERT INTO TEST (COL1, COL2) VALUES(?, ?)", 0, okTestMap);
			fail();
		}
		catch(VerifyFailedException exc)
		{
			//should throw exception
		}
	}
    
    public void testSQLStatementNullParameterPreparedStatement() throws Exception
    {
        preparePreparedStatements();
        module.getPreparedStatement(0).setString(1, null);
        module.getPreparedStatement(0).execute();
        module.verifySQLStatementParameter("INSERT INTO TEST (COL1, COL2) VALUES(?, ?)", 0, 1, null);
        try
        {
            module.verifySQLStatementParameter("INSERT INTO TEST (COL1, COL2) VALUES(?, ?)", 0, 1, "test");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
	
	public void testSQLStatementParameterCallableStatement() throws Exception
	{
		prepareCallableStatements();
		module.getCallableStatement(0).setString(1, "test1");
		module.getCallableStatement(0).setBytes(2, new byte[] {1});
		module.getCallableStatement(0).setInt("name", 1);
		module.getCallableStatement(0).execute();
		module.getCallableStatement(1).execute();
		module.verifySQLStatementParameter("{call getData(?, ?", 0, 1, "test1");
		module.setUseRegularExpressions(true);
		module.verifySQLStatementParameter(".*getData\\(\\?, \\?.*", 0, 1, "test1");
		module.setUseRegularExpressions(false);
		try
        {
		    module.verifySQLStatementParameter(".*getData\\(\\?, \\?.*", 0, 1, "test1");
            fail();
        } 
		catch(VerifyFailedException exc)
        {
		    //should throw exception
        }
		module.verifySQLStatementParameter("{call getData(?, ?, ?, ?)}", 0, 2, new byte[] {1});
		module.verifySQLStatementParameter("{call getData(?, ?, ?, ?)}", 0, "name", new Integer(1));
		try
		{
			module.verifySQLStatementParameter("{call getData(?, ?, ?, ?)}", 0, 2, new byte[] {1, 2});
			fail();
		}
		catch(VerifyFailedException exc)
		{
		    //should throw exception
		}
		try
		{
			module.verifySQLStatementParameter("{call setData(?, ?, ?, ?)}", 0, 1, "");
			fail();
		}
		catch(VerifyFailedException exc)
		{
			//should throw exception
		}
		try
		{
			module.verifySQLStatementParameter("select", 0, 1, "");
			fail();
		}
		catch(VerifyFailedException exc)
		{
			//should throw exception
		}
		module.setCaseSensitive(true);
		try
		{
			module.verifySQLStatementParameter("{CALL getData(?, ?", 0, 1, "test1");
			fail();
		}
		catch(VerifyFailedException exc)
		{
			//should throw exception
		}
	}
    
    public void testSQLStatementNullParameterCallableStatement() throws Exception
    {
        prepareCallableStatements();
        module.getCallableStatement(0).setString("1", null);
        module.getCallableStatement(0).execute();
        module.verifySQLStatementParameter("{call getData(?, ?, ?, ?)}", 0, "1", null);
        try
        {
            module.verifySQLStatementParameter("{call getData(?, ?, ?, ?)}", 0, "1", "test");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
	
	public void testSQLStatementParameterMultipleParameterSets() throws Exception
	{
		prepareCallableStatements();
		module.getCallableStatement(0).setString(1, "test1");
		module.getCallableStatement(0).execute();
		module.getCallableStatement(0).setString(1, "xyz");
		module.getCallableStatement(0).setBoolean("name", true);
		module.getCallableStatement(0).execute();
		module.getCallableStatement(0).execute();
		module.verifySQLStatementParameterNumber("{call getData(?, ?, ?, ?)}", 0, 1);
		module.verifySQLStatementParameterNumber("{call getData(?, ?, ?, ?)}", 1, 2);
		module.verifySQLStatementParameterNumber("{call getData(?, ?, ?, ?)}", 2, 2);
		module.setUseRegularExpressions(true);
		module.verifySQLStatementParameterNumber("{call getData\\(\\?, \\?, \\?, \\?\\)}", 2, 2);
		module.verifySQLStatementParameterNumber(".call getData.*}", 2, 2);
		module.setUseRegularExpressions(false);
		module.verifySQLStatementParameter("{call getData(?, ?, ?, ?)}", 1, 1, "xyz");
		module.verifySQLStatementParameter("{call getData(?, ?, ?, ?)}", 1, "name", new Boolean(true));
		HashMap testMap = new HashMap();
		testMap.put(new Integer(1), "test1");
		module.verifySQLStatementParameter("{call getData(?, ?, ?, ?)}", 0, testMap);
		try
		{
			module.verifySQLStatementParameterNumber("{call getData(?, ?, ?, ?)}", 2, 0);
			fail();
		}
		catch (VerifyFailedException exc)
		{
			//should throw exception
		}
		try
		{
			module.verifySQLStatementParameter("{call getData(?, ?, ?, ?)}", 0, new HashMap());
			fail();
		}
		catch (VerifyFailedException exc)
		{
			//should throw exception
		}
	}
    
    public void testSQLStatementParameterPreparedStatementBatchParameterSets() throws Exception
    {
        MockPreparedStatement preparedStatement = (MockPreparedStatement)mockfactory.getMockConnection().prepareStatement("insert into test");
        preparedStatement.setString(1, "test1");
        preparedStatement.setInt(2, 3);
        preparedStatement.addBatch();
        preparedStatement.setString(1, "test2");
        preparedStatement.setInt(2, 4);
        preparedStatement.addBatch();
        preparedStatement.executeBatch();
        module.verifySQLStatementParameter("insert into test", 0, 1, "test1");
        module.verifySQLStatementParameter("insert into test", 0, 2, new Integer(3));
        module.verifySQLStatementParameter("insert into test", 1, 1, "test2");
        module.verifySQLStatementParameter("insert into test", 1, 2, new Integer(4));
        Map testMap = new HashMap();
        testMap.put(new Integer(1), "test1");
        testMap.put(new Integer(2), new Integer(3));
        module.verifySQLStatementParameter("insert into test", 0, testMap);
        testMap = new HashMap();
        testMap.put(new Integer(1), "test2");
        testMap.put(new Integer(2), new Integer(4));
        module.verifySQLStatementParameter("insert into test", 1, testMap);
    }
    
    public void testSQLStatementParameterCallableStatementBatchParameterSets() throws Exception
    {
        MockCallableStatement callableStatement = (MockCallableStatement)mockfactory.getMockConnection().prepareCall("call getData");
        callableStatement.setString("xyz1", "test1");
        callableStatement.setLong(1, 3);
        callableStatement.addBatch();
        callableStatement.setString(1, "test2");
        callableStatement.setInt("xyz1", 4);
        callableStatement.setInt("xyz2", 7);
        callableStatement.addBatch();
        callableStatement.executeBatch();
        module.verifySQLStatementParameter("call getData", 0, "xyz1", "test1");
        module.verifySQLStatementParameter("call getData", 0, 1, new Long(3));
        module.verifySQLStatementParameter("call getData", 1, "xyz1", new Integer(4));
        module.verifySQLStatementParameter("call getData", 1, "xyz2", new Integer(7));
        module.verifySQLStatementParameter("call getData", 1, 1, "test2");
        Map testMap = new HashMap();
        testMap.put("xyz1", "test1");
        testMap.put(new Integer(1), new Long(3));
        module.verifySQLStatementParameter("call getData", 0, testMap);
        testMap = new HashMap();
        testMap.put("xyz1", new Integer(4));
        testMap.put("xyz2", new Integer(7));
        testMap.put(new Integer(1), "test2");
        module.verifySQLStatementParameter("call getData", 1, testMap);
    }
}
