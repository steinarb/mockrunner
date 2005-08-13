package com.mockrunner.struts;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.ValidatorResources;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorPlugIn;

import com.mockrunner.base.HTMLOutputModule;
import com.mockrunner.base.NestedApplicationException;
import com.mockrunner.base.VerifyFailedException;
import com.mockrunner.mock.web.ActionMockObjectFactory;
import com.mockrunner.mock.web.MockActionForward;
import com.mockrunner.mock.web.MockActionMapping;
import com.mockrunner.mock.web.MockPageContext;
import com.mockrunner.util.common.StreamUtil;

/**
 * Module for Struts action tests. Simulates Struts
 * without reading the <i>struts-config.xml</i> file.
 * Per default this class does everything like Struts
 * when calling an action but you can change the behaviour
 * (e.g. disable form population).
 * Please note: If your action throws an exception and an 
 * exception handler is registered (use {@link #addExceptionHandler}),
 * the handler will be called to handle the exception.
 * Otherwise the exception will be rethrown as {@link com.mockrunner.base.NestedApplicationException}.
 */
public class ActionTestModule extends HTMLOutputModule
{
    private final static Log log = LogFactory.getLog(ActionTestModule.class);
    private ActionMockObjectFactory mockFactory;
    private MockActionForward forward;
    private ActionForm formObj;
    private Action actionObj;
    private boolean reset;
    private boolean doPopulate;
    private boolean recognizeInSession;
    private String messageAttributeKey;
    private String errorAttributeKey;
    private List exceptionHandlers;
    
