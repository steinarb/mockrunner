package com.mockrunner.test.web;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import com.mockrunner.mock.web.MockServletContext;

import junit.framework.TestCase;

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
}
