package com.mockrunner.jdbc;

import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Interface for <code>ResultSet</code> factories.
 * Currently there's only the {@link FileResultSetFactory} 
 * but you can implement your own factories.
 */
public interface ResultSetFactory
{
    public MockResultSet create(String id);
}
