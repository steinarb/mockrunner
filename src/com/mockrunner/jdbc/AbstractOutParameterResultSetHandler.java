package com.mockrunner.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.util.ArrayUtil;

/**
 * Abstract base class for all statement types
 * that support out parameters, i.e. <code>CallableStatement</code>.
 */
public abstract class AbstractOutParameterResultSetHandler extends AbstractParameterResultSetHandler
{
    private Map globalOutParameter;
    private Map outParameterForStatement = new HashMap();
    private Map outParameterForStatementParameters = new HashMap();
    
    /**
     * Returns the first out parameter <code>Map</code> that matches 
     * the specified SQL string. Please note that you can modify
     * the search parameters with {@link #setCaseSensitive} and 
     * {@link #setExactMatch}.
     * @param sql the SQL string
     * @return the corresponding out parameter <code>Map</code>
     */
    public Map getOutParameter(String sql)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpression());
        List list = matcher.getMatchingObjects(outParameterForStatement, sql, true, true);
        if(null != list && list.size() > 0)
        {
            return (Map)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns the first out parameter <code>Map</code> that matches 
     * the specified SQL string and the specified parameters. 
     * Please note that you can modify the search parameters for 
     * the SQL string with {@link #setCaseSensitive} and
     * {@link #setExactMatch} and the search parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return the corresponding out parameter <code>Map</code>
     */
    public Map getOutParameter(String sql, Map parameters)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpression());
        List list = matcher.getMatchingObjects(outParameterForStatementParameters, sql, true, true);
        for(int ii = 0; ii < list.size(); ii++)
        {
            MockOutParameterWrapper wrapper = (MockOutParameterWrapper)list.get(ii);
            if(doParameterMatch(wrapper.getParamters(), parameters))
            {
                return wrapper.getOutParameter();
            }
        }
        return null;
    }
    
    /**
     * Clears the out parameters.
     */
    public void clearOutParameter()
    {
        outParameterForStatement.clear();
        outParameterForStatementParameters.clear();
    }
    
    /**
     * Returns the global out parameter <code>Map</code>.
     * The statement takes the global global out parameter 
     * <code>Map</code> if no out parameter <code>Map</code> 
     * can be found for the current SQL string.
     * @return the global out parameter <code>Map</code>
     */
    public Map getGlobalOutParameter()
    {
        return globalOutParameter;
    }
    
    /**
     * Prepares the global out parameter <code>Map</code>.
     * The statement takes the global global out parameter 
     * <code>Map</code> if no out parameter <code>Map</code> 
     * can be found for the current SQL string.
     * @param outParameters the global out parameter <code>Map</code>
     */
    public void prepareGlobalOutParameter(Map outParameters)
    {
        globalOutParameter = new HashMap(outParameters);
    }
    
    /**
     * Prepare an out parameter <code>Map</code> for a specified 
     * SQL string.
     * @param sql the SQL string
     * @param outParameters the out parameter <code>Map</code>
     */
    public void prepareOutParameter(String sql, Map outParameters)
    {
        outParameterForStatement.put(sql, new HashMap(outParameters));
    }
    
    /**
     * Prepare an out parameter <code>Map</code> for a specified SQL string and
     * the specified parameters. The specified parameters array
     * must contain the parameters in the correct order starting with 0 as
     * the first parameter. Please keep in mind that parameters in
     * <code>CallableStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters[0]</code> maps to the
     * parameter with index 1.
     * @param sql the SQL string
     * @param outParameters the corresponding out parameter <code>Map</code>
     * @param parameters the parameters
     */
    public void prepareOutParameter(String sql, Map outParameters, Object[] parameters)
    {
        prepareOutParameter(sql, outParameters, ArrayUtil.getListFromObjectArray(parameters));
    }
    
    /**
     * Prepare an out parameter <code>Map</code> for a specified SQL string and
     * the specified parameters. The specified parameters array
     * must contain the parameters in the correct order starting with 0 as
     * the first parameter. Please keep in mind that parameters in
     * <code>CallableStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the
     * parameter with index 1.
     * @param sql the SQL string
     * @param outParameters the corresponding out parameter <code>Map</code>
     * @param parameters the parameters
     */
    public void prepareOutParameter(String sql, Map outParameters, List parameters)
    {
        Map params = new HashMap();
        for(int ii = 0; ii < parameters.size(); ii++)
        {
            params.put(new Integer(ii + 1), parameters.get(ii));
        }
        prepareOutParameter(sql, outParameters,  params);
    }
    
    /**
     * Prepare an out parameter <code>Map</code> for a specified SQL string
     * and the specified parameters. The specified parameters <code>Map</code>
     * must contain the parameters by mapping <code>Integer</code> or
     * <code>String</code> objects to the corresponding parameter. 
     * An <code>Integer</code> object is the index of the parameter.
     * A <code>String</code> is the name of the parameter.
     * @param sql the SQL string
     * @param outParameters the corresponding out parameter <code>Map</code>
     * @param parameters the parameters
     */
    public void prepareOutParameter(String sql, Map outParameters, Map parameters)
    {
        List list = (List)outParameterForStatementParameters.get(sql);
        if(null == list)
        {
            list = new ArrayList();
            outParameterForStatementParameters.put(sql, list);
        }
        list.add(new MockOutParameterWrapper(new HashMap(outParameters), parameters));
    }
    
    private class MockOutParameterWrapper
    {
        private Map outParameter;
        private Map parameters;

        public MockOutParameterWrapper(Map outParameter, Map parameters)
        {
            this.outParameter = outParameter;
            this.parameters = parameters;
        }

        public Map getParamters()
        {
            return parameters;
        }

        public Map getOutParameter()
        {
            return outParameter;
        }
    }
}
