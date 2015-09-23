package com.mockrunner.jdbc;

import com.mockrunner.mock.jdbc.MockParameterMap;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    private boolean continueProcessingOnBatchFailure = false;
    private MockResultSet[] globalResultSets;
    private Integer[] globalUpdateCounts;
    private final Map<String, Boolean> returnsResultSetMap = new TreeMap<String, Boolean>();
    private MockResultSet globalGeneratedKeys;
    private final List<String> executedStatements = new ArrayList<String>();
    private final List<MockResultSet[]> returnedResultSets = new ArrayList<MockResultSet[]>();
    
    private final Map<String, List<ParameterWrapper<MockResultSet[]>>> resultSetsForStatement = new TreeMap<String, List<ParameterWrapper<MockResultSet[]>>>();
    private final Map<String, List<ParameterWrapper<Integer[]>>> updateCountForStatement = new TreeMap<String, List<ParameterWrapper<Integer[]>>>();
    private final Map<String, List<ParameterWrapper<SQLException>>> throwsSQLException = new TreeMap<String, List<ParameterWrapper<SQLException>>>();
    private final Map<String, List<ParameterWrapper<MockResultSet>>> generatedKeysForStatement = new TreeMap<String, List<ParameterWrapper<MockResultSet>>>();
	
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
        returnedResultSets.add(new MockResultSet[]{resultSet});
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
    public List<String> getExecutedStatements()
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
    public List<MockResultSet[]> getReturnedResultSets()
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
    
