package de.mockrunner.base;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.taglib.html.Constants;

import de.mockrunner.mock.MockActionForward;
import de.mockrunner.mock.MockActionMapping;
import de.mockrunner.mock.MockPageContext;

/**
 * Module for Struts action tests. Simulates the Struts container
 * without reading the <i>struts-config.xml</i> file
 */
public class ActionTestModule
{
    private MockObjectFactory mockFactory;
    private MockActionForward forward;
    private ActionForm formObj;
    private Action actionObj;
    
    public ActionTestModule(MockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }

    /**
     * Convinience method for nested properties. Creates a String
     * <i>value(property)</i>.
     * @param property the property
     */
    public String addMappedPropertyRequestPrefix(String property)
    {
        return "value(" + property + ")";
    }

    /**
     * Sets the parameter by delegating to {@link MockActionMapping#setParameter}.
     * You can test your Actions with different parameter settings.
     * @param parameter the parameter
     */
    public void setParameter(String parameter)
    {
        getMockActionMapping().setParameter(parameter);
    }

    /**
     * Sets if Form validation should be performed before calling the Action.
     * Delegates to {@link MockActionMapping#setValidate}.
     * @param validate should validation be performed
     */
    public void setValidate(boolean validate)
    {
        getMockActionMapping().setValidate(validate);
    }
    
    /**
     * Sets the input attribute. If Form validation fails, the
     * input attribute can be verified with {@link #verifyForward}.
     * Delegates to {@link MockActionMapping#setInput}.
     * @param input the input attribute
     */
    public void setInput(String input)
    {
        getMockActionMapping().setInput(input);
    }

    /**
     * Verifies the forward path returned by the action.
     * Delegates to {@link MockActionMapping#setValidate}.
     * @param validate should validation be performed
     * @throws VerifyFailedException if verification fails
     */
    public void verifyForward(String path)
    {
        if (null == getActionForward())
        {
            throw new VerifyFailedException("ActionForward == null");
        }
        else if (!getActionForward().verifyName(path))
        {
            throw new VerifyFailedException("expected " + path + ", received " + getActionForward().getPath());
        }
    }

    /**
     * Verifies that there are no action errors present.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNoActionErrors()
    {
        verifyNoActionMessages(getActionErrors());   
    }
    
    /**
     * Verifies that there are no action messages present.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNoActionMessages()
    {
        verifyNoActionMessages(getActionMessages());   
    }
    
    private void verifyNoActionMessages(ActionMessages messages)
    {
        if (containsMessages(messages))
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("has the following messages/errors: ");
            Iterator iterator = messages.get();
            while (iterator.hasNext())
            {
                ActionError error = (ActionError) iterator.next();
                buffer.append(error.getKey() + ";");
            }
            throw new VerifyFailedException(buffer.toString());
        }
    }

    /**
     * Verifies that there are action errors present.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyHasActionErrors()
    {
        if (!containsMessages(getActionErrors()))
        {
            throw new VerifyFailedException("no action errors");
        }
    }
    
    /**
     * Verifies that there are action messages present.
     * @throws VerifyFailedException if verification fails
     */
    public void verifyHasActionMessages()
    {
        if (!containsMessages(getActionMessages()))
        {
            throw new VerifyFailedException("no action messages");
        }
    }

