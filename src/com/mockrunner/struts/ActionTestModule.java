package com.mockrunner.struts;

import java.util.Iterator;

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

import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.web.MockActionForward;
import com.mockrunner.mock.web.MockActionMapping;
import com.mockrunner.mock.web.MockPageContext;
import com.mockrunner.mock.web.WebMockObjectFactory;

/**
 * Module for Struts action tests. Simulates Struts
 * without reading the <i>struts-config.xml</i> file.
 * Per default this class does everything like Struts
 * when calling an action but you can change the behaviour
 * (e.g. disable form poulation).
 */
public class ActionTestModule
{
    private WebMockObjectFactory mockFactory;
    private MockActionForward forward;
    private ActionForm formObj;
    private Action actionObj;
    private boolean reset;
    private boolean doPopulate;
    
    public ActionTestModule(WebMockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
        reset = true;
        doPopulate = true;
    }
    
    /**
     * Set if the reset method should be called before
     * populating a form with {@link #populateRequestToForm}.
     * Default is <code>true</code> which is the standard Struts 
     * behaviour.
     * @param reset should reset be called
     */
    public void setReset(boolean reset)
    {
        this.reset = reset;
    }
    
    /**
     * Set if the form should be populated with the request
     * parameters before calling the action.
     * Default is <code>true</code> which is the standard Struts
     * behaviour.
     * @param doPopulate should population be performed
     */
    public void setDoPopulate(boolean doPopulate)
    {
        this.doPopulate = doPopulate;
    }

    /**
     * Convinience method for map backed properties. Creates a String
     * <i>value(property)</i>.
     * @param property the property
     * @return the String in map backed propery style
     */
    public String addMappedPropertyRequestPrefix(String property)
    {
        return "value(" + property + ")";
    }

    /**
     * Sets the parameter by delegating to {@link MockActionMapping#setParameter}.
     * You can test your Actions with different parameter settings in the
     * same test method.
     * @param parameter the parameter
     */
    public void setParameter(String parameter)
    {
        getMockActionMapping().setParameter(parameter);
    }

    /**
     * Sets if Form validation should be performed before calling the action.
     * Delegates to {@link MockActionMapping#setValidate}. Default is false.
     * @param validate should validation be performed
     */
    public void setValidate(boolean validate)
    {
        getMockActionMapping().setValidate(validate);
    }
    
