package com.mockrunner.test;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import junit.framework.TestCase;
import com.mockrunner.base.ActionTestModule;
import com.mockrunner.base.MockObjectFactory;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.MockActionForward;

/**
 * Test for {@link com.mockrunner.base.ActionTestModule}
 */
public class ActionTestModuleTest extends TestCase
{
    private MockObjectFactory mockfactory;
    private ActionTestModule module;
    
    public ActionTestModuleTest(String arg0)
    {
        super(arg0);
    }
    
    protected void setUp() throws Exception
    {
        super.setUp();
        mockfactory = new MockObjectFactory();
        module = new ActionTestModule(mockfactory);
    }
    
    private ActionErrors createTestActionErrors()
    {
        ActionErrors errors = new ActionErrors();
        ActionError error1 = new ActionError("key1");
        ActionError error2 = new ActionError("key2", new String[]{"value1" , "value2"});
        ActionError error3 = new ActionError("key3", "value");
        errors.add(ActionErrors.GLOBAL_ERROR, error1);
        errors.add(ActionErrors.GLOBAL_ERROR, error2);
        errors.add(ActionErrors.GLOBAL_ERROR, error3);
        return errors;
    }
    
    private ActionMessages createTestActionMessages()
    {
        ActionMessages messages = new ActionMessages();
        ActionError message1 = new ActionError("key1");
        ActionError message2 = new ActionError("key2", new String[]{"value1" , "value2"});
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
    
    public void testGetActionErrorByKey()
    {
        assertFalse(module.hasActionErrors());
        module.setActionErrors(createTestActionErrors());
        assertTrue(module.hasActionErrors());
        ActionError error = module.getActionErrorByKey("key3");
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
    }
    
    public void testVerifyActionErrorPresent()
    {
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
    }
    
    public void testVerifyActionMessagePresent()
    {
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
        module.createActionForm(TestForm.class);
        module.populateRequestToForm();
        TestForm form = (TestForm)module.getActionForm();
        assertEquals("value1", form.getValue("key1"));
        assertEquals("value2", form.getValue("key2"));
        assertEquals("value3", form.getProperty());
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
}
