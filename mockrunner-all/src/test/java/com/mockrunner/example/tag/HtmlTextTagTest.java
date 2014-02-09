package com.mockrunner.example.tag;

import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

import org.junit.Before;
import org.junit.Test;

import com.mockrunner.mock.web.JasperJspFactory;
import com.mockrunner.tag.BasicTagTestCaseAdapter;

/**
 * Example test for the {@link HtmlTextTag}.
 * The example demonstrates how to use deferred expressions
 * with the <code>#{...}</code> syntax. Mockrunner supports the
 * evaluation of expressions with the <code>${...}</code>
 * and <code>#{...}</code> syntax. You have to set 
 * {@link com.mockrunner.mock.web.JasperJspFactory} as the default
 * JSP factory. The {@link com.mockrunner.mock.web.MockJspFactory},
 * which is the predefined JSP factory, does not support the
 * evaluation of expressions. Expression evaluation only works
 * with the unified Expression Language API introduced with JSP 2.1.
 */
public class HtmlTextTagTest extends BasicTagTestCaseAdapter
{
	@Before
    public void setUp() throws Exception
    {
        super.setUp();
        getWebMockObjectFactory().setDefaultJspFactory(new JasperJspFactory().configure(getWebMockObjectFactory()));
    }
    
    private ValueExpression prepareExpression(String expression)
    {
        JspApplicationContext applicationContext = JspFactory.getDefaultFactory().getJspApplicationContext(getWebMockObjectFactory().getMockPageContext().getServletContext());
        return applicationContext.getExpressionFactory().createValueExpression(getWebMockObjectFactory().getMockPageContext().getELContext(), expression, String.class);
    }
    
    @Test
    public void testDoTag()
    {
        Map attributeMap = new HashMap();
        attributeMap.put("name", "myname");
        attributeMap.put("value", prepareExpression("#{person.name}"));
        createNestedTag(HtmlTextTag.class, attributeMap);
        Person person = new Person();
        person.setName("Jane");
        getWebMockObjectFactory().getMockSession().setAttribute("person", person);
        processTagLifecycle();
        verifyOutput("<input type=\"text\" name=\"myname\" value=\"Jane\"/>");
    }
}
