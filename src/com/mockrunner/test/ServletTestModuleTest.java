package com.mockrunner.test;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.servlet.ServletTestModule;

public class ServletTestModuleTest extends BaseTestCase
{
    public ServletTestModuleTest(String arg0)
    {
        super(arg0);
    }

    public void testMethodsCalled()
    {
        ServletTestModule module = new ServletTestModule(getMockObjectFactory());
        TestServlet servlet = (TestServlet)module.createServlet(TestServlet.class);
        module.doDelete();
        assertTrue(servlet.wasDoDeleteCalled());
        module.doGet();
        assertTrue(servlet.wasDoGetCalled());
        module.doOptions();
        assertTrue(servlet.wasDoOptionsCalled());
        module.doPost();
        assertTrue(servlet.wasDoPostCalled());
        module.doPut();
        assertTrue(servlet.wasDoPutCalled());
        module.doTrace();
        assertTrue(servlet.wasDoTraceCalled());
    }
}
