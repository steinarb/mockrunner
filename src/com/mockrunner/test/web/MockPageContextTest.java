package com.mockrunner.test.web;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Enumeration;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.web.MockBodyContent;
import com.mockrunner.mock.web.MockExpressionEvaluator;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockJspWriter;
import com.mockrunner.mock.web.MockPageContext;
import com.mockrunner.mock.web.MockRequestDispatcher;
import com.mockrunner.mock.web.MockServletConfig;
import com.mockrunner.mock.web.MockVariableResolver;

public class MockPageContextTest extends BaseTestCase
{
    private MockPageContext pageContext;
        
    protected void setUp() throws Exception
    {
        super.setUp();
        pageContext = getWebMockObjectFactory().getMockPageContext();
    }

    public void testPushPopBody() throws Exception
    {
        JspWriter writer = pageContext.getOut();
        writer.print("TestOut");
        MockBodyContent content1 = (MockBodyContent)pageContext.pushBody();
        assertTrue(writer == content1.getEnclosingWriter());
        content1.clear();
        content1.print("TestContent");
        assertEquals("TestContent", content1.getString());
        StringWriter outWriter = new StringWriter();
        content1.writeOut(outWriter);
        assertEquals("TestContent", outWriter.toString());
        MockBodyContent content2 = (MockBodyContent)pageContext.pushBody();
        assertTrue(content1 == content2.getEnclosingWriter());
        assertEquals("", content2.getString());
        pageContext.popBody();
        writer = pageContext.getOut();
        assertEquals("TestContent", ((MockBodyContent)writer).getOutputAsString());
        pageContext.popBody();
        writer = pageContext.getOut();
        assertEquals("TestOut", ((MockJspWriter)writer).getOutputAsString());    
    }
    
    public void testPushBodyWithWriter() throws Exception
    {
        JspWriter writer1 = pageContext.getOut();
        StringWriter providedWriter = new StringWriter();
        JspWriter writer2 = pageContext.pushBody(providedWriter);
        writer1.print("writer1");
        pageContext.getOut().print("writer");
        pageContext.getOut().print(2);
        writer2.print("writer2");
        MockBodyContent content3 = (MockBodyContent)pageContext.pushBody();
        content3.print("writer3");
        pageContext.getOut().print("writer");
        pageContext.getOut().print(3);
        pageContext.getOut().print("writer3");
        pageContext.popBody();
        pageContext.getOut().print("anotherWriter2Text");
        pageContext.popBody();
        pageContext.getOut().print("anotherWriter1Text");
        writer1.flush();
        writer2.flush();
        assertEquals("writer1anotherWriter1Text", ((MockJspWriter)writer1).getOutputAsString());
        assertEquals("", ((MockJspWriter)writer2).getOutputAsString());
        assertEquals("writer2writer2anotherWriter2Text", providedWriter.toString());
        assertEquals("writer3writer3writer3", content3.getString());
    }
    
