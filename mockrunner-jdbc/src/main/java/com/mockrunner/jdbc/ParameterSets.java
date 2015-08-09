package com.mockrunner.jdbc;

import com.mockrunner.mock.jdbc.MockParameterMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the parameter sets for an executed
 * {@link com.mockrunner.mock.jdbc.MockPreparedStatement} or
 * {@link com.mockrunner.mock.jdbc.MockCallableStatement}.
 * If the prepared statement is executed multiple times, this
 * class contains multiple maps with the corresponding parameters.
 * Each <code>execute</code> call creates a parameter set.
 * A parameter set is a map, the index or the name of the 
 * parameter maps to the value.
 */
public class ParameterSets
{
	private final List<MockParameterMap> parameterSets;
	private final String sql;
	
	public ParameterSets(String sql)
	{
		parameterSets = new ArrayList<MockParameterMap>();
		this.sql = sql;
	}
	
	/**
	 * Get the SQL string.
	 * @return the SQL string
	 */
	public String getSQLStatement()
	{
		return sql;
	}
	
	/**
	 * Adds a parameter set.
	 * @param parameterSet the parameter set.
	 */
	public void addParameterSet(MockParameterMap parameterSet)
	{
		parameterSets.add(parameterSet);
	}
	
	/**
	 * Get the current number of parameter sets.
	 * @return the number of parameter sets
	 */
	public int getNumberParameterSets()
	{
		return parameterSets.size();
	}
	
	/**
	 * Gets a parameter set for a specified index.
	 * @param indexOfParameterSet the index
	 * @return the parameter set
	 */
	public MockParameterMap getParameterSet(int indexOfParameterSet)
	{
		if(indexOfParameterSet >= getNumberParameterSets()) return null;
		return parameterSets.get(indexOfParameterSet);
	}
}
