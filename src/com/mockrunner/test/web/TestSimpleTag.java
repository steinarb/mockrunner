package com.mockrunner.test.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.mockrunner.tag.DynamicAttribute;

public class TestSimpleTag extends SimpleTagSupport implements DynamicAttributes
{
    private boolean booleanProperty;
    private float floatProperty;
    private String stringProperty;
    private String testString;
    private boolean doTagCalled = false;
    private Map dynamicAttributes = new HashMap();
    
    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException
    {
        dynamicAttributes.put(localName, new DynamicAttribute(uri, value));
    }
    
    public void clearDynamicAttributes()
    {
        dynamicAttributes.clear();
    }
    
    public Map getDynamicAttributesMap()
    {
        return dynamicAttributes;
    }
    
    public void doTag() throws JspException, IOException
    {
        getJspContext().getOut().print("TestSimpleTag");
        doTagCalled = true;
    }
    
    public JspContext getJspContext()
    {
        return super.getJspContext();
    }
    
    public boolean wasDoTagCalled()
    {
        return doTagCalled;
    }
    
    public boolean getBooleanProperty()
    {
        return booleanProperty;
    }
    
    public void setBooleanProperty(boolean booleanProperty)
    {
        this.booleanProperty = booleanProperty;
    }
    
    public float getFloatProperty()
    {
        return floatProperty;
    }
    
    public void setFloatProperty(float floatProperty)
    {
        this.floatProperty = floatProperty;
    }
    
    public String getStringProperty()
    {
        return stringProperty;
    }
    
    public void setStringProperty(String stringProperty)
    {
        this.stringProperty = stringProperty;
    }
    
    public String getTestString()
    {
        return testString;
    }

    public void setTestString(String testString)
    {
        this.testString = testString;
    }
}
