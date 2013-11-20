package com.mockrunner.test.web;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import com.mockrunner.mock.web.MockActionForward;
import com.mockrunner.mock.web.MockActionMapping;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.struts.DefaultExceptionHandlerConfig;

public class DefaultExceptionHandlerConfigTest extends TestCase
{
    public void testConfigConstructor() throws Exception
    {
        ExceptionConfig config = new ExceptionConfig();
        config.setHandler(TestExceptionHandler.class.getName());
        config.setType(IOException.class.getName());
        DefaultExceptionHandlerConfig handlerConfig = new DefaultExceptionHandlerConfig(config);
        doTestCanHandle(handlerConfig);
        doTestHandle(handlerConfig);
        assertSame(config, handlerConfig.getExceptionConfig());
        assertTrue(handlerConfig.getExceptionHandler() instanceof TestExceptionHandler);
    }
    
    public void testConfigAndHandlerConstructor() throws Exception
    {
        ExceptionConfig config = new ExceptionConfig();
        TestExceptionHandler handler = new TestExceptionHandler();
        config.setType(IOException.class.getName());
        DefaultExceptionHandlerConfig handlerConfig = new DefaultExceptionHandlerConfig(handler, config);
        doTestCanHandle(handlerConfig);
        doTestHandle(handlerConfig);
        assertSame(config, handlerConfig.getExceptionConfig());
        assertSame(handler, handlerConfig.getExceptionHandler());
        assertSame(TestExceptionHandler.class.getName(), config.getHandler());
    }
    
    public void testExceptionAndHandlerConstructor() throws Exception
    {
        TestExceptionHandler handler = new TestExceptionHandler();
        DefaultExceptionHandlerConfig handlerConfig = new DefaultExceptionHandlerConfig(handler, IOException.class);
        doTestCanHandle(handlerConfig);
        doTestHandle(handlerConfig);
        ExceptionConfig config = handlerConfig.getExceptionConfig();
        assertSame(TestExceptionHandler.class.getName(), config.getHandler());
        assertSame(IOException.class.getName(), config.getType());
    }
    
    public void testExceptionConstructor() throws Exception
    {
        DefaultExceptionHandlerConfig handlerConfig = new DefaultExceptionHandlerConfig(IOException.class);
        doTestCanHandle(handlerConfig);
        ExceptionConfig config = handlerConfig.getExceptionConfig();
        assertSame(ExceptionHandler.class.getName(), config.getHandler());
        assertSame(IOException.class.getName(), config.getType());
    }
    
    private void doTestCanHandle(DefaultExceptionHandlerConfig config)
    {
        assertFalse(config.canHandle(null));
        assertFalse(config.canHandle(new Exception()));
        assertFalse(config.canHandle(new IllegalArgumentException()));
        assertTrue(config.canHandle(new IOException()));
        assertTrue(config.canHandle(new RemoteException()));
    }
    
    private void doTestHandle(DefaultExceptionHandlerConfig config) throws Exception
    {
        MockActionMapping mapping = new MockActionMapping();
        ActionForm form = new DynaActionForm();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertNull(config.handle(null, mapping, form, request, response));
        doTestNotCalled(config);
        assertNull(config.handle(new Exception(), mapping, form, request, response));
        Exception exception = new RemoteException();
        ActionForward forward = (ActionForward)config.handle(exception, mapping, form, request, response);
        doTestCalled(config, exception, mapping, form, request, response);
        assertTrue(forward instanceof MockActionForward);
        assertEquals("testname", ((MockActionForward)forward).getName());
    }
    
    private void doTestNotCalled(DefaultExceptionHandlerConfig config)
    {
        TestExceptionHandler handler = (TestExceptionHandler)config.getExceptionHandler();
        assertFalse(handler.wasExecuteCalled());
    }
    
    private void doTestCalled(DefaultExceptionHandlerConfig config, Exception exc, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
        TestExceptionHandler handler = (TestExceptionHandler)config.getExceptionHandler();
        assertTrue(handler.wasExecuteCalled());
        assertSame(exc, handler.getException());
        assertSame(config.getExceptionConfig(), handler.getExceptionConfig());
        assertSame(mapping, handler.getMapping());
        assertSame(form, handler.getForm());
        assertSame(request, handler.getRequest());
        assertSame(response, handler.getResponse());
    }
    
    public static class TestExceptionHandler extends ExceptionHandler
    {
        private Exception exception;
        private ExceptionConfig exceptionConfig;
        private ActionMapping mapping;
        private ActionForm form;
        private HttpServletRequest request;
        private HttpServletResponse response;
        private boolean executeCalled = false;
        
        public ActionForward execute(Exception exc, ExceptionConfig config, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
        {
            executeCalled = true;
            this.exception = exc;
            this.exceptionConfig = config;
            this.mapping = mapping;
            this.form = form;
            this.request = request;
            this.response = response;
            return new MockActionForward("testname");
        }

        public Exception getException()
        {
            return exception;
        }

        public ExceptionConfig getExceptionConfig()
        {
            return exceptionConfig;
        }

        public ActionForm getForm()
        {
            return form;
        }

        public ActionMapping getMapping()
        {
            return mapping;
        }

        public HttpServletRequest getRequest()
        {
            return request;
        }

        public HttpServletResponse getResponse()
        {
            return response;
        }
        
        public boolean wasExecuteCalled()
        {
            return executeCalled;
        }
    }
}
