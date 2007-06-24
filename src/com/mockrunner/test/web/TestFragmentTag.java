package com.mockrunner.test.web;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.TagSupport;

public class TestFragmentTag extends TagSupport
{
    private JspFragment testFragment;

    public void setTestFragment(JspFragment testFragment)
    {
        this.testFragment = testFragment;
    }

    public int doStartTag() throws JspException
    {
        try
        {
            StringWriter writer = new StringWriter();
            testFragment.invoke(writer);
            pageContext.getOut().print(writer.toString());
        }
        catch(IOException exc)
        {
            throw new RuntimeException(exc.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }
    
    
}
