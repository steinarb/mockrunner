package com.mockrunner.mock;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Mock implementation of <code>ActionMapping</code>.
 */
public class MockActionMapping extends ActionMapping
{
    private String parameter;
    private String actionClass;
    private String attribute;
    private String formAttribute;
    private String formClass;
    private String formPrefix;
    private String formScope;
    private String formSuffix;
    private String forward;
    private String include;
    private String input;
    private String inputForm;
    private String multipartClass;
    private String name;
    private String path;
    private String prefix;
    private String scope;
    private String suffix;
    private String type;
    private boolean validate;
    private boolean unknown;
    private ArrayList forwards;

    public MockActionMapping()
    {
        super();
        forwards = new ArrayList();
    }

    public String getActionClass()
    {
        return actionClass;
    }

    public void setActionClass(String actionClass)
    {
        this.actionClass = actionClass;
    }

    public String getAttribute()
    {
        return attribute;
    }

    public void setAttribute(String attribute)
    {
        this.attribute = attribute;
    }

    public String getFormAttribute()
    {
        return formAttribute;
    }

    public void setFormAttribute(String formAttribute)
    {
        this.formAttribute = formAttribute;
    }

    public String getFormClass()
    {
        return formClass;
    }

    public void setFormClass(String formClass)
    {
        this.formClass = formClass;
    }

    public String getFormPrefix()
    {
        return formPrefix;
    }

    public void setFormPrefix(String formPrefix)
    {
        this.formPrefix = formPrefix;
    }

    public String getFormScope()
    {
        return formScope;
    }

    public void setFormScope(String formScope)
    {
        this.formScope = formScope;
    }

    public String getFormSuffix()
    {
        return formSuffix;
    }

    public void setFormSuffix(String formSuffix)
    {
        this.formSuffix = formSuffix;
    }

    public String getForward()
    {
        return forward;
    }

    public void setForward(String forward)
    {
        this.forward = forward;
    }

    public String getInclude()
    {
        return include;
    }

    public void setInclude(String include)
    {
        this.include = include;
    }

    public String getInput()
    {
        return input;
    }

    public void setInput(String input)
    {
        this.input = input;
    }

    public String getInputForm()
    {
        return inputForm;
    }

    public void setInputForm(String inputForm)
    {
        this.inputForm = inputForm;
    }
    
    public String getMultipartClass()
    {
        return multipartClass;
    }

    public void setMultipartClass(String multipartClass)
    {
        this.multipartClass = multipartClass;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getParameter()
    {
        return parameter;
    }

    public void setParameter(String parameter)
    {
        this.parameter = parameter;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public boolean getUnknown()
    {
        return unknown;
    }

    public void setUnknown(boolean unknown)
    {
        this.unknown = unknown;
    }

    public boolean getValidate()
    {
        return validate;
    }

    public void setValidate(boolean validate)
    {
        this.validate = validate;
    }

    public void addForward(ActionForward forward)
    {
        throw new UnsupportedOperationException();
    }

    public ActionForm createFormInstance()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Always return a valid <code>ActionForward</code>
     * since we do not care if it exists in the
     * struts-config. The action want to have it, so
     * return it. We even do not care about forward name
     * and path. It's the same here since we do not have
     * a struts-config for an approriate mapping name ->
     * path.
     */
    public ActionForward findForward(String name)
    {
        return new MockActionForward(name, name, false);
    }
    
    /**
     * If the action wants to see special forwards,
     * specify them here.
     */
    public void setupForwards(String[] forwards)
    {
        if(null == forwards) return;
        for(int ii = 0; ii < forwards.length; ii++)
        {
            this.forwards.add(forwards[ii]);
        }
    }

    public String[] findForwards()
    {
        return (String[])forwards.toArray(new String[forwards.size()]);
    }
    
    /**
     * Always return a valid <code>ActionForward</code>
     * since we do not care if it exists in the
     * struts-config. The action wants to have it, so
     * return it. We even do not care about forward name
     * and path. It's the same here since we do not have
     * a struts-config for an approriate mapping name ->
     * path.
     */
    public ActionForward getInputForward()
    {
        return new MockActionForward(input, input, false);
    }

    public String toString()
    {
        return "ActionConfig[path=" + getPath() + ",name=" + getFormClass() + ",scope=" + getScope() + ",type=" + getActionClass();
    }
}
