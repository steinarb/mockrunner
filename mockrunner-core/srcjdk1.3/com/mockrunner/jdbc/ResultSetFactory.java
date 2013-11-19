package com.mockrunner.jdbc;

import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Interface for <code>ResultSet</code> factories.
 */
public interface ResultSetFactory
{
    public MockResultSet create(String id);
}
