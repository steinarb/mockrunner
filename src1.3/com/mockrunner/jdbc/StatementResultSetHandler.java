package com.mockrunner.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mockrunner.mock.jdbc.MockStatement;

/**
 * Concrete handler for {@link AbstractResultSetHandler}.
 */
public class StatementResultSetHandler extends AbstractResultSetHandler
{
    private List statements;

    public StatementResultSetHandler()
    {
        statements = new ArrayList();
    }  
    
    /**
     * The <code>Connection</code> adds new statements with
     * this method.
     * @param statement the {@link MockStatement}
     */
    public void addStatement(MockStatement statement)
    {
        statement.setResultSetHandler(this);
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
}
