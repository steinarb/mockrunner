package com.mockrunner.test.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import com.mockrunner.mock.jdbc.MockDatabaseMetaData;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.PolyResultSet;

public class MockDatabaseMetaDataTest extends TestCase
{
    private MockDatabaseMetaData metaData;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        metaData = new MockDatabaseMetaData();
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        metaData = null;
    }

    public void testExportedKeys() throws SQLException
    {
        assertNull(metaData.getExportedKeys(null, null, null));
        assertNull(metaData.getExportedKeys("abc1", "abc2", "abc3"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setExportedKeys(testResult);
        assertSame(testResult, metaData.getExportedKeys(null, null, null));
        assertSame(testResult, metaData.getExportedKeys("", "test", "xyz"));
        assertSame(testResult, metaData.getExportedKeys("abc1", "test", "xyz"));
        metaData.clearExportedKeys();
        assertNull(metaData.getExportedKeys("", "test", "xyz"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setExportedKeys("", "test%", "xyz", testResult);
        metaData.setExportedKeys("abc", "test", "xyz", testResult2);
        metaData.setExportedKeys(null, "test", "xyz", testResult3);
        assertSame(testResult2, metaData.getExportedKeys("abc", "test", "xyz"));
        PolyResultSet polyResult = (PolyResultSet)metaData.getExportedKeys(null, "test", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getExportedKeys("", "test%", "xyz"));
        assertSame(testResult3, metaData.getExportedKeys("", "test", "xyz"));
        assertSame(testResult2, metaData.getExportedKeys("abc", "test", "xyz"));
        assertNull(metaData.getExportedKeys(null, "test_", "xyz"));
        assertNull(metaData.getExportedKeys(null, "test", "xyz1"));
        assertNull(metaData.getExportedKeys(null, "test", null));
        metaData.setExportedKeys(testResult4);
        assertSame(testResult4, metaData.getExportedKeys(null, "test3", "xyz"));
        assertSame(testResult4, metaData.getExportedKeys(null, "test", "xyz1"));
        assertSame(testResult4, metaData.getExportedKeys(null, "test", null));
        polyResult = (PolyResultSet)metaData.getExportedKeys(null, "test", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testExportedKeysCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setExportedKeys("abc", "TEST", "XYz", testResult);
        metaData.setExportedKeys("abc", "test", "xyz", testResult2);
        metaData.setExportedKeys(null, "test", "xyz", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getExportedKeys(null, "test", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getExportedKeys(null, "test", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getExportedKeys("abc", "TEST", "XYz"));
        assertNull(metaData.getExportedKeys("ABC", "TEST", "XYz"));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getExportedKeys("ABC", "TEST", "XYz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testImportedKeys() throws SQLException
    {
        assertNull(metaData.getImportedKeys(null, null, null));
        assertNull(metaData.getImportedKeys("abc1", "abc2", "abc3"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setImportedKeys(testResult);
        assertSame(testResult, metaData.getImportedKeys(null, null, null));
        assertSame(testResult, metaData.getImportedKeys("", "test", "xyz"));
        assertSame(testResult, metaData.getImportedKeys("abc1", "test", "xyz"));
        metaData.clearImportedKeys();
        assertNull(metaData.getImportedKeys("", "test", "xyz"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setImportedKeys("", "test%", "xyz", testResult);
        metaData.setImportedKeys("abc", "test", "xyz", testResult2);
        metaData.setImportedKeys(null, "test", "xyz", testResult3);
        assertSame(testResult2, metaData.getImportedKeys("abc", "test", "xyz"));
        PolyResultSet polyResult = (PolyResultSet)metaData.getImportedKeys(null, "test", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getImportedKeys("", "test%", "xyz"));
        assertSame(testResult3, metaData.getImportedKeys("", "test", "xyz"));
        assertSame(testResult2, metaData.getImportedKeys("abc", "test", "xyz"));
        assertNull(metaData.getImportedKeys(null, "test_", "xyz"));
        assertNull(metaData.getImportedKeys(null, "test", "xyz1"));
        assertNull(metaData.getImportedKeys(null, "test", null));
        metaData.setImportedKeys(testResult4);
        assertSame(testResult4, metaData.getImportedKeys(null, "test3", "xyz"));
        assertSame(testResult4, metaData.getImportedKeys(null, "test", "xyz1"));
        assertSame(testResult4, metaData.getImportedKeys(null, "test", null));
        polyResult = (PolyResultSet)metaData.getImportedKeys(null, "test", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testImportedKeysCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setImportedKeys("abc", "TEST", "XYz", testResult);
        metaData.setImportedKeys("abc", "test", "xyz", testResult2);
        metaData.setImportedKeys(null, "test", "xyz", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getImportedKeys(null, "test", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getImportedKeys(null, "test", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getImportedKeys("abc", "TEST", "XYz"));
        assertNull(metaData.getImportedKeys("ABC", "TEST", "XYz"));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getImportedKeys("ABC", "TEST", "XYz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testPrimaryKeys() throws SQLException
    {
        assertNull(metaData.getPrimaryKeys(null, null, null));
        assertNull(metaData.getPrimaryKeys("abc1", "abc2", "abc3"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setPrimaryKeys(testResult);
        assertSame(testResult, metaData.getPrimaryKeys(null, null, null));
        assertSame(testResult, metaData.getPrimaryKeys("", "test", "xyz"));
        assertSame(testResult, metaData.getPrimaryKeys("abc1", "test", "xyz"));
        metaData.clearPrimaryKeys();
        assertNull(metaData.getPrimaryKeys("", "test", "xyz"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setPrimaryKeys("", "test%", "xyz", testResult);
        metaData.setPrimaryKeys("abc", "test", "xyz", testResult2);
        metaData.setPrimaryKeys(null, "test", "xyz", testResult3);
        assertSame(testResult2, metaData.getPrimaryKeys("abc", "test", "xyz"));
        PolyResultSet polyResult = (PolyResultSet)metaData.getPrimaryKeys(null, "test", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getPrimaryKeys("", "test%", "xyz"));
        assertSame(testResult3, metaData.getPrimaryKeys("", "test", "xyz"));
        assertSame(testResult2, metaData.getPrimaryKeys("abc", "test", "xyz"));
        assertNull(metaData.getPrimaryKeys(null, "test_", "xyz"));
        assertNull(metaData.getPrimaryKeys(null, "test", "xyz1"));
        assertNull(metaData.getPrimaryKeys(null, "test", null));
        metaData.setPrimaryKeys(testResult4);
        assertSame(testResult4, metaData.getPrimaryKeys(null, "test3", "xyz"));
        assertSame(testResult4, metaData.getPrimaryKeys(null, "test", "xyz1"));
        assertSame(testResult4, metaData.getPrimaryKeys(null, "test", null));
        polyResult = (PolyResultSet)metaData.getPrimaryKeys(null, "test", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testPrimaryKeysCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setPrimaryKeys("abc", "TEST", "XYz", testResult);
        metaData.setPrimaryKeys("abc", "test", "xyz", testResult2);
        metaData.setPrimaryKeys(null, "test", "xyz", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getPrimaryKeys(null, "test", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getPrimaryKeys(null, "test", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getPrimaryKeys("abc", "TEST", "XYz"));
        assertNull(metaData.getPrimaryKeys("ABC", "TEST", "XYz"));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getPrimaryKeys("ABC", "TEST", "XYz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testProcedures() throws SQLException
    {
        assertNull(metaData.getProcedures(null, null, null));
        assertNull(metaData.getProcedures("abc1", "abc2", "abc3"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setProcedures(testResult);
        assertSame(testResult, metaData.getProcedures(null, null, null));
        assertSame(testResult, metaData.getProcedures("", "test", "xyz"));
        assertSame(testResult, metaData.getProcedures("abc1", "test", "xyz"));
        metaData.clearProcedures();
        assertNull(metaData.getProcedures("", "test", "xyz"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setProcedures("", "test1", "xyz", testResult);
        metaData.setProcedures("abc", "test", "xyz", testResult2);
        metaData.setProcedures(null, "test", "xyz", testResult3);
        assertSame(testResult2, metaData.getProcedures("abc", "test", "xyz"));
        PolyResultSet polyResult = (PolyResultSet)metaData.getProcedures(null, "test", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getProcedures("", "test1", "xyz"));
        assertSame(testResult3, metaData.getProcedures("", "test", "xyz"));
        assertSame(testResult2, metaData.getProcedures("abc", "test", "xyz"));
        assertNull(metaData.getProcedures(null, "test3", "xyz"));
        assertNull(metaData.getProcedures(null, "test", "xyz1"));
        assertNull(metaData.getProcedures(null, "test", null));
        metaData.setProcedures(testResult4);
        assertSame(testResult4, metaData.getProcedures(null, "test3", "xyz"));
        assertSame(testResult4, metaData.getProcedures(null, "test", "xyz1"));
        assertSame(testResult4, metaData.getProcedures(null, "test", null));
        polyResult = (PolyResultSet)metaData.getProcedures(null, "test", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testProceduresWithWildcards() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        metaData.setProcedures("abc", "test", "xyz", testResult);
        assertSame(testResult, metaData.getProcedures("abc", "test", "xyz"));
        assertSame(testResult, metaData.getProcedures(null, "t__t", "%"));
        assertSame(testResult, metaData.getProcedures("abc", "tes%", "___"));
        assertSame(testResult, metaData.getProcedures("abc", "%es%", "x_z"));
        assertSame(testResult, metaData.getProcedures(null, "_est", "xyz"));
        assertSame(testResult, metaData.getProcedures(null, "test", "_yz"));
        assertSame(testResult, metaData.getProcedures("abc", "t%", "xy_"));
        assertSame(testResult, metaData.getProcedures("abc", "test%", "%xyz"));
        assertNull(metaData.getProcedures("ab_", "test", "xyz"));
        assertNull(metaData.getProcedures("a%", "te_t", "xyz"));
        assertNull(metaData.getProcedures("abc", "test", "a%"));
        assertNull(metaData.getProcedures("abc", "t____", "xyz"));
        assertNull(metaData.getProcedures(null, "test", "%xyz_"));
        assertNull(metaData.getProcedures("abc", "test1%", "x_z"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setProcedures("abc1", "abc", "xyz", testResult2);
        metaData.setProcedures("abc1", "abc", "xyz1", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getProcedures(null, "%", "%");
        assertEquals(3, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getProcedures("abc", "%", "xyz") instanceof MockResultSet);
        assertTrue(metaData.getProcedures("abc1", "%", "xyz") instanceof MockResultSet);
        polyResult = (PolyResultSet)metaData.getProcedures(null, "a__", "xyz%");
        assertEquals(2, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getProcedures(null, "a__", "xyz_") instanceof MockResultSet);
        assertNull(metaData.getProcedures("%", "%", "%"));
        polyResult = (PolyResultSet)metaData.getProcedures(null, "ab%", "%");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
    }
    
    public void testProceduresCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setProcedures("abc", "TEST", "XYz", testResult);
        metaData.setProcedures("abc", "test", "xyz", testResult2);
        metaData.setProcedures(null, "test", "xyz", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getProcedures(null, "t_st", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getProcedures(null, "t_st", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getProcedures("abc", "TE%", "XYz"));
        assertNull(metaData.getProcedures("ABC", "TEST", "XYz"));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getProcedures("ABC", "TEST", "_Yz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testSuperTables() throws SQLException
    {
        assertNull(metaData.getSuperTables(null, null, null));
        assertNull(metaData.getSuperTables("abc1", "abc2", "abc3"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setSuperTables(testResult);
        assertSame(testResult, metaData.getSuperTables(null, null, null));
        assertSame(testResult, metaData.getSuperTables("", "test", "xyz"));
        assertSame(testResult, metaData.getSuperTables("abc1", "test", "xyz"));
        metaData.clearSuperTables();
        assertNull(metaData.getSuperTables("", "test", "xyz"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setSuperTables("", "test1", "xyz", testResult);
        metaData.setSuperTables("abc", "test", "xyz", testResult2);
        metaData.setSuperTables(null, "test", "xyz", testResult3);
        assertSame(testResult2, metaData.getSuperTables("abc", "test", "xyz"));
        PolyResultSet polyResult = (PolyResultSet)metaData.getSuperTables(null, "test", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getSuperTables("", "test1", "xyz"));
        assertSame(testResult3, metaData.getSuperTables("", "test", "xyz"));
        assertSame(testResult2, metaData.getSuperTables("abc", "test", "xyz"));
        assertNull(metaData.getSuperTables(null, "test3", "xyz"));
        assertNull(metaData.getSuperTables(null, "test", "xyz1"));
        assertNull(metaData.getSuperTables(null, "test", null));
        metaData.setSuperTables(testResult4);
        assertSame(testResult4, metaData.getSuperTables(null, "test3", "xyz"));
        assertSame(testResult4, metaData.getSuperTables(null, "test", "xyz1"));
        assertSame(testResult4, metaData.getSuperTables(null, "test", null));
        polyResult = (PolyResultSet)metaData.getSuperTables(null, "test", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testSuperTablesWithWildcards() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        metaData.setSuperTables("abc", "test", "xyz", testResult);
        assertSame(testResult, metaData.getSuperTables("abc", "test", "xyz"));
        assertSame(testResult, metaData.getSuperTables(null, "t__t", "%"));
        assertSame(testResult, metaData.getSuperTables("abc", "tes%", "___"));
        assertSame(testResult, metaData.getSuperTables("abc", "%es%", "x_z"));
        assertSame(testResult, metaData.getSuperTables(null, "_est", "xyz"));
        assertSame(testResult, metaData.getSuperTables(null, "test", "_yz"));
        assertSame(testResult, metaData.getSuperTables("abc", "t%", "xy_"));
        assertSame(testResult, metaData.getSuperTables("abc", "test%", "%xyz"));
        assertNull(metaData.getSuperTables("ab_", "test", "xyz"));
        assertNull(metaData.getSuperTables("a%", "te_t", "xyz"));
        assertNull(metaData.getSuperTables("abc", "test", "a%"));
        assertNull(metaData.getSuperTables("abc", "t____", "xyz"));
        assertNull(metaData.getSuperTables(null, "test", "%xyz_"));
        assertNull(metaData.getSuperTables("abc", "test1%", "x_z"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setSuperTables("abc1", "abc", "xyz", testResult2);
        metaData.setSuperTables("abc1", "abc", "xyz1", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getSuperTables(null, "%", "%");
        assertEquals(3, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getSuperTables("abc", "%", "xyz") instanceof MockResultSet);
        assertTrue(metaData.getSuperTables("abc1", "%", "xyz") instanceof MockResultSet);
        polyResult = (PolyResultSet)metaData.getSuperTables(null, "a__", "xyz%");
        assertEquals(2, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getSuperTables(null, "a__", "xyz_") instanceof MockResultSet);
        assertNull(metaData.getSuperTables("%", "%", "%"));
        polyResult = (PolyResultSet)metaData.getSuperTables(null, "ab%", "%");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
    }
    
    public void testSuperTablesCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setSuperTables("abc", "TEST", "XYz", testResult);
        metaData.setSuperTables("abc", "test", "xyz", testResult2);
        metaData.setSuperTables(null, "test", "xyz", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getSuperTables(null, "t_st", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getSuperTables(null, "t_st", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getSuperTables("abc", "TE%", "XYz"));
        assertNull(metaData.getSuperTables("ABC", "TEST", "XYz"));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getSuperTables("ABC", "TEST", "_Yz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testSuperTypes() throws SQLException
    {
        assertNull(metaData.getSuperTypes(null, null, null));
        assertNull(metaData.getSuperTypes("abc1", "abc2", "abc3"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setSuperTypes(testResult);
        assertSame(testResult, metaData.getSuperTypes(null, null, null));
        assertSame(testResult, metaData.getSuperTypes("", "test", "xyz"));
        assertSame(testResult, metaData.getSuperTypes("abc1", "test", "xyz"));
        metaData.clearSuperTypes();
        assertNull(metaData.getSuperTypes("", "test", "xyz"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setSuperTypes("", "test1", "xyz", testResult);
        metaData.setSuperTypes("abc", "test", "xyz", testResult2);
        metaData.setSuperTypes(null, "test", "xyz", testResult3);
        assertSame(testResult2, metaData.getSuperTypes("abc", "test", "xyz"));
        PolyResultSet polyResult = (PolyResultSet)metaData.getSuperTypes(null, "test", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getSuperTypes("", "test1", "xyz"));
        assertSame(testResult3, metaData.getSuperTypes("", "test", "xyz"));
        assertSame(testResult2, metaData.getSuperTypes("abc", "test", "xyz"));
        assertNull(metaData.getSuperTypes(null, "test3", "xyz"));
        assertNull(metaData.getSuperTypes(null, "test", "xyz1"));
        assertNull(metaData.getSuperTypes(null, "test", null));
        metaData.setSuperTypes(testResult4);
        assertSame(testResult4, metaData.getSuperTypes(null, "test3", "xyz"));
        assertSame(testResult4, metaData.getSuperTypes(null, "test", "xyz1"));
        assertSame(testResult4, metaData.getSuperTypes(null, "test", null));
        polyResult = (PolyResultSet)metaData.getSuperTypes(null, "test", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testSuperTypesWithWildcards() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        metaData.setSuperTypes("abc", "test", "xyz", testResult);
        assertSame(testResult, metaData.getSuperTypes("abc", "test", "xyz"));
        assertSame(testResult, metaData.getSuperTypes(null, "t__t", "%"));
        assertSame(testResult, metaData.getSuperTypes("abc", "tes%", "___"));
        assertSame(testResult, metaData.getSuperTypes("abc", "%es%", "x_z"));
        assertSame(testResult, metaData.getSuperTypes(null, "_est", "xyz"));
        assertSame(testResult, metaData.getSuperTypes(null, "test", "_yz"));
        assertSame(testResult, metaData.getSuperTypes("abc", "t%", "xy_"));
        assertSame(testResult, metaData.getSuperTypes("abc", "test%", "%xyz"));
        assertNull(metaData.getSuperTypes("ab_", "test", "xyz"));
        assertNull(metaData.getSuperTypes("a%", "te_t", "xyz"));
        assertNull(metaData.getSuperTypes("abc", "test", "a%"));
        assertNull(metaData.getSuperTypes("abc", "t____", "xyz"));
        assertNull(metaData.getSuperTypes(null, "test", "%xyz_"));
        assertNull(metaData.getSuperTypes("abc", "test1%", "x_z"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setSuperTypes("abc1", "abc", "xyz", testResult2);
        metaData.setSuperTypes("abc1", "abc", "xyz1", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getSuperTypes(null, "%", "%");
        assertEquals(3, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getSuperTypes("abc", "%", "xyz") instanceof MockResultSet);
        assertTrue(metaData.getSuperTypes("abc1", "%", "xyz") instanceof MockResultSet);
        polyResult = (PolyResultSet)metaData.getSuperTypes(null, "a__", "xyz%");
        assertEquals(2, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getSuperTypes(null, "a__", "xyz_") instanceof MockResultSet);
        assertNull(metaData.getSuperTypes("%", "%", "%"));
        polyResult = (PolyResultSet)metaData.getSuperTypes(null, "ab%", "%");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
    }
    
    public void testSuperTypesCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setSuperTypes("abc", "TEST", "XYz", testResult);
        metaData.setSuperTypes("abc", "test", "xyz", testResult2);
        metaData.setSuperTypes(null, "test", "xyz", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getSuperTypes(null, "t_st", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getSuperTypes(null, "t_st", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getSuperTypes("abc", "TE%", "XYz"));
        assertNull(metaData.getSuperTypes("ABC", "TEST", "XYz"));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getSuperTypes("ABC", "TEST", "_Yz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testTablePrivileges() throws SQLException
    {
        assertNull(metaData.getTablePrivileges(null, null, null));
        assertNull(metaData.getTablePrivileges("abc1", "abc2", "abc3"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setTablePrivileges(testResult);
        assertSame(testResult, metaData.getTablePrivileges(null, null, null));
        assertSame(testResult, metaData.getTablePrivileges("", "test", "xyz"));
        assertSame(testResult, metaData.getTablePrivileges("abc1", "test", "xyz"));
        metaData.clearTablePrivileges();
        assertNull(metaData.getTablePrivileges("", "test", "xyz"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setTablePrivileges("", "test1", "xyz", testResult);
        metaData.setTablePrivileges("abc", "test", "xyz", testResult2);
        metaData.setTablePrivileges(null, "test", "xyz", testResult3);
        assertSame(testResult2, metaData.getTablePrivileges("abc", "test", "xyz"));
        PolyResultSet polyResult = (PolyResultSet)metaData.getTablePrivileges(null, "test", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getTablePrivileges("", "test1", "xyz"));
        assertSame(testResult3, metaData.getTablePrivileges("", "test", "xyz"));
        assertSame(testResult2, metaData.getTablePrivileges("abc", "test", "xyz"));
        assertNull(metaData.getTablePrivileges(null, "test3", "xyz"));
        assertNull(metaData.getTablePrivileges(null, "test", "xyz1"));
        assertNull(metaData.getTablePrivileges(null, "test", null));
        metaData.setTablePrivileges(testResult4);
        assertSame(testResult4, metaData.getTablePrivileges(null, "test3", "xyz"));
        assertSame(testResult4, metaData.getTablePrivileges(null, "test", "xyz1"));
        assertSame(testResult4, metaData.getTablePrivileges(null, "test", null));
        polyResult = (PolyResultSet)metaData.getTablePrivileges(null, "test", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testTablePrivilegesWithWildcards() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        metaData.setTablePrivileges("abc", "test", "xyz", testResult);
        assertSame(testResult, metaData.getTablePrivileges("abc", "test", "xyz"));
        assertSame(testResult, metaData.getTablePrivileges(null, "t__t", "%"));
        assertSame(testResult, metaData.getTablePrivileges("abc", "tes%", "___"));
        assertSame(testResult, metaData.getTablePrivileges("abc", "%es%", "x_z"));
        assertSame(testResult, metaData.getTablePrivileges(null, "_est", "xyz"));
        assertSame(testResult, metaData.getTablePrivileges(null, "test", "_yz"));
        assertSame(testResult, metaData.getTablePrivileges("abc", "t%", "xy_"));
        assertSame(testResult, metaData.getTablePrivileges("abc", "test%", "%xyz"));
        assertNull(metaData.getTablePrivileges("ab_", "test", "xyz"));
        assertNull(metaData.getTablePrivileges("a%", "te_t", "xyz"));
        assertNull(metaData.getTablePrivileges("abc", "test", "a%"));
        assertNull(metaData.getTablePrivileges("abc", "t____", "xyz"));
        assertNull(metaData.getTablePrivileges(null, "test", "%xyz_"));
        assertNull(metaData.getTablePrivileges("abc", "test1%", "x_z"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setTablePrivileges("abc1", "abc", "xyz", testResult2);
        metaData.setTablePrivileges("abc1", "abc", "xyz1", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getTablePrivileges(null, "%", "%");
        assertEquals(3, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getTablePrivileges("abc", "%", "xyz") instanceof MockResultSet);
        assertTrue(metaData.getTablePrivileges("abc1", "%", "xyz") instanceof MockResultSet);
        polyResult = (PolyResultSet)metaData.getTablePrivileges(null, "a__", "xyz%");
        assertEquals(2, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getTablePrivileges(null, "a__", "xyz_") instanceof MockResultSet);
        assertNull(metaData.getTablePrivileges("%", "%", "%"));
        polyResult = (PolyResultSet)metaData.getTablePrivileges(null, "ab%", "%");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
    }
    
    public void testTablePrivilegesCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setTablePrivileges("abc", "TEST", "XYz", testResult);
        metaData.setTablePrivileges("abc", "test", "xyz", testResult2);
        metaData.setTablePrivileges(null, "test", "xyz", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getTablePrivileges(null, "t_st", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getTablePrivileges(null, "t_st", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getTablePrivileges("abc", "TE%", "XYz"));
        assertNull(metaData.getTablePrivileges("ABC", "TEST", "XYz"));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getTablePrivileges("ABC", "TEST", "_Yz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testVersionColumns() throws SQLException
    {
        assertNull(metaData.getVersionColumns(null, null, null));
        assertNull(metaData.getVersionColumns("abc1", "abc2", "abc3"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setVersionColumns(testResult);
        assertSame(testResult, metaData.getVersionColumns(null, null, null));
        assertSame(testResult, metaData.getVersionColumns("", "test", "xyz"));
        assertSame(testResult, metaData.getVersionColumns("abc1", "test", "xyz"));
        metaData.clearVersionColumns();
        assertNull(metaData.getVersionColumns("", "test", "xyz"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setVersionColumns("", "test%", "xyz", testResult);
        metaData.setVersionColumns("abc", "test", "xyz", testResult2);
        metaData.setVersionColumns(null, "test", "xyz", testResult3);
        assertSame(testResult2, metaData.getVersionColumns("abc", "test", "xyz"));
        PolyResultSet polyResult = (PolyResultSet)metaData.getVersionColumns(null, "test", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getVersionColumns("", "test%", "xyz"));
        assertSame(testResult3, metaData.getVersionColumns("", "test", "xyz"));
        assertSame(testResult2, metaData.getVersionColumns("abc", "test", "xyz"));
        assertNull(metaData.getVersionColumns(null, "test_", "xyz"));
        assertNull(metaData.getVersionColumns(null, "test", "xyz1"));
        assertNull(metaData.getVersionColumns(null, "test", null));
        metaData.setVersionColumns(testResult4);
        assertSame(testResult4, metaData.getVersionColumns(null, "test3", "xyz"));
        assertSame(testResult4, metaData.getVersionColumns(null, "test", "xyz1"));
        assertSame(testResult4, metaData.getVersionColumns(null, "test", null));
        polyResult = (PolyResultSet)metaData.getVersionColumns(null, "test", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testVersionColumnsCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setVersionColumns("abc", "TEST", "XYz", testResult);
        metaData.setVersionColumns("abc", "test", "xyz", testResult2);
        metaData.setVersionColumns(null, "test", "xyz", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getVersionColumns(null, "test", "xyz");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getVersionColumns(null, "test", "xyz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getVersionColumns("abc", "TEST", "XYz"));
        assertNull(metaData.getVersionColumns("ABC", "TEST", "XYz"));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getVersionColumns("ABC", "TEST", "XYz");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testBestRowIdentifier() throws SQLException
    {
        assertNull(metaData.getBestRowIdentifier(null, null, null, 1, false));
        assertNull(metaData.getBestRowIdentifier("abc1", "abc2", "abc3", 2, true));
        ResultSet testResult = new MockResultSet("id");
        metaData.setBestRowIdentifier(testResult);
        assertSame(testResult, metaData.getBestRowIdentifier(null, null, null, 1, false));
        assertSame(testResult, metaData.getBestRowIdentifier("", "test", "xyz", 2, true));
        assertSame(testResult, metaData.getBestRowIdentifier("abc1", "test", "xyz", 3, false));
        metaData.clearBestRowIdentifier();
        assertNull(metaData.getBestRowIdentifier("", "test", "xyz", 1, false));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setBestRowIdentifier("", "test%", "xyz", 1, false, testResult);
        metaData.setBestRowIdentifier("abc", "test", "xyz", 2, true, testResult2);
        metaData.setBestRowIdentifier(null, "test", "xyz", 2, true, testResult3);
        assertSame(testResult2, metaData.getBestRowIdentifier("abc", "test", "xyz", 2, true));
        PolyResultSet polyResult = (PolyResultSet)metaData.getBestRowIdentifier(null, "test", "xyz", 2, true);
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getBestRowIdentifier("", "test%", "xyz", 1, false));
        assertSame(testResult3, metaData.getBestRowIdentifier("", "test", "xyz", 2, true));
        assertSame(testResult2, metaData.getBestRowIdentifier("abc", "test", "xyz", 2, true));
        assertNull(metaData.getBestRowIdentifier(null, "test_", "xyz", 1, false));
        assertNull(metaData.getBestRowIdentifier(null, "test", "xyz1", 2, true));
        assertNull(metaData.getBestRowIdentifier(null, "test", null, 2, true));
        metaData.setBestRowIdentifier(testResult4);
        assertSame(testResult4, metaData.getBestRowIdentifier(null, "test3", "xyz", 5, false));
        assertSame(testResult4, metaData.getBestRowIdentifier(null, "test", "xyz1", 2, true));
        assertSame(testResult4, metaData.getBestRowIdentifier(null, "test", null, 1, false));
        polyResult = (PolyResultSet)metaData.getBestRowIdentifier(null, "test", "xyz", 2, true);
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testBestRowIdentifierCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setBestRowIdentifier("abc", "TEST", "XYz", 1, false, testResult);
        metaData.setBestRowIdentifier("abc", "test", "xyz", 1, false, testResult2);
        metaData.setBestRowIdentifier(null, "test", "xyz", 1, false, testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getBestRowIdentifier(null, "test", "xyz", 1, false);
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getBestRowIdentifier(null, "test", "xyz", 1, false);
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getBestRowIdentifier("abc", "TEST", "XYz", 1, false));
        assertNull(metaData.getBestRowIdentifier("ABC", "TEST", "XYz", 1, false));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getBestRowIdentifier("ABC", "TEST", "XYz", 1, false);
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testIndexInfo() throws SQLException
    {
        assertNull(metaData.getIndexInfo(null, null, null, true, false));
        assertNull(metaData.getIndexInfo("abc1", "abc2", "abc3", false, true));
        ResultSet testResult = new MockResultSet("id");
        metaData.setIndexInfo(testResult);
        assertSame(testResult, metaData.getIndexInfo(null, null, null, true, false));
        assertSame(testResult, metaData.getIndexInfo("", "test", "xyz", false, true));
        assertSame(testResult, metaData.getIndexInfo("abc1", "test", "xyz", false, false));
        metaData.clearIndexInfo();
        assertNull(metaData.getIndexInfo("", "test", "xyz", true, false));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setIndexInfo("", "test%", "xyz", true, false, testResult);
        metaData.setIndexInfo("abc", "test", "xyz", false, true, testResult2);
        metaData.setIndexInfo(null, "test", "xyz", false, true, testResult3);
        assertSame(testResult2, metaData.getIndexInfo("abc", "test", "xyz", false, true));
        PolyResultSet polyResult = (PolyResultSet)metaData.getIndexInfo(null, "test", "xyz", false, true);
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getIndexInfo("", "test%", "xyz", true, false));
        assertSame(testResult3, metaData.getIndexInfo("", "test", "xyz", false, true));
        assertSame(testResult2, metaData.getIndexInfo("abc", "test", "xyz", false, true));
        assertNull(metaData.getIndexInfo(null, "test_", "xyz", true, false));
        assertNull(metaData.getIndexInfo(null, "test", "xyz1", false, true));
        assertNull(metaData.getIndexInfo(null, "test", null, false, true));
        metaData.setIndexInfo(testResult4);
        assertSame(testResult4, metaData.getIndexInfo(null, "test3", "xyz", true, false));
        assertSame(testResult4, metaData.getIndexInfo(null, "test", "xyz1", false, true));
        assertSame(testResult4, metaData.getIndexInfo(null, "test", null, false, false));
        polyResult = (PolyResultSet)metaData.getIndexInfo(null, "test", "xyz", false, true);
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testIndexInfoCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setIndexInfo("abc", "TEST", "XYz", true, false, testResult);
        metaData.setIndexInfo("abc", "test", "xyz", true, false, testResult2);
        metaData.setIndexInfo(null, "test", "xyz", true, false, testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getIndexInfo(null, "test", "xyz", true, false);
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getIndexInfo(null, "test", "xyz", true, false);
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getIndexInfo("abc", "TEST", "XYz", true, false));
        assertNull(metaData.getIndexInfo("ABC", "TEST", "XYz", true, false));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getIndexInfo("ABC", "TEST", "XYz", true, false);
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testUDTs() throws SQLException
    {
        assertNull(metaData.getUDTs(null, null, null, null));
        assertNull(metaData.getUDTs("abc1", "abc2", "abc3", new int[] {1}));
        ResultSet testResult = new MockResultSet("id");
        metaData.setUDTs(testResult);
        assertSame(testResult, metaData.getUDTs(null, null, null, null));
        assertSame(testResult, metaData.getUDTs("", "test", "xyz", new int[] {1, 2, 3}));
        assertSame(testResult, metaData.getUDTs("abc1", "test", "xyz", new int[] {}));
        metaData.clearUDTs();
        assertNull(metaData.getUDTs("", "test", "xyz", null));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setUDTs("", "test1", "xyz", null, testResult);
        metaData.setUDTs("abc", "test", "xyz", new int[] {1}, testResult2);
        metaData.setUDTs(null, "test", "xyz", new int[] {1, 2, 3}, testResult3);
        assertSame(testResult2, metaData.getUDTs("abc", "test", "xyz", new int[] {1, 2, 3}));
        PolyResultSet polyResult = (PolyResultSet)metaData.getUDTs(null, "test", "xyz", new int[] {1, 2, 3});
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getUDTs("", "test1", "xyz", null));
        assertSame(testResult3, metaData.getUDTs("", "test", "xyz", new int[] {1, 2, 3}));
        assertSame(testResult2, metaData.getUDTs("abc", "test", "xyz", new int[] {1, 2, 3}));
        assertNull(metaData.getUDTs(null, "test3", "xyz", null));
        assertNull(metaData.getUDTs(null, "test", "xyz1", new int[] {1, 2, 3}));
        assertNull(metaData.getUDTs(null, "test", null, new int[] {1, 2, 3}));
        metaData.setUDTs(testResult4);
        assertSame(testResult4, metaData.getUDTs(null, "test3", "xyz", null));
        assertSame(testResult4, metaData.getUDTs(null, "test", "xyz1", new int[] {1, 2, 3}));
        assertSame(testResult4, metaData.getUDTs(null, "test", null, new int[] {}));
        polyResult = (PolyResultSet)metaData.getUDTs(null, "test", "xyz", new int[] {1, 2, 3});
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.clearUDTs();
        metaData.setUDTs("1", "2", "3", null, testResult);
        metaData.setUDTs("1", "2", "3", new int[] {1, 2, 3}, testResult2);
        metaData.setUDTs("1", "2", "3", new int[] {4}, testResult3);
        metaData.setUDTs("1", "2", "3", new int[] {1}, testResult4);
        polyResult = (PolyResultSet)metaData.getUDTs("1", "2", "3", null);
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(4, resultSets.size());
        assertSame(testResult3, metaData.getUDTs("1", "2", "3", new int[] {4}));
    }
    
    public void testUDTsWithWildcards() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        metaData.setUDTs("abc", "test", "xyz", new int[] {1}, testResult);
        assertSame(testResult, metaData.getUDTs("abc", "test", "xyz", new int[] {1}));
        assertSame(testResult, metaData.getUDTs(null, "t__t", "%", new int[] {1}));
        assertSame(testResult, metaData.getUDTs("abc", "tes%", "___", new int[] {1}));
        assertSame(testResult, metaData.getUDTs("abc", "%es%", "x_z", new int[] {1}));
        assertSame(testResult, metaData.getUDTs(null, "_est", "xyz", new int[] {1}));
        assertSame(testResult, metaData.getUDTs(null, "test", "_yz", new int[] {1}));
        assertSame(testResult, metaData.getUDTs("abc", "t%", "xy_", new int[] {1}));
        assertSame(testResult, metaData.getUDTs("abc", "test%", "%xyz", new int[] {1}));
        assertNull(metaData.getUDTs("ab_", "test", "xyz", new int[] {1}));
        assertNull(metaData.getUDTs("a%", "te_t", "xyz", new int[] {1}));
        assertNull(metaData.getUDTs("abc", "test", "a%", new int[] {1}));
        assertNull(metaData.getUDTs("abc", "t____", "xyz", new int[] {1}));
        assertNull(metaData.getUDTs(null, "test", "%xyz_", new int[] {1}));
        assertNull(metaData.getUDTs("abc", "test1%", "x_z", new int[] {1}));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setUDTs("abc1", "abc", "xyz", new int[] {1}, testResult2);
        metaData.setUDTs("abc1", "abc", "xyz1", new int[] {1}, testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getUDTs(null, "%", "%", null);
        assertEquals(3, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getUDTs("abc", "%", "xyz", new int[] {1}) instanceof MockResultSet);
        assertTrue(metaData.getUDTs("abc1", "%", "xyz", null) instanceof MockResultSet);
        polyResult = (PolyResultSet)metaData.getUDTs(null, "a__", "xyz%", new int[] {1});
        assertEquals(2, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getUDTs(null, "a__", "xyz_", null) instanceof MockResultSet);
        assertNull(metaData.getUDTs("%", "%", "%", null));
        polyResult = (PolyResultSet)metaData.getUDTs(null, "ab%", "%", new int[] {1});
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
    }
    
    public void testUDTsWithTypes() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        metaData.setUDTs("abc", "test", "xyz", new int[] {1}, testResult);
        assertNull(metaData.getUDTs("abc", "test", "xyz", new int[] {2}));
        assertEquals(testResult, metaData.getUDTs("abc", "test", "xyz", null));
        assertEquals(testResult, metaData.getUDTs("abc", "test", "xyz", new int[] {1, 2}));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setUDTs("abc", "test", "xyz", new int[] {2, 4}, testResult2);
        assertEquals(testResult2, metaData.getUDTs("abc", "test", "xyz", new int[] {2}));
        PolyResultSet polyResult = (PolyResultSet)metaData.getUDTs("abc", "%", "%", new int[] {1, 2, 3});
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult));
        assertTrue(resultSets.contains(testResult2));
    }
    
    public void testUDTsCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setUDTs("abc", "TEST", "XYz", new int[] {1}, testResult);
        metaData.setUDTs("abc", "test", "xyz", new int[] {1}, testResult2);
        metaData.setUDTs(null, "test", "xyz", new int[] {1}, testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getUDTs(null, "t_st", "xyz", new int[] {1});
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getUDTs(null, "t_st", "xyz", new int[] {1});
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getUDTs("abc", "TE%", "XYz", new int[] {1}));
        assertNull(metaData.getUDTs("ABC", "TEST", "XYz", new int[] {1}));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getUDTs("ABC", "TEST", "_Yz", new int[] {1});
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testAttributes() throws SQLException
    {
        assertNull(metaData.getAttributes(null, null, null, null));
        assertNull(metaData.getAttributes("abc1", "abc2", "abc3", "abc4"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setAttributes(testResult);
        assertSame(testResult, metaData.getAttributes(null, null, null, null));
        assertSame(testResult, metaData.getAttributes("", "test", "xyz", "123"));
        assertSame(testResult, metaData.getAttributes("abc1", "test", "xyz", "12345"));
        metaData.clearAttributes();
        assertNull(metaData.getAttributes("", "test", "xyz", null));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setAttributes("", "test1", "xyz", "123", testResult);
        metaData.setAttributes("abc", "test", "xyz", "456", testResult2);
        metaData.setAttributes(null, "test", "xyz", "456", testResult3);
        assertSame(testResult2, metaData.getAttributes("abc", "test", "xyz", "456"));
        PolyResultSet polyResult = (PolyResultSet)metaData.getAttributes(null, "test", "xyz", "456");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getAttributes("", "test1", "xyz", "123"));
        assertSame(testResult3, metaData.getAttributes("", "test", "xyz", "456"));
        assertSame(testResult2, metaData.getAttributes("abc", "test", "xyz", "456"));
        assertNull(metaData.getAttributes(null, "test3", "xyz", "123"));
        assertNull(metaData.getAttributes(null, "test", "xyz1", "456"));
        assertNull(metaData.getAttributes(null, "test", null, "456"));
        assertNull(metaData.getAttributes("abc", "test", "xyz", null));
        metaData.setAttributes(testResult4);
        assertSame(testResult4, metaData.getAttributes(null, "test3", "xyz", null));
        assertSame(testResult4, metaData.getAttributes(null, "test", "xyz1", "123"));
        assertSame(testResult4, metaData.getAttributes(null, "test", null, null));
        polyResult = (PolyResultSet)metaData.getAttributes(null, "test", "xyz", "456");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testAttributesWithWildcards() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        metaData.setAttributes("abc", "test", "xyz", "123", testResult);
        assertSame(testResult, metaData.getAttributes("abc", "test", "xyz", "123"));
        assertSame(testResult, metaData.getAttributes(null, "t__t", "%", "12_"));
        assertSame(testResult, metaData.getAttributes("abc", "tes%", "___", "123"));
        assertSame(testResult, metaData.getAttributes("abc", "%es%", "x_z", "%%"));
        assertSame(testResult, metaData.getAttributes(null, "_est", "xyz", "1_3%"));
        assertSame(testResult, metaData.getAttributes(null, "test", "_yz", "%123%%"));
        assertSame(testResult, metaData.getAttributes("abc", "t%", "xy_", "___"));
        assertSame(testResult, metaData.getAttributes("abc", "test%", "%xyz", "123"));
        assertNull(metaData.getAttributes("ab_", "test", "xyz", "123"));
        assertNull(metaData.getAttributes("a%", "te_t", "xyz", "123"));
        assertNull(metaData.getAttributes("abc", "test", "a%", "1_3"));
        assertNull(metaData.getAttributes("abc", "t____", "xyz", "1%"));
        assertNull(metaData.getAttributes(null, "test", "%xyz_", "123"));
        assertNull(metaData.getAttributes("abc", "test1%", "x_z", "123"));
        assertNull(metaData.getAttributes("abc", "test%", "xyz", "1___%"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setAttributes("abc1", "abc", "xyz", "456", testResult2);
        metaData.setAttributes("abc1", "abc", "xyz1", "456", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getAttributes(null, "%", "%", "%");
        assertEquals(3, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getAttributes("abc", "%", "xyz", "1__") instanceof MockResultSet);
        assertTrue(metaData.getAttributes("abc1", "%", "xyz", "456") instanceof MockResultSet);
        polyResult = (PolyResultSet)metaData.getAttributes(null, "a__", "xyz%", "_56%");
        assertEquals(2, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getAttributes(null, "a__", "xyz_", "4_6") instanceof MockResultSet);
        assertNull(metaData.getAttributes("%", "%", "%", "%"));
        polyResult = (PolyResultSet)metaData.getAttributes(null, "ab%", "%", "4%");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3)); 
    }
    
    public void testAttributesCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setAttributes("abc", "TEST", "XYz", "efg", testResult);
        metaData.setAttributes("abc", "test", "xyz", "EFg", testResult2);
        metaData.setAttributes(null, "test", "xyz", "EFg", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getAttributes(null, "t_st", "xyz", "EFG");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getAttributes(null, "t_st", "xyz", "EFg");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getAttributes("abc", "TE%", "XYz", "efg"));
        assertNull(metaData.getAttributes("ABC", "TEST", "XYz", "efg"));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getAttributes("ABC", "TEST", "_Yz", "EFg");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testColumnPrivileges() throws SQLException
    {
        assertNull(metaData.getColumnPrivileges(null, null, null, null));
        assertNull(metaData.getColumnPrivileges("abc1", "abc2", "abc3", "abc4"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setColumnPrivileges(testResult);
        assertSame(testResult, metaData.getColumnPrivileges(null, null, null, null));
        assertSame(testResult, metaData.getColumnPrivileges("", "test", "xyz", "123"));
        assertSame(testResult, metaData.getColumnPrivileges("abc1", "test", "xyz", "12345"));
        metaData.clearColumnPrivileges();
        assertNull(metaData.getColumnPrivileges("", "test", "xyz", null));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setColumnPrivileges("", "test1", "xyz", "123", testResult);
        metaData.setColumnPrivileges("abc", "test", "xyz", "456", testResult2);
        metaData.setColumnPrivileges(null, "test", "xyz", "456", testResult3);
        assertSame(testResult2, metaData.getColumnPrivileges("abc", "test", "xyz", "456"));
        PolyResultSet polyResult = (PolyResultSet)metaData.getColumnPrivileges(null, "test", "xyz", "456");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getColumnPrivileges("", "test1", "xyz", "123"));
        assertSame(testResult3, metaData.getColumnPrivileges("", "test", "xyz", "456"));
        assertSame(testResult2, metaData.getColumnPrivileges("abc", "test", "xyz", "456"));
        assertNull(metaData.getColumnPrivileges(null, "test3", "xyz", "123"));
        assertNull(metaData.getColumnPrivileges(null, "test", "xyz1", "456"));
        assertNull(metaData.getColumnPrivileges(null, "test", null, "456"));
        assertNull(metaData.getColumnPrivileges("abc", "test", "xyz", null));
        metaData.setColumnPrivileges(testResult4);
        assertSame(testResult4, metaData.getColumnPrivileges(null, "test3", "xyz", null));
        assertSame(testResult4, metaData.getColumnPrivileges(null, "test", "xyz1", "123"));
        assertSame(testResult4, metaData.getColumnPrivileges(null, "test", null, null));
        polyResult = (PolyResultSet)metaData.getColumnPrivileges(null, "test", "xyz", "456");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testColumnPrivilegesWithWildcards() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        metaData.setColumnPrivileges("abc", "test", "xyz", "123", testResult);
        assertSame(testResult, metaData.getColumnPrivileges("abc", "test", "xyz", "123"));
        assertSame(testResult, metaData.getColumnPrivileges(null, "test", "xyz", "12_"));
        assertSame(testResult, metaData.getColumnPrivileges("abc", "test", "xyz", "%"));
        assertSame(testResult, metaData.getColumnPrivileges("abc", "test", "xyz", "%%"));
        assertSame(testResult, metaData.getColumnPrivileges(null, "test", "xyz", "1_3%"));
        assertNull(metaData.getColumnPrivileges("abc", "te_t", "xyz", "123"));
        assertNull(metaData.getColumnPrivileges("abc", "test", "a%", "1_3"));
        assertNull(metaData.getColumnPrivileges("abc", "t____", "xyz", "1%"));
        assertNull(metaData.getColumnPrivileges(null, "test", "%xyz_", "123"));
        assertNull(metaData.getColumnPrivileges("abc", "test1%", "x_z", "123"));
        assertNull(metaData.getColumnPrivileges("abc", "test%", "xyz", "1___%"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setColumnPrivileges("abc1", "abc", "xyz", "456", testResult2);
        metaData.setColumnPrivileges("abc1", "abc", "xyz1", "456", testResult3);
        assertSame(testResult2, metaData.getColumnPrivileges(null, "abc", "xyz", "%"));
        assertTrue(metaData.getColumnPrivileges("abc", "test", "xyz", "1__") instanceof MockResultSet);
        assertTrue(metaData.getColumnPrivileges("abc1", "abc", "xyz", "456") instanceof MockResultSet);
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setColumnPrivileges("abc1", "abc", "xyz1", "789", testResult4);
        PolyResultSet polyResult = (PolyResultSet)metaData.getColumnPrivileges("abc1", "abc", "xyz1", "___");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testColumnPrivilegesCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setColumnPrivileges("abc", "TEST", "XYz", "efg", testResult);
        metaData.setColumnPrivileges("abc", "test", "xyz", "EFg", testResult2);
        metaData.setColumnPrivileges(null, "test", "xyz", "EFg", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getColumnPrivileges(null, "test", "xyz", "EFG");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getColumnPrivileges(null, "test", "xyz", "E_g");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getColumnPrivileges("abc", "TEST", "XYz", "efg"));
        assertNull(metaData.getColumnPrivileges("ABC", "TEST", "XYz", "efg"));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getColumnPrivileges("ABC", "TEST", "XYz", "EF%");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testColumns() throws SQLException
    {
        assertNull(metaData.getColumns(null, null, null, null));
        assertNull(metaData.getColumns("abc1", "abc2", "abc3", "abc4"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setColumns(testResult);
        assertSame(testResult, metaData.getColumns(null, null, null, null));
        assertSame(testResult, metaData.getColumns("", "test", "xyz", "123"));
        assertSame(testResult, metaData.getColumns("abc1", "test", "xyz", "12345"));
        metaData.clearColumns();
        assertNull(metaData.getColumns("", "test", "xyz", null));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setColumns("", "test1", "xyz", "123", testResult);
        metaData.setColumns("abc", "test", "xyz", "456", testResult2);
        metaData.setColumns(null, "test", "xyz", "456", testResult3);
        assertSame(testResult2, metaData.getColumns("abc", "test", "xyz", "456"));
        PolyResultSet polyResult = (PolyResultSet)metaData.getColumns(null, "test", "xyz", "456");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getColumns("", "test1", "xyz", "123"));
        assertSame(testResult3, metaData.getColumns("", "test", "xyz", "456"));
        assertSame(testResult2, metaData.getColumns("abc", "test", "xyz", "456"));
        assertNull(metaData.getColumns(null, "test3", "xyz", "123"));
        assertNull(metaData.getColumns(null, "test", "xyz1", "456"));
        assertNull(metaData.getColumns(null, "test", null, "456"));
        assertNull(metaData.getColumns("abc", "test", "xyz", null));
        metaData.setColumns(testResult4);
        assertSame(testResult4, metaData.getColumns(null, "test3", "xyz", null));
        assertSame(testResult4, metaData.getColumns(null, "test", "xyz1", "123"));
        assertSame(testResult4, metaData.getColumns(null, "test", null, null));
        polyResult = (PolyResultSet)metaData.getColumns(null, "test", "xyz", "456");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testColumnsWithWildcards() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        metaData.setColumns("abc", "test", "xyz", "123", testResult);
        assertSame(testResult, metaData.getColumns("abc", "test", "xyz", "123"));
        assertSame(testResult, metaData.getColumns(null, "t__t", "%", "12_"));
        assertSame(testResult, metaData.getColumns("abc", "tes%", "___", "123"));
        assertSame(testResult, metaData.getColumns("abc", "%es%", "x_z", "%%"));
        assertSame(testResult, metaData.getColumns(null, "_est", "xyz", "1_3%"));
        assertSame(testResult, metaData.getColumns(null, "test", "_yz", "%123%%"));
        assertSame(testResult, metaData.getColumns("abc", "t%", "xy_", "___"));
        assertSame(testResult, metaData.getColumns("abc", "test%", "%xyz", "123"));
        assertNull(metaData.getColumns("ab_", "test", "xyz", "123"));
        assertNull(metaData.getColumns("a%", "te_t", "xyz", "123"));
        assertNull(metaData.getColumns("abc", "test", "a%", "1_3"));
        assertNull(metaData.getColumns("abc", "t____", "xyz", "1%"));
        assertNull(metaData.getColumns(null, "test", "%xyz_", "123"));
        assertNull(metaData.getColumns("abc", "test1%", "x_z", "123"));
        assertNull(metaData.getColumns("abc", "test%", "xyz", "1___%"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setColumns("abc1", "abc", "xyz", "456", testResult2);
        metaData.setColumns("abc1", "abc", "xyz1", "456", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getColumns(null, "%", "%", "%");
        assertEquals(3, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getColumns("abc", "%", "xyz", "1__") instanceof MockResultSet);
        assertTrue(metaData.getColumns("abc1", "%", "xyz", "456") instanceof MockResultSet);
        polyResult = (PolyResultSet)metaData.getColumns(null, "a__", "xyz%", "_56%");
        assertEquals(2, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getColumns(null, "a__", "xyz_", "4_6") instanceof MockResultSet);
        assertNull(metaData.getColumns("%", "%", "%", "%"));
        polyResult = (PolyResultSet)metaData.getColumns(null, "ab%", "%", "4%");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3)); 
    }
    
    public void testColumnsCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setColumns("abc", "TEST", "XYz", "efg", testResult);
        metaData.setColumns("abc", "test", "xyz", "EFg", testResult2);
        metaData.setColumns(null, "test", "xyz", "EFg", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getColumns(null, "t_st", "xyz", "EFG");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getColumns(null, "t_st", "xyz", "EFg");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getColumns("abc", "TE%", "XYz", "efg"));
        assertNull(metaData.getColumns("ABC", "TEST", "XYz", "efg"));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getColumns("ABC", "TEST", "_Yz", "EFg");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testProcedureColumns() throws SQLException
    {
        assertNull(metaData.getProcedureColumns(null, null, null, null));
        assertNull(metaData.getProcedureColumns("abc1", "abc2", "abc3", "abc4"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setProcedureColumns(testResult);
        assertSame(testResult, metaData.getProcedureColumns(null, null, null, null));
        assertSame(testResult, metaData.getProcedureColumns("", "test", "xyz", "123"));
        assertSame(testResult, metaData.getProcedureColumns("abc1", "test", "xyz", "12345"));
        metaData.clearProcedureColumns();
        assertNull(metaData.getProcedureColumns("", "test", "xyz", null));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setProcedureColumns("", "test1", "xyz", "123", testResult);
        metaData.setProcedureColumns("abc", "test", "xyz", "456", testResult2);
        metaData.setProcedureColumns(null, "test", "xyz", "456", testResult3);
        assertSame(testResult2, metaData.getProcedureColumns("abc", "test", "xyz", "456"));
        PolyResultSet polyResult = (PolyResultSet)metaData.getProcedureColumns(null, "test", "xyz", "456");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getProcedureColumns("", "test1", "xyz", "123"));
        assertSame(testResult3, metaData.getProcedureColumns("", "test", "xyz", "456"));
        assertSame(testResult2, metaData.getProcedureColumns("abc", "test", "xyz", "456"));
        assertNull(metaData.getProcedureColumns(null, "test3", "xyz", "123"));
        assertNull(metaData.getProcedureColumns(null, "test", "xyz1", "456"));
        assertNull(metaData.getProcedureColumns(null, "test", null, "456"));
        assertNull(metaData.getProcedureColumns("abc", "test", "xyz", null));
        metaData.setProcedureColumns(testResult4);
        assertSame(testResult4, metaData.getProcedureColumns(null, "test3", "xyz", null));
        assertSame(testResult4, metaData.getProcedureColumns(null, "test", "xyz1", "123"));
        assertSame(testResult4, metaData.getProcedureColumns(null, "test", null, null));
        polyResult = (PolyResultSet)metaData.getProcedureColumns(null, "test", "xyz", "456");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
    }
    
    public void testProcedureColumnsWithWildcards() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        metaData.setProcedureColumns("abc", "test", "xyz", "123", testResult);
        assertSame(testResult, metaData.getProcedureColumns("abc", "test", "xyz", "123"));
        assertSame(testResult, metaData.getProcedureColumns(null, "t__t", "%", "12_"));
        assertSame(testResult, metaData.getProcedureColumns("abc", "tes%", "___", "123"));
        assertSame(testResult, metaData.getProcedureColumns("abc", "%es%", "x_z", "%%"));
        assertSame(testResult, metaData.getProcedureColumns(null, "_est", "xyz", "1_3%"));
        assertSame(testResult, metaData.getProcedureColumns(null, "test", "_yz", "%123%%"));
        assertSame(testResult, metaData.getProcedureColumns("abc", "t%", "xy_", "___"));
        assertSame(testResult, metaData.getProcedureColumns("abc", "test%", "%xyz", "123"));
        assertNull(metaData.getProcedureColumns("ab_", "test", "xyz", "123"));
        assertNull(metaData.getProcedureColumns("a%", "te_t", "xyz", "123"));
        assertNull(metaData.getProcedureColumns("abc", "test", "a%", "1_3"));
        assertNull(metaData.getProcedureColumns("abc", "t____", "xyz", "1%"));
        assertNull(metaData.getProcedureColumns(null, "test", "%xyz_", "123"));
        assertNull(metaData.getProcedureColumns("abc", "test1%", "x_z", "123"));
        assertNull(metaData.getProcedureColumns("abc", "test%", "xyz", "1___%"));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setProcedureColumns("abc1", "abc", "xyz", "456", testResult2);
        metaData.setProcedureColumns("abc1", "abc", "xyz1", "456", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getProcedureColumns(null, "%", "%", "%");
        assertEquals(3, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getProcedureColumns("abc", "%", "xyz", "1__") instanceof MockResultSet);
        assertTrue(metaData.getProcedureColumns("abc1", "%", "xyz", "456") instanceof MockResultSet);
        polyResult = (PolyResultSet)metaData.getProcedureColumns(null, "a__", "xyz%", "_56%");
        assertEquals(2, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getProcedureColumns(null, "a__", "xyz_", "4_6") instanceof MockResultSet);
        assertNull(metaData.getProcedureColumns("%", "%", "%", "%"));
        polyResult = (PolyResultSet)metaData.getProcedureColumns(null, "ab%", "%", "4%");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3)); 
    }
    
    public void testProcedureColumnsCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setProcedureColumns("abc", "TEST", "XYz", "efg", testResult);
        metaData.setProcedureColumns("abc", "test", "xyz", "EFg", testResult2);
        metaData.setProcedureColumns(null, "test", "xyz", "EFg", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getProcedureColumns(null, "t_st", "xyz", "EFG");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getProcedureColumns(null, "t_st", "xyz", "EFg");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getProcedureColumns("abc", "TE%", "XYz", "efg"));
        assertNull(metaData.getProcedureColumns("ABC", "TEST", "XYz", "efg"));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getProcedureColumns("ABC", "TEST", "_Yz", "EFg");
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
    
    public void testTables() throws SQLException
    {
        assertNull(metaData.getTables(null, null, null, null));
        assertNull(metaData.getTables("abc1", "abc2", "abc3", new String[] {"1", "2", "3"}));
        ResultSet testResult = new MockResultSet("id");
        metaData.setTables(testResult);
        assertSame(testResult, metaData.getTables(null, null, null, null));
        assertSame(testResult, metaData.getTables("", "test", "xyz", new String[] {"1", "2", "3"}));
        assertSame(testResult, metaData.getTables("abc1", "test", "xyz", new String[] {}));
        metaData.clearTables();
        assertNull(metaData.getTables("", "test", "xyz", null));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        ResultSet testResult4 = new MockResultSet("id4");
        metaData.setTables("", "test1", "xyz", null, testResult);
        metaData.setTables("abc", "test", "xyz", new String[] {"1"}, testResult2);
        metaData.setTables(null, "test", "xyz", new String[] {"2", "3"}, testResult3);
        assertSame(testResult2, metaData.getTables("abc", "test", "xyz", new String[] {"1", "2", "3"}));
        PolyResultSet polyResult = (PolyResultSet)metaData.getTables(null, "test", "xyz", new String[] {"1", "2", "3"});
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        assertSame(testResult, metaData.getTables("", "test1", "xyz", null));
        assertSame(testResult3, metaData.getTables("", "test", "xyz", new String[] {"1", "2", "3"}));
        assertSame(testResult2, metaData.getTables("abc", "test", "xyz", new String[] {"1", "2", "3"}));
        assertNull(metaData.getTables(null, "test3", "xyz", null));
        assertNull(metaData.getTables(null, "test", "xyz1", new String[] {"1", "2", "3"}));
        assertNull(metaData.getTables(null, "test", null, new String[] {"1", "2", "3"}));
        metaData.setTables(testResult4);
        assertSame(testResult4, metaData.getTables(null, "test3", "xyz", null));
        assertSame(testResult4, metaData.getTables(null, "test", "xyz1", new String[] {"1", "2", "3"}));
        assertSame(testResult4, metaData.getTables(null, "test", null, new String[] {}));
        polyResult = (PolyResultSet)metaData.getTables(null, "test", "xyz", new String[] {"1", "2", "3"});
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.clearTables();
        metaData.setTables("1", "2", "3", null, testResult);
        metaData.setTables("1", "2", "3", new String[] {"1", "2", "3"}, testResult2);
        metaData.setTables("1", "2", "3", new String[] {"5"}, testResult3);
        metaData.setTables("1", "2", "3", new String[] {"1"}, testResult4);
        polyResult = (PolyResultSet)metaData.getTables("1", "2", "3", null);
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(4, resultSets.size());
        assertSame(testResult3, metaData.getTables("1", "2", "3", new String[] {"5", "6"}));
    }
    
    public void testTablesWithWildcards() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        metaData.setTables("abc", "test", "xyz", new String[] {"1"}, testResult);
        assertSame(testResult, metaData.getTables("abc", "test", "xyz", new String[] {"1"}));
        assertSame(testResult, metaData.getTables(null, "t__t", "%", new String[] {"1"}));
        assertSame(testResult, metaData.getTables("abc", "tes%", "___", new String[] {"1"}));
        assertSame(testResult, metaData.getTables("abc", "%es%", "x_z", new String[] {"1"}));
        assertSame(testResult, metaData.getTables(null, "_est", "xyz", new String[] {"1"}));
        assertSame(testResult, metaData.getTables(null, "test", "_yz", new String[] {"1"}));
        assertSame(testResult, metaData.getTables("abc", "t%", "xy_", new String[] {"1"}));
        assertSame(testResult, metaData.getTables("abc", "test%", "%xyz", new String[] {"1"}));
        assertNull(metaData.getTables("ab_", "test", "xyz", new String[] {"1"}));
        assertNull(metaData.getTables("a%", "te_t", "xyz", new String[] {"1"}));
        assertNull(metaData.getTables("abc", "test", "a%", new String[] {"1"}));
        assertNull(metaData.getTables("abc", "t____", "xyz", new String[] {"1"}));
        assertNull(metaData.getTables(null, "test", "%xyz_", new String[] {"1"}));
        assertNull(metaData.getTables("abc", "test1%", "x_z", new String[] {"1"}));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setTables("abc1", "abc", "xyz", new String[] {"1"}, testResult2);
        metaData.setTables("abc1", "abc", "xyz1", new String[] {"1"}, testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getTables(null, "%", "%", null);
        assertEquals(3, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getTables("abc", "%", "xyz", new String[] {"1"}) instanceof MockResultSet);
        assertTrue(metaData.getTables("abc1", "%", "xyz", null) instanceof MockResultSet);
        polyResult = (PolyResultSet)metaData.getTables(null, "a__", "xyz%", new String[] {"1"});
        assertEquals(2, polyResult.getUnderlyingResultSetList().size());
        assertTrue(metaData.getTables(null, "a__", "xyz_", null) instanceof MockResultSet);
        assertNull(metaData.getTables("%", "%", "%", null));
        polyResult = (PolyResultSet)metaData.getTables(null, "ab%", "%", new String[] {"1"});
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
    }
    
    public void testTablesWithTypes() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        metaData.setTables("abc", "test", "xyz", new String[] {"1"}, testResult);
        assertNull(metaData.getTables("abc", "test", "xyz", new String[] {"2"}));
        assertEquals(testResult, metaData.getTables("abc", "test", "xyz", null));
        assertEquals(testResult, metaData.getTables("abc", "test", "xyz", new String[] {"1", "2"}));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setTables("abc", "test", "xyz", new String[] {"2", "4"}, testResult2);
        assertEquals(testResult2, metaData.getTables("abc", "test", "xyz", new String[] {"2"}));
        PolyResultSet polyResult = (PolyResultSet)metaData.getTables("abc", "%", "%", new String[] {"1", "2", "3"});
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult));
        assertTrue(resultSets.contains(testResult2));
    }
    
    public void testTablesCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setTables("abc", "TEST", "XYz", new String[] {"a"}, testResult);
        metaData.setTables("abc", "test", "xyz", new String[] {"a"}, testResult2);
        metaData.setTables(null, "test", "xyz", new String[] {"a"}, testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getTables(null, "t_st", "xyz", new String[] {"a"});
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        metaData.setCaseSensitive(true);
        polyResult = (PolyResultSet)metaData.getTables(null, "t_st", "xyz", new String[] {"a"});
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertSame(testResult, metaData.getTables("abc", "TE%", "XYz", new String[] {"a"}));
        assertNull(metaData.getTables("ABC", "TEST", "XYz", new String[] {"a"}));
        metaData.setCaseSensitive(false);
        polyResult = (PolyResultSet)metaData.getTables("ABC", "TEST", "_Yz", new String[] {"a"});
        resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
    }
   
    public void testCrossReference() throws SQLException
    {
        assertNull(metaData.getCrossReference(null, null, null, null, null, null));
        assertNull(metaData.getCrossReference(null, "ab", null, "12", null, "34"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setCrossReference(testResult);
        assertSame(testResult, metaData.getCrossReference(null, null, null, null, null, null));
        assertSame(testResult, metaData.getCrossReference("", "", "", "", "", ""));
        assertSame(testResult, metaData.getCrossReference(null, "ab", null, "12", null, "34"));
        metaData.clearCrossReference();
        assertNull(metaData.getCrossReference(null, "ab", null, "12", null, "34"));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setCrossReference(null, "ab", "78", "12", null, "34", testResult);
        metaData.setCrossReference("cd", "ab", "78", "12", "56", "34", testResult2);
        PolyResultSet polyResult = (PolyResultSet)metaData.getCrossReference(null, "ab", "78", "12", null, "34");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(2, resultSets.size());
        assertTrue(resultSets.contains(testResult));
        assertTrue(resultSets.contains(testResult2));
        assertSame(testResult2, metaData.getCrossReference("cd", "ab", "78", "12", "56", "34"));
        assertNull(metaData.getCrossReference(null, "b", "78", "12", null, "34"));
        assertNull(metaData.getCrossReference("cd", "ab", "78", "12", "56", null));
        metaData.setCrossReference(testResult);
        assertSame(testResult, metaData.getCrossReference("cd", "ab", "78", "12", "56", null));
    }
    
    public void testCrossReferenceCaseSensitive() throws SQLException
    {
        ResultSet testResult = new MockResultSet("id");
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setCrossReference("ab", "cd", "ef", "gh", "ij", "kl", testResult);
        metaData.setCrossReference("Ab", "cd", "EF", "gh", "ij", "kl", testResult2);
        metaData.setCrossReference("ab", "CD", "ef", "gH", "IJ", "kl", testResult3);
        PolyResultSet polyResult = (PolyResultSet)metaData.getCrossReference("ab", "Cd", "ef", "gh", "ij", "kl");
        List resultSets = polyResult.getUnderlyingResultSetList();
        assertEquals(3, resultSets.size());
        assertTrue(resultSets.contains(testResult));
        assertTrue(resultSets.contains(testResult2));
        assertTrue(resultSets.contains(testResult3));
        metaData.setCaseSensitive(true);
        assertSame(testResult2, metaData.getCrossReference("Ab", "cd", "EF", "gh", "ij", "kl"));
    }
}
