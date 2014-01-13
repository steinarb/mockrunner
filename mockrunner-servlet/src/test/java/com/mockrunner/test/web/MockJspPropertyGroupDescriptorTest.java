package com.mockrunner.test.web;

import java.util.Iterator;

import junit.framework.TestCase;

import com.mockrunner.mock.web.MockJspPropertyGroupDescriptor;

public class MockJspPropertyGroupDescriptorTest extends TestCase
{
    private MockJspPropertyGroupDescriptor descriptor;
    
    protected void setUp() throws Exception
    {
        descriptor = new MockJspPropertyGroupDescriptor();
    }

    protected void tearDown() throws Exception
    {
        descriptor = null;
    }

    public void testReset()
    {
        descriptor.addIncludeCoda("coda");
        descriptor.addIncludePrelude("prelude");
        descriptor.addUrlPattern("urlPattern");
        descriptor.setIsXml("xyz");
        descriptor.setBuffer("25");
        descriptor.setDefaultContentType("xyz");
        descriptor.setDeferredSyntaxAllowedAsLiteral("true");
        descriptor.setElIgnored("123");
        descriptor.setErrorOnUndeclaredNamespace("true");
        descriptor.setPageEncoding("Encoding");
        descriptor.setScriptingInvalid("456");
        descriptor.setTrimDirectiveWhitespaces("true");
        assertEquals(1, descriptor.getIncludeCodas().size());
        assertEquals(1, descriptor.getIncludePreludes().size());
        assertEquals(1, descriptor.getUrlPatterns().size());
        assertEquals("xyz", descriptor.getIsXml());
        assertEquals("25", descriptor.getBuffer());
        assertEquals("xyz", descriptor.getDefaultContentType());
        assertEquals("true", descriptor.getDeferredSyntaxAllowedAsLiteral());
        assertEquals("123", descriptor.getElIgnored());
        assertEquals("true", descriptor.getErrorOnUndeclaredNamespace());
        assertEquals("Encoding", descriptor.getPageEncoding());
        assertEquals("456", descriptor.getScriptingInvalid());
        assertEquals("true", descriptor.getTrimDirectiveWhitespaces());
        descriptor.reset();
        assertTrue(descriptor.getIncludeCodas().isEmpty());
        assertTrue(descriptor.getIncludePreludes().isEmpty());
        assertTrue(descriptor.getUrlPatterns().isEmpty());
        assertEquals("false", descriptor.getIsXml());
        assertEquals("8kb", descriptor.getBuffer());
        assertEquals("text/html", descriptor.getDefaultContentType());
        assertEquals("false", descriptor.getDeferredSyntaxAllowedAsLiteral());
        assertEquals("false", descriptor.getElIgnored());
        assertEquals("false", descriptor.getErrorOnUndeclaredNamespace());
        assertEquals("ISO-8859-1", descriptor.getPageEncoding());
        assertEquals("false", descriptor.getScriptingInvalid());
        assertEquals("false", descriptor.getTrimDirectiveWhitespaces());
    }
    
    public void testIncludeAndURLPatterns()
    {
        assertTrue(descriptor.getIncludeCodas().isEmpty());
        assertTrue(descriptor.getIncludePreludes().isEmpty());
        assertTrue(descriptor.getUrlPatterns().isEmpty());
        descriptor.addIncludeCoda("coda1");
        descriptor.addIncludeCoda("coda2");
        descriptor.addIncludePrelude("prelude1");
        descriptor.addUrlPattern("urlPattern1");
        descriptor.addUrlPattern("urlPattern2");
        descriptor.addUrlPattern("urlPattern3");
        assertEquals(2, descriptor.getIncludeCodas().size());
        assertEquals(1, descriptor.getIncludePreludes().size());
        assertEquals(3, descriptor.getUrlPatterns().size());
        descriptor.clearIncludeCodas();
        descriptor.clearIncludePreludes();
        descriptor.clearUrlPatterns();
        assertTrue(descriptor.getIncludeCodas().isEmpty());
        assertTrue(descriptor.getIncludePreludes().isEmpty());
        assertTrue(descriptor.getUrlPatterns().isEmpty());
    }
    
    public void testIncludeAndURLPatternsListChange()
    {
        descriptor.addIncludeCoda("coda1");
        descriptor.addIncludePrelude("prelude1");
        descriptor.addUrlPattern("urlPattern1");
        assertEquals(1, descriptor.getIncludeCodas().size());
        assertEquals(1, descriptor.getIncludePreludes().size());
        assertEquals(1, descriptor.getUrlPatterns().size());
        descriptor.getIncludeCodas().add("coda2");
        descriptor.getIncludePreludes().add("prelude2");
        descriptor.getUrlPatterns().add("urlPattern2");
        assertEquals(1, descriptor.getIncludeCodas().size());
        assertEquals(1, descriptor.getIncludePreludes().size());
        assertEquals(1, descriptor.getUrlPatterns().size());
        Iterator codas = descriptor.getIncludeCodas().iterator();
        Iterator preludes = descriptor.getIncludePreludes().iterator();
        Iterator urlPatterns = descriptor.getUrlPatterns().iterator();
        assertEquals("coda1", codas.next());
        assertEquals("prelude1", preludes.next());
        assertEquals("urlPattern1", urlPatterns.next());
    }
}