//    /**
//     * Returns the <code>Map</code> of all <code>ResultSet</code>
//     * objects, that were added with {@link #prepareResultSet(String, MockResultSet)}.
//     * The SQL strings map to the corresponding <code>ResultSet</code>.
//     * @return the <code>Map</code> of <code>ResultSet</code> objects
//     */
//    public Map<String, MockResultSet[]> getResultSetMap()
//    {
//        return Collections.unmodifiableMap(resultSetsForStatement);
//    }
//    
//    /**
//     * Returns the <code>Map</code> of all update counts, that were added 
//     * with {@link #prepareUpdateCount(String, int)}.
//     * The SQL strings map to the corresponding update count as
//     * <code>Integer</code> object.
//     * @return the <code>Map</code> of <code>ResultSet</code> objects
//     */
//    public Map<String, Integer[]> getUpdateCountMap()
//    {
//        return Collections.unmodifiableMap(updateCountForStatement);
//    }
//    
//    /**
//     * Returns the <code>Map</code> of all generated keys <code>ResultSet</code>
//     * objects, that were added with {@link #prepareGeneratedKeys(String, MockResultSet)}.
//     * The SQL strings map to the corresponding generated keys <code>ResultSet</code>.
//     * @return the <code>Map</code> of generated keys <code>ResultSet</code> objects
//     */
//    public Map<String, MockResultSet> getGeneratedKeysMap()
//    {
//        return Collections.unmodifiableMap(generatedKeysForStatement);
//    }
    
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
        return getResultSet(sql, new MockParameterMap(), true);
    }
    
    protected MockResultSet getResultSet(String sql, MockParameterMap parameters, boolean exactMatchParameter)
    {
        MockResultSet[] resultSets = getResultSets(sql, parameters, exactMatchParameter);
        if(null != resultSets && resultSets.length > 0)
        {
            return resultSets[0];
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
        return getResultSets(sql, new MockParameterMap(), true);
    }

    protected MockResultSet[] getResultSets(String sql, MockParameterMap parameters, boolean exactMatchParameter)
    {
        ParameterWrapper<MockResultSet[]> wrapper = getMatchingParameterWrapper(sql, parameters, resultSetsForStatement, exactMatchParameter);

        if(null != wrapper){
            return wrapper.getWrappedObject();
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
        return hasMultipleResultSets(sql, new MockParameterMap(), true);
    }
    
    protected boolean hasMultipleResultSets(String sql, MockParameterMap parameters, boolean exactMatchParameter)
    {
        ParameterWrapper<MockResultSet[]> wrapper = getMatchingParameterWrapper(sql, parameters, resultSetsForStatement, exactMatchParameter);
        return (null != wrapper && wrapper.getWrappedObject().length > 1);
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
        return globalResultSets;
    }
    
    /**
     * Returns if multiple global result sets have been prepared, i.e. if
     * an array of global result sets was prepared.
     * @return <code>true</code> if an array of global result sets was prepared,
     *         <code>false</code> otherwise
     */
    public boolean hasMultipleGlobalResultSets()
    {
        return (globalResultSets!=null && globalResultSets.length > 1);
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
        return getUpdateCount(sql, new MockParameterMap(), true);
    }
    
    protected Integer getUpdateCount(String sql, MockParameterMap parameters, boolean exactMatchParameter)
    {
        Integer[] updateCounts = getUpdateCounts(sql, parameters, exactMatchParameter);
        if(null != updateCounts && updateCounts.length > 0)
        {
            return updateCounts[0];
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
        return getUpdateCounts(sql, new MockParameterMap(), true);
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
        return hasMultipleUpdateCounts(sql, new MockParameterMap(), true);
    }

    protected boolean hasMultipleUpdateCounts(String sql, MockParameterMap parameters, boolean exactMatchParameter)
    {
        ParameterWrapper<Integer[]> wrapper = getMatchingParameterWrapper(sql, parameters, updateCountForStatement, exactMatchParameter);
        return (wrapper != null && wrapper.getWrappedObject().length > 1);
    }    
    
    public Integer[] getUpdateCounts(String sql, MockParameterMap parameters, boolean exactMatchParameter)
    {
        ParameterWrapper<Integer[]> wrapper = getMatchingParameterWrapper(sql, parameters, updateCountForStatement, exactMatchParameter);
        if(null != wrapper)
        {
            return wrapper.getWrappedObject();
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
        if(null != globalUpdateCounts && globalUpdateCounts.length > 0)
        {
            return globalUpdateCounts[0];
        }
        return 0;
    }
    
    /**
     * Returns the array of global update counts.
     * If one single update count value was prepared, this value
     * will be wrapped in an array with one element.
     * @return the array of global update counts
     */
    public Integer[] getGlobalUpdateCounts()
    {
        if(null == globalUpdateCounts) return null;
        return globalUpdateCounts;
    }
    
    /**
     * Returns if multiple global update counts have been prepared, i.e. if
     * an array of global update counts was prepared.
     * @return <code>true</code> if an array of global update counts was prepared,
     *         <code>false</code> otherwise
     */
    public boolean hasMultipleGlobalUpdateCounts()
    {
        return (null != globalUpdateCounts && globalUpdateCounts.length > 1);
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
        return getGeneratedKeys(sql, new MockParameterMap(), true);
    }

    protected MockResultSet getGeneratedKeys(String sql, MockParameterMap parameters, boolean exactMatchParameter)
    {
        ParameterWrapper<MockResultSet> wrapper = getMatchingParameterWrapper(sql, parameters, generatedKeysForStatement, exactMatchParameter);
        if(null != wrapper)
        {
            return wrapper.getWrappedObject();
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
        SQLStatementMatcher<Boolean> matcher = new SQLStatementMatcher<Boolean>(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List<Boolean> list = matcher.getMatchingObjects(returnsResultSetMap, sql, true);
        if(null == list || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
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
        return getSQLException(sql, new MockParameterMap(), true);
    }

    protected SQLException getSQLException(String sql, MockParameterMap parameters, boolean exactMatchParameter)
    {
        ParameterWrapper<SQLException> wrapper = getMatchingParameterWrapper(sql, parameters, throwsSQLException, exactMatchParameter);
        if(null != wrapper)
        {
            return wrapper.getWrappedObject();
        }
        return null;
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
        prepareResultSet(sql, resultSet, new MockParameterMap());
    }

    protected void prepareResultSet(String sql, MockResultSet resultSet, MockParameterMap parameters)
    {
        List<ParameterWrapper<MockResultSet[]>> list = getListFromMapForSQLStatement(sql, resultSetsForStatement);
        list.add(new ParameterWrapper<MockResultSet[]>(new MockResultSet[]{resultSet}, new MockParameterMap(parameters)));
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
        prepareResultSets(sql, resultSets, new MockParameterMap());
    }

    protected void prepareResultSets(String sql, MockResultSet[] resultSets, MockParameterMap parameters)
    {
        List<ParameterWrapper<MockResultSet[]>> list = getListFromMapForSQLStatement(sql, resultSetsForStatement);
        list.add(new ParameterWrapper<MockResultSet[]>(resultSets.clone(), new MockParameterMap(parameters)));
    }    
    
    /**
     * Prepare the global <code>ResultSet</code>.
     * @param resultSet the {@link MockResultSet}
     */
    public void prepareGlobalResultSet(MockResultSet resultSet)
    {
        this.globalResultSets = new MockResultSet[]{resultSet};
    }
    
    /**
     * Prepare an array of global <code>ResultSet</code> objects.
     * @param resultSets the corresponding <code>MockResultSet[]</code>
     */
    public void prepareGlobalResultSets(MockResultSet[] resultSets)
    {
        this.globalResultSets = resultSets.clone();
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
        prepareUpdateCount(sql, updateCount, new MockParameterMap());
    }
    
    protected void prepareUpdateCount(String sql, int updateCount, MockParameterMap parameters)
    {
        List<ParameterWrapper<Integer[]>> list = getListFromMapForSQLStatement(sql, updateCountForStatement);
        list.add(new ParameterWrapper<Integer[]>(new Integer[]{updateCount}, new MockParameterMap(parameters)));
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
    public void prepareUpdateCounts(String sql, Integer[] updateCounts)
    {
       prepareUpdateCounts(sql, updateCounts, new MockParameterMap());
    }
    
    public void prepareUpdateCounts(String sql, Integer[] updateCounts, MockParameterMap parameters)
    {
        List<ParameterWrapper<Integer[]>> list = getListFromMapForSQLStatement(sql, updateCountForStatement);
        list.add(new ParameterWrapper<Integer[]>(updateCounts.clone(), new MockParameterMap(parameters)));
    }
    
    /**
     * Prepare the global update count for <code>executeUpdate</code> calls.
     * @param updateCount the update count
     */
    public void prepareGlobalUpdateCount(int updateCount)
    {
        this.globalUpdateCounts = new Integer[]{updateCount};
    }
    
    /**
     * Prepare an array of global update count values for <code>executeUpdate</code> calls.
     * @param updateCounts the update count array
     */
    public void prepareGlobalUpdateCounts(Integer[] updateCounts)
    {
        this.globalUpdateCounts = updateCounts.clone();
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
        prepareGeneratedKeys(sql, generatedKeysResult, new MockParameterMap());
    }

    protected void prepareGeneratedKeys(String sql, MockResultSet generatedKeysResult, MockParameterMap parameters)
    {
        List<ParameterWrapper<MockResultSet>> list = getListFromMapForSQLStatement(sql, generatedKeysForStatement);
        list.add(new ParameterWrapper<MockResultSet>(generatedKeysResult, new MockParameterMap(parameters)));
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
        returnsResultSetMap.put(sql, returnsResultSet);
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
        prepareThrowsSQLException(sql, new MockParameterMap());
    }
    
    protected void prepareThrowsSQLException(String sql, MockParameterMap parameters)
    {
        SQLException exc = new SQLException("Statement " + sql + " was specified to throw an exception");
        prepareThrowsSQLException(sql, exc, parameters);
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
        prepareThrowsSQLException(sql, exc, new MockParameterMap());
    }
    
    protected void prepareThrowsSQLException(String sql, SQLException exc, MockParameterMap parameters)
    {
        List<ParameterWrapper<SQLException>> list = getListFromMapForSQLStatement(sql, throwsSQLException);
        list.add(new ParameterWrapper<SQLException>(exc, new MockParameterMap(parameters)));
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

    /**
     * Given a SQL string, remove the associated entry from the resultSetsForStatement TreeMap
     * @param sql The SQL string associated with the resultset
     */
    public void removeAllResultSet(String sql) {
    	resultSetsForStatement.remove(sql);
    }

    /**
     * Remove the throws mock for the specified SQL string.
     * @param sql The SQL string which identifies the conditions under which to throw a SQLException
     */
    public void removeAllThrowsSqlException(String sql) {
    	throwsSQLException.remove(sql);
    }


    /**
     * Remove the update count mock for the specified SQL string.
     * @param sql The SQL string which identifies the conditions under which to return the specified update count
     */
    public void removeAllUpdateCount(String sql) {
    	updateCountForStatement.remove(sql);
    }


    /**
     * Remove the generated keys mock for the specified SQL string.
     * @param sql The SQL string which identifies the conditions under which the generated keys result would be returned.
     */
    public void removeAllGeneratedKeys(String sql) {
    	generatedKeysForStatement.remove(sql);
    }

    /**
     * Given a SQL string, remove the associated entry from the resultSetsForStatement TreeMap
     * @param sql The SQL string associated with the resultset
     */
    public void removeResultSet(String sql) {
        removeResultSet(sql, new MockParameterMap(), false);
    }

    protected void removeResultSet(String sql, MockParameterMap parameters, boolean exactMatchParameter) {
        removeMatchingParameterWrapper(sql, parameters, resultSetsForStatement, exactMatchParameter);
    }

    /**
     * Remove the throws mock for the specified SQL string.
     * @param sql The SQL string which identifies the conditions under which to throw a SQLException
     */
    public void removeThrowsSqlException(String sql) {
        removeThrowsSqlException(sql, new MockParameterMap(), false);
    }
    
    protected void removeThrowsSqlException(String sql, MockParameterMap parameters, boolean exactMatchParameter) {
        removeMatchingParameterWrapper(sql, parameters, throwsSQLException, exactMatchParameter);
    }

    /**
     * Remove the update count mock for the specified SQL string.
     * @param sql The SQL string which identifies the conditions under which to return the specified update count
     */
    public void removeUpdateCount(String sql){
        removeUpdateCount(sql, new MockParameterMap(), false);
    }
    
    protected void removeUpdateCount(String sql, MockParameterMap parameters, boolean exactMatchParameter) {
        removeMatchingParameterWrapper(sql, parameters, updateCountForStatement, exactMatchParameter);
    }

    /**
     * Remove the generated keys mock for the specified SQL string.
     * @param sql The SQL string which identifies the conditions under which the generated keys result would be returned.
     */
    public void removeGeneratedKeys(String sql){
        removeGeneratedKeys(sql, new MockParameterMap(), false);
    }
    

    protected void removeGeneratedKeys(String sql, MockParameterMap parameters, boolean exactMatchParameter) {
        removeMatchingParameterWrapper(sql, parameters, generatedKeysForStatement, exactMatchParameter);
    }
    
    protected <T> ParameterWrapper<T> getMatchingParameterWrapper(String sql, MockParameterMap parameters, Map<String, List<ParameterWrapper<T>>> statementMap, boolean exactMatchParameter)
    {
        SQLStatementMatcher<List<ParameterWrapper<T>>> matcher = new SQLStatementMatcher<List<ParameterWrapper<T>>>(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List<List<ParameterWrapper<T>>> list = matcher.getMatchingObjects(statementMap, sql, true);
        for(List<ParameterWrapper<T>> wrapperList : list)
        {
            for(ParameterWrapper<T> wrapper : wrapperList)
            {
                if(wrapper.getParameters().doParameterMatch(parameters, exactMatchParameter))
                {
                    return wrapper;
                }
            }
        }
        return null;
    }
    
    protected <T> ParameterWrapper<T> removeMatchingParameterWrapper(String sql, MockParameterMap parameters, Map<String, List<ParameterWrapper<T>>> statementMap, boolean exactMatchParameter)
    {
        SQLStatementMatcher<List<ParameterWrapper<T>>> matcher = new SQLStatementMatcher<List<ParameterWrapper<T>>>(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List<List<ParameterWrapper<T>>> list = matcher.getMatchingObjects(statementMap, sql, true);
        for(List<ParameterWrapper<T>> wrapperList : list)
        {
            for(ParameterWrapper<T> wrapper : wrapperList)
            {
                if(wrapper.getParameters().doParameterMatch(parameters, exactMatchParameter))
                {
                    wrapperList.remove(wrapper);
                    return wrapper;
                }
            }
        }
        return null;
    }
    
    protected <T> List<T> getListFromMapForSQLStatement(String sql, Map<String, List<T>> map)
    {
        List<T> list = map.get(sql);
        if(null == list)
        {
            list = new ArrayList<T>();
            map.put(sql, list);
        }
        return list;
    }
    
    
}
