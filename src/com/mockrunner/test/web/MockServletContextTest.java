package com.mockrunner.test.web;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

import junit.framework.TestCase;

import com.mockrunner.mock.web.MockServletContext;

public class MockServletContextTest extends TestCase
{
    public void testResources() throws Exception
    {
        MockServletContext context = new MockServletContext();
        context.setResource("testPath", new URL("http://test"));
        assertEquals(new URL("http://test"), context.getResource("testPath"));
        context.addResourcePath("testPath", "path1");
        ArrayList list = new ArrayList();
        list.add("path2");
        list.add("path3");
        context.addResourcePaths("testPath", list);
        Set paths = context.getResourcePaths("testPath");
        assertTrue(paths.size() == 3);
        assertTrue(paths.contains("path1"));
        assertTrue(paths.contains("path2"));
        assertTrue(paths.contains("path3"));
        assertNull(context.getResourcePaths("anotherTestPath"));
        byte[] data = new byte[] {1, 2, 3};
        context.setResourceAsStream("testPath", data);
        InputStream stream = context.getResourceAsStream("testPath");
        assertEquals(1, stream.read());
        assertEquals(2, stream.read());
        assertEquals(3, stream.read());
        assertEquals(-1, stream.read());
        data[0] = 5;
        stream = context.getResourceAsStream("testPath");
        assertEquals(1, stream.read());
        assertEquals(2, stream.read());
        assertEquals(3, stream.read());
        assertEquals(-1, stream.read());
    }
    
    public void testAttributeListenerCalled()
    {
        MockServletContext context = new MockServletContext();
        TestAttributeListener listener1 = new TestAttributeListener();
        TestAttributeListener listener2 = new TestAttributeListener();
        TestAttributeListener listener3 = new TestAttributeListener();
        context.addAttributeListener(listener1);
        context.addAttributeListener(listener2);
        context.addAttributeListener(listener3);
        context.setAttribute("key", "value");
        assertTrue(listener1.wasAttributeAddedCalled());
        assertTrue(listener2.wasAttributeAddedCalled());
        assertTrue(listener3.wasAttributeAddedCalled());
        assertFalse(listener1.wasAttributeReplacedCalled());
        assertFalse(listener2.wasAttributeReplacedCalled());
        assertFalse(listener3.wasAttributeReplacedCalled());
        listener1.reset();
        listener2.reset();
        listener3.reset();
        context.setAttribute("key", "value1");
        assertFalse(listener1.wasAttributeAddedCalled());
        assertFalse(listener2.wasAttributeAddedCalled());
        assertFalse(listener3.wasAttributeAddedCalled());
        assertTrue(listener1.wasAttributeReplacedCalled());
        assertTrue(listener2.wasAttributeReplacedCalled());
        assertTrue(listener3.wasAttributeReplacedCalled());
        context.removeAttribute("key");
        assertTrue(listener1.wasAttributeRemovedCalled());
        assertTrue(listener2.wasAttributeRemovedCalled());
        assertTrue(listener3.wasAttributeRemovedCalled());
    }

    public void testAttributeListenerValues()
    {
        MockServletContext context = new MockServletContext();
        TestAttributeOrderListener listener = new TestAttributeOrderListener();
        context.addAttributeListener(listener);
        context.setAttribute("key", "value");
        assertEquals("key", listener.getAddedEventKey());
        assertEquals("value", listener.getAddedEventValue());
        context.setAttribute("key", "anotherValue");
        assertEquals("key", listener.getReplacedEventKey());
        assertEquals("value", listener.getReplacedEventValue());
        context.removeAttribute("key");
        assertEquals("key", listener.getRemovedEventKey());
        assertEquals("anotherValue", listener.getRemovedEventValue());
    }

    public void testAttributeListenerNullValue()
    {
        MockServletContext context = new MockServletContext();
        TestAttributeListener listener = new TestAttributeListener();
        context.addAttributeListener(listener);
        context.setAttribute("key", null);
        assertFalse(listener.wasAttributeAddedCalled());
        context.setAttribute("key", "xyz");
        assertTrue(listener.wasAttributeAddedCalled());
        context.setAttribute("key", null);
        assertTrue(listener.wasAttributeRemovedCalled());
        assertFalse(listener.wasAttributeReplacedCalled());
        listener.reset();
        context.setAttribute("key", "xyz");
        assertTrue(listener.wasAttributeAddedCalled());
        assertFalse(listener.wasAttributeReplacedCalled());
        context.removeAttribute("myKey");
        assertFalse(listener.wasAttributeRemovedCalled());
    }
    
    private static class TestAttributeListener implements ServletContextAttributeListener
    {
        private boolean wasAttributeAddedCalled = false;
        private boolean wasAttributeReplacedCalled = false;
        private boolean wasAttributeRemovedCalled = false;
    
        public void attributeAdded(ServletContextAttributeEvent event)
        {
            wasAttributeAddedCalled = true;
        }

        public void attributeRemoved(ServletContextAttributeEvent event)
        {
            wasAttributeRemovedCalled = true;
        }

        public void attributeReplaced(ServletContextAttributeEvent event)
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

    private static class TestAttributeOrderListener implements ServletContextAttributeListener
    {
        private String addedEventKey;
        private Object addedEventValue;
        private String replacedEventKey;
        private Object replacedEventValue;
        private String removedEventKey;
        private Object removedEventValue;
    
        public void attributeAdded(ServletContextAttributeEvent event)
        {
            addedEventKey = event.getName();
            addedEventValue = event.getValue();
        }

        public void attributeRemoved(ServletContextAttributeEvent event)
        {
            removedEventKey = event.getName();
            removedEventValue = event.getValue();
        }

        public void attributeReplaced(ServletContextAttributeEvent event)
        {
            replacedEventKey = event.getName();
            replacedEventValue = event.getValue();
        }
    
        public String getAddedEventKey()
        {
            return addedEventKey;
        }

        public Object getAddedEventValue()
        {
            return addedEventValue;
        }

        public String getRemovedEventKey()
        {
            return removedEventKey;
        }

        public Object getRemovedEventValue()
        {
            return removedEventValue;
        }

        public String getReplacedEventKey()
        {
            return replacedEventKey;
        }

        public Object getReplacedEventValue()
        {
            return replacedEventValue;
        }
    }
}
