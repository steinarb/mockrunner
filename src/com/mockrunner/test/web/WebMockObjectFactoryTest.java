package com.mockrunner.test.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.mockrunner.mock.web.MockFilterChain;
import com.mockrunner.mock.web.MockFilterConfig;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockPageContext;
import com.mockrunner.mock.web.MockServletConfig;
import com.mockrunner.mock.web.MockServletContext;
import com.mockrunner.mock.web.WebMockObjectFactory;

import junit.framework.TestCase;

public class WebMockObjectFactoryTest extends TestCase
{
    public void testDifferentMockObjects()
    {
        WebMockObjectFactory factory1 = new WebMockObjectFactory();
        WebMockObjectFactory factory2 = new WebMockObjectFactory();
        assertTrue(factory1.getMockRequest() != factory2.getMockRequest());
        assertTrue(factory1.getMockResponse() != factory2.getMockResponse());
        assertTrue(factory1.getMockSession() != factory2.getMockSession());
        assertTrue(factory1.getMockServletConfig() != factory2.getMockServletConfig());
        assertTrue(factory1.getMockServletContext() != factory2.getMockServletContext());
    }
    
    public void testMockObjectsWithSameContext()
    {
        WebMockObjectFactory factory1 = new WebMockObjectFactory();
        WebMockObjectFactory factory2 = new WebMockObjectFactory(factory1);
        assertTrue(factory1.getMockRequest() != factory2.getMockRequest());
        assertTrue(factory1.getMockResponse() != factory2.getMockResponse());
        assertTrue(factory1.getMockSession() != factory2.getMockSession());
        assertTrue(factory1.getMockServletConfig() == factory2.getMockServletConfig());
        assertTrue(factory1.getMockServletContext() == factory2.getMockServletContext());
    }
    
    public void testMockObjectsWithSameSessionAndContext()
    {
        WebMockObjectFactory factory1 = new WebMockObjectFactory();
        WebMockObjectFactory factory2 = new WebMockObjectFactory(factory1, false);
        assertTrue(factory1.getMockRequest() != factory2.getMockRequest());
        assertTrue(factory1.getMockResponse() != factory2.getMockResponse());
        assertTrue(factory1.getMockSession() == factory2.getMockSession());
        assertTrue(factory1.getMockServletConfig() == factory2.getMockServletConfig());
        assertTrue(factory1.getMockServletContext() == factory2.getMockServletContext());
        factory2 = new WebMockObjectFactory(factory1, true);
        assertTrue(factory1.getMockRequest() != factory2.getMockRequest());
        assertTrue(factory1.getMockResponse() != factory2.getMockResponse());
        assertTrue(factory1.getMockSession() != factory2.getMockSession());
        assertTrue(factory1.getMockServletConfig() == factory2.getMockServletConfig());
        assertTrue(factory1.getMockServletContext() == factory2.getMockServletContext());
    }
    
    public void testAddRequestWrapper()
    {
        WebMockObjectFactory factory = new WebMockObjectFactory();
        factory.getMockRequest().setupAddParameter("test", "test");
        factory.addRequestWrapper(HttpServletRequestWrapper.class);
        HttpServletRequest request = factory.getWrappedRequest();
        assertTrue(request instanceof HttpServletRequestWrapper);
        assertEquals("test", request.getParameter("test"));
        HttpServletRequestWrapper requestWrapper = (HttpServletRequestWrapper)factory.getWrappedRequest();
        assertSame(factory.getMockRequest(), requestWrapper.getRequest());
    }
    
    public void testAddResponseWrapper() throws Exception
    {
        WebMockObjectFactory factory = new WebMockObjectFactory();
        factory.addResponseWrapper(HttpServletResponseWrapper.class);
        HttpServletResponse response = factory.getWrappedResponse();
        assertTrue(response instanceof HttpServletResponseWrapper);
        response.getWriter().print("test");
        response.getWriter().flush();
        assertEquals("test", factory.getMockResponse().getOutputStreamContent());    
        HttpServletResponseWrapper responseWrapper = (HttpServletResponseWrapper)factory.getWrappedResponse();
        assertSame(factory.getMockResponse(), responseWrapper.getResponse());
    }
    
