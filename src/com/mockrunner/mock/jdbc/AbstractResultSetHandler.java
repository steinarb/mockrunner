package com.mockrunner.mock.jdbc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.util.SearchUtil;

/**
 * Abstract base class for all <code>ResultSet</code> handlers.
 * Used to coordinate <code>ResultSet</code> objects for a
 * statements.
 */
public abstract class AbstractResultSetHandler
{
    private boolean caseSensitive = false;
    private boolean exactMatch = false;
    private MockResultSet resultSet;
    private Map resultSetsForStatementIndexMap = new HashMap();
    private Map resultSetsForStatementSQLMap = new HashMap();
    
    /**
     * Set if specified SQL strings should be handled case sensitive.
     * Defaults to to <code>false</code>, i.e. <i>INSERT</i> is the same
     * as <i>insert</i>.
     * @param caseSensitive enable or disable case sensitivity
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }

    /**
     * Set if specified SQL statements must match exactly.
     * Defaults to <code>false</code>, i.e. any SQL string
     * does not need to match exactly. If the original statement 
     * is <i>insert into mytable values(?, ?, ?)</i>
     * <code>getResultSet("insert into mytable")</code>
     * will match this statement.
     * @param exactMatch enable or disable exact matching
     */
    public void setExactMatch(boolean exactMatch)
    {
        this.exactMatch = exactMatch;
    }
    
    /**
     * Clears all specified <code>ResultSet</code> objects.
     */
    public void clearResultSets()
    {
        clearIndexedResultSets();
        clearSQLMappedResultSets();
        resultSet = null;
    }
    
    /**
     * Clears all specified <code>ResultSet</code> objects
     * that were added with {@link #prepareResultSet(int, MockResultSet)}.
     */
    public void clearIndexedResultSets()
    {
        resultSetsForStatementIndexMap.clear();
    }
    
    /**
     * Clears all specified <code>ResultSet</code> objects
     * that were added with {@link #prepareResultSet(String, MockResultSet)}.
     */
    public void clearSQLMappedResultSets()
    {
        resultSetsForStatementIndexMap.clear();
    }
    
    /**
     * Returns the <code>Map</code> of all <code>ResultSet</code>
     * objects, that were added with {@link #prepareResultSet(String, MockResultSet)}.
     * The SQL strings map to the corresponding <code>ResultSet</code>.
     * @return the <code>Map</code> of <code>ResultSet</code> objects
     */
    public Map getSQLMappedResultSets()
    {
        return Collections.unmodifiableMap(resultSetsForStatementSQLMap);
    }
    
    /**
     * Returns the first <code>ResultSet</code> that matches the
     * specified SQL string.
     * @param sql the SQL string
     * @return the corresponding {@link MockResultSet}
     */
    public MockResultSet getResultSet(String sql)
    {
        List list = SearchUtil.getMatchingObjects(resultSetsForStatementSQLMap, sql, getCaseSensitive(), getExactMatch());
        if(null != list && list.size() > 0)
        {
            return (MockResultSet)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns the <code>ResultSet</code> for the specified index.
     * @param index the index
     * @return the corresponding {@link MockResultSet}
     */
    public MockResultSet getResultSet(int index)
    {
        return (MockResultSet)resultSetsForStatementIndexMap.get(new Integer(index));
    }
    
    /**
     * Returns the global <code>ResultSet</code>.
     * @return the global {@link MockResultSet}
     */
    public MockResultSet getGlobalResultSet()
    {
        return resultSet;
    }
    
    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string.
     * @param sql the SQL string
     * @param resultSet the corresponding {@link MockResultSet}
     */
    public void prepareResultSet(String sql, MockResultSet resultSet)
    {
        resultSetsForStatementSQLMap.put(sql, resultSet);
    }

    /**
     * Prepare a <code>ResultSet</code> for a specified index.
     * @param index the index of the statement
     * @param resultSet the corresponding {@link MockResultSet}
     */
    public void prepareResultSet(int index, MockResultSet resultSet)
    {
        resultSetsForStatementIndexMap.put(new Integer(index), resultSet);
    }
    
    /**
     * Prepare a global <code>ResultSet</code>.
     * @param resultSet the {@link MockResultSet}
     */
    public void prepareGloablResultSet(MockResultSet resultSet)
    {
        this.resultSet = resultSet;
    }
    
    /**
     * Returns if specified SQL strings should be handled case sensitive.
     * @return is case sensitivity enabled or disabled
     */
    protected boolean getCaseSensitive()
    {
        return caseSensitive;
    }
    
    /**
     * Returns if specified SQL statements must match exactly.
     * @return is exact matching enabled or disabled
     */
    protected boolean getExactMatch()
    {
        return exactMatch;
    }
}
