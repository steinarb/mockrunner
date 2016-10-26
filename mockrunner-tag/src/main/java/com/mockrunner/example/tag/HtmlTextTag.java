package com.mockrunner.example.tag;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * This tag renders an HTMl text input field with the provided
 * name and value. The value is determined using a deferred value
 * expression.
 */
public class HtmlTextTag extends SimpleTagSupport
{
    private String name;
    private ValueExpression value;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ValueExpression getValue()
    {
        return value;
    }

    public void setValue(ValueExpression value)
    {
        this.value = value;
    }

    public void doTag() throws JspException, IOException
    {
        String html = "<input type=\"text\" " +
                "name=\"" +
                name +
                "\" " +
                "value=\"" +
                value.getValue(getJspContext().getELContext()) +
                "\"" +
                "/>";
        getJspContext().getOut().print(html);
    }
}
