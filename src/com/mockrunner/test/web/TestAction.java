package com.mockrunner.test.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

public class TestAction extends Action
{
    private MessageResources resources;
    private MessageResources resourcesForKey;
    private Locale locale;
    
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
    {
        resources = getResources(request);
        resourcesForKey = getResources(request, "test");
        locale = getLocale(request);
        return mapping.findForward("success");
    }
    
    public MessageResources getTestResourcesForKey()
    {
        return resourcesForKey;
    }
    
    public MessageResources getTestResources()
    {
        return resources;
    }
    
    public Locale getTestLocale()
    {
        return locale;
    }
}
