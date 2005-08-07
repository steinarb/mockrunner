package com.mockrunner.test.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.TestCase;

import com.mockrunner.mock.web.MockRequestDispatcher;
import com.mockrunner.mock.web.MockServletContext;
import com.mockrunner.util.common.StreamUtil;

public class MockServletContextTest extends TestCase
{
    private MockServletContext context;

    protected void setUp()
    {
        context = new MockServletContext();
    }

    protected void tearDown()
    {
        context = null;
    }
    
    public void testResetAll() throws Exception
    {
        context.setAttribute("key", "value");
        context.addResourcePaths("path", new ArrayList());
        context.setResource("path", new URL("file://test"));
        context.resetAll();
        assertNull(context.getAttribute("key"));
        assertNull(context.getResourcePaths("path"));
        assertNull(context.getResource("path"));
    }
    
    public void testResources() throws Exception
    {
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
    
    public void testGetAttributeNames()
    {
        Enumeration enumeration = context.getAttributeNames();
        assertFalse(enumeration.hasMoreElements());
        context.setAttribute("key", null);
        enumeration = context.getAttributeNames();
        assertFalse(enumeration.hasMoreElements());
        context.setAttribute("key1", "value1");
        context.setAttribute("key2", "value2");
        enumeration = context.getAttributeNames();
        List testList = new ArrayList();
        testList.add(enumeration.nextElement());
        testList.add(enumeration.nextElement());
        assertFalse(enumeration.hasMoreElements());
        assertTrue(testList.contains("key1"));
        assertTrue(testList.contains("key2"));
        context.setAttribute("key2", null);
        assertNull(context.getAttribute("key2"));
        enumeration = context.getAttributeNames();
        testList = new ArrayList();
        testList.add(enumeration.nextElement());
        assertFalse(enumeration.hasMoreElements());
        assertTrue(testList.contains("key1"));
        context.setAttribute("key1", null);
        assertNull(context.getAttribute("key1"));
        enumeration = context.getAttributeNames();
        assertFalse(enumeration.hasMoreElements());
    }
    
    public void testRequestDispatcher() throws Exception
    {
        final String rdPath1 = "rdPathOne";
        final String rdPath2 = "rdPathTwo";
        final String rdPath3 = "rdPathThree";
    
        assertEquals(0, context.getRequestDispatcherMap().size());

        MockRequestDispatcher rd1 = (MockRequestDispatcher)context.getRequestDispatcher(rdPath1);
        assertEquals(rdPath1, rd1.getPath());
        assertNull(rd1.getForwardedRequest());
        assertNull(rd1.getIncludedRequest());
        
        assertEquals(1, context.getRequestDispatcherMap().size());
        assertTrue(context.getRequestDispatcherMap().containsKey(rdPath1));
        assertSame(rd1, context.getRequestDispatcherMap().get(rdPath1));
        
        MockRequestDispatcher actualRd2 = new MockRequestDispatcher();
        context.setRequestDispatcher(rdPath2, actualRd2);
        MockRequestDispatcher rd2 = (MockRequestDispatcher)context.getRequestDispatcher(rdPath2);
        assertEquals(rdPath2, rd2.getPath());
        assertSame(actualRd2, rd2);
        assertNull(rd1.getForwardedRequest());
        assertNull(rd1.getIncludedRequest());
        
        rd2 = (MockRequestDispatcher)context.getNamedDispatcher(rdPath2);
        assertEquals(rdPath2, rd2.getPath());
        assertSame(actualRd2, rd2);
        
        assertEquals(2, context.getRequestDispatcherMap().size());
        assertTrue(context.getRequestDispatcherMap().containsKey(rdPath2));
        assertSame(rd2, context.getRequestDispatcherMap().get(rdPath2));
        
        RequestDispatcher actualRd3 = new TestRequestDispatcher();
        context.setRequestDispatcher(rdPath3, actualRd3);
        RequestDispatcher rd3 = context.getRequestDispatcher(rdPath3);
        assertSame(actualRd3, rd3);
        
        rd3 = context.getNamedDispatcher(rdPath3);
        assertSame(actualRd3, rd3);
        
        assertEquals(3, context.getRequestDispatcherMap().size());
        assertTrue(context.getRequestDispatcherMap().containsKey(rdPath3));
        assertSame(rd3, context.getRequestDispatcherMap().get(rdPath3));
        
        context.clearRequestDispatcherMap();
        assertEquals(0, context.getRequestDispatcherMap().size());
    }
    
    public void testSetResourceAsStream() throws Exception
    {
        byte[] input = {1, 2, 3, 4};
        context.setResourceAsStream("testpath1", input);
        InputStream result = context.getResourceAsStream("testpath1");
        assertTrue(Arrays.equals(input, StreamUtil.getStreamAsByteArray(result)));
        context.setResourceAsStream("testpath2", new ByteArrayInputStream(input));
        result = context.getResourceAsStream("testpath2");
        assertTrue(StreamUtil.compareStreams(new ByteArrayInputStream(input), result));
    }
    
    private class TestAttributeListener implements ServletContextAttributeListener
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

    private class TestAttributeOrderListener implements ServletContextAttributeListener
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
    
    private class TestRequestDispatcher implements RequestDispatcher
    {
        
        public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException
        {

        }
        
        public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException
        {

        }
    }
}
