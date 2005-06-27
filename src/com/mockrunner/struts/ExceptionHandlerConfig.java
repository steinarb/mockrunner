package com.mockrunner.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public interface ExceptionHandlerConfig
{
    public boolean canHandle(Exception exception);
    
    public Object handle(Exception exception, ActionMapping mapping, ActionForm formInstance, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
