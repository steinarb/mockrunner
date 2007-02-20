package com.mockrunner.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.util.common.ArrayUtil;

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
    private boolean continueProcessingOnBatchFailure = false;
    private Object globalResultSets;
    private Map resultSetsForStatement = new TreeMap();
    private Object globalUpdateCounts;
    private Map updateCountForStatement = new TreeMap();
    private MockResultSet globalGeneratedKeys;
    private Map generatedKeysForStatement = new TreeMap();
    private Map returnsResultSetMap = new TreeMap();
    private Map throwsSQLException = new TreeMap();
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
     * Set if batch processing should be continued if one of the commands
     * in the batch fails. This behaviour is driver dependend. The default is
     * <code>false</code>, i.e. if a command fails with an exception,
     * batch processing will not continue and the remaining commands
     * will not be executed.
     * @param continueProcessingOnBatchFailure should batch processing be continued
     */
    public void setContinueProcessingOnBatchFailure(boolean continueProcessingOnBatchFailure)
    {
        this.continueProcessingOnBatchFailure = continueProcessingOnBatchFailure;
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
     * Collects all <code>ResultSet[]</code> objects that were returned by
     * a <code>Statement</code>, <code>PreparedStatement</code> or
     * <code>CallableStatement</code>. Called if a statement returns
     * multiple result sets.
     * @param resultSets the <code>ResultSet[]</code>
     */
    public void addReturnedResultSets(MockResultSet[] resultSets)
    {
        if(null == resultSets) return;
        returnedResultSets.add(resultSets);
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
     * Returns the <code>List</code> of all returned <code>ResultSet</code> 
     * or <code>ResultSet[]</code> objects. The <code>List</code> contains
     * arrays of result sets, if a query returned multiple result sets.
     * If a query returned multiple result sets, the list will always contain
     * the full array of <code>ResultSet</code> objects that were prepared, even
     * if {@link com.mockrunner.mock.jdbc.MockStatement#getMoreResults()} was
     * not called for all the result sets.
     * @return the <code>List</code> of returned <code>ResultSet</code> or <code>ResultSet[]</code> objects
     */
    public List getReturnedResultSets()
    {
        return Collections.unmodifiableList(returnedResultSets);
    }
    
    /**
     * Clears all prepared <code>ResultSet</code> objects.
     */
    public void clearResultSets()
    {
        resultSetsForStatement.clear();
    }
    
    /**
     * Clears all prepared update counts.
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
     * Clears the prepared global <code>ResultSet</code>.
     */
    public void clearGlobalResultSet()
    {
        this.globalResultSets = null;
    }
    
    /**
     * Clears the prepared global generated keys <code>ResultSet</code>.
     */
    public void clearGlobalGeneratedKeys()
    {
        this.globalGeneratedKeys = null;
    }
    
    /**
     * Clears the prepared global update count.
     */
    public void clearGlobalUpdateCount()
    {
        this.globalUpdateCounts = null;
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
     * specified SQL string. If the specified SQL string was
     * prepared to return multiple result sets, the first one will
     * be returned.
     * Please note that you can modify the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return the corresponding {@link MockResultSet}
     */
    public MockResultSet getResultSet(String sql)
    {
        Object resultSets = getMatchingResultSets(sql);
        if(null == resultSets) return null;
        if(resultSets instanceof MockResultSet)
        {
            return (MockResultSet)resultSets;
        }
        else if(resultSets instanceof MockResultSet[])
        {
            MockResultSet[] actualResults = (MockResultSet[])resultSets;
            if(actualResults.length > 0)
            {
                return actualResults[0];
            }
        }
        return null;
    }
    
    /**
     * Returns the first <code>ResultSet[]</code> that matches the
     * specified SQL string. If the specified SQL string was
     * prepared to return one single result set, this <code>ResultSet</code>
     * will be wrapped in an array with one element.
     * Please note that you can modify the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return the corresponding <code>MockResultSet[]</code>
     */
    public MockResultSet[] getResultSets(String sql)
    {
        Object resultSets = getMatchingResultSets(sql);
        if(null == resultSets) return null;
        if(resultSets instanceof MockResultSet)
        {
            return new MockResultSet[] {(MockResultSet)resultSets};
        }
        else if(resultSets instanceof MockResultSet[])
        {
            return (MockResultSet[])resultSets;
        }
        return null;
    }

    /**
     * Returns the if the specified SQL string returns multiple result sets.
     * Please note that you can modify the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return <code>true</code> if the query returns multiple result sets,
     *         <code>false</code> otherwise
     */
    public boolean hasMultipleResultSets(String sql)
    {
        Object resultSets = getMatchingResultSets(sql);
        return (resultSets instanceof MockResultSet[]);
    }
    
    private Object getMatchingResultSets(String sql)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List list = matcher.getMatchingObjects(resultSetsForStatement, sql, true, true);
        if(null != list && list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * Returns the global <code>ResultSet</code>. 
     * If an array of global result sets was prepared, the first one will
     * be returned.
     * @return the global {@link MockResultSet}
     */
    public MockResultSet getGlobalResultSet()
    {
        if(null == globalResultSets) return null;
        if(globalResultSets instanceof MockResultSet)
        {
            return (MockResultSet)globalResultSets;
        }
        MockResultSet[] resultSets = getGlobalResultSets();
        if(null != resultSets && resultSets.length > 0)
        {
            return resultSets[0];
        }
        return null;
    }
    
    /**
     * Returns the global <code>ResultSet[]</code>. 
     * If one single <code>ResultSet</code> was prepared, this <code>ResultSet</code>
     * will be wrapped in an array with one element.
     * @return the global <code>MockResultSet[]</code>
     */
    public MockResultSet[] getGlobalResultSets()
    {
        if(null == globalResultSets) return null;
        if(globalResultSets instanceof MockResultSet[])
        {
            return (MockResultSet[])globalResultSets;
        }
        return new MockResultSet[] {(MockResultSet)globalResultSets};
    }
    
    /**
     * Returns if multiple global result sets have been prepared, i.e. if
     * an array of global result sets was prepared.
     * @return <code>true</code> if an array of global result sets was prepared,
     *         <code>false</code> otherwise
     */
    public boolean hasMultipleGlobalResultSets()
    {
        return (globalResultSets instanceof MockResultSet[]);
    }
    
    /**
     * Returns the first update count that matches the
     * specified SQL string. If the specified SQL string was
     * prepared to return multiple update counts, the first one will
     * be returned.
     * Please note that you can modify the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return the corresponding update count
     */
    public Integer getUpdateCount(String sql)
    {
        Object updateCounts = getMatchingUpdateCounts(sql);
        if(null == updateCounts) return null;
        if(updateCounts instanceof Integer)
        {
            return (Integer)updateCounts;
        }
        else if(updateCounts instanceof Integer[])
        {
            Integer[] actualUpdateCounts = (Integer[])updateCounts;
            if(actualUpdateCounts.length > 0)
            {
                return actualUpdateCounts[0];
            }
        }
        return null;
    }
    
    /**
     * Returns the first update count array that matches the
     * specified SQL string. If the specified SQL string was
     * prepared to return one update count, this value
     * will be wrapped in an array with one element.
     * Please note that you can modify the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return the corresponding update count array
     */
    public Integer[] getUpdateCounts(String sql)
    {
        Object updateCounts = getMatchingUpdateCounts(sql);
        if(null == updateCounts) return null;
        if(updateCounts instanceof Integer)
        {
            return new Integer[] {(Integer)updateCounts};
        }
        else if(updateCounts instanceof Integer[])
        {
            return (Integer[])updateCounts;
        }
        return null;
    }
    
    /**
     * Returns the if the specified SQL string returns multiple update counts.
     * Please note that you can modify the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return <code>true</code> if the SQL string returns multiple update counts,
     *         <code>false</code> otherwise
     */
    public boolean hasMultipleUpdateCounts(String sql)
    {
        Object updateCounts = getMatchingUpdateCounts(sql);
        return (updateCounts instanceof Integer[]);
    }
    
    private Object getMatchingUpdateCounts(String sql)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List list = matcher.getMatchingObjects(updateCountForStatement, sql, true, true);
        if(null != list && list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * Returns the global update count for <code>executeUpdate</code> calls.
     * If an array of global update counts was prepared, the first one will
     * be returned.
     * @return the global update count
     */
    public int getGlobalUpdateCount()
    {
        if(null == globalUpdateCounts) return 0;
        if(globalUpdateCounts instanceof Integer)
        {
            return ((Integer)globalUpdateCounts).intValue();
        }
        int[] updateCounts = getGlobalUpdateCounts();
        if(null != updateCounts && updateCounts.length > 0)
        {
            return updateCounts[0];
        }
        return 0;
    }
    
    /**
     * Returns the array of global update counts.
     * If one single update count value was prepared, this value
     * will be wrapped in an array with one element.
     * @return the array of global update counts
     */
    public int[] getGlobalUpdateCounts()
    {
        if(null == globalUpdateCounts) return null;
        if(globalUpdateCounts instanceof int[])
        {
            return (int[])globalUpdateCounts;
        }
        return new int[] {((Integer)globalUpdateCounts).intValue()};
    }
    
    /**
     * Returns if multiple global update counts have been prepared, i.e. if
     * an array of global update counts was prepared.
     * @return <code>true</code> if an array of global update counts was prepared,
     *         <code>false</code> otherwise
     */
    public boolean hasMultipleGlobalUpdateCounts()
    {
        return (globalUpdateCounts instanceof int[]);
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
     * Prepare an array of <code>ResultSet</code> objects for a specified SQL string.
     * This method can be used for queries that return multiple result sets.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @param resultSets the corresponding <code>MockResultSet[]</code>
     */
    public void prepareResultSets(String sql, MockResultSet[] resultSets)
    {
        resultSetsForStatement.put(sql, resultSets.clone());
    }

    /**
     * Prepare the global <code>ResultSet</code>.
     * @param resultSet the {@link MockResultSet}
     */
    public void prepareGlobalResultSet(MockResultSet resultSet)
    {
        this.globalResultSets = resultSet;
    }
    
    /**
     * Prepare an array of global <code>ResultSet</code> objects.
     * @param resultSets the corresponding <code>MockResultSet[]</code>
     */
    public void prepareGlobalResultSets(MockResultSet[] resultSets)
    {
        this.globalResultSets = (MockResultSet[])resultSets.clone();
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
     * Prepare an array update count values for <code>executeUpdate</code> calls 
     * for a specified SQL string. This method can be used if multiple update counts
     * are returned.
     * Please note that you can modify the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @param updateCounts the update count array
     */
    public void prepareUpdateCounts(String sql, int[] updateCounts)
    {
        updateCountForStatement.put(sql, ArrayUtil.convertToObjectArray(updateCounts));
    }
    
    /**
     * Prepare the global update count for <code>executeUpdate</code> calls.
     * @param updateCount the update count
     */
    public void prepareGlobalUpdateCount(int updateCount)
    {
        this.globalUpdateCounts = new Integer(updateCount);
    }
    
    /**
     * Prepare an array of global update count values for <code>executeUpdate</code> calls.
     * @param updateCounts the update count array
     */
    public void prepareGlobalUpdateCounts(int[] updateCounts)
    {
        this.globalUpdateCounts = (int[])updateCounts.clone();
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

    /**
     * Returns if batch processing should be continued if one of 
     * the commands in the batch fails.
     * @return if batch processing should be continued
     */
    public boolean getContinueProcessingOnBatchFailure()
    {
        return continueProcessingOnBatchFailure;
    }
}
