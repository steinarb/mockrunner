package com.mockrunner.jdbc;

import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mockrunner.base.MockObjectFactory;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.jdbc.MockPreparedStatement;

/**
 * Module for JDBC tests. Not complete yet.
 */
public class JDBCTestModule
{
    private MockObjectFactory mockFactory;
    private boolean caseSensitive = false;
    private boolean exactMatch = false;
      
    public JDBCTestModule(MockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }
    
    /**
     * Set if specified SQL statements should be handled case sensitive.
     * Defaults to to <code>false</code>, i.e. <i>INSERT</i> is the same
     * as <i>insert</i>.
     * @param caseSensitive enable or disable case sensitivity
     */
    public void setCaseSensitive(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }
    
    /**
     * Set if specified SQL statements must match exactly.
     * Defaults to <code>false</code>, i.e. the SQL statement used
     * to create a <code>PreparedStatement</code> must contain
     * the specified SQL statement, but does not have to match
     * exactly. If the original statement is <i>insert into mytable values(?, ?, ?)</i>
     * <code>verifyPreparedStatementPresent("insert into mytable")</code>
     * will pass.
     * @param caseSensitive enable or disable exact matching
     */
    public void setExactMatch(boolean exactMatch)
    {
        this.exactMatch = exactMatch;
    }
    
    /**
     * Returns a {@link com.mockrunner.mock.jdbc.MockPreparedStatement} that was 
     * created using a {@link com.mockrunner.mock.jdbc.MockConnection}.
     * If there are more than one {@link com.mockrunner.mock.jdbc.MockPreparedStatement}
     * objects with the specified SQL, the first one will be returned.
     * @param sql the SQL statement used to create the <code>PreparedStatement</code>
     * @return the <code>PreparedStatement</code>
     */
    public MockPreparedStatement getPreparedStatement(String sql)
    {
        List list = getPreparedStatementList(sql);
        if(null != list && list.size() > 0)
        {
            return (MockPreparedStatement)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns all {@link com.mockrunner.mock.jdbc.MockPreparedStatement} that were 
     * created using a {@link com.mockrunner.mock.jdbc.MockConnection} as a <code>List</code>.
     * @param sql the SQL statement used to create the <code>PreparedStatement</code>
     * @return the <code>List</code> of <code>PreparedStatement</code> objects
     */
    public List getPreparedStatementList(String sql)
    {
        Map sqlStatements = mockFactory.getMockConnection().getPreparedStatementMap();
        Iterator iterator = sqlStatements.keySet().iterator();
        while(iterator.hasNext())
        {
            String nextStatement = (String)iterator.next();
            if(doesPreparedStatementMatch(sql, nextStatement))
            {
                return (List)sqlStatements.get(nextStatement);
            } 
        }
        return null;
    }
    
    /**
     * Returns an object that was added to a <code>PreparedStatement</code>
     * using its <code>set</code> methods.
     * @param statement the <code>PreparedStatement</code>
     * @param index the index used to set the object
     * @return the corresponding object
     */
    public Object getPreparedStatementObject(PreparedStatement statement, int index)
    {
        return ((MockPreparedStatement)statement).getObject(index);
    }
    
    /**
     * Returns an object that was added to a <code>PreparedStatement</code>
     * using its <code>set</code> methods. Uses the first <code>PreparedStatement</code>
     * with the specified SQL statement.
     * @param sql the SQL statement
     * @param index the index used to set the object
     * @return the corresponding object
     */
    public Object getPreparedStatementObject(String sql, int index)
    {
        return getPreparedStatement(sql).getObject(index);
    }
    
    /**
     * Verifies that a <code>PreparedStatement</code> with the specified 
     * SQL statement is present.
     * @param sql the SQL statement
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementPresent(String sql)
    {
        if(null == getPreparedStatement(sql))
        {
            throw new VerifyFailedException("Prepared statement with SQL " +  sql + " not present.");
        }
    }
    
    /**
     * Verifies that an object was added to a <code>PreparedStatement</code> with
     * the specified index.
     * @param statement the <code>PreparedStatement</code>
     * @param index the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObjectPresent(PreparedStatement statement, int index)
    {
        if(null == getPreparedStatementObject(statement, index))
        {
            throw new VerifyFailedException("Prepared statement object with index " + index + " not present.");
        }
    }

    /**
     * Verifies that an object was added to a <code>PreparedStatement</code> with
     * the specified index. Uses the first <code>PreparedStatement</code> with
     * the specified SQL.
     * @param sql the SQL statement of the <code>PreparedStatement</code>
     * @param index the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObjectPresent(String sql, int index)
    {
        if(null == getPreparedStatementObject(sql, index))
        {
            throw new VerifyFailedException("Prepared statement object with index " + index + " not present.");
        }
    }
    
    /**
     * Verifies that an object from the specified <code>PreparedStatement</code> is equal
     * to the specified object.
     * @param statement the <code>PreparedStatement</code>
     * @param index the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObject(PreparedStatement statement, int index, Object object)
    {
        verifyPreparedStatementObjectPresent(statement, index);
        Object actualObject = getPreparedStatementObject(statement, index);
        if(!actualObject.equals(object))
        {
            throw new VerifyFailedException("Prepared statement object with index " + index + " not equal with specified object.");
        }
    }
    
    /**
     * Verifies that an object from the <code>PreparedStatement</code> with the
     * specified SQL statement is equal to the specified object.
     * Uses the first <code>PreparedStatement</code> with the specified SQL.
     * @param sql the SQL statement of the <code>PreparedStatement</code>
     * @param index the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObject(String sql, int index, Object object)
    {
        verifyPreparedStatementObjectPresent(sql, index);
        Object actualObject = getPreparedStatementObject(sql, index);
        if(!actualObject.equals(object))
        {
            throw new VerifyFailedException("Prepared statement object with index " + index + " not equal with specified object.");
        }
    }
    
    private boolean doesPreparedStatementMatch(String sql, String statement)
    {
        if(!caseSensitive)
        {
            statement = statement.toLowerCase();
            sql = sql.toLowerCase();
        }
        if(exactMatch)
        {
            if(sql.equals(statement)) return true;
        }
        else
        {
            if(-1 != statement.indexOf(sql)) return true;
        }
        return false;
    }
}
