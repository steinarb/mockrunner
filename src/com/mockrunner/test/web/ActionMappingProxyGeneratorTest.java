package com.mockrunner.test.web;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mockrunner.mock.web.MockActionForward;
import com.mockrunner.mock.web.MockActionMapping;
import com.mockrunner.struts.ActionMappingProxyGenerator;

import junit.framework.TestCase;

public class ActionMappingProxyGeneratorTest extends TestCase
{
    private MockActionMapping delegate;
    private ActionMappingProxyGenerator generator;

    protected void setUp() throws Exception
    {
        delegate = new MockActionMapping();
        delegate.addForward("success", "testpath");
        delegate.setInput("testinput");
        generator = new ActionMappingProxyGenerator(delegate);
    }
    
    public void testCreateActionMappingProxy()
    {
        TestMapping proxy = (TestMapping)generator.createActionMappingProxy(TestMapping.class);
        MockActionForward forward = (MockActionForward)proxy.findForward("success");
        assertEquals("testpath", forward.getPath());
        String[] forwards = proxy.findForwards();
        assertEquals(1, forwards.length);
        assertEquals("success", forwards[0]);
        MockActionForward inputForward = (MockActionForward)proxy.getInputForward();
        assertEquals("testinput", inputForward.getPath());
        proxy.freeze();
        assertFalse(proxy.wasFindForwardCalled());
        assertFalse(proxy.wasFindForwardsCalled());
        assertFalse(proxy.wasGetInputForwardCalled());
        assertTrue(proxy.wasFreezeCalled());
    }
    
    public static class TestMapping extends ActionMapping
    {
        private boolean findForwardCalled = false;
        private boolean findForwardsCalled = false;
        private boolean getInputForwardCalled = false;
        private boolean freezeCalled = false;
        
        public boolean wasFindForwardCalled()
        {
            return findForwardCalled;
        }
        
        public boolean wasFindForwardsCalled()
        {
            return findForwardsCalled;
        }
        
        public boolean wasFreezeCalled()
        {
            return freezeCalled;
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
        
        public void freeze()
        {
            freezeCalled = true;
        }
    }
}
