package com.mockrunner.struts;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.mockrunner.base.BaseTestCase;
import com.mockrunner.mock.web.MockActionForward;
import com.mockrunner.mock.web.MockActionMapping;
import com.mockrunner.mock.web.MockPageContext;

/**
 * Delegator for {@link ActionTestModule}. You can
 * subclass this adapter or use {@link ActionTestModule}
 * directly (so your test case can use another base
 * class).
 */
public class ActionTestCaseAdapter extends BaseTestCase
{
    private ActionTestModule actionTestModule;
    
    public ActionTestCaseAdapter()
    {
        
    }

    public ActionTestCaseAdapter(String arg0)
    {
        super(arg0);
    }
    
    protected void tearDown() throws Exception
    {
        super.tearDown();
        actionTestModule = null;
    }

    /**
     * Creates the <code>ActionTestModule</code>. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        actionTestModule = createActionTestModule(getWebMockObjectFactory());
    }
    
    /**
     * Gets the <code>ActionTestModule</code>. 
     * @return the <code>ActionTestModule</code>
     */
    protected ActionTestModule getActionTestModule()
    {
        return actionTestModule;
    }
    
    /**
     * Sets the <code>ActionTestModule</code>. 
     * @param actionTestModule the <code>ActionTestModule</code>
     */
    protected void setActionTestModule(ActionTestModule actionTestModule)
    {
        this.actionTestModule = actionTestModule;
    }

    /**
     * Delegates to {@link ActionTestModule#addMappedPropertyRequestPrefix}
     */
    protected String addMappedPropertyRequestPrefix(String str)
    {
        return actionTestModule.addMappedPropertyRequestPrefix(str);
    }
    
    /**
     * Delegates to {@link ActionTestModule#setParameter}
     */
    protected void setParameter(String parameter)
    {
        actionTestModule.setParameter(parameter);
    }
    
    /**
     * Delegates to {@link ActionTestModule#setInput}
     */
    protected void setInput(String input)
    {
        actionTestModule.setInput(input);
    }
    
    /**
     * Delegates to {@link ActionTestModule#setReset}
     */
    protected void setReset(boolean reset)
    {
        actionTestModule.setReset(reset);
    }
    
    /**
     * Delegates to {@link ActionTestModule#setDoPopulate}
     */
    protected void setDoPopulate(boolean doPopulate)
    {
        actionTestModule.setDoPopulate(doPopulate);
    }
    
    /**
     * Delegates to {@link ActionTestModule#setValidate}
     */
    protected void setValidate(boolean validate)
    {
        actionTestModule.setValidate(validate);
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyRedirect}
     */
    protected void verifyRedirect(boolean redirect)
    {
        actionTestModule.verifyRedirect(redirect);
    }

    /**
     * Delegates to {@link ActionTestModule#verifyForward}
     */
    protected void verifyForward(String path)
    {
        actionTestModule.verifyForward(path);
    }

    /**
     * Delegates to {@link ActionTestModule#verifyNoActionErrors}
     */
    protected void verifyNoActionErrors()
    {
        actionTestModule.verifyNoActionErrors();
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyNoActionMessages}
     */
    protected void verifyNoActionMessages()
    {
        actionTestModule.verifyNoActionMessages();
    }

    /**
     * Delegates to {@link ActionTestModule#verifyHasActionErrors}
     */
    protected void verifyHasActionErrors()
    {
        actionTestModule.verifyHasActionErrors();
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyHasActionMessages}
     */
    protected void verifyHasActionMessages()
    {
        actionTestModule.verifyHasActionMessages();
    }

    /**
     * Delegates to {@link ActionTestModule#verifyActionErrorPresent}
     */
    protected void verifyActionErrorPresent(String errorName)
    {
        actionTestModule.verifyActionErrorPresent(errorName);
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyActionMessagePresent}
     */
    protected void verifyActionMessagePresent(String messageName)
    {
        actionTestModule.verifyActionMessagePresent(messageName);
    }

    /**
     * Delegates to {@link ActionTestModule#verifyActionErrorNotPresent}
     */
    protected void verifyActionErrorNotPresent(String errorName)
    {
        actionTestModule.verifyActionErrorNotPresent(errorName);
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyActionMessageNotPresent}
     */
    protected void verifyActionMessageNotPresent(String messageName)
    {
        actionTestModule.verifyActionMessageNotPresent(messageName);
    }

    /**
     * Delegates to {@link ActionTestModule#verifyActionErrors}
     */
    protected void verifyActionErrors(String errorNames[])
    {
        actionTestModule.verifyActionErrors(errorNames);
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyActionMessages}
     */
    protected void verifyActionMessages(String messageNames[])
    {
        actionTestModule.verifyActionMessages(messageNames);
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyActionErrorValues}
     */
    protected void verifyActionErrorValues(String errorKey, Object[] values)
    {
        actionTestModule.verifyActionErrorValues(errorKey, values);
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyActionMessageValues}
     */
    protected void verifyActionMessageValues(String messageKey, Object[] values)
    {
        actionTestModule.verifyActionMessageValues(messageKey, values);
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyActionErrorValue}
     */
    protected void verifyActionErrorValue(String errorKey, Object value)
    {
        actionTestModule.verifyActionErrorValue(errorKey, value);
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyActionMessageValue}
     */
    protected void verifyActionMessageValue(String messageKey, Object value)
    {
        actionTestModule.verifyActionMessageValue(messageKey, value);
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyActionErrorProperty}
     */
    protected void verifyActionErrorProperty(String errorKey, String property)
    {
        actionTestModule.verifyActionErrorProperty(errorKey, property);
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyActionMessageProperty}
     */
    protected void verifyActionMessageProperty(String messageKey, String property)
    {
        actionTestModule.verifyActionMessageProperty(messageKey, property);
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyNumberActionErrors}
     */
    protected void verifyNumberActionErrors(int number)
    {
        actionTestModule.verifyNumberActionErrors(number);
    }
    
