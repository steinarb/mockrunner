package com.mockrunner.example.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * This example action takes a name from the request and
 * loads some messages from the current message resources.
 * It then creates a short greetings message and sets it to
 * the request. Demonstrates how to deal with messages resources.
 */
public class GreetingsAction extends Action
{
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
    { 
        String name = request.getParameter("name");
        Integer counter = (Integer)request.getSession().getServletContext().getAttribute("counter");
        counter = new Integer(counter.intValue() + 1);
        request.getSession().getServletContext().setAttribute("counter", counter);
        MessageResources resources = getResources(request);
        String helloMessage = resources.getMessage("hello.name", name);
        String visitorMessage = resources.getMessage("visitor.number", counter);
        request.setAttribute("greetings", helloMessage + " " + visitorMessage);
        return mapping.findForward("success");
    }
}
