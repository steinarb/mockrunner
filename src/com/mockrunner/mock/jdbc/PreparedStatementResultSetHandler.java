package com.mockrunner.mock.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.util.ArrayUtil;
import com.mockrunner.util.SearchUtil;

/**
 * Concrete handler for {@link MockPreparedStatement}.
 */
public class PreparedStatementResultSetHandler extends AbstractResultSetHandler
{
    private boolean exactMatchParameter;
    private List preparedStatements;
    private Map preparedStatementMap;
    private Map resultSetsForStatement;
    private Map updateCountForStatement;
    
    public PreparedStatementResultSetHandler()
    {
        preparedStatements = new ArrayList();
        preparedStatementMap = new HashMap();
        resultSetsForStatement = new HashMap();
        updateCountForStatement = new HashMap();
        exactMatchParameter = false;
    }
    
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
     * The <code>Connection</code> adds new statements with
     * this method.
     * @param statement the {@link MockPreparedStatement}
     */
    public void addPreparedStatement(MockPreparedStatement statement)
    { 
        statement.setPreparedStatementResultSetHandler(this);
        List list = (List)preparedStatementMap.get(statement.getSQL());
        if(null == list)
        {
            list = new ArrayList();
            preparedStatementMap.put(statement.getSQL(), list);
        }
        list.add(statement);
        preparedStatements.add(statement);
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
        List list = SearchUtil.getMatchingObjects(updateCountForStatement, sql, getCaseSensitive(), getExactMatch());
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
        List list = SearchUtil.getMatchingObjects(resultSetsForStatement, sql, getCaseSensitive(), getExactMatch());
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
    
    private boolean doParameterMatch(List actualParameters, List expectedParameters)
    {
        if(exactMatchParameter)
        {
            if(actualParameters.size() != expectedParameters.size()) return false;
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
     * Returns a <code>List</code> of all prepared statements.
     * @return the <code>List</code> of {@link MockPreparedStatement} objects
     */
    public List getPreparedStatements()
    {
        return Collections.unmodifiableList(preparedStatements);
    }
    
    /**
     * Returns a <code>Map</code> of all prepared statements.
     * The SQL strings map to the corresponding {@link MockPreparedStatement}
     * object.
     * @return the <code>Map</code> of {@link MockPreparedStatement} objects
     */
    public Map getPreparedStatementMap()
    {
        return Collections.unmodifiableMap(preparedStatementMap);
    }

    /**
     * Clears all prepared statements
     */
    public void clearPreparedStatements()
    {
        preparedStatements.clear();
        preparedStatementMap.clear();
    }

    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string and
     * the specified parameters.
     * @param sql the SQL string
     * @param paramters the parameters
     * @param resultSet the corresponding {@link MockResultSet}
     */
    public void prepareResultSet(String sql, MockResultSet resultSet, Object[] paramters)
    {
        prepareResultSet(sql, resultSet, ArrayUtil.getListFromObjectArray(paramters));
    }
    
    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string and
     * the specified parameters.
     * @param sql the SQL string
     * @param paramters the parameters
     * @param resultSet the corresponding {@link MockResultSet}
     */
    public void prepareResultSet(String sql, MockResultSet resultSet, List paramters)
    {
        List list = (List)resultSetsForStatement.get(sql);
        if(null == list)
        {
            list = new ArrayList();
            resultSetsForStatement.put(sql, list);
        }
        list.add(new MockResultSetWrapper(resultSet, paramters));
    }
    
    /**
     * Prepare the update count for execute update calls for a specified SQL string
     * and the specified parameters.
     * @param sql the SQL string
     * @param updateCount the update count
     * @param paramters the parameters
     */
    public void prepareUpdateCount(String sql, int updateCount, Object[] paramters)
    {
        prepareUpdateCount(sql, updateCount, ArrayUtil.getListFromObjectArray(paramters));
    }
    
    /**
     * Prepare the update count for execute update calls for a specified SQL string
     * and the specified parameters.
     * @param sql the SQL string
     * @param updateCount the update count
     * @param paramters the parameters
     */
    public void prepareUpdateCount(String sql, int updateCount, List paramters)
    {
        List list = (List)updateCountForStatement.get(sql);
        if(null == list)
        {
            list = new ArrayList();
            updateCountForStatement.put(sql, list);
        }
        list.add(new MockUpdateCountWrapper(updateCount, paramters));
    }
    
    private class MockResultSetWrapper
    {
        private MockResultSet resultSet;
        private List paramters;
        
        public MockResultSetWrapper(MockResultSet resultSet, List paramters)
        {
            this.resultSet = resultSet;
            this.paramters = paramters;
        }
        
        public List getParamters()
        {
            return paramters;
        }

        public MockResultSet getResultSet()
        {
            return resultSet;
        }
    }
    
    private class MockUpdateCountWrapper
    {
        private Integer updateCount;
        private List paramters;
    
        public MockUpdateCountWrapper(int updateCount, List paramters)
        {
            this.updateCount = new Integer(updateCount);
            this.paramters = paramters;
        }
    
        public List getParamters()
        {
            return paramters;
        }

        public Integer getUpdateCount()
        {
            return updateCount;
        }
    }
}