    public void testAddRequestAndResponseWrapperClasses()
    {
        WebMockObjectFactory factory = new WebMockObjectFactory();
        factory.addRequestWrapper(TestRequestWrapper.class);
        assertTrue(factory.getWrappedRequest() instanceof TestRequestWrapper);
        assertSame(factory.getMockRequest(), ((TestRequestWrapper)factory.getWrappedRequest()).getRequest());
        factory.addResponseWrapper(TestResponseWrapper.class);
        assertTrue(factory.getWrappedResponse() instanceof TestResponseWrapper);
        assertSame(factory.getMockResponse(), ((TestResponseWrapper)factory.getWrappedResponse()).getResponse());
    }
    
    public void testRefresh() throws Exception
    {
        WebMockObjectFactory factory = new WebMockObjectFactory();
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(factory.getMockRequest());
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(factory.getMockResponse());
        factory.addRequestWrapper(requestWrapper);
        factory.addResponseWrapper(responseWrapper);
        MockPageContext pageContext = factory.getMockPageContext();
        assertSame(factory.getMockRequest(), pageContext.getRequest());
        assertSame(factory.getMockResponse(), pageContext.getResponse());
        factory.refresh();
        pageContext = factory.getMockPageContext();
        assertSame(requestWrapper, pageContext.getRequest());
        assertSame(responseWrapper, pageContext.getResponse());
    }
    
    public void testOverrideCreate()
    {
        WebMockObjectFactory factory = new TestWebMockObjectFactory();
        assertNotSame(factory.getMockRequest().getClass(), MockHttpServletRequest.class);
        assertNotSame(factory.getMockResponse().getClass(), MockHttpServletResponse.class);
        assertNotSame(factory.getMockFilterChain().getClass(), MockFilterChain.class);
        assertNotSame(factory.getMockFilterConfig().getClass(), MockFilterConfig.class);
        assertNotSame(factory.getMockPageContext().getClass(), MockPageContext.class);
        assertNotSame(factory.getMockServletConfig().getClass(), MockServletConfig.class);
        assertNotSame(factory.getMockServletContext().getClass(), MockServletContext.class);
        assertNotSame(factory.getMockSession().getClass(), MockHttpSession.class);
    }
    
    public static class TestRequestWrapper extends MockHttpServletRequest
    {
        private HttpServletRequest request;
        
        public TestRequestWrapper(HttpServletRequest request)
        {
            this.request = request;
        }
        
        public HttpServletRequest getRequest()
        {
            return request;
        }
    }
    
    public static class TestResponseWrapper extends MockHttpServletResponse
    {
        private HttpServletResponse response;
        
        public TestResponseWrapper(HttpServletResponse response)
        {
            this.response = response;
        }
        
        public HttpServletResponse getResponse()
        {
            return response;
        }
    }
    
    public static class TestWebMockObjectFactory extends WebMockObjectFactory
    {
        public MockFilterChain createMockFilterChain()
        {
            return new MockFilterChain() {};
        }

        public MockFilterConfig createMockFilterConfig()
        {
            return new MockFilterConfig() {};
        }
        
        public MockPageContext createMockPageContext()
        {
            return new MockPageContext() {};
        }

        public MockHttpServletRequest createMockRequest()
        {
  
            return new MockHttpServletRequest() {};
        }

        public MockHttpServletResponse createMockResponse()
        {
            return new MockHttpServletResponse() {};
        }

        public MockServletConfig createMockServletConfig()
        {
            return new MockServletConfig() {};
        }

        public MockServletContext createMockServletContext()
        {
            return new MockServletContext() {};
        }

        public MockHttpSession createMockSession()
        {
            return new MockHttpSession() {};
        }
    }
}
