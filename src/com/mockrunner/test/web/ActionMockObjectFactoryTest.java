package com.mockrunner.test.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequestWrapper;

import junit.framework.TestCase;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mockrunner.mock.web.ActionMockObjectFactory;
import com.mockrunner.mock.web.MockActionForward;
import com.mockrunner.mock.web.MockActionMapping;
import com.mockrunner.mock.web.MockActionServlet;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockModuleConfig;

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
    
    public void testPrepareActionMapping()
    {
        ActionMockObjectFactory factory = new ActionMockObjectFactory();
        assertSame(factory.getMockActionMapping(), factory.getActionMapping());
        ActionMapping mapping = factory.prepareActionMapping(TestMapping.class);
        assertSame(mapping, factory.getActionMapping());
        assertNotSame(mapping, factory.getMockActionMapping());
        assertSame(factory.getActionMapping(), factory.getMockRequest().getAttribute(Globals.MAPPING_KEY));
        assertSame(factory.getMockModuleConfig(), factory.getMockRequest().getAttribute(Globals.MODULE_KEY));
        TestMapping testMapping = (TestMapping)mapping;
        testMapping.method();
        assertTrue(testMapping.wasMethodCalled());
        testMapping.setInput("testInput");
        assertEquals("testInput", testMapping.getInput());
        assertEquals("testInput", factory.getMockActionMapping().getInput());
        MockActionForward inputForward = (MockActionForward)testMapping.getInputForward();
        assertEquals("testInput", inputForward.getPath());
        factory.getMockActionMapping().addForward("success", "testpath");
        MockActionForward forward = (MockActionForward)testMapping.findForward("success");
        assertEquals("testpath", forward.getPath());
    }
    
    public void testOverrideCreate()
    {
        ActionMockObjectFactory factory = new TestActionMockObjectFactory();
        assertNotSame(factory.getMockActionMapping().getClass(), MockActionMapping.class);
        assertNotSame(factory.getActionMapping().getClass(), MockActionMapping.class);
        assertNotSame(factory.getMockActionServlet().getClass(), MockActionServlet.class);
        assertNotSame(factory.getMockModuleConfig().getClass(), MockModuleConfig.class);
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
    
    public static class TestMapping extends ActionMapping
    {
        private boolean findForwardCalled = false;
        private boolean findForwardsCalled = false;
        private boolean getInputForwardCalled = false;
        private boolean methodCalled = false;
        
        public boolean wasFindForwardCalled()
        {
            return findForwardCalled;
        }
        
        public boolean wasFindForwardsCalled()
        {
            return findForwardsCalled;
        }
        
        public boolean wasMethodCalled()
        {
            return methodCalled;
        }
        
        public boolean wasGetInputForwardCalled()
        {
            return getInputForwardCalled;
        }
        
        public ActionForward findForward(String name)
        {
            findForwardCalled = true;
            return null;
        }
        
        public String[] findForwards()
        {
            findForwardsCalled = true;
            return null;
        }
        
        public ActionForward getInputForward()
        {
            getInputForwardCalled = true;
            return null;
        }
        
        public void method()
        {
            methodCalled = true;
        }
    }
    
    public static class TestActionMockObjectFactory extends ActionMockObjectFactory
    {
        public MockActionMapping createMockActionMapping()
        {
            return new MockActionMapping() {};
        }

        public MockActionServlet createMockActionServlet()
        {
            return new MockActionServlet() {};
        }

        public MockModuleConfig createMockModuleConfig()
        {
            return new MockModuleConfig("testmodule") {};
        }  
    }
}
