package de.mockrunner.example;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
        if(loginOk) return mapping.findForward("success");
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

    private AuthenticationStrategy getAuthenticationStrategy(HttpServletRequest request)
    {
        ServletContext context = request.getSession().getServletContext();
        return (AuthenticationStrategy)context.getAttribute(AuthenticationStrategy.class.getName());
    }
}
