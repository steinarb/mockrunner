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
    
    public void testRequestAndResponseList() throws Exception
    {
        TestFilter filter1 = new TestFilter();
        TestFilter filter2 = new TestFilter();
        TestFilter filter3 = new TestFilter();
        MockHttpServletRequest request1 = new MockHttpServletRequest();
        MockHttpServletRequest request3 = new MockHttpServletRequest();
        MockHttpServletResponse response1 = new MockHttpServletResponse();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        filter3.setRequest(request3);
        filter2.setResponse(response2);
        chain.addFilter(filter1);
        chain.addFilter(filter2);
        chain.addFilter(filter3);
        TestServlet servlet = new TestServlet();
        chain.setServlet(servlet);
        chain.doFilter(request1, response1);
        assertEquals(4, chain.getRequestList().size());
        assertEquals(4, chain.getResponseList().size());
        assertSame(request1, chain.getRequestList().get(0));
        assertSame(request1, chain.getRequestList().get(1));
        assertSame(request1, chain.getRequestList().get(2));
        assertSame(request3, chain.getRequestList().get(3));
        assertSame(request3, chain.getLastRequest());
        assertSame(response1, chain.getResponseList().get(0));
        assertSame(response1, chain.getResponseList().get(1));
        assertSame(response2, chain.getResponseList().get(2));
        assertSame(response2, chain.getResponseList().get(3));
        assertSame(response2, chain.getLastResponse());
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
        private ServletRequest request;
        private ServletResponse response;

        public void init(FilterConfig config) throws ServletException
        {

        }

        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
        {
            doFilterCalled = true;
            if(doChain)
            {
                if(null != this.request)
                {
                    request = this.request;
                }
                if(null != this.response)
                {
                    response = this.response;
                }
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
        
        public void setRequest(ServletRequest request)
        {
            this.request = request;
        }
        
        public void setResponse(ServletResponse response)
        {
            this.response = response;
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
