package de.mockrunner.test;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Simple form used in the tests
 */
public class TestForm extends ActionForm
{
    private boolean validationOk = true;
    private HashMap mappedProperties = new HashMap();
    private String property;
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
