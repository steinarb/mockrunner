package com.mockrunner.mock.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.util.ArrayUtil;
import com.mockrunner.util.SearchUtil;

/**
 * Abstract base class for all statement types
 * that support parameters, i.e. <code>PreparedStatement</code>
 * and <code>CallableStatement</code>.
 */
abstract class AbstractParameterResultSetHandler extends AbstractResultSetHandler
{
    private boolean exactMatchParameter = false;
    private Map resultSetsForStatement = new HashMap();
    private Map updateCountForStatement = new HashMap();
    
    /**
     * Sets if the specified parameters must match exactly
     * in order and number.
     * Defaults to <code>false</code>, i.e. the spcified
     * parameters must be present in the actual parameter
     * list of the prepared statement, but they do not have
     * to match in order and number.
     * @param exactMatchParameter must parameters match exactly
     */
    public void setExactMatchParameter(boolean exactMatchParameter)
    {
        this.exactMatchParameter = exactMatchParameter;
    }

    /**
     * Returns the first update count that matches the
     * specified SQL string and the specified parameters. 
     * Please note that you can modify the search parameters with 
     * {@link #setCaseSensitive} and {@link #setExactMatch}. 
     * Returns <code>null</code> if no return value is present
     * for the specified SQL string.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return the corresponding update count
     */
    public Integer getUpdateCount(String sql, List parameters)
    {
        List list = SearchUtil.getMatchingObjects(updateCountForStatement, sql, getCaseSensitive(), getExactMatch(), true);
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
    public MockResultSet getResultSet(String sql, List parameters)
    {
        List list = SearchUtil.getMatchingObjects(resultSetsForStatement, sql, getCaseSensitive(), getExactMatch(), true);
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

    private boolean doParameterMatch(List expectedParameters, List actualParameters)
    {
        if(exactMatchParameter)
        {
            if(actualParameters.size() != expectedParameters.size() + 1) return false;
            return (-1 != Collections.indexOfSubList(actualParameters, expectedParameters));
        }
        else
        {
            for(int ii= 0; ii < expectedParameters.size(); ii++)
            {
                Object nextParameter = expectedParameters.get(ii);
                if(!actualParameters.contains(nextParameter)) return false;
            }
            return true;
        }
    }

    /**
     * Clears the <code>ResultSet</code> objects.
     */
    public void clearResultSets()
    {
        resultSetsForStatement.clear();
    }

    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string and
     * the specified parameters.
     * @param sql the SQL string
     * @param paramters the parameters
     * @param resultSet the corresponding {@link MockResultSet}
     */
    public void prepareResultSet(String sql, MockResultSet resultSet, Object[] parameters)
    {
        prepareResultSet(sql, resultSet, ArrayUtil.getListFromObjectArray(parameters));
    }

    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string and
     * the specified parameters.
     * @param sql the SQL string
     * @param paramters the parameters
     * @param resultSet the corresponding {@link MockResultSet}
     */
    public void prepareResultSet(String sql, MockResultSet resultSet, List parameters)
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
     * Prepare the update count for execute update calls for a specified SQL string
     * and the specified parameters.
     * @param sql the SQL string
     * @param updateCount the update count
     * @param paramters the parameters
     */
    public void prepareUpdateCount(String sql, int updateCount, Object[] parameters)
    {
        prepareUpdateCount(sql, updateCount, ArrayUtil.getListFromObjectArray(parameters));
    }

    /**
     * Prepare the update count for execute update calls for a specified SQL string
     * and the specified parameters.
     * @param sql the SQL string
     * @param updateCount the update count
     * @param paramters the parameters
     */
    public void prepareUpdateCount(String sql, int updateCount, List parameters)
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
        private List parameters;
    
        public MockResultSetWrapper(MockResultSet resultSet, List parameters)
        {
            this.resultSet = resultSet;
            this.parameters = parameters;
        }
    
        public List getParamters()
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
        private List parameters;

        public MockUpdateCountWrapper(int updateCount, List parameters)
        {
            this.updateCount = new Integer(updateCount);
            this.parameters = parameters;
        }

        public List getParamters()
        {
            return parameters;
        }

        public Integer getUpdateCount()
        {
            return updateCount;
        }
    }
}
