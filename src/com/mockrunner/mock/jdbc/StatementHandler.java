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
     * Clears all specified <code>ResultSet</code> objects.
     */
    public void clearResultSets()
    {
        resultSetsForStatement.clear();
        resultSetsForStatementMap.clear();
    }
    
    /**
     * Returns the <code>Map</code> of all <code>ResultSet</code>
     * objects, that were added with {@link #prepareResultSet(String, MockResultSet)}.
     * The SQL strings map to the corresponding <code>ResultSet</code>.
     * @return the <code>Map</code> of <code>ResultSet</code> objects
     */
    public Map getResultSetMap()
    {
        return Collections.unmodifiableMap(resultSetsForStatementMap);
    }
    
    /**
     * Returns the <code>List</code> of all <code>ResultSet</code>
     * objects, that were added with {@link #prepareResultSet(int, MockResultSet)}
     * resp. {@link #prepareResultSet(MockResultSet)}.
     * @return the <code>List</code> of <code>ResultSet</code> objects
     */
    public List getResultSets()
    {
        return Collections.unmodifiableList(resultSetsForStatement);
    }
    
    /**
     * Prepare a <code>ResultSet</code> for a specified SQL string.
     * If a statement does not have a unique <code>ResultSet</code>
     * set with {@link #prepareResultSet(int, MockResultSet)} or
     * {@link #prepareResultSet(MockResultSet)}, it
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
     * Prepares a <code>ResultSet</code> for all statements.
     * If a statement has a <code>ResultSet</code> specified with
     * this method, it always returns that, regardless of any
     * SQL strings.
     * @param resultSet the {@link MockResultSet}
     */
    public void prepareResultSet(MockResultSet resultSet)
    {
        resultSetsForStatement.clear();
        for(int ii = 0; ii < statements.size(); ii++)
        {
            resultSetsForStatement.add(resultSet);
        }
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
    
    /**
     * Resets the <code>ResultSet</code> for a specified index.
     * @param index the index of the statement
     */
    public void resetResultSet(int index)
    {
        if(index < resultSetsForStatement.size())
        {
            resultSetsForStatement.set(index, null);
        }
    }
}