    /**
     * Delegates to {@link ActionTestModule#verifyNumberActionMessages}
     */
    protected void verifyNumberActionMessages(int number)
    {
        actionTestModule.verifyNumberActionMessages(number);
    }

    /**
     * Delegates to {@link ActionTestModule#getActionErrorByKey}
     */
    protected ActionError getActionErrorByKey(String errorKey)
    {
        return actionTestModule.getActionErrorByKey(errorKey);
    }
    
    /**
     * Delegates to {@link ActionTestModule#getActionMessageByKey}
     */
    protected ActionMessage getActionMessageByKey(String messageKey)
    {
        return actionTestModule.getActionMessageByKey(messageKey);
    }

    /**
     * Delegates to {@link ActionTestModule#setActionErrors}
     */
    protected void setActionErrors(ActionErrors errors)
    {
        actionTestModule.setActionErrors(errors);
    }

    /**
     * Delegates to {@link ActionTestModule#getActionErrors}
     */
    protected ActionErrors getActionErrors()
    {
        return actionTestModule.getActionErrors();
    }

    /**
     * Delegates to {@link ActionTestModule#hasActionErrors}
     */
    protected boolean hasActionErrors()
    {
        return actionTestModule.hasActionErrors();
    }
    
    /**
     * Delegates to {@link ActionTestModule#setActionMessages}
     */
    protected void setActionMessages(ActionMessages messages)
    {
        actionTestModule.setActionMessages(messages);
    }

    /**
     * Delegates to {@link ActionTestModule#getActionMessages}
     */
    protected ActionMessages getActionMessages()
    {
        return actionTestModule.getActionMessages();
    }

    /**
     * Delegates to {@link ActionTestModule#hasActionMessages}
     */
    protected boolean hasActionMessages()
    {
        return actionTestModule.hasActionMessages();
    }
    
    /**
     * Delegates to {@link ActionTestModule#getMockActionMapping}
     */
    protected MockActionMapping getMockActionMapping()
    {
        return actionTestModule.getMockActionMapping();
    }

    /**
     * Delegates to {@link ActionTestModule#getMockPageContext}
     */
    protected MockPageContext getMockPageContext()
    {
        return actionTestModule.getMockPageContext();
    }

    /**
     * Delegates to {@link ActionTestModule#getActionForward}
     */
    protected MockActionForward getActionForward()
    {
        return actionTestModule.getActionForward();
    }

    /**
     * Delegates to {@link ActionTestModule#getActionForm}
     */
    protected ActionForm getActionForm()
    {
        return actionTestModule.getActionForm();
    }

    /**
     * Delegates to {@link ActionTestModule#getLastAction}
     */
    protected Action getLastAction()
    {
        return actionTestModule.getLastAction();
    }

    /**
     * Delegates to {@link ActionTestModule#addRequestParameter(String)}
     */
    protected void addRequestParameter(String key)
    {
        actionTestModule.addRequestParameter(key);
    }

    /**
     * Delegates to {@link ActionTestModule#addRequestParameter(String, String)}
     */
    protected void addRequestParameter(String key, String value)
    {
        actionTestModule.addRequestParameter(key, value);
    }

    /**
     * Delegates to {@link ActionTestModule#generateValidToken}
     */
    protected void generateValidToken()
    {
        actionTestModule.generateValidToken();
    }

    /**
     * Delegates to {@link ActionTestModule#createActionForm}
     */
    protected ActionForm createActionForm(Class form)
    {
        return actionTestModule.createActionForm(form);
    }

    /**
     * Delegates to {@link ActionTestModule#populateRequestToForm}
     */
    protected void populateRequestToForm()
    {
        actionTestModule.populateRequestToForm();
    }

    /**
     * Delegates to {@link ActionTestModule#actionPerform(Class)}
     */
    protected void actionPerform(Class action)
    {
        actionTestModule.actionPerform(action);
    }
    
    /**
     * Delegates to {@link ActionTestModule#actionPerform(Action)}
     */
    protected void actionPerform(Action action)
    {
        actionTestModule.actionPerform(action);
    }

    /**
     * Delegates to {@link ActionTestModule#actionPerform(Class, Class)}
     */
    protected void actionPerform(Class action, Class form)
    {
        actionTestModule.actionPerform(action, form);
    }
    
    /**
     * Delegates to {@link ActionTestModule#actionPerform(Action, Class)}
     */
    protected void actionPerform(Action action, Class form)
    {
        actionTestModule.actionPerform(action, form);
    }

    /**
     * Delegates to {@link ActionTestModule#actionPerform(Class, ActionForm)}
     */
    protected void actionPerform(Class action, ActionForm form)
    {
        actionTestModule.actionPerform(action, form);
    }
    
    /**
     * Delegates to {@link ActionTestModule#actionPerform(Action, ActionForm)}
     */
    protected void actionPerform(Action action, ActionForm form)
    {
        actionTestModule.actionPerform(action, form);
    }
}
