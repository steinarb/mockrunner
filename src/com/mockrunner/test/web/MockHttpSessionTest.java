package com.mockrunner.test.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import junit.framework.TestCase;

import com.mockrunner.mock.web.MockHttpSession;

public class MockHttpSessionTest extends TestCase
{
    private MockHttpSession session;

    protected void setUp()
    {
        session = new MockHttpSession();
    }

    protected void tearDown()
    {
        session = null;
    }

    public void testResetAll() throws Exception
    {
        session.setAttribute("key", "value");
        session.setMaxInactiveInterval(3);
        session.resetAll();
        assertNull(session.getAttribute("key"));
        assertEquals(-1, session.getMaxInactiveInterval());
    }
    
    public void testBindingListenerInvalidate()
    {
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
        InvalidateAttributeListener listener = new InvalidateAttributeListener();
        session.addAttributeListener(listener);
        session.invalidate();
        assertTrue(listener1.wasValueUnboundCalled());
        assertTrue(listener.getDidThrowIllegalStateException());
        try
        {
            session.invalidate();
            fail();
        } 
        catch(IllegalStateException exc)
        {
            //should throw exception
        }
    }
    
    public void testBindingListenerOverwriteAttribute()
    {
        TestSessionListener listener1 = new TestSessionListener();
        TestSessionListener listener2 = new TestSessionListener();
        session.setAttribute("key", listener1);
        session.setAttribute("key", listener2);
        assertTrue(listener1.wasValueUnboundCalled());
        assertTrue(listener2.wasValueBoundCalled());
        assertFalse(listener2.wasValueUnboundCalled());
    }
    
    public void testBindingListenerOverwriteSameAttribute()
    {
        TestSessionListener listener = new TestSessionListener();
        session.setAttribute("key", listener);
        listener.reset();
        session.setAttribute("key", listener);
        assertTrue(listener.wasValueUnboundCalled());
        assertTrue(listener.wasValueBoundCalled());
        assertTrue(listener.wasValueUnboundBeforeBoundCalled());
    }
    
    public void testBindingListenerCorrectOrder()
    {
        session.setAttribute("key", "test");
        TestSessionOrderListener listener = new TestSessionOrderListener();
        session.setAttribute("key", listener);
        assertEquals("key", listener.getBoundEventKey());
        assertEquals(listener, listener.getBoundEventValue());
        assertEquals(listener, listener.getBoundSessionValue());
        session.setAttribute("key", "xyz");
        assertEquals("key", listener.getUnboundEventKey());
        assertEquals(listener, listener.getUnboundEventValue());
        assertEquals("xyz", listener.getUnboundSessionValue());
        session = new MockHttpSession();
        listener = new TestSessionOrderListener();
        session.setAttribute("key", "abc");
        session.setAttribute("key", listener);
        session.removeAttribute("key");
        assertEquals("key", listener.getUnboundEventKey());
        assertEquals(listener, listener.getUnboundEventValue());
        assertEquals(null, listener.getUnboundSessionValue());
    }
    
    public void testAttributeListenerCalled()
    {
        TestAttributeListener listener1 = new TestAttributeListener();
        TestAttributeListener listener2 = new TestAttributeListener();
        TestAttributeListener listener3 = new TestAttributeListener();
        session.addAttributeListener(listener1);
        session.addAttributeListener(listener2);
        session.addAttributeListener(listener3);
        session.setAttribute("key", "value");
        assertTrue(listener1.wasAttributeAddedCalled());
        assertTrue(listener2.wasAttributeAddedCalled());
        assertTrue(listener3.wasAttributeAddedCalled());
        assertFalse(listener1.wasAttributeReplacedCalled());
        assertFalse(listener2.wasAttributeReplacedCalled());
        assertFalse(listener3.wasAttributeReplacedCalled());
        listener1.reset();
        listener2.reset();
        listener3.reset();
        session.setAttribute("key", "value1");
        assertFalse(listener1.wasAttributeAddedCalled());
        assertFalse(listener2.wasAttributeAddedCalled());
        assertFalse(listener3.wasAttributeAddedCalled());
        assertTrue(listener1.wasAttributeReplacedCalled());
        assertTrue(listener2.wasAttributeReplacedCalled());
        assertTrue(listener3.wasAttributeReplacedCalled());
        session.removeAttribute("key");
        assertTrue(listener1.wasAttributeRemovedCalled());
        assertTrue(listener2.wasAttributeRemovedCalled());
        assertTrue(listener3.wasAttributeRemovedCalled());
    }
    
