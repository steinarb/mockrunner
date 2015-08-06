package com.mockrunner.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mockrunner.mock.jdbc.MockCallableStatement;

/**
 * Concrete handler for {@link MockCallableStatement}.
 */
public class CallableStatementResultSetHandler extends AbstractOutParameterResultSetHandler
{
    private final List<MockCallableStatement> callableStatements;
    private final Map<String, List<MockCallableStatement>> callableStatementMap;

    public CallableStatementResultSetHandler()
    {
        callableStatements = new ArrayList();
        callableStatementMap = new TreeMap();
    }

    /**
     * The <code>Connection</code> adds new statements with
     * this method.
     * @param statement the {@link MockCallableStatement}
     */
    public void addCallableStatement(MockCallableStatement statement)
    { 
        statement.setCallableStatementResultSetHandler(this);
        List<MockCallableStatement> list = (List)callableStatementMap.get(statement.getSQL());
        if(null == list)
        {
            list = new ArrayList<MockCallableStatement>();
            callableStatementMap.put(statement.getSQL(), list);
        }
        list.add(statement);
        callableStatements.add(statement);
    }

    /**
     * Returns a <code>List</code> of all callable statements.
     * @return the <code>List</code> of {@link MockCallableStatement} objects
     */
    public List<MockCallableStatement> getCallableStatements()
    {
        return Collections.unmodifiableList(callableStatements);
    }

    /**
     * Returns a <code>Map</code> of all callable statements.
     * The SQL strings map to the corresponding {@link MockCallableStatement}
     * object.
     * @return the <code>Map</code> of {@link MockCallableStatement} objects
     */
    public Map<String, List<MockCallableStatement>> getCallableStatementMap()
    {
        return Collections.unmodifiableMap(callableStatementMap);
    }

    /**
     * Clears all callable statements
     */
    public void clearCallableStatements()
    {
        callableStatements.clear();
        callableStatementMap.clear();
    }   
}
