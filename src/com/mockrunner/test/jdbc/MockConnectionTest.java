package com.mockrunner.test.jdbc;

import java.sql.SQLException;

import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockDatabaseMetaData;

import junit.framework.TestCase;

public class MockConnectionTest extends TestCase
{
    public void testDatabaseMetaData() throws SQLException
    {
        MockConnection connection = new MockConnection();
        assertNotNull(connection.getMetaData());
        assertTrue(connection.getMetaData() instanceof MockDatabaseMetaData);
        assertSame(connection, connection.getMetaData().getConnection());
        connection.setMetaData(null);
        assertNull(connection.getMetaData());
        connection.setMetaData(new MockDatabaseMetaData());
        assertNotNull(connection.getMetaData());
        assertTrue(connection.getMetaData() instanceof MockDatabaseMetaData);
        assertSame(connection, connection.getMetaData().getConnection());
    }
}
