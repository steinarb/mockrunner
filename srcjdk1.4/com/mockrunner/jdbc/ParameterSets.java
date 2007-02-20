package com.mockrunner.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	private List parameterSets;
	private String sql;
	
	public ParameterSets(String sql)
	{
		parameterSets = new ArrayList();
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
	public void addParameterSet(Map parameterSet)
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
	public Map getParameterSet(int indexOfParameterSet)
	{
		if(indexOfParameterSet >= getNumberParameterSets()) return null;
		return (Map)parameterSets.get(indexOfParameterSet);
	}
}
