package com.mockrunner.util;

/**
 * Simple util class for SQL statements
 */
public class SQLUtil
{
    /**
     * Returns if the specified SQL string is a select, i.e.
     * contains the string <i>SELECT</i> (case doesn't matter).
     * @param sql the SQL string
     * @return <code>true</code> if the specified SQL string is a select
     */
    public static boolean isSelect(String sql)
    {
        sql = sql.toLowerCase();
        return (-1 != sql.indexOf("select"));
    }
}
