package com.mockrunner.mock.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.util.ArrayUtil;

/**
 * Concrete handler for {@link MockPreparedStatement}.
 */
public class PreparedStatementResultSetHandler extends AbstractResultSetHandler
{
    private List preparedStatements;
    private Map preparedStatementMap;
    
    public PreparedStatementResultSetHandler()
    {
        preparedStatements = new ArrayList();
        preparedStatementMap = new HashMap();
    }
    
    public void addPreparedStatement(MockPreparedStatement statement)
    { 
        //TODO: set result sets with parameters to PreparedStatement
        //order: index, than SQL
        MockResultSet resultSet = getResultSet(preparedStatements.size());
        if(null == resultSet)
        {
            resultSet = getResultSet(statement.getSQL());
        }
        if(null == resultSet)
        {
            resultSet = getGlobalResultSet();
        }
        statement.setResultSet(resultSet);
        List list = (List)preparedStatementMap.get(statement.getSQL());
        if(null == list)
        {
            list = new ArrayList();
            preparedStatementMap.put(statement.getSQL(), list);
        }
        list.add(statement);
        preparedStatements.add(statement);
    }
    
    public List getPreparedStatements()
    {
        return Collections.unmodifiableList(preparedStatements);
    }

    public void clearPreparedStatements()
    {
        preparedStatements.clear();
        preparedStatementMap.clear();
    }
  
    public Map getPreparedStatementMap()
    {
        return Collections.unmodifiableMap(preparedStatementMap);
    }

    public void prepareResultSet(String sql, MockResultSet resultSet, Object[] params)
    {
        prepareResultSet(sql, resultSet, ArrayUtil.getListFromObjectArray(params));
    }
    
    public void prepareResultSet(String sql, MockResultSet resultSet, List params)
    {
    
    }

    public void prepareResultSet(int index, MockResultSet resultSet, Object[] params)
    {
        prepareResultSet(index, resultSet, ArrayUtil.getListFromObjectArray(params));
    }
    
    public void prepareResultSet(int index, MockResultSet resultSet, List params)
    {

    }
}