    /**
     * Sets the input attribute. If form validation fails, the
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
     * @param path the expected path
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
     * Verifies the redirect attribute.
     * @param redirect the expected redirect attribute
     * @throws VerifyFailedException if verification fails
     */
    public void verifyRedirect(boolean redirect)
    {
        if (null == getActionForward())
        {
            throw new VerifyFailedException("ActionForward == null");
        }
        else if(!getActionForward().verifyRedirect(redirect))
        {
            throw new VerifyFailedException("expected " + redirect + ", received " + getActionForward().getRedirect());
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
     * @param errorKey the expected error key
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionErrorPresent(String errorKey)
    {
        verifyActionMessagePresent(errorKey, getActionErrors());
    }
    
    /**
     * Verifies that an action message with the specified key
     * is present.
     * @param messageKey the expected message key
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
     * @param errorKeys the array of expected error keys
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionErrors(String errorKeys[])
    {
        verifyActionMessages(errorKeys, getActionErrors());  
    }
    
    /**
     * Verifies that the specified action messages are present.
     * Regards number and order of action messages.
     * @param messageKeys the array of expected message keys
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
    
    /**
     * Verifies the values of the action error with the
     * specified key. Regards number and order of values.
     * @param errorKey the error key
     * @param values the exepcted values
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionErrorValues(String errorKey, Object[] values)
    {
        ActionError error = getActionErrorByKey(errorKey);
        if(null == error) throw new VerifyFailedException("action error " + errorKey + " not present");
        verifyActionMessageValues(error, values);
    }
    
    /**
     * Verifies the values of the action message with the
     * specified key. Regards number and order of values.
     * @param messageKey the message key
     * @param values the exepcted values
     * @throws VerifyFailedException if verification fails
     */
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
    
    /**
     * Verifies the value of the action error with the
     * specified key. Fails if the specified value does
     * not match the actual value or if the error has more
     * than one value.
     * @param errorKey the error key
     * @param value the exepcted value
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionErrorValue(String errorKey, Object value)
    {
        verifyActionErrorValues(errorKey, new Object[] { value });
    }
    
    /**
     * Verifies the value of the action message with the
     * specified key. Fails if the specified value does
     * not match the actual value or if the message has more
     * than one value.
     * @param messageKey the message key
     * @param value the exepcted value
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionMessageValue(String messageKey, Object value)
    {
        verifyActionMessageValues(messageKey, new Object[] { value });
    }
    
    /**
     * Verifies that the specified error is stored for the specified
     * property.
     * @param errorKey the error key
     * @param property the exepcted value
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionErrorProperty(String errorKey, String property)
    {
        verifyActionMessageProperty(errorKey, property, getActionErrors());
    }
    
    /**
     * Verifies that the specified message is stored for the specified
     * property.
     * @param messageKey the message key
     * @param property the exepcted value
     * @throws VerifyFailedException if verification fails
     */
    public void verifyActionMessageProperty(String messageKey, String property)
    {
        verifyActionMessageProperty(messageKey, property, getActionMessages());
    }
    
    private void verifyActionMessageProperty(String messageKey, String property, ActionMessages messages)
    {
        verifyActionMessagePresent(messageKey, messages);
        Iterator iterator = messages.get(property);
        while(iterator.hasNext())
        {
              ActionMessage message = (ActionMessage)iterator.next();
              if(message.getKey().equals(messageKey)) return;
        }
        throw new VerifyFailedException("action message/error " + messageKey + " not present for property " + property);
    }
    
    /**
     * Verifies the number of action errors.
     * @param number the expected number of errors
     * @throws VerifyFailedException if verification fails
     */
    public void verifyNumberActionErrors(int number)
    {
        verifyNumberActionMessages(number, getActionErrors());
    }
    
    /**
     * Verifies the number of action messages.
     * @param number the expected number of messages
     * @throws VerifyFailedException if verification fails
     */
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

    /**
     * Returns the action error with the specified key or null
     * if such an error does not exist.
     * @param errorKey the error key
     * @return the action error with the specified key
     */
    public ActionError getActionErrorByKey(String errorKey)
    {
        return (ActionError)getActionMessageByKey(errorKey, getActionErrors());
    }
    
    /**
     * Returns the action message with the specified key or null
     * if such a message does not exist.
     * @param errorKey the error key
     * @return the action message with the specified key
     */
    public ActionMessage getActionMessageByKey(String messageKey)
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
    
    /**
     * Sets the specified <code>ActionMessages</code> object 
     * as the currently present messages.
     * @param messages the ActionMessages object
     */
    public void setActionMessages(ActionMessages messages)
    {
        mockFactory.getWrappedRequest().setAttribute(Globals.MESSAGE_KEY, messages);
    }

    /**
     * Get the currently present action messages. Can be called
     * after {@link #actionPerform} to get the messages the action
     * has set.
     * @return the action messages
     */
    public ActionMessages getActionMessages()
    {
        return (ActionMessages) mockFactory.getWrappedRequest().getAttribute(Globals.MESSAGE_KEY);
    }
    
    /**
     * Returns if action messages are present.
     * @return true if messages are present, false otherwise
     */
    public boolean hasActionMessages()
    {
        ActionMessages messages = getActionMessages();
        return containsMessages(messages);
    }

    /**
     * Sets the specified <code>ActionErrors</code> object 
     * as the currently present errors.
     * @param messages the ActionErrors object
     */
    public void setActionErrors(ActionErrors errors)
    {
        mockFactory.getWrappedRequest().setAttribute(Globals.ERROR_KEY, errors);
    }

    /**
     * Get the currently present action errors. Can be called
     * after {@link #actionPerform} to get the errors the action
     * has set.
     * @return the action errors
     */
    public ActionErrors getActionErrors()
    {
        return (ActionErrors) mockFactory.getWrappedRequest().getAttribute(Globals.ERROR_KEY);
    }

    /**
     * Returns if action errors are present.
     * @return true if errors are present, false otherwise
     */
    public boolean hasActionErrors()
    {
        ActionErrors errors = getActionErrors();
        return containsMessages(errors);
    }

    /**
     * Returns the <code>MockActionMapping</code> passed to 
     * the action. Can be manipulated before and after 
     * {@link #actionPerform}.
     * Delegates to {@link com.mockrunner.mock.web.WebMockObjectFactory#getMockActionMapping}.
     * @return the MockActionMapping
     */
    public MockActionMapping getMockActionMapping()
    {
        return mockFactory.getMockActionMapping();
    }

    /**
     * Returns the <code>MockPageContext</code> object.
     * Delegates to {@link com.mockrunner.mock.web.WebMockObjectFactory#getMockPageContext}.
     * @return the MockPageContext
     */
    public MockPageContext getMockPageContext()
    {
        return mockFactory.getMockPageContext();
    }

    /**
     * Returns the current <code>ActionForward</code>. 
     * Can be called after {@link #actionPerform} to get 
     * the <code>ActionForward</code> the action
     * has returned.
     * @return the MockActionForward
     */
    public MockActionForward getActionForward()
    {
        return forward;
    }

    /**
     * Returns the last tested <code>Action</code> object.
     * @return the <code>Action</code> object
     */
    public Action getLastAction()
    {
        return actionObj;
    }

    /**
     * Adds an empty request parameter. Same as
     * <code>addRequestParameter(key, "")</code>.
     * @param key the request key
     */
    public void addRequestParameter(String key)
    {
        addRequestParameter(key, "");
    }

    /**
     * Adds a request parameter. Request parameters are populated
     * to the <code>ActionForm</code>. To add parameters for map 
     * backed properties use the <i>value(property)</i> style.
     * @param key the request key
     * @param key the request value
     */
    public void addRequestParameter(String key, String value)
    {
        mockFactory.getMockRequest().setupAddParameter(key, value);
    }

    /**
     * Generates a token and sets it to the session and the request.
     */
    public void generateValidToken()
    {
        String token = String.valueOf(Math.random());
        mockFactory.getMockSession().setAttribute(Globals.TRANSACTION_TOKEN_KEY, token);
        addRequestParameter(Constants.TOKEN_KEY, token);
    }
    
    /**
     * Returns the currently set <code>ActionForm</code>.
     * @return the <code>ActionForm</code> object
     */
    public ActionForm getActionForm()
    {
        return formObj;
    }

    /**
     * Sets the specified <code>ActionForm</code> object as the
     * current <code>ActionForm</code>. Will be used in next test.
     * @param formObj the <code>ActionForm</code> object
     */
    public void setActionForm(ActionForm formObj)
    {
        this.formObj = formObj;
    }

    /**
     * Creates a new <code>ActionForm</code> object of the specified
     * type and sets it as the current <code>ActionForm</code>. Will be 
     * used in next test.
     * @param form the <code>Class</code> of the form
     */
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

    /**
     * Populates the current request parameters to the
     * <code>ActionForm</code>. The form will be reseted 
     * before populating if reset is enabled ({@link #setReset}.
     * If form validation is enabled (use {@link #setValidate}) the
     * form will be validated after populating it and the
     * appropriate <code>ActionErrors</code> will be set.
     */
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

    /**
     * Calls the action of the specified type using
     * no <code>ActionForm</code>.
     * @param action the <code>Class</code> of the action
     * @return the resulting <code>ActionForward</code>
     */
    public ActionForward actionPerform(Class action)
    {
        try
        {
            return actionPerform(action, (ActionForm) null);
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Calls the specified action using
     * no <code>ActionForm</code>.
     * @param action the <code>Action</code>
     * @return the resulting <code>ActionForward</code>
     */
    public ActionForward actionPerform(Action action)
    {
        try
        {
            return actionPerform(action, (ActionForm) null);
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }

    /**
     * Calls the action of the specified type using
     * the <code>ActionForm</code> of the specified type. 
     * Creates the appropriate <code>ActionForm</code> and 
     * populates it before calling the action (if populating is
     * disabled, the form will not be populated, use {@link #setDoPopulate}). 
     * If form validation is enabled (use {@link #setValidate}) and 
     * fails, the action will not be called. In this case,
     * the returned  <code>ActionForward</code> is based on the 
     * input attribute. (Set it with {@link #setInput}).
     * @param action the <code>Class</code> of the action
     * @param form the <code>Class</code> of the form
     * @return the resulting <code>ActionForward</code>
     */
    public ActionForward actionPerform(Class action, Class form)
    {
        try
        {
            createActionForm(form);
            return actionPerform(action, formObj);
        }
        catch (Exception exc)
        {
            exc.printStackTrace(System.out);
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Calls the specified action using
     * the <code>ActionForm</code> of the specified type. 
     * Creates the appropriate <code>ActionForm</code> and 
     * populates it before calling the action (if populating is
     * disabled, the form will not be populated, use {@link #setDoPopulate}). 
     * If form validation is enabled (use {@link #setValidate}) and 
     * fails, the action will not be called. In this case,
     * the returned  <code>ActionForward</code> is based on the 
     * input attribute. (Set it with {@link #setInput}).
     * @param action the <code>Action</code>
     * @param form the <code>Class</code> of the form
     * @return the resulting <code>ActionForward</code>
     */
    public ActionForward actionPerform(Action action, Class form)
    {
        try
        {
            createActionForm(form);
            return actionPerform(action, formObj);
        }
        catch (Exception exc)
        {
            exc.printStackTrace(System.out);
            throw new RuntimeException(exc.getMessage());
        }
    }

    /**
     * Calls the action of the specified type using
     * the specified <code>ActionForm</code> object. The form 
     * will be populated before the action is called (if populating is
     * disabled, the form will not be populated, use {@link #setDoPopulate}).
     * Please note that request parameters will eventually overwrite
     * form values. Furthermore the form will be reseted
     * before populating it. If you do not want that, disable reset 
     * using {@link #setReset}. If form validation is enabled 
     * (use {@link #setValidate}) and fails, the action will not be 
     * called. In this case, the returned <code>ActionForward</code> 
     * is based on the input attribute. (Set it with {@link #setInput}).
     * @param action the <code>Class</code> of the action
     * @param form the <code>ActionForm</code> object
     * @return the resulting <code>ActionForward</code>
     */
    public ActionForward actionPerform(Class action, ActionForm form)
    {
        try
        {
            return actionPerform((Action)action.newInstance(), form);
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            throw new RuntimeException(exc.getMessage());
        }
    }
    
    /**
     * Calls the specified action using
     * the specified <code>ActionForm</code> object. The form 
     * will be populated before the action is called (if populating is
     * disabled, the form will not be populated, use {@link #setDoPopulate}).
     * Please note that request parameters will eventually overwrite
     * form values. Furthermore the form will be reseted
     * before populating it. If you do not want that, disable reset 
     * using {@link #setReset}. If form validation is enabled 
     * (use {@link #setValidate}) and fails, the action will not be 
     * called. In this case, the returned <code>ActionForward</code> 
     * is based on the input attribute. (Set it with {@link #setInput}).
     * @param action the <code>Action</code>
     * @param form the <code>ActionForm</code> object
     * @return the resulting <code>ActionForward</code>
     */
    public ActionForward actionPerform(Action action, ActionForm form)
    {
        try
        {
            actionObj = action;
            formObj = form;
            setActionErrors(null);
            if (null != formObj)
            {
                handleActionForm();
            }
            if (!hasActionErrors())
            {
                ActionForward currentForward = (ActionForward) actionObj.execute(getMockActionMapping(), formObj, mockFactory.getWrappedRequest(), mockFactory.getWrappedResponse());
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
        return getActionForward();
    }

    private void setResult(ActionForward currentForward)
    {
        if (null == currentForward)
        {
            forward = null;
        }
        else
        {
            forward = new MockActionForward(currentForward.getName(), currentForward.getPath(), currentForward.getRedirect(), currentForward.getContextRelative());
        }
    }

    private void handleActionForm() throws Exception
    {
        if(reset) getActionForm().reset(getMockActionMapping(), mockFactory.getWrappedRequest());
        if(doPopulate) populateMockRequest();
        if(getMockActionMapping().getValidate())
        {
            ActionErrors errors = formObj.validate(getMockActionMapping(), mockFactory.getWrappedRequest());
            if (containsMessages(errors))
            {
                mockFactory.getWrappedRequest().setAttribute(Globals.ERROR_KEY, errors);
            }
        }
    }

    private void populateMockRequest() throws Exception
    {
        BeanUtils.populate(getActionForm(), mockFactory.getWrappedRequest().getParameterMap());
    }
   
    private boolean containsMessages(ActionMessages messages)
    {
        return (null != messages) && (messages.size() > 0);
    }
}
