package com.mockrunner.test.web;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

public class TestBodyTag extends BodyTagSupport
{
    private String testString;
    private Integer testInteger;
    private double testDouble;
    private PageContext context;
    private JspWriter bufferedOut;
    private boolean releaseCalled = false;
    private int doStartTagReturnValue = TagSupport.EVAL_BODY_INCLUDE;
    private int doEndTagReturnValue = TagSupport.EVAL_PAGE;
    private int doAfterBodyReturnValue = TagSupport.SKIP_BODY;
    private boolean doStartTagCalled = false;
    private boolean doEndTagCalled = false;
    private boolean doAfterBodyCalled = false;
    private boolean doInitBodyCalled = false;
    
    public void setDoAfterBodyReturnValue(int doAfterBodyReturnValue)
    {
        this.doAfterBodyReturnValue = doAfterBodyReturnValue;
    }

    public void setDoEndTagReturnValue(int doEndTagReturnValue)
    {
        this.doEndTagReturnValue = doEndTagReturnValue;
    }

    public void setDoStartTagReturnValue(int doStartTagReturnValue)
    {
        this.doStartTagReturnValue = doStartTagReturnValue;
    }

    public int doStartTag() throws JspException
    {
        doStartTagCalled = true;
        try
        {
            pageContext.getOut().print("TestBodyTag");
        }
        catch(IOException exc)
        {
            throw new RuntimeException(exc.getMessage());
        }
        return doStartTagReturnValue;
    }
    
    public void doInitBody() throws JspException
    {
        doInitBodyCalled = true;
        bufferedOut = pageContext.getOut();
    }

    public int doAfterBody() throws JspException
    {
        doAfterBodyCalled = true;
        int returnValue = doAfterBodyReturnValue;
        if(BodyTagSupport.EVAL_BODY_AGAIN == doAfterBodyReturnValue)
        {
            doAfterBodyReturnValue = BodyTagSupport.SKIP_BODY;      
        }
        return returnValue;
    }

    public int doEndTag() throws JspException
    {
        doEndTagCalled = true;
        return doEndTagReturnValue;
    }

    public double getTestDouble()
    {
        return testDouble;
    }

    public Integer getTestInteger()
    {
        return testInteger;
    }

    public String getTestString()
    {
        return testString;
    }

    public void setTestDouble(double testDouble)
    {
        this.testDouble = testDouble;
    }

    public void setTestInteger(Integer testInteger)
    {
        this.testInteger = testInteger;
    }

    public void setTestString(String testString)
    {
        this.testString = testString;
    }
    
    public void setPageContext(PageContext context)
    {
        super.setPageContext(context);
        this.context = context;
    }

    public PageContext getPageContext()
    {
        return context;
    }
    
    public JspWriter getBufferedOut()
    {
        return bufferedOut;
    }
    
    public void release()
    {
        releaseCalled = true;
    }
    
    public boolean wasReleaseCalled()
    {
        return releaseCalled;
    }
    
    public boolean wasDoInitBodyCalled()
    {
        return doInitBodyCalled;
    }

    public boolean wasDoAfterBodyCalled()
    {
        return doAfterBodyCalled;
    }

    public boolean wasDoEndTagCalled()
    {
        return doEndTagCalled;
    }

    public boolean wasDoStartTagCalled()
    {
        return doStartTagCalled;
    }
}
