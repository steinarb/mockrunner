package com.mockrunner.test.web;

import com.mockrunner.mock.web.ActionMockObjectFactory;
import com.mockrunner.struts.BasicActionTestCaseAdapter;

public class BasicActionTestCaseAdapterTest extends BasicActionTestCaseAdapter
{
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
