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
    private MockResultSet globalResultSet;
    private Map resultSetsForStatement = new HashMap();
    
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
     * Clears the <code>ResultSet</code> objects.
     */
    public void clearResultSets()
    {
        resultSetsForStatement.clear();
    }
    
    /**
     * Returns the <code>Map</code> of all <code>ResultSet</code>
     * objects, that were added with {@link #prepareResultSet(String, MockResultSet)}.
     * The SQL strings map to the corresponding <code>ResultSet</code>.
     * @return the <code>Map</code> of <code>ResultSet</code> objects
     */
    public Map getResultSetMap()
    {
        return Collections.unmodifiableMap(resultSetsForStatement);
    }
    
    /**
     * Returns the first <code>ResultSet</code> that matches the
     * specified SQL string. Please note that you can modify
     * the search parameters with {@link #setCaseSensitive} and 
     * {@link #setExactMatch}.
     * @param sql the SQL string
     * @return the corresponding {@link MockResultSet}
     */
    public MockResultSet getResultSet(String sql)
    {
        List list = SearchUtil.getMatchingObjects(resultSetsForStatement, sql, getCaseSensitive(), getExactMatch());
        if(null != list && list.size() > 0)
        {
            return (MockResultSet)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns the global <code>ResultSet</code>.
     * @return the global {@link MockResultSet}
     */
    public MockResultSet getGlobalResultSet()
    {
        return globalResultSet;
    }
    
    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string.
     * @param sql the SQL string
     * @param resultSet the corresponding {@link MockResultSet}
     */
    public void prepareResultSet(String sql, MockResultSet resultSet)
    {
        resultSetsForStatement.put(sql, resultSet);
    }

    /**
     * Prepare a global <code>ResultSet</code>.
     * @param resultSet the {@link MockResultSet}
     */
    public void prepareGlobalResultSet(MockResultSet resultSet)
    {
        this.globalResultSet = resultSet;
    }
    
    /**
     * Clears the global <code>ResultSet</code>.
     */
    public void clearGloablResultSet()
    {
        this.globalResultSet = null;
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
