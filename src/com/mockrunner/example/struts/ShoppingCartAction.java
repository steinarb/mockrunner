package com.mockrunner.example.struts;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;

/**
 * This simple <code>LookupDispatchAction</code> returns
 * different forwards, depending on the called handler method.
 * Demonstrates how to test a <code>LookupDispatchAction</code>.
 */
public class ShoppingCartAction extends LookupDispatchAction
{
    protected Map getKeyMethodMap()
    {
        Map map = new HashMap();
        map.put("button.add", "add");
        map.put("button.order", "order");
        return map;
    }

    public ActionForward add(ActionMapping mapping,
                             ActionForm form,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception
    {
        return mapping.findForward("add");
    }
          
    public ActionForward order(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception
    {
        return mapping.findForward("order");
    }
}
