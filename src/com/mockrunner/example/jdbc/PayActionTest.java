package com.mockrunner.example.jdbc;

import com.mockrunner.jdbc.JDBCTestCaseAdapter;
import com.mockrunner.struts.ActionTestModule;

public class PayActionTest extends JDBCTestCaseAdapter
{
    private ActionTestModule actionModule;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        actionModule = new ActionTestModule(createWebMockObjectFactory());
    }

}
