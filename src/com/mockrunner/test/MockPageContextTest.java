package com.mockrunner.test;

import java.io.StringWriter;

import javax.servlet.jsp.JspWriter;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.MockBodyContent;
import com.mockrunner.mock.MockJspWriter;
import com.mockrunner.mock.MockPageContext;

public class MockPageContextTest extends BaseTestCase
{
    private MockPageContext pageContext;
        
    protected void setUp() throws Exception
    {
        super.setUp();
        pageContext = getMockObjectFactory().getMockPageContext();
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
}
