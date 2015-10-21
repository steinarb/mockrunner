package com.mockrunner.jdbc;

import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Interface for <code>ResultSet</code> factories.
 */
public interface ResultSetFactory
{
    public MockResultSet create(String id);

    class Default implements ResultSetFactory {
        public final static ResultSetFactory INSTANCE = new Default(false);
        protected final boolean columnsCaseSensitive;

        public Default(boolean columnsCaseSensitive) {
            this.columnsCaseSensitive = columnsCaseSensitive;
        }

        public MockResultSet create(String id) {
            MockResultSet resultSet = new MockResultSet(id);
            if (columnsCaseSensitive) {
                resultSet.setColumnsCaseSensitive(true);
            }
            return resultSet;
        }
    }
}
