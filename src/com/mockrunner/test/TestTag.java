package com.mockrunner.test;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class TestTag extends TagSupport
{
    private String testString;
    private Integer testInteger;
    private double testDouble;
    private boolean releaseCalled = false;
    private PageContext context;

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

    public void release()
    {
        releaseCalled = true;
    }

    public boolean getReleaseCalled()
    {
        return releaseCalled;
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
}
