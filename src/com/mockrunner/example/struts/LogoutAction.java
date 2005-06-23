package com.mockrunner.example.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * This example action does the same as 
 * {@link com.mockrunner.example.servlet.LogoutServlet}.
 * It is used to demonstrate the testing of actions with
 * specified filters since {@link com.mockrunner.struts.ActionTestModule}
 * does not provide any filter methods. Please note that usually it's not
 * necessary to test actions in conjunction with filters. You can
 * add the filtered values directly to the request.
 * This action uses the custom action mapping {@link LogoutActionMapping}
 * to get the request parameter name.
 */
public class LogoutAction extends Action
{
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
    {
        LogoutActionMapping logoutMapping = (LogoutActionMapping)mapping;
        String logout = request.getParameter(logoutMapping.getRequestParameterName());
        if(null != logout)
        {
            request.getSession().invalidate();
            request.getRequestDispatcher("/html/goodbye.html").forward(request, response);
        }
        return mapping.findForward("success");
    }
}
