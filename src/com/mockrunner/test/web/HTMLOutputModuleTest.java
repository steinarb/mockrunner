package com.mockrunner.test.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import junit.framework.TestCase;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Element;

import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.base.WebTestModule;
import com.mockrunner.mock.web.ActionMockObjectFactory;
import com.mockrunner.mock.web.MockActionForward;
import com.mockrunner.servlet.ServletTestModule;
import com.mockrunner.struts.ActionTestModule;
import com.mockrunner.tag.TagTestModule;
import com.mockrunner.util.common.StreamUtil;

public class HTMLOutputModuleTest extends TestCase
{
    private final static String testHTML = "<html><body><tag></body></html>";
    private ActionMockObjectFactory actionWebFactory;
    
     
    protected void setUp() throws Exception
    {
        super.setUp();
        actionWebFactory = new ActionMockObjectFactory();
    }
    
    public void testActionTestModuleOutput()
    {
        ActionTestModule module = new ActionTestModule(actionWebFactory);
        module.actionPerform(TestOutputAction.class);
        doTestOutputAsString(module);
        doTestOutputAsBufferedReader(module);
        doTestOutputAsJDOMDocument(module);
        doTestVerifyOutput(module);
        doTestVerifyOutputContains(module);
        doTestVerifyOutputRegularExpression(module);
    }
    
    public void testActionTestModuleAttributes()
    {
        ActionTestModule module = new ActionTestModule(actionWebFactory);
        doTestAttributes(module);
    }

    public void testServletTestModuleOutput()
    {
        ServletTestModule module = new ServletTestModule(actionWebFactory);
        module.createServlet(TestOutputServlet.class);
        module.doGet();
        doTestOutputAsString(module);
        doTestOutputAsBufferedReader(module);
        doTestOutputAsJDOMDocument(module);
        doTestVerifyOutput(module);
        doTestVerifyOutputContains(module);
        doTestVerifyOutputRegularExpression(module);
    }
    
    public void testServletTestModuleAttributes()
    {
        ServletTestModule module = new ServletTestModule(actionWebFactory);
        doTestAttributes(module);
    }
    
    public void testTagTestModuleOutput()
    {
        TagTestModule module = new TagTestModule(actionWebFactory);
        module.createTag(TestOutputTag.class);
        module.doStartTag();
        doTestOutputAsString(module);
        doTestOutputAsBufferedReader(module);
        doTestOutputAsJDOMDocument(module);
        doTestVerifyOutput(module);
        doTestVerifyOutputContains(module);
        doTestVerifyOutputRegularExpression(module);
    }
    
    public void testTagTestModuleAttributes()
    {
        TagTestModule module = new TagTestModule(actionWebFactory);
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
    
    private void doTestVerifyOutput(HTMLOutputModule module)
    {
        module.setCaseSensitive(true);
        module.verifyOutput("<html><body><tag></body></html>");
        try
        {
            module.verifyOutput("<HTml><body><tAg></body></html>");
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setCaseSensitive(false);
        module.verifyOutput("<HTml><body><tAg></body></html>");
        try
        {
            module.verifyOutput("xyz");
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    private void doTestVerifyOutputContains(HTMLOutputModule module)
    {
        module.setCaseSensitive(true);
        module.verifyOutputContains("<body>");
        try
        {
            module.verifyOutputContains("<BODY>");
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setCaseSensitive(false);
        module.verifyOutputContains("<BODY>");
        try
        {
            module.verifyOutputContains("boddy");
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    private void doTestVerifyOutputRegularExpression(HTMLOutputModule module)
    {
        module.setCaseSensitive(true);
        module.verifyOutputRegularExpression(".*<body.*");
        try
        {
            module.verifyOutputRegularExpression(".*<BO.*");
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setCaseSensitive(false);
        module.verifyOutputRegularExpression(".*<BO.*");
        try
        {
            module.verifyOutputRegularExpression(".*<BOG.*");
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    private void doTestAttributes(WebTestModule module)
    {
        module.setSessionAttribute("sessionatt", new Integer(3));
        module.addRequestParameter("requestparam");
        module.setRequestAttribute("requestatt", "xyz");
        assertEquals(new Integer(3), actionWebFactory.getMockSession().getAttribute("sessionatt"));
        assertEquals("", actionWebFactory.getMockRequest().getParameter("requestparam"));
        assertEquals("xyz", actionWebFactory.getMockRequest().getAttribute("requestatt"));
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
