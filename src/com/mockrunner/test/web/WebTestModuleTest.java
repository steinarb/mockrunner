package com.mockrunner.test.web;

import javax.servlet.http.HttpServletRequestWrapper;

import junit.framework.TestCase;

import com.mockrunner.base.WebTestModule;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.servlet.ServletTestModule;

public class WebTestModuleTest extends TestCase
{
    private WebTestModule module;
    private WebMockObjectFactory factory;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        factory = new WebMockObjectFactory();
        module = new ServletTestModule(factory);
    }
    
    public void testAttributes()
    {
        module.setRequestAttribute("requestKey", "requestValue");
        module.setSessionAttribute("sessionKey", "sessionValue");
        assertEquals("requestValue", module.getRequestAttribute("requestKey"));
        assertEquals("sessionValue", module.getSessionAttribute("sessionKey"));
    }
    
    public void testWrappedRequest()
    {
        TestWrapper wrapper = new TestWrapper();
        factory.addRequestWrapper(wrapper);
        module.setRequestAttribute("key", "value");
        assertTrue(wrapper.wasSetAttributeCalled());
        assertEquals("testAttribute", module.getRequestAttribute(null));
        assertEquals("testParameter", module.getRequestParameter(null));
    }
    
    private class TestWrapper extends HttpServletRequestWrapper
    {
        private boolean setAttributeCalled = false;
        
        public TestWrapper()
        {
            super(new MockHttpServletRequest());
        }
        
        public void setAttribute(String key, Object value)
        {
            setAttributeCalled = true;
        }
        
        public Object getAttribute(String key)
        {
            return "testAttribute";
        }
        
        public String getParameter(String arg0)
        {
            return "testParameter";
        }
        
        public boolean wasSetAttributeCalled()
        {
            return setAttributeCalled;
        }
    }
}
