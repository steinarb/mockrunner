package de.mockrunner.base;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessages;

import de.mockrunner.mock.MockActionForward;
import de.mockrunner.mock.MockActionMapping;
import de.mockrunner.mock.MockPageContext;

/**
 * Simply delegates to ActionTestModule.
 * The easiest way to write action tests
 * is to extend your testcases from this.
 */
public class ActionTestCaseAdapter extends BaseTestCase
{
    private ActionTestModule actionTestModule;

    public ActionTestCaseAdapter(String arg0)
    {
        super(arg0);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        actionTestModule = createActionTestModule(getMockObjectFactory());
    }

    protected ActionTestModule createActionTestModule(MockObjectFactory mockFactory)
    {
        return new ActionTestModule(mockFactory);
    }

    protected String addMappedPropertyRequestPrefix(String str)
    {
        return actionTestModule.addMappedPropertyRequestPrefix(str);
    }

    protected void setParameter(String parameter)
    {
        actionTestModule.setParameter(parameter);
    }

    protected void setValidate(boolean validate)
    {
        actionTestModule.setValidate(validate);
    }

    protected void verifyForward(String path)
    {
        actionTestModule.verifyForward(path);
    }

    protected void verifyNoActionErrors()
    {
        actionTestModule.verifyNoActionErrors();
    }
    
    protected void verifyNoActionMessages()
    {
        actionTestModule.verifyNoActionMessages();
    }

    protected void verifyHasActionErrors()
    {
        actionTestModule.verifyHasActionErrors();
    }
    
    protected void verifyHasActionMessages()
    {
        actionTestModule.verifyHasActionMessages();
    }

    protected void verifyActionErrorPresent(String errorName)
    {
        actionTestModule.verifyActionErrorPresent(errorName);
    }
    
    protected void verifyActionMessagePresent(String messageName)
    {
        actionTestModule.verifyActionMessagePresent(messageName);
    }

    protected void verifyActionErrorNotPresent(String errorName)
    {
        actionTestModule.verifyActionErrorNotPresent(errorName);
    }
    
    protected void verifyActionMessageNotPresent(String messageName)
    {
        actionTestModule.verifyActionMessageNotPresent(messageName);
    }

    protected void verifyActionErrors(String errorNames[])
    {
        actionTestModule.verifyActionErrors(errorNames);
    }
    
    protected void verifyActionMessages(String messageNames[])
    {
        actionTestModule.verifyActionMessages(messageNames);
    }
    
    protected void verifyActionErrorValues(String errorKey, Object[] values)
    {
        actionTestModule.verifyActionErrorValues(errorKey, values);
    }
    
    protected void verifyActionMessageValues(String messageKey, Object[] values)
    {
        actionTestModule.verifyActionMessageValues(messageKey, values);
    }
    
    protected void verifyActionErrorValue(String errorKey, Object value)
    {
        actionTestModule.verifyActionErrorValue(errorKey, value);
    }
    
    protected void verifyActionMessageValue(String messageKey, Object value)
    {
        actionTestModule.verifyActionMessageValue(messageKey, value);
    }
    
    protected void verifyNumberActionErrors(int number)
    {
        actionTestModule.verifyNumberActionErrors(number);
    }
    
    protected void verifyNumberActionMessages(int number)
    {
        actionTestModule.verifyNumberActionMessages(number);
    }

    protected ActionError getActionErrorByKey(String errorKey)
    {
        return actionTestModule.getActionErrorByKey(errorKey);
    }
    
    protected ActionError getActionMessageByKey(String messageKey)
    {
        return actionTestModule.getActionMessageByKey(messageKey);
    }

    protected void setActionErrors(ActionErrors errors)
    {
        actionTestModule.setActionErrors(errors);
    }

    protected ActionErrors getActionErrors()
    {
        return actionTestModule.getActionErrors();
    }

    protected boolean hasActionErrors()
    {
        return actionTestModule.hasActionErrors();
    }
    
    protected void setActionMessages(ActionMessages messages)
    {
        actionTestModule.setActionMessages(messages);
    }

    protected ActionMessages getActionMessages()
    {
        return actionTestModule.getActionMessages();
    }

    protected boolean hasActionMessages()
    {
        return actionTestModule.hasActionMessages();
    }
    
    protected MockActionMapping getMockActionMapping()
    {
        return actionTestModule.getMockActionMapping();
    }

    protected MockPageContext getMockPageContext()
    {
        return actionTestModule.getMockPageContext();
    }

    protected MockActionForward getActionForward()
    {
        return actionTestModule.getActionForward();
    }

    protected ActionForm getActionForm()
    {
        return actionTestModule.getActionForm();
    }

    protected Action getLastAction()
    {
        return actionTestModule.getLastAction();
    }

    protected void addRequestParameter(String key)
    {
        actionTestModule.addRequestParameter(key);
    }

    protected void addRequestParameter(String key, String value)
    {
        actionTestModule.addRequestParameter(key, value);
    }

    protected void generateValidToken()
    {
        actionTestModule.generateValidToken();
    }

    protected ActionForm createActionForm(Class form)
    {
        return actionTestModule.createActionForm(form);
    }

    protected void populateRequestToForm()
    {
        actionTestModule.populateRequestToForm();
    }

    protected void actionPerform(Class action)
    {
        actionTestModule.actionPerform(action);
    }

    protected void actionPerform(Class action, Class form)
    {
        actionTestModule.actionPerform(action, form);
    }

    protected void actionPerform(Class action, ActionForm form)
    {
        actionTestModule.actionPerform(action, form);
    }
}
