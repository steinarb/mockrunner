package com.mockrunner.test.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Element;

import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.WebTestModule;
import com.mockrunner.mock.web.MockActionForward;
import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.servlet.ServletTestModule;
import com.mockrunner.struts.ActionTestModule;
import com.mockrunner.tag.TagTestModule;
import com.mockrunner.util.common.StreamUtil;

import junit.framework.TestCase;

public class HTMLOutputModuleTest extends TestCase
{
    private final static String testHTML = "<html><body><tag></body></html>";
    private WebMockObjectFactory webfactory;
    
     
    protected void setUp() throws Exception
    {
        super.setUp();
        webfactory = new WebMockObjectFactory();
    }
    
    public void testActionTestModuleOutput()
    {
        ActionTestModule module = new ActionTestModule(webfactory);
        module.actionPerform(TestOutputAction.class);
        doTestOutputAsString(module);
        doTestOutputAsBufferedReader(module);
        doTestOutputAsJDOMDocument(module);      
    }
    
    public void testActionTestModuleAttributes()
    {
        ActionTestModule module = new ActionTestModule(webfactory);
        doTestAttributes(module);
    }

    public void testServletTestModuleOutput()
    {
        ServletTestModule module = new ServletTestModule(webfactory);
        module.createServlet(TestOutputServlet.class);
        module.doGet();
        doTestOutputAsString(module);
        doTestOutputAsBufferedReader(module);
        doTestOutputAsJDOMDocument(module);      
    }
    
    public void testServletTestModuleAttributes()
    {
        ServletTestModule module = new ServletTestModule(webfactory);
        doTestAttributes(module);
    }
    
    public void testTagTestModuleOutput()
    {
        TagTestModule module = new TagTestModule(webfactory);
        module.createTag(TestOutputTag.class);
        module.doStartTag();
        doTestOutputAsString(module);
        doTestOutputAsBufferedReader(module);
        doTestOutputAsJDOMDocument(module);      
    }
    
    public void testTagTestModuleAttributes()
    {
        TagTestModule module = new TagTestModule(webfactory);
        doTestAttributes(module);
    }
    
    private void doTestOutputAsString(HTMLOutputModule module)
    {
        assertEquals(testHTML, module.getOutput());
    }
    
    private void doTestOutputAsBufferedReader(HTMLOutputModule module)
    {
        assertEquals(testHTML, StreamUtil.getReaderAsString(module.getOutputAsBufferedReader()));
    }
    
    private void doTestOutputAsJDOMDocument(HTMLOutputModule module)
    {
        Element root = module.getOutputAsJDOMDocument().getRootElement();
        assertEquals("html", root.getName());
        Element body = root.getChild("body");
        assertNotNull(body);
        Element tag = body.getChild("tag");
        assertNotNull(tag);
    }
    
    private void doTestAttributes(WebTestModule module)
    {
        module.setSessionAttribute("sessionatt", new Integer(3));
        module.addRequestParameter("requestparam");
        module.setRequestAttribute("requestatt", "xyz");
        assertEquals(new Integer(3), webfactory.getMockSession().getAttribute("sessionatt"));
        assertEquals("", webfactory.getMockRequest().getParameter("requestparam"));
        assertEquals("xyz", webfactory.getMockRequest().getAttribute("requestatt"));
    }
    
    public static class TestOutputAction extends Action
    {
        
        public ActionForward execute(ActionMapping mspping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
        {
            response.getWriter().write(testHTML);
            return new MockActionForward();
        }
    }
    
    public static class TestOutputServlet extends HttpServlet
    {
        
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            response.getWriter().write(testHTML);
        }
    }
    
    public static class TestOutputTag extends TagSupport
    {
        public int doStartTag() throws JspException
        {
            try
            {
                pageContext.getOut().print(testHTML);
            }
            catch(IOException exc)
            {
                throw new RuntimeException(exc.getMessage());
            }
            return TagSupport.SKIP_BODY;
        }
    }
}
