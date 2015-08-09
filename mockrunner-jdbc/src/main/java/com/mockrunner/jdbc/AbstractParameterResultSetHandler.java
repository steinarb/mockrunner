package com.mockrunner.jdbc;

import com.mockrunner.mock.jdbc.MockParameterMap;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.ParameterReference;

/**
 * Abstract base class for all statement types
 * that support parameters, i.e. <code>PreparedStatement</code>
 * and <code>CallableStatement</code>.
 */
public abstract class AbstractParameterResultSetHandler extends AbstractResultSetHandler
{
    private boolean exactMatchParameter = false;
    private final Map<String, List<ParameterWrapper<MockResultSet[]>>> resultSetsForStatement = new TreeMap<String, List<ParameterWrapper<MockResultSet[]>>>();
    private final Map<String, List<ParameterWrapper<Integer[]>>> updateCountForStatement = new TreeMap<String, List<ParameterWrapper<Integer[]>>>();
    private final Map<String, List<ParameterWrapper<SQLException>>> throwsSQLException = new TreeMap<String, List<ParameterWrapper<SQLException>>>();
    private final Map<String, List<ParameterWrapper<MockResultSet>>> generatedKeysForStatement = new TreeMap<String, List<ParameterWrapper<MockResultSet>>>();
	private final Map<String, ParameterSets> executedStatementParameters = new TreeMap<String, ParameterSets>();
    
	/**
	 * Collects all SQL strings that were executed.
	 * @param sql the SQL string
	 * @param parameters a copy of the corresponding parameter map
	 */
	public void addParameterMapForExecutedStatement(String sql, MockParameterMap parameters)
	{
		if(null != parameters)
		{
			if(null == executedStatementParameters.get(sql))
			{
				executedStatementParameters.put(sql, new ParameterSets(sql));
			}
			ParameterSets sets = executedStatementParameters.get(sql);
			sets.addParameterSet(parameters);
		}
	}
	
	/**
	 * Returns the <code>ParameterSets</code> for a specified
	 * SQL string.
	 * @param sql the SQL string
	 * @return the <code>Map</code> of parameters
	 */
	public ParameterSets getParametersForExecutedStatement(String sql)
	{
		return executedStatementParameters.get(sql);
	}
	
	/**
	 * Returns the <code>Map</code> of executed SQL strings.
	 * Each string maps to the corresponding {@link ParameterSets}
	 * object.
	 * @return the <code>Map</code> of parameters
	 */
	public Map<String, ParameterSets> getExecutedStatementParameterMap()
	{
		return Collections.unmodifiableMap(executedStatementParameters);
	}
    
    /**
     * @deprecated use {@link #getExecutedStatementParameterMap}
     */
    @Deprecated
    public Map<String, ParameterSets> getExecutedStatementParameter()
    {
        return getExecutedStatementParameterMap();
    }
    
    /**
     * Sets if the specified parameters must match exactly
     * in order and number.
     * Defaults to <code>false</code>, i.e. the specified
     * parameters must be present in the actual parameter
     * list of the prepared statement with the correct index
     * but it's ok if there are more actual parameters.
     * @param exactMatchParameter must parameters match exactly
     */
    public void setExactMatchParameter(boolean exactMatchParameter)
    {
        this.exactMatchParameter = exactMatchParameter;
    }

    /**
     * Returns the first update count that matches the
     * specified SQL string and the specified parameters.
     * If the specified SQL string was prepared to return multiple update 
     * counts, the first one will be returned.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return the corresponding update count
     */
    public Integer getUpdateCount(String sql, MockParameterMap parameters)
    {
        Integer[] updateCounts = getUpdateCounts(sql, parameters);
        if(null != updateCounts && updateCounts.length > 0)
        {
            return updateCounts[0];
        }
        return null;
    }
    
    /**
     * Returns the first update count array that matches the
     * specified SQL string and the specified parameters. 
     * If the specified SQL string was prepared to return one update count, 
     * this value will be wrapped in an array with one element.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return the corresponding update count
     */
    public Integer[] getUpdateCounts(String sql, MockParameterMap parameters)
    {
        ParameterWrapper<Integer[]> wrapper = getMatchingParameterWrapper(sql, parameters, updateCountForStatement);
        if(null != wrapper)
        {
            wrapper.getWrappedObject();
        }
        return null;
    }
    
