package de.mockrunner.example;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * This example action demonstrates the access to a data repository
 * stored in the <code>ServletContext</code>. Since the repository
 * is not thread safe we have to synchronize the access. Used to
 * demonstrate how to do multithread testing. Check out
 * {@link de.mockrunner.example.test.StoreDataActionTest}.
 */
public class StoreDataAction extends Action
{
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
    {
        ServletContext context = request.getSession().getServletContext();
        synchronized(context)
        {
            MemoryBasedRepository repository = MemoryBasedRepository.instance(context);
            if(repository.get("id") != null) return mapping.findForward("failure");
            System.out.println("Thread " + Thread.currentThread().getName() + " wins the race");
            String id = request.getParameter("id");
            String data = request.getParameter("data");
            repository.set(id, data);
            return mapping.findForward("success");
        }
    }
}