    public void testAttributeListenerOrder()
    {
        TestAttributeOrderListener listener = new TestAttributeOrderListener();
        session.addAttributeListener(listener);
        session.setAttribute("key", "value");
        assertEquals("key", listener.getAddedEventKey());
        assertEquals("value", listener.getAddedEventValue());
        assertEquals("value", listener.getAddedSessionValue());
        session.setAttribute("key", "anotherValue");
        assertEquals("key", listener.getReplacedEventKey());
        assertEquals("value", listener.getReplacedEventValue());
        assertEquals("anotherValue", listener.getReplacedSessionValue());
        session.removeAttribute("key");
        assertEquals("key", listener.getRemovedEventKey());
        assertEquals("anotherValue", listener.getRemovedEventValue());
        assertNull("", listener.getRemovedSessionValue());
    }
    
    public void testAttributeListenerNullValue()
    {
        TestAttributeListener listener = new TestAttributeListener();
        session.addAttributeListener(listener);
        session.setAttribute("key", null);
        assertFalse(listener.wasAttributeAddedCalled());
        session.setAttribute("key", "xyz");
        assertTrue(listener.wasAttributeAddedCalled());
        session.setAttribute("key", null);
        assertTrue(listener.wasAttributeRemovedCalled());
        assertFalse(listener.wasAttributeReplacedCalled());
        listener.reset();
        session.setAttribute("key", "xyz");
        assertTrue(listener.wasAttributeAddedCalled());
        assertFalse(listener.wasAttributeReplacedCalled());
        session.removeAttribute("myKey");
        assertFalse(listener.wasAttributeRemovedCalled());
    }
    
    public void testGetAttributeNames()
    {
        Enumeration enumeration = session.getAttributeNames();
        assertFalse(enumeration.hasMoreElements());
        session.setAttribute("key", null);
        enumeration = session.getAttributeNames();
        assertFalse(enumeration.hasMoreElements());
        session.setAttribute("key1", "value1");
        session.setAttribute("key2", "value2");
        enumeration = session.getAttributeNames();
        List testList = new ArrayList();
        testList.add(enumeration.nextElement());
        testList.add(enumeration.nextElement());
        assertFalse(enumeration.hasMoreElements());
        assertTrue(testList.contains("key1"));
        assertTrue(testList.contains("key2"));
        session.setAttribute("key2", null);
        assertNull(session.getAttribute("key2"));
        enumeration = session.getAttributeNames();
        testList = new ArrayList();
        testList.add(enumeration.nextElement());
        assertFalse(enumeration.hasMoreElements());
        assertTrue(testList.contains("key1"));
        session.setAttribute("key1", null);
        assertNull(session.getAttribute("key1"));
        enumeration = session.getAttributeNames();
        assertFalse(enumeration.hasMoreElements());
    }
    
    private static class TestSessionListener implements HttpSessionBindingListener
    {
        private boolean valueBoundCalled = false;
        private boolean valueUnboundCalled = false;
        private boolean valueUnboundBeforeBoundCalled = false;
        
        public void reset()
        {
            valueBoundCalled = false;
            valueUnboundCalled = false;
            valueUnboundBeforeBoundCalled = false;
        }
        
        public void valueBound(HttpSessionBindingEvent event)
        {
            valueBoundCalled = true;
        }

        public void valueUnbound(HttpSessionBindingEvent event)
        {
            valueUnboundCalled = true;
            if(!valueBoundCalled)
            {
                valueUnboundBeforeBoundCalled = true;
            }
        }
        
        public boolean wasValueBoundCalled()
        {
            return valueBoundCalled;
        }

        public boolean wasValueUnboundCalled()
        {
            return valueUnboundCalled;
        }
        
        public boolean wasValueUnboundBeforeBoundCalled()
        {
            return valueUnboundBeforeBoundCalled;
        }
    }
    
    private static class TestSessionOrderListener implements HttpSessionBindingListener
    {
        private String boundEventKey;
        private Object boundEventValue;
        private Object boundSessionValue;
        private String unboundEventKey;
        private Object unboundEventValue;
        private Object unboundSessionValue;
        
        public void valueBound(HttpSessionBindingEvent event)
        {
            boundEventKey = event.getName();
            boundEventValue = event.getValue();
            boundSessionValue = event.getSession().getAttribute(boundEventKey);
        }

