package com.mockrunner.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.mockrunner.base.MockObjectFactory;

import junit.framework.TestCase;

public class MockObjectFactoryTest extends TestCase
{
    public void testDifferentMockObjects()
    {
        MockObjectFactory factory1 = new MockObjectFactory();
        MockObjectFactory factory2 = new MockObjectFactory();
        assertTrue(factory1.getMockRequest() != factory2.getMockRequest());
        assertTrue(factory1.getMockResponse() != factory2.getMockResponse());
        assertTrue(factory1.getMockSession() != factory2.getMockSession());
        assertTrue(factory1.getMockServletConfig() != factory2.getMockServletConfig());
        assertTrue(factory1.getMockServletContext() != factory2.getMockServletContext());
    }
    
    public void testMockObjectsWithSameContext()
    {
        MockObjectFactory factory1 = new MockObjectFactory();
        MockObjectFactory factory2 = new MockObjectFactory(factory1);
        assertTrue(factory1.getMockRequest() != factory2.getMockRequest());
        assertTrue(factory1.getMockResponse() != factory2.getMockResponse());
        assertTrue(factory1.getMockSession() != factory2.getMockSession());
        assertTrue(factory1.getMockServletConfig() == factory2.getMockServletConfig());
        assertTrue(factory1.getMockServletContext() == factory2.getMockServletContext());
    }
    
    public void testMockObjectsWithSameSessionAndContext()
    {
        MockObjectFactory factory1 = new MockObjectFactory();
        MockObjectFactory factory2 = new MockObjectFactory(factory1, false);
        assertTrue(factory1.getMockRequest() != factory2.getMockRequest());
        assertTrue(factory1.getMockResponse() != factory2.getMockResponse());
        assertTrue(factory1.getMockSession() == factory2.getMockSession());
        assertTrue(factory1.getMockServletConfig() == factory2.getMockServletConfig());
        assertTrue(factory1.getMockServletContext() == factory2.getMockServletContext());
        factory2 = new MockObjectFactory(factory1, true);
        assertTrue(factory1.getMockRequest() != factory2.getMockRequest());
        assertTrue(factory1.getMockResponse() != factory2.getMockResponse());
        assertTrue(factory1.getMockSession() != factory2.getMockSession());
        assertTrue(factory1.getMockServletConfig() == factory2.getMockServletConfig());
        assertTrue(factory1.getMockServletContext() == factory2.getMockServletContext());
    }
    
    public void testAddRequestWrapper()
    {
        MockObjectFactory factory = new MockObjectFactory();
        factory.getMockRequest().setupAddParameter("test", "test");
        factory.addRequestWrapper(HttpServletRequestWrapper.class);
        HttpServletRequest request = factory.getWrappedRequest();
        assertTrue(request instanceof HttpServletRequestWrapper);
        assertEquals("test", request.getParameter("test"));
    }
    
    public void testAddResponseWrapper() throws Exception
    {
        MockObjectFactory factory = new MockObjectFactory();
        factory.addResponseWrapper(HttpServletResponseWrapper.class);
        HttpServletResponse response = factory.getWrappedResponse();
        assertTrue(response instanceof HttpServletResponseWrapper);
        response.getWriter().print("test");
        response.getWriter().flush();
        assertEquals("test", factory.getMockResponse().getOutputStreamContents());     
    }
}
