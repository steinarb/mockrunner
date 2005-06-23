package com.mockrunner.test.web;

import junit.framework.TestCase;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mockrunner.mock.web.MockActionForward;
import com.mockrunner.mock.web.MockActionMapping;
import com.mockrunner.struts.ActionMappingProxyGenerator;

public class ActionMappingProxyGeneratorTest extends TestCase
{
    private MockActionMapping delegate;
    private ActionMappingProxyGenerator generator;

    protected void setUp() throws Exception
    {
        delegate = new MockActionMapping();
        delegate.addForward("success", "testpath");
        delegate.setInput("willBeReplaced");
        generator = new ActionMappingProxyGenerator(delegate);
    }
    
    public void testCreateActionMappingProxy()
    {
        TestMapping proxy = (TestMapping)generator.createActionMappingProxy(TestMapping.class);
        MockActionForward forward = (MockActionForward)proxy.findForward("success");
        proxy.setInput("testinput");
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
    
    public void testDuplicateMethods()
    {
        TestMapping proxy = (TestMapping)generator.createActionMappingProxy(TestMapping.class);
        proxy.setInput("testinput");
        assertTrue(proxy.wasSetInputCalled());
        assertEquals("testinput", proxy.getSetInputParam());
        assertEquals("testinput", delegate.getInput());
        assertEquals("testinput", proxy.getInput());
        proxy.setScope("request");
        proxy.setForward("testforward");
        assertEquals("request", proxy.getScope());
        assertEquals("testforward", proxy.getForward());
        assertEquals("request", delegate.getScope());
        assertEquals("testforward", delegate.getForward());
        proxy.setValidate(false);
        proxy.setParameter("parameter");
        proxy.setType("mytype");
        assertEquals(false, proxy.getValidate());
        assertEquals("parameter", proxy.getParameter());
        assertEquals("mytype", proxy.getType());
        assertEquals(false, delegate.getValidate());
        assertEquals("parameter", delegate.getParameter());
        assertEquals("mytype", delegate.getType());
    }
    
    public static class TestMapping extends ActionMapping
    {
        private boolean findForwardCalled = false;
        private boolean findForwardsCalled = false;
        private boolean getInputForwardCalled = false;
        private boolean setInputCalled = false;
        private boolean freezeCalled = false;
        private String setInputParam = null;
        
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
        
        public boolean wasSetInputCalled()
        {
            return setInputCalled;
        }
        
        public boolean wasGetInputForwardCalled()
        {
            return getInputForwardCalled;
        }
        
        public String getSetInputParam()
        {
            return setInputParam;
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
        
        public void setInput(String input)
        {
            setInputCalled = true;
            setInputParam = input;
            super.setInput(input);
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
