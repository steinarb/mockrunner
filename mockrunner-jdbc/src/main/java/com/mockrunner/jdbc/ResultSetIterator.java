package com.mockrunner.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import com.mockrunner.mock.jdbc.MockResultSet;

/**
 * Iterates over a MockResultSet
 * 
 * @author Carlos Martins
 */
public class ResultSetIterator implements Iterator<List<?>> {
	
	private final MockResultSet resultSet;

	/**
	 * Clones the MockResultSet passed and initiates an iterator over the cloned result set.
	 * The original result set is not affected by the operations carried out with the iterator.
	 * @param resultSet
	 */
	public ResultSetIterator(MockResultSet resultSet) {
		
		this.resultSet = (MockResultSet) resultSet.clone();
		try {
			this.resultSet.beforeFirst();
			this.resultSet.setFetchDirection(ResultSet.FETCH_FORWARD);
		} catch (SQLException e) {
		}
	}

	public boolean hasNext() {
		try {
			return resultSet.getRow() < resultSet.getFetchSize();
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Returns the list of column values of the next row, or null.
	 * @return a List object where the first value in the first column.
	 * @see java.util.Iterator#next()
	 */
	public List<?> next() {
		try {
			if(resultSet.next()) {
				List<?> row = resultSet.getRow(resultSet.getRow());
				return row;
			}
		} catch (SQLException e) {
		}
		return null;
	}

	/**
	 * Not implemented
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
	}
}
