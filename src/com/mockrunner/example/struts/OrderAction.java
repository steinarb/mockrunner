package com.mockrunner.example.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * This action fulfils the order for some type of product. 
 * If there's not enough in the stock, an error is returned.
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
            ActionMessages errors = new ActionMessages();
            ActionMessage error = new ActionMessage("not.enough.in.stock", id);
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            saveErrors(request, errors);
            return mapping.findForward("failure");
        }
        orderManager.order(id, amount);
        return mapping.findForward("success");
    } 
}