    public void testGetAttribute() throws Exception
    {
        pageContext.setAttribute("pageKey", "pageValue");
        getWebMockObjectFactory().getMockRequest().setAttribute("requestKey", "requestValue");
        getWebMockObjectFactory().getMockSession().setAttribute("sessionKey", "sessionValue");
        getWebMockObjectFactory().getMockServletContext().setAttribute("contextKey", "contextValue");
        assertEquals("pageValue", pageContext.getAttribute("pageKey"));
        assertEquals("pageValue", pageContext.getAttribute("pageKey", PageContext.PAGE_SCOPE));
        assertEquals("requestValue", pageContext.getAttribute("requestKey", PageContext.REQUEST_SCOPE));
        assertEquals("sessionValue", pageContext.getAttribute("sessionKey", PageContext.SESSION_SCOPE));
        assertEquals("contextValue", pageContext.getAttribute("contextKey", PageContext.APPLICATION_SCOPE));
        assertNull(pageContext.getAttribute("pageKey1"));
        assertNull(pageContext.getAttribute("pageKey1", PageContext.PAGE_SCOPE));
        assertNull(pageContext.getAttribute("requestKey1", PageContext.REQUEST_SCOPE));
        assertNull(pageContext.getAttribute("sessionKey1", PageContext.SESSION_SCOPE));
        assertNull(pageContext.getAttribute("contextKey1", PageContext.APPLICATION_SCOPE));
        pageContext.setServletRequest(null);
        assertNull(pageContext.getAttribute("requestKey", PageContext.REQUEST_SCOPE));
        assertNull(pageContext.getAttribute("sessionKey", PageContext.SESSION_SCOPE));
        assertEquals("contextValue", pageContext.getAttribute("contextKey", PageContext.APPLICATION_SCOPE));
        MockHttpServletRequest otherRequest = new MockHttpServletRequest();
        otherRequest.setAttribute("otherRequestKey", "otherRequestValue");
        pageContext.setServletRequest(otherRequest);
        assertEquals("otherRequestValue", pageContext.getAttribute("otherRequestKey", PageContext.REQUEST_SCOPE));
        assertNull(pageContext.getAttribute("sessionKey", PageContext.SESSION_SCOPE));
        pageContext.setServletConfig(null);
        assertNull(pageContext.getAttribute("contextKey", PageContext.APPLICATION_SCOPE));
        pageContext.setServletConfig(new MockServletConfig());
        assertNull(pageContext.getAttribute("contextKey", PageContext.APPLICATION_SCOPE));
        try
        {
            pageContext.getAttribute("contextKey", 60000);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
    }
    
    public void testSetAttribute() throws Exception
    {
        pageContext.setAttribute("pageKey", "pageValue");
        pageContext.setAttribute("requestKey", "requestValue", PageContext.REQUEST_SCOPE);
        pageContext.setAttribute("sessionKey", "sessionValue", PageContext.SESSION_SCOPE);
        pageContext.setAttribute("contextKey", "contextValue", PageContext.APPLICATION_SCOPE);
        assertEquals("pageValue", pageContext.getAttribute("pageKey"));
        assertEquals("requestValue", getWebMockObjectFactory().getMockRequest().getAttribute("requestKey"));
        assertEquals("sessionValue", getWebMockObjectFactory().getMockSession().getAttribute("sessionKey"));
        assertEquals("contextValue", getWebMockObjectFactory().getMockServletContext().getAttribute("contextKey"));
        pageContext.setServletRequest(null);
        pageContext.setAttribute("requestKey1", "requestValue1", PageContext.REQUEST_SCOPE);
        pageContext.setAttribute("sessionKey1", "sessionValue1", PageContext.SESSION_SCOPE);
        assertNull(getWebMockObjectFactory().getMockRequest().getAttribute("requestKey1"));
        assertNull(getWebMockObjectFactory().getMockSession().getAttribute("sessionKey1"));
        pageContext.setServletRequest(new MockHttpServletRequest());
        pageContext.setAttribute("sessionKey1", "sessionValue1", PageContext.SESSION_SCOPE);
        assertNull(getWebMockObjectFactory().getMockSession().getAttribute("sessionKey1"));
        pageContext.setServletConfig(null);
        pageContext.setAttribute("contextKey1", "contextValue1", PageContext.APPLICATION_SCOPE);
        assertNull(getWebMockObjectFactory().getMockServletContext().getAttribute("contextKey1"));
        try
        {
            pageContext.setAttribute("requestKey", "requestValue", 60000);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
    }
    
    public void testRemoveAttribute() throws Exception
    {
        pageContext.setAttribute("pageKey", "pageValue");
        pageContext.removeAttribute("pageKey", PageContext.APPLICATION_SCOPE);
        assertNotNull(pageContext.getAttribute("pageKey"));
        pageContext.removeAttribute("pageKey");
        assertNull(pageContext.getAttribute("pageKey"));
        pageContext.setAttribute("requestKey", "requestValue", PageContext.REQUEST_SCOPE);
        pageContext.removeAttribute("requestKey", PageContext.SESSION_SCOPE);
        assertNotNull(pageContext.getAttribute("requestKey", PageContext.REQUEST_SCOPE));
        pageContext.removeAttribute("requestKey", PageContext.REQUEST_SCOPE);
        assertNull(pageContext.getAttribute("requestKey", PageContext.REQUEST_SCOPE));
        pageContext.setAttribute("sessionKey", "sessionValue", PageContext.SESSION_SCOPE);
        pageContext.removeAttribute("sessionKey", PageContext.PAGE_SCOPE);
        assertNotNull(pageContext.getAttribute("sessionKey", PageContext.SESSION_SCOPE));
        pageContext.removeAttribute("sessionKey", PageContext.SESSION_SCOPE);
        assertNull(pageContext.getAttribute("sessionKey", PageContext.SESSION_SCOPE));
        pageContext.setAttribute("contextKey", "contextValue", PageContext.APPLICATION_SCOPE);
        pageContext.removeAttribute("contextKey", PageContext.REQUEST_SCOPE);
        assertNotNull(pageContext.getAttribute("contextKey", PageContext.APPLICATION_SCOPE));
        pageContext.removeAttribute("contextKey", PageContext.APPLICATION_SCOPE);
        assertNull(pageContext.getAttribute("contextKey", PageContext.APPLICATION_SCOPE));
        getWebMockObjectFactory().getMockRequest().setAttribute("requestKey1", "requestValue1");
        getWebMockObjectFactory().getMockSession().setAttribute("sessionKey1", "sessionValue1");
        pageContext.setServletRequest(null);
        pageContext.removeAttribute("requestKey1", PageContext.REQUEST_SCOPE);
        pageContext.removeAttribute("sessionKey1", PageContext.SESSION_SCOPE);
        assertNotNull(getWebMockObjectFactory().getMockRequest().getAttribute("requestKey1"));
        assertNotNull(getWebMockObjectFactory().getMockSession().getAttribute("sessionKey1"));
        getWebMockObjectFactory().getMockRequest().setSession(null);
        pageContext.setServletRequest(getWebMockObjectFactory().getMockRequest());
        pageContext.removeAttribute("requestKey1", PageContext.REQUEST_SCOPE);
        pageContext.removeAttribute("sessionKey1", PageContext.SESSION_SCOPE);
        assertNull(getWebMockObjectFactory().getMockRequest().getAttribute("requestKey1"));
        assertNotNull(getWebMockObjectFactory().getMockSession().getAttribute("sessionKey1"));
        getWebMockObjectFactory().getMockServletContext().setAttribute("contextKey1", "contextValue1");
        pageContext.setServletConfig(null);
        pageContext.removeAttribute("contextKey1", PageContext.APPLICATION_SCOPE);
        assertNotNull(getWebMockObjectFactory().getMockServletContext().getAttribute("contextKey1"));
        try
        {
            pageContext.removeAttribute("requestKey", 60000);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
    }
    
    public void testFindAttribute()
    {
        assertNull(pageContext.findAttribute("attribute"));
        getWebMockObjectFactory().getMockServletContext().setAttribute("attribute", "contextValue");
        assertEquals("contextValue", pageContext.findAttribute("attribute"));
        getWebMockObjectFactory().getMockSession().setAttribute("attribute", "sessionValue");
        assertEquals("sessionValue", pageContext.findAttribute("attribute"));
        getWebMockObjectFactory().getMockRequest().setAttribute("attribute", "requestValue");
        assertEquals("requestValue", pageContext.findAttribute("attribute"));
        pageContext.setAttribute("attribute", "pageValue");
        assertEquals("pageValue", pageContext.findAttribute("attribute"));
        pageContext.setAttribute("attribute", null);
        pageContext.setServletRequest(null);
        assertEquals("contextValue", pageContext.findAttribute("attribute"));
        pageContext.setServletConfig(null);
        assertNull(pageContext.findAttribute("attribute"));
    }
    
    public void testGetAttributesScope()
    {
        assertEquals(0, pageContext.getAttributesScope("attribute"));
        getWebMockObjectFactory().getMockServletContext().setAttribute("attribute", "contextValue");
        assertEquals(PageContext.APPLICATION_SCOPE, pageContext.getAttributesScope("attribute"));
        getWebMockObjectFactory().getMockSession().setAttribute("attribute", "sessionValue");
        assertEquals(PageContext.SESSION_SCOPE, pageContext.getAttributesScope("attribute"));
        getWebMockObjectFactory().getMockRequest().setAttribute("attribute", "requestValue");
        assertEquals(PageContext.REQUEST_SCOPE, pageContext.getAttributesScope("attribute"));
        pageContext.setAttribute("attribute", "pageValue");
        assertEquals(PageContext.PAGE_SCOPE, pageContext.getAttributesScope("attribute"));
        pageContext.setAttribute("attribute", null);
        pageContext.setServletRequest(null);
        assertEquals(PageContext.APPLICATION_SCOPE, pageContext.getAttributesScope("attribute"));
        pageContext.setServletConfig(null);
        assertEquals(0, pageContext.getAttributesScope("attribute"));
    }
    
    public void testGetAttributeNamesInScope()
    {
        getWebMockObjectFactory().getMockRequest().clearAttributes();
        assertFalse(pageContext.getAttributeNamesInScope(PageContext.PAGE_SCOPE).hasMoreElements());
        assertFalse(pageContext.getAttributeNamesInScope(PageContext.REQUEST_SCOPE).hasMoreElements());
        assertFalse(pageContext.getAttributeNamesInScope(PageContext.SESSION_SCOPE).hasMoreElements());
        assertFalse(pageContext.getAttributeNamesInScope(PageContext.APPLICATION_SCOPE).hasMoreElements());
        pageContext.setAttribute("pageKey", "pageValue");
        getWebMockObjectFactory().getMockRequest().setAttribute("requestKey", "requestValue");
        getWebMockObjectFactory().getMockSession().setAttribute("sessionKey", "sessionValue");
        getWebMockObjectFactory().getMockServletContext().setAttribute("contextKey", "contextValue");
        Enumeration pageEnumeration = pageContext.getAttributeNamesInScope(PageContext.PAGE_SCOPE);
        assertEquals("pageKey", pageEnumeration.nextElement());
        assertFalse(pageEnumeration.hasMoreElements());
        Enumeration requestEnumeration = pageContext.getAttributeNamesInScope(PageContext.REQUEST_SCOPE);
        assertEquals("requestKey", requestEnumeration.nextElement());
        assertFalse(requestEnumeration.hasMoreElements());
        Enumeration sessionEnumeration = pageContext.getAttributeNamesInScope(PageContext.SESSION_SCOPE);
        assertEquals("sessionKey", sessionEnumeration.nextElement());
        assertFalse(sessionEnumeration.hasMoreElements());
        Enumeration contextEnumeration = pageContext.getAttributeNamesInScope(PageContext.APPLICATION_SCOPE);
        assertEquals("contextKey", contextEnumeration.nextElement());
        assertFalse(contextEnumeration.hasMoreElements());
        pageContext.setServletRequest(null);
        assertFalse(pageContext.getAttributeNamesInScope(PageContext.REQUEST_SCOPE).hasMoreElements());
        assertFalse(pageContext.getAttributeNamesInScope(PageContext.SESSION_SCOPE).hasMoreElements());
        pageContext.setServletConfig(null);
        assertFalse(pageContext.getAttributeNamesInScope(PageContext.APPLICATION_SCOPE).hasMoreElements());
        try
        {
            pageContext.getAttributeNamesInScope(60000);
            fail();
        } 
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
    }
    
    public void testForward() throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        pageContext.setServletRequest(request);
        pageContext.setServletResponse(response);
        pageContext.forward("aPath");
        MockRequestDispatcher dispatcher = (MockRequestDispatcher)request.getRequestDispatcher("aPath");
        assertSame(request, dispatcher.getForwardedRequest());
        assertSame(response, dispatcher.getForwardedResponse());
    }
    
    public void testInclude() throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        pageContext.setServletRequest(request);
        pageContext.setServletResponse(response);
        pageContext.include("aPath");
        MockRequestDispatcher dispatcher = (MockRequestDispatcher)request.getRequestDispatcher("aPath");
        assertSame(request, dispatcher.getIncludedRequest());
        assertSame(response, dispatcher.getIncludedResponse());
    }
    
    public void testIncludeWithFlush() throws Exception
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        TestJspWriter jspWriter = new TestJspWriter();
        pageContext.setServletRequest(request);
        pageContext.setServletResponse(response);
        pageContext.setJspWriter(jspWriter);
        pageContext.include("aPath", false);
        MockRequestDispatcher dispatcher = (MockRequestDispatcher)request.getRequestDispatcher("aPath");
        assertSame(request, dispatcher.getIncludedRequest());
        assertSame(response, dispatcher.getIncludedResponse());
        assertFalse(jspWriter.isFlushed());
        pageContext.include("aPath", true);
        assertTrue(jspWriter.isFlushed());
    }
    
    public void testExpressionEvaluatorAndVariable() throws Exception
    {
        assertTrue(pageContext.getExpressionEvaluator() instanceof MockExpressionEvaluator);
        MockExpressionEvaluator evaluator = new MockExpressionEvaluator() {};
        pageContext.setExpressionEvaluator(evaluator);
        assertSame(evaluator, pageContext.getExpressionEvaluator());
        assertTrue(pageContext.getVariableResolver() instanceof MockVariableResolver);
        MockVariableResolver resolver = new MockVariableResolver() {};
        pageContext.setVariableResolver(resolver);
        assertSame(resolver, pageContext.getVariableResolver());
    }
    
    private class TestJspWriter extends MockJspWriter
    {
        private boolean flushed = false;
        
        public boolean isFlushed()
        {
            return flushed;
        }
        
        public void flush() throws IOException
        {
            super.flush();
            flushed = true;
        }
    }
}
