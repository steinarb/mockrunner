package com.mockrunner.example.struts;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * The <code>ActionForm</code> for the {@link AuthenticationAction}.
 * The {@link #validate} method will check if an username and a password
 * is present and generates the approriate <code>ActionErrors</code>.
 * See {@link AuthenticationActionTest}.
 */
public class AuthenticationForm extends ActionForm
{
    private String username;
    private String password;

    public String getPassword()
    {
        return password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        if (null == username || 0 == username.length())
        {
            addMissingValueError(errors, "username");
        }
        if (null == password || 0 == password.length())
        {
            addMissingValueError(errors, "password");
        }
        return errors;
    }

    private void addMissingValueError(ActionErrors errors, String field)
    {
        ActionMessage error = new ActionMessage("field.value.missing", field);
        errors.add(ActionMessages.GLOBAL_MESSAGE, error);
    }
}
