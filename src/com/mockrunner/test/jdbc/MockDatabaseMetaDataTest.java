package com.mockrunner.test.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

import com.mockrunner.mock.jdbc.MockDatabaseMetaData;
import com.mockrunner.mock.jdbc.MockResultSet;

public class MockDatabaseMetaDataTest extends TestCase
{
    private MockDatabaseMetaData metaData;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        metaData = new MockDatabaseMetaData();
    }
    
    public void testExportedKeys() throws SQLException
    {
        assertNull(metaData.getExportedKeys(null, null, null));
        ResultSet testResult = new MockResultSet("id");
        metaData.setExportedKeys(testResult);
        assertSame(testResult, metaData.getExportedKeys(null, null, null));
        assertSame(testResult, metaData.getExportedKeys("", "test", "xyz"));
        metaData.clearExportedKeys();
        assertNull(metaData.getExportedKeys("", "test", "xyz"));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setExportedKeys("", "test", "xyz", testResult);
        metaData.setExportedKeys("abc", "test", "xyz", testResult2);
        assertSame(testResult, metaData.getExportedKeys("", "test", "xyz"));
        assertSame(testResult2, metaData.getExportedKeys("abc", "test", "xyz"));
        assertNull(metaData.getExportedKeys(null, null, null));
        assertNull(metaData.getExportedKeys("Abc", "test", "xyz"));
        metaData.setExportedKeys(testResult);
        assertSame(testResult, metaData.getExportedKeys("Abc", "test", "xyz"));
    }
    
    public void testImportedKeys() throws SQLException
    {
        assertNull(metaData.getImportedKeys(null, null, null));
        ResultSet testResult = new MockResultSet("id");
        metaData.setImportedKeys(testResult);
        assertSame(testResult, metaData.getImportedKeys(null, null, null));
        assertSame(testResult, metaData.getImportedKeys("", "123", "xyz"));
        metaData.clearImportedKeys();
        assertNull(metaData.getImportedKeys("", "123", "xyz"));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setImportedKeys("", "123", "xyz", testResult);
        metaData.setImportedKeys("abc", "test", "xyz", testResult2);
        assertSame(testResult, metaData.getImportedKeys("", "123", "xyz"));
        assertSame(testResult2, metaData.getImportedKeys("abc", "test", "xyz"));
        assertNull(metaData.getImportedKeys(null, null, null));
        assertNull(metaData.getImportedKeys("abc", "xyz", "test"));
        metaData.setImportedKeys(testResult);
        assertSame(testResult, metaData.getImportedKeys("abc", "xyz", "test"));
    }
    
    public void testPrimaryKeys() throws SQLException
    {
        assertNull(metaData.getPrimaryKeys("", null, ""));
        ResultSet testResult = new MockResultSet("id");
        metaData.setPrimaryKeys(testResult);
        assertSame(testResult, metaData.getPrimaryKeys("", null, ""));
        assertSame(testResult, metaData.getPrimaryKeys("", "", ""));
        metaData.clearPrimaryKeys();
        assertNull(metaData.getPrimaryKeys("", "", ""));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setPrimaryKeys(null, null, null, testResult);
        metaData.setPrimaryKeys("abc", "test", "xyz", testResult2);
        assertSame(testResult, metaData.getPrimaryKeys(null, null, null));
        assertSame(testResult2, metaData.getPrimaryKeys("abc", "test", "xyz"));
        assertNull(metaData.getPrimaryKeys(null, "", null));
        assertNull(metaData.getPrimaryKeys("abc", "", "xyz"));
        metaData.setPrimaryKeys(testResult);
        assertSame(testResult, metaData.getPrimaryKeys("abc", "", "xyz"));
    }
    
    public void testSuperTables() throws SQLException
    {
        assertNull(metaData.getSuperTables("1", "2", "3"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setSuperTables(testResult);
        assertSame(testResult, metaData.getSuperTables("1", null, "3"));
        assertSame(testResult, metaData.getSuperTables("1", "2", "3"));
        metaData.clearSuperTables();
        assertNull(metaData.getPrimaryKeys("1", "2", "3"));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setSuperTables(null, "2", "3", testResult);
        metaData.setSuperTables("1", "2", "3", testResult2);
        assertSame(testResult, metaData.getSuperTables(null, "2", "3"));
        assertSame(testResult2, metaData.getSuperTables("1", "2", "3"));
        assertNull(metaData.getSuperTables("1", null, "3"));
        assertNull(metaData.getSuperTables("1", "3", "3"));
        metaData.setSuperTables(testResult);
        assertSame(testResult, metaData.getSuperTables("1", "3", "3"));
    }
    
    public void testSuperTypes() throws SQLException
    {
        assertNull(metaData.getSuperTypes(null, "2", "3"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setSuperTypes(testResult);
        assertSame(testResult, metaData.getSuperTypes(null, "2", "3"));
        assertSame(testResult, metaData.getSuperTypes("1", "2", "3"));
        metaData.clearSuperTypes();
        assertNull(metaData.getPrimaryKeys("1", "2", "3"));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setSuperTypes(null, "2", "3", testResult);
        metaData.setSuperTypes("1", "2", "3", testResult2);
        assertSame(testResult, metaData.getSuperTypes(null, "2", "3"));
        assertSame(testResult2, metaData.getSuperTypes("1", "2", "3"));
        assertNull(metaData.getSuperTypes("1", null, "3"));
        assertNull(metaData.getSuperTypes("", "2", "3"));
        metaData.setSuperTypes(testResult);
        assertSame(testResult, metaData.getSuperTypes("1", null, "3"));
    }
    
    public void testTablePrivileges() throws SQLException
    {
        assertNull(metaData.getTablePrivileges(null, null, null));
        ResultSet testResult = new MockResultSet("id");
        metaData.setTablePrivileges(testResult);
        assertSame(testResult, metaData.getTablePrivileges(null, "a", "b"));
        assertSame(testResult, metaData.getTablePrivileges(null, null, null));
        metaData.clearTablePrivileges();
        assertNull(metaData.getPrimaryKeys("1", "2", "3"));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setTablePrivileges(null, "a", null, testResult);
        metaData.setTablePrivileges("a", "a", "a", testResult2);
        assertSame(testResult, metaData.getTablePrivileges(null, "a", null));
        assertSame(testResult2, metaData.getTablePrivileges("a", "a", "a"));
        assertNull(metaData.getTablePrivileges(null, null, null));
        assertNull(metaData.getTablePrivileges("", "a", "a"));
        metaData.setTablePrivileges(testResult);
        assertSame(testResult, metaData.getTablePrivileges("", "a", "a"));
    }
    
    public void testVersionColumns() throws SQLException
    {
        assertNull(metaData.getVersionColumns("", null, null));
        ResultSet testResult = new MockResultSet("id");
        metaData.setVersionColumns(testResult);
        assertSame(testResult, metaData.getVersionColumns("", "", ""));
        assertSame(testResult, metaData.getVersionColumns(null, null, null));
        assertSame(testResult, metaData.getVersionColumns("", null, null));
        metaData.clearVersionColumns();
        assertNull(metaData.getPrimaryKeys("", "", ""));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setVersionColumns(null, "", null, testResult);
        metaData.setVersionColumns("a", "", "a", testResult2);
        assertSame(testResult, metaData.getVersionColumns(null, "", null));
        assertSame(testResult2, metaData.getVersionColumns("a", "", "a"));
        assertNull(metaData.getVersionColumns(null, "a", null));
        assertNull(metaData.getVersionColumns(null, "", "a"));
        metaData.setVersionColumns(testResult);
        assertSame(testResult, metaData.getVersionColumns(null, "", "a"));
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
        metaData.setCrossReference(null, "ab", null, "12", null, "34", testResult);
        metaData.setCrossReference("cd", "ab", "78", "12", "56", "34", testResult2);
        assertSame(testResult, metaData.getCrossReference(null, "ab", null, "12", null, "34"));
        assertSame(testResult2, metaData.getCrossReference("cd", "ab", "78", "12", "56", "34"));
        assertNull(metaData.getCrossReference(null, "b", null, "12", null, "34"));
        assertNull(metaData.getCrossReference("cd", "ab", "78", "12", "56", null));
        metaData.setCrossReference(testResult);
        assertSame(testResult, metaData.getCrossReference("cd", "ab", "78", "12", "56", null));
    }
    
    public void testBestRowIdentifier() throws SQLException
    {
        assertNull(metaData.getBestRowIdentifier(null, null, null, 1, false));
        ResultSet testResult = new MockResultSet("id");
        metaData.setBestRowIdentifier(testResult);
        assertSame(testResult, metaData.getBestRowIdentifier(null, null, null, 1, false));
        assertSame(testResult, metaData.getBestRowIdentifier("", "", "", 3, true));
        assertSame(testResult, metaData.getBestRowIdentifier("ab", "cd", "ef", 3, true));
        metaData.clearBestRowIdentifier();
        assertNull(metaData.getBestRowIdentifier(null, null, null, 1, false));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setBestRowIdentifier(null, null, null, 1, false, testResult);
        metaData.setBestRowIdentifier("ab", "cd", "ef", 3, false, testResult2);
        assertSame(testResult, metaData.getBestRowIdentifier(null, null, null, 1, false));
        assertSame(testResult2, metaData.getBestRowIdentifier("ab", "cd", "ef", 3, false));
        assertNull(metaData.getBestRowIdentifier("ab", "cd", "ef", 3, true));
        metaData.setBestRowIdentifier(testResult);
        assertSame(testResult, metaData.getBestRowIdentifier("ab", "cd", "ef", 3, true));
    }
    
    public void testIndexInfo() throws SQLException
    {
        assertNull(metaData.getIndexInfo(null, null, null, true, false));
        ResultSet testResult = new MockResultSet("id");
        metaData.setIndexInfo(testResult);
        assertSame(testResult, metaData.getIndexInfo(null, null, null, true, false));
        assertSame(testResult, metaData.getIndexInfo("", "", "", true, true));
        metaData.clearIndexInfo();
        assertNull(metaData.getIndexInfo(null, null, null, true, false));
        assertNull(metaData.getIndexInfo("", "", "", true, true));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setIndexInfo(null, null, null, true, false, testResult);
        metaData.setIndexInfo("12", "34", "56", true, true, testResult2);
        assertSame(testResult, metaData.getIndexInfo(null, null, null, true, false));
        assertSame(testResult2, metaData.getIndexInfo("12", "34", "56", true, true));
        assertNull(metaData.getIndexInfo(null, null, null, false, false));
        assertNull(metaData.getIndexInfo(null, null, null, false, true));
        metaData.setIndexInfo(testResult);
        assertSame(testResult, metaData.getIndexInfo(null, null, null, false, true));
    }
    
    public void testProcedures() throws SQLException
    {
        assertNull(metaData.getProcedures(null, null, null));
        ResultSet testResult = new MockResultSet("id");
        metaData.setProcedures(testResult);
        assertSame(testResult, metaData.getProcedures(null, null, null));
        assertSame(testResult, metaData.getProcedures("", "", ""));
        metaData.clearProcedures();
        assertNull(metaData.getProcedures(null, null, null));
        assertNull(metaData.getProcedures("", "", ""));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setProcedures(null, null, null, testResult);
        metaData.setProcedures("12", "34", "56", testResult2);
        assertSame(testResult, metaData.getProcedures(null, null, null));
        assertSame(testResult2, metaData.getProcedures("12", "34", "56"));
        assertNull(metaData.getProcedures(null, null, ""));
        assertNull(metaData.getProcedures("12", "34", "5"));
        metaData.setProcedures(testResult);
        assertSame(testResult, metaData.getProcedures(null, null, ""));
        assertSame(testResult, metaData.getProcedures("12", "34", "5"));
    }
    
    public void testTables() throws SQLException
    {
        assertNull(metaData.getTables(null, null, null, null));
        assertNull(metaData.getTables(null, null, null, new String[0]));
        assertNull(metaData.getTables(null, null, null, new String[1]));
        ResultSet testResult = new MockResultSet("id");
        metaData.setTables(testResult);
        assertSame(testResult, metaData.getTables(null, null, null, null));
        assertSame(testResult, metaData.getTables(null, null, null, new String[0]));
        assertSame(testResult, metaData.getTables(null, null, null, new String[1]));
        metaData.clearTables();
        assertNull(metaData.getTables(null, null, null, null));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setTables(null, "", null, null, testResult);
        metaData.setTables(null, "ab", "cd", new String[1], testResult2);
        metaData.setTables(null, null, null, new String[] {"1", "2", "3"}, testResult3);
        assertSame(testResult, metaData.getTables(null, "", null, null));
        assertSame(testResult2, metaData.getTables(null, "ab", "cd", new String[1]));
        assertSame(testResult3, metaData.getTables(null, null, null, new String[] {"1", "2", "3"}));
        assertNull(metaData.getTables(null, "", null, new String[0]));
        assertNull(metaData.getTables(null, "ab", "cd", new String[] {"3", "1", "3"}));
        assertNull(metaData.getTables(null, null, null, new String[] {"1", "2", "3", null}));
        metaData.setTables(testResult);
        assertSame(testResult, metaData.getTables(null, "", null, new String[0]));
        assertSame(testResult, metaData.getTables(null, "ab", "cd", new String[] {"3", "1", "3"}));
        assertSame(testResult, metaData.getTables(null, null, null, new String[] {"1", "2", "3", null}));
    }
    
    public void testUDTs() throws SQLException
    {
        assertNull(metaData.getUDTs(null, null, null, null));
        assertNull(metaData.getUDTs(null, null, null, new int[0]));
        assertNull(metaData.getUDTs(null, null, null, new int[1]));
        ResultSet testResult = new MockResultSet("id");
        metaData.setUDTs(testResult);
        assertSame(testResult, metaData.getUDTs(null, null, null, null));
        assertSame(testResult, metaData.getUDTs("", "", null, new int[0]));
        assertSame(testResult, metaData.getUDTs(null, null, null, new int[1]));
        metaData.clearUDTs();
        assertNull(metaData.getUDTs(null, null, null, new int[1]));
        assertNull(metaData.getUDTs("", "", null, new int[0]));
        ResultSet testResult2 = new MockResultSet("id2");
        ResultSet testResult3 = new MockResultSet("id3");
        metaData.setUDTs("", "", "", null, testResult);
        metaData.setUDTs("1", "2", "3", new int[2], testResult2);
        metaData.setUDTs(null, null, "3", new int[] {1, 2, 3}, testResult3);
        assertSame(testResult, metaData.getUDTs("", "", "", null));
        assertSame(testResult2, metaData.getUDTs("1", "2", "3", new int[2]));
        assertSame(testResult3, metaData.getUDTs(null, null, "3", new int[] {1, 2, 3}));
        assertNull(metaData.getUDTs("", "", "", new int[1]));
        assertNull(metaData.getUDTs("1", "2", "3", new int[] {2, 3, 1}));
        assertNull(metaData.getUDTs(null, null, "3", new int[] {1, 2, 3, 4}));
        metaData.setUDTs(testResult);
        assertSame(testResult, metaData.getUDTs("", "", "", new int[1]));
        assertSame(testResult, metaData.getUDTs("1", "2", "3", new int[] {2, 3, 1}));
        assertSame(testResult, metaData.getUDTs(null, null, "3", new int[] {1, 2, 3, 4}));
    }
    
    public void testAttributes() throws SQLException
    {
        assertNull(metaData.getAttributes(null, null, null, null));
        assertNull(metaData.getAttributes(null, null, null, "5"));
        ResultSet testResult = new MockResultSet("id");
        metaData.setAttributes(testResult);
        assertSame(testResult, metaData.getAttributes(null, null, null, null));
        assertSame(testResult, metaData.getAttributes(null, null, null, "5"));
        metaData.clearAttributes();
        assertNull(metaData.getAttributes(null, null, null, "5"));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setAttributes("", "", "", null, testResult);
        metaData.setAttributes("1", "2", "3", "4", testResult2);
        assertSame(testResult, metaData.getAttributes("", "", "", null));
        assertSame(testResult2, metaData.getAttributes("1", "2", "3", "4"));
        assertNull(metaData.getAttributes("", "", null, ""));
        assertNull(metaData.getAttributes("1", "2", "3", "5"));
        metaData.setAttributes(testResult);
        assertSame(testResult, metaData.getAttributes("", "", null, ""));
        assertSame(testResult, metaData.getAttributes("1", "2", "3", "5"));
    }
    
    public void testProcedureColumns() throws SQLException
    {
        assertNull(metaData.getProcedureColumns("", null, null, null));
        ResultSet testResult = new MockResultSet("id");
        metaData.setProcedureColumns(testResult);
        assertSame(testResult, metaData.getProcedureColumns(null, null, null, null));
        assertSame(testResult, metaData.getProcedureColumns("", null, null, null));
        metaData.clearProcedureColumns();
        assertNull(metaData.getProcedureColumns(null, null, null, null));
        assertNull(metaData.getProcedureColumns("", null, null, null));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setProcedureColumns("", "", "", "", testResult);
        metaData.setProcedureColumns("1", null, "3", null, testResult2);
        assertSame(testResult, metaData.getProcedureColumns("", "", "", ""));
        assertSame(testResult2, metaData.getProcedureColumns("1", null, "3", null));
        assertNull(metaData.getProcedureColumns(null, null, null, null));
        assertNull(metaData.getProcedureColumns(null, "2", null, "5"));
        metaData.setProcedureColumns(testResult);
        assertSame(testResult, metaData.getProcedureColumns(null, null, null, null));
        assertSame(testResult, metaData.getProcedureColumns(null, "2", null, "5"));
    }
    
    public void testColumns() throws SQLException
    {
        assertNull(metaData.getColumns(null, null, null, null));
        ResultSet testResult = new MockResultSet("id");
        metaData.setColumns(testResult);
        assertSame(testResult, metaData.getColumns(null, null, null, null));
        assertSame(testResult, metaData.getColumns("1", "2", "3", null));
        metaData.clearColumns();
        assertNull(metaData.getColumns("1", "2", "3", null));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setColumns("", null, "", "", testResult);
        metaData.setColumns("1", "2", "3", null, testResult2);
        assertSame(testResult, metaData.getColumns("", null, "", ""));
        assertSame(testResult2, metaData.getColumns("1", "2", "3", null));
        assertNull(metaData.getColumns("", "", "", ""));
        assertNull(metaData.getColumns("1", "2", "3", "4"));
        metaData.setColumns(testResult);
        assertSame(testResult, metaData.getColumns("", "", "", ""));
        assertSame(testResult, metaData.getColumns("1", "2", "3", "4"));
    }
    
    public void testColumnPrivileges() throws SQLException
    {
        assertNull(metaData.getColumnPrivileges(null, null, null, ""));
        ResultSet testResult = new MockResultSet("id");
        metaData.setColumnPrivileges(testResult);
        assertSame(testResult, metaData.getColumnPrivileges(null, null, null, ""));
        assertSame(testResult, metaData.getColumnPrivileges("1", "2", "3", null));
        metaData.clearColumnPrivileges();
        assertNull(metaData.getColumnPrivileges(null, null, null, ""));
        assertNull(metaData.getColumnPrivileges("1", "2", "3", null));
        ResultSet testResult2 = new MockResultSet("id2");
        metaData.setColumnPrivileges("", null, "ab", "", testResult);
        metaData.setColumnPrivileges("1", "2", "3", "4", testResult2);
        assertSame(testResult, metaData.getColumnPrivileges("", null, "ab", ""));
        assertSame(testResult2, metaData.getColumnPrivileges("1", "2", "3", "4"));
        assertNull(metaData.getColumns("", null, "a", ""));
        assertNull(metaData.getColumns("1", "2", "3", null));
        metaData.setColumns(testResult);
        assertSame(testResult, metaData.getColumns("", null, "a", ""));
        assertSame(testResult, metaData.getColumns("1", "2", "3", null));
    }
}
