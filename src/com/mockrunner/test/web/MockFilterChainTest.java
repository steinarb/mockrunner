package com.mockrunner.test.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import com.mockrunner.mock.web.MockFilterChain;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;

import junit.framework.TestCase;

public class MockFilterChainTest extends TestCase
{
    private MockFilterChain chain;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        chain = new MockFilterChain();
    }
    
    public void testNoServlet() throws Exception
    {
        chain.doFilter(null, null);
        TestServlet servlet = new TestServlet();
        chain.setServlet(servlet);
        chain.doFilter(null, null);
        assertTrue(servlet.wasServiceCalled());
        chain.release();
        servlet.reset();
        chain.doFilter(null, null);
        assertFalse(servlet.wasServiceCalled());
    }
    
    public void testFilterInstance() throws Exception
    {
        try
        {
            chain.addFilter(String.class);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        chain.addFilter(TestFilter.class);
    }
    
    public void testDoFilter() throws Exception
    {
        TestFilter filter1 = new TestFilter();
        TestFilter filter2 = new TestFilter();
        chain.addFilter(filter1);
        chain.addFilter(filter2);
        TestServlet servlet = new TestServlet();
        chain.setServlet(servlet);
        chain.doFilter(null, null);
        assertTrue(filter1.wasDoFilterCalled());
        assertTrue(filter2.wasDoFilterCalled());
        assertTrue(servlet.wasServiceCalled());
        filter1.reset();
        filter2.reset();
        servlet.reset();
        filter1.setDoChain(false);
        chain.doFilter(null, null);
        assertTrue(filter1.wasDoFilterCalled());
        assertFalse(filter2.wasDoFilterCalled());
        assertFalse(servlet.wasServiceCalled());
        assertNull(chain.getLastRequest());
        assertNull(chain.getLastResponse());
    }
    
    public void testLastRequestAndResponse() throws Exception
    {
        TestFilter filter1 = new TestFilter();
        TestFilter filter2 = new TestFilter();
        chain.addFilter(filter1);
        chain.addFilter(filter2);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertNull(chain.getLastRequest());
        assertNull(chain.getLastResponse());
        chain.doFilter(request, response);
        assertSame(request, chain.getLastRequest());
        assertSame(response, chain.getLastResponse());
    }
    
    public static class TestFilter implements Filter
    {
        private boolean doFilterCalled  = false;
        private boolean doChain = true;

        public void init(FilterConfig config) throws ServletException
        {

        }

        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
        {
            doFilterCalled = true;
            if(doChain)
            {
                chain.doFilter(request, response);
            }
        }

        public void destroy()
        {
       
        }

        public void setDoChain(boolean doChain)
        {
            this.doChain = doChain;
        }
        
        public boolean wasDoFilterCalled()
        {
            return doFilterCalled;
        }

        public void reset()
        {
            doFilterCalled  = false;
        }
    }
    
    public static class TestServlet extends HttpServlet
    {
        private boolean serviceCalled = false;
        
        public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException
        {
            serviceCalled = true;
        }
        
        public void reset()
        {
            serviceCalled = false;
        }
        
        public boolean wasServiceCalled()
        {
            return serviceCalled;
        }
    }
}