        public void valueUnbound(HttpSessionBindingEvent event)
        {
            unboundEventKey = event.getName();
            unboundEventValue = event.getValue();
            unboundSessionValue = event.getSession().getAttribute(unboundEventKey);
        }
        
        public String getBoundEventKey()
        {
            return boundEventKey;
        }

        public Object getBoundEventValue()
        {
            return boundEventValue;
        }

        public Object getBoundSessionValue()
        {
            return boundSessionValue;
        }

        public String getUnboundEventKey()
        {
            return unboundEventKey;
        }

        public Object getUnboundEventValue()
        {
            return unboundEventValue;
        }

        public Object getUnboundSessionValue()
        {
            return unboundSessionValue;
        }
    }
    
    private static class InvalidateAttributeListener implements HttpSessionAttributeListener
    {
        private boolean didThrowIllegalStateException = false;
        
        public void attributeAdded(HttpSessionBindingEvent event)
        {
            
        }

        public void attributeRemoved(HttpSessionBindingEvent event)
        {
            HttpSession session = event.getSession();
            didThrowIllegalStateException = false;
            try
            {
                session.getAttribute(event.getName());
            } 
            catch(IllegalStateException exc)
            {
                didThrowIllegalStateException = true;
            }
        }

        public void attributeReplaced(HttpSessionBindingEvent event)
        {
            
        }

        public boolean getDidThrowIllegalStateException()
        {
            return didThrowIllegalStateException;
        }
    }
    
    private static class TestAttributeListener implements HttpSessionAttributeListener
    {
        private boolean wasAttributeAddedCalled = false;
        private boolean wasAttributeReplacedCalled = false;
        private boolean wasAttributeRemovedCalled = false;
        
        public void attributeAdded(HttpSessionBindingEvent event)
        {
            wasAttributeAddedCalled = true;
        }

        public void attributeRemoved(HttpSessionBindingEvent event)
        {
            wasAttributeRemovedCalled = true;
        }

        public void attributeReplaced(HttpSessionBindingEvent event)
        {
            wasAttributeReplacedCalled = true;
        }
        
        public void reset()
        {
            wasAttributeAddedCalled = false;
            wasAttributeReplacedCalled = false;
            wasAttributeRemovedCalled = false;
        }
        
        public boolean wasAttributeAddedCalled()
        {
            return wasAttributeAddedCalled;
        }

        public boolean wasAttributeRemovedCalled()
        {
            return wasAttributeRemovedCalled;
        }

        public boolean wasAttributeReplacedCalled()
        {
            return wasAttributeReplacedCalled;
        }
    }
    
    private static class TestAttributeOrderListener implements HttpSessionAttributeListener
    {
        private String addedEventKey;
        private Object addedEventValue;
        private Object addedSessionValue;
        private String replacedEventKey;
        private Object replacedEventValue;
        private Object replacedSessionValue;
        private String removedEventKey;
        private Object removedEventValue;
        private Object removedSessionValue;
        
        public void attributeAdded(HttpSessionBindingEvent event)
        {
            addedEventKey = event.getName();
            addedEventValue = event.getValue();
            addedSessionValue = event.getSession().getAttribute(addedEventKey);
        }

        public void attributeRemoved(HttpSessionBindingEvent event)
        {
            removedEventKey = event.getName();
            removedEventValue = event.getValue();
            removedSessionValue = event.getSession().getAttribute(removedEventKey);
        }

        public void attributeReplaced(HttpSessionBindingEvent event)
        {
            replacedEventKey = event.getName();
            replacedEventValue = event.getValue();
            replacedSessionValue = event.getSession().getAttribute(replacedEventKey);
        }
        
        public String getAddedEventKey()
        {
            return addedEventKey;
        }

        public Object getAddedEventValue()
        {
            return addedEventValue;
        }

        public Object getAddedSessionValue()
        {
            return addedSessionValue;
        }

        public String getRemovedEventKey()
        {
            return removedEventKey;
        }

        public Object getRemovedEventValue()
        {
            return removedEventValue;
        }

        public Object getRemovedSessionValue()
        {
            return removedSessionValue;
        }

        public String getReplacedEventKey()
        {
            return replacedEventKey;
        }

        public Object getReplacedEventValue()
        {
            return replacedEventValue;
        }

        public Object getReplacedSessionValue()
        {
            return replacedSessionValue;
        }
    }
}
