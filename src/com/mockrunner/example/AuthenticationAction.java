package com.mockrunner.example;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * This example action simulates a simple authentication system
 * and generates the approriate <code>ActionErrors</code> and
 * <code>ActionMessages</code>. This action will be tested with
 * {@link de.mockrunner.example.test.AuthenticationActionTest} in order 
 * to demonstrate the usage of {@link de.mockrunner.base.ActionTestModule}.
 */
public class AuthenticationAction extends Action
{
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
    {
        AuthenticationForm authForm = (AuthenticationForm) form;
        String username = authForm.getUsername();
        String password = authForm.getPassword();
        boolean loginOk = getAuthenticationStrategy(request).authenticate(username, password);
        if(loginOk) 
        {
            addOkMessage(request);
            return mapping.findForward("success");
        }   
        addLoginErrors(request, username);
        return mapping.findForward("failure");
    }

    private void addLoginErrors(HttpServletRequest request, String username)
    {
        ActionErrors errors = new ActionErrors();
        if(!getAuthenticationStrategy(request).wasLastUserKnown())
        {
            ActionError error = new ActionError("auth.username.unknown", username);
            errors.add(ActionErrors.GLOBAL_ERROR, error);
        }
        else if(!getAuthenticationStrategy(request).wasLastPasswordOk())
        {
            ActionError error = new ActionError("auth.password.wrong");
            errors.add(ActionErrors.GLOBAL_ERROR, error);
        }
        else
        {
            ActionError error = new ActionError("auth.general.error");
            errors.add(ActionErrors.GLOBAL_ERROR, error);
        }
        saveErrors(request, errors);
    }
    
    private void addOkMessage(HttpServletRequest request)
    {
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("auth.login.successful"));
        saveMessages(request, messages);
    }

    private AuthenticationStrategy getAuthenticationStrategy(HttpServletRequest request)
    {
        ServletContext context = request.getSession().getServletContext();
        return (AuthenticationStrategy)context.getAttribute(AuthenticationStrategy.class.getName());
    }
}