    /**
     * Verifies that an action error with the specified key
     * is present.
     * @param errorKey the error key
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionErrorPresent(String errorKey)
    {
        verifyActionMessagePresent(errorKey, getActionErrors());
    }
    
    /**
     * Verifies that an action message with the specified key
     * is present.
     * @param messageKey the message key
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionMessagePresent(String messageKey)
    {
        verifyActionMessagePresent(messageKey, getActionMessages());
    }
    
    private void verifyActionMessagePresent(String messageKey, ActionMessages messages)
    {
        if (!containsMessages(messages)) throw new VerifyFailedException("no action messages/errors");
        Iterator iterator = messages.get();
        while (iterator.hasNext())
        {
            ActionMessage message = (ActionMessage) iterator.next();
            if (message.getKey().equals(messageKey))
            {
                return;
            }
        }
        throw new VerifyFailedException("message/error " + messageKey + " not present");
    }
    
    /**
     * Verifies that an action error with the specified key
     * is not present.
     * @param errorKey the error key
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionErrorNotPresent(String errorKey)
    {
        verifyActionMessageNotPresent(errorKey, getActionErrors());
    }
    
    /**
     * Verifies that an action message with the specified key
     * is not present.
     * @param messageKey the message key
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionMessageNotPresent(String messageKey)
    {
        verifyActionMessageNotPresent(messageKey, getActionMessages());
    }
    
    private void verifyActionMessageNotPresent(String messageKey, ActionMessages messages)
    {
        Iterator iterator = messages.get();
        while (iterator.hasNext())
        {
            ActionMessage message = (ActionMessage) iterator.next();
            if (message.getKey().equals(messageKey))
            {
                throw new VerifyFailedException("message/error " + messageKey + " present");
            }
        }
    }

    /**
     * Verifies that the specified action errors are present.
     * Regards number and order of action errors.
     * @param errorKeys the array of error keys
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionErrors(String errorKeys[])
    {
        verifyActionMessages(errorKeys, getActionErrors());  
    }
    
    /**
     * Verifies that the specified action messages are present.
     * Regards number and order of action messages.
     * @param messageKeys the array of message keys
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionMessages(String messageKeys[])
    {
        verifyActionMessages(messageKeys, getActionMessages());  
    }
    
    private void verifyActionMessages(String messageKeys[], ActionMessages messages)
    {
        if (!containsMessages(messages)) throw new VerifyFailedException("no action messages/errors");
        if(messages.size() != messageKeys.length) throw new VerifyFailedException("expected " + messageKeys.length + " messages/errors, received " + messages.size() + " messages/errors");
        Iterator iterator = messages.get();
        for (int ii = 0; ii < messageKeys.length; ii++)
        {
            ActionMessage message = (ActionMessage) iterator.next();
            if (!message.getKey().equals(messageKeys[ii]))
            {
                throw new VerifyFailedException("mismatch at position " + ii + ", actual: " + message.getKey() + ", expected: " + messageKeys[ii]);
            }
        }
    }
    
    public void verifyActionErrorValues(String errorKey, Object[] values)
    {
        ActionError error = getActionErrorByKey(errorKey);
        if(null == error) throw new VerifyFailedException("action error " + errorKey + " not present");
        verifyActionMessageValues(error, values);
    }
    
    public void verifyActionMessageValues(String messageKey, Object[] values)
    {
        ActionMessage message = getActionMessageByKey(messageKey);
        if(null == message) throw new VerifyFailedException("action message " + messageKey + " not present");
        verifyActionMessageValues(message, values);
    }
    
    private void verifyActionMessageValues(ActionMessage message, Object[] values)
    {
        Object[] actualValues = message.getValues();
        if(null == actualValues) throw new VerifyFailedException("action message/error " + message.getKey() + " has no values");
        if(values.length != actualValues.length) throw new VerifyFailedException("action message/error " + message.getKey() + " has " + actualValues + " values");
        for(int ii = 0; ii < actualValues.length; ii++)
        {
            if(!values[ii].equals(actualValues[ii]))
            {
                throw new VerifyFailedException("action message/error " + message.getKey() + ": expected value[" + ii + "]: " + values[ii] + " received value[" + ii + "]: " + actualValues[ii]);
            }
        }
    }
    
    public void verifyActionErrorValue(String errorKey, Object value)
    {
        verifyActionErrorValues(errorKey, new Object[] { value });
    }
    
    public void verifyActionMessageValue(String messageKey, Object value)
    {
        verifyActionMessageValues(messageKey, new Object[] { value });
    }
    
    public void verifyNumberActionErrors(int number)
    {
        verifyNumberActionMessages(number, getActionErrors());
    }
    
    public void verifyNumberActionMessages(int number)
    {
        verifyNumberActionMessages(number, getActionMessages());
    }
   
    private void verifyNumberActionMessages(int number, ActionMessages messages)
    {
        if (null != messages)
        {
            if (messages.size() == number) return;
            throw new VerifyFailedException("expected " + number + " messages/errors, received " + messages.size() + " messages/errors");
        }
        if (number == 0) return;
        throw new VerifyFailedException("no action messages/errors");
    }

    public ActionError getActionErrorByKey(String errorKey)
    {
        return (ActionError)getActionMessageByKey(errorKey, getActionErrors());
    }
    
    public ActionError getActionMessageByKey(String messageKey)
    {
        return (ActionError)getActionMessageByKey(messageKey, getActionMessages());
    }
    
    private ActionMessage getActionMessageByKey(String messageKey, ActionMessages messages)
    {
        if(null == messages) return null;
        Iterator iterator = messages.get();
        while (iterator.hasNext())
        {
            ActionMessage message = (ActionMessage) iterator.next();
            if (message.getKey().equals(messageKey))
            {
                return message;
            }
        }
        return null;
    }
    
    public void setActionMessages(ActionMessages messages)
    {
        mockFactory.getMockRequest().setAttribute(Globals.MESSAGE_KEY, messages);
    }

    public ActionMessages getActionMessages()
    {
        return (ActionMessages) mockFactory.getMockRequest().getAttribute(Globals.MESSAGE_KEY);
    }
    
    public boolean hasActionMessages()
    {
        ActionMessages messages = getActionMessages();
        return containsMessages(messages);
    }

    public void setActionErrors(ActionErrors errors)
    {
        mockFactory.getMockRequest().setAttribute(Globals.ERROR_KEY, errors);
    }

    public ActionErrors getActionErrors()
    {
        return (ActionErrors) mockFactory.getMockRequest().getAttribute(Globals.ERROR_KEY);
    }

    public boolean hasActionErrors()
    {
        ActionErrors errors = getActionErrors();
        return containsMessages(errors);
    }

    public MockActionMapping getMockActionMapping()
    {
        return mockFactory.getMockActionMapping();
    }

    public MockPageContext getMockPageContext()
    {
        return mockFactory.getMockPageContext();
    }

    public MockActionForward getActionForward()
    {
        return forward;
    }

    protected Action getLastAction()
    {
        return actionObj;
    }

    public void addRequestParameter(String key)
    {
        addRequestParameter(key, "");
    }

    public void addRequestParameter(String key, String value)
    {
        mockFactory.getMockRequest().setupAddParameter(key, value);
    }

    public void generateValidToken()
    {
        String token = String.valueOf(Math.random());
        mockFactory.getMockSession().setAttribute(Action.TRANSACTION_TOKEN_KEY, token);
        addRequestParameter(Constants.TOKEN_KEY, token);
    }
    
    public ActionForm getActionForm()
    {
        return formObj;
    }

    public void setActionForm(ActionForm formObj)
    {
        this.formObj = formObj;
    }

    public ActionForm createActionForm(Class form)
    {
        try
        {
            if (null == form)
            {
                formObj = null;
                return null;
            }
            formObj = (ActionForm) form.newInstance();
            return formObj;
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }

    public void populateRequestToForm()
    {
        try
        {
            handleActionForm();
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }

    public void actionPerform(Class action)
    {
        try
        {
            actionPerform(action, (ActionForm) null);
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }

    public void actionPerform(Class action, Class form)
    {
        try
        {
            createActionForm(form);
            actionPerform(action, formObj);
        }
        catch (Exception exc)
        {
            exc.printStackTrace(System.out);
            throw new RuntimeException(exc.getMessage());
        }
    }

    public void actionPerform(Class action, ActionForm form)
    {
        try
        {
            actionObj = (Action) action.newInstance();
            formObj = form;
            setActionErrors(null);
            if (null != formObj)
            {
                handleActionForm();
            }
            if (!hasActionErrors())
            {
                ActionForward currentForward = (ActionForward) actionObj.execute(getMockActionMapping(), formObj, mockFactory.getMockRequest(), mockFactory.getMockResponse());
                setResult(currentForward);
            }
            else
            {
                setResult(getMockActionMapping().getInputForward());
            }
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }

    private void setResult(ActionForward currentForward)
    {
        if (null == currentForward)
        {
            forward = null;
        }
        else
        {
            forward = new MockActionForward(currentForward.getName(), currentForward.getPath(), currentForward.getRedirect());
        }
    }

    private void handleActionForm() throws Exception
    {
        getActionForm().reset(getMockActionMapping(), mockFactory.getMockRequest());
        populateMockRequest();
        if (getMockActionMapping().getValidate())
        {
            ActionErrors errors = formObj.validate(getMockActionMapping(), mockFactory.getMockRequest());
            if (containsMessages(errors))
            {
                mockFactory.getMockRequest().setAttribute(Globals.ERROR_KEY, errors);
            }
        }
    }

    private void populateMockRequest() throws Exception
    {
        Map requestParameters = mockFactory.getMockRequest().getParameterMap();
        Iterator keys = requestParameters.keySet().iterator();
        while (keys.hasNext())
        {
            String key = (String) keys.next();
            String[] value = (String[]) requestParameters.get(key);
            BeanUtils.setProperty(getActionForm(), key, value[0]);
        }
    }
   
    private boolean containsMessages(ActionMessages messages)
    {
        return (null != messages) && (messages.size() > 0);
    }
}
