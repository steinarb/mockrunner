package com.mockrunner.test.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.apache.commons.validator.ValidatorResources;
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
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.FormPropertyConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.web.MockActionForward;
import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.struts.ActionTestModule;
import com.mockrunner.struts.MapMessageResources;

public class ActionTestModuleTest extends TestCase
{
    private WebMockObjectFactory mockFactory;
    private ActionTestModule module;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        mockFactory = new WebMockObjectFactory();
        module = new ActionTestModule(mockFactory);
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
        assertEquals(mockFactory.getMockActionServlet(), form.getServlet());
        assertEquals(mockFactory.getMockActionServlet(), ((TestForm)module.getActionForm()).getServlet());
        assertEquals(mockFactory.getMockActionServlet(), module.getLastAction().getServlet());
        TestAction testAction = new TestAction();
        module.actionPerform(testAction, form);
        assertEquals(mockFactory.getMockActionServlet(), form.getServlet());
        assertEquals(mockFactory.getMockActionServlet(), testAction.getServlet());
        module.actionPerform(TestAction.class, TestForm.class);
        assertEquals(mockFactory.getMockActionServlet(), module.getLastAction().getServlet());
        assertEquals(mockFactory.getMockActionServlet(), ((TestForm)module.getActionForm()).getServlet());
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
    
    public void testActionPerformMappingTypeSet()
    {
        module.actionPerform(TestAction.class);
        assertEquals(TestAction.class.getName(), mockFactory.getMockActionMapping().getType());
    }
    
    public void testSetResourcesAndLocale()
    {
        MapMessageResources resources1 = new MapMessageResources();
        MapMessageResources resources2 = new MapMessageResources();
        module.setResources(resources1);
        module.setResources("test", resources2);
        module.setLocale(Locale.TRADITIONAL_CHINESE);
        TestAction testAction = new TestAction();
        module.actionPerform(testAction);
        assertEquals(resources1, testAction.getTestResources());
        assertEquals(resources2, testAction.getTestResourcesForKey());
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
        assertEquals(3, mockFactory.getMockModuleConfig().findMessageResourcesConfigs().length);
        assertNotNull(mockFactory.getMockModuleConfig().findMessageResourcesConfig("test1"));
        assertNotNull(mockFactory.getMockModuleConfig().findMessageResourcesConfig("test2"));
        assertNotNull(mockFactory.getMockModuleConfig().findMessageResourcesConfig("test3"));
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
        ValidatorResources resources = module.createValidatorResources(files);
        TestValidatorForm form = new TestValidatorForm();
        form.setServlet(mockFactory.getMockActionServlet());
        mockFactory.getMockActionMapping().setName("testForm");
        form.setFirstName("ABCDEF");
        form.setLastName("ABCDEF");
        ActionErrors errors = form.validate(mockFactory.getMockActionMapping(), mockFactory.getMockRequest());
        assertTrue(errors.isEmpty());;
        form.setFirstName("ABCD");
        form.setLastName("12345678901");
        errors = form.validate(mockFactory.getMockActionMapping(), mockFactory.getMockRequest());
        assertTrue(errors.size() == 2);
        form.setLastName("ABCDEF");
        errors = form.validate(mockFactory.getMockActionMapping(), mockFactory.getMockRequest());
        assertTrue(errors.size() == 1);
        ActionError error = (ActionError)errors.get().next();
        assertEquals("errors.minlength", error.getKey());
    }
    
    public static class TestAction extends Action
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
}
