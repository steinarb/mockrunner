package com.mockrunner.test.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.servlet.ServletTestModule;

public class ServletTestModuleTest extends BaseTestCase
{
    private ServletTestModule module;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        module = new ServletTestModule(getWebMockObjectFactory());
    }
    
    public void testServletInitCalls() throws Exception
    {
        TestServlet servlet = (TestServlet)module.createServlet(TestServlet.class);
        assertTrue(servlet.wasInitCalled());
        servlet = new TestServlet();
        module.setServlet(servlet);
        assertFalse(servlet.wasInitCalled());
        servlet = new TestServlet();
        module.setServlet(servlet, false);
        assertFalse(servlet.wasInitCalled());
        servlet = new TestServlet();
        module.setServlet(servlet, true);
        assertTrue(servlet.wasInitCalled());
    }
    
    public void testFilterInitCalls() throws Exception
    {
        TestFilter filter1 = (TestFilter)module.createFilter(TestFilter.class);
        assertTrue(filter1.wasInitCalled());
        TestFilter filter2 = new TestFilter();
        module.addFilter(filter2);
        assertFalse(filter2.wasInitCalled());
        TestFilter filter3 = new TestFilter();
        module.addFilter(filter3, false);
        assertFalse(filter3.wasInitCalled());
        TestFilter filter4 = new TestFilter();
        module.addFilter(filter4, true);
        assertTrue(filter4.wasInitCalled());
    }
    
    public void testVerifyOutput() throws Exception
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
        module.verifyOutputRegularExpression("THIS.*");
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
        try
        {
            module.verifyOutputRegularExpression(".*TEST");
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
        module.setServlet(new TestServlet(), true);
        TestServlet servlet = (TestServlet)module.getServlet();
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
    
    public void testFilterChainWithRelease()
    {
        TestServlet servlet = (TestServlet)module.createServlet(TestServlet.class);
        TestFilter filter = new TestFilter();
        module.setDoChain(true);
        module.addFilter(filter, true);
        module.doPost();
        assertTrue(servlet.wasInitCalled());
        assertTrue(servlet.wasDoPostCalled());
        assertTrue(filter.wasInitCalled());
        assertTrue(filter.wasDoFilterCalled());
        servlet.reset();
        filter.reset();
        module.releaseFilters();
        module.doPost();
        assertTrue(servlet.wasDoPostCalled());
        assertFalse(filter.wasInitCalled());
        assertFalse(filter.wasDoFilterCalled());
    }
    
    public static class TestFilter implements Filter
    {
        private boolean initCalled  = false;
        private boolean doFilterCalled  = false;
        private FilterChain lastChain;

        public void init(FilterConfig arg0) throws ServletException
        {
            initCalled = true;
        }

        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
        {
            doFilterCalled = true;
            lastChain = chain;
            chain.doFilter(request, response);
        }

        public void destroy()
        {
       
        }

        public boolean wasDoFilterCalled()
        {
            return doFilterCalled;
        }

        public boolean wasInitCalled()
        {
            return initCalled;
        }

        public FilterChain getLastFilterChain()
        {
            return lastChain;
        }
        
        public void reset()
        {
            initCalled  = false;
            doFilterCalled  = false;
            lastChain = null;
        }
    }
    
    public static class TestServlet extends HttpServlet
    {
        private boolean initCalled = false;
        private boolean doGetCalled = false;
        private boolean doPostCalled = false;
        private boolean doDeleteCalled = false;
        private boolean doOptionsCalled = false;
        private boolean doPutCalled = false;
        private boolean doTraceCalled = false;
        private boolean doHeadCalled = false;
        
        public void init(ServletConfig config) throws ServletException
        {
            initCalled = true;
        }
    
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            doGetCalled = true;
        }

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            doPostCalled = true;
        }
    
        protected void doDelete(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
        {
            doDeleteCalled = true;
        }

        protected void doOptions(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
        {
            doOptionsCalled = true;
        }

        protected void doPut(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
        {
            doPutCalled = true;
        }

        protected void doTrace(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
        {
            doTraceCalled = true;
        }
    
        protected void doHead(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
        {
            doHeadCalled = true;
        }
        
        public boolean wasInitCalled()
        {
            return initCalled;
        }
    
        public boolean wasDoDeleteCalled()
        {
            return doDeleteCalled;
        }

        public boolean wasDoGetCalled()
        {
            return doGetCalled;
        }

        public boolean wasDoOptionsCalled()
        {
            return doOptionsCalled;
        }

        public boolean wasDoPostCalled()
        {
            return doPostCalled;
        }

        public boolean wasDoPutCalled()
        {
            return doPutCalled;
        }

        public boolean wasDoTraceCalled()
        {
            return doTraceCalled;
        }
    
        public boolean wasDoHeadCalled()
        {
            return doHeadCalled;
        }
        
        public void reset()
        {
            initCalled = false;
            doGetCalled = false;
            doPostCalled = false;
            doDeleteCalled = false;
            doOptionsCalled = false;
            doPutCalled = false;
            doTraceCalled = false;
            doHeadCalled = false;
        }
    }
}
