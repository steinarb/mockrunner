package com.mockrunner.example.struts;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
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
 * {@link AuthenticationActionTest} in order 
 * to demonstrate the usage of {@link com.mockrunner.struts.ActionTestModule}.
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
        ActionMessages errors = new ActionMessages();
        if(!getAuthenticationStrategy(request).wasLastUserKnown())
        {
            ActionMessage error = new ActionMessage("auth.username.unknown", username);
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
        }
        else if(!getAuthenticationStrategy(request).wasLastPasswordOk())
        {
            ActionMessage error = new ActionMessage("auth.password.wrong");
            errors.add(ActionErrors.GLOBAL_MESSAGE, error);
        }
        else
        {
            ActionMessage error = new ActionMessage("auth.general.error");
            errors.add(ActionErrors.GLOBAL_MESSAGE, error);
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
