package com.mockrunner.test;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.mockrunner.mock.MockHttpSession;

import junit.framework.TestCase;

/**
 * Test for {@link com.mockrunner.mock.MockHttpSessionTest}
 */
public class MockHttpSessionTest extends TestCase
{
    public MockHttpSessionTest(String arg0)
    {
        super(arg0);
    }
    
    public void testInvalidate()
    {
        MockHttpSession session = new MockHttpSession();
        TestSessionListener listener1 = new TestSessionListener();
        TestSessionListener listener2 = new TestSessionListener();
        session.setAttribute("key1", listener1);
        session.setAttribute("key2", listener2);
        assertTrue(listener1.wasValueBoundCalled());
        assertTrue(listener2.wasValueBoundCalled());
        assertFalse(listener1.wasValueUnboundCalled());
        assertFalse(listener2.wasValueUnboundCalled());
        session.removeAttribute("key2");
        assertTrue(listener2.wasValueUnboundCalled());
        session.invalidate();
        assertTrue(listener1.wasValueUnboundCalled());
    }
    
    private static class TestSessionListener implements HttpSessionBindingListener
    {
        private boolean valueBoundCalled = false;
        private boolean valueUnboundCalled = false;
        
        public void valueBound(HttpSessionBindingEvent arg0)
        {
            valueBoundCalled = true;
        }

        public void valueUnbound(HttpSessionBindingEvent arg0)
        {
            valueUnboundCalled = true;
        }
        
        public boolean wasValueBoundCalled()
        {
            return valueBoundCalled;
        }

        public boolean wasValueUnboundCalled()
        {
            return valueUnboundCalled;
        }
    }
}