    /**
     * Returns the if the specified SQL string with the specified parameters
     * returns multiple update counts.
     * Please note that you can modify the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return <code>true</code> if the SQL string returns multiple update counts,
     *         <code>false</code> otherwise
     */
    public boolean hasMultipleUpdateCounts(String sql, MockParameterMap parameters)
    {
        ParameterWrapper<Integer[]> wrapper = getMatchingParameterWrapper(sql, parameters, updateCountForStatement);
        return (wrapper != null && wrapper.getWrappedObject().length > 1);
    }

    /**
     * Returns the first <code>ResultSet</code> that matches the
     * specified SQL string and the specified parameters.
     * If the specified SQL string was prepared to return multiple result 
     * sets, the first one will be returned.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return the corresponding {@link MockResultSet}
     */
    public MockResultSet getResultSet(String sql, MockParameterMap parameters)
    {
        MockResultSet[] resultSets = getResultSets(sql, parameters);
        if(null != resultSets && resultSets.length > 0)
        {
            return resultSets[0];
        }
        return null;
    }
    
    /**
     * Returns the first <code>ResultSet[]</code> that matches the
     * specified SQL string and the specified parameters. 
     * If the specified SQL string was prepared to return one single 
     * <code>ResultSet</code>, this <code>ResultSet</code> will be wrapped 
     * in an array with  one element.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return the corresponding update count
     */
    public MockResultSet[] getResultSets(String sql, MockParameterMap parameters)
    {
        ParameterWrapper<MockResultSet[]> wrapper = getMatchingParameterWrapper(sql, parameters, resultSetsForStatement);

        if(null != wrapper){
            return wrapper.getWrappedObject();
        }
        return null;
    }
    
    /**
     * Returns the if the specified SQL string with the specified parameters
     * returns multiple result sets.
     * Please note that you can modify the match parameters with {@link #setCaseSensitive},
     * {@link #setExactMatch} and {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return <code>true</code> if the SQL string returns multiple update counts,
     *         <code>false</code> otherwise
     */
    public boolean hasMultipleResultSets(String sql, MockParameterMap parameters)
    {
        ParameterWrapper<MockResultSet[]> wrapper = getMatchingParameterWrapper(sql, parameters, resultSetsForStatement);
        return (null != wrapper && wrapper.getWrappedObject().length > 1);
    }
    
    /**
     * Returns if the specified SQL string with the specified parameters
     * should raise an exception.
     * This can be used to simulate database exceptions.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return <code>true</code> if the specified SQL string should raise an exception,
     *         <code>false</code> otherwise
     */
    public boolean getThrowsSQLException(String sql, MockParameterMap parameters)
    {
        return (getSQLException(sql, parameters) != null);
    }
    
    /**
     * Returns the <code>SQLException</code> the specified SQL string
     * should throw. Returns <code>null</code> if the specified SQL string
     * should not throw an exception.
     * This can be used to simulate database exceptions.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return the <code>SQLException</code> or <code>null</code>
     */
    public SQLException getSQLException(String sql, MockParameterMap parameters)
    {
        ParameterWrapper<SQLException> wrapper = getMatchingParameterWrapper(sql, parameters, throwsSQLException);
        if(null != wrapper)
        {
            return wrapper.getWrappedObject();
        }
        return null;
    }
    
    /**
     * Returns the first generated keys <code>ResultSet</code> that 
     * matches the specified SQL string. 
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return the corresponding generated keys {@link MockResultSet}
     */
    public MockResultSet getGeneratedKeys(String sql, MockParameterMap parameters)
    {
        ParameterWrapper<MockResultSet> wrapper = getMatchingParameterWrapper(sql, parameters, generatedKeysForStatement);
        if(null != wrapper)
        {
            return wrapper.getWrappedObject();
        }
        return null;
    }

