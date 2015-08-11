package com.mockrunner.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mockrunner.mock.jdbc.MockPreparedStatement;

/**
 * Concrete handler for {@link MockPreparedStatement}.
 */
public class PreparedStatementResultSetHandler extends AbstractParameterResultSetHandler
{ 
    private final List<MockPreparedStatement> preparedStatements;
    private final Map<String, List<MockPreparedStatement>> preparedStatementMap;
    
    public PreparedStatementResultSetHandler()
    {
        preparedStatements = new ArrayList<MockPreparedStatement>();
        preparedStatementMap = new TreeMap<String, List<MockPreparedStatement>>();
    }
    
    /**
     * The <code>Connection</code> adds new statements with
     * this method.
     * @param statement the {@link MockPreparedStatement}
     */
    public void addPreparedStatement(MockPreparedStatement statement)
    { 
        statement.setPreparedStatementResultSetHandler(this);
        List<MockPreparedStatement> list = preparedStatementMap.get(statement.getSQL());
        if(null == list)
        {
            list = new ArrayList<MockPreparedStatement>();
            preparedStatementMap.put(statement.getSQL(), list);
        }
        list.add(statement);
        preparedStatements.add(statement);
    }
    
    /**
     * Returns a <code>List</code> of all prepared statements.
     * @return the <code>List</code> of {@link MockPreparedStatement} objects
     */
    public List<MockPreparedStatement> getPreparedStatements()
    {
        return Collections.unmodifiableList(preparedStatements);
    }
    
    /**
     * Returns a <code>Map</code> of all prepared statements.
     * The SQL strings map to the corresponding {@link MockPreparedStatement}
     * object.
     * @return the <code>Map</code> of {@link MockPreparedStatement} objects
     */
    public Map<String, List<MockPreparedStatement>> getPreparedStatementMap()
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
