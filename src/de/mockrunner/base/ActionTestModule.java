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
import org.apache.struts.taglib.html.Constants;

import de.mockrunner.mock.MockActionForward;
import de.mockrunner.mock.MockActionMapping;
import de.mockrunner.mock.MockPageContext;

public class ActionTestModule
{
    private MockObjectFactory mockFactory;
    private boolean validate;
    private String parameter;
    private MockActionForward forward;
    private ActionForm formObj;
    private Action actionObj;

    public ActionTestModule(MockObjectFactory mockFactory)
    {
        this.mockFactory = mockFactory;
    }

    public String addMappedPropertyRequestPrefix(String str)
    {
        return "value(" + str + ")";
    }

    public void setParameter(String parameter)
    {
        this.parameter = parameter;
        getMockActionMapping().setParameter(parameter);
    }

    public void setValidate(boolean validate)
    {
        this.validate = validate;
    }

    public void verifyForward(String path)
    {
        if (null == getActionForward())
        {
            throw new VerifyFailedException("ActionForward == null");
        }
        else if (!getActionForward().verifyPath(path))
        {
            throw new VerifyFailedException("expected " + path + ", received " + getActionForward().getPath());
        }
    }

    public void verifyNoActionErrors()
    {
        ActionErrors errors = getActionErrors();
        if (null != errors && !errors.isEmpty())
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("has action errors: ");
            Iterator iterator = errors.get();
            while (iterator.hasNext())
            {
                ActionError error = (ActionError) iterator.next();
                buffer.append(error.getKey() + ";");
            }
            throw new VerifyFailedException(buffer.toString());
        }
    }

    public void verifyHasActionErrors()
    {
        ActionErrors errors = getActionErrors();
        if (null == errors || errors.isEmpty())
        {
            throw new VerifyFailedException("no action errors");
        }
    }

    public void verifyActionErrorPresent(String errorName)
    {
        if (!hasActionErrors())
        {
            throw new VerifyFailedException("no action errors");
        }
        ActionErrors errors = (ActionErrors) getActionErrors();
        Iterator iterator = errors.get();
        while (iterator.hasNext())
        {
            ActionError error = (ActionError) iterator.next();
            if (error.getKey().equals(errorName))
            {
                return;
            }
        }
        throw new VerifyFailedException(errorName + " not in array errorNames");
    }

    public void verifyActionErrorNotPresent(String errorName)
    {
        ActionErrors errors = (ActionErrors) getActionErrors();
        Iterator iterator = errors.get();
        while (iterator.hasNext())
        {
            ActionError error = (ActionError) iterator.next();
            if (error.getKey().equals(errorName))
            {
                throw new VerifyFailedException(errorName + " in array errorNames");
            }
        }
    }

    public void verifyActionErrors(String errorNames[])
    {
        if (!hasActionErrors()) throw new VerifyFailedException("no action errors");
        ActionErrors errors = (ActionErrors) getActionErrors();
        if(errors.size() != errorNames.length) throw new VerifyFailedException("expected " + errorNames.length + " errors, received " + errors.size() + " errors");
        Iterator iterator = errors.get();
        for (int ii = 0; ii < errorNames.length; ii++)
        {
            ActionError error = (ActionError) iterator.next();
            if (!error.getKey().equals(errorNames[ii]))
            {
                throw new VerifyFailedException("mismatch at position " + ii + ", actual: " + error.getKey() + ", expected: " + errorNames[ii]);
            }
        }
    }
    
    public void verifyActionErrorValues(String errorKey, Object[] values)
    {
        ActionError error = getActionErrorByKey(errorKey);
        if(null == error) throw new VerifyFailedException("action error " + errorKey + " not present");
        Object[] actualValues = error.getValues();
        if(null == actualValues) throw new VerifyFailedException("action error " + errorKey + " has no values");
        if(values.length != actualValues.length) throw new VerifyFailedException("action error " + errorKey + " has " + actualValues + " values");
        for(int ii = 0; ii < actualValues.length; ii++)
        {
            if(!values[ii].equals(actualValues[ii]))
            {
                throw new VerifyFailedException("action error " + errorKey + ": expected value[" + ii + "]: " + values[ii] + " received value[" + ii + "]: " + actualValues[ii]);
            }
        }
    }
    
    public void verifyActionErrorValue(String errorKey, Object value)
    {
        ActionError error = getActionErrorByKey(errorKey);
        if(null == error) throw new VerifyFailedException("action error " + errorKey + " not present");
        Object[] values = error.getValues();
        if(null == values) throw new VerifyFailedException("action error " + errorKey + " has no values");
        if(1 != values.length) throw new VerifyFailedException("action error " + errorKey + " has " + values.length + " values");
        if(value.equals(values[0])) return;
        throw new VerifyFailedException("action error " + errorKey + ": expected value: " + value + " received value: " + values[0]);
    }

    public ActionError getActionErrorByKey(String errorKey)
    {
        ActionErrors errors = (ActionErrors) getActionErrors();
        if (null == errors) return null;
        Iterator iterator = errors.get();
        while (iterator.hasNext())
        {
            ActionError error = (ActionError) iterator.next();
            if (error.getKey().equals(errorKey))
            {
                return error;
            }
        }
        return null;
    }

    public void verifyNumberActionErrors(int number)
    {
        ActionErrors errors = getActionErrors();
        if (null != errors)
        {
            if (errors.size() == number) return;
            throw new VerifyFailedException("expected " + number + " errors, received " + errors.size() + " errors");
        }
        if (number == 0) return;
        throw new VerifyFailedException("no action errors");
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
        return containsErrors(errors);
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
            forward = new MockActionForward(currentForward.getPath());
        }
    }

    private void handleActionForm() throws Exception
    {
        populateMockRequest();
        if (validate)
        {
            ActionErrors errors = formObj.validate(getMockActionMapping(), mockFactory.getMockRequest());
            if (containsErrors(errors))
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

    private boolean containsErrors(ActionErrors errors)
    {
        return (null != errors) && (errors.size() > 0);
    }
}
