package com.mockrunner.jdbc;

import java.sql.PreparedStatement;

import com.mockrunner.base.MockObjectFactory;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.jdbc.MockPreparedStatement;

/**
 * Module for JDBC tests. Not complete yet.
 */
public class JDBCTestModule
{
    private MockObjectFactory mockFactory;
      
    public JDBCTestModule(MockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }
    
    /**
     * Returns a {@link com.mockrunner.mock.jdbc.MockPreparedStatement} that was 
     * created using a {@link com.mockrunner.mock.jdbc.MockConnection}.
     * @param sql the SQL statement used to create the <code>PreparedStatement</code>
     * @return the <code>PreparedStatement</code>
     */
    public MockPreparedStatement getPreparedStatement(String sql)
    {
        return mockFactory.getMockConnection().getPreparedStatement(sql);
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
     * using its <code>set</code> methods. Uses the <code>PreparedStatement</code>
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
     * the specified index.
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
}
