package com.mockrunner.example.struts;

import javax.servlet.ServletContext;

/**
 * The <code>OrderManager</code> used in {@link OrderAction}.
 * This example implementation does nothing.
 */
public class OrderManager
{
    protected OrderManager()
    {
    
    }
    
    public static synchronized OrderManager instance(ServletContext context)
    {
        OrderManager manager = (OrderManager)context.getAttribute(OrderManager.class.getName());
        if(null == manager)
        {
            manager = new OrderManager();
            context.setAttribute(OrderManager.class.getName(), manager);
        }
        return manager;
    }
    
    public int getStock(String id)
    {
        //get stock
        return 0;
    }
    
    public void order(String id, int amount)
    {
        //do order
    }
}
