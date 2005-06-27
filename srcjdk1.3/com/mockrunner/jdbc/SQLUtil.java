package com.mockrunner.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Simple util class for SQL statements
 */
public class SQLUtil
{
    /**
     * Returns if the specified SQL string is a select, i.e.
     * contains the string <i>select</i> (case insensitive).
     * @param sql the SQL string
     * @return <code>true</code> if the specified SQL string is a select
     */
    public static boolean isSelect(String sql)
    {
        sql = sql.toLowerCase();
        return (-1 != sql.indexOf("select"));
    }
    
    /**
     * Throws an <code>SQLException</code> if the specified 
     * <code>fetchDirection</code> is invalid
     * @param fetchDirection the fetch direction
     */
    public static void checkFetchDirection(int fetchDirection) throws SQLException
    {
        if(fetchDirection != ResultSet.FETCH_FORWARD && fetchDirection != ResultSet.FETCH_REVERSE && fetchDirection != ResultSet.FETCH_UNKNOWN)
        {
            throw new SQLException("fetchDirection must be either FETCH_FORWARD, FETCH_REVERSE or FETCH_UNKNOWN");
        }
    }
}
