package com.mockrunner.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.mock.jdbc.MockCallableStatement;

/**
 * Concrete handler for {@link MockCallableStatement}.
 */
public class CallableStatementResultSetHandler extends AbstractOutParameterResultSetHandler
{
    private List callableStatements;
    private Map callbaleStatementMap;

    public CallableStatementResultSetHandler()
    {
        callableStatements = new ArrayList();
        callbaleStatementMap = new HashMap();
    }

    /**
     * The <code>Connection</code> adds new statements with
     * this method.
     * @param statement the {@link MockCallableStatement}
     */
    public void addCallableStatement(MockCallableStatement statement)
    { 
        statement.setCallableStatementResultSetHandler(this);
        List list = (List)callbaleStatementMap.get(statement.getSQL());
        if(null == list)
        {
            list = new ArrayList();
            callbaleStatementMap.put(statement.getSQL(), list);
        }
        list.add(statement);
        callableStatements.add(statement);
    }

    /**
     * Returns a <code>List</code> of all callable statements.
     * @return the <code>List</code> of {@link MockCallableStatement} objects
     */
    public List getCallableStatements()
    {
        return Collections.unmodifiableList(callableStatements);
    }

    /**
     * Returns a <code>Map</code> of all callable statements.
     * The SQL strings map to the corresponding {@link MockCallableStatement}
     * object.
     * @return the <code>Map</code> of {@link MockCallableStatement} objects
     */
    public Map getCallableStatementMap()
    {
        return Collections.unmodifiableMap(callbaleStatementMap);
    }

    /**
     * Clears all callable statements
     */
    public void clearCallableStatements()
    {
        callableStatements.clear();
        callbaleStatementMap.clear();
    }   
}
