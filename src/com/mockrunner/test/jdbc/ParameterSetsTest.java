package com.mockrunner.test.jdbc;

import java.util.HashMap;

import junit.framework.TestCase;

import com.mockrunner.jdbc.ParameterSets;

public class ParameterSetsTest extends TestCase
{
	public void testParameterSets()
	{
		ParameterSets sets = new ParameterSets("testsql");
		assertEquals("testsql", sets.getSQLStatement());
		assertEquals(0, sets.getNumberParameterSets());
		assertNull(sets.getParameterSet(0));
		HashMap parameters = new HashMap();
		sets.addParameterSet(parameters);
		assertEquals(1, sets.getNumberParameterSets());
		assertSame(parameters, sets.getParameterSet(0));
		assertNull(sets.getParameterSet(1));
		sets.addParameterSet(parameters);
		assertEquals(2, sets.getNumberParameterSets());
		assertSame(parameters, sets.getParameterSet(1));
		assertNull(sets.getParameterSet(2));
	}
}
