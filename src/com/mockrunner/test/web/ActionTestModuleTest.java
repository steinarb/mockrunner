package com.mockrunner.test.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.FormPropertyConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.web.MockActionForward;
import com.mockrunner.struts.ActionTestModule;
import com.mockrunner.struts.DefaultExceptionHandlerConfig;
import com.mockrunner.struts.ExceptionHandlerConfig;
import com.mockrunner.struts.MapMessageResources;

public class ActionTestModuleTest extends BaseTestCase
{
    private ActionTestModule module;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        module = new ActionTestModule(getActionMockObjectFactory());
    }
    
    private ActionErrors createTestActionErrors()
    {
        ActionErrors errors = new ActionErrors();
        ActionError error1 = new ActionError("key1");
        ActionMessage error2 = new ActionMessage("key2", new String[]{"value1" , "value2"});
        ActionMessage error3 = new ActionMessage("key3", "value");
        errors.add(ActionErrors.GLOBAL_ERROR, error1);
        errors.add(ActionMessages.GLOBAL_MESSAGE, error2);
        errors.add(ActionMessages.GLOBAL_MESSAGE, error3);
        return errors;
    }
    
    private ActionMessages createTestActionMessages()
    {
        ActionMessages messages = new ActionMessages();
        ActionMessage message1 = new ActionMessage("key1");
        ActionMessage message2 = new ActionMessage("key2", new String[]{"value1" , "value2"});
        messages.add(ActionErrors.GLOBAL_MESSAGE, message1);
        messages.add(ActionMessages.GLOBAL_MESSAGE, message2);
        return messages;
    }
    
    private ActionErrors createEmptyTestActionErrors()
    {
        return new ActionErrors();
    }
    
    private ActionMessages createEmptyTestActionMessages()
    {
        return new ActionMessages();
    }
    
    public void testVerifyOutput() throws Exception
    {
        getActionMockObjectFactory().getMockResponse().getWriter().write("This is a test");
        try
        {
            module.verifyOutput("this is a test");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setCaseSensitive(false);
        module.verifyOutput("this is a test");
        module.verifyOutputContains("TeSt");
        module.verifyOutputRegularExpression("THIS.*");
        module.setCaseSensitive(true);
        try
        {
            module.verifyOutputContains("THIS");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyOutputRegularExpression(".*TEST");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    public void testGetActionErrorByKey()
    {
        assertFalse(module.hasActionErrors());
        module.setActionErrors(createTestActionErrors());
        assertTrue(module.hasActionErrors());
        ActionMessage error = module.getActionErrorByKey("key3");
        assertEquals("value", error.getValues()[0]);
    }
    
    public void testGetActionMessageByKey()
    {
        assertFalse(module.hasActionMessages());
        module.setActionMessages(createTestActionMessages());
        assertTrue(module.hasActionMessages());
        ActionMessage message = module.getActionMessageByKey("key2");
        assertEquals("value2", message.getValues()[1]);
    }
    
    public void testGetActionMessages()
    {
        assertFalse(module.hasActionMessages());
        ActionMessages theMessages1 = createTestActionMessages();
        module.setActionMessagesToSession(theMessages1);
        assertTrue(module.hasActionMessages());
        module.setRecognizeMessagesInSession(false);
        assertFalse(module.hasActionMessages());
        ActionMessages theMessages2 = createTestActionMessages();
        module.setActionMessages(theMessages2);
        assertTrue(module.hasActionMessages());
        assertSame(theMessages2, module.getActionMessages());
        module.setRecognizeMessagesInSession(true);
        ActionMessages messages =  module.getActionMessages();
        assertEquals(4, messages.size());
        module.setActionMessages(null);
        assertSame(theMessages1, module.getActionMessages());
        module.setActionMessages(new ActionMessages());
        assertSame(theMessages1, module.getActionMessages());
        module.setActionMessagesToSession(new ActionMessages());
        module.setActionMessages(theMessages1);
        assertSame(theMessages1, module.getActionMessages());
    }
    
    public void testGetActionErrors()
    {
        assertFalse(module.hasActionErrors());
        ActionErrors theErrors1 = createTestActionErrors();
        module.setActionErrorsToSession(theErrors1);
        assertTrue(module.hasActionErrors());
        module.setRecognizeMessagesInSession(false);
        assertFalse(module.hasActionErrors());
        ActionErrors theErrors2 = createTestActionErrors();
        module.setActionErrors(theErrors2);
        assertTrue(module.hasActionErrors());
        assertSame(theErrors2, module.getActionErrors());
        module.setRecognizeMessagesInSession(true);
        ActionMessages errors =  module.getActionErrors();
        assertEquals(6, errors.size());
        module.setActionErrors(null);
        assertSame(theErrors1, module.getActionErrors());
        module.setActionErrors(new ActionErrors());
        assertSame(theErrors1, module.getActionErrors());
        module.setActionErrorsToSession(new ActionMessages());
        module.setActionErrors(theErrors1);
        assertSame(theErrors1, module.getActionErrors());
    }
    
    public void testActionMessagesInRequestAndSession()
    {
        ActionMessages theMessages1 = createTestActionMessages();
        ActionMessages theMessages2 = new ActionMessages();
        ActionMessage message1 = new ActionMessage("key3");
        ActionMessage message2 = new ActionMessage("key4", new String[]{"value1" , "value2"});
        theMessages2.add("abc", message1);
        theMessages2.add(ActionMessages.GLOBAL_MESSAGE, message2);
        module.setActionMessages(theMessages1);
        module.setActionMessagesToSession(theMessages2);
        module.verifyNumberActionMessages(4);
        module.verifyActionMessagePresent("key1");
        module.verifyActionMessagePresent("key2");
        module.verifyActionMessagePresent("key3");
        module.verifyActionMessagePresent("key4");
        module.verifyActionMessageNotPresent("key5");
        module.verifyActionMessageValues("key2", new String[]{"value1" , "value2"});
        module.verifyActionMessageValues("key4", new String[]{"value1" , "value2"});
        module.verifyActionMessageProperty("key3", "abc");
        module.setRecognizeMessagesInSession(false);
        module.verifyNumberActionMessages(2);
        module.verifyActionMessagePresent("key1");
        module.verifyActionMessagePresent("key2");
        module.verifyActionMessageNotPresent("key3");
    }
    
    public void testActionErrorsInRequestAndSession()
    {
        ActionErrors theErrors1 = createTestActionErrors();
        ActionErrors theErrors2 = new ActionErrors();
        ActionError error1 = new ActionError("key4");
        ActionMessage error2 = new ActionMessage("key5", new String[]{"value1" , "value2"});
        theErrors2.add(ActionErrors.GLOBAL_ERROR, error1);
        theErrors2.add("abc", error2);
        module.setActionErrors(theErrors1);
        module.setActionErrorsToSession(theErrors2);
        module.verifyNumberActionErrors(5);
        module.verifyActionErrorPresent("key1");
        module.verifyActionErrorPresent("key2");
        module.verifyActionErrorPresent("key3");
        module.verifyActionErrorPresent("key4");
        module.verifyActionErrorPresent("key5");
        module.verifyActionErrorNotPresent("key6");
        module.verifyActionErrorValues("key2", new String[]{"value1" , "value2"});
        module.verifyActionErrorValues("key5", new String[]{"value1" , "value2"});
        module.verifyActionErrorProperty("key5", "abc");
        module.setRecognizeMessagesInSession(false);
        module.verifyNumberActionErrors(3);
        module.verifyActionErrorPresent("key1");
        module.verifyActionErrorPresent("key2");
        module.verifyActionErrorPresent("key3");
        module.verifyActionErrorNotPresent("key4");
    }
    
    public void testActionErrorsInRequestAndSessionInstance()
    {
        module.setActionErrors(createTestActionMessages());
        module.setActionErrorsToSession(createTestActionMessages());
        assertTrue(module.getActionErrors() instanceof ActionMessages);
        module.setActionErrors(createTestActionErrors());
        assertTrue(module.getActionErrors() instanceof ActionErrors);
        module.setActionErrorsToSession(createTestActionErrors());
        assertTrue(module.getActionErrors() instanceof ActionErrors);
        module.setActionErrors(createTestActionMessages());
        assertTrue(module.getActionErrors() instanceof ActionErrors);
    }
    
    public void testSetMessageAttributeKey()
    {
        ActionMessages messages = createTestActionMessages();
        module.setActionMessages(messages);
        assertSame(messages, getActionMockObjectFactory().getMockRequest().getAttribute(Globals.MESSAGE_KEY));
        module.setActionMessagesToSession(messages);
        assertSame(messages, getActionMockObjectFactory().getMockSession().getAttribute(Globals.MESSAGE_KEY));
        ActionMessages otherMessages = createTestActionMessages();
        module.setMessageAttributeKey("mymessages");
        module.setActionMessages(otherMessages);
        assertSame(otherMessages, getActionMockObjectFactory().getMockRequest().getAttribute("mymessages"));
        module.setActionMessagesToSession(otherMessages);
        assertSame(otherMessages, getActionMockObjectFactory().getMockSession().getAttribute("mymessages"));
        assertEquals(4, module.getActionMessages().size());
        module.verifyActionMessagePresent("key1");
        module.verifyActionMessageNotPresent("key3");
        module.setMessageAttributeKey("test");
        module.verifyActionMessageNotPresent("key1");
        assertNull(module.getActionMessages());
        try
        {
            module.verifyHasActionMessages();
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setMessageAttributeKey("mymessages");
        module.verifyHasActionMessages();
        module.verifyActionMessages(new String[]{"key1", "key2", "key1", "key2"});
        module.verifyNumberActionMessages(4);
        module.verifyActionMessageValues("key2", new String[]{"value1", "value2"});
    }
    
    public void testSetErrorAttributeKey()
    {
        ActionErrors errors = createTestActionErrors();
        module.setActionErrors(errors);
        assertSame(errors, getActionMockObjectFactory().getMockRequest().getAttribute(Globals.ERROR_KEY));
        module.setActionErrorsToSession(errors);
        assertSame(errors, getActionMockObjectFactory().getMockSession().getAttribute(Globals.ERROR_KEY));
        ActionErrors otherErrors = createTestActionErrors();
        module.setErrorAttributeKey("othereerrors");
        module.setActionErrors(otherErrors);
        assertSame(otherErrors, getActionMockObjectFactory().getMockRequest().getAttribute("othereerrors"));
        module.setActionErrorsToSession(otherErrors);
        assertSame(otherErrors, getActionMockObjectFactory().getMockSession().getAttribute("othereerrors"));
        assertEquals(6, module.getActionErrors().size());
        module.verifyActionErrorPresent("key2");
        module.verifyActionErrorNotPresent("key4");
        module.setErrorAttributeKey("test");
        module.verifyActionErrorNotPresent("key2");
        assertNull(module.getActionErrors());
        try
        {
            module.verifyHasActionErrors();
            fail();
        } 
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setErrorAttributeKey("othereerrors");
        module.verifyHasActionErrors();
        module.verifyActionErrorPresent("key1");
        module.verifyActionErrorPresent("key2");
        module.verifyActionErrorPresent("key3");
        module.verifyNumberActionErrors(6);
        module.verifyActionErrorValues("key3", new String[]{"value"});
    }
    
    public void testVerifyHasActionErrors()
    {
        module.setActionErrors(createEmptyTestActionErrors());
        module.verifyNoActionErrors();
        try
        {
            module.verifyHasActionErrors();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setActionErrors(createTestActionErrors());
        module.verifyHasActionErrors();
        try
        {
            module.verifyNoActionErrors();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setActionErrors(null);
        module.verifyNoActionErrors();
        module.setActionErrorsToSession(createTestActionErrors());
        module.verifyHasActionErrors();
        module.setRecognizeMessagesInSession(false);
        module.verifyNoActionErrors();
    }
    
    public void testVerifyHasActionMessages()
    {
        module.setActionMessages(createEmptyTestActionMessages());
        module.verifyNoActionMessages();
        try
        {
            module.verifyHasActionMessages();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setActionMessages(createTestActionMessages());
        module.verifyHasActionMessages();
        try
        {
            module.verifyNoActionMessages();
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setActionMessages(null);
        module.verifyNoActionMessages();
        module.setActionMessagesToSession(createTestActionMessages());
        module.verifyHasActionMessages();
        module.setRecognizeMessagesInSession(false);
        module.verifyNoActionMessages();
    }
    
    public void testVerifyActionErrorPresent()
    {
        module.setActionErrors(createEmptyTestActionErrors());
        module.verifyActionErrorNotPresent("key1");
        module.setActionErrors(createTestActionErrors());
        module.verifyActionErrorPresent("key1");
        module.verifyActionErrorNotPresent("key4");
        try
        {
            module.verifyActionErrorPresent("key5");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyActionErrorNotPresent("key3");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setActionErrors(null);
        module.verifyActionErrorNotPresent("key1");
        module.setActionErrorsToSession(createTestActionErrors());
        module.verifyActionErrorPresent("key1");
    }
    
    public void testVerifyActionMessagePresent()
    {
        module.setActionMessages(createEmptyTestActionMessages());
        module.verifyActionMessageNotPresent("key1");
        module.setActionMessages(createTestActionMessages());
        module.verifyActionMessagePresent("key1");
        module.verifyActionMessageNotPresent("key3");
        try
        {
            module.verifyActionMessagePresent("key3");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyActionMessageNotPresent("key1");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setActionMessages(null);
        module.verifyActionMessageNotPresent("key1");
        module.setActionMessagesToSession(createTestActionMessages());
        module.verifyActionMessagePresent("key1");
    }
    
    public void testVerifyNumberActionErrors()
    {
        module.setActionErrors(createEmptyTestActionErrors());
        module.verifyNumberActionErrors(0);
        module.setActionErrors(createTestActionErrors());
        module.verifyNumberActionErrors(3);
        try
        {
            module.verifyNumberActionErrors(4);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setActionErrors(null);
        module.verifyNumberActionErrors(0);
        module.setActionErrorsToSession(createTestActionErrors());
        module.verifyNumberActionErrors(3);
    }
    
    public void testVerifyNumberActionMessages()
    {
        module.setActionMessages(createEmptyTestActionMessages());
        module.verifyNumberActionMessages(0);
        module.setActionMessages(createTestActionMessages());
        module.verifyNumberActionMessages(2);
        try
        {
            module.verifyNumberActionMessages(0);
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setActionMessages(null);
        module.verifyNumberActionMessages(0);
        module.setActionMessagesToSession(createTestActionMessages());
        module.verifyNumberActionMessages(2);
    }
    
    public void testVerifyActionErrors()
    {
        module.setActionErrors(createTestActionErrors());
        module.verifyActionErrors(new String[]{"key1", "key2", "key3"});
        try
        {
            module.verifyActionErrors(new String[]{"key1", "key2", "key3", "key4"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyActionErrors(new String[]{"key1", "key2"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyActionErrors(new String[]{"key4", "key2", "key3"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setActionErrors(null);
        module.setActionErrorsToSession(createTestActionErrors());
        module.verifyActionErrors(new String[]{"key1", "key2", "key3"});
        module.setRecognizeMessagesInSession(false);
        try
        {
            module.verifyActionErrors(new String[]{"key1", "key2", "key3"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    public void testVerifyActionMessages()
    {
        module.setActionMessages(createTestActionMessages());
        module.verifyActionMessages(new String[]{"key1", "key2"});
        try
        {
            module.verifyActionMessages(new String[]{"key1", "key3", "key4"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyActionMessages(new String[]{"key1"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setActionMessages(null);
        module.setActionMessagesToSession(createTestActionMessages());
        module.verifyActionMessages(new String[]{"key1", "key2"});
        module.setRecognizeMessagesInSession(false);
        try
        {
            module.verifyActionMessages(new String[]{"key1", "key2"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    public void testVerifyActionErrorValues()
    {
        module.setActionErrors(createTestActionErrors());
        module.verifyActionErrorValue("key3", "value");
        module.verifyActionErrorValues("key2", new String[]{"value1", "value2"});
        try
        {
            module.verifyActionErrorValue("key1", "test");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyActionErrorValue("key3", "test");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyActionErrorValue("key2", "value1");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyActionErrorValue("key2", new String[]{"value2", "value1"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setActionErrors(null);
        module.setActionErrorsToSession(createTestActionErrors());
        module.verifyActionErrorValues("key2", new String[]{"value1", "value2"});
        module.setRecognizeMessagesInSession(false);
        try
        {
            module.verifyActionErrorValues("key2", new String[]{"value1", "value2"});
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    public void testVerifyActionMessageValues()
    {
        module.setActionMessages(createTestActionMessages());
        module.verifyActionMessageValues("key2", new String[]{"value1", "value2"});
        try
        {
            module.verifyActionMessageValue("key2", "value1");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        try
        {
            module.verifyActionMessageValue("key1", "value1");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
    }
    
    public void testVerifyActionErrorProperty()
    {
        ActionErrors errors = new ActionErrors();
        ActionError error1 = new ActionError("error1");
        ActionError error2 = new ActionError("error2"); 
        errors.add("property", error1);
        errors.add(ActionErrors.GLOBAL_ERROR, error2);
        module.setActionErrors(errors);
        module.verifyActionErrorProperty("error1", "property");
        try
        {
            module.verifyActionMessageProperty("error2", "property");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.verifyActionErrorProperty("error2", ActionErrors.GLOBAL_ERROR);
        module.setActionErrors(null);
        module.setActionErrorsToSession(errors);
        module.verifyActionErrorProperty("error1", "property");
    }
    
    public void testVerifyActionMessageProperty()
    {
        ActionMessages messages = new ActionMessages();
        ActionMessage message1 = new ActionMessage("message1");
        ActionMessage message2 = new ActionMessage("message2");
        messages.add("property", message1);
        messages.add("property", message2);
        module.setActionMessages(messages);
        module.verifyActionMessageProperty("message1", "property");
        module.verifyActionMessageProperty("message2", "property");
        try
        {
            module.verifyActionMessageProperty("message2", "property1");
            fail();
        }
        catch(VerifyFailedException exc)
        {
            //should throw exception
        }
        module.setActionMessages(null);
        module.setActionMessagesToSession(messages);
        module.verifyActionMessageProperty("message1", "property");
    }
    
    public void testCreateActionForm()
    {
        module.createActionForm(null);
        assertNull(module.getActionForm());
        module.createActionForm(TestForm.class);
        assertNotNull(module.getActionForm());
        assertTrue(module.getActionForm() instanceof TestForm);
    }
    
    
    public void testPopulateRequestToForm()
    {
        module.addRequestParameter("value(key1)", "value1");
        module.addRequestParameter("value(key2)", "value2");
        module.addRequestParameter("property", "value3");
        module.addRequestParameter("indexedProperty[1]", "indexedValue");
        module.addRequestParameter("nested.property", "nestedValue");
        module.createActionForm(TestForm.class);
        module.populateRequestToForm();
        TestForm form = (TestForm)module.getActionForm();
        assertEquals("value1", form.getValue("key1"));
        assertEquals("value2", form.getValue("key2"));
        assertEquals("value3", form.getProperty());
        assertEquals("indexedValue", form.getIndexedProperty(1));
        assertEquals("nestedValue", form.getNested().getProperty());
        assertEquals(null, form.getValue("key3"));
    }
    
    public void testSetDoPopulateAndReset()
    {
        TestForm form = (TestForm)module.createActionForm(TestForm.class);
        module.addRequestParameter("property", "value");
        module.actionPerform(TestAction.class, form);
        assertEquals("value", form.getProperty());
        assertTrue(form.wasResetCalled());
        
        form = (TestForm)module.createActionForm(TestForm.class);
        module.addRequestParameter("property", "value");
        module.setDoPopulate(false);
        module.actionPerform(TestAction.class, form);
        assertNull(form.getProperty());
        assertTrue(form.wasResetCalled());
        
        form = (TestForm)module.createActionForm(TestForm.class);
        assertFalse(form.wasResetCalled());
        module.addRequestParameter("property", "value");
        module.setDoPopulate(true);
        module.setReset(false);
        module.actionPerform(TestAction.class, form);
        assertEquals("value", form.getProperty());
        assertFalse(form.wasResetCalled());
    }
    
    public void testActionPerform()
    {
        module.addRequestParameter("property", "value");
        
        TestForm form = (TestForm)module.createActionForm(TestForm.class);
        form.setValidationOk(false);
        module.setValidate(true);
        module.setInput("input");
        module.actionPerform(TestAction.class, form);
        module.verifyForward("input");
        assertEquals("value", form.getProperty());
        module.verifyHasActionErrors();
        module.verifyActionErrorPresent("testkey");
        
        form = (TestForm)module.createActionForm(TestForm.class);
        form.setValidationOk(false);
        module.setValidate(false);
        module.actionPerform(TestAction.class, form);
        assertEquals("success", ((MockActionForward)module.getActionForward()).getPath());
        module.verifyForward("success");
        assertEquals("value", form.getProperty());
        module.verifyNoActionErrors();
        
        form = (TestForm)module.createActionForm(TestForm.class);
        form.setValidationOk(true);
        module.setValidate(true);
        module.actionPerform(TestAction.class, form);
        module.verifyForward("success");
        module.verifyNoActionErrors();
        
        module.actionPerform(TestAction.class, TestForm.class);
        module.verifyForward("success");
        module.verifyNoActionErrors();
        assertEquals("value", ((TestForm)module.getActionForm()).getProperty());        
        module.actionPerform(TestAction.class);
        module.verifyForward("success");
        module.verifyNoActionErrors();
        assertNull(module.getActionForm());
    }
    
    public void testActionPerformServletSet()
    {
        TestForm form = (TestForm)module.createActionForm(TestForm.class);
        module.actionPerform(TestAction.class, form);   
        assertEquals(getActionMockObjectFactory().getMockActionServlet(), form.getServlet());
        assertEquals(getActionMockObjectFactory().getMockActionServlet(), ((TestForm)module.getActionForm()).getServlet());
        assertEquals(getActionMockObjectFactory().getMockActionServlet(), module.getLastAction().getServlet());
        TestAction testAction = new TestAction();
        module.actionPerform(testAction, form);
        assertEquals(getActionMockObjectFactory().getMockActionServlet(), form.getServlet());
        assertEquals(getActionMockObjectFactory().getMockActionServlet(), testAction.getServlet());
        module.actionPerform(TestAction.class, TestForm.class);
        assertEquals(getActionMockObjectFactory().getMockActionServlet(), module.getLastAction().getServlet());
        assertEquals(getActionMockObjectFactory().getMockActionServlet(), ((TestForm)module.getActionForm()).getServlet());
    }
    
    public void testVerifyForwardPathAndName()
    {
        TestForwardAction action = new TestForwardAction();
        module.actionPerform(action);
        module.verifyForward("success");
        module.verifyForwardName("success");
        MockActionForward forward = new MockActionForward("myName");
        action.setActionForward(forward);
        module.actionPerform(action);
        module.verifyForwardName("myName");
        try
        {
            module.verifyForward("myName");
            fail();
        }
        catch (VerifyFailedException exc)
        {
            //should throw exception
        }
        forward = new MockActionForward("myName", "myPath", true);
        action.setActionForward(forward);
        module.actionPerform(action);
        module.verifyForward("myPath");
        module.verifyForwardName("myName");
    }
    
    public void testVerifyForwardWithPresetForward()
    {
        getActionMockObjectFactory().getMockActionMapping().addForward("success", "myPath");
        TestForwardAction action = new TestForwardAction();
        module.actionPerform(action);
        module.verifyForward("myPath");
        module.verifyForwardName("success");
        getActionMockObjectFactory().getMockActionMapping().addForward("success", "anotherPath");
        module.actionPerform(action);
        module.verifyForward("anotherPath");
        module.verifyForwardName("success");
    }
    
    public void testActionPerformMappingTypeSet()
    {
        module.actionPerform(TestAction.class);
        assertEquals(TestAction.class.getName(), getActionMockObjectFactory().getMockActionMapping().getType());
    }
    
    public void testSetResourcesAndLocale()
    {
        getActionMockObjectFactory().getMockRequest().getSession(true);
        MapMessageResources resources1 = new MapMessageResources();
        MapMessageResources resources2 = new MapMessageResources();
        module.setResources(resources1);
        module.setResources("test", resources2);
        module.setLocale(Locale.TRADITIONAL_CHINESE);
        TestAction testAction = new TestAction();
        module.actionPerform(testAction);
        assertEquals(resources1, testAction.getTestResources());
        assertEquals(resources2, testAction.getTestResourcesForKey());
        System.out.println(testAction.getTestLocale());
        assertEquals(Locale.TRADITIONAL_CHINESE, testAction.getTestLocale());
    }
    
    public void testSetResourcesAddToModuleConfig()
    {
        MapMessageResources resources1 = new MapMessageResources();
        MapMessageResources resources2 = new MapMessageResources();
        MapMessageResources resources3 = new MapMessageResources();
        module.setResources("test1", resources1);
        module.setResources("test2", resources2);
        module.setResources("test3", resources3);
        assertEquals(3, getActionMockObjectFactory().getMockModuleConfig().findMessageResourcesConfigs().length);
        assertNotNull(getActionMockObjectFactory().getMockModuleConfig().findMessageResourcesConfig("test1"));
        assertNotNull(getActionMockObjectFactory().getMockModuleConfig().findMessageResourcesConfig("test2"));
        assertNotNull(getActionMockObjectFactory().getMockModuleConfig().findMessageResourcesConfig("test3"));
    }
    
    public void testDynaActionForm()
    {
        FormBeanConfig config = new FormBeanConfig();
        config.setType(TestDynaForm.class.getName());
        FormPropertyConfig property1 = new FormPropertyConfig();
        property1.setName("property1");
        property1.setType("java.lang.String");
        property1.setInitial("testValue1");
        FormPropertyConfig property2 = new FormPropertyConfig();
        property2.setName("property2");
        property2.setType("java.lang.Integer");
        property2.setInitial("2");
        config.addFormPropertyConfig(property1);
        config.addFormPropertyConfig(property2);
        DynaActionForm form = module.createDynaActionForm(config);
        String prop1Value = (String)form.get("property1");
        assertEquals("testValue1", prop1Value);
        Integer prop2Value = (Integer)form.get("property2");
        assertEquals(new Integer(2), prop2Value);
        try
        {
            form.set("property3", "3");
            fail();
        }
        catch(IllegalArgumentException exc)
        {
            //should throw exception
        }
        module.addRequestParameter("property2", "123");
        module.actionPerform(TestAction.class, form);
        assertEquals(new Integer(123), form.get("property2"));
    }

    public void testCreateValidatorResources()
    {
        String[] files = new String[2];
        files[0] = "src/com/mockrunner/test/web/validator-rules.xml";
        files[1] = "src/com/mockrunner/test/web/validation.xml";
        module.createValidatorResources(files);
        module.setResources(new MapMessageResources());
        TestValidatorForm form = new TestValidatorForm();
        form.setServlet(getActionMockObjectFactory().getMockActionServlet());
        getActionMockObjectFactory().getMockActionMapping().setName("testForm");
        form.setFirstName("ABCDEF");
        form.setLastName("ABCDEF");
        ActionErrors errors = form.validate(getActionMockObjectFactory().getMockActionMapping(), getActionMockObjectFactory().getMockRequest());
        assertTrue(errors.isEmpty());;
        form.setFirstName("ABCD");
        form.setLastName("12345678901");
        errors = form.validate(getActionMockObjectFactory().getMockActionMapping(), getActionMockObjectFactory().getMockRequest());
        System.out.println(errors.size());
        assertTrue(errors.size() == 2);
        form.setLastName("ABCDEF");
        errors = form.validate(getActionMockObjectFactory().getMockActionMapping(), getActionMockObjectFactory().getMockRequest());
        assertTrue(errors.size() == 1);
        ActionMessage error = (ActionMessage)errors.get().next();
        assertEquals("errors.minlength", error.getKey());
    }
    
    public void testWrappedRequest()
    {
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(getActionMockObjectFactory().getMockRequest());
        HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(getActionMockObjectFactory().getMockResponse());
        getActionMockObjectFactory().addRequestWrapper(requestWrapper);
        getActionMockObjectFactory().addResponseWrapper(responseWrapper);
        TestAction action = new TestAction();
        module.actionPerform(action);
        assertSame(requestWrapper, action.getRequest());
        assertSame(responseWrapper, action.getResponse());
    }
    
    public void testCustomActionMapping()
    {
        TestMapping mapping = (TestMapping)getActionMockObjectFactory().prepareActionMapping(TestMapping.class);
        TestMappingAction action = new TestMappingAction();
        TestMappingForm form = new TestMappingForm();
        module.actionPerform(action, form);
        assertSame(mapping, action.getActionMapping());
        assertSame(mapping, form.getResetActionMapping());
        assertSame(mapping, form.getValidateActionMapping());
    }
    
    public void testCustomActionMappingDelegation()
    {
        TestMapping mapping = (TestMapping)getActionMockObjectFactory().prepareActionMapping(TestMapping.class);
        module.setValidate(true);
        module.setParameter("testParameter");
        module.setInput("testInput");
        assertTrue(mapping.getValidate());
        assertEquals("testParameter", mapping.getParameter());
        assertEquals("testInput", mapping.getInput());
        assertTrue(getActionMockObjectFactory().getMockActionMapping().getValidate());
        assertEquals("testParameter", getActionMockObjectFactory().getMockActionMapping().getParameter());
        assertEquals("testInput", getActionMockObjectFactory().getMockActionMapping().getInput());
    }
    
    public void testExceptionHandler()
    {
        TestExceptionHandler handler1 = new TestExceptionHandler();
        TestExceptionHandler handler2 = new TestExceptionHandler();
        TestExceptionHandler handler3 = new TestExceptionHandler();
        DefaultExceptionHandlerConfig config1 = new DefaultExceptionHandlerConfig(handler1, FileNotFoundException.class);
        DefaultExceptionHandlerConfig config2 = new DefaultExceptionHandlerConfig(handler2, SQLException.class);
        DefaultExceptionHandlerConfig config3 = new DefaultExceptionHandlerConfig(handler3, IOException.class);
        module.addExceptionHandler(config1);
        module.addExceptionHandler(config2);
        module.addExceptionHandler(config3);
        IOException ioException = new IOException();
        ActionForward forward = module.actionPerform(new TestFailureAction(ioException));
        assertEquals("testname", forward.getName());
        assertFalse(handler1.wasExecuteCalled());
        assertFalse(handler2.wasExecuteCalled());
        assertTrue(handler3.wasExecuteCalled());
        assertSame(ioException, handler3.getException());
        handler3.reset(); 
        FileNotFoundException fileNotFoundException = new FileNotFoundException();
        forward = module.actionPerform(new TestFailureAction(fileNotFoundException));
        assertEquals("testname", forward.getName());
        assertTrue(handler1.wasExecuteCalled());
        assertFalse(handler2.wasExecuteCalled());
        assertFalse(handler3.wasExecuteCalled());
        assertSame(fileNotFoundException, handler1.getException());
        handler1.reset();
        SQLException sqlException = new SQLException();
        forward = module.actionPerform(new TestFailureAction(sqlException));
        assertEquals("testname", forward.getName());
        assertFalse(handler1.wasExecuteCalled());
        assertTrue(handler2.wasExecuteCalled());
        assertFalse(handler3.wasExecuteCalled());
        assertSame(sqlException, handler2.getException());
        handler2.reset();
        Exception exception = new Exception();
        try
        {
            module.actionPerform(new TestFailureAction(exception));
            fail();
        } 
        catch(NestedApplicationException exc)
        {
            assertSame(exception, exc.getRootCause());
        }
    }
    
    public void testExceptionHandlerActionForward()
    {
        TestExceptionHandlerConfig config1 = new TestExceptionHandlerConfig(true, new MockActionForward("test"));
        TestExceptionHandlerConfig config2 = new TestExceptionHandlerConfig(false, new Integer(1));
        module.addExceptionHandler(config1);
        module.addExceptionHandler(config2);
        ActionForward forward = module.actionPerform(new TestFailureAction(new Exception()));
        assertEquals("test", forward.getName());
        assertSame(forward, module.getActionForward());
        config1.setCanHandle(false);
        config2.setCanHandle(true);
        assertNull(module.actionPerform(new TestFailureAction(new Exception())));
        assertNull(module.getActionForward());
    }
    
    public void testNestedException()
    {
        try
        {
            module.actionPerform(TestFailureAction.class);
            fail();
        } 
        catch(NestedApplicationException exc)
        {
            assertEquals("Expected", exc.getRootCause().getMessage());
        }
    }
    
    public static class TestMappingAction extends Action
    {
        private ActionMapping mapping;
        
        public ActionMapping getActionMapping()
        {
            return mapping;
        }

        public ActionForward execute(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception
        {
            this.mapping = mapping;
            return mapping.findForward("success");
        }
    }
    
    public static class TestFailureAction extends Action
    {
        private Exception exception;
        
        public TestFailureAction()
        {
            exception = new Exception("Expected");
        }
        
        public TestFailureAction(Exception exception)
        {
            this.exception = exception;
        }

        public ActionForward execute(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception
        {
            throw exception;
        }
    }
    
    public static class TestAction extends Action
    {
        private MessageResources resources;
        private MessageResources resourcesForKey;
        private Locale locale;
        private HttpServletRequest request;
        private HttpServletResponse response;
    
        public ActionForward execute(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception
        {
            resources = getResources(request);
            resourcesForKey = getResources(request, "test");
            locale = getLocale(request);
            this.request = request;
            this.response = response;
            return mapping.findForward("success");
        }
    
        public HttpServletRequest getRequest()
        {
            return request;
        }
        
        public HttpServletResponse getResponse()
        {
            return response;
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
    
    public static class TestForwardAction extends Action
    {
        private ActionForward forward;
        
        public ActionForward execute(ActionMapping mapping,
                                             ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws Exception
        {
            
            if(null != forward) return forward;
            return mapping.findForward("success");
        }
        
        public void setActionForward(ActionForward forward)
        {
            this.forward = forward;
        }
    }
    
    public static class TestMappingForm extends ActionForm
    {
        private ActionMapping resetMapping;
        private ActionMapping validateMapping;
        
        public ActionMapping getResetActionMapping()
        {
            return resetMapping;
        }

        public ActionMapping getValidateActionMapping()
        {
            return validateMapping;
        }

        public void reset(ActionMapping mapping, HttpServletRequest request)
        {
            resetMapping = mapping;
        }
    
        public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
        {
            validateMapping = mapping;
            return null;
        }
    }
    
    public static class TestForm extends ActionForm
    {
        private boolean validationOk = true;
        private Map mappedProperties = new HashMap();
        private String property;
        private Map indexedProperties = new HashMap();
        private TestNested nested = new TestNested();
        private boolean resetCalled = false;
    
        public ActionServlet getServlet()
        {
            return super.getServlet();
        }
  
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

        public TestNested getNested()
        {
            return nested;
        }
    
        public void setNested(TestNested nested)
        {
            this.nested = nested;
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
    
    public static class TestDynaForm extends DynaActionForm
    {

    }
    
    public static class TestValidatorForm extends ValidatorForm
    {
        private String firstName;
        private String lastName;
    
        public String getFirstName()
        {
            return firstName;
        }

        public String getLastName()
        {
            return lastName;
        }

        public void setFirstName(String firstName)
        {
            this.firstName = firstName;
        }

        public void setLastName(String lastName)
        {
            this.lastName = lastName;
        }
    }

    public static class TestNested
    {
        private String property;
 
        public String getProperty()
        {
            return property;
        }

        public void setProperty(String string)
        {
            property = string;
        }
    }
    
    public static class TestMapping extends ActionMapping
    {
        
    }
    
    public static class TestExceptionHandler extends ExceptionHandler
    {
        private boolean executeCalled = false;
        private Exception exception;
        
        public ActionForward execute(Exception exc, ExceptionConfig config, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
        {
            executeCalled = true;
            exception = exc;
            return new MockActionForward("testname");
        }

        public void reset()
        {
            executeCalled = false;
            exception = null;
        }
        
        public Exception getException()
        {
            return exception;
        }

        public boolean wasExecuteCalled()
        {
            return executeCalled;
        }
    }
    
    public static class TestExceptionHandlerConfig implements ExceptionHandlerConfig
    {
        private boolean canHandle;
        private Object returnValue;
        
        public TestExceptionHandlerConfig(boolean handle, Object value)
        {            
            canHandle = handle;
            returnValue = value;
        }

        public void setCanHandle(boolean canHandle)
        {
            this.canHandle = canHandle;
        }

        public boolean canHandle(Exception exception)
        {
            return canHandle;
        }

        public Object handle(Exception exception, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
        {
            return returnValue;
        }
    }
}
