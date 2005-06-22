package com.mockrunner.test.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.PolyResultSet;

import junit.framework.TestCase;

public class PolyResultSetTest extends TestCase
{
    private List getTestResultSetList()
    {
        List list = new ArrayList();
        MockResultSet resultSet1 = new MockResultSet("id1");
        resultSet1.addRow(new String[] {"id1row1column1", "id1row1column2"});
        resultSet1.addRow(new String[] {"id1row2column1", "id1row2column2"});
        MockResultSet resultSet2 = new MockResultSet("id2");
        resultSet1.addRow(new String[] {"id2row1column1", "id2row1column2"});
        MockResultSet resultSet3 = new MockResultSet("id3");
        resultSet1.addRow(new String[] {"id3row1column1", "id3row1column2"});
        resultSet1.addRow(new String[] {"id3row2column1", "id3row2column2"});
        resultSet1.addRow(new String[] {"id3row3column1", "id3row3column2"});
        list.add(resultSet1);
        list.add(resultSet2);
        list.add(resultSet3);
        return list;
    }
    
    public void testGetUnderlyingResultSetList()
    {
        PolyResultSet resultSet = new PolyResultSet(getTestResultSetList());
        List resultSetList = resultSet.getUnderlyingResultSetList();
        assertEquals(3, resultSetList.size());
        MockResultSet resultSet1 = (MockResultSet)resultSetList.get(0);
        MockResultSet resultSet2 = (MockResultSet)resultSetList.get(1);
        MockResultSet resultSet3 = (MockResultSet)resultSetList.get(2);
        assertEquals("id1", resultSet1.getId());
        assertEquals("id2", resultSet2.getId());
        assertEquals("id3", resultSet3.getId());
    }
    
    public void testNext() throws Exception
    {
        PolyResultSet resultSet = new PolyResultSet(new ArrayList());
        assertFalse(resultSet.next());
        assertFalse(resultSet.next());
        resultSet = new PolyResultSet(getTestResultSetList());
        assertTrue(resultSet.next());
        assertEquals("id1row1column1", resultSet.getString(1));
        assertEquals("id1row1column2", resultSet.getString(2));
        assertTrue(resultSet.next());
        assertEquals("id1row2column1", resultSet.getString(1));
        assertEquals("id1row2column2", resultSet.getString(2));
        assertTrue(resultSet.next());
        assertEquals("id2row1column1", resultSet.getString(1));
        assertEquals("id2row1column2", resultSet.getString(2));
        assertTrue(resultSet.next());
        assertEquals("id3row1column1", resultSet.getString(1));
        assertEquals("id3row1column2", resultSet.getString(2));
        assertTrue(resultSet.next());
        assertEquals("id3row2column1", resultSet.getString(1));
        assertEquals("id3row2column2", resultSet.getString(2));
        assertTrue(resultSet.next());
        assertEquals("id3row3column1", resultSet.getString(1));
        assertEquals("id3row3column2", resultSet.getString(2));
        assertFalse(resultSet.next());
        assertFalse(resultSet.next());
    }
    
    public void testIllegalMethods() throws Exception
    {
        PolyResultSet resultSet = new PolyResultSet(getTestResultSetList());
        resultSet.next();
        try
        {
            resultSet.updateLong(1, 123);
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw exception
        }
        try
        {
            resultSet.first();
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw exception
        }
        try
        {
            resultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw exception
        }
        try
        {
            resultSet.updateString(2, "abc");
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw exception
        }
        try
        {
            resultSet.refreshRow();
            fail();
        } 
        catch(SQLException exc)
        {
            //should throw exception
        }
    }
}
