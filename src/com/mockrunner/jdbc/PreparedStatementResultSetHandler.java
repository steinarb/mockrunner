package com.mockrunner.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.mock.jdbc.MockPreparedStatement;

/**
 * Concrete handler for {@link MockPreparedStatement}.
 */
public class PreparedStatementResultSetHandler extends AbstractParameterResultSetHandler
{ 
    private List preparedStatements;
    private Map preparedStatementMap;
    
    public PreparedStatementResultSetHandler()
    {
        preparedStatements = new ArrayList();
        preparedStatementMap = new HashMap();
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
}
