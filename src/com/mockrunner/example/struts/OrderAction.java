package com.mockrunner.example.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * The example from the webpage. This action fulfils the order for some type 
 * of product. If there's not enough in the stock, an error is returned.
 * This action will be tested with {@link OrderActionTest} in order 
 * to demonstrate the usage of {@link com.mockrunner.struts.ActionTestModule}.
 */
public class OrderAction extends Action
{
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
    { 
        OrderForm orderForm = (OrderForm)form;
        String id = orderForm.getId();
        int amount = orderForm.getAmount();   
        OrderManager orderManager = OrderManager.instance(request.getSession().getServletContext());
        if(orderManager.getStock(id) < amount)
        {
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("not.enough.in.stock", id);
            errors.add(ActionErrors.GLOBAL_ERROR, error);
            saveErrors(request, errors);
            return mapping.findForward("failure");
        }
        orderManager.order(id, amount);
        return mapping.findForward("success");
    } 
}
