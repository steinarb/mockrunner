package com.mockrunner.test.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.mockrunner.mock.web.ActionMockObjectFactory;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class BasicActionTestCaseAdapterTest extends BasicActionTestCaseAdapter
{
	@Test
    public void testSetWebFactory()
    {
        ActionMockObjectFactory actionFactory = new ActionMockObjectFactory();
        setActionMockObjectFactory(actionFactory);
        assertSame(actionFactory, getActionMockObjectFactory());
        setActionMockObjectFactory(null);
        assertNotNull(getActionMockObjectFactory());
        assertNotSame(actionFactory, getActionMockObjectFactory());
    }
}
