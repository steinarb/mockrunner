package com.mockrunner.jdbc;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import com.mockrunner.base.MockObjectFactory;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockStatement;
import com.mockrunner.util.SearchUtil;

/**
 * Module for JDBC tests. Not complete yet.
 * Keeps track of statements and prepared statements.
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
     * @param exactMatch enable or disable exact matching
     */
    public void setExactMatch(boolean exactMatch)
    {
        this.exactMatch = exactMatch;
    }
    
    /**
     * Returns a {@link com.mockrunner.mock.jdbc.MockStatement} by its index.
     * @param index the index of the <code>Statement</code>
     * @return the <code>Statement</code> or <code>null</code>, if there is no such
     *         <code>Statement</code>
     */
    public MockStatement getStatement(int index)
    {
        List statements = getStatements();
        if(index < statements.size()) return (MockStatement)statements.get(index);
        return null;
    }
    
    /**
     * Returns all {@link com.mockrunner.mock.jdbc.MockStatement}.
     * @return the <code>List</code> of <code>Statement</code> objects
     */
    public List getStatements()
    {
        return mockFactory.getMockConnection().getStatementResultSetHandler().getStatements();
    }
    
    /**
     * Returns a {@link com.mockrunner.mock.jdbc.MockPreparedStatement} that was 
     * created using a {@link com.mockrunner.mock.jdbc.MockConnection} by its index.
     * @param index the index of the <code>PreparedStatement</code>
     * @return the <code>PreparedStatement</code> or <code>null</code>, if there is no such
     *         <code>PreparedStatement</code>
     */
    public MockPreparedStatement getPreparedStatement(int index)
    {
        List statements = getPreparedStatements();
        if(index < statements.size()) return (MockPreparedStatement)statements.get(index);
        return null;
    }
    
    /**
     * Returns a {@link com.mockrunner.mock.jdbc.MockPreparedStatement} that was 
     * created using a {@link com.mockrunner.mock.jdbc.MockConnection} by its SQL statement.
     * If there are more than one {@link com.mockrunner.mock.jdbc.MockPreparedStatement}
     * objects with the specified SQL, the first one will be returned.
     * Please note that you can modify the search parameters with 
     * {@link #setCaseSensitive} and {@link #setExactMatch}.
     * @param sql the SQL statement used to create the <code>PreparedStatement</code>
     * @return the <code>PreparedStatement</code> or <code>null</code>, if there is no macth
     */
    public MockPreparedStatement getPreparedStatement(String sql)
    {
        List list = getPreparedStatements(sql);
        if(null != list && list.size() > 0)
        {
            return (MockPreparedStatement)list.get(0);
        }
        return null;
    }
    
    /**
     * Returns all {@link com.mockrunner.mock.jdbc.MockPreparedStatement}.
     * @return the <code>List</code> of <code>PreparedStatement</code> objects
     */
    public List getPreparedStatements()
    {
        return mockFactory.getMockConnection().getPreparedStatementResultSetHandler().getPreparedStatements();
    }
    
    /**
     * Returns all {@link com.mockrunner.mock.jdbc.MockPreparedStatement} with
     * the specified SQL statement as a <code>List</code>. If there are no matches, an empty
     * <code>List</code> will be returned. Please note that you can modify
     * the search parameters with {@link #setCaseSensitive} and {@link #setExactMatch}.
     * @param sql the SQL statement used to create the <code>PreparedStatement</code>
     * @return the <code>List</code> of <code>PreparedStatement</code> objects
     */
    public List getPreparedStatements(String sql)
    {
        Map sqlStatements = mockFactory.getMockConnection().getPreparedStatementResultSetHandler().getPreparedStatementMap();
        return SearchUtil.getMatchingObjects(sqlStatements, sql, caseSensitive, exactMatch); 
    }
    
    /**
     * Returns an object that was added to a <code>PreparedStatement</code>
     * using its <code>set</code> methods.
     * @param statement the <code>PreparedStatement</code>
     * @param indexOfObject the index used to set the object
     * @return the corresponding object
     */
    public Object getPreparedStatementObject(PreparedStatement statement, int indexOfObject)
    {
        if(null == statement) return null;
        return ((MockPreparedStatement)statement).getObject(indexOfObject);
    }
    
    /**
     * Returns an object that was added to a <code>PreparedStatement</code>
     * using its <code>set</code> methods. Uses the first <code>PreparedStatement</code>
     * with the specified SQL statement.
     * @param sql the SQL statement
     * @param indexOfObject the index used to set the object
     * @return the corresponding object
     */
    public Object getPreparedStatementObject(String sql, int indexOfObject)
    {
        return getPreparedStatementObject(getPreparedStatement(sql), indexOfObject);
    }
    
    /**
     * Returns an object that was added to a <code>PreparedStatement</code>
     * using its <code>set</code> methods.
     * @param indexOfStatement the index of the statement
     * @param indexOfObject the index used to set the object
     * @return the corresponding object
     */
    public Object getPreparedStatementObject(int indexOfStatement, int indexOfObject)
    {
        return getPreparedStatementObject(getPreparedStatement(indexOfStatement), indexOfObject);
    }
    
    /**
     * Verifies the number of statements.
     * @param number the exepcted number
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberStatements(int number)
    {
        verifyNumberStatements(number, getStatements());
    }
    
    /**
     * Verifies the number of prepared statements.
     * @param number the exepcted number
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberPreparedStatements(int number)
    {
        verifyNumberStatements(number, getPreparedStatements());
    }
    
    /**
     * Verifies the number of prepared statements with the specified
     * SQL.
     * @param number the exepcted number
     * @param sql the SQL
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberPreparedStatements(int number, String sql)
    {
        verifyNumberStatements(number, getPreparedStatements(sql));
    }
    
    private void verifyNumberStatements(int number, List statements)
    {
        if(null == statements || statements.size() == 0)
        {
            if(number == 0) return;
            throw new VerifyFailedException("Expected " + number + " pstatements, received 0 prepared statements");
        }
        if(statements.size() != number)
        {
            throw new VerifyFailedException("Expected " + number + " statements, received " + statements.size()+ "prepared statements");
        }
    }
    
    /**
     * Verifies that a <code>PreparedStatement</code> with the specified 
     * SQL statement is present.
     * @param sql the SQL statement
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementPresent(String sql)
    {
        if(null == getPreparedStatement(sql) )
        {
            throw new VerifyFailedException("Prepared statement with SQL " +  sql + " present.");
        }
    }
    
    /**
     * Verifies that a <code>PreparedStatement</code> with the specified 
     * SQL statement is not present.
     * @param sql the SQL statement
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementNotPresent(String sql)
    {
        if(null != getPreparedStatement(sql) )
        {
            throw new VerifyFailedException("Prepared statement with SQL " +  sql + " not present.");
        }
    }
    
    /**
     * Verifies that an object was added to a <code>PreparedStatement</code> with
     * the specified index.
     * @param statement the <code>PreparedStatement</code>
     * @param indexOfObject the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObjectPresent(PreparedStatement statement, int indexOfObject)
    {
        if(null == getPreparedStatementObject(statement, indexOfObject))
        {
            throw new VerifyFailedException("Prepared statement object with index " + indexOfObject + " not present.");
        }
    }

    /**
     * Verifies that an object was added to a <code>PreparedStatement</code> with
     * the specified index. Uses the first <code>PreparedStatement</code> with
     * the specified SQL.
     * @param sql the SQL statement of the <code>PreparedStatement</code>
     * @param indexOfObject the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObjectPresent(String sql, int indexOfObject)
    {
        if(null == getPreparedStatementObject(sql, indexOfObject))
        {
            throw new VerifyFailedException("Prepared statement object with index " + indexOfObject + " not present.");
        }
    }
    
    /**
     * Verifies that an object was added to a <code>PreparedStatement</code> with
     * the specified index.
     * @param indexOfStatement the index of the statement
     * @param indexOfObject the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObjectPresent(int indexOfStatement, int indexOfObject)
    {
        if(null == getPreparedStatementObject(indexOfStatement, indexOfObject))
        {
            throw new VerifyFailedException("Prepared statement object with index " + indexOfObject + " not present.");
        }
    }
    
    /**
     * erifies that an object with the specified index is not present.
     * @param statement the <code>PreparedStatement</code>
     * @param indexOfObject the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObjectNotPresent(PreparedStatement statement, int indexOfObject)
    {
        if(null != getPreparedStatementObject(statement, indexOfObject))
        {
            throw new VerifyFailedException("Prepared statement object with index " + indexOfObject + " present.");
        }
    }

    /**
     * Verifies that an object with the specified index is not present.
     * Uses the first <code>PreparedStatement</code> with the specified SQL.
     * @param sql the SQL statement of the <code>PreparedStatement</code>
     * @param indexOfObject the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObjectNotPresent(String sql, int indexOfObject)
    {
        if(null != getPreparedStatementObject(sql, indexOfObject))
        {
            throw new VerifyFailedException("Prepared statement object with index " + indexOfObject + " present.");
        }
    }

    /**
     * Verifies that an object with the specified index is not present.
     * @param indexOfStatement the index of the statement
     * @param indexOfObject the index used to set the object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObjectNotPresent(int indexOfStatement, int indexOfObject)
    {
        if(null != getPreparedStatementObject(indexOfStatement, indexOfObject))
        {
            throw new VerifyFailedException("Prepared statement object with index " + indexOfObject + " present.");
        }
    }
    
    /**
     * Verifies that an object from the specified <code>PreparedStatement</code> is equal
     * to the specified object.
     * @param statement the <code>PreparedStatement</code>
     * @param indexOfObject the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObject(PreparedStatement statement, int indexOfObject, Object object)
    {
        verifyPreparedStatementObjectPresent(statement, indexOfObject);
        Object actualObject = getPreparedStatementObject(statement, indexOfObject);
        if(!actualObject.equals(object))
        {
            throw new VerifyFailedException("Prepared statement object with index " + indexOfObject + " not equal with specified object.");
        }
    }
    
    /**
     * Verifies that an object from the <code>PreparedStatement</code> with the
     * specified SQL statement is equal to the specified object.
     * Uses the first <code>PreparedStatement</code> with the specified SQL.
     * @param sql the SQL statement of the <code>PreparedStatement</code>
     * @param indexOfObject the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObject(String sql, int indexOfObject, Object object)
    {
        verifyPreparedStatementObjectPresent(sql, indexOfObject);
        Object actualObject = getPreparedStatementObject(sql, indexOfObject);
        if(!actualObject.equals(object))
        {
            throw new VerifyFailedException("Prepared statement object with index " + indexOfObject + " not equal with specified object.");
        }
    }
    
    /**
     * Verifies that an object from the <code>PreparedStatement</code> with the
     * specified SQL statement is equal to the specified object.
     * @param sql the SQL statement of the <code>PreparedStatement</code>
     * @param indexOfObject the index used to set the object
     * @param object the expected object
     * @throws VerifyFailedException if verification fails
     */
    public void verifyPreparedStatementObject(int indexOfStatement, int indexOfObject, Object object)
    {
        verifyPreparedStatementObjectPresent(indexOfStatement, indexOfObject);
        Object actualObject = getPreparedStatementObject(indexOfStatement, indexOfObject);
        if(!actualObject.equals(object))
        {
            throw new VerifyFailedException("Prepared statement object with index " + indexOfObject + " not equal with specified object.");
        }
    }
}
