package com.mockrunner.mock.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreparedStatementHandler
{
    private List preparedStatements;
    private Map preparedStatementMap;
    
    public PreparedStatementHandler()
    {
        preparedStatements = new ArrayList();
        preparedStatementMap = new HashMap();
    }
    
    public void addPreparedStatement(MockPreparedStatement statement)
    {
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
}
