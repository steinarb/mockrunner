package com.mockrunner.test;

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
}
