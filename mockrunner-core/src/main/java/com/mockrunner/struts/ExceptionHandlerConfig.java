package com.mockrunner.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Generic interface for exception handlers. The default implementation
 * is {@link DefaultExceptionHandlerConfig} and uses the Struts
 * exception handling mechanism. In special cases, you may provide your own 
 * implementations of this interface, that may be independent from
 * the Struts exception handling mechanism.
 * Exception handler are called if an action throws an exception. 
 * Use {@link ActionTestModule#addExceptionHandler} to register an exception handler.
 */
public interface ExceptionHandlerConfig
{
    /**
     * Returns if this handler is able to handle the exception.
     * @return <code>true</code> if this handler is able to handle the exception,
     *         <code>false</code> otherwise
     */
    public boolean canHandle(Exception exception);
    
    /**
     * Handles the exception.
     * @param exception the exception
     * @param mapping the current <code>ActionMapping</code>
     * @param form the current <code>ActionForm</code>
     * @param request the current request
     * @param response the current response
     * @return the handler return value, usually an <code>ActionForward</code>,
     *         but may be any object
     */
    public Object handle(Exception exception, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
