package com.mockrunner.test.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.struts.Globals;

import com.mockrunner.mock.web.ActionMockObjectFactory;
import com.mockrunner.mock.web.MockHttpServletRequest;

import junit.framework.TestCase;

public class ActionMockObjectFactoryTest extends TestCase
{
    public void testRefresh()
    {
        ActionMockObjectFactory factory = new ActionMockObjectFactory();
        TestWrapper wrapper = new TestWrapper();
        factory.addRequestWrapper(wrapper);
        assertTrue(wrapper.getAttributesMap().isEmpty());
        factory.refresh();
        assertSame(factory.getMockActionMapping(), wrapper.getAttributesMap().get(Globals.MAPPING_KEY));
        assertSame(factory.getMockModuleConfig(), wrapper.getAttributesMap().get(Globals.MODULE_KEY));
    }
    
    private class TestWrapper extends HttpServletRequestWrapper
    {
        private Map attributes = new HashMap();
        
        public TestWrapper()
        {
            super(new MockHttpServletRequest());
        }
        
        public void setAttribute(String key, Object value)
        {
            attributes.put(key, value);
        }
        
        public Map getAttributesMap()
        {
            return attributes;
        }
    }
}
