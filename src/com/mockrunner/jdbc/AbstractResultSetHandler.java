package com.mockrunner.jdbc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.util.SearchUtil;

/**
 * Abstract base class for all <code>ResultSet</code> handlers.
 * Used to coordinate <code>ResultSet</code> objects for a
 * statement. You can use this class to prepare <code>ResultSet</code>
 * objects and update count values that are returned by the
 * <code>execute</code> method of a statement, if the current
 * SQL string matches.
 * Furthermore it can be used to create <code>ResultSet</code> objects.
 */
public abstract class AbstractResultSetHandler
{
    private boolean caseSensitive = false;
    private boolean exactMatch = false;
    private MockResultSet globalResultSet;
    private Map resultSetsForStatement = new HashMap();
    private int globalUpdateCount = 0;
    private Map updateCountForStatement = new HashMap();
    private Map returnsResultSetMap = new HashMap();
    private Set throwsSQLException = new HashSet();
    private Set executedStatements = new HashSet();
    
    /**
     * Returns a new <code>ResultSet</code>.
     * @return the new <code>ResultSet</code>
     */
    public MockResultSet createResultSet()
    {
        return new MockResultSet();
    }
    
    /**
     * Returns a new <code>ResultSet</code> created by
     * the specified factory. Currently there's only
     * the {@link FileResultSetFactory} to create <code>ResultSet</code>
     * objects based on CSV files but you can implement your own factories.
     * @return the new <code>ResultSet</code>
     */
    public MockResultSet createResultSet(ResultSetFactory factory)
    {
        return factory.create();
    }
    
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
     * the string <i>insert into mytable</i> will match this statement.
     * Usually <code>false</code> is the best choice, so
     * prepared <code>ResultSet</code> objects do not have
     * to match exactly the current statements SQL string.
     * @param exactMatch enable or disable exact matching
     */
    public void setExactMatch(boolean exactMatch)
    {
        this.exactMatch = exactMatch;
    }
    
    /**
     * Collects all SQL strings that were executed.
     * @param sql the SQL string
     */
    public void addExecutedStatement(String sql)
    {
        executedStatements.add(sql);
    }
    
    /**
     * Returns the <code>Collection</code> of all executed SQL strings.
     * @param the <code>Collection</code> of executed SQL strings
     */
    public Collection getExecutedStatements()
    {
        return Collections.unmodifiableCollection(executedStatements);
    }
    
    /**
     * Clears the <code>ResultSet</code> objects.
     */
    public void clearResultSets()
    {
        resultSetsForStatement.clear();
    }
    
    /**
     * Clears the update counts.
     */
    public void clearUpdateCounts()
    {
        updateCountForStatement.clear();
    }
    
    /**
     * Clears the definitions if statements return
     * <code>ResultSet</code> objects or update counts.
     */
    public void clearReturnsResultSet()
    {
        returnsResultSetMap.clear();
    }
    
