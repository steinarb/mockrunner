package com.mockrunner.test.web;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.servlet.ServletTestModule;

public class ServletTestModuleTest extends BaseTestCase
{
    private ServletTestModule module;
    
    public ServletTestModuleTest(String arg0)
    {
        super(arg0);
    }
    
    protected void setUp() throws Exception
    {
        super.setUp();
        module = new ServletTestModule(getWebMockObjectFactory());
    }
    
    public void testCaseSensitive() throws Exception
    {
        getWebMockObjectFactory().getMockResponse().getWriter().write("This is a test");
        try
        {
            module.verifyOutput("this is a test");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setCaseSensitive(false);
        module.verifyOutput("this is a test");
        module.verifyOutputContains("TeSt");
        module.setCaseSensitive(true);
        try
        {
            module.verifyOutputContains("THIS");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }

    public void testServletMethodsCalled()
    {
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
        module.doHead();
        assertTrue(servlet.wasDoHeadCalled());
    }
    
    public void testFilterChain()
    {
        TestServlet servlet = (TestServlet)module.createServlet(TestServlet.class);
        TestFilter filter1 = (TestFilter)module.createFilter(TestFilter.class);
        TestFilter filter2 = (TestFilter)module.createFilter(TestFilter.class);
        TestFilter filter3 = (TestFilter)module.createFilter(TestFilter.class);
        module.doGet();
        assertTrue(filter1.wasInitCalled());
        assertTrue(filter2.wasInitCalled());
        assertTrue(filter3.wasInitCalled());
        assertFalse(filter1.wasDoFilterCalled());
        assertFalse(filter2.wasDoFilterCalled());
        assertFalse(filter3.wasDoFilterCalled());
        assertTrue(servlet.wasDoGetCalled());
        module.setDoChain(true);
        module.doGet();
        assertTrue(filter1.wasDoFilterCalled());
        assertTrue(filter2.wasDoFilterCalled());
        assertTrue(filter3.wasDoFilterCalled());
        assertTrue(getWebMockObjectFactory().getMockFilterChain() == filter1.getLastFilterChain());
        assertTrue(getWebMockObjectFactory().getMockFilterChain() == filter2.getLastFilterChain());
        assertTrue(getWebMockObjectFactory().getMockFilterChain() == filter3.getLastFilterChain());
    }
}
