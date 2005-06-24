package com.mockrunner.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public interface ExceptionHandlerConfig
{
    public boolean canHandle(Throwable exception);
    
    public Object handle(Throwable exception, ActionMapping mapping, ActionForm formInstance, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
