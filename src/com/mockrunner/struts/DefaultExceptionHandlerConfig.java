package com.mockrunner.struts;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import com.mockrunner.base.NestedApplicationException;

/**
 * The default implementation of {@link ExceptionHandlerConfig}. It uses the Struts
 * exception handling mechanism. Use the various constructors to provide your subclass
 * of <code>ExceptionHandler</code> or to configure exception handling using an
 * instance of <code>ExceptionConfig</code>. The <code>ExceptionConfig</code> class
 * allows you to set the handler class an exception type.
 * Use {@link ActionTestModule#addExceptionHandler} to register an exception handler.
 */
public class DefaultExceptionHandlerConfig implements ExceptionHandlerConfig
{
    private ExceptionConfig exceptionConfig;
    private ExceptionHandler exceptionHandler;
    private Class exceptionClass;
    
    public DefaultExceptionHandlerConfig(ExceptionConfig exceptionConfig)
    {
        this.exceptionConfig = exceptionConfig;
        try
        {
            Class handlerClass = exceptionConfig.getClass().getClassLoader().loadClass(exceptionConfig.getHandler());
            exceptionHandler = (ExceptionHandler)handlerClass.newInstance();
            exceptionClass = exceptionConfig.getClass().getClassLoader().loadClass(exceptionConfig.getType());
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    public DefaultExceptionHandlerConfig(ExceptionHandler exceptionHandler, ExceptionConfig exceptionConfig)
    {
        this.exceptionHandler = exceptionHandler;
        this.exceptionConfig = exceptionConfig;
        this.exceptionConfig.setHandler(exceptionHandler.getClass().getName());
        try
        {
            exceptionClass = exceptionConfig.getClass().getClassLoader().loadClass(exceptionConfig.getType());
        } 
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    public DefaultExceptionHandlerConfig(ExceptionHandler exceptionHandler, Class exceptionClass)
    {
        this.exceptionHandler = exceptionHandler;
        this.exceptionClass = exceptionClass;
        this.exceptionConfig = new ExceptionConfig();
        this.exceptionConfig.setHandler(exceptionHandler.getClass().getName());
        this.exceptionConfig.setType(exceptionClass.getName());
    }
    
    public DefaultExceptionHandlerConfig(Class exceptionClass)
    {
        this.exceptionHandler = new ExceptionHandler();
        this.exceptionClass = exceptionClass;
        this.exceptionConfig = new ExceptionConfig();
        this.exceptionConfig.setHandler(ExceptionHandler.class.getName());
        this.exceptionConfig.setType(exceptionClass.getName());
    }
    
    public boolean canHandle(Exception exception)
    {
        return exceptionClass.isInstance(exception);
    }

    public Object handle(Exception exception, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        if(!canHandle(exception)) return null;
        if(null == exceptionHandler) return null;
        return exceptionHandler.execute((Exception)exception, exceptionConfig, mapping, form, request, response);
    }

    /**
     * Get the underlying <code>ExceptionConfig</code>. If you did not provide
     * an instance of <code>ExceptionConfig</code>, this class will create one
     * internally.
     * @return the <code>ExceptionConfig</code>
     */
    public ExceptionConfig getExceptionConfig()
    {
        return exceptionConfig;
    }

    /**
     * Get the underlying <code>ExceptionHandler</code>.
     * @return the <code>ExceptionHandler</code>
     */
    public ExceptionHandler getExceptionHandler()
    {
        return exceptionHandler;
    }
}
