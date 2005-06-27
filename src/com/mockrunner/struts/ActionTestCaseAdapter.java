package com.mockrunner.struts;

import java.util.Locale;

import javax.sql.DataSource;

import org.apache.commons.validator.ValidatorResources;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
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
 * Delegator for {@link com.mockrunner.struts.ActionTestModule}. You can
 * subclass this adapter or use {@link com.mockrunner.struts.ActionTestModule}
 * directly (so your test case can use another base class).
 * This adapter extends {@link com.mockrunner.base.BaseTestCase}.
 * It can be used if you want to use several modules in conjunction.
 * <b>This class is generated from the {@link com.mockrunner.struts.ActionTestModule}
 * and should not be edited directly</b>.
 */
public class ActionTestCaseAdapter extends HTMLOutputTestCase
{
    private ActionTestModule actionTestModule;

    public ActionTestCaseAdapter()
    {

    }

    public ActionTestCaseAdapter(String name)
    {
        super(name);
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
        actionTestModule = null;
    }

    /**
     * Creates the {@link com.mockrunner.struts.ActionTestModule}. If you
     * overwrite this method, you must call <code>super.setUp()</code>.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        actionTestModule = createActionTestModule(getActionMockObjectFactory());
    }

    /**
     * Returns the {@link com.mockrunner.struts.ActionTestModule} as
     * {@link com.mockrunner.base.HTMLOutputModule}.
     * @return the {@link com.mockrunner.base.HTMLOutputModule}
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
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setReset(boolean)}
     */
    protected void setReset(boolean reset)
    {
        actionTestModule.setReset(reset);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setDoPopulate(boolean)}
     */
    protected void setDoPopulate(boolean doPopulate)
    {
        actionTestModule.setDoPopulate(doPopulate);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setRecognizeMessagesInSession(boolean)}
     */
    protected void setRecognizeMessagesInSession(boolean recognizeInSession)
    {
        actionTestModule.setRecognizeMessagesInSession(recognizeInSession);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setMessageAttributeKey(String)}
     */
    protected void setMessageAttributeKey(String messageAttributeKey)
    {
        actionTestModule.setMessageAttributeKey(messageAttributeKey);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setErrorAttributeKey(String)}
     */
    protected void setErrorAttributeKey(String errorAttributeKey)
    {
        actionTestModule.setErrorAttributeKey(errorAttributeKey);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#addMappedPropertyRequestPrefix(String)}
     */
    protected String addMappedPropertyRequestPrefix(String property)
    {
        return actionTestModule.addMappedPropertyRequestPrefix(property);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getActionMapping}
     */
    protected ActionMapping getActionMapping()
    {
        return actionTestModule.getActionMapping();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setValidate(boolean)}
     */
    protected void setValidate(boolean validate)
    {
        actionTestModule.setValidate(validate);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#addExceptionHandler(ExceptionHandlerConfig)}
     */
    protected void addExceptionHandler(ExceptionHandlerConfig handler)
    {
        actionTestModule.addExceptionHandler(handler);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setResources(String, MessageResources)}
     */
    protected void setResources(String key, MessageResources resources)
    {
        actionTestModule.setResources(key, resources);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setResources(MessageResources)}
     */
    protected void setResources(MessageResources resources)
    {
        actionTestModule.setResources(resources);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setDataSource(String, DataSource)}
     */
    protected void setDataSource(String key, DataSource dataSource)
    {
        actionTestModule.setDataSource(key, dataSource);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setDataSource(DataSource)}
     */
    protected void setDataSource(DataSource dataSource)
    {
        actionTestModule.setDataSource(dataSource);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#createValidatorResources(String[])}
     */
    protected ValidatorResources createValidatorResources(String[] resourcesFiles)
    {
        return actionTestModule.createValidatorResources(resourcesFiles);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setValidatorResources(ValidatorResources)}
     */
    protected void setValidatorResources(ValidatorResources validatorResources)
    {
        actionTestModule.setValidatorResources(validatorResources);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyForward(String)}
     */
    protected void verifyForward(String path)
    {
        actionTestModule.verifyForward(path);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getActionForward}
     */
    protected MockActionForward getActionForward()
    {
        return actionTestModule.getActionForward();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyForwardName(String)}
     */
    protected void verifyForwardName(String name)
    {
        actionTestModule.verifyForwardName(name);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyRedirect(boolean)}
     */
    protected void verifyRedirect(boolean redirect)
    {
        actionTestModule.verifyRedirect(redirect);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyNoActionErrors}
     */
    protected void verifyNoActionErrors()
    {
        actionTestModule.verifyNoActionErrors();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getActionErrors}
     */
    protected ActionMessages getActionErrors()
    {
        return actionTestModule.getActionErrors();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyNoActionMessages}
     */
    protected void verifyNoActionMessages()
    {
        actionTestModule.verifyNoActionMessages();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getActionMessages}
     */
    protected ActionMessages getActionMessages()
    {
        return actionTestModule.getActionMessages();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyHasActionErrors}
     */
    protected void verifyHasActionErrors()
    {
        actionTestModule.verifyHasActionErrors();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyHasActionMessages}
     */
    protected void verifyHasActionMessages()
    {
        actionTestModule.verifyHasActionMessages();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyActionErrorPresent(String)}
     */
    protected void verifyActionErrorPresent(String errorKey)
    {
        actionTestModule.verifyActionErrorPresent(errorKey);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyActionMessagePresent(String)}
     */
    protected void verifyActionMessagePresent(String messageKey)
    {
        actionTestModule.verifyActionMessagePresent(messageKey);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyActionErrorNotPresent(String)}
     */
    protected void verifyActionErrorNotPresent(String errorKey)
    {
        actionTestModule.verifyActionErrorNotPresent(errorKey);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyActionMessageNotPresent(String)}
     */
    protected void verifyActionMessageNotPresent(String messageKey)
    {
        actionTestModule.verifyActionMessageNotPresent(messageKey);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyActionErrors(String[])}
     */
    protected void verifyActionErrors(String[] errorKeys)
    {
        actionTestModule.verifyActionErrors(errorKeys);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyActionMessages(String[])}
     */
    protected void verifyActionMessages(String[] messageKeys)
    {
        actionTestModule.verifyActionMessages(messageKeys);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyActionErrorValues(String, Object[])}
     */
    protected void verifyActionErrorValues(String errorKey, Object[] values)
    {
        actionTestModule.verifyActionErrorValues(errorKey, values);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getActionErrorByKey(String)}
     */
    protected ActionMessage getActionErrorByKey(String errorKey)
    {
        return actionTestModule.getActionErrorByKey(errorKey);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyActionMessageValues(String, Object[])}
     */
    protected void verifyActionMessageValues(String messageKey, Object[] values)
    {
        actionTestModule.verifyActionMessageValues(messageKey, values);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getActionMessageByKey(String)}
     */
    protected ActionMessage getActionMessageByKey(String messageKey)
    {
        return actionTestModule.getActionMessageByKey(messageKey);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyActionErrorValue(String, Object)}
     */
    protected void verifyActionErrorValue(String errorKey, Object value)
    {
        actionTestModule.verifyActionErrorValue(errorKey, value);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyActionMessageValue(String, Object)}
     */
    protected void verifyActionMessageValue(String messageKey, Object value)
    {
        actionTestModule.verifyActionMessageValue(messageKey, value);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyActionErrorProperty(String, String)}
     */
    protected void verifyActionErrorProperty(String errorKey, String property)
    {
        actionTestModule.verifyActionErrorProperty(errorKey, property);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyActionMessageProperty(String, String)}
     */
    protected void verifyActionMessageProperty(String messageKey, String property)
    {
        actionTestModule.verifyActionMessageProperty(messageKey, property);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyNumberActionErrors(int)}
     */
    protected void verifyNumberActionErrors(int number)
    {
        actionTestModule.verifyNumberActionErrors(number);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#verifyNumberActionMessages(int)}
     */
    protected void verifyNumberActionMessages(int number)
    {
        actionTestModule.verifyNumberActionMessages(number);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setActionMessages(ActionMessages)}
     */
    protected void setActionMessages(ActionMessages messages)
    {
        actionTestModule.setActionMessages(messages);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setActionMessagesToSession(ActionMessages)}
     */
    protected void setActionMessagesToSession(ActionMessages messages)
    {
        actionTestModule.setActionMessagesToSession(messages);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getActionMessagesFromRequest}
     */
    protected ActionMessages getActionMessagesFromRequest()
    {
        return actionTestModule.getActionMessagesFromRequest();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getActionMessagesFromSession}
     */
    protected ActionMessages getActionMessagesFromSession()
    {
        return actionTestModule.getActionMessagesFromSession();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#hasActionMessages}
     */
    protected boolean hasActionMessages()
    {
        return actionTestModule.hasActionMessages();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setActionErrors(ActionMessages)}
     */
    protected void setActionErrors(ActionMessages errors)
    {
        actionTestModule.setActionErrors(errors);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setActionErrorsToSession(ActionMessages)}
     */
    protected void setActionErrorsToSession(ActionMessages errors)
    {
        actionTestModule.setActionErrorsToSession(errors);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getActionErrorsFromRequest}
     */
    protected ActionMessages getActionErrorsFromRequest()
    {
        return actionTestModule.getActionErrorsFromRequest();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getActionErrorsFromSession}
     */
    protected ActionMessages getActionErrorsFromSession()
    {
        return actionTestModule.getActionErrorsFromSession();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#hasActionErrors}
     */
    protected boolean hasActionErrors()
    {
        return actionTestModule.hasActionErrors();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getMockActionMapping}
     */
    protected MockActionMapping getMockActionMapping()
    {
        return actionTestModule.getMockActionMapping();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getMockPageContext}
     */
    protected MockPageContext getMockPageContext()
    {
        return actionTestModule.getMockPageContext();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getLastAction}
     */
    protected Action getLastAction()
    {
        return actionTestModule.getLastAction();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#generateValidToken}
     */
    protected void generateValidToken()
    {
        actionTestModule.generateValidToken();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#getActionForm}
     */
    protected ActionForm getActionForm()
    {
        return actionTestModule.getActionForm();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setActionForm(ActionForm)}
     */
    protected void setActionForm(ActionForm formObj)
    {
        actionTestModule.setActionForm(formObj);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#createActionForm(Class)}
     */
    protected ActionForm createActionForm(Class form)
    {
        return actionTestModule.createActionForm(form);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#createDynaActionForm(FormBeanConfig)}
     */
    protected DynaActionForm createDynaActionForm(FormBeanConfig formConfig)
    {
        return actionTestModule.createDynaActionForm(formConfig);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#populateRequestToForm}
     */
    protected void populateRequestToForm()
    {
        actionTestModule.populateRequestToForm();
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#actionPerform(Action)}
     */
    protected ActionForward actionPerform(Action action)
    {
        return actionTestModule.actionPerform(action);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#actionPerform(Action, ActionForm)}
     */
    protected ActionForward actionPerform(Action action, ActionForm form)
    {
        return actionTestModule.actionPerform(action, form);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#actionPerform(Class)}
     */
    protected ActionForward actionPerform(Class action)
    {
        return actionTestModule.actionPerform(action);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#actionPerform(Action, Class)}
     */
    protected ActionForward actionPerform(Action action, Class form)
    {
        return actionTestModule.actionPerform(action, form);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#actionPerform(Class, Class)}
     */
    protected ActionForward actionPerform(Class action, Class form)
    {
        return actionTestModule.actionPerform(action, form);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#actionPerform(Class, ActionForm)}
     */
    protected ActionForward actionPerform(Class action, ActionForm form)
    {
        return actionTestModule.actionPerform(action, form);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setInput(String)}
     */
    protected void setInput(String input)
    {
        actionTestModule.setInput(input);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setLocale(Locale)}
     */
    protected void setLocale(Locale locale)
    {
        actionTestModule.setLocale(locale);
    }

    /**
     * Delegates to {@link com.mockrunner.struts.ActionTestModule#setParameter(String)}
     */
    protected void setParameter(String parameter)
    {
        actionTestModule.setParameter(parameter);
    }
}