    public ActionTestModule(ActionMockObjectFactory mockFactory)
    {
        super(mockFactory);
        this.mockFactory = mockFactory;
        reset = true;
        doPopulate = true;
        recognizeInSession = true;
        messageAttributeKey = Globals.MESSAGE_KEY;
        errorAttributeKey = Globals.ERROR_KEY;
        exceptionHandlers = new ArrayList();
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
     * Set if messages that are saved to the session (instead of
     * the request) should be recognized.
     * Default is <code>true</code>.
     * @param recognizeInSession should messages in the session be recognized
     */
    public void setRecognizeMessagesInSession(boolean recognizeInSession)
    {
        this.recognizeInSession = recognizeInSession;
    }
    
    /**
     * Name of the key under which messages are stored. Default is
     * <code>Globals.MESSAGE_KEY</code>.
     * @param messageAttributeKey the message key
     */
    public void setMessageAttributeKey(String messageAttributeKey)
    {
        this.messageAttributeKey = messageAttributeKey;
    }
    
    /**
     * Name of the key under which errors are stored. Default is
     * <code>Globals.ERROR_KEY</code>.
     * @param errorAttributeKey the message key
     */
    public void setErrorAttributeKey(String errorAttributeKey)
    {
        this.errorAttributeKey = errorAttributeKey;
    }

    /**
     * Convenience method for map backed properties. Creates a String
     * <i>value(property)</i>.
     * @param property the property
     * @return the String in map backed propery style
     */
    public String addMappedPropertyRequestPrefix(String property)
    {
        return "value(" + property + ")";
    }

    /**
     * Sets the parameter by calling <code>ActionMapping.setParameter</code>
     * on the action mapping returned by {@link #getActionMapping}.
     * You can test your Actions with different parameter settings in the
     * same test method.
     * @param parameter the parameter
     */
    public void setParameter(String parameter)
    {
        getActionMapping().setParameter(parameter);
    }

    /**
     * Sets if form validation should be performed before calling the action.
     * Calls <code>ActionMapping.setValidate</code> on the action mapping returned 
     * by {@link #getActionMapping}. Default is <code>false</code>.
     * @param validate should validation be performed
     */
    public void setValidate(boolean validate)
    {
        getActionMapping().setValidate(validate);
    }
    
    /**
     * Sets the input attribute. If form validation fails, the
     * input attribute can be verified with {@link #verifyForward}.
     * Calls <code>ActionMapping.setInput</code> on the action mapping returned 
     * by {@link #getActionMapping}.
     * @param input the input attribute
     */
    public void setInput(String input)
    {
        getActionMapping().setInput(input);
    }
    
    /**
     * Registers an exception handler. The exception handler will
     * be called if an action throws an exception. Usually, you
     * will pass an instance of {@link DefaultExceptionHandlerConfig}
     * to this method. {@link DefaultExceptionHandlerConfig}
     * relies on Struts <code>ExceptionHandler</code> classes.
     * In special cases, you may add own implementations of
     * {@link ExceptionHandlerConfig}, that may be independent from
     * the Struts exception handling mechanism.
     * If no matching handler is registered, the exception will be rethrown
     * as {@link com.mockrunner.base.NestedApplicationException}.
     * @param handler the exception handler
     */
    public void addExceptionHandler(ExceptionHandlerConfig handler)
    {
        if(null != handler)
        {
            exceptionHandlers.add(handler);
        }
    }
    
    /**
     * Sets the specified messages resources as a request attribute
     * using <code>Globals.MESSAGES_KEY</code> as the key. You can
     * use this method, if your action calls 
     * <code>Action.getResources(HttpServletRequest)</code>.
     * The deprecated method <code>Action.getResources()</code>
     * takes the resources from the servlet context with the same key.
     * If your action uses this method, you have to set the resources
     * manually to the servlet context.
     * @param resources the messages resources
     */
    public void setResources(MessageResources resources)
    {
        mockFactory.getWrappedRequest().setAttribute(Globals.MESSAGES_KEY, resources);
    }
    
    /**
     * Sets the specified messages resources as a servlet context 
     * attribute using the specified key and the module config prefix.
     * You can use this method, if your action calls 
     * <code>Action.getResources(HttpServletRequest, String)</code>.
     * Please note that the {@link com.mockrunner.mock.web.MockModuleConfig}
     * is set by Mockrunner as the current module. It has the name <i>testmodule</i>.
     * This can be changed with <code>ModuleConfig.setPrefix</code>.
     * @param key the key of the messages resources
     * @param resources the messages resources
     */
    public void setResources(String key, MessageResources resources)
    {
        MessageResourcesConfig config = new MessageResourcesConfig();
        config.setKey(key);
        mockFactory.getMockModuleConfig().addMessageResourcesConfig(config);
        key = key + mockFactory.getMockModuleConfig().getPrefix();
        mockFactory.getMockServletContext().setAttribute(key, resources); 
    }
    
    /**
     * Sets the specified <code>DataSource</code>.
     * You can use this method, if your action calls 
     * <code>Action.getDataSource(HttpServletRequest)</code>.
     * @param dataSource <code>DataSource</code>
     */
    public void setDataSource(DataSource dataSource)
    {
        setDataSource(Globals.DATA_SOURCE_KEY, dataSource);
    }
    
    /**
     * Sets the specified <code>DataSource</code>.
     * You can use this method, if your action calls 
     * <code>Action.getDataSource(HttpServletRequest, String)</code>.
     * @param key the key of the <code>DataSource</code>
     * @param dataSource <code>DataSource</code>
     */
    public void setDataSource(String key, DataSource dataSource)
    {
        key = key + mockFactory.getMockModuleConfig().getPrefix();
        mockFactory.getMockServletContext().setAttribute(key, dataSource);
    }
    
    /**
     * Sets the specified locale as a session attribute
     * using <code>Globals.LOCALE_KEY</code> as the key. You can
     * use this method, if your action calls 
     * <code>Action.getLocale(HttpServletRequest)</code>.
     * @param locale the locale
     */
    public void setLocale(Locale locale)
    {
        mockFactory.getMockSession().setAttribute(Globals.LOCALE_KEY, locale);
    }
    
    /**
     * Creates a valid <code>ValidatorResources</code> object based
     * on the specified config files. Since the parsing of the files
     * is time consuming, it makes sense to cache the result.
     * You can set the returned <code>ValidatorResources</code> object
     * with {@link #setValidatorResources}. It is then used in
     * all validations.
     * @param resourcesFiles the array of config files
     */
    public ValidatorResources createValidatorResources(String[] resourcesFiles)
    {  
        if(resourcesFiles.length == 0) return null;
        setUpServletContextResourcePath(resourcesFiles);
        String resourceString = resourcesFiles[0];
        for(int ii = 1; ii < resourcesFiles.length; ii++)
        {
            resourceString += "," + resourcesFiles[ii];
        }
        ValidatorPlugIn plugIn = new ValidatorPlugIn();
        plugIn.setPathnames(resourceString);
        try
        {
            plugIn.init(mockFactory.getMockActionServlet(), mockFactory.getMockModuleConfig());
        }
        catch(ServletException exc)
        {
            log.error("Error initializing ValidatorPlugIn", exc);
            throw new RuntimeException("Error initializing ValidatorPlugIn: " + exc.getMessage());
        }
        String key = ValidatorPlugIn.VALIDATOR_KEY + mockFactory.getMockModuleConfig().getPrefix();
        return (ValidatorResources)mockFactory.getMockServletContext().getAttribute(key);
    }
    
    private void setUpServletContextResourcePath(String[] resourcesFiles)
    {
        for(int ii = 0; ii < resourcesFiles.length; ii++)
        {
            String file = resourcesFiles[ii];
            try
            {
                FileInputStream stream = new FileInputStream(file);
                byte[] fileData = StreamUtil.getStreamAsByteArray(stream);
                mockFactory.getMockServletContext().setResourceAsStream(file, fileData);
            }
            catch(FileNotFoundException exc)
            {
                log.error("File not found", exc);
                throw new NestedApplicationException(exc);
            }
        }
    }
    
    /**
     * Sets the specified <code>ValidatorResources</code>. The easiest
     * way to create <code>ValidatorResources</code> is the method
     * {@link #createValidatorResources}.
     * @param validatorResources the <code>ValidatorResources</code>
     */
    public void setValidatorResources(ValidatorResources validatorResources)
    {
        String key = ValidatorPlugIn.VALIDATOR_KEY + mockFactory.getMockModuleConfig().getPrefix();
        mockFactory.getMockServletContext().setAttribute(key, validatorResources);
    }

    /**
     * Verifies the forward path returned by the action.
     * If your action uses <code>mapping.findForward("success")</code>
     * to find the forward, you can use this method or
     * {@link #verifyForwardName} to test the <code>success</code> forward
     * name. If your action creates an <code>ActionForward</code> on its
     * own you can use this method to verify the forward <code>path</code>.
     * @param path the expected path
     * @throws VerifyFailedException if verification fails
     */
    public void verifyForward(String path)
    {
        if(null == getActionForward())
        {
            throw new VerifyFailedException("ActionForward == null");
        }
        else if (!getActionForward().verifyPath(path))
        {
            throw new VerifyFailedException("expected " + path + ", received " + getActionForward().getPath());
        }
    }
    
    /**
     * Verifies the forward name returned by the action.
     * If your action uses <code>mapping.findForward("success")</code>
     * to find the forward, you can use this method or
     * {@link #verifyForward} to test the <code>success</code> forward
     * name. If your action creates an <code>ActionForward</code> on its
     * own you can use this method to verify the forward <code>name</code>.
     * @param name the expected name
     * @throws VerifyFailedException if verification fails
     */
    public void verifyForwardName(String name)
    {
        if(null == getActionForward())
        {
            throw new VerifyFailedException("ActionForward == null");
        }
        else if (!getActionForward().verifyName(name))
        {
            throw new VerifyFailedException("expected " + name + ", received " + getActionForward().getName());
        }
    }
    
    /**
     * Verifies the redirect attribute.
     * @param redirect the expected redirect attribute
     * @throws VerifyFailedException if verification fails
     */
    public void verifyRedirect(boolean redirect)
    {
        if(null == getActionForward())
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
        if(containsMessages(messages))
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("has the following messages/errors: ");
            Iterator iterator = messages.get();
            while(iterator.hasNext())
            {
                ActionMessage message = (ActionMessage)iterator.next();
                buffer.append(message.getKey() + ";");
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
        if(!containsMessages(getActionErrors()))
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
        if(!containsMessages(getActionMessages()))
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
        if(!containsMessages(messages)) throw new VerifyFailedException("no action messages/errors");
        Iterator iterator = messages.get();
        while (iterator.hasNext())
        {
            ActionMessage message = (ActionMessage) iterator.next();
            if(message.getKey().equals(messageKey))
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
        if(!containsMessages(messages)) return;
        Iterator iterator = messages.get();
        while(iterator.hasNext())
        {
            ActionMessage message = (ActionMessage) iterator.next();
            if(message.getKey().equals(messageKey))
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
        for(int ii = 0; ii < messageKeys.length; ii++)
        {
            ActionMessage message = (ActionMessage) iterator.next();
            if(!message.getKey().equals(messageKeys[ii]))
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
        ActionMessage error = getActionErrorByKey(errorKey);
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
    public ActionMessage getActionErrorByKey(String errorKey)
    {
        return getActionMessageByKey(errorKey, getActionErrors());
    }
    
    /**
     * Returns the action message with the specified key or null
     * if such a message does not exist.
     * @param messageKey the message key
     * @return the action message with the specified key
     */
    public ActionMessage getActionMessageByKey(String messageKey)
    {
        return (ActionMessage)getActionMessageByKey(messageKey, getActionMessages());
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
     * as the currently present messages to the request.
     * @param messages the ActionMessages object
     */
    public void setActionMessages(ActionMessages messages)
    {
        mockFactory.getWrappedRequest().setAttribute(messageAttributeKey, messages);
    }
    
    /**
     * Sets the specified <code>ActionMessages</code> object 
     * as the currently present messages to the session.
     * @param messages the ActionMessages object
     */
    public void setActionMessagesToSession(ActionMessages messages)
    {
        mockFactory.getMockSession().setAttribute(messageAttributeKey, messages);
    }

    /**
     * Get the currently present action messages. Can be called
     * after {@link #actionPerform} to get the messages the action
     * has set. If messages in the session are recognized 
     * (use {@link #setRecognizeMessagesInSession}), this method
     * returns the union of request and session messages. Otherwise,
     * it only returns the request messages.
     * @return the action messages
     */
    public ActionMessages getActionMessages()
    {
        ActionMessages requestMessages = getActionMessagesFromRequest();
        ActionMessages sessionMessages = getActionMessagesFromSession();
        if(recognizeInSession)
        {
            if(null == requestMessages || requestMessages.isEmpty()) return sessionMessages;
            if(null == sessionMessages || sessionMessages.isEmpty()) return requestMessages;
            requestMessages = new ActionMessages(requestMessages);
            requestMessages.add(sessionMessages);
        }
        return requestMessages;
    }
    
    /**
     * Get the currently present action messages from the request.
     * @return the action messages
     */
    public ActionMessages getActionMessagesFromRequest()
    {
        return (ActionMessages)mockFactory.getWrappedRequest().getAttribute(messageAttributeKey);
    }
    
    /**
     * Get the currently present action messages from the session.
     * @return the action messages
     */
    public ActionMessages getActionMessagesFromSession()
    {
        return (ActionMessages)mockFactory.getMockSession().getAttribute(messageAttributeKey);
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
     * as the currently present errors to the request.
     * @param errors the ActionErrors object
     */
    public void setActionErrors(ActionMessages errors)
    {
        mockFactory.getWrappedRequest().setAttribute(errorAttributeKey, errors);
    }
    
    /**
     * Sets the specified <code>ActionErrors</code> object 
     * as the currently present errors to the session.
     * @param errors the ActionErrors object
     */
    public void setActionErrorsToSession(ActionMessages errors)
    {
        mockFactory.getMockSession().setAttribute(errorAttributeKey, errors);
    }

    /**
     * Get the currently present action errors. Can be called
     * after {@link #actionPerform} to get the errors the action
     * has set. If messages in the session are recognized 
     * (use {@link #setRecognizeMessagesInSession}), this method
     * returns the union of request and session errors. Otherwise,
     * it only returns the request errors.
     * @return the action errors
     */
    public ActionMessages getActionErrors()
    {
        ActionMessages requestErrors = getActionErrorsFromRequest();
        ActionMessages sessionErrors = getActionErrorsFromSession();
        if(recognizeInSession)
        {
            if(null == requestErrors || requestErrors.isEmpty()) return sessionErrors;
            if(null == sessionErrors || sessionErrors.isEmpty()) return requestErrors;
            if((requestErrors instanceof ActionErrors) || (sessionErrors instanceof ActionErrors))
            {
                ActionErrors tempErrors = new ActionErrors();
                tempErrors.add(requestErrors);
                requestErrors = tempErrors;
            }
            else
            {
                requestErrors = new ActionMessages(requestErrors);
            }
            requestErrors.add(sessionErrors);
        }
        return requestErrors;
    }
    
    /**
     * Get the currently present action errors from the request.
     * @return the action messages
     */
    public ActionMessages getActionErrorsFromRequest()
    {
        return (ActionMessages)mockFactory.getWrappedRequest().getAttribute(errorAttributeKey);
    }
    
    /**
     * Get the currently present action errors from the session.
     * @return the action messages
     */
    public ActionMessages getActionErrorsFromSession()
    {
        return (ActionMessages)mockFactory.getMockSession().getAttribute(errorAttributeKey);
    }

    /**
     * Returns if action errors are present.
     * @return true if errors are present, false otherwise
     */
    public boolean hasActionErrors()
    {
        ActionMessages errors = getActionErrors();
        return containsMessages(errors);
    }

    /**
     * Delegates to {@link com.mockrunner.mock.web.ActionMockObjectFactory#getMockActionMapping}.
     * @return the MockActionMapping
     */
    public MockActionMapping getMockActionMapping()
    {
        return mockFactory.getMockActionMapping();
    }
    
    /**
     * Delegates to {@link com.mockrunner.mock.web.ActionMockObjectFactory#getActionMapping}.
     * @return the MockActionMapping
     */
    public ActionMapping getActionMapping()
    {
        return mockFactory.getActionMapping();
    }

    /**
     * Returns the <code>MockPageContext</code> object.
     * Delegates to {@link com.mockrunner.mock.web.ActionMockObjectFactory#getMockPageContext}.
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
     * Generates a token and sets it to the session and the request.
     */
    public void generateValidToken()
    {
        String token = String.valueOf(Math.random());
        mockFactory.getMockSession().setAttribute(Globals.TRANSACTION_TOKEN_KEY, token);
        addRequestParameter(Constants.TOKEN_KEY, token);
    }
    
    /**
     * Returns the current <code>ActionForm</code>.
     * @return the <code>ActionForm</code> object
     */
    public ActionForm getActionForm()
    {
        return formObj;
    }

    /**
     * Sets the specified <code>ActionForm</code> object as the
     * current <code>ActionForm</code>.
     * @param formObj the <code>ActionForm</code> object
     */
    public void setActionForm(ActionForm formObj)
    {
        this.formObj = formObj;
    }

    /**
     * Creates a new <code>ActionForm</code> object of the specified
     * type and sets it as the current <code>ActionForm</code>.
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
            formObj = (ActionForm)form.newInstance();
            return formObj;
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Creates a new <code>DynaActionForm</code> based on the specified
     * form config and sets it as the current <code>ActionForm</code>.
     * @param formConfig the <code>FormBeanConfig</code>
     */
    public DynaActionForm createDynaActionForm(FormBeanConfig formConfig)
    {
        try
        {
            if (null == formConfig)
            {
                formObj = null;
                return null;
            }
            DynaActionFormClass formClass = DynaActionFormClass.createDynaActionFormClass(formConfig);
            formObj = (DynaActionForm)formClass.newInstance();
            return (DynaActionForm)formObj;
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }

    /**
     * Populates the current request parameters to the
     * <code>ActionForm</code>. The form will be reset
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
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
            throw new NestedApplicationException(exc);
        }
    }

    /**
     * Calls the action of the specified type using
     * no <code>ActionForm</code>. Sets the current action
     * form to <code>null</code>.
     * @param action the <code>Class</code> of the action
     * @return the resulting <code>ActionForward</code>
     */
    public ActionForward actionPerform(Class action)
    {
        return actionPerform(action, (ActionForm) null);
    }
    
    /**
     * Calls the specified action using
     * no <code>ActionForm</code>. Sets the current <code>ActionForm</code>
     * to <code>null</code>.
     * @param action the <code>Action</code>
     * @return the resulting <code>ActionForward</code>
     */
    public ActionForward actionPerform(Action action)
    {
        return actionPerform(action, (ActionForm) null);
    }

    /**
     * Calls the action of the specified type using
     * the <code>ActionForm</code> of the specified type. 
     * Creates the appropriate <code>ActionForm</code>, sets it as the 
     * current <code>ActionForm</code> and populates it before calling the action 
     * (if populating is disabled, the form will not be populated, use 
     * {@link #setDoPopulate}). 
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
        createActionForm(form);
        return actionPerform(action, formObj);
    }
    
    /**
     * Calls the specified action using
     * the <code>ActionForm</code> of the specified type. 
     * Creates the appropriate <code>ActionForm</code>, sets it as the 
     * current <code>ActionForm</code> and populates it before calling the action 
     * (if populating is disabled, the form will not be populated, use 
     * {@link #setDoPopulate}).
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
        createActionForm(form);
        return actionPerform(action, formObj);
    }

    /**
     * Calls the action of the specified type using
     * the specified <code>ActionForm</code> object. The form will
     * be set as the current <code>ActionForm</code> and
     * will be populated before the action is called (if populating is
     * disabled, the form will not be populated, use {@link #setDoPopulate}).
     * Please note that request parameters will eventually overwrite
     * form values. Furthermore the form will be reset
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
        Action actionToCall = null;
        try
        {
            actionToCall = (Action)action.newInstance();
        }
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
        return actionPerform(actionToCall, form);
    }
    
    /**
     * Calls the specified action using
     * the specified <code>ActionForm</code> object. The form will
     * be set as the current <code>ActionForm</code> and
     * will be populated before the action is called (if populating is
     * disabled, the form will not be populated, use {@link #setDoPopulate}).
     * Please note that request parameters will eventually overwrite
     * form values. Furthermore the form will be reset
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
            actionObj.setServlet(mockFactory.getMockActionServlet());
            formObj = form;
            setActionErrors(null);
            getActionMapping().setType(action.getClass().getName());
            if(null != formObj)
            {
                handleActionForm();
            }
            if(!hasActionErrors())
            {
                ActionForward currentForward = null;
                try
                {
                    currentForward = (ActionForward)actionObj.execute(getActionMapping(), formObj, mockFactory.getWrappedRequest(), mockFactory.getWrappedResponse());
                } 
                catch(Exception exc)
                {
                    ExceptionHandlerConfig handler = findExceptionHandler(exc);
                    if(null == handler)
                    {
                        throw exc;
                    }
                    else
                    {
                        Object result = handler.handle(exc, getActionMapping(), formObj, mockFactory.getWrappedRequest(), mockFactory.getWrappedResponse());
                        if(result instanceof ActionForward)
                        {
                            currentForward = (ActionForward)result;
                        }
                    }
                }
                setResult(currentForward);
            }
            else
            {
                setResult(getActionMapping().getInputForward());
            }
        }
        catch(Exception exc)
        {
            throw new NestedApplicationException(exc);
        }
        return getActionForward();
    }
    
    /**
     * Returns the HTML output as a string (if the action creates HTML output). 
     * Flushes the output before returning it.
     * @return the output
     */
    public String getOutput()
    {
        try
        {
            mockFactory.getMockResponse().getWriter().flush();    
        }
        catch(Exception exc)
        {
            log.error(exc.getMessage(), exc);
        }
        return mockFactory.getMockResponse().getOutputStreamContent();
    }

    private void setResult(ActionForward currentForward)
    {
        if (null == currentForward)
        {
            forward = null;
        }
        else
        {
            forward = new MockActionForward(currentForward);
        }
    }
    
    private ExceptionHandlerConfig findExceptionHandler(Exception exc)
    {
        for(int ii = 0; ii < exceptionHandlers.size(); ii++)
        {
            ExceptionHandlerConfig next = (ExceptionHandlerConfig)exceptionHandlers.get(ii);
            if(next.canHandle(exc)) return next;
        }
        return null;
    }

    private void handleActionForm() throws Exception
    {
        if(reset) getActionForm().reset(getActionMapping(), mockFactory.getWrappedRequest());
        if(doPopulate) populateMockRequest();
        formObj.setServlet(mockFactory.getMockActionServlet());
        if(getActionMapping().getValidate())
        {
            ActionMessages errors = formObj.validate(getActionMapping(), mockFactory.getWrappedRequest());
            if (containsMessages(errors))
            {
                mockFactory.getWrappedRequest().setAttribute(errorAttributeKey, errors);
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
