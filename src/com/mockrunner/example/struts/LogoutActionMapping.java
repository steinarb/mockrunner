package com.mockrunner.example.struts;

import org.apache.struts.action.ActionMapping;

/**
 * This custom action mapping is used by {@link LogoutAction}
 * to get the parameter name for the logout request parameter.
 * The parameter name can be set in the struts-config.xml file using
 * &lt;set-property property="requestParameterName" value="logout"/&gt;
 */
public class LogoutActionMapping extends ActionMapping
{
    private String requestParameterName;

    public String getRequestParameterName()
    {
        return requestParameterName;
    }

    public void setRequestParameterName(String requestParameterName)
    {
        this.requestParameterName = requestParameterName;
    }
}
