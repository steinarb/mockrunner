package com.mockrunner.mock.jdbc;

import java.sql.NClob;

/**
 * Mock implementation of <code>NClob</code>.
 */
public class MockNClob extends MockClob implements NClob
{
    public MockNClob(String data)
    {
        super(data);
    }
}
