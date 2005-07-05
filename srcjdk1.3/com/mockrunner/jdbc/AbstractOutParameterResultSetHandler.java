package com.mockrunner.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.util.common.ArrayUtil;

/**
 * Abstract base class for all statement types
 * that support out parameters, i.e. <code>CallableStatement</code>.
 */
public abstract class AbstractOutParameterResultSetHandler extends AbstractParameterResultSetHandler
{
    private boolean mustRegisterOutParameters = false;
    private Map globalOutParameter = null;
    private Map outParameterForStatement = new HashMap();
    private Map outParameterForStatementParameters = new HashMap();
    
    /**
     * Set if out parameters must be registered to be returned.
     * The default is <code>false</code>, i.e. if there are matching
     * out parameters prepared, they are returned even if the
     * <code>registerOutParameter</code> methods of <code>CallableStatement</code>
     * have not been called. If set to <code>true</code>, <code>registerOutParameter</code>
     * must be called.
     * @param mustOutParameterBeRegistered must out parameter be registered
     */
    public void setMustRegisterOutParameters(boolean mustOutParameterBeRegistered)
    {
        this.mustRegisterOutParameters = mustOutParameterBeRegistered;
    }
    
    /**
     * Get if out parameter must be registered to be returned.
     * @return must out parameter be registered
     */
    public boolean getMustRegisterOutParameters()
    {
        return mustRegisterOutParameters;
    }
    
    /**
     * Returns the first out parameter <code>Map</code> that matches 
     * the specified SQL string.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions}.
     * @param sql the SQL string
     * @return the corresponding out parameter <code>Map</code>
     */
    public Map getOutParameter(String sql)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
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
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
     * @param sql the SQL string
     * @param parameters the parameters
     * @return the corresponding out parameter <code>Map</code>
     */
    public Map getOutParameter(String sql, Map parameters)
    {
        SQLStatementMatcher matcher = new SQLStatementMatcher(getCaseSensitive(), getExactMatch(), getUseRegularExpressions());
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
     * @return the global out parameter <code>Map</code>
     */
    public Map getGlobalOutParameter()
    {
        return globalOutParameter;
    }
    
    /**
     * Prepares the global out parameter <code>Map</code>.
     * @param outParameters the global out parameter <code>Map</code>
     */
    public void prepareGlobalOutParameter(Map outParameters)
    {
        globalOutParameter = new HashMap(outParameters);
    }
    
    /**
     * Prepare an out parameter <code>Map</code> for a specified 
     * SQL string.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions}.
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
     * must contain the parameters in the correct order starting with index 0 for
     * the first parameter. Please keep in mind that parameters in
     * <code>CallableStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters[0]</code> maps to the
     * parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
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
     * must contain the parameters in the correct order starting with index 0 for
     * the first parameter. Please keep in mind that parameters in
     * <code>CallableStatement</code> objects start with 1 as the first
     * parameter. So <code>parameters.get(0)</code> maps to the
     * parameter with index 1.
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
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
     * Please note that you can modify the match parameters with 
     * {@link #setCaseSensitive}, {@link #setExactMatch} and 
     * {@link #setUseRegularExpressions} and the match parameters for the 
     * specified parameter list with {@link #setExactMatchParameter}.
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
        list.add(new MockOutParameterWrapper(new HashMap(outParameters), new HashMap(parameters)));
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
