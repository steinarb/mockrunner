package com.mockrunner.mock.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockrunner.util.CollectionUtil;

/**
 * Internal helper class to handle the <code>Statement</code> objects
 * from a <code>Connection</code> and to coordinate the corresponding
 * <code>ResultSet</code> objects. Usually you do not have to deal with 
 * that class.
 */
public class StatementHandler
{
    private List resultSetsForStatement;
    private Map resultSetsForStatementMap;
    private List statements;
    
    public StatementHandler()
    {
        resultSetsForStatement = new ArrayList();
        resultSetsForStatementMap = new HashMap();
        statements = new ArrayList();
    }
    
    /**
     * The <code>Connection</code> adds new statements with
     * this method. If a <code>ResultSet</code> is present
     * for this method, it will be set automatically.
     * @param statement the {@link MockStatement}
     */
    public void addStatement(MockStatement statement)
    {
        if(statements.size() < resultSetsForStatement.size())
        {
            statement.setResultSet((MockResultSet)resultSetsForStatement.get(statements.size()));
        }
        statements.add(statement);
    }
    
    /**
     * Returns a <code>List</code> of all statements.
     * @return the <code>List</code> of {@link MockStatement} objects
     */
    public List getStatements()
    {
        return Collections.unmodifiableList(statements);
    }

    /**
     * Clears the <code>List</code> of statements.
     */
    public void clearStatements()
    {
        statements.clear();
    }
    
    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string.
     * If a statement does not have a unique <code>ResultSet</code>
     * set with {@link #prepareResultSet(int, MockResultSet)}, it
     * iterates through the <code>Map</code> of <code>ResultSet</code>
     * objects and returns the one that matches the specifed SQL.
     * @param sql the SQL string
     * @param resultSet the corresponding {@link MockResultSet}
     */
    public void prepareResultSet(String sql, MockResultSet resultSet)
    {
        resultSetsForStatementMap.put(sql, resultSet);
    }
    
    /**
     * Prepare a <code>ResultSet</code> for a specified index.
     * If a statement has a <code>ResultSet</code> specified with
     * this method, it always returns that, regardless of any
     * SQL strings.
     * @param index the index of the statement
     * @param resultSet the corresponding {@link MockResultSet}
     */
    public void prepareResultSet(int index, MockResultSet resultSet)
    {
        if(resultSetsForStatement.size() < index + 1)
        {
            CollectionUtil.fillList(resultSetsForStatement, index + 1);
        }
        resultSetsForStatement.set(index, resultSet);
    }
}
