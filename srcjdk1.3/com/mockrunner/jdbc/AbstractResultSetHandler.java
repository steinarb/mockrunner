package com.mockrunner.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Abstract base class for all <code>ResultSet</code> handlers.
 * Used to coordinate <code>ResultSet</code> objects for a
 * statement. You can use this class to prepare <code>ResultSet</code>
 * objects and update count values that are returned by the
 * <code>execute</code> method of a statement, if the current
 * SQL string matches.
 * Furthermore it can be used to create <code>ResultSet</code> objects.
 * Please note that the <code>ResultSet</code> objects you create and
 * prepare with this handler are cloned when executing statements.
 * So you cannot rely on object identity. You have to use the id
 * of the <code>ResultSet</code> to identify it.
 * The <code>ResultSet</code> objects returned by {@link #getReturnedResultSets}
 * are actually the instances the executed statements returned.
 */
public abstract class AbstractResultSetHandler
{
    private boolean caseSensitive = false;
    private boolean exactMatch = false;
    private boolean useRegularExpressions = false;
    private MockResultSet globalResultSet;
    private Map resultSetsForStatement = new HashMap();
    private int globalUpdateCount = 0;
    private Map updateCountForStatement = new HashMap();
    private MockResultSet globalGeneratedKeys;
    private Map generatedKeysForStatement = new HashMap();
    private Map returnsResultSetMap = new HashMap();
    private Map throwsSQLException = new HashMap();
    private List executedStatements = new ArrayList();
    private List returnedResultSets = new ArrayList();
    
    /**
     * Creates a new <code>ResultSet</code> with a
     * random id.
     * @return the new <code>ResultSet</code>
     */
    public MockResultSet createResultSet()
    {
        return new MockResultSet(String.valueOf(Math.random()));
    }
    
    /**
     * Creates a new <code>ResultSet</code> with the specified id.
     * @param id the id
     * @return the new <code>ResultSet</code>
     */
    public MockResultSet createResultSet(String id)
    {
        return new MockResultSet(id);
    }
    
    /**
     * Returns a new <code>ResultSet</code> created by the specified factory.
     * Creates a random id.
     * @param factory the {@link ResultSetFactory}
     * @return the new <code>ResultSet</code>
     */
    public MockResultSet createResultSet(ResultSetFactory factory)
    {
        return factory.create(String.valueOf(Math.random()));
    }
    
    /**
     * Returns a new <code>ResultSet</code> created by the specified factory.
     * @param id the id
     * @param factory the {@link ResultSetFactory}
     * @return the new <code>ResultSet</code>
     */
    public MockResultSet createResultSet(String id, ResultSetFactory factory)
    {
        return factory.create(id);
    }
    
    /**
     * Set if specified SQL strings should be handled case sensitive.
     * Defaults to to <code>false</code>, i.e. <i>INSERT</i> is the same
     * as <i>insert</i>.
     * Please note that this method controls SQL statement
     * matching for prepared results and update counts, i.e. what
     * statements the tested application has to execute to receive
     * a specified result. Unlike {@link JDBCTestModule#setCaseSensitive(boolean)}
     * it does not control the statement matching of {@link JDBCTestModule}
     * methods.
     * @param caseSensitive enable or disable case sensitivity
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }

    /**
     * Set if specified SQL statements must match exactly.
     * Defaults to <code>false</code>, i.e. the SQL string
     * does not need to match exactly. If the original statement 
     * is <i>insert into mytable values(?, ?, ?)</i>
     * the string <i>insert into mytable</i> will match this statement.
     * Usually <code>false</code> is the best choice, so
     * prepared <code>ResultSet</code> objects do not have
     * to match exactly the current statements SQL string.
     * The current SQL string just has to contain the SQL string
     * for the prepared prepared <code>ResultSet</code>.
     * Please note that this method controls SQL statement
     * matching for prepared results and update counts, i.e. what
     * statements the tested application has to execute to receive
     * a specified result. Unlike {@link JDBCTestModule#setExactMatch(boolean)}
     * it does not control the statement matching of {@link JDBCTestModule}
     * methods.
     * @param exactMatch enable or disable exact matching
     */
    public void setExactMatch(boolean exactMatch)
    {
        this.exactMatch = exactMatch;
    }
    
    /**
     * Set if regular expressions should be used when matching
     * SQL statements. Irrelevant if <code>exactMatch</code> is
     * <code>true</code>. Default is <code>false</code>, i.e. you
     * cannot use regular expressions and matching is based
     * on string comparison.
     * Please note that this method controls SQL statement
     * matching for prepared results and update counts, i.e. what
     * statements the tested application has to execute to receive
     * a specified result. Unlike {@link JDBCTestModule#setUseRegularExpressions(boolean)}
     * it does not control the statement matching of {@link JDBCTestModule}
     * methods.
     * @param useRegularExpressions should regular expressions be used
     */
    public void setUseRegularExpressions(boolean useRegularExpressions)
    {
        this.useRegularExpressions = useRegularExpressions;
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
     * Collects all <code>ResultSet</code> objects that were returned by
     * a <code>Statement</code>, <code>PreparedStatement</code> or
     * <code>CallableStatement</code>.
     * @param resultSet the <code>ResultSet</code>
     */
    public void addReturnedResultSet(MockResultSet resultSet)
    {
        if(null == resultSet) return;
        returnedResultSets.add(resultSet);
    }
    
    /**
     * Returns the <code>List</code> of all executed SQL strings.
     * @return the <code>List</code> of executed SQL strings
     */
    public List getExecutedStatements()
    {
        return Collections.unmodifiableList(executedStatements);
    }
    
    /**
     * Returns the <code>List</code> of all returned <code>ResultSet</code> objects.
     * @return the <code>List</code> of returned <code>ResultSet</code> objects
     */
    public List getReturnedResultSets()
    {
        return Collections.unmodifiableList(returnedResultSets);
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
     * Clears the list of statements that return generated keys.
     */
    public void clearGeneratedKeys()
    {
        generatedKeysForStatement.clear();
    }
    
    /**
     * Clears the global <code>ResultSet</code>.
     */
    public void clearGlobalResultSet()
    {
        this.globalResultSet = null;
    }
    
    /**
     * Clears the global generated keys <code>ResultSet</code>.
     */
    public void clearGlobalGeneratedKeys()
    {
        this.globalGeneratedKeys = null;
    }
    
    /**
     * Clears the global update count.
     */
    public void clearGlobalUpdateCount()
    {
        this.globalUpdateCount = 0;
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
     * Returns the <code>Map</code> of all generated keys <code>ResultSet</code>
     * objects, that were added with {@link #prepareGeneratedKeys(String, MockResultSet)}.
     * The SQL strings map to the corresponding generated keys <code>ResultSet</code>.
     * @return the <code>Map</code> of generated keys <code>ResultSet</code> objects
     */
    public Map getGeneratedKeysMap()
    {
        return Collections.unmodifiableMap(generatedKeysForStatement);
    }
    
    /**
     * Returns the first <code>ResultSet</code> that matches the
     * specified SQL string. Please note that you can modify
     * the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return the corresponding {@link MockResultSet}
     */
    public MockResultSet getResultSet(String sql)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List list = matcher.getMatchingObjects(resultSetsForStatement, sql, true, true);
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
     * Returns the first update count that matches the
     * specified SQL string. Please note that you can modify
     * the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return the corresponding update count
     */
    public Integer getUpdateCount(String sql)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List list = matcher.getMatchingObjects(updateCountForStatement, sql, true, true);
        if(null != list && list.size() > 0)
        {
            return (Integer)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns the global update count for <code>executeUpdate</code> calls.
     * @return the global update count
     */
    public int getGlobalUpdateCount()
    {
        return globalUpdateCount;
    }
    
    /**
     * Returns the first generated keys <code>ResultSet</code> that 
     * matches the specified SQL string. Please note that you can modify
     * the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return the corresponding generated keys {@link MockResultSet}
     */
    public MockResultSet getGeneratedKeys(String sql)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List list = matcher.getMatchingObjects(generatedKeysForStatement, sql, true, true);
        if(null != list && list.size() > 0)
        {
            return (MockResultSet)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns the global generated keys <code>ResultSet</code>.
     * @return the global generated keys {@link MockResultSet}
     */
    public MockResultSet getGlobalGeneratedKeys()
    {
        return globalGeneratedKeys;
    }
    
    /**
     * Returns if the specified SQL string is a <i>select</i> that returns
     * a <code>ResultSet</code>.
     * Usually you do not have to specify this.
     * It is assumed that an SQL string returns a <code>ResultSet</code> 
     * if it contains the string <i>select</i> (case insensitive).
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return <code>true</code> if the SQL string returns a <code>ResultSet</code>
     */
    public Boolean getReturnsResultSet(String sql)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List list = matcher.getMatchingObjects(returnsResultSetMap, sql, true, true);
        if(null != list && list.size() > 0)
        {
            return (Boolean)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns if the specified SQL string should raise an exception.
     * This can be used to simulate database exceptions.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return <code>true</code> if the specified SQL string should raise an exception,
     *         <code>false</code> otherwise
     */
    public boolean getThrowsSQLException(String sql)
    {
        return (getSQLException(sql) != null);
    }
    
    /**
     * Returns the <code>SQLException</code> the specified SQL string
     * should throw. Returns <code>null</code> if the specified SQL string
     * should not throw an exception.
     * This can be used to simulate database exceptions.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return the <code>SQLException</code> or <code>null</code>
     */
    public SQLException getSQLException(String sql)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List list = matcher.getMatchingObjects(throwsSQLException, sql, true, true);
        if(null == list || list.size() == 0) return null;
        return (SQLException)list.get(0);
    }
    
    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @param resultSet the corresponding {@link MockResultSet}
     */
    public void prepareResultSet(String sql, MockResultSet resultSet)
    {
        resultSetsForStatement.put(sql, resultSet);
    }

    /**
     * Prepare the global <code>ResultSet</code>.
     * @param resultSet the {@link MockResultSet}
     */
    public void prepareGlobalResultSet(MockResultSet resultSet)
    {
        this.globalResultSet = resultSet;
    }
    
    /**
     * Prepare the update count for <code>executeUpdate</code> calls 
     * for a specified SQL string. Please note that you can modify
     * the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @param updateCount the update count
     */
    public void prepareUpdateCount(String sql, int updateCount)
    {
        updateCountForStatement.put(sql, new Integer(updateCount));
    }
    
    /**
     * Prepare the global update count for <code>executeUpdate</code> calls.
     * @param updateCount the update count
     */
    public void prepareGlobalUpdateCount(int updateCount)
    {
        this.globalUpdateCount = updateCount;
    }
    
    /**
     * Prepare the generated keys <code>ResultSet</code> 
     * for a specified SQL string. Please note that you can modify
     * the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @param generatedKeysResult the generated keys {@link MockResultSet}
     */
    public void prepareGeneratedKeys(String sql, MockResultSet generatedKeysResult)
    {
        generatedKeysForStatement.put(sql, generatedKeysResult);
    }
    
    /**
     * Prepare the global generated keys <code>ResultSet</code>.
     * @param generatedKeysResult the generated keys {@link MockResultSet}
     */
    public void prepareGlobalGeneratedKeys(MockResultSet generatedKeysResult)
    {
        this.globalGeneratedKeys = generatedKeysResult;
    }
    
    /**
     * Prepare if the specified SQL string is a <i>select</i> that returns
     * a <code>ResultSet</code>. Usually you do not have to specify this.
     * It is assumed that an SQL string returns a <code>ResultSet</code> 
     * if it contains the string <i>select</i> (case insensitive).
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @param returnsResultSet specify if the SQL string returns a <code>ResultSet</code>
     */
    public void prepareReturnsResultSet(String sql, boolean returnsResultSet)
    {
        returnsResultSetMap.put(sql, new Boolean(returnsResultSet));
    }
    
    /**
     * Prepare that the specified SQL string should raise an exception.
     * This can be used to simulate database exceptions. This method
     * creates an <code>SQLException</code> and will throw this exception.
     * With {@link #prepareThrowsSQLException(String, SQLException)} you
     * can specify the exception.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     */
    public void prepareThrowsSQLException(String sql)
    {
        throwsSQLException.put(sql, new SQLException("Statement " + sql + " was specified to throw an exception"));
    }
    
    /**
     * Prepare that the specified SQL string should raise an exception.
     * This can be used to simulate database exceptions. This method
     * takes an exception object that will be thrown.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @param exc the <code>SQLException</code> that should be thrown
     */
    public void prepareThrowsSQLException(String sql, SQLException exc)
    {
        throwsSQLException.put(sql, exc);
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
    
    /**
     * Returns if regular expression matching is enabled
     * @return if regular expression matching is enabled
     */
    protected boolean getUseRegularExpressions()
    {
        return useRegularExpressions;
    }
}
