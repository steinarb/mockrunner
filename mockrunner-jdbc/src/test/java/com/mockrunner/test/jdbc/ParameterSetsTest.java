package com.mockrunner.test.jdbc;

import junit.framework.TestCase;

import com.mockrunner.jdbc.ParameterSets;
import com.mockrunner.mock.jdbc.MockParameterMap;

public class ParameterSetsTest extends TestCase
{
	public void testParameterSets()
	{
		ParameterSets sets = new ParameterSets("testsql");
		assertEquals("testsql", sets.getSQLStatement());
		assertEquals(0, sets.getNumberParameterSets());
		assertNull(sets.getParameterSet(0));
		MockParameterMap parameters = new MockParameterMap();
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