    protected <T> ParameterWrapper<T> getMatchingParameterWrapper(String sql, MockParameterMap parameters, Map<String, List<ParameterWrapper<T>>> statementMap)
    {
        SQLStatementMatcher<List<ParameterWrapper<T>>> matcher = new SQLStatementMatcher<List<ParameterWrapper<T>>>(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List<List<ParameterWrapper<T>>> list = matcher.getMatchingObjects(statementMap, sql, true);
        for(List<ParameterWrapper<T>> wrapperList : list)
        {
            for(ParameterWrapper<T> wrapper : wrapperList)
            {
                if(doParameterMatch(wrapper.getParameters(), parameters))
                {
                    return wrapper;
                }
            }
        }
        return null;
    }
    
    private boolean doParameterMatch(MockParameterMap expectedParameters, MockParameterMap actualParameters)
    {
        if(exactMatchParameter)
        {
            if(actualParameters.size() != expectedParameters.size()) return false;
            Iterator<ParameterReference> iterator = actualParameters.keySet().iterator();
            while(iterator.hasNext())
            {
                ParameterReference currentKey = iterator.next();
                if(!actualParameters.containsKey(currentKey)) return false;
                Object expectedObject = expectedParameters.get(currentKey);
                if(!ParameterUtil.compareParameter(actualParameters.get(currentKey), expectedObject))
                {
                    return false;
                }
            }
            return true;
        }
        else
        {
            Iterator<ParameterReference> iterator = expectedParameters.keySet().iterator();
            while(iterator.hasNext())
            {
                ParameterReference currentKey = iterator.next();
                if(!actualParameters.containsKey(currentKey)) return false;
                Object actualObject = actualParameters.get(currentKey);
                if(!ParameterUtil.compareParameter(actualObject, expectedParameters.get(currentKey)))
                {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Clears the <code>ResultSet</code> objects.
     */
    @Override
    public void clearResultSets()
    {
        super.clearResultSets();
        resultSetsForStatement.clear();
    }
    
    /**
     * Clears the update counts.
     */
    @Override
    public void clearUpdateCounts()
    {
        super.clearUpdateCounts();
        updateCountForStatement.clear();
    }
    
    /**
     * Clears the list of statements that should throw an exception
     */
    @Override
    public void clearThrowsSQLException()
    {
        super.clearThrowsSQLException();
        throwsSQLException.clear();
    }
    
    /**
     * Clears the list of statements that return generated keys.
     */
    @Override
    public void clearGeneratedKeys()
    {
        super.clearGeneratedKeys();
        generatedKeysForStatement.clear();
    }

    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string and
     * the specified parameters. The specified parameters array
     * must contain the parameters in the correct order starting with index 0 for
     * the first parameter. Please keep in mind that parameters in
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters[0]</code> maps to the
     * parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param resultSet the corresponding {@link MockResultSet}
     * @param parameters the parameters
     */
    public void prepareResultSet(String sql, MockResultSet resultSet, Object[] parameters)
    {
        prepareResultSet(sql, resultSet, Arrays.asList(parameters));
    }
    
    /**
     * Prepare an array of <code>ResultSet</code> objects for a specified SQL string and
     * the specified parameters. This method can be used for queries that return 
     * multiple result sets. The specified parameters array
     * must contain the parameters in the correct order starting with index 0 for
     * the first parameter. Please keep in mind that parameters in
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters[0]</code> maps to the
     * parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param resultSets the corresponding <code>MockResultSet[]</code>
     * @param parameters the parameters
     */
    public void prepareResultSets(String sql, MockResultSet[] resultSets, Object[] parameters)
    {
        prepareResultSets(sql, resultSets, Arrays.asList(parameters));
    }

    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string and
     * the specified parameters. The specified parameters <code>List</code>
     * must contain the parameters in the correct order starting with index 0 for
     * the first parameter. Please keep in mind that parameters in
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the
     * parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param resultSet the corresponding {@link MockResultSet}
     * @param parameters the parameters
     */
    public void prepareResultSet(String sql, MockResultSet resultSet, List<Object> parameters)
    {
        MockParameterMap params = createParameterMap(parameters);
        prepareResultSet(sql, resultSet, params);
    }
    
    /**
     * Prepare an array of <code>ResultSet</code> objects for a specified SQL string and
     * the specified parameters. This method can be used for queries that return 
     * multiple result sets. The specified parameters <code>List</code>
     * must contain the parameters in the correct order starting with index 0 for
     * the first parameter. Please keep in mind that parameters in
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the
     * parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param resultSets the corresponding <code>MockResultSet[]</code>
     * @param parameters the parameters
     */
    public void prepareResultSets(String sql, MockResultSet[] resultSets, List<Object> parameters)
    {
        MockParameterMap params = createParameterMap(parameters);
        prepareResultSets(sql, resultSets, params);
    }
    
    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string and
     * the specified parameters. The specified parameters <code>Map</code>
     * must contain the parameters by mapping <code>Integer</code> objects
     * to the corresponding parameter. The <code>Integer</code> object
     * is the index of the parameter. In the case of a <code>CallableStatement</code>,
     * <code>String</code> keys for named parameters are also allowed.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param resultSet the corresponding {@link MockResultSet}
     * @param parameters the parameters
     */
    public void prepareResultSet(String sql, MockResultSet resultSet, MockParameterMap parameters)
    {
        List<ParameterWrapper<MockResultSet[]>> list = getListFromMapForSQLStatement(sql, resultSetsForStatement);
        list.add(new ParameterWrapper<MockResultSet[]>(new MockResultSet[]{resultSet}, new MockParameterMap(parameters)));
    }
    
    /**
     * Prepare an array of <code>ResultSet</code> objects for a specified SQL string and
     * the specified parameters. This method can be used for queries that return 
     * multiple result sets. The specified parameters <code>Map</code>
     * must contain the parameters by mapping <code>Integer</code> objects
     * to the corresponding parameter. The <code>Integer</code> object
     * is the index of the parameter. In the case of a <code>CallableStatement</code>,
     * <code>String</code> keys for named parameters are also allowed.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param resultSets the corresponding <code>MockResultSet[]</code>
     * @param parameters the parameters
     */
    public void prepareResultSets(String sql, MockResultSet[] resultSets, MockParameterMap parameters)
    {
        List<ParameterWrapper<MockResultSet[]>> list = getListFromMapForSQLStatement(sql, resultSetsForStatement);
        list.add(new ParameterWrapper<MockResultSet[]>(resultSets.clone(), new MockParameterMap(parameters)));
    }
    
    /**
     * Prepare that the specified SQL string with the specified parameters
     * should raise an exception.
     * This can be used to simulate database exceptions.
     * This method creates an <code>SQLException</code> and will throw this 
     * exception. With {@link #prepareThrowsSQLException(String, SQLException, Object[])} 
     * you can specify the exception.
     * The specified parameters array must contain the parameters in 
     * the correct order starting with index 0 for the first parameter. 
     * Please keep in mind that parameters in <code>PreparedStatement</code> 
     * objects start with 1 as the first parameter. So <code>parameters[0]</code> 
     * maps to the parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     */
    public void prepareThrowsSQLException(String sql, Object[] parameters)
    {
        SQLException exc = new SQLException("Statement " + sql + " was specified to throw an exception");
        prepareThrowsSQLException(sql, exc, parameters);
    }
    
    /**
     * Prepare that the specified SQL string with the specified parameters
     * should raise an exception.
     * This can be used to simulate database exceptions.
     * This method creates an <code>SQLException</code> and will throw this 
     * exception. With {@link #prepareThrowsSQLException(String, SQLException, List)} 
     * you can specify the exception.
     * The specified parameters <code>List</code> must contain the 
     * parameters in the correct order starting with index 0 for the first 
     * parameter. Please keep in mind that parameters in 
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the parameter 
     * with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     */
    public void prepareThrowsSQLException(String sql, List<Object> parameters)
    {
        SQLException exc = new SQLException("Statement " + sql + " was specified to throw an exception");
        prepareThrowsSQLException(sql, exc, parameters);
    }
    
    /**
     * Prepare that the specified SQL string with the specified parameters
     * should raise an exception.
     * This can be used to simulate database exceptions.
     * This method creates an <code>SQLException</code> and will throw this 
     * exception. With {@link #prepareThrowsSQLException(String, SQLException, Map)} 
     * you can specify the exception.
     * The specified parameters <code>Map</code> must contain the parameters by 
     * mapping <code>Integer</code> objects to the corresponding parameter. 
     * The <code>Integer</code> object is the index of the parameter. In the case
     * of a <code>CallableStatement</code>, 
     * <code>String</code> keys for named parameters are also allowed.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     */
    public void prepareThrowsSQLException(String sql, MockParameterMap parameters)
    {
        SQLException exc = new SQLException("Statement " + sql + " was specified to throw an exception");
        prepareThrowsSQLException(sql, exc, parameters);
    }
    
    /**
     * Prepare that the specified SQL string with the specified parameters
     * should raise an exception.
     * This can be used to simulate database exceptions.
     * This method takes an exception object that will be thrown.
     * The specified parameters array must contain the parameters in 
     * the correct order starting with index 0 for the first parameter. 
     * Please keep in mind that parameters in <code>PreparedStatement</code> 
     * objects start with 1 as the first parameter. So <code>parameters[0]</code> 
     * maps to the parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param exc the <code>SQLException</code> that should be thrown
     * @param parameters the parameters
     */
    public void prepareThrowsSQLException(String sql, SQLException exc, Object[] parameters)
    {
        prepareThrowsSQLException(sql, exc, Arrays.asList(parameters));
    }
    
    /**
     * Prepare that the specified SQL string with the specified parameters
     * should raise an exception.
     * This can be used to simulate database exceptions.
     * This method takes an exception object that will be thrown.
     * The specified parameters <code>List</code> must contain the 
     * parameters in the correct order starting with index 0 for the first 
     * parameter. Please keep in mind that parameters in 
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the parameter 
     * with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param exc the <code>SQLException</code> that should be thrown
     * @param parameters the parameters
     */
    public void prepareThrowsSQLException(String sql, SQLException exc, List<Object> parameters)
    {
        MockParameterMap params = createParameterMap(parameters);
        prepareThrowsSQLException(sql, exc, params);
    }
    
    /**
     * Prepare that the specified SQL string with the specified parameters
     * should raise an exception.
     * This can be used to simulate database exceptions.
     * This method takes an exception object that will be thrown.
     * The specified parameters <code>Map</code> must contain the parameters by 
     * mapping <code>Integer</code> objects to the corresponding parameter. 
     * The <code>Integer</code> object is the index of the parameter. In the case
     * of a <code>CallableStatement</code>, 
     * <code>String</code> keys for named parameters are also allowed.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param exc the <code>SQLException</code> that should be thrown
     * @param parameters the parameters
     */
    public void prepareThrowsSQLException(String sql, SQLException exc, MockParameterMap parameters)
    {
        List<ParameterWrapper<SQLException>> list = getListFromMapForSQLStatement(sql, throwsSQLException);
        list.add(new ParameterWrapper<SQLException>(exc, new MockParameterMap(parameters)));
    }

    /**
     * Prepare the update count for execute update calls for a specified SQL string
     * and the specified parameters. The specified parameters array
     * must contain the parameters in the correct order starting with index 0 for
     * the first parameter. Please keep in mind that parameters in
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters[0]</code> maps to the
     * parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param updateCount the update count
     * @param parameters the parameters
     */
    public void prepareUpdateCount(String sql, int updateCount, Object[] parameters)
    {
        prepareUpdateCount(sql, updateCount, Arrays.asList(parameters));
    }
    
    /**
     * Prepare an array update count values for execute update calls for a specified SQL string
     * and the specified parameters. This method can be used if multiple update counts
     * are returned. The specified parameters array
     * must contain the parameters in the correct order starting with index 0 for
     * the first parameter. Please keep in mind that parameters in
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters[0]</code> maps to the
     * parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param updateCounts the update count array
     * @param parameters the parameters
     */
    public void prepareUpdateCounts(String sql, Integer[] updateCounts, Object[] parameters)
    {
        prepareUpdateCounts(sql, updateCounts, Arrays.asList(parameters));
    }

    /**
     * Prepare the update count for execute update calls for a specified SQL string
     * and the specified parameters. The specified parameters <code>List</code>
     * must contain the parameters in the correct order starting with index 0 for
     * the first parameter. Please keep in mind that parameters in
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the
     * parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param updateCount the update count
     * @param parameters the parameters
     */
    public void prepareUpdateCount(String sql, int updateCount, List<Object> parameters)
    {
        MockParameterMap params = createParameterMap(parameters);
        prepareUpdateCount(sql, updateCount,  params);
    }
    
    /**
     * Prepare an array update count values for execute update calls for a specified SQL string
     * and the specified parameters. This method can be used if multiple update counts
     * are returned. The specified parameters <code>List</code>
     * must contain the parameters in the correct order starting with index 0 for
     * the first parameter. Please keep in mind that parameters in
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the
     * parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param updateCounts the update count array
     * @param parameters the parameters
     */
    public void prepareUpdateCounts(String sql, Integer[] updateCounts, List<Object> parameters)
    {
        MockParameterMap params = createParameterMap(parameters);
        prepareUpdateCounts(sql, updateCounts,  params);
    }
    
    /**
     * Prepare the update count for execute update calls for a specified SQL string
     * and the specified parameters. The specified parameters <code>Map</code>
     * must contain the parameters by mapping <code>Integer</code> objects
     * to the corresponding parameter. The <code>Integer</code> object
     * is the index of the parameter. In the case of a <code>CallableStatement</code>,
     * <code>String</code> keys for named parameters are also allowed.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param updateCount the update count
     * @param parameters the parameters
     */
    public void prepareUpdateCount(String sql, int updateCount, MockParameterMap parameters)
    {
        List<ParameterWrapper<Integer[]>> list = getListFromMapForSQLStatement(sql, updateCountForStatement);
        list.add(new ParameterWrapper<Integer[]>(new Integer[]{updateCount}, new MockParameterMap(parameters)));
    }
    
    /**
     * Prepare an array update count values for execute update calls for a specified SQL string
     * and the specified parameters. This method can be used if multiple update counts
     * are returned. The specified parameters <code>Map</code>
     * must contain the parameters by mapping <code>Integer</code> objects
     * to the corresponding parameter. The <code>Integer</code> object
     * is the index of the parameter. In the case of a <code>CallableStatement</code>,
     * <code>String</code> keys for named parameters are also allowed.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param updateCounts the update count array
     * @param parameters the parameters
     */
    public void prepareUpdateCounts(String sql, Integer[] updateCounts, MockParameterMap parameters)
    {
        List<ParameterWrapper<Integer[]>> list = getListFromMapForSQLStatement(sql, updateCountForStatement);
        list.add(new ParameterWrapper<Integer[]>(updateCounts.clone(), new MockParameterMap(parameters)));
    }
    
    /**
     * Prepare the generated keys <code>ResultSet</code> 
     * for a specified SQL string.
     * The specified parameters array must contain the parameters in 
     * the correct order starting with index 0 for the first parameter. 
     * Please keep in mind that parameters in <code>PreparedStatement</code> 
     * objects start with 1 as the first parameter. So <code>parameters[0]</code> 
     * maps to the parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param generatedKeysResult the generated keys {@link MockResultSet}
     * @param parameters the parameters
     */
    public void prepareGeneratedKeys(String sql, MockResultSet generatedKeysResult, Object[] parameters)
    {
        prepareGeneratedKeys(sql, generatedKeysResult, Arrays.asList(parameters));
    }
    
    /**
     * Prepare the generated keys <code>ResultSet</code> 
     * for a specified SQL string.
     * The specified parameters <code>List</code> must contain the 
     * parameters in the correct order starting with index 0 for the first 
     * parameter. Please keep in mind that parameters in 
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the parameter 
     * with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param generatedKeysResult the generated keys {@link MockResultSet}
     * @param parameters the parameters
     */
    public void prepareGeneratedKeys(String sql, MockResultSet generatedKeysResult, List<Object> parameters)
    {
        MockParameterMap params = createParameterMap(parameters);
        prepareGeneratedKeys(sql, generatedKeysResult, params);
    }
    
    /**
     * Prepare the generated keys <code>ResultSet</code> 
     * for a specified SQL string.
     * The specified parameters <code>Map</code> must contain the parameters by 
     * mapping <code>Integer</code> objects to the corresponding parameter. 
     * The <code>Integer</code> object is the index of the parameter. In the case
     * of a <code>CallableStatement</code>, 
     * <code>String</code> keys for named parameters are also allowed.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param generatedKeysResult the generated keys {@link MockResultSet}
     * @param parameters the parameters
     */
    public void prepareGeneratedKeys(String sql, MockResultSet generatedKeysResult, MockParameterMap parameters)
    {
        List<ParameterWrapper<MockResultSet>> list = getListFromMapForSQLStatement(sql, generatedKeysForStatement);
        list.add(new ParameterWrapper<MockResultSet>(generatedKeysResult, new MockParameterMap(parameters)));
    }
    
    private <T> List<T> getListFromMapForSQLStatement(String sql, Map<String, List<T>> map)
    {
        List<T> list = map.get(sql);
        if(null == list)
        {
            list = new ArrayList<T>();
            map.put(sql, list);
        }
        return list;
    }
    
    private MockParameterMap createParameterMap(List<Object> parameters)
    {
        MockParameterMap params = new MockParameterMap();
        for(int ii = 0; ii < parameters.size(); ii++)
        {
            params.put(ii + 1, parameters.get(ii));
        }
        return params;
    }
    
    protected class ParameterWrapper<T>
    {
        private final MockParameterMap parameters;
        private final T wrappedObject;
        
        public ParameterWrapper(T wrappedObject, MockParameterMap parameters)
        {
            this.wrappedObject = wrappedObject;
            this.parameters = parameters;
        }

        @Deprecated
        public MockParameterMap getParamters()
        {
            return parameters;
        }
        
        public MockParameterMap getParameters()
        {
            return parameters;
        }
        
        public T getWrappedObject(){
            return wrappedObject;
        }
    }
}