    /**
     * Clears the list of statements that should throw an exception.
     */
    public void clearThrowsSQLException()
    {
        throwsSQLException.clear();
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
     * Returns the <code>Map</code> of all update counts, that were added 
     * with {@link #prepareUpdateCount(String, int)}.
     * The SQL strings map to the corresponding update count as
     * <code>Integer</code> object.
     * @return the <code>Map</code> of <code>ResultSet</code> objects
     */
    public Map getUpdateCountMap()
    {
        return Collections.unmodifiableMap(updateCountForStatement);
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
        List list = SearchUtil.getMatchingObjects(resultSetsForStatement, sql, getCaseSensitive(), getExactMatch(), true);
        if(null != list && list.size() > 0)
        {
            return (MockResultSet)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns the global <code>ResultSet</code>.
     * The statement returns the global <code>ResultSet</code>
     * if no <code>ResultSet</code> can be found for the current
     * SQL string.
     * @return the global {@link MockResultSet}
     */
    public MockResultSet getGlobalResultSet()
    {
        return globalResultSet;
    }
    
    /**
     * Returns the first update count that matches the
     * specified SQL string. Please note that you can modify
     * the search parameters with {@link #setCaseSensitive} and 
     * {@link #setExactMatch}.
     * @param sql the SQL string
     * @return the corresponding update count
     */
    public Integer getUpdateCount(String sql)
    {
        List list = SearchUtil.getMatchingObjects(updateCountForStatement, sql, getCaseSensitive(), getExactMatch(), true);
        if(null != list && list.size() > 0)
        {
            return (Integer)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns the global update count for <code>executeUpdate</code>
     * calls.
     * The statement returns the global update count
     * if no update count can be found for the current
     * SQL string.
     * @return the global update count
     */
    public int getGlobalUpdateCount()
    {
        return globalUpdateCount;
    }
    
    /**
     * Returns if the specified SQL string is a select that returns
     * a <code>ResultSet</code>.
     * Usually you do not have to specify this.
     * It is assumed that an SQL string returns a <code>ResultSet</code> 
     * if it contains <i>SELECT</i>.
     * @param sql the SQL string
     * @return <code>true</code> if the SQL string returns a <code>ResultSet</code>
     */
    public Boolean getReturnsResultSet(String sql)
    {
        List list = SearchUtil.getMatchingObjects(returnsResultSetMap, sql, getCaseSensitive(), getExactMatch(), true);
        if(null != list && list.size() > 0)
        {
            return (Boolean)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns if the specified SQL string should raise an exception.
     * This can be used to simulate database exceptions.
     * @param sql the SQL string
     * @return <code>true</code> if the specified SQL string should raise an exception,
     *         <code>false</code> otherwise
     */
    public boolean getThrowsSQLException(String sql)
    {
        if(SearchUtil.contains(throwsSQLException, sql, getCaseSensitive(), getExactMatch(), true)) return true;
        return false;
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
     * Prepare the global <code>ResultSet</code>.
     * The statement returns the global <code>ResultSet</code>
     * if no <code>ResultSet</code> can be found for the current
     * SQL string.
     * @param resultSet the {@link MockResultSet}
     */
    public void prepareGlobalResultSet(MockResultSet resultSet)
    {
        this.globalResultSet = resultSet;
    }
    
    /**
     * Prepare the update count for <code>executeUpdate</code> calls 
     * for a specified SQL string.
     * @param sql the SQL string
     * @param updateCount the update count
     */
    public void prepareUpdateCount(String sql, int updateCount)
    {
        updateCountForStatement.put(sql, new Integer(updateCount));
    }
    
    /**
     * Prepare the global update count for <code>executeUpdate</code> calls.
     * The statement returns the global update count
     * if no update count can be found for the current
     * SQL string.
     * @param updateCount the update count
     */
    public void prepareGlobalUpdateCount(int updateCount)
    {
        this.globalUpdateCount = updateCount;
    }
    
    /**
     * Prepare if the specified SQL string is a select that returns
     * a <code>ResultSet</code>. Usually you do not have to specify this.
     * It is assumed that an SQL string returns a <code>ResultSet</code> 
     * if it contains <i>SELECT</i>.
     * @param sql the SQL string
     * @param returnsResultSet specify if the SQL string returns a <code>ResultSet</code>
     */
    public void prepareReturnsResultSet(String sql, boolean returnsResultSet)
    {
        returnsResultSetMap.put(sql, new Boolean(returnsResultSet));
    }
    
    /**
     * Prepare if the specified SQL string should raise an exception.
     * This can be used to simulate database exceptions.
     * @param sql the SQL string
     */
    public void prepareThrowsSQLException(String sql)
    {
        throwsSQLException.add(sql);
    }
    
    /**
     * Clears the global <code>ResultSet</code>.
     */
    public void clearGlobalResultSet()
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
