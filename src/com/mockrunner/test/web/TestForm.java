package com.mockrunner.test.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class TestForm extends ActionForm
{
    private boolean validationOk = true;
    private Map mappedProperties = new HashMap();
    private String property;
    private Map indexedProperties = new HashMap();
    private boolean resetCalled = false;
  
    public void setValidationOk(boolean validationOk)
    {
        this.validationOk = validationOk;
    }
    
    public String getProperty()
    {
        return property;
    }

    public void setProperty(String string)
    {
        property = string;
    }
    
    public String getIndexedProperty(int index)
    {
        return (String)indexedProperties.get(new Integer(index));
    }

    public void setIndexedProperty(int index, String string)
    {
        indexedProperties.put(new Integer(index), string);
    }

    public Object getValue(String name)
    {
        return mappedProperties.get(name);
    }
    
    public void setValue(String name, Object value)
    {
        mappedProperties.put(name, value);
    }
    
    public boolean wasResetCalled()
    {
        return resetCalled;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        super.reset(mapping, request);
        resetCalled = true;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        if(!validationOk)
        {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("testkey"));
        }
        return errors;
    } 
}
