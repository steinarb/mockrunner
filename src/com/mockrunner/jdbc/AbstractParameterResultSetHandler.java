package com.mockrunner.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.util.common.ArrayUtil;

/**
 * Abstract base class for all statement types
 * that support parameters, i.e. <code>PreparedStatement</code>
 * and <code>CallableStatement</code>.
 */
public abstract class AbstractParameterResultSetHandler extends AbstractResultSetHandler
{
    private boolean exactMatchParameter = false;
    private Map resultSetsForStatement = new HashMap();
    private Map updateCountForStatement = new HashMap();
    private Map throwsSQLException = new HashMap();
	private Map executedStatementParameters = new HashMap();
    
	/**
	 * Collects all SQL strings that were executed.
	 * @param sql the SQL string
	 * @param parameters a copy of the corresponding parameter map
	 */
	public void addParameterMapForExecutedStatement(String sql, Map parameters)
	{
		if(null != parameters)
		{
			if(null == executedStatementParameters.get(sql))
			{
				executedStatementParameters.put(sql, new ParameterSets(sql));
			}
			ParameterSets sets = (ParameterSets)executedStatementParameters.get(sql);
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
		return (ParameterSets)executedStatementParameters.get(sql);
	}
	
	/**
	 * Returns the <code>Map</code> of executed SQL strings.
	 * Each string maps to the corresponding {@link ParameterSets}
	 * object.
	 * @return the <code>Map</code> of parameters
	 */
	public Map getExecutedStatementParameter()
	{
		return Collections.unmodifiableMap(executedStatementParameters);
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
     * Please note that you can modify the search parameters for 
     * the SQL string with {@link #setCaseSensitive} and
     * {@link #setExactMatch} and the search parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return the corresponding update count
     */
    public Integer getUpdateCount(String sql, Map parameters)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List list = matcher.getMatchingObjects(updateCountForStatement, sql, true, true);
        for(int ii = 0; ii < list.size(); ii++)
        {
            MockUpdateCountWrapper wrapper = (MockUpdateCountWrapper)list.get(ii);
            if(doParameterMatch(wrapper.getParamters(), parameters))
            {
                return wrapper.getUpdateCount();
            }
        }
        return null;
    }

    /**
     * Returns the first <code>ResultSet</code> that matches the
     * specified SQL string and the specified parameters.
     * Please note that you can modify the search parameters for 
     * the SQL string with {@link #setCaseSensitive} and
     * {@link #setExactMatch} and the search parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return the corresponding {@link MockResultSet}
     */
    public MockResultSet getResultSet(String sql, Map parameters)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List list = matcher.getMatchingObjects(resultSetsForStatement, sql, true, true);
        for(int ii = 0; ii < list.size(); ii++)
        {
            MockResultSetWrapper wrapper = (MockResultSetWrapper)list.get(ii);
            if(doParameterMatch(wrapper.getParamters(), parameters))
            {
                return wrapper.getResultSet();
            }
        }
        return null;
    }
    
    /**
     * Returns if the specified SQL string with the specified parameters
     * should raise an exception.
     * This can be used to simulate database exceptions
     * @param sql the SQL string
     * @param parameters the parameters
     * @return <code>true</code> if the specified SQL string should raise an exception,
     *         <code>false</code> otherwise
     */
    public boolean getThrowsSQLException(String sql, Map parameters)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
        List list = matcher.getMatchingObjects(throwsSQLException, sql, true, true);
        for(int ii = 0; ii < list.size(); ii++)
        {
            if(doParameterMatch((Map)list.get(ii), parameters))
            {
                return true;
            }
        }
        return false;
    }

    protected boolean doParameterMatch(Map expectedParameters, Map actualParameters)
    {
        if(exactMatchParameter)
        {
            if(actualParameters.size() != expectedParameters.size()) return false;
            Iterator iterator = actualParameters.keySet().iterator();
            while(iterator.hasNext())
            {
                Object currentKey = iterator.next();
                Object expectedObject = expectedParameters.get(currentKey);
                if(null == expectedObject) return false;
                if(!ParameterUtil.compareParameter(actualParameters.get(currentKey), expectedObject))
                {
                    return false;
                }
            }
            return true;
        }
        else
        {
            Iterator iterator = expectedParameters.keySet().iterator();
            while(iterator.hasNext())
            {
                Object currentKey = iterator.next();
                Object actualObject = actualParameters.get(currentKey);
                if(null == actualObject) return false;
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
    public void clearResultSets()
    {
        super.clearResultSets();
        resultSetsForStatement.clear();
    }
    
    /**
     * Clears the update counts.
     */
    public void clearUpdateCounts()
    {
        super.clearUpdateCounts();
        updateCountForStatement.clear();
    }
    
    /**
     * Clears the list of statements that should throw an exception
     */
    public void clearThrowsSQLException()
    {
        super.clearThrowsSQLException();
        throwsSQLException.clear();
    }

    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string and
     * the specified parameters. The specified parameters array
     * must contain the parameters in the correct order starting with 0 as
     * the first parameter. Please keep in mind that parameters in
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters[0]</code> maps to the
     * parameter with index 1.
     * @param sql the SQL string
     * @param resultSet the corresponding {@link MockResultSet}
     * @param parameters the parameters
     */
    public void prepareResultSet(String sql, MockResultSet resultSet, Object[] parameters)
    {
        prepareResultSet(sql, resultSet, ArrayUtil.getListFromObjectArray(parameters));
    }

    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string and
     * the specified parameters. The specified parameters <code>List</code>
     * must contain the parameters in the correct order starting with 0 as
     * the first parameter. Please keep in mind that parameters in
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the
     * parameter with index 1.
     * @param sql the SQL string
     * @param resultSet the corresponding {@link MockResultSet}
     * @param parameters the parameters
     */
    public void prepareResultSet(String sql, MockResultSet resultSet, List parameters)
    {
        Map params = new HashMap();
        for(int ii = 0; ii < parameters.size(); ii++)
        {
            params.put(new Integer(ii + 1), parameters.get(ii));
        }
        prepareResultSet(sql, resultSet, params);
    }
    
    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string and
     * the specified parameters. The specified parameters <code>Map</code>
     * must contain the parameters by mapping <code>Integer</code> objects
     * to the corresponding parameter. The <code>Integer</code> object
     * is the index of the parameter. In the case of a <code>CallableStatement</code>
     * there are also allowed <code>String</code> keys for named parameters.
     * @param sql the SQL string
     * @param resultSet the corresponding {@link MockResultSet}
     * @param parameters the parameters
     */
    public void prepareResultSet(String sql, MockResultSet resultSet, Map parameters)
    {
        List list = (List)resultSetsForStatement.get(sql);
        if(null == list)
        {
            list = new ArrayList();
            resultSetsForStatement.put(sql, list);
        }
        list.add(new MockResultSetWrapper(resultSet, parameters));
    }
    
    /**
     * Prepare if the specified SQL string with the specified parameters
     * should raise an exception.
     * This can be used to simulate database exceptions.
     * The specified parameters array must contain the parameters in 
     * the correct order starting with 0 as the first parameter. 
     * Please keep in mind that parameters in <code>PreparedStatement</code> 
     * objects start with 1 as the first parameter. So <code>parameters[0]</code> 
     * maps to the parameter with index 1.
     * @param sql the SQL string
     * @param parameters the parameters
     */
    public void prepareThrowsSQLException(String sql, Object[] parameters)
    {
        prepareThrowsSQLException(sql, ArrayUtil.getListFromObjectArray(parameters));
    }
    
    /**
     * Prepare if the specified SQL string with the specified parameters
     * should raise an exception.
     * This can be used to simulate database exceptions.
     * The specified parameters <code>List</code> must contain the 
     * parameters in the correct order starting with 0 as the first 
     * parameter. Please keep in mind that parameters in 
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the parameter 
     * with index 1.
     * @param sql the SQL string
     * @param parameters the parameters
     */
    public void prepareThrowsSQLException(String sql, List parameters)
    {
        Map params = new HashMap();
        for(int ii = 0; ii < parameters.size(); ii++)
        {
            params.put(new Integer(ii + 1), parameters.get(ii));
        }
        prepareThrowsSQLException(sql, params);
    }
    
    /**
     * Prepare if the specified SQL string with the specified parameters
     * should raise an exception.
     * This can be used to simulate database exceptions.
     * @param sql the SQL string
     * @param parameters the parameters
     */
    public void prepareThrowsSQLException(String sql, Map parameters)
    {
        List list = (List)throwsSQLException.get(sql);
        if(null == list)
        {
            list = new ArrayList();
            throwsSQLException.put(sql, list);
        }
        list.add(parameters);
    }

    /**
     * Prepare the update count for execute update calls for a specified SQL string
     * and the specified parameters. The specified parameters array
     * must contain the parameters in the correct order starting with 0 as
     * the first parameter. Please keep in mind that parameters in
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters[0]</code> maps to the
     * parameter with index 1.
     * @param sql the SQL string
     * @param updateCount the update count
     * @param parameters the parameters
     */
    public void prepareUpdateCount(String sql, int updateCount, Object[] parameters)
    {
        prepareUpdateCount(sql, updateCount, ArrayUtil.getListFromObjectArray(parameters));
    }

    /**
     * Prepare the update count for execute update calls for a specified SQL string
     * and the specified parameters. The specified parameters <code>List</code>
     * must contain the parameters in the correct order starting with 0 as
     * the first parameter. Please keep in mind that parameters in
     * <code>PreparedStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the
     * parameter with index 1.
     * @param sql the SQL string
     * @param updateCount the update count
     * @param parameters the parameters
     */
    public void prepareUpdateCount(String sql, int updateCount, List parameters)
    {
        Map params = new HashMap();
        for(int ii = 0; ii < parameters.size(); ii++)
        {
            params.put(new Integer(ii + 1), parameters.get(ii));
        }
        prepareUpdateCount(sql, updateCount,  params);
    }
    
    /**
     * Prepare the update count for execute update calls for a specified SQL string
     * and the specified parameters. The specified parameters <code>Map</code>
     * must contain the parameters by mapping <code>Integer</code> objects
     * to the corresponding parameter. The <code>Integer</code> object
     * is the index of the parameter. In the case of a <code>CallableStatement</code>
     * there are also allowed <code>String</code> keys for named parameters.
     * @param sql the SQL string
     * @param updateCount the update count
     * @param parameters the parameters
     */
    public void prepareUpdateCount(String sql, int updateCount, Map parameters)
    {
        List list = (List)updateCountForStatement.get(sql);
        if(null == list)
        {
            list = new ArrayList();
            updateCountForStatement.put(sql, list);
        }
        list.add(new MockUpdateCountWrapper(updateCount, parameters));
    }
    
    private class MockResultSetWrapper
    {
        private MockResultSet resultSet;
        private Map parameters;
    
        public MockResultSetWrapper(MockResultSet resultSet, Map parameters)
        {
            this.resultSet = resultSet;
            this.parameters = parameters;
        }
    
        public Map getParamters()
        {
            return parameters;
        }

        public MockResultSet getResultSet()
        {
            return resultSet;
        }
    }

    private class MockUpdateCountWrapper
    {
        private Integer updateCount;
        private Map parameters;

        public MockUpdateCountWrapper(int updateCount, Map parameters)
        {
            this.updateCount = new Integer(updateCount);
            this.parameters = parameters;
        }

        public Map getParamters()
        {
            return parameters;
        }

        public Integer getUpdateCount()
        {
            return updateCount;
        }
    }
}
