package com.mockrunner.struts;

import java.util.Locale;

import javax.sql.DataSource;

import org.apache.commons.validator.ValidatorResources;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.util.MessageResources;

import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.HTMLOutputTestCase;
import com.mockrunner.mock.web.MockActionForward;
import com.mockrunner.mock.web.MockActionMapping;
import com.mockrunner.mock.web.MockPageContext;

/**
 * Delegator for {@link ActionTestModule}. You can
 * subclass this adapter or use {@link ActionTestModule}
 * directly (so your test case can use another base
 * class).
 */
public class ActionTestCaseAdapter extends HTMLOutputTestCase
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
     * Creates the {@link com.mockrunner.struts.ActionTestModule}. If you
     * overwrite this method, you must call 
     * <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        actionTestModule = createActionTestModule(getWebMockObjectFactory());
    }
    
    /**
     * Returns the {@link com.mockrunner.struts.ActionTestModule} as 
     * <code>HTMLOutputModule</code>.
     * @return the <code>HTMLOutputModule</code>
     */
    protected HTMLOutputModule getHTMLOutputModule()
    {
        return actionTestModule;
    }
    
    /**
     * Gets the {@link com.mockrunner.struts.ActionTestModule}. 
     * @return the {@link com.mockrunner.struts.ActionTestModule}
     */
    protected ActionTestModule getActionTestModule()
    {
        return actionTestModule;
    }
    
    /**
     * Sets the {@link com.mockrunner.struts.ActionTestModule}. 
     * @param actionTestModule the {@link com.mockrunner.struts.ActionTestModule}
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
     * Delegates to {@link ActionTestModule#setRecognizeMessagesInSession}
     */
    protected void setRecognizeMessagesInSession(boolean recognizeInSession)
    {
        actionTestModule.setRecognizeMessagesInSession(recognizeInSession);
    }
    
    /**
     * Delegates to {@link ActionTestModule#setValidate}
     */
    protected void setValidate(boolean validate)
    {
        actionTestModule.setValidate(validate);
    }
    
    /**
     * Delegates to {@link ActionTestModule#setResources(MessageResources)}
     */
    protected void setResources(MessageResources resources)
    {
        actionTestModule.setResources(resources);
    }
    
    /**
     * Delegates to {@link ActionTestModule#setResources(String, MessageResources)}
     */
    protected void setResources(String key, MessageResources resources)
    {
        actionTestModule.setResources(key, resources);
    }
    
    /**
     * Delegates to {@link ActionTestModule#setDataSource(DataSource)}
     */
    protected void setDataSource(DataSource dataSource)
    {
        actionTestModule.setDataSource(dataSource);
    }

    /**
     * Delegates to {@link ActionTestModule#setDataSource(String, DataSource)}
     */
    protected void setDataSource(String key, DataSource dataSource)
    {
        actionTestModule.setDataSource(key, dataSource);
    }
    
    /**
     * Delegates to {@link ActionTestModule#setLocale}
     */
    protected void setLocale(Locale locale)
    {
        actionTestModule.setLocale(locale);
    }
    
    /**
     * Delegates to {@link ActionTestModule#createValidatorResources}
     */
    protected ValidatorResources createValidatorResources(String[] resourcesFiles)
    {
        return actionTestModule.createValidatorResources(resourcesFiles);
    }
    
    /**
     * Delegates to {@link ActionTestModule#setValidatorResources}
     */
    protected void setValidatorResources(ValidatorResources validatorResources)
    {
        actionTestModule.setValidatorResources(validatorResources);
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
     * Delegates to {@link ActionTestModule#verifyForwardName}
     */
    protected void verifyForwardName(String name)
    {
        actionTestModule.verifyForwardName(name);
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
    protected ActionMessage getActionErrorByKey(String errorKey)
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
    protected void setActionErrors(ActionMessages errors)
    {
        actionTestModule.setActionErrors(errors);
    }
    
    /**
     * Delegates to {@link ActionTestModule#setActionErrorsToSession}
     */
    protected void setActionErrorsToSession(ActionMessages errors)
    {
        actionTestModule.setActionErrorsToSession(errors);
    }

    /**
     * Delegates to {@link ActionTestModule#getActionErrors}
     */
    protected ActionMessages getActionErrors()
    {
        return actionTestModule.getActionErrors();
    }
    
    /**
     * Delegates to {@link ActionTestModule#getActionErrorsFromRequest}
     */
    protected ActionMessages getActionErrorsFromRequest()
    {
        return actionTestModule.getActionErrorsFromRequest();
    }    
    
    /**
     * Delegates to {@link ActionTestModule#getActionErrorsFromSession}
     */
    protected ActionMessages getActionErrorsFromSession()
    {
        return actionTestModule.getActionErrorsFromSession();
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
     * Delegates to {@link ActionTestModule#setActionMessagesToSession}
     */
    protected void setActionMessagesToSession(ActionMessages messages)
    {
        actionTestModule.setActionMessagesToSession(messages);
    }

    /**
     * Delegates to {@link ActionTestModule#getActionMessages}
     */
    protected ActionMessages getActionMessages()
    {
        return actionTestModule.getActionMessages();
    }
    
    /**
     * Delegates to {@link ActionTestModule#getActionMessagesFromRequest}
     */
    protected ActionMessages getActionMessagesFromRequest()
    {
        return actionTestModule.getActionMessagesFromRequest();
    }
    
    /**
     * Delegates to {@link ActionTestModule#getActionMessagesFromSession}
     */
    protected ActionMessages getActionMessagesFromSession()
    {
        return actionTestModule.getActionMessagesFromSession();
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
     * Delegates to {@link ActionTestModule#setActionForm}
     */
    protected void setActionForm(ActionForm formObj)
    {
        actionTestModule.setActionForm(formObj);
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
     * Delegates to {@link ActionTestModule#addRequestParameter(String, String[])}
     */
    protected void addRequestParameter(String key, String[] values)
    {
        actionTestModule.addRequestParameter(key, values);
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
     * Delegates to {@link ActionTestModule#createDynaActionForm}
     */
    protected DynaActionForm createDynaActionForm(FormBeanConfig formConfig)
    {
        return actionTestModule.createDynaActionForm(formConfig);
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
    protected ActionForward actionPerform(Class action)
    {
        return actionTestModule.actionPerform(action);
    }
    
    /**
     * Delegates to {@link ActionTestModule#actionPerform(Action)}
     */
    protected ActionForward actionPerform(Action action)
    {
        return actionTestModule.actionPerform(action);
    }

    /**
     * Delegates to {@link ActionTestModule#actionPerform(Class, Class)}
     */
    protected ActionForward actionPerform(Class action, Class form)
    {
        return actionTestModule.actionPerform(action, form);
    }
    
    /**
     * Delegates to {@link ActionTestModule#actionPerform(Action, Class)}
     */
    protected ActionForward actionPerform(Action action, Class form)
    {
        return actionTestModule.actionPerform(action, form);
    }

    /**
     * Delegates to {@link ActionTestModule#actionPerform(Class, ActionForm)}
     */
    protected ActionForward actionPerform(Class action, ActionForm form)
    {
        return actionTestModule.actionPerform(action, form);
    }
    
    /**
     * Delegates to {@link ActionTestModule#actionPerform(Action, ActionForm)}
     */
    protected ActionForward actionPerform(Action action, ActionForm form)
    {
        return actionTestModule.actionPerform(action, form);
    }
}
