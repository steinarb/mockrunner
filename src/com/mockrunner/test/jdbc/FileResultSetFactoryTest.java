package com.mockrunner.test.jdbc;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.mockrunner.jdbc.FileResultSetFactory;
import com.mockrunner.mock.jdbc.MockResultSet;

public class FileResultSetFactoryTest extends TestCase
{
    public void testGoodCreate() throws Exception
    {
        FileResultSetFactory factory = new FileResultSetFactory("src/com/mockrunner/test/jdbc/testresult.txt");
        MockResultSet resultSet = factory.create("");
        doTestResultSet(factory, resultSet);
        factory = new FileResultSetFactory("/com/mockrunner/test/jdbc/testresult.txt");
        resultSet = factory.create("");
        doTestResultSet(factory, resultSet);
        factory = new FileResultSetFactory("com/mockrunner/test/jdbc/testresult.txt");
        resultSet = factory.create("");
        doTestResultSet(factory, resultSet);
        factory = new FileResultSetFactory(new File("src/com/mockrunner/test/jdbc/testresult.txt"));
        resultSet = factory.create("");
        doTestResultSet(factory, resultSet);
    }
    
    public void testBadCreate() throws Exception
    {
        FileResultSetFactory factory = new FileResultSetFactory("not found");
        try
        {
            factory.create("");
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        factory = new FileResultSetFactory(new File("not found"));
        try
        {
            factory.create("");
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }

    private void doTestResultSet(FileResultSetFactory factory, MockResultSet resultSet) throws SQLException
    {
        assertEquals(5, resultSet.getRowCount());
        assertEquals(3, resultSet.getColumnCount());
        resultSet.next();
        assertEquals("TestColumn1", resultSet.getString(1));
        assertEquals("TestColumn2", resultSet.getString(2));
        assertEquals("TestColumn3", resultSet.getString(3));
        resultSet.next();
        assertEquals(1, resultSet.getInt(1));
        assertEquals(3, resultSet.getLong("Column2"));
        assertEquals(4, resultSet.getShort(3));
        resultSet.next();
        assertEquals("Entry1", resultSet.getObject(1));
        assertEquals("Entry2", resultSet.getString(2));
        assertEquals("Entry3", resultSet.getObject("Column3"));
        resultSet.next();
        assertEquals(25.3, resultSet.getDouble("Column1"), 0.01);
        assertEquals(26.7, resultSet.getDouble(2), 0.01);
        assertEquals(12.3, resultSet.getFloat(3), 0.01);
        resultSet.next();
        assertEquals("Test", resultSet.getString(1));
        assertEquals(null, resultSet.getString(2));
        assertEquals("Test", resultSet.getString(3));
        factory.setFirstLineContainsColumnNames(true);
        resultSet = factory.create("");
        assertEquals(4, resultSet.getRowCount());
        assertEquals(3, resultSet.getColumnCount());
        resultSet.next();
        assertEquals(1, resultSet.getInt("TestColumn1"));
        assertEquals(3, resultSet.getLong("TestColumn2"));
        assertEquals(4, resultSet.getShort("TestColumn3"));
        resultSet.next();
        assertEquals("Entry1", resultSet.getObject(1));
        assertEquals("Entry2", resultSet.getString(2));
        assertEquals("Entry3", resultSet.getObject(3));
    }
    
    public void testCreateWithTemplates() throws Exception
    {
        FileResultSetFactory factory = new FileResultSetFactory("src/com/mockrunner/test/jdbc/testtemplateresult.txt");
        factory.setFirstLineContainsColumnNames(true);
        MockResultSet resultSet = factory.create("");
        doTestResultSetTemplatesDisabled(factory, resultSet);
        factory.setUseTemplates(true);
        resultSet = factory.create("");
        doTestResultSetDefaultTemplatesEnabled(factory, resultSet);
        Map customMap = new HashMap();
        customMap.put("customMarker", "template1");
        customMap.put("anotherCustomMarker", "template2");
        factory.setTemplateConfiguration("%", customMap);
        resultSet = factory.create("");
        doTestResultSetCustomTemplatesEnabled(factory, resultSet);
        factory.setUseTemplates(false);
        resultSet = factory.create("");
        doTestResultSetTemplatesDisabled(factory, resultSet);
    }
    
    private void doTestResultSetTemplatesDisabled(FileResultSetFactory factory, MockResultSet resultSet) throws SQLException
    {
        assertEquals(2, resultSet.getRowCount());
        assertEquals(3, resultSet.getColumnCount());
        resultSet.next();
        assertEquals("$defaultDate", resultSet.getString("TestColumn1"));
        assertEquals("$defaultString", resultSet.getString("TestColumn2"));
        assertEquals("$defaultInteger", resultSet.getString(3));
        resultSet.next();
        assertEquals("%customMarker", resultSet.getObject(1));
        assertEquals(null, resultSet.getString(2));
        assertEquals("%anotherCustomMarker", resultSet.getObject("TestColumn3"));
    }
    
    private void doTestResultSetDefaultTemplatesEnabled(FileResultSetFactory factory, MockResultSet resultSet) throws SQLException
    {
        assertEquals(2, resultSet.getRowCount());
        assertEquals(3, resultSet.getColumnCount());
        resultSet.next();
        assertEquals("1970-01-01", resultSet.getString(1));
        assertEquals("", resultSet.getString("TestColumn2"));
        assertEquals(0, resultSet.getInt("TestColumn3"));
        resultSet.next();
        assertEquals("%customMarker", resultSet.getObject(1));
        assertEquals(null, resultSet.getString(2));
        assertEquals("%anotherCustomMarker", resultSet.getObject("TestColumn3"));
    }
    
    private void doTestResultSetCustomTemplatesEnabled(FileResultSetFactory factory, MockResultSet resultSet) throws SQLException
    {
        assertEquals(2, resultSet.getRowCount());
        assertEquals(3, resultSet.getColumnCount());
        resultSet.next();
        assertEquals("$defaultDate", resultSet.getString("TestColumn1"));
        assertEquals("$defaultString", resultSet.getString("TestColumn2"));
        assertEquals("$defaultInteger", resultSet.getString(3));
        resultSet.next();
        assertEquals("template1", resultSet.getObject(1));
        assertEquals(null, resultSet.getString("TestColumn2"));
        assertEquals("template2", resultSet.getObject("TestColumn3"));
    }
    
    public void testGetFile()
    {
        FileResultSetFactory factory = new FileResultSetFactory("src/com/mockrunner/test/jdbc/testresult.txt");
        assertEquals(new File("src/com/mockrunner/test/jdbc/testresult.txt"), factory.getFile());
        factory = new FileResultSetFactory(new File("src/com/mockrunner/test/jdbc/testresult.txt"));
        assertEquals(new File("src/com/mockrunner/test/jdbc/testresult.txt"), factory.getFile());
        factory = new FileResultSetFactory("badfile");
        try
        {
            factory.getFile();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
        factory = new FileResultSetFactory(new File("badfile"));
        try
        {
            factory.getFile();
            fail();
        } 
        catch(RuntimeException exc)
        {
            //should throw exception
        }
    }
}
