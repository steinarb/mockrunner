package de.mockrunner.base;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import de.mockrunner.mock.MockActionForward;
import de.mockrunner.mock.MockActionMapping;
import de.mockrunner.mock.MockPageContext;

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

    protected void verifyHasActionErrors()
    {
        actionTestModule.verifyHasActionErrors();
    }

    protected void verifyActionErrorPresent(String errorName)
    {
        actionTestModule.verifyActionErrorPresent(errorName);
    }

    protected void verifyActionErrorNotPresent(String errorName)
    {
        actionTestModule.verifyActionErrorNotPresent(errorName);
    }

    protected void verifyActionErrors(String errorNames[])
    {
        actionTestModule.verifyActionErrors(errorNames);
    }
    
    protected void verifyActionErrorValues(String errorKey, Object[] values)
    {
        actionTestModule.verifyActionErrorValues(errorKey, values);
    }
    
    protected void verifyActionErrorValue(String errorKey, Object value)
    {
        actionTestModule.verifyActionErrorValue(errorKey, value);
    }

    protected ActionError getActionErrorByKey(String errorKey)
    {
        return actionTestModule.getActionErrorByKey(errorKey);
    }

    protected void verifyNumberActionErrors(int number)
    {
        actionTestModule.verifyNumberActionErrors(number);
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
